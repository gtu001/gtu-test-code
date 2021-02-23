package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultSingleSelectionModel;
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
    private CloneTabInterfaceGtu001 cloneTabInterfaceGtu001;
    private Map<String, Object> resourcesPool = new HashMap<String, Object>();
    private SysTrayUtil sysTray = SysTrayUtil.newInstance();
    private boolean isFixTabName;
    private List<String> tooltipLst = new ArrayList<String>();
    private static AtomicBoolean MY_CLICK_TAB_EVENT_HOLDER = new AtomicBoolean(false);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        SwingTabTemplateUI TAB = SwingTabTemplateUI.newInstance("XXXXX", null, null, false, null);
        TAB.startUI();
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
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

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

                    if (JMouseEventUtil.buttonLeftClick(2, e)) {
                        if (idx == clickTabIdx) {
                            String newName = JCommonUtil._jOptionPane_showInputDialog("修改分頁名子", tabbedPane.getTitleAt(idx));
                            if (StringUtils.isBlank(newName)) {
                                return;
                            }
                            tabbedPane.setTitleAt(idx, newName);
                        }
                    }

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
                            if (cloneTabInterfaceGtu001 != null) {
                                popupUtil.addJMenuItem("複製分頁", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        cloneTab(clickTabIdx, true);
                                    }
                                });//
                            }
                            if (getTabCount() > 1) {
                                popupUtil.addJMenuItem("移除分頁", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String message = "<html>移除分頁 : " + tabbedPane.getTitleAt(idx) + "</html>";
                                        boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(message, "是否移除分頁");
                                        if (confirm) {
                                            removeTab(idx);
                                        }
                                    }
                                });//
                            }
                            if (idx > 0) {
                                popupUtil.addJMenuItem("移除左邊分頁", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int tabSize = idx;
                                        for (int removeIdx = 0, ii = 0; ii < tabSize; ii++) {
                                            removeTab(removeIdx);
                                        }
                                    }
                                });//
                            }
                            if (getTabCount() - 1 > idx) {
                                popupUtil.addJMenuItem("移除右邊分頁", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int tabSize = getTabCount();
                                        for (int removeIdx = idx + 1, ii = idx + 1; ii < tabSize; ii++) {
                                            removeTab(removeIdx);
                                        }
                                    }
                                });//
                            }
                        }

                        if (isFixTabName) {
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
                        }

                        popupUtil.applyEvent(e);
                        popupUtil.show();
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
                TreeMap<Integer, String> tooltipMap = new TreeMap<Integer, String>();
                for (int ii = 0; ii < jframeKeeperLst.size(); ii++) {
                    JFrame f = jframeKeeperLst.get(ii);
                    oldMap.put(ii, f);
                    tooltipMap.put(ii, tooltipLst.get(ii));
                }
                {
                    JFrame from = oldMap.get(fromIndex);
                    JFrame to = oldMap.get(toIndex);
                    oldMap.put(fromIndex, to);
                    oldMap.put(toIndex, from);
                    jframeKeeperLst = new ArrayList<JFrame>(oldMap.values());
                }
                {
                    String from = tooltipMap.get(fromIndex);
                    String to = tooltipMap.get(toIndex);
                    tooltipMap.put(fromIndex, to);
                    tooltipMap.put(toIndex, from);
                    tooltipLst = new ArrayList<String>(tooltipMap.values());
                }
            }

            @Override
            public void afterMoveFromTo(int fromIndex, int toIndex) {
                flushTooltips();
            }
        });

        VetoableSingleSelectionModel model = new VetoableSingleSelectionModel();
        model.addVetoableChangeListener(validator);
        tabbedPane.setModel(model);

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
        try {
            if (StringUtils.isNotBlank(iconPath)) {
                if (iconPath.contains("/")) {
                    JCommonUtil.setJFrameIcon(jframe, iconPath);
                } else {
                    JCommonUtil.setJFrameIcon(jframe, "resource/images/ico/" + iconPath);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startUI() {
        startUI(null);
    }

    public void startUI(final Runnable afterStartRun) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    jframe.setVisible(true);
                    if (afterStartRun != null) {
                        afterStartRun.run();
                    }
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

    public interface CloneTabInterfaceGtu001 {
        boolean cloneTab(JFrame cloneFromFrame, JFrame cloneToFrame);
    }

    public void cloneTab(int cloneFromTabIndex, boolean moveToNew) {
        try {
            JFrame cloneFromFrame = jframeKeeperLst.get(cloneFromTabIndex);
            String tabName = this.getTabTitle(cloneFromTabIndex);
            JFrame childFrame = (JFrame) this.uiJframeClass.newInstance();
            boolean isValid = this.cloneTabInterfaceGtu001.cloneTab(cloneFromFrame, childFrame);
            if (!isValid) {
                return;
            }
            JPanel panel = new JPanel();
            tabbedPane.addTab(tabName, null, panel, null);
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            jframeKeeperLst.add(childFrame);
            this.setToolTipTextAt(jframeKeeperLst.size() - 1, null);
            if (moveToNew) {
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    public JFrame addTab(String tabName, boolean moveToNew) {
        JFrame childFrame = null;
        try {
            if (this.uiJframeClass == null) {
                this.uiJframeClass = JFrame.class;
            }
            childFrame = (JFrame) this.uiJframeClass.newInstance();
            JPanel panel = new JPanel();
            tabbedPane.addTab(tabName, null, panel, null);
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            jframeKeeperLst.add(childFrame);
            this.setToolTipTextAt(jframeKeeperLst.size() - 1, null);
            if (moveToNew) {
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
        return childFrame;
    }

    public JFrame addTab(String tabName, JFrame childFrame, boolean moveToNew) {
        try {
            JPanel panel = new JPanel();
            tabbedPane.addTab(tabName, null, panel, null);
            panel.setLayout(new BorderLayout(0, 0));
            panel.add(childFrame.getContentPane(), BorderLayout.CENTER);
            jframeKeeperLst.add(childFrame);
            this.setToolTipTextAt(jframeKeeperLst.size() - 1, null);
            if (moveToNew) {
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
        return childFrame;
    }

    public boolean removeAllTabs() {
        for (int ii = 0; ii < this.getTabCount(); ii++) {
            if (removeTab(ii) == null) {
                return false;
            }
        }
        return true;
    }

    public JFrame removeTab(int index) {
        JFrame childFrame = null;
        try {
            if (index >= 0 && index < getJframeKeeperLst().size()) {
                childFrame = getJframeKeeperLst().get(index);
                tabbedPane.remove(index);
                jframeKeeperLst.remove(index);
                tooltipLst.remove(index);
            }
            flushTooltips();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
        return childFrame;
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

    public String getTabTitle(Integer idx) {
        if (idx == null) {
            idx = tabbedPane.getSelectedIndex();
        }
        return tabbedPane.getTitleAt(idx);
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

    public void setCloneTabInterface(CloneTabInterfaceGtu001 cloneTabInterfaceGtu001) {
        this.cloneTabInterfaceGtu001 = cloneTabInterfaceGtu001;
    }

    public JFrame getCurrentChildJFrame() {
        int index = tabbedPane.getSelectedIndex();
        if (index >= 0 && index < getJframeKeeperLst().size()) {
            return getJframeKeeperLst().get(index);
        }
        return null;
    }

    public void setToolTipTextAt(Integer tabIndex, String tooltip) {
        if (tabIndex == null) {
            tabIndex = getSelectTabIndex();
        }
        if (tooltipLst.size() - 1 < tabIndex) {
            tooltipLst.add("");
        }
        tooltipLst.set(tabIndex, tooltip);
        flushTooltips();
    }

    private void flushTooltips() {
        for (int ii = 0; ii < tooltipLst.size(); ii++) {
            String tooltip1 = tooltipLst.get(ii);
            int tabIndex1 = ii;
            if (StringUtils.isNotBlank(tooltip1)) {
                tooltip1 = "、" + tooltip1;
            }
            String html = "<html><font color='BLUE'><b>" + tabIndex1 + "</b></font>" + StringUtils.trimToEmpty(tooltip1) + "<html>";
            tabbedPane.setToolTipTextAt(tabIndex1, html);
        }
    }

    VetoableChangeListener validator = new VetoableChangeListener() {
        @Override
        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
            int oldSelection = (Integer) evt.getOldValue();
            if ((oldSelection == -1) || isValidTab(evt))
                return;
            throw new PropertyVetoException("change not valid", evt);
        }

        private boolean isValidTab(PropertyChangeEvent evt) {
            // implement your validation logic here

            // 自訂click event ↓↓↓↓↓↓
            if (MY_CLICK_TAB_EVENT_HOLDER.get() == false) {
                MY_CLICK_TAB_EVENT_HOLDER.set(true);
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        MY_CLICK_TAB_EVENT_HOLDER.set(false);
                        timer.cancel();
                    }
                }, 300);
                return true;
            }
            // 自訂click event ↑↑↑↑↑↑
            return false;
        }
    };

    public static class VetoableSingleSelectionModel extends DefaultSingleSelectionModel {

        private VetoableChangeSupport vetoableChangeSupport;

        @Override
        public void setSelectedIndex(int index) {
            if (getSelectedIndex() == index)
                return;
            try {
                fireVetoableChange(getSelectedIndex(), index);
            } catch (PropertyVetoException e) {
                return;
            }
            super.setSelectedIndex(index);
        }

        private void fireVetoableChange(int oldSelectionIndex, int newSelectionIndex) throws PropertyVetoException {
            if (!isVetoable())
                return;
            vetoableChangeSupport.fireVetoableChange("selectedIndex", oldSelectionIndex, newSelectionIndex);

        }

        private boolean isVetoable() {
            if (vetoableChangeSupport == null)
                return false;
            return vetoableChangeSupport.hasListeners(null);
        }

        public void addVetoableChangeListener(VetoableChangeListener l) {
            if (vetoableChangeSupport == null) {
                vetoableChangeSupport = new VetoableChangeSupport(this);
            }
            vetoableChangeSupport.addVetoableChangeListener(l);
        }

        public void removeVetoableChangeListener(VetoableChangeListener l) {
            if (vetoableChangeSupport == null)
                return;
            vetoableChangeSupport.removeVetoableChangeListener(l);
        }
    }
}
