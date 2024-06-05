package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 属性查询
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceAttributeQueryDTO {
    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    @NotNull(message = "模型id不能为空")
    private Long modelId;

    @Schema(description = "模型属性名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "价格")
    private String name;

    @Schema(description = "模型属性标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "hostname")
    private String identifier;
}

