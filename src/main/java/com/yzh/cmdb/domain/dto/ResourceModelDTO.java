package com.yzh.cmdb.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

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
    private Long id;
    private String name;
    private String uniqueKey;
    private String description;
    private Long groupId;

    private Boolean isEnabled;
    private String groupName;
    private List<ResourceAttributeDTO> resourceAttributeDTOList;
}
