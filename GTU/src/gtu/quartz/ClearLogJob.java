package gtu.quartz;
import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

public class ClearLogJob {

    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = schedFact.getScheduler();
        sched.start();
        JobDetail jobDetail = new JobDetail("myJob", null, LogJob.class);
        
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String filename = "D:\\11_AboveE_OW\\defaultroot\\WEB-INF\\logs\\";
        jobDataMap.put("LOG", filename);
        
//        Trigger trigger = TriggerUtils.makeHourlyTrigger(); // fire every hour
        Trigger trigger = TriggerUtils.makeMinutelyTrigger(5); //每五分鐘清一次log
        
//        Date evenHourDate = TriggerUtils.getEvenHourDate(new Date());
        Date evenHourDate = new Date();
        
        System.out.println(evenHourDate);
        
        trigger.setStartTime(evenHourDate); // start on the next even hour
        trigger.setName("myTrigger");

        sched.scheduleJob(jobDetail, trigger);
    }
    
    /**
     * 建立一個 trigger : 從現在開始 每十秒 執行一次 四十秒後結束
     * @return
     */
    private Trigger testTrigger() {
        String name = "myTrigger";
        String group = "myGroup";
        Date startDate = new Date();
        Date endDate = new Date( System.currentTimeMillis() + 40000L);
        int repeat = SimpleTrigger.REPEAT_INDEFINITELY;
        long fireBetween = 10L * 1000L;
        SimpleTrigger trigger = new SimpleTrigger(name, group, startDate, endDate, repeat, fireBetween);
        return trigger;
    }
    
    /**
     * 清除log
     * @author Administrator
     */
    public static class LogJob implements Job {
        
        private Log log = LogFactory.getLog(LogJob.class);
        
        public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
            log.debug("start------------------------------------------------------------------");
            JobDetail jobDetail = paramJobExecutionContext.getJobDetail();
            String filename = jobDetail.getJobDataMap().getString("LOG");
            File file = new File(filename);
            File[] fs = file.listFiles();
            for(File f : fs) {
                boolean delok = f.delete();
                if(delok) {
                    log.debug("del ok : " + f.getName());
                }else {
                    log.debug("file on use : " + f.getName());
                }
            }
            log.debug("end  ------------------------------------------------------------------");
        }
    }
    
    public static class MyJob implements Job {
        public void execute(JobExecutionContext context) throws JobExecutionException {
//            Calendar cal = context.getCalendar();
//            Date fireTime = context.getFireTime();
//            JobDetail jobDetail = context.getJobDetail();
//            Job job = context.getJobInstance();
//            long jobRunTime = context.getJobRunTime();
//            JobDataMap jobDataMap = context.getMergedJobDataMap();
//            Date nextFireTime = context.getNextFireTime();
//            Date previousFireTime = context.getPreviousFireTime();
//            int refireCount = context.getRefireCount();
//            Object result = context.getResult();
//            Date scheduledFireTime = context.getScheduledFireTime();
//            Scheduler scheduler = context.getScheduler();
//            Trigger trigger = context.getTrigger();
//            
//            System.out.println("start ======================================================================");
//            System.out.println("cal:"+cal);
//            System.out.println("fireTime:"+fireTime);
//            System.out.println("jobDetail:"+jobDetail);
//            System.out.println("job:"+job);
//            System.out.println("jobRunTime:"+jobRunTime);
//            System.out.println("jobDataMap:"+jobDataMap);
//            System.out.println("nextFireTime:"+nextFireTime);
//            System.out.println("previousFireTime:"+previousFireTime);
//            System.out.println("refireCount:"+refireCount);
//            System.out.println("result:"+result);
//            System.out.println("scheduledFireTime:"+scheduledFireTime);
//            System.out.println("scheduler:"+scheduler);
//            System.out.println("trigger:"+trigger);
//            
//            String instName = context.getJobDetail().getName();
//            String instGroup = context.getJobDetail().getGroup();
//
//            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//            Map map = dataMap.getWrappedMap();
//            
//            System.out.println("dataMap:"+dataMap);
//            System.out.println("instName:"+instName);
//            System.out.println("instGroup:"+instGroup);
//            System.out.println("WrappedMap:"+map);
//            System.out.println("end   ======================================================================");
        }
    }
}








