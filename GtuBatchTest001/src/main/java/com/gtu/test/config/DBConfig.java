package com.gtu.test.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class DBConfig {

    @Autowired
    private Environment env;

    @Bean
    @Profile({ "ut", "local" })
    public DataSource getUtPoolDataSource() throws Exception {
        String driver = env.getProperty("spring.datasource.derby.driver-class-name");
        String url = env.getProperty("spring.datasource.derby.url");
        String username = env.getProperty("spring.datasource.derby.username");
        String password = env.getProperty("spring.datasource.derby.password");
        return DataSourceBuilder.create()//
                .driverClassName(driver)//
                .url(url)//
                .username(username)//
                .password(password)//
                .build();
    }
}