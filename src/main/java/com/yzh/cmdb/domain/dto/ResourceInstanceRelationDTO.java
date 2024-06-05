package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 实例关联
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceRelationDTO {
    @Schema(description = "实例关联id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    /**
     * 源端实例信息
     */
    @Schema(description = "源端实例id", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxx-xxx")
    @NotBlank(message = "源端实例id不能为空")
    private String sourceInstanceId;

    @Schema(description = "源端模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotBlank(message = "源端模型id不能为空")
    private Long sourceModelId;

    /**
     * 目标实例信息
     */
    @Schema(description = "目标端实例id", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxx-xxx")
    @NotBlank(message = "目标端实例id不能为空")
    private String targetInstanceId;

    @Schema(description = "目标端模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotBlank(message = "目标端模型id不能为空")
    private Long targetModelId;

    /**
     * 关系类型
     */
    @Schema(description = "关系类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotBlank(message = "请选择关系类型")
    private Long relationTypeId;

    /**
     * 关系约束
     */
    @Schema(description = "关系约束id", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotBlank(message = "请选择关系约束")
    private Integer relationBind;
}
