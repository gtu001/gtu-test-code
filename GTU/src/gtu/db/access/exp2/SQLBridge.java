package gtu.db.access.exp2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.servlet.ServletException;

/**
 * @author Troy 2009/02/02
 * 
 */
public class SQLBridge {
    private ConnPool connPool;
    private Connection conn;
    private ResultSet rs;
    private ResultSetMetaData rsmd;
    private Statement stmt;
    private String driverName;
    private String jdbcURL;
    private String username;
    private String password;

    private int pageSize; // 設定每張網頁顯示幾筆記錄
    private int showPage = 1; // 設定欲顯示的頁數
    private int pageCount; // ResultSet分頁後的總頁數
    private int rowCount; // ResultSet的記錄筆數

    public SQLBridge() {
        connPool = null;
        conn = null;
        rs = null;
        rsmd = null;
        stmt = null;
    }

    private void clearResult() throws SQLException {
        if (rs != null)
            rs.close();
        rs = null;
        if (stmt != null)
            stmt.close();
        stmt = null;
        rsmd = null;
    }

    public void closeDB() throws SQLException {
        clearResult();
        if (connPool != null) {
            connPool.returnConnection();
            connPool = null;
        } else {
            if (conn == null)
                throw new SQLException("This connection has been closed already.");
            if (conn.isClosed())
                throw new SQLException("This connection has been closed.");
            conn.close();
        }
        conn = null;
    }

    public int execSQL(String sqlStmt) throws SQLException {
        if (conn == null || conn.isClosed())
            throw new SQLException("This connection has not been established yet.");
        if (sqlStmt == null)
            throw new SQLException("SQL-statement is null.");
        clearResult();
        conn.setAutoCommit(true);
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        // 建立Statement物件, 並設定記錄指標類型為可前後移動
        if (sqlStmt.toUpperCase().startsWith("SELECT")) {
            rs = stmt.executeQuery(sqlStmt);
            rsmd = rs.getMetaData();
            return -1;
        } else {
            int numRow = stmt.executeUpdate(sqlStmt);
            clearResult();
            return numRow;
        }
    }

    public void execUpdate(String[] sqlStmts) throws SQLException {
        if (conn == null || conn.isClosed())
            throw new SQLException("The connection has not been established yet.");
        if (sqlStmts == null || sqlStmts.length == 0)
            throw new SQLException("SQL-statement is null.");
        clearResult();
        conn.setAutoCommit(false);
        try {
            for (int i = 0; i < sqlStmts.length; i++) {
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                // 建立Statement物件, 並設定記錄指標類型為可前後移動
                stmt.executeUpdate(sqlStmts[i]);
                stmt.close();
            }
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        }
    }

    public int getColumnCount() throws SQLException {
        if (rsmd == null)
            throw new SQLException("ResultSet is null.");
        return rsmd.getColumnCount();
    }

    public String[] getColumnNames() throws SQLException {
        if (rsmd == null)
            throw new SQLException("ResultSet is null.");
        String[] columnNames = new String[getColumnCount()];
        for (int i = 1; i <= columnNames.length; i++)
            columnNames[i - 1] = rsmd.getColumnName(i);
        return columnNames;
    }

    protected Object getField(int column, boolean convertToString) throws SQLException {
        if (rs == null || rsmd == null)
            throw new SQLException("ResultSet is null.");

        switch (rsmd.getColumnType(column)) {
        case Types.BIGINT:
            if (convertToString)
                return String.valueOf(rs.getLong(column));
            else
                return new Long(rs.getLong(column));

        case Types.BINARY:
            if (convertToString)
                return Byte.toString(rs.getByte(column));
            else
                return new Byte(rs.getByte(column));

        case Types.BIT:
            if (convertToString)
                return String.valueOf(rs.getBoolean(column));
            else
                return new Boolean(rs.getBoolean(column));

        case Types.CHAR:
            return rs.getString(column);

        case Types.DATE:
            if (convertToString)
                return (rs.getDate(column)).toString();
            else
                return rs.getDate(column);

        case Types.DECIMAL:
            if (convertToString)
                return (rs.getBigDecimal(column, rsmd.getScale(column))).toString();
            else
                return rs.getBigDecimal(column, rsmd.getScale(column));

        case Types.DOUBLE:
            if (convertToString)
                return String.valueOf(rs.getDouble(column));
            else
                return new Double(rs.getDouble(column));

        case Types.FLOAT:
            if (convertToString)
                return String.valueOf(rs.getDouble(column));
            else
                return new Float(rs.getDouble(column));

        case Types.INTEGER:
            if (convertToString)
                return String.valueOf(rs.getInt(column));
            else
                return new Integer(rs.getInt(column));

        case Types.LONGVARBINARY:
            if (convertToString)
                return (rs.getBinaryStream(column)).toString();
            else
                return rs.getBinaryStream(column);

        case Types.LONGVARCHAR:
            return rs.getString(column);

        case Types.NULL:
            if (convertToString)
                return "NULL";
            else
                return null;

        case Types.NUMERIC:
            if (convertToString)
                return (rs.getBigDecimal(column, rsmd.getScale(column))).toString();
            else
                return rs.getBigDecimal(column, rsmd.getScale(column));

        case Types.REAL:
            if (convertToString)
                return String.valueOf(rs.getFloat(column));
            else
                return new Float(rs.getFloat(column));

        case Types.SMALLINT:
            if (convertToString)
                return String.valueOf(rs.getShort(column));
            else
                return new Short(rs.getShort(column));

        case Types.TIME:
            if (convertToString)
                return (rs.getTime(column)).toString();
            else
                return rs.getTime(column);

        case Types.TIMESTAMP:
            if (convertToString)
                return (rs.getTimestamp(column)).toString();
            else
                return rs.getTimestamp(column);

        case Types.TINYINT:
            if (convertToString)
                return String.valueOf(rs.getByte(column));
            else
                return new Byte(rs.getByte(column));

        case Types.VARBINARY:
            if (convertToString)
                return (rs.getBytes(column)).toString();
            else
                return rs.getBytes(column);

        case Types.VARCHAR:
            return rs.getString(column);

        default:
            if (convertToString)
                return (rs.getObject(column)).toString();
            else
                return rs.getObject(column);
        }
    }

