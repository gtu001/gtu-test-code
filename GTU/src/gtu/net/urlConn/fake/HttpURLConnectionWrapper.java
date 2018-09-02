package gtu.net.urlConn.fake;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 补充
 * 
 * 如果用chrome浏览器可以按F12进入控制台
 * 
 * 选择Network 选择左边name就可以看到一些request的头信息了
 * 
 * 把这些头信息在代码里加入即可
 */
public class HttpURLConnectionWrapper extends HttpURLConnection {

    HttpURLConnection httpURLConnection;
    // 简单的CookieManager
    CookieManager cookieManager = CookieManager.getInstance();

    public HttpURLConnectionWrapper(URL u) throws IOException {
        super(u);
        httpURLConnection = (HttpURLConnection) u.openConnection();
        setFollowRedirects(false);
        fillRequestHeadField();
    }

    /**
     * 填充Request Header信息
     */
    private void fillRequestHeadField() {
//        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");
//        httpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        httpURLConnection.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
//        httpURLConnection.setRequestProperty("Accept-Encoding", "GB2312,utf-8;q=0.7,*;q=0.7");
//        httpURLConnection.setRequestProperty("Referer", "http://movie.douban.com/");
//        httpURLConnection.setRequestProperty("Cache-Control", "max-age=0");
//        httpURLConnection.setRequestProperty("Cookie", cookieManager.getCookies(url.getHost()));
        
        httpURLConnection.setRequestProperty("Host", "www.youdao.com");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
        httpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        httpURLConnection.setRequestProperty("Referer", "https://www.youdao.com/w/eng/disentangle/");
        httpURLConnection.setRequestProperty("Cookie", cookieManager.getCookies(url.getHost()));
        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        httpURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
    }

    public InputStream getInputStream_Fix() throws IOException {
        InputStream is = httpURLConnection.getInputStream();
        // 取到输入流中后处理Cookie信息
        resolveCookies();
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode != 200 && responseCode != 404) {
            // 清除cookie并重新发请求
            CookieManager.getInstance().removeCookies(url.getHost());
            try {
                httpURLConnection.disconnect();
                is.close();
            } catch (Exception e) {
            }
            httpURLConnection = (HttpURLConnection) this.getURL().openConnection();
            setFollowRedirects(false);
            fillRequestHeadField();
            is = httpURLConnection.getInputStream();
        }
        return is;
    }

    private void resolveCookies() {
        List<String> setCookies = getHeaderFields().get("Set-Cookie");
        if (setCookies != null && !setCookies.isEmpty()) {
            for (String setCookie : setCookies) {
                cookieManager.setCookies(this.url.getHost(), setCookie);
            }
        }
    }

    @Override
    public void disconnect() {
        httpURLConnection.disconnect();
    }

    @Override
    public boolean usingProxy() {
        return httpURLConnection.usingProxy();
    }

    @Override
    public void connect() throws IOException {
        httpURLConnection.connect();
    }
}