package gtu.quartz.ex3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cub.robo.fee.batch.service.LogSysBatchService;
import gtu.quartz.ex3.QuartzBatchApplication.DBConfig;

@Component
public class ExecCommandLine {

    private static final Logger logger = LoggerFactory.getLogger(ExecCommandLine.class);

    @Value("${app.ctlTemplateFile.path}")
    private String templateCtlPath;

    @Value("${app.ctlTemplateFile.sId}")
    private String sId;

    @Autowired
    private DBConfig dBConfig;

    @Autowired
    LogSysBatchService logSysBatchService;

    public void execSqlLoaderByCommandLine(String templateCtlFileName, String newCtlFileName, String rows,
            String jobLogName, String stepTruncateSgTableValue, String stepTruncateSgTableText,
            String stepReadRealFileDataValue, String stepReadRealFileDataText,
            String stepInsertSgTableValue, String stepInsertSgTableText) throws Exception {
        logger.info("(4){} data.....", stepTruncateSgTableText);
        logSysBatchService.saveData(jobLogName, stepTruncateSgTableValue, stepTruncateSgTableText);
        logger.info("ExecSqlLoaderByCommandLine.....");
        int returnCode = -1;

        try {
            String systemSeparator = FileSystems.getDefault().getSeparator();
            String parentPath = Paths.get(templateCtlPath).getParent().toString() + systemSeparator;

            String cmd = new StringBuffer("sqlldr ").append(dBConfig.getUserName()).append("/")
                .append(dBConfig.getPassword()).append("@").append(sId).append(" control=").append(parentPath)
                .append(newCtlFileName).append(" log=").append(parentPath).append(templateCtlFileName)
                .append(".log rows = ").append(rows).toString();

            logger.info("(5){}.....", stepReadRealFileDataText);
            logSysBatchService.saveData(jobLogName, stepReadRealFileDataValue, stepReadRealFileDataText);
            logger.info("(6){}.....", stepInsertSgTableText);
            logSysBatchService.saveData(jobLogName, stepInsertSgTableValue, stepInsertSgTableText);
            logger.info("Exec cmd = {}", cmd);
            Process process = Runtime.getRuntime().exec(cmd);

            // Get input streams
            BufferedReader stdInput = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), Charset.forName("BIG5")));

            BufferedReader stdError = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), Charset.forName("BIG5")));

            // Read command standard output
            String s;
            logger.debug("Standard output: ");
            while ((s = stdInput.readLine()) != null) {
                logger.debug(s);
            }

            // Read command errors
            logger.debug("Standard error: ");
            while ((s = stdError.readLine()) != null) {
                logger.error(s);
            }

            // 0:成功
            returnCode = process.waitFor();
            logger.info("returnCode = {}", returnCode);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (returnCode != 0) {
            throw new Exception("ExecSqlLoaderByCommandLine fail.....");
        }
    }

}
