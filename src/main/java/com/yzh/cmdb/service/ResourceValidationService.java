package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.dto.ResourceValidationDTO;
import com.yzh.cmdb.domain.vo.ResourceValidationVO;

import java.util.List;

/**
 * 模型唯一校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public interface ResourceValidationService {

    /**
     * 添加唯一校验
     *
     * @param resourceValidationDTO 唯一校验属性
     */
    void add(ResourceValidationDTO resourceValidationDTO);

    /**
     * 删除唯一校验
     *
     * @param id 唯一校验id
     */
    void delete(Long id);

    /**
     * 获取指定模型的唯一校验列表
     *
     * @param modelId 模型id
     * @return 唯一校验列表
     */
    List<ResourceValidationVO> list(Long modelId);
}
