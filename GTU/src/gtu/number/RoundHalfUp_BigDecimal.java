package gtu.number;
import java.math.BigDecimal;


public class RoundHalfUp_BigDecimal {
    
    /**
     * 四捨五入
     * @param args
     */
    public static void main(String[] args) {
        BigDecimal vs = new BigDecimal(100.4445);
        vs = vs.setScale(3, BigDecimal.ROUND_HALF_UP);//決定小數點後第3位的值
        System.out.println("3:" + vs.doubleValue());
        vs = vs.setScale(2, BigDecimal.ROUND_HALF_UP);// 決定小數點後第2位的值
        System.out.println("2:" + vs.doubleValue());
        vs = vs.setScale(1, BigDecimal.ROUND_HALF_UP);// 決定小數點後第1位的值
        System.out.println("1:" + vs.doubleValue());
        vs = vs.setScale(0, BigDecimal.ROUND_HALF_UP);// 決定小數點後第0位的值
        System.out.println("0:" + vs.doubleValue());
    }
}
