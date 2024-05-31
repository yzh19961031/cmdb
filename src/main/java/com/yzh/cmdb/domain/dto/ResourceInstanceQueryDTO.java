package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 资源实例查询
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceQueryDTO {
    private Long modelId;
    private String name;
    private String instanceId;

    private Integer pageIndex = 1;
    private Integer pageSize = 10;
}
