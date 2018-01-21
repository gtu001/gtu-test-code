package gtu.binary;

import java.security.MessageDigest;

public class Md5Util {
    public static void main(String[] args) {
        String str = "I love you";
        System.out.println(md5(str)); // 輸出 E4F58A805A6E1FD0F6BEF58C86F9CEB3
    }

    public static String md5(String str) {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] barr = md.digest(str.getBytes()); // 將 byte 陣列加密
            StringBuffer sb = new StringBuffer(); // 將 byte 陣列轉成 16 進制
            for (int i = 0; i < barr.length; i++) {
                sb.append(byte2Hex(barr[i]));
            }
            String hex = sb.toString();
            md5 = hex.toUpperCase(); // 一律轉成大寫
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    public static String byte2Hex(byte b) {
        String[] h = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        int i = b;
        if (i < 0) {
            i += 256;
        }
        return h[i / 16] + h[i % 16];
    }
}