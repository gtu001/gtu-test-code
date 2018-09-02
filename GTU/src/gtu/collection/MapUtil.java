/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.collection;

import gtu.reflect.ReflectUtil;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Troy 2012/1/8
 */
public class MapUtil {

    public static void main(String[] args) throws Exception {
        List<String> words = Arrays.asList("one", "two", "three", "one", "three");
        MultisetZ<String> wordBag = MultisetZ.create(words);
        System.out.println(wordBag); // [two, one x 2, three x 2]
        for (String word : wordBag.elementSet()) {
            System.out.println(word + " -> " + wordBag.count(word));
        }

        MultisetZ<String> wordBag2 = MultisetZ.create();
        for (String word : words) {
            wordBag2.add(word, 10);
        }
        System.out.println(wordBag2);

        MultisetZ<String> wordBag3 = MultisetZ.create();
        for (String word : words) {
            wordBag3.setCount(word, 100);
        }
        System.out.println(wordBag3);
    }

    /**
     * 將map的值對應到bean裡去
     */
    public static <T, V> T mapToBean(T bean, Map<String, V> map) {
        try {
            for (Map.Entry<String, V> entry : map.entrySet()) {
                String field = entry.getKey();
                Field fid = bean.getClass().getDeclaredField(field);
                boolean access = fid.isAccessible();
                fid.setAccessible(true);
                fid.set(bean, entry.getValue());
                fid.setAccessible(access);
            }
            return bean;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 將bean對應到map的值裡去
     */

    public static void beanToMap(Object bean, Map<String, Object> map) {
        try {
            for (Field f : bean.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                Object value = f.get(bean);
                f.setAccessible(false);
                map.put(f.getName(), value);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 若相同的key不會將value覆蓋而會組成List
     */
    @SuppressWarnings("unchecked")
    public static <K, V, L extends Collection<V>> void putAsCollection(K key, V value, Map<K, L> map) {
        List<V> list = new ArrayList<V>();
        if (map.containsKey(key)) {
            list = (List<V>) map.get(key);
        }
        list.add(value);
        map.put(key, (L) list);
    }

    /**
     * 若相同的key不會將value覆蓋而會組成Set
     */
    @SuppressWarnings("unchecked")
    public static <K, V, S extends Set<V>> void putAsSet(K key, V value, Map<K, S> map) {
        Set<V> set = new HashSet<V>();
        if (map.containsKey(key)) {
            set = (Set<V>) map.get(key);
        }
        set.add(value);
        map.put(key, (S) set);
    }

    /**
     * 呼叫list的Type的keyFiled的為key, valueField的為value塞成Map
     */
    public static Map<?, ?> getBeanListToMap(List<?> list, String keyField, String valueField) {
        try {
            Map<Object, Object> map = new HashMap<Object, Object>();
            for (Object obj : list) {
                Object key = ReflectUtil.getField(keyField, obj);
                Object val = ReflectUtil.getField(valueField, obj);
                map.put(key, val);
            }
            return map;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <K, V> void showMapInfo(Map<K, V> map) {
        showMapInfo(map, System.out);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <K, V> void showMapInfo(Map<K, V> map, PrintStream out) {
        List<K> keyList = new ArrayList<K>(map.keySet());
        try {
            Collections.sort((List<Comparable>) keyList);
        } catch (Exception ex) {
        }
        int index = 0;
        for (K key : keyList) {
            V value = map.get(key);
            Class<?> keyClz = key != null ? key.getClass() : null;
            Class<?> valueClz = value != null ? value.getClass() : null;
            String keyClzName = keyClz != null ? keyClz.getName() : null;
            String valueClzName = valueClz != null ? valueClz.getName() : null;
            out.format("no.%d  key(%s)[%s]\tvalue(%s)[%s]\n", index, keyClzName, key, valueClzName, value);
            index++;
        }
        out.println(map.getClass() + ", size = " + map.size());
    }

    /**
     * 移除key忽略大小寫
     */
    public static <V> void removeKeyIgnorecase(String removeKey, Map<String, V> map) {
        for (String key : map.keySet()) {
            if (removeKey.equalsIgnoreCase(key)) {
                map.remove(key);
                System.out.println("移除:" + key);
            }
        }
    }

    /**
     * 取得key對應value忽略大小寫
     */
    public static <V> List<V> getIgnorecase(String fetichKey, Map<String, V> map) {
        List<V> list = new ArrayList<V>();
        for (String key : map.keySet()) {
            if (fetichKey.equalsIgnoreCase(key)) {
                list.add(map.get(key));
            }
        }
        return list;
    }

    /**
     * 判斷key是否存在忽略大小寫
     */
    public static <V> boolean constainIgnorecase(String constainKey, Map<String, V> map) {
        for (String key : map.keySet()) {
            if (constainKey.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只保留keySet的key,若map不存在加入
     */
    public static <V> void keepKey(Map<String, V> map, Set<String> keySet, V defaultVal) {
        List<String> addCol = new ArrayList<String>();
        List<String> remCol = new ArrayList<String>();
        for (String k : keySet) {
            if (!map.containsKey(k)) {
                map.put(k, defaultVal);
                addCol.add(k);
            }
        }
        for (String k : map.keySet()) {
            if (!keySet.contains(k)) {
                remCol.add(k);
            }
        }
        for (String k : remCol) {
            map.remove(k);
        }
        System.out.println("#######################");
        System.out.println("新增=>" + addCol);
        System.out.println("移除=>" + remCol);
        System.out.println("#######################");
    }

    /**
     * 用value取得key
     */
    public static <K, V> List<K> getKeyByValue(V value, Map<K, V> map) {
        List<K> list = new ArrayList<K>();
        for (K key : map.keySet()) {
            if (map.get(key).equals(value)) {
                list.add(key);
            }
        }
        return list;
    }

    public static <T> Map<List<String>, List<T>> categoryMapByGetter(List<T> array, String... methodNames) {
        Map<List<String>, List<T>> map = new LinkedHashMap<List<String>,List<T>>();
        try {
            List<Method> methodList = new ArrayList<Method>();
            for(String methodName : methodNames){
                Method method = array.get(0).getClass().getDeclaredMethod(methodName, new Class[0]);
                method.setAccessible(true);
                methodList.add(method);
            }
            
            for(T t : array){
                List<String> key = new ArrayList<String>();
                for(Method m : methodList){
                    key.add(String.valueOf(m.invoke(t, new Object[0])));
                }
                
                List<T> currentValue = new ArrayList<T>();
                if(map.containsKey(key)){
                    currentValue = map.get(key);
                }
                currentValue.add(t);
                map.put(key, currentValue);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
