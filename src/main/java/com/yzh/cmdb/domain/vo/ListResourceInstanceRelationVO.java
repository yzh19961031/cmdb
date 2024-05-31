package com.yzh.cmdb.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 实例关联列表
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@Builder
public class ListResourceInstanceRelationVO {
    /**
     * 上级
     */
    private List<ResourceInstanceRelationVO> parents;
    /**
     * 下级
     */
    private List<ResourceInstanceRelationVO> children;
}
