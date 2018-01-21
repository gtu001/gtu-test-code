package gtu.mail;

import java.util.LinkedList;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.mail.javamail.JavaMailSender;

public class MailSendHandler {

    private final JavaMailSender mailSender;
    private final InternetAddress sender;
    private final MimeMessage message;

    private List<InternetAddress> to = new LinkedList<InternetAddress>();

    private static final String CHARSET = "big5";

    public static MailSendHandler newInstace(JavaMailSender mailSender, InternetAddress sender) {
        return new MailSendHandler(mailSender, sender);
    }

    private MailSendHandler(JavaMailSender mailSender, InternetAddress sender) {
        super();
        this.mailSender = mailSender;
        this.sender = sender;
        this.message = mailSender.createMimeMessage();
    }

    /**
     * 回傳 for mail log用
     * 
     * @param subject
     * @param mailContent
     * @return
     * @throws MessagingException
     */
    private void sendMail(String subject, String body) throws MessagingException {
        try {
            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(body, "text/html;charset=utf-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);
            message.setSubject(subject, CHARSET);
            if (this.to != null) {
                message.addRecipients(Message.RecipientType.TO, (Address[]) this.to.toArray(new Address[0]));
            }
            //            message.setSender(sender);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public MimeMessage getMessage() {
        return message;
    }

    public MailSendHandler newInstance() {
        return new MailSendHandler(mailSender, sender);
    }

    public void addRecipientTO(String personal, String email) throws Exception {
        this.addRecipient(personal, email, this.to);
    }

    private void addRecipient(String personal, String email, List recipeits) throws Exception {
        try {
            recipeits.add(new InternetAddress(email, personal, CHARSET));
        } catch (Exception e) {
            throw new Exception("Unable to instance a email address, error: " + e.getMessage(), e);
        }
    }
}