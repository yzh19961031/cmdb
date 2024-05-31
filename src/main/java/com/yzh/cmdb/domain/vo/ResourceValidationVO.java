package com.yzh.cmdb.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 唯一校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@Builder
public class ResourceValidationVO {
    private Long id;
    private Long modelId;
    /**
     * 唯一校验的列名 使用属性的标识拼接 用,分开
     */
    private String validateColumns;
    /**
     * 显示名称
     */
    private String displayName;
    /**
     * 属性的id 用于回显
     */
    private List<Long> attributeList;
}