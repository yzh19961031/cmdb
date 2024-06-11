package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.dto.ResourceModelDTO;
import com.yzh.cmdb.domain.vo.GroupResourceModelVO;
import com.yzh.cmdb.domain.vo.ResourceModelVO;

import java.util.List;

/**
 * 资源模型
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
public interface ResourceModelService {

    /**
     * 添加资源配置
     *
     * @param resourceModelDTO 资源配置参数
     */
    void add(ResourceModelDTO resourceModelDTO);

    /**
     * 删除指定模型
     *
     * @param id 模型id
     */
    void delete(Long id);

    /**
     * 获取资源模型分组列表
     *
     * @param name 名称
     * @return 资源模型分组列表
     */
    List<GroupResourceModelVO> list(String name);

    /**
     * 批量删除
     *
     * @param ids 模型id列表
     */
    void batchDelete(List<Long> ids);

    /**
     * 获取模型详情
     *
     * @param id 模型id
     * @return 模型详情
     */
    ResourceModelDTO detail(Long id);


    /**
     * 获取所有的模型列表
     *
     * @return 模型列表
     */
    List<ResourceModelVO> listAll();


    /**
     * 跟新资源模型
     *
     * @param resourceModelDTO 模型配置参数
     */
    void update(ResourceModelDTO resourceModelDTO);
}
