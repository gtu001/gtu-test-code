package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class JProgressBarHelper {

    private JDialog dlg;
    private JFrame parentFrame;
    private JLabel counterLabel;
    private JLabel stateLabel;
    private JProgressBar progressBar;
    private String title;
    private int max = 100;
    private int width = 300;
    private int height = 100;
    private boolean indeterminate = false;
    private int barValue = 0;
    private ActionListener closeListener;

    private static ActionListener DEFAULT_CLOSE_EVENT = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    public static JProgressBarHelper newInstance(JFrame parentFrame, String title) {
        return new JProgressBarHelper(parentFrame, title);
    }

    private JProgressBarHelper(JFrame parentFrame, String title) {
        this.parentFrame = parentFrame;
        this.title = title;
    }

    public JProgressBarHelper max(int max) {
        this.max = max;
        return this;
    }

    public JProgressBarHelper size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public JProgressBarHelper setStateText(String text) {
        stateLabel.setText(text);
        return this;
    }

    public JProgressBarHelper setBarValue(int value) {
        this.barValue = value;
        progressBar.setValue(value);
        counterLabel.setText(value + "/" + max + " (" + getDividePercentString(value, max) + ")");
        return this;
    }

    public JProgressBarHelper addOne() {
        this.barValue++;
        setBarValue(this.barValue);
        return this;
    }

    public JProgressBarHelper closeListener(ActionListener closeListener) {
        this.closeListener = closeListener;
        return this;
    }

    public JProgressBarHelper closeListenerDefault() {
        this.closeListener = DEFAULT_CLOSE_EVENT;
        return this;
    }

    private String getDividePercentString(int from, int max) {
        try {
            BigDecimal v3 = new BigDecimal(from)//
                    .divide(new BigDecimal(max), 4, RoundingMode.HALF_UP)//
                    .multiply(new BigDecimal(100));
            v3 = v3.setScale(2, RoundingMode.HALF_UP);
            return v3.toString() + "%";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "?%";
        }
    }

    /**
     * 左右移動特效
     */
    public JProgressBarHelper indeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        return this;
    }

    public int getBarValue() {
        return progressBar.getValue();
    }

    public JProgressBarHelper dismiss() {
        dlg.setVisible(false);
        return this;
    }

    public JProgressBarHelper dismissByMax() {
        setBarValue(max);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
        }
        dlg.setVisible(false);
        return this;
    }

    public JProgressBarHelper show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dlg.setVisible(true);
            }
        });
        return this;
    }

    // 設定關閉事件
    private void applyOncloseEvent(JDialog dlg) {
        dlg.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("jdialog window closed event received");
            }

            public void windowClosing(WindowEvent e) {
                boolean isDoClose = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要取消 : " + title, "取消 : " + title);
                if (isDoClose) {
                    closeListener.actionPerformed(new ActionEvent(JProgressBarHelper.this, -1, "doClose"));
                    dismiss();
                }
            }
        });
    }

    // 設定可移動範圍
    private void applyMoveBoundEvent(final JDialog dlg) {
        dlg.addComponentListener(new ComponentAdapter() {
            Point lastLocation;

            @Override
            public void componentMoved(ComponentEvent e) {
                if (parentFrame != null && parentFrame.isVisible()) {
                    Point newLoc = dlg.getLocation();

                    boolean setBackLoc = false;
                    if (newLoc.x + dlg.getSize().getWidth() >= //
                    parentFrame.getLocation().getX() + parentFrame.getSize().getWidth()) {
                        setBackLoc = true;
                    } else if (newLoc.x <= parentFrame.getLocation().getX()) {
                        setBackLoc = true;
                    } else if (newLoc.y + dlg.getSize().getHeight() >= //
                    parentFrame.getLocation().getY() + parentFrame.getSize().getHeight()) {
                        setBackLoc = true;
                    } else if (newLoc.y <= parentFrame.getLocation().getY()) {
                        setBackLoc = true;
                    }

                    if (setBackLoc) {
                        lastLocation = lastLocation != null ? lastLocation : newLoc;
                        dlg.setLocation(lastLocation);
                    } else {
                        lastLocation = newLoc;
                    }
                }
            }
        });
    }

    public JProgressBarHelper build() {
        dlg = new JDialog(parentFrame, title, true);
        progressBar = new JProgressBar(barValue, max);
        progressBar.setIndeterminate(indeterminate);
        counterLabel = new JLabel();
        stateLabel = new JLabel("");
        dlg.add(BorderLayout.CENTER, progressBar);
        dlg.add(BorderLayout.NORTH, stateLabel);
        dlg.add(BorderLayout.SOUTH, counterLabel);
        counterLabel.setText("");
        dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlg.setSize(width, height);
        dlg.setLocationRelativeTo(parentFrame);
        setBarValue(barValue);
        this.applyMoveBoundEvent(dlg);
        if (this.closeListener != null) {
            applyOncloseEvent(dlg);
        }
        return this;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);

        JButton btn = new JButton("test");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                final JProgressBarHelper inst = JProgressBarHelper.newInstance(frame, "test title");
                inst.max(100);
                inst.build();
                inst.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(100);
                                inst.addOne();
                                if (inst.getBarValue() >= 100) {
                                    inst.dismissByMax();
                                    break;
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }).start();
            }
        });

        frame.add(btn);
        frame.pack();
        frame.setVisible(true);
    }
}
