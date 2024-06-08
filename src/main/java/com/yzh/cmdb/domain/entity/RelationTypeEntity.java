package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 关系类型
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
@TableName("relation_type")
public class RelationTypeEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createAt;
}