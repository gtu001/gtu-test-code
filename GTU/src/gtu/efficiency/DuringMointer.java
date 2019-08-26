package gtu.efficiency;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gtu.html.simple.HtmlSimpleTableCreater;

public class DuringMointer {

    public static void main(String[] args) {
        DuringKey keyObj = During.createKey(new Object());

        During.clear(keyObj);
        DuringMointer d = new DuringMointer();
        During a1 = new During("迴圈一億次1");
        for (int ii = 0; ii < 100000000; ii++) {
        }
        a1.count(keyObj, "test1");

        During a2 = new During("迴圈一億次2");
        for (int ii = 0; ii < 100000000; ii++) {
        }
        a2.count(keyObj, "test2");

        for (int ii = 0; ii < 100; ii++) {
            During loopInner = new During("迴圈內部");
            loopInner.count(keyObj, "test_loop");
        }

        During.printAll(keyObj, true, 0);
        During.printHtml("SAVE_FILE.html", keyObj, true, 0);
        System.out.println("done...");
    }

    private static final Logger logger = LogManager.getLogger(During.class);
    private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final SimpleDateFormat YYYYMMDD_SDF_TIME = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public static class DuringKey {
        private Object object;

        public DuringKey(Object object) {
            this.object = object;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((object == null) ? 0 : object.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DuringKey other = (DuringKey) obj;
            if (object == null) {
                if (other.object != null)
                    return false;
            } else if (!object.equals(other.object))
                return false;
            return true;
        }
    }

    private static ThreadLocal<Map<DuringKey, Set<During>>> REPORT_MAP = new ThreadLocal<Map<DuringKey, Set<During>>>();
    private static final DuringKey DEFAULT_KEY_OBJECT = During.createKey(new Object());

    private static Set<During> getREPORT_MAP(DuringKey keyObj) {
        if (REPORT_MAP.get() == null) {
            REPORT_MAP.set(new LinkedHashMap<DuringKey, Set<During>>());
        }
        if (!REPORT_MAP.get().containsKey(keyObj)) {
            REPORT_MAP.get().put(keyObj, new LinkedHashSet<During>());
        }
        return REPORT_MAP.get().get(keyObj);
    }

    private static void clear(DuringKey keyObj) {
        if (REPORT_MAP.get() == null) {
            return;
        }
        if (REPORT_MAP.get().containsKey(keyObj)) {
            REPORT_MAP.get().remove(keyObj);
        }
    }

    public static class During {
        long startTime;
        long endTime;
        long duringTime;
        StackTraceElement startStack;
        StackTraceElement endStack;
        String tag;
        boolean isLoop = false;// 重複使用
        Object comment;
        InnerInfo infoObj;

        public During() {
            this("");
        }

        public static void log(DuringKey keyObj, String comment) {
            During d = new During();
            d.count(keyObj, comment);
        }

        public static void log(String comment) {
            During d = new During();
            d.count(comment);
        }

        public During(String tag) {
            this.startTime = System.currentTimeMillis();
            this.tag = tag;
            startStack = getCallerStack();
        }

        public String count() {
            return count(DEFAULT_KEY_OBJECT, null);
        }

        public String count(String comment) {
            return count(DEFAULT_KEY_OBJECT, comment);
        }

        public String count(DuringKey keyObj) {
            return count(keyObj, null);
        }

        public String count(DuringKey keyObj, Object comment) {
            getREPORT_MAP(keyObj).add(this);
            this.setComment(comment);
            this.infoObj = new InnerInfo();
            return infoObj.formatResult;
        }

        private String[] toHtmlTds(DuringKey keyObj) {
            return new String[] { //
                    infoObj.tag, //
                    infoObj.classInfo, //
                    infoObj.startLineNumber, //
                    infoObj.endLineNumber, //
                    YYYYMMDD_SDF_TIME.format(startTime), //
                    YYYYMMDD_SDF_TIME.format(endTime), //
                    "" + this.duringTime, //
                    "" + infoObj.comment //
            };
        }

        private static void appendTagDurning(During d, Map<String, List<Long>> duringMap, Map<String, Object> duringCommentMap, Map<String, During> duringSingleMap) {
            String tag = d.infoObj.summaryTag;
            if (StringUtils.isNotBlank(tag)) {
                List<Long> appLst = new ArrayList<Long>();
                if (duringMap.containsKey(tag)) {
                    appLst = duringMap.get(tag);
                }
                appLst.add(d.duringTime);
                duringMap.put(tag, appLst);

                if (!duringCommentMap.containsKey(tag)) {
                    duringCommentMap.put(tag, d.comment);
                }

                if (!duringSingleMap.containsKey(appLst)) {
                    duringSingleMap.put(tag, d);
                }
            }
        }

        /**
         * @param ignoreLoop
         *            在迴圈內的during是否distinct
         * @param overDurningTime
         *            <= 0 忽略超時
         */
        public static String printAll(boolean ignoreLoop, long overDurningTime) {
            return printAll(DEFAULT_KEY_OBJECT, ignoreLoop, overDurningTime);
        }

        /**
         * @param ignoreLoop
         *            在迴圈內的during是否distinct
         * @param overDurningTime
         *            <= 0 忽略超時
         */
        public static String printAll(DuringKey keyObj, boolean ignoreLoop, long overDurningTime) {
            List<String> logLst = new ArrayList<String>();

            // 檢查重複
            List<During> lst1 = new ArrayList<During>(getREPORT_MAP(keyObj));
            Map<String, List<Integer>> chkSummaryMap = new HashMap<String, List<Integer>>();
            for (int ii = 0; ii < lst1.size(); ii++) {
                During d1 = lst1.get(ii);
                String summaryTag = d1.infoObj.summaryTag;
                List<Integer> keyLst = new ArrayList<Integer>();
                if (chkSummaryMap.containsKey(summaryTag)) {
                    keyLst = chkSummaryMap.get(summaryTag);
                }
                keyLst.add(ii);
                chkSummaryMap.put(summaryTag, keyLst);
            }
            for (List<Integer> indexLst : chkSummaryMap.values()) {
                boolean isLoop = indexLst.size() > 1;
                for (int idx : indexLst) {
                    lst1.get(idx).isLoop = isLoop;
                }
            }

            Map<String, List<Long>> duringMap = new TreeMap<String, List<Long>>();
            Map<String, Object> duringCommentMap = new TreeMap<String, Object>();
            Map<String, During> duringSingleMap = new HashMap<String, During>();
            for (During d : getREPORT_MAP(keyObj)) {
                if (ignoreLoop && d.isLoop) {
                    // ignore
                } else if (overDurningTime > 0 && d.duringTime < overDurningTime) {
                    // ignore
                } else {
                    logLst.add(d.count(keyObj));
                }
                appendTagDurning(d, duringMap, duringCommentMap, duringSingleMap);
            }

            // 加總迴圈
            for (String tag : duringMap.keySet()) {
                List<Long> durLst = duringMap.get(tag);
                if (durLst.size() > 1) {
                    String msg = String.format(" 迴圈總計[%s] / [耗時] 總計 ： %s / 次數 : %s / 平均 : %s / 最大值 : %s  / 最小值 : %s  / 中位數 : %s  %s", //
                            tag, //
                            MyUtil.getSum(durLst), //
                            durLst.size(), //
                            MyUtil.getAvg(durLst), //
                            MyUtil.getMax(durLst), //
                            MyUtil.getMin(durLst), //
                            MyUtil.getMean(durLst), //
                            getLoopDuringComment(tag, duringCommentMap, true)//
                    );//
                    logLst.add(msg);
                }
            }

            logLst.add("report size : " + getREPORT_MAP(keyObj).size());
            for (String log : logLst) {
                // logger.debug(log);//XXXXXXXXXXXXXXXXX
                System.out.println(log);
            }
            return StringUtils.join(logLst, "\n");
        }

        /**
         * @param ignoreLoop
         *            在迴圈內的during是否distinct
         * @param overDurningTime
         *            <= 0 忽略超時
         */
        public static File printHtml(String fileName, DuringKey keyObj, boolean ignoreLoop, long overDurningTime) {
            HtmlSimpleTableCreater html = new HtmlSimpleTableCreater();
            html.addTh("標籤", "類別資訊", "開始行數", "結束行數", "開始時間", "結束時間", "耗時", "備註");

            // 檢查重複
            List<During> lst1 = new ArrayList<During>(getREPORT_MAP(keyObj));
            Map<String, List<Integer>> chkSummaryMap = new HashMap<String, List<Integer>>();
            for (int ii = 0; ii < lst1.size(); ii++) {
                During d1 = lst1.get(ii);
                String summaryTag = d1.infoObj.summaryTag;
                List<Integer> keyLst = new ArrayList<Integer>();
                if (chkSummaryMap.containsKey(summaryTag)) {
                    keyLst = chkSummaryMap.get(summaryTag);
                }
                keyLst.add(ii);
                chkSummaryMap.put(summaryTag, keyLst);
            }
            for (List<Integer> indexLst : chkSummaryMap.values()) {
                boolean isLoop = indexLst.size() > 1;
                for (int idx : indexLst) {
                    lst1.get(idx).isLoop = isLoop;
                }
            }

            Map<String, List<Long>> duringMap = new TreeMap<String, List<Long>>();
            Map<String, Object> duringCommentMap = new TreeMap<String, Object>();
            Map<String, During> duringSingleMap = new HashMap<String, During>();
            for (During d : getREPORT_MAP(keyObj)) {
                if (ignoreLoop && d.isLoop) {
                    // ignore
                } else if (overDurningTime > 0 && d.duringTime < overDurningTime) {
                    // ignore
                } else {
                    html.addTd(d.toHtmlTds(keyObj));
                    html.newTr();
                }
                appendTagDurning(d, duringMap, duringCommentMap, duringSingleMap);
            }

            // 加總迴圈
            for (String tag : duringMap.keySet()) {
                List<Long> durLst = duringMap.get(tag);
                if (durLst.size() > 1) {
                    String msg = String.format("[耗時] 總計 ： %s / 次數 : %s / 平均 : %s / 最大值 : %s / 最小值 : %s / 中位數 : %s", //
                            MyUtil.getSum(durLst), //
                            durLst.size(), //
                            MyUtil.getAvg(durLst), //
                            MyUtil.getMax(durLst), //
                            MyUtil.getMin(durLst), //
                            MyUtil.getMean(durLst) //
                    );//
                    During tmpDuring = duringSingleMap.get(tag);
                    html.addTd(tag);
                    html.addTd(tmpDuring.infoObj.classInfo);
                    html.addTd(tmpDuring.infoObj.startLineNumber);
                    html.addTd(tmpDuring.infoObj.endLineNumber);
                    html.addTd(msg, 3);
                    html.addTd(getLoopDuringComment(tag, duringCommentMap, false));
                    html.newTr();
                }
            }

            html.addTd("report size : " + getREPORT_MAP(keyObj).size(), 100);
            html.newTr();

            File saveHtmlFile = html.createFile(fileName);
            return saveHtmlFile;
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

        private class InnerInfo {
            String tag;
            String classInfo;
            String startLineNumber;
            String endLineNumber;
            String comment;
            String formatResult;
            String summaryTag;

            InnerInfo() {
                this.tag = StringUtils.isNotBlank(During.this.tag) ? "[" + During.this.tag + "]" : "";
                this.classInfo = getClassInfo();
                this.startLineNumber = String.valueOf(getStartStack().getLineNumber());
                this.endLineNumber = String.valueOf(getEndStack().getLineNumber());
                this.comment = During.this.comment != null ? String.valueOf(During.this.comment) : "";

                During.this.endTime = System.currentTimeMillis();
                During.this.duringTime = endTime - startTime;
                During.this.endStack = getCallerStack();

                String comment = "";
                if (StringUtils.isNotBlank(this.comment)) {
                    comment = ", 備註 : " + this.comment;
                }
                formatResult = String.format("%s %s 行數[%s -> %s] 耗時 : %s [起迄 : %s - %s] %s", this.tag, classInfo, startLineNumber, endLineNumber, duringTime, getTime(startTime), getTime(endTime),
                        comment);
                summaryTag = String.format("%s %s 行數[%s -> %s]", this.tag, classInfo, startLineNumber, endLineNumber);
            }
        }

        private String getTime(long dateTime) {
            return SDF_TIME.format(new Date(dateTime));
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
                    return getSimpleClassName(getStartStack()) + "." + getStartStack().getMethodName() + "->" + getEndStack().getMethodName() + "()";
                }
            } else {
                return getSimpleClassName(getStartStack()) + "." + getStartStack().getMethodName() + "->" + getSimpleClassName(getEndStack()) + getEndStack().getMethodName() + "()";
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

        private static String getLoopDuringComment(String tag, Map<String, Object> duringCommentMap, boolean isAddPrefix) {
            String comment = "";
            if (duringCommentMap.containsKey(tag)) {
                Object val = duringCommentMap.get(tag);
                if (val != null) {
                    String prefix = "";
                    if (isAddPrefix) {
                        prefix = ", 備註 : ";
                    }
                    comment = prefix + String.valueOf(val);
                }
            }
            return comment;
        }

        public static DuringKey createKey(Object value) {
            return new DuringKey(value);
        }

        public static void clear(DuringKey keyObj) {
            DuringMointer.clear(keyObj);
        }

        public static void clear() {
            DuringMointer.clear(DEFAULT_KEY_OBJECT);
        }
    }

    private static class MyUtil {
        private static Long getMin(List<Long> lst) {
            Long minVal = Long.MAX_VALUE;
            for (Long l : lst) {
                minVal = Long.min(minVal, l);
            }
            return minVal;
        }

        private static Long getMax(List<Long> lst) {
            Long maxVal = Long.MIN_VALUE;
            for (Long l : lst) {
                maxVal = Long.max(maxVal, l);
            }
            return maxVal;
        }

        private static Long getSum(List<Long> lst) {
            Long total = 0L;
            for (Long l : lst) {
                total += l;
            }
            return total;
        }

        private static Long getAvg(List<Long> lst) {
            if (lst.size() != 0) {
                return getSum(lst) / lst.size();
            }
            return 0L;
        }

        private static Long getMean(List<Long> lst) {
            if (lst.isEmpty()) {
                return -1L;
            } else {
                Collections.sort(lst);
                if (lst.size() % 2 == 0) {
                    int pos = lst.size() / 2 - 1;
                    return (lst.get(pos) + lst.get(pos + 1)) / 2;
                } else {
                    int pos = lst.size() / 2 - 1;
                    return lst.get(pos);
                }
            }
        }
    }
}