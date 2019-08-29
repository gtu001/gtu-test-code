package gtu.quartz.ex3;

import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultCtlLoaderTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCtlLoaderTasklet.class);

    private String jobLogName;

    @Value("${app.ctlTemplateFile.path}")
    private String templateCtlPath;

    private String sourceFilePath;

    private String stepTruncateSgTableValue;

    private String stepTruncateSgTableText;

    private String stepReadRealFileDataValue;

    private String stepReadRealFileDataText;

    private String stepInsertSgTableValue;

    private String stepInsertSgTableText;

    @Autowired
    private FileOperator fileOperator;

    @Autowired
    private ExecCommandLine execCommandLine;

    @Autowired
    LogSysBatchService logSysBatchService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        this.jobLogName = (String) chunkContext.getStepContext().getJobExecutionContext().get("jobLogName");
        String targetFlagFile = (String) chunkContext.getStepContext().getJobExecutionContext().get("targetFlagFile");
        this.sourceFilePath = (String) chunkContext.getStepContext().getJobExecutionContext().get("triggerFilePath");

        this.stepTruncateSgTableValue = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("stepTruncateSgTableValue");
        this.stepTruncateSgTableText = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("stepTruncateSgTableText");
        this.stepReadRealFileDataValue = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("stepReadRealFileDataValue");
        this.stepReadRealFileDataText = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("stepReadRealFileDataText");
        this.stepInsertSgTableValue = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("stepInsertSgTableValue");
        this.stepInsertSgTableText = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("stepInsertSgTableText");
        targetFlagFile = Paths.get(targetFlagFile).getFileName().toString();

        String templateCtlFileName = targetFlagFile.substring(0, targetFlagFile.length() - 4);
        String targetFileRealName = (String) chunkContext.getStepContext().getJobExecutionContext()
            .get("targetFileRealName");
        targetFileRealName = Paths.get(targetFileRealName).getFileName().toString();

        this.sourceFilePath = this.sourceFilePath.replaceAll("\\\\", "\\\\\\\\");
        String newCtlFileName = new StringBuilder(templateCtlFileName).append("Loader.ctl").toString();
        String ctlRows = "10000";

        this.generateCtlFile(templateCtlFileName, targetFileRealName, newCtlFileName);
        execCommandLine.execSqlLoaderByCommandLine(templateCtlFileName, newCtlFileName, ctlRows, jobLogName,
            stepTruncateSgTableValue, stepTruncateSgTableText,
            stepReadRealFileDataValue, stepReadRealFileDataText,
            stepInsertSgTableValue, stepInsertSgTableText);

        return RepeatStatus.FINISHED;
    }

    private void generateCtlFile(String templateCtlFileName, String targetFileRealName, String newCtlFileName)
            throws IOException {
        logger.info("GenerateCtlFile {}.....", jobLogName);
        fileOperator.generateCtlFileFromTemplate(templateCtlPath, templateCtlFileName, targetFileRealName,
            newCtlFileName, sourceFilePath);
    }
}
