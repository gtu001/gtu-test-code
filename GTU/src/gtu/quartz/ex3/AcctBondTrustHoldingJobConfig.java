package gtu.quartz.ex3;

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

import cub.robo.fee.batch.enums.DeciderEnum;

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

    @Autowired
    DefaultCtlLoaderTasklet defaultCtlLoaderTasklet;

    @Bean(name = "acctBondTrustHoldingJob")
    public Job acctBondTrustHoldingJob() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");//

        Flow flow = flowBuilder
            .from(defaultFileTriggerDecider)//
            .on(DeciderEnum.STOPPED.getValue())//
            .end()//
            .on(DeciderEnum.COMPLETED.getValue())//
            .to(acctBondTrustHoldingJobSgCtlLoaderStep())//
            .next(acctBondTrustHoldingInsertRenameStep())//
            .end();//

        return jobBuilderFactory.get("acctBondTrustHoldingJob")//
            .incrementer(new RunIdIncrementer())//
            .listener(defaultFileTriggerJobExecutionListener)//
            .start(flow)//
            .end()//
            .build();//
    }

    @Bean(name = "acctBondTrustHoldingJobSgCtlLoaderStep")
    public Step acctBondTrustHoldingJobSgCtlLoaderStep() {
        return stepBuilderFactory.get("acctBondTrustHoldingJobSgCtlLoaderStep")//
            .tasklet(defaultCtlLoaderTasklet)//
            .build();//
    }

    @Bean(name = "acctBondTrustHoldingInsertRenameStep")
    public Step acctBondTrustHoldingInsertRenameStep() {
        return stepBuilderFactory.get("acctBondTrustHoldingInsertRenameStep")//
            .tasklet(acctBondTrustHoldingInsertRenameTasklet)//
            .build();//
    }

}
