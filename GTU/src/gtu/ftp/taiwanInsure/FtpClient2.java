package gtu.ftp.taiwanInsure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpClient2 {

    private static final Logger logger = Logger.getLogger(FtpClient2.class);

    private FTPClient client;

    public static void main(String[] args) throws Exception {
        FtpClient2 ftp = new FtpClient2(true);
        ftp.connect("10.10.2.108", "root", "root123", 22);
        ftp.disconnect();
        System.out.println("done...");
    }

    public FtpClient2(boolean ftps) {
        client = new FTPClient();
    }

    public boolean changeDir(String remotePath) throws Exception {
        return client.changeWorkingDirectory(remotePath);
    }

    public boolean connect(String host, String login, String password, int port) throws Exception {
        logger.debug("FTP request connect to " + host + ":" + port);
        client.connect(host, port);
        int reply = client.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
            logger.debug("FTP request login as " + login);
            if (client.login(login, password)) {
                client.enterLocalPassiveMode();
                client.setRemoteVerificationEnabled(false);
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                return true;
            }
        }
        disconnect();
        return false;
    }

    public void disconnect() throws Exception {
        logger.debug("FTP request disconnect");
        client.disconnect();
    }

    protected boolean downloadFileAfterCheck(String remotePath, String localFile) throws IOException {

        boolean rst;
        FileOutputStream out = null;
        try {
            File file = new File(localFile);
            if (!file.exists()) {
                out = new FileOutputStream(localFile);
                rst = client.retrieveFile(remotePath, out);
            } else {
                rst = true;
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        if (out != null) {
            out.close();
        }
        return rst;
    }

    public boolean downloadFile(String remotePath, String localFile) throws IOException {
        boolean rst;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(localFile);
            rst = client.retrieveFile(remotePath, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return rst;
    }

    public Vector<String> listFileInDir(String remoteDir) throws Exception {
        if (changeDir(remoteDir)) {
            FTPFile[] files = client.listFiles();
            Vector<String> v = new Vector<String>();
            for (FTPFile file : files) {
                if (!file.isDirectory()) {
                    v.addElement(file.getName());
                }
            }
            return v;
        } else {
            return null;
        }
    }

    public boolean uploadFile(String localFile, String remotePath) throws IOException {
        FileInputStream in = new FileInputStream(localFile);
        boolean rst = false;
        try {
            rst = client.storeFile(remotePath, in);
        } finally {
            in.close();
        }
        return rst;
    }

    public Vector<String> listSubDirInDir(String remoteDir) throws Exception {
        if (changeDir(remoteDir)) {
            FTPFile[] files = client.listFiles();
            Vector<String> v = new Vector<String>();
            for (FTPFile file : files) {
                if (file.isDirectory()) {
                    v.addElement(file.getName());
                }
            }
            return v;
        } else {
            return null;
        }
    }

    protected boolean createDirectory(String dirName) {
        try {
            return client.makeDirectory(dirName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isARemoteDirectory(String path) {
        String cache = "/";
        try {
            cache = client.printWorkingDirectory();
        } catch (NullPointerException e) {
            // nop
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            boolean isDir = changeDir(path);
            changeDir(cache);
            return isDir;
        } catch (IOException e) {
            // e.printStackTrace();
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return false;
    }

    public String getWorkingDirectory() {
        try {
            return client.printWorkingDirectory();
        } catch (IOException e) {
        }
        return null;
    }

    public boolean delFile(String fileName) {
        boolean rst = false;
        try {
            rst = client.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rst;
    }

    public boolean reName(String oldFileName, String newFileName) {
        boolean rst = false;
        try {
            rst = client.rename(oldFileName, newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rst;
    }

}
