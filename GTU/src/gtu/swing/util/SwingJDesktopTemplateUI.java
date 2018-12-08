package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

import gtu._work.ui.FastDBQueryUI;
import gtu._work.ui.JMenuBarUtil;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu._work.ui.RegexReplacer;
import gtu.file.OsInfoUtil;

public class SwingJDesktopTemplateUI {
    private static final int IF_WIDTH = 150;
    private static final int IF_HEIGHT = 100;
    private Random random = new Random();

    private JFrame jframe;
    private JPanel contentPane;
    private JDesktopPane desktopPane;
    private List<MyJFrameDefine> jframeLst;

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        List<MyJFrameDefine> jframeLst = new ArrayList<MyJFrameDefine>();
        {
            MyJFrameDefine def = new MyJFrameDefine();
            def.setUiJframeClass(RegexReplacer.class);
            def.setTitle("文字替換器");
            jframeLst.add(def);
        }
        {
            MyJFrameDefine def = new MyJFrameDefine();
            def.setUiJframeClass(FastDBQueryUI.class);
            def.setTitle("資料庫處理");
            def.setEventAfterChangeTab(new ChangeTabHandlerGtu001() {
                public void afterChangeTab(List<JFrame> jframeKeeperLst) {
                    if (jframeKeeperLst != null && !jframeKeeperLst.isEmpty()) {
                        for (JFrame inFrame : jframeKeeperLst) {
                            ((FastDBQueryUI) inFrame).reloadAllProperties();
                        }
                    }
                }
            });
            jframeLst.add(def);
        }
        SwingJDesktopTemplateUI tabUI = SwingJDesktopTemplateUI.newInstance(null, "big_boobs.ico", jframeLst);
        tabUI.setSize(1000, 600);
        tabUI.startUI();
    }

    public static SwingJDesktopTemplateUI newInstance(String title, String iconPath, List<MyJFrameDefine> jframeLst) {
        return new SwingJDesktopTemplateUI(title, iconPath, jframeLst);
    }

    /**
     * Create the frame.
     */
    public SwingJDesktopTemplateUI(String title, String iconPath, List<MyJFrameDefine> jframeLst) {
        {
            this.applyWindowLookAndFeel();
            this.jframeLst = jframeLst;
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

            for (MyJFrameDefine jframeDef : this.jframeLst) {
                if (jframeDef.initOneTab) {
                    this.addInternalFrame(jframeDef.title, jframeDef);
                }
            }
        }
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

    private void addInternalFrame(final String title, final MyJFrameDefine jfreamDef) {
        try {
            final JInternalFrame internalFrame = new JInternalFrame(title);
            internalFrame.setClosable(true);
            internalFrame.setIconifiable(true);
            internalFrame.setMaximizable(true);
            internalFrame.setResizable(true);
            internalFrame.setVisible(true);
            int x = random.nextInt((int) jfreamDef.getInternalFrameSize().getWidth() - IF_WIDTH);
            int y = random.nextInt((int) jfreamDef.getInternalFrameSize().getHeight() - IF_HEIGHT);
            internalFrame.setLocation(x, y);
            internalFrame.toFront();

            desktopPane.add(internalFrame);
            desktopPane.getDesktopManager().activateFrame(internalFrame);

            JFrame childFrame = (JFrame) jfreamDef.getUiJframeClass().newInstance();
            if (jfreamDef.getJframeKeeperLst().isEmpty()) {
                internalFrame.setSize(childFrame.getSize());
            } else {
                internalFrame.setSize(jfreamDef.getInternalFrameSize());
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            internalFrame.add(panel);

            jfreamDef.getJframeKeeperLst().add(childFrame);

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
                                addInternalFrame(newName, jfreamDef);
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
                    if (jfreamDef.getEventAfterChangeTab() != null) {
                        jfreamDef.getEventAfterChangeTab().afterChangeTab(jfreamDef.getJframeKeeperLst());
                    }
                }
            });

            internalFrame.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    jfreamDef.setInternalFrameSize(((JInternalFrame) e.getSource()).getSize());
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
        JMenuAppender jMenuAppender = JMenuAppender.newInstance("視窗");
        for (final MyJFrameDefine jframeDef : jframeLst) {
            jMenuAppender.addMenuItem(jframeDef.getTitle(), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newName = JCommonUtil._jOptionPane_showInputDialog("新增分頁", jframeDef.getTitle());
                    if (StringUtils.isBlank(newName)) {
                        newName = jframeDef.getTitle();
                    }
                    addInternalFrame(newName, jframeDef);
                }
            });
        }
        JMenu mainMenu = JMenuAppender.newInstance("視窗")//
                .addChildrenMenu(jMenuAppender.getMenu())//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(jframe);
    }

    private void applyWindowLookAndFeel() {
        if (OsInfoUtil.isWindows()) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }
    }

    public static class MyJFrameDefine {
        String title;
        Class<?> uiJframeClass;
        ChangeTabHandlerGtu001 changeTabHandlerGtu001;
        List<JFrame> jframeKeeperLst = new ArrayList<JFrame>();
        Dimension internalFrameSize = new Dimension(300, 180);
        boolean initOneTab;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Class<?> getUiJframeClass() {
            return uiJframeClass;
        }

        public void setUiJframeClass(Class<?> uiJframeClass) {
            this.uiJframeClass = uiJframeClass;
        }

        public ChangeTabHandlerGtu001 getEventAfterChangeTab() {
            return changeTabHandlerGtu001;
        }

        public void setEventAfterChangeTab(ChangeTabHandlerGtu001 changeTabHandlerGtu001) {
            this.changeTabHandlerGtu001 = changeTabHandlerGtu001;
        }

        public List<JFrame> getJframeKeeperLst() {
            return jframeKeeperLst;
        }

        public void setJframeKeeperLst(List<JFrame> jframeKeeperLst) {
            this.jframeKeeperLst = jframeKeeperLst;
        }

        public Dimension getInternalFrameSize() {
            return internalFrameSize;
        }

        public void setInternalFrameSize(Dimension internalFrameSize) {
            this.internalFrameSize = internalFrameSize;
        }

        public boolean isInitOneTab() {
            return initOneTab;
        }

        public void setInitOneTab(boolean initOneTab) {
            this.initOneTab = initOneTab;
        }
    }
}
