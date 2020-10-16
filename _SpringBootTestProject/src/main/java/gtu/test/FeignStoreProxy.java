package gtu.test;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gtu.test.jpa.ExchangeValue;

@FeignClient(name = "currency-exchange-service", url = "http://localhost:8090/")//
public interface FeignStoreProxy {

    @RequestMapping(method = RequestMethod.GET, value = "/currency-exchange/from/{from}/to/{to}")
    ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
}