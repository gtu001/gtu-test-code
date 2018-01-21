package gtu.binary;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLDecoderEncoderTest {

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "轉碼測試TEST!";
        String encode = URLEncoder.encode(str, "UTF8");
        System.out.println("encode = " + encode);
        
        String decodeStr = "http://localhost:8080/pisso/AppAction!redir.shtml?saabApplication.AppUrl=http%3A%2F%2Fpspis.tradevan.com.tw%3A8093%2FAPSPIS%2F&saabApplication.AppId=SPIS&saabApplication.OrigImg=image%2FTopBanner1.gif";
        String decode = URLDecoder.decode(decodeStr, "UTF8");
        System.out.println("decode = " + decode);
    }
}
