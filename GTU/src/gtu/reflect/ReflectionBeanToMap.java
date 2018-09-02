package gtu.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class ReflectionBeanToMap {

    public static Map<String, Object> getReflectionToMap(Object object, boolean isFetchSuper, boolean sort){
        Class<?> clz = object.getClass();
        Map<String, Object> valMap = new LinkedHashMap<String, Object>();
        Method[] methods = clz.getMethods();
        if(!isFetchSuper){
            methods = clz.getDeclaredMethods();
        }
        for(Method method : methods){
            if(!Modifier.isPublic(method.getModifiers())){
                continue;
            }
            if((method.getName().startsWith("is") || method.getName().startsWith("get")) && //
                    method.getParameterTypes().length == 0 && method.getReturnType() != void.class){
                String name = method.getName().replaceFirst("^is", "").replaceFirst("^get", "");
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                try {
                    valMap.put(name, method.invoke(object, new Object[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                    valMap.put(name, "#ERROR# : " + e.getMessage());
                }
            }
        }
        if(sort){
            Map<String,Object> returnMap = new TreeMap<String,Object>();
            returnMap.putAll(valMap);
            valMap = returnMap;
        }
        return valMap;
    }
    
    public static Map<String, Object> getReflectionToMap_breakNull(Object object, boolean isFetchSuper, boolean sort){
        Map<String, Object> valMap = getReflectionToMap(object, isFetchSuper, sort);
        Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
        for(String k : valMap.keySet()){
            Object val = valMap.get(k);
            if(val != null){
                rtnMap.put(k, val);
            }
        }
        return rtnMap;
    }
    
    public static void printReflectionToMap_breakNull(String prefix, Object object){
        System.out.println(prefix + " -> " + getReflectionToMap_breakNull(object, false, true));
    }
    public static void printReflectionToMap(String prefix, Object object){
        System.out.println(prefix + " -> " + getReflectionToMap(object, false, true));
    }
}
