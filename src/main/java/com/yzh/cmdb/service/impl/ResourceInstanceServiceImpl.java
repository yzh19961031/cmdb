package com.yzh.cmdb.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.DynamicInstanceDTO;
import com.yzh.cmdb.domain.dto.InstanceDeleteDTO;
import com.yzh.cmdb.domain.dto.ResourceInstanceQueryDTO;
import com.yzh.cmdb.domain.entity.ResourceInstanceRelationEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.vo.DynamicInstanceVO;
import com.yzh.cmdb.mapper.ResourceInstanceRelationMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.service.ResourceInstanceService;
import com.yzh.cmdb.utils.SQLGenerator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private ResourceInstanceRelationMapper resourceInstanceRelationMapper;


    @Override
    public void add(DynamicInstanceDTO dynamicInstanceDTO) {
        this.checkInstanceDTO(dynamicInstanceDTO);
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(dynamicInstanceDTO.getModelId());
        Map<String, Object> data = dynamicInstanceDTO.getData();
        data.put("id", IdUtil.fastSimpleUUID());
        String insertSQL = SQLGenerator.generateInsertSQL(resourceModelEntity.getTableName(), data.keySet());
        jdbcTemplate.update(insertSQL, data.values().toArray());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(InstanceDeleteDTO deleteDTO) {
        String instanceId = deleteDTO.getInstanceId();
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
        return null;
    }

    @Override
    public void update(DynamicInstanceDTO dynamicInstanceDTO) {

    }

    @Override
    public DynamicInstanceVO detail(Long modelId, String instanceId) {
        return null;
    }



    private void checkInstanceDTO(DynamicInstanceDTO dynamicInstanceDTO) {

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
