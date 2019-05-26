package gtu.db.tradevan;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import gtu.db.tradevan.DBBatchUpdatePreapre_tradevan.BatchUpdatePrepareAction;
import gtu.db.tradevan.DBExecuteCall_tradevan.CallParameter;
import gtu.db.tradevan.DBExecuteQueryInnerLoop_tradevan.DBQueryAction_Map;
import gtu.db.tradevan.DBExecuteQueryInnerLoop_tradevan.DBQueryAction_T;
import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;

public class DBMain {

    private DataSource dataSource;
    private MyConnection myConnection;
    private Connection conn;

    public void generateDO(String sql, List<String> pkList, String tableName, File genFile) {
        Connection conn = _getConnection();
        DBQueryToDO_tradevan.getInstance().execute(sql, pkList, tableName, conn, genFile);
    }
    
    public void saveOrUpdate(Object doObject){
        SqlPrepareStatementDetail dbSql = DBSqlCreaterFindByPk_tradevan.getInstance().createFindByPkSql(doObject);
        List queryList = DBExecuteQuery_tradevan.getInstance().query(doObject.getClass(), dbSql, conn, false);
        if(queryList.isEmpty()){
            System.out.println("@insert");
            this.insert(doObject);
        }else{
            System.out.println("@update");
            this.update(doObject);
        }
    }

    public void insert(Object doObject) {
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(doObject, "insert");
        DBExecuteUpdater_tradevan.getInstance().executeUpdate(doObject.getClass(), dbSql, conn);
    }

    public void update(Object doObject) {
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(doObject, "update");
        DBExecuteUpdater_tradevan.getInstance().executeUpdate(doObject.getClass(), dbSql, conn);
    }

    public void update(Object doObject, List<String> whereList) {
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(doObject, whereList, "update");
        DBExecuteUpdater_tradevan.getInstance().executeUpdate(doObject.getClass(), dbSql, conn);
    }

    public void delete(Object doObject) {
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(doObject, "delete");
        DBExecuteUpdater_tradevan.getInstance().executeUpdate(doObject.getClass(), dbSql, conn);
    }

    public void delete(Object doObject, List<String> whereList) {
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(doObject, whereList, "delete");
        DBExecuteUpdater_tradevan.getInstance().executeUpdate(doObject.getClass(), dbSql, conn);
    }

    public <T> List<T> query(String sql, Class<T> clz) {
        Connection conn = _getConnection();
        List<T> queryList = DBExecuteQuery_tradevan.getInstance().query(sql, clz, conn, true);
        return queryList;
    }

    public List<Map<String, Object>> query(String sql) {
        Connection conn = _getConnection();
        List<Map<String, Object>> queryList = DBExecuteQuery_tradevan.getInstance().query(sql, conn, true);
        return queryList;
    }
    
    public List<Map<String,Object>> queryPage(String sql, int rownumStart, int querySize){
        Connection conn = _getConnection();
        List<Map<String, Object>> queryList = DBExecuteQueryPage_tradevan.getInstance().queryMap(sql, rownumStart, querySize, conn);
        return queryList;
    }
    
    public <T> List<T> queryPage(String sql, int rownumStart, int querySize, Class<T> clz){
        Connection conn = _getConnection();
        List<T> queryList = DBExecuteQueryPage_tradevan.getInstance().queryBean(clz, sql, rownumStart, querySize, conn);
        return queryList;
    }
    
    /**
     * 使用同一connection查詢, 用後不關閉
     */
    public <T> List<T> query__inSameConnection(String sql, Class<T> clz) {
        if(conn == null){
            conn = _getConnection();
        }
        List<T> queryList = DBExecuteQuery_tradevan.getInstance().query(sql, clz, conn, false);
        return queryList;
    }

    /**
     * 使用同一connection查詢, 用後不關閉
     */
    public List<Map<String, Object>> query__inSameConnection(String sql) {
        if(conn == null){
            conn = _getConnection();
        }
        List<Map<String, Object>> queryList = DBExecuteQuery_tradevan.getInstance().query(sql, conn, false);
        return queryList;
    }

    public void namingUpdate(String sql, Map<String, Object> param, Map<String, Integer> typeMap) {
        DBNameMapping_tradevan.getInstacne().executeUpdate(sql, param, typeMap, conn);
    }

    public void namingUpdate(String sql, Map<String, Object> param) {
        DBNameMapping_tradevan.getInstacne().executeUpdate(sql, param, null, conn);
    }

    public List<Map<String, Object>> namingQuery(String sql, Map<String, Object> param, Map<String, Integer> typeMap) {
        Connection conn = _getConnection();
        List<Map<String, Object>> queryList = DBNameMapping_tradevan.getInstacne().executeQuery(sql, param, typeMap, conn);
        return queryList;
    }

    public List<Map<String, Object>> namingQuery(String sql, Map<String, Object> param) {
        Connection conn = _getConnection();
        List<Map<String, Object>> queryList = DBNameMapping_tradevan.getInstacne().executeQuery(sql, param, null, conn);
        return queryList;
    }

    public <T> List<T> namingQuery(String sql, Map<String, Object> param, Map<String, Integer> typeMap, Class<T> clz) {
        Connection conn = _getConnection();
        List<Map<String, Object>> queryList = DBNameMapping_tradevan.getInstacne().executeQuery(sql, param, typeMap, conn);
        List<T> queryList2 = DBNameMapping_tradevan.getInstacne().transfor_mapListToClassList(queryList, clz);
        return queryList2;
    }

    public <T> List<T> namingQuery(String sql, Map<String, Object> param, Class<T> clz) {
        Connection conn = _getConnection();
        List<Map<String, Object>> queryList = DBNameMapping_tradevan.getInstacne().executeQuery(sql, param, null, conn);
        List<T> queryList2 = DBNameMapping_tradevan.getInstacne().transfor_mapListToClassList(queryList, clz);
        return queryList2;
    }

    public void queryInnerLoop(String sql, DBQueryAction_Map action) {
        Connection conn = _getConnection();
        DBExecuteQueryInnerLoop_tradevan.getInstance().queryMap(sql, action, conn);
    }

    public <T> void queryInnerLoop(String sql, DBQueryAction_T<T> action) {
        Connection conn = _getConnection();
        DBExecuteQueryInnerLoop_tradevan.getInstance().queryBean(sql, action, conn);
    }

    public void batchUpdate(int batchUpdateSize, boolean nonUpdateThrow, Connection conn, BatchUpdatePrepareAction action) {
        DBBatchUpdatePreapre_tradevan test = new DBBatchUpdatePreapre_tradevan(batchUpdateSize, nonUpdateThrow, conn);
        test.executeUpdate(action);
    }
    
    public void call(String callSql, List<CallParameter> inList, List<CallParameter> outList){
        Connection conn = _getConnection();
        inList = inList != null ? inList : Collections.<CallParameter>emptyList();
        outList = outList != null ? outList : Collections.<CallParameter>emptyList();
        DBExecuteCall_tradevan.getInstance().call(callSql, inList, outList, conn);
    }

    public void beginTransaction() {
        conn = _getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            conn.rollback();
            System.out.println("# rollback!!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;
    }

    public void commit() {
        try {
            conn.commit();
            System.out.println("# commit!!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;
    }
    
    public interface MyConnection {
        Connection getConnection() throws SQLException;
    }
    
    public void setMyConnection(MyConnection myConnection){
        this.myConnection = myConnection;
    }
    
    private Connection _getConnection() {
        try {
            if(myConnection != null){
                return myConnection.getConnection();
            }
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
