package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 实例关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceRelationDTO {
    private Long id;

    /**
     * 源端实例信息
     */
    private String sourceInstanceId;
    private Long sourceModelId;

    /**
     * 目标实例信息
     */
    private String targetInstanceId;
    private Long targetModelId;

    /**
     * 关系类型
     */
    private Long relationTypeId;
    /**
     * 关系约束
     */
    private Integer relationBind;
}
