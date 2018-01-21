package gtu.efficiency;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DuringMointer {
    
    public static void main(String[] args) {
        DuringMointer d = new DuringMointer();
        During a1 = new During("迴圈一億次1");
        for(int ii = 0 ;ii < 100000000; ii ++) {
        }
        a1.count();
        
        During a2 = new During("迴圈一億次2");
        for(int ii = 0 ;ii < 100000000; ii ++) {
        }
        a2.count();
        During.printAll();
        System.out.println("done...");
    }
    
    
    public static class During {
        private static final Logger logger = Logger.getLogger(During.class);
        
        private static Set<During> REPORT_MAP = new LinkedHashSet<During>();
        long startTime;
        long duringTime;
        StackTraceElement startStack;
        StackTraceElement endStack;
        String resultMessage;
        String tag;
        
        public During() {
            this("");
        }
        
        public During(String tag){
            this.startTime = System.currentTimeMillis();
            this.tag = tag;
            startStack = getCallerStack();
            REPORT_MAP.add(this);
        }
        
        public String count() {
            if(StringUtils.isBlank(resultMessage)) {
                duringTime = System.currentTimeMillis() - startTime;
                endStack = getCallerStack();
                resultMessage = getFormatResult();
            }
            return resultMessage;
        }
        
        public void print() {
            logger.info(count());
        }
        
        public static void printAll() {
            for(During d : REPORT_MAP) {
                d.print();
            }
            logger.info("report size : " + REPORT_MAP.size());
        }
        
        private String getFormatResult() {
            String _tag = StringUtils.isNotBlank(tag) ? "[" + tag + "]" : "";
            String classInfo = getClassInfo();
            String startLineNumber = String.valueOf(startStack.getLineNumber());
            String endLineNumber = String.valueOf(endStack.getLineNumber());
            return String.format("%s %s 行數[%s -> %s] 耗時 : %s", _tag, classInfo, startLineNumber, endLineNumber, duringTime);
        }
        
        private String getSimpleClassName(StackTraceElement stack) {
            String clzName = stack.getClassName();
            int pos = clzName.lastIndexOf(".");
            if(pos != -1) {
                clzName = clzName.substring(pos + 1);
            }
            return clzName;
        }
        
        private String getClassInfo() {
            if(StringUtils.equals(endStack.getClassName() ,startStack.getClassName())){
                if(StringUtils.equals(endStack.getMethodName(), startStack.getMethodName())) {
                    return getSimpleClassName(startStack) + "." + startStack.getMethodName() + "()";
                }else {
                    return getSimpleClassName(startStack) + "." + startStack.getMethodName() + "->" + endStack.getMethodName() + "()";
                }
            }else {
                return getSimpleClassName(startStack) + "." + startStack.getMethodName() + "->" + getSimpleClassName(endStack) + endStack.getMethodName() + "()";
            }
        }
        
        private StackTraceElement getCallerStack() {
            Class clz = this.getClass();
            boolean findOk = false;
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            StackTraceElement rtn = null;
            for(int ii = 0 ; ii< st.length; ii ++) {
                StackTraceElement s = st[ii];
                if(StringUtils.equals(clz.getName(), s.getClassName())) {
                    findOk = true;
                }else if(findOk == true && !StringUtils.equals(clz.getName(), s.getClassName())) {
                    //System.out.println("--->" + ii + " - " + s.getClassName() + " : " + s.getMethodName() + " : " + s.getLineNumber());
                    rtn = s;
                    break;
                }else {
                    //System.out.println(ii + " - " + s.getClassName() + " : " + s.getMethodName() + " : " + s.getLineNumber());
                }
            }
            return rtn;
        }
    }
}
