package com.yzh.cmdb.event.listener;

import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import com.yzh.cmdb.enums.ModelUpdateTypeEnum;
import com.yzh.cmdb.event.UpdateModelEvent;
import com.yzh.cmdb.utils.SQLGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * 更新模型监听
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Component
@Slf4j
public class UpdateModelListener {
    @Resource
    private JdbcTemplate jdbcTemplate;


    /**
     * 更新表
     *
     * @param event 更新模型事件
     */
    @Async
    @EventListener(classes = UpdateModelEvent.class)
    public void updateTable(UpdateModelEvent event) {
        ModelUpdateTypeEnum updateType = event.getUpdateType();
        String tableName = event.getTableName();
        switch (updateType) {
            case ADD_COLUMN:
                String addColumnSQL = SQLGenerator.generateAddColumnSQL(tableName, event.getTableColumnList());
                log.info("更新模型监听开始执行，执行添加列操作，本次执行的SQL语句为【{}】", addColumnSQL);
                jdbcTemplate.execute(addColumnSQL);
                log.info("表【{}】添加列成功", tableName);
                break;
            case DELETE_COLUMN:
                String dropColumnSQL = SQLGenerator.generateDropColumnSQL(tableName,
                        event.getTableColumnList().stream().map(ColumnDefinitionDTO::getColumnName).collect(Collectors.toList()));
                log.info("更新模型监听开始执行，执行删除列操作，本次执行的SQL语句为【{}】", dropColumnSQL);
                jdbcTemplate.execute(dropColumnSQL);
                log.info("表【{}】删除列成功", tableName);
                break;
            default:
                throw new IllegalArgumentException("不支持的更新类型");
        }
    }
}

