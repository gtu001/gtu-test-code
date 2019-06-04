package gtu._work.etc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu.net.https.SimpleHttpsPostUtil;

public class ChineseToJapanEnglish {

    private final static int MAX_SIZE = Integer.MAX_VALUE;
    private static final String JAPAN_URL = "http://www.worldlingo.com/S000.1/api";

    public static void main(String[] args) throws Exception {
        ChineseToJapanEnglish t = new ChineseToJapanEnglish();
        // System.out.println(t.testPing());
        WordInfo2 wordInfo = t.parseToWordInfo("橫濱");
        System.out.println("done...");
    }

    public boolean testPing() {
        System.out.println("# testPing ...");
        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress(JAPAN_URL, 80);
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

    public WordInfo2 parseToWordInfo(String word) {
        String fullStr = "";
        StringBuilder sb = new StringBuilder();
        try {
            Map<String, String> map = new LinkedHashMap<String, String>();

            // map.put("wl_url",
            // "http://www.worldlingo.com/zh_tw/products_services/worldlingo_translator.html");
            map.put("wl_srcenc", "UTF-8");
            map.put("wl_trgenc", "UTF-8");

            map.put("wl_data", word);
            // map.put("wl_text", word);

            map.put("wl_srclang", Language.zh_TW.langType);// en zh_TW
            map.put("wl_trglang", Language.en.langType);
            map.put("wl_glossary", "gl1");

            map.put("wl_password", "secret");
            map.put("wl_mimetype", "text/plain");// text/html / text/plain

            map.put("wl_opt", "1");
            map.put("wl_errorstyle", "1");
            map.put("wl_dictno", "");
            map.put("Submit", "Submit");

            for (String key : map.keySet()) {
                String strValue = URLEncoder.encode(map.get(key), "UTF8");
                sb.append("&").append(key).append("=").append(strValue);
            }

            fullStr = SimpleHttpsPostUtil.newInstance().queryPage(JAPAN_URL, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringUtils.isBlank(fullStr)) {
            return new WordInfo2();
        }
        WordInfo2 wordInfo = parseToWordInfo(word, fullStr);
        return wordInfo;
    }

    public static class WordInfo2 {
        String meaning;
        String word;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getMeaning() {
            return meaning;
        }

        public void setMeaning(String meaning) {
            this.meaning = meaning;
        }
    }

    WordInfo2 parseToWordInfo(String word, String fullStr) {
        WordInfo2 newWord = new WordInfo2();
        newWord.setWord(word);
        newWord.setMeaning(fullStr);
        return newWord;
    }

    private enum Language {
        af("af", "Afrikaans"), //
        sq("sq", "Albanian"), //
        ar("ar", "Arabic"), //
        az("az", "Azerbaijani"), //
        eu("eu", "Basque"), //
        be("be", "Belarusian"), //
        bn("bn", "Bengali"), //
        bg("bg", "Bulgarian"), //
        ca("ca", "Catalan"), //
        zh_CN("zh_CN", "Chinese(Simplified)"), //
        zh_TW("zh_TW", "Chinese(Traditional)"), //
        hr("hr", "Croatian"), //
        cs("cs", "Czech"), //
        da("da", "Danish"), //
        nl("nl", "Dutch"), //
        en("en", "English"), //
        eo("eo", "Esperanto"), //
        et("et", "Estonian"), //
        fa("fa", "Farsi"), //
        tl("tl", "Filipino"), //
        fi("fi", "Finnish"), //
        fr("fr", "French"), //
        gl("gl", "Galician"), //
        ka("ka", "Georgian"), //
        de("de", "German"), //
        el("el", "Greek"), //
        gu("gu", "Gujarati"), //
        ht("ht", "Haitian Creole"), //
        ha("ha", "Hausa"), //
        he("he", "Hebrew"), //
        hi("hi", "Hindi"), //
        hu("hu", "Hungarian"), //
        is("is", "Icelandic"), //
        id("id", "Indonesian"), //
        ga("ga", "Irish"), //
        it("it", "Italian"), //
        ja("ja", "Japanese"), //
        kn("kn", "Kannada"), //
        ko("ko", "Korean"), //
        la("la", "Latin"), //
        lv("lv", "Latvian"), //
        lt("lt", "Lithuanian"), //
        mk("mk", "Macedonian"), //
        ms("ms", "Malay"), //
        mt("mt", "Maltese"), //
        no("no", "Norwegian"), //
        pl("pl", "Polish"), //
        pt("pt", "Portuguese"), //
        ro("ro", "Romanian"), //
        ru("ru", "Russian"), //
        sr("sr", "Serbian"), //
        sk("sk", "Slovak"), //
        sl("sl", "Slovenian"), //
        so("so", "Somali"), //
        es("es", "Spanish"), //
        sw("sw", "Swahili"), //
        sv("sv", "Swedish"), //
        ta("ta", "Tamil"), //
        te("te", "Telugu"), //
        th("th", "Thai"), //
        tr("tr", "Turkish"), //
        uk("uk", "Ukrainian"), //
        ur("ur", "Urdu"), //
        vi("vi", "Vietnamese"), //
        cy("cy", "Welsh"), //
        yi("yi", "Yiddish"),//
        ;
        final String langType;
        final String label;

        Language(String langType, String label) {
            this.langType = langType;
            this.label = label;
        }
    }
}
