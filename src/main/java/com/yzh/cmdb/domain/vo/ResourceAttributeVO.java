package com.yzh.cmdb.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 模型属性
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceAttributeVO {
    @Schema(description = "模型属性id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "模型属性名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "价格")
    private String name;

    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private Long modelId;

    @Schema(description = "模型属性标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "hostname")
    private String identifier;

    @Schema(description = "可选项 如果输入框类型是下拉框 单选框 保存选项列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "value1, value2")
    private String options;

    @Schema(description = "属性类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED, example = "true | false")
    private Boolean isRequired;

    @Schema(description = "默认值 未启用", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true | false")
    private String defaultValue;

    @Schema(description = "校验规则", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "^[a-zA-Z][a-zA-Z0-9_]*$")
    private String validationRule;

    @Schema(description = "提示信息 未启用", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "123")
    private String validationMessage;
}