package gtu.rmi.test;

import java.rmi.Naming;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import gtu.rmi.test.RmiServerTest.MyRmiInterface;

public class RmiClientTest {
    public static void main(String[] args) {
        // Install a security manager!
//        System.setSecurityManager(new RMISecurityManager());
        try {
            // Get a reference to the remote object.
            Object object = Naming.lookup("rmi://localhost:888/HelloServer");
            System.out.println(ReflectionToStringBuilder.toString(object.getClass().getInterfaces(), ToStringStyle.MULTI_LINE_STYLE));
            MyRmiInterface server = (MyRmiInterface) Naming.lookup("rmi://localhost:888/HelloServer");
            System.out.println("Bound to: " + server);
            // Invoke the remote method.
            System.out.println(server.sayHello("哈哈哈"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}