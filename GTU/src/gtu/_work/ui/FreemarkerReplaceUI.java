package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.json.JSONObject2CollectionUtil2;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import net.sf.json.JSONObject;

public class FreemarkerReplaceUI extends JFrame {

    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JTextArea jsonArea;
    private JTextField filePathText;
    private JList filePathList;
    private JList recentFileList;
    private PropertiesUtilBean config = new PropertiesUtilBean(FreemarkerReplaceUI.class);
    private AtomicReference<File> keepFile = new AtomicReference<File>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(FreemarkerReplaceUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FreemarkerReplaceUI frame = new FreemarkerReplaceUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FreemarkerReplaceUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 558, 436);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("JSON", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.NORTH);

        JPanel panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.WEST);

        JPanel panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.SOUTH);

        JButton executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JSONObject jsonObj = JSONObject.fromObject(StringUtils.trimToEmpty(jsonArea.getText()));
                    Map<String, Object> root = JSONObject2CollectionUtil2.jsonToMap(jsonObj);

                    File dir = new File(FileUtil.DESKTOP_DIR, "freemarker_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));
                    dir.mkdirs();

                    for (int ii = 0; ii < filePathList.getModel().getSize(); ii++) {
                        File tempFile = (File) filePathList.getModel().getElementAt(ii);
                        FileOutputStream fos = new FileOutputStream(new File(dir, tempFile.getName()));
                        FreeMarkerSimpleUtil.replace(tempFile, root, fos);
                    }

                    JCommonUtil._jOptionPane_showMessageDialog_info("done...");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_4.add(executeBtn);

        JPanel panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        jsonArea = new JTextArea();
        panel.add(jsonArea, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Template", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.NORTH);

        filePathText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(filePathText, false);
        panel_6.add(filePathText);
        filePathText.setColumns(30);

        JButton addPathBtn = new JButton("加入");
        addPathBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!(filePathList.getModel() instanceof DefaultListModel)) {
                        DefaultListModel model = new DefaultListModel();
                        filePathList.setModel(model);
                    }
                    DefaultListModel model = (DefaultListModel) filePathList.getModel();
                    File f = new File(filePathText.getText());
                    if (f.exists() && f.isFile() && f.canRead()) {
                        boolean findOk = false;
                        for (int ii = 0; ii < model.getSize(); ii++) {
                            File f2 = (File) model.getElementAt(ii);
                            if (f2.equals(f)) {
                                findOk = true;
                            }
                        }
                        if (findOk == false) {
                            model.addElement(f);
                        }
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_6.add(addPathBtn);

        JPanel panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.WEST);

        JPanel panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.EAST);

        JPanel panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.SOUTH);

        filePathList = new JList();
        filePathList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                JListUtil.newInstance(filePathList).defaultJListKeyPressed(e);
            }
        });

        panel_1.add(JCommonUtil.createScrollComponent(filePathList), BorderLayout.CENTER);

        JPanel panel_10 = new JPanel();
        tabbedPane.addTab("最近載入", null, panel_10, null);
        panel_10.setLayout(new BorderLayout(0, 0));

        JPanel panel_11 = new JPanel();
        panel_10.add(panel_11, BorderLayout.NORTH);

        JPanel panel_12 = new JPanel();
        panel_10.add(panel_12, BorderLayout.WEST);

        JPanel panel_13 = new JPanel();
        panel_10.add(panel_13, BorderLayout.SOUTH);

        JPanel panel_14 = new JPanel();
        panel_10.add(panel_14, BorderLayout.EAST);

        recentFileList = new JList();
        panel_10.add(JCommonUtil.createScrollComponent(recentFileList), BorderLayout.CENTER);

        {
            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            this.applyAppMenu();
        }

        this.setTitle("You Set My World On Fire");
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item").addMenuItem("detail1", null).getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file").addMenuItem("load", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
                appendToRecentFileList("", keepFile.get());
                reloadRecentFileList();
            }
        }).addMenuItem("save", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileAs(false);
                appendToRecentFileList("", keepFile.get());
                reloadRecentFileList();
            }
        }).addMenuItem("save As", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileAs(true);
                appendToRecentFileList("", keepFile.get());
                reloadRecentFileList();
            }
        }).addChildrenMenu(menu1).getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    private void loadFile() {
        try {
            File loadFile = JCommonUtil._jFileChooser_selectFileOnly();
            if (loadFile.exists()) {
                Properties prop = new Properties();
                prop.load(new FileInputStream(loadFile));
                if (prop.containsKey("json")) {
                    jsonArea.setText(StringUtils.trimToEmpty(prop.getProperty("json")));
                }
                StringBuilder errSb = new StringBuilder();
                DefaultListModel model = new DefaultListModel();
                for (int ii = 0; ii < 1000; ii++) {
                    if (prop.containsKey("file" + ii)) {
                        File file = new File(prop.getProperty("file" + ii));
                        if (!file.exists()) {
                            errSb.append(file.getAbsolutePath() + "\n");
                        } else {
                            model.addElement(file);
                        }
                    } else {
                        break;
                    }
                }
                filePathList.setModel(model);
                keepFileSet(loadFile);
                JCommonUtil._jOptionPane_showMessageDialog_info("載入成功 \n FileNotFound : " + errSb);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void keepFileSet(File file) {
        keepFile.set(file);
        setTitle(getFileName(file));
    }

    private void saveFileAs(boolean saveToTarget) {
        try {
            Properties prop = new Properties();
            prop.setProperty("json", StringUtils.trimToEmpty(jsonArea.getText()));
            DefaultListModel model = (DefaultListModel) filePathList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                File f = (File) model.getElementAt(ii);
                prop.setProperty("file" + ii, f.getAbsolutePath());
            }
            File saveFile = null;
            if (saveToTarget) {
                saveFile = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
            } else {
                if (keepFile.get() == null) {
                    saveFile = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
                } else {
                    saveFile = keepFile.get();
                }
            }
            if (!prop.isEmpty()) {
                prop.store(new FileOutputStream(saveFile), DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
                keepFileSet(saveFile);
                JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功！");
            } else {
                JCommonUtil._jOptionPane_showMessageDialog_error("儲存失敗！");
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void appendToRecentFileList(String title, File configFile) {
        try {
            if (!(recentFileList.getModel() instanceof DefaultListModel)) {
                DefaultListModel model = new DefaultListModel();
                recentFileList.setModel(model);
            }
            if (StringUtils.isBlank(title)) {
                title = getFileName(configFile);
            }
            config.getConfigProp().setProperty(title, configFile.getAbsolutePath());
            config.store();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void reloadRecentFileList() {
        DefaultListModel model = new DefaultListModel();
        for (Enumeration<?> enu = config.getConfigProp().keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            File file = new File(config.getConfigProp().getProperty(key));
            if (file.exists()) {
                String title = getFileName(file);
                if (!StringUtils.equals(key, title)) {
                    model.addElement(new FileZ(key, file));
                } else {
                    model.addElement(new FileZ(title, file));
                }
            }
        }
        recentFileList.setModel(model);
    }

    private String getFileName(File file) {
        int pos = file.getName().lastIndexOf(".");
        String name = file.getName();
        if (pos != -1) {
            name = name.substring(0, pos);
        }
        return name;
    }

    private class FileZ {
        String title;
        File file;

        private FileZ(String title, File file) {
            this.title = title;
            if (StringUtils.isBlank(title)) {
                this.title = getFileName(this.file);
            }
            this.file = file;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
