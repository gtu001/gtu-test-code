package gtu.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpTest {

    public static void main(String[] args) throws IOException, JSchException  {
        SftpTest test = new SftpTest();
        test.download();
    }
    
    private void download() throws JSchException, IOException{
        FileOutputStream outputStream = null;
        Session sshSession = null;
        Channel channel = null;
        ChannelSftp sftp = null;
        try {
            
//            String filename = this.getClass().getResource("id_rsa").getFile();
            String filename = "D:/_桌面/workspace/GTU/src/gtu/ftp/id_rsa";
            System.out.println("filename = " + filename);

            JSch jsch = new JSch();
            File resourceFile = new File(filename);
            jsch.addIdentity(resourceFile.getAbsolutePath());

            sshSession = createSession(jsch);

            channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;

            // sftp.cd(downloadTypePath + downloadDir);

            // for (String downloadFileName : downloadFileNames) {
            // File file = new File(downloadRisFiles.getAbsolutePath() +
            // File.separator + downloadFileName);
            //
            // outputStream = new FileOutputStream(file);
            //
            // // sftp.get(downloadFileName, outputStream);
            // }

        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
            if ((null != sftp) && (sftp.isConnected())) {
                sftp.disconnect();
            }
            if ((null != channel) && (channel.isConnected())) {
                channel.disconnect();
            }
            if ((null != sshSession) && (sshSession.isConnected())) {
                sshSession.disconnect();
            }
        }
    }

    private Session createSession(JSch jsch) throws JSchException {
        Session sshSession = null;
        try {
            sshSession = jsch.getSession("gtu001", "192.168.10.10", 22);
        } catch (JSchException e) {
            throw e;
        }
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        return sshSession;
    }
}