package gtu.mail.gmail;

import java.io.File;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author Troy 2009/02/02
 * 
 */
public class MailTest {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(MailTest.class);

    public static void main(String[] args) {
        MailTest.sendmail1();
    }

    public static void sendmail1() {
        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

            mailSender.setProtocol("smtp");
            mailSender.setPort(25);
            mailSender.setHost("getech.com.tw");
            mailSender.setUsername("");
            mailSender.setPassword("");

            javax.mail.internet.MimeMessage mimeMessage = null;
            MimeMessageHelper mailMessageHelper = null;

            mimeMessage = mailSender.createMimeMessage();
            mailMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mailMessageHelper.setTo("gtu001@gmail.com");
            mailMessageHelper.setFrom("gtu001@gmail.com");

            mailMessageHelper.setSubject(javax.mail.internet.MimeUtility.encodeText("測試", "UTF-8", "Q"));

            String str = "test mail 測試信件";
            StringBuffer buf = new StringBuffer();
            buf.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><body>");
            buf.append(str);
            buf.append("</body><html>");

            mailMessageHelper.setText(buf.toString(), true);

            mailMessageHelper.addAttachment(MimeUtility.encodeText("測試附件.log", "UTF-8", "Q"), new File(
                    "c:/epoch_lims.log"));

            mailMessageHelper.setSentDate(new java.util.Date());

            System.out.println("? mailSender = " + mailSender);
            System.out.println("? mimeMessage = " + mimeMessage);
            mailSender.send(mimeMessage);
            System.out.println("寄送成功！");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendmail() {

        // mailSender.setProtocol("smtp");
        // mailSender.setPort(25);
        // mailSender.setHost("getech.com.tw");
        // mailSender.setUsername("");
        // mailSender.setPassword("");

        InternetAddress[] address = null;
        String mailserver = "getech.com.tw"; // <=此處所設必須和寄件人的信箱同一台伺服器,
        String From = "gtu001@pchome.com.tw"; // 並且必須考慮伺服器是否會mail-rely
        String to = "gtu001@pchome.com.tw";
        String Subject = "Subject!!";
        String messageText = "messageText!!";
        boolean sessionDebug = false;

        try {
            // 設定所要用的Mail 伺服器和所使用的傳送協定
            java.util.Properties props = System.getProperties();
            props.put("mail.host", mailserver);
            props.put("mail.transport.protocol", "smtp"); // <=設定所使用的protocol為SMTP(Small
                                                          // Mail Transfer
                                                          // Protocol)
            // 產生新的Session 服務
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            // 設定傳送郵件的發信人
            msg.setFrom(new InternetAddress(From));
            // 設定傳送郵件至收信人的信箱
            address = InternetAddress.parse(to, false);
            msg.setRecipients(Message.RecipientType.TO, address);
            // 設定信中的主題
            msg.setSubject(Subject);
            // 設定送信的時間
            msg.setSentDate(new Date());
            // 設定傳送信的MIME Type
            msg.setText(messageText);
            // 送信
            Transport.send(msg);
            System.out.println("郵件己順利傳送");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
