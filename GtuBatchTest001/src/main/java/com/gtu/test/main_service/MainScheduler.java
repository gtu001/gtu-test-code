package com.gtu.test.main_service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MainScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MainScheduler.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Scheduled(cron = "${app.scheduler.corn.mainSchedule}")
    public void mainSchedule() {
        logger.info("Start scheduler mainSchedule.....");
        String jobBeanName = "acctBondTrustHoldingJob";
        this.doJobProcess(jobBeanName);
    }

    private void doJobProcess(String jobBeanName) {
        try {
            Job job = (Job) applicationContext.getBean(jobBeanName);
            jobLauncher.run(job, this.addJobParameters(jobBeanName));
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private JobParameters addJobParameters(String jobBeanName) {
        return new JobParametersBuilder()//
                .addString("jobBeanName", jobBeanName)//
                .toJobParameters();
    }
}
