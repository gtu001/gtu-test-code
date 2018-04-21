
package org.springframework.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class ContextLoaderListenerTest extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        
        System.out.println("<< ContextLoaderListenerTest Loaded Bean -----------------------------------------");
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        for(String name : context.getBeanDefinitionNames()) {
            System.out.println("\t" + name);
        }
        System.out.println("<< ContextLoaderListenerTest Loaded Bean -----------------------------------------");
    }
}