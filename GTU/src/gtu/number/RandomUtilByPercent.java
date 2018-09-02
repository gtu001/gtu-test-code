package gtu.number;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultiset;

public class RandomUtilByPercent<T> {
    
    Map<T,Integer> percentMap;
    Map<T,Double[]> newPercentMap;
    
    public RandomUtilByPercent(Map<T,Integer> percentMap){
        this.percentMap = percentMap;
        this.newPercentMap = new HashMap<T,Double[]>();
        double total = 0;
        for(T key : percentMap.keySet()){
            BigDecimal temp = new BigDecimal(percentMap.get(key)).divide(BigDecimal.valueOf(100d));
            newPercentMap.put(key, new Double[]{total, (total + temp.doubleValue())});
            total += temp.doubleValue();
        }
        if(total != 1.0d){
//            throw new RuntimeException("百分比不可超過100% : " + total);
        }
    }
    
    public T nextRandomKey(){
        double temp = Math.random();
        for (T key : newPercentMap.keySet()) {
            Double[] range = newPercentMap.get(key);
            if(range[0] <= temp && temp <= range[1]){
                return key;
            }
        }
        throw new RuntimeException("無法取得對應百分比區間!! : " + temp + "-->" + configInfo());
    }
    
    private String configInfo(){
        StringBuilder sb = new StringBuilder("\n");
        for (T key : newPercentMap.keySet()) {
            sb.append(key + " = " + Arrays.toString(newPercentMap.get(key)) + "\n");
        }
        return sb.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<String,Integer> aaa = new HashMap<String,Integer>();
        aaa.put("50%", 50);
        aaa.put("30%", 30);
        aaa.put("10%", 10);
        aaa.put("5%", 5);
        aaa.put("5%1", 5);
        HashMultiset<String> sets = HashMultiset.<String>create();
        RandomUtilByPercent<String> percent = new RandomUtilByPercent<String>(aaa);
        for(int ii = 0 ; ii < 100 ; ii ++){
            sets.add(percent.nextRandomKey());
        }
        System.out.println(sets);
    }
}
