package com.yzh.cmdb.event.listener;

import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import com.yzh.cmdb.event.CreateModelEvent;
import com.yzh.cmdb.utils.SQLGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 创建模型监听
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Slf4j
@Component
public class CreateModelListener {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 创建表
     *
     * @param event 创建模型事件
     */
    @Async
    @EventListener(classes = CreateModelEvent.class)
    public void createTable(CreateModelEvent event) {
        String tableName = event.getTableName();
        List<ColumnDefinitionDTO> tableColumnList = event.getTableColumnList();
        String sql = SQLGenerator.generateCreateTableSQL(tableName, tableColumnList);
        log.info("创建模型监听开始执行，本次执行的建表语句为【{}】", sql);
        jdbcTemplate.execute(sql);
        log.info("表【{}】创建成功", tableName);
    }
}