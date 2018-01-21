package gtu.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {

    public static final String PARAM_MSG = "param_msg";

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dm = context.getJobDetail().getJobDataMap();
        String msg = (String) dm.get(PARAM_MSG);
        System.out.println("-------------------- hello world " + (new Date()) + " : " + msg);
    }
}
