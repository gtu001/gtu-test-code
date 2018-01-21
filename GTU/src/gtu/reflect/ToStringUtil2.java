package gtu.reflect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ToStringUtil2 {

    private static class Test {
        String aaa;
        String bbb;
        boolean ddd;
    }
    
    public static void main(String[] args) {
        Test t = new Test();
        t.aaa = "aaa";
        t.bbb = null;
        t.ddd = true;
        ToStringUtil2 test = new ToStringUtil2();
        test.object = t;
        System.out.println(test.toString(true));
        System.out.println("done...");
    }

    private ToStringUtil2() {
    }
    
    private Object object;
    
    private Map<String,Object> compareFromMap = new LinkedHashMap<String,Object>();
    private Map<String,Object> breakIfNullMap = new LinkedHashMap<String,Object>();
    
    public String toString(boolean breakIfNullValue){
        if(object == null){
            return "<null>";
        }
        
        compareFromMap = getBeanMap(object);
        breakIfNullMap = getBreakIfNullValueMap(new TreeMap<String,Object>(compareFromMap));
        
        String valueStr = null;
        if(breakIfNullValue){
            valueStr = breakIfNullMap.toString();
        }else{
            valueStr = compareFromMap.toString();
        }
        
        return getObjectStr(object.getClass(), valueStr);
    }
    
    private String getObjectStr(Class<?> clz, String valueStr){
        return "<" + clz.getSimpleName() + " : " + valueStr + ">";
    }
    
    private Map<String,Object> getBreakIfNullValueMap(Map<String,Object> map){
        List<String> keys = new ArrayList<String>();
        for(String key : map.keySet()){
            Object val = map.get(key);
            if(val == null){
                keys.add(key);
            }
        }
        for(String key : keys){
            map.remove(key);
        }
        return map;
    }

    private Map<String,Object> getBeanMap(Object object){
        Map<String,Object> map = new TreeMap<String,Object>();
        Class<?> clz = object.getClass();
        for(Field f : clz.getDeclaredFields()){
            f.setAccessible(true);
            try {
                map.put(f.getName(), f.get(object));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
