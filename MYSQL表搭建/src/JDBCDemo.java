import java.sql.*;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-05 19:08
 **/
public class JDBCDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //注册驱动——选择了乙方
        Class.forName("com.mysql.jdbc.Driver");

        /*完整的目标是执行select * from ……*/
        //建立数据库连接
        //写明mysql服务端所在地
        //以后写项目只需修改默认数据库名称即可
        String defaultDatabaseName = "student";//等于我们在 mysql 界面上用 use 库名称，指定默认库
        String password = "cardiac02200059";//填写你自己的数据库密码

        //下面基本不变
        String user = "root";
        String ur1 = "jdbc:mysql://127.0.0.1:3306/" + defaultDatabaseName + "?useSSL=false&characterEncoding=utf8";

        Connection connection = DriverManager.getConnection(ur1,user,password);
        //打印connection对象，验证是否连接成功
        System.out.println(connection);

        updateDemo(connection);

        // -1. 关闭刚才的连接
        connection.close();
    }


    //非查询操作：
    //1.获取Statement对象
    //2.执行executeUpdate并得到影响行数
    //-1.关闭Statement对象
    private static void updateDemo(Connection connection) throws SQLException {
        // 获取 Statement 对象
        Statement statement = connection.createStatement();
        String sql = "insert into exam_result (id, name, math) values ('11', '小陈', '88')";
        int affectedRows = statement.executeUpdate(sql);
        System.out.printf("Query OK, %d row affected%n", affectedRows);

        statement.close();
    }


    //查询操作：
    //1.获取Statement对象
    //2.执行executeQuery并得到ResultSet对象
    //3.控制行+列，获取需要的结果
    //-2.关闭ResultSet对象
    //-1.关闭Statement对象
    private static void queryDemo(Connection connection) throws SQLException {
        // 要真正的执行 sql 语言，并且获取数据库返回的结果
        // Statement 代表的是 “语句” 的抽象对象
        Statement statement = connection.createStatement();
        String sql = "select * from exam_result";  // 没有要求必须分号结尾了
        // 1. executeQuery 用在查询（query）场景下
        // 2. ResultSet 代表是 “结果集” 的抽象对象
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String chinese = resultSet.getString(3);
            String math = resultSet.getString(4);
            String english = resultSet.getString(5);
        }
        // -3. 关闭 resultSet 对象
        resultSet.close();
        // -2. 关闭 statement 对象
        statement.close();
    }
}
