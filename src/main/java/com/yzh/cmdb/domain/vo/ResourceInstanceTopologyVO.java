package com.yzh.cmdb.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 实例拓扑详情
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceTopologyVO {
    // 实例关联的id
    private Long instanceRelationId;
    private String instanceId;
    private String instanceName;
    private Long modelId;
    private String modelName;
    private List<ResourceInstanceTopologyVO> children;
    private List<ResourceInstanceTopologyVO> parents;
}