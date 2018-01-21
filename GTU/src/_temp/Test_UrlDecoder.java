package _temp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Test_UrlDecoder {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String v = URLDecoder.decode("%7B%22id%22%3A%22A839498647%22%2C%22t%22%3A%22http%3A%2F%2Fwww.scsb.com.tw%2Fcontent%2Fcard%2Fnews_061228-1.jsp%22%2C%22vc%22%3A%22%22%2C%22RemoteAddr%22%3A%22127.0.0.1%22%2C%22User-Agent%22%3A%22Mozilla%2F5.0+%28Windows+NT+10.0%3B+Win64%3B+x64%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F58.0.3029.110+Safari%2F537.36+Edge%2F16.16299%22%2C%22vid%22%3A%22MobileBill%22%7D", "utf8");
        System.out.println(v);
    }

}
