package gtu._work.etc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SimpleHttpsUtil {

    private SimpleHttpsUtil() {
    }

    public static SimpleHttpsUtil newInstance() {
        return new SimpleHttpsUtil();
    }

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    public String queryPage(String urls) {
        StringBuffer sb = new StringBuffer();
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("11.22.33.44", 8080));
            // URLConnection uc = url.openConnection(proxy);
            // uc.setRequestProperty("User-agent", "IE/6.0");

            URL u = new URL(urls);

            // URLConnection url = u.openConnection();
            HttpURLConnection url = (HttpURLConnection) u.openConnection();

            url.setRequestProperty("User-agent", DEFAULT_USER_AGENT);

            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("_ga", "GA1.2.439902964.1521730335");
            map.put("_gid", "GA1.2.95311867.1525080045");
            map.put("PHPSESSID", "khotctt1pfa7mgvfbutsgs2bui");

            StringBuilder cookie = new StringBuilder();
            for (String key : map.keySet()) {
                cookie.append(key + "=" + map.get(key) + ";");
            }
            url.setRequestProperty("Cookie", cookie.toString());

            // 设置SSLSocketFoactory，这里有两种：1.需要安全证书 2.不需要安全证书；看官且往下看
            if (url instanceof HttpsURLConnection) { // 是Https请求
                System.out.println("Go Https..");
                SSLContext sslContext = getSLLContextNoCertificate();
                // 从上述SSLContext对象中得到SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                ((HttpsURLConnection) url).setSSLSocketFactory(ssf);
                ((HttpsURLConnection) url).setHostnameVerifier(hostnameVerifier);
            }

            // 设置属性
            url.setConnectTimeout(8 * 1000);
            url.setReadTimeout(8 * 1000);

            // url.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null; ) {
                sb.append(line + "\n");
            }

            // System.out.println("<<<" + sb.toString());
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return sb.toString();
    }

    /**
     * 無安全證書寫法
     */
    private static SSLContext getSLLContextNoCertificate() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static SSLContext getSLLContext_Normal() throws Exception {
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = {};//new MyX509TrustManager()
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        return sslContext;
    }
}
