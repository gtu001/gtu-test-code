package gtu.efficiency;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DurningMointer {

    public static void main(String[] args) {
        DurningMointer d = new DurningMointer();
        Durning a1 = new Durning("迴圈一億次1");
        for (int ii = 0; ii < 100000000; ii++) {
        }
        a1.count("XXXXX2X");

        Durning a2 = new Durning("迴圈一億次2");
        for (int ii = 0; ii < 100000000; ii++) {
        }
        a2.count("XXXXX1X");
        Durning.printAll(false, 0);
        System.out.println("done...");
    }

    public static class Durning {
        private static final Logger logger = LogManager.getLogger(Durning.class);

        private static Set<Durning> REPORT_MAP = new LinkedHashSet<Durning>();
        long startTime;
        long duringTime;
        StackTraceElement startStack;
        StackTraceElement endStack;
        String resultMessage;
        String tag;
        boolean isLoop = false;// 重複使用
        Object comment;

        public Durning() {
            this("");
        }

        public Durning(String tag) {
            this.startTime = System.currentTimeMillis();
            this.tag = tag;
            startStack = getCallerStack();
            REPORT_MAP.add(this);
        }

        public String count() {
            return count(null);
        }

        public String count(Object comment) {
            this.setComment(comment);
            if (StringUtils.isBlank(resultMessage)) {
                duringTime = System.currentTimeMillis() - startTime;
                endStack = getCallerStack();
                resultMessage = getFormatResult();
            }
            return resultMessage;
        }

        private static void appendTagDurning(Durning d, Map<String, Pair<Long, Integer>> duringMap) {
            String tag = d.getSummaryTag();
            if (StringUtils.isNotBlank(tag)) {
                Long sumTime = 0L;
                Integer countTime = 0;
                if (duringMap.containsKey(tag)) {
                    sumTime = duringMap.get(tag).getLeft();
                    countTime = duringMap.get(tag).getRight();
                }
                sumTime += d.duringTime;
                countTime++;
                duringMap.put(tag, Pair.of(sumTime, countTime));
            }
        }

        public static String printAll(boolean ignoreLoop, long overDurningTime) {
            // 檢查重複
            List<Durning> lst1 = new ArrayList<Durning>(REPORT_MAP);
            for (int ii = 0; ii < lst1.size(); ii++) {
                for (int jj = ii + 1; jj < lst1.size(); jj++) {
                    Durning d1 = lst1.get(ii);
                    Durning d2 = lst1.get(jj);
                    if (StringUtils.equals(d1.getSummaryTag(), d2.getSummaryTag())) {
                        d1.isLoop = true;
                        d2.isLoop = true;
                    }
                }
            }

            List<String> logLst = new ArrayList<String>();
            Map<String, Pair<Long, Integer>> duringMap = new TreeMap<String, Pair<Long, Integer>>();
            for (Durning d : REPORT_MAP) {
                if (ignoreLoop && d.isLoop) {
                    // ignore
                } else if (overDurningTime > 0 && d.duringTime < overDurningTime) {
                    // ignore
                } else {
                    logLst.add(d.count());
                }
                appendTagDurning(d, duringMap);
            }

            // 加總迴圈
            for (String tag : duringMap.keySet()) {
                Pair<Long, Integer> sumData = duringMap.get(tag);
                if (sumData.getRight() > 1) {
                    Long sumDuring = sumData.getLeft();
                    String msg = String.format(" 總計[%s] : %s", tag, sumDuring);
                    logLst.add(msg);
                }
            }

            logLst.add("report size : " + REPORT_MAP.size());
            for (String log : logLst) {
                logger.info(log);
            }
            return StringUtils.join(logLst, "\n");
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

        private String getFormatResult() {
            String _tag = StringUtils.isNotBlank(tag) ? "[" + tag + "]" : "";
            String classInfo = getClassInfo();
            String startLineNumber = String.valueOf(getStartStack().getLineNumber());
            String endLineNumber = String.valueOf(getEndStack().getLineNumber());
            String comment = "";
            if (this.comment != null) {
                comment = StringUtils.trimToEmpty(String.valueOf(this.comment));
                if (StringUtils.isNotBlank(comment)) {
                    comment = ", 備註 : " + comment;
                }
            }
            return String.format("%s %s 行數[%s -> %s] 耗時 : %s %s", _tag, classInfo, startLineNumber, endLineNumber,
                duringTime, comment);
        }

        private String getSummaryTag() {
            String _tag = StringUtils.isNotBlank(tag) ? "[" + tag + "]" : "";
            String classInfo = getClassInfo();
            String startLineNumber = String.valueOf(getStartStack().getLineNumber());
            String endLineNumber = String.valueOf(getEndStack().getLineNumber());
            return String.format("%s %s 行數[%s -> %s]", _tag, classInfo, startLineNumber, endLineNumber);
        }

        private String getSimpleClassName(StackTraceElement stack) {
            String clzName = stack.getClassName();
            int pos = clzName.lastIndexOf(".");
            if (pos != -1) {
                clzName = clzName.substring(pos + 1);
            }
            return clzName;
        }

        private String getClassInfo() {
            if (StringUtils.equals(getEndStack().getClassName(), getStartStack().getClassName())) {
                if (StringUtils.equals(getEndStack().getMethodName(), getStartStack().getMethodName())) {
                    return getSimpleClassName(getStartStack()) + "." + getStartStack().getMethodName() + "()";
                } else {
                    return getSimpleClassName(getStartStack()) + "." + getStartStack().getMethodName() + "->"
                            + getEndStack().getMethodName() + "()";
                }
            } else {
                return getSimpleClassName(getStartStack()) + "." + getStartStack().getMethodName() + "->"
                        + getSimpleClassName(getEndStack()) + getEndStack().getMethodName() + "()";
            }
        }

        private StackTraceElement getStartStack() {
            if (startStack == null) {
                return new StackTraceElement("NA", "NA", "NA", 0);
            }
            return startStack;
        }

        private StackTraceElement getEndStack() {
            if (endStack == null) {
                return new StackTraceElement("NA", "NA", "NA", 0);
            }
            return endStack;
        }

        private StackTraceElement getCallerStack() {
            Class clz = this.getClass();
            boolean findOk = false;
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            StackTraceElement rtn = null;
            for (int ii = 0; ii < st.length; ii++) {
                StackTraceElement s = st[ii];
                if (StringUtils.equals(clz.getName(), s.getClassName())) {
                    findOk = true;
                } else if (findOk == true && !StringUtils.equals(clz.getName(), s.getClassName())) {
                    // System.out.println("--->" + ii + " - " + s.getClassName()
                    // + " : " +
                    // s.getMethodName() + " : " + s.getLineNumber());
                    rtn = s;
                    break;
                } else {
                    // System.out.println(ii + " - " + s.getClassName() + " : "
                    // + s.getMethodName()
                    // + " : " + s.getLineNumber());
                }
            }
            return rtn;
        }
    }
}