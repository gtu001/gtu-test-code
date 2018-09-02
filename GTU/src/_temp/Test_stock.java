package _temp;

import java.math.BigDecimal;

public class Test_stock {

    public static void main(String[] args) {
        double value = 102d;
        double cashVal = 4d;
        double stockVal = 1d;
        
        double newValue = (value - cashVal) / (1 + (stockVal / 10));
        int stockGiveNum = ((int)(stockVal / 10 * 1000));
        int cashGiveSum = ((int)(cashVal * 1000));
        
        System.out.println("除權前股價 : " + value);
        System.out.println("現金股利 : " + cashGiveSum + "元");
        System.out.println("股票配股 : " + stockGiveNum + "股");
        
        BigDecimal bigDec = new BigDecimal(newValue);
        bigDec = bigDec.setScale(3, BigDecimal.ROUND_DOWN);
        
        int stockGiveSum = (int)(bigDec.doubleValue() * stockGiveNum);
        int giveSum = (stockGiveSum + cashGiveSum);
        
        double ROE = ((double)giveSum/1000) / value; 
        
        System.out.println("配股市值 : " + stockGiveSum);
        
        System.out.println("除息後新股價 : " + bigDec.doubleValue());
        System.out.println("總收益 : " + giveSum);
        System.out.println("ROE : " + ROE);
    }
}
