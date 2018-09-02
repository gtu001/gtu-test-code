package gtu.image.game;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

/** */
/**
 *
 * <p>
 * Title: LoonFramework
 * </p>
 * <p>
 * Description:用于进行屏幕管理
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: LoonFramework
 * </p>
 * <p>
 * License: [url]http://www.apache.org/licenses/LICENSE-2.0</p[/url]>
 * 
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class ScreenManager {
    private GraphicsDevice device;

    public ScreenManager() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

    /** */
    /**
     * 返回系统支持的显示模式数组
     *
     * @return
     */
    public DisplayMode[] getCompatibleDisplayModes() {
        return device.getDisplayModes();
    }

    /** */
    /**
     * 返回与指定数组兼容的显示模式清单
     *
     * @param modes
     * @return
     */
    public DisplayMode findFirstCompatibleMode(DisplayMode modes[]) {
        DisplayMode goodModes[] = device.getDisplayModes();
        for (int i = 0; i < modes.length; i++) {
            for (int j = 0; j < goodModes.length; j++) {
                if (displayModesMatch(modes[i], goodModes[j])) {
                    return modes[i];
                }
            }
        }
        return null;
    }

    /** */
    /**
     * 返回目前采用的显示模式
     *
     * @return
     */
    public DisplayMode getCurrentDisplayMode() {
        return device.getDisplayMode();
    }

    /** */
    /**
     * 匹配两个指定的显示模式
     *
     * @param mode1
     * @param mode2
     * @return
     */
    public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2) {
        if (mode1.getWidth() != mode2.getWidth() || mode1.getHeight() != mode2.getHeight()) {
            return false;
        }
        if (mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && mode1.getBitDepth() != mode2.getBitDepth()) {
            return false;
        }
        if (mode1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && mode2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN && mode1.getRefreshRate() != mode2.getRefreshRate()) {
            return false;
        }
        return true;
    }

    /** */
    /**
     * 设置一个默认窗体的全屏模式
     *
     * @param displayMode
     */
    public void setFullScreen(DisplayMode displayMode) {
        final Frame frame = new Frame();
        frame.setBackground(Color.BLACK);
        setFullScreen(displayMode, frame);
    }

    /** */
    /**
     * 设定指定窗体的全屏模式
     *
     * @param displayMode
     * @param window
     */
    public void setFullScreen(DisplayMode displayMode, final Frame window) {
        window.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        window.setUndecorated(true);
        window.setResizable(false);
        window.setIgnoreRepaint(false);

        device.setFullScreenWindow(window);
        if (displayMode != null && device.isDisplayChangeSupported()) {
            try {
                device.setDisplayMode(displayMode);
            } catch (IllegalArgumentException ex) {
            }
            window.setSize(displayMode.getWidth(), displayMode.getHeight());
        }
        try {
            EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    window.createBufferStrategy(2);
                }
            });
        } catch (

        InterruptedException ex) {
        } catch (InvocationTargetException ex) {
        }
    }

    /** */
    /**
     * 取得当前的Graphics2D模式背景
     *
     * @return
     */
    public Graphics2D getGraphics() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            return (Graphics2D) strategy.getDrawGraphics();
        } else {
            return null;
        }
    }

    /** */
    /**
     * 刷新显示的数据
     *
     */
    public void update() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            if (!strategy.contentsLost()) {
                strategy.show();
            }
        }
        // 同步
        Toolkit.getDefaultToolkit().sync();
    }

    /** */
    /**
     * 返回当前窗口
     *
     * @return
     */
    public Frame getFullScreenWindow() {
        return (Frame) device.getFullScreenWindow();
    }

    public int getWidth() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            return window.getWidth();
        } else {
            return 0;
        }
    }

    public int getHeight() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            return window.getHeight();
        } else {
            return 0;
        }
    }

    /** */
    /**
     * 恢复屏幕的显示模式
     *
     */
    public void restoreScreen() {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }

    /** */
    /**
     * 创建一个与现有显示模式兼容的bufferedimage
     *
     * @param w
     * @param h
     * @param transparancy
     * @return
     */
    public BufferedImage createCompatibleImage(int w, int h, int transparancy) {
        Window window = device.getFullScreenWindow();
        if (window != null) {
            GraphicsConfiguration gc = window.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, transparancy);
        }
        return null;
    }
}