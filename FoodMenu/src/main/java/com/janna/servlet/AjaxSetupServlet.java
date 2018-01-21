package com.janna.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/AjaxSetupServlet")
public class AjaxSetupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AjaxSetupServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        System.out.println("#. AjaxSetupServlet .s");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        out.println("{\"rtnval\":true}");
        out.flush();
        out.close();
        System.out.println("#. AjaxSetupServlet .e");
    }
}
