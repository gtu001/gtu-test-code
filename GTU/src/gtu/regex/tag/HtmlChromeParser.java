package gtu.regex.tag;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.log.fakeAndroid.Log;

public class HtmlChromeParser {

    public static final String WORD_HTML_ENCODE = "BIG5";
    public static final int HYPER_LINK_LABEL_MAX_LENGTH = 50;

    public static void main(String[] args) {
        HtmlChromeParser parser = HtmlChromeParser.newInstance();

        File file = new File("C:/Users/gtu00/OneDrive/Desktop/new_TEST/How to Prevent Injuries Even if You Work a Desk Job.html");

        String result = parser.getFromFile(file, true, "");
        String dropboxDir = parser.picDirForDropbox;
        System.out.println("picDirForDropbox -- " + dropboxDir);
        System.out.println(result);

        System.out.println("done...");
    }

    private static final String TAG = HtmlChromeParser.class.getSimpleName();

    private HtmlChromeParser() {
    }

    private String picDirForDropbox = "";

    public static HtmlChromeParser newInstance() {
        return new HtmlChromeParser();
    }

    public String getFromFile(File file) {
        return getFromFile(file, false, null);
    }

    public String getFromFile(File file, boolean isPure, String checkStr) {
        String content = FileUtil.loadFromFile(file, WORD_HTML_ENCODE);
        return getFromContent(content, isPure, checkStr);
    }

    public String getFromFileDebug(File file, boolean isPure) {
        String content = FileUtil.loadFromFile(file, WORD_HTML_ENCODE);
        return getFromContent(content, isPure, "");
    }

    public String getFromContent(String context) {
        return getFromContent(context, false, null);
    }

    public String getFromContentDebug(String content, boolean isPure) {
        return getFromContent(content, isPure, "");
    }

    public String getFromContent(String content, boolean isPure, String checkStr) {
//        log("ORIGN start : =======================================================================");
//        // log(content);
//        log("ORIGN end   : =======================================================================");

        try {
            content = getFromContentMain(content, isPure, checkStr);
        } catch (Throwable e) {
            throw new RuntimeException("getFromContent ERR : " + e.getMessage(), e);
        }

//        log("RESULT start : =======================================================================");
//        // logContent(content);
//        System.out.println(content);
//        log("RESULT end    : =======================================================================");
        return content;
    }

    private String getFromContentMain(String content, boolean isPure, String checkStr) {
        log("# getFromContentMain START...");
        content = _step0_enum_all(content, isPure);
        validateContent("_step0_hiddenHead", content, checkStr);

        // 最後做這塊才會正常
        content = org.springframework.web.util.HtmlUtils.htmlUnescape(content);
        log("# getFromContentMain END...");
        return content;
    }

    private void logContent(String content) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(content));
            for (String line = null; (line = reader.readLine()) != null;) {
                log(line);
            }
        } catch (Exception ex) {
            Log.e(TAG, "logContent ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                Log.e(TAG, "logContent ERR : " + e.getMessage(), e);
            }
        }
    }

    private void filterImageDir(String srcDesc) {
        if (StringUtils.isNotBlank(picDirForDropbox)) {
            return;
        }
        try {
            String tmp = StringUtils.trimToEmpty(srcDesc);
            tmp = URLDecoder.decode(tmp, WORD_HTML_ENCODE);
            tmp = tmp.substring(0, tmp.lastIndexOf("/"));
            tmp = org.springframework.web.util.HtmlUtils.htmlUnescape(tmp);
            picDirForDropbox = tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPicDirForDropbox() {
        return picDirForDropbox;
    }

    private static void log(Object message) {
        System.out.println(message);
        try {
            Log.v(TAG, "" + message);
        } catch (Exception ex) {
        }
    }

    private void validateContent(String stepLabel, String content, String checkStr) {
        if (checkStr == null) {
            return;
        }
        if (checkStr != null && checkStr.length() == 0) {
            log("PROC : " + stepLabel + " Done !!!");
            return;
        }
        if (!StringUtils.trimToEmpty(content).contains(checkStr)) {
            throw new RuntimeException(stepLabel + " -> 查無 : " + checkStr);
        } else {
            log("CHECK : " + stepLabel + " OK!!!");
        }
    }

    // ---------------------------------------------------------------------------------------------

    private String hiddenByTagMatcher(String startTag, String endTag, String startPtn, String endPtn, int startTagOffset, int endTagOffset, String content) {
        TagMatcher tag = new TagMatcher(startTag, endTag, startPtn, endPtn, startTagOffset, endTagOffset, content, false);
        while (tag.findUnique()) {
            tag.appendReplacementForUnique("", true, true);
        }
        String rtnVal = tag.getContent();
        System.gc();
        return rtnVal;
    }

    private String _step0_enum_all(String content, boolean isPure) {
        for (ChromeParser e : ChromeParser.values()) {
            content = e.apply(content, isPure, this);
        }
        return content;
    }

    private enum ChromeParser {
        HEAD() {
            @Override
            String apply(String content, boolean isPure, HtmlChromeParser self) {
                content = self.hiddenByTagMatcher("<head", "</head>", "\\<head[\\s\t\n\r\\>]{1}", "\\<\\/head\\>", 0, 0, content);
                return content;
            }
        },//
        SCRIPT() {
            @Override
            String apply(String content, boolean isPure, HtmlChromeParser self) {
                content = self.hiddenByTagMatcher("<script", "</script>", "", "", 0, 0, content);
                return content;
            }
        },//
        ;

        ChromeParser() {
        }

        abstract String apply(String content, boolean isPure, final HtmlChromeParser self);
    }
}
