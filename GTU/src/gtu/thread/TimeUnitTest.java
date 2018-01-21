package gtu.thread;

import java.util.concurrent.TimeUnit;

public class TimeUnitTest {

    public static void main(String[] args) {
        Thread t1= new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    System.out.println("測試!!");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }});
        t1.start();
    }

}
