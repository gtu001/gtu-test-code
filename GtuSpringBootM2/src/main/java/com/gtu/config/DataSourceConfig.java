package com.gtu.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {
    
    @Bean(name = "hsqlDataSource")
    @Qualifier("hsqlDataSource")
    @ConfigurationProperties(prefix = "datasource.hsqlDB")
    public DataSource hsqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "hsqlJdbcTemplate")
    public NamedParameterJdbcTemplate hsqlJdbcTemplate(@Qualifier("hsqlDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

//    @Bean(name = "oracleDataSource")
//    @Qualifier("oracleDataSource")
//    @ConfigurationProperties(prefix = "datasource.oracle")
//    @Primary
//    public DataSource oracleDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "oracleJdbcTemplate")
//    public NamedParameterJdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDataSource") DataSource dataSource) {
//        return new NamedParameterJdbcTemplate(dataSource);
//    }

}