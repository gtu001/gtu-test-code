package gtu.db.hook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

//import org.apache.log4j.Logger;
//import org.apache.log4j.Category;

/**
 * @author Troy 2009/02/02
 * 
 */
class DBConnectionPool {
    // Log4jInit logger = new Log4jInit(DBConnectionPool.class);

    private int checkedOut;
    private Vector freeConnections = new Vector();
    private int maxConn;
    private String name;
    private String URL;
    private String dbInfo;
    private String userName;
    private String password;

    /**
     * 創建新的連接池
     * 
     * @param name
     *            連接池名字
     * @param URL
     *            資料庫的JDBC URL
     * @param dbInfo
     *            資料庫連接資訊
     * @param maxConn
     *            此連接池允許建立的最大連接數
     */
    public DBConnectionPool(String name, String URL, String userName, String password, int maxConn) {
        System.out.println("#DBConnectionPool\t" + name + "\t" + URL + "\t" + userName + "\t" + password + "\t"
                + maxConn);
        this.name = name;
        this.URL = URL;
        this.dbInfo = dbInfo;
        this.maxConn = maxConn;
        this.userName = userName;
        this.password = password;

    }

    /**
     * 將不再使用的連接返回給連接池
     * 
     * @param con
     *            用戶程式釋放的連接
     */
    public synchronized void freeConnection(Connection con) {
        // 將指定連接加入到向量末尾
        System.out.println("#freeConnection");
        freeConnections.addElement(con);
        checkedOut--;
        notifyAll();
    }

    /**
     * 從連接池穫得一個可用連接.如沒有空閒的連接且目前連接數小於最大連接 數限制,則創建新連接.如原來登記為可用的連接不再有效,則從向量刪除之,
     * 然後遞歸調用自己以嘗試新的可用連接.
     */
    public synchronized Connection getConnection() {
        System.out.println("#getConnection");
        Connection con = null;
        if (freeConnections.size() > 0) {
            // 穫取向量中第一個可用連接
            con = (Connection) freeConnections.firstElement();
            freeConnections.removeElementAt(0);
            try {
                if (con.isClosed()) {
                    log("From Conn Pool" + name + " Delete one unused conn");
                    // 遞歸調用自己,嘗試再次穫取可用連接
                    con = getConnection();
                }
            } catch (SQLException e) {
                log("From Conn Pool" + name + " Delete one unused conn");
                // 遞歸調用自己,嘗試再次穫取可用連接
                con = getConnection();
            }
        } else if (maxConn == 0 || checkedOut < maxConn) {
            con = newConnection();
        }
        if (con != null) {
            checkedOut++;
        }
        return con;
    }

    /**
     * 從連接池穫取可用連接.可以指定用戶程式能夠等待的最長時間 參見前一個getConnection()方法.
     * 
     * @param timeout
     *            以毫秒計的等待時間限制
     */
    public synchronized Connection getConnection(long timeout) {
        System.out.println("#getConnection(t)");
        long startTime = new Date().getTime();
        Connection con;
        while ((con = getConnection()) == null) {
            try {
                wait(timeout);
            } catch (InterruptedException e) {
            }

            if ((new Date().getTime() - startTime) >= timeout) {
                // wait()返回的原因是超時
                return null;
            }
        }
        return con;
    }

    /**
     * 關閉所有連接
     */
    public synchronized void release() {
        System.out.println("#release");
        Enumeration allConnections = freeConnections.elements();
        while (allConnections.hasMoreElements()) {
            Connection con = (Connection) allConnections.nextElement();
            try {
                con.close();
                log("close conn poll" + name + " one of connection");
            } catch (SQLException e) {
                log("cannot close" + name + " connection" + e);
            }
        }
        freeConnections.removeAllElements();
    }

    /**
     * 創建新的連接
     */
    private Connection newConnection() {
        System.out.println("#newConnection");
        Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            // Class.forName("org.gjt.mm.mysql.Driver").newInstance();

            String connectionUrl = URL + " user=" + userName + ";password=" + password;
            con = DriverManager.getConnection(connectionUrl);

            log("connection pool" + name + " create one connection");
        } catch (SQLException e) {
            log("cannot create conn : " + URL + e);
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (InstantiationException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
        return con;
    }

    /**
     * 將文本資訊寫入日誌文件
     */
    private void log(String msg) {
        System.out.println("#log");
        System.out.println(new Date() + ": " + msg);
    }

    /**
     * 將文本資訊與異常寫入日誌文件
     */
    private void log(Throwable e, String msg) {
        System.out.println("#log(e)");
        System.out.println(new Date() + ": " + msg);
        e.printStackTrace();
    }

}
