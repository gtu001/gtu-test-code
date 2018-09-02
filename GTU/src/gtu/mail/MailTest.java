package gtu.mail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    private void testMail1() throws MessagingException {
        Properties prop = new Properties();
        Session session = Session.getDefaultInstance(prop);
        MimeMessage mail = new MimeMessage(session);
        MimeMessageHelper helper = new MimeMessageHelper(mail);

        BodyPart mdp = new MimeBodyPart();
        mdp.setContent("messageBody", "text/html;charset=utf-8");
        Multipart mm = new MimeMultipart();
        mm.addBodyPart(mdp);
        helper.setTo("gtu001@gmail.com");
        mail.setContent(mm);
        helper.setSubject("主旨");
        helper.setFrom("gtu001@gmail.com");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", "true");
        mailSender.setJavaMailProperties(javaMailProperties);
        // xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
        mailSender.send(mail);
    }

    /**
     * 附加檔案
     */
    private void addAttachment(MimeMessageHelper helper, String data, String fileName) throws Exception {
        ByteArrayResource inputStrSource = new ByteArrayResource(data.getBytes("big5"));
        String attachFile = fileName;
        helper.addAttachment(attachFile, inputStrSource, "text/html;charset=big5");
    }

    private String getMessageContent(MimeMessage msg) throws MessagingException, IOException {
        try {
            int count = ((MimeMultipart) msg.getContent()).getCount();
            if (count == 0) {
                return msg.getContent().toString();
            }
            for (int i = 0; i < count; i++) {
                Object o = ((MimeMultipart) msg.getContent()).getBodyPart(i).getContent();
                String type = ((MimeMultipart) o).getBodyPart(0).getContentType();
                if (type.indexOf("text") >= 0) {
                    return ((MimeMultipart) o).getBodyPart(0).getContent().toString();
                }
            }
        } catch (Exception e) {
        }
        return "Your message can't find your text/plain!";
    }

    private String getMessageContent1(MimeMessage msg) throws MessagingException, IOException {
        try {
            MimeMultipart mutiPart = (MimeMultipart) msg.getContent();// multipart
                                                                      // /mixed;
                                                                      // mutiPart
                                                                      // .
                                                                      // getContentType
                                                                      // ()
            int count = mutiPart.getCount();// 取得多個BodyPart
            if (count == 0) {
                return msg.getContent().toString();
            }
            for (int i = 0; i < count; i++) {
                BodyPart bp = mutiPart.getBodyPart(i);
                String type = mutiPart.getContentType();
                if (bp.isMimeType("text/*")) { // text/html, text/plain
                    return bp.getContent().toString();

                }
                if (bp.isMimeType("multipart/related")) {// bp.isMimeType("multipart/*")
                    return bp.getContent().toString();

                }
                if (bp.isMimeType("text/html")) {
                    return bp.getContent().toString();
                }
            }
        } catch (Exception e) {
        }
        return "Your message can't find your text/plain!";
    }
}
