package gtu.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public abstract class BaseBatch {

	protected Logger logger = Logger.getLogger(this.getClass());
	protected boolean autoCommit;
	static String url = "", driverClassName = "", username = "", password = "";

	static {
		ResourceBundle resource = ResourceBundle.getBundle("connectinfo");
		url = resource.getString("url");
		driverClassName = resource.getString("driverClassName");
		username = resource.getString("username");
		password = resource.getString("password");

	}

	public BaseBatch() {
		try {
			Class.forName(driverClassName);
			this.con = DriverManager.getConnection(url, username, password);
			con.setAutoCommit(autoCommit);
		} catch (Exception e) {
			logger.error(e.getMessage());
			StackTraceElement stacks[] = e.getStackTrace();
			for (StackTraceElement stack : stacks) {
				logger.error(stack.toString());
			}
			System.exit(-1);
		}
	}

	private Connection con = null;

	public Connection getConnection() throws Exception {
		if (con == null || con.isClosed())
			try {
				this.con = DriverManager.getConnection(url, username, password);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
		return this.con;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public boolean getAutoCommit() {
		return autoCommit;
	}

	public void rollback() throws SQLException {
		con.rollback();
	}

	public void commit() throws SQLException {
		con.commit();
	}

	public void close() throws SQLException {
		con.close();
	}

	/**
	 * 只供查詢使用
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> query(String sql, Object param[]) throws Exception {
		logger.info("sql:" + sql);
		List<Map<String, Object>> rsList = new ArrayList<>();
		java.sql.ResultSet rs = null;
		Connection con = getConnection();
		try (java.sql.PreparedStatement ps = con.prepareStatement(sql);) {

			for (int i = 0; i < param.length; i++) {
				logger.info("param[" + i + "]:" + param[i]);
				ps.setObject(i + 1, param[i]);
			}

			rs = ps.executeQuery();
			java.sql.ResultSetMetaData mdata = rs.getMetaData();
			int cols = mdata.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				for (int i = 1; i <= cols; i++) {
					map.put(mdata.getColumnName(i), rs.getObject(i));
				}
				rsList.add(map);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			StackTraceElement stacks[] = e.getStackTrace();
			for (StackTraceElement stack : stacks) {
				logger.error(stack.toString());
			}
			throw e;// 為了讓外面的 rollback 機制知道有錯誤發生必須把錯誤物件往外拋
		} finally {
			rs.close();
		}

		return rsList;
	}

	/**
	 * 可以供新增修改刪除使用
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int modify(String sql, Object param[]) throws Exception {
		int rsCount = 0;
		logger.info("sql:" + sql);
		Connection con = getConnection();
		try (java.sql.PreparedStatement ps = con.prepareStatement(sql);) {
			for (int i = 0; i < param.length; i++) {
				logger.info("param[" + i + "]:" + param[i]);
				ps.setObject(i + 1, param[i]);
			}
			rsCount = ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage());
			StackTraceElement stacks[] = e.getStackTrace();
			for (StackTraceElement stack : stacks) {
				logger.error(stack.toString());
			}
			throw e;// 為了讓外面的 rollback 機制知道有錯誤發生必須把錯誤物件往外拋
		}

		return rsCount;
	}

	/**
	 * 只供新增使用 並回傳回新增後所產生的 key 值
	 * 
	 * @param sql
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public java.math.BigDecimal insert(String sql, Object param[]) throws Exception {
		java.math.BigDecimal key = java.math.BigDecimal.ZERO;
		logger.info("sql:" + sql);
		java.sql.ResultSet rs = null;
		Connection con = getConnection();
		try (java.sql.PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
			for (int i = 0; i < param.length; i++) {
				logger.info("param[" + i + "]:" + param[i]);
				ps.setObject(i + 1, param[i]);
			}

			rs = ps.getGeneratedKeys();
			while (rs.next()) {
				key = rs.getBigDecimal(1);
				// Get automatically generated key
				// value
				logger.info("Automatically generated key value = " + key);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			StackTraceElement stacks[] = e.getStackTrace();
			for (StackTraceElement stack : stacks) {
				logger.error(stack.toString());
			}
			throw e;// 為了讓外面的 rollback 機制知道有錯誤發生必須把錯誤物件往外拋
		} finally {
			rs.close();
		}

		return key;
	}

	/**
	 * select 時需要直接對 ResultSet 物件 直接做資料異動時 使用
	 * 
	 * @param sql
	 * @param param
	 * @param target
	 * @throws Exception
	 */
	public void query(String sql, Object param[], IQuery target) throws Exception {
		logger.info("sql:" + sql);

		java.sql.ResultSet rs = null;
		Connection con = getConnection();
		try (java.sql.PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);) {

			for (int i = 0; i < param.length; i++) {
				logger.info("param[" + i + "]:" + param[i]);
				ps.setObject(i + 1, param[i]);
			}

			rs = ps.executeQuery();
			target.run(rs);

		} catch (Exception e) {
			logger.error(e.getMessage());
			StackTraceElement stacks[] = e.getStackTrace();
			for (StackTraceElement stack : stacks) {
				logger.error(stack.toString());
			}
			throw e;// 為了讓外面的 rollback 機制知道有錯誤發生必須把錯誤物件往外拋
		} finally {
			rs.close();
		}

	}

	/**
	 * 未來實作此method 請不要加上 try catch 若有加上去記得要再把接到的Exception 再 throw 出來 以便做
	 * rollback 控制
	 * 
	 * @param arg
	 * @throws Exception
	 */
	protected abstract void execute(String arg[]) throws Exception;

	public void run(String arg[]) {
		logger.info("start");
		try {
			execute(arg);
			this.commit();// 預設autocommit 為false 所以 要記得呼叫
			logger.info("commit");
			this.close();// 關閉資料庫連線也要記得呼叫
		} catch (Exception e) {
			logger.error(e.getMessage(), e);//TODO 勿上傳
			StackTraceElement stacks[] = e.getStackTrace();
			for (StackTraceElement stack : stacks) {
				logger.error(stack.toString());
			}
			try {
				logger.info("rollback");
				this.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}
		}

		logger.info("end");
	}

	/**
	 * N天後之營業日
	 * <p>
	 * 起日起 n 天的日期
	 * <p>
	 * 所謂工作日是指有上班日期
	 * <p>
	 * 例 20160311 為週五， 3 個工作天後是那一日，結果是 20160316
	 * 
	 * @param oriDate
	 *            起日（yyyyMMdd）
	 * @param addDays
	 *            工作日數（可正值或負值）
	 * @return 迄日（yyyyMMdd）
	 * @throws Exception
	 *             為了讓 rollback 機制知道有錯誤發生必須把錯誤物件往外拋
	 */
	protected String increaseDays(String oriDate, BigDecimal addDays) throws Exception {
		String sql = null;
		Object[] param = null;
		List<Map<String, Object>> resultMaps = null;
		Map<String, Object> resultMap = null;
		// 驗證起日是否為正確的日期格式，若不正確時拋出例外
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		simpleDateFormat.setLenient(false);
		simpleDateFormat.parse(oriDate);
		// 若工作日數為零則返回起日
		if (addDays.equals(BigDecimal.ZERO)) {
			return oriDate;
		}
		// 設定步進前的日期為起日
		String h_comm_temp_holiday = oriDate;
		// 若工作日數是正數則讓迴圈每次步進正一，若工作日數是負數則讓迴圈每次步進負一
		int calInt = (addDays.compareTo(BigDecimal.ZERO) > 0) ? 1 : -1;
		int needDays = 0;
		// 若 needDays 滿足工作日數時迴圈結束
		while (needDays != addDays.intValue()) {
			// 此 SQL 查詢使步進前的日期依照 calInt 的值來步進
			sql = "SELECT TO_CHAR(TO_DATE(?, 'yyyymmdd') + ?, 'yyyymmdd') AS h_comm_new_holiday FROM dual";
			param = new Object[] { h_comm_temp_holiday, calInt };
			resultMap = query(sql, param).get(0);
			// 設定步進後的日期為以上 SQL 查詢的結果
			String h_comm_new_holiday = (String) resultMap.get("H_COMM_NEW_HOLIDAY");
			// 此 SQL 查詢從 ptr_holiday 資料表取出「例假日」欄位的資料
			sql = "SELECT holiday FROM ptr_holiday WHERE holiday = ?";
			param = new Object[] { h_comm_new_holiday };
			resultMaps = query(sql, param);
			// 若 resultMaps 的大小為零代表今天不是例假日，此時 needDays 依照 calInt 的值步進
			if (resultMaps.size() == 0) {
				needDays += calInt;
			}
			// 讓下一次迴圈從本次迴圈步進後的日期開始步進
			h_comm_temp_holiday = h_comm_new_holiday;
		}
		return h_comm_temp_holiday;
	}
}
