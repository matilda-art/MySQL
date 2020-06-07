package try_with_resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-07 11:52
 **/
public class JDBCDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/student?useSSL=false&characterEncoding=utf8";
        String user = "root";
        String password = "cardiac02200059";

        Class.forName("com.mysql.jdbc.Driver");

        // 不使用 try-with-resource 的写法
        {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url, user, password);
                // 执行具体的操作
            } finally {
                if (connection != null) {
                    // 说明当时连接成功了
                    // 连接成功，就应该手动调用 close 关闭连接
                    connection.close();
                }
            }
        }

        // 使用 try-with-resource 的写法，1.8 之后支持，目的就是简化代码
        {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                // 执行具体的操作
            }   // 会自动调用 connection.close()，编译器会帮你加上相应的代码
        }
    }
}

