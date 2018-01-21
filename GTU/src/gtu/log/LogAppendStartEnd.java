package gtu.log;

import gtu.file.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;




public class LogAppendStartEnd {

    public static void main(String[] args) throws IOException {
        LogAppendStartEnd test = new LogAppendStartEnd(null, null, false);
        File file = new File("C:/workspace/workspace_farEastStone/estore/fet_estore_search_engie_revamp/revamp_source_code/src/main/java/org/hibernate/event/def/AbstractFlushingEventListener.java");
        test.execute(file);
        System.out.println("done...");
    }
    
    private boolean onOffControl = true;
    private String logStartFormat;
    private String logEndFormat;
    public LogAppendStartEnd(String logStartFormat, String logEndFormat, boolean onOffControl) {
        this.logStartFormat = logStartFormat;
        this.logEndFormat = logEndFormat;
        this.onOffControl = onOffControl;
    }
    
    private static final String LOG_FORMAT = "log.info(%s);/* gtu001 */%n";
    private static final String LOG_FORMAT_ONOFF = "if(isDebug()) {log.info(%s);}/* gtu001 */%n";
    private static final String IS_DEBUG_METHOD;
    static{
        StringBuilder sb = new StringBuilder();
        sb.append("    private boolean isDebug() {                                                                               \n");
        sb.append("        java.util.Properties prop = new java.util.Properties();                                               \n");
        sb.append("        try{                                                                                                  \n");
        sb.append("            prop.load(new java.io.FileInputStream(\"C:/Users/gtu001/Desktop/deepClassInfo.properties\"));       \n");
        sb.append("            if(prop.containsKey(this.getClass().getSimpleName())){                                               \n");
        sb.append("                if(\"off\".equals(prop.getProperty(this.getClass().getSimpleName()))){                          \n");
        sb.append("                    return false;                                                                             \n");
        sb.append("                }                                                                                             \n");
        sb.append("            }                                                                                                 \n");
        sb.append("            return true;                                                                                      \n");
        sb.append("        }catch(Exception ex){                                                                                 \n");
        sb.append("            return true;                                                                                      \n");
        sb.append("        }                                                                                                     \n");
        sb.append("    }                                                                                                         \n");
        sb.append("    public void showStackTraceInfo() {                                                \n");
        sb.append("        log.info(\"#. showStackTraceInfo .s\");                                         \n");
        sb.append("        for (StackTraceElement s : Thread.currentThread().getStackTrace()) {          \n");
        sb.append("            log.info(\"--->\" + s);                                                     \n");
        sb.append("        }                                                                             \n");
        sb.append("        log.info(\"#. showStackTraceInfo .e\");                                         \n");
        sb.append("    }                                                                                 \n");
        IS_DEBUG_METHOD = sb.toString();
    }
    
