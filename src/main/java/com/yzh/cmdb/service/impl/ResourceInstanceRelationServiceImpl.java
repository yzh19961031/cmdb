package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.ResourceInstanceRelationDTO;
import com.yzh.cmdb.domain.entity.ResourceInstanceRelationEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.entity.ResourceRelationEntity;
import com.yzh.cmdb.domain.vo.ListResourceInstanceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceInstanceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceInstanceTopologyVO;
import com.yzh.cmdb.enums.RelationBindEnum;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.ResourceInstanceRelationMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.mapper.ResourceRelationMapper;
import com.yzh.cmdb.service.ResourceInstanceRelationService;
import com.yzh.cmdb.service.ResourceInstanceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 实例关联service
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceInstanceRelationServiceImpl implements ResourceInstanceRelationService {
    @Resource
    private ResourceInstanceRelationMapper resourceInstanceRelationMapper;

    @Resource
    private ResourceModelMapper resourceModelMapper;

    @Resource
    private ResourceRelationMapper resourceRelationMapper;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private ResourceInstanceService resourceInstanceService;


    @Override
    public void add(ResourceInstanceRelationDTO resourceInstanceRelationDTO) {
        ResourceInstanceRelationEntity resourceInstanceRelationEntity = this.checkResourceInstanceRelation(resourceInstanceRelationDTO);
        resourceInstanceRelationMapper.insert(resourceInstanceRelationEntity);
    }

    @Override
    public void delete(Long id) {
        resourceInstanceRelationMapper.deleteById(id);
    }

    @Override
    public ResourceInstanceTopologyVO recursiveTopology(Long modelId, Long instanceId) {
        // 创建根节点
        ResourceInstanceTopologyVO root = new ResourceInstanceTopologyVO();
        root.setInstanceId(instanceId);
        root.setModelId(modelId);
        ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
        root.setModelName(resourceModelEntity.getName());
        root.setInstanceName(getInstanceName(resourceModelEntity.getTableName(), instanceId));
        // 构建子节点
        buildChildren(root);
        // 构建父节点
        buildParents(root);
        return root;
    }

    @Override
    public ListResourceInstanceRelationVO list(Long modelId, Long instanceId) {
        List<ResourceInstanceRelationVO> children = new ArrayList<>();
        // 子节点
        List<ResourceRelationEntity> childResourceRelationEntities = resourceRelationMapper.selectList(new LambdaQueryWrapper<ResourceRelationEntity>()
                .eq(ResourceRelationEntity::getSourceId, modelId));
        // 过滤未开启
        if (CollectionUtils.isNotEmpty(childResourceRelationEntities)) {
            childResourceRelationEntities.stream()
                    .filter(child -> resourceModelMapper.selectById(child.getTargetId()).getIsEnabled())
                    .forEach(resourceRelationEntity -> {
                        ResourceInstanceRelationVO resourceInstanceRelationVO = new ResourceInstanceRelationVO();
                        Long targetId = resourceRelationEntity.getTargetId();
                        List<ResourceInstanceRelationEntity> childrenRelations = resourceInstanceRelationMapper.selectList(
                                new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                                        .eq(ResourceInstanceRelationEntity::getSourceModelId, modelId)
                                        .eq(ResourceInstanceRelationEntity::getTargetModelId, targetId)
                                        .eq(ResourceInstanceRelationEntity::getSourceInstanceId, instanceId));
                        resourceInstanceRelationVO.setModelName(resourceModelMapper.selectById(targetId).getName());
                        resourceInstanceRelationVO.setModelId(targetId);
                        if (CollectionUtils.isNotEmpty(childrenRelations)) {
                            List< ResourceInstanceRelationVO.ResourceInstanceRelationDetail> instanceVOList = childrenRelations.stream().map(resourceInstanceRelationEntity -> {
                                ResourceInstanceRelationVO.ResourceInstanceRelationDetail detail = new ResourceInstanceRelationVO.ResourceInstanceRelationDetail();
                                detail.setInstanceRelationId(resourceInstanceRelationEntity.getId());
                                detail.setDynamicInstance(resourceInstanceService.detail(targetId, resourceInstanceRelationEntity.getTargetInstanceId()));
                                return detail;
                            }).collect(Collectors.toList());
                            resourceInstanceRelationVO.setInstanceList(instanceVOList);
                        }
                        children.add(resourceInstanceRelationVO);
                    });
        }

        // 父节点
        List<ResourceInstanceRelationVO> parents = new ArrayList<>();
        List<ResourceRelationEntity> parentResourceRelationEntities = resourceRelationMapper.selectList(new LambdaQueryWrapper<ResourceRelationEntity>()
                .eq(ResourceRelationEntity::getTargetId, modelId));
        if (CollectionUtils.isNotEmpty(parentResourceRelationEntities)) {
            parentResourceRelationEntities.stream()
                    .filter(parent -> resourceModelMapper.selectById(parent.getSourceId()).getIsEnabled())
                    .forEach(resourceRelationEntity -> {
                        ResourceInstanceRelationVO resourceInstanceRelationVO = new ResourceInstanceRelationVO();
                        Long sourceId = resourceRelationEntity.getSourceId();
                        resourceInstanceRelationVO.setModelName(resourceModelMapper.selectById(sourceId).getName());
                        resourceInstanceRelationVO.setModelId(sourceId);
                        List<ResourceInstanceRelationEntity> parentRelations = resourceInstanceRelationMapper.selectList(
                                new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                                        .eq(ResourceInstanceRelationEntity::getSourceModelId, sourceId)
                                        .eq(ResourceInstanceRelationEntity::getTargetModelId, modelId)
                                        .eq(ResourceInstanceRelationEntity::getTargetInstanceId, instanceId));
                        if (CollectionUtils.isNotEmpty(parentRelations)) {
                            List< ResourceInstanceRelationVO.ResourceInstanceRelationDetail> instanceVOList = parentRelations.stream().map(resourceInstanceRelationEntity -> {
                                ResourceInstanceRelationVO.ResourceInstanceRelationDetail detail = new ResourceInstanceRelationVO.ResourceInstanceRelationDetail();
                                detail.setInstanceRelationId(resourceInstanceRelationEntity.getId());
                                detail.setDynamicInstance(resourceInstanceService.detail(sourceId, resourceInstanceRelationEntity.getSourceInstanceId()));
                                return detail;
                            }).collect(Collectors.toList());
                            resourceInstanceRelationVO.setInstanceList(instanceVOList);
                        }
                        parents.add(resourceInstanceRelationVO);
                    });
        }


        return ListResourceInstanceRelationVO.builder().parents(parents).children(children).build();
    }

    @Override
    public void batchAdd(List<ResourceInstanceRelationDTO> resourceInstanceRelationDTOList) {
        if (CollectionUtils.isEmpty(resourceInstanceRelationDTOList)) {
            throw new IllegalArgumentException("实例关联参数列表不能为空！！！");
        }
        resourceInstanceRelationDTOList.forEach(this::add);
    }

    private void buildParents(ResourceInstanceTopologyVO node) {
        List<ResourceInstanceRelationEntity> parentRelations = resourceInstanceRelationMapper.selectList(
                new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                        .eq(ResourceInstanceRelationEntity::getTargetModelId, node.getModelId())
                        .eq(ResourceInstanceRelationEntity::getTargetInstanceId, node.getInstanceId()));

        if (CollectionUtils.isNotEmpty(parentRelations)) {
            List<ResourceInstanceTopologyVO> parents = new ArrayList<>();
            for (ResourceInstanceRelationEntity relation : parentRelations) {
                ResourceInstanceTopologyVO parentNode = new ResourceInstanceTopologyVO();
                Long modelId = relation.getSourceModelId();
                Long instanceId = relation.getSourceInstanceId();
                ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
                if (!resourceModelEntity.getIsEnabled()) {
                    continue;
                }
                parentNode.setInstanceRelationId(relation.getId());
                parentNode.setInstanceId(instanceId);
                parentNode.setModelId(modelId);
                parentNode.setModelName(resourceModelEntity.getName());
                parentNode.setInstanceName(getInstanceName(resourceModelEntity.getTableName(), instanceId));
                // 递归构建父节点的父节点
                buildParents(parentNode);
                parents.add(parentNode);
            }
            node.setParents(parents);
        }
    }

    private void buildChildren(ResourceInstanceTopologyVO node) {
        List<ResourceInstanceRelationEntity> childrenRelations = resourceInstanceRelationMapper.selectList(
                new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                        .eq(ResourceInstanceRelationEntity::getSourceModelId, node.getModelId())
                        .eq(ResourceInstanceRelationEntity::getSourceInstanceId, node.getInstanceId()));

        if (CollectionUtils.isNotEmpty(childrenRelations)) {
            List<ResourceInstanceTopologyVO> children = new ArrayList<>();
            for (ResourceInstanceRelationEntity relation : childrenRelations) {
                Long modelId = relation.getTargetModelId();
                Long instanceId = relation.getTargetInstanceId();
                ResourceModelEntity resourceModelEntity = resourceModelMapper.selectById(modelId);
                if (!resourceModelEntity.getIsEnabled()) {
                    continue;
                }
                ResourceInstanceTopologyVO childNode = new ResourceInstanceTopologyVO();
                childNode.setInstanceRelationId(relation.getId());
                childNode.setInstanceId(instanceId);
                childNode.setModelId(modelId);
                childNode.setModelName(resourceModelEntity.getName());
                childNode.setInstanceName(getInstanceName(resourceModelEntity.getTableName(), instanceId));
                // 递归构建子节点的子节点
                buildChildren(childNode);
                children.add(childNode);
            }
            node.setChildren(children);
        }
    }


    /**
     * 获取实例名称
     *
     * @param tableName 表名称
     * @param instanceId 实例id
     * @return 实例名称
     */
    private String getInstanceName(String tableName, Long instanceId) {
        return jdbcTemplate.queryForObject("SELECT instance_name FROM " + tableName + " WHERE id = ?", String.class, instanceId);
    }

    private ResourceInstanceRelationEntity checkResourceInstanceRelation(ResourceInstanceRelationDTO resourceInstanceRelationDTO) {
        Objects.requireNonNull(resourceInstanceRelationDTO, "实例关联参数不能为空！！！");
        Long sourceInstanceId = resourceInstanceRelationDTO.getSourceInstanceId();
        Long sourceModelId = resourceInstanceRelationDTO.getSourceModelId();
        Long targetInstanceId = resourceInstanceRelationDTO.getTargetInstanceId();
        Long targetModelId = resourceInstanceRelationDTO.getTargetModelId();
        // 是否重复添加
        boolean relationExists = resourceInstanceRelationMapper.exists(new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                .eq(ResourceInstanceRelationEntity::getSourceInstanceId, sourceInstanceId)
                .eq(ResourceInstanceRelationEntity::getTargetInstanceId, targetInstanceId));
        if (relationExists) {
            throw new CmdbException("实例关联重复！！！");
        }

        ResourceRelationEntity resourceRelationEntity = resourceRelationMapper.selectOne(new LambdaQueryWrapper<ResourceRelationEntity>()
                .eq(ResourceRelationEntity::getSourceId, sourceModelId)
                .eq(ResourceRelationEntity::getTargetId, targetModelId));
        Integer relationBind = resourceRelationEntity.getRelationBind();
        RelationBindEnum relationBindEnum = RelationBindEnum.getByValue(relationBind);
        if (Objects.equals(relationBindEnum, RelationBindEnum.ONE2ONE)) {
            boolean exists1 = resourceInstanceRelationMapper.exists(new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                    .eq(ResourceInstanceRelationEntity::getTargetInstanceId, targetInstanceId)
                    .eq(ResourceInstanceRelationEntity::getSourceModelId, sourceModelId));

            boolean exists2 = resourceInstanceRelationMapper.exists(new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                    .eq(ResourceInstanceRelationEntity::getSourceInstanceId, sourceInstanceId)
                    .eq(ResourceInstanceRelationEntity::getTargetModelId, targetModelId));
            if (exists1 || exists2) {
                throw new CmdbException("违反关系约束 " + relationBindEnum.getDesc());
            }
        }

        // 如果是目标端 需要额外判断一对多关系
        if (Objects.equals(relationBindEnum, RelationBindEnum.ONE2MANY)) {
            boolean exists = resourceInstanceRelationMapper.exists(new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                    .eq(ResourceInstanceRelationEntity::getTargetInstanceId, targetInstanceId)
                    .eq(ResourceInstanceRelationEntity::getSourceModelId, sourceModelId));
            if (exists) {
                throw new CmdbException("违反关系约束 " + relationBindEnum.getDesc());
            }

        }
        ResourceInstanceRelationEntity resourceInstanceRelationEntity = new ResourceInstanceRelationEntity();
        BeanUtils.copyProperties(resourceInstanceRelationDTO, resourceInstanceRelationEntity);
        return resourceInstanceRelationEntity.setRelationBind(relationBind).setRelationTypeId(resourceRelationEntity.getRelationTypeId());
    }
}
