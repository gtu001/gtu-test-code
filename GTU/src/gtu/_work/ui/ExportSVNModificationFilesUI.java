package gtu._work.ui;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.time.DateFormatUtils;

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
public class ExportSVNModificationFilesUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton startCompareMatch;
    private JPanel jPanel5;
    private JScrollPane jScrollPane4;
    private JButton openExternalSrcFolder;
    private JTable compareTable;
    private JPanel jPanel4;
    private JButton srcBaseDir;
    private JScrollPane jScrollPane3;
    private JButton execute;
    private JButton outPutDir;
    private JList outPutList;
    private JList srcList;
    private JButton loadSrcTextarea;
    private JComboBox exportModeCombo;
    private JPanel jPanel6;
    private JTextArea srcArea;
    private JPanel jPanel3;
    private JPanel jPanel2;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ExportSVNModificationFilesUI inst = new ExportSVNModificationFilesUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public ExportSVNModificationFilesUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            final SwingActionUtil swingUtil = SwingActionUtil.newInstance(this);

            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setTitle("SVN BACKUP");
            this.setPreferredSize(new java.awt.Dimension(734, 442));
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                jTabbedPane1.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent evt) {
                        swingUtil.invokeAction("jTabbedPane1_changeEvent", evt);
                    }
                });
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("src text", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            srcArea = new JTextArea();
                            jScrollPane1.setViewportView(srcArea);
                        }
                    }
                    {
                        loadSrcTextarea = new JButton();
                        jPanel1.add(loadSrcTextarea, BorderLayout.SOUTH);
                        loadSrcTextarea.setText("load src textarea");
                        loadSrcTextarea.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("loadSrcTextarea.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        srcBaseDir = new JButton();
                        jPanel1.add(srcBaseDir, BorderLayout.NORTH);
                        srcBaseDir.setText("set src base dir");
                        srcBaseDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("srcBaseDir.actionPerformed", evt);
                            }
                        });
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("src list", null, jPanel2, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(531, 317));
                        {
                            ListModel srcListModel = new DefaultListModel();
                            srcList = new JList();
                            jScrollPane2.setViewportView(srcList);
                            srcList.setModel(srcListModel);
                            srcList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("srcList.mouseClicked", evt);
                                }
                            });
                            srcList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    swingUtil.invokeAction("srcList.keyPressed", evt);
                                }
                            });
                        }
                    }
                    {
                        srcListQuery = new JTextField();
                        srcListQuery.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                            public void process(DocumentEvent event) {
                                String scan = JCommonUtil.getDocumentText(event);
                                DefaultListModel model = new DefaultListModel();
                                Pattern pat = Pattern.compile(scan);
                                for (LineParser line : copySrcListForQuerySet) {
                                    if (!line.file.exists()) {
                                        continue;
                                    }
                                    if (line.file.getAbsolutePath().contains(scan)) {
                                        model.addElement(line);
                                        continue;
                                    }
                                    try {
                                        if (pat.matcher(line.file.getAbsolutePath()).find()) {
                                            model.addElement(line);
                                            continue;
                                        }
                                    } catch (Exception ex) {
                                    }
                                }
                                srcList.setModel(model);
                            }
                        }));
                        jPanel2.add(srcListQuery, BorderLayout.NORTH);
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("out list", null, jPanel3, null);
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel3.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            ListModel outPutListModel = new DefaultListModel();
                            outPutList = new JList();
                            jScrollPane3.setViewportView(outPutList);
                            outPutList.setModel(outPutListModel);
                            outPutList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("outPutList.mouseClicked", evt);
                                }
                            });
                            outPutList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    swingUtil.invokeAction("outPutList.keyPressed", evt);
                                }
                            });
                        }
                    }
                    {
                        outPutDir = new JButton();
                        jPanel3.add(outPutDir, BorderLayout.NORTH);
                        outPutDir.setText("set output dir");
                        outPutDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("outPutDir.actionPerformed", evt);
                            }
                        });
                    }
                    {
                        execute = new JButton();
                        jPanel3.add(execute, BorderLayout.SOUTH);
                        execute.setText("execute backup");
                        execute.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                swingUtil.invokeAction("execute.actionPerformed", evt);
                            }
                        });
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("compre", null, jPanel4, null);
                    {
                        jScrollPane4 = new JScrollPane();
                        jPanel4.add(jScrollPane4, BorderLayout.CENTER);
                        jScrollPane4.setPreferredSize(new java.awt.Dimension(713, 339));
                        {
                            TableModel compareTableModel = new DefaultTableModel();
                            compareTable = new JTable();
                            jScrollPane4.setViewportView(compareTable);
                            compareTable.setModel(compareTableModel);
                            JTableUtil.defaultSetting(compareTable);
                            compareTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    swingUtil.invokeAction("compareTable.mouseClicked", evt);
                                }
                            });
                        }
                    }
                    {
                        jPanel5 = new JPanel();
                        jPanel4.add(jPanel5, BorderLayout.NORTH);
                        jPanel5.setPreferredSize(new java.awt.Dimension(713, 42));
                        {
                            openExternalSrcFolder = new JButton();
                            jPanel5.add(openExternalSrcFolder);
                            openExternalSrcFolder.setText("open external src folder");
                            openExternalSrcFolder.setPreferredSize(new java.awt.Dimension(280, 29));
                            openExternalSrcFolder.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("openExternalSrcFolder.actionPerformed", evt);
                                }
                            });
                        }
                        {
                            startCompareMatch = new JButton();
                            jPanel5.add(startCompareMatch);
                            startCompareMatch.setText("start compare source");
                            startCompareMatch.setPreferredSize(new java.awt.Dimension(280, 29));
                            startCompareMatch.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    swingUtil.invokeAction("startCompareMatch.actionPerformed", evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel6 = new JPanel();
                    GroupLayout jPanel6Layout = new GroupLayout((JComponent) jPanel6);
                    jPanel6.setLayout(jPanel6Layout);
                    jTabbedPane1.addTab("configuration", null, jPanel6, null);
                    {
                        DefaultComboBoxModel exportModeComboModel = new DefaultComboBoxModel();
                        for (ParseMode mode : ParseMode.values()) {
                            exportModeComboModel.addElement(mode);
                        }
                        exportModeCombo = new JComboBox();
                        exportModeCombo.setModel(exportModeComboModel);
                    }
                    jPanel6Layout.setHorizontalGroup(jPanel6Layout.createSequentialGroup().addContainerGap(41, 41)
                            .addComponent(exportModeCombo, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE).addContainerGap(505, Short.MAX_VALUE));
                    jPanel6Layout.setVerticalGroup(jPanel6Layout.createSequentialGroup().addContainerGap(30, 30)
                            .addComponent(exportModeCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(321, Short.MAX_VALUE));
                }
                {
                    jPanel7 = new JPanel();
                    BorderLayout jPanel7Layout = new BorderLayout();
                    jPanel7.setLayout(jPanel7Layout);
                    jTabbedPane1.addTab("error log", null, jPanel7, null);
                    {
                        jScrollPane5 = new JScrollPane();
                        jPanel7.add(jScrollPane5, BorderLayout.CENTER);
                        {
                            DefaultListModel errorLogListModel = new DefaultListModel();
                            errorLogList = new JList();
                            jScrollPane5.setViewportView(errorLogList);
                            errorLogList.setModel(errorLogListModel);
                        }
                    }
                }
            }
            this.setSize(734, 442);

            swingUtil.addAction("openExternalSrcFolder.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File dir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                    if (dir == null) {
                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                        return;
                    }
                    externalDir = dir;
                }
            });
            swingUtil.addAction("startCompareMatch.actionPerformed", new Action() {

                public void action(EventObject evt) throws Exception {
                    Validate.notNull(manualBaseDir, "source folder not set!!");
                    Validate.notNull(externalDir, "external folder not set!!");
                    Validate.isTrue(!manualBaseDir.equals(externalDir), "source dir : " + manualBaseDir + "\nexternal dir : " + externalDir + "\n cant be the same!!");

                    List<File> externalSrcFolderList = new ArrayList<File>();
                    FileUtil.searchFileMatchs(externalDir, ".*", externalSrcFolderList);
                    System.out.println("externalSrcFolderList = " + externalSrcFolderList.size());

                    List<File> manualBaseSourceList = new ArrayList<File>();
                    FileUtil.searchFileMatchs(manualBaseDir, ".*", manualBaseSourceList);
                    System.out.println("manualBaseSourceList = " + manualBaseSourceList.size());

                    String cutExternalPath = FileUtil.exportReceiveBaseDir(externalSrcFolderList).getAbsolutePath();
                    System.out.println("cutExternalPath = " + cutExternalPath);
                    int cutExternalLength = cutExternalPath.length();

                    List<CompareFile> _compareList = new ArrayList<CompareFile>();
                    CompareFile compare = null;
                    File mostCloseFile = null;
                    List<File> searchMatchSrcList = new ArrayList<File>();
                    for (File external : externalSrcFolderList) {
                        compare = new CompareFile();
                        compare.external = external;

                        searchMatchSrcList.clear();
                        mostCloseFile = new File(manualBaseDir, external.getAbsolutePath().substring(cutExternalLength));
                        System.out.println(mostCloseFile.exists() + " == close file : " + mostCloseFile);
                        if (mostCloseFile.exists()) {
                            searchMatchSrcList.add(mostCloseFile);
                        } else {
                            for (File src : manualBaseSourceList) {
                                if (src.getName().equalsIgnoreCase(external.getName())) {
                                    searchMatchSrcList.add(src);
                                }
                            }
                        }

                        System.out.println(external.getName() + " => match source : " + searchMatchSrcList.size());
                        compare.srcSet = new HashSet<File>(searchMatchSrcList);
                        _compareList.add(compare);
                    }
                    compareList = _compareList;
                    reloadCompareTable();
                }
            });
            swingUtil.addAction("compareTable.mouseClicked", new Action() {
                String tortoiseMergeFormat = "cmd /c call TortoiseMerge /base:\"%s\" /theirs:\"%s\"";
                String openFileFormat = "cmd /c call \"%s\"";

                void openFile(File file) {
                    String command = String.format(openFileFormat, file);
                    System.out.println(command);
                    try {
                        ProcessWatcher.newInstance(Runtime.getRuntime().exec(command)).getStream();
                        System.out.println("do reload...");
                        reloadCompareTable();
                    } catch (IOException e1) {
                        JCommonUtil.handleException(e1);
                    }
                }

                public void action(EventObject evt) throws Exception {
                    MouseEvent event = (MouseEvent) evt;

                    int rowPos = JTableUtil.newInstance(compareTable).getSelectedRow();
                    final File external = (File) JTableUtil.newInstance(compareTable).getModel().getValueAt(rowPos, CompareTableIndex.EXTERNAL_FILE.pos);
                    final File srcFile = (File) JTableUtil.newInstance(compareTable).getModel().getValueAt(rowPos, CompareTableIndex.SOURCE_FILE.pos);

                    System.out.println("external : " + external);
                    System.out.println("srcFile : " + srcFile);

                    if (JMouseEventUtil.buttonLeftClick(2, event)) {
                        String command = String.format(tortoiseMergeFormat, external, srcFile);
                        System.out.println(command);
                        Runtime.getRuntime().exec(command);
                    }
                    if (JMouseEventUtil.buttonRightClick(1, event)) {
                        JMenuItem showInfoMenu = new JMenuItem();
                        showInfoMenu.setText("information");
                        showInfoMenu.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JOptionPaneUtil
                                        .newInstance()
                                        .iconInformationMessage()
                                        .showMessageDialog(//
                                                "source file : \n" + srcFile + "\n" + DateFormatUtils.format(srcFile.lastModified(), "yyyy/MM/dd HH:mm:ss") + "\n" + "size : " + srcFile.length()
                                                        / 1024 + "\n\n" + "external file : \n" + external + "\n" + DateFormatUtils.format(external.lastModified(), "yyyy/MM/dd HH:mm:ss") + "\n"
                                                        + "size : " + external.length() / 1024, "INFORMATION");
                            }
                        });
                        JMenuItem openFileMenu = new JMenuItem();
                        openFileMenu.setText("OPEN : source");
                        openFileMenu.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                openFile(srcFile);
                            }
                        });
                        JMenuItem openExternalMenu = new JMenuItem();
                        openExternalMenu.setText("OPEN : external");
                        openExternalMenu.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                openFile(external);
                            }
                        });
                        JMenuItem openPairlMenu = new JMenuItem();
                        openPairlMenu.setText("OPEN : all");
                        openPairlMenu.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                openFile(srcFile);
                                openFile(external);
                            }
                        });

                        JPopupMenuUtil.newInstance(compareTable).applyEvent(event).addJMenuItem(showInfoMenu, openFileMenu, openExternalMenu, openPairlMenu).show();
                    }
                }
            });
            swingUtil.addAction("loadSrcTextarea.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    loadSrcTextarea();
                }
            });
            swingUtil.addAction("srcList.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (JMouseEventUtil.buttonRightClick(1, evt)) {
                        JPopupMenuUtil.newInstance(srcList).applyEvent(evt).addJMenuItem("mark svn delete", new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().confirmButtonYesNo()
                                        .showConfirmDialog("are you sure, mark delete file : " + srcList.getSelectedValues().length, "SVN DELETE")) {
                                    return;
                                }
                                StringBuilder sb = new StringBuilder();
                                Process process = null;
                                InputStream in = null;
                                for (Object obj : srcList.getSelectedValues()) {
                                    LineParser l = (LineParser) obj;
                                    String command = String.format("svn delete \"%s\"", l.file);
                                    System.out.println(command);
                                    try {
                                        process = Runtime.getRuntime().exec(command);
                                        in = process.getInputStream();
                                        for (; in.read() != -1;)
                                            ;
                                        if (l.file.exists()) {
                                            sb.append(l.file.getName() + "\n");
                                        }
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                        sb.append(l.file.getName() + "\n");
                                    }
                                }
                                if (sb.length() > 0) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("error : \n" + sb, "ERROR");
                                } else {
                                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("mark delete completed!!", "SUCCESS");
                                }
                                DefaultListModel model = (DefaultListModel) srcList.getModel();
                                for (int ii = 0; ii < model.getSize(); ii++) {
                                    LineParser l = (LineParser) model.getElementAt(ii);
                                    if (!l.file.exists()) {
                                        model.remove(ii);
                                        ii--;
                                    }
                                }
                            }
                        }).show();
                    }

                    if (!JListUtil.newInstance(srcList).isCorrectMouseClick(evt)) {
                        return;
                    }
                    LineParser lineParser = (LineParser) JListUtil.getLeadSelectionObject(srcList);
                    if (lineParser == null) {
                        return;
                    }
                    Runtime.getRuntime().exec("cmd /c call \"" + lineParser.file + "\"");
                }
            });
            swingUtil.addAction("srcList.keyPressed", new Action() {
                public void action(EventObject evt) throws Exception {
                    JListUtil.newInstance(srcList).defaultJListKeyPressed(evt);
                }
            });
            swingUtil.addAction("outPutList.mouseClicked", new Action() {
                public void action(EventObject evt) throws Exception {
                    if (!JListUtil.newInstance(outPutList).isCorrectMouseClick(evt)) {
                        return;
                    }
                }
            });
            swingUtil.addAction("outPutList.keyPressed", new Action() {
                public void action(EventObject evt) throws Exception {
                    JListUtil.newInstance(outPutList).defaultJListKeyPressed(evt);
                }
            });
            swingUtil.addAction("outPutDir.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File dir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                    if (dir == null) {
                        outputDir = DEFAULT_OUTPUT_DIR;
                    } else {
                        outputDir = dir;
                    }
                    reflushOutputList();
                }
            });
            swingUtil.addAction("srcBaseDir.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    File dir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                    Validate.notNull(dir, "src base directory is not correct!");
                    manualBaseDir = dir;
                }
            });
            swingUtil.addAction("execute.actionPerformed", new Action() {
                public void action(EventObject evt) throws Exception {
                    DefaultListModel model = (DefaultListModel) outPutList.getModel();
                    for (int ii = 0; ii < model.size(); ii++) {
                        OutputFile file = (OutputFile) model.getElementAt(ii);
                        if (!file.destFile.getParentFile().exists()) {
                            file.destFile.getParentFile().mkdirs();
                        }
                        FileUtil.copyFile(file.srcFile, file.destFile);
                    }
                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("copy success!!\nsize = " + model.getSize(), getTitle());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<CompareFile> compareList;
    static final File NO_MATCH_FILE;

    static {
        File file = null;
        try {
            file = File.createTempFile("NO_MATCH_FILE__", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        NO_MATCH_FILE = file;
    }

    void reloadCompareTable() {
        DefaultTableModel model = JTableUtil.createModel(true, "no", "same", "file name", "external file", "src file");
        int index = 0;
        for (CompareFile tmpCompare : compareList) {
            if (tmpCompare.srcSet == null || tmpCompare.srcSet.isEmpty()) {
                model.addRow(new Object[] {//
                index,//
                        "NA",// 查無檔案
                        tmpCompare.external.getName(), //
                        tmpCompare.external, //
                        NO_MATCH_FILE //
                });
            } else {
                for (File matchSrcFile : tmpCompare.srcSet) {
                    model.addRow(new Object[] {//
                    index,//
                            (tmpCompare.external.length() == matchSrcFile.length() ? "" : "X"),// X=有差異
                            tmpCompare.external.getName(), //
                            tmpCompare.external, //
                            matchSrcFile //
                    });
                }
            }
            index++;
        }
        compareTable.setModel(model);
        JTableUtil.newInstance(compareTable).setMaxWidth(50, 50, 300, -1, -1);
    }

    enum CompareTableIndex {
        NO(0), SAME_LENGTH(1), FILE_NAME(2), EXTERNAL_FILE(3), SOURCE_FILE(4);
        final int pos;

        CompareTableIndex(int pos) {
            this.pos = pos;
        }
    }

    final static File DEFAULT_OUTPUT_DIR = new File(FileUtil.DESKTOP_PATH + //
            "/svn_output_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + "/");
    File outputDir;
    static File externalDir;
    static File manualBaseDir;

    void reflushOutputList() {
        DefaultListModel model = (DefaultListModel) srcList.getModel();
        LineParser lineParser = null;
        OutputFile outputFile = null;
        List<File> list = new ArrayList<File>();
        for (int ii = 0; ii < model.size(); ii++) {
            lineParser = (LineParser) model.getElementAt(ii);
            list.add(lineParser.file);
        }
        File baseDir = FileUtil.exportReceiveBaseDir(list);
        Validate.notNull(baseDir, "no common base directory!");
        DefaultListModel outputModel = new DefaultListModel();
        for (int ii = 0; ii < model.size(); ii++) {
            lineParser = (LineParser) model.getElementAt(ii);
            outputFile = new OutputFile();
            outputFile.srcFile = lineParser.file;
            outputFile.destFile = FileUtil.exportFileToTargetPath(lineParser.file, baseDir, outputDir);
            outputModel.addElement(outputFile);
        }
        outPutList.setModel(outputModel);
    }

    Set<LineParser> copySrcListForQuerySet;
    private JScrollPane jScrollPane5;
    private JList errorLogList;
    private JPanel jPanel7;
    private JTextField srcListQuery;

    void loadSrcTextarea() throws IOException {
        String srcText = null;
        Validate.notEmpty((srcText = srcArea.getText()), "src textarea can't empty");
        Validate.notNull(manualBaseDir, "src base directory not set!");
        BufferedReader reader = new BufferedReader(new StringReader(srcText));
        ParseMode mode = null;
        LineParser lineParser = null;
        Set<LineParser> set = new HashSet<LineParser>();
        DefaultListModel errorLogModel = new DefaultListModel();
        for (String line = null; (line = reader.readLine()) != null;) {
            mode = ((ParseMode) exportModeCombo.getSelectedItem());
            if (mode == null) {
                continue;
            }
            lineParser = mode.parse(line, errorLogModel);
            System.out.println("lineParser = " + lineParser);
            if (lineParser != null) {
                set.add(lineParser);
            }
        }
        errorLogList.setModel(errorLogModel);
        if (errorLogModel.getSize() != 0) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("same error occor!,\n check out error log panel", "ERROR");
        }

        DefaultListModel srcModel = new DefaultListModel();
        for (LineParser line : set) {
            srcModel.addElement(line);
        }
        srcList.setModel(srcModel);
        copySrcListForQuerySet = Collections.unmodifiableSet(set);
    }

    enum ParseMode {
        UPDATE() {
            LineParser parse(String line, DefaultListModel errorLogModel) {
                try {
                    Matcher matcher = updatePattern.matcher(line);
                    if (matcher.find()) {
                        if (line.endsWith("application/octet-stream")) {
                            return null;
                        }
                        File file = new File(matcher.group(2));
                        if (!file.exists() || file.isDirectory()) {
                            return null;
                        }
                        LineParser lp = new LineParser();
                        lp.orignLine = line;
                        lp.desc = matcher.group(1);
                        lp.file = file;
                        return lp;
                    }
                } catch (Exception ex) {
                    errorLogModel.addElement(ex);
                }
                return null;
            }
        },
        MODIFICATIONS() {
            LineParser parse(String line, DefaultListModel errorLogModel) {
                try {
                    Matcher matcher = modificationsPattern.matcher(line);
                    if (matcher.find()) {
                        File file = new File(manualBaseDir, matcher.group(1));
                        if (!file.exists()) {
                            errorLogModel.addElement("file not found : " + file);
                            return null;
                        }
                        if (file.isDirectory()) {
                            return null;
                        }
                        LineParser lp = new LineParser();
                        lp.orignLine = line;
                        lp.desc = matcher.group(1);
                        lp.file = file;
                        return lp;
                    }
                } catch (Exception ex) {
                    errorLogModel.addElement(ex);
                }
                return null;
            }
        },
        HISTORY() {
            LineParser parse(String line, DefaultListModel errorLogModel) {
                try {
                    Matcher matcher = historyPattern.matcher(line);
                    if (matcher.find()) {
                        String tmp = matcher.group(1);
                        File file = new File(manualBaseDir, tmp);
                        try {
                            for (; !file.exists();) {
                                tmp = tmp.substring((tmp.indexOf("/")) + 1);
                                tmp = tmp.substring((tmp.indexOf("/")));
                                file = new File(manualBaseDir, tmp).getAbsoluteFile();
                            }
                        } catch (Exception ex) {
                            errorLogModel.addElement("file not found : " + file);
                            return null;
                        }
                        if (file.isDirectory()) {
                            return null;
                        }
                        LineParser lp = new LineParser();
                        lp.orignLine = line;
                        lp.desc = matcher.group(1);
                        lp.file = file;
                        return lp;
                    }
                } catch (Exception ex) {
                    errorLogModel.addElement(ex);
                }
                return null;
            }
        },
        ;

        Pattern updatePattern = Pattern.compile("(\\w+):\\s(.*)");
        Pattern modificationsPattern = Pattern.compile("[\\s\\t]*([^\\t]+)[\\s\\t]*");
        Pattern historyPattern = Pattern.compile("[A-Za-z]+\\s\\:\\s(.+)");

        abstract LineParser parse(String line, DefaultListModel errorLogModel);
    }

    static class LineParser {
        String orignLine;
        String desc;
        File file;

        @Override
        public String toString() {
            return file.getAbsolutePath();
        }
    }

    static class OutputFile implements Cloneable {
        File srcFile;
        File destFile;

        @Override
        public String toString() {
            return destFile.getAbsolutePath();
        }
    }

    static class CompareFile {
        File external;
        Set<File> srcSet;
    }
}
