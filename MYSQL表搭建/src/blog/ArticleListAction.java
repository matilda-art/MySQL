package blog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-08 20:18
 **/

public class ArticleListAction implements Action {
    @Override
    public void run() {
        try (Connection c = DBUtil.getConnection()) {
            List<String[]> articleList = new ArrayList<>();

            String sql = "select id, author_id, title, published_at from articles order by published_at desc";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        String[] article = new String[4];
                        String id = rs.getString("id");
                        String authorId = rs.getString("author_id");
                        String title = rs.getString("title");
                        String publishedAt = rs.getString("published_at");
                        article[0] = id;
                        article[1] = authorId;
                        article[2] = title;
                        article[3] = publishedAt;

                        articleList.add(article);
                    }
                }
            }

            // TODO: 打印作者的昵称而不是id信息
            // 需要根据作者 id，再次去 users 表中查询出 用户的昵称信息

            // 提取出所有需要查询的作者的 id
            // 利用 Set 中不会存在重复的元素的特性
            Set<String> authorIdSet = new HashSet<>();
            for (String[] article : articleList) {
                String authorId = article[1];
                authorIdSet.add(authorId);
            }

            // 使用提取出的作者 id，拼接出一个根据 id，查询作者昵称的 sql
            // select id, nickname from users where id in (...)
            // 因为占位符需要多少个，在写代码的期间也不清楚，所以，需要动态生成
            StringBuilder querySql = new StringBuilder("select id, nickname from users where id in (");
            for (int i = 1; i < authorIdSet.size(); i++) {
                querySql.append("?,");
            }
            querySql.append("?)");
            System.out.println("DEBUG: " + querySql.toString());
            /*
            try (PreparedStatement s = c.prepareStatement(querySql)) {
                int i = 1;
                for (String id : authorIdSet) {
                    s.setString(i++, id);
                }
            }
            */

/*            Map<String,String> userIdToNicknameMap = new HashMap<>();
            try (PreparedStatement s = c.prepareStatement(querySql.toString())){
                int i = 1;
                for (String id:authorIdSet) {
                    s.setString(i++, id);
                }

                try(ResultSet rs = s.executeQuery()){
                    while (rs.next()){
                        String id = rs.getString("id");
                        String nickname = rs.getString("nickname");
                        userIdToNicknameMap.put(id,nickname);
                    }
                }*/

            System.out.printf("#ID  | 标题                  | 作者    | 发表时间%n");
            for (String[] article : articleList) {
                System.out.printf("%-4s | %-40s | %-10s | %s%n", article[0], article[2], article[1], article[3]);
            }

        } catch (SQLException e) {
            System.out.println("错误：" + e.getMessage());
        }
    }
}

