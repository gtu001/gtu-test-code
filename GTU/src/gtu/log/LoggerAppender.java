package gtu.log;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class LoggerAppender {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd HH:mm:ss");

    private BufferedWriter out;
    private File logFile;

    public LoggerAppender(File logFile) {
        this.logFile = logFile;
    }

    private void checkPrintStreamAvailable() {
        if (out == null) {
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), "utf8"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private String getPrefix() {
        StackTraceElement[] sks = Thread.currentThread().getStackTrace();
        StackTraceElement currentElement = null;
        boolean findThisOk = false;
        for (int ii = 0; ii < sks.length; ii++) {
            if (StringUtils.equals(sks[ii].getFileName(), LoggerAppender.class.getSimpleName() + ".java")) {
                findThisOk = true;
            }
            if (findThisOk && //
                    !StringUtils.equals(sks[ii].getFileName(), LoggerAppender.class.getSimpleName() + ".java")) {
                currentElement = sks[ii];
                break;
            }
        }
        String timestamp = SDF.format(new Date());
        if (currentElement != null) {
            return timestamp + "(" + currentElement.getClassName() + ":" + currentElement.getLineNumber() + ")";
        }
        return "(" + timestamp + ")";
    }

    public void debug(String message) {
        this.debug(message, null);
    }

    public synchronized void debug(String message, Throwable ex) {// root
        checkPrintStreamAvailable();
        String msg = getPrefix() + message;
        System.out.println(msg);
        try {
            out.write(msg + "\n");
            if (ex != null) {
                printError(ex, true);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
            out = null;
        }
    }

    private void printError(Throwable ex, boolean isRoot) {
        try {
            if (isRoot) {
                out.write(ex + "\n");
            } else {
                out.write("Caused by: " + ex + "\n");
            }
            for (StackTraceElement se : ex.getStackTrace()) {
                out.write("\tat " + se + "\n");
            }
            if (ex.getCause() != null) {
                printError(ex.getCause(), false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 顯示檔案
     */
    public void showFile() {
        try {
            if (logFile != null && logFile.exists()) {
                Desktop.getDesktop().browse(logFile.toURI());
            } else {
                System.out.println("logFile不存在!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getLogFile() {
        return logFile;
    }
}