package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 模型关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceRelationDTO {
    private Long id;
    private Long sourceId;
    private Long targetId;
    private Long relationTypeId;
    private Integer relationBind;
}