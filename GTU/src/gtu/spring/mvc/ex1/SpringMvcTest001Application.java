package gtu.spring.mvc.ex1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.annotation.JsonProperty;

@Configuration
@ComponentScan("gtu.spring.mvc.ex1")
@Component
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class })
public class SpringMvcTest001Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringMvcTest001Application.class, args);
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

    // @SessionAttributes({ "loginDomain", "roiEntity" })
    @RequestMapping("/fund")
    @RestController
    public static class FundController {
        @RequestMapping(value = "/qryTest", method = { RequestMethod.GET, RequestMethod.POST })
        @ResponseBody
        public ResponseEntity<TestBean> qryTest(//
                @ModelAttribute(value = "fundQryBranchWebRequestDto") //
                TestBean webRequestDto, //
                Model model) {
            // if (webRequestDto != null) {
            // TestBean bean = new TestBean();
            // bean.setTest(UUID.randomUUID().toString());
            // // set ui model
            // return new ResponseEntity<TestBean>(bean, HttpStatus.OK);
            // }
            // return new ResponseEntity(HttpStatus.NOT_FOUND);
            throw new CallWebServiceException("測試錯誤!");
        }
    }

    public static class CallWebServiceException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public CallWebServiceException(String message) {
            super(message);
        }
    }

    // @EnableWebMvc
    // @Configuration
    // public class WebConfig implements WebMvcConfigurer {
    //
    // @Override
    // public void addViewControllers(ViewControllerRegistry registry) {
    // registry.addViewController("/").setViewName("index");
    // }
    //
    // @Bean
    // public ViewResolver viewResolver() {
    // InternalResourceViewResolver bean = new InternalResourceViewResolver();
    // bean.setViewClass(JstlView.class);
    // bean.setPrefix("/WEB-INF/view/");
    // bean.setSuffix(".jsp");
    // return bean;
    // }
    // }

    @ControllerAdvice
    public class ControllerHandler {

        private static final String ERROR_PAGE = "error";

        @ExceptionHandler(CallWebServiceException.class)
        public ModelAndView handleCallWebServiceException(CallWebServiceException e) {
            System.out.println("in handleCallWebServiceException");
            e.printStackTrace();
            return this.generateModelAndView("Web Service連線失敗(" + e.getMessage() + ")!");
        }

        @ExceptionHandler(Exception.class)
        public ModelAndView handleException(Model model, Exception e) {
            System.out.println("in handleException");
            e.printStackTrace();
            return this.generateModelAndView("系統錯誤(" + e.getMessage() + ")!");
        }

        private ModelAndView generateModelAndView(String msgValue) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("errorDetail", msgValue);
            mav.setViewName(ERROR_PAGE);
            return mav;
        }
    }
}
