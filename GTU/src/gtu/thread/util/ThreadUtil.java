package gtu.thread.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.SwingUtilities;

public class ThreadUtil {

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
    
    public static <T> T  xxxxxxxxxxxxxxxx(Callable<T> task, long timeout) {
        final ArrayBlockingQueue<T> blockQueue = new ArrayBlockingQueue<T>(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
//                    blockQueue.offer(task.call(), 5, TimeUnit.MILLISECONDS);
                    blockQueue.add(task.call());
                } catch (Exception e) {
                    blockQueue.clear();
                    blockQueue.add(null);
                }
            }
        });
        try {
            // blockQueue.take();
            return blockQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
