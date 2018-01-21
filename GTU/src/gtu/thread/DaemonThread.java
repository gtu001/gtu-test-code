package gtu.thread;

/**
 * @author 2012/1/8
 */
public class DaemonThread {
    public static void main(String[] args) {

        Thread thread = new Thread(
        // 这是匿名类的写法
                new Runnable() {
                    public void run() {
                        while (true) {
                            System.out.print("T");
                        }
                    }
                });
        // 设置为Daemon线程
        thread.setDaemon(true);// 設為true 則主線程結束則跟著結束

        thread.start();
    }
}