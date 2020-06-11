package blog;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @program: MYSQL表搭建
 * @description
 * @author: matilda
 * @create: 2020-06-11 11:19
 **/
public class ArticleDetailAction implements Action{
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要查看的文章的id> ");
        String id = scanner.nextLine();
        String authorId= null;
        String title = null;
        String content = null;
        String publishedAt = null;
        int likeCount = 0;


        try(Connection c = DBUtil.getConnection()){
            //1.查找文章信息
            String queryArticleSql = "select id,author_id,title,content,published_at from articles where id = ?";
            try (PreparedStatement s = c.prepareStatement(queryArticleSql)){
                s.setString(1,id);
                try(ResultSet rs = s.executeQuery()){
                    if (!rs.next()){
                        System.out.println("** 没有这篇文章！");
                        return;
                    }

                    authorId = rs.getString("author_id");
                    title = rs.getString("title");
                    content = rs.getString("content");
                    publishedAt = rs.getString("published_at");
                }
            }

            //查询点赞数量
            String likeCountSql = "select count(*) from like_relations where article_id = ?";
            try(PreparedStatement s = c.prepareStatement(likeCountSql)){
                s.setString(1,id);
                try (ResultSet rs = s.executeQuery()){
                    rs.next();
                    likeCount = rs.getInt(1);
                }
            }

            //3.查询评论信息
            List<String[]> commentList = new ArrayList<>();
            String queryCommentSql = "select user_id,content,published_at from comments where article_id = ? order by published_at desc";
            try(PreparedStatement s = c.prepareStatement(queryCommentSql)){
                s.setString(1,id);
                try (ResultSet rs = s.executeQuery()){
                    while (rs.next()){
                        String[] comment = new String[3];
                        comment[0] = rs.getString("user_id");
                        comment[1] = rs.getString("content");
                        comment[2] = rs.getString("published_at");

                        commentList.add(comment);
                    }
                }
            }

            //4.根据用户id，查询用户昵称
            //用户id来自（作者id+评论者id）
            Set<String> userIdSet = new HashSet<>();
            userIdSet.add(authorId);
            for (String[] comment:commentList) {
                userIdSet.add(comment[0]);
            }

            StringBuffer queryNicknameSql = new StringBuffer("select id,nickname from users where id in (");
            for (int i = 1; i < userIdSet.size(); i++) {
                queryNicknameSql.append("?,");
            }
            queryNicknameSql.append("?)");

            Map<String,String> userIdToNicknameMap = new HashMap<>();
            try (PreparedStatement s = c.prepareStatement(queryNicknameSql.toString())){
                int i = 1;
                for (String userId:userIdSet) {
                    s.setString(i++,userId);
                }

                try (ResultSet rs = s.executeQuery()){
                    while (rs.next()){
                        userIdToNicknameMap.put(rs.getString("id"),rs.getString("nickname"));
                    }
                }
            }

            //5.根据所有的信息进行显示
            System.out.println(title);
            System.out.println(userIdToNicknameMap.get(authorId));
            System.out.println(publishedAt);
            System.out.println("点赞人数："+likeCount);
            System.out.println(content);

            for (String[] comment:commentList) {
                System.out.println(userIdToNicknameMap.get(comment[0])+"|"+comment[1]+"|"+comment[2]);
            }

        }catch (SQLException e){
            System.out.println("错误：" + e.getMessage());
        }
    }
}
