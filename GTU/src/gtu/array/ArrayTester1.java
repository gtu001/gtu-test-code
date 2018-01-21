package gtu.array;

import java.lang.reflect.Array;

/**
 * @author Troy 2009/05/22
 * 
 */
public class ArrayTester1 {

    /**
     * 此類根據反射來創建 一個動態的陣列
     */
    public static void main(String[] args) throws ClassNotFoundException {
        Class classType = Class.forName("java.lang.String");
        Object array = Array.newInstance(classType, 10); // 指定陣列的類型和大小

        // 對索引為5的位置進行賦值
        Array.set(array, 5, "hello");
        String s = (String) Array.get(array, 5);
        System.out.println(s);

        // 迴圈遍曆這個動態陣列
        for (int i = 0; i < ((String[]) array).length; i++) {
            String str = (String) Array.get(array, i);
            System.out.println(str);
        }
    }
}
