package com.yzh.cmdb.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 资源模型
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@Accessors(chain = true)
public class ResourceModelVO {
    private Long id;
    private String name;
    private String uniqueKey;
    private String description;
}

