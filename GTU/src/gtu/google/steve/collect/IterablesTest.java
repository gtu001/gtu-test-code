package gtu.google.steve.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class IterablesTest {

    @Test
    public void testTransform() {
        System.out.println("# testTransform ...");
        Iterable<String> list = Iterables.transform(Lists.newArrayList("A", "B", "C", "D", "E"),
                new Function<String, String>() {
                    public String apply(String paramF) {
                        return paramF.toLowerCase();
                    }
                });
        System.out.println(list);
    }

    @Test
    public void testPartition() {
        System.out.println("# testPartition ...");
        // 切成兩個三個元素的list, 少的不補
        System.out.println(Iterables.partition(Lists.newArrayList("A", "B", "C", "D", "E"), 3));
        // 切成兩個七個元素的list, 少的不補
        System.out.println(Iterables.partition(Lists.newArrayList("A", "B", "C", "D", "E"), 7));
    }

    /**
     * 切List成指定大小
     */
    @Test
    public void testPaddedPartition() {
        System.out.println("# testPaddedPartition ...");
        // 切成兩個三個元素的list, 少的補null
        System.out.println(Iterables.paddedPartition(Lists.newArrayList("A", "B", "C", "D", "E"), 3));
        // 切成一個七個元素的list, 少的補null
        System.out.println(Iterables.paddedPartition(Lists.newArrayList("A", "B", "C", "D", "E"), 7));
    }

    /**
     * 只取得唯一元素List
     */
    @Test
    public void testGetOnlyElement() {
        System.out.println("# testGetOnlyElement ...");
        // 只能放只有一個元素的List否則錯
        System.out.println(Iterables.getOnlyElement(Lists.newArrayList("AAA")));
        // 若為空List則回傳預設值
        System.out.println(Iterables.getOnlyElement(new ArrayList<String>(), "BBB"));
    }

    /**
     * 取得指定位置元素
     */
    @Test
    public void testGet() {
        System.out.println("# testGet ...");
        System.out.println(Iterables.get(Lists.newArrayList("AAA", "111", "BBB", "222"), 1));
    }

    /**
     * 找到符合的第一個
     */
    @Test
    public void testFind() {
        System.out.println("# testFind ...");
        String result = Iterables.find(Lists.newArrayList("AAA", "111", "BBB", "222"), new Predicate<String>() {
            public boolean apply(String paramT) {
                return StringUtils.isNumeric(paramT);
            }
        });
        System.out.println(result);
    }

    /**
     * 過濾所有符合
     */
    @Test
    public void testFilter() {
        System.out.println("# testFilter ...");
        Iterable<String> result = Iterables.filter(Lists.newArrayList("AAA", "111", "BBB", "222"),
                new Predicate<String>() {
                    public boolean apply(String paramT) {
                        return StringUtils.isNumeric(paramT);
                    }
                });
        System.out.println(result);
    }

    /**
     * 是否包含
     */
    @Test
    public void testContains() {
        System.out.println("# testContains ...");
        System.out.println(Iterables.contains(Lists.newArrayList("AAA", "BBB", "AAA"), "AAA"));
    }

    /**
     * 統計出現次數
     */
    @Test
    public void testFrequency() {
        System.out.println("# testFrequency ...");
        System.out.println(Iterables.frequency(Lists.newArrayList("AAA", "BBB", "AAA"), "AAA"));
    }

    /**
     * 會equal每個元素
     */
    @Test
    public void testElementsEqual() {
        System.out.println("# testElementsEqual ...");
        System.out.println(Iterables.elementsEqual(Lists.newArrayList("AAA", "BBB"), Lists.newArrayList("AAA", "BBB")));
    }

    /**
     * 會一直跑不完
     */
    @Test
    public void testCycle() {
        System.out.println("# testCycle ...");
        Iterable<String> cycle = Iterables.cycle(Lists.newArrayList("AAA", "BBB"));
        int index = 0;
        for (Iterator<String> it = cycle.iterator(); it.hasNext();) {
            System.out.println(it.next());
            index++;
            if (index == 10) {
                break;
            }
        }
    }

    /**
     * 左邊後面接右邊
     */
    @Test
    public void testConcat() {
        System.out.println("# testConcat ...");
        Iterable<String> concat = Iterables.concat(Lists.newArrayList("AAA", "BBB"), Lists.newArrayList("aaa", "bbb"));
        for (Iterator<String> it = concat.iterator(); it.hasNext();) {
            System.out.println(it.next());
        }
    }

    /**
     * 任何一個符合
     */
    @Test
    public void testAny() {
        System.out.println("# testAny ...");
        System.out.println(Iterables.any(Lists.newArrayList("AAA", "BBB"), Predicates.isNull()));
        System.out.println(Iterables.any(Lists.newArrayList("AAA", "BBB"), Predicates.notNull()));
    }

    /**
     * 全部符合
     */
    @Test
    public void testAll() {
        System.out.println("# testAll ...");
        System.out.println(Iterables.all(Lists.newArrayList("AAA", "BBB"), Predicates.isNull()));
        System.out.println(Iterables.all(Lists.newArrayList("AAA", "BBB"), Predicates.notNull()));
    }

    /**
     * 左邊後面接右邊
     */
    @Test
    public void testAddAll() {
        System.out.println("# testAddAll ...");
        List<String> list = Lists.newArrayList("AAA", "BBB");
        Iterables.addAll(list, Lists.newArrayList("aaa", "bbb", "AAA"));
        System.out.println(list);
    }
}
