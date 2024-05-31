package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型关系
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@TableName("resource_relation")
@EqualsAndHashCode(callSuper = true)
public class ResourceRelationEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sourceId;
    private Long targetId;
    private Long relationTypeId;
    private Integer relationBind;
}

