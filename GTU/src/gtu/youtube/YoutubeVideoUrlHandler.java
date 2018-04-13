package gtu.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class YoutubeVideoUrlHandler {
    private static final String scheme = "http";
    private static final String host = "www.youtube.com";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";

    public String getUrl(int format) {
        for (int ii = 0; ii < typeLst.size(); ii++) {
            DataFinal d = typeLst.get(ii);
            if (Integer.parseInt(d.itag.paramStr) == format) {
                return d.url.paramStr;
            }
        }
        throw new RuntimeException("找步道對應 format :" + format);
    }

    List<DataFinal> typeLst = new ArrayList<DataFinal>();

    public YoutubeVideoUrlHandler(String videoId, String format, String userAgent) {
        try {
            List<NameValuePair> infoMap = this.getVideoInfo(videoId, format, userAgent);

            String orignData = null;
            for (NameValuePair pair : infoMap) {
                String key = pair.getName();
                String val = pair.getValue();
                if ("url_encoded_fmt_stream_map".equals(key)) {
                    orignData = val;
                    break;
                }
            }

            String baseUrlString = URLDecoder.decode(orignData, DEFAULT_ENCODING);

            System.out.println("baseUrlString = " + baseUrlString);

            typeLst = findDataGroup(baseUrlString);

            for (DataFinal d : typeLst) {
                System.out.println("YoutubeUrlHandler >> : " + d);
            }
        } catch (Exception e) {
            throw new RuntimeException(" YoutubeVideoUrlHandler Err : " + e.getMessage(), e);
        }
    }

    private List<DataFinal> findDataGroup(String baseUrlString) {
        List<DataFinal> rtnLst = new ArrayList<DataFinal>();
        List<DataConfig> lst = new ArrayList<DataConfig>();

        // parse Paramster
        for (Field f : DataFinal.class.getDeclaredFields()) {
            Pattern ptn = Pattern.compile(f.getName());
            Matcher mth = ptn.matcher(baseUrlString);
            while (mth.find()) {

                String paramStr = "";
                if (f.getName().equals("url")) {
                    paramStr = this.getParamStr(baseUrlString, mth.end() + "=".length(), "\\,");
                } else if (f.getName().equals("quality")) {
                    paramStr = this.getParamStr_forCatch(f.getName(), baseUrlString, mth.end() + "=".length(), "\\w+");
                } else if (f.getName().equals("type")) {
                    paramStr = this.getParamStr_forCatch(f.getName(), baseUrlString, mth.end() + "=".length(), "video\\/\\w+\\;\\scodecs\\=\".*?\"");
                } else if (f.getName().equals("itag")) {
                    paramStr = this.getParamStr_forCatch(f.getName(), baseUrlString, mth.end() + "=".length(), "\\d+");
                }

                DataConfig d = new DataConfig();
                d.paramStr = paramStr;
                d.name = f.getName();
                d.start = mth.start();
                d.end = mth.end() + ("=" + paramStr).length();

                lst.add(d);
            }
        }

        // 修正URL
        for (int ii = 0; ii < lst.size(); ii++) {
            DataConfig d1 = lst.get(ii);

            if (d1.name.equals("url")) {
                // 取代 type
                d1.paramStr = d1.paramStr.replaceAll("type\\=video\\/\\w+\\;\\scodecs\\=\"[\\w\\.]+,?", "");
                d1.paramStr = d1.paramStr.replaceAll("type\\=video\\/\\w+\\;\\scodecs\\=\"[\\w\\.]+,\\s[\\w\\.]+\"", "");

                // 取代 quality
                d1.paramStr = d1.paramStr.replaceAll("quality\\=\\w+", "");

                System.out.println("correct url = " + d1.paramStr);
            }
        }

        // Grouping
        List<DataConfig> urlLst = getMatchIndexDataConfig("url", lst);
        for (int ii = 0; ii < urlLst.size(); ii++) {
            DataConfig urlData = urlLst.get(ii);
            DataConfig qualityData = getIndex("quality", getMatchIndexDataConfig("url", lst), ii);
            DataConfig typeData = getIndex("type", getMatchIndexDataConfig("url", lst), ii);

            DataFinal dd = new DataFinal();
            dd.url = urlData;
            dd.quality = qualityData;
            dd.type = typeData;
            dd.itag = getMockItag(urlData.paramStr);

            rtnLst.add(dd);
        }
        return rtnLst;
    }

    private DataConfig getMockItag(String url) {
        Pattern ptn = Pattern.compile("itag\\=(\\d+)");
        Matcher mth = ptn.matcher(url);
        DataConfig d = new DataConfig();
        if (mth.find()) {
            d.paramStr = mth.group(1);
        }
        return d;
    }

    private DataConfig getIndex(String name, List<DataConfig> singleLst, int index) {
        try {
            return singleLst.get(index);
        } catch (Exception ex) {
            throw new RuntimeException("超出arry範圍 " + name + ": index : " + index + " -> size : " + singleLst.size());
        }
    }

    private List<DataConfig> getMatchIndexDataConfig(String name, List<DataConfig> lst) {
        List<DataConfig> singleLst = new ArrayList<DataConfig>();
        for (int ii = 0; ii < lst.size(); ii++) {
            DataConfig d1 = lst.get(ii);
            if (d1.name.equals(name)) {
                singleLst.add(d1);
            }
        }
        Collections.sort(singleLst, new Comparator<DataConfig>() {
            @Override
            public int compare(DataConfig o1, DataConfig o2) {
                return new Integer(o1.start).compareTo(o2.start);
            }
        });
        return singleLst;
    }

    private String getParamStr_forCatch(String title, String baseUrlString, int start, String catchPattern) {
        String tmpUrl = StringUtils.substring(baseUrlString, start);
        Pattern ptn = Pattern.compile(catchPattern);
        Matcher mth = ptn.matcher(tmpUrl);
        if (mth.find()) {
            return mth.group();
        }
        throw new RuntimeException("找步道" + title + " : " + tmpUrl);
    }

    private String getParamStr(String baseUrlString, int start, String endPattern) {
        String tmpUrl = StringUtils.substring(baseUrlString, start);
        Pattern ptn = Pattern.compile(endPattern);
        Matcher mth = ptn.matcher(tmpUrl);
        int pos = -1;
        if (mth.find()) {
            pos = mth.start();
        } else {
            pos = baseUrlString.length();
        }
        String paramStr = StringUtils.substring(tmpUrl, 0, pos);
        return paramStr;
    }

    private static class DataConfig {
        String name;
        int start;
        int end;
        String paramStr;

        @Override
        public String toString() {
            // return "DataConfig [name=" + name + ", start=" + start + ", end="
            // + end + ", paramStr=" + paramStr + "]";
            return name + "=" + paramStr;
        }
    }

    private static class DataFinal {
        DataConfig itag;
        DataConfig type;
        DataConfig quality;
        DataConfig url;

        @Override
        public String toString() {
            return "DataFinal [itag=" + itag + ", type=" + type + ", quality=" + quality + ", url=" + url + "]";
        }
    }

    public List<NameValuePair> getVideoInfo(String videoId, String format, String userAgent) {
        try {
            List<NameValuePair> infoMap = new ArrayList<NameValuePair>();
            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("video_id", videoId));
            qparams.add(new BasicNameValuePair("fmt", "" + format));
            URI uri = getUri("get_video_info", qparams);

            if (StringUtils.isBlank(userAgent)) {
                userAgent = DEFAULT_USER_AGENT;
            }

            CookieStore cookieStore = new BasicCookieStore();
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
                String videoInfo = getStringFromInputStream(DEFAULT_ENCODING, instream);
                URLEncodedUtils.parse(infoMap, new Scanner(videoInfo), DEFAULT_ENCODING);
            }
            return infoMap;
        } catch (Exception ex) {
            throw new RuntimeException("play Err : " + ex.getMessage(), ex);
        }
    }

    private URI getUri(String path, List<NameValuePair> qparams) throws URISyntaxException {
        URI uri = URIUtils.createURI(scheme, host, -1, "/" + path, URLEncodedUtils.format(qparams, DEFAULT_ENCODING), null);
        return uri;
    }

    private String getStringFromInputStream(String encoding, InputStream instream) throws UnsupportedEncodingException, IOException {
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(instream, encoding));
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
}