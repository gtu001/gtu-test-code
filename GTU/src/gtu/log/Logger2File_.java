package gtu.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class Logger2File_ {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd HH:mm:ss");
    private static final Logger2File_ INSTANCE = new Logger2File_("Logger", false, true, true);
    
    private PrintStream out;
    private Logger2FileConfig config;
    
    public static Logger2File_ getLogger(){
        return INSTANCE;
    }
    
    public Logger2File_(String filename, boolean createNew, boolean writeFile, boolean writeSysout) {
        Logger2FileConfig config = new Logger2FileConfig();
        config.filename = filename;
        config.createNew = createNew;
        config.writeFile = writeFile;
        config.writeSysout = writeSysout;
        this.config = config;
    }
    
    private void checkPrintStreamAvailable(){
        if(out == null && config.writeFile){
            try {
                int index = 0;
                String fileFullName = null;
                if(config.createNew){
                  //累加版
                    do{
//                        String path = FileUtil.DESKTOP_PATH;
                        String path = "D:/";
                        String fileName2 = config.filename + (index != 0 ? "_" + index : "") + ".log";
                        fileFullName = path + "/" + fileName2;
                        index ++;
                    }while(new File(fileFullName).exists());
                }else{
                  //非累加版
//                    String path = FileUtil.DESKTOP_PATH;
                    String path = "D:/";
                    String fileName2 = config.filename + (index != 0 ? "_" + index : "") + ".log";
                    fileFullName = path + "/" + fileName2;
                }
                config.logFile = new File(fileFullName);
                out = new PrintStream(new FileOutputStream(config.logFile), true, "utf8");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private String getPrefix(){
        String tname = "[" + Thread.currentThread().getName() + "]";
        StackTraceElement[] sks = Thread.currentThread().getStackTrace();
        StackTraceElement currentElement = null;
        boolean findThisOk = false;
        for (int ii = 0; ii < sks.length; ii++) {
            if (StringUtils.equals(sks[ii].getFileName(), Logger2File_.class.getSimpleName() + ".java")) {
                findThisOk = true;
            }
            if (findThisOk && //
                    !StringUtils.equals(sks[ii].getFileName(), Logger2File_.class.getSimpleName() + ".java") ) {
                currentElement = sks[ii];
                break;
            }
        }
        String timestamp = SDF.format(new Date());
        if (currentElement != null) {
            return tname + timestamp + "(" + currentElement.getClassName() + ":" + currentElement.getLineNumber() + ")";
        }
        return "(" + tname + timestamp + ")";
    }
    
    public void close(){
        this.debug("logger will close...");
        if(out != null){
            try{
                out.flush();
                out.close();
                out = null;
            }catch(Exception ex){
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
    
    public void debug(Throwable ex){
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
    
    public void debug(String message) {//root
        checkPrintStreamAvailable();
        String msg = getPrefix() + message;
        if(config.writeSysout){
            System.out.println(msg);
        }
        if(config.writeFile){
            out.println(msg);
            out.flush();
        }
    }
    
    public void error(Object message, Throwable ex) {//root
        this.debug("[ERROR]:" + message);
        if(config.writeFile){
            ex.printStackTrace(out);
            out.flush();
        }
        ex.printStackTrace();
    }

    public Logger2FileConfig getConfig() {
        return config;
    }
    
    public static class Logger2FileConfig{
        String filename;
        boolean createNew = false;
        boolean writeFile = false;
        boolean writeSysout = true;
        File logFile;
        public String getFilename() {
            return filename;
        }
        public void setFilename(String filename) {
            this.filename = filename;
        }
        public boolean isCreateNew() {
            return createNew;
        }
        public void setCreateNew(boolean createNew) {
            this.createNew = createNew;
        }
        public boolean isWriteFile() {
            return writeFile;
        }
        public void setWriteFile(boolean writeFile) {
            this.writeFile = writeFile;
        }
        public boolean isWriteSysout() {
            return writeSysout;
        }
        public void setWriteSysout(boolean writeSysout) {
            this.writeSysout = writeSysout;
        }
        public File getLogFile() {
            return logFile;
        }
        public void setLogFile(File logFile) {
            this.logFile = logFile;
        }
    }
}