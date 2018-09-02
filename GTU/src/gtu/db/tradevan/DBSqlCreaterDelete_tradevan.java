package gtu.db.tradevan;

import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;

import java.util.List;
import java.util.Map;


public class DBSqlCreaterDelete_tradevan {

    private static final DBSqlCreaterDelete_tradevan _instance = new DBSqlCreaterDelete_tradevan();
    public static DBSqlCreaterDelete_tradevan getInstance(){
        return _instance;
    }
    
    /**
     * 傳入要建立的刪除資料
     */
    public SqlPrepareStatementDetail createDeleteSql(String tableName, Map<String, String> valmap, List<String> whereList, Class<?> clz) {
        SqlPrepareStatementDetail rtn = new SqlPrepareStatementDetail();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM " + tableName + " WHERE ");
        
        List<String> pkList = DBSqlCreater_tradevan.getPkList(clz);
        if(whereList!=null && !whereList.isEmpty()){
            pkList = whereList;
        }
        
        for (String key : pkList) {
            sb.append(" " + key + "=? and");
            rtn.valueList.add(new Object[]{key, valmap.get(key)});
        }
        if (pkList.size() != 0) {
            sb.delete(sb.length() - 4, sb.length());
        }else{
            sb.append(" 1!=1 ");
        }
        rtn.sql = sb.toString();
        return rtn;
    }
}
