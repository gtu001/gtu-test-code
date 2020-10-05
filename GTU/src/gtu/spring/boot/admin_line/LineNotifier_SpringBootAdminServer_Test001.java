package gtu.spring.boot.admin_line;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * https://ithelp.ithome.com.tw/articles/10191946?sc=iThelpR
 */
@EnableAdminServer
@SpringBootApplication
public class LineNotifier_SpringBootAdminServer_Test001 {

    public static void main(String[] args) {
        // 使用指定設定檔
        // --spring.config.location=classpath:/default.properties,classpath:/override.properties
        // --spring.config.location="file:/D:/workstuff/gtu-test-code/GTU/src/gtu/spring/boot/admin_line/application.yml"
        // -Dspring.config.location="file:/D:/workstuff/gtu-test-code/GTU/src/gtu/spring/boot/admin_line/application.yml"
        SpringApplication.run(LineNotifier_SpringBootAdminServer_Test001.class, args);
    }
}
