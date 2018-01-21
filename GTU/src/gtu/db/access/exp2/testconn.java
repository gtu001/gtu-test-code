package gtu.db.access.exp2;


/**
 * @author Troy 2009/02/02
 * 
 */
public class testconn {
    public static void main(String[] args) {
        try {
            // 資料庫連結設定..........初始
            SQLBridge sqlBridge = new SQLBridge();
            ConnPool connPool = new ConnPool();
            String dbpath = testconn.class.getResource("db.mdb").toURI().getPath().substring(1);
            String url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ=" + dbpath;
            System.out.println(url);
            connPool.setDriverName("sun.jdbc.odbc.JdbcOdbcDriver");
            // connPool.setJdbcURL("jdbc:odbc:ePaper");
            connPool.setJdbcURL(url);
            connPool.setUserName("");
            connPool.setPassword("");
            connPool.setConnectionSwitch("on");
            sqlBridge.setConnPool(connPool);

            sqlBridge.openDB(connPool);

            // 資料庫設定................結束

            String sql = "SELECT * FROM USER_INFO";

            sqlBridge.execSQL(sql);

            sqlBridge.lastRow();
            int RowCount1 = sqlBridge.getRowCount(); // 資料比數
            sqlBridge.firstRow();

            for (int i = 0; i < RowCount1; i++) {
                String s1 = sqlBridge.getFieldString("NAME");
                String s2 = sqlBridge.getFieldString("ID");
                String s3 = sqlBridge.getFieldString("PASSWORD");
                String s4 = sqlBridge.getFieldString("DEPARTMENT");
                String s5 = sqlBridge.getFieldString("TITLE");
                System.out.println(s1 + "\t" + s2 + "\t" + s3 + "\t" + s4 + "\t" + s5);
                if (!sqlBridge.nextRow())
                    break;
            }

            sqlBridge.closeDB();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
