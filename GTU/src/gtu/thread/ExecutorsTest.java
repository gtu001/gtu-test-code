package gtu.thread;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorsTest {
    
    private static class TestTask extends Thread {
        String name;
        TestTask(String name){
            this.name = name;
        }
        public void run(){
            while(true){
                System.out.println("thread : " + name);
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static class RandomPrimSearch implements Callable<BigInteger> {
        private static final Random prng = new SecureRandom();
        private int bitSize;
        RandomPrimSearch(int bitSize){
            this.bitSize = bitSize;
        }
        @Override
        public BigInteger call() throws Exception {
            return BigInteger.probablePrime(bitSize, prng);
        }
    }
    
    private static class TimePrinter implements Runnable {
        @Override
        public void run() {
            System.out.format("Current time : %tr%n", new Date());
        }
    }

    public static void main(String[] args) {
        // 存在大小為1的pool中,每次只執行一個task
        ExecutorService executor1 = Executors.newSingleThreadExecutor();

        // 建構指定的thread數目的pool來執行task
        ExecutorService executor2 = Executors.newFixedThreadPool(1);

        // 會使用它所需要的thread數目於他的queue中執行物件.他會反覆利用可用的thread,也會建構新的thread
        ExecutorService executor3 = Executors.newCachedThreadPool();

        // ??
        ScheduledExecutorService executor4 = Executors.newSingleThreadScheduledExecutor();
        
        if(true){
            executor2.execute(new TestTask("哈哈1"));
            executor2.execute(new TestTask("哈哈2"));
            executor2.execute(new TestTask("哈哈3"));
            executor2.execute(new TestTask("哈哈4"));
            executor2.execute(new TestTask("哈哈5"));
        }
        
        if(false){
            Future<BigInteger> prime1 = executor3.submit(new RandomPrimSearch(512));
            Future<BigInteger> prime2 = executor3.submit(new RandomPrimSearch(512));
            Future<BigInteger> prime3 = executor3.submit(new RandomPrimSearch(512));
            Future<BigInteger> prime4 = executor3.submit(new RandomPrimSearch(512));
            Future<BigInteger> prime5 = executor3.submit(new RandomPrimSearch(512));
            try{
                System.out.println(prime1.get());
                System.out.println(prime2.get());
                System.out.println(prime3.get());
                System.out.println(prime4.get());
                System.out.println(prime5.get());
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            FutureTask<BigInteger> task = new FutureTask<BigInteger>(new RandomPrimSearch(512));
            new Thread(task).start();
            try {
                System.out.println(task.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(false){
            final ScheduledFuture<?> timeHandle = executor4.scheduleAtFixedRate(new TimePrinter(), 0, 10, TimeUnit.SECONDS);
            executor4.schedule(new Runnable() {
                @Override
                public void run() {
                    timeHandle.cancel(false);
                }
            }, 60 * 60, TimeUnit.SECONDS);
        }
    }
}
