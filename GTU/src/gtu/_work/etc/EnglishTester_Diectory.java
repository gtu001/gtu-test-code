package gtu._work.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;

import gtu.binary.TransCodeUtil;
import gtu.net.https.MyX509TrustManager;
import gtu.net.https.SimpleHttpsUtil;

public class EnglishTester_Diectory {

    public static void main(String[] args) throws Exception {
        EnglishTester_Diectory t = new EnglishTester_Diectory();

        System.out.println(t.testPing());

        WordInfo wordInfo = t.parseToWordInfo("hurrah");
        System.out.println("pronounce=" + wordInfo.pronounce);
        System.out.println("meaning=" + wordInfo.meaning);
        System.out.println("done...v6");
    }

    public boolean testPing() {
        System.out.println("# testPing ...");
        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress("cdict.net", 80);
            server.connect(address, 5000);
            System.out.println("測試連線成功");
        } catch (UnknownHostException e) {
            System.out.println("telnet失败");
            return false;
        } catch (IOException e) {
            System.out.println("telnet失败");
            return false;
        } finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                }
        }
        return true;
    }

    public WordInfo parseToWordInfo(String word) {
        word = word.trim().replaceAll(" ", "%20");
        System.out.println(word);
        // String fullStr = searchWordOnline("http://cdict.net/?q=" + word);
        String fullStr = SimpleHttpsUtil.newInstance().queryPage("https://cdict.net/?q=" + word);
        WordInfo wordInfo = parseToWordInfo(word, fullStr);
        return wordInfo;
    }

    public static class WordInfo {
        String pronounce;
        String meaning;

        @Override
        public String toString() {
            return meaning;
        }

        public String getPronounce() {
            return pronounce;
        }

        public void setPronounce(String pronounce) {
            this.pronounce = pronounce;
        }

        public String getMeaning() {
            return meaning;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }
    }

    WordInfo parseToWordInfo(String word, String fullStr) {
        WordInfo newWord = new WordInfo();
        Pattern fainPattern = Pattern.compile("\\<span\\sclass\\=trans\\>([^<]+)\\<\\/span\\>", Pattern.MULTILINE);
        Pattern descriptPattern = Pattern.compile("\\<meta\\sname\\=\"description\"\\scontent\\=\"([^\"]+)\">", Pattern.MULTILINE);

        String pronounce = "";
        String meaning = "";
        Matcher mth1 = fainPattern.matcher(fullStr);
        if (mth1.find()) {
            pronounce = mth1.group(1);
            pronounce = transPronounce(pronounce);
            System.out.println("找到發音!" + pronounce);
        }
        Matcher mth2 = descriptPattern.matcher(fullStr);
        if (mth2.find()) {
            meaning = mth2.group(1);
            meaning = meaning.replaceFirst(word, "");
            System.out.println("找到解釋!" + meaning);
            if (StringUtils.defaultString(meaning).contains("的中文翻譯 | 英漢字典")) {
                meaning = "";
            }
        }

        newWord.pronounce = pronounce;
        newWord.meaning = meaningUnescape(meaning);
        return newWord;
    }

    private String meaningUnescape(String meaning) {
        meaning = org.springframework.web.util.HtmlUtils.htmlUnescape(meaning);
        meaning = meaning.replaceAll(Pattern.quote("\\"), "");
        return meaning;
    }

    private String transPronounce(String context) {
        if (StringUtils.isBlank(context)) {
            return "";
        }
        context = context.replaceAll("\\/", "");
        context = context.replaceAll("<[^>]+>", "");
        Pattern ptn = Pattern.compile("\\&\\#x(\\w+)\\;");
        Matcher mth = ptn.matcher(context);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            String val = mth.group(1);
            mth.appendReplacement(sb, TransCodeUtil.toChar(val));
        }
        mth.appendTail(sb);
        String str = sb.toString();
        str = str.replaceAll("[\\s]", "");
        return str;
    }

    String replaceNotAllow(String value) {
        for (;;) {
            int startPos = value.indexOf("<");
            int endPos = value.indexOf(">");

            if (startPos == -1 || endPos == -1) {
                break;
            }

            StringBuffer sb = new StringBuffer();
            sb.append(value);
            sb.delete(startPos, endPos + 1);
            value = sb.toString();
        }
        return value;
    }

    @Deprecated
    private String searchWordOnline(String urls) {
        StringBuffer sb = new StringBuffer();
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("11.22.33.44", 8080));
//          URLConnection uc = url.openConnection(proxy);
//          uc.setRequestProperty("User-agent", "IE/6.0");
            
            URL u = new URL(urls);
            URLConnection url = u.openConnection();
            url.setRequestProperty("User-agent", "IE/6.0");
            url.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line + "\n");
            }
            reader.close();
        } catch (java.io.FileNotFoundException ex) {
            ex.printStackTrace();
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return sb.toString();
    }
}
