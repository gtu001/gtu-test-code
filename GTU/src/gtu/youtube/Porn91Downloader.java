package gtu.youtube;

import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.log.LogbackUtil;
import gtu.log.line.SystemZ;
import gtu.swing.util.JCommonUtil;
import gtu.youtube.PornVideoUrlDetection.SingleVideoUrlConfig;

public class Porn91Downloader {

    private static final DecimalFormat commaFormatNoPrecision = new DecimalFormat("###,###");
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    /**
     * 下載進度調處理
     */
    private ActionListener progressPerformd;

    public void setProgressPerformd(ActionListener progressPerformd) {
        this.progressPerformd = progressPerformd;
    }

    public static void main(String[] args) throws Throwable {
        Porn91Downloader p = new Porn91Downloader();
        String url = "https://xvideos-im-24e6ee00-31316667-mp4.s.loris.llnwd.net/videos/mp4/4/2/d/xvideos.com_42d3bf243bc1136883a43d1ddcf19676.mp4?e=1524945829&ri=1024&rs=85&h=3258734aeecfc622cfa30c9c994aefb4";
        File testMp4 = new File(FileUtil.DESKTOP_DIR, "xxxxxx.mp4");
        long length = p.getContentLength(DEFAULT_USER_AGENT, url);
        SystemZ.out.println("length = " + length);
        SystemZ.out.println("done...");
    }

