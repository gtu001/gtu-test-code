package _temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Test38 {

    public static void main(String[] args) throws InterruptedException {
        final AtomicInteger count = new AtomicInteger(0);
        
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Callable<String>> lst = new ArrayList<Callable<String>>();
        for (int ii = 0; ii < 200; ii++) {
            final AtomicInteger it = new AtomicInteger();
            it.set(ii);
            lst.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    long sleepTime = new Random().nextInt(10) * 1000;
                    Thread.sleep(sleepTime);
                    String tag = "idx : " + it.get() + " -> " + sleepTime;
                    System.out.println("#" + tag + " --> " + count.get());
                    count.set(count.get() + 1);
                    return tag;
                }
            });
        }
        
        List<Future<String>> allDone = executor.invokeAll(lst);
        
        for(Future<String> v : allDone) {
        }
        System.out.println("done...");
    }

}
