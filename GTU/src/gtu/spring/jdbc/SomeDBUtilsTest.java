package gtu.spring.jdbc;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class SomeDBUtilsTest {

    public static void main(String[] args) {
        DataSourceUtils.releaseConnection(null, null);
        JdbcUtils.closeConnection(null);
        // 請看API
    }
}
