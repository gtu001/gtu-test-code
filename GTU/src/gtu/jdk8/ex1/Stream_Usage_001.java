package gtu.jdk8.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.ibm.icu.math.BigDecimal;

public class Stream_Usage_001 {

    private static final char[] CONST_ARRY;

    static {
        CONST_ARRY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    private static class GoldAsset {

        String testKey;
        int randomInt;
        BigDecimal randomBig;

        GoldAsset() {
            this.testKey = "" + CONST_ARRY[new Random().nextInt(CONST_ARRY.length)];
            this.randomInt = new Random().nextInt(10);
            this.randomBig = new BigDecimal(new Random().nextInt(10));
        }

        public String getTestKey() {
            return testKey;
        }

        public int getRandomInt() {
            return randomInt;
        }

        public BigDecimal getRandomBig() {
            return randomBig;
        }

        @Override
        public String toString() {
            return "GoldAsset [testKey=" + testKey + ", randomInt=" + randomInt + ", randomBig=" + randomBig + "]";
        }
    }

    public static void main(String[] args) {

        List<GoldAsset> list = IntStream.range(0, 100).mapToObj(i -> new GoldAsset()).collect(Collectors.toList());
        List<String> charArry = IntStream.range(0, 100).mapToObj(v -> "" + CONST_ARRY[new Random().nextInt(CONST_ARRY.length)]).collect(Collectors.toList());

        {
            int sumValue = list.stream().map(GoldAsset::getRandomInt).reduce(0, (v1, v2) -> v1 += v2);
            System.out.println("sumValue : " + sumValue);
        }

        {
            BigDecimal sumValue = list.stream().map(GoldAsset::getRandomBig).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("sumValue : " + sumValue);
        }

        {
            Map<String, GoldAsset> map = list.stream().collect(Collectors.toMap(dic -> dic.getTestKey(), dic -> dic, (v1, v2) -> v1));
            System.out.println("map : " + map);
        }

        {
            Map<String, GoldAsset> map = list.stream().collect(Collectors.toMap(GoldAsset::getTestKey, Function.identity(), (v1, v2) -> v1));
            System.out.println("map : " + map);
        }

        {
            List<GoldAsset> biggerLst = new ArrayList<>();
            list.stream().filter(v -> v.getRandomInt() >= 5).forEach(biggerLst::add);
            System.out.println("biggerLst : " + biggerLst);
        }

        {
            String maxOrderNo = charArry.stream().min(Comparator.reverseOrder()).orElse("EMPTY");
            System.out.println("maxOrderNo : " + maxOrderNo);
        }

        {
            Comparator<GoldAsset> goldAssetComparator = Comparator//
                    .comparing(//
                            GoldAsset::getTestKey, (s1, s2) -> new String(s1).compareTo(s2))//
                    .thenComparing(//
                            GoldAsset::getRandomInt, (s1, s2) -> s1.compareTo(s2)//
            );
            List<GoldAsset> list2 = list.stream().sorted(goldAssetComparator).collect(Collectors.toList());
            System.out.println(list2);
        }
        
        {
            Iterator<GoldAsset> iter = list.stream().collect(Collectors.toCollection(LinkedList::new)).descendingIterator();
        }
        
        {
            String[] arry = list.stream().map(GoldAsset::getTestKey).map(String::toUpperCase).toArray(size -> new String[list.size()]);
            System.out.println("arry : " + Arrays.toString(arry));
        }
        
        {
            GoldAsset vo = list.stream().filter(a -> a.getRandomInt() <= 5).findFirst().orElseGet(GoldAsset::new);
            System.out.println("vo : " + ReflectionToStringBuilder.toString(vo));
        }
        
        {
            List<GoldAsset> list2 = list.stream().sorted(Comparator.comparing(GoldAsset::getTestKey)).collect(Collectors.toList());
            System.out.println("list2 : " + list2);
        }
    }

}
