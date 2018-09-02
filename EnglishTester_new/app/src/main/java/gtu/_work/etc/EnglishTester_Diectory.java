package gtu._work.etc;

import android.content.Context;
import android.os.Handler;
import com.example.englishtester.common.Log;
import android.widget.Toast;

import com.example.englishtester.DropboxEnglishService;
import com.example.englishtester.common.NetWorkUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnglishTester_Diectory {
    private static final String TAG = EnglishTester_Diectory.class.getSimpleName();

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

    private void toastMessage(final Context context, final String message, Handler handler) {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public WordInfo parseToWordInfo(final String word, final Context context, final Handler handler) {
        Log.v(TAG, "connectionTest -- " + NetWorkUtil.connectionTest(context));
        if (context == null || !NetWorkUtil.connectionTest(context)) {
            toastMessage(context, "連線中斷,無法取得單字資訊!", handler);
            return new WordInfo();
        }

        if (!word.contains(" ")) {
            WordInfo wordInfo = DropboxEnglishService.getRunOnUiThread(new Callable<WordInfo>() {
                @Override
                public WordInfo call() throws Exception {
                    System.out.println(word);
                    try {
                        String fullStr = searchWordOnline("https://cdict.net/?q=" + word);
                        WordInfo wordInfo = parseToWordInfo(word, fullStr);
                        return wordInfo;
                    } catch (Exception ex) {
                        return new WordInfo();
                    }
                }
            }, -1L);
            return wordInfo;
        } else {
            WordInfo wordInfo = DropboxEnglishService.getRunOnUiThread(new Callable<WordInfo>() {
                @Override
                public WordInfo call() throws Exception {
                    String word2 = word.trim().replaceAll(" ", "%20");
                    System.out.println(word2);
                    WordInfo wordInfo = new WordInfo();
                    try {
                        EnglishTester_Diectory2 dic = new EnglishTester_Diectory2();
                        EnglishTester_Diectory2.WordInfo2 info2 = dic.parseToWordInfo(word2);
                        String meaning = StringUtils.join(info2.meaningList, ";");
                        wordInfo.setMeaning(meaning);
                        wordInfo.setEnglishId(word);
                        return wordInfo;
                    } catch (Exception ex) {
                        return new WordInfo();
                    }
                }
            }, -1L);
            return wordInfo;
        }
    }

    public static class WordInfo {
        String englishId;
        String pronounce;
        String meaning;

        public String getEnglishId() {
            return englishId;
        }

        public void setEnglishId(String englishId) {
            this.englishId = englishId;
        }

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

    private WordInfo parseToWordInfo(String word, String fullStr) {
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
        newWord.englishId = word;
        newWord.pronounce = pronounce;
        newWord.meaning = meaningUnescape(meaning);
        return newWord;
    }

    public static String meaningUnescape(String meaning) {
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

    private String replaceNotAllow(String value) {
        for (; ; ) {
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

    private String searchWordOnline(String urls) {
        return SimpleHttpsUtil.newInstance().queryPage(urls);
    }

    @Deprecated
    private String searchWordOnlineOld(String urls) {
        StringBuffer sb = new StringBuffer();
        try {
            URL u = new URL(urls);
            URLConnection url = u.openConnection();
            url.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.getInputStream(), "utf8"));
            for (String line = null; (line = reader.readLine()) != null; ) {
                sb.append(line + "\n");
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return sb.toString();
    }
}
