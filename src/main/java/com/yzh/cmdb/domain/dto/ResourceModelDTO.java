package com.yzh.cmdb.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 资源模型
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
@Accessors(chain = true)
public class ResourceModelDTO {
    @Schema(description = "模型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "模型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "模型1")
    @NotBlank(message = "模型名称不能为空")
    @Size(min = 2, max = 50, message = "模型名称长度必须在2到50个字符之间")
    private String name;

    @Schema(description = "模型标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "key")
    @NotBlank(message = "模型标识不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]*$", message = "模型唯一标识只允许字母开头，包含字母，数字和下划线")
    @Size(min = 2, max = 50, message = "模型标识长度必须在2到50个字符之间")
    private String uniqueKey;

    @Schema(description = "是否启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "true | false")
    private Boolean isEnabled;

    @Schema(description = "模型描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "xxxx")
    @Size(max = 500, message = "模型描述长度不能超过500位")
    private String description;

    @Schema(description = "分组id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long groupId;

    @Schema(description = "分组名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "分组1")
    private String groupName;

    @Schema(description = "属性列表", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<ResourceAttributeDTO> resourceAttributeDTOList;
}
