package com.example.englishtester.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gtu001 on 2018/7/8.
 */

public class FileUtilGtu {

    /**
     * 取得副檔名 不含"."
     *
     * @param file
     * @return
     */
    public static String getSubName(String filepath) {
        String name = filepath;
        int pos = name.lastIndexOf(".");
        if (pos != -1) {
            name = name.substring(pos + 1);
            Pattern ptn = Pattern.compile("\\w+");
            Matcher mth = ptn.matcher(name);
            if (mth.find()) {
                return mth.group();
            }
        }
        return "";
    }

    /**
     * 讀取檔案
     *
     * @param file
     * @param encode
     * @return
     */
    public static String loadFromFile(File file, String encode) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 取得檔案的大小敘述 Ex : 100kb
     *
     * @param filelength
     * @return
     */
    public static String getSizeDescription(long filelength) {
        long size = filelength;
        String suffix = null;
        String[] suffixS = new String[]{"kb", "mb", "gb"};
        BigDecimal result = null;
        for (int ii = 0; ii < suffixS.length && size > 1024; ii++) {
            if (size / 1024 < 1024) {
                result = BigDecimal.valueOf(size).divide(new BigDecimal(1024d));
                result = result.setScale(2, RoundingMode.HALF_UP);
            }
            size = size / 1024;
            suffix = suffixS[ii];
        }
        if (suffix == null) {
            suffix = "byte";
        }
        String sizeStr = String.valueOf(size);
        if (result != null) {
            // System.out.println(size + " / " + result.toString());
            sizeStr = result.toString();
        }
        return sizeStr + suffix;
    }
}
