package gtu.string;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Troy 2009/05/22
 * 
 */
public class StringUtilForDb {

    /**
     * 取得 DB_FIELD in ('VALUE1' , 'VALUE2', ...) 的結果字串
     * 
     * @param dbField
     *            欄位名
     * @param values
     *            值
     * @return
     */
    public static String whereInSql(String dbField, List<String> values) {
        String valuesStr = values.toString();
        valuesStr = "('" + valuesStr.substring(1, valuesStr.length() - 1) + "')";
        valuesStr = valuesStr.replaceAll(",", "','");
        return " " + dbField + " IN " + StringUtils.deleteWhitespace(valuesStr) + " ";
    }

    public static void main(String[] args) {
        for (String field : Arrays.asList("aaa_bbb_ccc", "DDD_EEE_FFF")) {
            System.out.println("dbFieldToJava = " + dbFieldToJava(field));
        }

        for (String field : Arrays.asList("AaaaBbbbCccc", "dddEeeeFffff")) {
            System.out.println("javaToDbField = " + javaToDbField(field));
        }

        System.out.println("done...");
    }

    /**
     * 資料庫欄位轉為變數字串 Ex: CODE_NAME -> codeName
     * 
     * @param columnName
     * @return
     */
    public static String dbFieldToJava(String columnName) {
        String[] values = columnName.trim().toLowerCase().split("([-|_|.])");
        StringBuffer sb = new StringBuffer();
        for (int ii = 0; ii < values.length; ii++) {
            if (ii == 0 || values[ii].length() == 0) {
                sb.append(values[ii]);
            } else {
                sb.append(values[ii].substring(0, 1).toUpperCase() + values[ii].substring(1));
            }
        }
        return sb.toString();
    }

    /**
     * 將Java變數名稱轉為DB欄位名稱 Ex: AaaBbbbbCcccc --> aaa_bbbbb_ccccc
     * 
     * @param value
     * @return
     */
    public static String javaToDbField(String columnName) {
        char[] coln = columnName.trim().toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int ii = 0; ii < coln.length; ii++) {
            if (ii != 0 && Character.isUpperCase(coln[ii])) {
                sb.append("_" + Character.toLowerCase(coln[ii]));
            } else {
                sb.append(Character.toLowerCase(coln[ii]));
            }
        }
        return sb.toString();
    }
}
