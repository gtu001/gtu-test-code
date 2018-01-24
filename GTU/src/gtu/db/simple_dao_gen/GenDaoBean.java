package gtu.db.simple_dao_gen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import gtu.db.DbConstant;
import gtu.db.tradevan.DBCommon_tradevan;
import gtu.db.tradevan.DBTypeMapping_tradevan;
import gtu.string.StringUtilForDb;

public class GenDaoBean {
    
    public static void main(String[] args) {
        Connection conn = null;
        try {
            GenDaoBean t = new GenDaoBean();
            conn = DbConstant.getTestDataSource_FucoOracle().getConnection();
            
            StringBuilder sb = new StringBuilder();
            sb.append(" select a.adprjid,                                 ");
            sb.append(" a.adprjname,                                      ");
            sb.append(" b.BlockCode,                                      ");
            sb.append(" b.BlockName,                                      ");
            sb.append(" a.sTime,                                          ");
            sb.append(" a.eTime,                                          ");
            sb.append(" a.creater,                                        ");
            sb.append(" a.approver,                                       ");
            sb.append(" a.approver2,                                      ");
            sb.append(" a.status,                                         ");
            sb.append(" a.status2,                                        ");
            sb.append(" a.ctime                                           ");
            sb.append(" from adCaseData a                                 ");
            sb.append(" join BlockMap b on a.BlockCode = b.BlockCode      ");
            sb.append(" order by a.ctime desc                             ");
            
            String result = t.execute(sb.toString(), "AD_LIST_VO", conn);
            System.out.println(result);
        }catch(Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (Exception e) {
            }
        }finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    public String execute(String sql, String tableName, Connection conn) {
        try {
            tableName = StringUtils.trimToEmpty(tableName);
            List<DBColumn> dbColmnList = queryDBColumn(sql, conn);
            String className = StringUtils.capitalise(StringUtilForDb.dbFieldToJava(tableName));
            StringBuilder sb = new StringBuilder();
            sb.append("public class " + className + "{\n");
            String javaContent = generateDoClass(tableName, dbColmnList);
            sb.append(javaContent);
            sb.append("}\n");
            System.out.println(sb);
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<DBColumn> queryDBColumn(String sql, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            List<DBColumn> list = new ArrayList<DBColumn>();
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("---------------------------");
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int ii = 1; ii <= rsmd.getColumnCount(); ii++) {
                DBColumn dbCol = new DBColumn();
                dbCol.fieldName = rsmd.getColumnLabel(ii);
                dbCol.typeName = rsmd.getColumnTypeName(ii);
                dbCol.type = rsmd.getColumnType(ii);
                dbCol.displaySize = rsmd.getColumnDisplaySize(ii);
                System.out.println(dbCol.fieldName);
                System.out.println(dbCol.typeName);
                System.out.println(dbCol.type);
                System.out.println(dbCol.displaySize);
                System.out.println("---------------------------");
                list.add(dbCol);
            }
            return list;
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            DBCommon_tradevan.closeConnection(rs, pstmt, con);
        }
    }

    private String generateDoClass(String tableName, List<DBColumn> dbColmnList) {
        StringBuffer sb = new StringBuffer();
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String dbField = col.fieldName.toUpperCase();
            String parameter = StringUtilForDb.dbFieldToJava(col.fieldName);
            String information = col.fieldName + "/" + col.typeName + col.type + "/" + col.displaySize;
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();
            sb.append("\t//" + information + "\n");
            sb.append("\t" + classPath + " " + parameter + ";\n");
        }
        
        //to map
        sb.append("public Map<String,Object> toMap(){\n");
        sb.append("\tMap<String,Object> map = new LinkedHashMap<String,Object>();\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String parameter = StringUtilForDb.dbFieldToJava(col.fieldName);
            String columnName = col.fieldName.toUpperCase();
            sb.append(String.format("\tmap.put(\"%s\", this.%s);\n", columnName, parameter));
        }
        sb.append("\treturn map;\n");
        sb.append("}\n");
        
        //to bean
        sb.append("public void toBean(Map<String,Object> map){\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String parameter = StringUtilForDb.dbFieldToJava(col.fieldName);
            String columnName = col.fieldName.toUpperCase();
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();
            sb.append(String.format("\tthis.%s = (%s)map.get(\"%s\");\n", parameter, classPath, columnName));
        }
        sb.append("}\n");
        return sb.toString();
    }

    private static class DBColumn {
        String fieldName;
        String typeName;
        int type;
        int displaySize;
    }
}
