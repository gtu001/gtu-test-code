package gtu.swing.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesUtil;

public class JFrameUtil {

    public static void setVisible(boolean isShow, JFrame frame) {
        if (OsInfoUtil.isWindows()) {
            frame.setVisible(isShow);
        } else {
            if (isShow) {
                frame.setVisible(true);
                frame.setState(java.awt.Frame.NORMAL);
            } else {
                frame.setState(java.awt.Frame.ICONIFIED);
            }
        }
    }

    public static boolean isVisible(JFrame frame) {
        if (OsInfoUtil.isWindows()) {
            return frame.isVisible();
        } else {
            if (frame.getState() == java.awt.Frame.NORMAL) {
                return true;
            } else if (frame.getState() == java.awt.Frame.ICONIFIED) {
                return false;
            }
        }
        return false;
    }

    public static JFrame createSimpleFrame(Class<?> clz) {
        JFrame frame = new JFrame();
        frame.setTitle(clz.getSimpleName());
        frame.setLayout(new FlowLayout());
        JCommonUtil.setJFrameDefaultSetting(frame);
        return frame;
    }

    public static boolean lockInstance(Class<?> clz) {
        return lockInstance(new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_lockfile.lock"));
    }

    public static boolean lockInstance(final File lockFile) {
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(lockFile, "rw");
            final FileLock fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            fileLock.release();
                            randomAccessFile.close();
                            lockFile.delete();
                        } catch (Exception e) {
                            System.out.println("Unable to remove lock file: " + lockFile);
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }
        } catch (Exception e) {
            System.out.println("Unable to create and/or lock file: " + lockFile);
            e.printStackTrace();
        }
        return false;
    }

    public static boolean lockInstance_delable(Class<?> clz) {
        return lockInstance_delable(new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_lockfile.lock"));
    }

    public static boolean lockInstance_delable(final File lockFile) {
        if (lockFile.exists()) {
            System.out.println("Unable to create and/or lock file: " + lockFile);
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");
        FileUtil.saveToFile(lockFile, sdf.format(new Date()), "utf8");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    lockFile.delete();
                } catch (Exception e) {
                    System.out.println("Unable to remove lock file: " + lockFile);
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    public static void showBoundSize(Component component) {
        component.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                JFrame frame = (JFrame) e.getSource();
                System.out.println("size " + frame.getSize());
            }
        });
    }

    public static Frame getFrame(Component c) {
        Component w = c;
        while (!(w instanceof Frame) && (w != null)) {
            w = w.getParent();
        }
        return (Frame) w;
    }

    public static class FrameAlwaysOnTopHandler {
        Map<Frame, Boolean> allFrameMap = new HashMap<Frame, Boolean>();

        public FrameAlwaysOnTopHandler() {
            Frame[] allFrames = Frame.getFrames();
            for (Frame f : allFrames) {
                allFrameMap.put(f, f.isAlwaysOnTop());
                f.setAlwaysOnTop(false);
            }
        }

        public void done() {
            for (Frame f : allFrameMap.keySet()) {
                f.setAlwaysOnTop(allFrameMap.get(f));
            }
        }
    }

    public static void setSizeWithPercent(float widthPercent, float heightPercent, Window frame) {
        java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(new Dimension((int) (scr_size.width * widthPercent), (int) (scr_size.height * heightPercent)));
    }
}
