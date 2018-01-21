package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;

public class DBExecuteQuery_tradevan {

    private static final DBExecuteQuery_tradevan _instance = new DBExecuteQuery_tradevan();

    public static DBExecuteQuery_tradevan getInstance() {
        return _instance;
    }

    public <T> List<T> query(String sql, Class<T> clz, Connection conn, boolean doClose) {
        List<T> rtnList = new ArrayList<T>();
        List<Map<String, Object>> queryList = query(sql, conn, doClose);
        for (Map<String, Object> dataMap : queryList) {
            try {
                T t = clz.newInstance();
                Field f = t.getClass().getDeclaredField("dataMap");
                f.setAccessible(true);
                f.set(t, dataMap);
                rtnList.add(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return rtnList;
    }

    public List<Map<String, Object>> query(String sql, Connection conn, boolean doClose) {
        List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
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
                rtnList.add(map);
            }
            return rtnList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if(doClose){
                DBCommon_tradevan.closeConnection(rs, stmt, conn);
            }else{
                DBCommon_tradevan.closeConnection(rs, stmt, null);
            }
        }
    }

    public <T> List<T> query(Class<T> clz, SqlPrepareStatementDetail detail, Connection conn, boolean doClose) {
        List<Map<String, Object>> queryList = new ArrayList<Map<String, Object>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            System.out.println(detail.sql);
            stmt = conn.prepareStatement(detail.sql);

            for (int ii = 0; ii < detail.valueList.size(); ii++) {
                Object[] values = detail.valueList.get(ii);
                System.out.println(Arrays.toString(values));
                String fieldName = (String) values[0];
                stmt.setObject(ii + 1, values[1], getType(fieldName, clz));
            }

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
                queryList.add(map);
            }

            List<T> rtnList = new ArrayList<T>();
            for (Map<String, Object> dataMap : queryList) {
                T t = clz.newInstance();
                Field f = t.getClass().getDeclaredField("dataMap");
                f.setAccessible(true);
                f.set(t, dataMap);
                rtnList.add(t);
            }
            
            return rtnList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if(doClose){
                DBCommon_tradevan.closeConnection(rs, stmt, conn);
            }else{
                DBCommon_tradevan.closeConnection(rs, stmt, null);
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
}
