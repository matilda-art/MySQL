import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Scanner;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-07 11:56
 **/

//使用PrepareStatement 替代 Statement
public class use_prepareStatement {
    public static void main(String[] args) throws SQLException {
        // 初始化一次，多次使用
        initDataSource();

        Scanner scanner = new Scanner(System.in);

        // 使用 Statement 的写法
        try (Connection c = getConnection()) {
            // 查询以下 SQL: select * from exam_result where english > 某个值 and chinese > 某个值
            // 这个值需要用户输入
            System.out.print("请输入英语获奖分数> ");
            int ei = scanner.nextInt();
            System.out.print("请输入语文获奖分数> ");
            int ci = scanner.nextInt();
            try (Statement s = c.createStatement()) {
                // 自己使用 String 拼接 SQL，很容易出问题
                String sql = "select * from exam_result where english > " + ei + " and chinese > " + ci;
                System.out.println(sql);
                try (ResultSet resultSet = s.executeQuery(sql)) {
                    while (resultSet.next()) {
                        String id = resultSet.getString("id");
                        String name = resultSet.getString("name");
                        String english = resultSet.getString("english");
                        String chinese = resultSet.getString("chinese");
                        String math = resultSet.getString("math");
                        System.out.println(id + ", " + name + ", " + english + ", " + chinese + ", " + math);
                    }
                }
            }
        }

        // 使用 PrepareStatement 的方式
        // 使用 Statement 进行sql执行时，如果 sql 中有参数，需要动态获取，Statement 比较差
        // 所以使用 PrepareStatement 代替：Statement 处理不谨慎，很容易 SQL 注入
        // PrepareStatement 可以有效的防止 SQL 注入
        try (Connection c = getConnection()) {
            // 查询以下 SQL: select * from exam_result where english > 某个值 and chinese > 某个值
            // 这个值需要用户输入
            System.out.print("请输入英语获奖分数> ");
            int ei = scanner.nextInt();
            System.out.print("请输入语文获奖分数> ");
            int ci = scanner.nextInt();
            // 提前把 sql 写好，使用 ? 作为占位符
            String sql = "select * from exam_result where english > ? and chinese > ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                // 使用具体的值，替代占位符
                s.setInt(1, ei);
                s.setInt(2, ci);

                try (ResultSet r = s.executeQuery()) {
                    while (r.next()) {
                        String id = r.getString("id");
                        String name = r.getString("name");
                        String english = r.getString("english");
                        String chinese = r.getString("chinese");
                        String math = r.getString("math");
                        System.out.println(id + ", " + name + ", " + english + ", " + chinese + ", " + math);
                    }
                }
            }
        }
    }

    private static DataSource dataSource = null;

    private static void initDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");//服务器名称
        mysqlDataSource.setPort(3306);//端口
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("cardiac02200059");
        mysqlDataSource.setDatabaseName("student");
        mysqlDataSource.setUseSSL(false);//是否使用useSSL
        mysqlDataSource.setCharacterEncoding("utf8");

        dataSource = mysqlDataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

