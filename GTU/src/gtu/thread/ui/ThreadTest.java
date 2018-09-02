package gtu.thread.ui;

public class ThreadTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Thread t1 = new Thread() {
            public void run() {
                System.out.println("go!!");
                for (long ii = 0; ii < 1000000000; ii++) {

                }
                System.out.println("go ok!!");
            }
        };
        t1.setName("t1");

        ThreadMointerUI ui1 = ThreadMointerUI.newInstance(t1);
        ui1.startWatch();
        System.out.println("done...");
    }

}
