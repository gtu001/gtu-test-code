package gtu.net.httpclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONSerializer;

public class HttpClientPostTest {

    private static final Logger logger = Logger.getLogger(HttpClientPostTest.class);

    public static void main(String[] args) throws JSONException, IOException {
//        16604305779|手机一|6252058105000640
//        16604305770|手机九|6252058105000723   
//        18598250378|手机三|6252058105000665   
        Map<String,String> req = new HashMap<String,String>();
        req.put("cardNbr", "6252051105000983");
        req.put("tranYm", "2412");
        req.put("option", "");
        req.put("pinFlag", "2");
        req.put("pin", "24E61831F8ECB6DB6BB2F3F30A3DDDB1");
        String paramStr = mapToParameterString(req);

         String url = "http://127.0.0.1:8081/queryPeriod";
//        String url = "http://127.0.0.1:8080/WARBill/BillingStatementAP";
        String result = HttpClientPostTest.doPostRequest(url, paramStr, "utf8");
        System.out.println(url);
        System.out.println("result -- " + result);
    }

    private static String mapToParameterString(Map<String, String> map) {
        try {
            StringBuffer sb = new StringBuffer();
            for (String key : map.keySet()) {
                String val = map.get(key);
                sb.append(key + "=" + URLEncoder.encode(val, "UTF8") + "&");
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("mapToParameterString ERR : " + ex.getMessage(), ex);
        }
    }

    @RequestMapping(value = "/adConverterReq", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> adConverterReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#. adConverterUpload start");
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONArray arry = (JSONArray) JSONSerializer.toJSON(getPostServerSideString(request));
            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            logger.info("#. adConverterReq end");
        }
    }

    // sendRequest 後 , 伺服端處理方式
    private String getPostServerSideString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            // reader = request.getReader();
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    @Async
    public static String sendRequest(String url, String content) {
        logger.info("sendRequest Url:" + url);
        try {
            // Produce the output
            // option 1
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            writer.write(content);
            writer.flush();
            writer.close();

            // option 2
            StringEntity strEntity = new StringEntity(content, HTTP.UTF_8);
            strEntity.setContentType("text/plain; charset=UTF-8");

            // Create the request
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            request.setEntity(new ByteArrayEntity(out.toByteArray()));

            CloseableHttpResponse response = httpClient.execute(request);

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                BufferedReader in = null;
                try {
                    StringBuffer sb = new StringBuffer();
                    InputStream instream = responseEntity.getContent();
                    in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                    for (String line = null; (line = in.readLine()) != null;) {
                        sb.append(line);
                    }
                    return sb.toString();
                } catch (IOException e2) {
                    logger.error("log error 2 !", e2);
                    return "";
                }
            }
        } catch (Exception e1) {
            logger.error("log error 1 !", e1);
        }
        return "";
    }

    public static String doPostRequest(String urlStr, String postData, String encode) throws IOException {
        StringBuffer response = new StringBuffer();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        InputStreamReader isr = null;
        char[] buff = new char[4096];
        int size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null)
                return "";
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            os = conn.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os);
            wr.write(postData);
            wr.flush();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= Integer.MAX_VALUE) {
                    break;
                }
            }
            return response.toString();

        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
            try {
                os.close();
            } catch (Exception e) {
            }
        }
    }
}
