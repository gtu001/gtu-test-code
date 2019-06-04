package gtu._work.etc;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import gtu.net.HttpUtil;

public class EnglishToJapanese {
    public static List<String> parseToJapanWord(String english) {
        try {
            String AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
            String url = "https://www.linguee.com/english-japanese/search?source=auto&query=" + URLEncoder.encode(english, "UTF8");
            String result = HttpUtil.doGetRequest_UserAgent(url, "utf8", AGENT);
            result = StringUtils.substring(result, 0, StringUtils.indexOf(result, "<!--translation_lines-->"));
            Pattern ptn1 = Pattern.compile("\\<a\\sid\\=\\'dictEntry\\d+\\'.*?\\>(.*?)\\<\\/a\\>", Pattern.DOTALL | Pattern.MULTILINE);
            List<String> lst = new ArrayList<String>();
            Matcher mth = ptn1.matcher(result);
            while (mth.find()) {
                lst.add(mth.group(1));
            }
            return lst;
        } catch (Exception e) {
            throw new RuntimeException("parseToJapanWord ERR : " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("done...");
    }
}
