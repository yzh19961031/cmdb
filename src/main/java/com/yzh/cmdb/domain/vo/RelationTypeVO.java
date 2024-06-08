package com.yzh.cmdb.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
    private String name;

    @Schema(description = "关系类型描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private LocalDateTime createAt;
}
