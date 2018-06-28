package gtu._work;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import gtu.binary.Base64JdkUtil;
import gtu.date.DateUtil;
import gtu.number.RandomUtil;
import gtu.properties.PropertiesUtilBean;

public class HermannEbbinghaus_Memory {

    public static void main(String[] args) {
        HermannEbbinghaus_Memory memory = new HermannEbbinghaus_Memory();
        // memory.init(new
        // File("‪e:/gtu001_dropbox/Dropbox/Apps/gtu001_test/etc_config/EnglishSearchUI_MemoryBank.properties"));
        memory.init(new File("d:/gtu001_dropbox/Dropbox/Apps/gtu001_test/etc_config/EnglishSearchUI_MemoryBank.properties"));
        memory.setMemDo(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                MemData mem = (MemData) arg0.getSource();
                mem.setWaitingTriggerTime(30 * 60 * 60);
            }
        });
        memory.start();
        for (String v : memory.getWaitingList()) {
            System.out.println(v);
        }
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
    private Thread checkConfigThread; // 檢查 config 是否有變更來自外力
    private AtomicReference<Range<Integer>> skipAll = new AtomicReference<Range<Integer>>();// 現在準備執行的全部取消
    private AtomicReference<Set<String>> queueSet = new AtomicReference<Set<String>>();// 顯示進入組列數

    public HermannEbbinghaus_Memory() {
    }

    public File getFile() {
        return config.getPropFile();
    }

    /**
     * 啟動
     */
    public void start() {
        this.start("start");
    }

    private void start(String label) {
        this.init(null);

        startPause.set(true);
        for (MemData v : memLst) {
            this.schedule(v);
        }
        initCheckConfigThread();

        // 啟動事件
        onOffDo.actionPerformed(new ActionEvent(this, -1, label));
    }

    private void initCheckConfigThread() {
        if (checkConfigThread == null || checkConfigThread.getState() == Thread.State.TERMINATED) {
            final long WAITTING_TIME = 30 * 1000;
            checkConfigThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (HermannEbbinghaus_Memory.this.startPause.get()) {
                        if (config.isFileChangeUncontrolled()) {
                            HermannEbbinghaus_Memory.this.start("設定檔異動重新起動!");
                        }

                        try {
                            Thread.sleep(WAITTING_TIME);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            });
            checkConfigThread.setDaemon(true);
            checkConfigThread.start();
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

        // 啟動事件
        onOffDo.actionPerformed(new ActionEvent(this, -1, "stop"));
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
    private ActionListener onOffDo = new ActionListener() {
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

    public void init(File customFile) {
        if (customFile != null) {
            config = new PropertiesUtilBean(customFile);
        }

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
            d.resetWaitingTriggerTime();
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

                // 放在 sync 裡沒鳥用
                QueueHandler queueHandler = new QueueHandler();
                if (queueHandler.contains_thanAdd(d.getKey())) {
                    return;
                }

                synchronized (HermannEbbinghaus_Memory.this) {
                    do {
                        if (d instanceof NotifyAllClz) {
                            HermannEbbinghaus_Memory.this.notifyAll();
                            return;
                        }

                        if (skipAll.get() != null) {
                            delayAll(d);
                        }

                    } while (skipAll.get() != null);

                    memDo.actionPerformed(act);
                }

                queueHandler.remove(d.getKey());

                // 紀錄下次執行
                if (!d.isCustomWaitingTrigger()) {
                    // 計算下次執行起算點
                    d.fixedTime = getCaculateFixedTime(d.reviewTime, d);

                    // 計算下期
                    d.reviewTime = ReviewTime.getNext(d.reviewTime).name();

                    // 儲存
                    if (containsKey(d.key)) {
                        storeMemData(d);
                    }
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
        for (MemData d : getAllMemData(true)) {
            ReviewTime reviewTime = ReviewTime.valueOf(d.reviewTime);
            if (reviewTime == ReviewTime.NONE) {
                continue;
            }

            long nextRuntime = (long) (reviewTime.min * 60 * 1000);
            long nextPeroid = this.getExecuteTime(d.fixedTime, nextRuntime);

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
            long nextPeroid = this.getExecuteTime(d.fixedTime, nextRuntime);
            String nextPeriodDesc = nextPeroid == 0 ? "馬上" : DateUtil.wasteTotalTime(nextPeroid);
            String detail = String.format("[%s] %s : %s -> %s", d.reviewTime, d.getKey(), nextPeriodDesc, d.getRemark());
            rtnLst.add(detail);
        }
        return rtnLst;
    }

    /**
     * 取得所有設定
     */
    public List<MemData> getAllMemData(boolean reload) {
        if (reload) {
            config.reload();
        }
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

        public void resetWaitingTriggerTime() {
            this.waitingTriggerTime = -1;
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

    public void setOnOffDo(ActionListener onOffDo) {
        this.onOffDo = onOffDo;
    }

    public boolean isFixedTimeUsing() {
        return fixedTimeUsing;
    }

    public void setFixedTimeUsing(boolean fixedTimeUsing) {
        this.fixedTimeUsing = fixedTimeUsing;
    }

    public void deleteKey(String key) {
        config.getConfigProp().remove(key);
        config.store();
    }

    public boolean containsKey(String key) {
        return config.getConfigProp().containsKey(key);
    }

    private void delayAll(MemData d) {
        Range<Integer> minRange = skipAll.get();
        if (minRange.getMinimum() == -1 && minRange.getMaximum() == -1) {
            System.out.println("@延遲 :" + d.getKey() + " ->  無限停止!!");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            int min = RandomUtil.rangeInteger(minRange.getMinimum(), minRange.getMaximum());
            long extendTime = min * 60 * 1000;
            System.out.println("@延遲 :" + d.getKey() + " -> " + min + "分鐘!!");
            try {
                this.wait(extendTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 即將觸發的往後延 (無限期)
     */
    public void skipRecent() {
        this.skipRecent(null);
    }

    /**
     * 即將觸發的往後延
     * 
     * @param minRange
     *            往後延後的分鐘數範圍
     */
    public void skipRecent(Range<Integer> minRange) {
        if (minRange == null) {
            minRange = Range.between(-1, -1);// 無限停止
        }
        skipAll.set(minRange);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1 * 60 * 1000);
                } catch (InterruptedException e) {
                }
                skipAll.set(null);
            }
        }).start();
    }

    // 純粹喚醒之用
    private class NotifyAllClz extends MemData {
        NotifyAllClz() {
            this.key = "";
            this.reviewTime = ReviewTime.getInitialReviewTime().name();
            this.registerTime = new Date();
            this.fixedTime = new Date();
            this.setWaitingTriggerTime(0);
        }
    }

    /**
     * 中斷恢復
     */
    public void resume() {
        skipAll.set(null);
        this.schedule(new NotifyAllClz());
    }

    /**
     * 取得測驗中的阻列
     */
    public Set<String> getQueue() {
        if (queueSet.get() == null) {
            queueSet.set(new LinkedHashSet<String>());
        }
        return queueSet.get();
    }

    private class QueueHandler {
        QueueHandler() {
            chkQueue();
        }

        public boolean contains_thanAdd(String key) {
            chkQueue();
            boolean contain = queueSet.get().contains(DATE_FORMAT);
            if (!contain) {
                queueSet.get().add(key);
            }
            return contain;
        }

        private void chkQueue() {
            if (queueSet.get() == null) {
                queueSet.set(new LinkedHashSet<String>());
            }
        }

        private void remove(String key) {
            chkQueue();
            if (queueSet.get().contains(key)) {
                queueSet.get().remove(key);
            }
        }
    }
}
