package com.gtu.example;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.kenai.jffi.Main;

public class Application2 {

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(Main.class);
        sa.setBannerMode(Banner.Mode.OFF);
        sa.setLogStartupInfo(false);

        ApplicationContext c = sa.run(args);
        MyObject bean = c.getBean(MyObject.class);
        bean.doSomething(); // TODO Auto-generated method stub
    }

    @Component
    private static class MyObject {

        public void doSomething() {
            System.out.println("-------------");
            System.out.println("working ...");
            System.out.println("-------------");
        }
    }
}
