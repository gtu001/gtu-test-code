package gtu;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.cloud.config.server.EnableConfigServer;
//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

//import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * https://ithelp.ithome.com.tw/articles/10191946?sc=iThelpR
 * https://www.javatpoint.com/using-feign-rest-client-for-service-invocation
 * 
 */
@ComponentScan("gtu.test")
// @EnableAdminServer
// @EnableZuulProxy
@SpringBootApplication
// @EnableConfigServer
@EnableFeignClients
//@EnableFeignClients(basePackages = {"com.eprogrammerz.examples"})

@EnableDiscoveryClient
public class SpringTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                System.out.println("Let's inspect the beans provided by Spring Boot:");
                String[] beanNames = ctx.getBeanDefinitionNames();
                Arrays.sort(beanNames);
                for (String beanName : beanNames) {
                    // System.out.println(beanName);
                }
            }
        };
    }
}
