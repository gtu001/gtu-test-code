package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gtu.db.tradevan.test.UserInfoDO;

public class DBSqlCreater_tradevan {

    public static void main(String[] args) {
        UserInfoDO user = new UserInfoDO();
        user.setAddress("內湖");
//        user.setCreateDate("20150408");
        user.setEmail("test@xxx.com");
        user.setUserId("test001");
//        user.setLaststLoginDate("20150408");
        user.setPassword("1357");
        user.setTelPhone("1111");
        user.setUserName("王曉明");
        
        SqlPrepareStatementDetail dbSql = DBSqlCreater_tradevan.getInstance().getDbSql(user, "insert");
        System.out.println(dbSql.sql);
        for(Object[] vvv : dbSql.valueList){
            System.out.println(Arrays.toString(vvv));
        }
    }
    
    private static final DBSqlCreater_tradevan _instance = new DBSqlCreater_tradevan();
    public static DBSqlCreater_tradevan getInstance(){
        return _instance;
    }
    
    /**
     * ### 
     * 進入點  XXX
     * ###
     */
    protected SqlPrepareStatementDetail getDbSql(Object doObject, String type){
        Map<String,String> dataMap = getFieldValue("dataMap", doObject);
        String tableName = getFieldValue("TABLENAME", doObject);
        SqlPrepareStatementDetail result = null;
        if ("insert".equalsIgnoreCase(type)) {
            result = DBSqlCreaterInsert_tradevan.getInstance().createInsertSql(doObject);
        }else if ("update".equalsIgnoreCase(type)) {
            result = DBSqlCreaterUpdate_tradevan.getInstance().createUpdateSql(tableName, dataMap, null, doObject.getClass());
        }else if ("delete".equalsIgnoreCase(type)) {
            result = DBSqlCreaterDelete_tradevan.getInstance().createDeleteSql(tableName, dataMap, null, doObject.getClass());
        }
        return result;
    }
    
    /**
     * ### 
     * 進入點  XXX
     * ###
     */
    protected SqlPrepareStatementDetail getDbSql(Object doObject, List<String> whereList, String type){
        Map<String,String> dataMap = getFieldValue("dataMap", doObject);
        String tableName = getFieldValue("TABLENAME", doObject);
        SqlPrepareStatementDetail result = null;
        if ("insert".equalsIgnoreCase(type)) {
            throw new UnsupportedOperationException("insert不支援帶whereList參數");
        }else if ("update".equalsIgnoreCase(type)) {
            result = DBSqlCreaterUpdate_tradevan.getInstance().createUpdateSql(tableName, dataMap, whereList, doObject.getClass());
        }else if ("delete".equalsIgnoreCase(type)) {
            result = DBSqlCreaterDelete_tradevan.getInstance().createDeleteSql(tableName, dataMap, whereList, doObject.getClass());
        }
        return result;
    }
    
    protected static Map<String,DBColumn_tradevan> getMappingAnnotation(Object doObject){
        Map<String,DBColumn_tradevan> map = new LinkedHashMap<String,DBColumn_tradevan>();
        for(Field f : doObject.getClass().getDeclaredFields()){
            if(f.isAnnotationPresent(DBColumn_tradevan.class)){
                try {
                    String dbField = (String)f.get(doObject.getClass());
                    DBColumn_tradevan anno = f.getAnnotation(DBColumn_tradevan.class);
                    map.put(dbField, anno);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return map;
    }
    
    protected static <T> T getFieldValue(String name,Object value){
        try {
            Field f = value.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (T)f.get(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected static List<String> getPkList(Class<?> clz){
        List<String> pkList = new ArrayList<String>();
        for(Field f : clz.getDeclaredFields()){
            if(f.isAnnotationPresent(gtu.db.tradevan.DBColumn_tradevan.class)){
                DBColumn_tradevan dbCol = f.getAnnotation(gtu.db.tradevan.DBColumn_tradevan.class);
                if(dbCol.pk()){
                    try {
                        pkList.add((String)f.get(clz));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return pkList;
    }
    
    public static class SqlPrepareStatementDetail {
        String sql;
        List<Object[]> valueList = new ArrayList<Object[]>();//Object[] 0-column_name, 1-value
    }
}
