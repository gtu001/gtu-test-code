package gtu.runtime;

/**
 * @author Troy 2009/02/02
 * 
 */
public class RuntimeTest {

    public static void main(String[] args) {
        // 虛擬機器所使用的記憶體空間
        System.out.println(Runtime.getRuntime().freeMemory()); // 剩餘
        System.out.println(Runtime.getRuntime().totalMemory()); // 總可用
    }
}
