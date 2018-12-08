package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;

public class SwingTabTemplateUI {

    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JFrame jframe;
    private Class<?> uiJframeClass = null;
    private List<JFrame> jframeKeeperLst = new ArrayList<JFrame>();
    private ChangeTabHandlerGtu001 changeTabHandlerGtu001;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    }

    public static SwingTabTemplateUI newInstance(String title, String iconPath, Class<?> uiJframeClass, boolean initOneTab) {
        return new SwingTabTemplateUI(title, iconPath, uiJframeClass, initOneTab);
    }

    /**
     * @wbp.parser.entryPoint
     */
    public SwingTabTemplateUI(String title, String iconPath, Class<?> uiJframeClass, boolean initOneTab) {
        {
            this.uiJframeClass = uiJframeClass;
        }
        jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        jframe.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println("changeTab : " + tabbedPane.getSelectedIndex());
                try {
                    if (tabbedPane.getSelectedIndex() == -1) {
                        return;
                    }
                    if (changeTabHandlerGtu001 != null) {
                        changeTabHandlerGtu001.afterChangeTab(tabbedPane.getSelectedIndex(), jframeKeeperLst);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        JPopupMenuUtil popupUtil = JPopupMenuUtil.newInstance(tabbedPane);//
                        final int idx = tabbedPane.getSelectedIndex();
                        if (idx != -1) {
                            popupUtil.addJMenuItem("修改分頁名子", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String newName = JCommonUtil._jOptionPane_showInputDialog("修改分頁名子", tabbedPane.getTitleAt(idx));
                                    if (StringUtils.isBlank(newName)) {
                                        return;
                                    }
                                    tabbedPane.setTitleAt(idx, newName);
                                }
                            });//
                            popupUtil.addJMenuItem("移除分頁", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確認移除 : " + tabbedPane.getTitleAt(idx), "移除分頁");
                                    if (confirm) {
                                        tabbedPane.remove(idx);
                                        jframeKeeperLst.remove(idx);
                                    }
                                }
                            });//
                        }
                        popupUtil.addJMenuItem("新增分頁", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String newName = JCommonUtil._jOptionPane_showInputDialog("新增分頁", "New Tab" + tabbedPane.getTabCount());
                                if (StringUtils.isBlank(newName)) {
                                    newName = "New Tab" + tabbedPane.getTabCount();
                                }
                                addTab(newName);
                            }
                        });//
                        popupUtil.applyEvent(e);
                        popupUtil.show();
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        {
            JCommonUtil.setJFrameCenter(jframe);
            JCommonUtil.defaultToolTipDelay();
            this.setIcon(iconPath);
            this.setUITitle(title);
            if (initOneTab) {
                this.addTab("New Tab");
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

    public interface ChangeTabHandlerGtu001 {
        public void afterChangeTab(int tabIndex, List<JFrame> jframeKeeperLst);
    }

    public void setEventAfterChangeTab(ChangeTabHandlerGtu001 changeTabHandlerGtu001) {
        this.changeTabHandlerGtu001 = changeTabHandlerGtu001;
    }

    public void setSize(int width, int height) {
        jframe.setSize(new Dimension(width, height));
        JCommonUtil.setJFrameCenter(jframe);
    }

    public void addTab(String tabName) {
        try {
            JFrame childFrame = (JFrame) this.uiJframeClass.newInstance();
            JPanel panel = new JPanel();
            tabbedPane.addTab(tabName, null, panel, null);
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            jframeKeeperLst.add(childFrame);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    public JFrame getJframe() {
        return jframe;
    }
}
