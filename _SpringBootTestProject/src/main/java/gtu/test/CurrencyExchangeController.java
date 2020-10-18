package gtu.test;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gtu.test.jpa.ExchangeValue;
import gtu.test.jpa.ExchangeValueRepository;
import gtu.test.jpa.H2RunScriptAction;

@SpringBootApplication
@RestController
public class CurrencyExchangeController {

    @Autowired
    private Environment environment;

    @Autowired
    private ExchangeValueRepository mExchangeValueRepository;

    @Autowired
    private H2RunScriptAction mH2RunScriptAction;

    @Value("${spring.h2.script}")
    private Resource h2ScriptSql;

    // http://localhost:8090/currency-exchange/from/USD/to/INR
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        ExchangeValue exchangeValue = mExchangeValueRepository.findByFromAndTo(from, to);
        exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return exchangeValue;
    }

    // http://localhost:8090/currency-exchange/run-script
    @GetMapping("/currency-exchange/run-script")
    public long runScript() {
        try {
            File h2File = h2ScriptSql.getFile();
            mH2RunScriptAction.runScript(h2File);
            long count = mExchangeValueRepository.count();
            System.out.println("count - " + count);
            return count;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}