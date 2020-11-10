package com.packtpub.mmj.rest.resources;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.packtpub.mmj.lib.model.Calculation;

@Component
public class RestTemplateExample implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(RestTemplateExample.class);

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("\n\n\n start RestTemplate client...");
        ResponseEntity<Calculation> exchange = //
                this.restTemplate.exchange(//
                        "http://localhost:9000/calculation/power?base=33&exponent=2", //
                        HttpMethod.GET, //
                        null, //
                        new ParameterizedTypeReference<Calculation>() {
                        }, //
                        (Object) "restaurants");
        Calculation body = exchange.getBody();
        logger.info("# RestTemplateExample : " + ReflectionToStringBuilder.toString(body, ToStringStyle.MULTI_LINE_STYLE));
    }
}
