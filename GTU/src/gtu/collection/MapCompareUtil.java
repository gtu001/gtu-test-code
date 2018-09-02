package gtu.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 由於有些專案不能用Inner class
 * 對於物件在做比對的時候變得很麻煩
 * 只能改用map作為替代方案來解決此問題
 */
public class MapCompareUtil {
    
    public static void main(String[] args){
        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("name", "gtu001");
        map1.put("email", "gtu001@gmail.com");
        map1.put("address", "板橋");
        map1.put("salary", 1000);
        
        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("name", "gtu001");
        map2.put("email", "gtu001@gmail.com");
        map2.put("address", "土城");
        map2.put("salary", 2000);
        
        Map<String,Object> map3 = new HashMap<String,Object>();
        map3.put("name", "aab001");
        map3.put("email", "aab001@gmail.com");
        map3.put("address", "中和");
        map3.put("salary", 3000);
        
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        list.add(map1);
        list.add(map2);
        list.add(map3);
        
        System.out.println(listIndexOf(list, map1, Arrays.asList("name", "email")));
        System.out.println(listIndexOf(list, map2, Arrays.asList("name", "email")));
        System.out.println(listIndexOf(list, map3, Arrays.asList("name", "email")));
        
        System.out.println("done...");
    }
    
    /**
     * 判斷Map在List裡的第幾個位置, compareKeys為要比對的key
     * @param list
     * @param map
     * @param compareKeys
     * @return
     */
    public static <T> int listIndexOf(List<Map<String,T>> list, Map<String,?> map, List<String> compareKeys){
        for(int ii = 0 ; ii < list.size() ; ii ++){
            Map<String,?> map2 = list.get(ii);
            if(mapEquals(map, map2, compareKeys)){
                return ii;
            }
        }
        return -1;
    }
    
    /**
     * 比對兩個map的值是否相等, compareKeys為要比對的key
     * @param map1
     * @param map2
     * @param compareKeys
     * @return
     */
    public static boolean mapEquals(Map<String,?> map1, Map<String,?> map2, List<String> compareKeys){
        Set<String> keys = new HashSet<String>();
        if(compareKeys != null && !compareKeys.isEmpty()){
            keys.addAll(compareKeys);
        }else{
            keys.addAll(map1.keySet());
            keys.addAll(map2.keySet());
        }
        for(String key : keys){
            Object val1 = map1.get(key);
            Object val2 = map2.get(key);
            if(val1 == null && val2 == null){
                continue;
            }else if(val1 == null || val2 == null){
                return false;
            }else if(!val1.equals(val2)){
                return false;
            }
        }
        return true;
    }
}
