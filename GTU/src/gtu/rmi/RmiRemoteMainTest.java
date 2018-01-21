package gtu.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmiRemoteMainTest {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(RmiRemoteMainTest.class);

    /** 執行DB Server上的Shell RMI程式介面 */
    private ExecShellManagementServerImpl rmiService;

    public RmiRemoteMainTest(String url) throws RemoteException {
        Registry registry;
        String ip = url.substring(url.indexOf("//") + 2, url.lastIndexOf(":"));
        logger.debug("call remote service by " + ip + ":" + RmiRegister.REGISTRY_PORT);
        try {
            registry = LocateRegistry.getRegistry(ip, (new Integer(RmiRegister.REGISTRY_PORT)).intValue());
            rmiService = (ExecShellManagementServerImpl) (registry.lookup(RmiRegister.EXEC_SHELL_SVC_NAME));
        } catch (Exception e) {
            logger.error("call remote service throws exception ", e);
        }
    }

    public int execute(String systemId, String programFile, String[] params) {
        try {
            int status = rmiService.execute(systemId, programFile, params);
            return status;
        } catch (Exception e) {

            logger.error("call rmi remote service error ", e);
            return 1;
        }
    }
}
