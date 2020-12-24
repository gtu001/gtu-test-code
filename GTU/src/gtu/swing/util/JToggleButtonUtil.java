package gtu.swing.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

public class JToggleButtonUtil {

    public static JToggleButton createSimpleButton(String onLabel, String offLabel, boolean isInitOn, final ActionListener mListener) {
        final JToggleButton toggleBtn = new JToggleButton(offLabel);
        toggleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String label = toggleBtn.isSelected() ? onLabel : offLabel;
                toggleBtn.setText(label);
                if (mListener != null) {
                    mListener.actionPerformed(new ActionEvent(toggleBtn, -1, "triggerBtn"));
                }
            }
        });
        if (isInitOn) {
            toggleBtn.setSelected(true);
        }
        return toggleBtn;
    }
}
