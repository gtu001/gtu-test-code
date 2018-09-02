package gtu.binary;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class Sha256Test {

    public static void main(String[] args) {
        System.out.println(digest("SHA1", "111"));
        System.out.println(digest("SHA-256", "222"));
        System.out.println(Sha256Test.toSha256("222"));
    }

    public static String toSha256(String data) {
        return DigestUtils.sha256Hex(data);
    }

    private static String encodeHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String digest(String alg, String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(alg);
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();
            return encodeHex(digest);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    
    public static String doHmacSHA256(String magic, String body) throws Exception {
        org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
        SecretKeySpec key = new SecretKeySpec(magic.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] source = body.getBytes("UTF-8");
        String signature = URLEncoder.encode(base64.encodeToString(mac.doFinal(source)), "UTF-8");
        return signature;
    }
}
