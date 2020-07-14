package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;

import gtu._work.CodeFindRelative001;
import gtu._work.CodeFindRelative001.SecondFindDef;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JProgressBarHelper;
import gtu.swing.util.JSwingCommonConfigUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextFieldUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class CodeFindRelative001UI extends JFrame {

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
    private JLabel lblNewLabel;
    private JTextField mainSearchText;
    private JTextField searchDirText;
    private JLabel label;
    private JLabel label_1;
    private JTextField fileNameFilterText;
    private JLabel label_2;
    private JTextField gapLineNumberText;
    private JCheckBox isAndChk;
    private JButton executeBtn;
    private JTable secondTable;
    private JButton clearBtn;
    private JCheckBox isIgnoreCaseChk;
    private JLabel lblNewLabel_1;
    private JTextField encodingText;

    private PropertiesUtilBean config = new PropertiesUtilBean(CodeFindRelative001UI.class);
    private JComboBox exportStyleCombobox;
    {
        JSwingCommonConfigUtil.checkTestingPropertiesUtilBean(config, getClass(), getClass().getSimpleName());
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(CodeFindRelative001UI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CodeFindRelative001UI frame = new CodeFindRelative001UI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                    frame.initSecondTable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public CodeFindRelative001UI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 785, 505);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        lblNewLabel = new JLabel("主搜尋");
        panel_3.add(lblNewLabel);

        mainSearchText = new JTextField();
        panel_3.add(mainSearchText);
        mainSearchText.setColumns(10);

        label_1 = new JLabel("檔名過濾");
        panel_3.add(label_1);

        fileNameFilterText = new JTextField();
        fileNameFilterText.setColumns(10);
        panel_3.add(fileNameFilterText);

        label_2 = new JLabel("範圍行數");
        panel_3.add(label_2);

        gapLineNumberText = new JTextField();
        gapLineNumberText.setColumns(5);
        panel_3.add(gapLineNumberText);

        isAndChk = new JCheckBox("AND條件相依");
        panel_3.add(isAndChk);

        exportStyleCombobox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("html");
        model.addElement("txt");
        exportStyleCombobox.setModel(model);
        panel_3.add(exportStyleCombobox);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.SOUTH);

        lblNewLabel_1 = new JLabel("編碼");
        panel_5.add(lblNewLabel_1);

        encodingText = new JTextField();
        panel_5.add(encodingText);
        encodingText.setColumns(10);

        isIgnoreCaseChk = new JCheckBox("忽略大小寫");
        panel_5.add(isIgnoreCaseChk);

        label = new JLabel("搜尋目錄");
        panel_5.add(label);

        searchDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(searchDirText, true);
        searchDirText.setColumns(10);
        panel_5.add(searchDirText);

        executeBtn = new JButton("開始");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeBtn.click", e);
            }
        });
        panel_5.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.click", e);
            }
        });
        panel_5.add(clearBtn);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.EAST);

        secondTable = new JTable();
        JTableUtil.defaultSetting(secondTable);
        secondTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                swingUtil.invokeAction("secondTable.click", arg0);
            }
        });
        panel.add(JCommonUtil.createScrollComponent(secondTable), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
            this.setTitle("You Set My World On Fire");

            initSecondTable();

            config.reflectInit(this);
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void initSecondTable() {
        final int[] editableColumns = new int[] { 0, 1, 2, 3 };
        final Object[] header = new Object[] { "搜尋字串", "有效行數", "是否為正則" };
        final Class<?>[] typeLst = new Class[] { String.class, Integer.class, Boolean.class, Boolean.class };
        DefaultTableModel model = JTableUtil.createModel(editableColumns, header, typeLst);
        model.addRow(new Object[] { "", 3, false, true });
        model.addRow(new Object[] { "", 3, false, true });
        model.addRow(new Object[] { "", 3, false, true });
        model.addRow(new Object[] { "", 3, false, true });
        secondTable.setModel(model);
        JTableUtil.setColumnWidths_Percent(secondTable, new float[] { 50f, 13f, 13f });
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
        swingUtil.addActionHex("clearBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                // searchDirText
                mainSearchText.setText("");
                fileNameFilterText.setText("");
                gapLineNumberText.setText("2");
                isAndChk.setSelected(false);
                initSecondTable();
            }
        });
        swingUtil.addActionHex("secondTable.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                MouseEvent e = (MouseEvent) evt;
                if (JMouseEventUtil.buttonRightClick(1, evt)) {
                    JPopupMenuUtil.newInstance(secondTable)//
                            .addJMenuItem(JTableUtil.newInstance(secondTable).getDefaultJMenuItems())//
                            .applyEvent(evt).show();
                }
            }
        });
        swingUtil.addActionHex("executeBtn.click", new Action() {

            @Override
            public void action(EventObject evt) throws Exception {
                final JProgressBarHelper prog = JProgressBarHelper.newInstance(CodeFindRelative001UI.this, "搜尋開始");
                prog.indeterminate(true);
                prog.build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CodeFindRelative001 t = new CodeFindRelative001();

                        File searchDir = JCommonUtil.filePathCheck(searchDirText.getText(), "必須為目錄", true);
                        String mainSearch = JCommonUtil.isBlankErrorMsg(mainSearchText.getText(), "主搜尋必輸輸入");

                        List<SecondFindDef> second_finds = new ArrayList<SecondFindDef>();
                        boolean ignoreCase = isIgnoreCaseChk.isSelected();

                        DefaultTableModel model = (DefaultTableModel) secondTable.getModel();
                        JTableUtil util = JTableUtil.newInstance(secondTable);
                        for (int ii = 0; ii < secondTable.getRowCount(); ii++) {
                            String findText = (String) util.getValueAt(true, ii, 0);
                            int affectLineNumber = (Integer) util.getValueAt(true, ii, 1);
                            boolean isRegExp = (Boolean) util.getValueAt(true, ii, 2);
                            if (StringUtils.isBlank(findText)) {
                                continue;
                            }
                            second_finds.add(new SecondFindDef(findText, affectLineNumber, isRegExp, ignoreCase));
                        }

                        File dir_path = searchDir;
                        String main_find_str = mainSearch;
                        String fileNameFilter = StringUtils.trimToEmpty(fileNameFilterText.getText());
                        int range_gap = JTextFieldUtil.getValueFailSetDefault(2, gapLineNumberText);
                        boolean isAnd = isAndChk.isSelected();
                        String encoding = JTextFieldUtil.getValueFailSetDefault("UTF8", encodingText);
                        String exportStyle = (String) exportStyleCombobox.getSelectedItem();

                        t.setStatusTextListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                prog.indeterminate(false);
                                prog.max(e.getID());
                                prog.setStateText(((File) e.getSource()).getName());
                                prog.addOne();
                            }
                        });

                        prog.show();
                        t.execute(dir_path, main_find_str, fileNameFilter, second_finds, ignoreCase, encoding, isAnd, range_gap, exportStyle);
                        prog.dismiss();

                        JCommonUtil._jOptionPane_showMessageDialog_info("執行完成!");

                        config.reflectSetConfig(CodeFindRelative001UI.this);
                        config.store();
                    }
                }).start();
            }
        });
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
