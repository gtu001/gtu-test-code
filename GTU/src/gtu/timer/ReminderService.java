package gtu.timer;

import gtu.class_.ClassPathUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/**
 * Read a file of reminders, run each when due using java.util.Timer
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: ReminderService.java,v 1.7 2004/02/09 03:33:46 ian Exp $
 */
public class ReminderService {

    /** The Timer object */
    Timer timer = new Timer();

    class Item extends TimerTask {
        String message;

        Item(String m) {
            message = m;
        }

        public void run() {
            message(message);
        }
    }

    public static void main(String[] argv) throws IOException {
        new ReminderService().load();
    }

    protected void load() throws IOException {

        String filePath = ClassPathUtil.getJavaFilePath(getClass()) + "test.txt";

        BufferedReader is = new BufferedReader(new FileReader(filePath));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd hh mm");
        String aLine;
        while ((aLine = is.readLine()) != null) {
            ParsePosition pp = new ParsePosition(0);// 从第一个字符开始解析
            Date date = formatter.parse(aLine, pp);
            if (date == null) {
                message("Invalid date in " + aLine + " errorIndex = " + pp.getErrorIndex());
                continue;
            }
            String mesg = aLine.substring(pp.getIndex());// 從解析成功的位置開始，也就是日期後面
            timer.schedule(new Item(mesg + "=" + pp.getIndex()), date);
        }
    }

    /**
     * Display a message on the console and in the GUI. Used both by Item tasks
     * and by mainline parser.
     */
    void message(String message) {
        System.out.println("\007" + message);
        JOptionPane.showMessageDialog(null, message, "Timer Alert", // titlebar
                JOptionPane.INFORMATION_MESSAGE); // icon
    }
}