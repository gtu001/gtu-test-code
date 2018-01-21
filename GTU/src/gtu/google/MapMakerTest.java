package gtu.google;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

public class MapMakerTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        MapMaker 是用來構造 ConcurrentMap 的工具類。為什麼可以把 MapMaker 叫做超級強大？看了下面的例子你就知道了。首先，它可以用來構造 ConcurrentHashMap:
            //ConcurrentHashMap with concurrency level 8 
            ConcurrentMap<String, Object> map1 = new MapMaker() 
               .concurrencyLevel(8) 
                .makeMap(); 
            
//           或者構造用各種不同 reference 作為 key 和 value 的 Map:
            //ConcurrentMap with soft reference key and weak reference value 
               .softKeys() 
               .weakValues() 
               .makeMap(); 
            
//           或者構造有自動移除時間過期項的 Map:
            //Automatically removed entries from map after 30 seconds since they are created 
            ConcurrentMap<String, Object> map3 = new MapMaker() 
               .expireAfterWrite(30, TimeUnit.SECONDS) 
               .makeMap(); 
            
//           或者構造有最大限制數目的 Map：
            //Map size grows close to the 100, the map will evict 
            //entries that are less likely to be used again 
            ConcurrentMap<String, Object> map4 = new MapMaker() 
               .maximumSize(100) 
               .makeMap(); 
            
//           或者提供當 Map 裡面不包含所 get 的項，而需要自動加入到 Map 的功能。這個功能當 Map 作為緩存的時候很有用 :
            //Create an Object to the map, when get() is missing in map 
            ConcurrentMap<String, Object> map5 = new MapMaker() 
               .makeComputingMap( 
                 new Function<String, Object>() { 
                   public Object apply(String key) { 
                     return createObject(key); 
               }}); 
            
//           這些還不是最強大的特性，最厲害的是 MapMaker 可以提供擁有以上所有特性的 Map:
            //Put all features together! 
            ConcurrentMap<String, Object> mapAll = new MapMaker() 
               .concurrencyLevel(8) 
               .softKeys() 
               .weakValues() 
               .expireAfterWrite(30, TimeUnit.SECONDS) 
               .maximumSize(100) 
               .makeComputingMap( 
                 new Function<String, Object>() { 
                   public Object apply(String key) { 
                     return createObject(key); 
                }}); 

    }
}
