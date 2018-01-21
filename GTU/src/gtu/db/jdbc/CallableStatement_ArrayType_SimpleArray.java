package gtu.db.jdbc;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.jfree.util.Log;

import com.mysql.jdbc.jdbc2.optional.ConnectionWrapper;

public class CallableStatement_ArrayType_SimpleArray {
    /*
    create or replace type number_array is table of number;
  
    procedure p_pos_refund_to_arap_4java(i_fee_id_array in number_array,
                                   i_operator_id IN NUMBER,
                                   i_policy_id in t_prem_arap.policy_id%type,
                                   i_pay_mode in t_prem_arap.pay_mode%type,
                                   i_withdraw_type in t_prem_arap.withdraw_type%type,
                                   i_payee_id in t_prem_arap.payee_id%type,
                                   i_chg_id in t_prem_arap.change_id%type,
                                   i_policy_chg_id in t_prem_arap.policy_chg_id%type
                                   )
    as
      v_cash_array PKG_LS_ARAP_CI_DT.cash_array := PKG_LS_ARAP_CI_DT.cash_array();
      v_fee_id number;
      v_cash PKG_LS_ARAP_CI_DT.cash;
      v_index number := 1;
    begin
      for fn in i_fee_id_array.first .. i_fee_id_array.last loop
        v_fee_id := i_fee_id_array(fn);
       
        select fee_id , pay_balance , pay_mode , prem_purpose , policy_id
          into v_cash.FEE_ID, v_cash.PAY_BALANCE, v_cash.PAY_MODE, v_cash.PREM_PURPOSE, v_cash.POLICY_ID
          from t_cash
         where fee_id = v_fee_id;
       
        v_cash_array.extend();
        v_cash_array(v_index) := v_cash;
        v_index := v_index + 1;
      end loop;
     
      p_pos_refund_to_arap(v_cash_array,
                                   i_operator_id,
                                   i_policy_id,
                                   i_pay_mode,
                                   i_withdraw_type,
                                   i_payee_id,
                                   i_chg_id,
                                   i_policy_chg_id);
    end p_pos_refund_to_arap_4java;
 */

    /**
     * 保全懸帳退費
     */
    public void posRefundToArap(List<CashVO> voList, Long UserId, Long PolicyId, Integer PayMode, String withdrawType, Long payeeId, Long chgId, Long policyChgId) throws GenericException {
        Connection con = null;
        CallableStatement cs = null;
        DBean db = new DBean();
        try {
            db.connect();
            con = db.getConnection();

            // 取得oracle connection
            ConnectionWrapper conn2 = (ConnectionWrapper) con;
            Field f = conn2.getClass().getDeclaredField("connection");
            f.setAccessible(true);
            Connection conn23 = (Connection) f.get(conn2);

            // 組feeId Array
            Object[] structs = new Object[voList.size()];
            for (int ii = 0; ii < voList.size(); ii++) {
                structs[ii] = voList.get(ii).getFeeId();
            }
            ArrayDescriptor arraydesc = new ArrayDescriptor("NUMBER_ARRAY", conn23);
            ARRAY list = new ARRAY(arraydesc, con, structs);

            cs = con.prepareCall("{call PKG_LS_ARAP_CI.p_pos_refund_to_arap_4java(?,?,?,?,?,?,?,?)}");
            cs.setArray(1, list);
            cs.setLong(2, UserId);// i_operator_id
            cs.setLong(3, PolicyId);// i_policy_id
            cs.setLong(4, PayMode);// i_pay_mode
            cs.setString(5, withdrawType);// i_withdraw_type
            cs.setLong(6, payeeId);// i_payee_id
            cs.setLong(7, chgId);// i_chg_id
            cs.setLong(8, policyChgId);// i_policy_chg_id
            boolean result = cs.execute();
            Log.info(this.getClass(), "# posRefundToArap :" + result);
        } catch (Exception ex) {
            Log.error(this.getClass(), ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            DBean.closeAll(null, cs, db);
        }
    }
}
