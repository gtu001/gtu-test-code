package gtu.swing.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JFrame;

import gtu.swing.util.SwingActionUtil.Action;

public class SwingActionUtil_Tester {
    private enum ActionApplyer implements Action {
        TEST_BTN_001() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("DDDDDDDDD");
            }
        }, //
        ;

        public static void addAction(SwingActionUtil swingUtil) {
            for (ActionApplyer e : ActionApplyer.values()) {
                swingUtil.addAction(e.name(), e);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = JFrameUtil.createSimpleFrame(SwingActionUtil_Tester.class);
        final SwingActionUtil swingUtil = SwingActionUtil.newInstance(frame);

        JButton btn = new JButton("test");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction(ActionApplyer.TEST_BTN_001.name(), e);
            }
        });

        frame.add(btn);

        ActionApplyer.addAction(swingUtil);

        frame.pack();
        frame.setVisible(true);
    }
}
