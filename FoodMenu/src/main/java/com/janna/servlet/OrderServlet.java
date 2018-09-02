package com.janna.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public OrderServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        System.out.println("#. OrderServlet .s");
        Map<String,String[]> reqMap = request.getParameterMap();
        for(String key : reqMap.keySet()){
            System.out.println(key + "..." + Arrays.toString(reqMap.get(key)));
        }
        System.out.println("#. OrderServlet .e");
        response.sendRedirect("/FoodMenu/page/orderQuery.jsp");
    }
}
