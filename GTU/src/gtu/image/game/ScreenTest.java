
package gtu.image.game;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;

public class ScreenTest extends Frame {
    /** */
    /**
    *
    */
    private static final long serialVersionUID = 1L;
    private static final long TIME = 9000;

    public static void main(String[] args) {
        // 创建一个显示模式及设定参数,分别为：宽、高、比特位数、刷新率(赫兹)
        DisplayMode displayMode = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
        ScreenTest test = new ScreenTest();
        test.run(displayMode);
    }

    public void run(DisplayMode displayMode) {
        setBackground(Color.black);
        setForeground(Color.white);
        setFont(new Font("Dialog", 0, 24));
        ScreenManager screen = new ScreenManager();
        try {
            screen.setFullScreen(displayMode, this);
            try {
                Thread.sleep(TIME);
            } catch (InterruptedException ex) {
            }
        } finally {
            screen.restoreScreen();
        }
    }

    public void paint(Graphics g) {
        g.drawString("Hello World!", 50, 50);
    }
}