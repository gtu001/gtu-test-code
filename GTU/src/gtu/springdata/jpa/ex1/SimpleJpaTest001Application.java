package gtu.springdata.jpa.ex1;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@Configuration
@ComponentScan("gtu.springdata.jpa.ex1")
@Component
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        // DataSourceAutoConfiguration.class,
        // DataSourceTransactionManagerAutoConfiguration.class, //
        // HibernateJpaAutoConfiguration.class
})
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        // DataSourceAutoConfiguration.class,
        // DataSourceTransactionManagerAutoConfiguration.class, //
        // HibernateJpaAutoConfiguration.class
})

@EnableTransactionManagement
@EnableJpaRepositories(//
        entityManagerFactoryRef = "entityManagerFactory", //
        transactionManagerRef = "transactionManager", //
        basePackages = { "gtu.springdata.jpa.ex1" })

public class SimpleJpaTest001Application {

    private static final Logger logger = LoggerFactory.getLogger(SimpleJpaTest001Application.class);

    // -------------------------------------------------------------------------------
    // ↓↓↓↓↓↓

    @Bean
    // @Profile({ "ut", "local" })
    public DataSource getDataSource() {
        return DataSourceBuilder//
                .create()//
                .driverClassName("org.apache.derby.jdbc.ClientDriver")//
                .url("jdbc:derby://localhost:1527/seconddb;create=true")//
                .username("test")//
                .password("1234")//
                .build();//
    }

    @Bean(name = "entityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> prop = new HashMap<String, Object>();
        // prop.put("hibernate.dialect",
        // env.getProperty("sqlserver.hibernate.dialect"));
        // prop.put("hibernate.show_sql",
        // env.getProperty("spring.jpa.show-sql"));
        return builder.dataSource(getDataSource())//
                .packages("gtu.springdata.jpa.ex1")//
                .persistenceUnit("secondaryPersistenceUnit")//
                .properties(prop)//
                .build();//
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }

    // -------------------------------------------------------------------------------
    // ↑↑↑↑↑↑

    @Component
    public static class EnvInfo {
        @Autowired
        private Environment environment;

        @PostConstruct
        public void postConstruct() {
            List<String> activeProfiels = Arrays.asList(environment.getActiveProfiles());
            logger.info("activeProfiels : " + activeProfiels);
        }
    }

    @Service
    public static class RunTest {

        @Autowired
        EmployeeRepository mEmployeeRepository;

        @PostConstruct
        public void postConstruct() {
            List<Employee> lst = mEmployeeRepository.qryCondition001("t1", "33");
            for (int ii = 0; ii < lst.size(); ii++) {
                logger.info(ii + " Employee = " + ReflectionToStringBuilder.toString(lst.get(ii)));
            }

            String maxEmployeeId = mEmployeeRepository.getMaxEmployeeId();
            logger.info("maxEmployeeId : " + maxEmployeeId);

            Optional<Employee> employee = mEmployeeRepository.findByEmployeeId(new BigDecimal(3));
            logger.info("FindById : " + ReflectionToStringBuilder.toString(employee.get()));

        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleJpaTest001Application.class, args);
    }
}
