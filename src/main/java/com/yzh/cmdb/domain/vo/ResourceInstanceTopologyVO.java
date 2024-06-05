package com.yzh.cmdb.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 实例拓扑详情
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceTopologyVO {
    // 实例关联的id
    @Schema(description = "实例关联id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long instanceRelationId;

    @Schema(description = "选择的实例id", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxxx")
    private String instanceId;

    @Schema(description = "选择的实例名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "实例1")
    private String instanceName;

    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long modelId;

    @Schema(description = "模型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "model1")
    private String modelName;


    @Schema(description = "子级列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[obj1, obj2]")
    private List<ResourceInstanceTopologyVO> children;

    @Schema(description = "父级列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[obj1, obj2]")
    private List<ResourceInstanceTopologyVO> parents;
}