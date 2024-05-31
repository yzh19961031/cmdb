package com.yzh.cmdb.enums;

import lombok.Getter;

/**
 * 模型更新类型
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public enum ModelUpdateTypeEnum {
    ADD_COLUMN(0, "新增列"),
    DELETE_COLUMN(1, "删除列");
    private final Integer code;
    private final String description;

    ModelUpdateTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
