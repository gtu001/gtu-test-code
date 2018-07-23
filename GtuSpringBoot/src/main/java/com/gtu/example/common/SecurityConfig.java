package com.gtu.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.gtu.example.rabbitmq.RabbitMqBeanDefiner;

/**
 * 預設的 spring security 
 * 帳號 : user
 * 密碼[[在log]]
 * ↓↓↓↓↓↓
 * Using generated security password: <password>
 * ↑↑↑↑↑↑
 * 
 * disable作法
 * ↓↓↓↓↓↓
 * org.springframework.boot:spring-boot-starter-security
 * ↑↑↑↑↑↑
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//
                .anyRequest()//
                .permitAll()//
                .and()//
                .csrf()//
                .disable();
    }
}