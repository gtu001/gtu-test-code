package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import gtu._work.ui.JMenuBarUtil;
import gtu._work.ui.RegexReplacer;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;

public class SwingJDesktopTemplateUI {
    private static final int IF_WIDTH = 150;
    private static final int IF_HEIGHT = 100;
    private Random random = new Random();

    private JFrame jframe;
    private JPanel contentPane;
    private JDesktopPane desktopPane;
    private Class<?> uiJframeClass = null;
    private List<JFrame> jframeKeeperLst = new ArrayList<JFrame>();
    private ChangeTabHandlerGtu001 changeTabHandlerGtu001;
    private Dimension internalFrameSize = new Dimension(300, 180);

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        SwingJDesktopTemplateUI tabUI = SwingJDesktopTemplateUI.newInstance(null, "big_boobs.ico", RegexReplacer.class, true);
        tabUI.setSize(1000, 600);
        tabUI.setInternalFrameSize(500, 300);
        tabUI.startUI();
    }

    public static SwingJDesktopTemplateUI newInstance(String title, String iconPath, Class<?> uiJframeClass, boolean initOneTab) {
        return new SwingJDesktopTemplateUI(title, iconPath, uiJframeClass, initOneTab);
    }

    /**
     * Create the frame.
     */
    public SwingJDesktopTemplateUI(String title, String iconPath, Class<?> uiJframeClass, boolean initOneTab) {
        {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
            this.uiJframeClass = uiJframeClass;
        }
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(new Dimension(800, 500));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        jframe.setContentPane(contentPane);

        desktopPane = new JDesktopPane();
        contentPane.add(desktopPane, BorderLayout.CENTER);

        {
            JCommonUtil.setJFrameCenter(jframe);
            JCommonUtil.defaultToolTipDelay();
            this.setIcon(iconPath);
            this.setUITitle(title);
            this.applyAppMenu();
            if (initOneTab) {
                this.addInternalFrame("New Frame", internalFrameSize);
            }
        }
    }

    public void setEventAfterChangeTab(ChangeTabHandlerGtu001 changeTabHandlerGtu001) {
        this.changeTabHandlerGtu001 = changeTabHandlerGtu001;
    }

    private void setUITitle(String title) {
        if (StringUtils.isBlank(title)) {
            title = "You Set My World On Fire";
        }
        jframe.setTitle(title);
    }

    private void setIcon(String iconPath) {
        if (StringUtils.isNotBlank(iconPath)) {
            if (iconPath.contains("/")) {
                JCommonUtil.setJFrameIcon(jframe, iconPath);
            } else {
                JCommonUtil.setJFrameIcon(jframe, "resource/images/ico/" + iconPath);
            }
        }
    }

    public void setSize(int width, int height) {
        jframe.setSize(new Dimension(width, height));
        JCommonUtil.setJFrameCenter(jframe);
    }

    public void setInternalFrameSize(int width, int height) {
        this.internalFrameSize = new Dimension(width, height);
    }

    public void startUI() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    jframe.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addInternalFrame(String title, Dimension position) {
        try {
            final JInternalFrame internalFrame = new JInternalFrame(title);
            internalFrame.setClosable(true);
            internalFrame.setIconifiable(true);
            internalFrame.setMaximizable(true);
            internalFrame.setResizable(true);
            internalFrame.setVisible(true);
            internalFrame.setSize(position);
            int x = random.nextInt((int) position.getWidth() - IF_WIDTH);
            int y = random.nextInt((int) position.getHeight() - IF_HEIGHT);
            internalFrame.setLocation(x, y);
            internalFrame.setVisible(true);
            internalFrame.toFront();
            desktopPane.add(internalFrame);
            desktopPane.getDesktopManager().activateFrame(internalFrame);

            JFrame childFrame = (JFrame) this.uiJframeClass.newInstance();
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            internalFrame.add(panel);

            jframeKeeperLst.add(childFrame);

            internalFrame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        JPopupMenuUtil popupUtil = JPopupMenuUtil.newInstance(internalFrame);//
                        popupUtil.addJMenuItem("修改分頁名子", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String newName = JCommonUtil._jOptionPane_showInputDialog("修改分頁名子", internalFrame.getTitle());
                                if (StringUtils.isBlank(newName)) {
                                    return;
                                }
                                internalFrame.setTitle(newName);
                            }
                        });//
                        popupUtil.addJMenuItem("新增分頁", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String newName = JCommonUtil._jOptionPane_showInputDialog("新增分頁", "New Frame");
                                if (StringUtils.isBlank(newName)) {
                                    newName = "New Frame";
                                }
                                addInternalFrame(newName, internalFrame.getSize());
                            }
                        });//
                        popupUtil.applyEvent(e);
                        popupUtil.show();
                    }
                }
            });

            internalFrame.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (changeTabHandlerGtu001 != null) {
                        changeTabHandlerGtu001.afterChangeTab(jframeKeeperLst);
                    }
                }
            });
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    public interface ChangeTabHandlerGtu001 {
        public void afterChangeTab(List<JFrame> jframeKeeperLst);
    }

    public JFrame getJframe() {
        return jframe;
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("視窗1")//
                .addMenuItem("新增視窗", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newName = JCommonUtil._jOptionPane_showInputDialog("新增分頁", "New Frame");
                        if (StringUtils.isBlank(newName)) {
                            newName = "New Frame";
                        }
                        Dimension newSize = internalFrameSize;
                        if (desktopPane.getSelectedFrame() != null) {
                            newSize = desktopPane.getSelectedFrame().getSize();
                        }
                        addInternalFrame(newName, newSize);
                    }
                })//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("視窗")//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(jframe);
    }
}
