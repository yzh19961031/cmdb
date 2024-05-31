package com.yzh.cmdb.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 关系约束类型
 * 页面不展示多对一
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public enum RelationBindEnum {
    ONE2ONE(0, "一对一"),
    ONE2MANY(1, "一对多"),
    MANY2MANY(2, "多对多");
    private final Integer value;
    private final String desc;

    RelationBindEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private static final Map<Integer, RelationBindEnum> MAP = Arrays.stream(RelationBindEnum.values())
            .collect(Collectors.toMap(RelationBindEnum::getValue, Function.identity()));

    /**
     * 根据value获取关系约束类型
     *
     * @param value 值
     * @return 约束类型
     */
    public static RelationBindEnum getByValue(Integer value) {
        return MAP.getOrDefault(value, ONE2ONE);
    }
}

