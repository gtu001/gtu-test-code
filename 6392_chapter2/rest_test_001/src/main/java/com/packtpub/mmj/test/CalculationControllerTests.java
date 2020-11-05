package com.packtpub.mmj.test;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.packtpub.mmj.lib.model.Calculation;
import com.packtpub.mmj.rest.RestSampleApp;
import com.packtpub.mmj.rest.resources.CalculationController;

//@SpringBootConfiguration
//@SpringBootApplication
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestSampleApp.class)
@WebAppConfiguration
// @Import(com.packtpub.mmj.test.CalculationControllerTests.class) //import bean
public class CalculationControllerTests {

    @Autowired
    CalculationController calculationController;

    @Test
    public void validPow() {
        Logger.getGlobal().info("### Start validPow test");
        Calculation calculation = calculationController.pow("5", "3");
        Assert.assertEquals(calculation.getFunction(), "power");
        Assert.assertNotNull(calculation.getOutput());
        Logger.getGlobal().info("### " + String.valueOf(calculation.getOutput()));
        Logger.getGlobal().info("### End validPow test");
    }

    @Test
    public void validSqrt() {
        Logger.getGlobal().info("### Start validSqrt test");
        Calculation calculation = calculationController.sqrt("49");
        Assert.assertEquals(calculation.getFunction(), "sqrt");
        Assert.assertNotNull(calculation.getOutput());
        Logger.getGlobal().info("### " + String.valueOf(calculation.getOutput()));
        Logger.getGlobal().info("### End validSqrt test");
    }
}