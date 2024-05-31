package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 实例删除
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class InstanceDeleteDTO {
    private Long modelId;
    private String instanceId;
}
