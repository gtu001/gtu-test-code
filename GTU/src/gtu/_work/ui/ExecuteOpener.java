package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateFormatUtils;

import gtu.clipboard.ClipboardUtil;
import gtu.collection.ListUtil;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JFileExecuteUtil;
import gtu.swing.util.JFileExecuteUtil_ConfigDlg;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTextFieldUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

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
public class ExecuteOpener extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JButton executeAll;
    private JList execList;
    private JButton contentFilterBtn;
    private JCheckBox useRegexOnly;
    private JComboBox fileOrDirTypeCombo;
    private JScrollPane jScrollPane6;
    private JTextArea scannerText;
    private JButton openSvnUpdate;
    private JTextField innerScannerText;
    private JLabel scannerStatus;
    private JScrollPane jScrollPane5;
    private JList ignoreScanList;
    private JTextField ignoreScanText;
    private JScrollPane jScrollPane4;
    private JList scanList;
    private JButton addListToExecList;
    private JComboBox scanType;
    private JButton fileScan;
    private JTextField scanDirText;
    private JPanel jPanel9;
    private JPanel jPanel8;
    private JScrollPane jScrollPane3;
    private JPanel jPanel7;
    private JButton reloadList;
    private JButton clearExecList;
    private JPanel jPanel6;
    private JButton deleteEmptyDir;
    private JButton loadClipboardPath;
    private JButton deleteSelected;
    private JButton moveFiles;
    private JTextField queryText;
    private JButton exportListHasOrignTree;
    private JButton loadProp;
    private JButton executeExport;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane1;
    private JButton saveList;
    private JButton browser;
    private JButton addArea;
    private JPanel jPanel5;
    private JTextArea exeArea;
    private JPanel jPanel4;
    private JPanel jPanel3;
    private JPanel jPanel2;

    private JFrameRGBColorPanel jFrameRGBColorPanel = null;
    private PropertiesUtilBean config = new PropertiesUtilBean(ExecuteOpener.class);
    private PropertiesUtilBean remarkConfig = new PropertiesUtilBean(ExecuteOpener.class, ExecuteOpener.class.getSimpleName() + "_Remark");
    private HideInSystemTrayHelper hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ExecuteOpener inst = new ExecuteOpener();
                inst.setLocationRelativeTo(null);
                gtu.swing.util.JFrameUtil.setVisible(true, inst);
            }
        });
    }

    static Properties prop = new Properties();
    static File jarPositionDir = PropertiesUtil.getJarCurrentPath(ExecuteOpener.class);
    static {
        System.out.println("jarPositionDir : " + jarPositionDir);
    }

    public ExecuteOpener() {
        super();
        initGUI();
    }

    private void initGUI() {
        final SwingActionUtil swingUtil = SwingActionUtil.newInstance(this);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        try {
            {
                this.setTitle("execute browser");
                this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                this.setPreferredSize(new java.awt.Dimension(870, 551));
                this.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        swingUtil.invokeAction("frame.mouseClicked", evt);
                    }
                });
            }
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1);
                jTabbedPane1.setPreferredSize(new java.awt.Dimension(384, 265));
                jTabbedPane1.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent evt) {
                        swingUtil.invokeAction("jTabbedPane1.stateChanged", evt);
                    }
                });
                jTabbedPane1.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        swingUtil.invokeAction("jTabbedPane1.mouseClicked", evt);
                    }
                });
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("file list", null, jPanel1, null);
                    {
                        jPanel4 = new JPanel();
                        BorderLayout jPanel4Layout = new BorderLayout();
                        jPanel4.setLayout(jPanel4Layout);
                        jPanel1.add(jPanel4, BorderLayout.NORTH);
                        jPanel4.setPreferredSize(new java.awt.Dimension(508, 81));
                        {
                            jScrollPane1 = new JScrollPane();
                            jPanel4.add(jScrollPane1, BorderLayout.CENTER);
                            {
                                exeArea = new JTextArea();
                                jScrollPane1.setViewportView(exeArea);
                            }
                        }
                        {
                            jPanel5 = new JPanel();
                            BorderLayout jPanel5Layout = new BorderLayout();
                            jPanel5.setLayout(jPanel5Layout);
                            jPanel4.add(jPanel5, BorderLayout.EAST);
                            jPanel5.setPreferredSize(new java.awt.Dimension(202, 81));
                            {
                                addArea = new JButton();
                                jPanel5.add(addArea, BorderLayout.CENTER);
                                addArea.setText("addArea");
                                addArea.setPreferredSize(new java.awt.Dimension(58, 30));
                                addArea.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent evt) {
                                        swingUtil.invokeAction("addArea.actionPerformed", evt);
                                    }
                                });
                            }
                        }
                        {
                            queryText = new JTextField();
                            jPanel4.add(queryText, BorderLayout.NORTH);
                            queryText.getDocument().addDocumentListener((DocumentListener) SwingActionUtil.ActionAdapter.DocumentListener.create("queryText.addDocumentListener", swingUtil));
                        }
                    }
                    {
                        jPanel3 = new JPanel();
                        jPanel1.add(jPanel3, BorderLayout.CENTER);
                        BorderLayout jPanel3Layout = new BorderLayout();
                        jPanel3.setLayout(jPanel3Layout);
                        jPanel3.setPreferredSize(new java.awt.Dimension(480, 220));
                        {
                            jScrollPane2 = new JScrollPane();
                            jPanel3.add(jScrollPane2, BorderLayout.CENTER);
                            {
                                DefaultListModel execListModel = new DefaultListModel();
                                for (Object obj : prop.keySet()) {
                                    execListModel.addElement((String) obj);
                                }
                                execList = new JList();
                                jScrollPane2.setViewportView(execList);
                                execList.setModel(execListModel);
                                execList.addKeyListener(new KeyAdapter() {
                                    public void keyPressed(KeyEvent evt) {
                                        swingUtil.invokeAction("execList.keyPressed", evt);
                                    }
                                });
                                execList.addListSelectionListener(new ListSelectionListener() {
                                    public void valueChanged(ListSelectionEvent evt) {
                                        swingUtil.invokeAction("execList.valueChanged", evt);
                                    }
                                });
                                execList.addMouseListener(new MouseAdapter() {
                                    public void mouseClicked(MouseEvent evt) {
                                        swingUtil.invokeAction("execList.mouseClicked", evt);
                                    }
                                });
                            }
                        }
                    }
                    {
                        jPanel6 = new JPanel();
                        jPanel1.add(jPanel6, BorderLayout.SOUTH);
                        jPanel6.setPreferredSize(new java.awt.Dimension(741, 47));
                        {
                            saveList = new JButton();
                            jPanel6.add(saveList);
                            saveList.setText("save list to default properties");
                            saveList.setPreferredSize(new java.awt.Dimension(227, 28));
                            saveList.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("saveList.actionPerformed", evt);
                                }
                            });
                        }
                        {
                            clearExecList = new JButton();
                            jPanel6.add(clearExecList);
                            clearExecList.setText("clear properties");
                            clearExecList.setPreferredSize(new java.awt.Dimension(170, 28));
                            clearExecList.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("clearExecList.actionPerformed", evt);
                                }
                            });
                        }
                        {
                            reloadList = new JButton();
                            jPanel6.add(reloadList);
                            reloadList.setText("reload list");
                            reloadList.setPreferredSize(new java.awt.Dimension(156, 28));
                            reloadList.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("reloadList.actionPerformed", evt);
                                }
                            });
                        }
                        {
                            contentFilterBtn = new JButton();
                            jPanel6.add(contentFilterBtn);
                            contentFilterBtn.setText("content filter");
                            contentFilterBtn.setPreferredSize(new java.awt.Dimension(176, 27));
                            contentFilterBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("contentFilterBtn.actionPerformed", evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    FlowLayout jPanel2Layout = new FlowLayout();
                    jTabbedPane1.addTab("config", null, jPanel2, null);
                    jPanel2.setPreferredSize(new java.awt.Dimension(573, 300));
                    jPanel2.setLayout(jPanel2Layout);
                    {
                        executeAll = new JButton();
                        jPanel2.add(executeAll);
                        executeAll.setText("execute all files");
                        executeAll.setPreferredSize(new java.awt.Dimension(137, 27));
                        executeAll.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("execute.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        browser = new JButton();
                        jPanel2.add(browser);
                        browser.setText("add file");
                        browser.setPreferredSize(new java.awt.Dimension(140, 28));
                        browser.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("browser.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        loadProp = new JButton();
                        jPanel2.add(loadProp);
                        loadProp.setText("load properties");
                        loadProp.setPreferredSize(new java.awt.Dimension(158, 32));
                        loadProp.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("loadProp.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        executeExport = new JButton();
                        jPanel2.add(executeExport);
                        executeExport.setText("export list");
                        executeExport.setPreferredSize(new java.awt.Dimension(145, 31));
                        executeExport.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("executeExport.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        exportListHasOrignTree = new JButton();
                        jPanel2.add(exportListHasOrignTree);
                        exportListHasOrignTree.setText("export list has orign tree");
                        exportListHasOrignTree.setPreferredSize(new java.awt.Dimension(204, 32));
                        exportListHasOrignTree.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("exportListHasOrignTree.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        moveFiles = new JButton();
                        jPanel2.add(moveFiles);
                        moveFiles.setText("move selected");
                        moveFiles.setPreferredSize(new java.awt.Dimension(161, 31));
                        moveFiles.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("moveFiles.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        deleteSelected = new JButton();
                        jPanel2.add(deleteSelected);
                        deleteSelected.setText("delete selected");
                        deleteSelected.setPreferredSize(new java.awt.Dimension(165, 31));
                        deleteSelected.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("deleteSelected.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        loadClipboardPath = new JButton();
                        jPanel2.add(loadClipboardPath);
                        loadClipboardPath.setText("load clipboard path");
                        loadClipboardPath.setPreferredSize(new java.awt.Dimension(222, 31));
                        loadClipboardPath.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("loadClipboardPath.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        deleteEmptyDir = new JButton();
                        jPanel2.add(deleteEmptyDir);
                        deleteEmptyDir.setText("delete empty dir");
                        deleteEmptyDir.setPreferredSize(new java.awt.Dimension(222, 31));
                        deleteEmptyDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("deleteEmptyDir.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        openSvnUpdate = new JButton();
                        jPanel2.add(openSvnUpdate);
                        openSvnUpdate.setText("list svn new or modify file");
                        openSvnUpdate.setPreferredSize(new java.awt.Dimension(210, 34));
                        {
                            restoreBackBtn = new JButton("備份回復");
                            restoreBackBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    swingUtil.invokeAction("restoreBackBtn.actionPerformed", e);
                                }
                            });
                            jPanel2.add(restoreBackBtn);
                        }
                        openSvnUpdate.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("openSvnUpdate.actionPerformed", evt);
                            }
                        });
                    }
                }
                {
                    jPanel7 = new JPanel();
                    BorderLayout jPanel7Layout = new BorderLayout();
                    jPanel7.setLayout(jPanel7Layout);
                    jTabbedPane1.addTab("properties", null, jPanel7, null);
                    {
                        {
                            panel_4 = new JPanel();
                            jPanel7.add(panel_4, BorderLayout.NORTH);
                            {
                                lblFilter = new JLabel("filter");
                                panel_4.add(lblFilter);
                            }
                            {
                                propertiesListFilterText = new JTextField();
                                propertiesListFilterText.addFocusListener(new FocusAdapter() {
                                    @Override
                                    public void focusLost(FocusEvent e) {
                                        swingUtil.invokeAction("propertiesListFilterText.focusLost", e);
                                    }
                                });
                                panel_4.add(propertiesListFilterText);
                                propertiesListFilterText.setColumns(30);
                            }
                        }
                        {
                            panel_5 = new JPanel();
                            jPanel7.add(panel_5, BorderLayout.WEST);
                        }
                        {
                            panel_6 = new JPanel();
                            jPanel7.add(panel_6, BorderLayout.EAST);
                        }
                        {
                            panel_7 = new JPanel();
                            jPanel7.add(panel_7, BorderLayout.SOUTH);
                        }
                        {
                            propertiesList = new JList();
                            JScrollPane scroll = JCommonUtil.createScrollComponent(propertiesList);
                            jPanel7.add(scroll, BorderLayout.CENTER);
                            reloadCurrentDirPropertiesList();
                            propertiesList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    swingUtil.invokeAction("propertiesList.keyPressed", evt);
                                }
                            });
                            propertiesList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("propertiesList.mouseClicked", evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel8 = new JPanel();
                    BorderLayout jPanel8Layout = new BorderLayout();
                    jTabbedPane1.addTab("scanner", null, jPanel8, null);
                    jPanel8.setLayout(jPanel8Layout);
                    {
                        jPanel9 = new JPanel();
                        jPanel8.add(jPanel9, BorderLayout.NORTH);
                        jPanel9.setPreferredSize(new java.awt.Dimension(741, 187));
                        {
                            scanDirText = new JTextField();
                            JTextFieldUtil.setupDragDropFilePath(scanDirText, null);
                            scanDirText.setToolTipText("scan dir");
                            jPanel9.add(scanDirText);
                            scanDirText.setPreferredSize(new java.awt.Dimension(225, 24));
                            scanDirText.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("scanDirText.mouseClicked", evt);
                                }
                            });
                        }
                        {
                            jScrollPane6 = new JScrollPane();
                            jPanel9.add(jScrollPane6);
                            jScrollPane6.setPreferredSize(new java.awt.Dimension(168, 69));
                            {
                                scannerText = new JTextArea();
                                scannerText.setToolTipText("query condition");
                                jScrollPane6.setViewportView(scannerText);
                            }
                        }
                        {
                            fileScan = new JButton();
                            jPanel9.add(fileScan);
                            fileScan.setText("start / stop");
                            fileScan.setPreferredSize(new java.awt.Dimension(113, 24));
                            fileScan.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("fileScan.actionPerformed", evt);
                                }
                            });
                        }
                        {
                            useRegexOnly = new JCheckBox();
                            jPanel9.add(useRegexOnly);
                            useRegexOnly.setText("regex only");
                        }
                        {
                            DefaultComboBoxModel scanTypeModel = new DefaultComboBoxModel();
                            for (ScanType s : ScanType.values()) {
                                scanTypeModel.addElement(s);
                            }
                            scanType = new JComboBox();
                            scanType.setToolTipText("scan type");
                            jPanel9.add(scanType);
                            scanType.setModel(scanTypeModel);
                            scanType.setPreferredSize(new java.awt.Dimension(147, 24));
                        }
                        {
                            DefaultComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
                            for (FileOrDirType f : FileOrDirType.values()) {
                                jComboBox1Model.addElement(f);
                            }
                            fileOrDirTypeCombo = new JComboBox();
                            jPanel9.add(fileOrDirTypeCombo);
                            fileOrDirTypeCombo.setModel(jComboBox1Model);
                            fileOrDirTypeCombo.setToolTipText("scan file or directory!");
                        }
                        {
                            addListToExecList = new JButton();
                            jPanel9.add(addListToExecList);
                            addListToExecList.setText("add list to file list");
                            addListToExecList.setPreferredSize(new java.awt.Dimension(158, 24));
                            addListToExecList.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("addListToExecList.actionPerformed", evt);
                                }
                            });
                        }
                        {
                            ignoreScanText = new JTextField();
                            ignoreScanText.setToolTipText("ignore scan condition");
                            ignoreScanText.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                                        return;
                                    }
                                    String ignore = null;
                                    if (StringUtils.isBlank(ignore = ignoreScanText.getText())) {
                                        return;
                                    }
                                    DefaultListModel model = (DefaultListModel) ignoreScanList.getModel();
                                    model.addElement(ignore);
                                }
                            });
                            jPanel9.add(ignoreScanText);
                            ignoreScanText.setPreferredSize(new java.awt.Dimension(153, 24));
                        }
                        {
                            jScrollPane5 = new JScrollPane();
                            jPanel9.add(jScrollPane5);
                            jScrollPane5.setPreferredSize(new java.awt.Dimension(125, 73));
                            jScrollPane5.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                            jScrollPane5.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                            {
                                DefaultListModel ignoreScanListModel = new DefaultListModel();
                                ignoreScanList = new JList();
                                ignoreScanList.setToolTipText("ignore scan condition list");
                                jScrollPane5.setViewportView(ignoreScanList);
                                ignoreScanList.setModel(ignoreScanListModel);
                                ignoreScanList.setPreferredSize(new java.awt.Dimension(125, 73));
                                ignoreScanList.addKeyListener(new KeyAdapter() {
                                    public void keyPressed(KeyEvent evt) {
                                        JListUtil.newInstance(ignoreScanList).defaultJListKeyPressed(evt);
                                    }
                                });
                            }
                        }
                        {
                            innerScannerText = new JTextField();
                            innerScannerText.setToolTipText("檔名再過濾");
                            jPanel9.add(innerScannerText);
                            innerScannerText.setPreferredSize(new java.awt.Dimension(164, 24));
                            {
                                scanLstShowDetailChk = new JCheckBox("顯示修改時間");
                                scanLstShowDetailChk.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        ZFile.is_show_detail = scanLstShowDetailChk.isSelected();
                                        scanList.updateUI();
                                        execList.updateUI();
                                    }
                                });
                                {
                                    innerContentFilterText = new JTextField();
                                    innerContentFilterText.setToolTipText("內文查詢!");
                                    innerContentFilterText.addMouseListener(new MouseAdapter() {
                                        @Override
                                        public void mouseClicked(MouseEvent e) {
                                            swingUtil.invokeAction("innerContentFilterText.addMouseListener", e);
                                        }
                                    });
                                    jPanel9.add(innerContentFilterText);
                                    innerContentFilterText.setColumns(10);
                                }
                                jPanel9.add(scanLstShowDetailChk);
                            }
                            innerScannerText.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("innerScannerText.addMouseListener", evt);
                                }
                            });
                        }
                    }
                    {
                        jScrollPane4 = new JScrollPane();
                        jPanel8.add(jScrollPane4, BorderLayout.CENTER);
                        {
                            DefaultListModel scanListModel = new DefaultListModel();
                            scanList = new JList();
                            jScrollPane4.setViewportView(scanList);
                            scanList.setModel(scanListModel);
                            scanList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("scanList.mouseClicked", evt);
                                }
                            });
                            scanList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    swingUtil.invokeAction("scanList.keyPressed", evt);
                                }
                            });
                        }
                    }
                    {
                        scannerStatus = new JLabel();
                        jPanel8.add(scannerStatus, BorderLayout.SOUTH);
                        scannerStatus.setPreferredSize(new java.awt.Dimension(741, 27));
                    }

                    {
                        button = new JButton("儲存設定");
                        jPanel2.add(button);
                    }
                    {
                        setExeConfigBtn = new JButton("設定exe路徑");
                        setExeConfigBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                swingUtil.invokeAction("setExeConfigBtn.mouseClick", e);
                            }
                        });
                        jPanel2.add(setExeConfigBtn);
                    }
                    {
                        panel_8 = new JPanel();
                        jTabbedPane1.addTab("設定", null, panel_8, null);
                        panel_8.setLayout(new FormLayout(
                                new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));
                        {
                            panel_9 = new JPanel();
                            panel_8.add(panel_9, "4, 38, fill, fill");
                        }
                    }
                    JCommonUtil.setJFrameIcon(this, "resource/images/ico/gtu001.ico");
                    jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
                    hideInSystemTrayHelper.apply(this);

                    jPanel2.add(jFrameRGBColorPanel.getToggleButton(false));
                    jPanel2.add(hideInSystemTrayHelper.getToggleButton(false));
                }
            }

            swingUtil.addAction("moveFiles.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) execList.getModel();
                    if (model.getSize() == 0 || execList.getSelectedValues().length == 0) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("no selected file can move!", "ERROR");
                        return;
                    }
                    File exportListTo = FileUtil.getDefaultExportDir(ExecuteOpener.class, true);
                    File file = null;
                    List<File> list = new ArrayList<File>();
                    for (Object obj : execList.getSelectedValues()) {
                        file = new File((String) obj);
                        if (file.exists() && file.isFile()) {
                            list.add(file);
                        }
                    }
                    File fromBaseDir = FileUtil.exportReceiveBaseDir(list);
                    System.out.println("fromBaseDir = " + fromBaseDir);
                    int cutLen = 0;
                    if (fromBaseDir != null) {
                        cutLen = fromBaseDir.getAbsolutePath().length();
                    }
                    StringBuilder err = new StringBuilder();
                    File newFile = null;
                    for (Object obj : execList.getSelectedValues()) {
                        file = new File((String) obj);
                        if (file.exists() && file.isFile()) {
                            newFile = new File(exportListTo + "/" + file.getParent().substring(cutLen), file.getName());
                            newFile.getParentFile().mkdirs();
                            System.out.println("move to : " + newFile);
                            file.renameTo(newFile);
                            if (!newFile.exists()) {
                                err.append(file + "\n");
                            }
                        }
                    }
                    if (err.length() > 0) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("move file error : \n" + err, "ERROR");
                    } else {
                        JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("move file success : " + execList.getSelectedValues().length, "SUCCESS");
                    }
                }
            });
            swingUtil.addAction("deleteSelected.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    StringBuilder sb = new StringBuilder();
                    for (Object obj : execList.getSelectedValues()) {
                        sb.append(new File((String) obj).getName() + "\n");
                    }
                    if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().confirmButtonYesNo().iconWaringMessage()
                            .showConfirmDialog("are you sure delete file : \n" + sb, "DELETE")) {
                        return;
                    }
                    File file = null;
                    sb = new StringBuilder();
                    for (Object obj : execList.getSelectedValues()) {
                        file = new File((String) obj);
                        if (!file.exists()) {
                            continue;
                        }
                        if (file.isDirectory() && file.list().length == 0) {
                            if (!file.delete()) {
                                sb.append(file.getName() + "\n");
                            }
                            continue;
                        }
                        if (!file.delete()) {
                            sb.append(file.getName() + "\n");
                        }
                    }
                    if (sb.length() != 0) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("delete error list :\n" + sb, "ERROR");
                    } else {
                        JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog("delete completed!", "SUCCESS");
                    }
                }
            });
            swingUtil.addAction("loadClipboardPath.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File file = new File(ClipboardUtil.getInstance().getContents());
                    if (!file.exists()) {
                        return;
                    }
                    List<File> list = new ArrayList<File>();
                    FileUtil.searchFileMatchs(file, ".*", list);
                    prop.clear();
                    for (File f : list) {
                        if (f.isFile()) {
                            prop.setProperty(f.getAbsolutePath(), "");
                        }
                    }
                    DefaultListModel model = new DefaultListModel();
                    for (Object key : prop.keySet()) {
                        model.addElement(key);
                    }
                    execList.setModel(model);
                }
            });
            swingUtil.addAction("deleteEmptyDir.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                    if (file == null) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                        return;
                    }
                    if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconWaringMessage().confirmButtonYesNo()
                            .showConfirmDialog("are you sure delete empty dir in \n" + file, "WARRNING")) {
                        return;
                    }
                    List<File> delDir = new ArrayList<File>();
                    FileUtil.deleteEmptyDir(file, delDir);
                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("delete dir list : \n" + delDir.toString().replace(',', '\n'), "DELETE");
                }
            });
            swingUtil.addAction("browser.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File file = JFileChooserUtil.newInstance().selectFileAndDirectory().showOpenDialog().getApproveSelectedFile();
                    if (file != null) {
                        DefaultListModel model = (DefaultListModel) execList.getModel();
                        model.addElement(file.getAbsolutePath());
                    }
                }
            });
            swingUtil.addAction("addArea.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (StringUtils.isBlank(exeArea.getText())) {
                        return;
                    }
                    DefaultListModel model = (DefaultListModel) execList.getModel();
                    StringTokenizer token = new StringTokenizer(exeArea.getText(), "\t\n\r\f");
                    while (token.hasMoreElements()) {
                        String val = ((String) token.nextElement()).trim();
                        model.addElement(new ZFile(val));
                        prop.put(val, "");
                    }
                    exeArea.setText("");
                }
            });
            swingUtil.addAction("execute.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) execList.getModel();
                    for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                        String val = (String) enu.nextElement();
                        exec(val);
                    }
                }
            });
            swingUtil.addAction("execList.keyPressed", new Action() {
                public void action(EventObject evt) throws Exception {
                    JListUtil.newInstance(execList).defaultJListKeyPressed(evt);
                }
            });
            // DEFAULT PROP SAVE
            swingUtil.addAction("saveList.actionPerformed", new Action() {

                private String getDefaultName() {
                    String title = getTitle();
                    Pattern ptn = Pattern.compile("properties\\s*\\:\\s*" + ExecuteOpener.class.getSimpleName() + "\\_(.*)\\.properties");
                    Matcher mth = ptn.matcher(title);
                    if (mth.find()) {
                        return mth.group(1);
                    }
                    return "";
                }

                public void action(EventObject evt) throws Exception {
                    String orignName = (String) JOptionPaneUtil.newInstance().iconPlainMessage().showInputDialog("input properties file name", "SAVE", getDefaultName());
                    File saveFile = null;
                    if (StringUtils.isNotBlank(orignName)) {
                        String fileName = orignName;
                        if (!fileName.toLowerCase().endsWith(".properties")) {
                            fileName += ".properties";
                        }
                        fileName = ExecuteOpener.class.getSimpleName() + "_" + fileName;
                        prop.clear();
                        DefaultListModel model = (DefaultListModel) execList.getModel();
                        for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                            ZFile ff = (ZFile) enu.nextElement();
                            prop.put(ff.getAbsolutePath(), "");
                        }
                        saveFile = new File(jarPositionDir, fileName);
                    } else {
                        JCommonUtil._jOptionPane_showMessageDialog_error("未輸入檔名!");
                        return;
                    }
                    PropertiesUtil.storeProperties(prop, saveFile, orignName);
                    setTitle("properties : " + saveFile.getName());
                    JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog(saveFile, "PROPERTIES CREATE");
                }
            });
            swingUtil.addAction("clearExecList.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    // prop.clear();
                    // reloadExecListProperties(prop);
                    execList.setModel(new DefaultListModel());
                }
            });
            swingUtil.addAction("execList.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    // right button single click event
                    jlistMouseRightClickEvent(execList, false, evt);

                    // left button double click event
                    int pos = execList.getLeadSelectionIndex();
                    if (pos == -1) {
                        return;
                    }
                    if (((MouseEvent) evt).getClickCount() < 2) {
                        return;
                    }
                    DefaultListModel model = (DefaultListModel) execList.getModel();
                    ZFile val = (ZFile) model.getElementAt(pos);
                    exec(val.getAbsolutePath());
                }
            });
            swingUtil.addAction("loadProp.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File file = JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog().getApproveSelectedFile();
                    if (file == null) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file not correct!", "ERROR");
                        return;
                    }
                    reloadExecListProperties(file);
                    setTitle("load prop : " + file.getName());
                }
            });
            swingUtil.addAction("reloadList.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    reloadExecListProperties(prop);
                }
            });
            swingUtil.addAction("exportListHasOrignTree.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) execList.getModel();
                    if (model.isEmpty()) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("no file can export!", "ERROR");
                        return;
                    }
                    List<File> allList = new ArrayList<File>();
                    for (int ii = 0; ii < model.getSize(); ii++) {
                        allList.add(new File((String) model.getElementAt(ii)));
                    }
                    File baseDir = FileUtil.exportReceiveBaseDir(allList);
                    System.out.println("common base dir : " + baseDir);
                    boolean dynamicBaseDir = baseDir == null;

                    File tmp = null;
                    File copyTo = null;
                    int realCopyCount = 0;

                    File exportListTo = FileUtil.getDefaultExportDir(ExecuteOpener.class, true);
                    for (int ii = 0; ii < model.getSize(); ii++) {
                        String val = (String) model.getElementAt(ii);
                        if (StringUtils.isBlank(val)) {
                            continue;
                        }
                        tmp = new File(val);
                        if (tmp.isDirectory()) {
                            continue;
                        }
                        File copyFrom = getCorrectFile(tmp);
                        if (dynamicBaseDir) {
                            baseDir = FileUtil.getRoot(copyFrom);
                        }
                        copyTo = FileUtil.exportFileToTargetPath(copyFrom, baseDir, exportListTo);
                        if (!copyTo.getParentFile().exists()) {
                            copyTo.getParentFile().mkdirs();
                        }
                        System.out.println("## file : " + tmp + " -- > " + copyFrom);
                        System.out.println("\t copy to : " + copyTo);
                        FileUtil.copyFile(copyFrom, copyTo);
                        realCopyCount++;
                    }
                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("copy completed!\ntotal : " + model.getSize() + "\ncopy : " + realCopyCount, "SUCCESS");
                }
            });
            swingUtil.addAction("executeExport.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) execList.getModel();
                    if (model.isEmpty()) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("no file can export!", "ERROR");
                        return;
                    }
                    File tmp = null;
                    File copyTo = null;
                    int realCopyCount = 0;
                    File exportListTo = FileUtil.getDefaultExportDir(ExecuteOpener.class, true);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportListTo + "/output_log.txt"), "BIG5"));
                    for (int ii = 0; ii < model.getSize(); ii++) {
                        String val = (String) model.getElementAt(ii);
                        if (StringUtils.isBlank(val)) {
                            continue;
                        }
                        tmp = new File(val);
                        if (tmp.isDirectory()) {
                            continue;
                        }
                        if (isNeedToCopy(exportListTo, tmp)) {
                            File copyFrom = getCorrectFile(tmp);
                            System.out.println("## file : " + tmp + " -- > " + copyFrom);
                            copyTo = new File(exportListTo, copyFrom.getName());
                            for (int jj = 0; copyTo.exists(); jj++) {
                                String name = copyFrom.getName();
                                int pos = name.lastIndexOf(".");
                                String prefix = name.substring(0, pos);
                                String rearfix = name.substring(pos);
                                copyTo = new File(exportListTo, prefix + "_R" + jj + rearfix);
                            }
                            FileUtil.copyFile(copyFrom, copyTo);
                            writer.write(tmp.getAbsolutePath() + (!tmp.getName().equals(copyTo.getName()) ? "\t [rename] : " + copyTo.getName() : ""));
                            realCopyCount++;
                        } else {
                            writer.write(tmp.getAbsolutePath() + "\t [has same file, ommit!]");
                        }
                        writer.newLine();
                    }
                    writer.flush();
                    writer.close();
                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("copy completed!\ntotal : " + model.getSize() + "\ncopy : " + realCopyCount, "SUCCESS");
                }
            });
            swingUtil.addAction("jTabbedPane1.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                        return;
                    }
                    File file = new File(getTitle());
                    if (file.exists()) {
                        JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog(file, "current properties");
                    }
                    ClipboardUtil.getInstance().setContents(file);
                }
            });
            swingUtil.addAction("propertiesList.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    final File file = (File) propertiesList.getSelectedValue();
                    if (file == null) {
                        return;
                    }

                    if (remarkConfig.getConfigProp().containsKey(file.getName())) {
                        String remark = remarkConfig.getConfigProp().getProperty(file.getName());
                        propertiesList.setToolTipText(remark);
                    } else {
                        propertiesList.setToolTipText("");
                    }

                    if (JMouseEventUtil.buttonRightClick(1, evt)) {
                        JPopupMenuUtil.newInstance(propertiesList).applyEvent(evt)//
                                .addJMenuItem("reload list", new ActionListener() {
                                    public void actionPerformed(ActionEvent paramActionEvent) {
                                        reloadCurrentDirPropertiesList();
                                    }
                                })//
                                .addJMenuItem(JFileExecuteUtil.newInstance(file).createDefaultJMenuItems())//
                                .addJMenuItem("編輯註解", new ActionListener() {
                                    public void actionPerformed(ActionEvent paramActionEvent) {
                                        ExecuteOpener_RemarkDlg.newInstance(file.getName(), remarkConfig);
                                    }
                                }).show();
                    }

                    if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                        prop.clear();
                        PropertiesUtil.loadProperties(new FileInputStream(file), prop);
                        currentPropFile = file;
                        reloadExecListProperties(prop);
                        setTitle("properties : " + file.getName());
                    }
                }
            });
            swingUtil.addAction("propertiesList.keyPressed", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                        return;
                    }
                    JListUtil.newInstance(propertiesList).defaultJListKeyPressed(evt);
                }
            });
            swingUtil.addAction("jTabbedPane1.stateChanged", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (jTabbedPane1.getSelectedIndex() == 2) {
                        reloadCurrentDirPropertiesList();
                    }
                }
            });
            swingUtil.addAction("scanList.keyPressed", new Action() {
                public void action(EventObject evt) throws Exception {
                    JListUtil.newInstance(scanList).defaultJListKeyPressed(evt);
                }
            });
            swingUtil.addAction("scanList.mouseClicked", new Action() {

                public void action(EventObject evt) throws Exception {
                    System.out.println("index = " + scanList.getLeadSelectionIndex());
                    jlistMouseRightClickEvent(scanList, true, evt);
                }
            });
            swingUtil.addAction("fileScan.actionPerformed", new Action() {

                Thread scanMainThread = null;

                public void action(EventObject evt) throws Exception {
                    String scanText_ = scannerText.getText();
                    final boolean anyFileMatch = StringUtils.isEmpty(scanText_);
                    final String scanText = anyFileMatch ? UUID.randomUUID().toString() : scanText_;
                    final FileOrDirType fileOrDirType = (FileOrDirType) fileOrDirTypeCombo.getSelectedItem();

                    String scanDir_ = scanDirText.getText();
                    final File scanDir = new File(scanDir_);
                    if (StringUtils.isEmpty(scanDir_)) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("scan dir text can't empty!", "ERROR");
                        return;
                    }
                    if (!scanDir.exists()) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("directory is't exists!", "ERROR");
                        return;
                    }

                    Object[] igArry_ = ((DefaultListModel) ignoreScanList.getModel()).toArray();
                    final String[] igArry = new String[igArry_.length];
                    for (int ii = 0; ii < igArry.length; ii++) {
                        igArry[ii] = (String) igArry_[ii];
                    }
                    final boolean ignoreCheck = igArry.length > 0;

                    final DefaultListModel model = new DefaultListModel();

                    final StringTokenizer tok = new StringTokenizer(scanText);

                    if (scanMainThread == null || scanMainThread.getState() == Thread.State.TERMINATED) {
                        scanMainThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {

                            ScanType scanTp;

                            public void run() {
                                currentScannerThreadStop = false;
                                final long startTime = System.currentTimeMillis();
                                scanTp = (ScanType) scanType.getSelectedItem();

                                List<Thread> threadList = new ArrayList<Thread>();
                                final Map<String, Integer> matchCountMap = new HashMap<String, Integer>();
                                for (; tok.hasMoreElements();) {
                                    final String scanVal = (String) tok.nextElement();
                                    System.out.println("add scan condition = " + scanVal);

                                    Pattern ppp = null;
                                    try {
                                        ppp = Pattern.compile(scanVal);
                                    } catch (Exception ex) {
                                        System.out.println(ex);
                                    }

                                    final Pattern scanTextPattern = ppp;

                                    Thread currentScannerThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {

                                        int matchCount = 0;

                                        void addElement(File file) {
                                            if (scanTp.filter(anyFileMatch, scanVal, scanTextPattern, file, ignoreCheck, igArry)) {
                                                for (int ii = 0;; ii++) {
                                                    try {
                                                        // xxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                        // new modify Troy
                                                        // 20181026
                                                        ZFile newFile = new ZFile(file.getParentFile(), file.getName());
                                                        model.addElement(newFile);
                                                        matchCount++;
                                                        break;
                                                    } catch (Exception ex) {
                                                        System.err.println(file + ", error occor !!! ==> " + ex);
                                                        if (ii > 10) {
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        void find(File file) {
                                            if (currentScannerThreadStop) {
                                                return;
                                            }
                                            if (file == null || !file.exists()) {
                                                System.out.println("file == null || !file.exists()\t" + file);
                                                return;
                                            }
                                            scannerStatus.setText(model.getSize() + " : " + file.getAbsolutePath());
                                            if (file.isDirectory()) {
                                                if (file.listFiles() != null) {
                                                    for (File f : file.listFiles()) {
                                                        find(f);
                                                    }
                                                } else {
                                                    System.out.println("file.listFiles() == null!!\t" + file);
                                                }
                                                switch (fileOrDirType) {
                                                case DIRECTORY_ONLY:
                                                    addElement(file);
                                                    break;
                                                case ALL:
                                                    addElement(file);
                                                    break;
                                                }
                                            } else if (file.isFile()) {
                                                switch (fileOrDirType) {
                                                case FILE_ONLY:
                                                    addElement(file);
                                                    break;
                                                case ALL:
                                                    addElement(file);
                                                    break;
                                                }
                                            }
                                        }

                                        public void run() {
                                            find(scanDir);
                                            matchCountMap.put(scanVal, matchCount);
                                        }
                                    }, "file_scann_" + System.currentTimeMillis());
                                    currentScannerThread.setDaemon(true);
                                    currentScannerThread.start();
                                    threadList.add(currentScannerThread);
                                }

                                for (;;) {
                                    try {
                                        Thread.sleep(1000);
                                        boolean allTerminated = true;
                                        for (int ii = 0; ii < threadList.size(); ii++) {
                                            if (threadList.get(ii).getState() != Thread.State.TERMINATED) {
                                                allTerminated = false;
                                                break;
                                            }
                                        }
                                        if (allTerminated) {
                                            System.out.println("all done...");
                                            break;
                                        }
                                    } catch (InterruptedException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }

                                long endTime = System.currentTimeMillis() - startTime;

                                String status = "scan completed \n during :" + endTime + "\n file : " + model.getSize() + "\n \tResult : \n " + matchCountMap;
                                JOptionPaneUtil.newInstance().iconPlainMessage().showMessageDialog(status, "COMPLETED");
                                scannerStatus.setText(status);
                                scanList.setModel(model);

                                arrayBackupForInnerScan = ((DefaultListModel) scanList.getModel()).toArray();

                                currentScannerThreadStop = false;
                            }
                        }, "file_scann_main_" + System.currentTimeMillis());
                        scanMainThread.setDaemon(true);
                        scanMainThread.start();
                    } else {
                        if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("scanner is running \n want to stop??", "WARNING")) {
                            currentScannerThreadStop = true;
                        }
                    }
                }
            });
            swingUtil.addAction("scanDirText.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                        return;
                    }
                    File dir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                    if (dir == null) {
                        return;
                    }
                    scanDirText.setText(dir.getAbsolutePath());
                }
            });
            swingUtil.addAction("addListToExecList.actionPerformed", new Action() {

                Thread moveThread = null;

                public void action(EventObject evt) throws Exception {
                    if (moveThread != null && moveThread.getState() != Thread.State.TERMINATED) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("add list process already running!");
                        return;
                    }
                    moveThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                        public void run() {
                            DefaultListModel model = (DefaultListModel) scanList.getModel();
                            DefaultListModel model2 = (DefaultListModel) execList.getModel();
                            for (int ii = 0; ii < model.getSize(); ii++) {
                                File f = (File) model.getElementAt(ii);
                                model2.addElement(f.getAbsolutePath());
                            }
                            if (model.getSize() > 1000) {
                                JCommonUtil._jOptionPane_showMessageDialog_info("add list completed!\n" + model.getSize());
                            }
                        }
                    }, "addListToExecList.actionPerformed_" + System.currentTimeMillis());
                    moveThread.setDaemon(true);
                    moveThread.start();
                }
            });
            swingUtil.addAction("openSvnUpdate.actionPerformed", new Action() {

                Pattern svnPattern = Pattern.compile("^(?:[M|\\?])\\s+\\d*\\s+(.+)$");

                Thread svnThread = null;

                public void action(EventObject evt) throws Exception {
                    if (svnThread != null && svnThread.getState() != Thread.State.TERMINATED) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("svn scan process already running!");
                        return;
                    }
                    final File svnDir = JCommonUtil._jFileChooser_selectDirectoryOnly();
                    if (svnDir == null) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("dir is not correct!");
                        return;
                    }

                    svnThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                        public void run() {
                            try {
                                Process process = Runtime.getRuntime().exec(String.format("svn status -u \"%s\"", svnDir));
                                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                                Matcher matcher = null;
                                File file = null;
                                DefaultListModel model = new DefaultListModel();
                                List<File> scanList = new ArrayList<File>();
                                for (String line = null; (line = reader.readLine()) != null;) {
                                    matcher = svnPattern.matcher(line);
                                    if (matcher.find()) {
                                        file = new File(matcher.group(1));
                                        if (file.isFile()) {
                                            model.addElement(file.getAbsolutePath());
                                        }
                                        if (file.isDirectory()) {
                                            scanList.clear();
                                            FileUtil.searchFileMatchs(file, ".*", scanList);
                                            for (File f : scanList) {
                                                model.addElement(f.getAbsolutePath());
                                            }
                                        }
                                    } else {
                                        System.out.println("ignore : [" + line + "]");
                                    }
                                }
                                reader.close();
                                execList.setModel(model);
                                setTitle("svn : " + svnDir);

                                JCommonUtil._jOptionPane_showMessageDialog_info("svn scan completed!");
                            } catch (IOException e) {
                                JCommonUtil.handleException(e);
                            }
                        }
                    }, "svn_scan" + System.currentTimeMillis());
                    svnThread.setDaemon(true);
                    svnThread.start();
                }
            });
            swingUtil.addAction("contentFilterBtn.actionPerformed", new Action() {
                Thread thread = null;
                String encode = Charset.defaultCharset().displayName();

                public void action(EventObject evt) throws Exception {
                    if (thread != null && thread.getState() != Thread.State.TERMINATED) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("scan process already running!");
                        return;
                    }

                    final String filter = JCommonUtil._jOptionPane_showInputDialog("input filter content?");
                    if (StringUtils.isEmpty(filter)) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("filter is empty!");
                        return;
                    }

                    Pattern tmpPattern = null;
                    try {
                        tmpPattern = Pattern.compile(filter);
                    } catch (Exception ex) {
                    }
                    final Pattern filterPattern = tmpPattern;

                    try {
                        encode = JCommonUtil._jOptionPane_showInputDialog("input encode?", encode);
                    } catch (Exception ex) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("error encode!");
                        return;
                    }

                    thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                        public void run() {
                            DefaultListModel model = (DefaultListModel) execList.getModel();
                            DefaultListModel model_ = new DefaultListModel();
                            File file = null;
                            BufferedReader reader = null;
                            for (int ii = 0; ii < model.getSize(); ii++) {
                                file = (ZFile) model.getElementAt(ii);
                                if (!file.exists()) {
                                    continue;
                                }
                                try {
                                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
                                    for (String line = null; (line = reader.readLine()) != null;) {
                                        if (line.contains(filter)) {
                                            model_.addElement(file.getAbsolutePath());
                                            break;
                                        }
                                        if (filterPattern != null && filterPattern.matcher(line).find()) {
                                            model_.addElement(file.getAbsolutePath());
                                            break;
                                        }
                                    }
                                    reader.close();
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                            execList.setModel(model_);
                            JCommonUtil._jOptionPane_showMessageDialog_info("completed!");
                        }
                    }, UUID.randomUUID().toString());
                    thread.setDaemon(true);
                    thread.start();
                }
            });
            swingUtil.addAction("restoreBackBtn.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File logFile = JCommonUtil._jFileChooser_selectFileOnly();
                    if (!logFile.getName().equals("CopyTo.log")) {
                        Validate.isTrue(false, "檔案名稱必須為 : " + "CopyTo.log");
                    }
                    ExecuteOpener_restoreBackProcess.newInstance(logFile);
                }
            });
            swingUtil.addAction("saveConfigBtn.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    config.reflectSetConfig(ExecuteOpener.this);
                    config.store();
                }
            });
            swingUtil.addAction("propertiesListFilterText.focusLost", new Action() {
                public void action(EventObject evt) throws Exception {
                    reloadCurrentDirPropertiesList();
                }
            });
            swingUtil.addAction("innerScannerText.addMouseListener", new Action() {
                Thread innerScanThread = null;
                boolean innerScanStop = false;

                public void action(EventObject evt) throws Exception {
                    if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                        return;
                    }
                    final String innerText = innerScannerText.getText();
                    if (arrayBackupForInnerScan == null) {
                        return;
                    }

                    innerScanStop = true;

                    if (innerScanThread == null || innerScanThread.getState() == Thread.State.TERMINATED) {
                        innerScanThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                            public void run() {
                                innerScanStop = false;
                                System.out.println(toString() + " ... start!! ==> " + innerScanStop);
                                DefaultListModel model = new DefaultListModel();
                                for (int ii = 0; ii < arrayBackupForInnerScan.length; ii++) {
                                    if (arrayBackupForInnerScan[ii].toString().contains(innerText)) {
                                        model.addElement(arrayBackupForInnerScan[ii]);
                                    }
                                    if (innerScanStop) {
                                        System.out.println(toString() + " ... over!! ==> " + innerScanStop);
                                        break;
                                    }
                                }
                                scanList.setModel(model);
                                System.out.println(toString() + " ... run over!! ==> " + innerScanStop);
                            }
                        }, "innerScanner_" + System.currentTimeMillis());
                        innerScanThread.setDaemon(true);
                        innerScanThread.start();
                    }
                }
            });
            swingUtil.addAction("innerContentFilterText.addMouseListener", new Action() {
                Thread innerScanThread = null;
                boolean innerScanStop = false;

                public void action(EventObject evt) throws Exception {
                    if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                        return;
                    }
                    final String innerText = innerContentFilterText.getText();
                    if (arrayBackupForInnerScan == null) {
                        return;
                    }

                    innerScanStop = true;

                    if (innerScanThread == null || innerScanThread.getState() == Thread.State.TERMINATED) {
                        innerScanThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                            public void run() {
                                innerScanStop = false;
                                System.out.println(toString() + " ... start!! ==> " + innerScanStop);
                                DefaultListModel model = new DefaultListModel();
                                for (int ii = 0; ii < arrayBackupForInnerScan.length; ii++) {
                                    File file = (File) arrayBackupForInnerScan[ii];
                                    String content = FileUtil.loadFromFile(file, "UTF8");
                                    if (StringUtils.contains(content, innerText)) {
                                        model.addElement(arrayBackupForInnerScan[ii]);
                                    }
                                    if (innerScanStop) {
                                        System.out.println(toString() + " ... over!! ==> " + innerScanStop);
                                        break;
                                    }
                                }
                                scanList.setModel(model);
                                System.out.println(toString() + " ... run over!! ==> " + innerScanStop);
                            }
                        }, "innerScanner_" + System.currentTimeMillis());
                        innerScanThread.setDaemon(true);
                        innerScanThread.start();
                    }
                }
            });
            swingUtil.addAction("queryText.addDocumentListener", new Action() {
                public void action(EventObject evt) throws Exception {
                    try {
                        String query = JCommonUtil.getDocumentText((DocumentEvent) ActionAdapter.DocumentListener.getOrignEvent(evt));
                        Pattern ptn = Pattern.compile(query);
                        DefaultListModel model = new DefaultListModel();
                        for (Object key : prop.keySet()) {
                            String val = key.toString();
                            if (val.contains(query)) {
                                model.addElement(key);
                                continue;
                            }
                            if (ptn.matcher(val).find()) {
                                model.addElement(key);
                                continue;
                            }
                        }
                        execList.setModel(model);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            swingUtil.addAction("setExeConfigBtn.mouseClick", new Action() {
                public void action(EventObject evt) throws Exception {
                    JFileExecuteUtil_ConfigDlg.newInstance();
                }
            });
            swingUtil.addAction("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", new Action() {
                public void action(EventObject evt) throws Exception {
                }
            });

            config.reflectInit(this);
            this.setSize(870, 561);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static File diffLeft;
    static File diffRight;

    static File currentPropFile;

    enum ScanType {
        FILE_NAME_ONLY {
            boolean filter(boolean anyFile, String text, Pattern pattern, File file, boolean ignoreCheck, String[] ignoreArry) {
                if (anyFile) {
                    return true;
                }
                String filename = file.getName();
                if (ignoreCheck) {
                    if (StringUtils.indexOfAny(filename, ignoreArry) != -1) {
                        return false;
                    }
                }
                if (filename.contains(text)) {
                    return true;
                }
                if (pattern == null) {
                    return false;
                }
                return pattern.matcher(filename).find();
            }
        }, //
        FULL_PATH {
            boolean filter(boolean anyFile, String text, Pattern pattern, File file, boolean ignoreCheck, String[] ignoreArry) {
                if (anyFile) {
                    return true;
                }
                String filename = file.getAbsolutePath();
                if (ignoreCheck) {
                    if (StringUtils.indexOfAny(filename, ignoreArry) != -1) {
                        return false;
                    }
                }
                if (filename.contains(text)) {
                    return true;
                }
                if (pattern == null) {
                    return false;
                }
                return pattern.matcher(filename).find();
            }
        }, //
        ;
        ScanType() {
        }

        abstract boolean filter(boolean anyFile, String text, Pattern pattern, File file, boolean ignoreCheck, String[] ignoreArry);
    }

    static boolean currentScannerThreadStop = false;
    Object[] arrayBackupForInnerScan;
    static Pattern renameMatchPattern = Pattern.compile("(.+)_R\\d+(\\.\\w+)");
    private JCheckBox scanLstShowDetailChk;
    private JButton restoreBackBtn;
    private JPanel panel;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JList propertiesList;
    private JLabel lblFilter;
    private JTextField propertiesListFilterText;
    private JTextField innerContentFilterText;
    private JPanel panel_8;
    private JPanel panel_9;
    private JButton button;
    private JButton setExeConfigBtn;

    void reloadCurrentDirPropertiesList() {
        final String $filterText = StringUtils.trimToEmpty(propertiesListFilterText.getText()).toLowerCase();
        List<File> lst = new ArrayList<File>();
        for (File file : jarPositionDir.listFiles(new FileFilter() {
            public boolean accept(File paramFile) {
                boolean isPropFile = paramFile.getName().toLowerCase().endsWith(".properties");
                if (StringUtils.isBlank($filterText)) {
                    if (isPropFile) {
                        return true;
                    }
                } else {
                    Pattern ptn = Pattern.compile("(.*)\\.properties");
                    Matcher mth = ptn.matcher(paramFile.getName());
                    if (mth.find()) {
                        String name = mth.group(0);
                        if (name.toLowerCase().contains($filterText)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        })) {
            lst.add(file);
        }
        Collections.sort(lst);
        DefaultListModel model = new DefaultListModel();
        for (File file : lst) {
            model.addElement(file);
        }
        propertiesList.setModel(model);
    }

    void reloadExecListProperties(File file) throws FileNotFoundException, IOException {
        prop.clear();
        PropertiesUtil.loadProperties(new FileInputStream(file), prop);
        reloadExecListProperties(prop);
        setTitle(file.getAbsolutePath());
    }

    void reloadExecListProperties(Properties prop) throws FileNotFoundException, IOException {
        DefaultListModel execListModel = new DefaultListModel();
        List<String> keys = ListUtil.getList(prop.keySet(), String.class);
        Collections.sort(keys);
        for (Object obj : keys) {
            execListModel.addElement(new ZFile((String) obj));
        }
        execList.setModel(execListModel);
    }

    boolean isNeedToCopy(File exportTargetDir, File orignFile) {
        for (; orignFile != null && !orignFile.exists();) {
            orignFile = orignFile.getParentFile();
        }
        if (orignFile.isDirectory()) {
            System.out.println("dir is't need to copy!");
            return false;
        }
        Matcher matcher = null;
        String matcherName = "";
        for (File f : exportTargetDir.listFiles()) {
            matcher = renameMatchPattern.matcher(f.getName());
            matcherName = "";
            if (matcher.find()) {
                matcherName = matcher.group(1) + matcher.group(2);
            }
            if ((f.getName().equals(orignFile.getName()) || matcherName.equals(orignFile.getName())) && f.length() == orignFile.length()) {
                return false;
            }
        }
        return true;
    }

    File getCorrectFile(File f) {
        if (!f.exists()) {
            return getCorrectFile(f.getParentFile());
        } else if (f.isFile()) {
            return f;
        }
        return null;
    }

    enum FileOrDirType {
        FILE_ONLY(), //
        DIRECTORY_ONLY(), //
        ALL(), //
        ;
    }

    enum FileExecuter {
        JAR(".jar", "C:/apps/jd-gui-0.3.1.windows/jd-gui.exe"), //
        XHTML(".xhtml", "C:/apps/notepad/MadEdit-0.2.9.1/MadEdit.exe"), //
        ;

        final String extension;
        final String executer;

        final String commandFormat = "cmd /c call \"%s\" \"%s\"";

        FileExecuter(String extension, String executer) {
            this.extension = extension;
            this.executer = executer;
        }

        static String getCommand(File file) {
            for (FileExecuter fff : FileExecuter.values()) {
                if (file.getName().toLowerCase().endsWith(fff.extension)) {
                    return String.format(fff.commandFormat, fff.executer, file);
                }
            }
            return String.format("cmd /c call \"%s\"", file);
        }
    }

    void exec(String val) throws IOException {
        File f = new File(val);
        if (f.isDirectory()) {
            Desktop.getDesktop().open(f);
        } else if (!f.exists()) {
            exec(f.getParent());
        } else if (f.isFile()) {
            String command = FileExecuter.getCommand(f);
            System.out.println("EXE => " + command);
            Runtime.getRuntime().exec(command);
        } else {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("unknow file :\n" + f, "ERROR");
        }
    }

    private void jlistMouseRightClickEvent(final JList scanList, boolean isScanList, EventObject evt) {
        if (JMouseEventUtil.buttonRightClick(1, evt)) {
            JPopupMenuUtil popupUtil = JPopupMenuUtil.newInstance(scanList).applyEvent(evt);

            if (scanList.getSelectedValues().length == 1) {
                popupUtil.addJMenuItem(JFileExecuteUtil.newInstance(((ZFile) scanList.getSelectedValues()[0])).createDefaultJMenuItems());
                popupUtil.addJMenuItem("----------------", false);
            }

            final Object[] vals = scanList.getSelectedValues();
            if (vals == null || vals.length == 0) {
                return;
            }

            if (isScanList) {
                popupUtil.addJMenuItem("+ 加到維護清單 : " + vals.length, new ActionListener() {
                    private void addModelElement(File file) {
                        DefaultListModel model = (DefaultListModel) execList.getModel();
                        for (int ii = 0; ii < model.getSize(); ii++) {
                            ZFile fff = (ZFile) model.get(ii);
                            if (StringUtils.equals(fff.getAbsolutePath(), file.getAbsolutePath())) {
                                return;
                            }
                        }
                        model.addElement(new ZFile(file.getParentFile(), file.getName()));
                    }

                    public void actionPerformed(ActionEvent arg0) {
                        File file = null;
                        for (Object v : vals) {
                            file = (File) v;
                            addModelElement(file);
                        }
                    }
                });
            }

            popupUtil.addJMenuItem("----------------", false);

            popupUtil//
                    .addJMenuItem("sort list", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            DefaultListModel model = (DefaultListModel) scanList.getModel();
                            Object[] arry = model.toArray();
                            Arrays.sort(arry, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    return ((ZFile) o1).getAbsolutePath().compareTo(((ZFile) o2).getAbsolutePath());
                                }
                            });
                            DefaultListModel model2 = new DefaultListModel();
                            for (Object obj : arry) {
                                ZFile file = (ZFile) obj;
                                model2.addElement(file);
                            }
                            scanList.setModel(model2);
                        }
                    }).addJMenuItem("keep exists", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            DefaultListModel model = (DefaultListModel) scanList.getModel();
                            DefaultListModel model2 = new DefaultListModel();
                            for (Object obj : model.toArray()) {
                                ZFile file = (ZFile) obj;
                                if (file.exists()) {
                                    model2.addElement(obj);
                                }
                            }
                            scanList.setModel(model2);
                        }
                    }).addJMenuItem("remove duplicate", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            DefaultListModel model = (DefaultListModel) scanList.getModel();
                            DefaultListModel model2 = new DefaultListModel();
                            Set<ZFile> set = new HashSet<ZFile>();
                            for (Object obj : model.toArray()) {
                                ZFile file = (ZFile) obj;
                                set.add(file);
                            }
                            for (ZFile val : set) {
                                model2.addElement(val);
                            }
                            scanList.setModel(model2);
                        }
                    }).addJMenuItem("remove folder", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            DefaultListModel model = (DefaultListModel) scanList.getModel();
                            for (int ii = 0; ii < model.getSize(); ii++) {
                                if (((ZFile) model.getElementAt(ii)).isDirectory()) {
                                    model.removeElementAt(ii);
                                    ii--;
                                }
                            }
                        }
                    }).addJMenuItem("remove empty folder", new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            DefaultListModel model = (DefaultListModel) scanList.getModel();
                            File dir = null;
                            for (int ii = 0; ii < model.getSize(); ii++) {
                                dir = ((ZFile) model.getElementAt(ii));
                                if (dir.isDirectory() && dir.list().length == 0) {
                                    model.removeElementAt(ii);
                                    ii--;
                                }
                            }
                        }
                    }).addJMenuItem("----------------", false).addJMenuItem("diff left : " + (diffLeft != null ? diffLeft.getName() : ""), true, new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            File value = ((ZFile) JListUtil.getLeadSelectionObject(scanList));
                            if (value != null && value.isFile() && value.exists()) {
                                diffLeft = value;
                            }
                        }
                    }).addJMenuItem("diff right : " + (diffRight != null ? diffRight.getName() : ""), true, new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            File value = ((ZFile) JListUtil.getLeadSelectionObject(scanList));
                            if (value != null && value.isFile() && value.exists()) {
                                diffRight = value;
                            }
                        }
                    }).addJMenuItem((diffLeft != null && diffRight != null) ? "diff compare" : "", (diffLeft != null && diffRight != null), new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            try {
                                Runtime.getRuntime().exec(String.format("cmd /c call TortoiseMerge.exe /base:\"%s\" /theirs:\"%s\"", diffLeft, diffRight));
                            } catch (IOException ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    }).addJMenuItem((scanList.getSelectedValues().length == 2) ? "diff compare" : "", (scanList.getSelectedValues().length == 2), new ActionListener() {
                        public void actionPerformed(ActionEvent arg0) {
                            try {
                                Runtime.getRuntime().exec(String.format("cmd /c call TortoiseMerge.exe /base:\"%s\" /theirs:\"%s\"", scanList.getSelectedValues()[0], scanList.getSelectedValues()[1]));
                            } catch (IOException ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    }).addJMenuItem("----------------", false);//

            popupUtil//
                    .addJMenuItem("copy to", new ActionListener() {

                        private File getToFile(File dir, String name) {
                            int ii = 0;
                            File f = null;
                            for (f = new File(dir, name); f.exists();) {
                                f = new File(dir, name + "." + ii);
                                ii++;
                            }
                            return f;
                        }

                        public void actionPerformed(ActionEvent e) {
                            File destDir = new File(FileUtil.DESKTOP_DIR, ExecuteOpener.class.getSimpleName() + "_CopyTo_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));
                            destDir.mkdirs();
                            BufferedWriter writer = null;
                            try {
                                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(destDir, "CopyTo.log"))));
                                int success = 0;
                                int fail = 0;
                                for (Object v : scanList.getSelectedValues()) {
                                    File fromFile = ((ZFile) v);
                                    File toFile = getToFile(destDir, fromFile.getName());
                                    if (fromFile.exists()) {
                                        boolean copyToSuccess = FileUtil.copyFile(fromFile, toFile);
                                        writer.write(String.format("from:%s\tto:%s\t%s", fromFile, toFile, (copyToSuccess ? "Y" : "N")));
                                        writer.newLine();
                                        if (copyToSuccess) {
                                            success++;
                                        } else {
                                            fail++;
                                        }
                                    } else {
                                        writer.write(String.format("from:%s\tto:%s\t%s", fromFile, "NA", "N(來源不存在)"));
                                        writer.newLine();
                                        fail++;
                                    }
                                }
                                JCommonUtil._jOptionPane_showMessageDialog_info("複製完成\n成功:" + success + "\n失敗:" + fail);
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            } finally {
                                try {
                                    writer.flush();
                                } catch (IOException e1) {
                                }
                                try {
                                    writer.close();
                                } catch (IOException e1) {
                                }
                            }
                        }
                    });
            popupUtil.show();//
        }
    }

    private static class ZFile extends File {

        private static boolean is_show_detail = false;

        public ZFile(String filePath) {
            super(filePath);
        }

        public ZFile(File parent, String child) {
            super(parent, child);
        }

        private static final long serialVersionUID = 1L;

        public String toString() {
            if (is_show_detail) {
                String createTime = DateFormatUtils.format(FileUtil.getCreateTime(this), "yyyy/MM/dd_HHmm");
                String modifyTime = DateFormatUtils.format(this.lastModified(), "yyyy/MM/dd_HHmm");
                String sizeDesc = FileUtil.getSizeDescription(this.length());
                String formatStr = "<html><font color='#cc88cc'>建%s</font>&nbsp;&nbsp;<font color='#cccc88'>改%s</font>&nbsp;&nbsp;<font color='#6341a5'>%s</font>&nbsp;&nbsp;&nbsp;&nbsp;%s</html>";
                return String.format(formatStr, createTime, modifyTime, sizeDesc, super.toString());
            } else {
                return super.toString();
            }
        }
    }
}
