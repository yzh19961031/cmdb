package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源分组
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resource_group")
public class ResourceGroupEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
}
