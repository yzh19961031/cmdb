package com.yzh.cmdb.event;

import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import com.yzh.cmdb.enums.ModelUpdateTypeEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 更新模型事件
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Getter
public class UpdateModelEvent extends ApplicationEvent {
    private final String tableName;
    private final ModelUpdateTypeEnum updateType;
    private final List<ColumnDefinitionDTO> tableColumnList;


    public UpdateModelEvent(Object source, String tableName, ModelUpdateTypeEnum updateType, List<ColumnDefinitionDTO> tableColumnList) {
        super(source);
        this.tableName = tableName;
        this.updateType = updateType;
        this.tableColumnList = tableColumnList;
    }
}