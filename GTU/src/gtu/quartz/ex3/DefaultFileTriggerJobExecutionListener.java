package gtu.quartz.ex3;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cub.robo.fee.batch.enums.ReturnCodeEnum;
import cub.robo.fee.batch.service.BchLogJobFinishService;
import cub.robo.fee.batch.util.FileOperator;

@Component
public class DefaultFileTriggerJobExecutionListener extends JobExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(DefaultFileTriggerJobExecutionListener.class);

    private static String backupDirName = "RECEIVEDONE";

    private static String successBackupDirName = "RECEIVEDONE";

    private static String errorBackupDirName = "error";

    private String jobLogName;

    private String fileBackupDays;

    private String jobTypeCd;

    private String batchDate;

    private String sourceFilePath;

    private String stepFileMoveValue;

    private String stepFileMoveText;

    private String stepFileHousekeepingValue;

    private String stepFileHousekeepingText;

    private String stepTruncateSgTableValue;

    private String stepTruncateSgTableText;

    private String stepReadRealFileDataValue;

    private String stepReadRealFileDataText;

    private String stepInsertSgTableValue;

    private String stepInsertSgTableText;

    @Autowired
    FileOperator fileOperator;

    @Autowired
    BchLogJobFinishService bchLogJobFinishService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        this.setDefaultParameters(jobExecution);
        this.setOtherParameters(jobExecution);
        logger.info("DefaultFileTriggerJobExecutionListener {} - beforeJob", this.jobLogName);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        try {
            logger.info("DefaultFileTriggerJobExecutionListener {} - afterJob, jobExecution.getStatus() = {}",
                this.jobLogName, jobExecution.getStatus());
            ExecutionContext executionContext = jobExecution.getExecutionContext();
            logger.info("executionContext = {}", executionContext);
            LocalDateTime startDateTime = (LocalDateTime) executionContext.get("startDateTime");
            String filePath = executionContext.getString("targetFlagFile");
            String flagFilePath = executionContext.getString("targetFileRealName");
            String programId = jobExecution.getJobParameters().getString("programId");
            String multiTrigger = jobExecution.getJobParameters().getString("multiTrigger");

            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                this.doFinishJobs(programId, multiTrigger, startDateTime, jobTypeCd, batchDate,
                    ReturnCodeEnum.SUCCESS.getValue(), ReturnCodeEnum.SUCCESS.getText());
                backupDirName = successBackupDirName;
                logger.info("In afterJob {} complete with reading file {}", this.jobLogName, filePath);

            } else if (jobExecution.getStatus() != BatchStatus.STOPPED) {
                this.doFinishJobs(programId, multiTrigger, startDateTime, jobTypeCd, batchDate,
                    ReturnCodeEnum.FAIL.getValue(), ReturnCodeEnum.FAIL.getText());
                backupDirName = errorBackupDirName;
                logger.error("In afterJob {} failed! Please check file {}", this.jobLogName, filePath);
            }

            this.doMoveAndDeleteFiles(filePath, flagFilePath);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private void doMoveAndDeleteFiles(String filePath, String flagFilePath) {
        // 移動來源檔案
        if (!"".equals(flagFilePath)) {
            //fileOperator.doMoveFiles(filePath, flagFilePath, backupDirName, jobLogName, stepFileMoveValue, stepFileMoveText);
            fileOperator.doMoveZipFiles(filePath, flagFilePath, backupDirName, jobLogName, stepFileMoveValue, stepFileMoveText);
            logger.info("FileBackupDays = {}", fileBackupDays);
            // 刪除備份來源檔案
            if (!"".equals(fileBackupDays)) {
                fileOperator.deleteBackupSourceFiles(filePath, successBackupDirName,
                    Integer.parseInt(fileBackupDays), jobLogName, stepFileHousekeepingValue, stepFileHousekeepingText);
            }
        }
    }

    private void doFinishJobs(String programId, String multiTrigger, LocalDateTime startDateTime,
            String jobTypeCd, String batchDate, String returnCode, String returnText) {
        bchLogJobFinishService.doSaveFinishJobRecord(programId, multiTrigger, startDateTime, jobTypeCd,
            batchDate, returnCode, returnText);
    }

    private void setDefaultParameters(JobExecution jobExecution) {
        this.jobLogName = jobExecution.getJobParameters().getString("jobLogName");
        this.fileBackupDays = jobExecution.getJobParameters().getString("fileBackupDays");
        this.jobTypeCd = jobExecution.getJobParameters().getString("jobTypeCd");
        this.batchDate = jobExecution.getJobParameters().getString("batchDate");
        this.sourceFilePath = jobExecution.getJobParameters().getString("triggerFilePath");
        this.stepFileMoveValue = jobExecution.getJobParameters().getString("stepFileMoveValue");
        this.stepFileMoveText = jobExecution.getJobParameters().getString("stepFileMoveText");
        this.stepFileHousekeepingValue = jobExecution.getJobParameters().getString("stepFileHousekeepingValue");
        this.stepFileHousekeepingText = jobExecution.getJobParameters().getString("stepFileHousekeepingText");
        this.stepTruncateSgTableValue = jobExecution.getJobParameters().getString("stepTruncateSgTableValue");
        this.stepTruncateSgTableText = jobExecution.getJobParameters().getString("stepTruncateSgTableText");
        this.stepReadRealFileDataValue = jobExecution.getJobParameters().getString("stepReadRealFileDataValue");
        this.stepReadRealFileDataText = jobExecution.getJobParameters().getString("stepReadRealFileDataText");
        this.stepInsertSgTableValue = jobExecution.getJobParameters().getString("stepInsertSgTableValue");
        this.stepInsertSgTableText = jobExecution.getJobParameters().getString("stepInsertSgTableText");
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        executionContext.put("startDateTime", LocalDateTime.now());
        executionContext.put("jobLogName", this.jobLogName);
        executionContext.put("triggerFilePath", this.sourceFilePath);
        executionContext.put("stepTruncateSgTableValue", this.stepTruncateSgTableValue);
        executionContext.put("stepTruncateSgTableText", this.stepTruncateSgTableText);
        executionContext.put("stepReadRealFileDataValue", this.stepReadRealFileDataValue);
        executionContext.put("stepReadRealFileDataText", this.stepReadRealFileDataText);
        executionContext.put("stepInsertSgTableValue", this.stepInsertSgTableValue);
        executionContext.put("stepInsertSgTableText", this.stepInsertSgTableText);
    }

    protected void setOtherParameters(JobExecution jobExecution) {

    }

}
