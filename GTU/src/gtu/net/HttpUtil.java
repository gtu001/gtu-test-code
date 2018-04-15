package gtu.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class HttpUtil {

    public static void main(String[] args) throws IOException {
        String default_user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
        String content = HttpUtil.doGetRequest_UserAgent("https://medium.com/@johnnemann/where-the-water-tastes-like-wine-postmortem-211a1f9d791a", "UTF-8", "");
        System.out.println(content);
        System.out.println("done..v2");
    }

    public static String doGetRequest_UserAgent(String url, String encoding, String userAgent) {
        try {
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            URI uri = new URI(url);

            CookieStore cookieStore = new BasicCookieStore();
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            if (userAgent != null && userAgent.length() > 0) {
                httpget.setHeader("User-Agent", userAgent);
            }

            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                InputStream instream = entity.getContent();
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(instream, encoding));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    instream.close();
                }
                String result = writer.toString();
                return result;
            } else {
                String errMsg = "StatusCode : " + response.getStatusLine().getStatusCode() + ", " + response.getStatusLine().getReasonPhrase();
                throw new RuntimeException(errMsg);
            }
        } catch (Exception ex) {
            throw new RuntimeException("doGetRequest_UserAgent Err : " + ex.getMessage(), ex);
        }
    }

    final static int MAX_SIZE = Integer.MAX_VALUE;

    static {
        try {
            ignoreVerifyHttpsHostName();
            ignoreVerifyHttpsTrustManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String doGetRequest(String urlStr) throws IOException {
        return doGetRequest(urlStr, "UTF-8");
    }

    public static String doGetRequest(String urlStr, String encode) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
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
            conn.setDoInput(true);

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= MAX_SIZE) {
                    break;
                }
            }

            return response.toString();

        } finally {
            safeClose(is, null);
        }

    }

    public static String doPostRequest(String urlStr, String postData) throws IOException {
        return doPostRequest(urlStr, postData, "UTF-8", "normal");
    }

    public static String doPostRequest(String urlStr, JSONObject postData) throws IOException {
        return doPostRequest(urlStr, postData.toString(), "UTF-8", "json");
    }

    public static String doPostRequest(String urlStr, String postData, String encode, String type) throws IOException {
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
            if (type == "json") {
                conn.setRequestProperty("Content-Type", "application/json");
            }
            if (conn == null)
                return "";
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            os = conn.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");
            wr.write(postData);
            wr.flush();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= MAX_SIZE) {
                    break;
                }
            }
            return response.toString();

        } finally {
            safeClose(is, os);
        }

    }

    public static String doSoapPostRequest(String urlStr, String postData) throws IOException {
        return doSoapPostRequest(urlStr, postData, "UTF-8");
    }

    public static String doSoapPostRequest(String urlStr, String postData, String encode) throws IOException {
        StringBuffer response = new StringBuffer();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader isr = null;
        OutputStream os = null;
        char[] buff = new char[4096];
        int size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null)
                return "";
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction", "http://billhunter.com.tw/SendMail_Now");
            conn.setRequestProperty("Content-Length", "" + postData.getBytes().length);

            os = conn.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os);
            wr.write(postData);
            wr.flush();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= MAX_SIZE) {
                    break;
                }
            }
            return response.toString();

        } finally {
            safeClose(is, os);
        }

    }

    public static String doSoapSSLPostRequest(String urlStr, String postData) throws IOException {
        return doSoapSSLPostRequest(urlStr, postData, "UTF-8");
    }

    public static String doSoapSSLPostRequest(String urlStr, String postData, String encode) throws IOException {
        StringBuffer response = new StringBuffer();
        URL url = null;
        HttpsURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        InputStreamReader isr = null;

        char[] buff = new char[4096];
        int size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpsURLConnection) url.openConnection();
            if (conn == null)
                return "";
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction", "http://billhunter.com.tw/SendMail_Now");
            conn.setRequestProperty("Content-Length", "" + postData.getBytes().length);

            os = conn.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os);
            wr.write(postData);
            wr.flush();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= MAX_SIZE) {
                    break;
                }
            }
            return response.toString();

        } finally {
            safeClose(is, os);
        }
    }

    private static void safeClose(InputStream is, OutputStream os) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }

    public static void ignoreVerifyHttpsTrustManager() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public static void ignoreVerifyHttpsHostName() {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    public static JSONObject parseParameter(HttpServletRequest req) {
        return parseParameter(req, null);
    }

    public static JSONObject parseParameter(HttpServletRequest req, String[] rule) {
        Map<String, String> rulemap = parseRule(rule);
        JSONObject ret = JSONObject.fromObject(req.getParameterMap(), new ParameterJsonConfig(rulemap));
        ret.put("X-Real-IP", req.getHeader("X-Real-IP"));
        ret.put("X-Forwarded-For", req.getHeader("X-Forwarded-For"));
        ret.put("RemoteAddr", req.getRemoteAddr());
        ret.put("User-Agent", req.getHeader("User-Agent"));
        return ret;
    }

    private static Map<String, String> parseRule(String[] rule) {
        Map<String, String> map = new HashMap<String, String>();
        if (rule != null && rule.length > 1) {
            for (int i = 0; i < rule.length; i += 2) {
                map.put(rule[i], rule[i + 1]);
            }
        }
        return map;
    }

    static class ParameterJsonConfig extends JsonConfig {

        public ParameterJsonConfig(final Map<String, String> rulemap) {
            registerJsonValueProcessor(String[].class, new JsonValueProcessor() {

                @Override
                public Object processObjectValue(String key, Object val, JsonConfig cfg) {
                    if (val instanceof String[]) {
                        String[] ary = (String[]) val;
                        if (ary.length > 1) {
                            return JSONArray.fromObject(ary);
                        } else if (ary.length == 1) {
                            String regular = rulemap.get(key);
                            if (regular != null && regular.length() > 0) {
                                if (ary[0].matches(regular)) {
                                    return ary[0];
                                } else {
                                    throw new IllegalArgumentException("key=" + key + " param=" + ary[0] + " regular=" + regular);
                                }
                            }
                            return ary[0];
                        } else {
                            return "";
                        }
                    } else {
                        return JSONArray.fromObject(val);
                    }
                }

                @Override
                public Object processArrayValue(Object val, JsonConfig cfg) {
                    return null;
                }
            });

        }

    }

}
