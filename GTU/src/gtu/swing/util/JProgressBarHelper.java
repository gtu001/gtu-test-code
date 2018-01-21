package gtu.swing.util;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        progressBar.setValue(value);
        counterLabel.setText(value + "/" + max + " (" + getDividePercentString(value, max) + ")");
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

    public JProgressBarHelper build() {
        dlg = new JDialog(parentFrame, title, true);
        progressBar = new JProgressBar(0, max);
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
        setBarValue(0);
        return this;
    }
}
