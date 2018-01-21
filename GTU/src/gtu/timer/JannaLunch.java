package gtu.timer;

import gtu.swing.util.JCommonUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/**
 * 提醒笨娟訂午餐
 * 
 * @author Troy
 */
public class JannaLunch {

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        JannaLunch.newInstance().execute();
        System.out.println("done...");
    }

    public static JannaLunch newInstance() {
        return new JannaLunch();
    }

    public void execute() throws ParseException {
        Long period = new Long(30000);
        System.out.println("period = " + period);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final String CLOSE_KEY_WORD = "ok";
                String result = JOptionPane.showInputDialog(null, String.format("記得訂便當!(請輸入\"%s\"->就不會再提醒)", CLOSE_KEY_WORD), "白癡", JOptionPane.INFORMATION_MESSAGE);

                boolean closeTimer1 = (result == null ? "" : result).equals(CLOSE_KEY_WORD);
                boolean closeTimer2 = isNotOrderLunchTime();
                boolean closeTimer3 = isNotWorkingDay();

                System.out.println("closeTimer1 = " + closeTimer1);
                System.out.println("closeTimer2 = " + closeTimer2);
                System.out.println("closeTimer3 = " + closeTimer3);

                if (closeTimer1 || closeTimer2 || closeTimer3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("關閉提醒 \n");
                    sb.append("剛才輸入ok了 = " + closeTimer1 + "\n");
                    sb.append("現在超過十點半 = " + closeTimer2 + "\n");
                    sb.append("今天是星期六日 = " + closeTimer3 + "\n");

                    JCommonUtil._jOptionPane_showMessageDialog_info(sb, "關閉");
                    timer.cancel();
                    //                    System.exit(0);
                }
            }
        }, 0, period);
    }

    private boolean isNotWorkingDay() {
        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
        case 1:
            return true;
        case 7:
            return true;
        }
        return false;
    }

    private boolean isNotOrderLunchTime() {
        Calendar cal = Calendar.getInstance();
        Calendar calAfter = Calendar.getInstance();
        calAfter.set(Calendar.HOUR_OF_DAY, 10);
        calAfter.set(Calendar.MINUTE, 30);
        return cal.after(calAfter);
    }
}
