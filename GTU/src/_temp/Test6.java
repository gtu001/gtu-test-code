package _temp;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Test6 {

    public static void main(String[] args) {
        new Test6().testScheduleDelayAndPeriod();
        System.out.println("done...");
    }

    void testScheduleDelayAndPeriod() {
        Timer timer = new Timer();
        System.out.println("Delay：3秒, Period：2秒");
        System.out.println("In testScheduleDelayAndPeriod：" + new Date());

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("哈哈哈");
            }
        }, 0, 2000);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

        timer.cancel();
        System.out.println("End testScheduleDelayAndPeriod：" + new Date() + "\n");
    }
}
