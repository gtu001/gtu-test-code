package gtu.binary;

import java.util.Vector;

/**
 * @author Troy 2009/02/02
 * 
 */
public class IntegerToBinary {

    /**
     * 二進制 基本法 number[0] 為個位數，index 越高、位數也越高
     * 
     * @param num
     * @return
     */
    public Boolean[] Binary(int num) {
        Vector v = new Vector();
        while (num / 2 != 0) {
            v.add(new Boolean(num % 2 != 0));
            num = num / 2;
        }
        v.add(new Boolean(num % 2 != 0));
        Boolean[] number = new Boolean[v.size()];
        v.toArray(number);
        return number;
    }

    /**
     * 二進制 利用BigInteger number[0] 為個位數，index 越高、位數也越高
     * 
     * @param num
     * @return
     */
    public Boolean[] Binary(java.math.BigInteger num) {
        Vector v = new Vector();
        for (int i = 0; i < num.bitLength(); i++) {
            v.add(new Boolean(num.testBit(i)));
        }
        Boolean[] number = new Boolean[v.size()];
        v.toArray(number);
        return number;
    }

    /**
     * 二進制 高手版 number[0] 為個位數，index 越高、位數也越高
     * 
     * @param num
     * @return
     */
    public Boolean[] Binary2(int num) {
        Vector v = new Vector();
        for (int i = 1; i <= num; i <<= 1) {
            v.add(new Boolean((num & i) != 0));
        }
        Boolean[] number = new Boolean[v.size()];
        v.toArray(number);
        return number;
    }

}
