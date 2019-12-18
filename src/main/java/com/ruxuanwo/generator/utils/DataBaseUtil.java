package com.ruxuanwo.generator.utils;

import com.ruxuanwo.generator.convert.IColumnType;
import com.ruxuanwo.generator.convert.ITypeConvert;
import com.ruxuanwo.generator.convert.MySqlTypeConvert;
import com.ruxuanwo.generator.exception.SqlErrorException;
import com.ruxuanwo.generator.model.CodeConfig;
import com.ruxuanwo.generator.model.DbConfig;
import com.ruxuanwo.generator.model.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * 自制操作连接数据库工具类
 *
 * @Author: ChenBin
 * @Date: 2018/4/20/0020 15:46
 */
public class DataBaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseUtil.class);


    /**
     * 获取数据库下的所有表名
     *
     * @param dbConfig 数据库配置对象
     * @return List集合
     */
    public static List<String> getTableNames(DbConfig dbConfig) {
        List<String> data = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(dbConfig);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SHOW TABLES");
            data = resultSetToList(resultSet);
            logger.info("查询成功" + data);

        } catch (SQLException e) {
            throw new SqlErrorException("获取数据库表名失败：" + e.getMessage());
        } finally {
            closeConnection(connection, resultSet, statement);
        }
        return data;
    }

    /**
     * 获取数据库下的所有表字段名称
     *
     * @param dbConfig 数据库配置对象
     * @return List集合
     */
    public static List<String> getColumnNames(DbConfig dbConfig) {
        List<String> columnNames = new ArrayList<>();
        Connection connection = getConnection(dbConfig);
        PreparedStatement statement = null;
        String sql = "SELECT * FROM " + dbConfig.getTableName();
        try {
            statement = connection.prepareStatement(sql);
            //结果集元数据
            ResultSetMetaData metaData = statement.getMetaData();
            //表列数
            int size = metaData.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(metaData.getColumnName(i + 1));
            }
            logger.info("查询成功" + columnNames);
        } catch (SQLException e) {
            throw new SqlErrorException("获取数据库表字段失败：" + e.getMessage());
        } finally {
            closeConnection(connection, null, statement);
        }
        return columnNames;
    }

    /**
     * 获取数据库下的所有表字段类型名称
     *
     * @param dbConfig 数据库配置对象
     * @return List集合
     */
    public static List<String> getColumnTypes(DbConfig dbConfig) {
        List<String> columnTypes = new ArrayList<>();
        Connection connection = getConnection(dbConfig);
        PreparedStatement statement = null;
        String sql = "SELECT * FROM " + dbConfig.getTableName();
        try {
            statement = connection.prepareStatement(sql);
            //结果集元数据
            ResultSetMetaData metaData = statement.getMetaData();
            //表列数
            int size = metaData.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.add(metaData.getColumnTypeName(i + 1));
            }
            logger.info("查询成功", columnTypes);
        } catch (SQLException e) {
            throw new SqlErrorException("获取数据库表字段类型失败：" + e.getMessage());
        } finally {
            closeConnection(connection, null, statement);
        }
        return columnTypes;
    }

    /**
     * 获取数据库下的所有表字段注释
     *
     * @param dbConfig 数据库配置对象
     * @return List集合
     */
    public static List<String> getColumnComments(DbConfig dbConfig) {
        List<String> columnComments = new ArrayList<>();
        Connection connection = getConnection(dbConfig);
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SHOW FULL COLUMNS FROM " + dbConfig.getTableName());
            while (resultSet.next()) {
                columnComments.add(resultSet.getString("Comment"));
            }
            logger.info("查询成功" + columnComments);
        } catch (SQLException e) {
            throw new SqlErrorException("获取数据库表字段注释失败[" + e.getMessage());
        } finally {
            closeConnection(connection, resultSet, statement);
        }
        return columnComments;
    }

    /**
     * 获取表名称、表主键类型以及表注释
     *
     * @param dbConfig
     * @return
     */
    public static List<Map<String, Object>> getTableComment(DbConfig dbConfig) {
        Connection connection = getConnection(dbConfig);
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> tableComment = new ArrayList<>();
        String tableName = dbConfig.getTableName();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b.TABLE_NAME AS tableName,b.TABLE_COMMENT AS tableComment,")
                .append("a.COLUMN_TYPE AS dataType ")
                .append("FROM ").append("( ")
                .append("SELECT TABLE_NAME,COLUMN_NAME ").append("FROM ")
                .append("INFORMATION_SCHEMA.KEY_COLUMN_USAGE ")
                .append("WHERE ").append("constraint_name = 'PRIMARY' ")
                .append("AND TABLE_SCHEMA = ").append("'" + dbConfig.getDataBase() + "'");
        if (!"".equals(tableName) && tableName != null) {
            sql.append(" AND TABLE_NAME LIKE ").append("'%").append(tableName).append("%'");
        }
        sql.append(" GROUP BY TABLE_NAME")
                .append(" ) p").append(" INNER JOIN information_schema.COLUMNS a ")
                .append("ON (").append(" a.TABLE_SCHEMA = ").append("'" + dbConfig.getDataBase() + "'")
                .append(" AND a.TABLE_NAME = p.TABLE_NAME ")
                .append("AND p.COLUMN_NAME = a.COLUMN_NAME ").append(")")
                .append(" INNER JOIN INFORMATION_SCHEMA.TABLES b ON ( ")
                .append("b.TABLE_SCHEMA = ").append("'" + dbConfig.getDataBase() + "'")
                .append(" AND b.TABLE_NAME = a.TABLE_NAME ").append(")");
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(60000);
            resultSet = statement.executeQuery(sql.toString());
            while (resultSet.next()) {
                String jdbcType = resultSet.getString("dataType").toUpperCase();
                if (jdbcType.contains("VARCHAR")) {
                    jdbcType = "String";
                } else if (jdbcType.contains("BIGINT")) {
                    jdbcType = "Long";
                } else {
                    jdbcType = "Integer";
                }
                Map<String, Object> map = new HashMap<>(16);
                map.put("tableName", resultSet.getString("tableName"));
                map.put("domainName", CodeUtil.tableNameConvertUpperCamel(resultSet.getString("tableName")));
                map.put("comment", resultSet.getString("tableComment"));
                map.put("jdbcType", jdbcType);
                tableComment.add(map);
            }

            logger.info("查询成功" + tableComment);
        } catch (SQLException e) {
            throw new SqlErrorException("获取表主键信息失败：" + e.getMessage());
        } finally {
            closeConnection(connection, resultSet, statement);
        }
        return tableComment;
    }

    /**
     * 往数据库中插入数据
     *
     * @param dbConfig
     * @return
     */
    public static void insert(Connection connection, DbConfig dbConfig, HashMap<String, String> hashMap, String state) throws SQLException {
        StringBuilder sql = new StringBuilder();
        Iterator iterator = hashMap.keySet().iterator();
        String key;
        sql.append("INSERT INTO ").append(dbConfig.getTableName())
                .append(" (");
        String stringNum = "0";
        if (stringNum.equals(state)) {
            sql.append("id");
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                sql.append(",").append(key);
            }
            sql.append(" )").append("VALUES(").append("REPLACE(UUID(),\"-\",\"\") ");
            iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                sql.append(",").append("'").append(hashMap.get(key)).append("'");
            }
            sql.append(")");
        } else {
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                sql.append(key).append(",");
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(" )").append("VALUES(");
            iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                sql.append("'").append(hashMap.get(key)).append("'").append(",");
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(")");
        }

        connection.createStatement().executeUpdate(sql.toString());
    }

    /**
     * 删除指定表
     *
     * @param dbConfig 数据库配置对象
     */
    public static void deleteTable(DbConfig dbConfig) {
        Connection connection = getConnection(dbConfig);
        String sql = "DROP TABLE " + dbConfig.getTableName();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            throw new SqlErrorException("删除表失败：" + e.getMessage());
        } finally {
            closeConnection(connection, null, statement);
        }
    }

    /**
     * 获取某张表下字段信息
     *
     * @param dbConfig 数据库配置对象
     * @return
     */
    public static List<TableField> getTableField(DbConfig dbConfig){
        Connection connection = getConnection(dbConfig);
        Statement statement = null;
        ResultSet resultSet = null;
        List<TableField> tableFields = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.column_Name AS columnName,")
        .append("CASE WHEN p.column_Name IS NULL THEN 'false' ELSE 'true' END AS pkColumn,")
        .append("CASE WHEN a.extra = 'auto_increment' THEN 'true' ELSE 'false' END AS autoAdd,")
        .append("a.data_type AS jdbcType,")
        .append("column_COMMENT AS descr ")
        .append("FROM information_schema. COLUMNS a ")
        .append("LEFT JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS p ON a.table_schema = p.table_schema ")
        .append("AND a.table_name = p.TABLE_NAME ")
        .append("AND a.COLUMN_NAME = p.COLUMN_NAME ")
        .append("AND p.constraint_name = 'PRIMARY' ")
        .append("WHERE a.table_schema = ").append("'" + dbConfig.getDataBase() + "' ")
        .append("AND a.table_name = ").append("'" + dbConfig.getTableName() + "' ")
        .append("ORDER BY a.ordinal_position");
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(60000);
            resultSet = statement.executeQuery(sql.toString());
            while (resultSet.next()) {
                String columnName = resultSet.getString("columnName");
                String jdbcType = resultSet.getString("jdbcType");
                boolean autoAdd = resultSet.getBoolean("autoAdd");
                ITypeConvert typeConvert = new MySqlTypeConvert();
                IColumnType columnType = typeConvert.processTypeConvert(jdbcType);
                TableField field = new TableField();
                field.setIsKey(resultSet.getBoolean("pkColumn"));
                field.setComment(resultSet.getString("descr"));
                field.setName(columnName);
                field.setPropertyName(CodeUtil.domainNameConvertMappingPath(columnName).substring(1));
                field.setType(jdbcType.toUpperCase());
                field.setPropertyType(columnType.getType());
                field.setImportPackage(columnType.getPkg());
                field.setKeyType(autoAdd ? "AUTO" : "UUID");
                tableFields.add(field);
            }
        } catch (SQLException e){
            throw new SqlErrorException("获取表字段信息失败：" + e.getMessage());
        } finally {
            closeConnection(connection, resultSet, statement);
        }
        return tableFields;
    }


    /**
     * 数据库连接
     *
     * @param dbConfig 连接配置对象
     * @return Connection对象
     */
    public static Connection getConnection(DbConfig dbConfig) {
        Connection connection;
        String usingPassword = "using password";
        String unknownDatabase = "Unknown database";
        String linkFailure = "link failure";
        try {
            Class.forName(dbConfig.getDriverClass());
            String url = "jdbc:mysql://" + dbConfig.getUrl() + ":" + dbConfig.getPort() + "/" + dbConfig.getDataBase() + CodeConfig.CONFIG_URL;
            logger.info("正在连接[" + url + "]...");
            connection = DriverManager.getConnection(url, dbConfig.getUser(), dbConfig.getPassWord());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("MYSQL" + "数据库连接失败[" + e.getMessage() + "]");
            throw new SqlErrorException("连接失败,请检查数据库驱动类型是否正确！");
        } catch (SQLException e) {
            if (e.getMessage().contains(usingPassword)) {
                throw new SqlErrorException();
            } else if (e.getMessage().contains(unknownDatabase)) {
                throw new SqlErrorException("找不到数据库名" + e.getMessage().substring((e.getMessage().indexOf("'")) + 1, e.getMessage().lastIndexOf("'")));
            } else if (e.getMessage().contains(linkFailure)) {
                throw new SqlErrorException("数据库连接失败，请检查配置是否正确！");
            } else {
                throw new SqlErrorException("数据库连接失败，请检查配置是否正确！");
            }
        }
        return connection;
    }

    /**
     * 转换元数据
     *
     * @param rs ResultSet对象
     * @return List数据
     * @throws SQLException
     */
    private static List<String> resultSetToList(ResultSet rs) throws SQLException {
        if (rs == null) {
            logger.info("调试" + "转换失败元数据为空");
            return null;
        }
        //获取字段数
        int columnCount = rs.getMetaData().getColumnCount();
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            for (int j = 1; j <= columnCount; j++) {
                result.add(rs.getString(j));
            }
        }
        return result;
    }

    /**
     * 模拟mybatis映射赋值
     *
     * @param resultSet 结果集
     * @param clazz     对应的class类
     * @param <T>
     * @return
     */
    private static <T> T mappingObj(ResultSet resultSet, Class<T> clazz) {
        T obj = null;
        try {
            //实例化映射对象
            obj = clazz.newInstance();
            //获取映射对象的方法
            Method[] methods = clazz.getMethods();
            //获取结果集中元数据信息
            ResultSetMetaData meta = resultSet.getMetaData();
            // 按字段数目循环结果集中记录，进行对象映射
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                // 构造当前列的set方法名称
                String columnName = meta.getColumnName(i);
                String methodName = "set" + columnName.replaceAll("[\\.|\\-|\\_]", "");
                // 循环查找同名方法，并通过反射调用该方法，设置属性
                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase(methodName)) {
                        method.invoke(obj, resultSet.getObject(i));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     * 获取所有不能为null的字段
     *
     * @param dbConfig
     * @return
     */
    public static List<String> getNotNUllField(DbConfig dbConfig) {
        Connection connection = getConnection(dbConfig);
        List<String> fields = new ArrayList<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dbmd = connection.getMetaData();
            rs = dbmd.getColumns(null, "%", dbConfig.getTableName(), "%");
            String flag;
            while (rs.next()) {
                flag = rs.getString("IS_NULLABLE");
                if ("NO".equals(flag)) {
                    fields.add(rs.getString("COLUMN_NAME"));
                }
            }
        } catch (SQLException e) {
            throw new SqlErrorException("获取非空字段失败：" + e.getMessage());
        } finally {
            closeConnection(connection, rs, null);
        }
        return fields;
    }


    /**
     * 关闭所有连接
     */
    public static void closeConnection(Connection connection, ResultSet resultSet, Statement statement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new SqlErrorException("resultSet关闭出错：" + e.getMessage());
            }
            resultSet = null;
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new SqlErrorException("statement关闭出错：" + e.getMessage());
            }
            statement = null;
        }

        if (connection != null) {
            try {
                connection.close();
                logger.info("数据库关闭连接");
            } catch (SQLException e) {
                throw new SqlErrorException("connection关闭出错：" + e.getMessage());
            }
            connection = null;
        }
    }
}
