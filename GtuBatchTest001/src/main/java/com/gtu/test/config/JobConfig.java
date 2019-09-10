package com.gtu.test.config;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class JobConfig extends DefaultBatchConfigurer {

    PlatformTransactionManager transactionManager;
    JobRepository jobRepository;
    JobLauncher jobLauncher;
    JobExplorer jobExplorer;

    @Override
    @Bean
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    @Override
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    @Bean
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    @Bean
    public SimpleJobLauncher getJobLauncher2() {
        return (SimpleJobLauncher) jobLauncher;
    }

    @Override
    @Bean
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }

    @PostConstruct
    public void initialize() {
        try {
            if (this.transactionManager == null) {
                this.transactionManager = new ResourcelessTransactionManager();
            }

            MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(this.transactionManager);
            jobRepositoryFactory.afterPropertiesSet();
            this.jobRepository = jobRepositoryFactory.getObject();

            MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(jobRepositoryFactory);
            jobExplorerFactory.afterPropertiesSet();
            this.jobExplorer = jobExplorerFactory.getObject();
            this.jobLauncher = createJobLauncher2();
        } catch (Exception ex) {
            throw new RuntimeException("initialize ERR : " + ex.getMessage(), ex);
        }
    }

    private JobLauncher createJobLauncher2() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}