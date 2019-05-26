package gtu.db;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;

public class ExternalJDBCDriverJarLoader {

    public List<File> jarLst = new ArrayList<File>();
    private URLClassLoader urlClassLoader = null;
    private Set<DriverShim> keeperSet = new HashSet<DriverShim>();

    private URLClassLoader createClassLoader() throws MalformedURLException {
        List<URL> urlLst = new ArrayList<URL>();
        for (File jar : jarLst) {
            if (!jar.isFile() || !jar.getName().toLowerCase().endsWith(".jar")) {
                continue;
            }
            urlLst.add(jar.toURL());
        }
        URLClassLoader loader = URLClassLoader.newInstance(//
                urlLst.toArray(new URL[0]), //
                Thread.currentThread().getContextClassLoader());
        return loader;
    }

    public void registerDriver(String className) {
        try {
            if (jarLst.isEmpty()) {
                System.out.println("沒有任何外部jar");
                return;
            }
            if (urlClassLoader == null) {
                urlClassLoader = createClassLoader();
            } else {
                for (DriverShim d : keeperSet) {
                    if (StringUtils.equals(d.driverClassName, className)) {
                        System.out.println("以註冊過driver : " + className);
                        return;
                    }
                }
            }
            Class clz = Class.forName(className, true, urlClassLoader);
            Class<? extends java.sql.Driver> clz2 = clz.asSubclass(java.sql.Driver.class);
            DriverShim driverShim = new DriverShim(clz2.newInstance());
            keeperSet.add(driverShim);
            DriverManager.registerDriver(driverShim);
        } catch (Exception ex) {
            throw new RuntimeException("registerDriver ERR : " + ex.getMessage(), ex);
        }
    }

    public URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    public boolean addJar(File jar) {
        if (!jar.isFile() || !jar.getName().toLowerCase().endsWith(".jar")) {
            return false;
        }
        if (!jarLst.contains(jar)) {
            jarLst.add(jar);
            urlClassLoader = null;
            keeperSet.clear();
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws MalformedURLException {
        ExternalJDBCDriverJarLoader t = new ExternalJDBCDriverJarLoader();
        t.jarLst.add(new File("/media/gtu001/OLD_D/my_tool/FastDBQueryUI/derbyclient.jar"));
        t.registerDriver("org.apache.derby.jdbc.ClientDriver");

        Connection conn = null;
        try {
            BasicDataSource bds = new BasicDataSource();
            bds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
            bds.setUrl("jdbc:derby://127.0.0.1:1527/seconddb;create=false");
            bds.setUsername("sa");
            bds.setPassword("1234");
            bds.setDriverClassLoader(t.urlClassLoader);
            conn = bds.getConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        System.out.println("done...");
    }

    private static class DriverShim implements Driver {
        private Driver driver;
        private String driverClassName;

        DriverShim(Driver d) {
            this.driver = d;
            this.driverClassName = d.getClass().getName();
        }

        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return Logger.getGlobal();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((driverClassName == null) ? 0 : driverClassName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DriverShim other = (DriverShim) obj;
            if (driverClassName == null) {
                if (other.driverClassName != null)
                    return false;
            } else if (!driverClassName.equals(other.driverClassName))
                return false;
            return true;
        }
    }
}
