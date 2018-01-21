package com.janna.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/DispatcherServlet")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DispatcherServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        System.out.println("#. DispatcherServlet .s");
        System.out.println("api = " + request.getParameter("api"));
        System.out.println("#. DispatcherServlet .e");
//        response.sendRedirect("/FoodMenu/page/orderQuery.jsp");
        
        RequestDispatcher requestdispatcher = getServletContext().getRequestDispatcher("/page/index.jsp");
        RequestDispatcher requestdispatcher2 = request.getRequestDispatcher("/Login/confirmAutoTopUp.jsp");
        requestdispatcher.forward(request, response);
    }
}
