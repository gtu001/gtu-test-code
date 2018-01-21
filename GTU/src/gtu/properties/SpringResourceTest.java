package gtu.properties;

import java.util.GregorianCalendar;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringResourceTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String[] config = { "gtu/properties/resourceTest.xml" };
        ApplicationContext ctx = new ClassPathXmlApplicationContext(config);
        Object[] params = { "John", new GregorianCalendar().getTime() };
        MessageSource message = (MessageSource) ctx.getBean("messageSource");
        System.out.println(message.getMessage("test", params, Locale.US));
        System.out.println(message.getMessage("test", params, Locale.CHINESE));
        System.out.println(message.getMessage("greeting.night", params, Locale.US));
        System.out.println(message.getMessage("greeting.night", params, new Locale("zh", "TW")));
        System.out.println(ctx.getMessage("test", params, Locale.US));
        System.out.println(ctx.getMessage("test", params, Locale.CHINESE));
        System.out.println(ctx.getMessage("greeting.night", params, Locale.US));
        System.out.println(ctx.getMessage("greeting.night", params, Locale.getDefault()));
    }

}
