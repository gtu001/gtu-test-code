package gtu.quartz.ex3;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import gtu.spring.mvc.common.WebConfigration.RestTemplateLoggingInterceptor;
import gtu.spring.mvc.ex1.SpringMvcTest001Application;

@Configuration
@ComponentScan("gtu.quartz.ex3")
@Component
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })

@EnableScheduling
public class QuartzBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcTest001Application.class, args);
    }

    @Configuration
    @EnableBatchProcessing
    public static class JobConfig {
        @Bean
        public MapJobRepositoryFactoryBean mapJobRepositoryFactory() throws Exception {
            MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(resourcelessTransactionManager());
            factory.afterPropertiesSet();
            return factory;
        }

        @Bean
        public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
            System.out.println("in jobRepository!!!!!");
            return factory.getObject();
        }

        @Bean
        public SimpleJobLauncher simpleJobLauncher(JobRepository jobRepository) {
            System.out.println("in simpleJobLauncher!!!!!");
            SimpleJobLauncher launcher = new SimpleJobLauncher();
            launcher.setJobRepository(jobRepository);
            return launcher;
        }

        public ResourcelessTransactionManager resourcelessTransactionManager() {
            return new ResourcelessTransactionManager();
        }
    }

    @Configuration
    public static class WebConfigration {
        @Bean
        public RestTemplate restTemplate() {
            RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
            restTemplate.getInterceptors().add(new RestTemplateLoggingInterceptor());
            return restTemplate;
        }
    }

    @Configuration
    public class DBConfig {
        @Bean
        @Profile({ "ut", "local" })
        public DataSource getUtPoolDataSource() throws Exception {
            return DataSourceBuilder.create()//
                    .driverClassName("org.apache.derby.jdbc.ClientDriver")//
                    .url("jdbc:derby://localhost:1527/seconddb;create=true")//
                    .username("test")//
                    .password("1234")//
                    .build();
        }
    }

}
