package com.example.englishtester.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by gtu001 on 2018/7/8.
 */

public class FileUtilGtu {
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
