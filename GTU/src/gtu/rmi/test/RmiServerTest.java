package gtu.rmi.test;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 1.rmic -classpath c:\xxxx\build\classes gtu.rmi.test.MyRmiInterfaceImpl -d 目的目錄
 * 2.切到目錄c:\xxxx\build\classes 執行rmiregistry [port]\n
 * 3.執行server
 * 4.執行client
 */
public class RmiServerTest {

    public static void main(String[] args) {
//        System.setSecurityManager(new RMISecurityManager());
        try {
            // Create the remote object.
            MyRmiInterface obj = new MyRmiInterfaceImpl();
            // Register the remote object as "HelloServer".
            Naming.rebind("rmi://localhost:888/HelloServer", obj);
            System.out.println("HelloServer bound in registry!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        try {
//            Registry registry = LocateRegistry.getRegistry(888);
//            registry.rebind("MyRmiInterface", new MyRmiInterfaceImpl());
//            System.out.println("done...");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
   
    public static class MyRmiInterfaceImpl extends UnicastRemoteObject implements MyRmiInterface {
        protected MyRmiInterfaceImpl() throws RemoteException {
            super();
        }

        private static final long serialVersionUID = 1L;

        @Override
        public String sayHello(String message) throws RemoteException {
            return "sayHello[" + message + "]";
        }
    }
    
    public interface MyRmiInterface extends Remote {
        public String sayHello(String message) throws RemoteException;
    }
}
