package gtu.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 實作Schedule Manager
 * 
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/3/17:下午 5:07:34
 */
public class ScheduleManagerImpl implements ScheduleManager {
	private static final long serialVersionUID = 1L;
	private Properties pros = new Properties();
	private String port = null;
	private String host = null;
	private String instanceName = null;
	private Scheduler scheduler = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmSS");

	public static void main(String[] args){
    	ScheduleManagerImpl manager = new ScheduleManagerImpl();
    	
    	manager.configure();
    	try {
			manager.initialize();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    	
    	// 決定要用那個trigger
    	Trigger trigger = TriggerUtils.makeImmediateTrigger("JOB_ID", 0, 10000);
    	
        // 準備Quartz object
        JobDetail jobDetail = manager.createJobDetail("JOB_ID", TestJob.class);
        jobDetail.setDescription("jobName");
        Map dataMap = new JobDataMap();
        dataMap.put("param_msg", "[[[[param_msg]]]]");
        jobDetail.setJobDataMap((JobDataMap) dataMap);

        // deploy job
        try {
			manager.setScheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	public Scheduler getScheduler() {
		return scheduler;
	}

	private JobDetail createJobDetail(String name, String group, Class jobClass) {
		JobDetail jobDetail = new JobDetail(name, group, jobClass);
		return jobDetail;
	}

	public JobDetail createJobDetail(String name, Class jobClass) {
		return this.createJobDetail(name, Scheduler.DEFAULT_GROUP, jobClass);
	}

	public CronTrigger createCronTrigger() {
		CronTrigger cronTrigger = new CronTrigger();
		cronTrigger.setName("cronTrigger-" + sdf.format(new Date()));
		cronTrigger.setStartTime(new Date());
		return cronTrigger;
	}

	public SimpleTrigger createSimpleTrigger() {
		SimpleTrigger st = new SimpleTrigger();
		st.setStartTime(new Date());
		st.setName("simpleTrigger-" + sdf.format(new Date()));
		return st;
	}

	public void deleteJob(String jobName) throws Exception {
		this.getRemoteScheduler(this.pros).deleteJob(jobName,
				Scheduler.DEFAULT_GROUP);
	}

	public void pasuseJob(String jobName) throws Exception {
		this.getRemoteScheduler(this.pros).pauseJob(jobName,
				Scheduler.DEFAULT_GROUP);
	}

	public void interruptJob(String jobName) throws Exception {
		this.getRemoteScheduler(this.pros).interrupt(jobName,
				Scheduler.DEFAULT_GROUP);
	}

	public void setScheduleJob(JobDetail jobDetail, org.quartz.Trigger trigger)
			throws Exception {
		this.scheduler = this.getRemoteScheduler(this.pros);
		this.scheduler.scheduleJob(jobDetail, trigger);
	}

	public void configure() {
		this.instanceName = "QuartzScheduler";
		this.host = "localhost";
		this.port = "1099";
		this.pros.setProperty("org.quartz.scheduler.rmi.registryHost", this.host);
		this.pros.setProperty("org.quartz.scheduler.rmi.registryPort", this.port);
		this.pros.setProperty("org.quartz.scheduler.rmi.proxy", "true");
		this.pros.setProperty("org.quartz.scheduler.instanceName", instanceName);
	}

	private Scheduler getRemoteScheduler(Properties pros) throws SchedulerException {
		try {
			this.scheduler.getCurrentlyExecutingJobs();
			return this.scheduler;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (new StdSchedulerFactory(this.pros)).getScheduler();
	}

	public void initialize() throws Exception {
		try {
			scheduler = this.getRemoteScheduler(pros);
		} catch (Exception e) {
			System.err.println("[ScheduleManagerImp.initialize] init error :" + e.getMessage());
		}
	}
}
