package gtu.db.jdbc;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class CallableStatementTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }

    private void callStoreProc(Connection conn, String contractID, String loginUserName) throws SQLException {
        String proCode = ("{call VC_PKG.P_CREATE_CONTRACT_REGION(?,?) }");
        CallableStatement cstmt = conn.prepareCall(proCode);
        cstmt.setString(1, contractID);
        cstmt.setString(2, loginUserName);
        cstmt.execute();
        cstmt.close();
    }

    private void callStoreProc1(Connection conn) throws SQLException {
        CallableStatement cstmt = null;
        cstmt = conn.prepareCall("{?=call GETDATABETWEENDATE(?)}");
        cstmt = conn.prepareCall("{call ?:= GETDATABETWEENDATE(?)}");
        cstmt.registerOutParameter(1, Types.ARRAY);
        cstmt.setString(2, "LASTWEEK");
        ResultSet rs = cstmt.executeQuery();
        if (rs.next()) {
            int i = rs.getInt(1);
        }
    }

    private void callStoreProc2(Connection conn) throws SQLException {
        CallableStatement cstmt = null;
        cstmt = conn.prepareCall("{ ? = call CreatSearchRingID }");
        cstmt = conn.prepareCall("{call ? :=  CreatSearchRingID }");
        cstmt.registerOutParameter(1, Types.VARCHAR);
        cstmt.execute();
        String rst = cstmt.getString(1);
    }

    private void callStoreProc3(Connection conn) throws SQLException {
        Object[] rtn = null;
        String sql = " begin erp_pkg.transfer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end; ";
        CallableStatement cstmt = null;
        try {
            cstmt = conn.prepareCall(sql);
            int pos = 1;
            cstmt.registerOutParameter(16, java.sql.Types.INTEGER);
            cstmt.registerOutParameter(17, java.sql.Types.VARCHAR);
            cstmt.setString(pos++, "v_user_id              ");
            cstmt.setString(pos++, "v_org                  ");
            cstmt.setString(pos++, "v_subinventory         ");
            cstmt.setString(pos++, "v_locator_id           ");
            cstmt.setString(pos++, "v_equip_id             ");
            cstmt.setString(pos++, "v_transfer_org         ");
            cstmt.setString(pos++, "v_transfer_subinvtory  ");
            cstmt.setString(pos++, "v_transfer_locator_id  ");
            cstmt.setString(pos++, "v_transfer_equip_id    ");
            cstmt.setString(pos++, "v_inventory_item_id    ");
            cstmt.setString(pos++, "v_item_no              ");
            cstmt.setInt(pos++, 5);
            cstmt.setString(pos++, "v_serial_no            ");
            cstmt.setString(pos++, "v_note                 ");
            cstmt.setString(pos++, "v_is_all_same          ");
            cstmt.setInt(pos++, 5);
            cstmt.setString(pos++, "run_error              ");
            cstmt.execute();
            rtn = new Object[] { cstmt.getInt(16), cstmt.getString(17) };
        } catch (Exception ex) {
            rtn = new Object[] { 1, ex.getMessage() };
        }
    }

    private void callStoreProc4(Connection conn) throws SQLException {
        CallableStatement cstmt = null;
        cstmt = conn.prepareCall("begin PLN_PKG.cloneprojectcate(?,?,?,?); end;");
        cstmt.setString(1, "XXXXXX");
        cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
        cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
        cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
        cstmt.execute();
        String newId = cstmt.getString(2);
        int errCode = cstmt.getInt(3);
        String errMsg = cstmt.getString(4);
    }

    private String[] callStoreFunc5(Connection conn, String masterID, String appID) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{ ? = call OWLET25.CO_PKG.F_CHECK_BTS_READY(?,?) }");
        cstmt.registerOutParameter(1, Types.ARRAY, "ERROR_MSG");
        cstmt.setString(2, masterID);
        cstmt.setString(3, appID);
        cstmt.executeUpdate();
        Array simpleArray = cstmt.getArray(1);
        String[] values = (String[]) simpleArray.getArray();
        cstmt.close();
        return values;
    }
}
