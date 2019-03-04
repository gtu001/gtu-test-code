package com.gtu.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {

    @Bean(name = "sqlserverCTFLHistoryDataSource")
    @Qualifier("sqlserverCTFLHistoryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver-CTFLHistory")
    public DataSource sqlserverCTFLHistoryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlserverCTFLHistoryJdbcTemplate")
    public NamedParameterJdbcTemplate sqlserverCTFLHistoryJdbcTemplate(@Qualifier("sqlserverCTFLHistoryDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name = "oracleDataSource")
    @Qualifier("oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oracleJdbcTemplate")
    public NamedParameterJdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}