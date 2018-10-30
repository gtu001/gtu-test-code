package gtu.jdk8.ex1;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil {

    public static <T> Stream of(T[] array) {
        switch (1) {
        case 1:
            return Arrays.stream(array);
        default:
            return Stream.of(array);
        }
    }

    public static <T> Stream of(Iterable<T> iter) {
        return StreamSupport.stream(iter.spliterator(), false);
    }

    public static <T> Stream of(Iterator<T> iter) {
        return StreamSupport.stream(//
                Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), //
                false);
    }

    public static <T> Stream of(Enumeration<T> iter) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
            public T next() {
                return iter.nextElement();
            }

            public boolean hasNext() {
                return iter.hasMoreElements();
            }
        }, Spliterator.ORDERED), false);
    }
}
