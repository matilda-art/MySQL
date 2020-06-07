import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-07 11:57
 **/

//使用 DataSource 替代 DriverManger
public class use_datasource {
    public static void getConnectionUseDriverManger() throws ClassNotFoundException, SQLException {
        // 1. 注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2. 获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/huojianban2_0601?charsetEncoding=utf8&useSSL=false";
        String user = "root";
        String password = "cardiac02200059";


        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // 我的目的就是要获取 Connection 对象
        }
    }

    // 1. 这个是新版 JDBC 标准提供的写法
    // 2. 写法比上述 url 的方式更加明确，不容易出现拼写错误
    // 3. 支持连接池的方式，所以可能效率更高
    public static void getConnectionUseDataSource() throws SQLException {
        DataSource dataSource;

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");//服务器名称
        mysqlDataSource.setPort(3306);//端口
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("cardiac02200059");
        mysqlDataSource.setDatabaseName("student");
        mysqlDataSource.setUseSSL(false);//是否使用useSSL
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;

        try (Connection connection = dataSource.getConnection()) {
            // 我的目的就是要获取 Connection 对象
        }
    }
}

