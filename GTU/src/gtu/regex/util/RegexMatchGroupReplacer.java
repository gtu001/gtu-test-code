package gtu.regex.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;

public class RegexMatchGroupReplacer {

    public static void main(String[] args) {
        File baseDir = new File("D:/workstuff/workspace_taida");
        List<File> fileLst = new ArrayList<>();
        FileUtil.searchFilefind(baseDir, ".*\\.java", fileLst);

        // fileLst.stream().forEach(f -> {
        // String content = FileUtil.loadFromFile(f, "UTF8");
        //// fixByPattern(content,
        // "\\@Id[\\s\\t\r\n]*?\\@GeneratedValue[\\s\\t\r\n]*?private\\s+(Long)\\s+id\\;");
        // });

        StringBuffer sb = new StringBuffer();
        sb.append("            @Id                                         \r\n");
        sb.append("            @GeneratedValue                             \r\n");
        sb.append("            @Column(name = \"material_class\")            \r\n");
        sb.append("            private Long gggggg;                        \r\n");

        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "---1---");
        map.put(2, "---2---");

        Map<Integer, String> rtnMap = new HashMap<Integer, String>();
        String result = fixByPattern(sb.toString(), "\\@Id[\\s\\t\r\n]*?(@GeneratedValue)[\\s\\t\r\n]*?\\@Column\\([^\\]]+\\)[\\s\\t\r\n]*?private\\s+(Long)\\s+(\\w+)\\;", map, rtnMap,
                Pattern.CASE_INSENSITIVE);

        System.out.println(result);

        System.out.println(rtnMap);
        System.out.println("done...");
    }

    public static String fixByPattern(String fromContent, String pattern, Map<Integer, String> fixMap, @Nullable List<Map<Integer, String>> returnDebugMapLst, @Nullable Integer appendPatternConfig) {
        StringBuffer sb = new StringBuffer();

        int patternConfig = Pattern.MULTILINE | Pattern.DOTALL;
        if (appendPatternConfig != null) {
            patternConfig = patternConfig | appendPatternConfig;
        }

        Pattern ptn = Pattern.compile(pattern, patternConfig);
        Matcher mth = ptn.matcher(fromContent);
        while (mth.find()) {
            List<String> appenderLst = new ArrayList<>();

            for (int ii = 1; ii <= mth.groupCount(); ii++) {
                int start = -1;
                if (ii == 1) {
                    start = mth.start();
                } else {
                    start = mth.end(ii - 1);
                }

                String prefix = fromContent.substring(start, mth.start(ii));
                appenderLst.add(prefix);

                // 存在才換
                if (fixMap.containsKey(ii)) {
                    appenderLst.add(StringUtils.defaultString(fixMap.get(ii)));
                } else {
                    appenderLst.add(mth.group(ii));
                }

                if (returnDebugMapLst != null) {
                    Map<Integer, String> debugMap = new TreeMap<Integer, String>();
                    debugMap.put(ii, mth.group(ii));
                    returnDebugMapLst.add(debugMap);
                }

                if (ii == mth.groupCount()) {
                    String suffix = fromContent.substring(mth.end(ii), mth.end());
                    appenderLst.add(suffix);
                }
            }

            for (int ii = 0; ii < appenderLst.size(); ii++) {
                System.out.println(">>>" + ii + " ---- " + appenderLst.get(ii));
            }

            String repStr = StringUtils.join(appenderLst, "");
            mth.appendReplacement(sb, repStr);
        }
        mth.appendTail(sb);
        return sb.toString();
    }
}
