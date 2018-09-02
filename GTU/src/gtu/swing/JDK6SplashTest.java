package gtu.swing;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 實現閃屏視窗 Splash Screen
 * 
 * @author Troy 2012/1/7
 */
public class JDK6SplashTest extends Frame {
    static void renderSplashFrame(Graphics2D g, int frame) {
        final String[] comps = { "foo", "bar", "baz" };
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(130, 250, 280, 40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("Loading " + comps[(frame / 5) % 3] + "...", 130, 260);
        g.fillRect(130, 270, (frame * 10) % 280, 20);
    }

    public JDK6SplashTest() {
        super("SplashScreen demo");
        setSize(500, 300);
        setLayout(new BorderLayout());
        Menu m1 = new Menu("File");
        MenuItem mi1 = new MenuItem("Exit");
        m1.add(mi1);
        mi1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });

        MenuBar mb = new MenuBar();
        setMenuBar(mb);
        mb.add(m1);
        final SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("SplashScreen.getSplashScreen() returned null");
            return;
        }
        Graphics2D g = (Graphics2D) splash.createGraphics();
        if (g == null) {
            System.out.println("g is null");
            return;
        }
        for (int i = 0; i < 100; i++) {
            renderSplashFrame(g, i);
            splash.update();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
        splash.close();
        setVisible(true);
        toFront();
    }

    public static void main(String args[]) {
        JDK6SplashTest test = new JDK6SplashTest();
    }
}