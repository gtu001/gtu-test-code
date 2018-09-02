package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBCommon_tradevan {
    
    /**
     * 取得value Map
     */
    public static Map<String, Object> getValueMapFrom(ResultSet result) {
        try{
            ResultSetMetaData rsmd = result.getMetaData();
            List<String> colArray = new ArrayList<String>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                colArray.add(rsmd.getColumnName(i).toUpperCase());
            }
            Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
            for (String col : colArray) {
                Object val = result.getObject(col);
                rtnMap.put(col, val);
            }
            return rtnMap;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * 取得value Map
     */
    public static <T> T getValueMapFrom(ResultSet result, Class<T> clz) {
        try{
            Map<String,Object> valMap = getValueMapFrom(result);
            Object obj = clz.newInstance();
            
            Field f = obj.getClass().getDeclaredField("dataMap");
            f.setAccessible(true);
            f.set(obj, valMap);
            return (T)obj;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public static void closeConnection(ResultSet rs, Statement stmt, Connection conn){
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
