package gtu.db.tradevan;

import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;
import gtu.db.tradevan.test.UserInfoDO;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

public class DBExecuteUpdater_tradevan {

    public static void main(String[] args) throws SQLException {
        UserInfoDO user = new UserInfoDO();
        user.setAddress("內湖");
        user.setCreateDate(new java.sql.Date(new Date().getTime()));
        user.setEmail("test@xxx.com");
        user.setUserId("test001");
        user.setLaststLoginDate(new java.sql.Timestamp(new Date().getTime()));
        user.setPassword("1357");
        user.setTelPhone("11112");
        user.setUserName("王曉明");
        user.setLoginTimes(5);
        
//        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(user, "insert");
//        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(user, "update");
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(user, "delete");
//        Connection conn = DBMain.getTestDataSource().getConnection();
        Connection conn = null;
        DBExecuteUpdater_tradevan.getInstance().executeUpdate(user.getClass(), dbSql, conn);
    }
    
    private static final DBExecuteUpdater_tradevan _instance = new DBExecuteUpdater_tradevan();
    public static DBExecuteUpdater_tradevan getInstance(){
        return _instance;
    }

    /**
     * ###
     * 進入點 XXX
     * ###
     */
    protected void executeUpdate(Class<?> clz, SqlPrepareStatementDetail detail, Connection conn) {
        PreparedStatement stmt = null;
        try {
            conn.setAutoCommit(false);
            System.out.println(detail.sql);
            stmt = conn.prepareStatement(detail.sql);
            
            for(int ii = 0 ; ii < detail.valueList.size() ; ii ++){
                Object[] values = detail.valueList.get(ii);
                System.out.println(Arrays.toString(values));
                String fieldName = (String)values[0];
                stmt.setObject(ii + 1, values[1], getType(fieldName, clz));
            }
            
            int result = stmt.executeUpdate();
            System.out.println("update rows = " + result);
            
            //conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
//            try {
//                conn.setAutoCommit(true);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            DBCommon_tradevan.closeConnection(null, stmt, null);
        }
    }
    
    private int getType(String fieldName, Class<?> clz){
        try {
            Field f = clz.getDeclaredField(fieldName);
            DBColumn_tradevan anno = f.getAnnotation(DBColumn_tradevan.class);
            return anno.type();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
