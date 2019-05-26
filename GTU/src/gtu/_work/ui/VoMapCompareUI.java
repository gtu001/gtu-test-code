package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class VoMapCompareUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JTextArea vo1Area;
    private JTextArea vo2Area;
    private JPanel panel_3;
    private JButton executeBtn;
    private JButton clearBtn;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JTable compareTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(VoMapCompareUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VoMapCompareUI frame = new VoMapCompareUI();
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
    public VoMapCompareUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 610, 458);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Vo ToString或Map", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), }));

        lblNewLabel = new JLabel("vo1");
        panel.add(lblNewLabel, "2, 2");

        vo1Area = new JTextArea();
        vo1Area.setRows(3);
        panel.add(JCommonUtil.createScrollComponent(vo1Area), "4, 2, fill, fill");

        lblNewLabel_1 = new JLabel("vo2");
        panel.add(lblNewLabel_1, "2, 4");

        vo2Area = new JTextArea();
        vo2Area.setRows(3);
        panel.add(JCommonUtil.createScrollComponent(vo2Area), "4, 4, fill, fill");

        lblNewLabel_3 = new JLabel("不支援 List,Array");
        lblNewLabel_3.setForeground(Color.RED);
        panel.add(lblNewLabel_3, "4, 6");

        panel_3 = new JPanel();
        panel.add(panel_3, "4, 16, fill, fill");

        executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeBtn.click", e);
            }
        });
        panel_3.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.click", e);
            }
        });
        panel_3.add(clearBtn);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("比對結果", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.NORTH);

        lblNewLabel_2 = new JLabel("filter");
        panel_4.add(lblNewLabel_2);

        filterText = new JTextField();
        panel_4.add(filterText);
        filterText.setColumns(30);
        filterText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                swingUtil.invokeAction("filterText,lostFocus", e);
            }
        });

        panel_5 = new JPanel();
        panel_1.add(panel_5, BorderLayout.WEST);

        panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.EAST);

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.SOUTH);

        compareTable = new JTable();
        panel_1.add(JCommonUtil.createScrollComponent(compareTable), BorderLayout.CENTER);
        JTableUtil.defaultSetting(compareTable);

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
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private DefaultTableModel allModel;
    private JLabel lblNewLabel_2;
    private JTextField filterText;
    private JLabel lblNewLabel_3;

    private void applyAllEvents() {
        swingUtil.addAction(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addAction(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addAction("executeBtn.click", new Action() {

            private Map<String, String> parseToString(String text) {
                Map<String, String> treeMap = new TreeMap<String, String>();
                {
                    Pattern ptn = Pattern.compile("(\\w+)\\=([\u4e00-\u9fa5\\w\\.○\\-\\_\\:\\s]*)");
                    Matcher mth = ptn.matcher(text);
                    while (mth.find()) {
                        String key = mth.group(1);
                        String val = mth.group(2);
                        treeMap.put(key, val);
                    }
                }
                {
                    Pattern ptn = Pattern.compile("(\\w+)\\=((?:.|\n|\\*)*?)\\,\\s(?=\\w+\\=)");
                    Matcher mth = ptn.matcher(text);
                    while (mth.find()) {
                        String key = mth.group(1);
                        String val = mth.group(2);
                        treeMap.put(key, val);
                    }
                }
                return treeMap;
            }

            private Map<String, String> parseJSON(String text) throws JSONException {
                Map<String, String> rtnMap = new HashMap<String, String>();
                JSONObject json = new JSONObject(text);
                for (Iterator it = json.keys(); it.hasNext();) {
                    String key = (String) it.next();
                    String value = json.getString(key);
                    rtnMap.put(key, value);
                }
                return rtnMap;
            }

            private Map<String, String> parseMain(String text) {
                try {
                    return parseJSON(text);
                } catch (JSONException ex) {
                    return parseToString(text);
                }
            }

            @Override
            public void action(EventObject evt) throws Exception {
                String text1 = StringUtils.trimToEmpty(vo1Area.getText());
                String text2 = StringUtils.trimToEmpty(vo2Area.getText());

                Map<String, String> vo1 = parseMain(text1);
                Map<String, String> vo2 = parseMain(text2);

                Set<String> set1 = new TreeSet<String>();
                set1.addAll(vo1.keySet());
                set1.addAll(vo2.keySet());

                allModel = JTableUtil.createModel(false, "欄位", "vo1", "vo2");
                DefaultTableModel model = allModel;
                compareTable.setModel(model);

                initTableConfig();

                for (String key : set1) {
                    String vo1Val = vo1.containsKey(key) ? vo1.get(key) : "NA";
                    String vo2Val = vo2.containsKey(key) ? vo2.get(key) : "NA";
                    model.addRow(new Object[] { key, vo1Val, vo2Val });
                }
            }
        });
        swingUtil.addAction("clearBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                vo1Area.setText("");
                vo2Area.setText("");
                DefaultTableModel model = JTableUtil.createModel(false, "欄位", "vo1", "vo2");
                compareTable.setModel(model);
            }
        });
        swingUtil.addAction("filterText,lostFocus", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                DefaultTableModel model = JTableUtil.createModel(false, "欄位", "vo1", "vo2");
                compareTable.setModel(model);
                initTableConfig();

                String text = StringUtils.trimToEmpty(filterText.getText());
                String[] text1 = StringUtils.trimToEmpty(filterText.getText()).split("\\^", -1);

                for (int ii = 0; ii < allModel.getRowCount(); ii++) {
                    String name = StringUtils.trimToEmpty((String) allModel.getValueAt(ii, 0));
                    String v1 = StringUtils.trimToEmpty((String) allModel.getValueAt(ii, 1));
                    String v2 = StringUtils.trimToEmpty((String) allModel.getValueAt(ii, 2));
                    boolean findOk = false;
                    if (StringUtils.isBlank(text)) {
                        findOk = true;
                    } else {
                        for (String t : text1) {
                            if (name.toLowerCase().contains(t.toLowerCase())) {
                                findOk = true;
                                break;
                            } else if (v1.toLowerCase().contains(t.toLowerCase())) {
                                findOk = true;
                                break;
                            } else if (v2.toLowerCase().contains(t.toLowerCase())) {
                                findOk = true;
                                break;
                            }
                        }
                    }
                    if (findOk) {
                        model.addRow(new Object[] { name, v1, v2 });
                    }
                }
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

    private void initTableConfig() {
        JTableUtil.newInstance(compareTable).setColumnColor_byCondition(0, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(compareTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2)) {
                    return Pair.of(Color.RED, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(compareTable).setColumnColor_byCondition(1, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(compareTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2) && StringUtils.isNotBlank(String.valueOf(v1)) && !"NA".equals(v1)) {
                    return Pair.of(Color.GREEN, null);
                }
                return null;
            }
        });

        JTableUtil.newInstance(compareTable).setColumnColor_byCondition(2, new JTableUtil.TableColorDef() {
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTableUtil util = JTableUtil.newInstance(compareTable);
                Object v1 = util.getRealValueAt(row, 1);
                Object v2 = util.getRealValueAt(row, 2);
                if (ObjectUtils.notEqual(v1, v2) && StringUtils.isNotBlank(String.valueOf(v2)) && !"NA".equals(v2)) {
                    return Pair.of(Color.GREEN, null);
                }
                return null;
            }
        });

        JTableUtil.setColumnWidths_Percent(compareTable, new float[] { 33, 33, 33 });
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
