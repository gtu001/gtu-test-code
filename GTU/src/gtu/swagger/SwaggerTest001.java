package gtu.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.annotation.JsonProperty;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class }) // disable
                                                                                                       // Mongo
@ComponentScan("gtu.swagger")
@Component
@SpringBootApplication(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class }) // disable
                                                                                                     // Mongo
public class SwaggerTest001 {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerTest001.class, args);
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

//    @SessionAttributes({ "loginDomain", "roiEntity" })
    @RequestMapping("/fund")
    @RestController
    public static class FundController {
        @RequestMapping(value = "/qryTest", method = { RequestMethod.GET, RequestMethod.POST })
        @ResponseBody
        public ResponseEntity<TestBean> qryTest(//
                @ModelAttribute(value = "fundQryBranchWebRequestDto") //
                TestBean webRequestDto, //
                Model model) {
            if (webRequestDto != null) {
                TestBean bean = new TestBean();
                // set ui model
                return new ResponseEntity<TestBean>(bean, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

//    @Profile({ "utvm", "utpaas", "uatvm", "uatpaas" })
    @Configuration
    @EnableSwagger2
    public static class SwaggerConfig {

        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)//
                    .apiInfo(apiInfo())//
                    .select()//
                    .apis(RequestHandlerSelectors.basePackage("gtu.swagger"))//
                    .paths(PathSelectors.any())//
                    .build();//
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()//
                    .title("DEMO API")//
                    .description("Swagger UI Demo")//
                    .version("v1")//
                    .build();//
        }
    }
}
