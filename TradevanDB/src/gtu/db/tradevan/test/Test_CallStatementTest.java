package gtu.db.tradevan.test;

import gtu.db.DbConstant;
import gtu.db.tradevan.DBExecuteCall_tradevan.CallParameter;
import gtu.db.tradevan.DBMain;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class Test_CallStatementTest {

    public static void main(String[] args){
        DBMain dbMain = new DBMain();
//        dbMain.setDataSource(DbConstant.getTestDataSource());
        
        List<CallParameter> inList = new ArrayList<CallParameter>();
        List<CallParameter> outList = new ArrayList<CallParameter>();
        
        outList.add(new CallParameter(1, null, java.sql.Types.VARCHAR));
        inList.add(new CallParameter(2, "t_yes_no", java.sql.Types.VARCHAR));
        inList.add(new CallParameter(3, null, java.sql.Types.VARCHAR));
        inList.add(new CallParameter(4, "Y", java.sql.Types.VARCHAR));
        
        dbMain.call("{call ? := pkg_ls_pa_query_policy.f_get_string_res(?,?,?)}", inList, outList);
    }

}
