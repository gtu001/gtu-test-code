package gtu.quartz.ex3;

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

    private String jobLogName;

    @Autowired
    FileOperator fileOperator;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        String flagFileName = jobExecution.getJobParameters().getString("targetFlagFile");
        String sourceFilePath = jobExecution.getJobParameters().getString("triggerFilePath");
        String bchDtType = jobExecution.getJobParameters().getString("bchDtType");
        String stepCheckFlgFileValue = jobExecution.getJobParameters().getString("stepCheckFlgFileValue");
        String stepCheckFlgFileText = jobExecution.getJobParameters().getString("stepCheckFlgFileText");
        String stepReadFlgFileValue = jobExecution.getJobParameters().getString("stepReadFlgFileValue");
        String stepReadFlgFileText = jobExecution.getJobParameters().getString("stepReadFlgFileText");
        String stepCheckRealFileValue = jobExecution.getJobParameters().getString("stepCheckRealFileValue");
        String stepCheckRealFileText = jobExecution.getJobParameters().getString("stepCheckRealFileText");
        this.jobLogName = executionContext.getString("jobLogName");
        logger.info("In DefaultFileTriggerDecider.decide! {}", jobLogName);

        if (fileOperator.checkSourceFilesExist(executionContext, flagFileName, sourceFilePath, jobLogName,
            stepCheckFlgFileValue, stepCheckFlgFileText,
            stepReadFlgFileValue, stepReadFlgFileText,
            stepCheckRealFileValue, stepCheckRealFileText, bchDtType)) {
            return FlowExecutionStatus.COMPLETED;
        } else {
            return FlowExecutionStatus.STOPPED;
        }
    }
}
