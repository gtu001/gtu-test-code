package gtu.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.junit.Test;

public class MapUtilsTest {

    @Test
    public void test_predicatedSortedMap() {
        try {
            SortedMap map = MapUtils.predicatedSortedMap(new TreeMap(), null, NotNullPredicate.getInstance());
            map.put("testNullValue", null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void test_putAll() {
        Map colorMap = MapUtils.putAll(new HashMap(), new String[][] { //
                { "RED", "#FF0000" }, //
                { "GREEN", "#00FF00" }, //
                { "BLUE", "#0000FF" }//
        });
        MapUtils.debugPrint(System.out, "test_putAll", colorMap);
    }

    @Test
    public void test_transformedMap() {
        Map<String, String> zmap = new HashMap<String, String>();
        zmap.put("a", "1");
        zmap.put("b", "2");
        zmap.put("c", "3");
        zmap = MapUtils.transformedMap(zmap, new Transformer() {
            @Override
            public Object transform(Object key) {
                String v = String.valueOf(key);
                return v + v;
            }
        }, new Transformer() {
            @Override
            public Object transform(Object val) {
                String v = String.valueOf(val);
                return v + v;
            }
        });
        zmap.put("d", "4");
        zmap.put("e", "5");
        zmap.put("f", "6");
        MapUtils.verbosePrint(System.out, "transformedMap", zmap);
    }

    @Test
    public void test_SwapKeyValue() {
        Map<String, String> zmap = new HashMap<String, String>();
        zmap.put("b", "2");
        zmap = MapUtils.invertMap(zmap);
        MapUtils.debugPrint(System.out, "swapKeyValue", zmap);
    }

    @Test
    public void test_multiValueMap() {
        Map<String, String> zmap = new HashMap<String, String>();
        zmap.put("b", "2");
        zmap = MapUtils.multiValueMap(zmap);
        zmap.put("a", "1");
        zmap.put("a", "2");
        zmap.put("a", "3");
        MapUtils.debugPrint(System.out, "multiValueMap", zmap);
    }

    @Test
    public void test_lazyMap() {
        Map map = MapUtils.lazyMap(new HashMap(), new Transformer() {
            @Override
            public Object transform(Object key) {
                return "LAZY_VAL___" + String.valueOf(key);
            }
        });
        Object val1 = map.get("test1");
        Object val2 = map.get("test2");
        System.out.println("val1 : " + val1);
        System.out.println("val2 : " + val2);
        MapUtils.debugPrint(System.out, "lazyMap", map);
    }

    @Test // 若value為null會變成空字串
    public void test_safeAddToMap() {
        Map<String, String> zmap = new HashMap<String, String>();
        zmap.put("b", "2");
        MapUtils.safeAddToMap(zmap, "nullVal", null);
        MapUtils.debugPrint(System.out, "safeAddToMap", zmap);
    }

    public static void main(String[] args) {

    }
}
