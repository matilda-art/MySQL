package blog;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-08 18:46
 **/

// Utilize 工具（为数据库提供的工具）
public class DBUtil {
    // 静态属性，这个代码运行过程中，只有一份（整个进程运行过程中）
    private static DataSource dataSource = null;

    static {
        initDataSource();
    }

    private static void initDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("cardiac02200059");
        mysqlDataSource.setDatabaseName("blog");
        mysqlDataSource.setUseSSL(false);
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
