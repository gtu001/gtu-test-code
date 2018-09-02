package gtu.db.sqlMaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import gtu.db.DBColumnType;
import gtu.file.FileUtil;

public class DbSqlCreater {

    BasicDataSource dbs;

    public DbSqlCreater(String driver, String url, String username, String password) {
        super();
        dbs = new BasicDataSource();
        dbs.setDriverClassName(driver);
        dbs.setUrl(url);
        dbs.setUsername(username);
        dbs.setPassword(password);
        dbs.setMaxActive(100);
        dbs.setMinIdle(30);
        // try {
        // File jarFile = new File(PropertiesUtil.getJarCurrentPath(getClass()),
        // "sqljdbc.jar");
        // URLClassLoader classLoader = URLClassLoader.newInstance(new
        // URL[]{jarFile.toURL()}, this.getClass().getClassLoader());
        // classLoader.loadClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        // dbs.setDriverClassLoader(classLoader);
        // } catch (Exception e) {
        // throw new RuntimeException(e);
        // }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // String url = "jdbc:oracle:thin:@172.31.70.50:1533:T13A";
        // String username = "pspismgr";
        // String password = "iEEcTkBvpH";
        String url = "jdbc:oracle:thin:@ploodse-scan.transglobe.com.tw:1521/ebaosit3";
        String username = "ls_sit3_read";
        String password = "ls_sit3_read_only";
        String driver = "oracle.jdbc.driver.OracleDriver";
        DbSqlCreater janna = new DbSqlCreater(driver, url, username, password);
        // janna.execute("EDI5105T4");

        Set<String> pkSet = new HashSet<String>();
        pkSet.add("POLICY_ID");
        janna.execute("t_contract_master", "rownum < 500", pkSet);
        System.out.println("done...");
    }

