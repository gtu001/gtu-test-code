package gtu.db.tradevan;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

public class DBNameMapping_tradevan {
    
    private static final DBNameMapping_tradevan _INST = new DBNameMapping_tradevan();
    public static DBNameMapping_tradevan getInstacne(){
        return _INST;
    }

    public static void main(String[] args) throws SQLException{
        StringBuilder sb = new StringBuilder();
        sb.append("select * from user_info where user_id = :userId and password = :password or password = ':ok'");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("userId", "gtu001");
        param.put("password", "1234");
        
        DataSource ds = null;
        //ds = DbConstant.getTestDataSource();
        
        DBNameMapping_tradevan test = new DBNameMapping_tradevan();
        System.out.println(test.executeQuery(sb.toString(), param, null, ds.getConnection()));
        
        System.out.println("done...");
    }
    
    protected <T> List<T> transfor_mapListToClassList(List<Map<String, Object>> queryList, Class<T> clz){
        List<T> rtnList = new ArrayList<T>();
        for(Map<String, Object> dataMap : queryList){
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
    
    protected List<Map<String,Object>> executeQuery(String sql, Map<String,Object> param, Map<String,Integer> typeMap, Connection conn) {
        List<Map<String,Object>> rtnList = new ArrayList<Map<String,Object>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            System.out.println("before sql : " + sql);
            NameMapping nameMapping = new NameMapping(sql, param);
            nameMapping.execute();
            
            stmt = conn.prepareStatement(nameMapping.questionSql);
            if(typeMap==null){
                typeMap = new HashMap<String,Integer>();
            }
            
            for(int ii = 0 ; ii < nameMapping.questionList.size() ; ii ++){
                String key = nameMapping.questionList.get(ii);
                Object value = param.get(key);
                if(!typeMap.containsKey(key)){
                    stmt.setObject(ii + 1, value);
                }else{
                    stmt.setObject(ii + 1, value, typeMap.get(key));
                }
            }
            
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
                rtnList.add(map);
            }
            return rtnList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            DBCommon_tradevan.closeConnection(rs, stmt, conn);
        }
    }
    
    protected void executeUpdate(String sql, Map<String,Object> param, Map<String,Integer> typeMap, Connection conn) {
        PreparedStatement stmt = null;
        try {
            conn.setAutoCommit(false);
            System.out.println("before sql : " + sql);
            NameMapping nameMapping = new NameMapping(sql, param);
            nameMapping.execute();
            stmt = conn.prepareStatement(nameMapping.questionSql);
            
            if(typeMap==null){
                typeMap = new HashMap<String,Integer>();
            }
            
            for(int ii = 0 ; ii < nameMapping.questionList.size() ; ii ++){
                String key = nameMapping.questionList.get(ii);
                Object value = param.get(key);
                if(!typeMap.containsKey(key)){
                    stmt.setObject(ii + 1, value);
                }else{
                    stmt.setObject(ii + 1, value, typeMap.get(key));
                }
            }
            
            int result = stmt.executeUpdate();
            System.out.println("update rows = " + result);
            
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBCommon_tradevan.closeConnection(null, stmt, conn);
        }
    }
    
    private static class NameMapping {
        final String sql;
        final Map<String,Object> param;
        String questionSql;
        List<String> questionList = new ArrayList<String>();
        
        public NameMapping(String sql, Map<String,Object> param){
            this.sql = sql;
            this.param = param;
        }
        
        public void execute(){
            Pattern ptn = Pattern.compile("\\:(\\w+)");
            Matcher mth = ptn.matcher(sql);
            
            StringBuffer sb2 = new StringBuffer();
            
            while(mth.find()){
                String key = mth.group(1);
                if(param.containsKey(key)){
                    questionList.add(key);
                    System.out.println("->" + key);
                    mth.appendReplacement(sb2, "?");
                }else{
                    System.out.println("找不到符合的key : " + key);
                    mth.appendReplacement(sb2, ":"+key);
                }
            }
            mth.appendTail(sb2);
            questionSql = sb2.toString();
            
            debug();
        }
        
        public void debug(){
            System.out.println("Mapping SQL : " + questionSql);
            System.out.println("serial param size = " + questionList.size());
            for(int ii = 0 ; ii < questionList.size(); ii ++){
                String key = questionList.get(ii);
                System.out.println((ii + 1) + "[" + key+ "]:" + param.get(key));
            }
        }
    }
}
