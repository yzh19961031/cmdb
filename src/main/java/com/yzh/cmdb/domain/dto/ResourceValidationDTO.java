package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 唯一校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceValidationDTO {
    @Schema(description = "唯一校验id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long modelId;

    @Schema(description = "唯一校验项", requiredMode = Schema.RequiredMode.REQUIRED, example = "key1,key2")
    private String validateColumns;
}

