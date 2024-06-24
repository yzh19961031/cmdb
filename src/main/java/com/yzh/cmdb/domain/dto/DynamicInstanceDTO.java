package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;

/**
 * 动态实例
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class DynamicInstanceDTO {
    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模型id不能为空")
    private Long modelId;

    @Schema(description = "实例id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long instanceId;

    /**
     * 实例的数据 按照下面的格式接收
     *  {
     *     "column1": "value1",
     *     "column2": "value2",
     *     "column3": "value3"
     *   }
     */
    @Schema(description = "实例数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private LinkedHashMap<String, Object> data;
}
