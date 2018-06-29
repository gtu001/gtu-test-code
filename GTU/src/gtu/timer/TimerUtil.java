package gtu.timer;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang.reflect.FieldUtils;

public class TimerUtil {
    
    public static <T extends TimerTask> List<T> getTimerTaskLst(Class<T> clz) {
        List<T> lst = new ArrayList<T>();

        Thread[] ths = new Thread[Thread.activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(ths);

        for (int ii = 0; ii < ths.length; ii++) {
            if (ths[ii].getClass().getName().equals("java.util.TimerThread")) {
                try {
                    TimerTask[] arry = (TimerTask[]) FieldUtils.readDeclaredField(FieldUtils.readDeclaredField(ths[ii], "queue", true), "queue", true);
                    for (int jj = 0; jj < arry.length; jj++) {
                        if (arry[jj] != null && clz.isAssignableFrom(arry[jj].getClass())) {
                            if (!lst.contains(arry[jj])) {
                                lst.add((T) arry[jj]);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return lst;
    }

}
