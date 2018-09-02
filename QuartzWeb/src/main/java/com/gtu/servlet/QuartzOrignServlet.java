package com.gtu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

import com.gtu.job.Test003Job;

public class QuartzOrignServlet extends HttpServlet {

    private static final long serialVersionUID = -8552176949468359556L;

    private Logger logger = Logger.getLogger(QuartzOrignServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("#. start");
        try {
            StdSchedulerFactory factory = (StdSchedulerFactory) req.getServletContext().getAttribute(QuartzInitializerServlet.QUARTZ_FACTORY_KEY);
            Scheduler scheduler = factory.getScheduler();
            
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("from", "orign");

            // Create a new Job
            JobKey jobKey = JobKey.jobKey("test003Job");
            JobDetail job = JobBuilder.newJob(Test003Job.class)//
                    .withIdentity(jobKey)//
                    .setJobData(dataMap)//
                    .storeDurably()//
                    .build();
            
            // Register this job to the scheduler
            scheduler.addJob(job, true);

            // Immediately fire the Job MyJob.class
            scheduler.triggerJob(jobKey);
            
            logger.info("isStarted - " + scheduler.isStarted());
            
            scheduler.start();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.info("#. end");
        }
    }
}
