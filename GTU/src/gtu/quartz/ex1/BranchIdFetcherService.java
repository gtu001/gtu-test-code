package gtu.quartz.ex1;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchIdFetcherService {

    private Logger logger = LoggerFactory.getLogger(BranchIdFetcherService.class);

    @Autowired
    private Scheduler scheduler;

    public BranchIdFetcherService() {
    }

    @PostConstruct
    public void afterDo() {
        JobDetail jobDetail = buildJobDetail();
        // Trigger trigger = buildJobTrigger(jobDetail, ZonedDateTime.now());
        Trigger trigger = buildJobTrigger2(jobDetail);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("BranchIdFetcherService ERR : " + e.getMessage(), e);
        }
    }

    private JobDetail buildJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("subject", "getBranchID");

        return JobBuilder.newJob(BranchIdFetcherJob.class)
            .withIdentity(UUID.randomUUID().toString(), "email-jobs")
            .withDescription("Send Email Job")
            .usingJobData(jobDataMap)
            .storeDurably()
            .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName(), "getBranchID")
            .withDescription("getBranchID")
            .startAt(Date.from(startAt.toInstant()))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build();
    }

    private Trigger buildJobTrigger2(JobDetail jobDetail) {
        CronTrigger crontrigger = TriggerBuilder.newTrigger()
            .withIdentity(jobDetail.getKey().getName(), "getBranchID")
            .withDescription("getBranchID")
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 * * ?"))
            .build();
        return crontrigger;
    }
}
