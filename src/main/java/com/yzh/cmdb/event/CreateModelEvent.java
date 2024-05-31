package com.yzh.cmdb.event;

import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 创建模型事件
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public class CreateModelEvent extends ApplicationEvent {
    private final String tableName;
    private final List<ColumnDefinitionDTO> tableColumnList;

    public CreateModelEvent(Object source, String tableName, List<ColumnDefinitionDTO> tableColumnList) {
        super(source);
        this.tableName = tableName;
        this.tableColumnList = tableColumnList;
    }
}