    public static void main_XXXXXX2(String[] args) throws Throwable {
        Porn91Downloader p = new Porn91Downloader();
        // List<VideoUrlConfig> videoLst =
        // p.processVideoLst("https://www.facebook.com/JKFR2.0/videos/434056733674860/");
        // List<VideoUrlConfig> videoLst =
        // p.processVideoLst("https://www.facebook.com/JKFLADY/videos/844465589081974/");
        // 每週一到每週六8:15-9:15有女郎直播

        String url = JCommonUtil._jOptionPane_showInputDialog("請輸入facebook網址:");
        String content = "";
        boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("使用記事本coockie", "");
        if (result) {
            content = ClipboardUtil.getInstance().getContents();
        }

        String headerContent = "";
        boolean result2 = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("使用記事本header", "");
        if (result2) {
            headerContent = ClipboardUtil.getInstance().getContents();
        }

        List<VideoUrlConfig> videoLst = p.processVideoLst(url, content, headerContent);

        StringBuilder msg = new StringBuilder();
        for (int ii = 0; ii < videoLst.size(); ii++) {
            VideoUrlConfig v = videoLst.get(ii);
            SystemZ.out.println("[" + ii + "] " + v);
            msg.append("[" + ii + "] " + v + "\n");
        }

        int index = Integer.parseInt(JCommonUtil._jOptionPane_showInputDialog("請輸入index : \n" + msg, "-1"));

        VideoUrlConfig video = videoLst.get(index);
        p.processDownload(video, null, null);
        SystemZ.out.println("done...v3");
    }

    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.INFO);
    }

    public List<VideoUrlConfig> processVideoLst(String videoUrl) {
        PornVideoUrlDetection p2 = new PornVideoUrlDetection(videoUrl);
        List<SingleVideoUrlConfig> videoOrignLst = p2.processMain();
        String title = this.getClass().getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
        List<VideoUrlConfig> videoLst = this.getVideoList(title, videoOrignLst);
        return videoLst;
    }

    public List<VideoUrlConfig> processVideoLst(String url, String cookieContent, String headerContent) {
        try {
            long detectSize = getContentLength(DEFAULT_USER_AGENT, url);
            final float MAX_SIZE = 1.5f * 1024 * 1024; // 超過1.5MB
            if (MAX_SIZE < detectSize) {
                SystemZ.out.println("偵測網頁大小太大 : Max : " + MAX_SIZE + " , current len : " + detectSize);
                return new ArrayList<VideoUrlConfig>();
            }
        } catch (Throwable e) {
        }

        String content = getVideoInfo(URI.create(url), "", cookieContent, headerContent);
        String title = getTitleForFileName(content);

        PornVideoUrlDetection p2 = new PornVideoUrlDetection(content);
        List<SingleVideoUrlConfig> videoOrignLst = p2.processMain();

        List<VideoUrlConfig> videoLst = this.getVideoList(title, videoOrignLst);
        content = String.format("<!-- Detect URL :%s -->\n\n", url) + content;
        this.debugSaveHtml(title, content, videoLst.isEmpty());
        return videoLst;
    }

    public void processDownload(VideoUrlConfig v, File destDir, Integer percentScale) throws Throwable {
        String prefix = StringUtils.isNotBlank(v.title) ? v.title + "_" : "";
        String filename = prefix + v.fileName;
        if (destDir == null) {
            destDir = FileUtil.DESKTOP_DIR;
        }
        File saveVideoFile = new File(destDir, filename);
        downloadWithHttpClient(DEFAULT_USER_AGENT, v.url, saveVideoFile, percentScale);
    }

    private void debugSaveHtml(String title, String content, boolean doSave) {
        if (!doSave) {
            return;
        }
        if (StringUtils.isBlank(title)) {
            title = "FacebookVideoErrorHtml";
        }
        String tempFileName = title + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File saveFile = new File(FileUtil.DESKTOP_PATH, tempFileName + ".txt");
        FileUtil.saveToFile(saveFile, content, "UTF-8");
    }

    private List<VideoUrlConfig> getVideoList(String title, List<PornVideoUrlDetection.SingleVideoUrlConfig> orignLst) {
        List<VideoUrlConfig> videoLst = new ArrayList<VideoUrlConfig>();

        for (SingleVideoUrlConfig from : orignLst) {
            VideoUrlConfig conf = new VideoUrlConfig(from);
            conf.title = title;
            try {
                conf.length = this.getContentLength(DEFAULT_USER_AGENT, conf.url);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if (!PornVideoUrlDetection.isVideo(conf.fileName)) {
                SystemZ.out.println(conf.getFileName() + " is not video >> ignored!");
                continue;
            }

            if (conf.length <= 0) {
                SystemZ.out.println(conf.getFileName() + " size 0 >>  ignored!");
                continue;
            }

            videoLst.add(conf);
        }

        SystemZ.out.println("video size = " + videoLst.size());
        return videoLst;
    }

    public static class VideoUrlConfig {

        private PornVideoUrlDetection.SingleVideoUrlConfig orignConfig;

        public VideoUrlConfig(PornVideoUrlDetection.SingleVideoUrlConfig orignConfig) {
            this.orignConfig = orignConfig;
            this.fileName = this.orignConfig.finalFileName;
            this.url = this.orignConfig.orignUrl;
        }

        String fileName;
        String title;
        String url;
        long length = -1;

        public String getFileName() {
            String prefix = StringUtils.isNotBlank(title) ? title + "_" : "";
            String renameFileName = prefix + fileName;
            return renameFileName;
        }

        public String getFizeSize() {
            return FileUtil.getSizeDescription(length);
        }

        public String toString() {
            return getFileName() + " / " + getFizeSize();
        }

        public PornVideoUrlDetection.SingleVideoUrlConfig getOrignConfig() {
            return orignConfig;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public long getLength() {
            return length;
        }
    }

    private URI getURI() throws URISyntaxException {
        List<NameValuePair> infoMap = new ArrayList<NameValuePair>();
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("viewkey", "f0a28c86986015409496"));

        String scheme = "http";
        String host = "91porn.com";
        String path = "view_video.php";
        URI uri = URIUtils.createURI(scheme, host, -1, "/" + path, URLEncodedUtils.format(qparams, DEFAULT_ENCODING), null);
        return uri;
    }

    /**
     * 多行Cookie key \t value
     */
    private BasicCookieStore getCookieString(String cookieStr) {
        BasicCookieStore cookstore = new BasicCookieStore();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(cookieStr));
            for (String line = null; (line = reader.readLine()) != null;) {
                String[] arry = line.split("\t", -1);
                if (arry != null && arry.length == 2) {
                    arry[0] = StringUtils.trimToEmpty(arry[0]);
                    arry[1] = StringUtils.trimToEmpty(arry[1]);
                    cookstore.addCookie(new BasicClientCookie(arry[0], arry[1]));
                    SystemZ.out.println("cookie : " + Arrays.toString(arry));
                }
            }
            return cookstore;
        } catch (Exception ex) {
            throw new RuntimeException(" Err : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    // 地一行先key在value
    private void appendRequestHeader(HttpGet httpget, String headerContent) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(headerContent));
            String tempKey = null;
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.trimToEmpty(line);
                if (StringUtils.isNotBlank(line)) {
                    if (tempKey == null) {
                        tempKey = line;
                    } else {
                        String key = StringUtils.trimToEmpty(tempKey);
                        String value = StringUtils.trimToEmpty(line);
                        SystemZ.out.format("appendHeader k:[%s] \t v:[%s]\n", key, value);
                        httpget.setHeader(key, value);
                        tempKey = null;
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(" Err : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public String getVideoInfo(URI uri, String userAgent, String cookieContent, String headerContent) {
        try {
            if (StringUtils.isBlank(userAgent)) {
                userAgent = DEFAULT_USER_AGENT;
            }

            BasicCookieStore cookstore = new BasicCookieStore();
            CookieStore cookieStore = cookstore;

            // 加入cookie
            if (StringUtils.isNotBlank(cookieContent)) {
                cookieStore = getCookieString(cookieContent);
            }

            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            if (userAgent != null && userAgent.length() > 0) {
                httpget.setHeader("User-Agent", userAgent);
            }

            // 加入黨頭
            if (StringUtils.isNotBlank(headerContent)) {
                this.appendRequestHeader(httpget, headerContent);
            }

            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                InputStream instream = entity.getContent();
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(instream, DEFAULT_ENCODING));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    instream.close();
                }
                String result = writer.toString();
                return result;
            }
            throw new RuntimeException("Error StatusCode : " + response.getStatusLine().getStatusCode());
        } catch (Exception ex) {
            throw new RuntimeException("Error : " + uri + " , " + ex.getMessage(), ex);
        }
    }

    public static long getContentLength(String userAgent, String downloadUrl) throws Throwable {
        SystemZ.out.println("getContentLength URL : " + downloadUrl);
        HttpGet httpget2 = new HttpGet(downloadUrl);
        if (userAgent != null && userAgent.length() > 0) {
            httpget2.setHeader("User-Agent", userAgent);
        }
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
        HttpClient httpclient2 = new DefaultHttpClient(httpParams);
        HttpResponse response2 = httpclient2.execute(httpget2);
        HttpEntity entity2 = response2.getEntity();
        if (entity2 != null && response2.getStatusLine().getStatusCode() == 200) {
            long length = entity2.getContentLength();
            SystemZ.out.println("length : " + length);
            return length;
        }
        SystemZ.out.println("length : Fail ");
        return -1;
    }

    private HttpRequestRetryHandler customRetryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= 99) {
                return false;
            }
            return true;
        }
    };

    private void downloadWithHttpClient(String userAgent, String downloadUrl, File outputfile, Integer percentScale) throws Throwable {
        SystemZ.out.println("Download URL : " + downloadUrl);
        HttpGet httpget2 = new HttpGet(downloadUrl);
        if (userAgent != null && userAgent.length() > 0) {
            httpget2.setHeader("User-Agent", userAgent);
        }

        // set timeout with 5 seconds.
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5 * 1000).build();

        // use custom retry handler to make retry handler effect.
        // HttpClient httpclient2 = new DefaultHttpClient(); //原始邏輯
        CloseableHttpClient httpclient2 = HttpClients.custom().setRetryHandler(customRetryHandler).setDefaultRequestConfig(config).build();

        // Execute request and get response.
        HttpResponse response2 = httpclient2.execute(httpget2);
        HttpEntity entity2 = response2.getEntity();
        if (entity2 != null && response2.getStatusLine().getStatusCode() == 200) {
            long length = entity2.getContentLength();
            if (length <= 0) {
                // Unexpected, but do not divide by zero
                length = 1;
            }
            InputStream instream2 = entity2.getContent();
            SystemZ.out.println("Writing " + commaFormatNoPrecision.format(length) + " bytes to " + outputfile);
            if (outputfile.exists()) {
                outputfile.delete();
            }
            BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(outputfile));
            SystemZ.out.println("outputfile " + outputfile);

            DownloadProgressHandler downloadHandler = new DownloadProgressHandler(length, instream2, outstream, percentScale);
            if (progressPerformd != null) {
                downloadHandler.setProgressPerformd(progressPerformd);
            }
            downloadHandler.start();

            SystemZ.out.println("Done...");
        } else {
            SystemZ.out.println("Err : " + response2.getStatusLine().getStatusCode());
        }
    }

    private String getTitleForFileName(String content) {
        String title = "";
        try {
            SystemZ.out.println("-----------------------------------------------------------------------");
            // SystemZ.out.println(content);
            Pattern ptn = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(content);
            if (mth.find()) {
                title = mth.group(1);
                title = StringEscapeUtils.unescapeHtml(title);
                title = FileUtil.escapeFilename(title, true);
                SystemZ.out.println(title);
            }
            SystemZ.out.println("-----------------------------------------------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return title;
    }
}
