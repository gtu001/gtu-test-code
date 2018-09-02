package gtu.collection;

import java.util.EnumMap;

public class EnumMapTest {

    private enum SizeMap {
        LARGE, MEDIUM, SMALL
    }

    public static void main(String[] args) {
        // 顯示原enum的內容
        System.out.println("原本的enum的內容為：");
        for (SizeMap sm : SizeMap.values()) {
            System.out.println(sm);
        }

        // 現在，利用EnumMap重新設定SizeMap的內容
        EnumMap<SizeMap, String> em1 = new EnumMap<SizeMap, String>(SizeMap.class);
        em1.put(SizeMap.SMALL, "小號的");
        em1.put(SizeMap.MEDIUM, "中號的");
        em1.put(SizeMap.LARGE, "大號的");

        // 顯示更改後的enum的內容
        System.out.println("\n更改後的enum的內容為：");
        // EnumMap會按照enum本身的順序
        for (SizeMap sm : SizeMap.values()) {
            System.out.println(em1.get(sm));
        }

        // 現在，利用EnumMap重新設定SizeMap的內容，使用Generics
        EnumMap<SizeMap, String> em2 = new EnumMap<SizeMap, String>(SizeMap.class);
        em2.put(SizeMap.SMALL, "Generics的小號");
        em2.put(SizeMap.MEDIUM, "Generics的中號");
        em2.put(SizeMap.LARGE, "Generics的大號");

        // 顯示更改後的enum的內容
        System.out.println("\n使用Generics，更改後的enum的內容為：");
        // EnumMap會按照enum本身的順序
        for (SizeMap sm : SizeMap.values()) {
            System.out.println(em2.get(sm));
        }
    }
}
