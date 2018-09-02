package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBExecuteQueryPage_tradevan {

    private static final DBExecuteQueryPage_tradevan _instance = new DBExecuteQueryPage_tradevan();

    public static DBExecuteQueryPage_tradevan getInstance() {
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

    public <T> List<T> queryBean(Class<?> clz, String sql, int rownumStart, int pageSize, Connection conn) {
        List<T> rtnList = new ArrayList<T>();
        List<Map<String,Object>> queryList = queryMap(sql, rownumStart, pageSize, conn);
        for(Map<String,Object> m : queryList){
            rtnList.add((T)_getBean(m, clz));
        }
        return rtnList;
    }

    public List<Map<String,Object>> queryMap(String sql, int rownumStart, int pageSize, Connection conn) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            List<Map<String,Object>> queryList = new ArrayList<Map<String,Object>>();
            
            stmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //stmt.setFetchSize(1000);
            
            rs = stmt.executeQuery();

            List<String> colList = new ArrayList<String>();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int ii = 1; ii <= rsmd.getColumnCount(); ii++) {
                String fieldName = rsmd.getColumnLabel(ii);
                colList.add(fieldName.toUpperCase());
            }

            int rownum = 0;
            while (rs.next()) {
                rownum ++;
                
                if(rownum < rownumStart){
                    continue;
                }
                
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (String key : colList) {
                    map.put(key, rs.getObject(key));
                }
                queryList.add(map);
                
                if(queryList.size() >= pageSize){
                    break;
                }
            }
            
            return queryList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            DBCommon_tradevan.closeConnection(rs, stmt, conn);
        }
    }
}
