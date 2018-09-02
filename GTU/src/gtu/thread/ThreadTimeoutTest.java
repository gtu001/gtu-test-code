package gtu.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadTimeoutTest {

    public static void main(String[] args) {

        ExecutorService service = Executors.newCachedThreadPool();

        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (int ii = 0; ii < 100; ii++) {
                    try {
                        System.out.println(" i = " + ii);
                        Thread.sleep(100);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return 999;
            }
        });

        try {
            Integer val = future.get(3000, TimeUnit.MILLISECONDS);
            System.out.println("val = " + val);
        } catch (Exception e) {
            e.printStackTrace();
            future.cancel(true); // this method will stop the running underlying
                                 // task
        }
    }

}
