package gtu.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.collections4.map.LRUMap;

public class CacheTest001 {

    public static void main(String[] args) {
        Map<String, Reference<String>> cache = Collections.synchronizedMap(new WeakHashMap());
        cache.put("key", new WeakReference("value"));
        String value = (String) ((Reference) cache.get("key")).get();
        System.out.println(value);
        
        LRUMap lmap = new LRUMap(200);
    }
}
