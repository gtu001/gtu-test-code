package _temp.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
// www .j  av a2 s.c om
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Main4 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Frame");
        frame.add(Box.createRigidArea(new Dimension(400, 300)));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
         gtu.swing.util.JFrameUtil.setVisible(true,frame);

        final JDialog dialog = new JDialog(frame, "Dialog", true);

        int condition = JPanel.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = ((JPanel) dialog.getContentPane()).getInputMap(condition);
        ActionMap actionMap = ((JPanel) dialog.getContentPane()).getActionMap();
        String enter = "enter";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
        actionMap.put(enter, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(Box.createRigidArea(new Dimension(200, 200)));
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
         gtu.swing.util.JFrameUtil.setVisible(true,dialog);

    }
}
