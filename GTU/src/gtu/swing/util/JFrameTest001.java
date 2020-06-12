package gtu.swing.util;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class JFrameTest001 {

    public static void main(String[] args) {
        JFrame frame = JFrameUtil.createSimpleFrame(JFrameUtil.class);
        frame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent arg0) {
                System.out.println("componentResized");
            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
                System.out.println("componentMoved");
            }
        });
        frame.show();
    }

}
