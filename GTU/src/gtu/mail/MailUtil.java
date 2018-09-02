package gtu.mail;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

public class MailUtil {
    private static final Logger logger = Logger.getLogger(MailUtil.class);
    private final static String ENCODING = "UTF8"; //MS950

    public static void sendEmail(String stMailServerIP, String stEmailFrom, String[] stEmailTo, String stSubject, String stContent, java.io.File[] attachment, boolean bIsIncludeImage) {
        try {
            String type = "text/html;charsetset=" + ENCODING;

            // 設定所要用的Mail伺服器和所使用的傳送協定
            java.util.Properties props = System.getProperties();
            props.put("mail.host", stMailServerIP);
            props.put("mail.transport.protocol", "smtp");

            // 產生新的Session 服務
            javax.mail.Session mailSession = javax.mail.Session.getDefaultInstance(props, null);
            mailSession.setDebug(false);
            Message message = new MimeMessage(mailSession);

            Multipart multiPart = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(stContent, type);
            multiPart.addBodyPart(bodyPart);
            if (attachment != null && attachment.length > 0) {
                // 附加檔案到信件
                for (int i = 0; i < attachment.length; i++) {
                    MimeBodyPart mbp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(attachment[i]);
                    mbp.setDataHandler(new DataHandler(fds));
                    mbp.setFileName(MimeUtility.encodeText(fds.getName(), ENCODING, "B"));
                    multiPart.addBodyPart(mbp);
                }

                if (bIsIncludeImage) {
                    for (int i = 0; i < attachment.length; i++) {
                        java.io.File file = attachment[i];
                        String stFileSuffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        if ("JPG".equalsIgnoreCase(stFileSuffix) || "GIF".equalsIgnoreCase(stFileSuffix) || "JPEG".equalsIgnoreCase(stFileSuffix) || "PNG".equalsIgnoreCase(stFileSuffix)
                                || "TIF".equalsIgnoreCase(stFileSuffix) || "BMP".equalsIgnoreCase(stFileSuffix)) {
                            MimeBodyPart mbp = new MimeBodyPart();
                            FileDataSource fds = new FileDataSource(file);
                            mbp.setDataHandler(new DataHandler(fds));
                            mbp.setHeader("Content-ID", "<img_" + file.getName().substring(0, file.getName().indexOf(".")) + ">");
                            multiPart.addBodyPart(mbp);
                        }
                    }
                }

                message.setContent(multiPart);
            }
            message.setContent(multiPart);

            // 設定傳送郵件的發信人
            message.setFrom(new InternetAddress(stEmailFrom));

            // 設定傳送郵件至收信人的信箱
            InternetAddress[] address = new InternetAddress[stEmailTo.length];
            for (int i = 0; i < stEmailTo.length; i++) {
                address[i] = new InternetAddress();
                address[i].setAddress(stEmailTo[i]);
            }
            message.setRecipients(Message.RecipientType.TO, address);
            // 設定信中的主題
            message.setSubject(stSubject);
            // 設定送信的時間
            message.setSentDate(new java.util.Date());

            // 設定傳送信的MIME Type
            // msg.setText(content);
            // 送信
            Transport.send(message);
        } catch (Exception e) {
            logger.error("SendEmail Exception:", e.fillInStackTrace());
        }
    }

    public static void sendEmail2(String mailserver, String from, String[] toList, String subject, String messageText, boolean isHtml) throws Exception {

        String type = isHtml ? "text/html;charsetset=" + ENCODING : "text/plain;charsetset=" + ENCODING;

        // 設定所要用的Mail 伺服器和所使用的傳送協定
        java.util.Properties props = System.getProperties();
        props.put("mail.host", mailserver);
        props.put("mail.transport.protocol", "smtp");

        // 產生新的Session 服務
        javax.mail.Session mailSession = javax.mail.Session.getDefaultInstance(props, null);
        mailSession.setDebug(false);
        Message msg = new MimeMessage(mailSession);

        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp = new MimeBodyPart();

        mbp.setText(messageText, ENCODING);
//        mbp.setContent(messageText, type);
        mp.addBodyPart(mbp);
        msg.setContent(mp);

        // 設定傳送郵件的發信人
        msg.setFrom(new InternetAddress(from));

        // 設定傳送郵件至收信人的信箱
        List list = new ArrayList();
        for (int i = 0; i < toList.length; i++) {
            InternetAddress internetAddress = new InternetAddress();
            internetAddress.setAddress(toList[i]);
            list.add(internetAddress);
        }
        InternetAddress address[] = (InternetAddress[]) list.toArray(new InternetAddress[list.size()]);
        msg.setRecipients(Message.RecipientType.TO, address);
        // 設定信中的主題
        msg.setSubject(subject);
        // 設定送信的時間
        msg.setSentDate(new java.util.Date());

        // 設定傳送信的MIME Type
        // msg.setText(content);
        // 送信
        Transport.send(msg);
    }
}
