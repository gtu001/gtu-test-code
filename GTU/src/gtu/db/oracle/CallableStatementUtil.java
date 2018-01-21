package gtu.db.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 依據驗收Item所屬儲位決定為物料移轉, 第二維修商物料移轉, 或是雜收/雜發
 * 1.  ERP_Accept_type = 01, 則進行雜收/雜發
 * 2.  ERP_Accept_type =02, 則進行儲位移轉
 * 3.  ERP_Accept_type=03, 則將送修件移至第二維修商儲位, 驗收件由第二維修商移至目的儲位
 * 4.  若驗收Item所屬儲位非以上三項, 則顯示錯誤訊息,不可驗收
 *    
 * @author Troy
 * @version 1.0, 2010/8/5
 */
public class CallableStatementUtil  {

    private static final long serialVersionUID = 1L;
    
    /**
     * 驗收單 - 雜發
     */
    private Object[] doMiscIssue(String v_user_id, String v_org, String v_subinventory_code, String v_locator_id, String v_equipment_id, String v_inventory_item_id, String v_item_no, String v_serial_no,
            String v_note, String v_is_all_same, String v_rmano, String v_rmano_relation, String v_is_new_rmano, String v_continue_exchg, int run_result, String run_error, Connection conn)
            throws Exception {
        String sql = " begin erp_pkg.misc_issue(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end; ";         
//        CallableStatement cstmt = conn.prepareCall(sql);
        CallableStatementTest cstmt = new CallableStatementTest(sql, conn);
        int pos = 1;
        cstmt.registerOutParameter(15, java.sql.Types.INTEGER);
        cstmt.registerOutParameter(14, java.sql.Types.VARCHAR);
        cstmt.registerOutParameter(16, java.sql.Types.VARCHAR);
        cstmt.setString(pos++, v_user_id             );
        cstmt.setString(pos++, v_org                 );
        cstmt.setString(pos++, v_subinventory_code   );
        cstmt.setString(pos++, v_locator_id          );
        cstmt.setString(pos++, v_equipment_id        );
        cstmt.setString(pos++, v_inventory_item_id   );
        cstmt.setString(pos++, v_item_no             );
        cstmt.setString(pos++, v_serial_no           );
        cstmt.setString(pos++, v_note                );
        cstmt.setString(pos++, v_is_all_same         );
        cstmt.setString(pos++, v_rmano               );
        cstmt.setString(pos++, v_rmano_relation      );
        cstmt.setString(pos++, v_is_new_rmano        );
        cstmt.setString(pos++, v_continue_exchg      );
        cstmt.setInt   (pos++, run_result            );
        cstmt.setString(pos++, run_error             );
        cstmt.execute();        
        Object[] rtn = new Object[] {cstmt.getInt(15), cstmt.getString(14), cstmt.getString(16)};
        cstmt.close();
        return rtn;
    }
    
    private static class CallableStatementTest {
        private Map<Integer, Object> params = new TreeMap<Integer, Object>();
        private String sql;
        private java.sql.CallableStatement stmt;
        public CallableStatementTest(String sql, Connection conn) throws SQLException {
            this.sql = sql;
            this.stmt = conn.prepareCall(sql);
        }
        public void setString(int ii, String str) throws SQLException {
            params.put(ii, str);
            stmt.setString(ii, str);
        }
        public void setInt(int ii, int val) throws SQLException {
            params.put(ii, val);
            stmt.setInt(ii, val);
        }
        public String getString(int ii) throws SQLException {
            return stmt.getString(ii);
        }
        public Integer getInt(int ii) throws SQLException {
            return stmt.getInt(ii);
        }
        public void registerOutParameter(int ii, int val) throws SQLException {
            stmt.registerOutParameter(ii, val);
        }
        public void execute() throws SQLException {
            Object[] args = new Object[params.size()];
            args = params.values().toArray(args);
            String[] args1 = new String[params.size()];
            for(int ii = 0 ; ii < args.length; ii ++) {
                if(args[ii] == null) {
                    continue;
                }else if(args[ii] instanceof Integer) {
                    args1[ii] = String.valueOf((Integer)args[ii]);
                }else {
                    args1[ii] = "'" + ((String)args[ii]).trim() + "'";
                }
            }
            StringBuilder ssql = new StringBuilder();
            for(char c : sql.toCharArray()) {
                if(c == '?') {
                    ssql.append("%s");
                }else {
                    ssql.append(c);
                }
            }
            String ss = String.format(ssql.toString(), args1);
            this.debug(ss);
            stmt.execute();
            return;
        }
        private void debug(String ss) {
            System.out.println(ss);
        }
        public void close() throws SQLException {
            stmt.close();
            return;
        }
    }
}
