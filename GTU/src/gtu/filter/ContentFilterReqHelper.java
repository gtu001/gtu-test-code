package gtu.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentFilterReqHelper {

    private static Logger logger = LoggerFactory.getLogger(ContentFilterReqHelper.class);

    /**
     * (2005/7/28) 說明：轉成中文全型字
     * 
     * @author jonz
     * @param s
     * @return
     */
    public static String toChanisesFullChar(String s) {
        if (s == null || s.equals("")) {
            return "";
        }
        char[] ca = s.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            if (ca[i] > '\200') {
                continue;
            }      // 超過這個應該都是中文字了…
            if (ca[i] == 32) {
                ca[i] = (char) 12288;
                continue;
            }  // 半型空白轉成全型空白
            if (Character.isLetterOrDigit(ca[i])) {
                ca[i] = (char) (ca[i] + 65248);
                continue;
            }  // 是有定義的字、數字及符號

            ca[i] = (char) 12288;  // 其它不合要求的，全部轉成全型空白。
        }
        return String.valueOf(ca);
    }

    /**
     * 全形字串轉換半形字串
     *
     * @param fullWidthStr
     *            非空的全形字串
     * @return 半形字串
     */
    public static String fullWidth2halfWidth(String fullWidthStr) {
        if (null == fullWidthStr || fullWidthStr.length() <= 0) {
            return "";
        }
        char[] charArray = fullWidthStr.toCharArray();
        // 對全形字元轉換的char陣列遍歷
        for (int i = 0; i < charArray.length; i++) {
            int charIntValue = (int) charArray[i];
            // 如果符合轉換關係,將對應下標之間減掉偏移量65248;如果是空格的話,直接做轉換
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    }

    public static <T> List<T> processContentFilterList(List<T> lst, Class<?> clz,
            String contentFilterReq,
            String[] fieldNames) {
        if (lst == null || lst.isEmpty()) {
            return (List<T>) Collections.emptyList();
        }
        List<T> rtnLst = new ArrayList<>();

        contentFilterReq = StringUtils.trimToEmpty(contentFilterReq);
        // 轉全形比對
        contentFilterReq = fullWidth2halfWidth(contentFilterReq);

        for (T bean : lst) {
            if (bean.getClass() != clz) {
                throw new RuntimeException("型態不符 : " + clz + ", 實際 : " + bean.getClass());
            }
            boolean isMatch = false;
            A: for (String fieldName : fieldNames) {
                Field fld = FieldUtils.getDeclaredField(clz, fieldName, true);
                if (fld == null) {
                    throw new RuntimeException("無此FieldName : " + fieldName + ", 類別 : " + clz);
                }
                try {
                    String strValue = StringUtils
                        .defaultString((String) FieldUtils.readDeclaredField(bean, fieldName, true));

                    // 轉全形比對
                    strValue = fullWidth2halfWidth(strValue);

                    if (strValue.contains(contentFilterReq)) {
                        isMatch = true;
                        break A;
                    }
                } catch (Exception e) {
                    logger.error("processContentFilterList ERR : " + e.getMessage(), e);
                }
            }
            if (isMatch) {
                rtnLst.add(bean);
            }
        }
        return rtnLst;
    }
}
