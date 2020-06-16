package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;

import gtu.swing.util.DraggableTabbedPane.DraggableTabbedPaneMove;

public class SwingTabTemplateUI {

    private JPanel contentPane;
    private DraggableTabbedPane tabbedPane;
    private JFrame jframe;
    private Class<?> uiJframeClass = null;
    private List<JFrame> jframeKeeperLst = new ArrayList<JFrame>();
    private ChangeTabHandlerGtu001 changeTabHandlerGtu001;
    private FocusTabHandlerGtu001 focusTabHandlerGtu001;
    private Map<String, Object> resourcesPool = new HashMap<String, Object>();
    private SysTrayUtil sysTray = SysTrayUtil.newInstance();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    }

    public static SwingTabTemplateUI newInstance(String title, String iconPath, Class<?> uiJframeClass, boolean initOneTab, SwingTabTemplateUI_Callback callback) {
        return new SwingTabTemplateUI(title, iconPath, uiJframeClass, initOneTab, callback);
    }

    /**
     * @wbp.parser.entryPoint
     */
    public SwingTabTemplateUI(String title, String iconPath, Class<?> uiJframeClass, boolean initOneTab, SwingTabTemplateUI_Callback callback) {
        {
            this.uiJframeClass = uiJframeClass;
        }
        jframe = new JFrame();
        if (callback != null) {
            callback.beforeInit(this);
        }
        // jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        jframe.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        tabbedPane = new DraggableTabbedPane();// new
                                               // JTabbedPane(JTabbedPane.TOP);
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
                    final int idx = tabbedPane.getSelectedIndex();
                    final int clickTabIdx = getClickTabIndex(e);
                    System.out.println("tab clk " + idx + " / " + clickTabIdx);

                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        JPopupMenuUtil popupUtil = JPopupMenuUtil.newInstance(tabbedPane);//
                        if (clickTabIdx == -1) {
                            popupUtil.addJMenuItem("新增分頁", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String newName = JCommonUtil._jOptionPane_showInputDialog("新增分頁", "New Tab" + tabbedPane.getTabCount());
                                    if (StringUtils.isBlank(newName)) {
                                        newName = "New Tab" + tabbedPane.getTabCount();
                                    }
                                    addTab(newName, true);
                                }
                            });//
                        }
                        if (idx == clickTabIdx) {
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
                        /*
                         * popupUtil.addJMenuItem("修改分頁名子", new ActionListener()
                         * {
                         * 
                         * @Override public void actionPerformed(ActionEvent e)
                         * { String newName =
                         * JCommonUtil._jOptionPane_showInputDialog( "修改分頁名子",
                         * tabbedPane.getTitleAt(idx)); if
                         * (StringUtils.isBlank(newName)) { return; }
                         * tabbedPane.setTitleAt(idx, newName); } });//
                         */

                        popupUtil.applyEvent(e);
                        popupUtil.show();
                    }

                    if (JMouseEventUtil.buttonLeftClick(2, e)) {
                        if (idx == clickTabIdx) {
                            String newName = JCommonUtil._jOptionPane_showInputDialog("修改分頁名子", tabbedPane.getTitleAt(idx));
                            if (StringUtils.isBlank(newName)) {
                                return;
                            }
                            tabbedPane.setTitleAt(idx, newName);
                        }
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        tabbedPane.setEventOfMove(new DraggableTabbedPaneMove() {
            @Override
            public void beforeMoveFromTo(int fromIndex, int toIndex) {
                TreeMap<Integer, JFrame> oldMap = new TreeMap<Integer, JFrame>();
                for (int ii = 0; ii < jframeKeeperLst.size(); ii++) {
                    JFrame f = jframeKeeperLst.get(ii);
                    oldMap.put(ii, f);
                }
                JFrame from = oldMap.get(fromIndex);
                JFrame to = oldMap.get(toIndex);
                oldMap.put(fromIndex, to);
                oldMap.put(toIndex, from);
                jframeKeeperLst = new ArrayList<JFrame>(oldMap.values());
            }
        });
        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowLostFocus(WindowEvent e) {
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (focusTabHandlerGtu001 != null) {
                    focusTabHandlerGtu001.focusOnWin(jframeKeeperLst);
                }
            }
        });
        jframe.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
            }
        });
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        {
            JCommonUtil.setJFrameCenter(jframe);
            JCommonUtil.defaultToolTipDelay();
            this.setIcon(iconPath);
            this.setUITitle(title);
            if (initOneTab) {
                this.addTab("New Tab", true);
            }
        }

        if (callback != null) {
            callback.afterInit(this);
        }
    }

    public void setWindowCloseEvent(final ActionListener closeListener) {
        jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jframe.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("jdialog window closed event received");
            }

            public void windowClosing(WindowEvent e) {
                boolean isDoClose = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要取消 : " + jframe.getTitle(), "取消 : " + jframe.getTitle());
                if (isDoClose) {
                    if (closeListener != null) {
                        closeListener.actionPerformed(new ActionEvent(SwingTabTemplateUI.this, -1, "doClose"));
                    }
                    JFrameUtil.setVisible(false, jframe);
                    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        });
    }

    public SysTrayUtil getSysTrayUtil() {
        return sysTray;
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

    public interface SwingTabTemplateUI_Callback {
        void beforeInit(SwingTabTemplateUI self);

        void afterInit(SwingTabTemplateUI self);
    }

    public interface ChangeTabHandlerGtu001 {
        public void afterChangeTab(int tabIndex, List<JFrame> jframeKeeperLst);
    }

    public interface FocusTabHandlerGtu001 {
        public void focusOnWin(List<JFrame> jframeKeeperLst);
    }

    public void setEventAfterChangeTab(ChangeTabHandlerGtu001 changeTabHandlerGtu001) {
        this.changeTabHandlerGtu001 = changeTabHandlerGtu001;
    }

    public void setEventOnFocus(FocusTabHandlerGtu001 focusTabHandlerGtu001) {
        this.focusTabHandlerGtu001 = focusTabHandlerGtu001;
    }

    public void setSize(int width, int height) {
        jframe.setSize(new Dimension(width, height));
        JCommonUtil.setJFrameCenter(jframe);
    }

    public void setPosition(int x, int y) {
        jframe.setBounds(x, y, jframe.getBounds().width, jframe.getBounds().height);
    }

    public void addTab(String tabName, boolean moveToNew) {
        try {
            JFrame childFrame = (JFrame) this.uiJframeClass.newInstance();
            JPanel panel = new JPanel();
            tabbedPane.addTab(tabName, null, panel, null);
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            jframeKeeperLst.add(childFrame);
            if (moveToNew) {
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    public JFrame getJframe() {
        return jframe;
    }

    public Map<String, Object> getResourcesPool() {
        return resourcesPool;
    }

    public int getSelectTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public void setSelectTabIndex(int idx) {
        tabbedPane.setSelectedIndex(idx);
    }

    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    public List<JFrame> getJframeKeeperLst() {
        return jframeKeeperLst;
    }

    public void setTabTitle(Integer idx, String title) {
        if (idx == null) {
            idx = tabbedPane.getSelectedIndex();
        }
        tabbedPane.setTitleAt(idx, title);
    }

    public int getClickTabIndex(MouseEvent e) {
        return tabbedPane.indexAtLocation(e.getX(), e.getY());
    }
}
