package gtu.jdk8.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Stream_simple_001 {

    public static void main(String[] args) {
        List<String> givenList = Arrays.asList("a", "bb", "ccc", "dd", "bb");

        {
            List<String> result = givenList.stream().collect(Collectors.toList());
            System.out.println("1 -- " + result);
        }

        {
            Set<String> result = givenList.stream().collect(Collectors.toSet());
            System.out.println("2 -- " + result);
        }

        {
            List<String> result = givenList.stream().collect(Collectors.toCollection(LinkedList::new));
            System.out.println("3 -- " + result);
        }

        {
            Map<String, Integer> result = givenList.stream()//
                    .collect(Collectors.toMap(Function.identity(), String::length, (v1, v2) -> v1));
            System.out.println("4 -- " + result);
        }

        {
            String result = givenList.stream().collect(Collectors.joining());
            System.out.println("5 -- " + result);
        }

        {
            String result = givenList.stream().collect(Collectors.joining(" ", "RRE-", "-POST"));
            System.out.println("6 -- " + result);
        }

        {
            Long result = givenList.stream().collect(Collectors.counting());
            System.out.println("7 -- " + result);
        }

        {
            DoubleSummaryStatistics result = givenList.stream()//
                    .collect(Collectors.summarizingDouble(String::length));
            System.out.println("8 -- " + result);
        }

        {
            Double result = givenList.stream()//
                    .collect(Collectors.averagingDouble(String::length));
            System.out.println("9 -- " + result);
        }

        {
            Double result = givenList.stream()//
                    .collect(Collectors.summingDouble(String::length));
            System.out.println("10 -- " + result);
        }

        {// 取得自然排序最大的那個
            Optional<String> result = givenList.stream()//
                    .collect(Collectors.maxBy(Comparator.naturalOrder()));
            System.out.println("11 -- " + result);
        }

        {
            Map<Integer, Set<String>> result = givenList.stream()//
                    .collect(Collectors.groupingBy(String::length, Collectors.toSet()));
            System.out.println("groupBy -- " + result);
        }

        {
            List<GroupByTestBean> lst1 = new ArrayList<>();
            lst1.add(new GroupByTestBean("bbb", 11));
            lst1.add(new GroupByTestBean("aaa", 22));
            lst1.add(new GroupByTestBean("bb", 5));
            lst1.add(new GroupByTestBean("ccc", 9));
            lst1.add(new GroupByTestBean("ccc", 3));
            //TODO
        }

        {
            Map<Boolean, List<String>> result = givenList.stream()//
                    .collect(Collectors.partitioningBy(s -> s.length() > 2));
            System.out.println("條件分組 -- " + result);
        }

        {
            List<Integer> lst2 = givenList.stream().map((str) -> {
                return str.length();
            }).collect(Collectors.toList());
            System.out.println("轉換 -- " + lst2);
        }

        {
            System.out.println("14 -- before");
            List<TestBean> lst = new ArrayList<TestBean>();
            TestBean a1 = new TestBean();
            a1.aaa = "aaaaa";
            lst.add(a1);
            List<TestBean2> lst2 = lst.stream().collect(new TC());
            lst2.stream().forEach((vo) -> {
                System.out.println(ReflectionToStringBuilder.toString(vo));
            });
            System.out.println("size - " + lst2.stream().count());
            System.out.println("14 -- after");
        }
    }

    private static class GroupByTestBean {
        String str;
        int val;

        GroupByTestBean(String str, int val) {
            this.str = str;
            this.val = val;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }
    }

    private static class TestBean {
        String aaa;
    }

    private static class TestBean2 {
        String bbb;
    }

    private static class TC implements Collector<TestBean, List, List<TestBean2>> {

        /*
         * a function that is used for adding a new element to an existing
         * accumulator object
         */
        @Override
        public BiConsumer<List, TestBean> accumulator() {
            return List::add;
        }

        /*
         * method is used to provide Stream with some additional information
         * that will be used for internal optimizations. In this case, we do not
         * pay attention to the elements order in a Set so that we will use
         * Characteristics.UNORDERED. To obtain more information regarding this
         * subject, check Characteristics‘ JavaDoc.
         */
        @Override
        public Set<java.util.stream.Collector.Characteristics> characteristics() {
            Set set = new HashSet<>();
            set.add(Characteristics.UNORDERED);
            return set;
        }

        /*
         * a function that is used for merging two accumulators together
         */
        @Override
        public BinaryOperator<List> combiner() {
            return (l1, l2) -> {
                l1.addAll(l2);
                return l1;
            };
        }

        /*
         * a function that is used for converting an accumulator to final result
         * type
         */
        @Override
        public Function<List, List<TestBean2>> finisher() {
            return new Function<List, List<TestBean2>>() {
                @Override
                public List<TestBean2> apply(List t) {
                    List<TestBean2> lst2 = new ArrayList<>();
                    for (int ii = 0; ii < t.size(); ii++) {
                        TestBean t1 = (TestBean) t.get(ii);
                        TestBean2 t2 = new TestBean2();
                        t2.bbb = t1.aaa;
                        lst2.add(t2);
                    }
                    return lst2;
                }
            };
        }

        @Override
        public Supplier<List> supplier() {
            return ArrayList::new;
        }
    }
}
