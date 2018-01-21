package com.gtu.servlet;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.context.WebApplicationContext;

import com.gtu.dao.AnotherBean;
import com.gtu.job.Test001Job;
import com.gtu.job.Test003Job;

public class SpringQuartzServlet extends HttpServlet {

    private static final long serialVersionUID = -8552176949468359556L;

    private Logger logger = Logger.getLogger(SpringQuartzServlet.class);
    
    public static String APPLICATION_CONTEXT_KEY;

    @Override
    public void init(ServletConfig config) throws ServletException {
        SchedulerFactoryBean factory = getSchedulerFactoryBean(config.getServletContext());
        APPLICATION_CONTEXT_KEY = getApplicationContextKey(factory);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("#. start");
        try {
            SchedulerFactoryBean factory = getSchedulerFactoryBean(req.getServletContext());
            Scheduler scheduler = factory.getScheduler();

            logger.info("---run start---");
            runImmediately("test1", Test001Job.class, scheduler);
            runImmediately("test3", Test003Job.class, scheduler);
            logger.info("---run end---");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.info("#. end");
        }
    }
    
    /**
     * 取得設定的ApplicationContext的Key
     */
    private String getApplicationContextKey(SchedulerFactoryBean factory) {
        try {
            Field f = SchedulerFactoryBean.class.getDeclaredField("applicationContextSchedulerContextKey");
            f.setAccessible(true);
            String key = (String) f.get(factory);
            logger.info("applicationContext key = " + key);
            return key;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return "";
        }
    }
    
    /**
     * 取得schedulerFactoryBean
     */
    private SchedulerFactoryBean getSchedulerFactoryBean(ServletContext ctx) {
        WebApplicationContext webContext = (WebApplicationContext) ctx.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        SchedulerFactoryBean factory = webContext.getBean(SchedulerFactoryBean.class);
        return factory;
    }

    /**
     * 立即啟動job
     */
    private void runImmediately(String key, Class<? extends Job> clz, Scheduler scheduler) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("anotherBean", new AnotherBean());//沒自動注入就要自此設定

        // Create a new Job
        JobKey jobKey = JobKey.jobKey(key);
        JobDetail job = JobBuilder.newJob(clz)//
                .withIdentity(jobKey)//
                .setJobData(dataMap)//
                .storeDurably()//
                .build();

        // Register this job to the scheduler
        scheduler.addJob(job, true);

        // Immediately fire the Job MyJob.class
        scheduler.triggerJob(jobKey);
    }
}
