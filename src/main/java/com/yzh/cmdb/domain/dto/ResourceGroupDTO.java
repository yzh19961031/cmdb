package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 资源分组
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
public class ResourceGroupDTO {
    private Long id;
    private String name;
    private String description;
}