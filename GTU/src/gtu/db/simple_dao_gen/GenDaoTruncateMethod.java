package gtu.db.simple_dao_gen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class GenDaoTruncateMethod {

    public String execute(String tableName) throws SQLException {
        tableName = StringUtils.trimToEmpty(tableName);
        String methodResult = getTruncateSql(tableName);
        System.out.println(methodResult);
        return methodResult;
    }

    private String getTruncateSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("    public boolean truncate(Connection conn) {                              \n");
        sb.append("        PreparedStatement stmt = null;                                      \n");
        sb.append("        try {                                                               \n");
        sb.append("            conn.setAutoCommit(false);                                      \n");
        sb.append("            String sql = \"truncate table " + tableName + " \";           \n");
        sb.append("            stmt = conn.prepareStatement(sql);                              \n");
        sb.append("                                                                            \n");
        sb.append("            boolean result = stmt.execute();                                \n");
        sb.append("            System.out.println(\"truncate result : \" + result);              \n");
        sb.append("            // conn.commit();                                               \n");
        sb.append("            return result;                                                  \n");
        sb.append("        } catch (Exception e) {                                             \n");
        sb.append("            e.printStackTrace();                                            \n");
        sb.append("            try {                                                           \n");
        sb.append("                conn.rollback();                                            \n");
        sb.append("            } catch (Exception e1) {                                        \n");
        sb.append("                e1.printStackTrace();                                       \n");
        sb.append("            }                                                               \n");
        sb.append("            throw new RuntimeException(e);                                  \n");
        sb.append("        } finally {                                                         \n");
        sb.append("            try {                                                           \n");
        sb.append("                stmt.close();                                               \n");
        sb.append("            } catch (Exception ex) {                                        \n");
        sb.append("                ex.printStackTrace();                                       \n");
        sb.append("            }                                                               \n");
        sb.append("        }                                                                   \n");
        sb.append("    }                                                                       \n");
        return sb.toString();
    }
}
