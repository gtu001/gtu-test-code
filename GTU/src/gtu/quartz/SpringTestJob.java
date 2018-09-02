package gtu.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

public class SpringTestJob implements Job {

    public static void main(String[] args) {

    }

    @Override
    public void execute(JobExecutionContext jctx) throws JobExecutionException {
        JobDataMap dataMap = jctx.getJobDetail().getJobDataMap();
        String size = (String) dataMap.get("size");
        ApplicationContext ctx = (ApplicationContext) dataMap.get("applicationContext");
        System.out.println("size : " + size);
        dataMap.put("size", size + "0");
    }

}
