package gtu.db.access.exp1;

/* 模型組件 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Troy 2009/02/02
 * 
 */
public class loginHandler {
    public loginHandler() {
    }

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    // 檢查是否已注冊
    public boolean checkLogin(ArrayList arr) {
        // 從數據訪問組件dbPool中取得連接
        conn = dbPool.getConnection();

        String name = (String) arr.get(0);
        String password = (String) arr.get(1);

        try {
            String sql = "select * from T_UserInfo where username=? and password=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                // 釋放資源
                dbPool.dbClose(conn, ps, rs);
                return true;
            } else {
                dbPool.dbClose(conn, ps, rs);
                return false;
            }

        } catch (SQLException e) {
            return false;
        }
    }

}
