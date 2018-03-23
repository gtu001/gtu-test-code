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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import gtu.net.HttpUtil;
import net.sf.json.JSONSerializer;

public class HttpClientPostTest {

//    private static final Logger logger = Logger.getLogger(HttpClientPostTest.class);

    public static void main(String[] args) throws JSONException, IOException {
        // 16604305779|手机一|6252058105000640
        // 16604305770|手机九|6252058105000723 nodata
        // 18598250378|手机三|6252058105000665
        
        if(false){
            JSONObject req = new JSONObject();
            req.put("cardNbr", "6252058105000640");
            req.put("tranYm", "");
            req.put("option", "");
            req.put("pinFlag", "0");
            req.put("pin", "");
            String paramStr = mapToParameterString_JSON(req);

            String url = "http://127.0.0.1:8081/queryPeriod";
//             String url = "http://127.0.0.1:8080/WARBill/BillingStatementAP";
            String result = HttpUtil.doPostRequest(url, paramStr);
            System.out.println(url);
            System.out.println("result -- " + result);

//            JSONObject json = new JSONObject(result);
//            String returnCode = json.getJSONObject("system").getString("returnCode");
//            String returnMessage = json.getJSONObject("system").getString("returnMessage");
//            System.out.println("returnCode : " + returnCode);
//            System.out.println("returnMessage : " + returnMessage);
        }
        
        if(true){
            JSONObject req = new JSONObject();
            req.put("cardnbr", "6252058105000665");
            req.put("opt", "1");
            req.put("credno", "1");
            req.put("prodid", "2");
            req.put("nbrMths", "3");
            req.put("billAmt", "100000");
            req.put("currNum", "156");
            req.put("retain", "");
            
            String paramStr = mapToParameterString_JSON(req);

            String url = "http://127.0.0.1:8081/calcuPeriod";
//             String url = "http://127.0.0.1:8080/WARBill/InstallmentCalculateAP";
            String result = HttpUtil.doPostRequest(url, paramStr);
            System.out.println(url);
            System.out.println("result -- " + result);

//            JSONObject json = new JSONObject(result);
//            String returnCode = json.getJSONObject("system").getString("returnCode");
//            String returnMessage = json.getJSONObject("system").getString("returnMessage");
//            System.out.println("returnCode : " + returnCode);
//            System.out.println("returnMessage : " + returnMessage);
        }
        
        if(false){
            JSONObject req = new JSONObject();
            req.put("cardnbr", "6252058105000723");
            req.put("pinFlag", "0");
            req.put("pin", "");
            req.put("opertp", "2");
            req.put("credNo", "");
            req.put("nbrMths", "");
            req.put("amt", "0");
            req.put("currNum", "156");
            req.put("samtPnt", "");
            req.put("deduFee", "0");
            req.put("feePcnt", "");
            req.put("revs", "");
            
            String paramStr = mapToParameterString_JSON(req);

            String url = "http://127.0.0.1:8081/applyInstallment";
//             String url = "http://127.0.0.1:8080/WARBill/BBCQAP";
            String result = HttpUtil.doPostRequest(url, paramStr);
            System.out.println(url);
            System.out.println("result -- " + result);

//            JSONObject json = new JSONObject(result);
//            String returnCode = json.getJSONObject("system").getString("returnCode");
//            String returnMessage = json.getJSONObject("system").getString("returnMessage");
//            System.out.println("returnCode : " + returnCode);
//            System.out.println("returnMessage : " + returnMessage);
        }
        System.out.println("done...");
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

    private static String mapToParameterString_JSON(JSONObject map) {
        try {
            StringBuffer sb = new StringBuffer();
            for (Iterator it = map.keys(); it.hasNext();) {
                String key = (String) it.next();
                String val = map.getString(key);
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
        System.out.println("#. adConverterUpload start");
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONArray arry = (JSONArray) JSONSerializer.toJSON(getPostServerSideString(request));
            return map;
        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("#. adConverterReq end");
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
        System.out.println("sendRequest Url:" + url);
        try {
            // Produce the output
            // option 1
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            writer.write(content);
            writer.flush();
            writer.close();
            ByteArrayEntity entity1 = new ByteArrayEntity(out.toByteArray());

            // option 2
            StringEntity entity2 = new StringEntity(content, "UTF-8");
            entity2.setContentType("text/plain; charset=UTF-8");

            // option 3
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", "John"));
            params.add(new BasicNameValuePair("password", "pass"));
            UrlEncodedFormEntity entity3 = new UrlEncodedFormEntity(params);

            // Create the request
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            request.setEntity(entity1);

            // append header
            // UsernamePasswordCredentials creds = new
            // UsernamePasswordCredentials("John", "pass");
            // request.addHeader(new BasicScheme().authenticate(creds, request,
            // null));

            // 如果為json 格式
            // request.setHeader("Accept", "application/json");
            // request.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);
            System.out.println("responseReasonPhrase = " + response.getStatusLine().getReasonPhrase());
            System.out.println("responseStatusCode = " + response.getStatusLine().getStatusCode());

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
//                    logger.error("log error 2 !", e2);
                    e2.printStackTrace();
                    return "";
                }
            }
        } catch (Exception e1) {
//            logger.error("log error 1 !", e1);
            e1.printStackTrace();
        }
        return "";
    }
}
