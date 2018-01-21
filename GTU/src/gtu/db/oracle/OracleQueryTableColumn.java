package gtu.db.oracle;

import gtu.db.DbConstant;
import gtu.db.JdbcDBUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class OracleQueryTableColumn {

    public static void main(String[] args) {
        OracleQueryTableColumn test = new OracleQueryTableColumn();
        test.execute();
        System.out.println("done...");
    }
    
    private final static String TABLE_SQL;
    static {
        StringBuilder sb= new StringBuilder();
        sb.append(" SELECT                                                         ");
        sb.append("   C.OWNER, C.TABLE_NAME, C.COLUMN_ID, C.COLUMN_NAME,           ");
        sb.append("   DATA_TYPE, DATA_LENGTH, DATA_PRECISION, DATA_DEFAULT,        ");
        sb.append("   NULLABLE, COMMENTS                                           ");
        sb.append(" FROM                                                           ");
        sb.append("   ALL_TAB_COLUMNS C                                            ");
        sb.append(" JOIN ALL_TABLES T ON                                           ");
        sb.append("   C.OWNER = T.OWNER AND C.TABLE_NAME = T.TABLE_NAME            ");
        sb.append(" LEFT JOIN ALL_COL_COMMENTS R ON                                ");
        sb.append("   C.OWNER = R.Owner AND                                        ");
        sb.append("   C.TABLE_NAME = R.TABLE_NAME AND                              ");
        sb.append("   C.COLUMN_NAME = R.COLUMN_NAME                                ");
        sb.append(" WHERE                                                          ");
        sb.append("     c.owner != 'schema_name'                                   ");
        sb.append("     and c.table_name = 'EVISION_T2'                            ");
        sb.append(" ORDER BY C.TABLE_NAME, C.COLUMN_ID                             ");
        TABLE_SQL = sb.toString();
    }
    
    public void execute(){
        String url = "jdbc:oracle:thin:@172.31.70.50:1533:T13A";
        String userName = "pspismgr";
        String password = "iEEcTkBvpH";
        Connection conn = JdbcDBUtil.getConnection(DbConstant.DRIVER_ORACLE, url, userName, password);
        List<String> queryList = JdbcDBUtil.queryForList(TABLE_SQL, new String[]{"COLUMN_NAME"}, conn);
        for(String map : queryList){
            System.out.println(map);
        }
    }
}
