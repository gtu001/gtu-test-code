package gtu.log;

import gtu.file.FileUtil;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class Logger2File {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd HH:mm:ss");
    private static final Logger2File INSTANCE = new Logger2File("Logger", true);

    private BufferedWriter out;
    private boolean logAppend;
    private File logFile;

    public static Logger2File getLogger() {
        return INSTANCE;
    }

    public Logger2File(String filename, boolean logAppend) {
        this(null, filename, logAppend);
    }

    public Logger2File(String basepath, String filename, boolean logAppend) {
        String path = FileUtil.DESKTOP_PATH;
        if (StringUtils.isNotBlank(basepath)) {
            path = basepath;
        }
        String fileFullName = path + "/" + filename + ".log";
        logFile = new File(fileFullName);
        this.logAppend = logAppend;
    }

    private void checkPrintStreamAvailable() {
        if (out == null) {
            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, logAppend), "utf8"));
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
            if (StringUtils.equals(sks[ii].getFileName(), Logger2File.class.getSimpleName() + ".java")) {
                findThisOk = true;
            }
            if (findThisOk && //
                    !StringUtils.equals(sks[ii].getFileName(), Logger2File.class.getSimpleName() + ".java")) {
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

    public void close() {
        this.debug("logger will close...");
        if (out != null) {
            try {
                out.flush();
                out.close();
                out = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void warn(Object... vals) {
        this.debug(vals);
    }

    public void warn(Object message, Throwable ex) {
        this.error(message, ex);
    }

    public void debug(Object... vals) {
        this.debug(Arrays.toString(vals));
    }

    public void debug(String message, Object val) {
        this.debug(message + " = " + val);
    }

    public void debug(Throwable ex) {
        this.error(ex);
    }

    public void info(String message) {
        this.debug(message);
    }

    public void error(String message) {
        this.debug(message);
    }

    public void error(Throwable ex) {
        this.error(ex.getMessage(), ex);
    }

    public synchronized void debug(String message) {// root
        checkPrintStreamAvailable();
        String msg = getPrefix() + message;
        System.out.println(msg);
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void error(Object message, Throwable ex) {// root
        this.debug("[ERROR]:" + message);
        try {
            printError(ex, true);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
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
            }else{
                System.out.println("logFile不存在!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 顯示檔案
     */
    public void showFileIndicateEditor(File editorExe) {
        try {
            if (logFile != null && logFile.exists()) {
                String command = String.format("cmd /c call \"%s\" \"%s\"", editorExe, logFile);
                Runtime.getRuntime().exec(command);
                System.out.println(command);
            }else{
                System.out.println("logFile不存在!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}