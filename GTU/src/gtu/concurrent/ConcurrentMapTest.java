package gtu.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMapTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();
        map.put("AAA", "aaa");
    }
}
