package com.yzh.cmdb.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 动态实例
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DynamicInstanceVO {
    /**
     * 实例id
     */
    private String instanceId;


    /**
     * 属性名和属性值
     */
    private List<Attributes> resourceDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attributes {
        private String name;
        private String identifier;
        private Object value;
    }
}
