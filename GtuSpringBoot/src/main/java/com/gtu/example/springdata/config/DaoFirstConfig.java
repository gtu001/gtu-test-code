package com.gtu.example.springdata.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Profile({ "spring-data", "servers" })

// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources
// https://github.com/tonym2105/samples/tree/master/boot-multidb-sample
//
//@Configuration // 若同時需要兩個DB作法<註解此行就disable>
//@EntityScan("com.gtu.example.model.entity")

//@EnableJpaRepositories(//
//        entityManagerFactoryRef = "serversEntityManager", //
//        transactionManagerRef = "serversTransactionManager", //
//        basePackages = { "com.gtu.example.springdata.dao_1" }//
//) //

// @EnableMongoRepositories
//@EnableJpaAuditing
//@Primary
public class DaoFirstConfig {

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
        // map.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        map.put("hibernate.dialect", "org.hibernate.dialect.ProgressDialect");
        map.put("hibernate.show_sql", "true");
        return map;
    }

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

    @Bean(name = "serversTransactionManager")
    public JpaTransactionManager transactionManager(//
            @Qualifier("serversEntityManager") //
            EntityManagerFactory serversEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(serversEntityManager);
        return transactionManager;
    }
}
