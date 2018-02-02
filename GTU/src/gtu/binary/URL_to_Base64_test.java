package gtu.binary;

import java.io.UnsupportedEncodingException;

public class URL_to_Base64_test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String testStr = "測試字串";
        String string = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(testStr.getBytes("utf8"));
        System.out.println("base64 : " + string);
        byte[] arry2= org.apache.commons.codec.binary.Base64.decodeBase64(string);
        String result = new String(arry2, "utf8");
        System.out.println("result : " + result);
    }

}
