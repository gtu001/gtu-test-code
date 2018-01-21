package gtu.db.jdbc;
import java.sql.Connection;

import org.springframework.jdbc.datasource.DriverManagerDataSource;


public class DBTest {
    
    public static void main(String[] args) throws Exception {
        DriverManagerDataSource dbs = new DriverManagerDataSource();
        dbs.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dbs.setUrl("jdbc:oracle:thin:@PC-NEODB01:1521:CSPMS03");
        dbs.setUsername("owlet2");
        dbs.setPassword("owlet45");
        Connection conn = dbs.getConnection();
//        pc.processBySchedule("JOB000000935", conn);
//        DriverManagerDataSource dbs = new DriverManagerDataSource();
//        dbs.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        dbs.setUrl("jdbc:oracle:thin:@10.64.16.127:1530:CSIMS02T");
//        dbs.setUsername("owlet25");
//        dbs.setPassword("owleti5");
//        Connection conn = dbs.getConnection();
//        pc.processBySchedule("JOB000000935", conn);
    }
}
