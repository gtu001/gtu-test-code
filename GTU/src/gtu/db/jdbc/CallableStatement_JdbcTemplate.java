package gtu.db.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

public class CallableStatement_JdbcTemplate {

    public static void main(String[] args) {
        final String sql = "{call PKG_LS_ARAP_LTR_CATASTROPHE.p_catastrophe_holdover_batch(?,?)}";
        final Long policyId = 100L;
        JdbcTemplate t = new JdbcTemplate();
        Long result3 = t.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                CallableStatement cs = con.prepareCall(sql);
                cs.setLong(1, policyId);
                cs.registerOutParameter(2, Types.DOUBLE);
                return cs;
            }
        }, new CallableStatementCallback<Long>() {
            @Override
            public Long doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.execute();
                Long result3 = cs.getLong(2);
                return result3;
            }
        });
    }
}
