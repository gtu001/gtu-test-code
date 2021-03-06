package gtu.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Troy 2009/02/02
 * 
 */
public class NumberUtil {

    public static void main(String[] args) {
        // System.out.println(numberFormat(123456789.123456789));
        testNumberFormatTest();
    }

    private static void testNumberFormatTest() {
        // 建構時決定資料輸出格式
        // #字號當為小數後面為0時會自動去除
        DecimalFormat formatter = new DecimalFormat("#.###");

        // 輸出0.35
        System.out.println(formatter.format(0.350));

        // 透過applyPattern改變格式
        // Pattern 裡0的用處為，當需要自動補0時可以遞補
        formatter.applyPattern("0.00000");

        // 輸出1.30000
        System.out.println(formatter.format(1.3));

        // 金錢格式，輸出1,300與200,000
        formatter.applyPattern("#,###,###");
        System.out.println(formatter.format(1300));
        System.out.println(formatter.format(200000));

        // 與字串一起輸出
        formatter.applyPattern("'abcdefg'#,###,###");
        System.out.println(formatter.format(1300));
        System.out.println(formatter.format(200000));
    }

    public static String numberFormat(double num) {
        NumberFormat instance = NumberFormat.getInstance();
        return instance.format(num);
    }

    /**
     * 四舍五入1
     * 
     * @param d
     *            The Double
     * 
     * @param places
     *            保留的位数
     */
    public static String round1(double d, int places) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(places);
        nf.setMaximumFractionDigits(places);
        return nf.format(d);
    }

    /**
     * 四舍五入2(從第三位開始,捨到整數位)
     * 
     * @param d
     * @return
     */
    public static BigDecimal round2(double d) {
        BigDecimal vs = new BigDecimal(d);
        vs = vs.setScale(3, BigDecimal.ROUND_HALF_UP);
        vs = vs.setScale(2, BigDecimal.ROUND_HALF_UP);
        vs = vs.setScale(1, BigDecimal.ROUND_HALF_UP);
        vs = vs.setScale(0, BigDecimal.ROUND_HALF_UP);
        return vs;
    }

    /**
     * random(5,15) -> 產生 5~15 共 11個亂數(不重複)
     * 
     * @param start
     *            亂數起始
     * @param end
     *            亂數結束
     */
    public static List<Integer> randomInteger(int start, int end) {
        List<Integer> al = new ArrayList<Integer>();
        for (; true;) {
            double d = Math.random();
            double d2 = d * (end - start + 1) + start;
            int d22 = (int) d2;
            if (!al.contains(d22)) {
                System.out.println(d22);
                al.add(d22);
            }
            if (al.size() == (end - start + 1)) {
                break;
            }
        }
        return al;
    }

    public void numberFormatTest() {
        String value = "10000000000000.12341234324324";
        NumberFormat nf = new DecimalFormat("####,###,###,###,###.###############");
        nf.setMaximumFractionDigits(5);// 設定最大小數位
        nf.setMinimumFractionDigits(0);// 設定最小小數位
        String result = nf.format(new BigDecimal(value));
        System.out.println(result);
    }

    /**
     * 移動小數點
     * 
     * @param value
     * @moveDigit 負值往左移,正值往右移
     */
    public static BigDecimal moveDigit(String value, int moveDigit) {
        BigDecimal orignVal = new BigDecimal(value);
        orignVal = orignVal.movePointRight(moveDigit);
        return orignVal;
    }

    /**
     * -1 = 負值 0 = 0 1 = 正值
     * 
     * @param value
     * @return
     */
    public static int numberType(String value) {
        return new BigDecimal(value).signum();
    }

    public static boolean isNumber(String val, boolean thousandComma) {
        String pattern = "\\-?[\\d]+\\.?\\d*";
        if (thousandComma) {
            pattern = "\\-?[\\d,]+\\.?\\d*";
        }
        return StringUtils.trimToEmpty(val).matches(pattern);
    }

    // 他進位轉10進位
    public void otherToDecimal() {
        // option 1
        System.out.println("2轉10進位" + Integer.valueOf("1010", 2).toString());
        System.out.println("8轉10進位" + Integer.valueOf("10", 8).toString());
        System.out.println("16轉10進位" + Integer.valueOf("10", 16).toString());
        // option 2
        System.out.println("2轉10進位" + Integer.parseInt("1010", 2));
        System.out.println("8轉10進位" + Integer.parseInt("10", 8));
        System.out.println("16轉10進位" + Integer.parseInt("10", 16));
    }

    public void decimalToOther() {
        // 十進制轉成十六進制：
        System.out.println(Integer.toHexString(10));
        // 十進制轉成八進制
        System.out.println(Integer.toOctalString(10));
        // 十進制轉成二進制
        System.out.println(Integer.toBinaryString(10));
    }

    public void important() {
        System.out.println("轉二進\t" + Integer.toBinaryString(21));
        System.out.println("拿掉最右位\t" + Integer.toBinaryString(21 >> 1));
        System.out.println("最右位補0\t" + Integer.toBinaryString(21 << 1));
        // System.out.println("or \t" + Integer.toBinaryString(21 |
        // 0b11));//jdk7才支援
        // System.out.println("and \t" + Integer.toBinaryString(21 & 0b11));
        System.out.println("八進位表示\t" + 010);
        // System.out.println("二進位表示\t" + 0b110);
        System.out.println("十六進位表示\t" + 0x10);
    }

    /**
     * 電文數值format 電文格式應為 000003000000000 小數位為 六位 --> 3,000 data =
     * 000003000000000 V9Length = 6
     * 
     * @param data
     * @param V9Length
     * @param scaleFormat
     * @param toCurrency
     * @return
     */
    public static String format09V9(String data, int V9Length, String scaleFormat, boolean isCurrency) {
        String orignData = StringUtils.trimToEmpty(data).toString();
        try {
            data = StringUtils.trimToEmpty(data);
            String prefix = StringUtils.substring(data, 0, V9Length * -1);
            String suffix = StringUtils.substring(data, V9Length * -1);
            double d = Double.parseDouble(prefix + "." + suffix);
            String format = (!isCurrency ? "########################." : "###,###,###,###,###,###,###,###.") + scaleFormat;
            DecimalFormat df = new DecimalFormat(format);
            String result = df.format(d);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException("format09V9 錯誤 , 原始資料 : " + orignData + " , ERR : " + ex.getMessage(), ex);
        }
    }

    //取得被除數(為零就給1)
    public static BigDecimal getBigDecimalForDivided(Object val) {
        BigDecimal val2 = getBigDecimal(val);
        if (val2 == null) {
            return BigDecimal.ONE;
        } else if (BigDecimal.ZERO.compareTo(val2) == 0) {
            return BigDecimal.ONE;
        }
        return val2;
    }

    //任何數值轉
    public static BigDecimal getBigDecimal(Object val) {
        if (val == null) {
            return BigDecimal.ZERO;
        }
        if (val instanceof BigDecimal) {
            return (BigDecimal) val;
        }
        try {
            String tmpVal = String.valueOf(val);
            tmpVal = StringUtils.trimToEmpty(tmpVal).replaceAll(",", "");
            return new BigDecimal(tmpVal);
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    public static String formatNumberByCurrencyDigit(String oldNumber, int keepLength) {
        if (StringUtils.isBlank(oldNumber)) {
            return "";
        }
        StringBuilder sb = new StringBuilder("###,###,###,###,###,###,###,###,##0");
        if (keepLength >= 0) {
            for (int ii = 0; ii < keepLength; ii++) {
                if (ii == 0) {
                    sb.append(".");
                }
                sb.append("0");
            }
        } else if (keepLength == -1) {
            sb.append(".#####################################");
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(NumberUtil.getBigDecimal(oldNumber));
    }
}
