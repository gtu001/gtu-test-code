package gtu.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

public class SimpleTriggerRunner {

    public static class SimpleJob implements Job {
        @Override
        public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
            System.out.println(((SimpleTriggerImpl) jobCtx.getTrigger()).getName() + " triggered. time is : " + new Date());
        }
    }

    public static void main(String[] args) {
        try {
            JobDetailImpl jobDetail = new JobDetailImpl("job1_1", "jgroup1", SimpleJob.class);
            SimpleTriggerImpl simpleTrigger = new SimpleTriggerImpl();
            simpleTrigger.setStartTime(new Date());//即刻開使
            simpleTrigger.setRepeatInterval(2000);//每兩秒一次
            simpleTrigger.setRepeatCount(100);//共100次
            simpleTrigger.setName("test");

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
            scheduler.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
