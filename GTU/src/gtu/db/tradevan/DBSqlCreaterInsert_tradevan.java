package gtu.db.tradevan;

import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;

import java.util.Map;


public class DBSqlCreaterInsert_tradevan {

    private static final DBSqlCreaterInsert_tradevan _instance = new DBSqlCreaterInsert_tradevan();
    public static DBSqlCreaterInsert_tradevan getInstance(){
        return _instance;
    }
    
    public SqlPrepareStatementDetail createInsertSql(Object doObject){
        Map<String,Object> dataMap = DBSqlCreater_tradevan.getFieldValue("dataMap", doObject);
        String tableName = DBSqlCreater_tradevan.getFieldValue("TABLENAME", doObject);
        
        Map<String, DBColumn_tradevan> mapping = DBSqlCreater_tradevan.getMappingAnnotation(doObject);
        
        SqlPrepareStatementDetail rtn = new SqlPrepareStatementDetail();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("INSERT INTO %s  (", tableName));
        for (String key : mapping.keySet()) {
            sb.append(key + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") VALUES ( ");
        for (String key : mapping.keySet()) {
            //若該欄位是使用sequence來取得
            if(_Tradevan_StringUtilsTest.isBlank(mapping.get(key).sequence())){
                sb.append("?,");
                rtn.valueList.add(new Object[]{key, dataMap.get(key)});
            }else{
                sb.append(mapping.get(key).sequence() + ",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
        rtn.sql = sb.toString();
        return rtn;
    }
}
