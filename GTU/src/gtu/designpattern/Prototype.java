package gtu.designpattern;

import java.util.Random;

public class Prototype {

    public static void main(String[] args) {
        final int MAX_COUNT = 6;
        Mail mail = new Mail(new AdvTemplate());
        mail.tail = "XX銀行版權所有";
        for (int ii = 0; ii < MAX_COUNT; ii++) {
            Mail cloneMail = mail.clone();
            cloneMail.appellation = getRandString(5) + "先生(女士)";
            cloneMail.receiver = getRandString(5) + "@" + getRandString(8) + ".com";
            sendMail(cloneMail);
        }

        //用原型實例指定創建物件的種類，並且透過拷貝這些原型創建新的物件
        //Specify the kinds of objects to create using a prototypical instance , and create new objects by copying this prototype.
    }

    static void sendMail(Mail mail) {
        System.out.println("標題:" + mail.subject + "\t收件人:" + mail.receiver + "\t...發送成功!");
    }

    static String getRandString(int maxLength) {
        String source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuffer sb = new StringBuffer();
        Random rand = new Random();
        for (int ii = 0; ii < maxLength; ii++) {
            sb.append(source.charAt(rand.nextInt(source.length())));
        }
        return sb.toString();
    }

    static class AdvTemplate {
        //廣告信名稱
        String advSubject = "XX銀行國慶信用卡抽獎活動";
        //廣告信內容
        String advContext = "國慶抽獎活動通知：只要刷卡就送你一百萬!..";

        public String getAdvSubject() {
            return advSubject;
        }

        public String getAdvContext() {
            return advContext;
        }
    }

    static class Mail implements Cloneable {
        String receiver;
        String subject;
        String appellation;
        String context;
        String tail;

        public Mail(AdvTemplate advtemplate) {
            this.context = advtemplate.getAdvContext();
            this.subject = advtemplate.getAdvSubject();
        }

        @Override
        protected Mail clone() {
            try {
                return (Mail) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
