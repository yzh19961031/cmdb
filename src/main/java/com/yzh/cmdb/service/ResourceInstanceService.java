package com.yzh.cmdb.service;

import com.yzh.cmdb.domain.PageResult;
import com.yzh.cmdb.domain.dto.DynamicInstanceDTO;
import com.yzh.cmdb.domain.dto.InstanceDeleteDTO;
import com.yzh.cmdb.domain.dto.ResourceInstanceQueryDTO;
import com.yzh.cmdb.domain.vo.DynamicInstanceVO;

/**
 * 模型实例
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
public interface ResourceInstanceService {

    /**
     * 新增实例数据
     *
     * @param dynamicInstanceDTO 实例信息
     */
    void add(DynamicInstanceDTO dynamicInstanceDTO);


    /**
     * 删除指定实例
     *
     * @param deleteDTO 删除详情
     */
    void delete(InstanceDeleteDTO deleteDTO);

    /**
     * 获取实例列表
     *
     * @param resourceInstanceQueryDTO 查询参数
     * @return 实例列表
     */
    PageResult<DynamicInstanceVO> list(ResourceInstanceQueryDTO resourceInstanceQueryDTO);


    /**
     * 更新实例
     *
     * @param dynamicInstanceDTO 实例详情
     */
    void update(DynamicInstanceDTO dynamicInstanceDTO);

    /**
     * 获取实例详情
     *
     * @param modelId 模型id
     * @param instanceId 实例id
     * @return 实例详情
     */
    DynamicInstanceVO detail(Long modelId, String instanceId);
}
