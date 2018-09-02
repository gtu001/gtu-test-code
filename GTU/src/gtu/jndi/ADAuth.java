package gtu.jndi;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class ADAuth {

    public boolean authenticate() {
        DirContext ctx = null;
        Hashtable<String, String> env = null;
        try {
            env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://10.68.77.17:" + "389");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, "v_tsu@fareastone.com.tw");// id
            env.put(Context.SECURITY_CREDENTIALS, "sugahuei");// pwd
        } catch (Exception ex) {
        }
        try {
            ctx = new InitialDirContext(env);
            return true;
        } catch (AuthenticationException authe) {
            authe.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ctx.close();
            } catch (Exception Ignore) {
            }
        }
    }

    public static void main(String[] args) {
        if (new ADAuth().authenticate())
            System.out.println("驗證通過");
        else
            System.out.println("驗證失敗");
    }
}