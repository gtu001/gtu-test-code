package gtu.spring.jdbc;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DriverManagerDataSourceTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        datasource.setUrl("jdbc:oracle:thin:@192.168.156.140:1521:CSIMS");
        datasource.setUsername("OWLET25");
        datasource.setPassword("OWLET25");
    }
}
