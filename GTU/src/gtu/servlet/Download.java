package gtu.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Download extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String path = this.getServletContext().getInitParameter("workPath");
        String file = request.getParameter("file");
        File download = new File(path, file);
        BufferedInputStream in = null;
        ServletOutputStream out = null;
        try {
            response.setContentType(getContentType(download.getAbsolutePath()) + "; charset=UTF-8");
            response.setHeader("Content-disposition",
                    "attachment; filename=\"" + URLEncoder.encode(download.getName(), "UTF8") + "\"");
            URL aUrl = new URL(download.toURL(), "");
            in = new BufferedInputStream(aUrl.openStream());
            out = response.getOutputStream();
            int aRead = 0;
            while ((aRead = in.read()) != -1 & in != null) {
                out.write(aRead);
            }
            out.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    

    /**
     * 取得檔案的內容型態.
     * 
     * @param fileName
     *            : 檔案名稱.
     * @return 回傳檔案的內容型態.
     * @throws Throwable
     */
    public String getContentType(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            throw new RuntimeException("檔名為空 : " + fileName);
        }
        String contentType = fileName.substring(fileName.lastIndexOf("."));
        if (contentType == null || contentType.length() == 0) {
            throw new RuntimeException("無法取得副檔名 : " + fileName);
        }
        if(".pdf".equalsIgnoreCase(contentType)){
            contentType = "application/pdf";
        } else if (".asf".equalsIgnoreCase(contentType)) {
            contentType = "video/x-ms-asf";
        } else if (".avi".equalsIgnoreCase(contentType)) {
            contentType = "video/avi";
        } else if (".doc".equalsIgnoreCase(contentType) || //
                ".docx".equalsIgnoreCase(contentType)) {
            contentType = "application/msword";
        } else if (".zip".equalsIgnoreCase(contentType) || //
                ".rar".equalsIgnoreCase(contentType)) {
            contentType = "application/zip";
        } else if (".xls".equalsIgnoreCase(contentType) || //
                ".xlsx".equalsIgnoreCase(contentType)) {
            contentType = "application/vnd.ms-excel";
        } else if (".gif".equalsIgnoreCase(contentType)) {
            contentType = "image/gif";
        } else if (".png".equalsIgnoreCase(contentType)) {
            contentType = "image/png";
        } else if (".jpg".equalsIgnoreCase(contentType) || //
                ".jpeg".equalsIgnoreCase(contentType)) {
            contentType = "image/jpeg";
        } else if (".wav".equalsIgnoreCase(contentType)) {
            contentType = "audio/wav";
        } else if (".mp3".equalsIgnoreCase(contentType)) {
            contentType = "audio/mpeg3";
        } else if (".mpg".equalsIgnoreCase(contentType) || //
                ".mpeg".equalsIgnoreCase(contentType)) {
            contentType = "video/mpeg";
        } else if (".rtf".equalsIgnoreCase(contentType)) {
            contentType = "application/rtf";
        } else if (".htm".equalsIgnoreCase(contentType) || //
                ".html".equalsIgnoreCase(contentType)) {
            contentType = "text/html";
        } else if (".asp".equalsIgnoreCase(contentType)) {
            contentType = "text/asp";
        } else if (".xml".equalsIgnoreCase(contentType)) {
            contentType = "text/xml"; // "application/xml";
        } else if (".txt".equalsIgnoreCase(contentType)) {
            contentType = "txt/plain";
        } else {
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}
