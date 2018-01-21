package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;

public class DBBatchUpdatePreapre_tradevan {

    private Map<String, PreparedStatementGroup> pstmtMap = new HashMap<String, PreparedStatementGroup>();
    private int batchUpdateSize = -1;// 幾筆更新一次
    private boolean isUpdateCountZeroThrow = false;// 更新沒有更新到是否要throw
    private Connection conn;

    public DBBatchUpdatePreapre_tradevan(int batchUpdateSize, boolean isUpdateCountZeroThrow, Connection conn) {
        this.batchUpdateSize = batchUpdateSize;
        this.isUpdateCountZeroThrow = isUpdateCountZeroThrow;
        this.conn = conn;
    }

    /**
     * 檢查update屬性是否正確
     */
    private void checkUpdateType(String updateType) {
        if (!updateType.equalsIgnoreCase("insert") && //
                !updateType.equalsIgnoreCase("update") && //
                !updateType.equalsIgnoreCase("delete")) {
            throw new RuntimeException("updateType 必須為 insert,update,delete");
        }
    }

    /**
     * 檢查sql 欄位是否相同
     */
    private boolean checkDataOk(Class<?> clz, SqlPrepareStatementDetail detail, PreparedStatementGroup group) {
        if (clz != group.firstClz || //
                !group.firstDetail.sql.equalsIgnoreCase(detail.sql) || //
                isColumnSame(group.firstDetail, detail) == false) {//
            return false;
        }
        return true;
    }

    /**
     * 檢查欄位是否相同
     */
    private boolean isColumnSame(SqlPrepareStatementDetail detail1, SqlPrepareStatementDetail detail2) {
        List<String> list1 = getColumnList(detail1);
        List<String> list2 = getColumnList(detail2);
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int ii = 0; ii < list1.size(); ii++) {
            String column1 = list1.get(ii);
            String column2 = list2.get(ii);
            if (!column1.equalsIgnoreCase(column2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 取得column 清單
     */
    private List<String> getColumnList(SqlPrepareStatementDetail detail1) {
        List<String> list = new ArrayList<String>();
        for (Object[] vals : detail1.valueList) {
            String column = (String) vals[0];
            list.add(column);
        }
        return list;
    }

    /**
     * 批次update結果
     */
    private boolean checkBatchResult(int[] result) {
        for (int v : result) {
            if (v == 0) {
                return false;
            }
        }
        return true;
    }

    public interface BatchUpdatePrepareAction {
        BatchUpdatePrepareActionObject[] apply() throws Exception;
    }

    public static class BatchUpdatePrepareActionObject {
        Object bean;
        String updateType;
        List<String> whereColumn;

        public Object getBean() {
            return bean;
        }

        public void setBean(Object bean) {
            this.bean = bean;
        }

        public List<String> getWhereColumn() {
            return whereColumn;
        }

        public void setWhereColumn(List<String> whereColumn) {
            this.whereColumn = whereColumn;
        }

        public String getUpdateType() {
            return updateType;
        }

        public void setUpdateType(String updateType) {
            this.updateType = updateType;
        }
    }

    private void doExecuteBatch(PreparedStatement pstmt) throws SQLException {
        int[] result = pstmt.executeBatch();
        if (!checkBatchResult(result) && isUpdateCountZeroThrow) {
            throw new SQLException("並非所有update都成功");
        }
    }

    /**
     * ### 進入點 XXX ###
     */
    public void executeUpdate(BatchUpdatePrepareAction batchAction) {
        try {
            conn.setAutoCommit(false);

            A: for (int count = 0;; count++) {

                BatchUpdatePrepareActionObject[] objectArry = batchAction.apply();

                if (objectArry == null || objectArry.length == 0) {
                    break A;
                }

                for (BatchUpdatePrepareActionObject object : objectArry) {
                    Object obj = object.bean;
                    if (object.bean == null) {
                        break A;
                    }

                    Class<?> clz = obj.getClass();
                    List<String> whereColumns = object.whereColumn;
                    String updateType = object.updateType;
                    
                    if(updateType == null || (!updateType.equalsIgnoreCase("insert") //
                            && !updateType.equalsIgnoreCase("update") && !updateType.equalsIgnoreCase("delete"))){
                        throw new Exception("BatchUpdatePrepareActionObject.updateType 必須為 insert, update, delete");
                    }

                    SqlPrepareStatementDetail detail = null;
                    if (whereColumns != null && !whereColumns.isEmpty()) {
                        detail = DBSqlCreater_tradevan.getInstance().getDbSql(obj, whereColumns, updateType);
                    } else {
                        detail = DBSqlCreater_tradevan.getInstance().getDbSql(obj, updateType);
                    }

                    // 取得PreparedStatement group
                    PreparedStatementGroup group = null;
                    if (pstmtMap.containsKey(detail.sql)) {
                        group = pstmtMap.get(detail.sql);
                    } else {
                        group = new PreparedStatementGroup();
                        group.firstClz = clz;
                        group.firstDetail = detail;
                        group.pstmt = conn.prepareStatement(detail.sql);
                        pstmtMap.put(detail.sql, group);
                    }

                    // 檢查sql 欄位是否相同
                    if (!checkDataOk(clz, detail, group)) {
                        throw new SQLException("資料比對有誤!");
                    }

                    for (int ii = 0; ii < detail.valueList.size(); ii++) {
                        Object[] values = detail.valueList.get(ii);
                        System.out.println(Arrays.toString(values));
                        String fieldName = (String) values[0];
                        group.pstmt.setObject(ii + 1, values[1], getType(fieldName, clz));
                    }

                    group.pstmt.addBatch();

                    if (batchUpdateSize != -1 && group.count % batchUpdateSize == 0) {
                        // 執行update
                        doExecuteBatch(group.pstmt);
                    }
                    
                    group.count ++;
                }
            }

            // 執行update
            for (String sql : pstmtMap.keySet()) {
                doExecuteBatch(pstmtMap.get(sql).pstmt);
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            // try {
            // conn.setAutoCommit(true);
            // } catch (SQLException e) {
            // e.printStackTrace();
            // }
            for (String sql : pstmtMap.keySet()) {
                DBCommon_tradevan.closeConnection(null, pstmtMap.get(sql).pstmt, null);
            }
        }
    }

    private int getType(String fieldName, Class<?> clz) {
        try {
            Field f = clz.getDeclaredField(fieldName);
            DBColumn_tradevan anno = f.getAnnotation(DBColumn_tradevan.class);
            return anno.type();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class PreparedStatementGroup {
        private PreparedStatement pstmt;
        private int count = 0;
        private Class<?> firstClz;
        private SqlPrepareStatementDetail firstDetail;
    }
}
