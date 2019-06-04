package gtu._work.etc;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import gtu.net.HttpUtil;

public class ChineseToJapanese {

    public static void main(String[] args) {
        String text = ChineseToJapanese.parseToJapanWord("淺草");
        System.out.println(text);
    }

    public static String parseToJapanWord(String chinese) {
        try {
            String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
            String url = "https://www.jcinfo.net/tw/tools/kanji";

            Map<String, String> map = new HashMap<String, String>();
            map.put("txt", chinese);
            map.put("lang", "tw");

            StringBuffer sb = new StringBuffer();
            for (String key : map.keySet()) {
                String value = map.get(key);
                sb.append("&").append(key).append("=").append(URLEncoder.encode(value, "UTF8"));
            }

            String result = HttpUtil.doPostRequest_UserAgent(url, sb.toString(), "UTF8", "", AGENT);

            Pattern ptn = Pattern.compile("\\<.*?\\>");
            String rtnString = "";

            int findLinePos = -1;
            LineNumberReader reader = new LineNumberReader(new StringReader(result));
            for (String line = null; (line = reader.readLine()) != null;) {
                int pos = reader.getLineNumber();
                line = StringUtils.defaultString(line);
                if (line.contains("<div style=\"margin-top:10px;\">日文漢字</div>")) {
                    findLinePos = pos + 1;
                }
                if (pos == findLinePos) {
                    StringBuffer sb1 = new StringBuffer();
                    Matcher mth = ptn.matcher(line);
                    while (mth.find()) {
                        mth.appendReplacement(sb1, "");
                    }
                    mth.appendTail(sb1);
                    rtnString = sb1.toString();
                    rtnString = StringUtils.defaultString(rtnString).replaceAll("[\\s\\t]", "");
                    break;
                }
            }
            return rtnString;
        } catch (Exception e) {
            throw new RuntimeException("parseToJapanWord ERR : " + e.getMessage(), e);
        }
    }
}
