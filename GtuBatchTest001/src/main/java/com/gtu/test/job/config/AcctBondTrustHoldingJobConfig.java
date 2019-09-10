package com.gtu.test.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gtu.test.job.decider.DefaultFileTriggerDecider;
import com.gtu.test.job.listener.DefaultFileTriggerJobExecutionListener;
import com.gtu.test.job.task.AcctBondTrustHoldingInsertRenameTasklet;

@Configuration
public class AcctBondTrustHoldingJobConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier(value = "defaultFileTriggerDecider")
    DefaultFileTriggerDecider defaultFileTriggerDecider;

    @Autowired
    AcctBondTrustHoldingInsertRenameTasklet acctBondTrustHoldingInsertRenameTasklet;

    @Autowired
    DefaultFileTriggerJobExecutionListener defaultFileTriggerJobExecutionListener;

    @Bean(name = "acctBondTrustHoldingJob")
    public Job acctBondTrustHoldingJob() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");//
        Flow flow = flowBuilder.from(defaultFileTriggerDecider)//
                .on("STOP")//
                .end()//
                .on("COMPLETE")//
                .to(acctBondTrustHoldingInsertRenameTasklet_step1())//
                .next(acctBondTrustHoldingInsertRenameTasklet_step2())//
                .end();//

        return jobBuilderFactory.get("acctBondTrustHoldingJob")//
                .incrementer(new RunIdIncrementer())//
                .listener(defaultFileTriggerJobExecutionListener)//
                .start(flow)//
                .end()//
                .build();//
    }

    @Bean(name = "acctBondTrustHoldingInsertRenameTasklet_step1")
    public Step acctBondTrustHoldingInsertRenameTasklet_step1() {
        return stepBuilderFactory.get("acctBondTrustHoldingInsertRenameTasklet_step1")//
                .tasklet(acctBondTrustHoldingInsertRenameTasklet)//
                .build();//
    }

    @Bean(name = "acctBondTrustHoldingInsertRenameTasklet_step2")
    public Step acctBondTrustHoldingInsertRenameTasklet_step2() {
        return stepBuilderFactory.get("acctBondTrustHoldingInsertRenameTasklet_step2")//
                .tasklet(acctBondTrustHoldingInsertRenameTasklet)//
                .build();//
    }
}
