package gtu.swing.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesUtil;
import gtu.swing.JFrameTest;

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
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(300, 150));
        JCommonUtil.setJFrameCenter(frame);
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

    public static void main(String[] args) {
        System.out.println("-----1");
        boolean forceOpen = lockInstance_delable(new File(FileUtil.DESKTOP_PATH, "lock_file.txt"));
        System.out.println("-----2");
        if (forceOpen) {
            JFrameTest.simpleTestComponent(new JLabel());
        }
        System.out.println("done...");
    }

    public static boolean lockInstance_delable(Class<?> clz) {
        return lockInstance_delable(new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_lockfile.lock"));
    }

    public static boolean lockInstance_delable(final File lockFile) {
        final Runnable addHook = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");
                FileUtil.saveToFile(lockFile, sdf.format(new Date()), "utf8");
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            System.out.println("delete hook file : " + lockFile);
                            lockFile.delete();
                        } catch (Exception e) {
                            System.out.println("Unable to remove lock file: " + lockFile);
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        if (lockFile.exists()) {
            System.out.println("Unable to create and/or lock file: " + lockFile);
            boolean forceOpen = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("已有存在的執行,是否要強制開啟?", "強制開啟");
            if (forceOpen) {
                lockFile.delete();
                addHook.run();
                return true;
            }
            return false;
        }
        addHook.run();
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

    // =================================================================================================================================
    // =================================================================================================================================
    // =================================================================================================================================

    /**
     * 視窗最大化
     * 
     * @param frame
     */
    public static void frameMaximize(JFrame frame) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    /**
     * 視窗無法被移出螢幕
     * 
     * @param frame
     */
    public static void frameCantMoveOutScreen(JFrame frame) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent evt) {
                Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
                int x = evt.getComponent().getX();
                int y = evt.getComponent().getY();
                if (y < 0) {
                    y = 0;
                }
                if (x < 0) {
                    x = 0;
                }
                if (x > size.getWidth() - evt.getComponent().getWidth()) {
                    x = (int) size.getWidth() - evt.getComponent().getWidth();
                }
                if (y > size.getHeight() - evt.getComponent().getHeight()) {
                    y = (int) size.getHeight() - evt.getComponent().getHeight();
                }
                evt.getComponent().setLocation(x, y);
            }
        });
    }

    /**
     * 雜項設定
     * 
     * @param frame
     */
    public static void setting(JFrame frame) {
        frame.setResizable(true);

        // 設為true則無邊框也沒有最上面(縮小,關閉)那一列
        frame.setUndecorated(false);

        // 讓視窗按照所含的原建範圍顯示
        // frame.pack();

        frame.setAlwaysOnTop(true);
        frame.setLocationByPlatform(true);

        // 設為false拖曳效果會不見
        frame.setFocusableWindowState(true);
    }

    /**
     * 取得所有存在視窗資訊
     */
    public static void showExistsFrameInfo() {
        Frame[] frames = Frame.getFrames();
        System.out.println("frame count = " + frames.length);
        for (int i = 0; i < frames.length; i++) {
            String title = frames[i].getTitle();
            boolean isVisible = frames[i].isVisible();
            System.out.println("視窗 : [" + title + "] 是否顯示  : [" + isVisible + "]");
        }
    }

    /**
     * 設定關閉視窗須確認
     * 
     * @param frame
     */
    public static void frameCloseConfirm(final JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "確定關閉?") == JOptionPane.YES_OPTION) {
                    gtu.swing.util.JFrameUtil.setVisible(false, frame);
                    frame.dispose();
                }
            }
        });
    }

    /**
     * 設定ICON
     * 
     * @param frame
     */
    public static void setIconImages(JFrame frame) {
        final int SMALL_ICON_WIDTH = 16;
        final int SMALL_ICON_HEIGHT = 16;
        final int SMALL_ICON_RENDER_WIDTH = 10;

        List<BufferedImage> images = new ArrayList<BufferedImage>();

        BufferedImage bi = new BufferedImage(SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, SMALL_ICON_RENDER_WIDTH, SMALL_ICON_HEIGHT);
        g.dispose();
        images.add(bi);

        frame.setIconImages(images);

        // 下面效果不明
        // frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));
    }

    /**
     * 當按下Esc時觸發的事件
     * 
     * @param frame
     */
    public static void frameEscEvents(JFrame frame) {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                System.out.println("Esc action performd...");
            }
        };
        JPanel content = (JPanel) frame.getContentPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        content.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * 關閉視窗的模式
     * 
     * @param jframe
     */
    public static void frameCloseMode(JFrame jframe) {
        // JFrame.EXIT_ON_CLOSE; //關閉就中斷程式
        // JFrame.HIDE_ON_CLOSE; //關閉就隱藏不中斷程式(預設值)
        // JFrame.DISPOSE_ON_CLOSE;
        // JFrame.DO_NOTHING_ON_CLOSE;
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 顯示螢幕尺寸
     */
    public static void showScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) screenSize.getHeight();
        int width = (int) screenSize.getWidth();
        System.out.println(String.format("螢幕  : 寬 [%d] 高[%d]", width, height));
    }

    /**
     * 設定視窗大小位置
     * 
     * @param width
     * @param height
     * @param jframe
     */
    public static void setFrameLocationAndSize(int width, int height, JFrame jframe) {
        // 取得中心點
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        // 取得置中位置
        int left = (int) center.getX() - width / 2;
        int top = (int) center.getY() - height / 2;

        jframe.setSize(width, height);// 寬 高
        jframe.setLocation(left, top);// 寬 高

        // 或是一次設兩種
        jframe.setBounds(left, top, width, height);
    }
}
