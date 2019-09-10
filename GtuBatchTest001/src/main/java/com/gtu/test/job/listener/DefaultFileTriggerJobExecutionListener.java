package com.gtu.test.job.listener;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultFileTriggerJobExecutionListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFileTriggerJobExecutionListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        String jobBeanName = jobExecution.getJobParameters().getString("jobBeanName");
        logger.info("DefaultFileTriggerJobExecutionListener {} - beforeJob", jobBeanName);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        String jobBeanName = jobExecution.getJobParameters().getString("jobBeanName");
        logger.info("DefaultFileTriggerJobExecutionListener {} - afterJob", jobBeanName);
    }
}
