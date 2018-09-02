package gtu.binary;

import java.util.Arrays;

public class TransCode2 {
    
    public static void main(String[] args){
        String rec = "okok哈哈哈1234";
        int[] parm = {4,9,4};//utf8 中文長度3
        String[] arry = getFieldValue(rec, parm);
        System.out.println(Arrays.toString(arry));
    }

    /**
     * @param rec
     * @param parm
     * @return
     */
    public static String[] getFieldValue(String rec, int[] parm) {
        int x = 0;
        int y = 0;
        byte[] bt = null;
        String[] ss = new String[parm.length];
        try {
            bt = rec.getBytes("UTF8");//MS950
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i : parm) {
            ss[y] = new String(bt, x, i);
            y++;
            x = x + i;
        }
        return ss;
    }
}
