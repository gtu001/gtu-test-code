package gtu.sort;

import java.util.Arrays;

/**
 * @author Troy 2009/02/02
 * 
 */
public class main {

    /**
     * @param args
     */
    public static void main(String args[]) {
        PsObj array[] = new PsObj[2];
        array[0] = new PsObj(10, "第1個物件");
        array[1] = new PsObj(-10, "第2個物件");

        System.out.println("==> 用本身的排序方式 <==");
        Arrays.sort(array);
        showArray(array);

        System.out.println("==> 用 name 的第二個字元排序 <==");
        Arrays.sort(array, new PsObjName());
        showArray(array);
    }

    private static void showArray(PsObj a[]) {
        for (int i = 0; i < a.length; i++) {
            System.out.println("Array[" + i + "] → " + a[i]);
        }
        System.out.println("=============================================");
    }

}
