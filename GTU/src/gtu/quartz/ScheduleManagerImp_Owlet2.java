package gtu.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;

import com.omniwise.owlet2.core.component.Component;

/**
 * 實作Schedule Manager
 * 
 * 
 * @author <a href="mailto:chris@mail.omniwise.com.tw">Chris</a>
 * @version 2008/3/17:下午 5:07:34
 */
public class ScheduleManagerImp_Owlet2 extends AbstractLogEnabled implements Component, ScheduleManager, Composable, Configurable, Initializable, ThreadSafe {
    private static final long serialVersionUID = 1L;
    private Properties pros = new Properties();
    private String port = null;
    private String host = null;
    private String instanceName = null;
    private Scheduler scheduler = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmSS");

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
        this.getRemoteScheduler(this.pros).deleteJob(jobName, Scheduler.DEFAULT_GROUP);
    }

    public void pasuseJob(String jobName) throws Exception {
        this.getRemoteScheduler(this.pros).pauseJob(jobName, Scheduler.DEFAULT_GROUP);
    }

    public void interruptJob(String jobName) throws Exception {
        this.getRemoteScheduler(this.pros).interrupt(jobName, Scheduler.DEFAULT_GROUP);
    }

    public void setScheduleJob(JobDetail jobDetail, org.quartz.Trigger trigger) throws Exception {
        this.scheduler = this.getRemoteScheduler(this.pros);
        this.scheduler.scheduleJob(jobDetail, trigger);
    }

    public void compose(ComponentManager cMgr) throws ComponentException {

    }

    public void configure(Configuration config) throws ConfigurationException {
        this.instanceName = config.getAttribute("instanceName", "QuartzScheduler");
        this.host = config.getAttribute("host", "localhost");
        this.port = config.getAttribute("port", "1099");
        this.pros.setProperty("org.quartz.scheduler.rmi.registryHost", this.host);
        this.pros.setProperty("org.quartz.scheduler.rmi.registryPort", this.port);
        this.pros.setProperty("org.quartz.scheduler.rmi.proxy", "true");
        this.pros.setProperty("org.quartz.scheduler.instanceName", instanceName);

    }

    
    private Scheduler getRemoteScheduler(Properties pros) throws SchedulerException { 
        try {
            this.scheduler.getCurrentlyExecutingJobs();
            return this.scheduler;
        }catch(Exception e ) {  
        }
        return (new StdSchedulerFactory(this.pros)).getScheduler();
    }

    public void initialize() throws Exception {
        try {
            scheduler = this.getRemoteScheduler(pros);
        } catch (Exception e) {
            this.getLogger().error("[ScheduleManagerImp.initialize] init error :" + e.getMessage());
        }
    }

}
