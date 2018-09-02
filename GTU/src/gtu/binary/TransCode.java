package gtu.binary;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.sql.Timestamp;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Troy 2009/02/02
 * 
 */
public class TransCode {

    /**
     * Turn GB2312 into ISO8859_1
     * 
     * @param para
     * @return
     */
    public String transGB2312toISO8859_1(String para) {
        try {
            if (para == null || para.equals(""))
                para = "";
            else
                para = new String(para.getBytes("ISO8859_1"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println(e);
        }
        return para;
    }

    /**
     * Turn ISO8859_1 into GB2312
     * 
     * @param para
     * @return
     */
    public String transISO8859_1toGB2312(String para) {
        try {
            if (para == null || para.equals(""))
                para = "";
            else
                para = new String(para.getBytes("GB2312"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.out.println(e);
        }
        return para;
    }

    /**
     * 
     * 
     * @param systemFormattedMessage
     * @return
     */
    private String getSystemAlertMessage(String systemFormattedMessage) {

        // 跳脫 Javascript 特殊字元。
        String systemEscapeMessage = StringEscapeUtils.escapeJavaScript(systemFormattedMessage);

        // 轉換為 Unicode 表示。
        StringBuilder systemAlertMessageBuilder = new StringBuilder();
        char[] systemEscapeMessageCharArray = systemEscapeMessage.toCharArray();
        for (char systemEscapeMessageChar : systemEscapeMessageCharArray) {
            systemAlertMessageBuilder.append("\\u").append(
                    String.format("%04x", Integer.valueOf((int) systemEscapeMessageChar)));
        }
        String systemAlertMessage = systemAlertMessageBuilder.toString();

        return systemAlertMessage;
    }

}