package gtu.db.tradevan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DBQueryToDO_tradevan {

    private static final String DBColumn_tradevan_str = DBColumn_tradevan.class.getSimpleName();

    private static final DBQueryToDO_tradevan _INST = new DBQueryToDO_tradevan();

    public static DBQueryToDO_tradevan getInstance() {
        return _INST;
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("done...");
    }

    public void execute(String sql, List<String> pkList, String tableName, Connection conn, File genFile) {
        try {
            List<DBColumn> dbColmnList = queryDBColumn(sql, conn);
            if (pkList == null) {
                pkList = new ArrayList<String>();
            }
            String javaContent = generateDoClass(tableName, pkList, dbColmnList);
            System.out.println(javaContent);
            if (genFile != null) {
                saveToFile(genFile, javaContent, "utf8");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final String PARSE_DBFIELD;
    private static final String PARSE_GETTER_SETTER;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("{1}\n");
        sb.append("public static final String {0} = \"{0}\";");
        PARSE_DBFIELD = sb.toString();

        sb = new StringBuilder();
        sb.append("    public {3} get{0}() '{'                \n");
        sb.append("        return ({3})dataMap.get({1});           \n");
        sb.append("    '}'                                          \n");
        sb.append("    public void set{0}({3} {2}) '{'     \n");
        sb.append("        dataMap.put({1}, {2});             \n");
        sb.append("    '}'                                          \n");
        PARSE_GETTER_SETTER = sb.toString();
    }

    private String generateDoClass(String tableName, List<String> pkList, List<DBColumn> dbColmnList) {
        String className = _Tradevan_StringUtilsTest.capitalize(_Tradevan_StringUtilForDb.dbFieldToJava(tableName));

        StringBuilder sb = new StringBuilder();

        sb.append("public class " + className + "DO {\n");

        // table name
        sb.append("public static String TABLENAME = \"" + tableName + "\";\n");
        sb.append("private Map<String,Object> dataMap = new LinkedHashMap<String,Object>();\n");

        // column decleartion
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String dbField = col.fieldName.toUpperCase();
            String parameter = _Tradevan_StringUtilForDb.dbFieldToJava(col.fieldName);
            String getterSetter = _Tradevan_StringUtilsTest.capitalize(parameter);
            String isPkStr = isPkField(dbField, pkList) ? "true" : "false";
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();

            String information = String.format("@" + DBColumn_tradevan_str + "(typeName=\"%s\",type=%s,size=%s,pk=%s)", //
                    col.typeName, col.type, col.displaySize, isPkStr);
            sb.append(MessageFormat.format(PARSE_DBFIELD, new Object[] { dbField, information }) + "\n");
        }

        // column getter setter
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String dbField = col.fieldName.toUpperCase();
            String parameter = _Tradevan_StringUtilForDb.dbFieldToJava(col.fieldName);
            String getterSetter = _Tradevan_StringUtilsTest.capitalize(parameter);
            String information = col.fieldName + "/" + col.typeName + col.type + "/" + col.displaySize;
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();

            sb.append(MessageFormat.format(PARSE_GETTER_SETTER, new Object[] { getterSetter, dbField, parameter, classPath }) + "\n");
        }

        // toString
        sb.append(" public String toString(){\n");
        sb.append("StringBuilder sb = new StringBuilder();\n");
        for (int ii = 0; ii < dbColmnList.size(); ii++) {
            DBColumn col = dbColmnList.get(ii);
            String dbField = col.fieldName.toUpperCase();
            String parameter = _Tradevan_StringUtilForDb.dbFieldToJava(col.fieldName);
            String getterSetter = _Tradevan_StringUtilsTest.capitalize(parameter);
            String information = col.fieldName + "/" + col.typeName + col.type + "/" + col.displaySize;
            String classPath = DBTypeMapping_tradevan.JdbcTypeMappingToJava.getMappingClass(col.type).getName();
            if (ii == 0) {
                sb.append("sb.append(\"" + className + "\");\n");
            }
            sb.append(MessageFormat.format("sb.append(\", {0} = \" + this.get{1}());", new Object[] { parameter, getterSetter }) + "\n");
            if (ii == dbColmnList.size() - 1) {
                sb.append("return sb.toString();\n");
            }
        }
        sb.append("}\n");

        sb.append("}\n");
        return sb.toString();
    }

    private boolean isPkField(String fieldName, List<String> pkList) {
        if (pkList.contains(fieldName)) {
            return true;
        }
        for (String p : pkList) {
            if (_Tradevan_StringUtilsTest.equalsIgnoreCase(fieldName, p)) {
                return true;
            }
        }
        return false;
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

    private void saveToFile(File file, String content, String encode) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encode));
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class DBColumn {
        String fieldName;
        String typeName;
        int type;
        int displaySize;
    }
}
