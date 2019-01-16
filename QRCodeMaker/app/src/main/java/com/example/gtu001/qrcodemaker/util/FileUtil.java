package com.example.gtu001.qrcodemaker.util;

import com.example.gtu001.qrcodemaker.common.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    public static boolean moveFile(File fromFile, File destFile) {
        try {
            boolean moveOk = fromFile.renameTo(fromFile);
            if (moveOk) {
                return true;
            }
        } catch (Exception ex) {
        }

        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = destFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            in = new FileInputStream(fromFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            // write the output file
            out.flush();
            out.close();
            out = null;
            // delete the original file
            if (destFile.exists() && fromFile.length() == destFile.length()) {
                fromFile.delete();
                return true;
            }
        } catch (FileNotFoundException fnfe1) {
            throw new RuntimeException("moveFile FileNotFoundException : " + fromFile + " -> " + destFile, fnfe1);
        } catch (Exception e) {
            throw new RuntimeException("moveFile ERR : " + fromFile + " -> " + destFile + " : " + e.getMessage(), e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return false;
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
