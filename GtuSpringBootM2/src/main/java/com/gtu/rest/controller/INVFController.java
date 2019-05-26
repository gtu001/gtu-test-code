package com.gtu.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * INVG-rest端口 http://localhost:8080/swagger-ui.html
 * 
 * @author INVG-rest小組
 *
 */
@RestController
@RequestMapping(value = "/INVF-rest")
public class INVFController {

    private static final Logger LOG = LogManager.getLogger(INVFController.class);

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @RequestMapping(value = "/service", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> query(@RequestBody Map<String, Object> reqMap, HttpServletRequest req) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
        } catch (Exception e) {
            LOG.error("query ERR : " + e.getMessage(), e);
        }
        return result;
    }
}
