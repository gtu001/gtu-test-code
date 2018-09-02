package gtu.util;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {

    public static void main(String[] args) {
        QueueTest test = new QueueTest();
        test.threadSafeQueue();
    }

    private void threadSafeQueue() {
        final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String str = UUID.randomUUID().toString();
                    System.out.println("produce : " + str);
                    // queue.offer(str);
                    try {
                        queue.put(str);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep((long) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // String str = queue.poll();
                    String str = null;
                    try {
                        str = queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("consume : " + str);
                    try {
                        Thread.sleep((long) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }

    private void sortQueue() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer arg0, Integer arg1) {
                return arg0.compareTo(arg1);
            }
        };
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(20, comparator);
        for (int ii = 0; ii < 20; ii++) {
            queue.offer(20 - ii);
        }
        for (int ii = 0; ii < 20; ii++) {
            System.out.println(queue.poll());
        }
    }

    private void baseQueue() {
        Queue<String> queue = new LinkedList<String>();
        queue.offer("First");// 用offer不要用add
        queue.offer("Second");
        queue.offer("Third");

        String data = null;
        while ((data = queue.poll()) != null) {// 用poll不要用remove
            System.out.println(data);
        }
    }
}
