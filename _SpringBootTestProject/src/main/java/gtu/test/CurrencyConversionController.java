package gtu.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gtu.test.jpa.CurrencyConversionBean;

@RestController
public class CurrencyConversionController {

    // http://localhost:8090/currency-converter/from/USD/to/INR/quantity/100
    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        String restUrl = "http://localhost:8090/currency-exchange/from/{from}/to/{to}";

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(restUrl, CurrencyConversionBean.class, uriVariables);
        CurrencyConversionBean response = responseEntity.getBody();
        return new CurrencyConversionBean(response.getId(), //
                from, //
                to, //
                response.getConversionMultiple(), //
                quantity, //
                quantity.multiply(response.getConversionMultiple()), //
                response.getPort()//
        ); //
    }
}