package gtu.swing;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;

import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;

public class Test001 {

    public static void main(String[] args) {
        JFrame f = JFrameUtil.createSimpleFrame(Test001.class);
        JList l1 = new JList();

        DefaultListModel model = (DefaultListModel) JListUtil.createModel();
        model.addElement(new TTT());

        l1.setModel(model);

        f.add(l1);
        f.pack();
        f.setVisible(true);
    }

    static class TTT {
        public String toString() {
            return "<html><font color='#cc88cc'>OK1</font>&nbsp;&nbsp;<font color='#cccc88'>OK2</font>&nbsp;&nbsp;&nbsp;&nbsp;XXXXX</html>";
        }
    }
}
