package gtu.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gtu.test.jpa.ExchangeValue;

@RestController
public class FeignStoreProxyController {

    @Autowired
    private FeignStoreProxy client;

    @GetMapping("/feign-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        return client.retrieveExchangeValue(from, to);
    }
}