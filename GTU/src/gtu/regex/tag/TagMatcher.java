package gtu.regex.tag;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.log.fakeAndroid.Log;

public class TagMatcher {

    private static final String TAG = TagMatcher.class.getSimpleName();

    public static void main(String[] args) {
        File file = new File("e:/gtu001_dropbox/Dropbox/Apps/gtu001_test/english_txt/A Life-Changing Exercise to Make You a Better Writer.htm");
        String content = FileUtil.loadFromFile(file, "UTF8");

        String startTag = "<head";
        String endTag = "</head>";
        String startPtn = "\\<head[\r\n\\s\t\\>]{1}";
        String endPtn = "\\<\\/head\\>";

        TagMatcher t = new TagMatcher(startTag, endTag, startPtn, endPtn, 0, 0, content, true);
        while (t.find()) {
            System.out.println(t.group());
            System.out.println("startPad = " + t.startPad);
        }

        System.out.println("unique = " + t.findUnique());

        System.out.println("done...");
    }

    public TagMatcher(String startTag, String endTag, String startPtnStr, String endPtnStr, Integer startTagOffset, Integer endTagOffset, String content, boolean debugMode) {
        this.startTag = startTag;
        this.endTag = endTag;
        this.content = content;
        this.debugMode = debugMode;
        this.startPtn = null;
        this.endPtn = null;
        _initPatternReference(startPtnStr, endPtnStr, startTagOffset, endTagOffset);
    }

    private void _initPatternReference(String startPtnStr, String endPtnStr, Integer startTagOffset, Integer endTagOffset) {
        this.startTagOffset = 0;
        this.endTagOffset = 0;
        if (StringUtils.isNotBlank(startPtnStr)) {
            this.startPtn = new StringUtilsByPattern(startPtnStr);
            if (startTagOffset != null) {
                this.startTagOffset = startTagOffset;
            }
        }
        if (StringUtils.isNotBlank(endPtnStr)) {
            this.endPtn = new StringUtilsByPattern(endPtnStr);
            if (endTagOffset != null) {
                this.endTagOffset = endTagOffset;
            }
        }
    }

    StringUtilsByPattern startPtn;
    StringUtilsByPattern endPtn;
    String startTag;
    String endTag;
    String content;
    int startPad = 0;

    int startTagOffset;
    int endTagOffset;

    boolean debugMode = false;
    TagMatcherInfo info;

    public boolean findUnique() {
        startPad = 0;
        info = null;
        while (find()) {
            String currentContent = this.group().groupWithoutTag();
            if (StringUtils.isBlank(currentContent)) {
                continue;
            }
            if ((startPtn != null ? !startPtn.contain(currentContent) : !currentContent.contains(startTag)) && //
                    (endPtn != null ? !endPtn.contain(currentContent) : !currentContent.contains(endTag))) {
                info = this.group();
                return true;
            }
        }
        return false;
    }

    public void reset(String content) {
        this.content = content;
        this.startPad = 0;
    }

    public boolean find() {
        Log.v(TAG, "find ------------------------------------");
        int startPos = getStartPos();
        Log.v(TAG, "-init-startPos : " + startPos + " --> " + fixLengthForDebug(StringUtils.substring(content, startPos)));
        if (startPos == -1) {
            info = null;
            return false;
        }

        int endPos = findMatchEnd(startPos);
        if (endPos == -1) {
            info = null;
            return false;
        }

        info = new TagMatcherInfo();
        info.content = content;
        info.startWithTag = startPos;
        info.startWithoutTag = startPos + getStartTagLength();
        info.endWithoutTag = endPos;
        info.endWithTag = endPos + getEndTagLength();

        Log.v(TAG, "startPos : " + startPos);
        Log.v(TAG, "endPos : " + endPos);
        Log.v(TAG, "content : " + fixLengthForDebug(content.substring(startPos, endPos)));

        startPad = startPos + getStartTagLength();
        return true;
    }

    public String appendReplacementForUnique(String replace, boolean checkResult, boolean resetContent) {
        if (info == null) {
            throw new RuntimeException("請先執行findUnique");
        }
        String contentCopy = content.toString();
        StringBuffer sb = new StringBuffer();
        sb.append(content.substring(0, info.getStartWithTag()));
        sb.append(replace);
        sb.append(content.substring(info.getEndWithTag()));
        String resultContent = sb.toString();
        if (checkResult) {
            if (StringUtils.equals(resultContent, contentCopy)) {
                String message = String.format("未被正常取代!! startTag : %s, endTag : %s , group : %s , [replce to]--> %s ", startTag, endTag, info, replace);
                throw new RuntimeException(message);
            }
        }
        if (resetContent) {
            this.reset(resultContent);
        }
        return resultContent;
    }

    public void appendReplacement(StringBuffer sb, String replace) {
        throw new UnsupportedOperationException();
    }

    public void appendTail(StringBuffer sb) {
        throw new UnsupportedOperationException();
    }

    public TagMatcherInfo group() {
        return info;
    }

    // private void Log.v(TAG, String message) {
    // if (debugMode) {
    // System.out.println(message);
    // }
    // }

    private static String fixLengthForDebug(String str) {
        if (str == null) {
            return "";
        }
        str = str.replaceAll("[\r\n]", "");
        if (str.length() > 60) {
            return StringUtils.substring(str, 0, 30) + "...." + StringUtils.substring(str, -30);
        }
        return str;
    }

    public static class TagMatcherInfo {
        String content;

        int startWithoutTag;
        int endWithoutTag;

