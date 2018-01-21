package gtu.net.urlConn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.testng.log4testng.Logger;

public class UrlConnectionUtil {

    private Logger logger = Logger.getLogger(getClass());

    public boolean doPost(String sURL, String data, String cookie, String referer, String charset) {
        boolean doSuccess = false;
        java.io.BufferedWriter wr = null;
        try {

            URL url = new URL(sURL);
            HttpURLConnection URLConn = (HttpURLConnection) url.openConnection();

            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            ((HttpURLConnection) URLConn).setRequestMethod("POST");
            URLConn.setUseCaches(false);
            URLConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(true);

            URLConn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) " + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
            URLConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            URLConn.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
            URLConn.setRequestProperty("Accept-Charse", "Big5,utf-8;q=0.7,*;q=0.7");
            if (cookie != null)
                URLConn.setRequestProperty("Cookie", cookie);
            if (referer != null)
                URLConn.setRequestProperty("Referer", referer);

            URLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            URLConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            java.io.DataOutputStream dos = new java.io.DataOutputStream(URLConn.getOutputStream());
            dos.writeBytes(data);

            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(URLConn.getInputStream(), charset));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

            rd.close();
        } catch (java.io.IOException e) {
            doSuccess = false;
            logger.error(e.getMessage(), e);
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (java.io.IOException ex) {
                    logger.info(ex);
                }
                wr = null;
            }
        }
        return doSuccess;
    }

    public boolean doGet(String sURL, String cookie, String referer, String charset) {
        boolean doSuccess = false;
        BufferedReader in = null;
        try {
            URL url = new URL(sURL);
            HttpURLConnection URLConn = (HttpURLConnection) url.openConnection();
            URLConn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) " + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
            URLConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            URLConn.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
            URLConn.setRequestProperty("Accept-Charse", "Big5,utf-8;q=0.7,*;q=0.7");

            if (cookie != null)
                URLConn.setRequestProperty("Cookie", cookie);
            if (referer != null)
                URLConn.setRequestProperty("Referer", referer);
            URLConn.setDoInput(true);
            URLConn.setDoOutput(true);
            URLConn.connect();
            URLConn.getOutputStream().flush();
            in = new BufferedReader(new InputStreamReader(URLConn.getInputStream(), charset));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            doSuccess = false;
            logger.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (java.io.IOException ex) {
                    logger.info(ex);
                }
                in = null;
            }
        }
        return doSuccess;
    }

    public boolean doPostSSL(String sURL, String data, String referer, String charset) {
        StringBuffer buff = null;
        String sessionid = null;
        buff = new StringBuffer();
        boolean doSuccess = true;
        java.io.BufferedWriter wr = null;
        try {

            URL url = new URL(sURL);
            // Proxy proxy = new Proxy(Proxy.Type.HTTP, new
            // InetSocketAddress("proxy.hinet.net", 80));
            // trustAllHosts();
            HttpsURLConnection URLConn = (HttpsURLConnection) url.openConnection();

            URLConn.setDoOutput(true);
            URLConn.setDoInput(true);
            URLConn.setRequestMethod("POST");
            URLConn.setUseCaches(false);
            // URLConn.setHostnameVerifier(DO_NOT_VERYFY);
            URLConn.setAllowUserInteraction(true);
            HttpsURLConnection.setFollowRedirects(true);
            // URLConn.setInstanceFollowRedirects(true);
            URLConn.setInstanceFollowRedirects(false);

            URLConn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
            URLConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            URLConn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            URLConn.setRequestProperty("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4");
            // URLConn.setRequestProperty("Accept-Charse",
            // "Big5,utf-8;q=0.7,*;q=0.7");
            if (sessionid != null)
                URLConn.setRequestProperty("Cookie", sessionid);
            if (referer != null)
                URLConn.setRequestProperty("Referer", referer);

            URLConn.setReadTimeout(30000);

            URLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            URLConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            java.io.DataOutputStream dos = new java.io.DataOutputStream(URLConn.getOutputStream());
            dos.writeBytes(data);

            InputStream ins = null;
            // System.out.println("111111111111111\r\n"+URLConn.getContentEncoding()+"\r\n------------------------");
            if (URLConn.getContentEncoding() != null && !URLConn.getContentEncoding().equals("")) {
                String encode = URLConn.getContentEncoding().toLowerCase();
                if (encode != null && !encode.equals("") && encode.indexOf("gzip") >= 0) {
                    ins = new GZIPInputStream(URLConn.getInputStream());
                }
            }
            if (ins == null)
                ins = URLConn.getInputStream();

            java.io.BufferedReader rd = new java.io.BufferedReader(new java.io.InputStreamReader(ins, charset));
            String line;
            while ((line = rd.readLine()) != null) {
                buff.append(line + "\r\n");
            }
            if (sessionid == null) {
                String key;
                if (URLConn != null) {
                    for (int i = 1; (key = URLConn.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            sessionid = URLConn.getHeaderField(key);
                            sessionid = sessionid.substring(0, sessionid.indexOf(";"));
                        }
                    }
                }
            }
            // System.out.println("------------------------------------"+sessionid);
            rd.close();
        } catch (java.io.IOException e) {
            doSuccess = false;
            logger.error(e.getMessage(), e);
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (java.io.IOException ex) {
                    logger.info(ex);
                    ex.printStackTrace();
                }
                wr = null;
            }
        }

        return doSuccess;
    }

    static {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
