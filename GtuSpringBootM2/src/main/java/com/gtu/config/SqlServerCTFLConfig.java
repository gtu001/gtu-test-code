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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "entityManagerFactorySqlServerCTFL", 
    transactionManagerRef = "transactionManagerSqlServerCTFL", 
    basePackages = {"com.gtu.rest.sqlserver" })
public class SqlServerCTFLConfig {
    
    @Autowired
    @Qualifier("sqlserverCTFLDataSource")
    private DataSource SqlServerCTFLDataSource;

    @Bean(name = "entityManagerCTFL")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactorySqlServerCTFL(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactorySqlServerCTFL")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySqlServerCTFL(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(SqlServerCTFLDataSource)
                .properties(getVendorProperties(SqlServerCTFLDataSource))
                .packages("com.cathaybk.invf.rest.sqlserver.CTFL")
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Primary
    @Bean(name = "transactionManagerSqlServerCTFL")
    public PlatformTransactionManager transactionManagerSqlServerCTFL(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactorySqlServerCTFL(builder).getObject());
    }
}