package gtu.regex.tag;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import gtu.file.FileUtil;
import gtu.log.line.Log;

public class TagMatcher2 {

    private static final String TAG = TagMatcher2.class.getSimpleName();

    public static void main(String[] args) {
        File file = new File("C:\\Users\\wistronits\\Desktop\\EtfExRateQueryComponent.java");
        String content = FileUtil.loadFromFile(file, "UTF8");

        String startTag = "@Override";
        String endTag = "}";
        String startPtn = "\\@Override[\\s\\t\\n]*?public\\s+String\\s+toString\\(\\)";
        String endPtn = "return\\s*sb\\.toString\\(\\)\\;[\\s\\t\\n]*?\\}";

        TagMatcher2 t = new TagMatcher2(startTag, endTag, startPtn, endPtn, 0, 0, content);
        StringBuffer sb = new StringBuffer();
        while (t.find()) {

            System.out.println("Find -----> " + t.group().groupWithTag());

            t.appendReplacement(sb, "XXXXXXXXX");

            System.out.println("startPad = " + t.startPad);
        }
        t.appendTail(sb);

        System.out.println(sb);

        System.out.println("done...");
    }

    public TagMatcher2(String startTag, String endTag, String startPtnStr, String endPtnStr, String content) {
        this(startTag, endTag, startPtnStr, endPtnStr, 0, 0, content);
    }

    public TagMatcher2(String startTag, String endTag, String startPtnStr, String endPtnStr, Integer startTagOffset, Integer endTagOffset, String content) {
        this.startTag = startTag;
        this.endTag = endTag;
        Validate.notEmpty(this.startTag, "開始標籤不可為空!");
        Validate.notEmpty(this.endTag, "結束標籤不可為空!");
        this.content = content;
        this.startPtn = null;
        this.endPtn = null;
        _initPatternReference(startTag, endTag, startPtnStr, endPtnStr, startTagOffset, endTagOffset);
    }

    private void _initPatternReference(String startTag, String endTag, String startPtnStr, String endPtnStr, Integer startTagOffset, Integer endTagOffset) {
        this.startTagOffset = 0;
        this.endTagOffset = 0;
        if (StringUtils.isNotBlank(startPtnStr)) {
            this.startPtn = new StringUtilsByPattern(startTag, startPtnStr);
            if (startTagOffset != null) {
                this.startTagOffset = startTagOffset;
            }
        }
        if (StringUtils.isNotBlank(endPtnStr)) {
            this.endPtn = new StringUtilsByPattern(endTag, endPtnStr);
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
    StartPadHolder startPad = new StartPadHolder();

    int startTagOffset;
    int endTagOffset;

    boolean debugMode = false;
    TagMatcherInfo info;

    private boolean isPureContent(String groupContentWithoutTag) {
        if ((startPtn != null ? !startPtn.contain(groupContentWithoutTag) : !groupContentWithoutTag.contains(startTag)) && //
                (endPtn != null ? !endPtn.contain(groupContentWithoutTag) : !groupContentWithoutTag.contains(endTag))) {
            return true;
        }
        return false;
    }

    public boolean findUnique() {
        Log.v(TAG, "[findUnique] ------------------------------------Start");
        info = null;
        while (find()) {
            String currentContent = this.group().groupWithoutTag();
            if (StringUtils.isBlank(currentContent)) {
                continue;
            }
            if (isPureContent(currentContent)) {
                info = this.group();
                return true;
            }
        }
        return false;
    }

    public boolean find() {
        Log.v(TAG, "[find] ------------------------------------Start");
        startPad.setupStartPad();

        int startPos = getStartPos();
        if (startPos == -1) {
            // info = null;
            return false;
        }

        int endPos = findMatchEnd(startPos);
        if (endPos == -1) {
            // info = null;
            return false;
        }

        info = new TagMatcherInfo();
        info.content = content.toString();
        info.startWithTag = startPos;
        info.startWithoutTag = startPos + getStartTagLength();
        info.endWithoutTag = endPos;
        info.endWithTag = endPos + getEndTagLength();
        info.startPtnGroupMap = (Map<Integer, String>) (startPtn != null ? startPtn.groupMap : Collections.emptyMap());
        info.endPtnGroupMap = (Map<Integer, String>) (endPtn != null ? endPtn.groupMap : Collections.emptyMap());

        Log.v(TAG, "[TagMatcherInfo] : " + info);

        // startPad = startPos + getStartTagLength();
        startPad.startTagLength = getStartTagLength();
        return true;
    }

    private class StartPadHolder {
        int startPad = 0;
        int startTagLength = 0;
        boolean resetStartPad = false;

        private void setupStartPad() {
            if (resetStartPad) {
                startPad = 0;
                Log.v(TAG, "[startPad] reset!!");
            } else {
                startPad = startPad + startTagLength;
                if (info != null) {
                    startPad = info.getEndWithTag();
                }
            }
            resetStartPad = false;
        }
    }

    public String appendReplacementForUnique(String replace, boolean checkResult, boolean resetContent, boolean resetStartPos) {
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
            this.content = resultContent;
        }
        this.startPad.resetStartPad = resetStartPos;
        return resultContent;
    }

    public void appendReplacement(StringBuffer sb, String replace) {
        // throw new UnsupportedOperationException();
        String content = this.content.toString();
        String prefix = content.substring(startPad.startPad, this.group().getStartWithTag());
        sb.append(prefix).append(replace);
        startPad.startPad = this.group().getEndWithTag();
    }

    public void appendTail(StringBuffer sb) {
        // throw new UnsupportedOperationException();
        String content = this.content.toString();
        if (this.group() != null) {
            String suffix = content.substring(this.group().getEndWithTag());
            sb.append(suffix);
        } else {
            sb.append(content);
        }
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

        Map<Integer, String> startPtnGroupMap;
        Map<Integer, String> endPtnGroupMap;

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

        public Map<Integer, String> getStartGroupMap() {
            return startPtnGroupMap;
        }

        public Map<Integer, String> getEndGroupMap() {
            return endPtnGroupMap;
        }
    }

    private int findMatchEnd(int startPos) {
        int endPos = -1;
        int token = 1;

        String tmpContent = StringUtils.substring(content, startPos);

        while ((endPos = getEndPos(tmpContent, token)) != -1) {
            // Log.v(TAG, " -- endPos : " + endPos);

            String middleStr = tmpContent.substring(0, endPos + getEndTagLength());
            // Log.v(TAG, "middleStr : " + fixLengthForDebug(middleStr));
            int startTagAppear = countMatches_startTag(middleStr);
            int endTagAppear = countMatches_endTag(middleStr);

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
                // String tmpContent2 = StringUtils.substring(tmpContent, pos,
                // pos + endPtn.ptn.pattern().length());
                String tmpContent2 = StringUtils.substring(tmpContent, pos, pos + endPtn.group().length());
                int fixOffset = tmpContent2.indexOf(endTag);
                fixOffset = fixOffset == -1 ? 0 : fixOffset;
                int rtnPos = pos + fixOffset;
                Log.v(TAG, "[endTagPtn] [pos " + rtnPos + "/auto_offset:" + fixOffset + "] : " + tmpContent2);// ok
                return rtnPos;
            }
        }

    }

