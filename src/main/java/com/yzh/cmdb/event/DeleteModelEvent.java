package com.yzh.cmdb.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 删除模型事件
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public class DeleteModelEvent extends ApplicationEvent {
    private final String tableName;

    public DeleteModelEvent(Object source, String tableName) {
        super(source);
        this.tableName = tableName;
    }
}
