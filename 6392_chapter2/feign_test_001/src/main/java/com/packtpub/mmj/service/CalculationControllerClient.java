package com.packtpub.mmj.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.packtpub.mmj.lib.model.Calculation;

@FeignClient("rest-test-001")
interface CalculationControllerClient {
    @RequestMapping(method = RequestMethod.GET, value = "/power")
    public Calculation pow(@RequestParam(value = "base") String b, @RequestParam(value = "exponent") String e);

    @RequestMapping(method = RequestMethod.GET, value = "/sqrt/{value:.+}")
    public Calculation sqrt(@PathVariable(value = "value") String aValue);
}