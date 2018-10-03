package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.JarFinder;
import gtu._work.JarFinder.IfMatch;
import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFileChooserUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JOptionPaneUtil.ComfirmDialogResult;

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
public class JarFinderUI extends javax.swing.JFrame {

    private PropertiesUtilBean configBean = new PropertiesUtilBean(JarFinderUI.class);

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JarFinderUI inst = new JarFinderUI();
                inst.setLocationRelativeTo(null);
                gtu.swing.util.JFrameUtil.setVisible(true, inst);
            }
        });
    }

    public JarFinderUI() {
        super();
        initGUI();
    }

    Properties prop = new Properties();

    private void initGUI() {
        try {

            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setTitle("Jar finder");
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("src dir", null, jPanel1, null);
                    {
                        openDir = new JButton();
                        jPanel1.add(openDir, BorderLayout.NORTH);
                        openDir.setText("open dir");
                        openDir.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                            }
                        });
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(406, 255));
                        {
                            DefaultListModel jList1Model = new DefaultListModel();
                            try {
                                prop.load(new FileInputStream(CONFIG_FILE));
                                for (Enumeration<?> enu = prop.keys(); enu.hasMoreElements();) {
                                    File val = new File((String) enu.nextElement());
                                    jList1Model.addElement(val);
                                }
                            } catch (Exception ex) {
                                JCommonUtil.handleException("找不到參數檔:" + CONFIG_FILE, ex);
                            }
                            jarFileDirs = new JList();
                            jScrollPane1.setViewportView(jarFileDirs);
                            jarFileDirs.setModel(jList1Model);
                            jarFileDirs.setPreferredSize(new java.awt.Dimension(406, 221));
                            jarFileDirs.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    if (!JListUtil.newInstance(jarFileDirs).isCorrectMouseClick(evt)) {
                                        return;
                                    }
                                    File file = (File) JListUtil.getLeadSelectionObject(jarFileDirs);
                                    try {
                                        Desktop.getDesktop().open(file);
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                }
                            });
                            jarFileDirs.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(jarFileDirs).defaultJListKeyPressed(evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("result", null, jPanel2, null);
                    {
                        jPanel3 = new JPanel();
                        BorderLayout jPanel3Layout = new BorderLayout();
                        jPanel3.setLayout(jPanel3Layout);
                        jPanel2.add(jPanel3, BorderLayout.NORTH);
                        jPanel3.setPreferredSize(new java.awt.Dimension(406, 26));
                        {
                            searchText = new JTextField();
                            jPanel3.add(searchText, BorderLayout.WEST);
                            searchText.setPreferredSize(new java.awt.Dimension(326, 26));
                        }
                        {
                            search = new JButton();
                            jPanel3.add(search, BorderLayout.CENTER);
                            search.setText("search");
                            search.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    jButton2ActionPerformed(evt);
                                }
                            });
                        }
                    }
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel2.add(jScrollPane2, BorderLayout.CENTER);
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(406, 231));
                        {
                            ListModel searchResultModel = new DefaultListModel();
                            searchResult = new JList();
                            jScrollPane2.setViewportView(searchResult);
                            searchResult.setModel(searchResultModel);
                            searchResult.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    jList1MouseClicked(evt);
                                }
                            });
                            searchResult.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(searchResult).defaultJListKeyPressed(evt);
                                }
                            });
                        }
                    }
                    {
                        resetFinder = new JButton();
                        jPanel2.add(resetFinder, BorderLayout.SOUTH);
                        resetFinder.setText("reset finder");
                        resetFinder.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                jarfinder.clear();
                            }
                        });
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("copy to", null, jPanel4, null);
                    {
                        copyToBtn = new JButton();
                        jPanel4.add(copyToBtn, BorderLayout.NORTH);
                        copyToBtn.setText("copy to");
                        copyToBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
                                if (file == null) {
                                    JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("copy to dir undefined!", getTitle());
                                    return;
                                } else {
                                    copyToFile = file;
                                }
                            }
                        });
                    }
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel4.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            DefaultListModel copyToListModel = new DefaultListModel();
                            copyToList = new JList();
                            jScrollPane3.setViewportView(copyToList);
                            copyToList.setModel(copyToListModel);
                            {
                                panel = new JPanel();
                                jTabbedPane1.addTab("config", null, panel, null);
                                panel.setLayout(new FormLayout(
                                        new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));
                                {
                                    lblNewLabel = new JLabel("JD Gui");
                                    panel.add(lblNewLabel, "2, 2, right, default");
                                }
                                {
                                    jdGuiText = new JTextField();
                                    JCommonUtil.jTextFieldSetFilePathMouseEvent(jdGuiText, false);
                                    panel.add(jdGuiText, "4, 2, fill, default");
                                    jdGuiText.setColumns(10);
                                }
                                {
                                    saveConfigBtn = new JButton("儲存設定");
                                    saveConfigBtn.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                configBean.reflectSetConfig(JarFinderUI.this);
                                                configBean.store();
                                                JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
                                            } catch (Exception ex) {
                                                JCommonUtil.handleException(ex);
                                            }
                                        }
                                    });
                                    panel.add(saveConfigBtn, "2, 24");
                                }
                            }
                            copyToList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    // copyToFile
                                    if (evt.getClickCount() != 2) {
                                        return;
                                    }
                                    if (copyToList.getLeadSelectionIndex() == -1) {
                                        return;
                                    }
                                    if (copyToFile == null) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("copy to dir undefined!", getTitle());
                                        return;
                                    }
                                    DefaultListModel model = (DefaultListModel) copyToList.getModel();
                                    Object val = model.getElementAt(copyToList.getLeadSelectionIndex());
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("是否複製檔案\n");
                                    sb.append("file : " + val + "\n");
                                    sb.append("copy to dir : " + copyToFile + "\n");
                                    ComfirmDialogResult result = JOptionPaneUtil.newInstance().confirmButtonYesNo().showConfirmDialog(sb, getTitle());
                                    File srcFile = new File((String) val);
                                    if (!srcFile.exists()) {
                                        JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(srcFile + "  not found!", getTitle());
                                        return;
                                    }
                                    File copyDestFile = new File(copyToFile, srcFile.getName());
                                    switch (result) {
                                    case YES_OK_OPTION:
                                        System.out.println("yes..");
                                        try {
                                            FileUtil.copyFile(srcFile, copyDestFile);
                                        } catch (IOException e) {
                                            JCommonUtil.handleException(e.toString(), e);
                                        }
                                        if (srcFile != null && //
                                        copyDestFile != null && //
                                        srcFile.length() == copyDestFile.length()) {
                                            JOptionPaneUtil.newInstance().iconInformationMessage().showMessageDialog("success!\n" + copyDestFile, getTitle());
                                        } else {
                                            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("failed!\n" + copyDestFile, getTitle());
                                        }
                                        break;
                                    case NO_OPTION:
                                        System.out.println("no..");
                                        break;
                                    }
                                }
                            });
                        }
                    }
                }

                jTabbedPane1.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent evt) {
                        copyToList.setModel(searchResult.getModel());
                    }
                });
            }
            this.setSize(562, 407);

            JCommonUtil.frameCloseDo(this, new WindowAdapter() {
                public void windowClosing(WindowEvent paramWindowEvent) {
                    if (CONFIG_FILE.exists()) {
                        DefaultListModel model = (DefaultListModel) jarFileDirs.getModel();
                        for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                            Object obj = enu.nextElement();
                            prop.setProperty(((File) obj).getAbsolutePath(), "");
                        }
                        try {
                            prop.store(new FileOutputStream(CONFIG_FILE), "testtesttesttest");
                        } catch (Exception e) {
                            JCommonUtil.handleException("", e);
                        }
                    }
                    setVisible(false);
                    dispose();
                }
            });

            {
                configBean.reflectInit(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel jPanel1;
    private JList searchResult;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton search;
    private JTextField searchText;
    private JPanel jPanel3;
    private JList jarFileDirs;
    private JButton openDir;
    private JPanel jPanel2;
    private JTabbedPane jTabbedPane1;

    File copyToFile;

    private static File CONFIG_FILE;
    private JScrollPane jScrollPane3;
    private JButton resetFinder;
    private JList copyToList;
    private JButton copyToBtn;
    private JPanel jPanel4;
    static {
        CONFIG_FILE = new File(PropertiesUtil.getJarCurrentPath(JarFinderUI.class), JarFinderUI.class.getSimpleName() + ".properties");
        if (!CONFIG_FILE.exists()) {
            try {
                CONFIG_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    JarFinder jarfinder = JarFinder.newInstance().ifMatchEvent(new IfMatch() {
        public void apply(String jarFile, String targetName, Map<String, Collection<String>> matchMap) {
            List<String> list = new ArrayList<String>(matchMap.keySet());
            Collections.sort(list);
            DefaultListModel dl = new DefaultListModel();
            for (Iterator<String> it = list.iterator(); it.hasNext();) {
                String key = it.next();
                dl.addElement(key);
            }
            searchResult.setModel(dl);
        }
    });
    private JPanel panel;
    private JLabel lblNewLabel;
    private JTextField jdGuiText;
    private JButton saveConfigBtn;

    // 目錄
    private void jButton1ActionPerformed(ActionEvent evt) {
        File file = JFileChooserUtil.newInstance().selectDirectoryOnly().showOpenDialog().getApproveSelectedFile();
        if (file != null) {
            DefaultListModel model = (DefaultListModel) jarFileDirs.getModel();
            model.addElement(file);
        }
    }

    // 尋找
    private void jButton2ActionPerformed(ActionEvent evt) {
        String searchtext = searchText.getText();
        if (StringUtils.isEmpty(searchtext) || searchtext.length() < 2) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog("至少輸入兩個字", "error");
            return;
        }
        try {
            DefaultListModel model = (DefaultListModel) jarFileDirs.getModel();

            searchtext = searchtext.replace('/', '.');
            searchtext = searchtext.replace('\\', '.');

            jarfinder.pattern(searchtext);

            for (Enumeration<?> enu = model.elements(); enu.hasMoreElements();) {
                File file = (File) enu.nextElement();
                jarfinder.setDir(file).execute();
            }
        } catch (Exception ex) {
            // JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(),
            // "error");
            JCommonUtil.handleException(ex);
        }
    }

    // 尋找結果清單
    private void jList1MouseClicked(MouseEvent evt) {
        if (evt.getClickCount() != 2) {
            return;
        }

        try {
            JList list = (JList) evt.getSource();

            StringBuilder sb = new StringBuilder("按'是'開啟jar檔\r\n按'否'開啟所在目錄\r\n");
            String fileName = (String) list.getSelectedValue();

            ClipboardUtil.getInstance().setContents(fileName);

            int linecount = 0;
            for (String clz : jarfinder.getMap().get(fileName)) {
                if (linecount == 10) {
                    sb.append("...以下省略");
                    break;
                }
                sb.append(clz + "\r\n");
                linecount++;
            }

            ComfirmDialogResult result = JOptionPaneUtil.newInstance().iconInformationMessage().confirmButtonYesNoCancel().showConfirmDialog(sb, fileName);

            String jdGuiExe = StringUtils.trimToEmpty(jdGuiText.getText());
            if (result == ComfirmDialogResult.YES_OK_OPTION) {
                if (OsInfoUtil.isWindows()) {
                    RuntimeBatPromptModeUtil.newInstance().command(String.format("cmd /c call \"%s\" \"%s\"", jdGuiExe, fileName)).apply();
                } else {
                    RuntimeBatPromptModeUtil.newInstance().command(String.format("java -jar \"%s\" \"%s\"", jdGuiExe, fileName)).apply();
                }
            }

            if (result == ComfirmDialogResult.NO_OPTION) {
                DesktopUtil.openDir(new File(fileName).getParentFile());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            // JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(),
            // "error");
            JCommonUtil.handleException(ex);
        }
    }
}
