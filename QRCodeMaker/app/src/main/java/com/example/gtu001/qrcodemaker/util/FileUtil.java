package com.example.gtu001.qrcodemaker.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

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

    /**
     * 避掉黨名的特殊符號 \/:*?"<>|
     */
    public static String escapeFilename(String filename, boolean escapeFileSeparator) {
        String replaceSeparator = "\\\\\\/";
        if (!escapeFileSeparator) {
            replaceSeparator = "";
        }
        Pattern ptn = Pattern.compile("[" + replaceSeparator + "\\:\\*\\?\"\\<\\>\\|\r\n]", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(filename);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            mth.appendReplacement(sb, "-");
        }
        mth.appendTail(sb);
        String fileName = sb.toString();
        fileName = fileName.replaceAll("[\r\n]", "");
        fileName = fileName.replaceAll("  ", " ");
        fileName = fileName.replaceAll("　", "");
        fileName = fileName.trim();
        return fileName;
    }

    /**
     * 避掉黨名的特殊符號 \/:*?"<>|
     *
     * @param filename
     * @param ignoreNotEscapeFileSepator false = 要把 \/轉成全形, true = 不把 \/轉成全形
     * @return
     */
    public static String escapeFilename_replaceToFullChar(String filename, boolean ignoreNotEscapeFileSepator) {
        StringBuffer sb = new StringBuffer();
        char[] arry = StringUtils.trimToEmpty(filename).toCharArray();
        Character cc = null;
        for (char c : arry) {
            switch (c) {
                case ':':
                    cc = '：';
                    break;//
                case '*':
                    cc = '＊';
                    break;//
                case '?':
                    cc = '？';
                    break;//
                case '"':
                    cc = '＂';
                    break;//
                case '<':
                    cc = '＜';
                    break;//
                case '>':
                    cc = '＞';
                    break;//
                case '|':
                    cc = '｜';
                    break;//
                case '\\':
                    cc = '＼';
                    break;//
                case '/':
                    cc = '／';
                    break;//
                default:
                    cc = c;
                    break;
            }

            if (c == '\r' || c == '\n') {
                continue;
            }
            if (ignoreNotEscapeFileSepator) {
                if (c == '/' || c == '\\') {
                    sb.append(c);
                    continue;
                }
            }

            sb.append(cc);
        }
        return sb.toString();
    }
}
