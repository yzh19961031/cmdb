package com.yzh.cmdb.domain.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础实体属性
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
@Data
public class BaseEntity {
    @TableLogic
    private Boolean isDeleted;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}