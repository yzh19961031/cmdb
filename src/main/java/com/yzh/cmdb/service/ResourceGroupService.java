package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.dto.ResourceGroupDTO;

import java.util.List;

/**
 * 资源分组
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
public interface ResourceGroupService {

    /**
     * 新增分组
     *
     * @param resourceGroupDTO 分组信息
     */
    void add(ResourceGroupDTO resourceGroupDTO);

    /**
     * 删除分组
     *
     * @param id 指定分组id
     */
    void delete(Long id);

    /**
     * 修改分组
     *
     * @param resourceGroupDTO 分组信息
     */
    void update(ResourceGroupDTO resourceGroupDTO);

    /**
     * 获取分组列表
     *
     * @return 分组列表
     */
    List<ResourceGroupDTO> list();
}
