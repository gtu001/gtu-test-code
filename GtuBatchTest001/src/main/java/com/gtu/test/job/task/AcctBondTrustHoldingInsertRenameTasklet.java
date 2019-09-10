package com.gtu.test.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class AcctBondTrustHoldingInsertRenameTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(AcctBondTrustHoldingInsertRenameTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String jobBeanName = (String) chunkContext.getStepContext().getJobExecutionContext().get("jobBeanName");
        logger.info("jobBeanName : {}", jobBeanName);
        return RepeatStatus.FINISHED;
    }
}
