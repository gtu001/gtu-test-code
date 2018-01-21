package gtu._work.etc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class EnglishTester_Diectory2 {

    public static void main(String[] args) throws Exception {
        EnglishTester_Diectory2 t = new EnglishTester_Diectory2();

        System.out.println(t.testPing());

        WordInfo2 wordInfo = t.parseToWordInfo("disentangle", 1);
        System.out.println("meaning=" + wordInfo.meaningList);
        System.out.println("meaning2=" + wordInfo.meaning2);
        System.out.println("sentance=" + wordInfo.exampleSentanceList);
        System.out.println("done...");
    }

    public boolean testPing() {
        System.out.println("# testPing ...");
        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress("https://tw.ichacha.net", 80);
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
        return parseToWordInfo(word, 1);
    }
    
    public WordInfo2 parseToWordInfo(String word, int page) {
        word = word.trim().replaceAll(" ", "%20");
        System.out.println(word);
        // String fullStr =
        // searchWordOnline(String.format("http://www.ichacha.net/m/%s.html",
        // word));
        // String fullStr =
        // searchWordOnline(String.format("https://tw.ichacha.net/m/%s.html",
        // word));
        String fullStr = searchWordOnline(String.format("https://tw.ichacha.net/m.aspx?q=%s&p="+page+"&l=en#bilingual", word));
        if (StringUtils.isBlank(fullStr)) {
            return new WordInfo2();
        }
        WordInfo2 wordInfo = parseToWordInfo(word, fullStr);
        return wordInfo;
    }

    public static class WordInfo2 {
        List<String> meaningList = new ArrayList<String>();
        List<Pair<String, String>> exampleSentanceList = new ArrayList<Pair<String, String>>();
        String meaning2;

        public void setMeaningList(List<String> meaningList) {
            this.meaningList = meaningList;
        }

        public void setExampleSentanceList(List<Pair<String, String>> exampleSentanceList) {
            this.exampleSentanceList = exampleSentanceList;
        }

        public List<String> getMeaningList() {
            return meaningList;
        }

        public List<Pair<String, String>> getExampleSentanceList() {
            return exampleSentanceList;
        }

        public String getMeaning2() {
            return meaning2;
        }

        public void setMeaning2(String meaning2) {
            this.meaning2 = meaning2;
        }
    }

    WordInfo2 parseToWordInfo(String word, String fullStr) {
        WordInfo2 newWord = new WordInfo2();

        String find1 = "<div class=\"base\">";
        // String find2 = "例句与用法";
        String find2 = "例句與用法";

        String exampleSentanceOrign = "";
        String meaningOrign = "";

        int meaningPos = -1;

        LineNumberReader reader = new LineNumberReader(new StringReader(fullStr));
        try {
            for (String line = null; (line = reader.readLine()) != null;) {
                if (line.contains(find2)) {
                    exampleSentanceOrign = line.substring(line.indexOf(find2) + find2.length());
                }

                if (meaningPos == -1) {
                    if (line.contains(find1)) {
                        meaningPos = reader.getLineNumber() + 1;
                    }
                } else if (meaningPos == reader.getLineNumber()) {
                    meaningOrign = line;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Pattern meaningPtn = Pattern.compile("\\<span\\sstyle=\"font\\-size\\:17px\"\\>(.*?)\\<\\/span\\>");
        List<String> meaningList = new ArrayList<String>();
        Matcher mth = meaningPtn.matcher(meaningOrign);
        while (mth.find()) {
            meaningList.add(s2t(replaceNotAllow(mth.group(1))));
        }

        Pattern exampleSentancePtn = Pattern.compile("\\<li\\>(.*?)\\<\\/li\\>");
        List<Pair<String, String>> exampleSentanceList = new ArrayList<Pair<String, String>>();
        Matcher mth2 = exampleSentancePtn.matcher(exampleSentanceOrign);
        System.out.println(exampleSentanceOrign);
        while (mth2.find()) {
            String exampleOrign1 = mth2.group(1);
            String[] exampleOrignArry = exampleOrign1.split("<br>", -1);
            if (exampleOrignArry.length != 2) {
                continue;
            }
            String example1 = replaceNotAllow(exampleOrignArry[0]);
            String example2 = s2t(replaceNotAllow(exampleOrignArry[1]));
            Pair<String, String> pair = ImmutablePair.of(example1, example2);
            exampleSentanceList.add(pair);
        }

        newWord.meaning2 = s2t(getMeaningFull(meaningOrign));
        newWord.meaningList = meaningList;
        newWord.exampleSentanceList = exampleSentanceList;
        return newWord;
    }

    private String getMeaningFull(String meaningOrign) {
        Pattern ptn = Pattern.compile("\\<.*?\\>");
        Matcher mth = ptn.matcher(meaningOrign);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            mth.appendReplacement(sb, "");
        }
        mth.appendTail(sb);
        String rtnVal = sb.toString();
        rtnVal = rtnVal.replaceAll("&nbsp;", "");
        rtnVal = rtnVal.replaceAll("；", ";");
        rtnVal = rtnVal.replaceAll("。", ".");
        return rtnVal;
    }

    private String s2t(String str) {
        // try {
        // return JChineseConvertor.getInstance().s2t(str);
        // } catch (Exception ex) {
        return str;
        // }
    }

    private String replaceNotAllow(String value) {
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

    String searchWordOnline(String urls) {
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
