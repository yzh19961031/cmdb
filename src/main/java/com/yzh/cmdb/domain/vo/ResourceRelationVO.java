package com.yzh.cmdb.domain.vo;

import lombok.Data;

/**
 * 模型关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceRelationVO {
    private Long id;
    private Long sourceId;
    private String sourceModelName;
    private Long targetId;
    private String targetModelName;
    // 关系类型
    private Long relationTypeId;
    private String relationName;
    // 绑定关系
    private Integer relationBind;
    private String relationBindName;
}
