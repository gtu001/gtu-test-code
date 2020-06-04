package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.lang.StringUtils;

import gtu._work.ui.FastDBQueryUI.S2T_And_T2S_EventHandler;

public class SimpleTextDlg {
    final JDialog dlg;
    final JLabel lbl;
    final JTextArea text;
    final AtomicReference<String> strVal = new AtomicReference<String>("");

    public String getMessage() {
        return strVal.get();
    }

    public SimpleTextDlg(Object val, String title, final Dimension size) {
        dlg = new JDialog() {
            public Dimension getPreferredSize() {
                if (size != null) {
                    return size;
                }
                return new Dimension(450, 250);
            }
        };
        dlg.setModal(true);
        final JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout(0, 0));
        lbl = new JLabel("");
        pan.add(lbl, BorderLayout.NORTH);
        text = new JTextArea();
        JTextAreaUtil.applyCommonSetting(text, false);
        pan.add(JCommonUtil.createScrollComponent(text), BorderLayout.CENTER);
        final JButton btn = new JButton("確定");
        pan.add(btn, BorderLayout.SOUTH);
        dlg.getContentPane().add(pan);
        dlg.pack();

        JCommonUtil.setJFrameCenter(dlg);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlg.dispose();
            }
        });
        final Runnable runner = new Runnable() {
            @Override
            public void run() {
                String strVal = StringUtils.defaultString(text.getSelectedText());
                lbl.setText("選擇長度:" + strVal.length() + "/位元長度:" + strVal.getBytes().length);
            }
        };

        S2T_And_T2S_EventHandler mS2T_And_T2S_EventHandler = new S2T_And_T2S_EventHandler(text);
        final JPopupMenuUtil jpopUtil = JPopupMenuUtil.newInstance(text)//
                .addJMenuItem(mS2T_And_T2S_EventHandler.getMenuItem(true))//
                .addJMenuItem(mS2T_And_T2S_EventHandler.getMenuItem(false))//
                .addJMenuItem(mS2T_And_T2S_EventHandler.getMenuItem2(true))//
                .addJMenuItem(mS2T_And_T2S_EventHandler.getMenuItem2(false));

        text.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                runner.run();
                if (JMouseEventUtil.buttonRightClick(1, e)) {
                    jpopUtil.applyEvent(e).show();
                }
            }
        });
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                runner.run();
            }
        });

        if (val != null) {
            strVal.set(String.valueOf(val));
        } else {
            strVal.set("");
        }

        if (StringUtils.isBlank(title)) {
            lbl.setText("選擇長度:" + strVal.get().length() + "/位元長度:" + strVal.get().getBytes().length);
        }
    }

    public void show() {
        text.setText(strVal.get());
        dlg.setVisible(true);
    }
}