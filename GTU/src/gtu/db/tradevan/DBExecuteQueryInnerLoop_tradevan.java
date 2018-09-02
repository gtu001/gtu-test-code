package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBExecuteQueryInnerLoop_tradevan {

    private static final DBExecuteQueryInnerLoop_tradevan _instance = new DBExecuteQueryInnerLoop_tradevan();

    public static DBExecuteQueryInnerLoop_tradevan getInstance() {
        return _instance;
    }

    private <T> T _getBean(Map<String, Object> dataMap, Class<T> clz) {
        try {
            T t = clz.newInstance();
            Field f = t.getClass().getDeclaredField("dataMap");
            f.setAccessible(true);
            f.set(t, dataMap);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args){
        DBQueryAction_T<Byte> x = new DBQueryAction_T<Byte>() {
            @Override
            public boolean action(Byte bean) {
                return false;
            }
        };
    }
    
    private static <T> Class<T> getDBQueryActionTClass(DBQueryAction_T<T> action){
        ParameterizedType type = (ParameterizedType) action.getClass().getGenericInterfaces()[0];
        Class<?> type2 = (Class<?>)type.getActualTypeArguments()[0];
        return (Class<T>) type2;
    }

    public interface DBQueryAction_T<T> {
        boolean action(T bean);
    }

    public <T> void queryBean(String sql, DBQueryAction_T<T> action, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(1000);
            
            rs = stmt.executeQuery();
            
            List<String> colList = new ArrayList<String>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int ii = 1; ii <= rsmd.getColumnCount(); ii++) {
                String fieldName = rsmd.getColumnLabel(ii);
                colList.add(fieldName.toUpperCase());
            }
            
            while(rs.next()){
                Map<String,Object> map = new LinkedHashMap<String,Object>();
                for(String key : colList){
                    map.put(key, rs.getObject(key));
                }
                
                Class<T> clz = getDBQueryActionTClass(action);
                T bean = _getBean(map, clz);
                if(!action.action(bean)){
                    break;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            DBCommon_tradevan.closeConnection(rs, stmt, conn);
        }
    }

    public interface DBQueryAction_Map {
        boolean action(Map<String, Object> map);
    }

    public void queryMap(String sql, DBQueryAction_Map action, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stmt.setFetchSize(1000);
            
            rs = stmt.executeQuery();

            List<String> colList = new ArrayList<String>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int ii = 1; ii <= rsmd.getColumnCount(); ii++) {
                String fieldName = rsmd.getColumnLabel(ii);
                colList.add(fieldName.toUpperCase());
            }

            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (String key : colList) {
                    map.put(key, rs.getObject(key));
                }

                if (!action.action(map)) {
                    break;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            DBCommon_tradevan.closeConnection(rs, stmt, conn);
        }
    }
}
