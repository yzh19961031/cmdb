package com.yzh.cmdb.event.listener;

import com.yzh.cmdb.event.DeleteModelEvent;
import com.yzh.cmdb.utils.SQLGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 删除模型监听
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Slf4j
@Component
public class DeleteModelListener {
    @Resource
    private JdbcTemplate jdbcTemplate;


    /**
     * 删除表
     *
     * @param event 删除模型事件
     */
    @Async
    @EventListener(classes = DeleteModelEvent.class)
    public void deleteTable(DeleteModelEvent event) {
        String tableName = event.getTableName();
        String dropTableSQL = SQLGenerator.generateDropTableSQL(tableName);
        log.info("删除模型监听开始执行，本次执行的SQL语句为【{}】", dropTableSQL);
        jdbcTemplate.execute(dropTableSQL);
        log.info("表【{}】删除成功", tableName);
    }
}
