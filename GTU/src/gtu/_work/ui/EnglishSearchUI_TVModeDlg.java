package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameUtil;

import javax.swing.JSlider;
import javax.swing.JTextArea;

public class EnglishSearchUI_TVModeDlg extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JSlider slider;
    private JTextArea textArea;
    private JCheckBox isEditableChk;
    private int fontSize = 30;
    private ActionListener setFontSizeCallback = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("FontSize = " + e.getID());
        }
    };
    private ActionListener okConfirmListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EnglishSearchUI_TVModeDlg.newInstance("測試XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 30, null, null);
        System.out.println("done...");
    }

    public static EnglishSearchUI_TVModeDlg newInstance(String strVal, Integer fontSize, ActionListener setFontSizeCallback, ActionListener okConfirmListener) {
        EnglishSearchUI_TVModeDlg dialog = new EnglishSearchUI_TVModeDlg(fontSize);
        try {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.textArea.setText(strVal);
            if (fontSize != null) {
                dialog.setFontSize(fontSize);
            }
            dialog.setVisible(true);
            if (setFontSizeCallback != null) {
                dialog.setFontSizeCallback = setFontSizeCallback;
            }
            if (okConfirmListener != null) {
                dialog.okConfirmListener = okConfirmListener;
            }
            return dialog;
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
        return dialog;
    }

    private JSlider getJSlider(Integer defaultFontSize) {
        int mix = 24;
        int max = 300;
        int defaultVal = 24;
        if (defaultFontSize != null) {
            defaultVal = defaultFontSize;
        }
        final JSlider jslider = new JSlider(JSlider.HORIZONTAL, mix, max, defaultVal);// 最小值
        // ,最大值,default值
        jslider.setMajorTickSpacing(10);
        jslider.setMinorTickSpacing(5);
        jslider.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jslider.setPaintTicks(true);
        jslider.setPaintLabels(true);
        jslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setFontSize(jslider.getValue());
                setFontSizeCallback.actionPerformed(new ActionEvent(jslider, jslider.getValue(), "fontSizeChange"));
            }
        });
        return jslider;
    }

    private void setFontSize(int textSize) {
        textArea.setFont(new Font("新細明體", Font.PLAIN, textSize));
        fontSize = textSize;
    }

    /**
     * Create the dialog.
     */
    public EnglishSearchUI_TVModeDlg(Integer defaultFontSize) {
        setBounds(100, 100, 652, 474);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setFocusable(false);
            textArea.setEditable(false);
            contentPanel.add(JCommonUtil.createScrollComponent(textArea), BorderLayout.CENTER);
        }
        {
            isEditableChk = new JCheckBox();
            isEditableChk.setToolTipText("可編輯");
            isEditableChk.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    textArea.setFocusable(isEditableChk.isSelected());
                }
            });
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(isEditableChk, BorderLayout.WEST);

            slider = getJSlider(defaultFontSize);
            panel.add(slider, BorderLayout.CENTER);

            contentPanel.add(panel, BorderLayout.NORTH);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.requestFocus();
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okConfirmListener.actionPerformed(new ActionEvent(getFontSize(), getFontSize(), "fontSizeChange"));
                        EnglishSearchUI_TVModeDlg.this.dispose();
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        EnglishSearchUI_TVModeDlg.this.dispose();
                    }
                });
            }
        }
        JFrameUtil.setSizeWithPercent(0.7f, 0.7f, this);
        JCommonUtil.setJFrameCenter(this);
    }

    public int getFontSize() {
        return fontSize;
    }
}
