package gtu.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

@Configuration
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class }) // disable
                                                                                                       // Mongo
@ComponentScan("gtu.aop")
@Component
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class }) // disable
                                                                                                     // Mongo
public class AopAspectTest001 {

    public static void main(String[] args) {
        SpringApplication.run(AopAspectTest001.class, args);
    }

    public static class TestBean {
        @JsonProperty("test")
        String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }
    
    /*
     * http://localhost:8080/fund/qryTest?test=11111
     */
    @RequestMapping("/fund")
    @RestController
    public static class FundController {

        @RequestMapping(value = "/qryTest", method = { RequestMethod.GET, RequestMethod.POST })
        @ResponseBody
        @ApLogger
        public ResponseEntity<TestBean> qryTest(//
                @ModelAttribute(value = "fundQryBranchWebRequestDto") //
                TestBean webRequestDto, //
                Model model) {
            if (webRequestDto != null) {
                TestBean bean = new TestBean();
                bean.setTest(UUID.randomUUID().toString());
                // set ui model
                return new ResponseEntity<TestBean>(bean, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ApLogger {
    }

    @Aspect
    @Component
    public static class AopConfig {

        private static Logger logger = LoggerFactory.getLogger(AopAspectTest001.class);

        @Pointcut("execution(public * gtu.aop..*.*Grid(..)) && !excludeQryControllerGrid()")
        public void qryControllerGrid() {
        }

        @Pointcut("execution(public * gtu.aop..*.*Process(..))")
        public void qryServiceProcess() {
        }

        @Pointcut("execution(public * gtu.aop..*.FundController.qryRoiGrid(..))")
        public void excludeQryControllerGrid() {
        }

        @Pointcut("@annotation(gtu.aop.AopAspectTest001$ApLogger) && !excludeQryControllerGrid()")
        public void log() {
        }

        @Around("log() || qryControllerGrid() || qryServiceProcess()")
        public Object doCheckLogin(ProceedingJoinPoint joinPoint) throws Throwable {
            LocalDateTime startTime = LocalDateTime.now();
            logger.info("===== START (CLASS_METHOD = {}) START {} =====", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), startTime);

            Object ret = joinPoint.proceed();

            LocalDateTime endTime = LocalDateTime.now();
            Duration duration = Duration.between(startTime, endTime);
            logger.info("===== END (CLASS_METHOD = {}) END {} ,total cost {} millisecond(s) =====", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), endTime,
                    duration.toMillis());
            return ret;
        }

        @Before("log()")
        public void before(JoinPoint joinPoint) throws Throwable {
            LocalDateTime startTime = LocalDateTime.now();
            logger.info("===== START (CLASS_METHOD = {}) START {} =====", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), startTime);
        }

        @After("log()")
        public void after(JoinPoint joinPoint) throws Throwable {
            LocalDateTime endTime = LocalDateTime.now();
            logger.info("===== END (CLASS_METHOD = {}) END {} =====", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), endTime);
        }
    }
}
