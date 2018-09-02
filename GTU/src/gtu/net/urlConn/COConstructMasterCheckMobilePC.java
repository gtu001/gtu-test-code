package gtu.net.urlConn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;

import sun.misc.BASE64Encoder;

/**
 * 若是Home Booster的RF Report Location為Private 或 Pico Home Booster則需檢查是否遠傳門號 透過網頁,
 * 回傳門號有效性:
 * http://sp.istyle.com.tw/sdk/servlet/com.fet.csp.servlet.GetFETUserProfile
 * ?CSPSDKXML= <CSPSDKInput> <serviceID>CSSYS00029</serviceID>
 * <servicePassword>fh3e67s1</servicePassword> <MSISDN>0955990509</MSISDN>
 * </CSPSDKInput>
 * 
 * @author Bengo
 * 
 */
public class COConstructMasterCheckMobilePC {
    private static final long serialVersionUID = 1L;

    public final static String URL_STR = "http://sp.istyle.com.tw/sdk/servlet/com.fet.csp.servlet.GetFETUserProfile";

    public static class DefaultAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("fareastone\\v_achen", "50445044".toCharArray());
        }
    }

    public static void main(String[] args) throws Exception {
        COConstructMasterCheckMobilePC test = new COConstructMasterCheckMobilePC();
        test.checkFETMobile("0954006788");
        System.out.println("done...");
    }

    public boolean checkFETMobile(String phoneNum) throws Exception {
        StringBuffer reqStr = new StringBuffer();
        OutputStreamWriter wr = null;
        BufferedReader rd = null;

        Authenticator.setDefault(new DefaultAuthenticator());

        // proxy
        HttpURLConnection conn;
        InetSocketAddress ISA = new java.net.InetSocketAddress("fetfw.fareastone.com.tw", 8080);
        Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, ISA);

        try {
            reqStr.append(URLEncoder.encode("CSPSDKXML", "UTF-8"));
            reqStr.append("=");
            reqStr.append(URLEncoder.encode("<CSPSDKInput>", "UTF-8"));
            reqStr.append(URLEncoder.encode("<serviceID>", "UTF-8"));
            reqStr.append(URLEncoder.encode("CSSYS00029", "UTF-8"));
            reqStr.append(URLEncoder.encode("</serviceID>", "UTF-8"));
            reqStr.append(URLEncoder.encode("<servicePassword>", "UTF-8"));
            reqStr.append(URLEncoder.encode("fh3e67s1", "UTF-8"));
            reqStr.append(URLEncoder.encode("</servicePassword>", "UTF-8"));
            reqStr.append(URLEncoder.encode("<MSISDN>", "UTF-8"));
            reqStr.append(URLEncoder.encode(phoneNum, "UTF-8"));
            reqStr.append(URLEncoder.encode("</MSISDN>", "UTF-8"));
            reqStr.append(URLEncoder.encode("</CSPSDKInput>", "UTF-8"));

            URL url = new URL(URL_STR);
            conn = (HttpURLConnection) url.openConnection(proxy);
            String userPassword = "fareastone\\v_achen" + ":" + "50445044";
            String encodedUserPassword = new BASE64Encoder().encode(userPassword.getBytes());
            conn.setRequestProperty("Proxy-Authorization", "NTLM " + encodedUserPassword);
            conn.setFollowRedirects(true);

            // try request header
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Host", "sp.istyle.com.tw");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-TW; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-tw,en-us;q=0.7,en;q=0.3");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("Accept-Charset", "Big5,utf-8;q=0.7,*;q=0.7");
            conn.setRequestProperty("Keep-Alive", "300");
            conn.setRequestProperty("Proxy-Connection", "keep-alive");
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);

            wr = new OutputStreamWriter(conn.getOutputStream());
            System.out.println("request = " + reqStr.toString());
            wr.write(reqStr.toString());
            wr.flush();
            System.out.println(conn.getResponseCode());

            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            if ((line = rd.readLine()) != null) {
                System.out.println(line);
                return line.contains("Success");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (null != wr)
                wr.close();
            if (null != rd)
                rd.close();
        }
        return false;
    }

}
