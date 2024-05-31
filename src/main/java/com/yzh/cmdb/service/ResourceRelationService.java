package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.dto.ResourceRelationDTO;
import com.yzh.cmdb.domain.vo.GroupResourceRelationVO;
import com.yzh.cmdb.domain.vo.ResourceModelTopologyVO;

/**
 * 模型关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public interface ResourceRelationService {

    /**
     * 添加模型关联关系
     *
     * @param resourceRelationDTO 模型关联关系
     */
    void add(ResourceRelationDTO resourceRelationDTO);

    /**
     * 删除模型关联关系
     *
     * @param id 关联关系id
     */
    void delete(Long id);

    /**
     * 获取指定模型的关联列表
     *
     * @param modelId 模型id
     * @return 关联列表
     */
    GroupResourceRelationVO list(Long modelId);

    /**
     * 生成模型拓扑图
     *
     * @return 拓扑详情
     */
    ResourceModelTopologyVO recursiveTopology();
}
