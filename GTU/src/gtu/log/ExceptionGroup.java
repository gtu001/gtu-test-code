package gtu.log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

public class ExceptionGroup {
    private Logger log = LoggerFactory.getLogger(getClass());
    private List<Throwable> exceptionList = new ArrayList<Throwable>();
    private final Pattern matchPattern;

    public ExceptionGroup(String matchPattern) {
        this.matchPattern = Pattern.compile(matchPattern, Pattern.CASE_INSENSITIVE);
    }

    /**
     * 加入exception
     */
    public void addExecption(Throwable e) {
        exceptionList.add(e);
    }
    
    public void checkException() {
        log.error("#.Exception Size=" + exceptionList.size());
        if (!exceptionList.isEmpty()) {
            log.error("#.Exception START===============================================================");
            int ii = 0;
            for (Throwable ex : exceptionList) {
                log.error("error index " + (++ii), ex);
            }
            
            List<String> messageList = new ArrayList<String>();
            for (Throwable ex : exceptionList) {
                this.getThrowMessage(ex, messageList);
            }
            log.error("#.Exception END=================================================================");
            
            StringBuilder err = new StringBuilder();
            err.append("\nsummary start=========================================\n");
            for(String message : messageList){
                err.append(message + "\n");
            }
            err.append("\nsummary end  =========================================\n");
            throw new RuntimeException("有錯誤發生:" + err.toString());
        }
    }
    
    private void getThrowMessage(Throwable ex, List<String> messageList) {
        messageList.add(ex.getMessage());
        for (StackTraceElement e : ex.getStackTrace()) {
            if (matchPattern.matcher(e.getClassName()).find()) {
                String message = e.getClassName() + "." + e.getMethodName() + ":" + e.getLineNumber();
                if(!messageList.contains(message)){
                    messageList.add(message);
                }
            }
        }
        if (ex.getCause() != null) {
            getThrowMessage(ex.getCause(), messageList);
        }
    }
}