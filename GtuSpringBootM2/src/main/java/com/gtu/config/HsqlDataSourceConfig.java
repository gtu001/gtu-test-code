package com.gtu.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gtu.main.INVFAppliaction;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryHsql", transactionManagerRef = "transactionManagerHsql", basePackages = { "com.gtu.rest.hsql" })
public class HsqlDataSourceConfig {

    private static Logger logger = LoggerFactory.getLogger(INVFAppliaction.class);

    @Autowired
    @Qualifier("hsqlDataSource")
    private DataSource hsqlDataSource;

    @Bean(name = "entityManagerHsql")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryHsql(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryHsql")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryHsql(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(hsqlDataSource)//
                .properties(getVendorProperties(hsqlDataSource))//
                .packages("com.cathaybk.invf.rest.sqlserver.CTFL")//
                .persistenceUnit("primaryPersistenceUnit")//
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map getVendorProperties(DataSource dataSource) {
        Map config = jpaProperties.getHibernateProperties(dataSource);
        logger.info("### getVendorProperties = {}", config);
        return config;
    }

    @Primary
    @Bean(name = "transactionManagerHsql")
    public PlatformTransactionManager transactionManagerHsql(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryHsql(builder).getObject());
    }
}