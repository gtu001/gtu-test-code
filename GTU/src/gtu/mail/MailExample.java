package gtu.mail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailExample {

    private void sendEmail(String mailserver, String from, String[] toList, String subject, String messageText, boolean isHtml) throws Exception {

        String type = isHtml ? "text/html;charsetset=MS950" : "text/plain;charsetset=MS950";

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

        mbp.setContent(messageText, type);
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
