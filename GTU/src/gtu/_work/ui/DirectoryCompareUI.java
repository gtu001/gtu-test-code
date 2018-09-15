package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.file.FileUtil.FileZ;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.ProcessWatcher;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextFieldUtil;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DirectoryCompareUI extends javax.swing.JFrame {
    private static final Pattern SEARCHTEXTPATTERN = Pattern.compile("\\s*(name|path|date|size)\\s*(>\\=|>|<\\=|<|!\\=|\\=|\\^\\=|\\$\\=|\\*\\=)\\s*(.*)\\s*");
    private static final String FINDFUNCTION_REL = "相對路徑";
    private static final String FINDFUNCTION_ABS = "絕對路徑";
    private static final String EXTENSION_CUSTOM = "自訂";
    private static final String EXTENSION_ALL = "全部";
    private static final long serialVersionUID = -77988777417910379L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea leftDirText;
    private JLabel jLabel1;
    private JComboBox extensionNameComboBox;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JTextField searchText;
    private JButton executeBtn;
    private JTable dirCompareTable;
    private JTextArea rightDirText;
    private JPanel jPanel2;
    private JCheckBox[] diffMergeChkBox;
    private JButton resetQueryBtn;
    private JButton resetBtn;
    private JLabel jLabel2;
    private JComboBox compareStyleComboBox;
    private JComboBox diffToolComboBox;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DirectoryCompareUI inst = new DirectoryCompareUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
        System.out.println("done...");
    }

    public DirectoryCompareUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
                    {
                        jPanel2 = new JPanel();
                        BoxLayout jPanel2Layout = new BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS);
                        jPanel2.setLayout(jPanel2Layout);
                        jPanel1.add(jPanel2, BorderLayout.NORTH);
                        jPanel2.setPreferredSize(new java.awt.Dimension(660, 36));
                        {
                            leftDirText = new JTextArea();
                            leftDirText.setPreferredSize(leftDirText.getPreferredSize());
                            leftDirText.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                            JCommonUtil.jTextFieldSetFilePathMouseEvent(leftDirText, true);
                            leftDirText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                @Override
                                public void process(DocumentEvent event) {
                                }
                            }));
                            jPanel2.add(leftDirText);
                            JTextFieldUtil.setupDragDropFilePath(leftDirText);
                        }
                        {
                            rightDirText = new JTextArea();
                            rightDirText.setPreferredSize(rightDirText.getPreferredSize());
                            rightDirText.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
                            JCommonUtil.jTextFieldSetFilePathMouseEvent(rightDirText, true);
                            rightDirText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                @Override
                                public void process(DocumentEvent event) {
                                }
                            }));
                            jPanel2.add(rightDirText);
                            JTextFieldUtil.setupDragDropFilePath(rightDirText);
                        }
                        {
                            executeBtn = new JButton();
                            jPanel2.add(executeBtn);
                            jPanel2.add(getResetBtn());
                            executeBtn.setText("\u958b\u59cb\u6bd4\u5c0d");
                            executeBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    compareStart();
                                }
                            });
                        }
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(660, 362));
                        {
                            dirCompareTable = new JTable();
                            // JTableUtil.defaultSetting(dirCompareTable);
                            JTableUtil.defaultSetting_AutoResize(dirCompareTable);
                            jScrollPane1.setViewportView(dirCompareTable);
                            dirCompareTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    dirCompareTableMouseClicked(evt);
                                }
                            });
                            dirCompareTable.setModel(getDefaultTableModel());
                        }
                    }
                    {
                        jPanel3 = new JPanel();
                        FlowLayout jPanel3Layout = new FlowLayout();
                        jPanel3Layout.setAlignOnBaseline(true);
                        jPanel1.add(jPanel3, BorderLayout.SOUTH);
                        jPanel3.setLayout(jPanel3Layout);
                        jPanel3.setPreferredSize(new java.awt.Dimension(843, 62));
                        {
                            jLabel1 = new JLabel();
                            jPanel3.add(jLabel1);
                            jLabel1.setText("\u526f\u6a94\u540d");
                        }
                        {
                            DefaultComboBoxModel extensionNameComboBoxModel = new DefaultComboBoxModel();
                            extensionNameComboBox = new JComboBox();
                            jPanel3.add(extensionNameComboBox);
                            jPanel3.add(getDiffToolComboBox());
                            jPanel3.add(getJLabel2());
                            jPanel3.add(getSearchText());
                            jPanel3.add(getCompareStyleComboBox());
                            jPanel3.add(getResetQueryBtn());
                            addDiffMergeChkBox();
                            extensionNameComboBox.setModel(extensionNameComboBoxModel);
                            {
                                panel = new JPanel();
                                jTabbedPane1.addTab("New tab", null, panel, null);
                                panel.setLayout(new FormLayout(
                                        new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
                                {
                                    lblCustomCommand = new JLabel("custom command");
                                    panel.add(lblCustomCommand, "2, 2, right, default");
                                }
                                {
                                    customCompareText = new JTextField();
                                    customCompareText.setText("\"C:\\Program Files\\TortoiseGit\\bin\\TortoiseGitMerge.exe\"   /base:\"%s\" /theirs:\"%s\"");
                                    panel.add(customCompareText, "4, 2, fill, default");
                                    customCompareText.setColumns(10);
                                }
                                {
                                    configSaveBtn = new JButton("儲存");
                                    configSaveBtn.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                boolean configChange = false;
                                                String customCompareUrl = customCompareText.getText();
                                                if (StringUtils.isNotBlank(customCompareUrl)) {
                                                    configBean.getConfigProp().setProperty(CUSTOM_COMPARE_URL_KEY, customCompareUrl);
                                                    configChange = true;
                                                }
                                                if (configChange) {
                                                    configBean.store();
                                                    JCommonUtil._jOptionPane_showMessageDialog_info("設定儲存成功!");
                                                }
                                            } catch (Exception ex) {
                                                JCommonUtil.handleException(ex);
                                            }
                                        }
                                    });
                                    panel.add(configSaveBtn, "2, 36");
                                }
                            }
                            extensionNameComboBox.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    totalScanFiles(null);
                                }
                            });
                        }
                    }
                }
            }
            pack();
            this.setSize(864, 563);
            
            JCommonUtil.setJFrameIcon(getOwner(), "images/ico/file_merge.ico");

            initConfigBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File NOT_EXIST_FILE;
    private boolean mergeSwap;
    private PropertiesUtilBean configBean = new PropertiesUtilBean(DirectoryCompareUI.class);
    private static final String STARTEAM_KEY = "starTeamConfig";
    private static final String CUSTOM_COMPARE_URL_KEY = "customCompareUrl";

    static {
        int failTime = 0;
        while (NOT_EXIST_FILE == null || !NOT_EXIST_FILE.exists()) {
            try {
                NOT_EXIST_FILE = File.createTempFile("THIS_FILE_", "_NOT_EXISTS");
            } catch (IOException e) {
                e.printStackTrace();
                failTime++;
                if (failTime > 20) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private DefaultTableModel getDefaultTableModel() {
        DefaultTableModel dirCompareTableModel = JTableUtil.createModel(true, "異動", "檔案", "左邊資訊", "右邊資訊");
        return dirCompareTableModel;
    }

    List<Object[]> modelOrignList = new ArrayList<Object[]>();
    MainCompareSearch compareSearch;

    /**
     * TODO
     */
    private void compareStart() {
        try {
            String leftStr = leftDirText.getText();
            String rightStr = rightDirText.getText();
            Validate.notBlank(leftStr, "左邊目錄不可為空");
            Validate.notBlank(rightStr, "右邊目錄不可為空");

            initComponents();

            File leftFile = new File(StringUtils.trim(leftStr));
            File rightFile = new File(StringUtils.trim(rightStr));
            if (!leftFile.exists()) {
                leftFile = File.createTempFile("left_", ".txt");
                if (leftFile.exists()) {
                    leftFile.delete();
                }
                FileUtils.write(leftFile, leftStr, "utf8");
            }
            if (!rightFile.exists()) {
                rightFile = File.createTempFile("right_", ".txt");
                if (rightFile.exists()) {
                    rightFile.delete();
                }
                FileUtils.write(rightFile, rightStr, "utf8");
            }

            File mainFile = leftFile;
            File compareFile = rightFile;

            modelOrignList.clear();
            compareSearch = new MainCompareSearch(mainFile, compareFile) {
                @Override
                void compareFileNotExists(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft) {
                    modelOrignList.add(getCreateRow(mainFile, compareToFile, mainFileIsLeft, DiffMergeStatus.NOT_EXISTS));
                }

                @Override
                void sameFile(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft) {
                    modelOrignList.add(getCreateRow(mainFile, compareToFile, mainFileIsLeft, DiffMergeStatus.SAME));
                }

                @Override
                void differentFile(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft) {
                    modelOrignList.add(getCreateRow(mainFile, compareToFile, mainFileIsLeft, DiffMergeStatus.DIFFERENT));
                }

                @Override
                void complete() {
                    DefaultTableModel dirCompareModel = getDefaultTableModel();
                    DefaultComboBoxModel extensionModel = new DefaultComboBoxModel();
                    Set<String> extensionSet = new TreeSet<String>();

                    for (Object[] rows : modelOrignList) {
                        dirCompareModel.addRow(rows);
                        InfoObj infoObj = (InfoObj) rows[0];
                        // 取得副檔名
                        checkExtensionName(infoObj, extensionSet);
                    }
                    extensionModel.addElement(EXTENSION_ALL);
                    extensionModel.addElement(EXTENSION_CUSTOM);
                    for (String name : extensionSet) {
                        extensionModel.addElement(name);
                    }

                    while (extensionNameComboBox.getModel().getSize() != extensionModel.getSize()) {
                        extensionNameComboBox.setModel(extensionModel);
                        System.out.println("reset extensionNameComboBox");
                    }
                    while (dirCompareTable.getModel().getRowCount() != dirCompareModel.getRowCount()) {
                        dirCompareTable.setModel(dirCompareModel);
                        System.out.println("reset dirCompareTable");
                    }

                    System.out.println("extensionNameComboBox = " + (extensionNameComboBox.getModel() == extensionModel));
                    System.out.println("dirCompareTable = " + (dirCompareTable.getModel() == dirCompareModel));
                    System.out.println("extensionNameComboBox = " + extensionNameComboBox.getModel().getSize());
                    System.out.println("dirCompareTable = " + dirCompareTable.getModel().getRowCount());

                    setTitle("左邊檔案數:" + mainFileCount + "/右邊檔案數:" + compareToFileCount + ", 耗時 :" + completeTime);

                    JCommonUtil._jOptionPane_showMessageDialog_info(//
                            "掃描完成" + //
                    "\n相同:" + this.countSameFile + //
                    "\n不同:" + this.countDifferentFile + //
                    "\n不存在:" + this.countNotExists + //
                    "\n總耗時:" + completeTime + //
                    "\n需要merge:" + modelOrignList.size());
                }
            };

            if (leftFile.isDirectory() && rightFile.isDirectory()) {
                if (StringUtils.equals(FINDFUNCTION_ABS, String.valueOf(compareStyleComboBox.getSelectedItem()))) {
                    compareSearch.executeSameDirectory();
                } else if (StringUtils.equals(FINDFUNCTION_REL, String.valueOf(compareStyleComboBox.getSelectedItem()))) {
                    List<File> mainFileList = new ArrayList<File>();
                    List<File> compareFileList = new ArrayList<File>();
                    FileUtil.searchFilefind(mainFile, ".*", mainFileList);
                    FileUtil.searchFilefind(compareFile, ".*", compareFileList);
                    compareSearch.executeTwoDirectoryButNotSame(mainFileList, compareFileList);
                } else {
                    Validate.isTrue(false, "選擇超出範圍!");
                }
            } else if (leftFile.isFile() && rightFile.isFile()) {
                compareSearch.executeTwoFile();
            } else if (leftFile.isDirectory() && rightFile.isFile()) {
                List<File> mainFileList = new ArrayList<File>();
                List<File> compareFileList = new ArrayList<File>();
                FileUtil.searchFilefind(mainFile, ".*", mainFileList);
                compareFileList.add(compareFile);
                compareSearch.executeTwoDirectoryButNotSame(mainFileList, compareFileList);
                compareStyleComboBox.setSelectedItem(FINDFUNCTION_REL);
            } else if (leftFile.isFile() && rightFile.isDirectory()) {
                List<File> mainFileList = new ArrayList<File>();
                List<File> compareFileList = new ArrayList<File>();
                mainFileList.add(mainFile);
                FileUtil.searchFilefind(compareFile, ".*", compareFileList);
                compareSearch.executeTwoDirectoryButNotSame(mainFileList, compareFileList);
                compareStyleComboBox.setSelectedItem(FINDFUNCTION_REL);
            } else {
                Validate.isTrue(false, "不支援的操作!");
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex, false);
        }
    }

    private void initConfigBean() {
        String customCompareUrl = configBean.getConfigProp().getProperty(CUSTOM_COMPARE_URL_KEY);
        if (StringUtils.isNotBlank(customCompareUrl)) {
            customCompareText.setText(customCompareUrl);
        }
    }

    private void initComponents() {
        DefaultTableModel dirCompareModel = getDefaultTableModel();
        DefaultComboBoxModel extensionModel = new DefaultComboBoxModel();
        extensionModel.addElement(EXTENSION_ALL);

        while (extensionNameComboBox.getModel().getSize() != extensionModel.getSize()) {
            extensionNameComboBox.setModel(extensionModel);
            System.out.println("reset extensionNameComboBox");
        }
        while (dirCompareTable.getModel().getRowCount() != dirCompareModel.getRowCount()) {
            dirCompareTable.setModel(dirCompareModel);
            System.out.println("reset dirCompareTable");
        }

        searchText.setText("");
        setTitle("");
        compareStyleComboBox.setSelectedItem(EXTENSION_ALL);
        if (diffMergeChkBox != null) {
            for (JCheckBox chk : diffMergeChkBox) {
                chk.setSelected(true);
            }
        }
    }

    private void addDiffMergeChkBox() {
        diffMergeChkBox = new JCheckBox[DiffMergeStatus.values().length];
        int ii = 0;
        for (DiffMergeStatus dif : DiffMergeStatus.values()) {
            diffMergeChkBox[ii] = new JCheckBox();
            JCheckBox tempChk = diffMergeChkBox[ii];
            ii++;

            tempChk.setText(dif.toString());
            tempChk.setSelected(true);
            jPanel3.add(tempChk);
            tempChk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    totalScanFiles(null);
                }
            });
        }
    }

    private Object[] getCreateRow(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft, DiffMergeStatus status) {
        InfoObj info = new InfoObj();
        if (mainFileIsLeft) {
            info.mainFile = mainFile;
            info.compareToFile = compareToFile;
        } else {
            info.mainFile = compareToFile;
            info.compareToFile = mainFile;
        }
        info.status2 = status;
        info.status = status.toString();
        if (status == DiffMergeStatus.NOT_EXISTS) {
            info.status = status.toString() + (mainFileIsLeft ? "(右缺)" : "(左缺)");
        } else if (status == DiffMergeStatus.DIFFERENT) {
            String suffix = "";
            if (info.mainFile.lastModified() != info.compareToFile.lastModified() && info.mainFile.lastModified() > info.compareToFile.lastModified()) {
                suffix = mainFileIsLeft ? "(左較新)" : "(右較新)";
            }
            info.status = status.toString() + suffix;
        }
        Object[] rows = new Object[] { info, info.mainFile.getName(), info.getMainFileInfo(), info.getCompareToFileInfo() };
        return rows;
    }

    private boolean checkExtensionName(InfoObj infoObj, Set<String> extensionSet) {
        FileZ file = null;
        if (infoObj.mainFile.exists() && infoObj.mainFile.isFile()) {
            file = infoObj.mainFile;
        } else if (infoObj.compareToFile.exists() && infoObj.compareToFile.isFile()) {
            file = infoObj.compareToFile;
        }
        if (file != null) {
            String fileName = file.getName();
            int subPos = fileName.lastIndexOf(".");
            if (subPos != -1) {
                fileName = fileName.substring(subPos).toLowerCase();
                extensionSet.add(fileName);
            } else {
                System.out.println("extension error : " + fileName);
            }
            return true;
        }
        return false;
    }

    private String getStarTeamPath_FileCompareMerge(boolean ifEmptyThrow) {
        String starTeamStr = null;
        for (File root : File.listRoots()) {
            if (root.listFiles() == null) {
                continue;
            }
            for (File chkRoot : root.listFiles()) {
                File tempExe = new File(chkRoot, "Borland/StarTeam Cross-Platform Client 2008 R2/File Compare Merge.exe");
                if (tempExe.exists()) {
                    try {
                        starTeamStr = tempExe.getCanonicalPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (configBean.getConfigProp().containsKey(STARTEAM_KEY)) {
            starTeamStr = configBean.getConfigProp().getProperty(STARTEAM_KEY);
        }
        if (StringUtils.isBlank(starTeamStr) && ifEmptyThrow) {
            throw new RuntimeException("找不到對應starTeam \"File Compare Merge.exe\"路徑");
        }
        return starTeamStr;
    }

    private void dirCompareTableMouseClicked(MouseEvent evt) {
        try {
            final JTableUtil util = JTableUtil.newInstance(dirCompareTable);
            InfoObj obj = getInfoObj(util.getSelectedRow(), util);

            if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                File mainFile = obj.mainFile.getFile().exists() ? obj.mainFile.getFile() : NOT_EXIST_FILE;
                File compareFile = obj.compareToFile.getFile().exists() ? obj.compareToFile.getFile() : NOT_EXIST_FILE;

                File fileA = mainFile;
                File fileB = compareFile;
                if (mergeSwap) {
                    File tempFile = fileA;
                    fileA = fileB;
                    fileB = tempFile;
                }

                DiffMergeCommand diffCommand = (DiffMergeCommand) diffToolComboBox.getSelectedItem();
                String command = diffCommand.getCommand(this, fileA, fileB);
                System.out.println(command);
                ProcessWatcher.newInstance(Runtime.getRuntime().exec(command)).getStream();
            }
            if (evt.getClickCount() == 1 && evt.getButton() == MouseEvent.BUTTON3) {
                final File mainFile = obj.mainFile.getFile();
                final File compareToFile = obj.compareToFile.getFile();

                JPopupMenuUtil.newInstance(dirCompareTable).applyEvent(evt)//
                        .addJMenuItem("資訊", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JCommonUtil._jOptionPane_showMessageDialog_info(//
                                        "左邊: " + (mainFile.exists() ? mainFile.getAbsolutePath() : "不存在") + //
                                "\n右邊: " + (compareToFile.exists() ? compareToFile.getAbsolutePath() : "不存在"), //
                                        mainFile.getName());
                            }
                        })//
                        .addJMenuItem("merge位置交換:" + (!mergeSwap ? "(左右)" : "(右左)"), new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mergeSwap = !mergeSwap;
                            }
                        })//
                        .addJMenuItem("左 [open file]", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                openFile(mainFile);
                            }
                        })//
                        .addJMenuItem("左 [open dir]", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                openFile(mainFile.getParentFile());
                            }
                        })//
                        .addJMenuItem("右 [open file]", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                openFile(compareToFile);
                            }
                        })//
                        .addJMenuItem("右 [open dir]", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                openFile(compareToFile.getParentFile());
                            }
                        })//
                        .addJMenuItem("左 -> 右 (蓋掉)", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int[] rows = util.getSelectedRows();
                                if (rows != null && rows.length >= 1) {
                                    if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("你選了" + rows.length + "檔案, 確定覆蓋?!", "覆蓋檔案")) {
                                        List<File> fileList = new ArrayList<File>();
                                        for (int ii = 0; ii < rows.length; ii++) {
                                            InfoObj obj = getInfoObj(rows[ii], util);
                                            File mainFile = obj.mainFile.getFile();
                                            File compareToFile = obj.compareToFile.getFile();
                                            overwriteFile(mainFile, compareToFile, fileList);
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        for (File f : fileList) {
                                            sb.append(f + "\n");
                                        }
                                        JCommonUtil._jOptionPane_showMessageDialog_info(sb, "複製完成");
                                    }
                                } else {
                                    overwriteFile(mainFile, compareToFile, null);
                                }
                            }
                        })//
                        .addJMenuItem("右 -> 左 (蓋掉)", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int[] rows = util.getSelectedRows();
                                if (rows != null && rows.length >= 1) {
                                    if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("你選了" + rows.length + "檔案, 確定覆蓋?!", "覆蓋檔案")) {
                                        List<File> fileList = new ArrayList<File>();
                                        for (int ii = 0; ii < rows.length; ii++) {
                                            InfoObj obj = getInfoObj(rows[ii], util);
                                            File mainFile = obj.mainFile.getFile();
                                            File compareToFile = obj.compareToFile.getFile();
                                            overwriteFile(compareToFile, mainFile, fileList);
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        for (File f : fileList) {
                                            sb.append(f + "\n");
                                        }
                                        JCommonUtil._jOptionPane_showMessageDialog_info(sb, "複製完成");
                                    }
                                } else {
                                    overwriteFile(compareToFile, mainFile, null);
                                }
                            }
                        })//
                        .addJMenuItem("倒出左邊(按照目錄結構)", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                pourOutFilesByOrignPath(true);
                            }
                        })//
                        .addJMenuItem("倒出右邊(按照目錄結構)", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                pourOutFilesByOrignPath(false);
                            }
                        }).show();
            }
        } catch (Exception e) {
            JCommonUtil.handleException(e, false);
        }
    }

    /**
     * 道出檔案
     */
    private void pourOutFilesByOrignPath(boolean isLeftPourOut) {
        try {
            JTableUtil util = JTableUtil.newInstance(dirCompareTable);
            int[] rows = util.getSelectedRows();
            System.out.println(Arrays.toString(rows));
            if (rows == null || rows.length == 0) {
                JCommonUtil._jOptionPane_showMessageDialog_error("未選擇檔案!");
                return;
            }

            File pourDir = new File(leftDirText.getText());
            if (!isLeftPourOut) {
                pourDir = new File(rightDirText.getText());
            }

            if (!pourDir.exists() || !pourDir.isDirectory()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("base目錄不存在 : " + pourDir);
                return;
            }

            File pourBaseDir = new File(FileUtil.DESKTOP_DIR + File.separator + //
                    "pourOut_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + //
                    File.separator + pourDir.getName());
            if (!pourBaseDir.exists()) {
                pourBaseDir.mkdirs();
            }

            String basePath = pourDir.getAbsolutePath().toString();

            List<File> fileList = new ArrayList<File>();
            for (int ii = 0; ii < rows.length; ii++) {
                InfoObj obj = getInfoObj(rows[ii], util);
                File mainFile = obj.mainFile.getFile();
                File compareToFile = obj.compareToFile.getFile();
                System.out.println("mainFile : " + mainFile);
                System.out.println("compareToFile : " + compareToFile);

                File pourFile = mainFile;
                if (!isLeftPourOut) {
                    pourFile = compareToFile;
                }

                if (!pourFile.getAbsolutePath().toLowerCase().startsWith(basePath.toLowerCase())) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("道出檔案路徑與base目錄不吻合  \nbase : " + basePath + "\n target : " + pourFile);
                    return;
                }

                String newPath = pourFile.getAbsolutePath().toString().substring(basePath.length());
                System.out.println("newPath - " + newPath);

                File toFile = new File(pourBaseDir, File.separator + newPath);
                if (!toFile.getParentFile().exists()) {
                    toFile.getParentFile().mkdirs();
                }
                FileUtil.copyFile(pourFile, toFile);

                fileList.add(toFile);
            }

            StringBuffer sb = new StringBuffer();
            for (File v : fileList) {
                sb.append(v + "\r\n");
            }

            FileUtil.saveToFile(new File(pourBaseDir, "pourFiles.log"), sb.toString(), "UTF8");
            JCommonUtil._jOptionPane_showMessageDialog_info("匯出完成! 檔案數 : " + fileList.size());
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void openFile(File file) {
        if (!file.exists()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
            return;
        }
        try {
            Desktop.getDesktop().browse(file.toURI());
        } catch (IOException e1) {
            JCommonUtil.handleException(e1, false);
        }
    }

    private InfoObj getInfoObj(int rowPos, JTableUtil util) {
        Object value = util.getModel().getValueAt(rowPos, 0);
        InfoObj obj = null;
        if (value instanceof InfoObj) {
            obj = (InfoObj) value;
        } else {
            throw new RuntimeException("型別不符[InfoObj]!!" + value);
        }
        return obj;
    }

    private void overwriteFile(File srcFile, File destFile, List<File> fileList) {
        try {
            if (!srcFile.exists()) {
                JCommonUtil._jOptionPane_showMessageDialog_info("來源不存在!");
                return;
            }
            if (destFile.exists()) {
                destFile.renameTo(new File(destFile.getParent(), destFile.getName() + ".bak"));
            }
            FileUtils.copyFile(srcFile, destFile);
            if (fileList == null) {
                JCommonUtil._jOptionPane_showMessageDialog_info(destFile, "檔案複製完成");
            } else {
                fileList.add(destFile);
            }
        } catch (IOException e) {
            JCommonUtil.handleException(e, false);
        }
    }

    private JComboBox getDiffToolComboBox() {
        if (diffToolComboBox == null) {
            DefaultComboBoxModel diffToolComboBoxModel = new DefaultComboBoxModel();
            for (DiffMergeCommand e : DiffMergeCommand.values()) {
                diffToolComboBoxModel.addElement(e);
            }
            diffToolComboBox = new JComboBox();
            diffToolComboBox.setModel(diffToolComboBoxModel);
            diffToolComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    DiffMergeCommand e = (DiffMergeCommand) diffToolComboBox.getSelectedItem();
                    if (e == DiffMergeCommand.StarTeam) {
                        String path = getStarTeamPath_FileCompareMerge(false);
                        if (StringUtils.isBlank(path)) {
                            try {
                                File file = JCommonUtil._jFileChooser_selectFileOnly();
                                if (file == null) {
                                    Validate.isTrue(false, "檔案部正確!");
                                }
                                Validate.isTrue(StringUtils.equalsIgnoreCase("File Compare Merge.exe", file.getName()), "檔案不適File Compare Merge.exe");
                                String starTeamConfig = file.getCanonicalPath();

                                Properties prop = configBean.getConfigProp();
                                prop.setProperty(STARTEAM_KEY, starTeamConfig);
                                configBean.store();
                                JCommonUtil._jOptionPane_showMessageDialog_info("路徑儲存成功!");
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex, false);
                            }
                        }
                    }
                }
            });
        }
        return diffToolComboBox;
    }

    /**
     * TODO
     */
    private void totalScanFiles(EventObject event) {
        try {
            // 搜尋字串
            final Map<String, Object> searchTextMap = new HashMap<String, Object>();
            if (event != null && event.getSource() == searchText && //
                    StringUtils.isNotBlank(searchText.getText()) && ((KeyEvent) event).getKeyCode() != 10)//
            {
                return;
            } else {
                String text = searchText.getText();
                Matcher matcher = SEARCHTEXTPATTERN.matcher(text);
                if (matcher.matches()) {
                    final String left = StringUtils.trim(matcher.group(1));
                    String operator = StringUtils.trim(matcher.group(2));
                    String condition = StringUtils.trim(matcher.group(3));
                    System.out.println("left = " + left);
                    System.out.println("operator = " + operator);
                    System.out.println("condition = " + condition);

                    SearchTextEnum currentSearchType = null;
                    for (SearchTextEnum e : SearchTextEnum.values()) {
                        if (StringUtils.equals(e.name(), left)) {
                            currentSearchType = e;
                            break;
                        }
                    }
                    Validate.notNull(currentSearchType, "找不到對應比較方式 : " + left);

                    searchTextMap.put("currentSearchType", currentSearchType);
                    searchTextMap.put("operator", operator);
                    searchTextMap.put("condition", condition);
                } else if (event != null && event instanceof KeyEvent && ((KeyEvent) event).getKeyCode() == 10) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("輸入格式錯誤:" + SEARCHTEXTPATTERN.pattern());
                }
            }

            // 勾選狀態
            final List<DiffMergeStatus> chkList = new ArrayList<DiffMergeStatus>();
            A: for (DiffMergeStatus diff2 : DiffMergeStatus.values()) {
                for (int ii = 0; ii < diffMergeChkBox.length; ii++) {
                    if (StringUtils.equals(diffMergeChkBox[ii].getText(), diff2.toString()) && diffMergeChkBox[ii].isSelected()) {
                        chkList.add(diff2);
                        continue A;
                    }
                }
            }

            // 附檔名搜尋
            String extensionName = (String) extensionNameComboBox.getSelectedItem();
            SingletonMap extensionNameMessageMap = new SingletonMap();
            final SingletonMap extensionNameMap = new SingletonMap();
            if (StringUtils.equals(EXTENSION_CUSTOM, extensionName)) {
                String result = JCommonUtil._jOptionPane_showInputDialog("設定多個符合副檔名項目", EXTENSION_ALL);
                if (StringUtils.isNotBlank(result)) {
                    String[] arry = result.split(",", -1);
                    extensionNameMap.setValue(arry);
                    extensionNameMessageMap.setValue(Arrays.toString(arry));
                } else {
                    extensionNameMap.setValue(new String[] { EXTENSION_ALL });
                    extensionNameMessageMap.setValue(EXTENSION_ALL);
                }
            } else {
                extensionNameMap.setValue(new String[] { extensionName });
                extensionNameMessageMap.setValue(extensionName);
            }
            if (extensionNameCache == null) {
                extensionNameCache = new ExtensionNameCache();
                extensionNameCache.extensionNameMap = extensionNameMap;
                extensionNameCache.extensionNameMessageMap = extensionNameMessageMap;
            }
            if (event != null && event.getSource() != extensionNameComboBox) {
                extensionNameMap.setValue(extensionNameCache.extensionNameMap.getValue());
                extensionNameMessageMap.setValue(extensionNameCache.extensionNameMessageMap.getValue());
            }

            FileSearch search = new FileSearch(DirectoryCompareUI.this) {
                @Override
                boolean isMatch(InfoObj infoObj) {
                    boolean searchTextResult = true;
                    if (searchTextMap.containsKey("currentSearchType") && searchTextMap.containsKey("operator") && searchTextMap.containsKey("condition")) {
                        SearchTextEnum currentSearchType = (SearchTextEnum) searchTextMap.get("currentSearchType");
                        String operator = (String) searchTextMap.get("operator");
                        String condition = (String) searchTextMap.get("condition");
                        searchTextResult = currentSearchType.isMatch(infoObj, operator, condition);
                    }
                    boolean extensionNameResult = true;
                    if (extensionNameMap.getValue() != null) {
                        String[] extensionNameArray = (String[]) extensionNameMap.getValue();
                        if (StringUtils.indexOfAny(EXTENSION_ALL, extensionNameArray) == -1) {
                            boolean findOk = false;
                            for (String extension : extensionNameArray) {
                                if ((infoObj.mainFile.isFile() && infoObj.mainFile.getName().toLowerCase().endsWith(extension))
                                        || (infoObj.compareToFile.isFile() && infoObj.compareToFile.getName().toLowerCase().endsWith(extension))) {
                                    findOk = true;
                                    break;
                                }
                            }
                            if (!findOk) {
                                extensionNameResult = false;
                            }
                        }
                    }

                    boolean chkListResult = chkList.contains(infoObj.status2);
                    return chkListResult && extensionNameResult && searchTextResult;
                }
            };
            search.execute(DirectoryCompareUI.this, //
                    "搜尋[條件:" + StringUtils.defaultString(searchText.getText()) + "][副檔名:" + extensionNameMessageMap.getValue() + "][狀態:" + chkList + "]");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex, false);
        }
    }

    private JTextField getSearchText() {
        if (searchText == null) {
            searchText = new JTextField();
            ToolTipManager.sharedInstance().setInitialDelay(0);
            searchText.setToolTipText("日期: date >= 20140829, 檔案大小: size < 1mb, 檔名: name *= action, 路徑 path *= WEB-INF [詳細:" + SEARCHTEXTPATTERN.pattern() + "]");
            searchText.setPreferredSize(new java.awt.Dimension(222, 24));
            searchText.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent evt) {
                    System.out.println("KeyEvent.getKeyCode = " + evt.getKeyCode());
                    int[] keyCodeIgnore = new int[] { 38, 40, 37, 39, 8, 17, 18, 16 };
                    if (!ArrayUtils.contains(keyCodeIgnore, evt.getKeyCode())) {
                        totalScanFiles(evt);
                    }
                }
            });
        }
        return searchText;
    }

    public JComboBox getCompareStyleComboBox() {
        if (compareStyleComboBox == null) {
            ComboBoxModel compareStyleComboBoxModel = new DefaultComboBoxModel(new String[] { FINDFUNCTION_ABS, FINDFUNCTION_REL });
            compareStyleComboBox = new JComboBox();
            compareStyleComboBox.setModel(compareStyleComboBoxModel);
            ToolTipManager.sharedInstance().setInitialDelay(0);
            compareStyleComboBox.setToolTipText("絕對路徑:完整目錄比對\n相對路徑:檔案較少的目錄被設定為匯入來源,來檢查較多的目錄");
        }
        return compareStyleComboBox;
    }

    private JButton getResetQueryBtn() {
        if (resetQueryBtn == null) {
            resetQueryBtn = new JButton();
            resetQueryBtn.setText("重設重尋條件");
            resetQueryBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    DefaultTableModel dirCompareModel = getDefaultTableModel();
                    for (Object[] rows : modelOrignList) {
                        dirCompareModel.addRow(rows);
                    }
                    while (dirCompareTable.getModel().getRowCount() != dirCompareModel.getRowCount()) {
                        dirCompareTable.setModel(dirCompareModel);
                        System.out.println("reset dirCompareTable");
                    }
                    initComponents();
                }
            });
        }
        return resetQueryBtn;
    }

    private JLabel getJLabel2() {
        if (jLabel2 == null) {
            jLabel2 = new JLabel();
            jLabel2.setText("\u689d\u4ef6\u5f0f");
        }
        return jLabel2;
    }

    private JButton getResetBtn() {
        if (resetBtn == null) {
            resetBtn = new JButton();
            resetBtn.setText("\u91cd\u8a2d");
            resetBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    leftDirText.setText("");
                    rightDirText.setText("");
                    searchText.setText("");
                    initComponents();
                }
            });
        }
        return resetBtn;
    }

    enum SearchTextEnum {
        name("檔名") {
            @Override
            boolean isMatch(InfoObj infoObj, String operator, String condition) {
                String name = infoObj.mainFile.getName();
                if (StringUtils.equals(operator, ">")) {
                } else if (StringUtils.equals(operator, ">=")) {
                } else if (StringUtils.equals(operator, "<")) {
                } else if (StringUtils.equals(operator, "<=")) {
                } else if (StringUtils.equals(operator, "!=")) {
                    return !StringUtils.equalsIgnoreCase(name, condition);
                } else if (StringUtils.equals(operator, "=")) {
                    return StringUtils.equalsIgnoreCase(name, condition);
                } else if (StringUtils.equals(operator, "^=")) {
                    return StringUtils.startsWith(name.toLowerCase(), condition.toLowerCase());
                } else if (StringUtils.equals(operator, "$=")) {
                    return StringUtils.endsWith(name.toLowerCase(), condition.toLowerCase());
                } else if (StringUtils.equals(operator, "*=")) {
                    return StringUtils.contains(name.toLowerCase(), condition.toLowerCase());
                }
                throw new RuntimeException(this + "不支援此運算 : " + operator);
            }
        },
        path("檔案全路徑") {
            @Override
            boolean isMatch(InfoObj infoObj, String operator, String condition) {
                List<String> pathList = new ArrayList<String>();
                try {
                    pathList.add(infoObj.mainFile.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    pathList.add(infoObj.compareToFile.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (String name : pathList) {
                    if (StringUtils.equals(operator, ">")) {
                    } else if (StringUtils.equals(operator, ">=")) {
                    } else if (StringUtils.equals(operator, "<")) {
                    } else if (StringUtils.equals(operator, "<=")) {
                    } else if (StringUtils.equals(operator, "!=")) {
                        return !StringUtils.equalsIgnoreCase(name, condition);
                    } else if (StringUtils.equals(operator, "=")) {
                        return StringUtils.equalsIgnoreCase(name, condition);
                    } else if (StringUtils.equals(operator, "^=")) {
                        return StringUtils.startsWith(name.toLowerCase(), condition.toLowerCase());
                    } else if (StringUtils.equals(operator, "$=")) {
                        return StringUtils.endsWith(name.toLowerCase(), condition.toLowerCase());
                    } else if (StringUtils.equals(operator, "*=")) {
                        return StringUtils.contains(name.toLowerCase(), condition.toLowerCase());
                    }
                }
                throw new RuntimeException(this + "不支援此運算 : " + operator);
            }
        },
        date("檔案日期") {
            @Override
            boolean isMatch(InfoObj infoObj, String operator, String condition) {
                Validate.isTrue(StringUtils.isNumeric(condition), "日期格視為yyyyMMddHHmmss(最少要輸入年) : " + condition);
                int length = condition.length();
                SimpleDateFormat sdf = new SimpleDateFormat();
                switch (length) {
                case 4:
                    sdf.applyPattern("yyyy");
                    break;
                case 6:
                    sdf.applyPattern("yyyyMM");
                    break;
                case 8:
                    sdf.applyPattern("yyyyMMdd");
                    break;
                case 10:
                    sdf.applyPattern("yyyyMMddHH");
                    break;
                case 12:
                    sdf.applyPattern("yyyyMMddHHmm");
                    break;
                case 14:
                    sdf.applyPattern("yyyyMMddHHmmss");
                    break;
                }
                Date conditionDateTime = null;
                try {
                    conditionDateTime = sdf.parse(condition);
                } catch (ParseException e) {
                    throw new RuntimeException("無法解析的條件 : " + condition);
                }
                List<Date> dateList = new ArrayList<Date>();
                if (infoObj.mainFile.exists()) {
                    Date tempDate = new Date(infoObj.mainFile.lastModified());
                    try {
                        dateList.add(sdf.parse(sdf.format(tempDate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (infoObj.compareToFile.exists()) {
                    Date tempDate = new Date(infoObj.compareToFile.lastModified());
                    try {
                        dateList.add(sdf.parse(sdf.format(tempDate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                for (Date fileDate : dateList) {
                    if (StringUtils.equals(operator, ">")) {
                        return fileDate.after(conditionDateTime);
                    } else if (StringUtils.equals(operator, ">=")) {
                        return fileDate.after(conditionDateTime) || conditionDateTime.equals(fileDate);
                    } else if (StringUtils.equals(operator, "<")) {
                        return conditionDateTime.after(fileDate);
                    } else if (StringUtils.equals(operator, "<=")) {
                        return conditionDateTime.after(fileDate) || conditionDateTime.equals(fileDate);
                    } else if (StringUtils.equals(operator, "!=")) {
                        return !conditionDateTime.equals(fileDate);
                    } else if (StringUtils.equals(operator, "=")) {
                        return conditionDateTime.equals(fileDate);
                    } else if (StringUtils.equals(operator, "^=")) {
                    } else if (StringUtils.equals(operator, "$=")) {
                    } else if (StringUtils.equals(operator, "*=")) {
                    }
                }
                throw new RuntimeException(this + "不支援此運算 : " + operator);
            }
        },
        size("檔案大小") {
            @Override
            boolean isMatch(InfoObj infoObj, String operator, String condition) {
                Pattern pattern = Pattern.compile("(\\d+)(kb|mb|k|byte|)");
                Matcher matcher = pattern.matcher(condition);
                if (!matcher.matches()) {
                    throw new RuntimeException("條件式格式不符:" + pattern.pattern() + " ==> " + condition);
                }
                BigDecimal size = BigDecimal.valueOf(Long.parseLong(matcher.group(1)));
                String type = matcher.group(2);
                double divide = 1;
                if (StringUtils.isNotBlank(type)) {
                    if (StringUtils.equals("kb", type) || StringUtils.equals("k", type)) {
                        divide = 1024;
                    } else if (StringUtils.equals("mb", type)) {
                        divide = 1024 * 1024;
                    }
                }
                List<BigDecimal> sizeList = new ArrayList<BigDecimal>();
                if (infoObj.mainFile.exists()) {
                    sizeList.add(fetchFixFileSize(infoObj.mainFile.length(), divide));
                }
                if (infoObj.compareToFile.exists()) {
                    sizeList.add(fetchFixFileSize(infoObj.compareToFile.length(), divide));
                }
                for (BigDecimal fileSize : sizeList) {
                    int resultValue = fileSize.compareTo(size);
                    if (StringUtils.equals(operator, ">")) {
                        return resultValue > 0;
                    } else if (StringUtils.equals(operator, ">=")) {
                        return resultValue >= 0;
                    } else if (StringUtils.equals(operator, "<")) {
                        return resultValue < 0;
                    } else if (StringUtils.equals(operator, "<=")) {
                        return resultValue <= 0;
                    } else if (StringUtils.equals(operator, "!=")) {
                        return resultValue != 0;
                    } else if (StringUtils.equals(operator, "=")) {
                        return resultValue == 0;
                    } else if (StringUtils.equals(operator, "^=")) {
                    } else if (StringUtils.equals(operator, "$=")) {
                    } else if (StringUtils.equals(operator, "*=")) {
                    }
                }
                throw new RuntimeException(this + "不支援此運算 : " + operator);
            }
        };

        String label;

        SearchTextEnum(String label) {
            this.label = label;
        }

        abstract boolean isMatch(InfoObj infoObj, String operator, String condition);

        public String toString() {
            return label;
        }

        private static BigDecimal fetchFixFileSize(long filelength, double divide) {
            if (divide == 1) {
                return BigDecimal.valueOf(filelength);
            }
            BigDecimal result = BigDecimal.valueOf(filelength).divide(new BigDecimal(divide));
            result = result.setScale(2, BigDecimal.ROUND_HALF_UP);
            return result;
        }
    }

    static abstract class FileSearch {
        DirectoryCompareUI this_;

        FileSearch(DirectoryCompareUI this_) {
            this.this_ = this_;
        }

        abstract boolean isMatch(InfoObj infoObj);

        void execute(DirectoryCompareUI ui, String message) {
            long startTime = System.currentTimeMillis();
            final DefaultTableModel model2 = ui.getDefaultTableModel();
            ui.dirCompareTable.setModel(model2);
            int total = 0;
            for (Object[] rows : ui.modelOrignList) {
                InfoObj infoObj = (InfoObj) rows[0];
                if (isMatch(infoObj)) {
                    model2.addRow(rows);
                    total++;
                }
            }
            if (StringUtils.isBlank(message)) {
                message = "搜尋完成";
            }
            startTime = System.currentTimeMillis() - startTime;
            this_.setTitle(message + " 數量 : " + total + ", 耗時 : " + startTime);
        }
    }

    static class InfoObj {
        FileZ mainFile;
        FileZ compareToFile;
        String status;
        DiffMergeStatus status2;

        private String getDate(long modifyTime) {
            return DateFormatUtils.format(modifyTime, "yyyy/MM/dd HH:mm:ss");
        }

        public String getMainFileInfo() {
            if (mainFile.exists()) {
                String sizeDescription = FileUtil.getSizeDescription(mainFile.length());
                return getDate(mainFile.lastModified()) + "(" + sizeDescription + ")";
            }
            return "NA";
        }

        public String getCompareToFileInfo() {
            if (compareToFile.exists()) {
                String sizeDescription = FileUtil.getSizeDescription(compareToFile.length());
                return getDate(compareToFile.lastModified()) + "(" + sizeDescription + ")";
            }
            return "NA";
        }

        @Override
        public String toString() {
            return status;
        }

        public String getMainFileNew() {
            boolean mainFileNew = mainFile.lastModified() > compareToFile.lastModified();
            if (mainFileNew) {
                return "新";
            }
            return "";
        }

        public String getCompareToFileNew() {
            boolean mainFileNew = mainFile.lastModified() > compareToFile.lastModified();
            if (!mainFileNew) {
                return "新";
            }
            return "";
        }
    }

    static abstract class MainCompareSearch {
        File main;
        File compareTo;
        int countNotExists = 0;
        int countSameFile = 0;
        int countDifferentFile = 0;
        long completeTime = 0L;
        List<String> comparePathList = new ArrayList<String>();
        int mainFileCount = 0;
        int compareToFileCount = 0;

        public void fileCountAdd(FileZ mainFile, FileZ compareFile, boolean mainFileIsLeft) {
            if (mainFileIsLeft) {
                if (mainFile.exists() && mainFile.isFile()) {
                    mainFileCount++;
                }
                if (compareFile.exists() && compareFile.isFile()) {
                    compareToFileCount++;
                }
            } else {
                if (mainFile.exists() && mainFile.isFile()) {
                    compareToFileCount++;
                }
                if (compareFile.exists() && compareFile.isFile()) {
                    mainFileCount++;
                }
            }
        }

        MainCompareSearch(File main, File compareTo) {
            this.main = main;
            this.compareTo = compareTo;
        }

        public void executeTwoDirectoryButNotSame(List<File> mainList, List<File> compareToList) {
            long startTime = System.currentTimeMillis();

            Multimap<String, FileZ> mainFileMap = ArrayListMultimap.create();
            Multimap<String, FileZ> compareToFileMap = ArrayListMultimap.create();
            for (File f : mainList) {
                mainFileMap.put(f.getName(), new FileZ(f));
            }
            for (File f : compareToList) {
                compareToFileMap.put(f.getName(), new FileZ(f));
            }

            boolean mainIsLeft = true;
            Multimap<String, FileZ> tempMap = null;
            if (mainList.size() < compareToList.size()) {
                tempMap = mainFileMap;
                mainFileMap = compareToFileMap;
                compareToFileMap = tempMap;
                mainIsLeft = false;
            }

            String mainBasePath = FileUtil.getCanonicalPath(getParentFileIfNotDirectory(main));
            String compareToAbsPath = FileUtil.getCanonicalPath(getParentFileIfNotDirectory(compareTo));
            int compareToAbsLength = compareToAbsPath.length();
            if (!mainIsLeft) {
                mainBasePath = FileUtil.getCanonicalPath(getParentFileIfNotDirectory(compareTo));
                compareToAbsPath = FileUtil.getCanonicalPath(getParentFileIfNotDirectory(main));
                compareToAbsLength = compareToAbsPath.length();
            }

            for (String fileName : compareToFileMap.keySet()) {
                if (mainFileMap.containsKey(fileName)) {
                    for (FileZ compareToFileZ : compareToFileMap.get(fileName)) {
                        for (FileZ mainFileZ : mainFileMap.get(fileName)) {
                            checkTwoFile(mainFileZ, compareToFileZ, mainIsLeft);
                        }
                    }
                } else {
                    for (FileZ compareToFileZ : compareToFileMap.get(fileName)) {
                        System.out.println("cz = " + compareToFileZ.getAbsolutePath());
                        System.out.println("czLen = " + compareToAbsLength);
                        System.out.println("czRoot = " + compareToAbsPath);
                        String suffix = compareToFileZ.getAbsolutePath().substring(compareToAbsLength);
                        FileZ mainFileZ = new FileZ(new File(mainBasePath, suffix));
                        System.out.println("mainFileZ = " + mainFileZ);
                        checkTwoFile(compareToFileZ, mainFileZ, !mainIsLeft);
                    }
                }
            }
            completeTime = System.currentTimeMillis() - startTime;
            if (!mainIsLeft) {
                int tempCount = mainFileCount;
                mainFileCount = compareToFileCount;
                compareToFileCount = tempCount;
            }
            complete();
        }

        private File getParentFileIfNotDirectory(File file) {
            if (!file.isDirectory()) {
                return file.getParentFile();
            }
            return file;
        }

        public void executeTwoFile() {
            long startTime = System.currentTimeMillis();
            checkTwoFile(new FileZ(main), new FileZ(compareTo), true);
            completeTime = System.currentTimeMillis() - startTime;
            complete();
        }

        public void executeSameDirectory() {
            long startTime = System.currentTimeMillis();
            for (File f : main.listFiles()) {
                File tmpFile = new File(compareTo, f.getName());
                checkTwoFile(new FileZ(f), new FileZ(tmpFile), true);
            }
            for (File f : compareTo.listFiles()) {
                File tmpFile = new File(main, f.getName());
                checkTwoFile(new FileZ(f), new FileZ(tmpFile), false);
            }
            completeTime = System.currentTimeMillis() - startTime;
            complete();
        }

        abstract void complete();

        abstract void compareFileNotExists(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft);

        abstract void sameFile(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft);

        abstract void differentFile(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft);

        private boolean validateComareFileCheck(FileZ mainFile, FileZ compareToFile) {
            boolean mainFileExist = comparePathList.contains(mainFile.getAbsolutePath());
            boolean compareToFileExist = comparePathList.contains(compareToFile.getAbsolutePath());
            if (mainFileExist && compareToFileExist) {
                return false;
            }
            if (!mainFileExist) {
                comparePathList.add(mainFile.getAbsolutePath());
            }
            if (!compareToFileExist) {
                comparePathList.add(compareToFile.getAbsolutePath());
            }
            return true;
        }

        private void checkTwoDirectory(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft) {
            for (File f : mainFile.getFile().listFiles()) {
                File tmpFile = new File(compareToFile.getFile(), f.getName());
                checkTwoFile(new FileZ(f), new FileZ(tmpFile), mainFileIsLeft);
            }
        }

        private void checkTwoFile(FileZ mainFile, FileZ compareToFile, boolean mainFileIsLeft) {
            if (!mainFile.exists()) {
                throw new RuntimeException("檔案不存在:" + mainFile);
            }

            if (mainFile.isFile() && !compareToFile.exists()) {
                if (validateComareFileCheck(mainFile, compareToFile)) {
                    countNotExists++;
                    fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                    this.compareFileNotExists(mainFile, compareToFile, mainFileIsLeft);
                    return;
                }
            } else if (mainFile.isDirectory() && !compareToFile.exists()) {
                if (validateComareFileCheck(mainFile, compareToFile)) {
                    fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                    checkTwoDirectory(mainFile, compareToFile, mainFileIsLeft);
                }
                return;
            }
            if (mainFile.isFile() && compareToFile.isFile()) {
                if (mainFile.length() == compareToFile.length()) {
                    if (mainFile.lastModified() != compareToFile.lastModified()) {
                        try {
                            byte[] b1 = FileUtils.readFileToByteArray(mainFile.getFile());
                            byte[] b2 = FileUtils.readFileToByteArray(compareToFile.getFile());
                            if (Arrays.equals(b1, b2)) {
                                if (validateComareFileCheck(mainFile, compareToFile)) {
                                    countSameFile++;
                                    fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                                    this.sameFile(mainFile, compareToFile, mainFileIsLeft);
                                }
                                return;
                            } else {
                                if (validateComareFileCheck(mainFile, compareToFile)) {
                                    countDifferentFile++;
                                    fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                                    this.differentFile(mainFile, compareToFile, mainFileIsLeft);
                                }
                                return;
                            }
                        } catch (IOException e) {
                            if (validateComareFileCheck(mainFile, compareToFile)) {
                                countDifferentFile++;
                                fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                                this.differentFile(mainFile, compareToFile, mainFileIsLeft);
                            }
                            return;
                        }
                    } else {
                        if (validateComareFileCheck(mainFile, compareToFile)) {
                            countSameFile++;
                            fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                            this.sameFile(mainFile, compareToFile, mainFileIsLeft);
                        }
                        return;
                    }
                } else {
                    if (validateComareFileCheck(mainFile, compareToFile)) {
                        countDifferentFile++;
                        fileCountAdd(mainFile, compareToFile, mainFileIsLeft);
                        this.differentFile(mainFile, compareToFile, mainFileIsLeft);
                    }
                    return;
                }
            }
            if (mainFile.isDirectory() && compareToFile.isDirectory()) {
                if (validateComareFileCheck(mainFile, compareToFile)) {
                    checkTwoDirectory(mainFile, compareToFile, mainFileIsLeft);
                }
                return;
            }
        }
    }

    enum DiffMergeStatus {
        NOT_EXISTS("資料不存在"), //
        DIFFERENT("不同"), //
        SAME("相同"),//
        ;
        String label;

        DiffMergeStatus(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    enum DiffMergeCommand {
        CustomCompare("自訂比對") {
            @Override
            String getCommand(DirectoryCompareUI this_, File fileA, File fileB) {
                try {
                    String customCompareCommandURL = "cmd /c call " + this_.customCompareText.getText();
                    String command = String.format(customCompareCommandURL, fileA, fileB);
                    return command;
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                    return "";
                }
            }
        }, //
        TortoiseSVN("小烏龜SVN") {
            @Override
            String getCommand(DirectoryCompareUI this_, File fileA, File fileB) {
                String tortoiseMergeFormat = "cmd /c call TortoiseMerge /base:\"%s\" /theirs:\"%s\"";
                String command = String.format(tortoiseMergeFormat, fileA, fileB);
                return command;
            }
        }, //
        TortoiseGIT("小烏龜GIT") {
            @Override
            String getCommand(DirectoryCompareUI this_, File fileA, File fileB) {
                String tortoiseMergeFormat = "cmd /c call TortoiseGitMerge /base:\"%s\" /theirs:\"%s\"";
                String command = String.format(tortoiseMergeFormat, fileA, fileB);
                return command;
            }
        }, //
        StarTeam("StarTeam") {
            @Override
            String getCommand(DirectoryCompareUI this_, File fileA, File fileB) {
                String command = String.format("\"%s\" -base \"%s\" -result \"%s\"", this_.getStarTeamPath_FileCompareMerge(true), fileA, fileB);
                return command;
            }
        },//
        ;
        String label;

        DiffMergeCommand(String label) {
            this.label = label;
        }

        abstract String getCommand(DirectoryCompareUI this_, File fileA, File fileB);

        public String toString() {
            return label;
        }
    }

    private ExtensionNameCache extensionNameCache;
    private JPanel panel;
    private JLabel lblCustomCommand;
    private JTextField customCompareText;
    private JButton configSaveBtn;

    static class ExtensionNameCache {
        SingletonMap extensionNameMessageMap = new SingletonMap();
        SingletonMap extensionNameMap = new SingletonMap();
    }
}
