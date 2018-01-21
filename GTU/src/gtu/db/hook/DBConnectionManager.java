package gtu.db.hook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Troy 2009/02/02
 * 
 */
public class DBConnectionManager {
    // Log4jInit logger = new Log4jInit(DBConnectionPool.class);

    static private DBConnectionManager instance; // 唯一實例
    static private int clients;

    private Vector drivers = new Vector();
    private PrintWriter log;
    private Hashtable pools = new Hashtable();

    /**
     * 返回唯一實例.如果是第一次調用此方法,則創建實例
     * 
     * @return DBConnectionManager 唯一實例
     */
    static synchronized public DBConnectionManager getInstance() {
        System.out.println("#syn DBConnectionManager");
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        clients++;
        return instance;
    }

    /**
     * 建構函數私有以防止其它對象創建本類實例
     */
    private DBConnectionManager() {
        System.out.println("#init DBConnectionManager");
        init();
    }

    /**
     * 將連接對象返回給由名字指定的連接池
     * 
     * @param name
     *            在屬性文件中定義的連接池名字
     * @param con
     *            連接對象
     */
    public void freeConnection(String name, Connection con) {
        System.out.println("#freeConnection");
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            pool.freeConnection(con);
        }
    }

    /**
     * 穫得一個可用的(空閒的)連接.如果沒有可用連接,且已有連接數小於最大連接數 限制,則創建並返回新連接
     * 
     * @param name
     *            在屬性文件中定義的連接池名字
     * @return Connection 可用連接或null
     */
    public Connection getConnection(String name) {
        System.out.println("#getConnection(n)");
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);

        if (pool != null) {
            return pool.getConnection();
        }
        return null;
    }

    /**
     * 穫得一個可用連接.若沒有可用連接,且已有連接數小於最大連接數限制, 則創建並返回新連接.否則,在指定的時間內等待其它線程釋放連接.
     * 
     * @param name
     *            連接池名字
     * @param time
     *            以毫秒計的等待時間
     * @return Connection 可用連接或null
     */
    public Connection getConnection(String name, long time) {
        System.out.println("#getConnection(n,t)");
        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if (pool != null) {
            return pool.getConnection(time);
        }
        return null;
    }

    /**
     * 關閉所有連接,撤銷驅動程式的註冊
     */
    public synchronized void release() {
        // 等待直到最後一個用戶程式調用
        System.out.println("#release");
        if (--clients != 0) {
            return;
        }

        Enumeration allPools = pools.elements();
        while (allPools.hasMoreElements()) {
            DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
            pool.release();
        }
        Enumeration allDrivers = drivers.elements();
        while (allDrivers.hasMoreElements()) {
            Driver driver = (Driver) allDrivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log(" deregisterDriver  " + driver.getClass().getName() + " ");
            } catch (SQLException e) {
                log(e, " SQLException: " + driver.getClass().getName());
            }
        }
    }

    /**
     * 根據指定屬性創建連接池實例.
     * 
     * @param props
     *            連接池屬性
     */
    private void createPools(Properties props) {
        System.out.println("#createPools");
        Enumeration propNames = props.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".url")) {
                String poolName = name.substring(0, name.lastIndexOf("."));

                String url = props.getProperty(poolName + ".url");
                if (url == null) {
                    log("沒有為連接池" + poolName + "指定URL");
                    continue;
                }
                String user = props.getProperty(poolName + ".user");

                String password = props.getProperty(poolName + ".password");
                String dbip = props.getProperty(poolName + ".db_ip", "192.168.96.1");
                String dbport = props.getProperty(poolName + ".db_port", "1521");
                String dbuid = props.getProperty(poolName + ".db_uid", "ORACLE9I");
                String maxconn = props.getProperty(poolName + ".maxconn", "0");

                // 連接資訊
                String dbInfo = user + "/" + password + "@" + dbip + ":" + dbport + ":" + dbuid; // not
                                                                                                 // use
                int max = 0;
                try {
                    // max = Integer.valueOf(maxconn).intValue();
                    max = Integer.parseInt(maxconn);

                } catch (NumberFormatException e) {
                    log("錯誤的最大連接數限制: " + maxconn + " .連接池: " + poolName);
                    max = 0;
                }
                DBConnectionPool pool = new DBConnectionPool(poolName, url, user, password, max);
                pools.put(poolName, pool);
                log("success create pool " + poolName);
            }
        }
    }

    /**
     * 讀取屬性完成初始化
     */
    private void init() {
        System.out.println("#init");
        String fileName = new String();
        Properties dbProps = new Properties();
        try {
            if (System.getProperties().containsKey("db.properties")) {
                fileName = System.getProperties().getProperty("db.properties");
                dbProps.load(new FileInputStream(fileName));
            } else {
                InputStream is = getClass().getResourceAsStream("db.properties");
                dbProps.load(is);
            }
        } catch (Exception e) {
            System.err.println("不能讀取屬性文件. 請確保db.properties在CLASSPATH指定的路徑中");
            return;
        }
        String logFile = dbProps.getProperty("logfile", "newslog.txt");
        try {
            log = new PrintWriter(new FileWriter(logFile, true), true);
        } catch (IOException e) {
            System.err.println("無法打開日誌文件: " + logFile);
            log = new PrintWriter(System.err);
        }
        loadDrivers(dbProps);
        createPools(dbProps);
    }

    /**
     * 裝載和註冊所有JDBC驅動程式
     * 
     * @param props
     *            屬性
     */
    private void loadDrivers(Properties props) {
        System.out.println("#loadDrivers");
        String driverClasses = props.getProperty("driver");
        StringTokenizer st = new StringTokenizer(driverClasses);
        while (st.hasMoreElements()) {
            String driverClassName = st.nextToken().trim();
            try {
                Driver driver = (Driver) Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(driver);
                drivers.addElement(driver);
                log(" Success Reg JDBC Driver" + driverClassName);
            } catch (Exception e) {
                log("無法註冊JDBC驅動程式: " + driverClassName + ", 錯誤: " + e);
            }
        }
    }

    /**
     * 將文本資訊寫入日誌文件
     */
    private void log(String msg) {
        System.out.println("#log");
        log.println(new Date() + ": " + msg);
    }

    /**
     * 將文本資訊與異常寫入日誌文件
     */
    private void log(Throwable e, String msg) {
        System.out.println("log(e)");
        log.println(new Date() + ": " + msg);
        e.printStackTrace(log);
    }

}
