package com.yzh.cmdb.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据库列定义
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@Accessors(chain = true)
public class ColumnDefinitionDTO {
    private String columnName;
    private String columnType;
}
