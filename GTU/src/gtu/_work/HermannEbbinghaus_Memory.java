package gtu._work;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;

public class HermannEbbinghaus_Memory {

    private static final ReviewTime INITIAL = ReviewTime.M1;

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

        ReviewTime(int min) {
            this.min = min;
        }

        private static ReviewTime getNext(String name) {
            ReviewTime v = ReviewTime.valueOf(name);
            if (v.ordinal() + 1 >= ReviewTime.NONE.ordinal()) {
                return ReviewTime.NONE;
            }
            return ReviewTime.values()[v.ordinal() + 1];
        }
    }

    private PropertiesUtilBean config;

    public HermannEbbinghaus_Memory(String configName) {
        File dir = PropertiesUtil.getJarCurrentPath(HermannEbbinghaus_Memory.class);
        config = new PropertiesUtilBean(new File(dir, configName));
        this.init();
    }

    public File getFile() {
        return config.getPropFile();
    }

    public void start() {
        startPause.set(true);
        for (MemData v : memLst) {
            this.schedule(v);
        }
    }

    public void stop() {
        startPause.set(false);
        for (Timer t : timerLst) {
            try{
                t.cancel();
            }catch(Exception ex){
            }
        }
        timerLst.clear();
    }

    private AtomicBoolean startPause = new AtomicBoolean(false);
    private List<MemData> memLst = new ArrayList<MemData>();
    private List<Timer> timerLst = new ArrayList<Timer>();
    private ActionListener memDo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent paramActionEvent) {
            System.out.println("TODO");
        }
    };

    private Timer newClock() {
        Timer t = new Timer();
        timerLst.add(t);
        return t;
    }

    private void init() {
        for (Object k : config.getConfigProp().keySet()) {
            String key = (String) k;
            String value = config.getConfigProp().getProperty(key);
            System.out.println("<<" + key + " \t " + value);
            value.split("^", -1);
            MemData d = new MemData(key, value);
            memLst.add(d);
        }
    }

    public void append(String key) {
        System.out.println("## 加入  " + key);

        MemData d = new MemData();
        d.key = key;
        d.registerTime = new Date();
        d.reviewTime = INITIAL.name();
        config.getConfigProp().setProperty(key, d.toValue());
        config.store();

        if (startPause.get()) {
            this.schedule(d);
        }
    }

    public void schedule(MemData d) {
        ReviewTime reviewTime = ReviewTime.valueOf(d.reviewTime);

        if (reviewTime == ReviewTime.NONE) {
            return;
        }

        long nextRuntime = (long) (reviewTime.min * 60 * 1000);
        long nextPeroid = this.getExecuteTime(d.registerTime, nextRuntime);
        System.out.println("## 排成  " + d.getKey() + " - " + d.reviewTime + " - " + nextPeroid);

        Timer timer = newClock();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(">> time up - " + d.getKey() + " , " + d.reviewTime + " , " + nextPeroid);
                // target = d
                // unknow = -1
                // command = ENUM
                // when = time
                // unknow = -1
                ActionEvent act = new ActionEvent(d, -1, d.reviewTime, nextPeroid, -1);
                memDo.actionPerformed(act);

                // 紀錄下次執行
                d.reviewTime = ReviewTime.getNext(d.reviewTime).name();
                storeMemData(d);
            }
        }, nextPeroid);
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
        String reviewTime;

        final String dateFormat = "yyyy-MM-dd_HH:mm:ss";
        final SimpleDateFormat SDF = new SimpleDateFormat(dateFormat);

        MemData() {
        }

        MemData(String key, String value) {
            try {
                String[] arry = StringUtils.trimToEmpty(value).split("\\^", -1);
                this.key = key;
                this.reviewTime = getArry(0, arry);
                this.registerTime = SDF.parse(getArry(1, arry));
            } catch (Exception e) {
                throw new RuntimeException("MemData ERR : " + e.getMessage(), e);
            }
        }

        String toValue() {
            return this.reviewTime + "^" + DateFormatUtils.format(this.registerTime, dateFormat);
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

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }

        public String getDateFormat() {
            return dateFormat;
        }
    }

    public void setMemDo(ActionListener memDo) {
        this.memDo = memDo;
    }
}
