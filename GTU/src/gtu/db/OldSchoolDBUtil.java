package gtu.db;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class OldSchoolDBUtil {
	private static final Logger logger = Logger.getLogger(OldSchoolDBUtil.class);
	
	public List<Map<String, Object>> queryForMap(String sql, List<Object> paramList, Connection conn)
			throws SQLException {
		List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
		java.sql.ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int ii = 0; ii < paramList.size(); ii++) {
				stmt.setObject(ii + 1, paramList.get(ii));
			}

			rs = stmt.executeQuery();
			java.sql.ResultSetMetaData mdata = rs.getMetaData();
			int cols = mdata.getColumnCount();
			List<String> colList = new ArrayList<String>();
			for (int i = 1; i <= cols; i++) {
				colList.add(mdata.getColumnName(i).toUpperCase());
			}

			while (rs.next()) {
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				for (String col : colList) {
					map.put(col, rs.getObject(col));
				}
				rsList.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				stmt.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return rsList;
	}
	
	public <T> List<T> toBeanList(List<Map<String,Object>> lst, Class<T> clz) {
		List<T> rtnLst = new ArrayList<T>();
		for(Map<String,Object> m : lst){
			try {
				T obj = clz.newInstance();
				mapToBean(m, obj);
				rtnLst.add(obj);
			} catch (Exception e) {
				throw new RuntimeException("初始化失敗  :" + clz, e);
			}
		}
		return rtnLst;
	}

	private String dbFieldToJava(String columnName) {
		String[] values = columnName.trim().toLowerCase().split("([-|_|.])");
		StringBuffer sb = new StringBuffer();
		for (int ii = 0; ii < values.length; ii++) {
			if (ii == 0 || values[ii].length() == 0) {
				sb.append(values[ii]);
			} else {
				sb.append(values[ii].substring(0, 1).toUpperCase() + values[ii].substring(1));
			}
		}
		return sb.toString();
	}

	private <T> void mapToBean(Map<String, Object> map, T t) {
		Class clz = t.getClass();
		for (String k : map.keySet()) {
			String javaName = dbFieldToJava(k);
			Field f = null;
			try {
				f = clz.getDeclaredField(javaName);
				f.setAccessible(true);
				f.set(t, map.get(k));
			} catch (java.lang.IllegalArgumentException e) {
				if(map.get(k).getClass() == BigDecimal.class && f.getType() == int.class){
					Integer val = ((BigDecimal)map.get(k)).intValue();
					try {
						f.set(t, val);
					} catch (Exception e1) {
					}
				}else if(map.get(k).getClass() == BigDecimal.class && f.getType() == long.class){
					Long val = ((BigDecimal)map.get(k)).longValue();
					try {
						f.set(t, val);
					} catch (Exception e1) {
					}
				}else {
					logger.error("field not found : " + t.getClass().getSimpleName() + "." + javaName, e);
				}
			} catch (Exception e) {
				boolean findOk = false;
				for (Field f2 : clz.getDeclaredFields()) {
					if (StringUtils.equalsIgnoreCase(f2.getName(), javaName)) {
						f2.setAccessible(true);
						try {
							f2.set(t, map.get(k));
							findOk = true;
						} catch (Exception e1) {
							logger.error("field not found[2] : " + t.getClass().getSimpleName() + "." + javaName, e);
						}
					}
				}
				if(!findOk){
					logger.error("field not found : " + t.getClass().getSimpleName() + "." + javaName, e);	
				}
			}
		}
	}
}