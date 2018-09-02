package gtu.jndi;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;

public class NamingTest {
    
    public static void main(String[] args) {
        Context ctx = null;
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            ctx = new InitialContext(properties);
            File file = (File)ctx.lookup("E:\\1.txt");
            System.out.println("file = " + file);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        System.out.println("done...");
    }

    private void showAllBindingJndi(InitialContext ctx2) {
        try {
            for (Enumeration<NameClassPair> enu = ctx2.list(""); enu.hasMoreElements();) {
                NameClassPair key = enu.nextElement();
                System.out.println(key);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
