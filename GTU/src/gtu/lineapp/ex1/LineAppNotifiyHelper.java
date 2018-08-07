package gtu.lineapp.ex1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.commons.lang.Validate;

/**
 * https://notify-bot.line.me/my/
 * https://poychang.github.io/line-notify-1-basic/
 * 
 * @author gtu001
 *
 */
public class LineAppNotifiyHelper {

    final static int MAX_SIZE = Integer.MAX_VALUE;

    private static final String URL = "https://notify-api.line.me/api/notify";

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    public static void main(String[] args) {
        String lineToken = "qVxcCOM9qUmxVUwUKxmd320JA3a6fe6PPhafqxUou2R";

        _StickerMessasgeBuilder m1 = new _StickerMessasgeBuilder();
        m1.setMessage("純裕測試!!");
        m1.setStickerId("1");
        m1.setStickerPackageId("1");

        String result = LineAppNotifiyHelper.newInstance().lineToken(lineToken).message(m1).send();
        System.out.println(result);
        System.out.println("done...v2");
    }

    private _BaseMessasgeBuilder message;
    private String lineToken;

    public LineAppNotifiyHelper message(_BaseMessasgeBuilder message) {
        this.message = message;
        return this;
    }

    public LineAppNotifiyHelper lineToken(String lineToken) {
        this.lineToken = lineToken;
        return this;
    }

    public String send() {
        try {
            Validate.notNull(message, "必須設定message!");
            Validate.notEmpty(lineToken, "必須設定lineToken");

            String postForm = BeanToHttpForm.newInstance(message).getParameterString();
            System.out.println("#postForm = " + postForm);

            String result = doPostRequest(URL, postForm, "UTF8", lineToken);
            System.out.println("#result = " + result);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException("send ERR : " + ex.getMessage(), ex);
        }
    }

    public static LineAppNotifiyHelper newInstance() {
        return new LineAppNotifiyHelper();
    }

    private LineAppNotifiyHelper() {
    }

    public static class _BaseMessasgeBuilder {
        private String message;// "測試一下"

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class _StickerMessasgeBuilder extends _BaseMessasgeBuilder {
        String stickerPackageId;// "1"
        String stickerId;// "2"

        public String getStickerPackageId() {
            return stickerPackageId;
        }

        public String getStickerId() {
            return stickerId;
        }

        public void setStickerPackageId(String stickerPackageId) {
            this.stickerPackageId = stickerPackageId;
        }

        public void setStickerId(String stickerId) {
            this.stickerId = stickerId;
        }
    }

    public static class _ImageMessasgeBuilder extends _BaseMessasgeBuilder {
        String imageThumbnail; // '預覽縮圖網址',
        String imageFullsize; // 圖片網址'

        public String getImageThumbnail() {
            return imageThumbnail;
        }

        public String getImageFullsize() {
            return imageFullsize;
        }

        public void setImageThumbnail(String imageThumbnail) {
            this.imageThumbnail = imageThumbnail;
        }

        public void setImageFullsize(String imageFullsize) {
            this.imageFullsize = imageFullsize;
        }
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
