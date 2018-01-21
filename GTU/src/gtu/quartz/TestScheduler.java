package gtu.quartz;

import java.text.ParseException;
import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class TestScheduler {

    /**
     * @param args
     * @throws SchedulerException
     * @throws ParseException
     */
    public static void main(String[] args) throws SchedulerException, ParseException {
        Properties pros = new Properties();

        pros.setProperty("org.quartz.scheduler.rmi.registryHost", "localhost");
        pros.setProperty("org.quartz.scheduler.rmi.registryPort", "1099");
        pros.setProperty("org.quartz.scheduler.rmi.proxy", "true");
        pros.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");

        Scheduler scheduler = (new StdSchedulerFactory(pros)).getScheduler();
        System.out.println(scheduler.getCurrentlyExecutingJobs().size());
        JobDetail jobDetail = JobBuilder.newJob().ofType(TestJob.class).storeDurably(true).withIdentity("test-chris1234", "job_group").build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger_name", "job_group").withSchedule(CronScheduleBuilder.cronSchedule("0 1 * * * ?")).build();

        jobDetail.getJobDataMap().put("param_msg", " test schedule-manager =>  chris&&&&&&");
        scheduler.scheduleJob(jobDetail, trigger);
    }
}
