package gtu.youtube;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import gtu.file.FileUtil;
import gtu.log.LogbackUtil;
import gtu.swing.util.JCommonUtil;

public class Porn91Downloader {

    private static final DecimalFormat commaFormatNoPrecision = new DecimalFormat("###,###");
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
    private static final String FILE_EXTENSTION_PATTERN = "(mp4|avi|flv|rm|rmvb|mp3)";
    private static final boolean DEBUT_MODE = false;

    public static void main(String[] args) throws Throwable {
        Porn91Downloader p = new Porn91Downloader();
        // List<VideoUrlConfig> videoLst =
        // p.processVideoLst("https://www.facebook.com/JKFR2.0/videos/434056733674860/");
        // List<VideoUrlConfig> videoLst =
        // p.processVideoLst("https://www.facebook.com/JKFLADY/videos/844465589081974/");
        // 每週一到每週六8:15-9:15有女郎直播

        String url = JCommonUtil._jOptionPane_showInputDialog("請輸入facebook網址:");
        List<VideoUrlConfig> videoLst = p.processVideoLst(url);

        StringBuilder msg = new StringBuilder();
        for (int ii = 0; ii < videoLst.size(); ii++) {
            VideoUrlConfig v = videoLst.get(ii);
            System.out.println("[" + ii + "] " + v);
            msg.append("[" + ii + "] " + v + "\n");
        }

        int index = Integer.parseInt(JCommonUtil._jOptionPane_showInputDialog("請輸入index : \n" + msg, "-1"));

        VideoUrlConfig video = videoLst.get(index);
        p.processDownload(video);
        System.out.println("done...v3");
    }

    static {
        LogbackUtil.setRootLevel(ch.qos.logback.classic.Level.INFO);
    }

    public List<VideoUrlConfig> processVideoLst(String url) {
        String content = getVideoInfo(URI.create(url), "");
        String title = getTitleForFileName(content);
        this.debugSaveHtml(content, DEBUT_MODE);
        List<VideoUrlConfig> videoLst = this.getVideoList(title, content);
        return videoLst;
    }

    public void processDownload(VideoUrlConfig v) throws Throwable {
        String prefix = StringUtils.isNotBlank(v.title) ? v.title + "_" : "";
        String filename = prefix + v.fileName;
        File saveVideoFile = new File(FileUtil.DESKTOP_PATH, filename);
        downloadWithHttpClient(DEFAULT_USER_AGENT, v.url, saveVideoFile);
    }

    private void debugSaveHtml(String content, boolean doSave) {
        if (!doSave) {
            return;
        }
        String tempFileName = this.getClass().getSimpleName() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File saveFile = new File(FileUtil.DESKTOP_PATH, tempFileName + ".txt");
        FileUtil.saveToFile(saveFile, content, "UTF-8");
    }

