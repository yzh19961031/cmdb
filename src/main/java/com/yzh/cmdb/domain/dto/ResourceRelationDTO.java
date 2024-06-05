package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 模型关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceRelationDTO {
    @Schema(description = "模型关联id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "源端模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long sourceId;

    @Schema(description = "目标端模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long targetId;

    @Schema(description = "关系类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long relationTypeId;

    @Schema(description = "关系约束类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer relationBind;
}