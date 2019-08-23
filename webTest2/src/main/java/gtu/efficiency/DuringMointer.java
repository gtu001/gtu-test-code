package gtu.efficiency;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DuringMointer {

    public static void main(String[] args) {
        DuringKey keyObj = During.createKey(new Object());

        DuringMointer d = new DuringMointer();
        During a1 = new During("迴圈一億次1");
        for (int ii = 0; ii < 100000000; ii++) {
        }
        a1.count(keyObj);

        During a2 = new During("迴圈一億次2");
        for (int ii = 0; ii < 100000000; ii++) {
        }
        a2.count(keyObj);

        for (int ii = 0; ii < 100; ii++) {
            During loopInner = new During("迴圈內部");
            loopInner.count(keyObj);
        }
        During.printAll(keyObj, true, 0);
        During.clear(keyObj);
        System.out.println("done...");
    }

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
        private static final Logger logger = LogManager.getLogger(During.class);

        long startTime;
        long duringTime;
        StackTraceElement startStack;
        StackTraceElement endStack;
        String resultMessage;
        String tag;
        boolean isLoop = false;// 重複使用
        Object comment;

        public During() {
            this("");
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
            if (StringUtils.isBlank(resultMessage)) {
                duringTime = System.currentTimeMillis() - startTime;
                endStack = getCallerStack();
                resultMessage = getFormatResult();
            }
            return resultMessage;
        }

        private static void appendTagDurning(During d, Map<String, List<Long>> duringMap) {
            String tag = d.getSummaryTag();
            if (StringUtils.isNotBlank(tag)) {
                List<Long> appLst = new ArrayList<Long>();
                if (duringMap.containsKey(tag)) {
                    appLst = duringMap.get(tag);
                }
                appLst.add(d.duringTime);
                duringMap.put(tag, appLst);
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
            // 檢查重複
            List<During> lst1 = new ArrayList<During>(getREPORT_MAP(keyObj));
            for (int ii = 0; ii < lst1.size(); ii++) {
                for (int jj = ii + 1; jj < lst1.size(); jj++) {
                    During d1 = lst1.get(ii);
                    During d2 = lst1.get(jj);
                    if (StringUtils.equals(d1.getSummaryTag(), d2.getSummaryTag())) {
                        d1.isLoop = true;
                        d2.isLoop = true;
                    }
                }
            }

            List<String> logLst = new ArrayList<String>();
            Map<String, List<Long>> duringMap = new TreeMap<String, List<Long>>();
            for (During d : getREPORT_MAP(keyObj)) {
                if (ignoreLoop && d.isLoop) {
                    // ignore
                } else if (overDurningTime > 0 && d.duringTime < overDurningTime) {
                    // ignore
                } else {
                    logLst.add(d.count(keyObj));
                }
                appendTagDurning(d, duringMap);
            }

            // 加總迴圈
            for (String tag : duringMap.keySet()) {
                List<Long> durLst = duringMap.get(tag);
                if (durLst.size() > 1) {
                    Long sumDuring = getSumDuring(durLst);
                    Long avgDuring = sumDuring / durLst.size();
                    Long meanDuring = getMeanDuring(durLst);
                    String msg = String.format(" 總計[%s] : %s / 次數 : %s / 平均 : %s / 中位數 : %s", tag, sumDuring,
                        durLst.size(),
                        avgDuring, meanDuring);
                    logLst.add(msg);
                }
            }

            logLst.add("report size : " + getREPORT_MAP(keyObj).size());
            for (String log : logLst) {
                logger.debug(log);
            }
            return StringUtils.join(logLst, "\n");
        }

        private static Long getSumDuring(List<Long> lst) {
            Long total = 0L;
            for (Long l : lst) {
                total += l;
            }
            return total;
        }

        private static Long getMeanDuring(List<Long> lst) {
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
}