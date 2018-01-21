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

public class UrlConnectionTest001 {

    public static void main(String[] args) throws Exception {
        UrlConnectionTest001 test = new UrlConnectionTest001();
        int result = test.requestPost();
        System.out.println("done...");
    }

    private int requestPost() throws Exception {
        try {
            URL url = new URL("http://imgur.com/search?q=test");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url.toURI());
            postRequest.addHeader("Authorization", "Client-ID " + "a2686b09bad8534");

            postRequest.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            List<NameValuePair> postParams = new ArrayList<NameValuePair>();

            // postParams.add(new BasicNameValuePair("BUSINESSCODE", "BA"));
            // postParams.add(new BasicNameValuePair("TEMPLATEVAR", ""));

            postRequest.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Runtime Failed HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF8"));

            String output = null;
            while ((output = br.readLine()) != null) {
                // System.out.println(output);

                if (output.contains("//i.imgur.com/")) {
                    System.out.println("--------------------------------------------------" + output);
                }
            }

            httpClient.getConnectionManager().shutdown();

        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
}
