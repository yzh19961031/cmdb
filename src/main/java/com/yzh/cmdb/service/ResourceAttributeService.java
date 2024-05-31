package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.dto.ResourceAttributeDTO;
import com.yzh.cmdb.domain.dto.ResourceAttributeQueryDTO;
import com.yzh.cmdb.domain.vo.ResourceAttributeVO;

import java.util.List;

/**
 * 模型属性
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public interface ResourceAttributeService {

    /**
     * 添加模型属性
     *
     * @param resourceAttributeDTO 模型属性
     */
    void addAttribute(ResourceAttributeDTO resourceAttributeDTO);

    /**
     * 删除模型属性
     *
     * @param modelId 模型id
     * @param attrId 属性id
     */
    void deleteAttribute(Long modelId, Long attrId);

    /**
     * 编辑模型属性
     *
     * @param resourceAttributeDTO 模型属性
     */
    void update(ResourceAttributeDTO resourceAttributeDTO);

    /**
     * 查询模型属性列表
     *
     * @param resourceAttributeQueryDTO 模型属性列表查询参数
     * @return 模型属性列表
     */
    List<ResourceAttributeVO> list(ResourceAttributeQueryDTO resourceAttributeQueryDTO);

    /**
     * 查看属性详情
     *
     * @param attrId 属性id
     * @return 属性详情
     */
    ResourceAttributeVO detail(Long attrId);

}