    private List<VideoUrlConfig> getVideoList(String title, String content) {
        List<VideoUrlConfig> videoLst = new ArrayList<VideoUrlConfig>();
        Map<String, String> mp4Map = findVideoUrl(FILE_EXTENSTION_PATTERN, content);
        System.out.println("Mp4Map size = " + mp4Map.size());
        for (String v : mp4Map.keySet()) {
            System.out.println(v + "\t" + mp4Map.get(v));

            VideoUrlConfig conf = new VideoUrlConfig();
            conf.fileName = v;
            conf.url = mp4Map.get(v);
            conf.title = title;
            try {
                conf.length = this.getContentLength(DEFAULT_USER_AGENT, mp4Map.get(v));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            videoLst.add(conf);
        }
        return videoLst;
    }

    private class VideoUrlConfig {
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

    private Map<String, String> findVideoUrl(String fileExtenstion, String content) {
        Pattern urlPtn = Pattern.compile("\"(https?\\:.*?)\"", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        String urlDetailPtn = "https?\\:.*?\\." + fileExtenstion + "\\??.*?";
        Pattern filenamePtn = Pattern.compile("\\w+\\." + fileExtenstion, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher mth = urlPtn.matcher(content);
        Map<String, String> map = new LinkedHashMap<String, String>();
        while (mth.find()) {
            String url = mth.group(1);
            url = StringEscapeUtils.unescapeJava(url);
            if (!Pattern.matches(urlDetailPtn, url)) {
                // System.out.println("ignore -> " + url);
                continue;
            }
            String filename = "Unknow." + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSSSS") + "." + fileExtenstion;
            Matcher mth2 = filenamePtn.matcher(url);
            if (mth2.find()) {
                filename = mth2.group();
            }
            map.put(filename, url);
        }
        return map;
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
                    cookstore.addCookie(new BasicClientCookie(arry[0], arry[1]));
                    System.out.println("cookie : " + Arrays.toString(arry));
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

    public String getVideoInfo(URI uri, String userAgent) {
        try {
            if (StringUtils.isBlank(userAgent)) {
                userAgent = DEFAULT_USER_AGENT;
            }

            BasicCookieStore cookstore = new BasicCookieStore();
            cookstore.addCookie(new BasicClientCookie("_ga", "GA1.2.68028505.1524029678"));
            cookstore.addCookie(new BasicClientCookie("_gat", "1"));
            cookstore.addCookie(new BasicClientCookie("_gid", "GA1.2.1162949767.1524029678"));
            cookstore.addCookie(new BasicClientCookie("bs", "8kef3mp24n8xlye1fo4g43jz0fcgzkmw"));
            cookstore.addCookie(new BasicClientCookie("g36FastPopSessionRequestNumber", "2"));
            cookstore.addCookie(new BasicClientCookie("platform", "pc"));
            cookstore.addCookie(new BasicClientCookie("RNLBSERVERID", "ded6698"));
            cookstore.addCookie(new BasicClientCookie("ss", "150222930604599849"));

            CookieStore cookieStore = cookstore;
            // CookieStore cookieStore = getCookieFromClipborad();

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
            throw new RuntimeException("Error : " + ex.getMessage(), ex);
        }
    }

    private long getContentLength(String userAgent, String downloadUrl) throws Throwable {
        System.out.println("getContentLength URL : " + downloadUrl);
        HttpGet httpget2 = new HttpGet(downloadUrl);
        if (userAgent != null && userAgent.length() > 0) {
            httpget2.setHeader("User-Agent", userAgent);
        }
        HttpClient httpclient2 = new DefaultHttpClient();
        HttpResponse response2 = httpclient2.execute(httpget2);
        HttpEntity entity2 = response2.getEntity();
        if (entity2 != null && response2.getStatusLine().getStatusCode() == 200) {
            long length = entity2.getContentLength();
            return length;
        }
        return -1;
    }

    private void downloadWithHttpClient(String userAgent, String downloadUrl, File outputfile) throws Throwable {
        System.out.println("Download URL : " + downloadUrl);
        HttpGet httpget2 = new HttpGet(downloadUrl);
        if (userAgent != null && userAgent.length() > 0) {
            httpget2.setHeader("User-Agent", userAgent);
        }

        HttpClient httpclient2 = new DefaultHttpClient();
        HttpResponse response2 = httpclient2.execute(httpget2);
        HttpEntity entity2 = response2.getEntity();
        if (entity2 != null && response2.getStatusLine().getStatusCode() == 200) {
            long length = entity2.getContentLength();
            if (length <= 0) {
                // Unexpected, but do not divide by zero
                length = 1;
            }
            InputStream instream2 = entity2.getContent();
            System.out.println("Writing " + commaFormatNoPrecision.format(length) + " bytes to " + outputfile);
            if (outputfile.exists()) {
                outputfile.delete();
            }
            BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(outputfile));
            System.out.println("outputfile " + outputfile);

            new DownloadProgressHandler(length, instream2, outstream, null);
            System.out.println("Done");
        }
    }

    private String getTitleForFileName(String content) {
        String title = "";
        try {
            System.out.println("-----------------------------------------------------------------------");
            // System.out.println(content);
            Pattern ptn = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(content);
            if (mth.find()) {
                title = mth.group(1);
                title = StringEscapeUtils.unescapeHtml(title);
                title = FileUtil.escapeFilename(title);
                System.out.println(title);
            }
            System.out.println("-----------------------------------------------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return title;
    }
}
