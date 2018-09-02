package gtu.db.jdbc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PreparedStatementAppender {

    private static Logger logger = Logger.getLogger(PreparedStatementAppender.class);

    public static PreparedStatementAppender newInstance() {
        return new PreparedStatementAppender();
    }

    private PreparedStatementAppender() {
    }

    private StringBuilder sqlSb = new StringBuilder();
    private List<ActionListener> paramsAppenderLst = new ArrayList<ActionListener>();

    public PreparedStatementAppender append(String sql) {
        sqlSb.append(sql);
        return this;
    }

    public PreparedStatementAppender append(final String sql, final int sqlPos, final Object val) {
        sqlSb.append(sql);
        paramsAppenderLst.add(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                PreparedStatement stmt = (PreparedStatement) arg0.getSource();
                try {
                    stmt.setObject(sqlPos, val);
                    logger.info(String.format("Param[%d] : %s", sqlPos, val));
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        });
        return this;
    }

    public String getSql() {
        String rtnSql = sqlSb.toString();
        logger.info("SQL : " + rtnSql);
        return rtnSql;
    }

    public void applyParams(PreparedStatement stmt) {
        for (ActionListener act : paramsAppenderLst) {
            act.actionPerformed(new ActionEvent(stmt, -1, "test"));
        }
    }
}
