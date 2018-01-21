package gtu.db;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.support.JdbcUtils;
 
/**
 * 可用於批次更新資料
 */
public class JdbcDBOperateTemplate {
 
    private void insertBANK(final LineRowData rowData, final BigDecimal seq) {
        rowData.updateList.add(new BatchUpdateObject() {
            @Override
            public String getSql() {
                return "insert into T_BANK ("
                                + "BANK_CODE, "
                                + "BANK_NAME, "
                                + "BANK_CLASS, "
                                + "STATUS, "
                                + "INSERT_TIME, "
                                + "UPDATE_TIME, "
                                + "BANK_TYPE, "
                                + "IS_BASIC, "
                                + "TRANSFER_TYPE, "
                                + "ABBR_NAME, "
                                + "UPDATER_ID, "
                                + "BANK_ID, "
                                + "RECORDER_ID, "
                                + "INSERT_TIMESTAMP, "
                                + "UPDATE_TIMESTAMP,"
                                + "BANK_ORG_ID"
                                + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
            }
 
            @Override
            public boolean apply(PreparedStatement insPstmt)
                            throws SQLException {
                // 現在時間
                java.sql.Date nowDate = new java.sql.Date(new Date().getTime());
                // 塞入參數
                insPstmt.setString(1, rowData.bankCode);
                insPstmt.setString(2, rowData.bankName);
                insPstmt.setString(3, rowData.bankClass);
                insPstmt.setString(4, "0");
                insPstmt.setDate(5, nowDate);
                insPstmt.setDate(6, nowDate);
                insPstmt.setString(7, "1");
                insPstmt.setString(8, "N");
                insPstmt.setString(9, "0");
                insPstmt.setString(10, "NA");
                insPstmt.setString(11, "100001");
                insPstmt.setInt(12, seq.intValue());
                insPstmt.setString(13, "401");
                insPstmt.setDate(14, nowDate);
                insPstmt.setDate(15, nowDate);
                insPstmt.setLong(16, seq.intValue());
                return false;
            }
        });
    }
 
    private Map<String, Object> queryBank(String bankCode) {
        StringBuffer tBankSql = new StringBuffer(1024);
        tBankSql.append(" select * from T_BANK ");
        tBankSql.append(" where 1=1 ");
        tBankSql.append(" and BANK_CODE =" + "'" + bankCode + "'");
        SQLQuery qry1 = session.createSQLQuery(tBankSql.toString());
        qry1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Map<String, Object> bank = (Map<String, Object>) qry1.uniqueResult();
        return bank;
    }
 
    private static class LineRowData {
        String bankCode;
        String bankName;
        String addressId;
        String telephone;
        String bankClass;
        List<BatchUpdateObject> updateList = new ArrayList<BatchUpdateObject>();
        LineRowData(String line) {
        }
    }

    public void batchUpdate(List<BatchUpdateObject> applyList) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);
 
            for (BatchUpdateObject bObj : applyList) {
                String sql = bObj.getSql();
                pstmt = conn.prepareStatement(sql);
                System.out.println("updateSQL : " + sql);
                bObj.apply(pstmt);
                int updateResult = pstmt.executeUpdate();
                System.out.println("update : " + updateResult);
            }
 
            conn.commit();
        } catch (Exception ex) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new RuntimeException(ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeAll(rs, pstmt, conn);
        }
    }
 
    public List<Map<String, Object>> queryList(String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DB.getConnection();
 
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
 
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int ii = 1; ii <= metaData.getColumnCount(); ii++) {
                    String label = metaData.getColumnLabel(ii);
                    map.put(label, rs.getObject(label));
                }
                list.add(map);
            }
 
            return list;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeAll(rs, pstmt, conn);
        }
    }
    
    private void closeAll(ResultSet rs, Statement pstmt, Connection conn){
        JdbcUtils.closeConnection(conn);
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
    }
    
    private interface BatchUpdateObject {
        abstract String getSql();
 
        abstract boolean apply(PreparedStatement pstmt) throws Exception;
    }
}