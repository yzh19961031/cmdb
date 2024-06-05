package com.yzh.cmdb.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
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
            new ColumnDefinitionDTO().setColumnName("id").setColumnType("VARCHAR(64) PRIMARY KEY"),
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ResourceModelDTO resourceModelDTO) {
        this.checkResourceModel(resourceModelDTO);
        // 添加模型表
        ResourceModelEntity resourceModelEntity = new ResourceModelEntity();
        BeanUtils.copyProperties(resourceModelDTO, resourceModelEntity);
        // 设置表名
        String tableName = "cmdb_" + IdUtil.objectId();
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
        // 是否停用
        if (resourceModelEntity.getIsEnabled()) {
            throw new CmdbException("请先停用该模型！！！");
        }
        // 是否存在模型关联关系
        List<ResourceRelationEntity> resourceRelationEntities = resourceRelationMapper.selectList(
                new LambdaQueryWrapper<ResourceRelationEntity>()
                        .eq(ResourceRelationEntity::getSourceId, id)
                        .or()
                        .eq(ResourceRelationEntity::getTargetId, id));
        if (CollectionUtils.isNotEmpty(resourceRelationEntities)) {
            throw new CmdbException("该模型存在模型关联关系，无法删除！！！");
        }

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
    public List<GroupResourceModelVO> list(String name, Boolean isEnable) {
        List<ResourceGroupEntity> resourceGroupEntities = resourceGroupMapper.selectList(null);
        if (CollectionUtils.isEmpty(resourceGroupEntities)) {
            return Collections.emptyList();
        }

        List<GroupResourceModelVO> groupResourceModelVOList = new ArrayList<>();
        resourceGroupEntities.forEach(resourceGroupEntity -> {
            Long groupId = resourceGroupEntity.getId();
            List<ResourceModelEntity> resourceModelEntities = resourceModelMapper.selectList(new LambdaQueryWrapper<ResourceModelEntity>()
                    .like(StringUtils.isNotEmpty(name), ResourceModelEntity::getName, name)
                    .eq(Objects.nonNull(isEnable), ResourceModelEntity::getIsEnabled, isEnable)
                    .eq(ResourceModelEntity::getGroupId, groupId));
            GroupResourceModelVO groupResourceModelVO = new GroupResourceModelVO();
            groupResourceModelVO.setId(groupId);
            groupResourceModelVO.setName(resourceGroupEntity.getName());
            groupResourceModelVO.setDescription(resourceGroupEntity.getDescription());
            groupResourceModelVO.setResourceModelList(resourceModelEntities.stream()
                    .map(resourceModelEntity -> new ResourceModelVO()
                            .setIsEnabled(resourceModelEntity.getIsEnabled())
                            .setId(resourceModelEntity.getId())
                            .setName(resourceModelEntity.getName()))
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
    public void doSwitch(Long id, Boolean isEnabled) {
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(id);
        if (Objects.equals(isEnabled, resourceModelEntity.getIsEnabled())) {
            return;
        }
        resourceModelEntity.setIsEnabled(isEnabled);
        resourceModelMapper.updateById(resourceModelEntity);
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
}
