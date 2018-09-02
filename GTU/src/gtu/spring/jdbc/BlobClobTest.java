package gtu.spring.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.FileCopyUtils;

public class BlobClobTest {

    //    <bean id="nativeJdbcExtractor" class="org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor" lazy-init="true" />
    //    <bean id="lobHandler" class="org.springframework.jdbc.support.lob.OracleLobHandler" lazy-init="true" p:nativeJdbcExtractor-ref="nativeJdbcExtractor" />

    void addPost(JdbcTemplate template) {
        String sql = "INSERT INTO t_post (post_id, user_id, post_text, post_attach) VALUES (?,?,?,?)";
        LobHandler lobHandler = new DefaultLobHandler();
        template.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                ps.setString(1, "post_id");
                ps.setString(2, "user_id");
                lobCreator.setClobAsString(ps, 3, "........");
                lobCreator.setBlobAsBytes(ps, 4, new byte[0]);
            }
        });
    }

    void getAttachs(JdbcTemplate template) {
        class Post {
            int postId;
            byte[] attach;
        }
        String sql = "SELECT post_id, post_attach, FROM t_post WHERE user_id = ? and post_attach is not null";
        final LobHandler lobHandler = new DefaultLobHandler();
        template.query(sql, new Object[] { "userId" }, new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                Post post = new Post();
                post.postId = rs.getInt(1);
                post.attach = lobHandler.getBlobAsBytes(rs, 2);
                return post;
            }
        });
    }

    @SuppressWarnings("unchecked")
    void getAttach(JdbcTemplate template, int postId, final OutputStream os) {
        String sql = "SELECT post_attach FROM t_post WHERE post_id = ? ";
        final LobHandler lobHandler = new DefaultLobHandler();
        template.query(sql, new Object[] { postId }, new AbstractLobStreamingResultSetExtractor() {
            @Override
            protected void handleNoRowFound() throws DataAccessException {
                System.out.println("Not Found reslut!");
            }

            @Override
            protected void streamData(final ResultSet rs) throws SQLException, IOException, DataAccessException {
                InputStream is = lobHandler.getBlobAsBinaryStream(rs, 1);
                if (is != null) {
                    FileCopyUtils.copy(is, os);
                }
            }
        });
    }
}
