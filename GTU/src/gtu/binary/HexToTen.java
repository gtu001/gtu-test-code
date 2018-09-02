package gtu.binary;

public class HexToTen {

    public static void main(String[] args) {
        System.out.println("done...");
    }
    
    /**
     * 10轉16進位
     */
    public static String toHex(int D1) {
        String H1 = Integer.toHexString(D1);
        System.out.println("10進位:" + D1 + "  轉16進位=" + H1);
        return H1;
    }

    /**
     * 16轉10進位
     */
    public static int hexToTen(String H2) {
        int D2 = Integer.parseInt(H2, 16);
        System.out.println("16進位:" + H2 + "  轉10進位=" + D2);
        return D2;
    }
}
