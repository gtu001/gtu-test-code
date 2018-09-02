package com.janna.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

//@WebServlet("/JqGridServlet")
public class JqGridServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public JqGridServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        System.out.println("#. JqGridServlet .s");
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        
        Map<String,Object> map1 = new HashMap<String,Object>();
        List<Map<String,Object>> rowsList = new ArrayList<Map<String,Object>>();
        for(int ii = 0 ; ii < 170 ; ii ++){
            Map<String,Object> row = new HashMap<String,Object>();
            row.put("CategoryName", "CategoryName" + ii);
            row.put("ProductName", "ProductName" + ii);
            row.put("Country", "Country" + ii);
            row.put("Price", ii);
            row.put("Quantity", ii);
            rowsList.add(row);
        }
        map1.put("rows", rowsList);
        JSONObject obj = new JSONObject(map1);
        
        System.out.println(obj.toString());
        out.println(obj.toString());
        
        out.flush();
        out.close();
        System.out.println("#. JqGridServlet .e");
    }
}
