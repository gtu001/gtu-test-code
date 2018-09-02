package gtu.mail.gmail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;

public class SendMail {

    @Test
    public static void main(String[] args) {
        
        // 帳號密碼
        Properties props = System.getProperties();
//        String smtpServer = "mail.omniwise.com.tw";
        String smtpServer = "smtp.gmail.com";
        final String user = "gtu001";
        final String pass = "luv90cxc048c";
        
        props.put("mail.smtp.host", smtpServer); //你的SMTP SERVER
        // 採用認證,因為使用外部smtp主機
        props.put("mail.smtp.auth", "true");
        
        props.put("mail.smtp.username", user);
        props.put("mail.smtp.password", pass);
        
        //for gmail ↓↓↓↓↓↓
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        //for gmail ↑↑↑↑↑↑
        
        // 建立session
        Session session = Session.getDefaultInstance(props,  new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });
        
//        URLName urlname = new URLName("smtp", smtpServer, 25, "INBOX", user, pass);
//        PasswordAuthentication auth = new PasswordAuthentication(user, pass);
//        session.setPasswordAuthentication(urlname, auth);

        session.setDebug(true);

        // 建立新的MimeMessage物件
        MimeMessage msg = new MimeMessage(session);

        // 設定送件者的資料
        try {
            msg.setFrom(new InternetAddress("oooxx@yourmail", "寄件者", "Big5"));

            // 設定郵件標題
            msg.setSubject("標題", "Big5");

            // 設定發送時間
            msg.setSentDate(new Date());

            // 設定收件者資料
            InternetAddress[] address;
            address = new InternetAddress[2];
            address[0] = new InternetAddress("xxxx@yahoo.com.tw");
            address[1] = new InternetAddress("oooo.xxx@gmail.com");
            msg.setRecipients(Message.RecipientType.TO, address);

            // 建立多內容郵件物件
            MimeMultipart mp = new MimeMultipart();

            // 建立內文
            MimeBodyPart mbp1 = new MimeBodyPart();

            mbp1.setText("這是郵件的內容", "Big5");
            mp.addBodyPart(mbp1);

            // 建立夾檔
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource("/root/junit.log");
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(new String("單元測試.zip".getBytes("Big5"), "ISO-8859-1"));
            mp.addBodyPart(mbp2);

            msg.setContent(mp);

            Transport trans = session.getTransport("smtp");

            // 外部主機大多要帳號認證
            trans.connect(smtpServer, user, pass);
            msg.saveChanges();
            Transport.send(msg);

            System.out.println("ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}