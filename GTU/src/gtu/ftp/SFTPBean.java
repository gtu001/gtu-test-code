package gtu.ftp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.UserInfo;

;

/**
 * <p>
 * Title: eBaoTech Life SYstem -- Foundation
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 *
 * <p>
 * Company: eBaoTech
 * </p>
 *
 * @author not attributable
 */
public class SFTPBean {

    public static void main(String[] args) throws Exception {
        SFTPBean t = new SFTPBean("10.10.2.108");
        boolean loginResult = t.login("root", "root123", 22);
        System.out.println("loginResult : " + loginResult);
    }

    private static Logger logger = Logger.getLogger(SFTPBean.class);

    private static JSch jsch = new JSch();

    private Session session;

    private String host;

    private String user;

    private String password;

    private Channel channel;

    private ChannelSftp sftpC;

    private final int connectTimeOut = 1000 * 20;// connection time out
                                                 // millSeconds,set to 20
                                                 // seconds

    private boolean useKey = false;// true if use pirvate key/public key and

    // fale if use username/password

    private String privateKeyFile;// private key file full name

    // private int port=22;

    public SFTPBean(String host) {
        this.host = host;
    }

    /**
     * @throws Exception
     *             if encounter a error.
     *
     * @param dir
     *            String the given directory
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether change successful
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean changeWorkdingDirectory(String dir) throws Exception {
        try {
            sftpC.cd(dir);
            return true;
        } catch (Exception ex) {
            throw new Exception("change dir error :" + dir, ex);
        }
    }

    /**
     * connect to fes server
     *
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether connect successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean connect() throws Exception {

        try {
            session.connect(connectTimeOut);
        } catch (JSchException e) {
            throw new Exception("connect error", e);
        }

        try {
            channel = session.openChannel("sftp");
        } catch (JSchException e1) {
            throw new Exception("connect error", e1);
        }
        try {
            channel.connect();
        } catch (JSchException e2) {
            throw new Exception("connect error", e2);
        }
        sftpC = (ChannelSftp) channel;
        return true;

    }

    /**
     *
     * @param localFile
     *            local file to put data in
     * @param remoteFile
     *            name of remote file in current directory
     * @throws Exception
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean get(String localFile, String remoteFile) throws Exception {
        try {
            sftpC.get(remoteFile, localFile);
            return true;
        } catch (Exception ex) {
            throw new Exception("download error: localFile=" + localFile + "remoteFile=" + remoteFile, ex);
        }
    }

    /**
     *
     * @return String the ftp host name
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @return String the login user password
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public String getPassWord() {
        return password;
    }

    /**
     *
     * @return String the login user name
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public String getUserName() {
        return user;
    }

    /**
     * @throws Exception
     *             if encounter a error.
     *
     * @throws Exception
     *             if encounter a error.
     * @return String the current directory
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public String getWorkingDirectory() throws Exception {
        return sftpC.pwd();
    }

    /**
     * @param userName
     *            String the login user name
     * @param keyFile
     *            String the pirvate key full name
     * @return boolean show whether login successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean loginByKey(String userName, String keyFile) throws Exception {
        this.user = userName;
        this.privateKeyFile = keyFile;
        this.useKey = true;
        return login();
    }

    /**
     * login with the user name and password
     *
     * @param userName
     *            String the login user name
     * @param keyFile
     *            String the login private key file full path and name
     * @param port
     *            int the port of the ftp server
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether login successfully
     */
    public boolean loginByKey(String userName, String keyFile, int port) throws Exception {
        this.privateKeyFile = keyFile;
        // validate port...
        if (port <= 0 || port > 65535) {
            port = 22;
        }
        if (this.privateKeyFile == null || this.privateKeyFile.equals("")) {
            throw new Exception("You should define  right private key file full path ,please check!");
        }
        this.user = userName;
        try {
            jsch.addIdentity(this.privateKeyFile);
            session = jsch.getSession(user, host, port);
            // username and password will be given via UserInfo interface.
            MyUserInfo ui = new MyUserInfo();
            ui.setPassword(this.password);
            session.setUserInfo(ui);
            boolean result = this.connect();
            return result;
        } catch (Exception ex) {
            throw new Exception("login error,please check user name and port and  key file!", ex);
        }
    }

    /**
     * @param userName
     *            String the login user name
     * @return boolean show whether login successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean loginByKey(String userName) throws Exception {
        this.user = userName;
        this.useKey = true;
        return login();
    }

    /**
     *
     * log in by user name and password, using port 22
     *
     * @param userName
     *            String the login user name
     * @param passWord
     *            String the login password
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether login successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean login(String userName, String passWord) throws Exception {
        this.user = userName;
        this.password = passWord;
        return login();
    }

    /**
     * login with the user name and password
     *
     * @param userName
     *            String the login user name
     * @param passWord
     *            String the login password
     * @param port
     *            int the port of the ftp server
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether login successfully
     */
    public boolean login(String userName, String passWord, int port) throws Exception {
        int thePort = port;
        // validate port...
        if (thePort <= 0 || thePort > 65535) {
            thePort = 22;
        }

        this.user = userName;
        this.password = passWord;

        try {
            session = jsch.getSession(user, host, thePort);
        } catch (JSchException e) {
            logger.error(" ,catch exception in login() method while calling jsch.getSession(user, host, thePort)", e);
            throw new RuntimeException(e);
        }
        logger.info("jsch.getSession(user, host, thePort) is successful");
        // username and password will be given via UserInfo interface.
        MyUserInfo ui = new MyUserInfo();
        ui.setPassword(this.password);
        session.setUserInfo(ui);
        boolean result = this.connect();
        return result;
    }

