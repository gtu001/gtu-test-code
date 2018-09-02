package gtu.timer;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Troy 2012/1/8
 */
public class AnnoyingBeep {

    public static void main(String[] args) {
        AnnoyingBeep test = new AnnoyingBeep();
    }

    Toolkit toolkit;
    Timer timer;

    public AnnoyingBeep() {
        toolkit = Toolkit.getDefaultToolkit();
        timer = new Timer();
        timer.schedule(new RemindTask(), 5000); // 五秒後執行
        // timer.schedule(new RemindTask(), 2000, 1000); // 兩秒後執行,之後每一秒一次
        // timer.schedule(new RemindTask(), date, 1000); // 在設定的時間點執行,之後每一秒一次

        // cancel()方法结束这个定时器。

        // schedule(TimerTask task, long delay, long
        // period)方法设定指定任务task在指定延迟delay后进行固定延迟peroid的执行。

        // scheduleAtFixedRate(TimerTask task, long delay, long
        // period)方法设定指定任务task在指定延迟delay后进行固定频率peroid的执行。
    }

    class RemindTask extends TimerTask {
        int numWarningBeeps = 3;

        public void run() {
            if (numWarningBeeps > 0) {
                toolkit.beep();
                System.out.println("Beep!");
                numWarningBeeps--;
            } else {
                toolkit.beep();
                System.out.println("Time’s up!");
                // timer.cancel(); //Not necessary because we call System.exit
                // System.exit(0);
                // Stops the AWT thread (and everything else)
            }
        }
    }
}