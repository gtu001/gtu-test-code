package gtu.test.testng.spring.ex1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.timer.TimerFactoryBean;
import org.springframework.test.context.ContextLoader;

import com.ebao.foundation.module.configframework.ServerInfo;
import com.ebao.foundation.module.configframework.support.spring.LazyFileSystemXmlApplicationContext;
import com.ebao.foundation.module.configframework.support.spring.LazyWebXmlApplicationContext;

public class IcpInitContextLoader implements ContextLoader {

    private PathMatchingResourcePatternResolver resolver = new NonOsgiPathMatchingResourcePatternResolver();
    private String moduleConfigPattrern = "classpath*:META-INF/**/*.mcf.xml";

    private static final int MAX_MEMORY_LIMIT = 256 * 1024 * 1024;

    private static final Logger logger = LoggerFactory.getLogger(IcpInitContextLoader.class);

    @Override
    public ApplicationContext loadContext(String... as) throws Exception {
        long maxMemory = Runtime.getRuntime().maxMemory();
        Assert.assertTrue("JVM max memory is not enough" + "(http://wiki.ebaotech.com/confluence/x/py)", maxMemory > MAX_MEMORY_LIMIT);
        long start = System.currentTimeMillis();

        // 正是應該要改成帶resourceList
        // ApplicationContext context = getApplicationContextFromJarAndXml(); //正式應使用
        ApplicationContext context = getApplicationContext();

        long end = System.currentTimeMillis();
        logger.info("Spring context initialized in {}s", (end - start) / 1000);
        Assert.assertNotNull(context.getBean(AnnotationConfigUtils.COMMON_ANNOTATION_PROCESSOR_BEAN_NAME));
        // ctx.getBean("hibernateDataInterceptor3");
        // enable TimerFactoryBean if it's in lazy initialization
        Assert.assertNotNull("testApplicationContext-framework.xml not loaded", context.getBean(TimerFactoryBean.class.getName()));
        return context;
    }

    protected AbstractApplicationContext getApplicationContextFromJarAndXml() {
        List<Resource> resourceList = loadModuleConfigResources(moduleConfigPattrern);

        // TODO 將resourceList_oring.txt -> 轉成 resourceList.txt XXX
        List<String> convertResourceList = new ArrayList<String>();

        return getApplicationContext(convertResourceList, null);
    }

    private AbstractApplicationContext getApplicationContext() {
        return new ClassPathXmlApplicationContext("/gtu/test/testng/spring/ex1/testApplicationContext-framework.xml");
    }

    protected List<Resource> loadModuleConfigResources(String pattern) {
        Resource[] resources = null;
        try {
            resources = resolver.getResources(pattern);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resources == null) {
            resources = new Resource[] {};
        }
        List<Resource> rtnList = new ArrayList<Resource>();
        for (Resource resource : resources) {
            rtnList.add(resource);
        }
        return rtnList;
    }

    /**
     * 資源list 請參考 resourceList.txt
     */
    private AbstractApplicationContext getApplicationContext(List<String> resourceList, ServletContext servletContext) {
        AbstractApplicationContext context = null;
        try {
            if (ServerInfo.getContextPath() != null) {
                context = new LazyWebXmlApplicationContext(resourceList.toArray(new String[0]), servletContext);
            } else {
                context = new LazyFileSystemXmlApplicationContext(resourceList.toArray(new String[0]));
            }
            context.registerShutdownHook();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("spring init fail!", e);
        }
        return context;
    }

    @Override
    public String[] processLocations(Class<?> arg0, String... arg1) {
        return new String[0];
    }

}