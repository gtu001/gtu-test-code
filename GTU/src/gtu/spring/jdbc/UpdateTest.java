package gtu.spring.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class UpdateTest {

    public void update1(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("some sql", new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, "aaa");
                ps.setString(2, "bbb");
                ps.setString(3, "ccc");
                ps.setString(4, "ddd");
            }
        });
    }

    public void update_then_getPK(JdbcTemplate jdbcTemplate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection paramConnection) throws SQLException {
                PreparedStatement ps = paramConnection.prepareStatement("some sql");
                ps.setString(1, "aaa");
                ps.setString(2, "bbb");
                ps.setString(3, "ccc");
                ps.setString(4, "ddd");
                return ps;
            }
        }, keyHolder);
        Number pk = keyHolder.getKey();
    }

    public void update_batch(JdbcTemplate jdbcTemplate) {
        class Forum {
            String name;
            String desc;
        }

        final List<Forum> list = new ArrayList<Forum>();

        jdbcTemplate.batchUpdate("insert into FORUM(name,desc) values (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int paramInt) throws SQLException {
                ps.setString(1, list.get(paramInt).name);
                ps.setString(2, list.get(paramInt).desc);
            }

            @Override
            public int getBatchSize() {
                return list.size();//依次提交,非分批提交
            }
        });
    }
}
