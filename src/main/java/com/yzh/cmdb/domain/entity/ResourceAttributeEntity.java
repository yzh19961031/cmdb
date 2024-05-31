package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 模型属性
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("resource_attr")
public class ResourceAttributeEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long modelId;
    /**
     * 属性标识
     */
    private String identifier;
    private String options;
    private Integer type;
    private Boolean isRequired;
    private String defaultValue;
    private String validationRule;
    private String validationMessage;
}
