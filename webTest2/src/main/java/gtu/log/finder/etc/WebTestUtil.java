package gtu.log.finder.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import gtu.log.Logger2File;
import gtu.log.finder.DebugMointerUI;

public class WebTestUtil {

    private Logger2File logger = DebugMointerUI.getLogger();

    private WebTestUtil() {
    }

    private HttpServletRequest request;
    private HttpServletResponse response;

    public static WebTestUtil newInstnace() {
        return new WebTestUtil();
    }

    public WebTestUtil request(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public WebTestUtil response(HttpServletResponse response) {
        this.response = response;
        return this;
    }

    public ApplicationContext getContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
    }

    public void showHeader() {
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line + "\n");
            }
            logger.debug("Header------------------------------start");
            logger.info(sb.toString());
            logger.debug("Header------------------------------end");
        } catch (Exception ex) {
            throw new RuntimeException("requestHeader ERR : " + ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e2) {
            }
        }
    }

    public WebTestUtil debugParameter() {
        Map<String, String[]> reqMap = request.getParameterMap();
        logger.debug("Parameter------------------------------start");
        for (String key : reqMap.keySet()) {
            logger.debug(String.format("\tk:%s \t v:%s", key, Arrays.toString(reqMap.get(key))));
        }
        logger.debug("Parameter------------------------------end");
        return this;
    }

    public WebTestUtil debugRequestAttr() {
        logger.debug("RequestAttr------------------------------start");
        for (Enumeration enu = request.getAttributeNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            Object val = request.getAttribute(key);
            logger.debug(String.format("\tk:%s \t v:%s", key, val));
        }
        logger.debug("RequestAttr------------------------------end");
        return this;
    }
    
    public void download(InputStream inputStream, String fileName) throws IOException {
        // 下面兩行要先寫(setContentType, setHeader)，否則檔案名稱可能會隨機變成網址名稱
        response.setContentType("application/octet-stream");
        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");

        OutputStream out = response.getOutputStream();
        IOUtils.copy(inputStream, out);
        inputStream.close();

        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        response.getOutputStream().close();
        response.flushBuffer();
    }
}