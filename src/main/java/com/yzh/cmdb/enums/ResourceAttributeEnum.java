package com.yzh.cmdb.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资源属性枚举
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public enum ResourceAttributeEnum {

    INPUT(0, "输入框"),
    INTEGER(1, "整数"),
    FLOAT(2, "浮点数"),
    DATE(3, "日期"),
    ENUMS(4, "枚举"),
    PASSWORD(5, "密码");


    private final Integer value;
    private final String description;

    ResourceAttributeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }


    private static final Map<Integer, ResourceAttributeEnum> RESOURCE_ATTRIBUTE_ENUM_MAP = Arrays.stream(ResourceAttributeEnum.values())
            .collect(Collectors.toMap(ResourceAttributeEnum::getValue, Function.identity()));


    /**
     * 根据值获取对应的枚举类
     *
     * @param value 值
     * @return 枚举
     */
    public static ResourceAttributeEnum getByValue(Integer value) {
        return RESOURCE_ATTRIBUTE_ENUM_MAP.getOrDefault(value, INPUT);
    }
}
