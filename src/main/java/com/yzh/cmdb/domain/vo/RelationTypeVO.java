package com.yzh.cmdb.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 关系类型
 *
 * @author yuanzhihao
 * @since 2024/6/7
 */
@Data
public class RelationTypeVO {
    @Schema(description = "关系类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "关系类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotBlank(message = "关系类型名称不能为空")
    @Size(min = 2, max = 50, message = "关系类型名称长度必须在2到50个字符之间")
    private String name;

    @Schema(description = "关系类型描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Size(max = 500, message = "关系类型描述长度不能超过500位")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private LocalDateTime createAt;
}
