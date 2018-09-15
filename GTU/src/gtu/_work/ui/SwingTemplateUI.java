package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.net.socket.ex1.SocketUtilForSwing;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameUtil;

public class SwingTemplateUI extends JFrame {

    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(SwingTemplateUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SwingTemplateUI frame = new SwingTemplateUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SwingTemplateUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        {
            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            this.applyAppMenu();
        }
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item").addMenuItem("detail1", null).getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file").addMenuItem("item1", null).addMenuItem("item2", null).addChildrenMenu(menu1).getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }
}
