package com.example.englishtester.common;

import org.apache.commons.lang3.StringUtils;

public class Base64JdkUtil {

    public static String encode(String str) {
        try {
            byte[] arry = StringUtils.trimToEmpty(str).getBytes("UTF8");
            byte[] newArry = android.util.Base64.encode(arry, android.util.Base64.DEFAULT);
            return new String(newArry, "UTF8");
        } catch (Exception e) {
            throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
        }
    }

    public static String decode(String str) {
        try {
            byte[] arry = StringUtils.trimToEmpty(str).getBytes("UTF8");
            byte[] newArry = android.util.Base64.decode(arry, android.util.Base64.DEFAULT);
            return new String(newArry, "UTF8");
        } catch (Exception e) {
            throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
        }
    }
}