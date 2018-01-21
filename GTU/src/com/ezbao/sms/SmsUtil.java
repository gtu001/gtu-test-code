package com.ezbao.sms;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class SmsUtil {

    private static final Logger logger = Logger.getLogger(SmsUtil.class);

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat dateTimeFormat1 = new SimpleDateFormat("MMddHHmmss");
    private static SimpleDateFormat dateTimeFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Random randomGenerator = new Random();
    private static FTPClient ftpClient = new FTPClient();

    public static void main(String[] args) throws Exception {
        SmsUtil.sendSms("886954006788", "台灣人壽測試abc");
        System.out.println("done...");
    }

    public static boolean sendSms(String phone, String message) throws Exception {
        String smsFileName = getSMSFileName();
        String userUnit = "100100";
//        String phone = "886954006788";
        String msg2 = message;
        String msg = combineMsgContent(userUnit, phone, msg2);
        String ftpIp = "10.1.2.40";
        int ftpPort = 21;
        String ftpUser = "ezbouat";
        String ftpPassword = "ru04au/6ezbo";
        String ftpEncode = "Big5";
        return SmsUtil.putSmsMsgToFtp(smsFileName, msg, ftpIp, ftpPort, ftpUser, ftpPassword, ftpEncode);
    }

    /**
     * 透過FTP傳送簡訊檔到簡訊平台
     * 
     * @param smsFileName
     * @param msg
     * @param ftpIp
     * @param ftpPort
     * @param ftpUser
     * @param ftpPassword
     * @param ftpEncode
     * @return
     * @throws IOException
     */
    public static boolean putSmsMsgToFtp(String smsFileName, final String msg, String ftpIp, int ftpPort, String ftpUser, String ftpPassword, String ftpEncode) throws Exception {

        boolean ret = false;
        logger.info("ftpIp=" + ftpIp);
        logger.info("ftpPort=" + ftpPort);
        logger.info("ftpUser=" + ftpUser);
        logger.info("ftpPassword=" + ftpPassword);
        logger.info("ftpEncode=" + ftpEncode);
        logger.info("smsFileName=" + smsFileName);
        logger.info("msg=" + msg);

        InputStream in = null;
        try {
            // File file = new File(smsFileName);
            // FileUtils.writeStringToFile(file, msg);
            // logger.info("產生檔案完成,path="+file.getAbsolutePath());

            in = IOUtils.toInputStream(msg, ftpEncode);
            // in = new FileInputStream(file);

            logger.info("InputStream=" + in);
            ftpClient.setDefaultPort(ftpPort);
            ftpClient.connect(ftpIp);
            // logger.info("after FTP.connect");
            int replyCode = ftpClient.getReplyCode();// 連線回應碼
            // logger.info("FTP.getReplyCode()="+replyCode);
            // logger.info("FTP.getReplyString()="+ftpClient.getReplyString());
            if (FTPReply.isPositiveCompletion(replyCode))// 檢查是否登入成功
            {
                logger.info("FTPReply.isPositiveCompletion is OK");
                if (ftpClient.login(ftpUser, ftpPassword)) {
                    // logger.info("FTP.login 成功");
                    ftpClient.enterLocalPassiveMode();
                    // logger.info("準備轉換目錄到->/home/ezbouat");
                    ftpClient.changeWorkingDirectory("/sms");
                    // logger.info("轉換目錄完成");
                    // logger.info("Remote system =" +
                    // ftpClient.getSystemType());
                    // logger.info("Current directory =" +
                    // ftpClient.printWorkingDirectory());

                    ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
                    ftpClient.setBufferSize(1);
                    // logger.info("before FTP.storeFile");
                    if (ftpClient.storeFile(smsFileName, in)) {
                        logger.info("after FTP.storeFile-檔案上傳成功:smsFileName=【" + smsFileName + "】,msg=【" + msg + "】");
                        ret = true;
                    } else {
                        logger.error("FTP傳送檔案失敗");
                        throw new Exception();
                    }
                } else {
                    logger.error("FTP.login 失敗");
                    throw new Exception();
                }
            } else {
                ftpClient.disconnect();
                logger.error("FTP 拒絕連線");
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new Exception();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    throw new IOException();
                }
            }
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    throw new IOException();
                }
            }
        }
        return ret;
    }

    public static String getSMSFileName() {
        return dateTimeFormat.format(new Date()) + randomGenerator.nextInt(10000000) + ".txt";
    }

    public static String getNum() {
        return dateTimeFormat1.format(new Date());
    }

    public static String getDataTime(int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, second);
        Date date = calendar.getTime();
        return dateTimeFormat2.format(date);
    }

    public static String combineMsgContent(String userUnit, String phone, String msg) {
        // 組成簡訊內容
        StringBuffer sb = new StringBuffer();
        sb.append(SmsUtil.getNum()).append(",").append(userUnit).append(",").append(phone).append(",").append(msg).append(",").append(SmsUtil.getDataTime(-7200));
        return sb.toString();
    }
}
