package gtu.db.tradevan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gtu.db.tradevan.DBSqlCreater_tradevan.SqlPrepareStatementDetail;

public class DBSqlCreaterFindByPk_tradevan {

    private static final DBSqlCreaterFindByPk_tradevan _instance = new DBSqlCreaterFindByPk_tradevan();

    public static DBSqlCreaterFindByPk_tradevan getInstance() {
        return _instance;
    }

    public SqlPrepareStatementDetail createFindByPkSql(Object doObject) {
        Map<String, Object> dataMap = DBSqlCreater_tradevan.getFieldValue("dataMap", doObject);
        String tableName = DBSqlCreater_tradevan.getFieldValue("TABLENAME", doObject);

        Map<String, DBColumn_tradevan> mapping = DBSqlCreater_tradevan.getMappingAnnotation(doObject);

        SqlPrepareStatementDetail rtn = new SqlPrepareStatementDetail();

        StringBuffer appendWhereSb = new StringBuffer();
        List<Object[]> valueList = new ArrayList<Object[]>();

        List<String> pkList = DBSqlCreater_tradevan.getPkList(doObject.getClass());
        for (String column : pkList) {
            valueList.add(new Object[] { column, dataMap.get(column) });
            appendWhereSb.append(" and " + column + " = ? ");
        }

        rtn.sql = " select * from " + tableName + " where 1=1 " + appendWhereSb;
        rtn.valueList = valueList;
        return rtn;
    }
}
