package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import gtu._work.DockerKiller;
import gtu._work.DockerKiller.DockerContainer;
import gtu._work.DockerKiller.DockerImage;
import gtu._work.DockerKiller.DockerVolume;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.SimpleTextDlg;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.JCommonUtil.ActionListenerHex;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class DockerKillerUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JTable psMinusATable;
    private JTable imagesTable;
    private JButton psMinusABtn;
    private JButton imagesBtn;
    DockerKiller mDockerKiller = new DockerKiller();
    private JPanel panel_11;
    private JPanel panel_12;
    private JPanel panel_13;
    private JPanel panel_14;
    private JPanel panel_15;
    private JTable volumeTable;
    private JButton volumeRefreshBtn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(DockerKillerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DockerKillerUI frame = new DockerKillerUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DockerKillerUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 679, 469);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("PS-A", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        psMinusABtn = new JButton("重新整理");
        psMinusABtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                swingUtil.invokeAction("psMinusABtn.click", arg0);
            }
        });
        panel_3.add(psMinusABtn);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        psMinusATable = new JTable();
        JTableUtil.defaultSetting(psMinusATable);
        panel.add(JTableUtil.getScrollPane(psMinusATable), BorderLayout.CENTER);

        psMinusATable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("psMinusATable.click", e);
            }
        });

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("IMAGES", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.NORTH);

        imagesBtn = new JButton("重新整理");
        imagesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("imagesBtn.click", e);
            }
        });
        panel_7.add(imagesBtn);

        panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.WEST);

        panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.EAST);

        panel_10 = new JPanel();
        panel_1.add(panel_10, BorderLayout.SOUTH);

        imagesTable = new JTable();
        JTableUtil.defaultSetting(imagesTable);
        panel_1.add(JTableUtil.getScrollPane(imagesTable), BorderLayout.CENTER);

        imagesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("imagesTable.click", e);
            }
        });

        panel_11 = new JPanel();
        tabbedPane.addTab("VOLUME", null, panel_11, null);
        panel_11.setLayout(new BorderLayout(0, 0));

        panel_12 = new JPanel();
        panel_11.add(panel_12, BorderLayout.NORTH);

        volumeRefreshBtn = new JButton("重新整理");
        volumeRefreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("volumeRefreshBtn.click", e);
            }
        });
        panel_12.add(volumeRefreshBtn);

        panel_13 = new JPanel();
        panel_11.add(panel_13, BorderLayout.WEST);

        panel_14 = new JPanel();
        panel_11.add(panel_14, BorderLayout.EAST);

        panel_15 = new JPanel();
        panel_11.add(panel_15, BorderLayout.SOUTH);

        volumeTable = new JTable();
        volumeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("volumeTable.click", e);
            }
        });
        panel_11.add(JTableUtil.getScrollPane(volumeTable), BorderLayout.CENTER);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/docker.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
            this.setTitle("You Set My World On Fire");
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addActionHex(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addActionHex(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addActionHex("psMinusABtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                initPsMinusATable();
            }
        });
        swingUtil.addActionHex("imagesBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                initImageTable();
            }
        });
        swingUtil.addActionHex("volumeRefreshBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                initVolumeTable();
            }
        });
        swingUtil.addActionHex("psMinusATable.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                int beanColIndex = 7;

                JTable table = (JTable) evt.getSource();
                JTableUtil tabUtl = JTableUtil.newInstance(table);
                DefaultTableModel model = (DefaultTableModel) tabUtl.getModel();
                int rowId = tabUtl.getRealSelectedRow();
                int colId = tabUtl.getRealColumnPos(beanColIndex, table);
                System.out.println("select : " + rowId + " , " + colId);
                DockerContainer bean = (DockerContainer) table.getValueAt(rowId, colId);
                final String deleteId = bean.getCONTAINER_ID();

                if (JMouseEventUtil.buttonRightClick(1, (MouseEvent) evt)) {
                    JPopupMenuUtil.newInstance((JComponent) evt.getSource())//
                            .addJMenuItem("啟動", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent arg0) {
                                    mDockerKiller.commandContainer("start", deleteId);
                                    initPsMinusATable();
                                }
                            })//
                            .addJMenuItem("重新啟動", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent arg0) {
                                    mDockerKiller.commandContainer("restart", deleteId);
                                    initPsMinusATable();
                                }
                            })//
                            .addJMenuItem("停止", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent arg0) {
                                    mDockerKiller.commandContainer("stop", deleteId);
                                    initPsMinusATable();
                                }
                            })//
                            .addJMenuItem("Log", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent arg0) {
                                    String outputMsg = mDockerKiller.commandContainer("logs", deleteId);
                                    SimpleTextDlg mSimpleTextDlg = new SimpleTextDlg(outputMsg, deleteId, new Dimension(650, 450));
                                    mSimpleTextDlg.show();
                                }
                            })//
                            .addJMenuItem("Console", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent arg0) {
                                    mDockerKiller.openConsoleContainer(deleteId);
                                }
                            })//
                            .addJMenuItem("刪除", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent arg0) {
                                    boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除：" + deleteId, "刪除");
                                    if (deleteConfirm) {
                                        mDockerKiller.commandContainer("rm", deleteId);
                                        initPsMinusATable();
                                    }
                                }
                            })//
                            .applyEvent(evt)//
                            .show();
                }
            }
        });
        swingUtil.addActionHex("imagesTable.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                int beanColIndex = 5;

                JTable table = (JTable) evt.getSource();
                JTableUtil tabUtl = JTableUtil.newInstance(table);
                DefaultTableModel model = (DefaultTableModel) tabUtl.getModel();
                int rowId = tabUtl.getRealSelectedRow();
                int colId = tabUtl.getRealColumnPos(beanColIndex, table);
                System.out.println("select : " + rowId + " , " + colId);
                DockerImage bean = (DockerImage) table.getValueAt(rowId, colId);
                final String deleteId = bean.getIMAGE_ID();

                if (JMouseEventUtil.buttonRightClick(1, (MouseEvent) evt)) {
                    JPopupMenuUtil.newInstance((JComponent) evt.getSource())//
                            .addJMenuItem("刪除", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent e) {
                                    boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除：" + deleteId, "刪除");
                                    if (deleteConfirm) {
                                        mDockerKiller.deleteImage(deleteId);
                                        initImageTable();
                                    }
                                }
                            })//
                            .applyEvent(evt)//
                            .show();
                }
            }
        });
        swingUtil.addActionHex("volumeTable.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                int beanColIndex = 2;

                JTable table = (JTable) evt.getSource();
                JTableUtil tabUtl = JTableUtil.newInstance(table);
                DefaultTableModel model = (DefaultTableModel) tabUtl.getModel();
                int rowId = tabUtl.getRealSelectedRow();
                int colId = tabUtl.getRealColumnPos(beanColIndex, table);
                System.out.println("select : " + rowId + " , " + colId);
                DockerVolume bean = (DockerVolume) table.getValueAt(rowId, colId);
                final String deleteId = bean.getVOLUME_NAME();

                if (JMouseEventUtil.buttonRightClick(1, (MouseEvent) evt)) {
                    JPopupMenuUtil.newInstance((JComponent) evt.getSource())//
                            .addJMenuItem("檢視", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent e) {
                                    String volumeInfo = mDockerKiller.inspectVolume(deleteId);
                                    SimpleTextDlg mSimpleTextDlg = new SimpleTextDlg(volumeInfo, deleteId, new Dimension(550, 350));
                                    mSimpleTextDlg.show();
                                }
                            })//
                            .addJMenuItem("刪除", new ActionListenerHex() {
                                @Override
                                public void actionPerformedInner(ActionEvent e) {
                                    boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除：" + deleteId, "刪除");
                                    if (deleteConfirm) {
                                        mDockerKiller.deleteVolume(deleteId);
                                        initVolumeTable();
                                    }
                                }
                            })//
                            .applyEvent(evt)//
                            .show();
                }
            }
        });
    }

    private void initPsMinusATable() {
        DefaultTableModel model = JTableUtil.createModel(true, "CONTAINER_ID", "IMAGE", "COMMAND", "CREATED", "STATUS", "PORTS", "NAMES", "bean");
        List<DockerContainer> lst = mDockerKiller.ps_minus_a();
        for (DockerContainer c : lst) {
            model.addRow(c.toRows());
        }
        psMinusATable.setModel(model);

        Map<String, Object> preferences = new HashMap<String, Object>();
        Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
        presetColumns.put(7, 0);
        preferences.put("presetColumns", presetColumns);

        JTableUtil.newInstance(psMinusATable).setColumnWidths_ByDataContent(psMinusATable, preferences, getInsets(), false);
        System.out.println("psMinusATable size = " + model.getRowCount());
    }

    private void initImageTable() {
        DefaultTableModel model = JTableUtil.createModel(true, "REPOSITORY", "TAG", "IMAGE_ID", "CREATED", "SIZE", "bean");
        List<DockerImage> lst = mDockerKiller.images();
        for (DockerImage c : lst) {
            model.addRow(c.toRows());
        }
        imagesTable.setModel(model);

        Map<String, Object> preferences = new HashMap<String, Object>();
        Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
        presetColumns.put(5, 0);
        preferences.put("presetColumns", presetColumns);

        JTableUtil.newInstance(imagesTable).setColumnWidths_ByDataContent(imagesTable, preferences, getInsets(), false);
        System.out.println("imagesTable size = " + model.getRowCount());
    }

    private void initVolumeTable() {
        DefaultTableModel model = JTableUtil.createModel(true, "DRIVER", "VOLUME NAME", "bean");
        List<DockerVolume> lst = mDockerKiller.volume_ls();
        for (DockerVolume c : lst) {
            model.addRow(c.toRows());
        }
        volumeTable.setModel(model);

        Map<String, Object> preferences = new HashMap<String, Object>();
        Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
        presetColumns.put(2, 0);
        preferences.put("presetColumns", presetColumns);

        JTableUtil.newInstance(volumeTable).setColumnWidths_ByDataContent(volumeTable, preferences, getInsets(), false);
        System.out.println("imagesTable size = " + model.getRowCount());
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item")//
                .addMenuItem("detail1", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file")//
                .addMenuItem("item1", null)//
                .addMenuItem("item2", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
