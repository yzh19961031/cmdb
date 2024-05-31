package com.yzh.cmdb.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 模型关联分组
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@Builder
public class GroupResourceRelationVO {
    /**
     * 作为源端模型的关联列表
     */
    private List<ResourceRelationVO> initiativeList;

    /**
     * 作为目标模型的关联列表
     */
    private List<ResourceRelationVO> passiveList;
}

