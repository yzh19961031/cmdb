package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 模型属性
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceAttributeDTO {
    @Schema(description = "模型属性id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "模型属性名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "价格")
    @NotBlank(message = "模型属性名称不能为空")
    @Size(min = 2, max = 50, message = "模型属性名称长度必须在2到50个字符之间")
    private String name;

    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    @NotNull(message = "模型id不能为空")
    private Long modelId;

    @Schema(description = "模型属性标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "hostname")
    @NotBlank(message = "模型属性标识不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "属性标识只允许字母开头，包含字母，数字和下划线")
    @Size(min = 2, max = 50, message = "属性标识长度必须在2到50个字符之间")
    private String identifier;

    @Schema(description = "可选项 如果输入框类型是下拉框 单选框 保存选项列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "value1, value2")
    private String options;

    @Schema(description = "模型属性类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模型属性类型不能为空")
    private Integer type;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED, example = "true | false")
    @NotNull(message = "模型属性是否必填参数不能为空")
    private Boolean isRequired;

    @Schema(description = "默认值 未启用", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true | false")
    private String defaultValue;

    @Schema(description = "校验规则", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "^[a-zA-Z][a-zA-Z0-9_]*$")
    private String validationRule;

    @Schema(description = "提示信息 未启用", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "123")
    private String validationMessage;
}
