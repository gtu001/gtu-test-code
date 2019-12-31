package gtu.thread;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest001 {

    public static void main(String[] args) {
        TestThread a1 = new TestThread("A1");
        TestThread b2 = new TestThread("B1");
        
        a1.start();
        b2.start();
        
        System.out.println("done...");
    }

    private static Lock LOCK = new ReentrantLock();
    
    private static class TestThread extends Thread {
        TestThread(String name) {
            this.setName(name);
        }

        private String getPrefix() {
            return "[" + this.getName() + "] ";
        }

        public void run() {
            System.out.println(getPrefix() + "ready to lock");
            LOCK.lock();

            int randomHoldingTime = new Random().nextInt(20);
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(500);
                    count++;
                } catch (InterruptedException e) {
                }
                System.out.println(getPrefix() + "\t...." + count);
                if (count >= randomHoldingTime) {
                    break;
                }
            }

            System.out.println(getPrefix() + "unlock");
            LOCK.unlock();

            System.out.println(getPrefix() + "final");
        }
    }
}
