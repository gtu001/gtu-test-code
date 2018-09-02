package gtu.quartz;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;

/**
 * 定義Schedule Manager interface
 * Comment for ScheduleManager
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/3/17:下午 4:43:02
 */
public interface ScheduleManager {
	
    public final static String ROLE = "com.omniwise.owlet2.scheduling.ScheduleManager";

    public Scheduler getScheduler();

    public void setScheduleJob(JobDetail jobDetail, org.quartz.Trigger trigger) throws Exception;

    public void interruptJob(String jobName) throws Exception;

    public void pasuseJob(String jobName) throws Exception;

    public void deleteJob(String jobName) throws Exception;

    public SimpleTrigger createSimpleTrigger();

    public CronTrigger createCronTrigger();

    public JobDetail createJobDetail(String name, Class jobClass);
}
