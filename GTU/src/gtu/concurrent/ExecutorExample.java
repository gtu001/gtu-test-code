package gtu.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorExample {

    private Executor executor; //聲明執行器

    public void executeTasks() {
        for (int ii = 0; ii < 6; ii++) {
            executor.execute(new SimpleTask("task" + ii));
        }
    }

    static class SimpleTask implements Runnable {
        String taskName;

        public SimpleTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println("do " + taskName + " ... in Thread: " + Thread.currentThread().getId());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorExample ee = new ExecutorExample();
        ee.executor = Executors.newFixedThreadPool(3);//創建一個線程池，重複使用一組固定的現成運行了一個共享的無屆對列
        ee.executeTasks();
        ee.executor = Executors.newCachedThreadPool();//線程池是動態的，不夠用時創建新的線程，長時間不用的線程將被回收
        ee.executeTasks();
        ee.executor = Executors.newScheduledThreadPool(3);//創建一個線程池，可在整定延遲後運行或者定期的執行
        ee.executeTasks();
    }

}
