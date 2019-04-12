package gtu.thread.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import gtu.swing.util.JCommonUtil;

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

    private Object ____template____(int waitSecond) {
        final BlockingQueue<Triple<String, Throwable, Object>> queue = new ArrayBlockingQueue<Triple<String, Throwable, Object>>(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object result = null;
                try {

                } catch (Throwable ex) {
                    queue.offer(Triple.of("fail", ex, null));
                } finally {
                    queue.offer(Triple.of("ok", null, result));
                }
            }
        }).start();
        try {
            Triple<String, Throwable, Object> result2 = queue.poll(waitSecond * 1000L, TimeUnit.MILLISECONDS);
            if ("fail".equals(result2.getLeft())) {
                throw result2.getMiddle();
            }
            return result2.getRight();
        } catch (InterruptedException ex) {
            String message = "時間超出範圍 " + waitSecond + "秒!!";
            throw new RuntimeException(message, ex);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
