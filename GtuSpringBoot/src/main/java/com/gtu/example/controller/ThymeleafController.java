package com.gtu.example.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

public class ThymeleafController {

    public void process(final HttpServletRequest request, final HttpServletResponse response, //
            final ServletContext servletContext, final ITemplateEngine templateEngine) throws Exception {
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();
        
        ctx.setVariable("today", dateFormat.format(cal.getTime()));
         
        templateEngine.process("home", ctx, response.getWriter()); 
    }
}