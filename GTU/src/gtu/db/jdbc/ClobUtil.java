package gtu.db.jdbc;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

public class ClobUtil {
    /** 取得Clob中的字串 */
    public static String getClobText(Clob clb) throws Exception {
        char clobVal[] = new char[(int) clb.length()];
        Reader r = clb.getCharacterStream();
        r.read(clobVal);
        String str = new String();
        String a = String.copyValueOf(clobVal);
        // return new String(clobVal);
        return a;
    }

    public static char[] getClobTextEx(Clob clb) throws Exception {
        char clobVal[] = new char[(int) clb.length()];
        Reader r = clb.getCharacterStream();
        r.read(clobVal);
        return clobVal;
    }

    public static String getClobToStr(Clob clb) {
        String text = "";
        try {
            text = new String(IOUtils.toByteArray(clb.getCharacterStream(), "big5"), "big5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
    
    public static Clob createClob(String valueStr, Connection conn) {
        try {
            Clob clob = conn.createClob();
            clob.setString(1, valueStr);
            return clob;
        } catch (Exception e) {
            throw new RuntimeException("createClob err : " + e.getMessage(), e);
        }
    }
    
    public static void setClobData(String key, int pos, String valueStr, PreparedStatement stmt) throws SQLException {
        stmt.setCharacterStream(pos, new StringReader(valueStr));
    }
}
