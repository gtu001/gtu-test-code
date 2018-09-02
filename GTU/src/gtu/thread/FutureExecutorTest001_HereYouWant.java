package gtu.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureExecutorTest001_HereYouWant {

    public static void main(String[] args) {
        System.out.println("---------------------0");
        FutureExecutorTest001_HereYouWant t = new FutureExecutorTest001_HereYouWant();
        System.out.println(t.getMultipleResult());
        System.out.println("done...");
    }

    private boolean getSingleResult() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<Boolean> getVal = executor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    // TODO
                    return true;
                }
            });
            return getVal.get(5 * 60 * 1000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            throw new RuntimeException("getSingleResult ERR : " + ex.getMessage(), ex);
        }
    }

    private boolean getMultipleResult() {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            List<Callable<Boolean>> lst = new ArrayList<>();

            Callable<Boolean> test1 = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    int[] val = new int[] { 0, 1, 2, 3 };
                    Thread.sleep(new Random().nextInt(val.length) * 1000);
                    System.out.println("----ok!!");
                    return true;
                }
            };
            
            lst.add(test1);
            lst.add(test1);
            lst.add(test1);
            lst.add(test1);
            lst.add(test1);
            
            System.out.println("---------------------1");

            List<Future<Boolean>> rtnLst = executor.invokeAll(lst, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
//            for (Future<Boolean> f : rtnLst) {
//                System.out.println("---------------------2");
//                f.get();
//                System.out.println("---------------------3");
//            }
            return true;
        } catch (Exception ex) {
            throw new RuntimeException("getSingleResult ERR : " + ex.getMessage(), ex);
        }
    }
}
