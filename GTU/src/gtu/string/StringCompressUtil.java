/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.string;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


/**
 * 字串壓縮/BASE64 CODEC工具.
 * 
 * @author tsaicf
 */
public class StringCompressUtil {

    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====

    /** Logger Object. */
//    final private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StringCompressUtil.class);

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop 
    //================================================
    //== [Constructors] Block Start (含init method)
    //====

    /**
     * 
     */
    private StringCompressUtil() {
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //####################################################################
    //## [Static Method] sub-block : 內部轉 BASE 64
    //####################################################################    

    /**
     * 將資料轉成base64格式
     * 
     * @param buf 欲轉換的資料
     * @return base64資料
     */
    private static String encodeBase64(byte[] buf) {
        return BASE64.encode(buf);
    }

    /**
     * 反轉base64格式回原資料
     * 
     * @param buf 欲反轉的base64格式資料
     * @return 原資料
     */
    private static byte[] decodeBase64(String b64str) {
        if (b64str == null || b64str.length() == 0) {
            return new byte[0];
        }
        return BASE64.decode(b64str);
    }

    //####################################################################
    //## [Static Method] sub-block : 內部 zip/unzip
    //####################################################################    

    /**
     * 壓縮資料
     * 
     * @param buf 欲壓縮的資料
     * @return 壓縮完成的資料
     */
    private static byte[] compress(byte[] buf) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(buf);
            gzip.close();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("資料壓縮失敗");
        }
    }

    /**
     * 解壓縮資料
     * 
     * @param buf 欲解壓縮的資料
     * @return 解壓縮完成的資料
     */
    private static byte[] uncompress(byte[] buf) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(buf));
            IOUtils.copy(gunzip, out); // TODO TEST
            return out.toByteArray();
        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
            throw new RuntimeException("資料解壓縮失敗");
        }
    }

    //####################################################################
    //## [Static Method] sub-block : 字串操作
    //####################################################################    

    /**
     * 將字串資料壓縮再轉成base64格式
     * 
     * @param buf 欲轉換的資料
     * @return base64資料
     */
    public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        final byte[] bytes;
        try {
            bytes = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encodeBase64(compress(bytes));
    }

    /**
     * 反轉base64格式再解壓縮回原字串資料
     * 
     * @param buf 欲反轉的base64格式資料
     * @return 原資料
     */
    public static String uncompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        final byte[] uncompressBase64 = uncompress(decodeBase64(str));
        try {
            return new String(uncompressBase64, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("非UTF-8編碼資料");
        }
    }

    //####################################################################
    //## [Static Method] sub-block : 檔案操作
    //####################################################################    

    /**
     * 將檔案內容轉成base64字串
     * 
     * @param file 檔案
     * @return base64字串
     * @throws IOException 當發生錯誤時
     */
    public static String readFileAsBase64String(File file) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return BASE64.encode(bytes);
    }

    /**
     * 將base64字串轉回原內容寫入檔案
     * 
     * @param bas64str base64字串
     * @param file 寫入的檔案
     * @throws IOException 當發生錯誤時
     */
    public static void writeBase64String2File(String bas64str, File file) throws IOException {
        FileUtils.writeByteArrayToFile(file, BASE64.decode(bas64str));
    }

    /**
     * 將檔案內容壓縮後轉成base64字串
     * 
     * @param file 檔案
     * @return base64字串
     * @throws IOException 當發生錯誤時
     */
    public static String readFileAsCompressBase64String(File file) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return encodeBase64(compress(bytes)); // compressBase64(bytes);
    }

    /**
     * 將base64字串轉回並解壓縮為原內容寫入檔案
     * 
     * @param bas64str base64字串
     * @param file 寫入的檔案
     * @throws IOException 當發生錯誤時
     */
    public static void writeCompressBase64String2File(String bas64str, File file) throws IOException {
        final byte[] uncompressBase64 = uncompress(decodeBase64(bas64str));
        FileUtils.writeByteArrayToFile(file, uncompressBase64); // uncompressBase64(bas64str)
    }

    //####################################################################
    //## [Static Method] sub-block : 序列化物件操作
    //####################################################################    

    /**
     * 序列化物件
     * 
     * @param obj 物件
     * @return 序列化後的byte陣列
     * @throws IOException
     */
    public static String serializeAsBase64(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        baos.close();
        final byte[] ziped = compress(baos.toByteArray());
        return encodeBase64(ziped);
    }

    public static Object deserializeFromBase64(String str64) throws IOException, ClassNotFoundException {
        final byte[] unziped = uncompress(decodeBase64(str64));
        ByteArrayInputStream bais = new ByteArrayInputStream(unziped);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object obj = ois.readObject();
        bais.close();
        ois.close();
        return obj;
    }

    //====
    //== [Static Method] Block Stop 
    //================================================

}
