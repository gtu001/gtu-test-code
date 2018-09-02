package gtu.spring;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class UtilsTest {

    @Test
    public void testSystemPropertyUtils() {
        System.out.println(SystemPropertyUtils.resolvePlaceholders("${user.home}", true));
        System.out.println(SystemPropertyUtils.resolvePlaceholders("${xxxxxxxxxxx}", true));
        System.out.println(SystemPropertyUtils.resolvePlaceholders("${USER.HOME}", true));
        System.out.println(SystemPropertyUtils.resolvePlaceholders("${yyyyyyyyyyy}"));
        System.out.println("done...");
    }

    @Test
    public void testWebApplicationContextUtils() {
        ServletContext servletContext = Mockito.mock(ServletContext.class);
        WebApplicationContext context = (WebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        System.out.println("context = " + context);
        context = ContextLoader.getCurrentWebApplicationContext();
        System.out.println("context = " + context);
    }
}
