package com.packtpub.mmj.service;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.packtpub.mmj.lib.model.Calculation;

@Component
public class CalculationControllerClientTest implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(CalculationControllerClientTest.class);

    @Autowired
    private CalculationControllerClient calculationControllerClient;

    @Override
    public void run(String... strings) throws Exception {
        Calculation mCalculation = this.calculationControllerClient.pow("5", "5");
        logger.info("# CalculationClientTest : " + ReflectionToStringBuilder.toString(mCalculation, ToStringStyle.MULTI_LINE_STYLE));
    }
}
