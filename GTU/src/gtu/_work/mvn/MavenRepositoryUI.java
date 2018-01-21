package gtu._work.mvn;

import gtu._work.JarFinder;
import gtu._work.mvn.LoadPomListDependency.DependencyKey;
import gtu._work.mvn.LoadPomListDependency.Pom;
import gtu.collection.ListUtil;
import gtu.date.DateFormatUtil;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtil;
import gtu.runtime.ClipboardUtil;
import gtu.runtime.ProcessWatcher;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


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
public class MavenRepositoryUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTable scanTable;
    private JPanel jPanel5;
    private JScrollPane jScrollPane3;
    private JList scanList2;
    private JTextField scanText2;
    private JPanel jPanel4;
    private JButton copyToDir;
    private JPanel jPanel3;
    private JButton jarFindExecute;
    private JButton deleteOldJarBtn;
    private JButton exportListJar;
    private JButton pomOutputJarDir;
    private JButton clipboardListJar;
    private JPanel jPanel7;
    private JScrollPane jScrollPane5;
    private JTable pomDenpendencyTable;
    private JButton openPom;
    private JPanel jPanel6;
    private JButton resetM2Dir;
    private JScrollPane jScrollPane4;
    private JList jarFindList;
    private JTextField jarFindText;
    private JList scanList;
    private JTextField scanText;
    private JPanel jPanel2;
    private JButton saveCurrentDataBtn;
    private JButton loadConfigDataBtn;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MavenRepositoryUI inst = new MavenRepositoryUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public MavenRepositoryUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            {
            }
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    scanList = new JList();
                    jTabbedPane1.addTab("repository", null, jPanel1, null);
                    {
                        scanText = new JTextField();
                        jPanel1.add(scanText, BorderLayout.NORTH);
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            ListModel scanListModel = new DefaultListModel();
                            jScrollPane1.setViewportView(scanList);
                            scanList.setModel(scanListModel);
                            scanList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    defaultJListClick(scanList, evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("repository only jar", null, jPanel4, null);
                    jPanel4.setPreferredSize(new java.awt.Dimension(520, 298));
                    {
                        scanText2 = new JTextField();
                        jPanel4.add(scanText2, BorderLayout.NORTH);
                    }
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel4.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            scanList2 = new JList();
                            jScrollPane3.setViewportView(scanList2);
                            ListModel scanList2Model = new DefaultListModel();
                            scanList2.setModel(scanList2Model);
                            scanList2.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    defaultJListClick(scanList2, evt);
                                }
                            });
                        }
                    }
                    {
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("jar find", null, jPanel2, null);
                    {
                        jarFindText = new JTextField();
                        jPanel2.add(jarFindText, BorderLayout.NORTH);
                    }
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        {
                            ListModel jarFindListModel = new DefaultListModel();
                            jarFindList = new JList();
                            jScrollPane2.setViewportView(jarFindList);
                            jarFindList.setModel(jarFindListModel);
                            jarFindList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    defaultJListClick(jarFindList, evt);
                                }
                            });
                        }
                    }
                    {
                        jarFindExecute = new JButton();
                        jPanel2.add(jarFindExecute, BorderLayout.SOUTH);
                        jarFindExecute.setText("find");
                        jarFindExecute.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                String searchtext = jarFindText.getText();
                                if (StringUtils.isEmpty(searchtext) || searchtext.length() < 2) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("至少輸入兩個字", "error");
                                    return;
                                }
                                try {
                                    DefaultListModel model = new DefaultListModel();
                                    jarFindList.setModel(model);

                                    searchtext = searchtext.trim();
                                    searchtext = searchtext.replace('/', '.');
                                    searchtext = searchtext.replace('\\', '.');

                                    if (jarfinder == null) {
                                        jarfinder = JarFinder.newInstance();
                                    } else {
                                        jarfinder.clear();
                                    }

                                    jarfinder.pattern(searchtext);

                                    DefaultListModel scanModel = (DefaultListModel) scanList.getModel();
                                    PomFile pomFile = null;
                                    for (int ii = 0; ii < scanModel.getSize(); ii++) {
                                        pomFile = (PomFile) scanModel.getElementAt(ii);
                                        if (pomFile.jarFile == null) {
                                            continue;
                                        }
                                        jarfinder.setDir(pomFile.jarFile);
                                        if (!jarfinder.execute().isEmpty()) {
                                            model.addElement(pomFile);
                                        }
                                        jarfinder.getMap().clear();
                                    }
                                } catch (Exception ex) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(), "error");
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }
                }
                {
                    jPanel5 = new JPanel();
                    BorderLayout jPanel5Layout = new BorderLayout();
                    jTabbedPane1.addTab("detail", null, jPanel5, null);
                    jPanel5.setLayout(jPanel5Layout);
                    {
                        jScrollPane4 = new JScrollPane();
                        jPanel5.add(jScrollPane4, BorderLayout.CENTER);
                        {
                            TableModel scanTableModel = new DefaultTableModel();
                            scanTable = new JTable();
                            BorderLayout scanTableLayout = new BorderLayout();
                            scanTable.setLayout(scanTableLayout);
                            jScrollPane4.setViewportView(scanTable);
                            scanTable.setModel(scanTableModel);
                            JTableUtil.defaultSetting(scanTable);

                            scanTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    tableMouseClicked(scanTable, 0, evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel3 = new JPanel();
                    jTabbedPane1.addTab("config", null, jPanel3, null);
                    GroupLayout jPanel3Layout = new GroupLayout((JComponent) jPanel3);
                    jPanel3.setLayout(jPanel3Layout);
                    {
                        copyToDir = new JButton();
                        copyToDir.setText("set copy to dir");
                        copyToDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                                if (file == null || !file.exists() || !file.isDirectory()) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir is not correct!, set default desktop", "error");
                                    file = FileUtil.DESKTOP_DIR;
                                }
                                copyTo = file;
                                System.out.println("copyTo: " + copyTo);
                            }
                        });
                    }
                    {
                        resetM2Dir = new JButton();
                        resetM2Dir.setText("set .m2 dir");
                        resetM2Dir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                                if (file == null || !file.exists() || !file.isDirectory()) {
                                    showErrorMsg();
                                    repositoryDir = DEFAULT_REPOSITORY_DIR;
                                    reloadRepositoryDir();
                                    return;
                                }
                                File newRepository = new File(file, "repository");
                                File settings = new File(file, "settings.xml");
                                if (settings.exists() && settings.isFile() && newRepository.exists() && newRepository.isDirectory()) {
                                    repositoryDir = newRepository;
                                    reloadRepositoryDir();
                                } else {
                                    showErrorMsg();
                                }
                            }

                            void showErrorMsg() {
                                JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir is not correct!, set default .m2 dir", "error");
                            }
                        });
                    }
                    {
                        saveCurrentDataBtn = new JButton();
                        saveCurrentDataBtn.setText("save current data");
                        saveCurrentDataBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File cfgFile = new File(PropertiesUtil.getJarCurrentPath(MavenRepositoryUI.class), MavenRepositoryUI.class.getSimpleName() + "_"
                                        + DateFormatUtil.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".cfg");
                                try {
                                    ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(cfgFile));
                                    writer.writeObject(pomFileList);
                                    writer.writeObject(pomFileJarList);
                                    writer.writeObject(pomFileMap);
                                    writer.flush();
                                    writer.close();
                                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("save completed!\n" + cfgFile, "SUCCESS");
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }
                    {
                        loadConfigDataBtn = new JButton();
                        loadConfigDataBtn.setText("load config data");
                        loadConfigDataBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File cfgFile = JFileChooserUtil.newInstance().selectFileOnly().addAcceptFile("cfg", ".cfg").showOpenDialog().getApproveSelectedFile();
                                if (cfgFile == null) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!", "ERROR");
                                    return;
                                }
                                try {
                                    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(cfgFile));
                                    pomFileList = (Set<PomFile>) reader.readObject();
                                    pomFileJarList = (Set<PomFile>) reader.readObject();
                                    pomFileMap = (Map<DependencyKey, PomFile>) reader.readObject();
                                    reader.close();
                                    resetUIStatus();
                                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("load completed!\n" + cfgFile, "SUCCESS");
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    ex.printStackTrace();
                                }
                            }
                        });

                    }
                    jPanel3Layout.setHorizontalGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup()
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(loadConfigDataBtn, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(saveCurrentDataBtn, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(copyToDir, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(resetM2Dir, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(getJButton1(), GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(281, Short.MAX_VALUE));
                    jPanel3Layout.setVerticalGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(25, 25)
                        .addComponent(copyToDir, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addGap(22)
                        .addComponent(resetM2Dir, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addGap(24)
                        .addComponent(saveCurrentDataBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addGap(25)
                        .addComponent(loadConfigDataBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addGap(28)
                        .addComponent(getJButton1(), GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(34, Short.MAX_VALUE));
                }
                {
                    jPanel6 = new JPanel();
                    BorderLayout jPanel6Layout = new BorderLayout();
                    jPanel6.setLayout(jPanel6Layout);
                    jTabbedPane1.addTab("pom dency", null, jPanel6, null);
                    {
                        openPom = new JButton();
                        jPanel6.add(openPom, BorderLayout.NORTH);
                        openPom.setText("open");
                        openPom.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectFileAndDirectory().showDialog("選擇pom所在目錄,或pom檔").getApproveSelectedFile();
                                if (file == null) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("file is not correct!!", "ERROR");
                                    return;
                                }
                                List<File> pomList = new ArrayList<File>();
                                if (file.isFile() && (file.getName().endsWith(".xml") || file.getName().endsWith(".pom"))) {
                                    pomList.add(file);
                                } else {
                                    FileUtil.searchFileMatchs(file, "pom.xml", pomList);
                                }
                                Set<PomFile> poms = loadPomList(pomList);
                                resetUIStatus();

                                Map<DependencyKey, PomFile> map = new HashMap<DependencyKey, PomFile>();
                                Set<LoadPomListDependency.DependencyKey> errorSet = new HashSet<LoadPomListDependency.DependencyKey>();
                                for (PomFile p : poms) {
                                    openPomFetchDependency(p.pom, map, errorSet);
                                }

                                PomFile pfile = null;
                                DefaultTableModel model = JTableUtil.createModel(true, "groupId", "artifactId", "jar", "pomFile");
                                for (DependencyKey key : map.keySet()) {
                                    pfile = map.get(key);
                                    model.addRow(new Object[] { pfile.pom.groupId, pfile.pom.artifactId, (pfile.jarFile == null ? "" : pfile.jarFile.getName()), pfile });
                                }
                                for (LoadPomListDependency.DependencyKey key : errorSet) {
                                    model.addRow(new Object[] { key.groupId, key.artifactId, "ERROR" });
                                }
                                pomDenpendencyTable.setModel(model);
                            }
                        });
                    }
                    {
                        jScrollPane5 = new JScrollPane();
                        jPanel6.add(jScrollPane5, BorderLayout.CENTER);
                        {
                            TableModel pomDenpendencyTableModel = new DefaultTableModel();
                            pomDenpendencyTable = new JTable();
                            jScrollPane5.setViewportView(pomDenpendencyTable);
                            pomDenpendencyTable.setModel(pomDenpendencyTableModel);
                            pomDenpendencyTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    tableMouseClicked(pomDenpendencyTable, 3, evt);
                                }
                            });
                            JTableUtil.defaultSetting(pomDenpendencyTable);
                        }
                    }
                    {
                        jPanel7 = new JPanel();
                        FlowLayout jPanel7Layout = new FlowLayout();
                        jPanel7Layout.setAlignOnBaseline(true);
                        jPanel6.add(jPanel7, BorderLayout.SOUTH);
                        jPanel7.setLayout(jPanel7Layout);
                        jPanel7.setPreferredSize(new java.awt.Dimension(520, 36));
                        {
                            clipboardListJar = new JButton();
                            jPanel7.add(clipboardListJar);
                            clipboardListJar.setText("jar list to clipboard");
                            clipboardListJar.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    List<File> list = fetchPomDependencyTableJarList();
                                    StringBuilder sb = new StringBuilder();
                                    for (File f : list) {
                                        sb.append(f + "\n");
                                    }
                                    ClipboardUtil.getInstance().setContents(sb);
                                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("clipboard set ok!", "SUCCESS");
                                }
                            });
                        }
                        {
                            pomOutputJarDir = new JButton();
                            jPanel7.add(pomOutputJarDir);
                            pomOutputJarDir.setText("set output jar dir");
                            pomOutputJarDir.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showDialog("選擇匯出Jar清單目錄").getApproveSelectedFile();
                                    if (file == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("dir is not correct!!", "ERROR");
                                        return;
                                    }
                                    pomOutputJarDir_ = file;
                                }
                            });
                        }
                        {
                            exportListJar = new JButton();
                            jPanel7.add(exportListJar);
                            exportListJar.setText("export list jar");
                            exportListJar.setPreferredSize(new java.awt.Dimension(113, 24));
                            exportListJar.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    List<File> list = fetchPomDependencyTableJarList();
                                    if (pomOutputJarDir_ == null || !pomOutputJarDir_.exists() || !pomOutputJarDir_.isDirectory()) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("output dir is not correct!!", "ERROR");
                                        return;
                                    }
                                    if (JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION == JOptionPaneUtil.newInstance().confirmButtonYesNo().iconWaringMessage()
                                            .showConfirmDialog("are you sure copy list jar count:(" + list.size() + ") to\n" + pomOutputJarDir_, "WARN")) {
                                        StringBuilder sb = new StringBuilder();
                                        StringBuilder fsb = new StringBuilder();
                                        sb.append("total : " + list.size() + "\n");
                                        int ok = 0;
                                        int noOk = 0;
                                        for (File f : list) {
                                            try {
                                                FileUtil.copyFile(f, new File(pomOutputJarDir_, f.getName()));
                                                ok++;
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                noOk++;
                                                fsb.append(f + "\n");
                                            }
                                        }
                                        sb.append("success : " + ok + "\n");
                                        sb.append("failed : " + noOk + "\n");
                                        sb.append("Failed jar :\n");
                                        sb.append(fsb);

                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(sb, "COPY RESULT");
                                    }

                                }
                            });
                        }
                    }
                }
            }
            this.setSize(541, 365);

            reloadRepositoryDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Set<PomFile> pomFileList;//全部的pom不一定有jar
    static Set<PomFile> pomFileJarList;//有對應jar黨的pom
    static Map<DependencyKey, PomFile> pomFileMap;

    static JarFinder jarfinder;
    static File copyTo = FileUtil.DESKTOP_DIR;//single jar copy to
    static File pomOutputJarDir_;//multi jar copy to

    static File repositoryDir;

    final static String dependencyConfig;
    final static File DEFAULT_REPOSITORY_DIR;
    static {
        DEFAULT_REPOSITORY_DIR = new File(System.getProperty("user.home") + "/.m2/repository");
        repositoryDir = DEFAULT_REPOSITORY_DIR;
        System.out.println(repositoryDir + " : " + repositoryDir.exists());

        StringBuilder sb = new StringBuilder();
        sb.append("     <dependency>                                       \n");
        sb.append("         <groupId>%s</groupId>                   \n");
        sb.append("         <artifactId>%s</artifactId>             \n");
        sb.append("     </dependency>                                      \n");
        dependencyConfig = sb.toString();
    }

    DocumentListener getDocumentListener(final JList list, final Set<PomFile> pomfilelist) {
        return JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            public void process(DocumentEvent event) {
                if (pomfilelist == null || pomfilelist.isEmpty()) {
                    return;
                }
                String text = JCommonUtil.getDocumentText(event);
                DefaultListModel model = new DefaultListModel();
                for (PomFile pomfile : pomfilelist) {
                    if (pomfile.xmlFile.getName().contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.modelVersion).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.groupId).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.artifactId).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.packaging).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.name).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.version).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.url).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                    if (StringUtils.defaultString(pomfile.pom.description).contains(text)) {
                        model.addElement(pomfile);
                        continue;
                    }
                }
                list.setModel(model);
            }
        });
    }

    void openPomFetchDependency(Pom pom, Map<LoadPomListDependency.DependencyKey, PomFile> model, Set<LoadPomListDependency.DependencyKey> errorSet) {
        DependencyKey key = null;
        PomFile pomFile = null;
        if (pom.dependencies != null && !pom.dependencies.isEmpty()) {
            for (LoadPomListDependency.Dependency depend : pom.dependencies) {
                key = new LoadPomListDependency.DependencyKey(depend.groupId, depend.artifactId);
                if (model.containsKey(key)) {
                    continue;
                }
                pomFile = pomFileMap.get(key);
                if (pomFile == null) {
                    errorSet.add(new DependencyKey(depend.groupId, depend.artifactId));
                    continue;
                }
                model.put(key, pomFile);
                openPomFetchDependency(pomFile.pom, model, errorSet);
            }
        }
        if (pom.parent != null) {
            openPomFetchDependency(pom.parent, model, errorSet);
        }
    }

    List<File> fetchPomDependencyTableJarList() {
        TableModel model = pomDenpendencyTable.getModel();
        PomFile pomFile = null;
        List<File> list = new ArrayList<File>();
        for (int ii = 0; ii < model.getRowCount(); ii++) {
            pomFile = (PomFile) model.getValueAt(ii, 3);
            if (pomFile != null && pomFile.jarFile != null && !list.contains(pomFile.jarFile)) {
                list.add(pomFile.jarFile);
            }
        }
        Collections.sort(list);
        return list;
    }

    void defaultJListClick(final JList jList, MouseEvent evt) {
        if (jList.getLeadSelectionIndex() == -1) {
            return;
        }

        //多選
        if (jList.getSelectedValues().length > 1) {
            defaultPopupMenu(ListUtil.getList(jList.getSelectedValues(), PomFile.class), jList, evt);
            return;
        }

        //單選
        final PomFile pomFile = (PomFile) JListUtil.getLeadSelectionObject(jList);
        this.defaultPopupMenu(Arrays.asList(pomFile), jList, evt);
        if (JListUtil.newInstance(jList).isCorrectMouseClick(evt)) {
            this.showPomInfo(pomFile);
        }
    }

    void defaultPopupMenu(final List<PomFile> pomFileList, Component component, MouseEvent evt) {
        if (pomFileList == null || pomFileList.isEmpty()) {
            return;
        }
        final PomFile pomFile = pomFileList.size() == 1 ? pomFileList.get(0) : null;

        if (pomFileList.size() > 0) {
            final JMenuItem copyAllMenu = new JMenuItem();
            {
                final Set<File> jarList = new HashSet<File>();
                final StringBuilder sb = new StringBuilder();
                for (PomFile _pomFile : pomFileList) {
                    if (_pomFile.jarFile != null) {
                        sb.append(_pomFile.jarFile.getName() + "\n");
                        jarList.add(_pomFile.jarFile);
                    }
                }
                if (sb.length() > 200) {
                    sb.delete(200, sb.length() - 1);
                    sb.append("...etc");
                }

                final String title = "COPY SELECTED JAR : " + jarList.size();
                copyAllMenu.setText(title);
                copyAllMenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        System.out.println("# copyMenu action ...");
                        JOptionPaneUtil.ComfirmDialogResult result = JOptionPaneUtil.newInstance().confirmButtonYesNoCancel().showConfirmDialog(//
                                "are you sure copy file : \n" + sb + "\tto \n" + copyTo + "\n\t???", title);
                        if (result != JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION) {
                            return;
                        }

                        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                            public void run() {
                                long startTime = System.currentTimeMillis();
                                File copyToFile = null;
                                int successCount = 0;
                                int errorCount = 0;
                                StringBuilder err = new StringBuilder();
                                for (File jar : jarList) {
                                    copyToFile = new File(copyTo, jar.getName());
                                    try {
                                        FileUtil.copyFile(jar, copyToFile);
                                        successCount++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        errorCount++;
                                        err.append(jar + "\n");
                                    }
                                }
                                JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(//
                                        "copy completed!! \n" + //
                                                "during : " + (System.currentTimeMillis() - startTime) + "\n" + "success : " + successCount + "\n" + //
                                                "fail : " + errorCount + "\n" + //
                                                "fail list : \n" + err, "COMPLETED");
                            }
                        }, "copy all jar to target " + hashCode()).start();
                    }
                });
            }

            final JMenuItem copyAllNewMenu = new JMenuItem();
            {
                final Set<File> jarList = new HashSet<File>();
                final StringBuilder sb = new StringBuilder();
                DependencyKey depenKey = null;
                PomFile _newPomFile = null;
                for (PomFile _pomFile : pomFileList) {
                    if (_pomFile.jarFile != null) {
                        depenKey = new DependencyKey(_pomFile.pom.groupId, _pomFile.pom.artifactId);
                        if (pomFileMap.containsKey(depenKey)) {
                            _newPomFile = pomFileMap.get(depenKey);
                            System.out.println("find new success: " + _pomFile + " --> " + _newPomFile);
                            _pomFile = _newPomFile;
                        } else {
                            System.out.println("find new faild!!: " + _pomFile);
                        }
                        if (_pomFile.jarFile != null) {
                            sb.append(_pomFile.jarFile.getName() + "\n");
                            jarList.add(_pomFile.jarFile);
                        }
                    }
                }
                if (sb.length() > 200) {
                    sb.delete(200, sb.length() - 1);
                    sb.append("...etc");
                }

                final String title = "COPY SELECTED JAR(newest) : " + jarList.size();
                copyAllNewMenu.setText(title);
                copyAllNewMenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        System.out.println("# copyMenu action ...");
                        JOptionPaneUtil.ComfirmDialogResult result = JOptionPaneUtil.newInstance().confirmButtonYesNoCancel().showConfirmDialog(//
                                "are you sure copy file : \n" + sb + "\tto \n" + copyTo + "\n\t???", title);
                        if (result != JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION) {
                            return;
                        }

                        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                            public void run() {
                                long startTime = System.currentTimeMillis();
                                File copyToFile = null;
                                int successCount = 0;
                                int errorCount = 0;
                                StringBuilder err = new StringBuilder();
                                for (File jar : jarList) {
                                    copyToFile = new File(copyTo, jar.getName());
                                    try {
                                        FileUtil.copyFile(jar, copyToFile);
                                        successCount++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        errorCount++;
                                        err.append(jar + "\n");
                                    }
                                }
                                JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(//
                                        "copy completed!! \n" + //
                                                "during : " + (System.currentTimeMillis() - startTime) + "\n" + "success : " + successCount + "\n" + //
                                                "fail : " + errorCount + "\n" + //
                                                "fail list : \n" + err, "COMPLETED");
                            }
                        }, "copy all jar to target " + hashCode()).start();
                    }
                });
            }

            JPopupMenuUtil.newInstance(component).applyEvent(evt).addJMenuItem(copyAllMenu, copyAllNewMenu).show();
        }

        if (pomFile != null) {
            final JMenuItem copyMenu = new JMenuItem();
            {
                if (pomFile.jarFile == null) {
                    copyMenu.setText("COPY : no jar file");
                    copyMenu.setEnabled(false);
                } else if (copyTo == null) {
                    copyMenu.setText("COPY : copy to not set");
                    copyMenu.setEnabled(false);
                } else {
                    copyMenu.setText("COPY : " + this.getJarName(pomFile));
                    copyMenu.setEnabled(true);
                }
                copyMenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        System.out.println("# copyMenu action ...");
                        File copyToFile = new File(copyTo, pomFile.jarFile.getName());
                        JOptionPaneUtil.ComfirmDialogResult result = JOptionPaneUtil.newInstance().confirmButtonYesNoCancel().showConfirmDialog(//
                                "are you sure copy file : \n" + //
                                        pomFile.jarFile.getParent() + "\nto\n" + //
                                        copyToFile.getParent() + "\n" + //
                                        "\t???", getJarName(pomFile));
                        if (result == JOptionPaneUtil.ComfirmDialogResult.YES_OK_OPTION) {
                            try {
                                FileUtil.copyFile(pomFile.jarFile, copyToFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                                JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(e.toString(), "ERROR");
                            }
                            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("copy success!!", "SUCCESS");
                        }
                    }
                });
            }

            final JMenuItem updateSnapshotMenu = new JMenuItem();
            {
                updateSnapshotMenu.setText("update snapshot");
                updateSnapshotMenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                            public void run() {
                                try {
                                    String command = String.format("cmd /c mvn -U -f \"%s\"", pomFile.xmlFile);
                                    System.out.println(command);
                                    final ProcessWatcher watcher = ProcessWatcher.newInstance(Runtime.getRuntime().exec("cmd /c @echo TODO!!"));
                                    //TODO
                                    //FIXME
                                    //XXX
                                    JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("TODO", "MAVEN UPDATE");
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        }, "execute Maven update").start();
                    }
                });
            }

            final JMenuItem jdJarMenu = new JMenuItem();
            {
                jdJarMenu.setText("jd-gui");
                if (pomFile.jarFile == null) {
                    jdJarMenu.setEnabled(false);
                }
                jdJarMenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        try {
                            Runtime.getRuntime().exec(String.format("cmd /c call \"%s\" \"%s\"", //
                                    "C:/apps/jd-gui-0.3.1.windows/jd-gui.exe", pomFile.jarFile));
                        } catch (IOException ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
            }
            final JMenuItem openJarDirMenu = new JMenuItem();
            {
                openJarDirMenu.setText("open dir");
                openJarDirMenu.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        try {
                            Desktop.getDesktop().open(pomFile.xmlFile.getParentFile());
                        } catch (IOException ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
            }
            final JMenuItem showDepedencyMessage = new JMenuItem();
            {
                showDepedencyMessage.setText("show dependency");
                showDepedencyMessage.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent paramActionEvent) {
                        clipboardPomJarConfig(pomFile, true);
                    }
                });
            }

            JPopupMenuUtil.newInstance(component).applyEvent(evt).addJMenuItem(copyMenu, updateSnapshotMenu, jdJarMenu, openJarDirMenu, showDepedencyMessage).show();
        }
    }
    
    private void clipboardPomJarConfig(PomFile pomFile, boolean isOpen){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("             <dependency>                                   \n");
            sb.append("                     <!-- {0} -->                           \n");
            sb.append("                     <groupId>{1}</groupId>                 \n");
            sb.append("                     <artifactId>{2}</artifactId>           \n");
            sb.append("                     <version>{3}</version>                 \n");
            sb.append("             </dependency>                                  \n");
            
            String jarName = pomFile.jarFile == null ? "" : pomFile.jarFile.getName();
            Pom pom = pomFile.pom;
            String groupId = pom.groupId;
            String artifactId = pom.artifactId;
            String version = pom.version;
            {
                Pom tmpPom = pom;
                while (groupId == null && tmpPom != null) {
                    tmpPom = pom.parent;
                    groupId = tmpPom.groupId;
                }
            }
            {
                Pom tmpPom = pom;
                while (artifactId == null && tmpPom != null) {
                    tmpPom = pom.parent;
                    artifactId = tmpPom.artifactId;
                }
            }
            {
                Pom tmpPom = pom;
                while (version == null && tmpPom != null) {
                    tmpPom = pom.parent;
                    version = tmpPom.version;
                }
            }
            groupId = StringUtils.defaultString(groupId);
            artifactId = StringUtils.defaultString(artifactId);
            version = StringUtils.defaultString(version);
            
            String pomMessage = MessageFormat.format(sb.toString(), jarName, groupId, artifactId, version);
            if(isOpen){
                JCommonUtil._jOptionPane_showMessageDialog_info(pomMessage);
            }
            ClipboardUtil.getInstance().setContents(pomMessage);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    void showPomInfo(PomFile pomFile) {
        String dependencyConfigStr = String.format(dependencyConfig, pomFile.pom.groupId, pomFile.pom.artifactId);
        ClipboardUtil.getInstance().setContents(dependencyConfigStr);
        String message = pomFile.pom.toString().replace(',', '\n');
        String fileName = pomFile.xmlFile.getName();
        if (pomFile.jarFile != null) {
            fileName = getJarName(pomFile);
        }
        JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(message, fileName);
        clipboardPomJarConfig(pomFile, false);
    }

    String getJarName(PomFile pomFile) {
        return (pomFile.jarFile == null ? "NA" : pomFile.jarFile.getName());
    }

    void reloadRepositoryDir() {
        if (!repositoryDir.exists()) {
            setTitle("repository not exist! : " + repositoryDir);
            return;
        }

        Thread initThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
            public void run() {
                long startTime = System.currentTimeMillis();
                setTitle("loading... " + repositoryDir);

                List<File> pomList = new ArrayList<File>();
                FileUtil.searchFileMatchs(repositoryDir, ".*\\.pom", pomList);

                pomFileList = new HashSet<PomFile>();
                pomFileJarList = new HashSet<PomFile>();
                pomFileMap = new HashMap<DependencyKey, PomFile>();

                loadPomList(pomList);

                long endTime = System.currentTimeMillis() - startTime;
                String message = "load completed! \ntime:" + endTime + ", poms:" + pomFileList.size() + ", jars:" + pomFileJarList.size();
                setTitle(message);
                JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog(message, "SCAN COMPLETED");

                resetUIStatus();
            }
        }, "reloadRepositoryDir");
        initThread.setDaemon(true);
        initThread.start();
    }

    Set<PomFile> loadPomList(List<File> pomList) {
        Set<PomFile> pomFileListInner = new HashSet<PomFile>();
        Set<PomFile> pomFileJarListInner = new HashSet<PomFile>();
        Map<DependencyKey, PomFile> pomFileMapInner = new HashMap<DependencyKey, PomFile>();

        for (File f : pomList) {
            try {
                Document doc = new SAXReader().read(f);
                this.addPom(doc, f, pomFileListInner, pomFileJarListInner, pomFileMapInner);
            } catch (Exception e) {
                try{
                    //替換掉特殊字元 &xxxx; 會造成錯誤
                    String xmlStr = FileUtils.readFileToString(f, "utf8");
                    xmlStr = xmlStr.replaceAll("\\&[\\w\\(\\)\\#]*\\;", "");
                    Document doc = new SAXReader().read(new StringReader(xmlStr));
                    this.addPom(doc, f, pomFileListInner, pomFileJarListInner, pomFileMapInner);
                }catch(Exception ex){
                    JCommonUtil.handleException(ex);
                }
            }
        }
        pomFileList.addAll(pomFileListInner);
        pomFileJarList.addAll(pomFileJarListInner);
        pomFileMap.putAll(pomFileMapInner);
        return pomFileListInner;
    }
    
    private void addPom(Document doc, File f, Set<PomFile> pomFileListInner, Set<PomFile> pomFileJarListInner, Map<DependencyKey, PomFile> pomFileMapInner) throws DocumentException{
        Element root = (Element) doc.selectSingleNode("*");
        PomFile pomFile = new PomFile();
        pomFile.pom = new Pom(root);
        pomFile.xmlFile = f;
        File jarFile = new File(f.getParent(), f.getName().replaceAll(".pom", ".jar"));
        if (jarFile.exists()) {
            pomFile.jarFile = jarFile;
        }
        pomFileListInner.add(pomFile);
        if (pomFile.jarFile != null) {
            pomFileJarListInner.add(pomFile);
        }
        DependencyKey key = new DependencyKey(pomFile.pom.groupId, pomFile.pom.artifactId);
        if (pomFileMapInner.containsKey(key)) {
            if (pomFileMapInner.get(key).xmlFile.lastModified() < pomFile.xmlFile.lastModified()) {
                PomFile tempPomFile = pomFileMapInner.get(key);
                pomFileMapInner.put(key, pomFile);
                System.out.println("duplicate : " + key + " ==> " + tempPomFile.xmlFile.getName() + " ==> " + pomFile.xmlFile.getName());
            }
        } else {
            pomFileMapInner.put(key, pomFile);
        }
    }

    void resetUIStatus() {
        {
            DefaultListModel model = new DefaultListModel();
            for (PomFile pom : pomFileList) {
                model.addElement(pom);
            }
            scanList.setModel(model);
        }
        {
            DefaultListModel model = new DefaultListModel();
            for (PomFile pom : pomFileJarList) {
                model.addElement(pom);
            }
            scanList2.setModel(model);
        }
        {
            Object[] header = new Object[] { "pom", "lastModified", "jar", "parent", "modelVersion", " groupId", " artifactId", " packaging", " name", " version", " url", " description" };
            DefaultTableModel model = new DefaultTableModel(new Object[][] {}, header) {
                private static final long serialVersionUID = 1L;

                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (PomFile pom : pomFileList) {
                model.addRow(new Object[] {//
                pom,//
                        DateFormatUtils.format(pom.xmlFile.lastModified(), "yyyy/MM/dd HH:mm:ss"),//
                        (pom.jarFile == null ? "" : DateFormatUtils.format(pom.jarFile.lastModified(), "yyyy/MM/dd HH:mm:ss") + "(" + (pom.jarFile.length() / 1024) + ")"), //
                        (pom.pom.parent == null ? "" : "Y"),//
                        pom.pom.modelVersion, //
                        pom.pom.groupId, //
                        pom.pom.artifactId, //
                        pom.pom.packaging, //
                        pom.pom.name, //
                        pom.pom.version, //
                        pom.pom.url, //
                        pom.pom.description //
                });
            }
            scanTable.setModel(model);
        }

        scanText.getDocument().addDocumentListener(getDocumentListener(scanList, pomFileList));
        scanText2.getDocument().addDocumentListener(getDocumentListener(scanList2, pomFileJarList));
    }

    private void tableMouseClicked(JTable table, int pomFileColPos, MouseEvent evt) {
        //多選
        if (table.getSelectedRowCount() > 1) {
            int realRowPos = -1;
            PomFile pomFile = null;
            List<PomFile> list = new ArrayList<PomFile>();
            for (int rowPos : table.getSelectedRows()) {
                realRowPos = table.getRowSorter().convertRowIndexToModel(rowPos);
                pomFile = (PomFile) table.getModel().getValueAt(realRowPos, pomFileColPos);
                list.add(pomFile);
            }
            defaultPopupMenu(list, table, evt);
            return;
        }

        //單選
        Object selectValue = JTableUtil.newInstance(table).getSelectedValue();
        int rowIndex = JTableUtil.newInstance(table).getSelectedRow();
        PomFile pomFile = (PomFile) table.getModel().getValueAt(rowIndex, pomFileColPos);
        ClipboardUtil.getInstance().setContents(selectValue);
        if (JMouseEventUtil.buttonRightClick(1, evt)) {
            this.defaultPopupMenu(Arrays.asList(pomFile), table, evt);
        }
        if (JMouseEventUtil.buttonLeftClick(2, evt)) {
            this.showPomInfo(pomFile);
        }
    }
    
    private JButton getJButton1() {
        if(deleteOldJarBtn == null) {
            deleteOldJarBtn = new JButton();
            deleteOldJarBtn.setText("delete old jarfile");
            deleteOldJarBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        int count = 0;
                        int count1 = 0;
                        List<PomFile> list = new ArrayList<PomFile>(pomFileJarList);
                        Collections.sort(list, new Comparator<PomFile>() {
                            @Override
                            public int compare(PomFile o1, PomFile o2) {
                                return o1.jarFile.compareTo(o2.jarFile);
                            }
                        });
                        Pattern pattern = Pattern.compile("^.*\\-[\\d]{8}\\.[\\d]{6}\\-\\d+\\.jar$");
                        for(PomFile f : list){
                            if(pattern.matcher(f.jarFile.getName()).matches()){
                                System.out.println(f.jarFile);
                                if(f.jarFile.delete()){
                                    count ++;
                                }
                                if(f.xmlFile.delete()){
                                    count1 ++;
                                }
                            }
                        }
                        JCommonUtil._jOptionPane_showMessageDialog_info("刪除舊的非snapshot的jar檔\njar檔案數:" + count+"\npom檔:" + count1);
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                        ex.printStackTrace();
                    }
                }
            });
        }
        return deleteOldJarBtn;
    }

    static class PomFile implements Serializable {
        private static final long serialVersionUID = -7626618107381194112L;
        Pom pom;
        File xmlFile;
        File jarFile;

        @Override
        public String toString() {
            return xmlFile != null ? xmlFile.getName() : (pom != null ? pom.toString() : "ERROR");
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((xmlFile == null) ? 0 : xmlFile.hashCode());
            result = prime * result + ((jarFile == null) ? 0 : jarFile.hashCode());
            result = prime * result + ((pom == null) ? 0 : pom.hashCode());
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
            PomFile other = (PomFile) obj;
            if (xmlFile == null) {
                if (other.xmlFile != null)
                    return false;
            } else if (!xmlFile.equals(other.xmlFile))
                return false;
            if (jarFile == null) {
                if (other.jarFile != null)
                    return false;
            } else if (!jarFile.equals(other.jarFile))
                return false;
            if (pom == null) {
                if (other.pom != null)
                    return false;
            } else if (!pom.equals(other.pom))
                return false;
            return true;
        }
    }
}
