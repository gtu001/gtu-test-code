package gtu.concurrent;

import java.util.concurrent.TimeUnit;

public class TimeUnitTest {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TimeUnit unit = TimeUnit.valueOf("SECONDS");
        unit.sleep(3L);
        System.out.println("done...");
    }

}
