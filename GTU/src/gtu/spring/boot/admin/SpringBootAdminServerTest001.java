package gtu.spring.boot.admin;

/**
 * dependencies {
        compile('org.springframework.boot:spring-boot-starter-web')
        compile 'de.codecentric:spring-boot-admin-server:1.5.5'
        compile 'de.codecentric:spring-boot-admin-server-ui:1.5.5'
        testCompile('org.springframework.boot:spring-boot-starter-test')
    }

加上了 EnableAdminServer 就可以了
啟動之後 打開 http://localhost:8080

https://ithelp.ithome.com.tw/users/20107338/ironman/1445?page=1
 *
 */

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringBootAdminServerTest001 {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminServerTest001.class, args);
    }
}
