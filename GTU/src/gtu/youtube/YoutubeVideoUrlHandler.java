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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
    // private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows;
    // U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";

    public static void main(String[] args) {
        YoutubeVideoUrlHandler t = new YoutubeVideoUrlHandler("vBM2tg5FDH8", "", DEFAULT_USER_AGENT);
        System.out.println("done...");
    }

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
            for (NameValuePair pair : infoMap) {
                String key = pair.getName();
                String val = pair.getValue();
                System.out.println("\t" + key + "\t\t\t\t" + val);
            }

            Map<String, String> fmtMap = new HashMap<String, String>();
            String orignData = null;
            for (NameValuePair pair : infoMap) {
                String key = pair.getName();
                String val = pair.getValue();
                if ("url_encoded_fmt_stream_map".equals(key)) {
                    orignData = val;
                }
                if ("fmt_list".equals(key)) {
                    String[] vals1 = val.split(",", -1);
                    for(String v2 : vals1) {
                        String[] kv2 = v2.split("\\/", -1);
                        fmtMap.put(kv2[0], kv2[1]);
                    }
                }
            }

            String baseUrlString = URLDecoder.decode(orignData, DEFAULT_ENCODING);

            System.out.println("baseUrlString = " + baseUrlString);

            DataFinal.fmtMap  = fmtMap;
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
            DataConfig qualityData = getIndex("quality", getMatchIndexDataConfig("quality", lst), ii);
            DataConfig typeData = getIndex("type", getMatchIndexDataConfig("type", lst), ii);

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
        d.name = "itag";
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
            return paramStr;
        }
    }

    private static class DataFinal {
        public static Map<String, String> fmtMap = Collections.emptyMap();
        DataConfig itag;
        DataConfig type;
        DataConfig quality;
        DataConfig url;

        public String getFileExtension() {
            Pattern ptn = Pattern.compile("video\\/(\\w+)\\;");
            Matcher mth = ptn.matcher(type.paramStr);
            if (mth.find()) {
                return mth.group(1);
            }
            throw new RuntimeException("Not Match -> " + type.paramStr);
        }

        public String getQualityString() {
            return quality.paramStr;
        }
        
        public String getSolution() {
            return fmtMap.get(itag.paramStr);
        }

        @Override
        public String toString() {
            return getFileExtension() + " , " + getQualityString() + " , " + getSolution();
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
    
//    token               1
//    vid             vBM2tg5FDH8
//    itct                CAEQu2kiEwjQtsrhurnaAhWF1lgKHWzgCu8o6NQB
//    fexp                23703445,23704844,23708904,23708906,23708910,23710476,23712544,23713711,23714639,23716256,23716971,23718325,23720994,23721188,23721753,23721898,23723618,23723926,23726563,23727194,23727964,23728032,23728274,23729419,23729442,23729689,23731309,23732729,23733473,23733494,23733751,23734488,23735348,23735876,23736448,23736808,23736872,9422596,9441381,9449243,9458145,9475686,9485000
//    host_language               zh-TW
//    watermark               ,http://s.ytimg.com/yts/img/watermark/youtube_watermark-vflHX6b6E.png,http://s.ytimg.com/yts/img/watermark/youtube_hd_watermark-vflAzLcD6.png
//    player_response             {"playbackTracking":{},"videoDetails":{"thumbnail":{"thumbnails":[{"url":"http://i.ytimg.com/vi/vBM2tg5FDH8/hqdefault.jpg?sqp=-oaymwEWCKgBEF5IWvKriqkDCQgBFQAAiEIYAQ==\u0026rs=AOn4CLCovFb9OoyLvNG-ChdgqVq1yw5jcg","width":168,"height":94},{"url":"http://i.ytimg.com/vi/vBM2tg5FDH8/hqdefault.jpg?sqp=-oaymwEWCMQBEG5IWvKriqkDCQgBFQAAiEIYAQ==\u0026rs=AOn4CLBYvRCF6tCLHCqPVD9J7mH1H3Zb2A","width":196,"height":110},{"url":"http://i.ytimg.com/vi/vBM2tg5FDH8/hqdefault.jpg?sqp=-oaymwEXCPYBEIoBSFryq4qpAwkIARUAAIhCGAE=\u0026rs=AOn4CLCmaSJqU-HIuLU-JQ5HvpOsofLTEw","width":246,"height":138},{"url":"http://i.ytimg.com/vi/vBM2tg5FDH8/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=\u0026rs=AOn4CLC7ycUGne6FCpSy9qUbry88bwOxgQ","width":336,"height":188}]}},"endscreen":{"endscreenUrlRenderer":{"url":"//www.youtube.com/get_endscreen?client=1\u0026ei=w8rRWpDJMoWt4wLswKv4Dg\u0026v=vBM2tg5FDH8"}},"adSafetyReason":{"isEmbed":true,"isRemarketingEnabled":true,"isFocEnabled":true}}
//    is_listed               1
//    gapi_hint_params                m;/_/scs/abc-static/_/js/k=gapi.gapi.en.wjAGE-AVlTo.O/m=__features__/am=AAE/rt=j/d=1/rs=AHpOoo-gY863zTybf8wCUIW8gIU8UfC49g
//    xhr_apiary_host             youtubei.youtube.com
//    cl              192651238
//    ps              desktop-polymer
//    idpj                -4
//    uid             X4RuNFRuzF-HEFUlPOZ-ZA
//    status              ok
//    cr              TW
//    tmi             1
//    allow_embed             1
//    c               WEB
//    fmt_list                22/1280x720,43/640x360,18/640x360,36/320x180,17/176x144
//    loudness                -17.8990001678
//    thumbnail_url               http://i.ytimg.com/vi/vBM2tg5FDH8/default.jpg
//    ptk             youtube_single
//    cver                2.20180412
//    iv_invideo_url              http://www.youtube.com/annotations_invideo?cap_hist=1&video_id=vBM2tg5FDH8&client=1&ei=w8rRWpDJMoWt4wLswKv4Dg
//    ppv_remarketing_url             https://www.googleadservices.com/pagead/conversion/971134070/?backend=innertube&cname=1&cver=2_20180412&data=backend%3Dinnertube%3Bcname%3D1%3Bcver%3D2_20180412%3Bdactive%3DNone%3Bdynx_itemid%3DvBM2tg5FDH8%3Bptype%3Dppv&label=iuZUCLmC72YQ9qiJzwM&ptype=ppv
//    hl              zh_TW
//    pltype              content
//    no_get_video_log                1
//    title               「越獄」救命！好人獄警心臟病發！囚犯「集體撞鐵欄」知道會被掃射也要救他
//    atc             a=3&b=35K5prBNuqN6Fr5T0cOcnPM31XY&c=1523698372&d=1&e=vBM2tg5FDH8&c3a=12&c1a=1&c6a=1&hh=V6nnZz2P4upExkQ7qYcAei90nb9aa1_v3qJPlxZoAh8
//    baseUrl             https://www.youtube.com/pagead/viewthroughconversion/962985656/
//    video_id                vBM2tg5FDH8
//    iv_load_policy              1
//    external_play_video             1
//    avg_rating              4.90086936951
//    innertube_api_version               v1
//    eventid             w8rRWpDJMoWt4wLswKv4Dg
//    player_error_log_fraction               1.0
//    videostats_playback_base_url                https://s.youtube.com/api/stats/playback?fexp=23703445%2C23704844%2C23708904%2C23708906%2C23708910%2C23710476%2C23712544%2C23713711%2C23714639%2C23716256%2C23716971%2C23718325%2C23720994%2C23721188%2C23721753%2C23721898%2C23723618%2C23723926%2C23726563%2C23727194%2C23727964%2C23728032%2C23728274%2C23729419%2C23729442%2C23729689%2C23731309%2C23732729%2C23733473%2C23733494%2C23733751%2C23734488%2C23735348%2C23735876%2C23736448%2C23736808%2C23736872%2C9422596%2C9441381%2C9449243%2C9458145%2C9475686%2C9485000&of=ytGq7r6_8JwZ0B6TYmVheg&cl=192651238&len=119&plid=AAVpy6w0l2mHfTWh&vm=CAEQARgE&docid=vBM2tg5FDH8&ns=yt&el=embedded&ei=w8rRWpDJMoWt4wLswKv4Dg
//    view_count              164945
//    ptchn               X4RuNFRuzF-HEFUlPOZ-ZA
//    rmktEnabled             1
//    oid             pFeSQ3R00P-5wFle3tH-Og
//    iv_allow_in_place_switch                1
//    vm              CAEQARgE
//    apiary_host_firstparty              
//    author              人生勝利組Life victory group
//    enablecsi               1
//    length_seconds              119
//    csn             w8rRWpDJMoWt4wLswKv4Dg
//    cbr             Firefox
//    account_playback_token              QUFFLUhqbDN1eTNxSnZYQ0M3U2VfOU5ZQnA4RzhsY25CUXxBQ3Jtc0ttZ3ZDTV8ySFdRanN2bkY4Rm9fQjlNb2Q4U25WdXhWanZ0QUZmemh1N3dIb2hFcjk0VEpBRzk3eElzdmZNb3RBQWllWXk5RTlZcnZCUWZaelh3TElzbmkyUXN1bHdPX3dIUm1RSDhjWEc5ODBhaEZwQQ==
//    root_ve_type                27240
//    vss_host                s.youtube.com
//    ucid                UCX4RuNFRuzF-HEFUlPOZ-ZA
//    cbrver              59.0
//    apiary_host             
//    keywords                獄警,好人,心臟病,囚犯,美國,德州,Gary,Grim,工作,生命,救人,同事,尊重,平等,CPR,尊敬,感情,family friendly
//    storyboard_spec             http://i9.ytimg.com/sb/vBM2tg5FDH8/storyboard3_L$L/$N.jpg|48#27#100#10#10#0#default#rs$AOn4CLD5srRVYuVRvVAQfJVdA_tkICg8Xg|80#45#120#10#10#1000#M$M#rs$AOn4CLAfRXmDEQLVPXaMSPh0IUn7ZxVJQg|160#90#120#5#5#1000#M$M#rs$AOn4CLDlsQ29N82hPSvABzc8e_7edz90hA
//    adaptive_fmts               fps=30&index=765-1072&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3DD017C631259D9E781B1D247C54A2F666260F9A19.54AC66042D5C6D9CB88657B78486E705C195BA52%26fvip%3D3%26expire%3D1523719971%26itag%3D137%26key%3Dyt6%26clen%3D23994787%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.918%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523634898490890%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4&itag=137&projection_type=1&quality_label=1080p&type=video%2Fmp4%3B+codecs%3D%22avc1.640028%22&clen=23994787&lmt=1523634898490890&bitrate=2006260&size=1920x1080&init=0-764,fps=30&index=220-601&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D4F66BF3CE189A9F8F17441EBDDDCF0FEFE848AC5.A7633F082CFA507FE74D84D45E87F4F8CDBEC43A%26fvip%3D3%26expire%3D1523719971%26itag%3D248%26key%3Dyt6%26clen%3D7061232%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.919%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523637336155914%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm&itag=248&projection_type=1&quality_label=1080p&type=video%2Fwebm%3B+codecs%3D%22vp9%22&clen=7061232&eotf=bt709&lmt=1523637336155914&bitrate=1102640&size=1920x1080&primaries=bt709&init=0-219,fps=30&index=763-1070&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D5531E394CA34A57A1D48EF90B04BA9F05578C412.7D4AEAF9E8E43650ABA6D5D3F11C26D07C218104%26fvip%3D3%26expire%3D1523719971%26itag%3D136%26key%3Dyt6%26clen%3D13473375%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.918%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523634879807188%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4&itag=136&projection_type=1&quality_label=720p&type=video%2Fmp4%3B+codecs%3D%22avc1.4d401f%22&clen=13473375&lmt=1523634879807188&bitrate=1097360&size=1280x720&init=0-762,fps=30&index=220-601&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3DA19EE2473712F65EC856C3F887BB918866D82427.23CA00409C4C5DB262AAAEC2D629534725C525F8%26fvip%3D3%26expire%3D1523719971%26itag%3D247%26key%3Dyt6%26clen%3D4486316%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.919%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523637678887099%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm&itag=247&projection_type=1&quality_label=720p&type=video%2Fwebm%3B+codecs%3D%22vp9%22&clen=4486316&eotf=bt709&lmt=1523637678887099&bitrate=588802&size=1280x720&primaries=bt709&init=0-219,fps=30&index=763-1070&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D16821CA62CBA2CE7D990816314BDF386A2C7FE65.1A4BAFF55B1F1CD36CA3CB95A5BFC48840D7B07F%26fvip%3D3%26expire%3D1523719971%26itag%3D135%26key%3Dyt6%26clen%3D7254061%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.918%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523634844510531%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4&itag=135&projection_type=1&quality_label=480p&type=video%2Fmp4%3B+codecs%3D%22avc1.4d401f%22&clen=7254061&lmt=1523634844510531&bitrate=545120&size=854x480&init=0-762,fps=30&index=220-601&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D3D1BF8869432ECBAED58E6B584ED1F401E339483.A728EB8565A9D4772FF518A910A8F4238DA06E94%26fvip%3D3%26expire%3D1523719971%26itag%3D244%26key%3Dyt6%26clen%3D2685631%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.919%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523637272254897%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm&itag=244&projection_type=1&quality_label=480p&type=video%2Fwebm%3B+codecs%3D%22vp9%22&clen=2685631&eotf=bt709&lmt=1523637272254897&bitrate=313976&size=854x480&primaries=bt709&init=0-219,fps=30&index=763-1070&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D603576C784420AAF736506B65854948A5822AD77.7367BB08B9F9A47005CB772A34A0FE08D0D7CABA%26fvip%3D3%26expire%3D1523719971%26itag%3D134%26key%3Dyt6%26clen%3D3455253%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.918%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523634860145593%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4&itag=134&projection_type=1&quality_label=360p&type=video%2Fmp4%3B+codecs%3D%22avc1.4d401e%22&clen=3455253&lmt=1523634860145593&bitrate=252397&size=640x360&init=0-762,fps=30&index=219-599&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D9F523ED6B08A23106B21F813DEAB36762E9FA067.D9A3D7E751C299FC5381EF28AE7F2D21A925804D%26fvip%3D3%26expire%3D1523719971%26itag%3D243%26key%3Dyt6%26clen%3D1913150%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.919%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523637374945184%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm&itag=243&projection_type=1&quality_label=360p&type=video%2Fwebm%3B+codecs%3D%22vp9%22&clen=1913150&eotf=bt709&lmt=1523637374945184&bitrate=207850&size=640x360&primaries=bt709&init=0-218,fps=30&index=763-1070&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D42B521DA4E21617B966281CD1A2640A72754313D.2812C34AE04082D1E3145EBD98BF751A3F3B9BE8%26fvip%3D3%26expire%3D1523719971%26itag%3D133%26key%3Dyt6%26clen%3D1006172%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.918%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523634841311201%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4&itag=133&projection_type=1&quality_label=240p&type=video%2Fmp4%3B+codecs%3D%22avc1.4d4015%22&clen=1006172&lmt=1523634841311201&bitrate=91396&size=426x240&init=0-762,fps=30&index=218-598&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D0BFAA2A5933CB8CAD43206FCF5421C9E262FDB1D.28278817FE7AC9E9230F6052621EDD638229CBA8%26fvip%3D3%26expire%3D1523719971%26itag%3D242%26key%3Dyt6%26clen%3D1185393%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.919%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523637273837611%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm&itag=242&projection_type=1&quality_label=240p&type=video%2Fwebm%3B+codecs%3D%22vp9%22&clen=1185393&eotf=bt709&lmt=1523637273837611&bitrate=121486&size=426x240&primaries=bt709&init=0-217,fps=30&index=762-1069&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3DBC0508EC8F173A89FD1CCAD8B097ACE4CE67A67F.8AF9BD99DDFD1BFA1338E3F93D8D021E4BAEC7A8%26fvip%3D3%26expire%3D1523719971%26itag%3D160%26key%3Dyt6%26clen%3D576061%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.918%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523634828371278%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4&itag=160&projection_type=1&quality_label=144p&type=video%2Fmp4%3B+codecs%3D%22avc1.4d400c%22&clen=576061&lmt=1523634828371278&bitrate=52330&size=256x144&init=0-761,fps=30&index=218-598&xtags=&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D4292B288D1CEAECC0A958AD9848E6458C06BE0E5.29EB89238EB3D8912619FE3C6164DB2C6C8C279D%26fvip%3D3%26expire%3D1523719971%26itag%3D278%26key%3Dyt6%26clen%3D1121967%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.919%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%26source%3Dyoutube%26lmt%3D1523637451533721%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Daitags%252Cclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm&itag=278&projection_type=1&quality_label=144p&type=video%2Fwebm%3B+codecs%3D%22vp9%22&clen=1121967&eotf=bt709&lmt=1523637451533721&bitrate=97607&size=256x144&primaries=bt709&init=0-217,projection_type=1&lmt=1523632034006897&index=656-831&xtags=&bitrate=130484&type=audio%2Fmp4%3B+codecs%3D%22mp4a.40.2%22&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D26D2BFBDBF6C2B58EBB73947ACB60C45A4D26B67.1A7037936E845962FDE0E3CA7654820964F79299%26fvip%3D3%26expire%3D1523719971%26itag%3D140%26key%3Dyt6%26clen%3D1926768%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.979%26source%3Dyoutube%26lmt%3D1523632034006897%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Daudio%252Fmp4&itag=140&clen=1926768&init=0-655,projection_type=1&lmt=1523635889432517&index=4439-4639&xtags=&bitrate=139282&type=audio%2Fwebm%3B+codecs%3D%22vorbis%22&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D4F5CFC89D7ADE4BD5D891D1848D35E5D7567DF99.8573EEED97D0DA13D9B78C43CCA847AEF2119A21%26fvip%3D3%26expire%3D1523719971%26itag%3D171%26key%3Dyt6%26clen%3D2034757%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.935%26source%3Dyoutube%26lmt%3D1523635889432517%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Daudio%252Fwebm&itag=171&clen=2034757&init=0-4438,projection_type=1&lmt=1523635889980447&index=259-459&xtags=&bitrate=69237&type=audio%2Fwebm%3B+codecs%3D%22opus%22&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3DCC448B2D2E8783E98F32910B0E5510D66FDEEF5E.CE2C0F341E4ED555DAE81950B432D5DFC37C610D%26fvip%3D3%26expire%3D1523719971%26itag%3D249%26key%3Dyt6%26clen%3D849119%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.941%26source%3Dyoutube%26lmt%3D1523635889980447%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Daudio%252Fwebm&itag=249&clen=849119&init=0-258,projection_type=1&lmt=1523635890301612&index=259-459&xtags=&bitrate=89560&type=audio%2Fwebm%3B+codecs%3D%22opus%22&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D515A38BB988ADCC30A89A80556E0CB593C79A62E.64C9AD98D2A6173A6E24CE6B23E6D39DB9D6AD72%26fvip%3D3%26expire%3D1523719971%26itag%3D250%26key%3Dyt6%26clen%3D1121224%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.941%26source%3Dyoutube%26lmt%3D1523635890301612%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Daudio%252Fwebm&itag=250&clen=1121224&init=0-258,projection_type=1&lmt=1523635890408100&index=259-459&xtags=&bitrate=169002&type=audio%2Fwebm%3B+codecs%3D%22opus%22&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26keepalive%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3DB5D1B553D0C833029682854A3D0F09B527BC6BDE.31ADD991CB92747FD30ABDAFDB74A0D6A4A54736%26fvip%3D3%26expire%3D1523719971%26itag%3D251%26key%3Dyt6%26clen%3D2218162%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.941%26source%3Dyoutube%26lmt%3D1523635890408100%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Ckeepalive%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Daudio%252Fwebm&itag=251&clen=2218162&init=0-258
//    of              ytGq7r6_8JwZ0B6TYmVheg
//    timestamp               1523698372
//    focEnabled              1
//    url_encoded_fmt_stream_map              type=video%2Fmp4%3B+codecs%3D%22avc1.64001F%2C+mp4a.40.2%22&quality=hd720&itag=22&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26key%3Dyt6%26signature%3D948A517BE4C8F07FF16A8EC5431C373D0E863673.73905BFD3B9E257FF07A7CD5909BACA851559B09%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26mt%3D1523698253%26requiressl%3Dyes%26ip%3D111.241.17.27%26pl%3D21%26dur%3D118.979%26mv%3Dm%26source%3Dyoutube%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26c%3DWEB%26ipbits%3D0%26fvip%3D3%26ratebypass%3Dyes%26initcwndbps%3D870000%26sparams%3Ddur%252Cei%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26expire%3D1523719971%26lmt%3D1523635148066033%26mime%3Dvideo%252Fmp4%26itag%3D22,type=video%2Fwebm%3B+codecs%3D%22vp8.0%2C+vorbis%22&quality=medium&itag=43&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D16D3400C7BC92FC979FC8957A07EFDEE14C0BC06.A3B342FE02F628E10E446223536F2D422E5B0DD9%26fvip%3D3%26ratebypass%3Dyes%26expire%3D1523719971%26itag%3D43%26key%3Dyt6%26clen%3D7197413%26gir%3Dyes%26requiressl%3Dyes%26dur%3D0.000%26source%3Dyoutube%26lmt%3D1523635968930297%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fwebm,type=video%2Fmp4%3B+codecs%3D%22avc1.42001E%2C+mp4a.40.2%22&quality=medium&itag=18&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3DCA41654799791087C26A7B4E8BA41DFB1B2E5380.E07947D95E08CE30A3A5BB2BFDE883EB05A0D6C9%26fvip%3D3%26ratebypass%3Dyes%26expire%3D1523719971%26itag%3D18%26key%3Dyt6%26clen%3D5785381%26gir%3Dyes%26requiressl%3Dyes%26dur%3D118.979%26source%3Dyoutube%26lmt%3D1523629910416602%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Cratebypass%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252Fmp4,type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22&quality=small&itag=36&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D3DA5468F5B4E7495D0FC778BCD72B69EB84B726C.D94CBA9C451233CCB50C89D341C1C9B6B9BB0A2C%26fvip%3D3%26expire%3D1523719971%26itag%3D36%26key%3Dyt6%26clen%3D3296220%26gir%3Dyes%26requiressl%3Dyes%26dur%3D119.025%26source%3Dyoutube%26lmt%3D1523629927056731%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252F3gpp,type=video%2F3gpp%3B+codecs%3D%22mp4v.20.3%2C+mp4a.40.2%22&quality=small&itag=17&url=https%3A%2F%2Fr8---sn-ipoxu-un5r.googlevideo.com%2Fvideoplayback%3Fmn%3Dsn-ipoxu-un5r%252Csn-un57sn7z%26mm%3D31%252C29%26id%3Do-AKW4JqGxCO8ob4FvEakT9_Qm7-wkMIypCBm4TVj6mncO%26ip%3D111.241.17.27%26pl%3D21%26mv%3Dm%26mt%3D1523698253%26ms%3Dau%252Crdu%26ei%3Dw8rRWpDJMoWt4wLswKv4Dg%26signature%3D07BA074F176626AC621FC791F762402143470F49.8667F930D3D3B65B29B8805E962F09218C5BDAA5%26fvip%3D3%26expire%3D1523719971%26itag%3D17%26key%3Dyt6%26clen%3D1172724%26gir%3Dyes%26requiressl%3Dyes%26dur%3D119.025%26source%3Dyoutube%26lmt%3D1523630058774280%26c%3DWEB%26ipbits%3D0%26initcwndbps%3D870000%26sparams%3Dclen%252Cdur%252Cei%252Cgir%252Cid%252Cinitcwndbps%252Cip%252Cipbits%252Citag%252Clmt%252Cmime%252Cmm%252Cmn%252Cms%252Cmv%252Cpl%252Crequiressl%252Csource%252Cexpire%26mime%3Dvideo%252F3gpp
//    plid                AAVpy6w0l2mHfTWh
//    relative_loudness               3.10099983215
//    watch_xlb               http://s.ytimg.com/yts/xlbbin/watch-strings-zh_TW-vfl3R2gPI.xlb
//    remarketing_url             https://www.youtube.com/pagead/viewthroughconversion/962985656/?backend=innertube&cname=1&cver=2_20180412&data=backend%3Dinnertube%3Bcname%3D1%3Bcver%3D2_20180412%3Bptype%3Dview%3Btype%3Dview%3Butuid%3DX4RuNFRuzF-HEFUlPOZ-ZA%3Butvid%3DvBM2tg5FDH8&foc_id=X4RuNFRuzF-HEFUlPOZ-ZA&label=followon_view&ptype=view
//    iv3_module              1
//    innertube_api_key               AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8
//    fflags              enable_active_view_presence_data_collection=true&html5_progressive_signature_reload=true&html5_no_shadow_env_data_redux=true&html5_subsegment_readahead_min_buffer_health_secs_on_timeout=0.1&html5_live_only_disable_loader=true&mweb_muted_autoplay=true&html5_subsegment_readahead_controlled_by_buffer_health=true&html5_sticky_disables_variability=true&html5_platform_minimum_readahead_seconds=0.0&website_actions_throttle_percentage=1.0&html5_video_tbd_min_kb=0&html5_clear_by_reattaching=true&disable_client_side_midroll_freq_capping=true&use_fast_fade_in_0s=true&html5_probe_media_capabilities=true&html5_qoe_unstarted_in_initialization=true&html5_qoe_intercept=&html5_pipeline_ultra_low_latency=true&html5_ad_stats_bearer=true&html5_spherical_bicubic_mode=1&html5_serverside_call_server_on_biscotti_timeout=true&html5_tight_max_buffer_allowed_bandwidth_stddevs=0.0&disable_new_pause_state3=true&html5_ultra_low_latency_streaming_responses=true&html5_maximum_readahead_seconds=0.0&html5_manifestless_no_redundant_seek_to_head=true&html5_use_has_subfragmented_fmp4=true&mweb_playsinline=true&stop_using_ima_sdk_gpt_request_activity=true&html5_seek_implies_rebuffer=true&html5_background_cap_idle_secs=60&html5_connect_timeout_secs=7.0&send_html5_api_stats_ads_abandon=true&html5_webgl_hide_video_element=true&html5_subsegment_readahead_min_buffer_health_secs=0.25&ad_video_end_renderer_duration_milliseconds=7000&html5_pipeline_manifestless=true&mweb_cougar=true&html5_get_video_info_promiseajax=true&html5_live_ultra_low_latency_bandwidth_window=5.0&html5_aspect_from_adaptive_format=true&html5_live_normal_latency_bandwidth_window=0.0&html5_min_secs_between_format_selections=8.0&vmap_enabled_living_room=true&html5_sticky_reduces_discount_by=0.0&html5_subsegment_readahead_seek_latency_fudge=0.5&sdk_wrapper_levels_allowed=0&web_player_api_logging_fraction=0.01&html5_live_abr_repredict_fraction=0.0&html5_streaming_xhr_buffer_mdat=true&html5_drm_generate_request_delay=0&web_player_tabindex_killswitch=true&html5_tight_max_buffer_allowed_impaired_time=0.0&html5_subsegment_readahead_always_delay_appends=true&html5_stop_video_in_cancel_playback=true&fixed_padding_skip_button=true&midroll_notify_time_seconds=5&html5_subsegment_readahead_min_load_speed=1.5&safari_enable_spherical=true&sdk_ad_prefetch_time_seconds=-1&html5_live_probe_primary_host=true&html5_subsegment_readahead_target_buffer_health_secs=0.5&www_for_videostats=true&html5_manifestless_captions=true&html5_defer_background_errors=true&html5_max_headm_for_streaming_xhr=0&kevlar_allow_multistep_video_init=true&ad_duration_threshold_for_showing_endcap_seconds=15&html5_widevine_robustness_strings=true&allow_live_autoplay=true&persist_text_on_preview_button=true&html5_use_adaptive_live_readahead=true&call_release_video_in_bulleit=true&html5_mweb_client_cap=true&html5_disable_webgl_antialias=true&html5_ignore_bad_bitrates=true&html5_fludd_suspend=true&tvhtml5_background_su=true&lightweight_watch_video_swf=true&html5_post_interrupt_readahead=20&html5_minimum_readahead_seconds=0.0&segment_volume_reporting=true&player_external_control_on_classic_desktop=true&dynamic_ad_break_pause_threshold_sec=0&html5_max_readahead_bandwidth_cap=0&max_resolution_for_white_noise=360&html5_min_readbehind_cap_secs=60&html5_drop_large_gvi=true&html5_strip_emsg=true&html5_enable_bandwidth_estimation_type=true&html5_min_upgrade_health=0&html5_request_size_min_secs=0.0&html5_quality_cap_min_age_secs=0&html5_throttle_rate=0.0&html5_remove_pause=false&html5_jumbo_ull_subsegment_readahead_target=1.3&html5_live_no_streaming_impedance_mismatch=true&enable_live_state_auth=true&live_chunk_readahead=3&flex_theater_mode=true&html5_suspend_loader=true&html5_default_quality_cap=0&html5_streaming_xhr_progress_includes_latest=true&html5_prefer_server_bwe3=true&interaction_click_on_gel_web=true&enable_afv_div_reset_in_kevlar=true&html5_qoe_post=true&html5_stale_dash_manifest_retry_factor=1.0&uniplayer_dbp=true&html5_enable_embedded_player_visibility_signals=true&html5_bandwidth_window_size=0&html5_dont_predict_end_time_in_past=true&html5_serverside_call_server_on_biscotti_error=true&html5_composite_stall=true&html5_live_4k_more_buffer=true&html5_streaming_xhr_optimize_lengthless_mp4=true&html5_max_av_sync_drift=50&fix_gpt_pos_params=true&safari_show_cued=true&html5_hfr_quality_cap=0&html5_expire_preloaded_players=true&show_interstitial_white=true&html5_enable_360_api=true&html5_resume_implies_rebuffer=true&html5_readahead_target_secs=0&html5_start_date_from_element=true&skip_restore_on_abandon_in_bulleit=true&html5_background_quality_cap=360&html5_disable_move_pssh_to_moov=true&html5_request_sizing_multiplier=0.8&html5_request_size_max_secs=31&html5_jumbo_ull_nonstreaming_mffa_ms=4000&show_countdown_on_bumper=true&html5_subsegment_readahead_tail_margin_secs=0.2&use_new_style=true&html5_error_reload_cooldown_ms=30000&html5_mobile_perf_cap_240=true&html5_subsegment_readahead_progress_timeout_fraction=0.8&vss_dni_delayping=0&html5_live_pin_to_tail=true&youtubei_for_web=true&html5_mse_retry=true&html5_separate_init_posts_fatal=true&html5_manifestless_accurate_sliceinfo=true&use_html5_player_event_timeout=true&html5_timeupdate_readystate_check=true&forced_brand_precap_duration_ms=2000&use_refreshed_overlay_buttons=true&html5_ignore_public_setPlaybackQuality=true&html5_elbow_tracking_tweaks=true&html5_serverside_biscotti_id_wait_ms=1000&html5_preload_media=true&live_readahead_seconds_multiplier=0.8&set_interstitial_start_button=true&html5_check_all_slices_for_emsg=true&web_player_edge_autohide_killswitch2=true&web_player_native_controls_live_captions_fix=true&html5_min_buffer_to_resume=6&html5_nnr_downgrade_adjacency=true&html5_readahead_target_window_secs=0&doubleclick_gpt_retagging=true&mweb_autonav=true&html5_incremental_parser_buffer_duration_secs=1.5&autoplay_time=8000&html5_mse_refactor=true&html5_hls_initial_bitrate=0&html5_nnr_downgrade_count=4&html5_pause_video_fix=true&html5_restrict_streaming_xhr_on_sqless_requests=true&html5_repredict_interval_secs=0.0&html5_readahead_target_backstop=0.0&html5_use_media_capabilities=true&mweb_muted_autoplay_animation=shrink&html5_throttle_burst_secs=15.0&html5_readahead_ratelimit=3000&player_destroy_old_version=true&html5_new_fallback=true&dynamic_ad_break_seek_threshold_sec=0&html5_incremental_parser_buffer_extra_bytes=16384&hide_preskip=true&html5_allowable_liveness_drift_chunks=2&fix_bulleit_cue_range_seek=true&enable_bulleit_lidar_integration=true&playready_on_borg=true&mweb_cougar_big_controls=true&html5_new_autoplay_redux=true&html5_report_all_states=true&enable_prefetch_for_postrolls=true&mpu_visible_threshold_count=2&html5_live_disable_dg_pacing=true&html5_get_video_info_timeout_ms=30000&html5_default_ad_gain=0.5&html5_live_use_alternate_bandwidth_window_sizes=true&html5_disable_urgent_upgrade_for_quality=true&variable_load_timeout_ms=0&html5_reload_on_unparseable=true&desktop_cleanup_companion_on_instream_begin=true&show_thumbnail_behind_ypc_offer_module=true&html5_adunit_from_adformat=true&html5_vp9_live_blacklist_edge=true&show_interstitial_for_3s=true&html5_player_autonav_logging=true&postroll_notify_time_seconds=5&html5_variability_full_discount_thresh=3.0&html5_disable_complete_segments=true&html5_subsegment_readahead_load_speed_check_interval=0.5&html5_live_low_latency_bandwidth_window=0.0&mweb_cougar_ads_backend=true&html5_disable_preserve_reference=true&html5_max_reseek_count=0&king_crimson_player_redux=true&html5_disable_subscribe_new_vis=true&html5_license_constraint_delay=5000&html5_waiting_sync_tracker=true&html5_adjust_effective_request_size=true&html5_probe_secondary_during_timeout_miss_count=0&html5_resume_polls=true&html5_deadzone_multiplier=1.0&live_fresca_v2=true&html5_suspended_state=true&html5_variability_discount=0.5&android_max_reloads_on_expired_stream_load=0&use_forced_linebreak_preskip_text=true&html5_variability_no_discount_thresh=1.0&html5_disable_non_contiguous=true&html5_parse_inline_fallback_host=true&use_survey_skip_in_0s=true&html5_start_off_live=0&html5_msi_error_fallback=true&html5_move_seek_resume=true&dash_manifest_version=5&html5_local_max_byterate_lookahead=15&html5_subsegment_readahead_timeout_secs=2.0&html5_live_abr_head_miss_fraction=0.0&html5_ad_no_buffer_abort_after_skippable=true&player_unified_fullscreen_transitions=true&web_player_disable_flash_playerproxy=true&fast_autonav_in_background=true&html5_enable_mesh_projection=true&html5_suspend_manifest_on_pause=true&html5_reattach_resource_after_timeout_limit=0&spacecast_uniplayer_decorate_manifest=true&set_interstitial_advertisers_question_text=true&allow_midrolls_on_watch_resume_in_bulleit=true&mweb_playsinline_webview=true&use_new_skip_icon=true&html5_min_startup_smooth_target=10.0&html5_min_readbehind_secs=0&bulleit_get_midroll_info_timeout_ms=2000&html5_max_buffer_health_for_downgrade=15&html5_vp9_live_whitelist=true&tvhtml5_min_readbehind_secs=20&html5_max_buffer_duration=120&html5_disable_audio_slicing=true&html5_incremental_parser_coalesce_slice_buffers=true&low_engagement_player_quality_cap=360&html5_use_equirect_mesh=true&legacy_autoplay_flag=true&show_thumbnail_on_standard=true&playready_first_play_expiration=-1
//    ismb                6960000
//    t               1
//    vmap                <?xml version="1.0" encoding="UTF-8"?><vmap:VMAP xmlns:vmap="http://www.iab.net/videosuite/vmap" xmlns:yt="http://youtube.com" version="1.0"></vmap:VMAP>
//    innertube_context_client_version                2.20180412
//    csi_page_type               embed
//    cos             Windows
//    allow_ratings               1
//    cosver              10.0
//    ldpj                -4
}