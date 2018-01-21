package gtu.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PressureTest {

    public static void main(String[] args) throws InterruptedException {

        final List<Callable<Object>> partitions = new ArrayList<Callable<Object>>();
        partitions.add(createSimpleCallable("A"));
        partitions.add(createSimpleCallable("B"));
        partitions.add(createSimpleCallable("C"));
        partitions.add(createSimpleCallable("D"));
        partitions.add(createSimpleCallable("E"));
        partitions.add(createSimpleCallable("F"));
        partitions.add(createSimpleCallable("G"));
        partitions.add(createSimpleCallable("H"));
        partitions.add(createSimpleCallable("I"));

        final ExecutorService executorPool = Executors.newFixedThreadPool(5);
        final List<Future<Object>> resultFromParts = executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
        executorPool.shutdown();

        for (Future<Object> future : resultFromParts) {
            try {
                System.out.println("Result = " + future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    static Callable<Object> createSimpleCallable(final String label) {
        Callable<Object> callable = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (int ii = 0; ii < 10; ii++) {
                    System.out.println(ii + "....." + label);
                    Thread.sleep(100);
                }
                System.out.println(label + "..done");
                return label;
            }
        };
        return callable;
    }
}
