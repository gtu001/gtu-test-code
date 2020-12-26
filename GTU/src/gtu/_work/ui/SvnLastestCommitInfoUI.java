package gtu._work.ui;

import gtu.file.FileUtil;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
public class SvnLastestCommitInfoUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 4635407648659900833L;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton saveCurrentData;
    private JLabel jLabel1;
    private JLabel matchCount;
    private JButton loadAuthorReference;
    private JButton loadDataFromFile;
    private JButton multiExtendsionFilter;
    private JList fileExtensionFilter;
    private JComboBox fileExtensionComboBox;
    private JComboBox authorComboBox;
    private JTable svnTable;
    private JTextField filterText;
    private JPanel jPanel3;
    private JButton choiceSvnDir;
    private JPanel jPanel2;
    private JTabbedPane jTabbedPane1;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SvnLastestCommitInfoUI inst = new SvnLastestCommitInfoUI();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public SvnLastestCommitInfoUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(thisLayout);
            this.setFocusable(false);
            this.setTitle("SVN lastest commit wather");
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("svn dir", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            TableModel svnTableModel = new DefaultTableModel();
                            svnTable = new JTable();
                            jScrollPane1.setViewportView(svnTable);
                            svnTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    try{
                                        if (evt.getButton() == 3) {
                                            final List<File> list = new ArrayList<File>();
                                            SvnFile svnFile = null;
                                            final StringBuilder sb = new StringBuilder();
                                            for (int row : svnTable.getSelectedRows()) {
                                                svnFile = (SvnFile) svnTable.getModel().getValueAt(svnTable.getRowSorter().convertRowIndexToModel(row), SvnTableColumn.SVN_FILE.pos);
                                                list.add(svnFile.file);
                                                sb.append(svnFile.file.getName() + ",");
                                            }
                                            if (sb.length() > 200) {
                                                sb.delete(200, sb.length() - 1);
                                            }
                                            JMenuItem copySelectedMeun = new JMenuItem();
                                            copySelectedMeun.setText("copy selected file : " + list.size());
                                            copySelectedMeun.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent e) {
                                                    if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconPlainMessage().confirmButtonYesNo()
                                                            .showConfirmDialog("are you sure copy files :\n" + sb + "\n???", "COPY SELECTED FILE : " + list.size())) {
                                                        return;
                                                    }
                                                    final File copyToDir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                                                    if (copyToDir == null) {
                                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir folder is not correct!", "ERROR");
                                                        return;
                                                    }
                                                    new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                                                        public void run() {
                                                            StringBuilder errMsg = new StringBuilder();
                                                            int errCount = 0;
                                                            for (File f : list) {
                                                                try {
                                                                    FileUtil.copyFile(f, new File(copyToDir, f.getName()));
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    errCount++;
                                                                    errMsg.append(f + "\n");
                                                                }
                                                            }

                                                            JOptionPaneUtil.newInstance().iconPlainMessage()
                                                                    .showMessageDialog("copy completed!\nerror : " + errCount + "\nerror list : \n" + errMsg, "COMPLETED");
                                                        }
                                                    }, "copySelectedFiles_" + hashCode()).start();
                                                }
                                            });

                                            JMenuItem copySelectedOringTreeMeun = new JMenuItem();
                                            copySelectedOringTreeMeun.setText("copy selected file (orign tree): " + list.size());
                                            copySelectedOringTreeMeun.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent e) {
                                                    if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION != JOptionPaneUtil.newInstance().iconPlainMessage().confirmButtonYesNo()
                                                            .showConfirmDialog("are you sure copy files :\n" + sb + "\n???", "COPY SELECTED FILE : " + list.size())) {
                                                        return;
                                                    }
                                                    final File copyToDir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                                                    if (copyToDir == null) {
                                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir folder is not correct!", "ERROR");
                                                        return;
                                                    }
                                                    new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                                                        public void run() {
                                                            File srcBaseDir = FileUtil.exportReceiveBaseDir(list);
                                                            int cutLength = 0;
                                                            if (srcBaseDir != null) {
                                                                cutLength = srcBaseDir.getAbsolutePath().length();
                                                            }

                                                            StringBuilder errMsg = new StringBuilder();
                                                            int errCount = 0;
                                                            File newFile = null;
                                                            for (File f : list) {
                                                                try {
                                                                    newFile = new File(copyToDir + "/" + f.getAbsolutePath().substring(cutLength), f.getName());
                                                                    newFile.getParentFile().mkdirs();
                                                                    FileUtil.copyFile(f, newFile);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    errCount++;
                                                                    errMsg.append(f + "\n");
                                                                }
                                                            }

                                                            JOptionPaneUtil.newInstance().iconPlainMessage()
                                                                    .showMessageDialog("copy completed!\nerror : " + errCount + "\nerror list : \n" + errMsg, "COMPLETED");
                                                        }
                                                    }, "copySelectedFiles_orignTree_" + hashCode()).start();
                                                }
                                            });

                                            JPopupMenuUtil.newInstance(svnTable).applyEvent(evt).addJMenuItem(copySelectedMeun, copySelectedOringTreeMeun).show();
                                        }

                                        if (!JMouseEventUtil.buttonLeftClick(2, evt)) {
                                            return;
                                        }
                                        int row = JTableUtil.newInstance(svnTable).getSelectedRow();
                                        SvnFile svnFile = (SvnFile) svnTable.getModel().getValueAt(row, SvnTableColumn.SVN_FILE.pos);
                                        String command = String.format("cmd /c call \"%s\"", svnFile.file);
                                        System.out.println(command);
                                        try {
                                            Runtime.getRuntime().exec(command);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }catch(Exception ex){
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                            svnTable.setModel(svnTableModel);
                            JTableUtil.defaultSetting(svnTable);
                        }
                    }
                    {
                        jPanel3 = new JPanel();
                        jPanel1.add(jPanel3, BorderLayout.NORTH);
                        jPanel3.setPreferredSize(new java.awt.Dimension(379, 35));
                        {
                            filterText = new JTextField();
                            jPanel3.add(filterText);
                            filterText.setPreferredSize(new java.awt.Dimension(258, 24));
                            filterText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                public void process(DocumentEvent event) {
                                    try{
                                        String scanText = JCommonUtil.getDocumentText(event);
                                        reloadSvnTable(scanText, _defaultScanProcess);
                                    }catch(Exception ex){
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            }));
                        }
                        {
                            choiceSvnDir = new JButton();
                            jPanel3.add(choiceSvnDir);
                            choiceSvnDir.setText("choice svn dir");
                            choiceSvnDir.setPreferredSize(new java.awt.Dimension(154, 24));
                            choiceSvnDir.addActionListener(new ActionListener() {

                                Pattern svnOutputPattern = Pattern.compile("\\s*(\\d*)\\s+(\\d+)\\s+(\\w+)\\s+([\\S]+)");

                                public void actionPerformed(ActionEvent evt) {
                                    try{
                                        System.out.println("choiceSvnDir.actionPerformed, event=" + evt);
                                        final File svnDir = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                                        if (svnDir == null) {
                                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir is not correct!", "ERROR");
                                            return;
                                        }
                                        Thread thread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                                            public void run() {
                                                long startTime = System.currentTimeMillis();
                                                String command = String.format("cmd /c svn status -v \"%s\"", svnDir);
                                                Matcher matcher = null;
                                                try {
                                                    long projectLastestVersion = 0;
                                                    SvnFile svnFile = null;
                                                    Process process = Runtime.getRuntime().exec(command);
                                                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "BIG5"));
                                                    for (String line = null; (line = reader.readLine()) != null;) {
                                                        matcher = svnOutputPattern.matcher(line);
                                                        if (matcher.find()) {
                                                            try {
                                                                if (StringUtils.isNotBlank(matcher.group(1))) {
                                                                    projectLastestVersion = Math.max(projectLastestVersion, Long.parseLong(matcher.group(1)));
                                                                }
                                                                svnFile = new SvnFile();
                                                                svnFile.lastestVersion = Long.parseLong(matcher.group(2));
                                                                svnFile.author = matcher.group(3);
                                                                svnFile.filePath = matcher.group(4);
                                                                svnFile.file = new File(svnFile.filePath);
                                                                svnFile.fileName = svnFile.file.getName();
                                                                svnFileSet.add(svnFile);
                                                                authorSet.add(svnFile.author);
                                                                String extension = null;
                                                                if (svnFile.file.isFile() && (extension = getExtension(svnFile.fileName)) != null) {
                                                                    fileExtenstionSet.add(extension);
                                                                }
                                                            } catch (Exception ex) {
                                                                ex.printStackTrace();
                                                            }
                                                        } else {
                                                            System.out.println("ignore : " + line);
                                                        }
                                                    }
                                                    reader.close();
                                                    lastestVersion = projectLastestVersion;
                                                    projectName = svnDir.getName();
                                                    resetUiAndShowMessage(startTime, projectName, projectLastestVersion);
                                                } catch (IOException e) {
                                                    JCommonUtil.handleException(e);
                                                }
                                            }
                                        }, "loadSvnLastest_" + this.hashCode());
                                        thread.setDaemon(true);
                                        thread.start();
                                        setTitle("sweeping...");
                                    }catch(Exception ex){
                                        JCommonUtil.handleException(ex);
                                    }
                                }

                                String getExtension(String name) {
                                    int pos = -1;
                                    if ((pos = name.lastIndexOf(".")) != -1) {
                                        return name.substring(pos).toLowerCase();
                                    }
                                    return null;
                                }
                            });
                        }
                        {
                            jLabel1 = new JLabel();
                            jPanel3.add(jLabel1);
                            jLabel1.setText("match :");
                            jLabel1.setPreferredSize(new java.awt.Dimension(56, 22));
                        }
                        {
                            matchCount = new JLabel();
                            jPanel3.add(matchCount);
                            matchCount.setPreferredSize(new java.awt.Dimension(82, 22));
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    FlowLayout jPanel2Layout = new FlowLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("config", null, jPanel2, null);
                    {
                        DefaultComboBoxModel authorComboBoxModel = new DefaultComboBoxModel();
                        authorComboBox = new JComboBox();
                        jPanel2.add(authorComboBox);
                        authorComboBox.setModel(authorComboBoxModel);
                        authorComboBox.setPreferredSize(new java.awt.Dimension(260, 24));
                        authorComboBox.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try{
                                    Object author = authorComboBox.getSelectedItem();
                                    if (author instanceof Map.Entry) {
                                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) author;
                                        reloadSvnTable((String) entry.getKey(), _authorScanProcess);
                                    } else {
                                        reloadSvnTable((String) author, _authorScanProcess);
                                    }
                                }catch(Exception ex){
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                    {
                        ComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
                        fileExtensionComboBox = new JComboBox();
                        jPanel2.add(fileExtensionComboBox);
                        fileExtensionComboBox.setModel(jComboBox1Model);
                        fileExtensionComboBox.setPreferredSize(new java.awt.Dimension(186, 24));
                        fileExtensionComboBox.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try{
                                    String extension = (String) fileExtensionComboBox.getSelectedItem();
                                    reloadSvnTable(extension, _fileExtensionScanProcess);
                                }catch(Exception ex){
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                    {
                        jScrollPane2 = new JScrollPane();
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(130, 200));
                        jPanel2.add(jScrollPane2);
                        {
                            DefaultListModel model = new DefaultListModel();
                            fileExtensionFilter = new JList();
                            jScrollPane2.setViewportView(fileExtensionFilter);
                            fileExtensionFilter.setModel(model);
                            fileExtensionFilter.addListSelectionListener(new ListSelectionListener() {
                                public void valueChanged(ListSelectionEvent evt) {
                                    try{
                                        System.out.println(Arrays.toString(fileExtensionFilter.getSelectedValues()));
                                        String extensionStr = "";
                                        StringBuilder sb = new StringBuilder();
                                        for (Object val : fileExtensionFilter.getSelectedValues()) {
                                            sb.append(val + ",");
                                        }
                                        extensionStr = (sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1) : sb).toString();
                                        System.out.format("extensionStr = [%s]\n", extensionStr);
                                        multiExtendsionFilter.setName(extensionStr);
                                    }catch(Exception ex){
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                        }
                    }
                    {
                        multiExtendsionFilter = new JButton();
                        jPanel2.add(multiExtendsionFilter);
                        multiExtendsionFilter.setText("multi extension filter");
                        multiExtendsionFilter.setPreferredSize(new java.awt.Dimension(166, 34));
                        multiExtendsionFilter.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try{
                                    reloadSvnTable(multiExtendsionFilter.getName(), _fileExtensionMultiScanProcess);
                                }catch(Exception ex){
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                    {
                        loadAuthorReference = new JButton();
                        jPanel2.add(loadAuthorReference);
                        loadAuthorReference.setText("load author reference");
                        loadAuthorReference.setPreferredSize(new java.awt.Dimension(188, 35));
                        loadAuthorReference.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try{
                                    File file = JFileChooserUtil.newInstance().selectFileOnly().showOpenDialog().getApproveSelectedFile();
                                    if (file == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                                        return;
                                    }
                                    authorProps.load(new FileInputStream(file));
                                    reloadAuthorComboBox();
                                }catch(Exception ex){
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                    {
                        saveCurrentData = new JButton();
                        jPanel2.add(saveCurrentData);
                        saveCurrentData.setText("save current data");
                        saveCurrentData.setPreferredSize(new java.awt.Dimension(166, 34));
                        saveCurrentData.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try{
                                    File saveDataConfig = new File(jarExistLocation, getSaveCurrentDataFileName());
                                    ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(saveDataConfig));
                                    writer.writeObject(projectName);
                                    writer.writeObject(authorSet);
                                    writer.writeObject(fileExtenstionSet);
                                    writer.writeObject(svnFileSet);
                                    writer.writeObject(authorProps);
                                    writer.writeObject(lastestVersion);
                                    writer.flush();
                                    writer.close();

                                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("current project : " + projectName + " save completed! \n" + saveDataConfig, "SUCCESS");
                                }catch(Exception ex){
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                    {
                        loadDataFromFile = new JButton();
                        jPanel2.add(loadDataFromFile);
                        loadDataFromFile.setText("load data cfg");
                        loadDataFromFile.setPreferredSize(new java.awt.Dimension(165, 35));
                        loadDataFromFile.addActionListener(new ActionListener() {
                            @SuppressWarnings("unchecked")
                            public void actionPerformed(ActionEvent evt) {
                                try{
                                    File file = JFileChooserUtil.newInstance().selectFileOnly().addAcceptFile(".cfg", ".cfg").showOpenDialog().getApproveSelectedFile();
                                    if (file == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                                        return;
                                    }
                                    long startTime = System.currentTimeMillis();
                                    ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
                                    projectName = (String) input.readObject();
                                    authorSet = (Set<String>) input.readObject();
                                    fileExtenstionSet = (Set<String>) input.readObject();
                                    svnFileSet = (Set<SvnFile>) input.readObject();
                                    authorProps = (Properties) input.readObject();
                                    lastestVersion = (Long) input.readObject();
                                    input.close();

                                    resetUiAndShowMessage(startTime, projectName, lastestVersion);
                                }catch(Exception ex){
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        });
                    }
                }
            }
            pack();
            this.setSize(726, 459);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getSaveCurrentDataFileName() {
        return String.format("%s_%s_%s_data.cfg", //
                SvnLastestCommitInfoUI.class.getSimpleName(), //
                projectName,//
                DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));
    }

    void reloadFileExtensionComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("");
        for (String extension : fileExtenstionSet) {
            model.addElement(extension);
        }
        fileExtensionComboBox.setModel(model);
        fileExtensionFilter.setModel(model);
    }

    void reloadAuthorComboBox() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("");
        for (String author : authorSet) {
            boolean findRef = false;
            for (Map.Entry<?, ?> _entry : authorProps.entrySet()) {
                if (_entry.getKey().equals(author)) {
                    model.addElement(_entry);
                    findRef = true;
                    break;
                }
            }
            if (!findRef) {
                model.addElement(author);
            }
        }
        authorComboBox.setModel(model);
    }

    void resetUiAndShowMessage(long startTime, String projectName, long projectLastestVersion) {
        reloadAuthorComboBox();
        reloadFileExtensionComboBox();
        reloadSvnTable("", _addAllProcess);
        long duringTime = System.currentTimeMillis() - startTime;
        String message = projectName + ",\n Revision : " + projectLastestVersion + ",\n during : " + duringTime + ",\n size : " + svnFileSet.size() + ",\n author : " + authorSet.size();
        setTitle(message);
        if (duringTime > 40000) {
            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(message, "SUCCESS");
        }
    }

    interface ScanProcess {
        void process(String scanText, SvnFile svnFile, DefaultTableModel model);
    }

    final ScanProcess _addAllProcess = new ScanProcess() {
        public void process(String scanText, SvnFile svnFile, DefaultTableModel model) {
            addRow(svnFile, model);
        }
    };

    final ScanProcess _defaultScanProcess = new ScanProcess() {
        public void process(String scanText, SvnFile svnFile, DefaultTableModel model) {
            if (StringUtils.contains(svnFile.filePath.toLowerCase(), scanText.toLowerCase())) {
                addRow(svnFile, model);
                return;
            }
            if (StringUtils.contains(svnFile.author.toLowerCase(), scanText.toLowerCase())) {
                addRow(svnFile, model);
                return;
            }
            if (StringUtils.contains(String.valueOf(svnFile.lastestVersion).toLowerCase(), scanText.toLowerCase())) {
                addRow(svnFile, model);
                return;
            }
        }
    };

    final ScanProcess _authorScanProcess = new ScanProcess() {
        public void process(String scanText, SvnFile svnFile, DefaultTableModel model) {
            if (StringUtils.contains(svnFile.author.toLowerCase(), scanText.toLowerCase())) {
                addRow(svnFile, model);
            }
        }
    };

    final ScanProcess _fileExtensionScanProcess = new ScanProcess() {
        public void process(String scanText, SvnFile svnFile, DefaultTableModel model) {
            if (svnFile.fileName.endsWith(scanText)) {
                addRow(svnFile, model);
            }
        }
    };

    final ScanProcess _fileExtensionMultiScanProcess = new ScanProcess() {
        public void process(String scanText, SvnFile svnFile, DefaultTableModel model) {
            for (String extension : scanText.split(",")) {
                if (svnFile.fileName.endsWith(extension)) {
                    addRow(svnFile, model);
                    return;
                }
            }
        }
    };

    enum SvnTableColumn {
        FILE_NAME(0, "file name", -1), //
        AUTHOR(1, "author", 100), //
        REVISION(2, "revision", 100), //
        LASTMODIFIED(3, "lastModified", 150), //
        LENGTH(4, "length", 50), //
        SVN_FILE(5, "svn file", -1), ;//

        final int pos;
        final String colName;
        final int maxWidth;

        SvnTableColumn(int pos, String colName, int maxWidth) {
            this.pos = pos;
            this.colName = colName;
            this.maxWidth = maxWidth;
        }

        static Object[] getColumn() {
            List<String> list = new ArrayList<String>();
            for (SvnTableColumn s : SvnTableColumn.values()) {
                list.add(s.colName);
            }
            return list.toArray(new String[list.size()]);
        }

        static int[] getMaxWidth() {
            List<Integer> list = new ArrayList<Integer>();
            for (SvnTableColumn s : SvnTableColumn.values()) {
                list.add(s.maxWidth);
            }
            return ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
        }
    }

    void reloadSvnTable(String scanText, ScanProcess scanProcess) {
        boolean needFilterScanText = StringUtils.isNotBlank((scanText = StringUtils.defaultString(scanText)));
        DefaultTableModel model = JTableUtil.createModel(true, SvnTableColumn.getColumn());
        for (SvnFile svnFile : svnFileSet) {
            if (!svnFile.file.exists() || !svnFile.file.isFile()) {
                continue;
            }
            if (needFilterScanText) {
                scanProcess.process(scanText, svnFile, model);
            } else {
                this.addRow(svnFile, model);
            }
        }
        svnTable.setModel(model);
        matchCount.setText(String.valueOf(model.getRowCount()));
        if (model.getRowCount() != 0) {
            JTableUtil.newInstance(svnTable).setMaxWidth(SvnTableColumn.getMaxWidth());
        }
    }

    void addRow(SvnFile svnFile, DefaultTableModel model) {
        model.addRow(new Object[] {//
        svnFile.fileName, //
                svnFile.author, //
                svnFile.lastestVersion, //
                DateFormatUtils.format(svnFile.file.lastModified(), "yyyy/MM/dd HH:mm:ss"), //
                svnFile.file.length() / 1024 + "k",//
                svnFile //
        });
    }

    static String projectName = "";
    static Set<String> authorSet = new TreeSet<String>();
    static Set<String> fileExtenstionSet = new TreeSet<String>();
    static Set<SvnFile> svnFileSet = new TreeSet<SvnFile>();
    static Properties authorProps = new Properties();
    static Long lastestVersion = 0L;

    static final File jarExistLocation;
    static {
        jarExistLocation = PropertiesUtil.getJarCurrentPath(SvnLastestCommitInfoUI.class);
    }

    static class SvnFile implements Comparable<SvnFile>, Serializable {
        private static final long serialVersionUID = 1326802331489780898L;
        File file;
        String fileName;
        String filePath;
        String author;
        long lastestVersion = -1L;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
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
            SvnFile other = (SvnFile) obj;
            if (filePath == null) {
                if (other.filePath != null)
                    return false;
            } else if (!filePath.equals(other.filePath))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return filePath;
        }

        @Override
        public int compareTo(SvnFile o) {
            return fileName.compareTo(o.fileName);
        }
    }
}
