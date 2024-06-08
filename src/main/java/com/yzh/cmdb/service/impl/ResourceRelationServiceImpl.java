package com.yzh.cmdb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzh.cmdb.domain.dto.ResourceRelationDTO;
import com.yzh.cmdb.domain.entity.RelationTypeEntity;
import com.yzh.cmdb.domain.entity.ResourceInstanceRelationEntity;
import com.yzh.cmdb.domain.entity.ResourceModelEntity;
import com.yzh.cmdb.domain.entity.ResourceRelationEntity;
import com.yzh.cmdb.domain.vo.GroupResourceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceModelTopologyVO;
import com.yzh.cmdb.domain.vo.ResourceRelationVO;
import com.yzh.cmdb.enums.RelationBindEnum;
import com.yzh.cmdb.exception.CmdbException;
import com.yzh.cmdb.mapper.RelationTypeMapper;
import com.yzh.cmdb.mapper.ResourceInstanceRelationMapper;
import com.yzh.cmdb.mapper.ResourceModelMapper;
import com.yzh.cmdb.mapper.ResourceRelationMapper;
import com.yzh.cmdb.service.ResourceRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模型关联service
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Service
public class ResourceRelationServiceImpl implements ResourceRelationService {

    @Resource
    private ResourceRelationMapper resourceRelationMapper;

    @Resource
    private ResourceModelMapper resourceModelMapper;

    @Resource
    private RelationTypeMapper relationTypeMapper;

    @Resource
    private ResourceInstanceRelationMapper resourceInstanceRelationMapper;

    @Override
    public void add(ResourceRelationDTO resourceRelationDTO) {
        this.checkResourceRelation(resourceRelationDTO);
        ResourceRelationEntity resourceRelationEntity = new ResourceRelationEntity();
        BeanUtils.copyProperties(resourceRelationDTO, resourceRelationEntity);
        resourceRelationMapper.insert(resourceRelationEntity);
    }

    @Override
    public void delete(Long id) {
        // 删除对应的实例关联关系
        ResourceRelationEntity resourceRelationEntity = resourceRelationMapper.selectById(id);
        Long sourceId = resourceRelationEntity.getSourceId();
        Long targetId = resourceRelationEntity.getTargetId();
        resourceInstanceRelationMapper.delete(new LambdaQueryWrapper<ResourceInstanceRelationEntity>()
                .eq(ResourceInstanceRelationEntity::getSourceModelId, sourceId)
                .eq(ResourceInstanceRelationEntity::getTargetModelId, targetId));
        resourceRelationMapper.deleteById(id);
    }

    @Override
    public GroupResourceRelationVO list(Long modelId) {
        List<ResourceRelationVO> initiativeList = getResourceRelationVOList(modelId, true);
        List<ResourceRelationVO> passiveList = getResourceRelationVOList(modelId, false);

        return GroupResourceRelationVO
                .builder()
                .initiativeList(initiativeList)
                .passiveList(passiveList)
                .build();
    }

    @Override
    public ResourceModelTopologyVO recursiveTopology() {
        List<ResourceRelationEntity> resourceRelationEntities = resourceRelationMapper.selectList(null);
        Map<Long, String> relationTypeMap = Optional.ofNullable(relationTypeMapper.selectList(null))
                .orElseThrow(() -> new IllegalArgumentException("关系类型参数非法！！！"))
                .stream()
                .collect(Collectors.toMap(RelationTypeEntity::getId, RelationTypeEntity::getName));
        List<ResourceModelTopologyVO.Edge> edges = resourceRelationEntities.stream().map(resourceRelationEntity ->
                        ResourceModelTopologyVO.Edge.builder()
                                .source(resourceRelationEntity.getSourceId().toString())
                                .target(resourceRelationEntity.getTargetId().toString())
                                .label(relationTypeMap.getOrDefault(resourceRelationEntity.getRelationTypeId(), ""))
                                .build())
                .collect(Collectors.toList());

        List<ResourceModelEntity> resourceModelEntityList = resourceModelMapper.selectList(null);

        List<ResourceModelTopologyVO.Node> nodes = resourceModelEntityList.stream()
                .map(resourceModelEntity ->
                        ResourceModelTopologyVO.Node.builder()
                                .id(resourceModelEntity.getId().toString())
                                .label(resourceModelEntity.getName())
                                .build())
                .collect(Collectors.toList());

        return ResourceModelTopologyVO.builder().edges(edges).nodes(nodes).build();
    }

