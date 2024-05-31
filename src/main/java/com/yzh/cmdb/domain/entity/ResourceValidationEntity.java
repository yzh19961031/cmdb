package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 唯一校验
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resource_validation")
@Accessors(chain = true)
public class ResourceValidationEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long modelId;
    /**
     * 唯一校验的列名 使用属性的标识拼接 用,分开
     */
    private String validateColumns;
}
