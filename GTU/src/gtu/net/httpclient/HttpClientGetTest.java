package gtu.net.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class HttpClientGetTest {
    
    private static final Logger logger = Logger.getLogger(HttpClientGetTest.class);

    public static void main(String[] args) {
        HttpClientGetTest t = new HttpClientGetTest();
//        t.sendRequestOrign("https://iam.amazonaws.com/?Action=ListUsers&Version=2010-05-08");
        t.sendRequestOrign("http://127.0.0.1:9090/AS/admobReq?ugid=&blockcode=F1");
//        t.sendRequestOrign("http://127.0.0.1:8080/BWS/admobReq?ugid=&blockcode=F1");
    }
    
    public void sendRequestOrign(String urlStr) {
        logger.info("v2 sendRequestOrign Url:" + urlStr);
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestProperty("Accept", "application/json");
            //OutputStream os = conn.getOutputStream();//只要一執行此行就會自動變POST
            
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.info("連線失敗 : " + conn.getResponseCode() + " , msg : " + conn.getResponseMessage());
                return;
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for(String line = null; (line = reader.readLine() )!=null;) {
                logger.info(line);
            }
            reader.close();
            conn.disconnect();
        }catch(Exception e1) {
            logger.error("log error 1 !", e1);
        }
    }

    @Async
    public JSONObject sendRequest(String url) {
        logger.info("sendRequest Url:" + url);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                try {
                    InputStream instream = responseEntity.getContent(); 
                    BufferedReader in = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    for (String line = null; (line = in.readLine()) != null;) {
                        sb.append(line);
                    }
                    JSONObject obj = (JSONObject)JSONSerializer.toJSON(sb.toString());
                    return obj;
                } catch (IOException e2) {
                    logger.error("log error 2 !", e2);
                }
            }
        } catch (Exception e1) {
            logger.error("log error 1 !", e1);
        }
        return null;
    }
}
