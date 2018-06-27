package gtu._work;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.binary.Base64JdkUtil;
import gtu.date.DateUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;

public class HermannEbbinghaus_Memory {

    public static void main(String[] args) {
        String v = Base64JdkUtil.encode("gogo測試");
        System.out.println(v);
        System.out.println(Base64JdkUtil.decode(v));
        System.out.println("done...");
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss";
    private static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);

    private enum ReviewTime {
        M1(5), // 1．第一個記憶週期：5分鐘
        M2(30), // 2．第二個記憶週期：30分鐘
        M3(12 * 60), // 3．第三個記憶週期：12小時
        M4(1 * 24 * 60), // 4．第四個記憶週期：1天
        M5(2 * 24 * 60), // 5．第五個記憶週期：2天
        M6(4 * 24 * 60), // 6．第六個記憶週期：4天
        M7(7 * 24 * 60), // 7．第七個記憶週期：7天
        M8(15 * 24 * 60), // 8．第八個記憶週期：15天
        NONE(-1),//
        ;
        final float min;

        ReviewTime(float min) {
            this.min = min;
        }

        private static ReviewTime getNext(String name) {
            ReviewTime v = ReviewTime.valueOf(name);
            if (v.ordinal() + 1 >= ReviewTime.NONE.ordinal()) {
                return ReviewTime.NONE;
            }
            return ReviewTime.values()[v.ordinal() + 1];
        }

        private static ReviewTime getInitialReviewTime() {
            return ReviewTime.values()[0];
        }
    }

    private PropertiesUtilBean config;
    private boolean fixedTimeUsing = true;// 判斷推算下次時間用登入起算[false],還是每次複習時修正下次時間[true]

    public HermannEbbinghaus_Memory(File dir, String configName) {
        if (dir == null) {
            dir = PropertiesUtil.getJarCurrentPath(HermannEbbinghaus_Memory.class);
        }
        config = new PropertiesUtilBean(new File(dir, configName));
    }

    public HermannEbbinghaus_Memory(String configName) {
        this(null, configName);
    }

    public File getFile() {
        return config.getPropFile();
    }

    /**
     * 啟動
     */
    public void start() {
        this.init();
        startPause.set(true);
        for (MemData v : memLst) {
            this.schedule(v);
        }
    }

    /**
     * 關閉
     */
    public void stop() {
        startPause.set(false);
        for (Timer t : timerMap.values()) {
            try {
                t.cancel();
            } catch (Exception ex) {
            }
        }
        timerMap.clear();
    }

    private AtomicBoolean startPause = new AtomicBoolean(false);
    private List<MemData> memLst = new ArrayList<MemData>();
    private Map<String, Timer> timerMap = new HashMap<String, Timer>();