    public void execute(File file){
        try{
            //掃一次原始文件
            Pattern classStartPattern = Pattern.compile("public\\sclass\\s[A-Z]");
            Pattern methodStartPattern = Pattern.compile("[\\/\\*\\s\\d]*(public|protected)\\s([^\\(]+)\\(");
            Pattern methodEndPattern = Pattern.compile("[\\/\\*\\s\\d]*\\}");
            Matcher matcher = null;
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            
            Map<Integer,MethodInfo> methodMap = new LinkedHashMap<Integer,MethodInfo>();
            SingletonMap singleMap = new SingletonMap();
            singleMap.setValue(new MethodInfo());
            
            int finalEndline = -1;
            
            int skipClassStartLine = -1;
            boolean skipClassStartOk = false;
            for(String line = null; (line = reader.readLine())!=null;){
                if(line.endsWith("}")){
                    finalEndline = Math.max(finalEndline, reader.getLineNumber());
                }
                matcher = classStartPattern.matcher(line);
                if(matcher.find()){
                    System.out.println("CLASS START = " + line);
                    skipClassStartLine = reader.getLineNumber();
                    if(line.endsWith("{")){
                        skipClassStartOk = true;
                    }
                    continue;
                }
                if(skipClassStartLine != -1 && skipClassStartOk == false && line.endsWith("{")){
                    skipClassStartOk = true;
                    continue;
                }
                
                MethodInfo mth = (MethodInfo)singleMap.getValue();
                matcher = methodStartPattern.matcher(line);
                if(matcher.find()){
                    String methodName = matcher.group(2);
                    int dentPos = line.indexOf(matcher.group(1));
                    System.out.println(methodName);
                    System.out.println("start pos " + dentPos + " / " + reader.getLineNumber());
//                    System.out.println("start" + reader.getLineNumber());
                    mth.name = methodName.trim();
                    mth.dentPos = dentPos;
                    if(methodName.contains(" ")){
                        mth.returnType = methodName.split(" ")[0];
                        if("void".equals(mth.returnType)){
                            mth.returnType = null;
                        }
                    }
                    if(line.endsWith("{")){
                        mth.start = reader.getLineNumber();
                        methodMap.put(reader.getLineNumber(), mth);
                    }
                }
                if(mth.name!= null && mth.start == -1 && line.endsWith("{")){
                    mth.start = reader.getLineNumber();
                    methodMap.put(reader.getLineNumber(), mth);
                }
                matcher = methodEndPattern.matcher(line);
                if(matcher.find()){
//                    System.out.println("end" + reader.getLineNumber());
                    int dentPos = line.lastIndexOf("}");
                    System.out.println("end pos " + dentPos + " / " + reader.getLineNumber());
                    if(mth.dentPos !=-1 && dentPos == mth.dentPos){
                        int endKey = reader.getLineNumber();
                        if(mth.returnType!=null){
                            endKey -= 1;
                        }
                        mth.end = endKey;
                        if(mth.isValid()){
                            methodMap.put(endKey, mth);
                            //刷新物件
                            singleMap.setValue(new MethodInfo());
                        }else{
                            System.out.println("物件無效 : " + mth);
                        }
                    }
                }
            }
            reader.close();
            
            for(MethodInfo value : methodMap.values()){
                System.out.println(value);
            }
            
            String logFormat = onOffControl ? LOG_FORMAT_ONOFF : LOG_FORMAT;
            
            //開始附加log
            Pattern returnTypePattern = Pattern.compile("[\\/\\*\\s\\d]*return\\s(.*\\;)");
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            StringBuilder sb = new StringBuilder();
            for(String line = null; (line = reader.readLine())!=null;){
                int key = reader.getLineNumber();
                if(methodMap.containsKey(key)){
                    MethodInfo info = methodMap.get(key);
                    String preSpace = StringUtils.leftPad("", (info.dentPos!=-1?info.dentPos:0) + 4, ' ');
                    if(info.start == key){
                        System.out.println("START---------------->" + key);
                        sb.append(line + "\n");
                        sb.append(preSpace+String.format(logFormat, LogStyle.START.appendLog(info, logStartFormat)));
                    }else if(info.end == key){
                        matcher = returnTypePattern.matcher(line);
                        if(matcher.find()){
                            System.out.println("find Return ==>" + line);
                            String returnValueStr = matcher.group(1);
                            returnValueStr = info.returnType + " returnValue = " + "("+ info.returnType + ")" + returnValueStr;
                            sb.append(preSpace+returnValueStr + "\n");
                            sb.append(preSpace+String.format(logFormat,  LogStyle.END.appendLog(info, logEndFormat)));
                            sb.append(preSpace+String.format(logFormat, "\"" + info.returnType + " = \" + returnValue"));
                            sb.append(preSpace+"return returnValue;\n");
//                            sb.append("}\n");//看起來不用
                        }else{
                            sb.append(preSpace+String.format(logFormat,  LogStyle.END.appendLog(info, logEndFormat)));
                            sb.append(line + "\n");
                        }
                    }else{
                        System.out.println("ERROR : " + info.start + " /// " + key);
                    }
                }else if(key == finalEndline && onOffControl){
                    sb.append("\n");
                    sb.append(IS_DEBUG_METHOD);
                    sb.append(line + "\n");
                }else{
                    sb.append(line + "\n");
                }
            }
            reader.close();
            
            File bakFile = new File(file.getParent(), file.getName() + ".bak");
            file.renameTo(bakFile);
            FileUtils.write(file, sb, "utf8");
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    enum LogStyle {
        START("\"#. ${method} .s\""),
        END("\"#. ${method} .e\"")
        ;
        String format;
        LogStyle(String format){
            this.format = format;
        }
        String appendLog(MethodInfo info, String format){
            if(StringUtils.isBlank(format)){
                format = this.format;
            }
            return format.replaceAll(Pattern.quote("${method}"), info.name);
        }
    }
    
    static class MethodInfo {
        String name;
        String returnType;
        int start = -1;
        int end = -1;
        int dentPos = -1;
        @Override
        public String toString() {
            return "MethodInfo [name=" + name + ", returnType=" + returnType + ", start=" + start + ", end=" + end
                    + "]";
        }
        boolean isValid(){
            if(name == null){
                return false;
            }
            if(start == -1){
                return false;
            }
            if(end == -1){
                return false;
            }
            if(start > end){
                throw new RuntimeException("起始大於結束 : " + this);
            }
            return true;
        }
    }
}