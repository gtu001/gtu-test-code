package gtu.jdk8.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Stream_Example_001 {

    public static void main(String[] args) {
        List<String> givenList = Arrays.asList("a", "bb", "ccc", "dd", "bb");

        List<Person> persons = //
                Arrays.asList(//
                        new Person("Max", 18), //
                        new Person("Peter", 23), //
                        new Person("Pamela", 23), //
                        new Person("David", 12)//
                );//

        {
            List<String> result = givenList.stream().collect(Collectors.toList());
            System.out.println("轉型List -- " + result);
        }

        {
            Set<String> result = givenList.stream().collect(Collectors.toSet());
            System.out.println("轉型Set -- " + result);
        }

        {
            List<String> result = givenList.stream().collect(Collectors.toCollection(LinkedList::new));
            System.out.println("轉型LinkedList -- " + result);
        }

        {
            Map<String, Integer> result = givenList.stream()//
                    .collect(Collectors.toMap(Function.identity(), String::length, (v1, v2) -> v1));
            System.out.println("以鍵值轉Map -- " + result);
        }

        {
            String result = givenList.stream().collect(Collectors.joining());
            System.out.println("串接 -- " + result);
        }

        {
            String result = givenList.stream().collect(Collectors.joining(" ", "RRE-", "-POST"));
            System.out.println("串接 -- " + result);
        }

        {
            Long result = givenList.stream().collect(Collectors.counting());
            System.out.println("技數 -- " + result);
        }

        {
            DoubleSummaryStatistics result = givenList.stream()//
                    .collect(Collectors.summarizingDouble(String::length));
            System.out.println("多總數據使用 -- " + result);
        }

        {
            Double result = givenList.stream()//
                    .collect(Collectors.averagingDouble(String::length));
            System.out.println("平均 -- " + result);
        }

        {
            Double result = givenList.stream()//
                    .collect(Collectors.summingDouble(String::length));
            System.out.println("加總 -- " + result);
        }

        {
            Optional<String> result = givenList.stream()//
                    .collect(Collectors.maxBy(Comparator.naturalOrder()));
            System.out.println("取得自然排序最大的那個 -- " + result);
        }

        {
            Map<Integer, Set<String>> result = givenList.stream()//
                    .collect(Collectors.groupingBy(String::length, Collectors.toSet()));
            System.out.println("groupBy -- " + result);
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
            System.out.println("custom -- before");
            List<TestBean> lst = new ArrayList<TestBean>();
            TestBean a1 = new TestBean();
            a1.aaa = "aaaaa";
            lst.add(a1);
            List<TestBean2> lst2 = lst.stream().collect(new TC());
            lst2.stream().forEach((vo) -> {
                System.out.println(ReflectionToStringBuilder.toString(vo));
            });
            System.out.println("size - " + lst2.stream().collect(Collectors.counting()));
            System.out.println("custom -- after");
        }

        {
            List<String> myList = Arrays.asList("a1", "a2", "b1", "c2", "c1");
            myList.stream()//
                    .filter(s -> s.startsWith("c"))//
                    .map(String::toUpperCase)//
                    .sorted()//
                    .forEach(System.out::println);
        }

        {
            {
                Arrays.asList("a1", "a2", "a3").stream()//
                        .findFirst()//
                        .ifPresent(System.out::println); // a1
            }
            {
                Stream.of("a1", "a2", "a3")//
                        .findFirst()//
                        .ifPresent(System.out::println); // a1
            }
        }

        {
            IntStream.range(1, 4)//
                    .forEach(System.out::println);
        }

        {
            Arrays.stream(new int[] { 1, 2, 3 })//
                    .map(n -> 2 * n + 1)//
                    .average()//
                    .ifPresent(System.out::println); // 5.0
        }

        {
            Stream.of("a1", "a2", "a3")//
                    .map(s -> s.substring(1))//
                    .mapToInt(Integer::parseInt)//
                    .max()//
                    .ifPresent(System.out::println); // 3
        }

        {
            IntStream.range(1, 4)//
                    .mapToObj(i -> "a" + i)//
                    .forEach(System.out::println);
            // a1
            // a2
            // a3
        }

        {
            Stream.of(1.0, 2.0, 3.0)//
                    .mapToInt(Double::intValue)//
                    .mapToObj(i -> "a" + i)//
                    .forEach(System.out::println);//
            // a1
            // a2
            // a3
        }

        {
            Stream.of("d2", "a2", "b1", "b3", "c")//
                    .filter(s -> {
                        System.out.println("filter: " + s);
                        return true;
                    }).forEach(s -> System.out.println("forEach: " + s));
        }

        {
            List<TestBean> lst = Stream.of("d2", "a2", "b1", "b3", "c")//
                    .map(str -> {
                        TestBean t = new TestBean();
                        t.aaa = str;
                        return t;
                    }).collect(Collectors.toList());
            lst.stream().forEach(vo -> System.out.println(ReflectionToStringBuilder.toString(vo)));
        }

        {
            Stream.of("d2", "a2", "b1", "b3", "c").map(s -> {
                System.out.println("map: " + s);
                return s.toUpperCase();
            }).anyMatch(s -> {
                System.out.println("anyMatch: " + s);
                return s.startsWith("A");
            });
            // map: d2
            // anyMatch: D2
            // map: a2
            // anyMatch: A2
        }

        {
            System.out.println("reuse stream !!");
            Supplier<Stream<String>> streamSupplier = //
                    () -> Stream.of("d2", "a2", "b1", "b3", "c")//
                            .filter(s -> s.startsWith("a"));
            System.out.println(streamSupplier.get().anyMatch(s -> true)); // ok
            System.out.println(streamSupplier.get().noneMatch(s -> true)); // ok
        }

        {
            Map<Integer, List<Person>> personsByAge = persons.stream()//
                    .collect(Collectors.groupingBy(p -> p.age));//
            personsByAge//
                    .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
            // age 18: [Max]
            // age 23: [Peter, Pamela]
            // age 12: [David]
        }

        {
            Double averageAge = persons.stream()//
                    .collect(Collectors.averagingInt(p -> p.age));//
            System.out.println(averageAge); // 19.0
        }

        {
            Map<Integer, String> map = persons.stream()//
                    .collect(Collectors.toMap(//
                            p -> p.age, //
                            p -> p.name, //
                            (name1, name2) -> name1 + ";" + name2));//

            System.out.println(map);
            // {18=Max, 23=Peter;Pamela, 12=David}
        }

        {
            Collector<Person, StringJoiner, String> personNameCollector = //
                    Collector.of(//
                            () -> new StringJoiner(" | "), // supplier
                            (j, p) -> j.add(p.name.toUpperCase()), // accumulator
                            (j1, j2) -> j1.merge(j2), // combiner
                            StringJoiner::toString); // finisher

            String names = persons//
                    .stream()//
                    .collect(personNameCollector);//

            System.out.println(names); // MAX | PETER | PAMELA | DAVID
        }

        {
            Collector<Person, List<Person>, Map<Integer, List<Person>>> lstToMapCollector = Collector.<Person, List<Person>, Map<Integer, List<Person>>> of(//
                    () -> new ArrayList<>(), // supplier
                    (lst, p) -> lst.add(p), // accumulator
                    (lst1, lst2) -> { // combiner
                        lst1.addAll(lst2);
                        return lst2;
                    }, (lst) -> {// finisher
                        Map<Integer, List<Person>> map = new HashMap<Integer, List<Person>>();
                        for (int ii = 0; ii < lst.size(); ii++) {
                            List<Person> tmpLst = new ArrayList<Person>();
                            Person p = lst.get(ii);
                            if (map.containsKey(p.age)) {
                                tmpLst = map.get(p.age);
                            }
                            tmpLst.add(p);
                            map.put(p.age, tmpLst);
                        }
                        return map;
                    });
            Map<Integer, List<Person>> map = persons.stream().collect(lstToMapCollector);
            System.out.println("lstToMapCollector -- " + map);
        }

        {
            {
                List<Foo> foos = new ArrayList<>();

                // create foos
                IntStream//
                        .range(1, 4)//
                        .forEach(i -> foos.add(new Foo("Foo" + i)));//

                // create bars
                foos.forEach(f -> //
                IntStream//
                        .range(1, 4)//
                        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));//

                foos.stream().flatMap(f -> f.bars.stream()).forEach(b -> System.out.println(b.name));

                // Bar1 <- Foo1
                // Bar2 <- Foo1
                // Bar3 <- Foo1
                // Bar1 <- Foo2
                // Bar2 <- Foo2
                // Bar3 <- Foo2
                // Bar1 <- Foo3
                // Bar2 <- Foo3
                // Bar3 <- Foo3
            }
            // 上面等於下面
            {
                IntStream.range(1, 4)//
                        .mapToObj(i -> new Foo("Foo" + i))//
                        .peek(f -> IntStream.range(1, 4)//
                                .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))//
                                .forEach(f.bars::add))//
                        .flatMap(f -> f.bars.stream())//
                        .forEach(b -> System.out.println(b.name));//
            }
        }

        {
            Optional.of(new Outer())//
                    .flatMap(o -> Optional.ofNullable(o.nested))//
                    .flatMap(n -> Optional.ofNullable(n.inner))//
                    .flatMap(i -> Optional.ofNullable(i.foo))//
                    .ifPresent(System.out::println);//
        }

        {
            persons//
                    .stream()//
                    .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)//
                    .ifPresent(System.out::println); // Pamela
        }

        {
            Person result = //
                    persons//
                            .stream()//
                            .reduce(new Person("", 0), (p1, p2) -> {//
                                p1.age += p2.age;
                                p1.name += p2.name + ",";
                                return p1;
                            });

            System.out.format("name=%s; age=%s %n", result.name, result.age);
            // name=MaxPeterPamelaDavid; age=76
        }

        {
            Integer ageSum = persons//
                    .stream()//
                    .reduce(0, //
                            (sum, p) -> {//
                                System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                                return sum += p.age;
                            }, //
                            (sum1, sum2) -> {
                                System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                                return sum1 + sum2;
                            });

            System.out.println("ageSum : " + ageSum);

            // accumulator: sum=0; person=Max
            // accumulator: sum=18; person=Peter
            // accumulator: sum=41; person=Pamela
            // accumulator: sum=64; person=David
        }

        {// 多線程作法無視順序
            Integer ageSum = persons//
                    .parallelStream()//
                    .reduce(0, //
                            (sum, p) -> {//
                                System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
                                return sum += p.age;
                            }, //
                            (sum1, sum2) -> {
                                System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
                                return sum1 + sum2;
                            });

            System.out.println("ageSum : " + ageSum);

            // Since the accumulator is called in parallel, the combiner is
            // needed to sum up the separate accumulated values

            // accumulator: sum=0; person=Pamela
            // accumulator: sum=0; person=David
            // accumulator: sum=0; person=Max
            // accumulator: sum=0; person=Peter
            // combiner: sum1=18; sum2=23
            // combiner: sum1=23; sum2=12
            // combiner: sum1=41; sum2=35
        }

        {
            ForkJoinPool commonPool = ForkJoinPool.commonPool();
            System.out.println(commonPool.getParallelism()); // 3
            // configurable
            // -Djava.util.concurrent.ForkJoinPool.common.parallelism=5
        }

        {
            Arrays.asList("a1", "a2", "b1", "c2", "c1")//
                    .parallelStream()//
                    .filter(s -> {//
                        System.out.format("filter: %s [%s]\n", s, Thread.currentThread().getName());
                        return true;
                    })//
                    .map(s -> {//
                        System.out.format("map: %s [%s]\n", s, Thread.currentThread().getName());
                        return s.toUpperCase();
                    })//
                    .forEach(s -> System.out.format("forEach: %s [%s]\n", s, Thread.currentThread().getName()));
        }
    }

    private static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Foo {
        String name;
        List<Bar> bars = new ArrayList<>();

        Foo(String name) {
            this.name = name;
        }
    }

    private static class Bar {
        String name;

        Bar(String name) {
            this.name = name;
        }
    }

    private static class Outer {
        Nested nested;
    }

    private static class Nested {
        Inner inner;
    }

    private static class Inner {
        String foo;
    }

    private static class TestBean {
        String aaa;
    }

    private static class TestBean2 {
        String bbb;
    }

    private static class TC implements Collector<TestBean, List<TestBean>, List<TestBean2>> {

        @Override
        public Supplier<List<TestBean>> supplier() {
            return ArrayList::new;
        }

        /*
         * a function that is used for adding a new element to an existing
         * accumulator object
         */
        @Override
        public BiConsumer<List<TestBean>, TestBean> accumulator() {
            // return List::add;
            return new BiConsumer<List<TestBean>, TestBean>() {
                @Override
                public void accept(List<TestBean> t, TestBean u) {
                    t.add(u);
                }
            };
        }

        /*
         * a function that is used for merging two accumulators together
         */
        @Override
        public BinaryOperator<List<TestBean>> combiner() {
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
        public Function<List<TestBean>, List<TestBean2>> finisher() {
            return new Function<List<TestBean>, List<TestBean2>>() {
                @Override
                public List<TestBean2> apply(List<TestBean> t) {
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
    }
}
