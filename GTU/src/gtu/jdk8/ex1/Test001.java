package gtu.jdk8.ex1;

import java.util.concurrent.Callable;

public class Test001 {

    public static void main(String[] args) {
        test1(() -> 3);
        test2(() -> {
            System.out.println("ok1");
        });
        test2(() -> System.out.println("ok2"));
    }

    // ------------------------------------------------------------------------------
    interface Test2Act {
        void run();
    }

    private static void test1(Callable<Integer> call) {
        try {
            System.out.println("--->" + call.call());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2(Test2Act act) {
        act.run();
    }
}
