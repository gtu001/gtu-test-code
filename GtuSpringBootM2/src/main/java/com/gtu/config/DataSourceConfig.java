package com.gtu.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {

    private static Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean(name = "hsqlDataSource")
    @Qualifier("hsqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hsqldb")
    public DataSource hsqlDataSource() {
        DataSource ds = DataSourceBuilder.create().build();
        logger.info("dataSource = " + ds);
        return ds;
    }
    
    @Bean(name = "hsqlJdbcTemplate")
    public NamedParameterJdbcTemplate hsqlJdbcTemplate(@Qualifier("hsqlDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

//    @Bean(name = "mysqlDataSource")
//    @Qualifier("mysqlDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.mysql")
//    // @Primary
//    public DataSource mysqlDataSource() {
//        DataSource ds = DataSourceBuilder.create().build();
//        logger.info("dataSource = " + ds);
//        return ds;
//    }
//
//    @Bean(name = "mysqlJdbcTemplate")
//    public NamedParameterJdbcTemplate oracleJdbcTemplate(@Qualifier("mysqlDataSource") DataSource dataSource) {
//        return new NamedParameterJdbcTemplate(dataSource);
//    }
}