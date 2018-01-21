package gtu.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 2012/1/8 线程池测试类
 */
public class TestThreadPool {

    public static void main(String[] args) {

        // 構造一個執行緒池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(//
                2, //核心執行數
                4, //最大容納數
                3, //未執行存活時間 
                TimeUnit.SECONDS,//為執行存活時間單位
                new ArrayBlockingQueue<Runnable>(2), //存放中心
                new ThreadPoolExecutor.DiscardOldestPolicy()//未被執行所要做的處理
                );

        for (int i = 1; i <= 10; i++) {
            try {
                // 產生一個任務，並將其加入到執行緒池
                String task = "task@ " + i;
                
                System.out.println("put " + task);
                
                MyThread myThread = new MyThread();
                myThread.name = task;
                threadPool.execute(myThread);

                // 便於觀察，等待一段時間
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class MyThread extends Thread {
        String name;
        @Override
        public void run() {
           try{
               System.out.println(name + " start ...");
               Thread.sleep(5000);
               System.out.println(name + " end ...");
           }catch(Exception ex){
               ex.printStackTrace();
           }
        }
    };
}
