package com.gtu.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Test004Job implements Job{
    
    private Logger logger = Logger.getLogger(getClass());

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Test004Job running...");
    }
}
