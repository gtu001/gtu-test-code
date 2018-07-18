package gtu.regex.tag;

import org.apache.commons.lang3.StringUtils;

public class TagMatcher {

    public static void main(String[] args) {
        String startTag = "<";
        String endTag = ">";
        String content = "<111 333<4444> <5555<788888>>9999999>";

        TagMatcher t = new TagMatcher(startTag, endTag, content, false);
        while (t.find()) {
            System.out.println(t.group());
        }

        System.out.println("unique = "  + t.findUnique());

        System.out.println("done...");
    }

    public TagMatcher(String startTag, String endTag, String content, boolean debugMode) {
        this.startTag = startTag;
        this.endTag = endTag;
        this.content = content;
        this.debugMode = debugMode;
    }

    String startTag;
    String endTag;
    String content;
    int startPad = 0;
    boolean debugMode = false;
    TagMatcherInfo info;

    public TagMatcherInfo findUnique() {
        startPad = 0;
        info = null;
        while (find()) {
            String currentContent = this.group().groupWithoutTag();
            if (!currentContent.contains(startTag) && !currentContent.contains(endTag)) {
                return this.group();
            }
        }
        return null;
    }

    public boolean find() {
        log("find ------------------------------------");
        int startPos = content.indexOf(startTag, startPad);
        if (startPos == -1) {
            info = null;
            return false;
        }

        int endPos = findMatchEnd(startPos);

        info = new TagMatcherInfo();
        info.content = content;
        info.startWithTag = startPos;
        info.startWithoutTag = startPos + startTag.length();
        info.endWithoutTag = endPos;
        info.endWithTag = endPos + endTag.length();

        log("startPos : " + startPos);
        log("endPos : " + endPos);
        log("content : " + content.substring(startPos, endPos));

        startPad = startPos + startTag.length();
        return true;
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

    private void log(String message) {
        if (debugMode) {
            System.out.println(message);
        }
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
                    + groupWithoutTag() + ", groupWithTag()=" + groupWithTag() + "]";
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
        log("----------------------------------");
        int endPos = -1;
        int token = 1;

        String tmpContent = content.substring(startPos);

        while ((endPos = StringUtils.ordinalIndexOf(tmpContent, endTag, token)) != -1) {
            String middleStr = tmpContent.substring(0, endPos + endTag.length());
            log("middleStr : " + middleStr);
            int startTagAppear = StringUtils.countMatches(middleStr, startTag);
            int endTagAppear = StringUtils.countMatches(middleStr, endTag);

            if (startTagAppear != endTagAppear) {
                token++;
            } else {
                break;
            }
        }
        return endPos + startPos;
    }
}