        int startWithTag;
        int endWithTag;

        @Override
        public String toString() {
            return "TagMatcherInfo [startWithoutTag=" + startWithoutTag + ", endWithoutTag=" + endWithoutTag + ", startWithTag=" + startWithTag + ", endWithTag=" + endWithTag + ", groupWithoutTag()="
                    + fixLengthForDebug(groupWithoutTag()) + ", groupWithTag()=" + fixLengthForDebug(groupWithTag()) + "]";
        }

        public String groupWithoutTag() {
            return content.substring(startWithoutTag, endWithoutTag);
        }

        public String groupWithTag() {
            return content.substring(startWithTag, endWithTag);
        }

        public String getContent() {
            return content;
        }

        public int getStartWithoutTag() {
            return startWithoutTag;
        }

        public int getEndWithoutTag() {
            return endWithoutTag;
        }

        public int getStartWithTag() {
            return startWithTag;
        }

        public int getEndWithTag() {
            return endWithTag;
        }
    }

    private int findMatchEnd(int startPos) {
        Log.v(TAG, "----------------------------------");
        int endPos = -1;
        int token = 1;

        String tmpContent = content.substring(startPos);

        while ((endPos = getEndPos(tmpContent, token)) != -1) {
            Log.v(TAG, " -- endPos : " + endPos);

            String middleStr = tmpContent.substring(0, endPos + getEndTagLength());
            Log.v(TAG, "middleStr : " + fixLengthForDebug(middleStr));
            int startTagAppear = countMatches_startTag(middleStr);
            int endTagAppear = countMatches_endTag(middleStr);

            Log.v(TAG, "startTagAppear : " + startTagAppear);
            Log.v(TAG, "endTagAppear : " + endTagAppear);

            if (startTagAppear != endTagAppear) {
                token++;
            } else {
                break;
            }
        }
        if (endPos == -1) {
            return -1;
        }
        return endPos + startPos;
    }

    private int countMatches_startTag(String middleStr) {
        return startPtn != null ? startPtn.countMatches(middleStr) : StringUtils.countMatches(middleStr, startTag);
    }

    private int countMatches_endTag(String middleStr) {
        return endPtn != null ? endPtn.countMatches(middleStr) : StringUtils.countMatches(middleStr, endTag);
    }

    // private int getEndPos(String tmpContent, int token) {
    // return endPtn != null ? //
    // endPtn.ordinalIndexOf(tmpContent, token) : //
    // StringUtils.ordinalIndexOf(tmpContent, endTag, token);
    // }
    //
    // private int getStartPos() {
    // return startPtn != null ? //
    // startPtn.indexOf(content, startPad) : //
    // content.indexOf(startTag, startPad);
    // }

    private int getEndPos(String tmpContent, int token) {
        if (endPtn == null) {
            return StringUtils.ordinalIndexOf(tmpContent, endTag, token);
        } else {
            int pos = endPtn.ordinalIndexOf(tmpContent, token);
            if (pos == -1) {
                return -1;
            } else {
                String tmpContent2 = StringUtils.substring(tmpContent, pos, pos + endPtn.ptn.pattern().length());
                Log.v(TAG, "<<<---" + tmpContent2);
                int fixOffset = tmpContent2.indexOf(endTag);
                fixOffset = fixOffset == -1 ? 0 : fixOffset;
                return pos + fixOffset;
            }
        }

    }

    private int getStartPos() {
        if (startPtn == null) {
            return content.indexOf(startTag, startPad);
        } else {
            int pos = startPtn.indexOf(content, startPad);
            if (pos == -1) {
                return -1;
            } else {
                String tmpContent = StringUtils.substring(content, pos, pos + startPtn.ptn.pattern().length());
                Log.v(TAG, "<<<---" + tmpContent);
                int fixOffset = tmpContent.indexOf(startTag);
                fixOffset = fixOffset == -1 ? 0 : fixOffset;
                return pos + fixOffset;
            }
        }
    }

    private int getStartTagLength() {
        return startTag.length() + startTagOffset;
    }

    private int getEndTagLength() {
        return endTag.length() + endTagOffset;
    }

    public static class StringUtilsByPattern {

        private Pattern ptn = null;
        private String group;

        public StringUtilsByPattern(String ptnStr) {
            ptn = Pattern.compile(ptnStr, Pattern.MULTILINE | Pattern.DOTALL);
        }

        public StringUtilsByPattern(Pattern ptn) {
            this.ptn = ptn;
        }

        public int countMatches(String content) {
            Matcher mth = ptn.matcher(content);
            int count = 0;
            while (mth.find()) {
                count++;
            }
            return count;
        }

        public int ordinalIndexOf(String content, int token) {
            Matcher mth = ptn.matcher(content);
            int idx = 0;
            while (mth.find()) {
                idx++;
                if (idx == token) {
                    group = mth.group();
                    return mth.start();
                }
            }
            return -1;
        }

        public int indexOf(String content, int startPad) {
            if (content.length() <= startPad) {
                return -1;
            }
            content = content.substring(startPad);
            Matcher mth = ptn.matcher(content);
            while (mth.find()) {
                group = mth.group();
                return mth.start();
            }
            return -1;
        }

        public boolean contain(String content) {
            return indexOf(content, 0) != -1;
        }

        public String group() {
            return group;
        }
    }

    public String getContent() {
        return content;
    }

    // private static class Log {
    // private static void v(String tag, String message){
    // System.out.println(String.format("%s : %s", tag, message));
    // }
    // }
}