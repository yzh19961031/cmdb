package com.yzh.cmdb.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 资源模型分组
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
@Accessors(chain = true)
public class GroupResourceModelVO {
    private Long id;
    private String name;
    private String description;
    private List<ResourceModelVO> resourceModelList;
}