package blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-08 19:12
 **/

// 负责用户登录功能
public class UserLoginAction implements Action {
    @Override
    public void run() {
        System.out.println("开始用户登录 ...");
        System.out.println();

        // 读取用户输入的信息
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名> ");
        String username = scanner.nextLine();
        System.out.print("请输入密码> ");
        String password = scanner.nextLine();

        try (Connection c = DBUtil.getConnection()) {
            String sql = "select id, nickname from users where username = ? and password = ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setString(1, username);
                s.setString(2, password);

                try (ResultSet rs = s.executeQuery()) {
                    // 因为 username 是 unique
                    // 所以查找的过程，要不返回 1 行数据，要不返回 0 行数据
                    // 不可能找到多个
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String nickname = rs.getString(2);
                        User user = new User();
                        user.id = id;
                        user.username = username;
                        user.nickname = nickname;

                        // 进行用户登录
                        User.login(user);
                    } else {
                        System.out.println("** 用户名或者密码错误，请重新输入!!");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("错误：" + e.getMessage());
        }
        // 搞定用户登录的过程
        // 根据 username + password 判断用户是否登录成功
        // select id, nickname from users where username = ? and password = ?

    }
}

