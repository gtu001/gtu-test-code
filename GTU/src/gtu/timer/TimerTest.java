package gtu.timer;

import java.util.Timer;
import java.util.TimerTask;


public class TimerTest {

    public static void main(String[] args) {

        int delay = 5000;// 延迟5秒

        Timer timer = new Timer();// 生成一个Timer对象

        NewTask myTask = new NewTask();// 初始化我们的任务

        timer.schedule(myTask, delay);// 还有其他重载方法...

    }

}

class NewTask extends TimerTask {// 继承TimerTask类

    public void run() {

        System.out.println("printing!");

    }

}