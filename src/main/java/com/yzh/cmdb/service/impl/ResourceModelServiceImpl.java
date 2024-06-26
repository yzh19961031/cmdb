package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import com.yzh.cmdb.domain.dto.ResourceAttributeDTO;
import com.yzh.cmdb.domain.dto.ResourceModelDTO;
import com.yzh.cmdb.domain.entity.ResourceAttributeEntity;
import com.yzh.cmdb.domain.entity.ResourceGroupEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.entity.ResourceRelationEntity;
import com.yzh.cmdb.domain.entity.ResourceValidationEntity;
import com.yzh.cmdb.domain.vo.GroupResourceModelVO;
import com.yzh.cmdb.domain.vo.ResourceModelVO;
import com.yzh.cmdb.enums.ResourceAttributeEnum;
import com.yzh.cmdb.event.CreateModelEvent;
import com.yzh.cmdb.event.DeleteModelEvent;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.ResourceAttributeMapper;
import com.yzh.cmdb.mapper.ResourceGroupMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.mapper.ResourceRelationMapper;
import com.yzh.cmdb.mapper.ResourceValidationMapper;
import com.yzh.cmdb.service.ResourceModelService;
import com.yzh.cmdb.utils.SQLGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源模型service
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Service
public class ResourceModelServiceImpl implements ResourceModelService {

    /**
     * 默认表字段
     */
    private static final List<ColumnDefinitionDTO> DEFAULT_COLUMN_LIST = Arrays.asList(
            new ColumnDefinitionDTO().setColumnName("id").setColumnType("BIGINT AUTO_INCREMENT PRIMARY KEY"),
            new ColumnDefinitionDTO().setColumnName("instance_name").setColumnType("VARCHAR(255)"));

    @Resource
    private ResourceModelMapper resourceModelMapper;

    @Resource
    private ResourceAttributeMapper resourceAttributeMapper;

    @Resource
    private ResourceGroupMapper resourceGroupMapper;

    @Resource
    private ResourceValidationMapper resourceValidationMapper;

