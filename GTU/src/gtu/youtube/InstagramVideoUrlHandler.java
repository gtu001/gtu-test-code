package gtu.youtube;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.log.line.SystemZ;
import gtu.net.https.SimpleHttpsUtil;
import gtu.youtube.PornVideoUrlDetection.SingleVideoUrlConfig;
import net.sf.json.JSONObject;

public class InstagramVideoUrlHandler extends Porn91Downloader {

    public static void main(String[] args) {
        InstagramVideoUrlHandler t = new InstagramVideoUrlHandler("https://www.instagram.com/p/CGt2iqkHEYw/", "", "");
        t.execute();
        System.out.println("done...");
    }

    private String url;
    private boolean igPage;
    private String cookieContent;
    private String headerContent;

    private Map<String, String> getCookies(String cookieStr) {
        Map<String, String> cooke = new LinkedHashMap<String, String>();
        try {
            JSONObject json = net.sf.json.JSONObject.fromObject(cookieStr);
            for (Iterator it = json.keys(); it.hasNext();) {
                String key = (String) it.next();
                String value = json.getString(key);
                SystemZ.out.println("cookie : " + Arrays.toString(new String[] { key, value }));
                cooke.put(key, value);
            }
            return cooke;
        } catch (Exception ex) {
            System.out.println("[getCookieStringByJSON] JSON parse err : " + ex.getMessage());
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(cookieStr));
            for (String line = null; (line = reader.readLine()) != null;) {
                String[] arry = line.split("\t", -1);
                if (arry != null && arry.length == 2) {
                    arry[0] = StringUtils.trimToEmpty(arry[0]);
                    arry[1] = StringUtils.trimToEmpty(arry[1]);
                    cooke.put(arry[0], arry[1]);
                    SystemZ.out.println("cookie : " + Arrays.toString(arry));
                }
            }
            return cooke;
        } catch (Exception ex) {
            throw new RuntimeException(" Err : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public void execute() {
        // String content = super.getVideoInfo(URI.create(url),
        // DEFAULT_USER_AGENT, cookieContent, headerContent);

        Map<String, String> cookie = getCookies(cookieContent);
        String content = SimpleHttpsUtil.newInstance().queryPage(url, cookie);

        System.out.println("START=====================================================================");
        System.out.println("=====================================================================");
        System.out.println(content);
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "InstagramVideoUrlHandler_CONTENT.txt"), content, "utf8");
        System.out.println("=====================================================================");
        System.out.println("END=====================================================================");

        String picUrl = "";
        String picName = "";
        for (FindType e : FindType.values()) {
            picUrl = e.findIgPicUrl(content);
            picName = e.findIgPicName(picUrl);
            if (StringUtils.isNotBlank(picUrl)) {
                break;
            }
        }

        String title = super.getTitleForFileName(content);

        if (StringUtils.isNotBlank(picUrl)) {
            SingleVideoUrlConfig su = new SingleVideoUrlConfig();
            su.htmlUrl = url;
            su.orignUrl = picUrl;
            su.finalFileName = picName;
            VideoUrlConfig su2 = new VideoUrlConfig(su);
            su2.title = title;
            try {
                su2.length = Porn91Downloader.getContentLength(DEFAULT_USER_AGENT, url);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            videoFor91Lst.add(su2);
        }
    }

    List<VideoUrlConfig> videoFor91Lst = new ArrayList<VideoUrlConfig>();

    private boolean isIgPage(String url) {
        Pattern ptn = Pattern.compile("https\\:\\/{2}www\\.instagram\\.com\\/p\\/(\\w+)");
        Matcher mth = ptn.matcher(url);
        if (mth.find()) {
            return true;
        }
        return false;
    }

    //

