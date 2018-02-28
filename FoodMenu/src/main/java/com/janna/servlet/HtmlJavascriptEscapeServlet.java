package com.janna.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

//@WebServlet("/HtmlJavascriptEscapeServlet")
public class HtmlJavascriptEscapeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(HtmlJavascriptEscapeServlet.class);

    public HtmlJavascriptEscapeServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("#. HtmlJavascriptEscapeServlet .s");
        String html = org.springframework.web.util.HtmlUtils.htmlEscape(StringUtils.trimToEmpty(request.getParameter("html")));
        String javascript = org.springframework.web.util.JavaScriptUtils.javaScriptEscape(StringUtils.trimToEmpty(request.getParameter("javascript")));
        logger.info("orign html - " + html);
        logger.info("orign javascript - " + javascript);
        Map<String, String> valMap = new LinkedHashMap<String, String>();
        valMap.put("html", html);
        valMap.put("javascript", javascript);
        logger.info("valMap - " + valMap);
        writeOutputData(valMap, response);
        logger.info("#. HtmlJavascriptEscapeServlet .e");
    }

    private void writeOutputData(Map<String, String> valMap, HttpServletResponse response) {
        try {
            ServletOutputStream out = response.getOutputStream();
            JSONObject jobj = new JSONObject();
            response.setContentType("application/json");
            for (String key : valMap.keySet()) {
                jobj.put(key, valMap.get(key));
            }
            out.println(jobj.toString());
            out.flush();
            out.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
