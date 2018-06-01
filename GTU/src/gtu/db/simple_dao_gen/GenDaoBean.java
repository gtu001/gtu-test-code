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
//import gtu.db.DbConstant;
import gtu.db.tradevan.DBCommon_tradevan;
import gtu.db.tradevan.DBTypeMapping_tradevan;
import gtu.string.StringUtilForDb;

public class GenDaoBean {
    
    public static void main(String[] args) {
        Connection conn = null;
        try {
            GenDaoBean t = new GenDaoBean();
            conn = DbConstant.getTestConnection_CTBC();
            StringBuilder sb = new StringBuilder();
            
            sb.append(" SELECT                                   ");
            sb.append("     *                                    ");
            sb.append(" FROM                                     ");
            sb.append("     INEB_RATE_SUMMARY                    ");
            sb.append(" WHERE                                    ");
            sb.append("     PROD_KIND_1='外幣存款利率'           ");
            sb.append(" AND ACCT_TYPE NOT LIKE '%48'             ");
            sb.append(" ORDER BY                                 ");
            sb.append("     ACCT_TYPE,                           ");
            sb.append("     BASIS,                               ");
            sb.append("     PERIOD,                              ");
            sb.append("     INT_CAT,                             ");
            sb.append("     MINIMUM_TYPE ASC                     ");
            
            String result = t.execute(sb.toString(), "FORIGN_INEB_RATE_SUMMARY", conn);
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
        
        //JAVA Mode
        sb.append("\t//JAVA Mode\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String dbField = col.fieldName.toUpperCase();
            String parameter = StringUtilForDb.dbFieldToJava(col.fieldName);
            String information = col.fieldName + "/" + col.typeName + col.type + "/" + col.displaySize;
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();
            sb.append("\t//" + information + "\n");
            sb.append("\t private " + classPath + " " + parameter + ";\n");
        }
        
        //Orign Mode
        sb.append("\t//Orign Mode\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String dbField = col.fieldName.toUpperCase();
            String parameter = dbField.toLowerCase();
            String information = col.fieldName + "/" + col.typeName + col.type + "/" + col.displaySize;
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();
            sb.append("\t//" + information + "\n");
            sb.append("\t private " + classPath + " " + parameter + ";\n");
        }
        
        //JAVA Mode
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
        
        //Orign Mode
        //to map
        sb.append("public Map<String,Object> toMap2(){\n");
        sb.append("\tMap<String,Object> map = new LinkedHashMap<String,Object>();\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String parameter = col.fieldName.toLowerCase();
            String columnName = col.fieldName.toUpperCase();
            sb.append(String.format("\tmap.put(\"%s\", this.%s);\n", columnName, parameter));
        }
        sb.append("\treturn map;\n");
        sb.append("}\n");
        
        //to bean
        sb.append("public void toBean2(Map<String,Object> map){\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String parameter = col.fieldName.toLowerCase();
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
