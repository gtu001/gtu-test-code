package com.example.gtuandroid.component;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class WebComponet {

    private static final int GOOD_STATUS = 200;

    public String getPostData(String url, List<NameValuePair> parms) throws Exception {
        String out = "";
        HttpClient client = new DefaultHttpClient();
        try {

            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.addAll(parms);

            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse responsePOST = client.execute(post);
            if (responsePOST.getStatusLine().getStatusCode() == GOOD_STATUS) {
                HttpEntity resEntity = responsePOST.getEntity();
                out = EntityUtils.toString(resEntity);
            } else {
                out = "";
            }

        } catch (Exception e) {

            out = "";
        } finally {
            client.getConnectionManager().shutdown();
        }
        return out;
    }

    public String getGetData(String url) throws Exception {
        String out = "";
        HttpClient client = new DefaultHttpClient();
        try {

            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity resEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == GOOD_STATUS) {
                out = EntityUtils.toString(resEntity);
            } else {
                out = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "";
            // throw e;
        } finally {
            client.getConnectionManager().shutdown();
        }
        return out;
    }

    private String testPost(String urlPath, String parms) throws Exception {
        String urlParameters = "user=caterpillar&passwd=123456";
        String request = "http://myappmap1987.appspot.com/login.html";
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        // connection.setRequestProperty("Content-Type",
        // "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
        connection.setUseCaches(false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        connection.disconnect();
        return "";
    }
}