    private int getStartPos() {
        if (startPtn == null) {
            return content.indexOf(startTag, startPad.startPad);
        } else {
            int pos = startPtn.indexOf(content, startPad.startPad);
            if (pos == -1) {
                return -1;
            } else {
                int startPos = pos + startPad.startPad;
                int endPos = startPos + startPtn.group().length();
                String tmpContent = StringUtils.substring(content, startPos, endPos);
                int fixOffset = tmpContent.indexOf(startTag);
                fixOffset = fixOffset == -1 ? 0 : fixOffset;
                int rtnPos = pos + fixOffset;
                Log.v(TAG, "[startTagPtn] [pos " + rtnPos + "/auto_offset:" + fixOffset + "] : " + tmpContent);// ok

                if (info != null) {
                    rtnPos += startPad.startPad;
                }
                return rtnPos;
            }
        }
    }

    private int getStartTagLength() {
        // if (startPtn == null) {
        // return startTag.length() + startTagOffset;
        // } else {
        // return startPtn.group().length() + startTagOffset;
        // }
        return startTag.length() + startTagOffset;
    }

    private int getEndTagLength() {
        // if (endPtn == null) {
        // return endTag.length() + endTagOffset;
        // } else {
        // return endPtn.group().length() + endTagOffset;
        // }
        return endTag.length() + endTagOffset;
    }

    public static class StringUtilsByPattern {

        private String orignTag;
        private Pattern ptn = null;
        private String group;
        private Map<Integer, String> groupMap = new TreeMap<Integer, String>();

        private void doGroupMap(Matcher mth) {
            groupMap.clear();
            for (int ii = 0; ii <= mth.groupCount(); ii++) {
                groupMap.put(ii, mth.group(ii));
            }
        }

        public StringUtilsByPattern(String orignTag, String ptnStr) {
            this.orignTag = orignTag;
            ptn = Pattern.compile(ptnStr, Pattern.MULTILINE | Pattern.DOTALL);
        }

        public StringUtilsByPattern(String orignTag, Pattern ptn) {
            this.orignTag = orignTag;
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
                    this.doGroupMap(mth);
                    return mth.start();
                }
            }
            return -1;
        }

        public int indexOf(String content, int startPad) {
            if (content.length() <= startPad) {
                return -1;
            }

            // 起碼要找到 startTag or endTag
            String currentContent = StringUtils.substring(content, startPad);
            if (!currentContent.contains(this.orignTag)) {
                return -1;
            }
            Matcher mth = ptn.matcher(currentContent);
            int finalPos = -1;
            while (mth.find()) {
                group = mth.group();
                this.doGroupMap(mth);
                return mth.start();
            }
            return finalPos;
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