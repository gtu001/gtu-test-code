package gtu.net.urlConn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;

public class HttpRequestTest {
    
    public static void main(String[] args) throws Exception{
        HttpRequestTest test = new HttpRequestTest();
        int result = test.requestPost();
        System.out.println("done...");
    }

    private int requestPost() throws Exception {
        try {
            URL url = new URL("http://172.61.5.60/ApiIBT/Sms/SendSms");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url.toURI());
            postRequest.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            
            postParams.add(new BasicNameValuePair("BUSINESSCODE", "BA"));
            Map<String, String> map = new LinkedHashMap<String, String>();

            String h_i_date = "20160808";
            String h_i_time = "1747";
            String amt = "0";
            map.put("VAR1", h_i_date.substring(4, 6));
            map.put("VAR2", h_i_date.substring(6, 8));
            map.put("VAR3", h_i_time.substring(0, 2));
            map.put("VAR4", h_i_time.substring(2, 4));
            map.put("VAR5", amt);

            Gson gson = new Gson();
            String jsonString = gson.toJson(map);
            postParams.add(new BasicNameValuePair("TEMPLATEVAR", jsonString));
            
            // 失效時間
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            postParams.add(new BasicNameValuePair("STOPTIME", sdf.format(new Date(Calendar.getInstance().getTimeInMillis() + (15 * 60000)))));

            postRequest.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Runtime Failed HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));

            String h_rowid = "";
            String h_errorcode = "";
            String output;
            
            // System.out.println("result from Server .... \n");
            while ((output = br.readLine()) != null) {
                String[] aarray = output.replaceAll("\\[", "").replaceAll("\\{", "").replaceAll("\\:", ",").replaceAll("\\}", "").replaceAll("\\]", "").split(",");
                if (aarray.length == 2) {
                    if (aarray[0].equals("ErrorCode")) {
                        h_rowid = null;
                        h_errorcode = aarray[1];
                    }
                } else if (aarray.length == 4) {
                    h_rowid = aarray[1];
                    h_errorcode = aarray[3];
                }
            }

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
}
