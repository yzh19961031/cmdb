package com.yzh.cmdb.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.PageResult;
import com.yzh.cmdb.domain.dto.DynamicInstanceDTO;
import com.yzh.cmdb.domain.dto.InstanceDeleteDTO;
import com.yzh.cmdb.domain.dto.ResourceInstanceQueryDTO;
import com.yzh.cmdb.domain.entity.ResourceAttributeEntity;
import com.yzh.cmdb.domain.entity.ResourceInstanceRelationEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.entity.ResourceValidationEntity;
import com.yzh.cmdb.domain.vo.DynamicInstanceVO;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.ResourceAttributeMapper;
import com.yzh.cmdb.mapper.ResourceInstanceRelationMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.mapper.ResourceValidationMapper;
import com.yzh.cmdb.service.ResourceInstanceService;
import com.yzh.cmdb.utils.SQLGenerator;
import com.yzh.cmdb.validator.ValidatorChain;
import com.yzh.cmdb.validator.impl.UniqueValidator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 模型实例service
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Service
@Slf4j
public class ResourceInstanceServiceImpl implements ResourceInstanceService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private ResourceModelMapper resourceModelMapper;

    @Resource
    private ResourceAttributeMapper resourceAttributeMapper;

    @Resource
    private ResourceInstanceRelationMapper resourceInstanceRelationMapper;

    @Resource
    private ResourceValidationMapper resourceValidationMapper;


    @Override
    public void add(DynamicInstanceDTO dynamicInstanceDTO) {
        this.checkInstanceDTO(dynamicInstanceDTO);
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(dynamicInstanceDTO.getModelId());
        Map<String, Object> data = dynamicInstanceDTO.getData();
        String insertSQL = SQLGenerator.generateInsertSQL(resourceModelEntity.getTableName(), data.keySet());
        jdbcTemplate.update(insertSQL, data.values().toArray());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(InstanceDeleteDTO deleteDTO) {
        Long instanceId = deleteDTO.getInstanceId();
        Long modelId = deleteDTO.getModelId();
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        String tableName = resourceModelEntity.getTableName();
        String deleteSQL = SQLGenerator.generateDeleteSQL(tableName, Collections.singletonList("id"));
        jdbcTemplate.update(deleteSQL, instanceId);
        // 删除实例关联
        resourceInstanceRelationMapper.delete(new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                .eq(ResourceInstanceRelationEntity::getSourceInstanceId, instanceId)
                .or()
                .eq(ResourceInstanceRelationEntity::getTargetInstanceId, instanceId));
    }

    @Override
    public PageResult<DynamicInstanceVO> list(ResourceInstanceQueryDTO resourceInstanceQueryDTO) {
        Long modelId = resourceInstanceQueryDTO.getModelId();
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        String tableName = resourceModelEntity.getTableName();
        ParameterizedSql querySql = buildParameterizedSql(tableName, resourceInstanceQueryDTO, false);
        List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(querySql.getSql(), querySql.getParams());
        if (CollectionUtils.isNotEmpty(queryResult)) {
            Map<String, String> systemNameMap = new HashMap<>();
            List<DynamicInstanceVO> dynamicInstanceVOS = new ArrayList<>();
            // 参数组装
            queryResult.forEach(result -> {
                DynamicInstanceVO dynamicInstanceVO = new DynamicInstanceVO();
                dynamicInstanceVO.setInstanceId((long) result.get("id"));
                List<DynamicInstanceVO.Attributes> attributes = result.entrySet()
                        .stream()
                        .filter(entry -> !StringUtils.equals("id", entry.getKey()))
                        .map(entry -> {
                            Object value = entry.getValue();
                            String key = entry.getKey();
                            String name = systemNameMap.computeIfAbsent(key, identifier ->
                                    resourceAttributeMapper.selectOne(new LambdaQueryWrapper<ResourceAttributeEntity>().eq(ResourceAttributeEntity::getIdentifier, identifier)).getName());
                            return DynamicInstanceVO.Attributes.builder().value(value).identifier(key).name(name).build();
                        })
                        .collect(Collectors.toList());
                dynamicInstanceVO.setResourceDetails(attributes);
                dynamicInstanceVOS.add(dynamicInstanceVO);
            });
            ParameterizedSql countSql = buildParameterizedSql(tableName, resourceInstanceQueryDTO, true);
            long count = Optional
                    .ofNullable(jdbcTemplate.queryForObject(countSql.getSql(), Integer.class, countSql.getParams()))
                    .orElse(0);
            return new PageResult<>(dynamicInstanceVOS, count);
        }
        return new PageResult<>(Collections.emptyList(), 0L);
    }

    @Override
    public void update(DynamicInstanceDTO dynamicInstanceDTO) {
        //

    }

    @Override
    public DynamicInstanceVO detail(Long modelId, Long instanceId) {
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        String tableName = resourceModelEntity.getTableName();
        String selectSQL = SQLGenerator.generateSelectSQL(tableName, Collections.singletonList("*"), Collections.singletonList("id"));

        Map<String, Object> objectMap = jdbcTemplate.queryForMap(selectSQL, instanceId);
        DynamicInstanceVO dynamicInstanceVO = new DynamicInstanceVO();
        Map<String, ResourceAttributeEntity> attributeEntityMap = resourceAttributeMapper.selectList(new LambdaQueryWrapper<ResourceAttributeEntity>()
                        .eq(ResourceAttributeEntity::getModelId, modelId))
                .stream()
                .collect(Collectors.toMap(ResourceAttributeEntity::getIdentifier, Function.identity()));

        List<DynamicInstanceVO.Attributes> resourceDetails = new ArrayList<>();
        objectMap.forEach((key, value) -> {
            if (attributeEntityMap.containsKey(key)) {
                DynamicInstanceVO.Attributes attributes = new DynamicInstanceVO.Attributes();
                attributes.setIdentifier(key);
                attributes.setValue(value);
                attributes.setName(attributeEntityMap.get(key).getName());
                resourceDetails.add(attributes);
            }
        });

        dynamicInstanceVO.setResourceDetails(resourceDetails);
        return dynamicInstanceVO;
    }

    
    private void checkInstanceDTO(DynamicInstanceDTO dynamicInstanceDTO) {
        // 判断参数类型是否正确
        Objects.requireNonNull(dynamicInstanceDTO, "实例参数不能为空！");

        // 校验唯一规则
        Long modelId = dynamicInstanceDTO.getModelId();
        List<ResourceValidationEntity> resourceValidationEntities = resourceValidationMapper.selectList(new LambdaQueryWrapper<ResourceValidationEntity>().eq(ResourceValidationEntity::getModelId, modelId));
        if (CollectionUtils.isNotEmpty(resourceValidationEntities)) {
            ValidatorChain<DynamicInstanceDTO> validatorChain = new ValidatorChain<>(resourceValidationEntities.stream().map(UniqueValidator::new).collect(Collectors.toSet()));
            if (!validatorChain.doValidate(dynamicInstanceDTO)) {
                throw new CmdbException("实例参数非法，唯一校验规则失败！！！");
            }
        }
    }


    /**
     * 生成查询参数化sql语句
     *
     * @param tableName  表名称
     * @param param      查询参数
     * @param countQuery 是否查询count
     * @return sql语句
     */
    private ParameterizedSql buildParameterizedSql(String tableName, ResourceInstanceQueryDTO param, boolean countQuery) {
        StringBuilder sql = new StringBuilder("SELECT");
        if (countQuery) {
            sql.append(" COUNT(1) FROM ");
        } else {
            sql.append(" * FROM ");
        }

        sql.append(tableName).append(" WHERE 1 = 1");
        List<Object> paramsList = new ArrayList<>();

        // 动态拼接条件
        String name = param.getName();
        if (StringUtils.isNotEmpty(name)) {
            sql.append(" AND instance_name like ?");
            paramsList.add("%" + name + "%");
        }

        if (!countQuery) {
            int pageIndex = param.getPageIndex();
            int pageSize = param.getPageSize();
            // 计算偏移量
            int offset = (pageIndex - 1) * pageSize;
            sql.append(" LIMIT ? OFFSET ?");
            paramsList.add(pageSize);
            paramsList.add(offset);
        }

        return ParameterizedSql.builder().sql(sql.toString()).params(paramsList.toArray(new Object[0])).build();
    }


    /**
     * 参数化sql
     */
    @Data
    @Builder
    private final static class ParameterizedSql {
        private String sql;
        private Object[] params;
    }
}