    private ActionListener memDo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent paramActionEvent) {
            System.out.println("TODO");
        }
    };

    private Timer newClock(String key) {
        Timer t = new Timer();
        if (timerMap.containsKey(key)) {
            timerMap.get(key).cancel();
        }
        timerMap.put(key, t);
        return t;
    }

    private void init() {
        memLst = new ArrayList<MemData>();
        timerMap = new HashMap<String, Timer>();
        startPause = new AtomicBoolean(false);

        for (Object k : config.getConfigProp().keySet()) {
            String key = (String) k;
            String value = config.getConfigProp().getProperty(key);
            System.out.println("<<" + key + " \t " + value);
            value.split("^", -1);
            MemData d = new MemData(key, value);
            memLst.add(d);
        }
    }

    /**
     * 加入新項目
     * 
     * @param key
     */
    public void append(String key) {
        this.append(key, "");
    }

    /**
     * 加入新項目
     * 
     * @param key
     * @param remark
     */
    public void append(String key, String remark) {
        System.out.println("## 加入  " + key);

        Date newDate = new Date();
        MemData d = new MemData();
        d.key = key;
        d.registerTime = newDate;
        d.fixedTime = newDate;
        d.setRemark(remark);
        d.resetReviewTime();
        this.memLst.add(d);

        config.getConfigProp().setProperty(key, d.toValue());
        config.store();

        if (startPause.get()) {
            this.schedule(d);
        }
    }

    /**
     * 排成記憶內容
     */
    private void schedule(final MemData d) {
        ReviewTime reviewTime = ReviewTime.valueOf(d.reviewTime);

        if (reviewTime == ReviewTime.NONE) {
            return;
        }

        final AtomicLong nextPeroid = new AtomicLong(-1);
        if (d.isCustomWaitingTrigger()) {
            nextPeroid.set(d.getWaitingTriggerTime());
            d.setWaitingTriggerTime(-1);
        } else {
            long nextRuntime = (long) (reviewTime.min * 60 * 1000);
            nextPeroid.set(this.getExecuteTime(d.fixedTime, nextRuntime));
        }

        System.out.println("## 排成  " + d.getKey() + " - " + d.reviewTime + " - " + nextPeroid + " - " + DateUtil.wasteTotalTime(nextPeroid.get()));

        Timer timer = newClock(d.getKey());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(">> time up - " + d.getKey() + " , " + d.reviewTime + " , " + nextPeroid);

                // target = d , command = ENUM , when = time ,
                ActionEvent act = new ActionEvent(d, -1, d.reviewTime, nextPeroid.get(), -1);

                synchronized (HermannEbbinghaus_Memory.this) {
                    memDo.actionPerformed(act);
                }

                // 紀錄下次執行
                if (!d.isCustomWaitingTrigger()) {
                    // 計算下次執行起算點
                    d.fixedTime = getCaculateFixedTime(d.reviewTime, d);

                    // 計算下期
                    d.reviewTime = ReviewTime.getNext(d.reviewTime).name();

                    // 儲存
                    storeMemData(d);
                }

                // 準備執行下次
                schedule(d);
            }
        }, nextPeroid.get());
    }

    private Date getCaculateFixedTime(String thisReviewTime, MemData d) {
        if (fixedTimeUsing) {
            ReviewTime thisTime = ReviewTime.valueOf(thisReviewTime);
            long fixedTime = System.currentTimeMillis() - (long) (thisTime.min * 60 * 1000);
            return new Date(fixedTime);
        } else {
            return d.registerTime;
        }
    }

    /**
     * 取得等待清單
     */
    public List<String> getWaitingList() {
        TreeMap<Long, List<MemData>> map = new TreeMap<Long, List<MemData>>();
        for (MemData d : getAllMemData()) {
            ReviewTime reviewTime = ReviewTime.valueOf(d.reviewTime);
            if (reviewTime == ReviewTime.NONE) {
                continue;
            }

            long nextRuntime = (long) (reviewTime.min * 60 * 1000);
            long nextPeroid = this.getExecuteTime(d.registerTime, nextRuntime);

            List<MemData> lst = new ArrayList<MemData>();
            if (map.containsKey(nextPeroid)) {
                lst = map.get(nextPeroid);
            }
            lst.add(d);
            map.put(nextPeroid, lst);
        }

        List<MemData> allLst = new ArrayList<MemData>();
        for (Long key : map.keySet()) {
            List<MemData> lst2 = map.get(key);
            allLst.addAll(lst2);
        }

        List<String> rtnLst = new ArrayList<String>();
        for (MemData d : allLst) {
            ReviewTime reviewTime = ReviewTime.valueOf(d.reviewTime);
            long nextRuntime = (long) (reviewTime.min * 60 * 1000);
            long nextPeroid = this.getExecuteTime(d.registerTime, nextRuntime);
            String detail = String.format("[%s] %s : %s -> %s", d.reviewTime, d.getKey(), DateUtil.wasteTotalTime(nextPeroid), d.getRemark());
            rtnLst.add(detail);
        }
        return rtnLst;
    }

    /**
     * 取得所有設定
     */
    public List<MemData> getAllMemData() {
        List<MemData> lst = new ArrayList<MemData>();
        for (Enumeration<?> enu = this.config.getConfigProp().keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = this.config.getConfigProp().getProperty(key);
            MemData d = new MemData(key, value);
            lst.add(d);
        }
        return lst;
    }

    /**
     * 複寫原來設定
     */
    public void overwrite(List<MemData> lst) {
        for (MemData v : lst) {
            System.out.println("overwrite : " + v.getKey() + "\t" + v.toValue());
            config.getConfigProp().setProperty(v.getKey(), v.toValue());
        }
        config.store();
    }

    private void storeMemData(MemData d) {
        config.getConfigProp().setProperty(d.key, d.toValue());
        config.store();
    }

    private long getExecuteTime(Date startTime, long period) {
        long val = startTime.getTime() + period - System.currentTimeMillis();
        if (val < 0) {
            val = 0;
        }
        return val;
    }

    public static class MemData {
        String key;
        Date registerTime;
        Date fixedTime;
        String reviewTime;
        String remark;
        long waitingTriggerTime = -1;// 設定值於此 則可自訂trigger時間

        public MemData() {
        }

        public MemData(String key, String value) {
            try {
                String[] arry = StringUtils.trimToEmpty(value).split("\\^", -1);
                this.key = key;
                this.reviewTime = getArry(0, arry);
                this.registerTime = __getDateFromString(getArry(1, arry));
                this.fixedTime = __getDateFromString(getArry(2, arry));
                this.remark = getArry(3, arry);
            } catch (Exception e) {
                throw new RuntimeException("MemData ERR : " + e.getMessage(), e);
            }
        }

        private Date __getDateFromString(String dateStr) {
            try {
                return SDF.parse(dateStr);
            } catch (Exception ex) {
                return new Date();
            }
        }

        String toValue() {
            return this.reviewTime + "^" + DateFormatUtils.format(this.registerTime, DATE_FORMAT) + "^" + DateFormatUtils.format(this.fixedTime, DATE_FORMAT) + "^" + this.remark;
        }

        private String getArry(int index, String[] arry) {
            if (arry.length > index) {
                return arry[index];
            }
            return "";
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Date getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(Date registerTime) {
            this.registerTime = registerTime;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public void resetReviewTime() {
            this.reviewTime = ReviewTime.getInitialReviewTime().name();
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }

        public String getDateFormat() {
            return DATE_FORMAT;
        }

        public String getRemark() {
            try {
                return Base64JdkUtil.decode(remark);
            } catch (Exception ex) {
                System.out.println("remark => " + remark);
                ex.printStackTrace();
                return "__ERROR__";
            }
        }

        public void setRemark(String remark) {
            this.remark = Base64JdkUtil.encode(remark);
        }

        public long getWaitingTriggerTime() {
            return waitingTriggerTime;
        }

        public void setWaitingTriggerTime(long waitingTriggerTime) {
            this.waitingTriggerTime = waitingTriggerTime;
        }

        public boolean isCustomWaitingTrigger() {
            return this.waitingTriggerTime >= 0;
        }

        public Date getFixedTime() {
            return fixedTime;
        }

        public void setFixedTime(Date fixedTime) {
            this.fixedTime = fixedTime;
        }
    }

    public void setMemDo(ActionListener memDo) {
        this.memDo = memDo;
    }

    public boolean isFixedTimeUsing() {
        return fixedTimeUsing;
    }

    public void setFixedTimeUsing(boolean fixedTimeUsing) {
        this.fixedTimeUsing = fixedTimeUsing;
    }
}
