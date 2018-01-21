package gtu.db.connectionPool;

import java.sql.*;
import java.io.Serializable;

/**
 * 存儲基本的資料庫連接Bean
 */
public class ConnBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1723634572169059912L;
	
	private Connection conn = null;
	private boolean inuse = false;

	public ConnBean() {
	}

	public ConnBean(Connection con) {
		if (con != null)
			conn = con;
	}

	public void setConnection(Connection con) {
		conn = con;
	}

	public Connection getConnection() {
		return conn;
	}

	public void setInuse(boolean inuse) {
		this.inuse = inuse;
	}

	public boolean getInuse() {
		return inuse;
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
		}
	}
}
