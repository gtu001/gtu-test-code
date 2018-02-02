package gtu.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SftpUtil {
    
    public static void main_OLD(String[] args) throws JSchException, SftpException {
        SftpUtil test = new SftpUtil();
        String prePath = "/jboss/jboss-eap-7.0/standalone/deployments";
        ChannelSftp sftp = test.connect("192.168.10.10", 22, "gtu001", "123474736");// 個人區
//        ChannelSftp sftp = test.connect("195.19.8.13", 21, "srismapp", "Sth!aix1");// 外面戶所
        List<SftpFileInfo> fileList = new ArrayList<SftpFileInfo>();
        test.scanFileFind(prePath, ".*", fileList, true, sftp);
        sftp.disconnect();
        sftp.exit();
        for (SftpFileInfo f : fileList) {
            System.out.println(f.getAbsolutePath());
        }
    }

    public static void main(String[] args) throws JSchException, SftpException {
        SftpUtil test = new SftpUtil();

        String prePath = "/jboss/jboss-eap-7.0/standalone/deployments";
        File dir = new File("C:\\Users\\gtu00\\OneDrive\\文件\\delpoyments\\20180131");
        
        {
            ChannelSftp sftp = test.connect("10.10.2.108", 22, "root", "root123");
            List<SftpFileInfo> currentRemoteLst = new ArrayList<SftpFileInfo>();
            test.scanFileFind(prePath, ".*", currentRemoteLst, false, sftp);
            for(SftpFileInfo s : currentRemoteLst) {
                if(s.filename.endsWith(".war")) {
                    continue;
                }
                test.delete(prePath, s.filename, sftp);
                System.out.println("del : " + s.filename);
            }

            List<String> backLst = new ArrayList<String>();
            for(File f : dir.listFiles()) {
                if(!f.getName().equals("Web.war")) {
                    backLst.add(f.getName());
                }
            }
            
            for(String name : backLst) {
                test.upload(prePath, name, sftp);   
                System.out.println("upload : " + name);
            }
            sftp.disconnect();
            System.out.println("後臺完成!!");
        }
        {
            ChannelSftp sftp = test.connect("10.10.2.109", 22, "root", "root123");
            List<SftpFileInfo> currentRemoteLst = new ArrayList<SftpFileInfo>();
            test.scanFileFind(prePath, ".*", currentRemoteLst, false, sftp);
            for(SftpFileInfo s : currentRemoteLst) {
                if(s.filename.endsWith(".war")) {
                    continue;
                }
                test.delete(prePath, s.filename, sftp);
                System.out.println("del : " + s.filename);
            }

            List<String> frontLst = new ArrayList<String>();
            frontLst.add("Web.war");
            
            for(String name : frontLst) {
                test.upload(prePath, name, sftp);   
                System.out.println("upload : " + name);
            }
            sftp.disconnect();
            System.out.println("前台完成!!");
        }
        
        System.out.println("done...");
    }
    
    public void scanFileFind(String prePath, String patternStr, List<SftpFileInfo> fileList, boolean recurring, ChannelSftp sftp) throws SftpException{
        Vector<ChannelSftp.LsEntry> vector = listFiles(prePath, sftp);
        for(Enumeration<ChannelSftp.LsEntry> enu = vector.elements(); enu.hasMoreElements();){
            ChannelSftp.LsEntry val = enu.nextElement();
            SftpFileInfo info = getFileInfo(prePath, val);
            if(!info.filename.equals(".") && !info.filename.equals("..")){
                if(!info.dir){
                    if(Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE).matcher(info.filename).find()){
                        fileList.add(info);
                    }
                }
                if(info.dir && recurring){
                    scanFileFind(prePath + "/" + info.filename, patternStr, fileList, recurring, sftp);
                }
            }
        }
    }

    /**
     * 連接sftp服務器 * @param host主機 * @param port端口 * @param username用戶名 * @param
     * password密碼 * @return
     */
    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created. ");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sftp;
    }

    /**
     * 上傳文件 * @param directory上傳的目錄 * @param uploadFile要上傳的文件 * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            sftp.put(new FileInputStream(file), file.getName());
            fileInputStream.close();
            fileInputStream = null;
            file = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下載文件 * @param directory下載目錄 * @param downloadFile下載的文件 * @param
     * saveFile存在本地的路徑 * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFile, fileOutputStream);
            fileOutputStream.close();
            fileOutputStream = null;
            file = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 刪除文件 * @param directory要刪除文件所在目錄 * @param deleteFile要刪除的文件 * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 列出目錄下的文件 * @param directory要列出的目錄 * @param sftp * @return * @throws
     * SftpException
     */
    public Vector<ChannelSftp.LsEntry> listFiles(String directory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(directory);
    }
    
    static public class SftpFileInfo {
        private String filename;
        private String prePath;
        private boolean dir;
        private String mtime;
        private String atime;
        private long size;
        public String getFilename() {
            return filename;
        }
        public String getPrePath() {
            return prePath;
        }
        public boolean isDir() {
            return dir;
        }
        public String getMtime() {
            return mtime;
        }
        public String getAtime() {
            return atime;
        }
        public long getSize() {
            return size;
        }
        public String getAbsolutePath(){
            return prePath + "/" + filename;
        }
        @Override
        public String toString() {
            return "FileInfo [filename=" + filename + ", prePath=" + prePath + ", dir=" + dir + ", mtime=" + mtime
                    + ", atime=" + atime + ", size=" + size + "]";
        }
    }
    
    private SftpFileInfo getFileInfo(String prePath, ChannelSftp.LsEntry entry){
        SftpFileInfo info = new SftpFileInfo();
        info.filename = entry.getFilename();
        info.prePath = prePath;
        SftpATTRS attrs = entry.getAttrs();
        info.dir = attrs.getPermissionsString().startsWith("d");
        
        SimpleDateFormat locale = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = new Date(attrs.getMTime() * 1000L);
        Date date2 = new Date(attrs.getATime());
        info.mtime = locale.format(date1);
        info.atime = locale.format(date2);
        
        info.size = attrs.getSize();
        return info;
    }
}
