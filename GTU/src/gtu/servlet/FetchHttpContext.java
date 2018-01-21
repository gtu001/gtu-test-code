package gtu.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gtu 2009/02/02
 * 
 */
public class FetchHttpContext {

    /**
     * 設定 Proxy 應該要試試看 URL.openConnection(Proxy) 的方式，不過 Proxy 看起來好像就不是要讓人寫來用的。
     * 
     * @param proxySet
     * @param proxyURL
     * @param proxyProt
     */
    public static void setProxy(boolean proxySet, String proxyURL, String proxyProt) {
        System.setProperty("proxySet", (proxySet ? "true" : "false"));
        System.setProperty("http.proxyHost", proxyURL);
        System.setProperty("http.proxyPort", proxyProt);
    }

    /**
     * 抓網頁成字串 抓(get)網址成字串
     * 
     * @param connection
     * @param encoding
     *            編碼
     * @return 網頁 html 碼
     */
    public static String urlToString(HttpURLConnection connection, String encoding) {
        StringBuffer result = new StringBuffer();
        String tmp;
        BufferedReader bis;
        try {
            bis = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            while ((tmp = bis.readLine()) != null) {
                result.append(tmp + "\n");
            }
            bis.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return result.toString();
    }

    /**
     * 把 http header 提供的資訊都變成字串
     * 
     * @param c
     * @return 字串
     */
    public static String getHeaderString(HttpURLConnection c) {
        Map map = c.getHeaderFields();
        StringBuffer sb = new StringBuffer();

        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String param = (String) iter.next();
            sb.append(param + "=" + map.get(param));
            sb.append("\n");
        }
        return sb.toString();
    }
}