    /**
     * @throws Exception
     *             if encounter a error.
     *
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether login successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean login() throws Exception {
        int thePort = 22;
        if (this.useKey) {
            if (this.privateKeyFile == null || this.privateKeyFile.equals("")) {
                throw new Exception("You should define  private key file full path !PrivateKeyFile= " + privateKeyFile);
            }
            try {
                jsch.addIdentity(this.privateKeyFile);
            } catch (JSchException e) {
                throw new Exception("You should define  private key file full path !PrivateKeyFile= " + privateKeyFile);
            }
        }

        try {
            session = jsch.getSession(user, host, thePort);
        } catch (JSchException e) {
            logger.error(" ,catch exception in login() method while calling jsch.getSession(user, host, thePort)", e);
            throw new Exception("  catch exception  while login ,maybe ftp server is down ");
            // throw ExceptionFactory.parse(e);
        }
        // username and password will be given via UserInfo interface.
        MyUserInfo ui = new MyUserInfo();
        if (!useKey) {
            ui.setPassword(this.password);
            session.setUserInfo(ui);
        }
        // session.setTimeout(timeOut);
        if (session != null && session.isConnected()) {
            return true;
        }

        boolean res = this.connect();
        return res;

    }

    public boolean logout() throws Exception {
        try {
            session.disconnect();
            return true;
        } catch (Exception ex) {
            throw new Exception("logout error", ex);
        }
    }

    /**
     * @throws Exception
     *             if encounter a error.
     *
     * @param host
     *            String the ftp host
     * @param userName
     *            String the login user name
     * @param passWord
     *            String the login password
     * @throws Exception
     *             if encounter a error.
     * @return boolean show whether connect successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean login(String host, String userName, String passWord) throws Exception {
        this.host = host;
        this.user = userName;
        this.password = passWord;
        this.login();
        return true;
    }

    /**
     * @throws Exception
     *             if encounter an error.
     *
     * @param dir
     *            String the directory name
     * @throws Exception
     *             if encounter an error.
     * @return boolean show whether make successfully
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean makeDirectory(String dir) throws Exception {
        try {
            sftpC.mkdir(dir);
            return true;
        } catch (Exception ex) {
            throw new Exception("create dir error:" + dir, ex);
        }
    }

    /**
     * Put a local file onto the FTP server.
     *
     * @param localFile
     *            path of the local file
     * @param remoteFile
     *            name of remote file
     * @throws Exception
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public boolean put(String localFile, String remoteFile) throws Exception {
        try {
            try {
                sftpC.rm(remoteFile);
            } catch (Exception ex) {
                logger.info("SFTPBean.put().sftpC.rm  catch exception:remoteFile=" + remoteFile + " localFile=" + localFile);
            }

            sftpC.put(localFile, remoteFile);
            return true;
        } catch (Exception ex) {
            throw new Exception("upload error:localFile=" + localFile + " ,remoteFile=" + remoteFile, ex);
        }
    }

    /**
     *
     * @param passWord
     *            String the login user password
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public void setPassWord(String passWord) {
        this.password = passWord;
    }

    /**
     *
     * @param userName
     *            String the login user name
     * @todo Implement this com.ebao.pub.util.ftp.FTP method
     */
    public void setUserName(String userName) {
        this.user = userName;
    }

    public String[] listNames(String folderName) throws Exception {
        try {
            @SuppressWarnings("unchecked")
            Vector<String> nameV = sftpC.ls(folderName);
            String[] names = new String[nameV.size()];
            nameV.toArray(names);
            return names;
        } catch (Exception ex) {
            throw new Exception("list error:" + folderName, ex);
        }
    }

    public boolean move(String remoteFromFile, String remoteToFile) throws Exception {

        try {
            delete(remoteToFile);
        } catch (Exception ex) {
            logger.info("SFTPBean.move().sftpC.rm catch exception:remoteFromFile=" + remoteFromFile + " remoteToFile=" + remoteToFile);
        }
        try {
            sftpC.rename(remoteFromFile, remoteToFile);
            return true;
        } catch (Exception ex) {
            throw new Exception("rename error maybe have the file at target folder..." + remoteToFile, ex);
        }
    }

    public boolean delete(String remoteFile) throws Exception {
        try {
            sftpC.rm(remoteFile);
            return true;
        } catch (Exception ex) {
            throw new Exception("delete error " + remoteFile, ex);
        }
    }

    public boolean deleteDir(String remoteFile) throws Exception {
        try {
            sftpC.rmdir(remoteFile);
            return true;
        } catch (Exception ex) {
            throw new Exception("delete error " + remoteFile, ex);
        }
    }

    public String[] lsNames(String folderName) throws Exception {
        try {
            @SuppressWarnings("unchecked")
            Vector<String> nameV = sftpC.lsnames(folderName);
            String[] names = new String[nameV.size()];
            nameV.toArray(names);
            return names;
        } catch (Exception ex) {
            throw new Exception("list error:" + folderName, ex);
        }
    }

    public String getCreationDate(String folderName) throws Exception {
        String creationDt = null;
        try {
            SftpATTRS attrs = sftpC.stat(folderName);
            if (attrs != null) {
                creationDt = (new SimpleDateFormat("yyyyMMMdd")).format(new Date(((long) attrs.getMTime()) * 1000));
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return creationDt;
    }

    public boolean checkIfDirectory(String folderName) throws Exception {
        boolean isDir = false;
        try {
            SftpATTRS attrs = sftpC.stat(folderName);
            if (attrs != null) {
                if (attrs.isDir()) {
                    isDir = true;
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        return isDir;
    }

    private static class MyUserInfo implements UserInfo {

        private String password;

        @Override
        public String getPassphrase() {
            return null;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
        }
    }
}
