package gtu.rmi;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiRegister {

    public static final int REGISTRY_PORT = 1099;

    /** 系統列印服務名稱 */
    public static final String SYS_PNT_SVC_NAME = "rmiSysPrinterServer";

    /** 系統列印管理介面服務名稱 */
    public static final String SYS_PNT_MNG_SVC_NAME = "rmiSysPrinterManagementService";

    /** 執行shell服務名稱 */
    public static final String EXEC_SHELL_SVC_NAME = "rmiExecShellServer";

    public static void start() {
        System.out.println("start rmi register");

        String address = "";
        Registry registry = null;
        try {
            address = (InetAddress.getLocalHost()).toString();

            System.out.println("create remote service address:" + address + ",port:" + REGISTRY_PORT);
        } catch (Exception e) {
            System.out.println("can't get inet address:" + e);
        }

        try {
            System.out.println("create rmi registry");
            registry = LocateRegistry.createRegistry(REGISTRY_PORT);
        } catch (RemoteException e) {
            try {
                System.out.println("get rmi registry");
                registry = LocateRegistry.getRegistry(REGISTRY_PORT);
            } catch (Exception e2) {
                System.out.println("can't create registry " + e2);
            }
        }

        try {
            System.out.println("rebind " + SYS_PNT_SVC_NAME);
            registry.rebind(SYS_PNT_SVC_NAME, new SysPrinterServerImpl());

            System.out.println("rebind " + SYS_PNT_MNG_SVC_NAME);
            registry.rebind(SYS_PNT_MNG_SVC_NAME, new SysPrinterManagementServerImpl());

            System.out.println("rebind " + EXEC_SHELL_SVC_NAME);
            registry.rebind(EXEC_SHELL_SVC_NAME, new ExecShellManagementServerImpl());
        } catch (Exception e2) {
            System.out.println("can't rebind service " + e2);
        }
    }

}
