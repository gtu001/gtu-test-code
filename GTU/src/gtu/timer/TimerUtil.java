package gtu.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.reflect.FieldUtils;

public class TimerUtil {

    private static final TimerUtil _INST = new TimerUtil();

    public static TimerUtil getInstance() {
        return _INST;
    }

    private Thread[] getCurrentThreads() {
        Thread[] ths = new Thread[Thread.activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(ths);
        return ths;
    }

    private List<TimerTask> getTimerTaskArrayFromTimerThread(Thread ths) {
        try {
            if (ths != null && ths.getClass().getName().equals("java.util.TimerThread")) {
                TimerTask[] arry = (TimerTask[]) FieldUtils.readDeclaredField(FieldUtils.readDeclaredField(ths, "queue", true), "queue", true);
                List<TimerTask> lst = new ArrayList<TimerTask>();
                for (int ii = 0; ii < arry.length; ii++) {
                    if (arry[ii] != null) {
                        lst.add(arry[ii]);
                    }
                }
                return lst;
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends TimerTask> List<T> filterClass(List<TimerTask> arry, Class<T> clz) {
        List<T> lst = new ArrayList<T>();
        for (int jj = 0; jj < arry.size(); jj++) {
            TimerTask d = arry.get(jj);
            if (d != null && clz.isAssignableFrom(d.getClass())) {
                if (!lst.contains(d)) {
                    lst.add((T) d);
                }
            }
        }
        return lst;
    }

    public <T extends TimerTask> void showInfo(Map<Thread.State, List<T>> map) {
        for (Thread.State st : Thread.State.values()) {
            if (map.containsKey(st)) {
                System.out.println("> " + st + "\t" + map.get(st).size());
            } else {
                System.out.println("> " + st + "\t" + "0");
            }
        }
    }

    public <T extends TimerTask> Map<Thread.State, List<T>> getTimerTaskMap(Class<T> clz) {
        Map<Thread.State, List<T>> map = new HashMap<Thread.State, List<T>>();

        Thread[] ths = getCurrentThreads();

        for (int ii = 0; ii < ths.length; ii++) {
            List<TimerTask> arry = getTimerTaskArrayFromTimerThread(ths[ii]);
            if (arry == null) {
                continue;
            }

            List<T> appendLst = this.filterClass(arry, clz);

            if (!appendLst.isEmpty()) {
                Thread.State key = ths[ii].getState();
                List<T> valLst = new ArrayList<T>();
                if (map.containsKey(key)) {
                    valLst = map.get(key);
                }
                valLst.addAll(appendLst);
                map.put(key, valLst);
            }
        }
        return map;
    }

    public Map<Thread.State, Integer> getTimerThreadStatus() {
        Thread[] ths = getCurrentThreads();

        Map<Thread.State, Integer> map = new HashMap<Thread.State, Integer>();

        for (int ii = 0; ii < ths.length; ii++) {
            if (ths[ii].getClass().getName().equals("java.util.TimerThread")) {
                Thread.State v = ths[ii].getState();
                int val = 0;
                if (map.containsKey(v)) {
                    val = map.get(v);
                }
                map.put(v, ++val);
            }
        }
        return map;
    }

    public static void killTimer(Timer timer) {
        timer.cancel();
        timer.purge();
    }
}