    public Object getField(int column) throws SQLException {
        return getField(column, false);
    }

    public Object getField(String fieldName) throws SQLException {
        return getField(rs.findColumn(fieldName), false);
    }

    public String getFieldString(int column) throws SQLException {
        return (String) getField(column, true);
    }

    public String getFieldString(String fieldName) throws SQLException {
        return (String) getField(rs.findColumn(fieldName), true);
    }

    public boolean nextRow() throws SQLException {
        if (rs == null)
            throw new SQLException("ResultSet is null.");
        return rs.next();
    }

    // ----------------------------------------------------------------
    public boolean lastRow() throws SQLException {
        if (rs == null)
            throw new SQLException("ResultSet is null.");
        return rs.last();
    }

    public boolean firstRow() throws SQLException {
        if (rs == null)
            throw new SQLException("ResultSet is null.");
        return rs.first();
    }

    public int getRowCount() throws SQLException {
        if (rs == null)
            throw new SQLException("ResultSet is null.");
        return rs.getRow();
    }

    public boolean absoluteRow(int location) throws SQLException {
        if (rs == null)
            throw new SQLException("ResultSet is null.");
        return rs.absolute(location);
    }

    public void page(String toPage) throws SQLException {
        if (rs == null)
            throw new SQLException("ResultSet is null.");
        rs.last();
        rowCount = rs.getRow();
        pageCount = ((rowCount % pageSize) == 0 ? (rowCount / pageSize) : (rowCount / pageSize) + 1);

        // 判斷是否可正確取得ToPage參數,
        // 可取得則表示JSP網頁應顯示特定分頁記錄的敘述

        if (toPage != null) {
            showPage = Integer.parseInt(toPage);
        }
        rs.absolute((showPage - 1) * pageSize + 1);
    }

    // ------------------------------------------------------------------------
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getShowPage() {
        return showPage;
    }

    // -------------------------------------------------------------------------
    public void openDB(String drvName, String url, String uname, String passwd) throws SQLException {
        if (conn != null && !conn.isClosed())
            throw new SQLException("The connection has been established already.");
        clearResult();
        try {
            Class.forName(drvName);
        } catch (ClassNotFoundException ex) {
            throw new SQLException(ex.toString());
        }
        conn = DriverManager.getConnection(url, uname, passwd);
    }

    public void openDB(ConnPool pool) throws SQLException {
        if (conn != null && !conn.isClosed())
            throw new SQLException("The connection has been established already.");
        if (pool == null)
            throw new SQLException("The connection pool cannot be found.");
        clearResult();
        connPool = pool;
        conn = connPool.getConnection();
    }

    public void setConnectionSwitch(String on_off) throws ServletException {
        try {
            if (on_off.equalsIgnoreCase("ON")) {
                if (connPool == null)
                    openDB(driverName, jdbcURL, username, password);
                else
                    openDB(connPool);
            } else if (on_off.equalsIgnoreCase("OFF"))
                closeDB();
        } catch (SQLException ex) {
            throw new ServletException(ex.toString());
        }
    }

    public void setConnPool(ConnPool pool) {
        connPool = pool;
    }

    public void setDriverName(String drvName) {
        driverName = drvName;
    }

    public void setJdbcURL(String url) {
        jdbcURL = url;
    }

    public void setUserName(String uname) {
        username = uname;
    }

    public void setPassword(String passwd) {
        password = passwd;
    }
}
