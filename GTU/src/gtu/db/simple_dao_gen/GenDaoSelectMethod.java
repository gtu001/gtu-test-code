package gtu.db.simple_dao_gen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.JdbcUtils;

public class GenDaoSelectMethod {
    
    public String execute(String tableName, List<String> pkList) throws SQLException{
        String sql = this.createSelectSql(tableName, pkList);
        String methodResult = getQueryMethod(sql, pkList);
        System.out.println(methodResult);
        return methodResult;
    }
    
    private List<String> getColumnList(String tableName, Connection conn) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("select * from " + tableName + " where 1!=1 ");
        ResultSetMetaData metaData = stmt.getMetaData();
        List<String> colList = new ArrayList<String>();
        for (int ii = 1; ii <= metaData.getColumnCount(); ii++) {
            String columnLabel = metaData.getColumnLabel(ii);
            colList.add(columnLabel);
        }
        JdbcUtils.closeConnection(conn);
        return colList;
    }

    private String createSelectSql(String tableName, List<String> pkList) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM " + tableName + " WHERE ");
        for (String key : pkList) {
            String key_ = key.toUpperCase();
            sb.append(" " + key + "=?" + " and");
        }
        if (pkList.size() != 0) {
            sb.delete(sb.length() - 4, sb.length());
        }
        return sb.toString();
    }
    
    private String getQueryMethod(String sql, List<String> pkList) {
        StringBuilder sb = new StringBuilder();
        sb.append("    public List<Map<String, Object>> query(Map<String,Object> pkMap, Connection conn) {         \n");
        sb.append("        List<Map<String, Object>> rsList = new ArrayList<Map<String,Object>>();                                   \n");
        sb.append("        java.sql.ResultSet rs = null;                                                                             \n");
        sb.append("        PreparedStatement stmt = null;                                                                            \n");
        sb.append("        try {                                                                                                     \n");
        sb.append("            String sql = \""+sql+"\"; \n");
        sb.append("            stmt = conn.prepareStatement(sql);                                                                    \n");
        for (int ii = 0; ii < pkList.size(); ii++) {
            String colName = pkList.get(ii);
            sb.append("            stmt.setObject(" + (ii + 1) + ", pkMap.get(\"" + colName + "\")); \n");
        }
        sb.append("                                                                                                                  \n");
        sb.append("            rs = stmt.executeQuery();                                                                             \n");
        sb.append("            java.sql.ResultSetMetaData mdata = rs.getMetaData();                                                  \n");
        sb.append("            int cols = mdata.getColumnCount();                                                                    \n");
        sb.append("            List<String> colList = new ArrayList<String>();                                                       \n");
        sb.append("            for (int i = 1; i <= cols; i++) {                                                                     \n");
        sb.append("                colList.add(mdata.getColumnName(i).toUpperCase());                                                              \n");
        sb.append("            }                                                                                                     \n");
        sb.append("                                                                                                                  \n");
        sb.append("            while (rs.next()) {                                                                                   \n");
        sb.append("                Map<String, Object> map = new LinkedHashMap<String, Object>();                                    \n");
        sb.append("                for (String col : colList) {                                                                      \n");
        sb.append("                    map.put(col, rs.getObject(col));                                                              \n");
        sb.append("                }                                                                                                 \n");
        sb.append("                rsList.add(map);                                                                                  \n");
        sb.append("            }                                                                                                     \n");
        sb.append("                                                                                                                  \n");
        sb.append("        } catch (Exception e) {                                                                                \n");
        sb.append("            throw new RuntimeException(e);                                                                                \n");
        sb.append("        } finally {                                                                                               \n");
        sb.append("            try{                                                                                                  \n");
        sb.append("                rs.close();                                                                                       \n");
        sb.append("            }catch(Exception ex){                                                                                 \n");
        sb.append("                ex.printStackTrace();                                                                             \n");
        sb.append("            }                                                                                                     \n");
        sb.append("            try{                                                                                                  \n");
        sb.append("                stmt.close();                                                                                     \n");
        sb.append("            }catch(Exception ex){                                                                                 \n");
        sb.append("                ex.printStackTrace();                                                                             \n");
        sb.append("            }                                                                                                     \n");
        sb.append("        }                                                                                                         \n");
        sb.append("        return rsList;                                                                                            \n");
        sb.append("    }                                                                                                             \n");
        return sb.toString();
    }
}
