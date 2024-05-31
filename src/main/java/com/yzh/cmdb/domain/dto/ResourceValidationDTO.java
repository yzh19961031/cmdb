package com.yzh.cmdb.domain.dto;

import lombok.Data;

/**
 * 唯一校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
public class ResourceValidationDTO {
    private Long id;
    private Long modelId;
    private String validateColumns;
}

