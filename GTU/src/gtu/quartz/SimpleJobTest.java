package gtu.quartz;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

public class SimpleJobTest {
    public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    //public final static String DEFAULT_SPRING_CONFIG = "beans-fet.xml";
    //public final static String DEFAULT_SPRING_CONFIG = "beans-test.xml";
    public final static String DEFAULT_SPRING_CONFIG = "bin/gtu/quartz/beans-sit2-test.xml";
    public final static String BEAN_SCHEDULER_NAME = "scheduler";
    public final static String BEAN_DATASOURCE_NAME = "dataSource";
    public final static String BEAN_FACTORY = "beanfactory";
    private static final String MAIL_TEMPLATE_DIR = "bin/gtu/quartz/";

    private Scheduler scheduler = null;

    public SimpleJobTest() {
        try {
            String springConfigFile = null;
            springConfigFile = System.getProperty("spring.configuration");
            if (null == springConfigFile) {
                springConfigFile = DEFAULT_SPRING_CONFIG;
            }

            Resource resource = new FileSystemResource(springConfigFile);
            BeanFactory beanFactory = new XmlBeanFactory(resource);
            System.out.println(this.readTemplateFile("DI_WRITE_OFF_JOB"));

            scheduler = (Scheduler) beanFactory.getBean(BEAN_SCHEDULER_NAME);
            scheduler.getContext().put(APPLICATION_CONTEXT_KEY, beanFactory);
            DataSource o = (DataSource)beanFactory.getBean("cedsDataSource");
            o.getConnection();
            System.out.println(o);
//            System.out.println(o.getLoginTimeout());
            JdbcTemplate jdbcTemplate = (JdbcTemplate)
            beanFactory.getBean("cedsJdbcTemplate");
            JdbcTemplate oJdbcTemplate = (JdbcTemplate)
            beanFactory.getBean("jdbcTemplate");
            int owUserCount = oJdbcTemplate.queryForInt("select count(*) from ow_user");
//            int i = jdbcTemplate.queryForInt("select count(*) from viemember");
//            System.out.println(i);
            System.out.println(owUserCount);
            System.out.println(o.getConnection().getAutoCommit());
            scheduler.start();
            System.out.println("scheduler start......");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected String readTemplateFile(String templateID) throws IOException {
        Resource resource = new FileSystemResource(this.MAIL_TEMPLATE_DIR + "//" + templateID);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(resource.getFile());
            int x = fis.available();
            byte b[] = new byte[x];
            fis.read(b);
            return new String(b, "Big5");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            fis.close();
        }
        return null;
    }

    public void run(Class clazz, long waitMillis) throws SchedulerException, InterruptedException {
        JobDetail jobDetail = new JobDetail("kkkkllllll", Scheduler.DEFAULT_GROUP, clazz);
        Trigger trigger = TriggerUtils.makeSecondlyTrigger(1);
        ((SimpleTrigger) trigger).setRepeatCount(0);
        trigger.setName("SimpleTrigger");
        trigger.setStartTime(new Date());
        scheduler.scheduleJob(jobDetail, trigger);
        System.out.println(String.format("waiting %s sec", new Object[]{String.valueOf(waitMillis/1000)}));
        Thread.sleep(waitMillis);
        scheduler.shutdown();
        System.out.println("結束測試!");
    }

    /**
     * @param args
     * @throws SchedulerException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws SchedulerException, InterruptedException {
        SimpleJobTest testJob = new SimpleJobTest();
        testJob.run(TestJob.class, 10000);
    }
}
