package gtu.test;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gtu.test.jpa.ExchangeValue;
import gtu.test.jpa.ExchangeValueRepository;

@SpringBootApplication
@RestController
public class CurrencyExchangeController {

    @Autowired
    private Environment environment;

    @Autowired
    private ExchangeValueRepository mExchangeValueRepository;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        ExchangeValue exchangeValue = new ExchangeValue(1000L, from, to, BigDecimal.valueOf(65));
        exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return exchangeValue;
    }

    @GetMapping("/currency-exchange/test001")
    public ExchangeValue test001() {
        ExchangeValue exchangeValue = new ExchangeValue();
        exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        exchangeValue.setConversionMultiple(new BigDecimal(1000));
        exchangeValue.setFrom("CHN");
        exchangeValue.setTo("AMN");
        exchangeValue.setId(1000L);
        mExchangeValueRepository.save(exchangeValue);
        mExchangeValueRepository.flush();
        long count = mExchangeValueRepository.count();
        System.out.println("count - " + count);
        return exchangeValue;
    }

    @GetMapping("/currency-exchange/test002")
    public List<ExchangeValue> test002() {
        List<ExchangeValue> lst = mExchangeValueRepository.findAll();
        for (ExchangeValue vo : lst) {
            System.out.println("\t>>" + ReflectionToStringBuilder.toString(vo));
        }
        return lst;
    }
}