package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.dto.ResourceInstanceRelationDTO;
import com.yzh.cmdb.domain.vo.ListResourceInstanceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceInstanceTopologyVO;

import java.util.List;

/**
 * 实例关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public interface ResourceInstanceRelationService {

    /**
     * 添加实例关联关系
     *
     * @param resourceInstanceRelationDTO 实例关联详情
     */
    void add(ResourceInstanceRelationDTO resourceInstanceRelationDTO);


    /**
     * 删除实例关联
     *
     * @param id 实例关联id
     */
    void delete(Long id);

    /**
     * 生成指定实例的拓扑图
     *
     * @param modelId 模型id
     * @param instanceId 实例id
     * @return 拓扑详情
     */
    ResourceInstanceTopologyVO recursiveTopology(Long modelId, String instanceId);

    /**
     * 获取指定实例列表
     *
     * @param modelId 模型id
     * @param instanceId 实例id
     * @return 实例列表
     */
    ListResourceInstanceRelationVO list(Long modelId, String instanceId);

    /**
     * 批量添加实例关联关系
     *
     * @param resourceInstanceRelationDTOList 实例关联详情列表
     */
    void batchAdd(List<ResourceInstanceRelationDTO> resourceInstanceRelationDTOList);
}
