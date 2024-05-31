package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 属性查询
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceAttributeQueryDTO {
    private Long modelId;
    private String name;
    private String identifier;
}

