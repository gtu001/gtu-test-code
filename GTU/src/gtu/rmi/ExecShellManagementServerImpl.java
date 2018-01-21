package gtu.rmi;

import java.io.File;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tw.gov.moi.common.SystemConfig;
import tw.gov.moi.exception.RisRuntimeException;

/**
 * 執行DB Server上的Shell RMI Server程式實作
 * 
 * @author Sandy Chiu
 * 
 */
public class ExecShellManagementServerImpl extends java.rmi.server.UnicastRemoteObject {

    private static final long serialVersionUID = -6660207089262162750L;

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(ExecShellManagementServerImpl.class);

    @Autowired
    private SystemConfig systemConfig;

    public ExecShellManagementServerImpl() throws RemoteException {

    }

    public int execute(String systemId, String programFile, String[] params) throws RemoteException {

        logger.debug("=====good job!!======");

        int status = 0;
        try {
            String dataPath = systemConfig.getDataPath();
            logger.debug("root data path:{}", dataPath);

            String[] paramsStr = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                String str = params[i];
                logger.debug("str:{}", str);
                if (-1 != str.indexOf("file://")) {
                    paramsStr[i] = str.replace("file://", dataPath + File.separator + systemId + File.separator);
                    logger.debug("after modify:{}", paramsStr[i]);
                } else {
                    paramsStr[i] = str;
                }
            }

            String shellPath = dataPath + File.separator + systemId + File.separator + "sh";
            logger.debug("shellPath:{}", shellPath);
            Process process = Runtime.getRuntime().exec("cd " + shellPath);
            logger.debug("exec command:{} {}", "cd " + shellPath, process.waitFor());

            StringBuffer sb = new StringBuffer();
            sb.append("." + File.separator + programFile);
            for (String str : paramsStr) {
                sb.append(" \"" + str + "\"");
            }
            logger.debug("exec command:{}", sb);

            process = Runtime.getRuntime().exec(sb.toString());

            status = process.waitFor();
            logger.debug("status:{}", status);

        } catch (Exception e) {
            throw new RisRuntimeException("shell執行失敗:" + e.getMessage());
        }

        return status;
    }

}
