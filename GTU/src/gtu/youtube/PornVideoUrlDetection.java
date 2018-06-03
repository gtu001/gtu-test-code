package gtu.youtube;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import gtu.date.DateFormatUtil;
import gtu.log.JdkLoggerUtil;

public class PornVideoUrlDetection {

    public static final String FILE_EXTENSTION_VIDEO_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3)";
    private static final String FILE_EXTENSTION_POSSIBLE_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3|jpg|jpeg|gif|tif|png|bmp)";

    private class PatternConfig {
        Pattern urlPtn;
        String urlDetailPtn;
        Pattern urlPtn_NonHttp;
        String urlDetailPtn_NonHttp;
        final Pattern filenamePtn;

        private PatternConfig(String fileExtenstion) {
            // 有http開頭的
            urlPtn = Pattern.compile("[\"\'](https?\\:.*?)[\"\']", Pattern.CASE_INSENSITIVE);
            urlDetailPtn = "https?\\:.*?\\." + fileExtenstion + "\\??.*?";

            // 沒有http開頭的
            urlPtn_NonHttp = Pattern.compile("[\"\'](.*?)[\"\']", Pattern.CASE_INSENSITIVE);
            urlDetailPtn_NonHttp = ".*?\\." + fileExtenstion + "\\??.*?";

            // 檔名格式
            filenamePtn = Pattern.compile("\\/((.*)\\.(" + fileExtenstion + "))", Pattern.CASE_INSENSITIVE);
        }
    }
    
    private static Logger logger = JdkLoggerUtil.getLogger(PornVideoUrlDetection.class, true);
    static {
//        JdkLoggerUtil.setupRootLogLevel(Level.FINE);
    }

    PatternConfig ptn;
    String htmlContent;

    public PornVideoUrlDetection(String fileExtension, String htmlContent) {
        ptn = new PatternConfig(fileExtension);
        this.htmlContent = htmlContent;
    }
    public PornVideoUrlDetection(String htmlContent) {
        ptn = new PatternConfig(FILE_EXTENSTION_VIDEO_PATTERN);
        this.htmlContent = htmlContent;
    }

    public static class SingleVideoUrlConfig {
        List<String> possibleLst = new ArrayList<String>();
        String orignUrl;
        String finalFileName;

        public List<String> getPossibleLst() {
            return possibleLst;
        }

        public String getOrignUrl() {
            return orignUrl;
        }

        public String getFinalFileName() {
            return finalFileName;
        }

        public SingleVideoUrlConfig() {
            finalFileName = "UnknownVideo_" + DateFormatUtil.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
        }

        @Override
        public String toString() {
            return "SingleVideoUrlConfig [finalFileName=" + finalFileName + ", possibleLst=" + possibleLst + ", orignUrl=" + orignUrl + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((orignUrl == null) ? 0 : orignUrl.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SingleVideoUrlConfig other = (SingleVideoUrlConfig) obj;
            if (orignUrl == null) {
                if (other.orignUrl != null)
                    return false;
            } else if (!orignUrl.equals(other.orignUrl))
                return false;
            return true;
        }
    }

    private List<String> getPossibableSubName(String singleUrl) {
        Pattern ptn = Pattern.compile("\\.(\\w{2,6})(.?)");
        Matcher mth = ptn.matcher(singleUrl);
        List<String> possableLst = new ArrayList<String>();
        while (mth.find()) {
            String subname = mth.group(1);
            String chkChar = mth.group(2);
            if (StringUtils.isBlank(chkChar) || chkChar.matches("[^a-z]")) {
                if (subname.matches("[0-9a-z]+")) {
                    possableLst.add(subname);
                }
            }
        }
        return possableLst;
    }

    private String fixUrlIfNeed(String url) {
        if (url.startsWith("http")) {
            return url;
        }
        Pattern ptn = Pattern.compile("^\\/+(.*)$");
        if (Pattern.matches(ptn.pattern(), url)) {
            Matcher mth = ptn.matcher(url);
            if (mth.find()) {
                return "http://" + mth.group(1);
            }
        }
        throw new RuntimeException("不可預期URL : " + url);
    }

    private String getPossiblFileSubName(List<String> possableLst) {
        for (int ii = possableLst.size() - 1; ii > 0; ii--) {
            if (StringUtils.isNotBlank(possableLst.get(ii)) && possableLst.get(ii).matches(FILE_EXTENSTION_POSSIBLE_PATTERN)) {
                return possableLst.get(ii);
            }
        }
        throw new RuntimeException("無合理副檔名 : " + possableLst);
    }

    public SingleVideoUrlConfig transforToSingleVideoUrl(String singleUrl) {
        SingleVideoUrlConfig conf = new SingleVideoUrlConfig();

        conf.orignUrl = this.fixUrlIfNeed(singleUrl);
        conf.possibleLst = this.getPossibableSubName(singleUrl);

        Matcher mth2 = ptn.filenamePtn.matcher(singleUrl);
        if (mth2.find()) {
            String finalFileName = "";

            String filename = mth2.group(2);
            filename = filename.substring(filename.lastIndexOf("/") + 1);
            String subName = mth2.group(3);

            // 預設檔名
            finalFileName = filename + "." + subName;

            // 取得最可能的副檔名
            if (!conf.possibleLst.isEmpty()) {
                String maybeRealSubname = getPossiblFileSubName(conf.possibleLst);
                if (!StringUtils.equals(subName, maybeRealSubname)) {
                    finalFileName = filename + "." + maybeRealSubname;
                }
            }

            conf.finalFileName = finalFileName;
        }
        return conf;
    }

    public static void main(String[] args) {
        Porn91Downloader p = new Porn91Downloader();
        String url = "https://www.youjizz.com/videos/vixen-tasha-reign-has-intense-sex-with-a-college-friend-47628091.html";
        String content = p.getVideoInfo(URI.create(url), "", "", "");

        PornVideoUrlDetection p2 = new PornVideoUrlDetection(FILE_EXTENSTION_VIDEO_PATTERN, content);
        p2.processMain();
        System.out.println("done...");
    }
    
    public List<SingleVideoUrlConfig> processMain(){
        List<String> urlLst = filterVideoURL(htmlContent);
        List<SingleVideoUrlConfig> videoLst = new ArrayList<SingleVideoUrlConfig>();
        for (String singleUrl : urlLst) {
            SingleVideoUrlConfig singleVO = null;
            try {
                singleVO = transforToSingleVideoUrl(singleUrl);
            }catch(Exception ex) {
                logger.severe("無法取得singleVO : " + ex.getMessage());
                continue;
            }
            if(videoLst.contains(singleVO)) {
                continue;
            }
            videoLst.add(singleVO);
            logger.info("" + singleVO);
        }
        return videoLst;
    }

    private List<String> filterVideoURL(String content) {
        // [\w+\/\\-\.\?\=\_] //TODO
        List<String> urlLst = new ArrayList<String>();

        // 有http開頭
        Matcher mth = ptn.urlPtn.matcher(content);
        while (mth.find()) {
            String url = mth.group(1);
            url = unescapeJavaSilent(url);
            if (!Pattern.matches(ptn.urlDetailPtn, url)) {
                // System.out.println("ignore -> " + url);
                continue;
            } else {
                logger.info("Add[1] -> " + url);
            }
            urlLst.add(url);
        }

        // 沒有http開頭
        Matcher mth_ = ptn.urlPtn_NonHttp.matcher(content);
        while (mth_.find()) {
            String url = mth_.group(1);
            url = unescapeJavaSilent(url);
            if (!Pattern.matches(ptn.urlDetailPtn_NonHttp, url)) {
                // System.out.println("ignore[2] -> " + url);
                continue;
            } else {
                logger.info("Add[2] -> " + url);
            }
            urlLst.add(url);
        }

        return urlLst;
    }
    
    private String unescapeJavaSilent(String url) {
        try {
            return StringEscapeUtils.unescapeJava(url);
        }catch(Exception ex) {
            System.err.println("unescapeJavaSilent ERR : " + ex.getMessage() + " -> " + url);
        }
        return url;
    }
    
    public static boolean isVideo(String fileName) {
        return fileName.matches("^.*\\." + FILE_EXTENSTION_VIDEO_PATTERN + "$");
    }
}
