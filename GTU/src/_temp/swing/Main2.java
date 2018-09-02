package _temp.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// w  w w.j av  a2 s .c o  m
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main2 extends JFrame {

    private Timer timer;
    private JDialog blocker;

    public Main2() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        blocker = new JDialog(this, true);
        blocker.setLayout(new FlowLayout());
        blocker.add(new JLabel("I'm blocking EDT!"));
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        blocker.add(progress);
        blocker.pack();

        timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSomeWork();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void doSomeWork() {
        Runnable runnable = new Runnable() {
            public void run() {
                showBlocker();
                try {
                    Thread.sleep(2000);
                } catch (Exception ex) {
                } finally {
                    hideBlocker();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void showBlocker() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                blocker.setLocationRelativeTo(Main2.this);
                blocker.setVisible(true);
            }
        });
    }

    private void hideBlocker() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                blocker.setVisible(false);
                timer.restart();
            }
        });
    }

    public static void main(String[] args) {
        new Main2().setVisible(true);
    }
}