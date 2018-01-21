package com.janna.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DownloadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        System.out.println("#. DownloadServlet .s");
        // Cookie cookie = new Cookie("fileDownload", "true");
        // cookie.setPath("/");
        // response.addCookie(cookie);
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("Cache-Control", "must-revalidate");
      
        response.setContentType("application/pdf; charset=UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=Report0.pdf");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        URL aUrl = new File("C:/Users/gtu001/Desktop/Clean_Code.pdf").toURL();
        System.out.println(aUrl);
        BufferedInputStream in = new BufferedInputStream(aUrl.openStream());
        ServletOutputStream out = response.getOutputStream();
        int aRead = 0;
        while ((aRead = in.read()) != -1 & in != null) {
            out.write(aRead);
        }
        out.flush();
        out.close();
        System.out.println("#. DownloadServlet .e");
    }
}
