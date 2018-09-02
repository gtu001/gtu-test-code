package gtu.synchronized_;

import java.util.LinkedList;

/**
 * Design Pattern: Producer Consumer 模式
 * 
 * @author 2012/1/8
 */
public class ProducerConsumer {
    public static void main(String[] args) {
        Store store = new Store();
        (new Thread(new Producer(store))).start();
        (new Thread(new Consumer(store))).start();
    }
}

class Store {
    private LinkedList<Integer> products = new LinkedList<Integer>();

    synchronized void add(Integer product) {
        while (products.size() >= 2) { // 容量限制為 2
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        products.addLast(product);
        notifyAll();
    }

    synchronized Integer get() {
        while (products.size() <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Integer product = products.removeFirst();
        notifyAll();
        return product;
    }
}

class Producer implements Runnable {
    private Store store;

    Producer(Store store) {
        this.store = store;
    }

    public void run() {
        for (int product = 1; product <= 10; product++) {
            try {
                // wait for a random time
                Thread.sleep((int) Math.random() * 3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            store.add(product);
            System.out.println("Produce " + product);
        }
    }
}

class Consumer implements Runnable {
    private Store store;

    Consumer(Store store) {
        this.store = store;
    }

    public void run() {
        for (int i = 1; i <= 10; i++) {
            try {
                // wait for a random time
                Thread.sleep((int) (Math.random() * 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Consume " + store.get());
        }
    }
}
