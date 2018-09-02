package gtu.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 * Unicode 字串工具.
 * 
 * @author 1005382
 */
public class UnicodeUtil {
    
    public static String substring_(String str, int beginIndex, int endIndex) {
        char[] ach = str.toCharArray(); // a char array copied from str
        int len = ach.length; // the length of ach
        int[] acp = new int[Character.codePointCount(ach, 0, len)];
        System.out.println("---->" + acp.length);
        int j = 0; // an index for acp

        for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(ach, i);
            acp[j++] = cp;
        }

        int[] unicode = acp;
        beginIndex = beginIndex < 0 ? 0 : beginIndex;
        endIndex = endIndex < 0 ? 0 : endIndex;
        if(beginIndex > endIndex){
            beginIndex = endIndex;
        }
        int count = (endIndex - beginIndex);
        if (count > unicode.length - beginIndex) {
            count = unicode.length - beginIndex;
        }
        String rtnVal = new String(unicode, beginIndex, count);
        return rtnVal;
    }

    /**
     * 將字串轉換成Unicode integer陣列.
     * 
     * @param str
     *            字串
     * @return Unicode integer陣列
     */
    static public int[] toCodePointArray(String str) {
        char[] ach = str.toCharArray(); // a char array copied from str
        int len = ach.length; // the length of ach
        int[] acp = new int[Character.codePointCount(ach, 0, len)];
        int j = 0; // an index for acp

        for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(ach, i);
            acp[j++] = cp;
        }
        return acp;
    }

    /*
     * public int[] toCodePointArray(String str) { int len =
     * str.codePointCount(0, str.length()); int[] acp = new int[len]; for(int i
     * = 0; i < len; i++) { acp[i] = str.codePointAt(i); } return acp; }
     */

    /**
     * 將整Unicode integer陣列轉換為字串.
     * 
     * @param buf
     *            Unicode integer陣列
     * @return 字串
     */
    static public String toString(int[] buf) {
        return new String(buf, 0, buf.length);
    }

    /**
     * 將Unicode integer陣列依所選之範圍轉換為字串.
     * 
     * @param buf
     *            Unicode integer陣列
     * @param offset
     *            the offset
     * @param len
     *            the len
     * @return 字串
     */
    static public String toString(int[] buf, int offset, int len) {
        return new String(buf, offset, len);
    }

    /**
     * 將字串依輸入長度等份切割.
     * 
     * @param str
     *            欲切割之字串
     * @param width
     *            每一段字串的長度
     * @return 切割完成之字串陣例
     */
    static public String[] sliceString(String str, int width) {
        List<String> slices = new ArrayList<String>();
        // (1) the length of str
        int len = str.length();
        // (2) Do not slice beyond here. [Modified]
        int sliceLimit = (len >= width * 2 || str.codePointCount(0, len) > width) ? str.offsetByCodePoints(len, -width) : 0;
        // the current position per char type
        int pos = 0;

        while (pos < sliceLimit) {
            int begin = pos; // (3)
            int end = str.offsetByCodePoints(pos, width); // (4) [Modified]
            slices.add(str.substring(begin, end));
            pos = end; // (5) [Modified]
        }
        slices.add(str.substring(pos)); // (6)
        return slices.toArray(new String[slices.size()]);
    }

    /**
     * 回傳原始字串之開始點到結束之內容
     * 
     * @param string
     *            原始字串
     * @param beginIndex
     *            開始點
     * @return 範圍內的字串
     */
    static public String substring(String string, int beginIndex) {
        return substring(string, beginIndex, length(string));
    }

    /**
     * 回傳原始字串之開始點到結束點間的內容
     * 
     * @param string
     *            原始字串
     * @param beginIndex
     *            開始點
     * @param endIndex
     *            結束點
     * @return 範圍內的字串
     */
    static public String substring(String string, int beginIndex, int endIndex) {
        int[] unicode = toCodePointArray(string);
        return toString(unicode, beginIndex, endIndex - beginIndex);
    }

    /**
     * 算出Unicode字串長度.
     * 
     * @param string
     *            字串
     * @return Unicode字串長度
     */
    static public int length(String string) {
        return string.codePointCount(0, string.length());
    }

    public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
            return out.toString("ISO-8859-1");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String uncompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
            return out.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String compressBase64(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            Base64 base64 = new Base64();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();
            return new String(base64.encode(out.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String uncompressBase64(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try {
            Base64 base64 = new Base64();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(base64.decode(str.getBytes()));
            GZIPInputStream gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
            return out.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 半型字串轉全型
     * 
     * @param str
     * @return
     */
    public static String converHelf2Full(String str) {
        int[] buf = toCodePointArray(str);
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == 32) {
                buf[i] = 12288;
            } else if (buf[i] > 32 && buf[i] < 127) {
                buf[i] += 65248;
            }
        }
        return toString(buf);
    }

    /**
     * 全型字串轉半型
     * 
     * @param str
     * @return
     */
    public static String converFull2Helf(String str) {
        str = str.replaceAll("〔", "[").replaceAll("〕", "]").replaceAll("’", "'");
        int[] buf = toCodePointArray(str);
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == 12288) {
                buf[i] = 32;
            } else if (buf[i] > 65280 && buf[i] < 65375) {
                buf[i] -= 65248;
            }
        }
        return toString(buf);
    }

    public static String readFileAsBase64String(File file) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return Base64.encodeBase64String(bytes);
    }

    public static void writeBase64String2File(String bas64str, File file) throws IOException {
        FileUtils.writeByteArrayToFile(file, Base64.decodeBase64(bas64str));
    }
}
