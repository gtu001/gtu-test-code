/*
 * Copyright (c) 2010-2020 IISI. All rights reserved.
 * 
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternConstant {

    /** 符合全型的範圍-只能輸入中文 */
    public static Pattern FULL_CHAR = Pattern.compile("^[\u4e00-\u9fa5|\uFE30-\uFFA0]{0,}$");

    /** 可輸入中英字，但全型符號半型符號不行 */
    public static Pattern WITHOUT_ASCII_SYMBOLE = Pattern.compile("^[\\w|\u4e00-\u9fa5]+$");

    /**
     * 身分證字號驗證
     */
    public void personIdCheck(final String value) throws IllegalArgumentException {
        /*
         * 檢查傳入的字串是否符合身分證統一編號格式。若是，不回傳任何值;若否，丟出InvalidCharacterException。
         */
        // A=10 台北市 J=18 新竹縣 S=26 高雄縣
        // B=11 台中市 K=19 苗栗縣 T=27 屏東縣
        // C=12 基隆市 L=20 台中縣 U=28 花蓮縣
        // D=13 台南市 M=21 南投縣 V=29 台東縣
        // E=14 高雄市 N=22 彰化縣 W=32 金門縣
        // F=15 台北縣 O=35 新竹市 X=30 澎湖縣
        // G=16 宜蘭縣 P=23 雲林縣 Y=31 陽明山
        // H=17 桃園縣 Q=24 嘉義縣 Z=33 連江縣
        // I=34 嘉義市 R=25 台南縣
        if (value == null) {
            throw new IllegalArgumentException("統號格式有誤:" + value);
        }
        final int iv[] = { 10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19, 20, 21, 22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30, 31, 33 };

        int sum;
        int[] ids = new int[10];
        final Pattern personIdPattern = Pattern.compile("^[A-Z][12][0-9]{8}$");
        final Matcher matcher = personIdPattern.matcher(value);
        if (!matcher.find()) {
            throw new IllegalArgumentException("統號格式有誤:" + value);
        }

        final String upperValue = value.toUpperCase();
        ids[0] = iv[upperValue.charAt(0) - 'A'];
        sum = ids[0] / 10 + ids[0] % 10 * 9;
        for (int i = 1; i <= 8; i++) {
            ids[i] = upperValue.charAt(i) - '0';
            sum += ids[i] * (9 - i);
        }
        ids[9] = upperValue.charAt(9) - '0';
        sum += ids[9];
        if ((sum % 10) != 0) {
            throw new IllegalArgumentException("統號格式有誤:" + upperValue);
        }
    }
}
