package gtu.db.connectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class TestBean {
	public static void main(String[] args) {
		Connection conn = null;
		try {
			PoolBean poolbean = new PoolBean();
			poolbean.setDriver("com.mysql.jdbc.Driver");
			poolbean.setURL("jdbc:mysql://localhost:3306/ecweb");
			poolbean.setSize(10);
			poolbean.setUsername("root");
			poolbean.setPassword("");
			poolbean.initializePool();
			conn = poolbean.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();

			}
		}
	}
}
