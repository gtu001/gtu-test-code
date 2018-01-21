package gtu.spring.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EventTest {

    static class MailSendEvent extends ApplicationContextEvent {
        private static final long serialVersionUID = 3593371328614321298L;

        public MailSendEvent(ApplicationContext source, String to) {
            super(source);
            this.to = to;
        }

        String to;

        public String getTo() {
            return this.to;
        }
    }

    static class MailSendListener implements ApplicationListener<MailSendEvent> {
        @Override
        public void onApplicationEvent(MailSendEvent arg0) {
            System.out.println("MailSendListener : 向 " + arg0.getTo() + " 發送完一封郵件");
        }
    }

    static class MailSender implements ApplicationContextAware {
        ApplicationContext ctx;

        @Override
        public void setApplicationContext(ApplicationContext arg0) throws BeansException {
            this.ctx = arg0;
        }

        public void sendMail(String to) {
            System.out.println("MailSender : 模擬發送郵件..");
            MailSendEvent mse = new MailSendEvent(this.ctx, to);
            ctx.publishEvent(mse);//XXX
        }
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("gtu/spring/event/eventTest.xml");
        MailSender mailSender = (MailSender) ctx.getBean("mailSender");
        mailSender.sendMail("aaa@bbb.ccc");
        System.out.println("done...");
    }
}
