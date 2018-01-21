package gtu.thread;

public class SynchronizedTEST implements Runnable {
    static int i = 1;
    String name;
    private Object lockObject;

    SynchronizedTEST(String name, Object o) {
        this.name = name;
        this.lockObject = o;
    }

    public void run() {
        dd();
    }

    public synchronized void dd() {
        synchronized (lockObject) {
            boolean doWait = false;
            while (i < 100) {
                if (!doWait) {
                    System.out.println(name + " " + i + " notify!");
                    i++;
                    doWait = true;
                    lockObject.notify();
                } else {
                    doWait = false;
                    try {
                        System.out.println(name + " wait!");
                        lockObject.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String args[]) {
        Object lockObjectx = new Object();
        Thread p1 = new Thread(new SynchronizedTEST("AAAAAA-->", lockObjectx));
        p1.start();
        Thread p2 = new Thread(new SynchronizedTEST("BBBBBB-->", lockObjectx));
        p2.start();
    }
}

// 這段代碼是我從別人那裏得到的，我自己寫的時候沒有寫出來。
// 我現在的疑問就是：
// 這段代碼裏，他用synchronized修飾了lockobject這個對象，也就是說這個鎖，鎖的就是這個對象。
// 但是P1和P2這兩個線程裏邊，都有各自的lockobject對象。
// 解鎖還需上鎖人。
// P1的lockobject把自己送到wait狀態那么就要用P1的lockobject再把自己notify起來，
// 但是這段代碼並不是這樣啊，他是怎么實現的呢？
// 希望大家給指出來我的思路錯在哪裏，謝謝啦，分數不多，我剛剛來到CSDN，還望各位大哥大姐們多多幫助哈
// 下面是最好的回答：
// 但是P1和P2這兩個線程裏邊，都有各自的lockobject對象。
// p1,p2只是共享一個lockobject,就是你在main中定義的那個,你的兩個線程只是把這同一個對象的引用給了兩個不同的線程,所以還是一個lockobject
