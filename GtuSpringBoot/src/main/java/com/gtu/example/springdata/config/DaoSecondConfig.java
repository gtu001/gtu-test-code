package com.gtu.example.springdata.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Profile({ "spring-data", "domains" })

// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources
// https://github.com/tonym2105/samples/tree/master/boot-multidb-sample
//
@Configuration // 若同時需要兩個DB作法<註解此行就disable>
@EntityScan("com.gtu.example.model.entity")

@EnableJpaRepositories(//
        entityManagerFactoryRef = "domainsEntityManager", //
        transactionManagerRef = "domainsTransactionManager", //
        basePackages = { "com.gtu.example.springdata.dao_1" }//
) //

@EnableJpaAuditing
@Primary
public class DaoSecondConfig {

    @Bean(name = "domainsEntityManager")
    public LocalContainerEntityManagerFactoryBean getDomainsEntityManager(//
            EntityManagerFactoryBuilder builder, //
            @Qualifier("domainsDataSource") DataSource domainsDataSource) {
        return builder.dataSource(domainsDataSource)//
                .packages("com.gtu.example.springdata.entity")//
                .persistenceUnit("domains")//
                .properties(additionalJpaProperties())//
                .build();
    }

    Map<String, ?> additionalJpaProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("hibernate.hbm2ddl.auto", "create");
        // map.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        // map.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        map.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        map.put("hibernate.show_sql", "true");
        return map;
    }

    @Primary
    @Bean("domainsDataSourceProperties")
    @ConfigurationProperties("datasource.domains")
    public DataSourceProperties domainsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("domainsDataSource")
    public DataSource domainsDataSource(//
            @Qualifier("domainsDataSourceProperties") //
            DataSourceProperties domainsDataSourceProperties) {
        return domainsDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "domainsTransactionManager")
    public JpaTransactionManager transactionManager(//
            @Qualifier("domainsEntityManager") //
            EntityManagerFactory domainsEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(domainsEntityManager);
        return transactionManager;
    }
}