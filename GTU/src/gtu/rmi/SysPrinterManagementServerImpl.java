package gtu.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系統列印管理實作
 * 
 * @author Sandy Chiu
 * 
 */
public class SysPrinterManagementServerImpl extends java.rmi.server.UnicastRemoteObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1544132207014423304L;
    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(SysPrinterManagementServerImpl.class);

    public SysPrinterManagementServerImpl() throws RemoteException {
    }

    public String[] getPrinterName(String systemId, String siteId) throws RemoteException {

        // 依系統代碼及作業點找出所有印表機
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        List<String> candidate = new ArrayList<String>();
        for (PrintService p : ps) {
            if (p.getName().startsWith(systemId + siteId)) {
                candidate.add(p.getName());
            }
        }

        if (0 == candidate.size()) {
            return null;
        }

        String[] result = new String[candidate.size()];
        for (int i = 0; i < candidate.size(); i++) {
            result[i] = candidate.get(i);
        }

        logger.debug("rmi service return printer size:{}", result.length);
        return result;
    }
}
