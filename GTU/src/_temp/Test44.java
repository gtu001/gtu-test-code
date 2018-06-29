package _temp;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Test44 {

    final Semaphore sem = new Semaphore(0);

    final AtomicInteger val = new AtomicInteger(0);

    private class TTTT extends Thread {
        @Override
        public void run() {

            val.incrementAndGet();

            synchronized (val) {
                System.out.println("q - " + sem.getQueueLength());
                System.out.println("a - " + val.get());
                
                try {
                    val.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static void main(String[] args) throws InterruptedException, IOException {
        Test44 t = new Test44();

        for (int ii = 0; ii < 10; ii++) {
            t.new TTTT().start();
        }

        System.out.println("done...");
    }
}
