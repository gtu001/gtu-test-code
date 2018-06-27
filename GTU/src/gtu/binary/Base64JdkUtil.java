package gtu.binary;

import java.util.Base64;

import org.apache.commons.lang.StringUtils;

public class Base64JdkUtil {

    public static String encode(String str) {
        try {
            byte[] arry = StringUtils.trimToEmpty(str).getBytes("UTF8");
            return Base64.getEncoder().encodeToString(arry);
        } catch (Exception e) {
            throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
        }
    }

    public static String decode(String str) {
        try {
            return new String(Base64.getDecoder().decode(str), "UTF8");
        } catch (Exception e) {
            try {
                return CipherBase64.decode(str, "UTF8");
            } catch (Exception ex) {
                throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
            }
        }
    }
}