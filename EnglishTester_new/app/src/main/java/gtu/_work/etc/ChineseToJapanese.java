package gtu._work.etc;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

            String result = doPostRequest_UserAgent(url, sb.toString(), "UTF8", "", AGENT);

            Pattern ptn = Pattern.compile("\\<.*?\\>");
            String rtnString = "";

            int findLinePos = -1;
            LineNumberReader reader = new LineNumberReader(new StringReader(result));
            for (String line = null; (line = reader.readLine()) != null; ) {
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

    public static String doPostRequest_UserAgent(String urlStr, String postData, String encode, String type, String userAgent) throws IOException {
        StringBuffer response = new StringBuffer();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        InputStreamReader isr = null;
        char[] buff = new char[4096];
        int size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (type == "json") {
                conn.setRequestProperty("Content-Type", "application/json");
            }
            if (conn == null)
                return "";
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            if (StringUtils.isNotBlank(userAgent)) {
                conn.setRequestProperty("User-Agent", userAgent);
            }

            os = conn.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os, "UTF-8");
            wr.write(postData);
            wr.flush();

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= Integer.MAX_VALUE) {
                    break;
                }
            }
            return response.toString();
        } finally {
            safeClose(is, os);
        }
    }

    private static void safeClose(InputStream is, OutputStream os) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }
}
