package gtu.db.connectionPool;

import java.sql.*;
import java.util.*;
import java.lang.InterruptedException;
import java.io.Serializable;

/**
 * 連接池管理Bean
 */
public class PoolBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6273627516913565832L;
	
	private String driver = null;
	private String url = null;
	private int size = 0;
	private String username = "";
	private String password = "";
	private ConnBean connBean = null;
	private Vector pool = null;
	private String dbType = "1";

	public PoolBean() {
	}

	public void setDriver(String d) {
		if (d != null)
			driver = d;
	}

	public String getDriver() {
		return driver;
	}

	public void setURL(String u) {
		if (u != null)
			url = u;
	}

	public String getURL() {
		return url;
	}

	public void setSize(int s) {
		if (s > 1)
			size = s;
	}

	public int getSize() {
		return size;
	}

	public void setUsername(String un) {
		if (un != null)
			username = un;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String pwd) {
		if (pwd != null)
			password = pwd;
	}

	public String getPassword() {
		return password;
	}

	public void setConnBean(ConnBean cb) {
		if (cb != null)
			connBean = cb;
	}

	public ConnBean getConnBean() throws Exception {
		Connection con = getConnection();
		ConnBean cb = new ConnBean(con);
		cb.setInuse(true);
		return cb;

	}

	private Connection createConnection() throws Exception {
		Connection con = null;
		con = DriverManager.getConnection(url, username, password);
		return con;
	}

	public synchronized void initializePool() throws Exception {
		if (driver == null)
			throw new Exception("沒有提供驅動程式名稱！");
		if (url == null)
			throw new Exception("沒有提供URL！");
		if (size < 1)
			throw new Exception("連接池大小不能小於一！");
		try {
			// DriverManager.registerDriver(new
			// COM.ibm.db2.jdbc.app.DB2Driver());
			Class.forName(driver).newInstance();

			for (int i = 0; i < size; i++) {
				Connection con = createConnection();
				if (con != null) {
					ConnBean cb = new ConnBean(con);
					addConnection(cb);

				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	private void addConnection(ConnBean cb) {
		if (pool == null)
			pool = new Vector(size);
		pool.addElement(cb);
	}

	public synchronized void releaseConnection(Connection con) {
		for (int i = 0; i < pool.size(); i++) {
			ConnBean cb = (ConnBean) pool.elementAt(i);
			if (cb.getConnection() == con) {
				System.err.println("釋放第" + i + "個連接");
				cb.setInuse(false);
				break;
			}
		}
	}

	public synchronized Connection getConnection() throws Exception {
		ConnBean cb = null;
		for (int i = 0; i < pool.size(); i++) {
			cb = (ConnBean) pool.elementAt(i);
			if (cb.getInuse() == false) {
				cb.setInuse(true);
				Connection con = cb.getConnection();
				if (!con.isClosed()) {
					return con;
				} else {
					pool.removeElement(cb);
				}
			}
		}
		try {
			Connection con = createConnection();
			cb = new ConnBean(con);
			cb.setInuse(true);
			pool.addElement(cb);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return cb.getConnection();
	}

	public synchronized void emptyPool() {
		for (int i = 0; i < pool.size(); i++) {
			System.err.println("關閉第" + i + "JDBC連接");
			ConnBean cb = (ConnBean) pool.elementAt(i);
			if (cb.getInuse() == false)
				cb.close();
			else {
				try {
					java.lang.Thread.sleep(20000);
					cb.close();
				} catch (InterruptedException ie) {
					System.err.println(ie.getMessage());
				}

			}
		}
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
