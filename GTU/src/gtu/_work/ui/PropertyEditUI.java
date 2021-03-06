package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu._work.etc.EnglishTester_Diectory2;
import gtu._work.etc.EnglishTester_Diectory2.WordInfo2;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtil;
import gtu.runtime.DesktopUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import taobe.tec.jcc.JChineseConvertor;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY
 * FORquery ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PropertyEditUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JMenu jMenu1;
    private JMenuBar jMenuBar1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextField contentQueryText;
    private JTextField fileQueryText;
    private JMenuItem openDirectoryAndChildren;
    private JTextField queryText;
    private JMenuItem jMenuItem5;
    private JMenuItem jMenuItem4;
    private JMenuItem jMenuItem3;
    private JMenuItem jMenuItem2;
    private JMenuItem jMenuItem1;
    private JList fileList;
    private JTable propTable;
    private JPanel jPanel3;
    private JPanel jPanel2;

    List<File> backupFileList = new ArrayList<File>();

    static File currentFile;
    List<Triple<Integer, String, String>> backupModel;

    private static final boolean DEBUG = !PropertiesUtil.isClassInJar(PropertyEditUI.class);
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        final List<File> sourceFileLst = new ArrayList<File>();
        if (args != null) {
            for (String val : args) {
                File f = new File(val);
                if (f.exists() && f.getName().endsWith(".properties")) {
                    sourceFileLst.add(f);
                }
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PropertyEditUI inst = new PropertyEditUI(sourceFileLst);
                inst.setLocationRelativeTo(null);
                gtu.swing.util.JFrameUtil.setVisible(true, inst);
            }
        });
    }

    public PropertyEditUI(List<File> sourceFileLst) {
        super();
        initGUI(sourceFileLst);
    }

    private void initGUI(List<File> sourceFileLst) {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            BorderLayout thisLayout = new BorderLayout();
            this.setTitle("PropertyEditUI");
            getContentPane().setLayout(thisLayout);
            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    // JTableUtil.setColumnWidths_Percent(propTable, new float[]
                    // { 5, 30, 65 });
                }
            });
            {
                jMenuBar1 = new JMenuBar();
                setJMenuBar(jMenuBar1);
                {
                    jMenu1 = new JMenu();
                    jMenuBar1.add(jMenu1);
                    jMenu1.setText("File");
                    {
                        jMenuItem1 = new JMenuItem();
                        jMenu1.add(jMenuItem1);
                        jMenuItem1.setText("open directory");
                        jMenuItem1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JCommonUtil._jFileChooser_selectDirectoryOnly();
                                if (file == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("請選擇正確目錄!");
                                    return;
                                }
                                loadCurrentDirectory(file);
                            }
                        });
                    }
                    {
                        openDirectoryAndChildren = new JMenuItem();
                        jMenu1.add(openDirectoryAndChildren);
                        openDirectoryAndChildren.setText("open directory and children");
                        openDirectoryAndChildren.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                System.out.println("openDirectoryAndChildren.actionPerformed, event=" + evt);
                                File file = JCommonUtil._jFileChooser_selectDirectoryOnly();
                                if (file == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_info("目錄有誤!");
                                }
                                DefaultListModel model = new DefaultListModel();
                                List<File> list = new ArrayList<File>();
                                FileUtil.searchFileMatchs(file, ".*\\.properties", list);
                                loadCurrentFileLst(list);
                            }
                        });
                    }
                    {
                        jMenuItem2 = new JMenuItem();
                        jMenu1.add(jMenuItem2);
                        jMenuItem2.setText("save");
                        jMenuItem2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                System.out.println("jMenu3.actionPerformed, event=" + evt);
                                if (currentFile == null) {
                                    return;
                                }
                                if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("save to " + currentFile.getName(), "SAVE")) {
                                    try {
                                        Properties prop = new Properties();
                                        // try {
                                        // prop.load(new
                                        // FileInputStream(currentFile));
                                        // } catch (Exception e) {
                                        // e.printStackTrace();
                                        // JCommonUtil.handleException(e);
                                        // return;
                                        // }
                                        loadModelToProperties(prop);
                                        prop.store(new FileOutputStream(currentFile), getTitle());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            }
                        });
                    }

                    {
                        jMenuItem3 = new JMenuItem();
                        jMenu1.add(jMenuItem3);
                        jMenuItem3.setText("save to target");
                        jMenuItem3.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectFileOnly().showSaveDialog().getApproveSelectedFile();
                                if (file == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("file name is not correct!");
                                    return;
                                }
                                if (!file.getName().contains(".properties")) {
                                    file = new File(file.getParent(), file.getName() + ".properties");
                                }
                                try {
                                    Properties prop = new Properties();
                                    // try {
                                    // prop.load(new
                                    // FileInputStream(currentFile));
                                    // } catch (Exception e) {
                                    // e.printStackTrace();
                                    // JCommonUtil.handleException(e);
                                    // return;
                                    // }
                                    loadModelToProperties(prop);
                                    prop.store(new FileOutputStream(file), getTitle());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                    {
                        jMenuItem4 = new JMenuItem();
                        jMenu1.add(jMenuItem4);
                        jMenuItem4.setText("save file(sorted)");
                        jMenuItem4.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                if (currentFile == null) {
                                    return;
                                }
                                try {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile)));
                                    List<String> sortList = new ArrayList<String>();
                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        sortList.add(line);
                                    }
                                    reader.close();
                                    Collections.sort(sortList);
                                    StringBuilder sb = new StringBuilder();
                                    for (String line : sortList) {
                                        sb.append(line + "\n");
                                    }
                                    FileUtil.saveToFile(currentFile, sb.toString(), "UTF8");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    {
                        jMenuItem5 = new JMenuItem();
                        jMenu1.add(jMenuItem5);
                        jMenuItem5.setText("翻譯");
                        jMenuItem5.addActionListener(new ActionListener() {
                            private void setMeaning(String meaning, int rowPos) {
                                propTable.getRowSorter().getModel().setValueAt(meaning, rowPos, 2);
                            }

                            private void setMeaningEn1(String english, int rowPos, List<String> errSb) {
                                EnglishTester_Diectory eng = new EnglishTester_Diectory();
                                try {
                                    WordInfo wordInfo = eng.parseToWordInfo(english);
                                    String meaning = getChs2Big5(wordInfo.getMeaning());
                                    if (hasChinese(meaning)) {
                                        setMeaning(meaning, rowPos);
                                    } else {
                                        setMeaningEn2(english, rowPos, errSb);
                                    }
                                } catch (Exception e) {
                                    errSb.add(english);
                                    e.printStackTrace();
                                }
                            }

                            private void setMeaningEn2(String english, int rowPos, List<String> errSb) {
                                EnglishTester_Diectory2 eng2 = new EnglishTester_Diectory2();
                                try {
                                    WordInfo2 wordInfo = eng2.parseToWordInfo(english);
                                    String meaning = getChs2Big5(StringUtils.join(wordInfo.getMeaningList(), ";"));
                                    setMeaning(meaning, rowPos);
                                } catch (Exception e) {
                                    errSb.add(english);
                                    e.printStackTrace();
                                }
                            }

                            private boolean hasChinese(String val) {
                                return new StringUtil_().getChineseWord(val, true).length() > 0;
                            }

                            public void actionPerformed(ActionEvent evt) {
                                if (currentFile == null) {
                                    return;
                                }

                                // Properties memoryProp = loadFromMemoryBank();
                                Properties memoryProp = new Properties();

                                List<String> errSb = new ArrayList<String>();
                                for (int row = 0; row < propTable.getModel().getRowCount(); row++) {
                                    int rowPos = propTable.getRowSorter().convertRowIndexToModel(row);
                                    String english = StringUtils.trimToEmpty((String) propTable.getRowSorter().getModel().getValueAt(rowPos, 1)).toLowerCase();
                                    String desc = (String) propTable.getRowSorter().getModel().getValueAt(rowPos, 2);

                                    if (memoryProp.containsKey(english) && StringUtils.isNotBlank(memoryProp.getProperty(english))) {
                                        setMeaning(memoryProp.getProperty(english), rowPos);
                                    } else {
                                        if (StringUtils.isBlank(desc) || !hasChinese(desc)) {
                                            // if (!english.contains(" ")) {
                                            // setMeaningEn1(english, rowPos,
                                            // errSb);
                                            // } else {
                                            setMeaningEn2(english, rowPos, errSb);
                                            // }
                                        }
                                    }

                                    if (StringUtils.trimToEmpty(desc).contains("未收錄此詞條")) {
                                        setMeaning("", rowPos);
                                    }
                                }
                                if (!errSb.isEmpty()) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("有錯誤單字:\n" + errSb);
                                }
                            }
                        });
                    }

                    {
                        JMenuItem jMenuItem6 = new JMenuItem();
                        jMenu1.add(jMenuItem6);
                        jMenuItem6.setText("檢查有效");
                        jMenuItem6.addActionListener(new ActionListener() {
                            private void setMeaning(String meaning, int rowPos) {
                                propTable.getRowSorter().getModel().setValueAt(meaning, rowPos, 2);
                            }

                            public void actionPerformed(ActionEvent evt) {
                                for (int row = 0; row < propTable.getModel().getRowCount(); row++) {
                                    int rowPos = propTable.getRowSorter().convertRowIndexToModel(row);
                                    String english = StringUtils.trimToEmpty((String) propTable.getRowSorter().getModel().getValueAt(rowPos, 1)).toLowerCase();
                                    String desc = (String) propTable.getRowSorter().getModel().getValueAt(rowPos, 2);

                                    if (StringUtils.trimToEmpty(desc).contains("未收錄此詞條")) {
                                        setMeaning("", rowPos);
                                    }
                                }
                            }
                        });
                    }

                    {
                        JMenuItem jMenuItem7 = new JMenuItem();
                        jMenu1.add(jMenuItem7);
                        jMenuItem7.setText("只顯示有錯英文");
                        jMenuItem7.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                if (currentFile == null) {
                                    return;
                                }
                                resetPropTable_onlyWorngEnglish();
                            }
                        });
                    }

                    {
                        JMenuItem jMenuItem7 = new JMenuItem();
                        jMenu1.add(jMenuItem7);
                        jMenuItem7.setText("移除錯誤英文");
                        jMenuItem7.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                if (currentFile == null) {
                                    return;
                                }
                                resetPropTable_onlyWorngEnglish_removeErrors();
                            }
                        });
                    }

                    {
                        JMenuItem jMenuItem8 = new JMenuItem();
                        jMenu1.add(jMenuItem8);
                        jMenuItem8.setText("載入校正檔");
                        jMenuItem8.addActionListener(new ActionListener() {

                            private void setFixWord(String oldWord, String newWord, List<String> logLst) {
                                for (int ii = 0; ii < backupModel.size(); ii++) {
                                    Triple<Integer, String, String> t = backupModel.get(ii);
                                    if (StringUtils.equalsIgnoreCase(t.getMiddle(), oldWord)) {

                                        if (!StringUtil_.hasChineseWord(newWord)) {
                                            // 英文替換原來的
                                            backupModel.set(ii, Triple.of(t.getLeft(), newWord, t.getRight()));
                                        } else {
                                            // 中文放右邊
                                            backupModel.set(ii, Triple.of(t.getLeft(), t.getMiddle(), newWord));
                                        }

                                        System.out.println("setFixWord : \t" + ii + "\t" + oldWord + "\t" + newWord);
                                        logLst.add(oldWord + " -> " + newWord);
                                    }
                                }
                            }

                            public void actionPerformed(ActionEvent evt) {
                                File selectFile = JCommonUtil._jFileChooser_selectFileOnly();
                                if (selectFile == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案錯誤!");
                                    return;
                                }
                                List<String> lst = FileUtil.loadFromFile_asList(selectFile, "UTF8");
                                List<String> logLst = new ArrayList<String>();
                                Pattern ptn = Pattern.compile("(.*)?\t(.*)");
                                for (String strVal : lst) {
                                    Matcher mth = ptn.matcher(strVal);
                                    if (mth.find()) {
                                        String oldWord = StringUtils.trimToEmpty(mth.group(1));
                                        String newWord = StringUtils.trimToEmpty(mth.group(2));
                                        if (StringUtils.isNotBlank(newWord)) {
                                            setFixWord(oldWord, newWord, logLst);
                                        }
                                    }
                                }
                                resetPropTable("");
                                JCommonUtil._jOptionPane_showMessageDialog_info("修正以下單字 : \n" + StringUtils.join(logLst, "\n"));
                            }
                        });
                    }

                    {
                        JMenuItem jMenuItem8 = new JMenuItem();
                        jMenu1.add(jMenuItem8);
                        jMenuItem8.setText("載入含等於txt檔");
                        jMenuItem8.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                File selectFile = JCommonUtil._jFileChooser_selectFileOnly();
                                if (selectFile == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案錯誤!");
                                    return;
                                }
                                File_ file2 = new File_(selectFile);
                                Properties prop = PropertiesUtil.loadFromEqualMarkTxt(null, selectFile);
                                file2.loadPropertiesToModel(prop, PropertyEditUI.this);
                                JCommonUtil._jOptionPane_showMessageDialog_info("done!");
                            }
                        });
                    }

                    {
                        JMenuItem jMenuItem8 = new JMenuItem();
                        jMenu1.add(jMenuItem8);
                        jMenuItem8.setText("另存含等於txt檔");
                        jMenuItem8.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectFileOnly().showSaveDialog().getApproveSelectedFile();
                                if (file == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("file name is not correct!");
                                    return;
                                }
                                if (!file.getName().contains(".properties")) {
                                    file = new File(file.getParent(), file.getName() + ".properties");
                                }
                                try {
                                    Properties prop = new Properties();
                                    loadModelToProperties(prop);
                                    PropertiesUtil.storeAsEqualMarkTxt(prop, file, DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
                                    JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                }
            }
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("editor", null, jPanel2, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(550, 314));
                        {
                            TableModel propTableModel = new DefaultTableModel(new String[][] { { "", "", "" }, { "", "", "" } }, new String[] { "index", "Key", "value" });
                            propTable = new JTable();
                            JTableUtil.defaultSetting_AutoResize(propTable);
                            jScrollPane1.setViewportView(propTable);
                            propTable.setModel(propTableModel);
                            propTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    if (JMouseEventUtil.buttonRightClick(1, evt)) {
                                        JTableUtil jTabUtil = JTableUtil.newInstance(propTable);
                                        int[] rows = jTabUtil.getSelectedRows(true);
                                        for (int ii : rows) {
                                            System.out.println(jTabUtil.getModel().getValueAt(ii, 1));
                                        }
                                        JPopupMenuUtil.newInstance(propTable).applyEvent(evt).addJMenuItem(JTableUtil.newInstance(propTable).getDefaultJMenuItems()).show();
                                    }
                                }
                            });
                        }
                    }
                    {
                        queryText = new JTextField();
                        jPanel2.add(queryText, BorderLayout.NORTH);
                        queryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {

                            @Override
                            public void process(DocumentEvent event) {
                                if (currentFile == null) {
                                    return;
                                }
                                String text = StringUtils.trimToEmpty(queryText.getText()).toLowerCase();
                                resetPropTable(text);
                            }
                        }));
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("folder", null, jPanel3, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel3.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(550, 314));
                        {
                            fileList = new JList();
                            jScrollPane2.setViewportView(fileList);
                            fileList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(fileList).defaultJListKeyPressed(evt);
                                }
                            });
                            fileList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    final File_ file = JListUtil.getLeadSelectionObject(fileList);

                                    if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
                                        try {
                                            DesktopUtil.browse(file.file.toURL().toString());
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        return;
                                    }

                                    if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 1) {
                                        file.loadPropertiesToModel(PropertyEditUI.this);
                                        return;
                                    }

                                    if (evt.getButton() == MouseEvent.BUTTON3) {
                                        final File parent = file.file.getParentFile();
                                        JMenuItem openTargetDir = new JMenuItem();
                                        openTargetDir.setText("open : " + parent);
                                        openTargetDir.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                try {
                                                    Desktop.getDesktop().open(parent);
                                                } catch (IOException e1) {
                                                    JCommonUtil.handleException(e1);
                                                }
                                            }
                                        });

                                        JPopupMenuUtil.newInstance(fileList).addJMenuItem(openTargetDir).applyEvent(evt).show();
                                        return;
                                    }
                                }
                            });
                        }
                    }
                    {// 搜尋列 -----------------------↓↓↓↓↓↓↓
                        JPanel filterPanel = new JPanel();
                        {
                            fileQueryText = new JTextField();
                            fileQueryText.setToolTipText("檔名過濾");
                            jPanel3.add(fileQueryText, BorderLayout.NORTH);
                            fileQueryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                @Override
                                public void process(DocumentEvent event) {
                                    String text = JCommonUtil.getDocumentText(event);
                                    System.out.println("fileQueryText : [" + text + "]");
                                    text = text.toLowerCase();
                                    DefaultListModel model = new DefaultListModel();
                                    for (File f : backupFileList) {
                                        if (StringUtils.isBlank(text) || f.getName().replaceAll("\\.properties", "").toLowerCase().contains(text)) {
                                            File_ ff = new File_(f);
                                            model.addElement(ff);
                                        }
                                    }
                                    fileList.setModel(model);
                                }
                            }));

                            contentQueryText = new JTextField();
                            contentQueryText.setToolTipText("內容過濾(按Enter生效)");
                            contentQueryText.addActionListener(new ActionListener() {

                                void addModel(File f, DefaultListModel model) {
                                    File_ ff = new File_(f);
                                    model.addElement(ff);
                                }

                                void resetAllData() {
                                    DefaultListModel model = new DefaultListModel();
                                    for (File f : backupFileList) {
                                        model.addElement(new File_(f));
                                    }
                                    fileList.setModel(model);
                                }

                                public void actionPerformed(ActionEvent evt) {
                                    DefaultListModel model = new DefaultListModel();
                                    String text = contentQueryText.getText();
                                    if (StringUtils.isBlank(contentQueryText.getText())) {
                                        resetAllData();
                                        return;
                                    }
                                    text = text.toLowerCase();
                                    for (File f : backupFileList) {
                                        Properties p = PropertiesUtil.loadProperties(f, null, false);
                                        for (Entry e : p.entrySet()) {
                                            if (StringUtils.defaultString((String) e.getKey()).toLowerCase().contains(text) || //
                                            StringUtils.defaultString((String) e.getValue()).toLowerCase().contains(text)) {
                                                model.addElement(new File_(f));
                                            }
                                        }
                                    }
                                    fileList.setModel(model);
                                    JCommonUtil._jOptionPane_showMessageDialog_info("符合檔案筆數 : " + model.getSize());
                                }
                            });
                        }
                        {
                            filterPanel.setLayout(new FormLayout(
                                    new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                            FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
                            lblNewLabel = new JLabel("檔名過濾");
                            filterPanel.add(lblNewLabel, "2, 2, right, default");
                            filterPanel.add(fileQueryText, "4, 2, fill, default");
                            lblNewLabel_1 = new JLabel("內容過濾(需按Enter)");
                            filterPanel.add(lblNewLabel_1, "2, 4, right, default");
                            filterPanel.add(contentQueryText, "4, 4, fill, default");
                            jPanel3.add(filterPanel, BorderLayout.NORTH);
                        }
                    } // 搜尋列 -----------------------↑↑↑↑↑↑↑
                }
            }

            JCommonUtil.defaultToolTipDelay();
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/english.ico");
            JCommonUtil.setJFrameCenter(this);
            pack();
            this.setSize(571, 408);

            JCommonUtil.applyDropFiles(this, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadInitDragFiles((List<File>) e.getSource());
                }
            });

            this.loadInitDragFiles(sourceFileLst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInitDragFiles(List<File> sourceFileLst) {
        if (sourceFileLst.isEmpty()) {
            loadCurrentDirectory(PropertiesUtil.getJarCurrentPath(this.getClass()));
        } else {
            if (sourceFileLst.size() == 1) {
                File file = sourceFileLst.get(0);
                if (file.isDirectory()) {
                    loadCurrentDirectory(file);
                } else {
                    loadCurrentFileLst(sourceFileLst);
                }
            } else {
                loadCurrentFileLst(sourceFileLst);
            }
        }
        if (!backupFileList.isEmpty() && backupFileList.size() == 1) {
            new File_(backupFileList.get(0)).loadPropertiesToModel(this);
            jTabbedPane1.setSelectedIndex(0);
        } else {
            jTabbedPane1.setSelectedIndex(1);
        }
    }

    void applyPropTableOnBlurEvent() {
        JTableUtil.newInstance(propTable).applyOnCellBlurEvent(null, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Object> map = (Map<String, Object>) e.getSource();

                int row = (Integer) map.get("row");
                int fixCol = (Integer) map.get("col");
                int index = (Integer) propTable.getValueAt(row, JTableUtil.getRealColumnPos(0, propTable));
                String strVal = (String) map.get("value");

                A: for (int ii = 0; ii < backupModel.size(); ii++) {
                    Triple<Integer, String, String> p = backupModel.get(ii);
                    if (p.getLeft() == index) {
                        switch (fixCol) {
                        case 1:
                            backupModel.set(ii, Triple.of(p.getLeft(), strVal, p.getRight()));
                            break;
                        case 2:
                            backupModel.set(ii, Triple.of(p.getLeft(), p.getMiddle(), strVal));
                            break;
                        }
                        break A;
                    }
                }
            }
        });
    }

    void loadModelToProperties(Properties prop) {
        DefaultTableModel model = JTableUtil.newInstance(propTable).getModel();
        for (int ii = 0; ii < model.getRowCount(); ii++) {
            String key = (String) model.getValueAt(ii, 1);
            String value = getChs2Big5((String) model.getValueAt(ii, 2));
            System.out.println(key + " = " + value);
            prop.setProperty(key, value);
        }
    }

    private String getChs2Big5(String value) {
        try {
            value = StringUtils.defaultString(value);
            value = value.replace((char) 65292, ',');
            value = value.replace((char) 65288, '(');
            value = value.replace((char) 65289, ')');
            value = value.replace((char) 65307, ';');
            value = value.replace((char) 65306, ':');
            value = value.replace((char) 8220, '"');
            value = value.replace((char) 8221, '"');
            value = value.replace((char) 12289, ',');
            value = value.replaceAll("…", "...");
            try {
                value = JChineseConvertor.getInstance().s2t(value);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    void loadCurrentDirectory(File file) {
        if (file == null) {
            file = PropertiesUtil.getJarCurrentPath(getClass());
        }
        if (!file.isDirectory() || file.listFiles() == null) {
            return;
        }
        DefaultListModel model = new DefaultListModel();
        backupFileList.clear();
        for (File f : file.listFiles()) {
            if (!(f.isFile() && f.getName().endsWith(".properties"))) {
                continue;
            }
            File_ ff = new File_(f);
            model.addElement(ff);
            backupFileList.add(f);
        }
        fileList.setModel(model);
    }

    void loadCurrentFileLst(List<File> fileLst) {
        if (fileLst == null) {
            return;
        }
        DefaultListModel model = new DefaultListModel();
        backupFileList.clear();
        for (File f : fileLst) {
            if (!(f.isFile() && f.getName().endsWith(".properties"))) {
                continue;
            }
            File_ ff = new File_(f);
            model.addElement(ff);
            backupFileList.add(f);
        }
        fileList.setModel(model);
    }

    private void resetPropTable_onlyWorngEnglish() {
        List<String> wordLst = new ArrayList<String>();
        propTable.setFont(new Font("Serif", Font.PLAIN, 20));
        DefaultTableModel model = JTableUtil.createModel(false, "index", "key", "value");
        for (Triple<Integer, String, String> p : backupModel) {
            String desc = p.getRight();
            if (StringUtils.isBlank(desc) || !StringUtil_.hasChineseWord(desc)) {
                model.addRow(new Object[] { p.getLeft(), p.getMiddle(), p.getRight() });
                wordLst.add(p.getMiddle());
            }
        }
        propTable.setModel(model);
        JTableUtil.newInstance(propTable).columnIsJTextArea("key", 20);
        JTableUtil.newInstance(propTable).columnIsJTextArea("value", 20);
        JTableUtil.setColumnWidths_Percent(propTable, new float[] { 5, 30, 65 });
        applyPropTableOnBlurEvent();
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "GoogleWord.txt"), StringUtils.join(wordLst, "\r\n"), "utf8");
    }

    private void resetPropTable_onlyWorngEnglish_removeErrors() {
        List<String> wordLst = new ArrayList<String>();
        propTable.setFont(new Font("Serif", Font.PLAIN, 20));
        DefaultTableModel model = JTableUtil.createModel(false, "index", "key", "value");
        for (Triple<Integer, String, String> p : backupModel) {
            String desc = p.getRight();
            if (StringUtils.isBlank(desc) || !StringUtil_.hasChineseWord(desc)) {
                // do nothing
            } else {
                model.addRow(new Object[] { p.getLeft(), p.getMiddle(), p.getRight() });
                wordLst.add(p.getMiddle());
            }
        }
        propTable.setModel(model);
        JTableUtil.newInstance(propTable).columnIsJTextArea("key", 20);
        JTableUtil.newInstance(propTable).columnIsJTextArea("value", 20);
        JTableUtil.setColumnWidths_Percent(propTable, new float[] { 5, 30, 65 });
        applyPropTableOnBlurEvent();
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "GoogleWord.txt"), StringUtils.join(wordLst, "\r\n"), "utf8");
    }

    private void resetPropTable(String text) {
        propTable.setFont(new Font("Serif", Font.PLAIN, 20));
        DefaultTableModel model = JTableUtil.createModel(false, "index", "key", "value");
        for (Triple<Integer, String, String> p : backupModel) {
            if (StringUtils.isBlank(text)) {
                model.addRow(new Object[] { p.getLeft(), p.getMiddle(), p.getRight() });
            } else if (p.getMiddle().toLowerCase().contains(text) || p.getRight().toLowerCase().contains(text)) {
                model.addRow(new Object[] { p.getLeft(), p.getMiddle(), p.getRight() });
            }
        }
        propTable.setModel(model);
        JTableUtil.newInstance(propTable).columnIsJTextArea("key", 20);
        JTableUtil.newInstance(propTable).columnIsJTextArea("value", 20);
        JTableUtil.setColumnWidths_Percent(propTable, new float[] { 5, 30, 65 });
        applyPropTableOnBlurEvent();
    }

    static class File_ {
        File file;

        File_(File f) {
            this.file = f;
        }

        @Override
        public String toString() {
            return file.getName();
        }

        void loadPropertiesToModel(PropertyEditUI propertyEditUI) {
            Properties prop = new Properties();
            PropertyEditUI.currentFile = this.file;
            propertyEditUI.setTitle(PropertyEditUI.currentFile.getName());
            try {
                prop.load(new FileInputStream(this.file));
            } catch (Exception e) {
                e.printStackTrace();
            }

            propertyEditUI.backupModel = new ArrayList<Triple<Integer, String, String>>();
            String value = null;
            int index = 0;
            for (String key : prop.stringPropertyNames()) {
                index++;
                value = prop.getProperty(key);
                propertyEditUI.backupModel.add(Triple.of(index, key, propertyEditUI.getChs2Big5(value)));
            }
            propertyEditUI.resetPropTable(null);
        }

        void loadPropertiesToModel(Properties prop, PropertyEditUI propertyEditUI) {
            PropertyEditUI.currentFile = this.file;
            propertyEditUI.setTitle(PropertyEditUI.currentFile.getName());

            propertyEditUI.backupModel = new ArrayList<Triple<Integer, String, String>>();
            String value = null;
            int index = 0;
            for (String key : prop.stringPropertyNames()) {
                index++;
                value = prop.getProperty(key);
                propertyEditUI.backupModel.add(Triple.of(index, key, propertyEditUI.getChs2Big5(value)));
            }
            propertyEditUI.resetPropTable(null);
        }
    }
}
