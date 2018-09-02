package com.example.gtuandroid.component;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by gtu001 on 2017/11/14.
 */

public class ThreadUtil {

    private static final String TAG = ThreadUtil.class.getSimpleName();

    public static <T> T getRunOnUiThread(Callable<T> task, long timeout) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(1);
            Future<T> future = executor.submit(task);
            System.out.println("future done? " + future.isDone());
            T result = null;
            if (timeout == -1) {
                result = future.get();
            } else {
                result = future.get(timeout, TimeUnit.MILLISECONDS);
            }
            Log.v(TAG, "future done? " + future.isDone());
            Log.v(TAG, "result: " + result);
            return result;
        } catch (Exception ex) {
            Log.e(TAG, "getRunInThread ERROR", ex);
            return null;
        }
    }
}
