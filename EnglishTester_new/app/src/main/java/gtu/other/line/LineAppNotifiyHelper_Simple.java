package gtu.other.line;

//import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * https://notify-bot.line.me/my/
 * https://poychang.github.io/line-notify-1-basic/
 *
 * @author gtu001
 */
public class LineAppNotifiyHelper_Simple {

    final static int MAX_SIZE = Integer.MAX_VALUE;

    private static final String URL = "https://notify-api.line.me/api/notify";

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    public static void main(String[] args) {
        String lineToken = "qVxcCOM9qUmxVUwUKxmd320JA3a6fe6PPhafqxUou2R";
        LineAppNotifiyHelper_Simple.getInstance().send(lineToken, "test");
        System.out.println("done...v2");
    }

    private static final LineAppNotifiyHelper_Simple _INST = new LineAppNotifiyHelper_Simple();

    public static LineAppNotifiyHelper_Simple getInstance() {
        return _INST;
    }

    public String send(String message) {
        return this.send("qVxcCOM9qUmxVUwUKxmd320JA3a6fe6PPhafqxUou2R", message);
    }

    public String send(final String lineToken, String message) {
        try {
            Validate.notNull(message, "必須設定message!");
            Validate.isTrue(StringUtils.isNotBlank(lineToken), "必須設定lineToken");

            final String postForm = "&message=" + URLEncoder.encode(message, "UTF8");
            final ArrayBlockingQueue<String> waitQue = new ArrayBlockingQueue<String>(1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = null;
                    try {
                        result = doPostRequest(URL, postForm, "UTF8", lineToken);
                    } catch (IOException e) {
                        e.printStackTrace();
                        result = "ERROR : " + e.getMessage();
                    }
                    System.out.println("#result = " + result);
                    waitQue.add(result);
                }
            }).start();

            return waitQue.take();
        } catch (Exception ex) {
            throw new RuntimeException("send ERR : " + ex.getMessage(), ex);
        }
    }

    public static LineAppNotifiyHelper_Simple newInstance() {
        return new LineAppNotifiyHelper_Simple();
    }

    private LineAppNotifiyHelper_Simple() {
    }

    public static void setHeaderToken(HttpURLConnection myURLConnection, String token) throws ProtocolException {
        myURLConnection.setRequestProperty("Authorization", "Bearer " + token);
        myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// or
        // "multipart/form-data"
        myURLConnection.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        System.out.println("Authorization = " + "Bearer " + token);
        myURLConnection.setRequestMethod("POST");
        myURLConnection.setUseCaches(false);
    }

    public static String doPostRequest(String urlStr, String postData, String encode, String lineToken) throws IOException {
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
            if (conn == null)
                return "";

            // 設定token
            setHeaderToken(conn, lineToken);

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            os = conn.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os, encode);
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
}
