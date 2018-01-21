package com.janna.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.json.JSONObject;

//@WebServlet("/AjaxUploadServlet")
public class AjaxUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AjaxUploadServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("#. AjaxUploadServlet .s");
        this.processUploadFile(request);
        this.responseToClient(response);
        System.out.println("#. AjaxUploadServlet .e");
    }
    
    private void responseToClient(HttpServletResponse response) {
        try{
            response.setContentType("text/html;charset=UTF-8");
//            response.setContentType("application/json;charset=UTF-8");
            PrintWriter pw = response.getWriter();
//            JSONObject obj = new JSONObject();
//            obj.put("result", true);
//            pw.write(obj.toString());
            pw.write("{\"result\":true}");
            pw.flush();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    private void processUploadFile(HttpServletRequest request){
        DiskFileUpload fu = new DiskFileUpload();
        fu.setSizeMax(10000000);// 上傳的檔案最大可以有1000000bytes
        fu.setSizeThreshold(4096);// 最多可以在memery中有4096bytes的cache
        fu.setRepositoryPath("/");// 檔案大於getSizeThreshold時的暫存路徑
        List<FileItem> fileItems = null;
        try {
            fileItems = fu.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        for (FileItem fi : fileItems) {
            System.out.println(fi.getName());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
