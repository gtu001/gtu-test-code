package gtu.quartz;

import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

public class CronTriggerRunnerQuart {

    public static class SimpleJob implements Job {
        @Override
        public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
            System.out.println(((CronTriggerImpl) jobCtx.getTrigger()).getName() + " triggered. time is : " + new Date());
        }
    }

    
//    A、  範例:
//        "0/5 0 * * * ?" 每5秒執行一次
//        "0 0/1 * * * ?" 每分鐘執行一次
//        "0 0 8,12,16 * * ?" 每天8點、12點、16點執行一次
//        "0 0/30 9-18 * * ?" 工作時間內9~18點，每半小時執行一次
//        "0 30/1 * * * ?" 每小時的30分~59分，每分鐘執行一次
//        "0 0 12 * * ?" 每天中午12點執行一次
//        "0 * 14 * * ?" 每天下午14:00~14:59每1分鐘執行一次
//        "0 0/5 14 * * ?" 每天14:00~14:59每5分鐘執行一次
//        "0 0-5 14 * * ?" 每天14:00~14:05每分鐘執行一次
//        "0 0 12 ? * WED" 每個星期三中午12點執行一次
//        "0 15 10 15 * ?" 每月十五日10:15分執行一次
//        "0 15 10 * * ? 2005" 2005年的每天10:15執行一次
//        "0 15 10 L * ?" 每月最後一日的10:15執行一次
//        "0 15 10 ? * 6L" 每月的最後一個星期五上午10:15執行一次
//        "0 15 10 ? * 6L 2002-2005" 2002年~2005年每月的最後一個星期五上午10:15執行一次
//        "0 15 10 ? * 6#3" 每月的第三個星期五上午10:15
        
    public static void main(String[] args) {
        //        * 每一 Ex:每分鐘
        //        ? 只用於日期,星期, 其相當於佔位符
        //        - 表達一範圍 Ex:表示十點到十二點 -> 10-12
        //        , 表示依列表直 Ex:MON,WED,FRI
        //        / x/y表達衣等步增長序列,x起始,y增長直 Ex:0/15 表示0,15,30,45秒  另外 */y等同0/y
        //        L 表示Last,只用於日期,星期  若日期表示這個月最後一天,星期表示星期六等同7;若L錢袋數值X,則表示"這個月的最後X天"例如6L:表示該月最後一個星期五
        //        W 工作日最近的一天,15W離15號最近的工作日,若15日為星期六則為14,若為星期日則為16;注意的是部會跨月,若1W為星期六,則最近的為3號部會是上個月
        //        LW 最後一個工作天
        //        # 只用於星期,表達某月某個工作天;6#3表示第三周的星期五(6=星期五),4#5表第五周的星期三(若沒有這一天則忽略部觸發)
        //        C 只用於星期與日期,日其:5C:表示五日後的第一天,星期:1C:在星期字斷中相當於星期日後的第一天
        //        
        //        表達式 : 秒 分 時 幾號 月 星期 年(年通常省略)
        try {
            JobDetailImpl jobDetail = new JobDetailImpl("job1_1", "jgroup1", SimpleJob.class);
            CronTriggerImpl cronTrigger = new CronTriggerImpl("trigger1_2", "tgroup1");//定義組及名稱
            CronExpression cexp = new CronExpression("0/5 * * * * ?");
            cronTrigger.setCronExpression(cexp);

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
