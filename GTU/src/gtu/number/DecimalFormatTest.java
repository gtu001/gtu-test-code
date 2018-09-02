package gtu.number;

import java.text.DecimalFormat;

/**
 * @author Troy 2009/02/02
 * 
 */
public class DecimalFormatTest {

    public static void main(String[] args) {
        DecimalFormat f = new DecimalFormat();
        f.applyPattern("0.000");
        System.out.println("pattern = 0.000");
        System.out.println(f.format(2341233434344.432345D));
        System.out.println(f.format(1D));
        System.out.println(f.format(0D));
        System.out.println("1=========================");
        f.applyPattern("#.000");
        System.out.println("pattern = #.000");
        System.out.println(f.format(2341233434344.432345D));
        System.out.println(f.format(1D));
        System.out.println(f.format(0D));
        System.out.println("2=========================");
        f.applyPattern("0.###");
        System.out.println("pattern = 0.###");
        System.out.println(f.format(2341233434344.432345D));
        System.out.println(f.format(1D));
        System.out.println(f.format(0D));
        System.out.println("3=========================");
        f.applyPattern("#,###");
        System.out.println("pattern = #,###");
        System.out.println(f.format(2341233434344.432345D));
        System.out.println(f.format(1D));
        System.out.println(f.format(0D));
        System.out.println("4=========================");
        f.applyPattern("#,####");
        System.out.println("pattern = #,####");
        System.out.println(f.format(2341233434344.432345D));
        System.out.println(f.format(1D));
        System.out.println(f.format(0D));
        System.out.println("5=========================");
    }
}
