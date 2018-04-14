package gtu.youtube;

/**
 * This work is licensed under a Creative Commons Attribution 3.0 Unported
 * License (http://creativecommons.org/licenses/by/3.0/). This work is placed
 * into the public domain by the author.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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

import gtu.file.FileUtil;

/**
 * Locally download a YouTube.com video.
 */
public class JavaYoutubeDownloader extends Formatter {

    private static final String scheme = "http";
    private static final String host = "www.youtube.com";
    private static final String YOUTUBE_WATCH_URL_PREFIX = scheme + "://" + host + "/watch?v=";
    private static final String ERROR_MISSING_VIDEO_ID = "Missing video id. Extract from " + YOUTUBE_WATCH_URL_PREFIX + "VIDEO_ID";
    // private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows;
    // U; Windows NT 6.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String newline = System.getProperty("line.separator");
    private static final Logger log = Logger.getLogger(JavaYoutubeDownloader.class.getCanonicalName());
    private static final Logger rootlog = Logger.getLogger("");
    private static final Pattern commaPattern = Pattern.compile(",");
    private static final Pattern pipePattern = Pattern.compile("\\|");
    private static final char[] ILLEGAL_FILENAME_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };
    private static final int BUFFER_SIZE = 2048;
    private static final DecimalFormat commaFormatNoPrecision = new DecimalFormat("###,###");
    private static final double ONE_HUNDRED = 100;
    private static final double KB = 1024;

    private String customDownloadUrl_by_gtu001;// 自訂下載網址

    private void usage(String error) {
        if (error != null) {
            System.err.println("Error: " + error);
        }
        System.err.println("usage: JavaYoutubeDownload VIDEO_ID");
        System.err.println();
        System.err.println("Options:");
        System.err.println("\t[-dir DESTINATION_DIR] - Specify output directory.");
        System.err.println("\t[-format FORMAT] - Format number" + newline + "\t\tSee http://en.wikipedia.org/wiki/YouTube#Quality_and_codecs");
        System.err.println("\t[-ua USER_AGENT] - Emulate a browser user agent.");
        System.err.println("\t[-enc ENCODING] - Default character encoding.");
        System.err.println("\t[-verbose] - Verbose logging for downloader component.");
        System.err.println("\t[-verboseall] - Verbose logging for all components (e.g. HttpClient).");
        System.exit(-1);
    }
    
    public static void main(String[] args) {
        JavaYoutubeDownloader.mainRun("vBM2tg5FDH8", "", DEFAULT_ENCODING);
    }

    public String[] makeFakeArgs(String videoId, String outputDir) {
        if (StringUtils.isBlank(outputDir)) {
            outputDir = FileUtil.DESKTOP_PATH;
        }
        String[] args = new String[] { //
                videoId, // 
                "-dir", outputDir, //
                "-format", "18", //
                "-ua", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0", //
                "-verboseall" }; //
        return args;
    }

    public static void mainRun(String videoId, String realUrl, String outputDir) {
        try {
            JavaYoutubeDownloader t = new JavaYoutubeDownloader();
            String[] args = t.makeFakeArgs(videoId, outputDir);
            t.customDownloadUrl_by_gtu001 = StringUtils.trimToNull(realUrl);
            t.run(args);
            System.out.println("done...");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void run(String[] args) throws Throwable {
        setupLogging(Level.WARNING, Level.WARNING);

        String videoId = null;
        String outdir = ".";
        int format = 18;
        String encoding = DEFAULT_ENCODING;
        String userAgent = DEFAULT_USER_AGENT;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            // Options start with either -, --
            // Do not accept Windows-style args that start with / because of abs
            // paths on linux for file names.
            if (arg.charAt(0) == '-') {

                // For easier processing, convert any double dashes to
                // single dashes
                if (arg.length() > 1 && arg.charAt(1) == '-') {
                    arg = arg.substring(1);
                }

                String larg = arg.toLowerCase();

                // Process the option
                if (larg.equals("-help") || larg.equals("-?") || larg.equals("-usage") || larg.equals("-h")) {
                    usage(null);
                } else if (larg.equals("-verbose")) {
                    setupLogging(Level.ALL, Level.WARNING);
                } else if (larg.equals("-verboseall")) {
                    setupLogging(Level.ALL, Level.ALL);
                } else if (larg.equals("-dir")) {
                    outdir = args[++i];
                } else if (larg.equals("-format")) {
                    format = Integer.parseInt(args[++i]);
                } else if (larg.equals("-ua")) {
                    userAgent = args[++i];
                } else if (larg.equals("-enc")) {
                    encoding = args[++i];
                } else {
                    usage("Unknown command line option " + args[i]);
                }
            } else {
                // Non-option (i.e. does not start with -, --

                videoId = arg;

                // Break if only the first non-option should be used.
                break;
            }
        }

        if (videoId == null) {
            usage(ERROR_MISSING_VIDEO_ID);
        }

        log.fine("Starting");

        if (videoId.startsWith(YOUTUBE_WATCH_URL_PREFIX)) {
            videoId = videoId.substring(YOUTUBE_WATCH_URL_PREFIX.length());
        }
        int a = videoId.indexOf('&');
        if (a != -1) {
            videoId = videoId.substring(0, a);
        }

        File outputDir = new File(outdir);
        String extension = getExtension(format);

        play(videoId, format, encoding, userAgent, outputDir, extension);

        log.fine("Finished");
    }

    private String getExtension(int format) {
        switch (format) {
        case 18:
            return "mp4";
        default:
            throw new Error("Unsupported format " + format);
        }
    }

    private void play(String videoId, int format, String encoding, String userAgent, File outputdir, String extension) throws Throwable {
        // 取得Youtube下載網址
        YoutubeVideoUrlHandler urlHandler = new YoutubeVideoUrlHandler(videoId, String.valueOf(format), userAgent);

        // 原來邏輯
        log.fine("Retrieving " + videoId);
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("video_id", videoId));
        qparams.add(new BasicNameValuePair("fmt", "" + format));
        URI uri = getUri("get_video_info", qparams);

        CookieStore cookieStore = new BasicCookieStore();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(uri);
        if (userAgent != null && userAgent.length() > 0) {
            httpget.setHeader("User-Agent", userAgent);
        }

        log.finer("Executing " + uri);
        HttpResponse response = httpclient.execute(httpget, localContext);
        HttpEntity entity = response.getEntity();
        if (entity != null && response.getStatusLine().getStatusCode() == 200) {
            InputStream instream = entity.getContent();
            String videoInfo = getStringFromInputStream(encoding, instream);
            System.out.println("videoInfo = " + videoInfo);
            if (videoInfo != null && videoInfo.length() > 0) {
                List<NameValuePair> infoMap = new ArrayList<NameValuePair>();
                URLEncodedUtils.parse(infoMap, new Scanner(videoInfo), encoding);
                System.out.println("infoMap = " + infoMap);

                String downloadUrl = null;
                String filename = videoId;

                for (NameValuePair pair : infoMap) {
                    String key = pair.getName();
                    String val = pair.getValue();
                    log.finest(key + "=" + val);
                    // System.out.println(key + "\t" + val);
                    if (key.equals("title")) {
                        filename = val;
                    } else if (key.equals("fmt_url_map")) { // 原來的
                        String[] formats = commaPattern.split(val);
                        boolean found = false;
                        for (String fmt : formats) {
                            String[] fmtPieces = pipePattern.split(fmt);
                            if (fmtPieces.length == 2) {
                                int pieceFormat = Integer.parseInt(fmtPieces[0]);
                                log.fine("Available format=" + pieceFormat);
                                if (pieceFormat == format) {
                                    // found what we want
                                    downloadUrl = fmtPieces[1];
                                    System.out.println(">>> downloadUrl = " + downloadUrl);
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if (!found) {
                            log.warning("Could not find video matching specified format, however some formats of the video do exist (use -verbose).");
                        }
                    }
                }

                filename = cleanFilename(filename);
                if (filename.length() == 0) {
                    filename = videoId;
                } else {
                    filename += "_" + videoId;
                }
                filename += "." + extension;
                File outputfile = new File(outputdir, filename);

                //自訂下載網址1
                if (downloadUrl == null) {
                    downloadUrl = customDownloadUrl_by_gtu001;
                }
                
                //自訂下載網址2
                if (downloadUrl == null) {
                    YoutubeVideoUrlHandler gtu001 = new YoutubeVideoUrlHandler(videoId, "", userAgent);
                    downloadUrl = gtu001.getUrl(format);
                }

                if (downloadUrl != null) {
                    downloadWithHttpClient(userAgent, downloadUrl, outputfile);
                } else {
                    log.severe("Could not find video");
                }
            } else {
                log.severe("Did not receive content from youtube");
            }
        } else {
            log.severe("Could not contact youtube: " + response.getStatusLine());
        }
    }

    private void downloadWithHttpClient(String userAgent, String downloadUrl, File outputfile) throws Throwable {
        HttpGet httpget2 = new HttpGet(downloadUrl);
        if (userAgent != null && userAgent.length() > 0) {
            httpget2.setHeader("User-Agent", userAgent);
        }

        log.finer("Executing " + httpget2.getURI());
        HttpClient httpclient2 = new DefaultHttpClient();
        HttpResponse response2 = httpclient2.execute(httpget2);
        HttpEntity entity2 = response2.getEntity();
        if (entity2 != null && response2.getStatusLine().getStatusCode() == 200) {
            double length = entity2.getContentLength();
            if (length <= 0) {
                // Unexpected, but do not divide by zero
                length = 1;
            }
            InputStream instream2 = entity2.getContent();
            System.out.println("Writing " + commaFormatNoPrecision.format(length) + " bytes to " + outputfile);
            if (outputfile.exists()) {
                outputfile.delete();
            }
            FileOutputStream outstream = new FileOutputStream(outputfile);
            System.out.println("outputfile " + outputfile);
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                double total = 0;
                int count = -1;
                int progress = 10;
                long start = System.currentTimeMillis();
                while ((count = instream2.read(buffer)) != -1) {
                    total += count;
                    int p = (int) ((total / length) * ONE_HUNDRED);
                    if (p >= progress) {
                        long now = System.currentTimeMillis();
                        double s = (now - start) / 1000;
                        int kbpers = (int) ((total / KB) / s);
                        System.out.println(progress + "% (" + kbpers + "KB/s)");
                        progress += 10;
                    }
                    outstream.write(buffer, 0, count);
                }
                outstream.flush();
            } finally {
                outstream.close();
            }
            System.out.println("Done");
        }
    }

    private String cleanFilename(String filename) {
        for (char c : ILLEGAL_FILENAME_CHARACTERS) {
            filename = filename.replace(c, '_');
        }
        return filename;
    }

    private URI getUri(String path, List<NameValuePair> qparams) throws URISyntaxException {
        URI uri = URIUtils.createURI(scheme, host, -1, "/" + path, URLEncodedUtils.format(qparams, DEFAULT_ENCODING), null);
        System.out.println("getUri = " + uri);
        return uri;
    }

    private void setupLogging(Level myLevel, Level globalLevel) {
        changeFormatter(this);
        explicitlySetAllLogging(myLevel, globalLevel);
    }

    @Override
    public String format(LogRecord arg0) {
        return arg0.getMessage() + newline;
    }

    private void changeFormatter(Formatter formatter) {
        Handler[] handlers = rootlog.getHandlers();
        for (Handler handler : handlers) {
            handler.setFormatter(formatter);
        }
    }

    private void explicitlySetAllLogging(Level myLevel, Level globalLevel) {
        rootlog.setLevel(Level.ALL);
        for (Handler handler : rootlog.getHandlers()) {
            handler.setLevel(Level.ALL);
        }
        log.setLevel(myLevel);
        rootlog.setLevel(globalLevel);
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

/**
 * <pre>
 * Exploded results from get_video_info:
 * 
 * fexp=909302
 * allow_embed=1
 * fmt_stream_map=35|http://v9.lscache8.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=35&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=9E0A8E67154145BCADEBCF844CC155282548288F.2BBD0B2E125E3E533D07866C7AE91B38DD625D30&factor=1.25&id=4ba2193f7c9127d2||tc.v9.cache8.c.youtube.com,34|http://v6.lscache3.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=34&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=6726793A7B041E6456B52C0972596D0D58974141.42B5A0573F62B85AEA7979E5EE1ADDD47EB9E909&factor=1.25&id=4ba2193f7c9127d2||tc.v6.cache3.c.youtube.com,18|http://v12.lscache7.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=18&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=AE58398D4CC4D760C682D2A5B670B4047777FFF0.952E4FC4554E786FD937E7A89140E1F79B6DD8B7&factor=1.25&id=4ba2193f7c9127d2||tc.v12.cache7.c.youtube.com,5|http://v1.lscache7.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=5&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=43434DCB6CFC463FF4522D9EE7CD019FE47237B1.C60A9522E361130938663AF2DAD83A5C2821AF5C&factor=1.25&id=4ba2193f7c9127d2||tc.v1.cache7.c.youtube.com
 * fmt_url_map=35|http://v9.lscache8.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=35&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=9E0A8E67154145BCADEBCF844CC155282548288F.2BBD0B2E125E3E533D07866C7AE91B38DD625D30&factor=1.25&id=4ba2193f7c9127d2,34|http://v6.lscache3.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=34&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=6726793A7B041E6456B52C0972596D0D58974141.42B5A0573F62B85AEA7979E5EE1ADDD47EB9E909&factor=1.25&id=4ba2193f7c9127d2,18|http://v12.lscache7.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=18&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=AE58398D4CC4D760C682D2A5B670B4047777FFF0.952E4FC4554E786FD937E7A89140E1F79B6DD8B7&factor=1.25&id=4ba2193f7c9127d2,5|http://v1.lscache7.c.youtube.com/videoplayback?ip=174.0.0.0&sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Calgorithm%2Cburst%2Cfactor&fexp=909302&algorithm=throttle-factor&itag=5&ipbits=8&burst=40&sver=3&expire=1294549200&key=yt1&signature=43434DCB6CFC463FF4522D9EE7CD019FE47237B1.C60A9522E361130938663AF2DAD83A5C2821AF5C&factor=1.25&id=4ba2193f7c9127d2
 * allow_ratings=1
 * keywords=Stefan Molyneux,Luke Bessey,anarchy,stateless society,giant stone cow,the story of our unenslavement,market anarchy,voluntaryism,anarcho capitalism
 * track_embed=0
 * fmt_list=35/854x480/9/0/115,34/640x360/9/0/115,18/640x360/9/0/115,5/320x240/7/0/0
 * author=lukebessey
 * muted=0
 * length_seconds=390
 * plid=AASZXXGQtTEDKwAw
 * ftoken=null
 * status=ok
 * watermark=http://s.ytimg.com/yt/swf/logo-vfl_bP6ud.swf,http://s.ytimg.com/yt/swf/hdlogo-vfloR6wva.swf
 * timestamp=1294526523
 * has_cc=False
 * fmt_map=35/854x480/9/0/115,34/640x360/9/0/115,18/640x360/9/0/115,5/320x240/7/0/0
 * leanback_module=http://s.ytimg.com/yt/swfbin/leanback_module-vflJYyeZN.swf
 * hl=en_US
 * endscreen_module=http://s.ytimg.com/yt/swfbin/endscreen-vflk19iTq.swf
 * vq=auto
 * avg_rating=5.0
 * video_id=S6IZP3yRJ9I
 * token=vjVQa1PpcFNhI3jvw6hfEHivcKK-XY5gb-iszDMrooA=
 * thumbnail_url=http://i4.ytimg.com/vi/S6IZP3yRJ9I/default.jpg
 * title=The Story of Our Unenslavement - Animated
 * </pre>
 */
