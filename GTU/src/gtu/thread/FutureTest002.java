package gtu.thread;

import java.util.Optional;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan("gtu.thread")
@EnableAsync
@Component
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
public class FutureTest002 {

    public static void main(String[] args) {
        SpringApplication.run(FutureTest002.class, args);
    }
    
    @PostConstruct
    public void afterDo() {
        CustInfo tmpCustInfo = new CustInfo();
        Future<CustInfo> p1 = new AsyncResult<>(tmpCustInfo);
        p1 = foo.getCustInfo();
        System.out.println("...............");
        while (!p1.isDone()) {
            try {
                Thread.sleep(100);
                System.out.println("waiting...");
            } catch (InterruptedException e) {
            }
        }
        System.out.println("done...");
    }

    @Autowired
    private Foo foo;

    @Component
    public static class Foo {
        @Async
        public Future<CustInfo> getCustInfo() {
            CustInfo info = new CustInfo();
            Optional<CustInfo> opt = Optional.ofNullable(info);
            opt.ifPresent(obj -> {
                try {
                    long sleepTime = RandomUtils.nextInt(10) * 1000L;
                    System.out.println("sleepTime : " + sleepTime);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return new AsyncResult<CustInfo>(info);
        }
    }

    private static class CustInfo {
    }
}
