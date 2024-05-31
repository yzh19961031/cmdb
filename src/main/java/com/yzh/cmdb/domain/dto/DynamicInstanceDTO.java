package com.yzh.cmdb.domain.dto;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * 动态实例
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class DynamicInstanceDTO {
    /**
     * 模型的id
     */
    private Long modelId;

    /**
     * 实例id
     */
    private String instanceId;


    /**
     * 实例的数据 按照下面的格式接收
     *  {
     *     "column1": "value1",
     *     "column2": "value2",
     *     "column3": "value3"
     *   }
     */
    private LinkedHashMap<String, Object> data;
}
