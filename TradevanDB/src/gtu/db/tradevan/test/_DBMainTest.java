package gtu.db.tradevan.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import gtu.db.tradevan.DBMain;

public class _DBMainTest {

    public static void main(String[] args) throws SQLException, IOException {
        _DBMainTest test = new _DBMainTest();
        test.testOrign();
        System.out.println("done...");
    }

    private void testOrign() {
        DBMain dbMain = getDBMain();
        dbMain.generateDO("select * from user_info", Arrays.asList(""), "user_info", null);

        List<UserInfoDO> queryList = getDBMain().namingQuery("select * from user_info", null, UserInfoDO.class);
        for (UserInfoDO dd : queryList) {
            System.out.println(dd);
        }

        queryList = dbMain.query("select * from user_info where user_id = 'liu001'", UserInfoDO.class);
        UserInfoDO user = queryList.get(0);

        dbMain.beginTransaction();
        user.setUserName("劉小娟");
        user.setAddress("士林");
        dbMain.update(user);
        dbMain.commit();
    }

    private static DBMain getDBMain() {
        DBMain dbMain = new DBMain();
//        DataSource dataSource = DbConstant.getTestDataSource_R5UAT();
        DataSource dataSource = null;
        dbMain.setDataSource(dataSource);
        return dbMain;
    }
}
