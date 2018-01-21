package gtu.string;

public class HexNumberTest {

    public static void main(String[] args) {
        int D1 = 1023;
        String H1 = Integer.toHexString(D1);
        System.out.println("10進位:" + D1 + "  轉16進位=" + H1);

        String H2 = "fff9";
        int D2 = Integer.parseInt(H2, 16);
        System.out.println("16進位:" + H2 + "  轉10進位=" + D2);
    }
}
