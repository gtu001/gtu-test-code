package gtu.spring.boot.cache;

/**
 * dependencies {
        compile('org.springframework.boot:spring-boot-starter-cache')
        compile 'com.github.ben-manes.caffeine:caffeine:2.6.0'
    }
 *
 * yml : 
 *  spring.cache.cache-names: getTime,currentTimeMillis
    spring.cache.caffeine.spec: maximumSize=100,expireAfterWrite=5s
    
    spring.cache.cache-names 是對應到 cacheNames
    spring.cache.caffeine.spec 就是 caffeine 提供給我們的策略了
    有哪些策略可以配置請參考 javadoc Class CaffeineSpec
    目前我是配置 存入後 cache 5 秒
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@EnableCaching
@EnableRetry
@SpringBootApplication
public class SpringBootCacheTest001 {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootCacheTest001.class, args);
    }

    @Service
    public static class TimeService {
        @Cacheable(cacheNames = "getTime")
        public Date getTime() {
            return new Date();
        }

        @Cacheable("currentTimeMillis")
        public Long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }
    }
}
