package com.packtpub.mmj.rest.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.packtpub.mmj.common.ServiceHelper;
import com.packtpub.mmj.lib.model.Calculation;

@RestController
@RequestMapping("calculation")
public class HystrixCalculationController {

    private static Logger LOG = LoggerFactory.getLogger(HystrixCalculationController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceHelper serviceHelper;

    @Bean
    public RestTemplate createTemplate() {
        return new RestTemplate();
    }

    /**
     * http://localhost:8890/calculation/power?base=5&exponent=5
     * 
     * @param b
     * @param e
     * @return
     */
    @RequestMapping("/power")
    // @HystrixCommand(fallbackMethod = "defaultPower")
    public ResponseEntity<Calculation> power(@RequestParam(value = "base") String b, @RequestParam(value = "exponent") String e) {
        LOG.info("# power --start");
        String url = "http://REST-TEST-001/power?base=" + b + "&exponent=" + e;
        LOG.debug("GetRestaurant from URL: {}", url);
        ResponseEntity<Calculation> result = restTemplate.getForEntity(url, Calculation.class);
        LOG.info("GetRestaurant http-status: {}", result.getStatusCode());
        LOG.debug("GetRestaurant body: {}", result.getBody());
        return serviceHelper.createOkResponse(result.getBody());
    }

    public ResponseEntity<Calculation> defaultPower(@RequestParam(value = "base") String b, @RequestParam(value = "exponent") String e) {
        return serviceHelper.createResponse(null, HttpStatus.BAD_GATEWAY);
    }
}
