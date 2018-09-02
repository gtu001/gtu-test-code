package _temp;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

public class Test_DataSource {

    public static void main(String[] args) {
        JdbcTemplate t = new JdbcTemplate(UnitTestDataSourceFactory.getDataSource());
        
        int val = t.queryForInt("select 1 from dual");
        
        System.out.println(val);
    }

    static final DataSourceFactory UnitTestDataSourceFactory = new DataSourceFactory() {
        private DataSource dataSource;

        @Override
        public ConnectionProperties getConnectionProperties() {
            return null;
        }

        @Override
        public DataSource getDataSource() {
            if (dataSource == null) {
                BasicDataSource bds = new BasicDataSource();
                String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=122.116.167.154)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=IBTDCS1)))";
                String username = "sysadm";
                String password = "123456";
                String driverClass = "oracle.jdbc.driver.OracleDriver";
                bds.setUrl(url);
                bds.setUsername(username);
                bds.setPassword(password);
                bds.setDriverClassName(driverClass);
                dataSource = bds;
            }
            return dataSource;
        }

    };
}
