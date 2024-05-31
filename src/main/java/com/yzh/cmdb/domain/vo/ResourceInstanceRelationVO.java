package com.yzh.cmdb.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 实例关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceRelationVO {
    private Long modelId;
    private String modelName;
    private List<ResourceInstanceRelationDetail> instanceList;

    @Data
    public static class ResourceInstanceRelationDetail {
        private Long instanceRelationId;
        private DynamicInstanceVO dynamicInstance;
    }
}
