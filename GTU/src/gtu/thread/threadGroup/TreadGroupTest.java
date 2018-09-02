package gtu.thread.threadGroup;

/**
 * @author 2012/1/8
 */
class MyThread extends Thread {
    boolean stopped;

    MyThread(ThreadGroup tg, String name) {
        super(tg, name);
        stopped = false;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " starting.");
        try {
            for (int i = 1; i < 1000; i++) {
                System.out.print(".");
                Thread.sleep(250);
                synchronized (this) {
                    if (stopped)
                        break;
                }
            }
        } catch (Exception exc) {
            System.out.println(Thread.currentThread().getName() + " interrupted.");
        }
        System.out.println(Thread.currentThread().getName() + " exiting.");
    }

    synchronized void myStop() {
        stopped = true;
    }
}

public class TreadGroupTest {
    public static void main(String args[]) throws Exception {
        ThreadGroup tg = new ThreadGroup("My Group");

        MyThread thrd1 = new MyThread(tg, "MyThread #1");
        MyThread thrd2 = new MyThread(tg, "MyThread #2");
        MyThread thrd3 = new MyThread(tg, "MyThread #3");

        thrd1.start();
        thrd2.start();
        thrd3.start();

        Thread.sleep(1000);

        System.out.println(tg.activeCount() + " threads in thread group.");

        Thread thrds[] = new Thread[tg.activeCount()];
        tg.enumerate(thrds);
        for (Thread t : thrds)
            System.out.println("@ " + t.getName());

        // 如果我們想要一次取得群組中所有的執行緒進行操作，您可以使用enumerate()方法，例如：
        // Thread[] threads = new Thread[threadGroup1.activeCount()];
        // threadGroup1.enumerate(threads);
        // activeCount()方法取得群組中作用中的執行緒數量，enumerate()方法要傳入一個Thread陣列物件，它會將執行緒物件設定至每個陣列欄位中，之後您就可以指定陣列索引來操作這些執行緒

        thrd1.myStop();

        Thread.sleep(1000);

        System.out.println(tg.activeCount() + " threads in tg.");
        tg.interrupt();
    }
}