package blog;

import java.sql.*;
import java.util.Scanner;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-08 18:46
 **/

// 完整实现用户注册的功能
public class UserRegisterAction implements Action {
    @Override
    public void run() {
        // 1. 提示用户输入需要的信息，并且使用 jdbc 执行 sql
        System.out.println("开始用户注册...");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名称> ");
        String username = scanner.nextLine();
        System.out.print("请输入用户昵称> ");
        String nickname = scanner.nextLine();
        System.out.print("请输入密码> ");
        String password = scanner.nextLine();

        String sql = "insert into users (username, nickname, password) values (?, ?, ?)";
        try (Connection connection = DBUtil.getConnection()) {
            //Statement.RETURN_GENERATED_KEYS：返回生成的自增主键
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, username);
                statement.setString(2, nickname);
                statement.setString(3, password);

                statement.executeUpdate();

                int id;
                //返回一个单列n行的数据，n是你插入的行数
                try (ResultSet r = statement.getGeneratedKeys()) {
                    r.next();
                    id = r.getInt(1);
                }

                System.out.println("注册成功，欢迎您的加入，" + nickname);

                // 是否应该让刚注册的这个用户自动登录成功呢？还是让该用户重新登录？
                // 两种方法都可以接受
                // 在这里我们选择让其自动登录成功
                User user = new User();
                user.id = id;
                user.nickname = nickname;
                user.username = username;
                User.login(user);
            }
        } catch (SQLException e) {
            System.out.println("错误: " + e.getMessage());
        }
    }
}
