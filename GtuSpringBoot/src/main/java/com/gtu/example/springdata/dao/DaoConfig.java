package com.gtu.example.springdata.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Profile({ "spring-data" })

@Configuration

@EnableTransactionManagement
// @EntityScan("com.gtu.example.model.entity")
// <repositories base-package="com.gtu.example.model.entity" />

@EnableJpaRepositories(// more details
        repositoryBaseClass = MyRepositoryImpl.class, // 可自定repository基礎類
        entityManagerFactoryRef = "serversEntityManager", //
        transactionManagerRef = "serversTransactionManager", //
        basePackages = { "com.gtu.example.springdata.dao" }//
) //

// @EnableMongoRepositories(basePackages = "com.gtu.example.springdata.entity")
// //
@Primary
public class DaoConfig {

    @Bean("serversDataSourceProperties")
    @Primary
    @ConfigurationProperties("datasource.servers")
    public DataSourceProperties serversDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("serversDataSource")
    @Primary
    @ConfigurationProperties("datasource.servers")
    public DataSource serversDataSource(//
            @Qualifier("serversDataSourceProperties") //
            DataSourceProperties serversDataSourceProperties) {
        return serversDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "serversEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean getServersEntityManager(//
            EntityManagerFactoryBuilder builder, //
            @Qualifier("serversDataSource") DataSource serversDataSource) {
        return builder.dataSource(serversDataSource)//
                .packages("com.gtu.example.springdata.entity")//
                .persistenceUnit("servers")//
                .properties(additionalJpaProperties())//
                .build();
    }

    Map<String, ?> additionalJpaProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", "create");
        // map.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        // map.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        map.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        map.put("hibernate.dialect", "org.hibernate.dialect.ProgressDialect");
        map.put("hibernate.show_sql", "true");
        return map;
    }

    @Bean(name = "serversTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("serversEntityManager") EntityManagerFactory serversEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(serversEntityManager);
        return transactionManager;
    }
}
