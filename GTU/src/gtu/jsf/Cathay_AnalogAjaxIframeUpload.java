package gtu.jsf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.testng.log4testng.Logger;

import gtu.file.FileUtil;
import gtu.log.finder.etc.DebugMointerUIHotServlet;
import net.sf.json.JSONObject;

/**
 * 用古老寫法完成 類似 ajax 上傳檔案的效果
 * 
 * 前端請參考 Cathay_AnalogAjaxIframeUpload_前端處理.txt 檔案
 * 
 * @author wistronits
 */
public class Cathay_AnalogAjaxIframeUpload extends HttpServlet {

    private static final Logger log = Logger.getLogger(Cathay_AnalogAjaxIframeUpload.class);

    public Map<String, String> uploadFileFilterParameterMap(HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            return Collections.emptyMap();
        }

        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            Map<String, String> parameterMap = new LinkedHashMap<String, String>();

            for (FileItem item : items) {
                if (item.isFormField()) {
                    // Process regular form field (input
                    // type="text|radio|checkbox|etc", select, etc).
                    String fieldName = item.getFieldName();
                    String fieldValue = item.getString();
                    // ... (do your job here)

                    parameterMap.put(fieldName, fieldValue);

                } else {
                    try {
                        String fieldName = item.getFieldName();
                        String fileName = FilenameUtils.getName(item.getName());

                        if (StringUtils.isBlank(fileName)) {
                            continue;
                        }

                        // 上傳檔案的處理 TODO
                        // 上傳檔案的處理 TODO
                        // 上傳檔案的處理 TODO
                        // 上傳檔案的處理 TODO
                        File tempFile = new File(FileUtil.DESKTOP_DIR, fileName);
                        BOMInputStream fileStream = new BOMInputStream(item.getInputStream());
                        IOUtils.copy(fileStream, new FileOutputStream(tempFile));
                    } catch (Exception e) {
                        throw new RuntimeException("uploadFile failed : " + e.getMessage(), e);
                    }
                }
            }

            return parameterMap;
        } catch (Exception e1) {
            throw new RuntimeException("uploadFileFilterParameterMap ERR : " + e1.getMessage(), e1);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        try {
            String serverEncoding = "UTF8";

            // 取得 requestMap 與處理上傳檔案  TODO
            Map<String, String> requestMap = uploadFileFilterParameterMap(req);

            Map<String, Object> responseMap = new HashMap<String, Object>();
            String responseText = JSONObject.fromObject(responseMap).toString();

            StringBuilder htmlCode = new StringBuilder();
            htmlCode.append("<span id = 'filedata'>");
            htmlCode.append(responseText);
            htmlCode.append("</span>");

            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE HTML>");
            sb.append("<html>");
            sb.append("<head>");
            sb.append("<meta http-equiv='Content-Type' content='text/html; charset=");
            sb.append(serverEncoding).append("'>");
            sb.append("</head>");
            sb.append("<body>");
            sb.append(htmlCode);
            sb.append("</body>");
            sb.append("</html>");

            response.setContentType("text/html");
            response.setCharacterEncoding(serverEncoding);

            ServletOutputStream output = response.getOutputStream();
            output.write(sb.toString().getBytes(serverEncoding));
            output.flush();
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
