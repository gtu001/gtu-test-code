package gtu.google.steve.base;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

public class PredicatesTest {

    @Test
    public void testAnd() {
        System.out.println("# testAnd ...");
        final List<Predicate<String>> list = new ArrayList<Predicate<String>>();
        list.add(new Predicate<String>() {
            public boolean apply(String paramT) {
                return paramT != null;
            }
        });
        list.add(new Predicate<String>() {
            public boolean apply(String paramT) {
                return paramT.length() == 5;
            }
        });

        System.out.println("and = " + Predicates.<String> and(list).apply("what"));
        System.out.println("and = " + Predicates.<String> and(list).apply("what?"));
    }

    @Test
    public void testAndNotIn() {
        System.out.println("# testAndNotIn ...");
        List<String> list1 = Lists.newArrayList("1", "2", "3");
        List<String> list2 = Lists.newArrayList("1", "4", "5");
        List<String> list3 = Lists.newArrayList("1", "4", "6");
        // 不存在list1,存在list2,list3
        Predicate<String> predicates = Predicates.and(Predicates.not(Predicates.in(list1)), Predicates.in(list2),
                Predicates.in(list3));
        System.out.println("and not in (1) = " + predicates.apply("1"));
        System.out.println("and not in (4) = " + predicates.apply("4"));
    }

    @Test
    public void testAlway() {
        System.out.println("# testAlway ...");
        System.out.println("alwaysFalse = " + Predicates.<String> alwaysFalse().apply("alwayFalse"));
        System.out.println("alwaysTrue = " + Predicates.<String> alwaysTrue().apply("alwaysTrue"));
    }

    @Test
    public void testCompose() {
        System.out.println("# testCompose ...");
        List<String> list1 = Lists.newArrayList("A1", "A2", "A3");
        // 先作function在predicate
        boolean result = Predicates.compose(Predicates.in(list1), new Function<String, String>() {
            public String apply(String from) {
                return "A" + from;
            }
        }).apply("1");
        // 1->A1->判斷是否在list1
        System.out.println("compose = " + result);
    }

    @Test
    public void testEqualTo() {
        System.out.println("# testEqualTo ...");
        System.out.println("equalTo = " + Predicates.equalTo("AAAA").apply("BBBB"));
        System.out.println("equalTo = " + Predicates.equalTo("AAAA").apply("BBBB"));
    }
}
