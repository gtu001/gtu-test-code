package gtu.annotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

public class Boss {
    // 自动注入类型为 Car 的 Bean  
    @Resource
    private String car;
    // 自动注入 bean 名称为 office 的 Bean  
    @Resource(name = "office", type = String.class)
    private String office;

    @PostConstruct
    public void postConstruct1() {
        System.out.println("postConstruct1" + office + car);
    }

    @PreDestroy
    public void preDestroy1() {
        System.out.println("preDestroy1");
    }
}