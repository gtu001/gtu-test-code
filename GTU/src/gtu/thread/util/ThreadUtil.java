package gtu.thread.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadUtil {

    public static void main(String[] args) {
        Object rtnVal = ThreadUtil.runUseBlockingQueue(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(2000);
                return "XXXXX";
            }
        }, 10000);
        System.out.println(rtnVal);
        System.out.println("done...");
    }

    public static <T> T getFutureResult(Callable<T> task, long timeout) throws TimeoutException {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<T> future = executor.submit(task);
            T result = null;
            if (timeout <= 0) {
                System.out.println("get wait forever");
                result = future.get();
            } else {
                System.out.println("get wait " + timeout);
                result = future.get(timeout, TimeUnit.MILLISECONDS);
            }
            return result;
        } catch (java.util.concurrent.TimeoutException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T runUseBlockingQueue(Callable<T> task, long timeout) {
        final ArrayBlockingQueue<AtomicReference<T>> blockQueue = new ArrayBlockingQueue<AtomicReference<T>>(1);
        new Thread(new Runnable() {
            public void run() {
                AtomicReference<T> rtnObj = new AtomicReference<T>();
                try {
                    // blockQueue.offer(task.call(), 5, TimeUnit.MILLISECONDS);
                    rtnObj.set(task.call());
                } catch (Exception e) {
                    rtnObj.set(null);
                    e.printStackTrace();
                } finally {
                    blockQueue.add(rtnObj);
                }
            }
        }).start();
        try {
            if (timeout <= 0) {
                return blockQueue.take().get();
            } else {
                return blockQueue.poll(timeout, TimeUnit.MILLISECONDS).get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
