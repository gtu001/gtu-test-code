package _temp;

import javax.swing.JOptionPane;

public class Test54 {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>SWING ERROR GET_CAUSE -----------------------↓↓↓↓↓↓                                                                                                                                                                                                  ");
        sb.append(" 參考 : [score:0.030] <font color=\"GREEN\">test</font>                                                                                                                                                                                                  ");
        sb.append(" 'TEST' 直欄不在 FROM 清單內的任何表格中，或它出現在合併規格內且在合併規格的範圍之外，或它出現在 HAVING 子句中，且不在 GROUP BY 清單內。如果這是 CREATE 或 ALTER TABLE 陳述式，'TEST' 就不是目標表格中的直欄。=java.sql.SQLSyntaxErrorException        ");
        sb.append(" 'TEST' 直欄不在 FROM 清單內的任何表格中，或它出現在合併規格內且在合併規格的範圍之外，或它出現在 HAVING 子句中，且不在 GROUP BY 清單內。如果這是 CREATE 或 ALTER TABLE 陳述式，'TEST' 就不是目標表格中的直欄。=org.apache.derby.client.am.SqlException ");
        sb.append("SWING ERROR GET_CAUSE -----------------------↑↑↑↑↑↑                                                                                                                                                                                                        ");
        sb.append("                                                                                                                                                                                                                                                           ");
        sb.append("org.apache.derby.client.am.SQLExceptionFactory.getSQLException(Unknown Source)                                                                                                                                                                             ");
        sb.append("org.apache.derby.client.am.SqlException.getSQLException(Unknown Source)                                                                                                                                                                                    ");
        sb.append("org.apache.derby.client.am.ClientConnection.prepareStatement(Unknown Source)                                                                                                                                                                               ");
        sb.append("org.apache.commons.dbcp.DelegatingConnection.prepareStatement(DelegatingConnection.java:281)                                                                                                                                                               ");
        sb.append("org.apache.commons.dbcp.PoolingDataSource$PoolGuardConnectionWrapper.prepareStatement(PoolingDataSource.java:313)                                                                                                                                          ");
        sb.append("gtu.db.JdbcDBUtil.queryForList_customColumns(JdbcDBUtil.java:303)                                                                                                                                                                                          ");
        sb.append("gtu._work.ui.FastDBQueryUI.executeSqlButtonClick(FastDBQueryUI.java:1312)                                                                                                                                                                                  ");
        sb.append("gtu._work.ui.FastDBQueryUI.access$11(FastDBQueryUI.java:1245)                                                                                                                                                                                              ");
        sb.append("gtu._work.ui.FastDBQueryUI$12.actionPerformed(FastDBQueryUI.java:422)                                                                                                                                                                                      ");
        sb.append("javax.swing.AbstractButton.fireActionPerformed(AbstractButton.java:2022)                                                                                                                                                                                   ");
        sb.append("</html>                                                                                                                                                                                                                                                    ");
        sb.append("                                                                                                                                                                                                                                                           ");
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
