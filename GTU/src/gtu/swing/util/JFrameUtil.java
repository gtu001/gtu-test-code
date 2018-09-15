package gtu.swing.util;

import java.awt.FlowLayout;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import javax.swing.JFrame;

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
}
