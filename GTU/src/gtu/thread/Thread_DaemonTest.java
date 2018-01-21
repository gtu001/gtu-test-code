package gtu.thread;


public class Thread_DaemonTest {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                System.out.println("DDDDDD");
            }
        });
        thread.setDaemon(true);//設為true,主憲程結束就結束
        thread.start();
        Thread.sleep(3000);
        System.out.println("done...");
    }
}
