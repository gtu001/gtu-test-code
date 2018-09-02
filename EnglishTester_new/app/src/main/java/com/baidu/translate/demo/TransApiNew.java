package com.baidu.translate.demo;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by wistronits on 2018/7/17.
 */

public class TransApiNew {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private String appid;
    private String securityKey;

    public TransApiNew(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public String getTransResult(String query, String from, String to) throws UnsupportedEncodingException {
        Map params = this.buildParams(query, from, to);
        return HttpPost.post("http://api.fanyi.baidu.com/api/trans/vip/translate", params);
    }

    private Map<String, String> buildParams(String query, String from, String to) throws UnsupportedEncodingException {
        HashMap params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", this.appid);
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        String src = this.appid + query + salt + this.securityKey;
        params.put("sign", MD5.md5(src));
        return params;
    }

    private static class HttpPost {
        protected static final int SOCKET_TIMEOUT = 10000;
        protected static final String GET = "GET";
        private static TrustManager myX509TrustManager = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };

        private static String post(String host, Map<String, String> params) {
            try {
                SSLContext e = SSLContext.getInstance("TLS");
                e.init((KeyManager[]) null, new TrustManager[]{myX509TrustManager}, (SecureRandom) null);
                String postContent = getUrlWithQueryString(params);

                URL uri = new URL(host);
                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                if (conn instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) conn).setSSLSocketFactory(e.getSocketFactory());
                }

                conn.setReadTimeout(10000);
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");
                wr.write(postContent);
                wr.flush();

                int statusCode = conn.getResponseCode();
                if (statusCode != 200) {
                    System.out.println("Http错误码：" + statusCode);
                }

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder builder = new StringBuilder();
                String line = null;

                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }

                String text = builder.toString();
                close(br);
                close(is);
                conn.disconnect();
                return text;
            } catch (MalformedURLException var12) {
                var12.printStackTrace();
            } catch (IOException var13) {
                var13.printStackTrace();
            } catch (KeyManagementException var14) {
                var14.printStackTrace();
            } catch (NoSuchAlgorithmException var15) {
                var15.printStackTrace();
            }
            return null;
        }

        public static String getUrlWithQueryString(Map<String, String> params) {
            if (params == null || params.isEmpty()) {
                return "";
            } else {
                StringBuilder builder = new StringBuilder();

                int i = 0;
                Iterator var5 = params.keySet().iterator();

                while (var5.hasNext()) {
                    String key = (String) var5.next();
                    String value = (String) params.get(key);
                    if (value != null) {
                        if (i != 0) {
                            builder.append('&');
                        }

                        builder.append(key);
                        builder.append('=');
                        builder.append(encode(value));
                        ++i;
                    }
                }
                return builder.toString();
            }
        }

        public static String encode(String input) {
            if (input == null) {
                return "";
            } else {
                try {
                    return URLEncoder.encode(input, "utf-8");
                } catch (UnsupportedEncodingException var2) {
                    var2.printStackTrace();
                    return input;
                }
            }
        }

        protected static void close(Closeable closeable) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        }
    }
}
