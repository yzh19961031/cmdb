package com.yzh.cmdb.utils;

import com.yzh.cmdb.domain.dto.ColumnDefinitionDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

/**
 * SQL生成器
 *
 * @author yuanzhihao
 * @since 2024/5/30
 */
public class SQLGenerator {


    /**
     * 创建建表语句
     *
     * @param tableName 表名
     * @param columnDefinitions 表列
     * @return 语句
     */
    public static String generateCreateTableSQL(String tableName, List<ColumnDefinitionDTO> columnDefinitions) {
        if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(columnDefinitions)) {
            throw new IllegalArgumentException("Table name and column names cannot be null or empty");
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE ").append(tableName).append(" (");

        for (ColumnDefinitionDTO columnDefinition : columnDefinitions) {
            sqlBuilder.append(columnDefinition.getColumnName()).append(" ").append(columnDefinition.getColumnType()).append(", ");
        }

        // 删除最后一个逗号和空格
        sqlBuilder.delete(sqlBuilder.length() - 2, sqlBuilder.length());
        sqlBuilder.append(")");

        return sqlBuilder.toString();
    }


    /**
     * 生成添加列的sql语句
     *
     * @param tableName 表名
     * @param columnDefinitions 列定义
     * @return sql语句
     */
    public static String generateAddColumnSQL(String tableName, List<ColumnDefinitionDTO> columnDefinitions) {
        if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(columnDefinitions)) {
            throw new IllegalArgumentException("Table name and column names cannot be null or empty");
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ");
        for (int i = 0; i < columnDefinitions.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            ColumnDefinitionDTO columnDefinitionDTO = columnDefinitions.get(i);
            sqlBuilder.append(columnDefinitionDTO.getColumnName()).append(" ").append(columnDefinitionDTO.getColumnType());
        }
        return sqlBuilder.toString();
    }


    /**
     * 生成删除列的sql语句
     *
     * @param tableName 表名
     * @param columnNames 列
     * @return sql语句
     */
    public static String generateDropColumnSQL(String tableName, List<String> columnNames) {
        if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(columnNames)) {
            throw new IllegalArgumentException("Table name and column names cannot be null or empty");
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("ALTER TABLE ").append(tableName).append(" DROP COLUMN ");
        for (int i = 0; i < columnNames.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append(columnNames.get(i));
        }
        return sqlBuilder.toString();
    }


    /**
     * 创建删除表语句
     *
     * @param tableName 表名
     * @return sql语句
     */
    public static String generateDropTableSQL(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        return "DROP TABLE " + tableName;
    }


    /**
     * 创建新增数据SQL语句 使用占位符的格式 防止SQL注入
     *
     * @param tableName 表名
     * @param columnNames 列名列表
     * @return sql语句 ex: INSERT INTO test (name,age) VALUES (?,?)
     */
    public static String generateInsertSQL(String tableName, Collection<String> columnNames) {
        if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(columnNames)) {
            throw new IllegalArgumentException("Table name and column names cannot be null or empty");
        }
        // 构建 SQL 插入语句
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder valuesBuilder = new StringBuilder(") VALUES (");
        for (String key : columnNames) {
            sqlBuilder.append(key).append(",");
            valuesBuilder.append("?,");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1).append(valuesBuilder.substring(0, valuesBuilder.length() - 1)).append(")");
        return sqlBuilder.toString();
    }


    /**
     * 生成联合索引SQL语句 唯一校验
     *
     * @param tableName 表名
     * @param columnNames 列名列表
     * @return SQL语句 ex: ALTER TABLE example_table ADD UNIQUE INDEX idx_column1_column2_column3 (column1, column2, column3);
     */
    public static String generateUniqueIndex(String tableName, Collection<String> columnNames) {
        if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(columnNames)) {
            throw new IllegalArgumentException("Table name and column names cannot be null or empty");
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (String columnName : columnNames) {
            joiner.add(columnName);
        }

        String indexName = "idx_" + String.join("_", columnNames);
        String columns = joiner.toString();
        return String.format(Locale.ROOT, "ALTER TABLE %s ADD UNIQUE INDEX %s (%s);", tableName, indexName, columns);
    }