    private enum FindType {
        MP4() {
            public String findIgPicUrl(String content) {
                // <meta property="og:video"
                // content="https://instagram.ftpe2-2.fna.fbcdn.net/v/t50.2886-16/131088941_1013091425860350_2446071313511469680_n.mp4?_nc_ht=instagram.ftpe2-2.fna.fbcdn.net&_nc_cat=106&_nc_ohc=AjveLsVMrecAX93D0W_&oe=5FD98772&oh=42ee70c403e5cea50681db6227fdb607"
                // />
                String rtnVal = "";
                Pattern ptn = Pattern.compile("\\<meta\\s+property\\=\"og\\:video\"\\s+content\\=\"(.*?)\"\\s+\\/\\>");
                Matcher mth = ptn.matcher(content);
                if (mth.find()) {
                    rtnVal = mth.group(1);
                }
                System.out.println("findIgPicUrl--" + rtnVal);
                return rtnVal;
            }

            public String findIgPicName(String picUrl) {
                // /122250812_3673508459325879_7577152237268457512_n.jpg
                String rtnVal = "";
                Pattern ptn = Pattern.compile("\\/(.*?\\.mp4)");
                Matcher mth = ptn.matcher(picUrl);
                if (mth.find()) {
                    rtnVal = mth.group(1);
                    if (rtnVal.contains("/")) {
                        rtnVal = rtnVal.substring(rtnVal.lastIndexOf("/") + 1);
                    }
                }
                if (StringUtils.isBlank(rtnVal)) {
                    rtnVal = System.currentTimeMillis() + ".jpg";
                }
                System.out.println("findIgPicName--" + rtnVal);
                return rtnVal;
            }
        }, //
        IMAGE() {
            public String findIgPicUrl(String content) {
                // <meta property="og:image"
                // content="https://instagram.ftpe2-1.fna.fbcdn.net/v/t51.2885-15/e35/p1080x1080/122250812_3673508459325879_7577152237268457512_n.jpg?_nc_ht=instagram.ftpe2-1.fna.fbcdn.net&_nc_cat=103&_nc_ohc=6_T9s1VSSpAAX88nOeL&tp=1&oh=466f0051d4fdf48de12d2ad18fd2c348&oe=60018DE4"
                // />
                String rtnVal = "";
                Pattern ptn = Pattern.compile("\\<meta\\s+property\\=\"og\\:image\"\\s+content\\=\"(.*?)\"\\s+\\/\\>");
                Matcher mth = ptn.matcher(content);
                if (mth.find()) {
                    rtnVal = mth.group(1);
                }
                System.out.println("findIgPicUrl--" + rtnVal);
                return rtnVal;
            }

            public String findIgPicName(String picUrl) {
                // /122250812_3673508459325879_7577152237268457512_n.jpg
                String rtnVal = "";
                Pattern ptn = Pattern.compile("\\/(.*?\\.jpg)");
                Matcher mth = ptn.matcher(picUrl);
                if (mth.find()) {
                    rtnVal = mth.group(1);
                    if (rtnVal.contains("/")) {
                        rtnVal = rtnVal.substring(rtnVal.lastIndexOf("/") + 1);
                    }
                }
                if (StringUtils.isBlank(rtnVal)) {
                    rtnVal = System.currentTimeMillis() + ".jpg";
                }
                System.out.println("findIgPicName--" + rtnVal);
                return rtnVal;
            }
        },;

        abstract String findIgPicUrl(String content);

        abstract String findIgPicName(String picUrl);
    }

    public InstagramVideoUrlHandler(String url, String cookieContent, String headerContent) {
        try {
            videoFor91Lst = new ArrayList<VideoUrlConfig>();
            this.igPage = isIgPage(url);
            this.url = url;
            this.cookieContent = cookieContent;
            this.headerContent = headerContent;
        } catch (Exception e) {
            throw new RuntimeException(" YoutubeVideoUrlHandler Err : " + e.getMessage(), e);
        }
    }

    public List<VideoUrlConfig> getVideoFor91Lst() {
        return videoFor91Lst;
    }

    public boolean isIgPage() {
        return igPage;
    }
}