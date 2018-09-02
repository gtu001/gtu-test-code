package gtu.jdk8.ex1;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Stream_Iterate_001 {

    public static void main(String[] args) {
        Stream<BigDecimal> s = Stream.iterate(//
                BigDecimal.TEN, //
                bigDecimal -> bigDecimal.add(BigDecimal.ONE)
        )//
                .limit(10)//
                .peek(System.out::println);

        LinkedList<BigDecimal> l = s.collect(Collectors.toCollection(LinkedList::new));
        System.out.println(l);
    }
}
