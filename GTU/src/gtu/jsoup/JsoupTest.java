package gtu.jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupTest {

    public static void main(String[] args) {
        try {
            URL url = new URL(String.format("https://www.youdao.com/w/%s", "disentangle"));
            Document xmlDoc = Jsoup.parse(url, 3000);
            System.out.println(xmlDoc);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
