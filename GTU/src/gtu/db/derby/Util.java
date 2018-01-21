package gtu.db.derby;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/*
 * Embedded Apache Derby Demo - DerbyTax
 *
 * This class contains Utility methods.
 * 
 * @author francois.orsini@sun.com
 * @version 1.0
 * 
 */

public class Util {
	/*
	 * Generate XML String from a JDBC ResultSet
	 */
	public static String toXML(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		StringBuffer xml = new StringBuffer();

		xml.append("<ResultSet>");

		while (rs.next()) {
			xml.append("<Row>");

			for (int i = 1; i <= colCount; i++) {
				String columnName = rsmd.getColumnName(i);
				Object value = rs.getObject(i);
				xml.append("<" + columnName + ">");

				if (value != null) {
					xml.append(value.toString().trim());
				}
				xml.append("</" + columnName + ">");
			}
			xml.append("</Row>");
		}

		xml.append("</ResultSet>");

		return xml.toString();
	}
}
