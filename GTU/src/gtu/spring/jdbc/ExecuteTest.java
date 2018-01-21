package gtu.spring.jdbc;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

public class ExecuteTest {

    void execute(JdbcTemplate template) {
        String sql = "{call P_GET_TOPIC_NUM(?,?)}";
        Integer num = template.execute(sql, new CallableStatementCallback<Integer>() {
            @Override
            public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.setString(1, "userId");
                cs.registerOutParameter(2, Types.INTEGER);
                cs.execute();
                return cs.getInt(2);
            }
        });
    }

    void execute2(JdbcTemplate template) {
        String sql = "{? = call P_GET_TOPIC_NUM(?,?)}";
        CallableStatementCreatorFactory fac = new CallableStatementCreatorFactory(sql);
        fac.addParameter(new SqlParameter("userId", Types.INTEGER));
        fac.addParameter(new SqlOutParameter("topicNum", Types.INTEGER));
        Map<String, Integer> paramsMap = new HashMap<String, Integer>();
        paramsMap.put("userId", 11);
        CallableStatementCreator csc = fac.newCallableStatementCreator(paramsMap);
        Integer num = template.execute(csc, new CallableStatementCallback<Integer>() {
            @Override
            public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.execute();
                return cs.getInt(2);
            }
        });
    }
}
