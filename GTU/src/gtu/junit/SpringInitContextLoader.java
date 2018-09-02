package gtu.junit;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.timer.TimerFactoryBean;
import org.springframework.test.context.ContextLoader;

public class SpringInitContextLoader implements ContextLoader {

    private static final int MAX_MEMORY_LIMIT = 256 * 1024 * 1024;

    private static final Logger logger = LoggerFactory.getLogger(SpringInitContextLoader.class);

    @Override
    public ApplicationContext loadContext(String... as) throws Exception {
        long maxMemory = Runtime.getRuntime().maxMemory();
        Assert.assertTrue("JVM max memory is not enough" + "(http://wiki.ebaotech.com/confluence/x/py)", maxMemory > MAX_MEMORY_LIMIT);

        long start = System.currentTimeMillis();
        ApplicationContext ctx = getApplicationContext();//init fetch spring context
        long end = System.currentTimeMillis();
        logger.info("Spring context initialized in {}s", (end - start) / 1000);
        Assert.assertNotNull(ctx.getBean(AnnotationConfigUtils.COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
        // ctx.getBean("hibernateDataInterceptor3");
        // enable TimerFactoryBean if it's in lazy initialization
        Assert.assertNotNull("testApplicationContext-framework.xml not loaded", ctx.getBean(TimerFactoryBean.class.getName()));
        return ctx;
    }
    
    private ApplicationContext getApplicationContext(){
        ApplicationContext context = new ClassPathXmlApplicationContext("com/iisigroup/ris/risConfigFinder_test.xml");
        return context;
    }

    @Override
    public String[] processLocations(Class<?> arg0, String... arg1) {
        return new String[0];
    }
}