    @Resource
    private ResourceRelationMapper resourceRelationMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private JdbcTemplate jdbcTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ResourceModelDTO resourceModelDTO) {
        this.checkResourceModel(resourceModelDTO);
        // 添加模型表
        ResourceModelEntity resourceModelEntity = new ResourceModelEntity();
        BeanUtils.copyProperties(resourceModelDTO, resourceModelEntity);
        // 设置表名
        String tableName = "cmdb_" + resourceModelDTO.getUniqueKey();
        resourceModelEntity.setTableName(tableName);
        resourceModelMapper.insert(resourceModelEntity);
        Long modelId = resourceModelEntity.getId();
        // 设置一个默认的ID主键和 固定属性实例名称instance_name
        ResourceAttributeEntity resourceAttributeEntity = new ResourceAttributeEntity()
                .setModelId(modelId)
                .setName("实例名称")
                .setIdentifier("instance_name")
                .setType(ResourceAttributeEnum.INPUT.getValue())
                .setIsRequired(true);
        resourceAttributeMapper.insert(resourceAttributeEntity);

        // 添加唯一校验
        ResourceValidationEntity resourceValidationEntity = new ResourceValidationEntity()
                .setValidateColumns("instance_name")
                .setModelId(modelId);
        resourceValidationMapper.insert(resourceValidationEntity);

        // 发布创建模型事件
        applicationEventPublisher.publishEvent(new CreateModelEvent(this, tableName, DEFAULT_COLUMN_LIST));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 删除模型
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(id);
        // 判断是否停用
        if (resourceModelEntity.getIsEnabled()) {
            throw new CmdbException("该模型已启用，请先停用后再删除！！！");
        }

        // 删除所有的模型关联
        resourceRelationMapper.delete(
                new LambdaQueryWrapper<ResourceRelationEntity>()
                        .eq(ResourceRelationEntity::getSourceId, id)
                        .or()
                        .eq(ResourceRelationEntity::getTargetId, id));

        resourceModelMapper.deleteById(id);
        // 删除资源属性
        resourceAttributeMapper.delete(new LambdaQueryWrapper<ResourceAttributeEntity>().eq(ResourceAttributeEntity::getModelId, id));
        // 删除校验信息
        resourceValidationMapper.delete(new LambdaQueryWrapper<ResourceValidationEntity>().eq(ResourceValidationEntity::getModelId, id));
        // 发布删除模型事件
        applicationEventPublisher.publishEvent(new DeleteModelEvent(this, resourceModelEntity.getTableName()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        // 先用循环删除
        if (CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("请选择需要删除的模型！！！");
        }
        ids.forEach(this::delete);
    }

    @Override
    public List<GroupResourceModelVO> list(String name) {
        List<ResourceGroupEntity> resourceGroupEntities = resourceGroupMapper.selectList(null);
        if (CollectionUtils.isEmpty(resourceGroupEntities)) {
            return Collections.emptyList();
        }

        List<GroupResourceModelVO> groupResourceModelVOList = new ArrayList<>();
        resourceGroupEntities.forEach(resourceGroupEntity -> {
            Long groupId = resourceGroupEntity.getId();
            List<ResourceModelEntity> resourceModelEntities = resourceModelMapper.selectList(new LambdaQueryWrapper<ResourceModelEntity>()
                    .like(StringUtils.isNotEmpty(name), ResourceModelEntity::getName, name)
                    .eq(ResourceModelEntity::getGroupId, groupId));
            GroupResourceModelVO groupResourceModelVO = new GroupResourceModelVO();
            groupResourceModelVO.setId(groupId);
            groupResourceModelVO.setName(resourceGroupEntity.getName());
            groupResourceModelVO.setDescription(resourceGroupEntity.getDescription());
            groupResourceModelVO.setResourceModelList(resourceModelEntities.stream()
                    .map(resourceModelEntity -> new ResourceModelVO()
                            .setId(resourceModelEntity.getId())
                            .setName(resourceModelEntity.getName())
                            .setDescription(resourceModelEntity.getDescription())
                            .setIsEnabled(resourceModelEntity.getIsEnabled())
                            .setUniqueKey(resourceModelEntity.getUniqueKey()))
                    .collect(Collectors.toList()));
            groupResourceModelVOList.add(groupResourceModelVO);
        });
        return groupResourceModelVOList;
    }

    @Override
    public ResourceModelDTO detail(Long id) {
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(id);
        ResourceModelDTO resourceModelDTO = new ResourceModelDTO();
        BeanUtils.copyProperties(resourceModelEntity, resourceModelDTO);
        resourceModelDTO.setGroupName(resourceGroupMapper.selectById(resourceModelEntity.getGroupId()).getName());
        List<ResourceAttributeDTO> resourceAttributeDTOList = resourceAttributeMapper
                .selectList(new LambdaQueryWrapper<ResourceAttributeEntity>().eq(ResourceAttributeEntity::getModelId, id))
                .stream().map(resourceAttributeEntity -> {
                    ResourceAttributeDTO attributeDTO = new ResourceAttributeDTO();
                    BeanUtils.copyProperties(resourceAttributeEntity, attributeDTO);
                    return attributeDTO;
                }).collect(Collectors.toList());
        resourceModelDTO.setResourceAttributeDTOList(resourceAttributeDTOList);
        return resourceModelDTO;
    }

    @Override
    public List<ResourceModelVO> listAll(Boolean isEnabled) {
        LambdaQueryWrapper<ResourceModelEntity> queryWrapper = new LambdaQueryWrapper<ResourceModelEntity>()
                .eq(Objects.nonNull(isEnabled), ResourceModelEntity::getIsEnabled, isEnabled);
        List<ResourceModelEntity> resourceModelEntities = resourceModelMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(resourceModelEntities)) {
            return Collections.emptyList();
        }
        return resourceModelEntities.stream().map(resourceModelEntity -> {
            ResourceModelVO resourceModelVO = new ResourceModelVO();
            BeanUtils.copyProperties(resourceModelEntity, resourceModelVO);
            return resourceModelVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void update(ResourceModelDTO resourceModelDTO) {
        Long id = resourceModelDTO.getId();
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(id);
        String originalName = resourceModelEntity.getName();
        String name = resourceModelDTO.getName();

        if (!StringUtils.equals(originalName, name)) {
            boolean existsName = resourceModelMapper.exists(new LambdaQueryWrapper<ResourceModelEntity>()
                    .eq(ResourceModelEntity::getName, name));
            if (existsName) {
                throw new IllegalArgumentException("模型名称重复！！！");
            }
        }

        Boolean originalEnabled = resourceModelEntity.getIsEnabled();
        if (!Objects.equals(originalEnabled, resourceModelDTO.getIsEnabled()) && !resourceModelDTO.getIsEnabled()) {
            // 如果是停用操作 判断是否存在示例数据
            if (instanceExist(resourceModelEntity.getTableName())) {
                throw new IllegalArgumentException("该模型下存在实例数据，无法停用，请先清空实例数据！！！");
            }
        }
        ResourceModelEntity entity = new ResourceModelEntity();
        BeanUtils.copyProperties(resourceModelDTO, entity);
        resourceModelMapper.updateById(entity);
    }

    private void checkResourceModel(ResourceModelDTO resourceModelDTO) {
        ResourceModelDTO dto = Objects.requireNonNull(resourceModelDTO, "资源模型参数不能为空！！！");
        String name = dto.getName();
        boolean existsName = resourceModelMapper.exists(new LambdaQueryWrapper<ResourceModelEntity>()
                .eq(ResourceModelEntity::getName, name));
        if (existsName) {
            throw new IllegalArgumentException("模型名称重复！！！");
        }

        String uniqueKey = dto.getUniqueKey();
        boolean existsUniqueKey = resourceModelMapper.exists(new LambdaQueryWrapper<ResourceModelEntity>()
                .eq(ResourceModelEntity::getUniqueKey, uniqueKey));
        if (existsUniqueKey) {
            throw new IllegalArgumentException("唯一标识重复");
        }
    }

    /**
     * 判断当前模型下是否存在实例
     *
     * @param tableName 表名
     * @return T OR F
     */
    private boolean instanceExist(String tableName) {
        String sql = SQLGenerator.generateCountSQL(tableName);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return Objects.nonNull(count) && count > 0;
    }
}
