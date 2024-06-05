package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 资源实例查询
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceInstanceQueryDTO {
    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模型id不能为空")
    private Long modelId;

    @Schema(description = "模糊查询名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String name;

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小从1开始")
    private Integer pageIndex = 1;

    @Schema(description = "每页数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "每页数量不能为空")
    @Min(value = 1, message = "每页数量范围为1-100")
    @Max(value = 100, message = "每页数量范围为1-100")
    private Integer pageSize = 10;
}
