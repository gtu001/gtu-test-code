package gtu.binary;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Encoder;

public class DESUtils {

    private static Key key;
    private static String KEY_STR = "myKey";

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DES");
            generator.init(new SecureRandom(KEY_STR.getBytes()));
            key = generator.generateKey();
            generator = null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getEncryptString(String str) {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byte[] strBytes = str.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptStrByte = cipher.doFinal(strBytes);
            return base64en.encode(encryptStrByte);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getDecryptString(String str) {
        BASE64Encoder base64en = new BASE64Encoder();
        try {
            byte[] strBytes = str.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] descryptStrByte = cipher.doFinal(strBytes);
            return base64en.encode(descryptStrByte);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("請輸入要加密的字符，用空格分隔.");
            return;
        }
        for (String arg : args) {
            System.out.println(arg + ":" + getEncryptString(arg));
        }
    }
}
