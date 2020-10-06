package gtu.mapstruct;

import java.util.ArrayList;

import org.springframework.cloud.netflix.ribbon.RibbonClientSpecification;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; 

@Configuration
public class WebConfigure {

    @Bean
    public SpringClientFactory springClientFactory() {
        SpringClientFactory factory = new SpringClientFactory();
        ArrayList<RibbonClientSpecification> list = new ArrayList<RibbonClientSpecification>();
//        list.add(new RibbonClientSpecification("default.easytrans", new Class[] { RestEasyTransactionConfiguration.class }));
        factory.setConfigurations(list);
        return factory;
    }
}