    /**
     * 删除指定索引
     *
     * @param tableName 表名
     * @param indexName 索引名称
     * @return SQL语句  ex: alter table example_table drop index idx_age_name_email
     */
    public static String generateDropIndex(String tableName, String indexName) {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(indexName)) {
            throw new IllegalArgumentException("Table name and index name cannot be null or empty");
        }
        return "ALTER TABLE " +  tableName + " DROP INDEX " + indexName;
    }


    /**
     * 生成COUNT查询
     *
     * @param tableName 表名
     * @return count语句
     */
    public static String generateCountSQL(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        return "SELECT COUNT(1) FROM " +  tableName;
    }


    /**
     * 生成获取指定列总数的SQL语句
     *
     * @param tableName 表名
     * @param selectColumnNames 列名
     * @return sql ex:SELECT COUNT(1) FROM t_table WHERE attr1 = ? AND attr2 = ?
     */
    public static String generateSelectCountSQL(String tableName, List<String> selectColumnNames) {
        if (StringUtils.isEmpty(tableName) || CollectionUtils.isEmpty(selectColumnNames)) {
            throw new IllegalArgumentException("Table name or select column cannot be null or empty");
        }

        StringBuilder whereBuilder = new StringBuilder();
        for (int i = 0; i < selectColumnNames.size(); i++) {
            if (i > 0) {
                whereBuilder.append(" AND ");
            }
            whereBuilder.append(selectColumnNames.get(i)).append(" = ?");
        }
        return String.format("SELECT COUNT(1) FROM %s WHERE %s", tableName, whereBuilder);
    }


    /**
     * 生成查询SQL语句
     *
     * @param tableName 表名
     * @param selectColumnNames 查询的列名
     * @param conditionalColumnNames 条件列名
     * @return ex: SELECT id, name, age FROM users WHERE id = ? AND status = ?
     */
    public static String generateSelectSQL(String tableName, List<String> selectColumnNames, List<String> conditionalColumnNames) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name cannot be null or empty");
        }
        if (CollectionUtils.isEmpty(selectColumnNames)) {
            throw new IllegalArgumentException("Select column names cannot be null or empty");
        }

        StringJoiner selectColumns = new StringJoiner(", ");
        for (String column : selectColumnNames) {
            selectColumns.add(column);
        }

        StringBuilder whereClause = new StringBuilder();
        if (CollectionUtils.isNotEmpty(conditionalColumnNames)) {
            StringJoiner conditions = new StringJoiner(" AND ");
            for (String column : conditionalColumnNames) {
                conditions.add(column + " = ?");
            }
            whereClause.append(" WHERE ").append(conditions);
        } else {
            whereClause.append(" WHERE 1 = 1");
        }

        return "SELECT " + selectColumns + " FROM " + tableName + whereClause;
    }


    /**
     * 生成删除语句
     *
     * @param tableName 表名
     * @param deleteColumnNames 选择的列名
     * @return sql ex: DELETE FROM t_table WHERE attr1 = ? AND attr2 = ?
     */
    public static String generateDeleteSQL(String tableName, List<String> deleteColumnNames) {
        if (CollectionUtils.isEmpty(deleteColumnNames) || StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name or delete column cannot be null or empty");
        }
        StringBuilder whereBuilder = new StringBuilder();
        for (int i = 0; i < deleteColumnNames.size(); i++) {
            if (i > 0) {
                whereBuilder.append(" AND ");
            }
            whereBuilder.append(deleteColumnNames.get(i)).append(" = ?");
        }
        return String.format(Locale.ROOT, "DELETE FROM %s WHERE %s", tableName, whereBuilder);
    }
}
