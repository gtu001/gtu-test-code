package gtu.net.httpclient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONSerializer;

public class HttpClientPostTest {
    
    private static final Logger logger = Logger.getLogger(HttpClientPostTest.class);

    public static void main(String[] args) {
    }
    
    @RequestMapping(value = "/adConverterReq", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> adConverterReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("#. adConverterUpload start");
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONArray arry = (JSONArray)JSONSerializer.toJSON(getPostServerSideString(request));
            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            logger.info("#. adConverterReq end");
        }
    }
    
    //sendRequest 後 , 伺服端處理方式
    private String getPostServerSideString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
//            reader = request.getReader();
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    @Async
    public void sendRequest(String url, String content) {
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
                try {
                    InputStream instream = responseEntity.getContent();
                    BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                } catch (IOException e2) {
                    logger.error("log error 2 !", e2);
                }
            }
        } catch (Exception e1) {
            logger.error("log error 1 !", e1);
        }
    }
}