    @Override
    public List<ResourceRelationVO> listAll() {
        List<ResourceRelationEntity> resourceRelationEntities = resourceRelationMapper.selectList(null);
        if (CollectionUtils.isEmpty(resourceRelationEntities)) {
            return Collections.emptyList();
        }

        // 这边查询可以用map缓存一下
        return resourceRelationEntities.stream().map(resourceRelationEntity -> {
            ResourceRelationVO resourceRelationVO = new ResourceRelationVO();
            BeanUtils.copyProperties(resourceRelationEntity, resourceRelationVO);
            resourceRelationVO.setRelationBindName(RelationBindEnum.getByValue(resourceRelationVO.getRelationBind()).getDesc());
            resourceRelationVO.setRelationName(relationTypeMapper.selectById(resourceRelationVO.getRelationTypeId()).getName());
            resourceRelationVO.setSourceModelName(resourceModelMapper.selectById(resourceRelationVO.getSourceId()).getName());
            resourceRelationVO.setTargetModelName(resourceModelMapper.selectById(resourceRelationVO.getTargetId()).getName());
            return resourceRelationVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取模型关系
     *
     * @param modelId 模型id
     * @param isInitiative 是否作为源端模型
     * @return 关系列表
     */
    private List<ResourceRelationVO> getResourceRelationVOList(Long modelId, boolean isInitiative) {
        List<ResourceRelationEntity> relationList = isInitiative ?
                resourceRelationMapper.selectList(new LambdaQueryWrapper<ResourceRelationEntity>().eq(ResourceRelationEntity::getSourceId, modelId)) :
                resourceRelationMapper.selectList(new LambdaQueryWrapper<ResourceRelationEntity>().eq(ResourceRelationEntity::getTargetId, modelId));

        return relationList.stream().map(resourceRelationEntity -> {
            ResourceRelationVO resourceRelationVO = new ResourceRelationVO();
            BeanUtils.copyProperties(resourceRelationEntity, resourceRelationVO);
            resourceRelationVO.setRelationBindName(RelationBindEnum.getByValue(resourceRelationVO.getRelationBind()).getDesc());
            resourceRelationVO.setRelationName(relationTypeMapper.selectById(resourceRelationVO.getRelationTypeId()).getName());
            resourceRelationVO.setSourceModelName(resourceModelMapper.selectById(resourceRelationVO.getSourceId()).getName());
            resourceRelationVO.setTargetModelName(resourceModelMapper.selectById(resourceRelationVO.getTargetId()).getName());
            return resourceRelationVO;
        }).collect(Collectors.toList());
    }

    private void checkResourceRelation(ResourceRelationDTO resourceRelationDTO) {
        List<ResourceRelationEntity> resourceRelationEntities = resourceRelationMapper.selectList(null);
        if (CollectionUtils.isEmpty(resourceRelationEntities)) {
            return;
        }

        boolean match = resourceRelationEntities
                .stream()
                .anyMatch(resourceRelationEntity -> Objects.equals(resourceRelationEntity.getSourceId(), resourceRelationDTO.getSourceId())
                        && Objects.equals(resourceRelationEntity.getTargetId(), resourceRelationDTO.getTargetId()));
        if (match) {
            throw new CmdbException("模型关系已存在！！！");
        }

        // 判断是否依赖循环
        List<ResourceRelationDTO> collect = resourceRelationEntities.stream().map(resourceRelationEntity -> {
            ResourceRelationDTO relationDTO = new ResourceRelationDTO();
            BeanUtils.copyProperties(resourceRelationEntity, relationDTO);
            return relationDTO;
        }).collect(Collectors.toList());
        collect.add(resourceRelationDTO);

        DependencyChecker dependencyChecker = new DependencyChecker(collect);
        if (dependencyChecker.hasCycle(resourceRelationDTO.getSourceId())) {
            throw new CmdbException("模型关系存在依赖循环！！！");
        }
    }

    /**
     * 依赖循环校验
     *
     * @author yuanzhihao
     * @since 2024/5/31
     */
    private static class DependencyChecker {
        private final Map<Long, List<Long>> graph;

        public DependencyChecker(List<ResourceRelationDTO> relations) {
            this.graph = new HashMap<>();
            buildGraph(relations);
        }

        /**
         * 是否存在依赖循环
         *
         * @return T OR F
         */
        public boolean hasCycle(Long sourceId) {
            Set<Long> visited = new HashSet<>();
            Set<Long> recursionStack = new HashSet<>();
            return hasCycleUtil(sourceId, visited, recursionStack);
        }

        /**
         * 构建图
         *
         * @param relations 模型关联的列表
         */
        private void buildGraph(List<ResourceRelationDTO> relations) {
            for (ResourceRelationDTO relation : relations) {
                graph.putIfAbsent(relation.getSourceId(), new ArrayList<>());
                graph.get(relation.getSourceId()).add(relation.getTargetId());
            }
        }

        /**
         * 使用深度优先搜索 (DFS) 遍历图来检测环路
         *
         * @param node 当前节点
         * @param visited 已访问过的节点集合
         * @param recursionStack 递归栈，用于检测环路
         * @return 是否存在环路
         */
        private boolean hasCycleUtil(Long node, Set<Long> visited, Set<Long> recursionStack) {
            // 如果当前节点在递归栈中已经存在，说明存在环路，返回 true
            if (recursionStack.contains(node)) {
                return true;
            }
            // 如果当前节点已经被访问过，说明已经处理过，不需要继续遍历，返回 false
            if (visited.contains(node)) {
                return false;
            }

            // 将当前节点标记为已访问，并加入递归栈
            visited.add(node);
            recursionStack.add(node);

            // 获取当前节点的邻居节点列表
            List<Long> neighbors = graph.get(node);
            // 遍历当前节点的邻居节点
            if (neighbors != null) {
                for (Long neighbor : neighbors) {
                    // 递归调用 hasCycleUtil 方法，对邻居节点进行深度优先搜索
                    if (hasCycleUtil(neighbor, visited, recursionStack)) {
                        return true; // 如果发现环路，立即返回 true
                    }
                }
            }

            // 从递归栈中移除当前节点
            recursionStack.remove(node);
            // 当前节点及其邻居节点都被处理，返回 false，表示不存在环路
            return false;
        }
    }

}
