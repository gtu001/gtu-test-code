package _temp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ebao.foundation.common.resource.EnvResourceManager;
import com.ebao.foundation.module.db.DBean;
import com.ebao.foundation.module.db.jdbc.datasource.DataSourceConstant;

public class Test2 {

    public static void main(String[] args) throws Exception {
        System.out.println(EnvResourceManager.getValue(DataSourceConstant.DATASOURCE_TYPE));
        System.out.println(EnvResourceManager.getValue(DataSourceConstant.PASSWORD));
        System.out.println(EnvResourceManager.getValue(DataSourceConstant.URL));
        System.out.println(EnvResourceManager.getValue(DataSourceConstant.USERNAME));
        System.out.println(EnvResourceManager.getValue(DataSourceConstant.DRIVER_CLASS));

        DBean dbean = new DBean();
        dbean.connect();
        
        Connection conn = dbean.getConnection();

        StringBuilder sb = new StringBuilder();
        sb.append(" select *                                        ");
        sb.append("   from t_pa_seal_record                         ");
        sb.append("  where send_list_id is not null                 ");
        sb.append("    and send_date is not null                    ");
        sb.append("    and send_seq is not null                     ");
        sb.append("    and send_no is not null                      ");
        sb.append("    and rcpt_list_id is null                     ");
        sb.append("    and account_type = 2                         ");
        sb.append("    and approval_status = 3                      ");
        sb.append("    and bank_code = '009'                        ");
        sb.append("    and bank_account = '92835106268700'          ");

        PreparedStatement stmt = conn.prepareStatement(sb.toString());
        
        ResultSet rs = stmt.executeQuery();
        
        
        while(rs.next()){
            System.out.println(rs.getString("LIST_ID"));
        }
         
        rs.close();
        stmt.close();
        conn.close();
        
        System.out.println("done...");
    }
}