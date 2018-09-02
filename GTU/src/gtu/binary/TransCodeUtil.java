package gtu.binary;

public class TransCodeUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String value = "偉婕";
        for(char c : value.toCharArray()){
            String unicode = toUnicode(c);
            String str = toChar(unicode);
            System.out.println("unicode = " + unicode);
            System.out.println("str = " + str);
        }
    }

    /**
     * 偉 -> 5049
     * @param c
     * @return
     */
    public static String toUnicode(char c){
        int intVal = c;
        return Integer.toHexString(intVal);
    }
    
    public static String toChar(String unicode){
        int intVal = Integer.parseInt(unicode, 16);
        String str = new String(new char[]{(char)intVal});
        return str;
    }
}
