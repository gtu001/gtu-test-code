package gtu.spring.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlFunction;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.object.StoredProcedure;

public class ForumOODao {
    @Autowired
    private DataSource dataSource;

    private ForumQuery forumQuery;
    private ForumInsert forumInsert;
    private GetTopicNaum getTopicNaum;
    private SqlFunction<Integer> forumNumCount;

    @PostConstruct
    public void init() {
        this.forumQuery = new ForumQuery(dataSource);
        this.forumInsert = new ForumInsert(dataSource);
        this.getTopicNaum = new GetTopicNaum(dataSource);
        this.forumNumCount = new SqlFunction<Integer>(dataSource, "SELECT COUNT(*) FROM t_forum");
        this.forumNumCount.compile();
    }

    public int getForumNum() {
        return forumNumCount.run();
    }

    private class GetTopicNaum extends StoredProcedure {
        public GetTopicNaum(DataSource dataSource) {
            setDataSource(dataSource);
            setSql("P_GET_TOPIC_NUM");
            declareParameter(new SqlParameter("userId", Types.INTEGER));
            declareParameter(new SqlOutParameter("outNum", Types.INTEGER));
            compile();
        }

        public int getTopicNum(int userId) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("userId", userId);
            Map<String, Object> outMap = execute(map);
            return (Integer) outMap.get("outNum");
        }
    }

    private class ForumInsert extends SqlUpdate {
        public ForumInsert(DataSource ds) {
            super(ds, "INSERT INTO t_forum(forum_name, forum_desc) VALUES(:forumName, :forumDesc)");
            declareParameter(new SqlParameter("forumDesc", Types.VARCHAR));
            declareParameter(new SqlParameter("forumName", Types.VARCHAR));
            compile();
        }

        public void insert(Forum forum) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("forumName", forum.forumName);
            params.put("forumDesc", forum.forumDesc);
            super.updateByNamedParam(params);
        }
    }

    private class ForumQuery extends MappingSqlQuery<Forum> {
        public ForumQuery(DataSource dataSource) {
            super(dataSource, "SELECT forum_id, forum_name, forum_desc FROM t_forum WHERE forum_id = ?");
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        @Override
        protected Forum mapRow(ResultSet rs, int rowNum) throws SQLException {
            Forum forum = new Forum();
            forum.forumId = rs.getString("forum_id");
            forum.forumName = rs.getString("forum_name");
            forum.forumDesc = rs.getString("forum_desc");
            return forum;
        }
    }

    class Forum {
        String forumId;
        String forumName;
        String forumDesc;
    }
}
