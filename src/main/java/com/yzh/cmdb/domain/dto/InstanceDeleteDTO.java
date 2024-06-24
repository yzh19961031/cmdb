package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 实例删除
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class InstanceDeleteDTO {
    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模型id不能为空")
    private Long modelId;

    @Schema(description = "实例id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "实例id不能为空")
    private Long instanceId;
}
