package gtu.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Troy 2009/02/02
 * 
 */
public class NumberUtil {

    public static void main(String[] args) {
//        System.out.println(numberFormat(123456789.123456789));
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
        NumberFormat nf = new DecimalFormat("#.###,###,###,###,###.###############");
        nf.setMaximumFractionDigits(5);//設定最大小數位
        nf.setMinimumFractionDigits(0);//設定最小小數位
        String result =  nf.format(new BigDecimal(value));
        System.out.println(result);
    }
}
