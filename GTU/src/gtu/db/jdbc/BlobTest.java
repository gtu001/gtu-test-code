package gtu.db.jdbc;

import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * download job result for execel
 * 
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/4/11:下午 3:29:39
 * 
 *          DownloadJobResultPC
 * 
 */
public class BlobTest {

    private static final long serialVersionUID = 1L;

    /**
     * 將byte array 存入於 Blob
     */
    public void setBlob(Connection conn, String tableName, String blobColumn, String columnPK, String PKValue, byte[] bArray) {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        try {
            StringBuffer selectSql = new StringBuffer();
            StringBuffer updateBlobSql = new StringBuffer();
            selectSql.append(" SELECT ").append(blobColumn).append(" FROM ").append(tableName).append(" WHERE ").append(columnPK).append("=? FOR UPDATE ");
            updateBlobSql.append(" UPDATE ").append(tableName).append(" SET ").append(blobColumn).append(" = ? ").append(" WHERE ").append(columnPK).append("= ?");
            ps = conn.prepareStatement(selectSql.toString());
            ps.setString(1, PKValue);
            rs = ps.executeQuery();
            int res = 0;
            if (rs.next()) {
                Blob dbBlob = writeDataToBlob(rs, 1, bArray);
                ps2 = conn.prepareStatement(updateBlobSql.toString());
                ps2.setBlob(1, dbBlob);
                ps2.setString(2, PKValue);
                res = ps2.executeUpdate();
            }
            rs.close();
            ps.close();
            ps2.close();

        } catch (SQLException e) {
            System.out.println("[BlobManagerImp.setBlob] SQL Error:" + e.getMessage());
        } finally {
            if (rs  != null) try{rs.close();}catch(SQLException ignore){};
            if (ps  != null) try{ps.close();}catch(SQLException ignore){};
            if (ps2 != null) try{ps2.close();}catch(SQLException ignore){};
            if (conn != null) try {conn.close();}catch(SQLException ignore){};
        }
    }

    /**
     * 實際取得Blob 物件 (可能會隨著資料庫有所調整)
     * 
     * @param rs
     * @param column
     * @param data
     * @return
     * @throws SQLException
     */
    protected Blob writeDataToBlob(ResultSet rs, int column, byte[] data) throws SQLException {
        Blob blob = rs.getBlob(column);
        if (blob == null) {
            throw new SQLException("Driver's Blob representation is null!");
        }
        // handle thin driver's blob
        if (blob instanceof weblogic.jdbc.vendor.oracle.OracleThinBlob) {
            ((weblogic.jdbc.vendor.oracle.OracleThinBlob) blob).putBytes(1, data);
            return blob;
        } else if (blob.getClass().getPackage().getName().startsWith("weblogic.")) {
            try {
                // try to find putBytes method...
                Method m = blob.getClass().getMethod("putBytes", new Class[] { long.class, byte[].class });
                m.invoke(blob, new Object[] { new Long(1), data });
            } catch (Exception e) {
                throw new SQLException("Unable to find putBytes(long,byte[]) method on blob: " + e);
            }
            return blob;
        } else {

        }
        return blob;
    }

    /**
     * 取得 blob bytes[]
     * 
     * @param rs
     * @param column
     * @return
     * @throws SQLException
     */
    protected byte[] getDataFromBlob(ResultSet rs, int column) throws SQLException {
        Blob blob = rs.getBlob(column);
        long len = blob.length();
        if (len == 0) {
            return null;
        }
        return blob.getBytes(1, (int) len);
    }
}
