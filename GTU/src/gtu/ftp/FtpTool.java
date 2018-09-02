package gtu.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

/**
 * 使用sun.net.ftp工具包进行ftp上传下载
 */
public class FtpTool {
    
    String ip;
    int port;
    String user;
    String pwd;
    String remotePath;
    String localPath;
    FtpClient ftpClient;

    /**
     * 连接ftp服务器
     * 
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     * @throws Exception
     */
    public boolean connectServer(String ip, int port, String user, String pwd) throws Exception {
        boolean isSuccess = false;
        try {
            ftpClient = new FtpClient();
            ftpClient.openServer(ip, port);
            ftpClient.login(user, pwd);
            isSuccess = true;
        } catch (Exception ex) {
            throw new Exception("Connect ftp server error:" + ex.getMessage());
        }
        return isSuccess;
    }

    /**
     * 下载文件
     * 
     * @param remotePath
     *            ftp文件夹路径
     * @param localPath
     *            下载到本地路径
     * @param filename
     *            ftp文件名称
     * @throws Exception
     */
    public void downloadFile(String remotePath, String localPath, String filename) throws Exception {
        try {
            if (connectServer(getIp(), getPort(), getUser(), getPwd())) {
                // 创建文件夹
                java.io.File myFilePath = new java.io.File(localPath);
                if (!myFilePath.isDirectory()) {
                    myFilePath.mkdir();
                }

                if (remotePath.length() != 0){
                    ftpClient.cd(remotePath);
                }
                ftpClient.binary();
                TelnetInputStream is = ftpClient.get(filename);
                File file_out = new File(localPath + File.separator + filename);
                FileOutputStream os = new FileOutputStream(file_out);
                byte[] bytes = new byte[1024];
                int c;
                while ((c = is.read(bytes)) != -1) {
                    os.write(bytes, 0, c);
                }
                is.close();
                os.close();
                ftpClient.closeServer();
            }
        } catch (Exception ex) {
            throw new Exception("ftp download file error:" + ex.getMessage());
        }
    }

    /**
     * 上传文件
     * 
     * @param remotePath
     * @param localPath
     * @param filename
     * @throws Exception
     */
    public void uploadFile(String remotePath, String localPath, String filename) throws Exception {
        try {
            if (connectServer(getIp(), getPort(), getUser(), getPwd())) {
                if (remotePath.length() != 0)
                    ftpClient.cd(remotePath);
                ftpClient.binary();
                TelnetOutputStream os = ftpClient.put(filename);
                File file_in = new File(localPath + File.separator + filename);
                FileInputStream is = new FileInputStream(file_in);
                byte[] bytes = new byte[1024];
                int c;
                while ((c = is.read(bytes)) != -1) {
                    os.write(bytes, 0, c);
                }
                is.close();
                os.close();
                ftpClient.closeServer();
            }
        } catch (Exception ex) {
            throw new Exception("ftp upload file error:" + ex.getMessage());
        }
    }

    /**
     * @return
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * @return
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * @param string
     */
    public void setIp(String string) {
        ip = string;
    }

    /**
     * @param i
     */
    public void setPort(int i) {
        port = i;
    }

    /**
     * @param string
     */
    public void setPwd(String string) {
        pwd = string;
    }

    /**
     * @param string
     */
    public void setUser(String string) {
        user = string;
    }

    /**
     * @return
     */
    public FtpClient getFtpClient() {
        return ftpClient;
    }

    /**
     * @param client
     */
    public void setFtpClient(FtpClient client) {
        ftpClient = client;
    }

    /**
     * @return
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * @param string
     */
    public void setRemotePath(String string) {
        remotePath = string;
    }

    /**
     * @return
     */
    public String getLocalPath() {
        return localPath;
    }

    /**
     * @param string
     */
    public void setLocalPath(String string) {
        localPath = string;
    }

    /**
     * 
     * 取得指定目录下的所有文件列表，包括子目录.
     * 
     * @param baseDir
     *            File 指定的目录
     * 
     * @return 包含java.io.File的List
     * 
     */

    private static List<File> getSubFiles(File baseDir) {

        List<File> ret = new ArrayList<File>();

        File[] tmp = baseDir.listFiles();

        for (int i = 0; i < tmp.length; i++) {

            if (tmp[i].isFile())

                ret.add(tmp[i]);

            if (tmp[i].isDirectory())

                ret.addAll(getSubFiles(tmp[i]));

        }

        return ret;

    }

    /**
     * 
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * 
     * @param zippath
     *            指定根目录
     * 
     * @param absFileName
     *            相对路径名，来自于ZipEntry中的name
     * 
     * @return java.io.File 实际的文件
     * 
     */

    private static File getRealFileName(String zippath, String absFileName) {

        String[] dirs = absFileName.split("/", absFileName.length());

        File ret = new File(zippath);// 创建文件对象

        if (dirs.length > 1) {

            for (int i = 0; i < dirs.length - 1; i++) {

                ret = new File(ret, dirs[i]);

            }

        }

        if (!ret.exists()) {// 检测文件是否存在

            ret.mkdirs();// 创建此抽象路径名指定的目录

        }

        ret = new File(ret, dirs[dirs.length - 1]);// 根据 ret 抽象路径名和 child
                                                   // 路径名字符串创建一个新 File 实例

        return ret;

    }

    /**
     * 
     * 取得ftp服务器上某个目录下的所有文件名
     * 
     * @param ftp
     *            , FtpClient类实例; folderName,服务器的文件夹名
     * 
     * @throws Exception
     * 
     * @return list 某目录下文件名列表
     * 
     */

    private List getServerFileNameList(FtpClient ftp, String folderName) throws Exception {

        BufferedReader dr = new BufferedReader(new InputStreamReader(ftp.nameList(folderName)));

        List<String> list = new ArrayList<String>();

        String s;

        while ((s = dr.readLine()) != null) {

            list.add(s);

        }

        return list;

    }

    public static void main(String[] args) throws Exception {
//        FtpTool tt = new FtpTool();
//        boolean bb = tt.connectServer("195.205.1.2", 21, "ftppub", "ftppub");
//        System.out.println(bb);
//        tt.ip = "195.205.1.2";
//        tt.port = 21;// 端口号
//        tt.user = "ftppub";
//        tt.pwd = "ftppub";
//        // tt.downloadFile("/opt/up/20100810/","E:/ftp","SEQNO_20100810.zip");
//        tt.downloadFile("/opt/up/20100810/", "E:/ftp/oppt", "DIR.SEQNO_20100810.txt");
//        /*
//         * List list = tt.getServerFileNameList(tt.ftpClient,
//         * "/opt/up/20100810/SEQNO_20100810.zip"); for(int
//         * i=0;i<list.size();i++){ System.out.println(list.get(i)); }
//         */
        try {
            FtpClient ftp = new FtpClient();
            ftp.openServer("ftp.fileswap.com", 21);
            ftp.login("gtu001@gmail.com", "3JMSYtjcyaTvzd");
            System.out.println(ftp.getResponseString());
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Connect ftp server error:" + ex.getMessage());
        }
    }

}