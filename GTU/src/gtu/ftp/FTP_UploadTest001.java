package gtu.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import gtu.file.FileUtil;

public class FTP_UploadTest001 {

    public static void main(String[] args) throws Exception {
        String ftpHost = "10.10.2.35";
        int ftpPort = 21;
        String ftpId = "ccbilldb";
        String ftpPwd = "ccbilldb";
        
        File file = new File(FileUtil.DESKTOP_PATH, "test.txt");
        
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(ftpHost, ftpPort);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(ftpId, ftpPwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();

        ftp.changeWorkingDirectory("/out");

        ftp.storeFile(file.getName(), new FileInputStream(file));
    }

}
