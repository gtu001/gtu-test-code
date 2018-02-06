package gtu.log.finder.etc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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

    public String getInputStreamToString(String encode) {
        BufferedReader reader = null;
        InputStream data = null;
        try {
            StringBuilder sb = new StringBuilder();
            data = new BufferedInputStream(request.getInputStream());
            data.mark(0);
            reader = new BufferedReader(new InputStreamReader(data, encode));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line);
            }
            logger.debug("Header------------------------------start");
            logger.info(sb.toString());
            logger.debug("Header------------------------------end");
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("requestHeader ERR : " + ex);
        } finally {
            try {
                // reader.close();
                data.reset();
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
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
        // response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");

        OutputStream out = response.getOutputStream();
        IOUtils.copy(inputStream, out);
        inputStream.close();

        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        response.getOutputStream().close();
        response.flushBuffer();
    }

    public static class WebParamterWrapper {
        String encode;
        HttpServletRequest request;
        String inputStreamStr; 
        Map<String,String> map = new LinkedHashMap<String,String>();
        
        public WebParamterWrapper(HttpServletRequest request, String encode){
            this.encode = encode;
            this.request = request;
            this.inputStreamStr = getInputStreamToString();
            this.map = this.parseToMap(inputStreamStr);
            DebugMointerUI.getLogger().debug("getInputStream = " + this.inputStreamStr);
        }

        private Map<String, String> parseToMap(String inputStreamStr) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            Pattern ptn = Pattern.compile("(.*?)\\=((.*?)\\&|(.*))");
            Matcher mth = ptn.matcher(inputStreamStr);
            while (mth.find()) {
                String key = mth.group(1);
                String val1 = mth.group(3);
                String val2 = mth.group(4);
                String val = val1;
                if (StringUtils.isNotBlank(val2)) {
                    val = val2;
                }
                map.put(key, val);
            }
            return map;
        }
        
        public String getParameter(String key) {
            String val = map.get(key);
            DebugMointerUI.getLogger().debug(String.format("get > [%s] : [%s]", key, val));
            return val;
        }
        
        private String getInputStreamToString() {
            BufferedReader reader = null;
            InputStream data = null;
            try {
                StringBuilder sb = new StringBuilder();
                data = new BufferedInputStream(request.getInputStream());
                data.mark(0);
                reader = new BufferedReader(new InputStreamReader(data, encode));
                for (String line = null; (line = reader.readLine()) != null;) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (Exception ex) {
                throw new RuntimeException("requestHeader ERR : " + ex);
            } finally {
                try {
                    // reader.close();
                    data.reset();
                } catch (Exception e2) {
                }
            }
        }
    }
}