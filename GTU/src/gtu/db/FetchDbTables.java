package gtu.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class FetchDbTables {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("done...");
    }

    //    public ResultSet getTables(String catalog,
    //            String schemaPattern,
    //            String tableNamePattern,
    //            String[] types)
    //     throws SQLException;
    //
    //平常在使用時，catalog及tableNamePattern皆設為null即可，schemaPattern 通常就是登入的帳號，types可使用的值有 --
    //"TABLE","VIEW","SYSTEM TABLE","GLOBAL TEMPORARY","LOCAL TEMPORARY", "ALIAS", "SYNONYM" 在取出的ResultSet中，含有以下的欄位值﹕
    //
    //TABLE_CAT
    //TABLE_SCHEM
    //TABLE_NAME
    //TABLE_TYPE
    //REMARKS
    //TYPE_CAT
    //TYPE_SCHEM
    //TYPE_NAME
    //SELF_REFERENCING_COL_NAME
    //REF_GENERATION
    // ###################################################################################################################
    //    public ResultSet getColumns(String catalog,
    //            String schemaPattern,
    //            String tableNamePattern,
    //            String columnNamePattern)
    //     throws SQLException

    void columnInfo(String tablePattern, Connection conn) {
        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns(null, null, tablePattern, null);
            while (rs.next()) {
                String TABLE_CAT = rs.getString("TABLE_CAT");
                String TABLE_SCHEM = rs.getString("TABLE_SCHEM");
                String TABLE_NAME = rs.getString("TABLE_NAME");
                String COLUMN_NAME = rs.getString("COLUMN_NAME");
                String DATA_TYPE = rs.getString("DATA_TYPE");
                String TYPE_NAME = rs.getString("TYPE_NAME");
                String COLUMN_SIZE = rs.getString("COLUMN_SIZE");
                String BUFFER_LENGTH = rs.getString("BUFFER_LENGTH");
                String DECIMAL_DIGITS = rs.getString("DECIMAL_DIGITS");
                String NUM_PREC_RADIX = rs.getString("NUM_PREC_RADIX");
                String NULLABLE = rs.getString("NULLABLE");
                String REMARKS = rs.getString("REMARKS");
                String COLUMN_DEF = rs.getString("COLUMN_DEF");
                String SQL_DATA_TYPE = rs.getString("SQL_DATA_TYPE");
                String SQL_DATETIME_SUB = rs.getString("SQL_DATETIME_SUB");
                String CHAR_OCTET_LENGTH = rs.getString("CHAR_OCTET_LENGTH");
                String ORDINAL_POSITION = rs.getString("ORDINAL_POSITION");
                String IS_NULLABLE = rs.getString("IS_NULLABLE");
                //                String SCOPE_CATLOG = rs.getString("SCOPE_CATLOG");
                //                String SCOPE_SCHEMA = rs.getString("SCOPE_SCHEMA");
                //                String SCOPE_TABLE = rs.getString("SCOPE_TABLE");
                //                String SOURCE_DATA_TYPE = rs.getString("SOURCE_DATA_TYPE");

                System.out.println(TABLE_CAT + " ," + //
                        TABLE_SCHEM + " ," + //
                        TABLE_NAME + " ," + //
                        COLUMN_NAME + " ," + //
                        DATA_TYPE + " ," + //
                        TYPE_NAME + " ," + //
                        COLUMN_SIZE + " ," + //
                        BUFFER_LENGTH + " ," + //
                        DECIMAL_DIGITS + " ," + //
                        NUM_PREC_RADIX + " ," + //
                        NULLABLE + " ," + //
                        REMARKS + " ," + //
                        COLUMN_DEF + " ," + //
                        SQL_DATA_TYPE + " ," + //
                        SQL_DATETIME_SUB + " ," + //
                        CHAR_OCTET_LENGTH + " ," + //
                        ORDINAL_POSITION + " ," + //
                        IS_NULLABLE + " ,"//
                        //SCOPE_CATLOG         +" ,"+//
                        //SCOPE_SCHEMA         +" ,"+//
                        //SCOPE_TABLE          +" ,"+//
                        //SOURCE_DATA_TYPE     //
                );
            }
            rs.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void test1(Connection conn) {
        try {
            DatabaseMetaData md = conn.getMetaData();
            // 取出table資料

            String[] types = { "TABLE" };
            ResultSet tableRs = md.getTables(null, "testuser", null, types);
            // ResultSet tableRs = md.getTables(null, null, "%", types);

            while (tableRs.next()) {
                String tableName = tableRs.getString("TABLE_NAME");
            }

            // 取出欄位資料
            ResultSet rsColumns = md.getColumns(null, "testuser", null, null);

            while (rsColumns.next()) {
                String tableCat = rsColumns.getString("TABLE_CAT");
                String tableName = rsColumns.getString("TABLE_NAME");
                String columnName = rsColumns.getString("COLUMN_NAME");
                String typeName = rsColumns.getString("TYPE_NAME");
                String columnSize = rsColumns.getString("COLUMN_SIZE");
            }

            tableRs.close();
            rsColumns.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
