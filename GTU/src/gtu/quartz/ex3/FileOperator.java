package gtu.quartz.ex3;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileOperator {

    private static final Logger logger = LoggerFactory.getLogger(FileOperator.class);

    private static final String LINE_BREAKS = "\r\n";

    private static final String TARGET_FLAG_FILE = "targetFlagFile";

    private static final String TARGET_FILE_REALNAME = "targetFileRealName";

    private static final String TARGET_FILE_DATETIME = "targetFileDateTime";

    private static final String TARGET_FILE_SUFFIX_TXT = ".txt";

    private static final String TARGET_FILE_SUFFIX_CSV = ".csv";

    private DateTimeFormatter archiveDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String systemSeparator = FileSystems.getDefault().getSeparator();

    @Autowired
    LogSysBatchService logSysBatchService;

    public boolean checkSourceFilesExist(ExecutionContext executionContext, String flagFileName, String sourceFilePath,
            String jobLogName, String stepCheckFlgFileValue, String stepCheckFlgFileText, String stepReadFlgFileValue,
            String stepReadFlgFileText, String stepCheckRealFileValue, String stepCheckRealFileText, String bchDtType) {
        boolean isExist = false;
        String flagFilePath = sourceFilePath + FileSystems.getDefault().getSeparator();
        File targetFlagFile = new File(flagFilePath + flagFileName);

        logger.info("(1)Check {} exist.....", targetFlagFile.getAbsolutePath());
        logSysBatchService.saveData(jobLogName, stepCheckFlgFileValue, stepCheckFlgFileText);

        if (targetFlagFile.exists()) {
            executionContext.put(TARGET_FLAG_FILE, targetFlagFile.getAbsolutePath());
            logger.info("{} exist!", targetFlagFile.getAbsolutePath());
            logger.info("(2)Load {} data .....", targetFlagFile.getAbsolutePath());
            logSysBatchService.saveData(jobLogName, stepReadFlgFileValue, stepReadFlgFileText);
            String targetFileName = this.getTargetFileName(targetFlagFile);
            File targetFile = new File(flagFilePath + targetFileName);
            Path targetFilePath = Paths.get(flagFilePath, targetFileName);

            logger.info("(3)Check {} exist.....", targetFile.getAbsolutePath());
            logSysBatchService.saveData(jobLogName, stepCheckRealFileValue, stepCheckRealFileText);

            if (targetFile.exists()) {
                isExist = true;
                executionContext.put(TARGET_FILE_REALNAME, targetFile.getAbsolutePath());
                executionContext.put(TARGET_FILE_DATETIME, this.getTargetFileDateTimeByPath(targetFilePath, bchDtType));
                logger.info("{} exist!", targetFile.getAbsolutePath());

            } else {
                executionContext.put(TARGET_FILE_REALNAME, "");
                executionContext.put(TARGET_FILE_DATETIME, "");
                logger.info("{} does not exist!", targetFile.getAbsolutePath());
            }

        } else {
            executionContext.put(TARGET_FLAG_FILE, "");
            executionContext.put(TARGET_FILE_DATETIME, "");
            logger.info("{} does not exist!", targetFlagFile.getAbsolutePath());
        }

        if (!isExist) {
            executionContext.put(TARGET_FLAG_FILE, "");
            executionContext.put(TARGET_FILE_DATETIME, "");
            executionContext.put(TARGET_FILE_REALNAME, "");
        }

        return isExist;
    }

    public String getTargetFileName(File targetFlagFile) {
        String theFileName = "";

        try (ReadableByteChannel channelFromFile = Channels.newChannel(new FileInputStream(targetFlagFile))) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            channelFromFile.read(byteBuffer);
            theFileName = new String(byteBuffer.array());

            byteBuffer.clear();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return theFileName.trim();
    }

    public String getTargetFileDateTimeByPath(Path targetFilePath, String bchDtType) {
        return this.getTargetFileDateTimeByFileName(targetFilePath.getFileName().toString(), bchDtType);
    }

    public String getTargetFileDateTimeByFileName(String targetFileName) {
        return targetFileName.substring(targetFileName.length() - 12, targetFileName.length() - 4);
    }
    
    public String getTargetFileDateTimeByFileName(String targetFileName, String bchDtType) {
        String result = LocalDateTime.now().format(archiveDateTimeFormatter).toString();
        logger.info("getTargetFileDateTimeByFileName targetFileName {} bchDtType {} result {}", targetFileName, bchDtType, result);
        if(StringUtils.equals("00", bchDtType)) {
            return result;
        }
        return targetFileName.substring(targetFileName.length() - 12, targetFileName.length() - 4);
    }

    public void doMoveFiles(String filePath, String flagFilePath, String backupDirName, String jobLogName,
            String moveFileStepValue, String moveFileStepText) {
        logger.info("({})Move files, backupDirName = {} .....", this.getStepSeq(moveFileStepValue), backupDirName);
        logSysBatchService.saveData(jobLogName, moveFileStepValue, moveFileStepText);

        this.archiveFile(filePath, backupDirName);
        this.archiveFile(flagFilePath, backupDirName);
    }
    
    public void doMoveZipFiles(String filePath, String flagFilePath, String backupDirName, String jobLogName,
            String moveFileStepValue, String moveFileStepText) {
        logger.info("({})Move files, backupDirName = {} .....", this.getStepSeq(moveFileStepValue), backupDirName);
        logSysBatchService.saveData(jobLogName, moveFileStepValue, moveFileStepText);

        this.archiveZipFile(filePath, backupDirName);
        this.archiveZipFile(flagFilePath, backupDirName);
    }

    public String getDateTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String result = sdFormat.format(date);
        return result;
    }
    
    // 壓縮檔案
    private void doZipOutputStream(String sourceFilePath,  String backupDirName) throws IOException  {

        // 開啟欲壓縮的檔案
        File f = new File(sourceFilePath);
        FileInputStream fis = new FileInputStream(f);
        
        String systemSeparator = FileSystems.getDefault().getSeparator();

        // 開起壓縮後輸出的檔案 | 把副檔名去掉替換成zip
        // String[] strArray = f.getName().split("\\.");
        // ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(backupDirName + systemSeparator + strArray[0] + ".zip")));
        // 整個檔名後面加.zip
        ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(backupDirName + systemSeparator + f.getName() + ".zip")));

        /*
         * 在壓縮檔內建立一個項目(表示一個壓縮的檔案或目錄，可以目錄結構的方式表示， 解壓縮後可以設定的目錄結構放置檔案)
         */
        zOut.putNextEntry(new ZipEntry("ZIP/" + f.getName()));
        //zOut.putNextEntry(new ZipEntry(f.getParent() + f.getName()));

        // 設定壓縮的程度0~9
        zOut.setLevel(9);

        // 以byte的方式讀取檔案並寫入壓縮檔
        int byteNo;
        byte[] b = new byte[1024];
        while ((byteNo = fis.read(b)) > 0) {
            zOut.write(b, 0, byteNo);// 將檔案寫入壓縮檔
        }
        zOut.close();
        fis.close();
    }
    
    public void archiveZipFile(String sourceFilePath, String backupDirName) {
        if (StringUtils.isBlank(sourceFilePath)) {
            return;
        }

        File sourceFile = new File(sourceFilePath);
        String theBackupDir = new StringBuilder(this.getBackupParentDir(sourceFilePath))
            .append(backupDirName)
            .append(systemSeparator)
            .append(LocalDateTime.now().format(archiveDateTimeFormatter)).toString();
        File theBackupDirFile = new File(theBackupDir);

        if (!theBackupDirFile.exists()) {
            theBackupDirFile.mkdirs();
        }

        Path targetPath = Paths.get(theBackupDir, sourceFile.getName());

        try {
            doZipOutputStream(sourceFilePath, theBackupDir);
            this.deleteFile(sourceFile.getParent(), sourceFile.getName());
        } catch (IOException e) {
            logger.error("Move file " + sourceFile.getPath() + " failed!", e);
        } 

        logger.info("Move file {} to targetPath = {} complete!", sourceFilePath, targetPath);
    }
    
    public void archiveFile(String sourceFilePath, String backupDirName) {
        if (StringUtils.isBlank(sourceFilePath)) {
            return;
        }

        File sourceFile = new File(sourceFilePath);
        String theBackupDir = new StringBuilder(this.getBackupParentDir(sourceFilePath))
            .append(backupDirName)
            .append(systemSeparator)
            .append(LocalDateTime.now().format(archiveDateTimeFormatter)).toString();
        File theBackupDirFile = new File(theBackupDir);

        if (!theBackupDirFile.exists()) {
            theBackupDirFile.mkdirs();
        }

        Path targetPath = Paths.get(theBackupDir, sourceFile.getName());

        try {
            this.deleteFile(theBackupDir, sourceFile.getName());
            Files.move(Paths.get(sourceFilePath), targetPath);

        } catch (IOException e) {
            logger.error("Move file " + sourceFile.getPath() + " failed!", e);
        }

        logger.info("Move file {} to targetPath = {} complete!", sourceFilePath, targetPath);
    } 

    public void deleteBackupSourceFiles(String sourceFilePath, String backupDirName, int fileBackupDays,
            String jobLogName, String moveFileStepValue, String moveFileStepText) {
        logger.info("({})File Housekeeping .....", this.getStepSeq(moveFileStepValue));
        logSysBatchService.saveData(jobLogName, moveFileStepValue, moveFileStepText);
        String theBackupDir = this.getBackupParentDir(sourceFilePath) + backupDirName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        logger.info("deleteBaseDate = {}", LocalDate.now().minusDays(fileBackupDays));
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(theBackupDir))) {
            for (Path path : directoryStream) {

                if (path.toFile().isDirectory()) {

                    LocalDate dirDate = LocalDate.parse(path.toFile().getName(), formatter);

                    if (dirDate.isBefore(LocalDate.now().minusDays(fileBackupDays))) {
                        logger.info("delete dir {} (includes files inside)!", path.toFile());
                        FileUtils.deleteDirectory(path.toFile());
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private String getBackupParentDir(String sourceFilePath) {
        File sourceFile = new File(sourceFilePath);
        File parentDir = sourceFile.getParentFile();
        File grandParentDir = parentDir.getParentFile();

        return grandParentDir + systemSeparator;
    }
    
    public void generateEmptyFile(String dirPath, String fileName) {
        
        String systemSeparator = FileSystems.getDefault().getSeparator();
        
        File file = new File(dirPath + systemSeparator + fileName);
        
        if(!file.exists())
        {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void generateFile(String dirPath, String fileName, String content, String charsetName) {
        Path targetDir = Paths.get(dirPath);
        Path target = Paths.get(dirPath, fileName);

        if (!targetDir.toFile().exists()) {
            targetDir.toFile().mkdirs();
        }

        Charset charset = Charset.forName(charsetName);
        byte[] datas = (content + LINE_BREAKS).getBytes(charset);
        OpenOption[] openOptions = new OpenOption[] { StandardOpenOption.APPEND, StandardOpenOption.CREATE,
                StandardOpenOption.WRITE };

        try (
                ReadableByteChannel channelFrom = Channels.newChannel(new ByteArrayInputStream(datas));
                WritableByteChannel channelTo = Files.newByteChannel(target, openOptions)) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while ((channelFrom.read(byteBuffer)) > 0) {
                byteBuffer.flip();
                channelTo.write(byteBuffer);
                byteBuffer.flip();
            }

            logger.debug("generateFile task complete -> {}", target.toAbsolutePath());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void generateCtlFileFromTemplate(String templateCtlPath, String templateCtlFileName,
            String targetFileRealName, String newCtlFileName, String newCtlFilePath) throws IOException {
        Path fromTemplatePath = Paths.get(templateCtlPath, templateCtlFileName + ".ctl");
        Path toCtlPath = null;
        logger.info("fromTemplatePath = {} ", fromTemplatePath.toAbsolutePath());

        try (
                ReadableByteChannel channelFromFile = Channels
                    .newChannel(new FileInputStream(fromTemplatePath.toAbsolutePath().toString()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                WritableByteChannel channelToOutputStream = Channels.newChannel(baos)) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while ((channelFromFile.read(byteBuffer)) > 0) {
                byteBuffer.flip();
                channelToOutputStream.write(byteBuffer);
                byteBuffer.flip();
            }

            String recordStr = baos.toString();
            byteBuffer.clear();

            File sourceFile = new File(fromTemplatePath.toAbsolutePath().toString());
            File parentDir = sourceFile.getParentFile().getParentFile();
            toCtlPath = Paths.get(parentDir.toString(), newCtlFileName);

            // delete file first
            this.deleteFile(parentDir.toString(), newCtlFileName);

            // check targetFileRealName suffix
            String targetFileSuffix = TARGET_FILE_SUFFIX_TXT;
            if (targetFileRealName.indexOf(TARGET_FILE_SUFFIX_CSV) != -1) {
                targetFileSuffix = TARGET_FILE_SUFFIX_CSV;
            }

            // replace source fileName to source fileName + sourceFileDate
            recordStr = recordStr.replaceAll(new StringBuffer(templateCtlFileName).append(targetFileSuffix).toString(),
                new StringBuilder(newCtlFilePath)
                    .append(systemSeparator)
                    .append(systemSeparator)
                    .append(targetFileRealName).toString());

            // generateCtlFile
            this.generateCtlFile(recordStr, toCtlPath);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        logger.debug("generateCtlFileFromTemplate task complete -> {}",
            toCtlPath != null ? toCtlPath.toAbsolutePath() : "");
    }

    private void generateCtlFile(String recordStr, Path toCtlPath) {
        byte[] datas = recordStr.getBytes();
        OpenOption[] openOptions = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE };
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try (
                ReadableByteChannel channelFromCtl = Channels.newChannel(new ByteArrayInputStream(datas));
                WritableByteChannel channelToCtl = Files.newByteChannel(toCtlPath, openOptions)) {

            while ((channelFromCtl.read(byteBuffer)) > 0) {
                byteBuffer.flip();
                channelToCtl.write(byteBuffer);
                byteBuffer.flip();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public boolean deleteFile(String filePath, String fileName) throws IOException {
        Path path = Paths.get(filePath, fileName);

        return Files.deleteIfExists(path);
    }

    public void generateFlgFile(String flgFilePath, String realFileName, String flgFileName) throws IOException {
        byte[] datas = realFileName.getBytes();
        Path target = Paths.get(flgFilePath, flgFileName);

        this.deleteFile(flgFilePath, flgFileName);
        OpenOption[] openOptions = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE };

        try (
                ReadableByteChannel channelFrom = Channels.newChannel(new ByteArrayInputStream(datas));
                WritableByteChannel channelTo = Files.newByteChannel(target, openOptions)) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            while ((channelFrom.read(byteBuffer)) > 0) {
                byteBuffer.flip();
                channelTo.write(byteBuffer);
                byteBuffer.flip();
            }

            logger.debug("generateFlgFile task complete -> {}", target.toAbsolutePath());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String getStepSeq(String stepValue) {
        StringBuilder sb = new StringBuilder();
        if (stepValue.length() >= 4) {
            sb.append(stepValue.substring(4));
        }

        return sb.toString();
    }

}
