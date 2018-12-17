package gtu.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiDBUtil {

    public static DataSource getJndiDataSource(String jndiName) {
        DataSource ds = null;
        Object ref = null;
        Context ctx = null;
        try {
            try {
                ctx = new InitialContext();
                ref = ctx.lookup(jndiName);
            } catch (Throwable localThrowable) {
                System.out.println("getJndi ERR : 1 , " + localThrowable.getMessage());
            }
            try {
                if (ref == null)
                    ref = ((Context) ctx.lookup("java:comp/env")).lookup(jndiName);
            } catch (Throwable localThrowable1) {
                System.out.println("getJndi ERR : 2 , " + localThrowable1.getMessage());
            }
            if (ref == null) {
                ref = ((Context) ctx.lookup("java:comp/env")).lookup("java:comp/env/" + jndiName);
            }
            ds = (DataSource) ref;
            return ds;
        } catch (NamingException e) {
            System.out.println("getJndi ERR : 3 , " + e.getMessage());
            throw new RuntimeException("getJndi ERR : 3 , " + e.getMessage(), e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
