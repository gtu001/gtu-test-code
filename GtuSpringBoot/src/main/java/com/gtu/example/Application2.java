package com.gtu.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.kenai.jffi.Main;

public class Application2 {

    private static final Logger log = LoggerFactory.getLogger(Application2.class);

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(Main.class);
        sa.setBannerMode(Banner.Mode.OFF);
        sa.setLogStartupInfo(false);

        ApplicationContext c = sa.run(args);
        MyObject bean = c.getBean(MyObject.class);
        bean.doSomething();
    }

    @Component
    private static class MyObject {

        public void doSomething() {
            log.info("-------------");
            log.info("working ...");
            log.info("-------------");
        }
    }
}
