package com.gtu.test.job.decider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultFileTriggerDecider implements JobExecutionDecider {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFileTriggerDecider.class);

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        String flagFileName = jobExecution.getJobParameters().getString("targetFlagFile");
        String jobBeanName = executionContext.getString("jobBeanName");
        logger.info("In DefaultFileTriggerDecider.decide! {}", jobBeanName);

        if (true) {
            return FlowExecutionStatus.COMPLETED;
        } else {
            return FlowExecutionStatus.STOPPED;
        }
    }
}
