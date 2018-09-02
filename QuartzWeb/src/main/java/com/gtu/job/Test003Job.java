package com.gtu.job;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gtu.servlet.SpringQuartzServlet;

public class Test003Job extends QuartzJobBean {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Test003Job running..");
        try {
            logger.info("context - " + context.getScheduler().getContext().get(SpringQuartzServlet.APPLICATION_CONTEXT_KEY));
        }catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
