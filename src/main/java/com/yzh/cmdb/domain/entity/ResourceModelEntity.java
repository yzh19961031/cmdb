package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源模型
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resource_model")
public class ResourceModelEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    /**
     * 唯一id
     */
    private String uniqueKey;
    private String description;
    private Long groupId;
    private Boolean isEnabled;
    /**
     * 表名称 生成规则 mongodb的objectId
     */
    private String tableName;
}
