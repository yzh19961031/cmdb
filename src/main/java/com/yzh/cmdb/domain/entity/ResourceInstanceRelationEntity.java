package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 实例关联实体
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@TableName("resource_instance_relation")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ResourceInstanceRelationEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 源端实例信息
     */
    private String sourceInstanceId;
    private Long sourceModelId;

    /**
     * 目标实例信息
     */
    private String targetInstanceId;
    private Long targetModelId;

    /**
     * 关系类型
     */
    private Long relationTypeId;
    /**
     * 关系约束
     */
    private Integer relationBind;
}
