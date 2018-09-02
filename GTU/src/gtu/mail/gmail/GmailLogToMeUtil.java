package gtu.mail.gmail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GmailLogToMeUtil {

    private static Session SESSION;

    private static Logger log = LoggerFactory.getLogger(GmailLogToMeUtil.class);
    private static InternetAddress DEFAULT_FROM;
    private static InternetAddress[] DEFAULT_TO;

    static {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        final String username = "gtu001";
        final String password = "luv90cxc048c";
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        session.setDebug(true);
        SESSION = session;

        DEFAULT_FROM = createAddress("gtu001@gmail.com");
        DEFAULT_TO = new InternetAddress[] { DEFAULT_FROM, createAddress("luding001@gmail.com") };
    }

    static InternetAddress createAddress(String email) {
        try {
            return new InternetAddress(email);
        } catch (AddressException e) {
            log.error("address init", e);
        }
        return null;
    }

    public interface Process {
        void process(Message message) throws MessagingException;
    }

    public static void main(String[] args) {
        GmailLogToMeUtil.sendMail("你好我是孫月", "你好我是孫月222");
    }

    /**
     * 簡易mail
     * 
     * @param subject
     * @param mailContent
     */
    public static void sendMail(final String subject, final String mailContent) {
        GmailLogToMeUtil.sendMail(new Process() {
            public void process(Message message) throws MessagingException {
                MimeMultipart mp = new MimeMultipart();
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setText(mailContent, "UTF8");
                mp.addBodyPart(mbp1);
                message.setContent(mp);
                message.setSubject(subject);
            }
        });
    }

    public static void sendMail(final Process process) {
        Validate.notNull(process);
        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            public void run() {
                int time = 0;
                for (;;) {
                    try {
                        Message msg = new MimeMessage(SESSION);
                        msg.setFrom(DEFAULT_FROM);
                        msg.setRecipients(Message.RecipientType.TO, DEFAULT_TO);
                        process.process(msg);
                        msg.setSentDate(new Date());
                        Transport.send(msg);
                        break;
                    } catch (MessagingException e) {
                        log.error("send", e);
                    }
                    if (time > 10) {
                        break;
                    }
                    time++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        log.debug("mainSearchThread sleep", e);
                    }
                }
            }
        }, GmailLogToMeUtil.class.getSimpleName() + System.currentTimeMillis() + "Thread").start();
    }
}