    public TableInfo execute(String tableName, String whereCondition, Set<String> pkSet) {
        try {
            TableInfo info = new TableInfo();
            info.execute("select * from " + tableName + " where rownum = 1 ", dbs.getConnection());

            if (StringUtils.isBlank(info.tableName)) {
                info.tableName = tableName;// XXX
            }
            if (info.pkColumns.isEmpty()) {
                info.pkColumns = pkSet;// XXX
            }

            XStream xstream = new XStream(new DomDriver());
            xstream.alias("tableInfo", TableInfo.class);
            xstream.alias("fieldInfo", FieldInfo4DbSqlCreater.class);

            File mkdir = new File(FileUtil.DESKTOP_PATH, "CAC前代DB");
            mkdir.mkdirs();

            // table schema xml
            File schemaFile = new File(mkdir, tableName + "_schema.xml");
            FileWriter swriter = new FileWriter(schemaFile);
            ObjectOutputStream out = xstream.createObjectOutputStream(swriter);
            out.writeObject(info);
            out.flush();
            out.close();

            // table csv
            File dataFile = new File(mkdir, tableName + "_data.csv");
            exportToCsv("select * from " + tableName + " where " + whereCondition, dbs.getConnection(), dataFile);

            // create insert sql
            File sqlFile1 = new File(mkdir, tableName + "_insert.sql");
            _createSQLFile("select * from " + tableName + " where " + whereCondition, dbs.getConnection(), sqlFile1, info, 'i');
            File sqlFile2 = new File(mkdir, tableName + "_update.sql");
            _createSQLFile("select * from " + tableName + " where " + whereCondition, dbs.getConnection(), sqlFile2, info, 'u');
            File sqlFile3 = new File(mkdir, tableName + "_delete.sql");
            _createSQLFile("select * from " + tableName + " where " + whereCondition, dbs.getConnection(), sqlFile3, info, 'd');

            System.out.println("done...");
            return info;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static class TableInfo {

        Set<String> noNullsCol = new HashSet<String>();
        Set<String> numberCol = new HashSet<String>();
        Set<String> columns = new TreeSet<String>();
        Set<String> pkColumns = new TreeSet<String>();
        Map<String, FieldInfo4DbSqlCreater> columnInfo = new LinkedHashMap<String, FieldInfo4DbSqlCreater>();
        String tableName;
        String schemaName;
        String catalogName;

        /**
         * 取得table格式,可能會有數職欄位或非null欄位
         */
        public void execute(String sql, Connection conn) {
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = conn.createStatement();
                System.out.println("<<<<<<<" + sql);
                rs = stmt.executeQuery(sql);

                if (conn instanceof oracle.jdbc.driver.OracleConnection) {
                    ((oracle.jdbc.driver.OracleConnection) conn).setRemarksReporting(true);
                }

                ResultSetMetaData meta = rs.getMetaData();
                String colLabel = null;
                for (int ii = 1; ii <= meta.getColumnCount(); ii++) {
                    colLabel = meta.getColumnLabel(ii).toLowerCase();
                    if (tableName == null) {
                        tableName = meta.getTableName(ii);
                    }
                    if (schemaName == null) {
                        schemaName = meta.getSchemaName(ii);
                    }
                    if (catalogName == null) {
                        catalogName = meta.getCatalogName(ii);
                    }
                    if (ResultSetMetaData.columnNoNulls == meta.isNullable(ii)) {
                        noNullsCol.add(colLabel);
                    }
                    if (DBColumnType.isNumber(meta.getColumnType(ii))) {
                        numberCol.add(colLabel);
                    }

                    FieldInfo4DbSqlCreater finfo = new FieldInfo4DbSqlCreater();
                    finfo.setCatalogName(meta.getCatalogName(ii));
                    finfo.setColumnClassName(meta.getColumnClassName(ii));
                    finfo.setColumnDisplaySize(meta.getColumnDisplaySize(ii));
                    finfo.setColumnLabel(meta.getColumnLabel(ii));
                    finfo.setColumnName(meta.getColumnName(ii));
                    finfo.setColumnType(meta.getColumnType(ii));
                    finfo.setColumnTypeName(meta.getColumnTypeName(ii));
                    finfo.setPrecision(meta.getPrecision(ii));
                    finfo.setScale(meta.getScale(ii));
                    finfo.setSchemaName(meta.getSchemaName(ii));
                    finfo.setTableName(meta.getTableName(ii));
                    finfo.setAutoIncrement(meta.isAutoIncrement(ii));
                    finfo.setCaseSensitive(meta.isCaseSensitive(ii));
                    finfo.setCurrency(meta.isCurrency(ii));
                    finfo.setDefinitelyWritable(meta.isDefinitelyWritable(ii));
                    finfo.setNullable(meta.isNullable(ii));
                    finfo.setReadOnly(meta.isReadOnly(ii));
                    finfo.setSearchable(meta.isSearchable(ii));
                    finfo.setSigned(meta.isSigned(ii));
                    finfo.setWritable(meta.isWritable(ii));
                    finfo.setColumnTypeZ(DBColumnType.lookup(meta.getColumnType(ii)));
                    columnInfo.put(colLabel, finfo);

                    columns.add(colLabel);
                }
                tableName = StringUtils.defaultIfBlank(tableName, null);
                schemaName = StringUtils.defaultIfBlank(schemaName, null);
                catalogName = StringUtils.defaultIfBlank(catalogName, null);

                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet pk = dbmd.getPrimaryKeys(catalogName, schemaName, tableName);
                System.out.println("============================================================pk start");
                while (pk.next()) {
                    Map<String, String> pkMap = getResultSetToMap(pk);
                    System.out.println(pkMap);
                    System.out.println(pkMap);
                }
                System.out.println("============================================================pk end");
                // ResultSet rs2 = dbmd.getIndexInfo(catalogName, schemaName,
                // tableName, false, true);
                // // unique - 該參數為 true 時，僅返回唯一值的索引；該參數為 false
                // 時，返回所有索引，不管它們是否唯一
                // // approximate - 該參數為 true 時，允許結果是接近的資料值或這些資料值以外的值；該參數為 false
                // // 時，要求結果是精確結果
                // System.out.println("============================================================index
                // start");
                // while (rs2.next()) {
                // Map<String, String> indexMap = getResultSetToMap(rs2);
                // System.out.println(indexMap);
                // }
                // System.out.println("============================================================index
                // end");
                System.out.println("資料表pk =>" + pkColumns);
                // ResultSet t = dbmd.getCatalogs();
                // while (t.next()) {
                // System.out.println(t.getString(0));
                // }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                // JdbcUtils.closeConnection(conn);
                // JdbcUtils.closeStatement(stmt);
                // JdbcUtils.closeResultSet(rs);
                this.close(conn, stmt, rs);
            }
        }

        private void close(Connection conn, Statement stmt, ResultSet rs) {
            try {
                conn.close();
            } catch (Exception e) {
            }
            try {
                stmt.close();
            } catch (Exception e) {
            }
            try {
                rs.close();
            } catch (Exception e) {
            }
        }

        private void validateData() {
            Validate.notEmpty(tableName, "tableName為空");
            Validate.notEmpty(columns, "columns欄位為空");
            Validate.notEmpty(pkColumns, "pkColumns欄位為空");
        }

        /**
         * 傳入要建立的新增資料
         */
        public String createInsertSql(Map<String, String> valmap) {
            validateData();
            StringBuilder sb = new StringBuilder();
            String value = null;
            sb.append(String.format("INSERT INTO %s  (", tableName));
            for (String key : columns) {
                sb.append(key + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") VALUES ( ");
            for (String key : columns) {
                String key_ = key.toUpperCase();
                value = valmap.get(key_);
                sb.append(this.getRealValue(key, value) + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(") ");
            // sb.append("; ");
            return sb.toString();
        }

        /**
         * 傳入要建立的更新資料
         */
        public String createUpdateSql(Map<String, String> valmap, Map<String, String> pkValMap, boolean ignoreNull) {
            validateData();
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE " + tableName + " SET ");
            for (String key : columns) {
                String key_ = key.toUpperCase();
                if (StringUtils.isBlank(valmap.get(key_)) && ignoreNull) {
                    continue;
                }
                sb.append(key + "=" + getRealValue(key, valmap.get(key_)) + ",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(" WHERE ");
            System.out.println("pkValMap == " + pkValMap);
            for (String key : pkValMap.keySet()) {
                String key_ = key.toUpperCase();
                sb.append(" " + key + "=" + getRealValue(key, pkValMap.get(key_)) + " and");
            }
            if (pkColumns.size() != 0) {
                sb.delete(sb.length() - 4, sb.length());
            }
            // sb.append(";");
            return sb.toString();
        }

        /**
         * 傳入要建立的查詢資料
         */
        public String createSelectSql(Map<String, String> valmap) {
            validateData();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM " + tableName + " WHERE ");
            for (String key : pkColumns) {
                String key_ = key.toUpperCase();
                sb.append(" " + key + "=" + getRealValue(key, valmap.get(key_)) + " and");
            }
            if (pkColumns.size() != 0) {
                sb.delete(sb.length() - 4, sb.length());
            }
            // sb.append(";");
            return sb.toString();
        }

        /**
         * 傳入要建立的刪除資料
         */
        public String createDeleteSql(Map<String, String> valmap) {
            validateData();
            StringBuilder sb = new StringBuilder();
            sb.append("DELETE FROM " + tableName + " WHERE ");
            for (String key : pkColumns) {
                String key_ = key.toUpperCase();
                sb.append(" " + key + "=" + getRealValue(key, valmap.get(key_)) + " and");
            }
            if (pkColumns.size() != 0) {
                sb.delete(sb.length() - 4, sb.length());
            }
            // sb.append(";");
            return sb.toString();
        }

        private String getRealValue(String key_, String value) {
            if (value == null) {
                if (numberCol.contains(key_)) {
                    return "0";
                } else if (noNullsCol.contains(key_)) {
                    return "''";
                } else {
                    return "null";
                }
            } else {
                // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                if (value.equalsIgnoreCase("null")) {
                    return "null";
                }
                // 日期格式//2001-03-16 00:00:00.0
                Pattern dateFormat = Pattern.compile("([12]\\d{3})\\-(\\d{2})\\-(\\d{2})\\s(\\d{2})\\:(\\d{2})\\:(\\d{2})\\.\\d+");
                Matcher mthDate = dateFormat.matcher(value);
                if (mthDate.matches()) {
                    String yyyy = mthDate.group(1);
                    String mm = mthDate.group(2);
                    String dd = mthDate.group(3);
                    String hh = mthDate.group(4);
                    String MM = mthDate.group(5);
                    String ss = mthDate.group(6);
                    String rtnVal = String.format("to_date('%s', 'yyyy.mm.dd.HH24.MI.SS')", yyyy + "." + mm + "." + dd + "." + hh + "." + MM + "." + ss);
                    return rtnVal;
                }
                if (value.startsWith("#date(")) {
                    Pattern ptn = Pattern.compile("\\#date\\((\\d+)\\)");
                    Matcher mth = ptn.matcher(value);
                    if (mth.matches()) {
                        String yyyyMMdd = mth.group(1);
                        return " to_date('" + yyyyMMdd + "', 'yyyymmdd') ";
                    }
                }
                if (value.startsWith("#date(")) {
                    Pattern ptn = Pattern.compile("\\#date\\((.*)\\)");
                    Matcher mth = ptn.matcher(value);
                    if (mth.matches()) {
                        String sysdate = mth.group(1);
                        return sysdate;
                    }
                }
                if (value.startsWith("#seq(")) {
                    Pattern ptn = Pattern.compile("\\#seq\\((.*)\\)");
                    Matcher mth = ptn.matcher(value);
                    if (mth.matches()) {
                        String seqText = mth.group(1);
                        return seqText;
                    }
                }
                // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                if (numberCol.contains(key_)) {
                    int numVal = 0;
                    try {
                        numVal = Integer.parseInt(value);
                    } catch (Exception ex) {
                        System.out.println("轉型數值失敗 => [" + key_ + "]=[" + value + "]");
                        throw new RuntimeException(ex);
                    }
                    return String.valueOf(numVal);
                } else {
                    return String.format("'%s'", value);
                }
            }
        }

        public Set<String> getNoNullsCol() {
            return noNullsCol;
        }

        public void setNoNullsCol(Set<String> noNullsCol) {
            this.noNullsCol = noNullsCol;
        }

        public Set<String> getNumberCol() {
            return numberCol;
        }

        public void setNumberCol(Set<String> numberCol) {
            this.numberCol = numberCol;
        }

        public Set<String> getColumns() {
            return columns;
        }

        public void setColumns(Set<String> columns) {
            this.columns = columns;
        }

        public Set<String> getPkColumns() {
            return pkColumns;
        }

        public void setPkColumns(Set<String> pkColumns) {
            this.pkColumns = pkColumns;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public Map<String, FieldInfo4DbSqlCreater> getColumnInfo() {
            return columnInfo;
        }

        public void setColumnInfo(Map<String, FieldInfo4DbSqlCreater> columnInfo) {
            this.columnInfo = columnInfo;
        }

        public String getSchemaName() {
            return schemaName;
        }

        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }

        public String getCatalogName() {
            return catalogName;
        }

        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }
    }

    public static Set<String> queryColumn(String tablePattern, Connection conn) {
        Set<String> set = new HashSet<String>();
        ResultSet rs = null;
        try {
            DatabaseMetaData md = conn.getMetaData();
            rs = md.getColumns(null, null, tablePattern, null);
            while (rs.next()) {
                set.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeResultSet(rs);
        }
        return set;
    }

    private void _createSQLFile(String sql, Connection conn, File file, TableInfo info, char updateType) {
        Statement stmt = null;
        ResultSet rs = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Map<String, String> map = getResultSetToMap(rs);
                String genSql = "";
                switch (updateType) {
                case 'i':
                    genSql = info.createInsertSql(map);
                    break;
                case 'u':
                    Map<String, String> pkMap = new HashMap<String, String>();
                    for (String col : info.pkColumns) {
                        String col_ = col.toUpperCase();
                        String val = map.get(col_);
                        pkMap.put(col_, val);
                    }
                    genSql = info.createUpdateSql(map, pkMap, false);
                    break;
                case 'd':
                    genSql = info.createDeleteSql(map);
                    break;
                }

                // 結尾符號
                if (!genSql.trim().endsWith(";")) {
                    genSql = genSql + ";";
                }

                writer.write(genSql);
                writer.newLine();
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public static void exportToCsv(String sql, Connection conn, File file) {
        Statement stmt = null;
        ResultSet rs = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            boolean first = true;
            while (rs.next()) {
                Map<String, String> map = getResultSetToMap(rs);
                if (first) {
                    writer.write(map.keySet().toString().replaceAll("[\\[\\]]", ""));
                    writer.newLine();
                    first = false;
                } else {
                    writer.write(map.values().toString().replaceAll("[\\[\\]]", ""));
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public static List<Map<String, String>> querySql(String sql, Connection conn) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(getResultSetToMap(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
        return list;
    }

    public static class FieldInfo4DbSqlCreater {
        String catalogName;
        String columnClassName;
        int columnDisplaySize;
        String columnLabel;
        String columnName;
        int columnType;
        String columnTypeName;
        int precision;
        int scale;
        String schemaName;
        String tableName;
        boolean autoIncrement;
        boolean caseSensitive;
        boolean currency;
        boolean definitelyWritable;
        int nullable;
        boolean readOnly;
        boolean searchable;
        boolean signed;
        boolean writable;
        DBColumnType columnTypeZ;

        public String getCatalogName() {
            return catalogName;
        }

        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }

        public String getColumnClassName() {
            return columnClassName;
        }

        public void setColumnClassName(String columnClassName) {
            this.columnClassName = columnClassName;
        }

        public int getColumnDisplaySize() {
            return columnDisplaySize;
        }

        public void setColumnDisplaySize(int columnDisplaySize) {
            this.columnDisplaySize = columnDisplaySize;
        }

        public String getColumnLabel() {
            return columnLabel;
        }

        public void setColumnLabel(String columnLabel) {
            this.columnLabel = columnLabel;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getColumnType() {
            return columnType;
        }

        public void setColumnType(int columnType) {
            this.columnType = columnType;
        }

        public String getColumnTypeName() {
            return columnTypeName;
        }

        public void setColumnTypeName(String columnTypeName) {
            this.columnTypeName = columnTypeName;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public String getSchemaName() {
            return schemaName;
        }

        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public boolean isAutoIncrement() {
            return autoIncrement;
        }

        public void setAutoIncrement(boolean autoIncrement) {
            this.autoIncrement = autoIncrement;
        }

        public boolean isCaseSensitive() {
            return caseSensitive;
        }

        public void setCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        public boolean isCurrency() {
            return currency;
        }

        public void setCurrency(boolean currency) {
            this.currency = currency;
        }

        public boolean isDefinitelyWritable() {
            return definitelyWritable;
        }

        public void setDefinitelyWritable(boolean definitelyWritable) {
            this.definitelyWritable = definitelyWritable;
        }

        public int getNullable() {
            return nullable;
        }

        public void setNullable(int nullable) {
            this.nullable = nullable;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        public void setReadOnly(boolean readOnly) {
            this.readOnly = readOnly;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

        public boolean isSigned() {
            return signed;
        }

        public void setSigned(boolean signed) {
            this.signed = signed;
        }

        public boolean isWritable() {
            return writable;
        }

        public void setWritable(boolean writable) {
            this.writable = writable;
        }

        public DBColumnType getColumnTypeZ() {
            return columnTypeZ;
        }

        public void setColumnTypeZ(DBColumnType columnTypeZ) {
            this.columnTypeZ = columnTypeZ;
        }

        @Override
        public String toString() {
            return "FieldInfo [catalogName=" + catalogName + ", columnClassName=" + columnClassName + ", columnDisplaySize=" + columnDisplaySize + ", columnLabel=" + columnLabel + ", columnName="
                    + columnName + ", columnType=" + columnType + ", columnTypeName=" + columnTypeName + ", precision=" + precision + ", scale=" + scale + ", schemaName=" + schemaName + ", tableName="
                    + tableName + ", autoIncrement=" + autoIncrement + ", caseSensitive=" + caseSensitive + ", currency=" + currency + ", definitelyWritable=" + definitelyWritable + ", nullable="
                    + nullable + ", readOnly=" + readOnly + ", searchable=" + searchable + ", signed=" + signed + ", writable=" + writable + ", columnTypeZ=" + columnTypeZ + "]";
        }
    }

    private static Map<String, String> getResultSetToMap(ResultSet rs) {
        Map<String, String> treeMap = new TreeMap<String, String>();
        try {
            ResultSetMetaData meta = rs.getMetaData();
            for (int ii = 1; ii <= meta.getColumnCount(); ii++) {
                String columnLabel = meta.getColumnLabel(ii);
                treeMap.put(columnLabel, rs.getString(columnLabel));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return treeMap;
    }
}
