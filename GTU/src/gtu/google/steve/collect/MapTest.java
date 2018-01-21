package gtu.google.steve.collect;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/**
 * BiMap. A Map that guarantees unique values, and supports an inverse view.
 * Multimap. Similar to Map, but may contain duplicate keys. Has subtypes
 * SetMultimap and ListMultimap providing more specific behavior.
 * 
 * @author Troy
 */
public class MapTest {

    @Test
    public void testBiMap() {
        System.out.println("# testBiMap ...");
        ImmutableBiMap<String, String> bmap = ImmutableBiMap.<String, String> builder().put("AA", "aa").put("BB", "bb")
                .put("aa", "BB").build();

        BiMap<String, String> orgBiMap = ImmutableBiMap.copyOf(bmap);

        System.out.println("containsKey = " + orgBiMap.containsKey("AA"));
        System.out.println("containsValue = " + orgBiMap.containsValue("bb"));

        // orgBiMap.forcePut("CC", "cc");

        System.out.println("key<-->value = " + orgBiMap.inverse());
    }

    @Test
    public void testImmutableMap() {
        System.out.println("# testImmutableMap ...");
        Map<String, String> map = ImmutableMap.<String, String> of("aa", "AA", "bb", "BB");
        System.out.println(map);
    }

    @Test
    public void testListMultimap() {
        System.out.println("# testListMultimap ...");
        ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.<String, String> builder()
                .put("aa", "AA").put("bb", "BB").put("cc", "CC").put("aa", "A1").put("b1", "BB").put("cc", "C1");

        ListMultimap<String, String> listmultimap = builder.build();
        System.out.println(listmultimap);
    }

    @Test
    public void testMultimap() {
        System.out.println("# testMultimap ...");
        Multimap<String, String> multimap = ArrayListMultimap.create();

        multimap.put("AA", "33");
        multimap.put("AA", "222");
        multimap.put("AA", "555");
        multimap.put("BB", "tt");
        multimap.put("BB", "vv");

        Map<String, Collection<String>> multimap2 = multimap.asMap();
        System.out.println(multimap2);
    }

}
