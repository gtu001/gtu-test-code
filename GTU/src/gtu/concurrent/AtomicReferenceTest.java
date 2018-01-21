package gtu.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        AtomicReference<String> test = new AtomicReference<String>();

        test.set("AAAAAA");
        System.out.println(test.get());
    }
}
