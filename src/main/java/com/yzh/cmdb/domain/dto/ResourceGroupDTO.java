package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 资源分组
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
public class ResourceGroupDTO {
    @Schema(description = "资源分组id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "资源分组名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "商品")
    @NotBlank(message = "资源分组不能为空")
    @Size(min = 2, max = 50, message = "资源分组长度必须在2到50个字符之间")
    private String name;

    @Schema(description = "分组描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "xxx")
    @Size(max = 500, message = "分组描述长度不能超过500位")
    private String description;
}