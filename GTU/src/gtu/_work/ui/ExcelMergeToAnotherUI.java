package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class ExcelMergeToAnotherUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JTextField mergeFromExcelText;
    private JTextField mergeToExcelText;
    private JTextField mergeFromSheetIndexText;
    private JTextField mergeToSheetIndexText;
    private JPanel panel_3;
    private JButton executeBtn;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JList mergeColumnLst;
    private JRadioButton pkRadio;
    private JRadioButton mergeRadio;
    private JTextField columnFromText;
    private JLabel lblNewLabel_4;
    private JLabel lblToColumn;
    private JTextField columnToText;
    private JButton addColumnDefBtn;
    private ButtonGroup buttonGroup;
    private JLabel lblNewLabel_5;
    private JLabel lblNewLabel_6;
    private JTextField includeRowsText;
    private JTextField excludeRowsText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(ExcelMergeToAnotherUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ExcelMergeToAnotherUI frame = new ExcelMergeToAnotherUI();
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
    public ExcelMergeToAnotherUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 658, 464);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Excel", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        lblNewLabel = new JLabel("From Excel File");
        panel.add(lblNewLabel, "2, 2, right, default");

        mergeFromExcelText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(mergeFromExcelText, false);
        panel.add(mergeFromExcelText, "4, 2, fill, default");
        mergeFromExcelText.setColumns(10);

        lblNewLabel_1 = new JLabel("To Excel");
        panel.add(lblNewLabel_1, "2, 4, right, default");

        mergeToExcelText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(mergeToExcelText, false);
        panel.add(mergeToExcelText, "4, 4, fill, default");
        mergeToExcelText.setColumns(10);

        lblNewLabel_2 = new JLabel("From Sheet Index");
        panel.add(lblNewLabel_2, "2, 8, right, default");

        mergeFromSheetIndexText = new JTextField();
        panel.add(mergeFromSheetIndexText, "4, 8, fill, default");
        mergeFromSheetIndexText.setColumns(10);
        mergeFromSheetIndexText.setText("0");

        lblNewLabel_3 = new JLabel("To Sheet Index");
        panel.add(lblNewLabel_3, "2, 10, right, default");

        mergeToSheetIndexText = new JTextField();
        panel.add(mergeToSheetIndexText, "4, 10, fill, default");
        mergeToSheetIndexText.setColumns(10);
        mergeToSheetIndexText.setText("0");

        lblNewLabel_5 = new JLabel("include rows");
        panel.add(lblNewLabel_5, "2, 14, right, default");

        includeRowsText = new JTextField();
        panel.add(includeRowsText, "4, 14, fill, default");
        includeRowsText.setColumns(10);

        lblNewLabel_6 = new JLabel("exclude rows");
        panel.add(lblNewLabel_6, "2, 16, right, default");

        excludeRowsText = new JTextField();
        panel.add(excludeRowsText, "4, 16, fill, default");
        excludeRowsText.setColumns(10);

        panel_3 = new JPanel();
        panel.add(panel_3, "4, 28, fill, fill");

        executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                swingUtil.invokeAction("executeBtn.click", arg0);
            }
        });
        panel_3.add(executeBtn);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("欄位", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.NORTH);

        pkRadio = new JRadioButton("PK");
        panel_4.add(pkRadio);

        mergeRadio = new JRadioButton("merge");
        panel_4.add(mergeRadio);

        lblNewLabel_4 = new JLabel("from column");
        panel_4.add(lblNewLabel_4);

        columnFromText = new JTextField();
        panel_4.add(columnFromText);
        columnFromText.setColumns(10);

        lblToColumn = new JLabel("<-->  to column");
        panel_4.add(lblToColumn);

        columnToText = new JTextField();
        panel_4.add(columnToText);
        columnToText.setColumns(10);

        addColumnDefBtn = new JButton("加入");
        addColumnDefBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("addColumnDefBtn.click", e);
            }
        });
        panel_4.add(addColumnDefBtn);

        panel_5 = new JPanel();
        panel_1.add(panel_5, BorderLayout.WEST);

        panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.EAST);

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.SOUTH);

        mergeColumnLst = new JList();
        mergeColumnLst.setModel(new DefaultListModel());
        mergeColumnLst.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                JListUtil.newInstance(mergeColumnLst).defaultJListKeyPressed(arg0);
            }
        });
        panel_1.add(JCommonUtil.createScrollComponent(mergeColumnLst), BorderLayout.CENTER);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            buttonGroup = JButtonGroupUtil.createRadioButtonGroup(pkRadio, mergeRadio);

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
        swingUtil.addActionHex("executeBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                Validate.notBlank(mergeFromExcelText.getText(), "未輸入FromExcel");
                Validate.notBlank(mergeToExcelText.getText(), "未輸入ToExcel");
                Validate.notBlank(mergeFromSheetIndexText.getText(), "未輸入FromSheetIndex");
                Validate.notBlank(mergeToSheetIndexText.getText(), "未輸入ToSheetIndex");
                Validate.isTrue(mergeColumnLst.getModel().getSize() != 0, "必須加入條件List");

                File fromExcelFile = new File(mergeFromExcelText.getText());
                File toExcelFile = new File(mergeToExcelText.getText());
                int fromSheetIndex = Integer.parseInt(mergeFromSheetIndexText.getText());
                int toSheetIndex = Integer.parseInt(mergeToSheetIndexText.getText());

                Workbook fromBook = ExcelUtil.getInstance().readExcelAll(fromExcelFile);
                Workbook toBook = ExcelUtil.getInstance().readExcelAll(toExcelFile);

                Sheet fromSheet = fromBook.getSheetAt(fromSheetIndex);
                Sheet toSheet = toBook.getSheetAt(toSheetIndex);

                List<Integer> processRowsArray = this.getProcessRowIndexes(toSheet);
                this.mainProcess(fromSheet, toSheet, processRowsArray);

                String newName = FileUtil.getNameNoSubName(toExcelFile) + "[Merge]." + FileUtil.getSubName(toExcelFile);
                File destExcelFile = new File(FileUtil.DESKTOP_DIR, newName);
                ExcelUtil.getInstance().writeExcel(destExcelFile, toBook);
                JCommonUtil._jOptionPane_showMessageDialog_info(destExcelFile + " 產生完成!");
            }

            private List<Integer> getProcessRowIndexes(Sheet toSheet) {
                String include = includeRowsText.getText();
                String exclude = excludeRowsText.getText();
                List<Integer> allArray = getToSheetAllRows(toSheet);
                if (StringUtils.isNotBlank(include)) {
                    allArray = getRowsArray(include);
                }
                if (StringUtils.isNotBlank(exclude)) {
                    List<Integer> excludeLst = getRowsArray(exclude);
                    allArray.removeAll(excludeLst);
                }
                return allArray;
            }

            private List<Integer> getToSheetAllRows(Sheet toSheet) {
                List<Integer> lst = new ArrayList<Integer>();
                A: for (int ii = 0; ii <= toSheet.getLastRowNum(); ii++) {
                    Row row = toSheet.getRow(ii);
                    if (row == null) {
                        continue A;
                    }
                    lst.add(ii);
                }
                return lst;
            }

            private List<Integer> getRowsArray(String text) {
                Set<Integer> set = new TreeSet<Integer>();
                String[] arry = StringUtils.trimToEmpty(text).split(",", -1);
                for (String str : arry) {
                    if (str.matches("\\s*\\d+\\s*")) {
                        set.add(Integer.parseInt(str));
                    } else if (str.matches("\\s*\\d+\\s*\\-\\s*\\d+\\s*")) {
                        String[] vs = str.split("-");
                        for (int ii = Integer.parseInt(vs[0]); ii <= Integer.parseInt(vs[1]); ii++) {
                            set.add(ii);
                        }
                    }
                }
                return new ArrayList<Integer>(set);
            }

            private void mainProcess(Sheet fromSheet, Sheet toSheet, List<Integer> processRowsArray) {
                List<AddInfo> pkLst = new ArrayList<AddInfo>();
                List<AddInfo> mergeLst = new ArrayList<AddInfo>();
                DefaultListModel model = (DefaultListModel) mergeColumnLst.getModel();
                for (int ii = 0; ii < model.getSize(); ii++) {
                    AddInfo ad = (AddInfo) model.getElementAt(ii);
                    if (ad.addType.equals("PK")) {
                        pkLst.add(ad);
                    } else {
                        mergeLst.add(ad);
                    }
                }

                List<Pair<List<AddInfo>, List<AddInfo>>> lst = new ArrayList<Pair<List<AddInfo>, List<AddInfo>>>();
                for (int ii = 0; ii <= fromSheet.getLastRowNum(); ii++) {
                    Row row = fromSheet.getRow(ii);
                    if (row == null) {
                        continue;
                    }

                    List<AddInfo> pkLst1 = new ArrayList<AddInfo>();
                    List<AddInfo> mergeLst1 = new ArrayList<AddInfo>();
                    for (AddInfo ad : pkLst) {
                        AddInfo ad2 = getClone(ad);
                        ad2.fromValue = StringUtils.trimToEmpty(ExcelUtil.getInstance().readCell(row, ad2.fromColumn));
                        pkLst1.add(ad2);
                    }
                    for (AddInfo ad : mergeLst) {
                        AddInfo ad2 = getClone(ad);
                        ad2.fromValue = ExcelUtil.getInstance().readCell(row, ad2.fromColumn);
                        mergeLst1.add(ad2);
                    }
                    lst.add(Pair.of(pkLst1, mergeLst1));
                }

                B: for (Pair<List<AddInfo>, List<AddInfo>> pair : lst) {
                    List<AddInfo> pkLst1 = pair.getLeft();
                    List<AddInfo> mergeLst1 = pair.getRight();

                    A: for (int ii = 0; ii <= toSheet.getLastRowNum(); ii++) {
                        Row row = toSheet.getRow(ii);
                        if (row == null) {
                            continue A;
                        } else if (!processRowsArray.contains(ii)) {
                            continue A;
                        }

                        for (AddInfo ad2 : pkLst1) {
                            String toValue = StringUtils.trimToEmpty(ExcelUtil.getInstance().readCell(row, ad2.toColumn));
                            if (!StringUtils.equals(toValue, ad2.fromValue)) {
                                continue A;
                            }
                        }
                        for (AddInfo ad2 : mergeLst1) {
                            System.out.println("\t更新 row " + ii + " -> " + ad2.fromValue);
                            String orignValue = ExcelUtil.getInstance().readCell(row, ad2.toColumn);
                            if (StringUtils.isNotBlank(orignValue)) {
                                orignValue = orignValue + " ";
                            }
                            ExcelUtil.getInstance().setCellValue(row, ad2.toColumn, orignValue + ad2.fromValue);
                        }
                    }
                }
            }

            private AddInfo getClone(AddInfo ad) {
                try {
                    return (AddInfo) ad.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException("無法clone : " + ReflectionToStringBuilder.toString(ad));
                }
            }
        });

        swingUtil.addActionHex("addColumnDefBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                String addType = "";
                if (JButtonGroupUtil.getSelectedButton(buttonGroup) == pkRadio) {
                    addType = "PK";
                } else if (JButtonGroupUtil.getSelectedButton(buttonGroup) == mergeRadio) {
                    addType = "Merge";
                } else {
                    Validate.isTrue(false, "請選擇Radio!");
                }

                Validate.notBlank(columnFromText.getText(), "輸入來源column");
                Validate.notBlank(columnToText.getText(), "輸入目的column");

                AddInfo ad = new AddInfo();
                ad.addType = addType;
                ad.fromColumn = StringUtils.trimToEmpty(columnFromText.getText()).toUpperCase();
                ad.toColumn = StringUtils.trimToEmpty(columnToText.getText()).toUpperCase();

                DefaultListModel model = (DefaultListModel) mergeColumnLst.getModel();
                for (int ii = 0; ii < model.getSize(); ii++) {
                    if (model.getElementAt(ii).equals(ad)) {
                        Validate.isTrue(false, "資料已存在");
                    }
                }
                model.addElement(ad);
            }
        });
    }

    private class AddInfo implements Cloneable, Serializable {
        String fromColumn;
        String toColumn;
        String addType;

        String fromValue;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public String toString() {
            String prefix = "";
            if ("PK".equalsIgnoreCase(addType)) {
                prefix = "比較來源";
            } else {
                prefix = "設定內容";
            }
            return "<html>[" + prefix + "]&nbsp;&nbsp;<font color='blue'>" + fromColumn + "</font> -> <font color='green'>" + toColumn + "</font></html>";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getEnclosingInstance().hashCode();
            result = prime * result + ((addType == null) ? 0 : addType.hashCode());
            result = prime * result + ((fromColumn == null) ? 0 : fromColumn.hashCode());
            result = prime * result + ((toColumn == null) ? 0 : toColumn.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            AddInfo other = (AddInfo) obj;
            if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
                return false;
            if (addType == null) {
                if (other.addType != null)
                    return false;
            } else if (!addType.equals(other.addType))
                return false;
            if (fromColumn == null) {
                if (other.fromColumn != null)
                    return false;
            } else if (!fromColumn.equals(other.fromColumn))
                return false;
            if (toColumn == null) {
                if (other.toColumn != null)
                    return false;
            } else if (!toColumn.equals(other.toColumn))
                return false;
            return true;
        }

        private ExcelMergeToAnotherUI getEnclosingInstance() {
            return ExcelMergeToAnotherUI.this;
        }
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
