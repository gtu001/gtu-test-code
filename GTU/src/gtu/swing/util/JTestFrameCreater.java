package gtu.swing.util;

import java.awt.FlowLayout;

import javax.swing.JFrame;

public class JTestFrameCreater {

    public static JFrame createSimpleFrame(Class<?> clz) {
        JFrame frame = new JFrame();
        frame.setTitle(clz.getSimpleName());
        frame.setLayout(new FlowLayout());
        JCommonUtil.setJFrameDefaultSetting(frame);
        return frame;
    }
}
