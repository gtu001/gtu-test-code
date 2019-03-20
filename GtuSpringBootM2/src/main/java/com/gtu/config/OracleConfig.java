package com.gtu.config;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//    entityManagerFactoryRef = "entityManagerFactoryOracle", 
//    transactionManagerRef = "transactionManagerOracle", 
//    basePackages = {"com.gtu.rest.oracle" }) 
//public class OracleConfig {
//
//    @Autowired
//    @Qualifier("oracleDataSource")
//    private DataSource oracleDataSource;
//
//    @Bean(name = "entityManagerOracle")
//    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
//        return entityManagerFactoryOracle(builder).getObject().createEntityManager();
//    }
//
//    @Bean(name = "entityManagerFactoryOracle")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryOracle(EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(oracleDataSource)
//                .properties(getVendorProperties(oracleDataSource))
//                .packages("com.gtu.rest.oracle") 
//                .persistenceUnit("secondaryPersistenceUnit")
//                .build();
//    }
//
//    @Autowired
//    private JpaProperties jpaProperties;
//
//    private Map getVendorProperties(DataSource dataSource) {
//        return jpaProperties.getHibernateProperties(dataSource);
//    }
//
//    @Bean(name = "transactionManagerOracle")
//    PlatformTransactionManager transactionManagerOracle(EntityManagerFactoryBuilder builder) {
//        return new JpaTransactionManager(entityManagerFactoryOracle(builder).getObject());
//    }
//}