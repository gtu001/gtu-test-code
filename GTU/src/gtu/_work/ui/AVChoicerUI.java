package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.date.DateFormatUtil;
import gtu.file.FileUtil;
import gtu.jdk8.ex1.StreamUtil;
import gtu.properties.PropertiesGroupUtils_ByKey;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.recyclebin.RecycleBinTrashcanUtil;
import gtu.recyclebin.RecycleBinUtil_forWin;
import gtu.runtime.DesktopUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;

public class AVChoicerUI extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final String FILE_EXTENSTION_VIDEO_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3)";

    private static boolean isWindows = false;

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    private JPanel contentPane;
    private JTextField avDirText;
    private JList avDirList;
    private JButton choiceAVBtn;
    private JTextField avExeText;

    private static final String AV_LIST_KEY = "avDirList";
    private static final String AV_EXE_KEY = "avExeText";

    private PropertiesUtilBean config = new PropertiesUtilBean(AVChoicerUI.class);
    // private PropertiesUtilBean config = new PropertiesUtilBean(new
    // File("/media/gtu001/OLD_D/my_tool/AVChoicerUI_config.properties"));

    private Set<File> clickAvSet = new HashSet<File>();
    private CurrentFileHandler currentFileHandler = new CurrentFileHandler();
    private List<File> cacheFileList = new ArrayList<File>();
    private JLabel deleteAVFileLabel;
    private JLabel movCountLabel;
    private JButton replayBtn;
    private JTextField moveToText;
    private JList moveToList;
    private MoveToHandler moveToHandler = new MoveToHandler();
    private JCheckBox moveToChkJpgChkBox;
    private HideInSystemTrayHelper trayUtil;

    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private JTextField avExeFormatText;
    private JTextField avExeAliasText;
    private AvExeConfigHandler avExeConfigHandler;
    private JTextField avExeEncodeText;
    private JTextField dirCheckText;
    private JList dirCheckList;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(AVChoicerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AVChoicerUI frame = new AVChoicerUI();
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
    public AVChoicerUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 563, 433);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("播放影片", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_7 = new JPanel();
        panel.add(panel_7, BorderLayout.NORTH);

        deleteAVFileLabel = new JLabel("");
        panel_7.add(deleteAVFileLabel);

        JButton deleteAVFileBtn = new JButton("刪除VOD");
        deleteAVFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                currentFileHandler.deleteFile();
            }
        });

        JButton openContainDirBtn = new JButton("開啟目錄");
        openContainDirBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                currentFileHandler.openContainDir();
            }
        });

        movCountLabel = new JLabel("");
        panel_7.add(movCountLabel);
        panel_7.add(openContainDirBtn);
        panel_7.add(deleteAVFileBtn);

        replayBtn = new JButton("重播");
        replayBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                currentFileHandler.replay();
            }
        });
        panel_7.add(replayBtn);

        JPanel panel_8 = new JPanel();
        panel.add(panel_8, BorderLayout.WEST);

        JPanel panel_9 = new JPanel();
        panel.add(panel_9, BorderLayout.SOUTH);

        JPanel panel_10 = new JPanel();
        panel.add(panel_10, BorderLayout.EAST);

        choiceAVBtn = new JButton("隨機撥放");
        choiceAVBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choiceAVBtnAction();
            }
        });
        panel.add(choiceAVBtn, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("搜尋目錄", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.NORTH);

        avDirText = new JTextField();
        panel_3.add(avDirText);
        avDirText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(avDirText, true);

        JButton avDirAddBtn = new JButton("add");
        avDirAddBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                avDirAddBtnAction();
            }
        });
        panel_3.add(avDirAddBtn);

        JButton saveConfigBtn = new JButton("save");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfigBtnAction();
            }
        });
        panel_3.add(saveConfigBtn);

        JButton reloadListBtn = new JButton("reload");
        reloadListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                resetCacheFileList();
                initAvDirList();
            }
        });
        panel_3.add(reloadListBtn);

        JPanel panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.WEST);

        JPanel panel_5 = new JPanel();
        panel_1.add(panel_5, BorderLayout.EAST);

        JPanel panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.SOUTH);

        avDirList = new JList();
        panel_1.add(JCommonUtil.createScrollComponent(avDirList), BorderLayout.CENTER);
        avDirList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent paramKeyEvent) {
                resetCacheFileList();
                JListUtil.newInstance(avDirList).defaultJListKeyPressed(paramKeyEvent);
            }
        });
        avDirList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (JMouseEventUtil.buttonLeftClick(2, e)) {
                    File f = (File) avDirList.getSelectedValue();
                    DesktopUtil.openDir(f);
                }
            }
        });

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("設定", null, panel_2, null);
        panel_2.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel lblNewLabel_1 = new JLabel("別名");
        panel_2.add(lblNewLabel_1, "2, 2, right, default");

        avExeAliasText = new JTextField();
        panel_2.add(avExeAliasText, "4, 2, fill, default");
        avExeAliasText.setColumns(10);

        JLabel label = new JLabel("撥放器");
        panel_2.add(label, "2, 4, right, default");

        avExeText = new JTextField();
        panel_2.add(avExeText, "4, 4, fill, default");
        avExeText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(avExeText, true);

        JLabel lblNewLabel = new JLabel("format");
        panel_2.add(lblNewLabel, "2, 6, right, default");

        avExeFormatText = new JTextField();
        avExeFormatText.setColumns(10);
        panel_2.add(avExeFormatText, "4, 6, fill, default");

        JLabel lblNewLabel_2 = new JLabel("encoding");
        panel_2.add(lblNewLabel_2, "2, 8, right, default");

        avExeEncodeText = new JTextField();
        panel_2.add(avExeEncodeText, "4, 8, fill, default");
        avExeEncodeText.setColumns(10);

        JPanel panel_17 = new JPanel();
        panel_2.add(panel_17, "4, 20, fill, fill");

        JButton nextConfigBtn = new JButton("下一組");
        nextConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                avExeConfigHandler.nextConfig();
            }
        });
        panel_17.add(nextConfigBtn);

        JButton saveConfigBtn2 = new JButton("儲存");
        panel_17.add(saveConfigBtn2);
        saveConfigBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                avExeConfigHandler.saveConfig();
            }
        });

        JPanel panel_11 = new JPanel();
        tabbedPane.addTab("MoveTo", null, panel_11, null);
        panel_11.setLayout(new BorderLayout(0, 0));

        JPanel panel_12 = new JPanel();
        panel_11.add(panel_12, BorderLayout.WEST);

        JPanel panel_13 = new JPanel();
        panel_11.add(panel_13, BorderLayout.EAST);

        JPanel panel_14 = new JPanel();
        panel_11.add(panel_14, BorderLayout.SOUTH);

        JPanel panel_15 = new JPanel();
        panel_11.add(panel_15, BorderLayout.NORTH);

        JButton moveToAddBtn = new JButton("add");
        moveToAddBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                moveToHandler.add();
            }
        });

        moveToText = new JTextField();
        panel_15.add(moveToText);
        moveToText.setColumns(10);
        panel_15.add(moveToAddBtn);

        JButton moveToSaveBtn = new JButton("save");
        moveToSaveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                moveToHandler.save();
            }
        });
        panel_15.add(moveToSaveBtn);

        JButton moveToReloadBtn = new JButton("reload");
        moveToReloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveToHandler.reload();
            }
        });
        panel_15.add(moveToReloadBtn);

        JButton moveToBtn = new JButton("移動");
        moveToBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveToHandler.moveTo();
            }
        });
        panel_15.add(moveToBtn);

        moveToChkJpgChkBox = new JCheckBox("檢查jpg");
        panel_15.add(moveToChkJpgChkBox);

        moveToList = new JList();
        panel_11.add(JCommonUtil.createScrollComponent(moveToList), BorderLayout.CENTER);

        JPanel panel_16 = new JPanel();
        tabbedPane.addTab("Config", null, panel_16, null);
        moveToList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent paramKeyEvent) {
                JListUtil.newInstance(moveToList).defaultJListKeyPressed(paramKeyEvent);
            }
        });
        moveToList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent paramMouseEvent) {
                if (JMouseEventUtil.buttonLeftClick(2, paramMouseEvent)) {
                    File dir = new File((String) JListUtil.getLeadSelectionObject(moveToList));
                    DesktopUtil.openDir(dir);
                }
            }
        });

        config.reflectInit(this);
        initAvDirList();
        avExeConfigHandler = new AvExeConfigHandler();
        avExeConfigHandler.nextConfig();

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setJFrameIcon(this, "resource/images/ico/pornHub.ico");
        JCommonUtil.defaultToolTipDelay();

        trayUtil = HideInSystemTrayHelper.newInstance();
        trayUtil.apply(this);

        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);

        panel_16.add(jFrameRGBColorPanel.getToggleButton(false));

        JPanel panel_18 = new JPanel();
        tabbedPane.addTab("目錄檢視", null, panel_18, null);
        panel_18.setLayout(new BorderLayout(0, 0));

        JPanel panel_19 = new JPanel();
        panel_18.add(panel_19, BorderLayout.NORTH);

        JLabel lblNewLabel_3 = new JLabel("檢視目錄");
        panel_19.add(lblNewLabel_3);

        dirCheckText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(dirCheckText, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirCheckTextActionPerformed();
            }
        });
        dirCheckText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                dirCheckTextActionPerformed();
            }
        });

        panel_19.add(dirCheckText);
        dirCheckText.setColumns(30);

        JPanel panel_20 = new JPanel();
        panel_18.add(panel_20, BorderLayout.WEST);

        JPanel panel_21 = new JPanel();
        panel_18.add(panel_21, BorderLayout.EAST);

        JPanel panel_22 = new JPanel();
        panel_18.add(panel_22, BorderLayout.SOUTH);

        dirCheckList = new JList();
        dirCheckList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (JMouseEventUtil.buttonLeftClick(2, arg0)) {
                    dirCheckListMouseClicked();
                }
            }
        });
        panel_18.add(JCommonUtil.createScrollComponent(dirCheckList), BorderLayout.CENTER);
    }

    private class MoveToHandler {
        private PropertiesUtilBean moveConfig = new PropertiesUtilBean(MoveToHandler.class, AVChoicerUI.class.getSimpleName() + "_" + MoveToHandler.class.getSimpleName());
        // private PropertiesUtilBean moveConfig = new PropertiesUtilBean(new
        // File("/media/gtu001/OLD_D/my_tool/AVChoicerUI_MoveToHandler_config.properties"));

        private void add() {
            try {
                File dir = JCommonUtil.filePathCheck(moveToText.getText(), "輸入目錄", true);
                if (!dir.exists()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("目錄不存在:" + dir);
                    return;
                }
                moveConfig.getConfigProp().setProperty(dir.getAbsolutePath(), dir.getAbsolutePath());
                reload();
                moveToText.setText("");
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        private void moveTo() {
            try {
                File dir = new File((String) moveToList.getSelectedValue());

                if (moveToChkJpgChkBox.isSelected() && currentFileHandler.getJpgFile() == null) {
                    boolean openDir = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("沒有jpg圖, 要開啟來源嗎?", "開啟目錄");
                    if (openDir) {
                        currentFileHandler.openContainDir();
                        return;
                    }
                }

                StringBuffer sb = new StringBuffer();
                sb.append("確定移動到 : " + dir + "\r\n");
                sb.append("檔案1 : " + currentFileHandler.getAvFile() + "\r\n");
                sb.append("檔案2 : " + currentFileHandler.getJpgFile() + "\r\n");

                boolean moveChk = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString(), "移動");

                if (moveChk) {

                    Object[] p1 = fileMove(currentFileHandler.getAvFile(), getMoveTargetFile(dir, currentFileHandler.getAvFile()));
                    Object[] p2 = fileMove(currentFileHandler.getJpgFile(), getMoveTargetFile(dir, currentFileHandler.getJpgFile()));

                    StringBuffer sb1 = new StringBuffer();
                    sb1.append("移動結果 : \r\n");
                    sb1.append("檔案1 : " + Arrays.toString(p1) + "\r\n");
                    sb1.append("檔案1 : " + Arrays.toString(p2) + "\r\n");

                    JCommonUtil._jOptionPane_showMessageDialog_info(sb1.toString());
                } else {
                    JCommonUtil._jOptionPane_showMessageDialog_info("移動取消!");
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        private File getMoveTargetFile(File dir, File srcFile) {
            if (srcFile == null) {
                return null;
            }
            return new File(dir, srcFile.getName());
        }

        private Object[] fileMove(File srcFile, File toFile) {
            Boolean result = false;
            String errorMessage = "";
            try {
                if (srcFile == null || !srcFile.exists()) {
                    return new Object[] { false, "檔案不存在!" };
                }

                FileUtils.moveFile(srcFile, toFile);
                result = toFile.exists();
            } catch (Exception ex) {
                errorMessage = ex.toString();
            }
            return new Object[] { result, toFile, errorMessage };
        }

        private void save() {
            try {
                moveConfig.store();
                moveConfig.reload();
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        private void reload() {
            try {
                DefaultListModel model = JListUtil.createModel();
                List<File> flst = new ArrayList<File>();

                StreamUtil.of(moveConfig.getConfigProp().keys())//
                        .map(key -> new File(String.valueOf(key)))//
                        .filter(file -> ((File) file).exists())//
                        .sorted(Comparator.comparing(File::getAbsolutePath))//
                        .forEach(file -> model.addElement(((File) file).getAbsolutePath()));
                moveToList.setModel(model);
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }

    private void initAvDirList() {
        try {
            JListUtil jUtil = JListUtil.newInstance(avDirList);
            DefaultListModel model = jUtil.createModel();
            config.getConfigProp().keySet().stream()//
                    .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                    .forEach(key -> {
                        File f = new File(config.getConfigProp().getProperty((String) key));
                        if (f.exists()) {
                            model.addElement(f);
                        }
                    });
            avDirList.setModel(model);
            System.out.println("INIT size = " + avDirList.getModel().getSize());
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private int getMaxAvListId() {
        Pattern ptn = Pattern.compile(AV_LIST_KEY + "\\_(\\d+)");
        String reduce = config.getConfigProp().keySet().stream()//
                .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                .map(key -> {
                    Matcher mth = ptn.matcher((String) key);
                    mth.find();
                    return mth.group(1);
                }).reduce("-1", (a, b) -> {
                    if (Integer.parseInt(a) >= Integer.parseInt(b)) {
                        return a;
                    } else {
                        return b;
                    }
                });
        return Integer.parseInt(reduce);
    }

    private int getMaxAvExeId() {
        Pattern ptn = Pattern.compile(AV_EXE_KEY + "\\_(\\d+)");
        String reduce = config.getConfigProp().keySet().stream()//
                .filter(key -> ((String) key).startsWith(AV_EXE_KEY))//
                .map(key -> {
                    Matcher mth = ptn.matcher((String) key);
                    mth.find();
                    return mth.group(1);
                }).reduce("-1", (a, b) -> {
                    if (Integer.parseInt(a) >= Integer.parseInt(b)) {
                        return a;
                    } else {
                        return b;
                    }
                });
        return Integer.parseInt(reduce);
    }

    private void saveConfigBtnAction() {
        try {
            StringBuffer sb = new StringBuffer();
            {
                File exeFile = new File(avExeText.getText());
                if (exeFile.exists()) {
                    List<File> fileLst = config.getConfigProp().keySet().stream()//
                            .filter(key -> ((String) key).startsWith(AV_EXE_KEY))//
                            .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                            .collect(Collectors.toList());
                    if (!fileLst.contains(exeFile)) {
                        String key = AV_EXE_KEY + "_" + (getMaxAvExeId() + 1);
                        config.getConfigProp().setProperty(key, exeFile.getAbsolutePath());
                        sb.append("exe儲存成功!\n");
                    }
                }
            }

            {
                DefaultListModel model = (DefaultListModel) avDirList.getModel();
                List<File> fileLst = config.getConfigProp().keySet().stream()//
                        .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                        .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                        .collect(Collectors.toList());

                AtomicBoolean bool = new AtomicBoolean(false);
                AtomicInteger index = new AtomicInteger(getMaxAvListId() + 1);
                IntStream.range(0, model.size())//
                        .mapToObj(i -> (File) model.getElementAt(i))//
                        .forEach(file -> {
                            if (!fileLst.contains(file)) {
                                config.getConfigProp().setProperty(AV_LIST_KEY + "_" + index.get(), file.getAbsolutePath());
                                index.set(index.get() + 1);
                                bool.set(true);
                            }
                        });

                if (bool.get()) {
                    sb.append("list儲存成功!\n");
                }
            }

            if (sb.length() > 0) {
                config.store();
            }

            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成果~\n" + sb);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void avDirAddBtnAction() {
        try {
            Validate.notBlank(avDirText.getText(), "目錄或檔案為空!");

            File newFile = new File(avDirText.getText());

            List<File> fileLst = config.getConfigProp().keySet().stream()//
                    .filter(key -> ((String) key).startsWith(AV_LIST_KEY))//
                    .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                    .collect(Collectors.toList());

            DefaultListModel model = (DefaultListModel) avDirList.getModel();
            List<File> fileLst2 = IntStream.range(0, model.size())//
                    .mapToObj(i -> (File) model.getElementAt(i))//
                    .collect(Collectors.toList());

            boolean anyMatch1 = fileLst.stream().anyMatch(f -> f.getAbsolutePath().equals(newFile.getAbsolutePath()));
            boolean anyMatch2 = fileLst2.stream().anyMatch(f -> f.getAbsolutePath().equals(newFile.getAbsolutePath()));

            if (anyMatch1 || anyMatch2) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案已存在!");
            } else {
                model.addElement(newFile);
                resetCacheFileList();
                JCommonUtil._jOptionPane_showMessageDialog_error("新增成功!");
            }

            resetCacheFileList();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private File getRandomAvFile() {
        if (cacheFileList == null || cacheFileList.isEmpty()) {
            cacheFileList = new ArrayList<File>();

            DefaultListModel model = (DefaultListModel) avDirList.getModel();
            IntStream.range(0, model.size())//
                    .mapToObj(i -> (File) model.getElementAt(i))//
                    .forEach(file -> {
                        FileUtil.searchFileMatchs(file, ".*\\." + FILE_EXTENSTION_VIDEO_PATTERN, cacheFileList);
                    });
        }

        if (cacheFileList == null) {
            cacheFileList = new ArrayList<>();
        }

        System.out.println("cacheFileList.size() = " + cacheFileList.size());

        File choiceFile = null;
        List<File> cloneLst = new ArrayList<>(cacheFileList);
        cloneLst.removeAll(clickAvSet);

        if (!cloneLst.isEmpty()) {
            choiceFile = cloneLst.get(new Random().nextInt(cloneLst.size()));
        } else {
            JCommonUtil._jOptionPane_showMessageDialog_info("清單已跑完一輪");
            choiceFile = cacheFileList.get(new Random().nextInt(cacheFileList.size()));
            clickAvSet.clear();
        }

        clickAvSet.add(choiceFile);
        return choiceFile;
    }

    private class CurrentFileHandler {
        private AtomicReference<File> tempFile = new AtomicReference<File>();
        private AtomicReference<File> tempJpgFile = new AtomicReference<File>();
        private AtomicReference<File> tempDir = new AtomicReference<File>();

        private File __findJpgFile(File avFile) {
            try {
                String currentFileName = FileUtil.getNameNoSubName(avFile);
                Pattern ptn = Pattern.compile(currentFileName, Pattern.CASE_INSENSITIVE);
                return Stream.of(avFile.getParentFile().listFiles()).filter(f -> f.getName().endsWith(".jpg")).filter(f -> {
                    String jpgName = FileUtil.getNameNoSubName(f);
                    Matcher mth = ptn.matcher(jpgName);
                    return mth.find();
                }).findAny().orElse(null);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        public File getAvFile() {
            return tempFile.get();
        }

        public File getJpgFile() {
            return tempJpgFile.get();
        }

        private void setFile(File avFile) {
            tempFile.set(avFile);
            tempJpgFile.set(__findJpgFile(avFile));// 找圖片

            choiceAVBtn.setToolTipText(avFile.getName());
            tempDir.set(avFile.getParentFile());
            setTitle(avFile.getParentFile().getName() + "/" + avFile.getName());
            String desc = DateFormatUtil.format(avFile.lastModified(), "yyyy/MM/dd") + " " + FileUtil.getSizeDescription(avFile.length());
            deleteAVFileLabel.setText(desc);
            setCountLabel();
        }

        private void setCountLabel() {
            int totalSize = -1;
            int alreadyLookSize = -1;
            try {
                totalSize = cacheFileList.size();
            } catch (Exception ex) {
            }
            try {
                alreadyLookSize = clickAvSet.size();
            } catch (Exception ex) {
            }
            movCountLabel.setText(String.format("以看%d, 總數%d", alreadyLookSize, totalSize));
        }

        private void showInputString(InputStream is) throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            IOUtils.copy(is, bos);
            String string = bos.toString("BIG5");
            if (StringUtils.isNotBlank(string)) {
                JCommonUtil._jOptionPane_showMessageDialog_error(string);
            }
        }

        private void deleteFile() {
            File file = tempFile.get();
            if (!file.exists()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                return;
            }
            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除此檔 : " + file, "刪除檔案!");
            if (result) {
                try {
                    boolean delResult = false;
                    if (isWindows) {
                        delResult = RecycleBinUtil_forWin.moveTo(file);
                    } else {
                        delResult = RecycleBinTrashcanUtil.moveToTrashCan(file);
                        if (!delResult) {
                            try {
                                FileUtils.forceDelete(file);
                                delResult = file.exists();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    trayUtil.displayMessage(delResult ? "刪除成功!" : "刪除失敗", file.toString(), MessageType.INFO);
                    deleteAVFileLabel.setText(file.exists() ? "Done!" : "NotDone!");
                    setCountLabel();
                    resetCacheFileList();
                } catch (Exception e) {
                    JCommonUtil.handleException(e);
                }
            }
        }

        private void openContainDir() {
            try {
                File dir = tempDir.get();
                if (!dir.exists()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("目錄不存在!");
                    return;
                }
                Desktop.getDesktop().open(dir);
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        private void replay() {
            playAvFile(tempFile.get());
        }
    }

    private void resetCacheFileList() {
        cacheFileList = null;
    }

    private File getMediaPlayerExe() throws Exception {
        File exe = config.getConfigProp().keySet().stream()//
                .filter(key -> ((String) key).startsWith(AV_EXE_KEY))//
                .map(key -> new File(config.getConfigProp().getProperty((String) key)))//
                .filter(File::exists)//
                .findFirst().orElseThrow(() -> {
                    return new Exception("未設定Movie Exe!!");
                });
        return exe;
    }

    private void choiceAVBtnAction() {
        playAvFile(getRandomAvFile());
    }

    private void playAvFile(File avFile) {
        try {
            // File exe = getMediaPlayerExe();
            File exe = new File(avExeText.getText());
            String commandFormat = avExeFormatText.getText();
            String encoding = avExeEncodeText.getText();

            currentFileHandler.setFile(avFile);

            RuntimeBatPromptModeUtil t = RuntimeBatPromptModeUtil.newInstance();
            String command = String.format(commandFormat, exe, avFile);
            System.out.println("CMD ==> " + command);
            System.out.println("encoding ==> " + encoding);
            t.command(command);
            t.runInBatFile(false);

            ProcessWatcher watcher = ProcessWatcher.newInstance(t.apply("tmpPlayer_", encoding));
            String str1 = watcher.getInputStreamToString();
            String err1 = watcher.getErrorStreamToString();
            System.out.println("Input=============================================Start");
            System.out.println(str1);
            System.out.println("Input=============================================End");
            System.out.println("Error=============================================Start");
            System.out.println(err1);
            System.out.println("Error=============================================End");
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private class AvExeConfigHandler {
        private PropertiesGroupUtils_ByKey avExeConfig = new PropertiesGroupUtils_ByKey(
                new File(PropertiesUtil.getJarCurrentPath(AVChoicerUI.class), AVChoicerUI.class.getSimpleName() + "_avExeConfig.properties"));

        private final String AV_EXE_KEY = "avExe";
        private final String AV_EXE_FORMAT_KEY = "avExeFormat";
        private final String AV_EXE_ENCODE_KEY = "avExeEncode";

        public void AvExeConfigHandler() {
        }

        private void nextConfig() {
            try {
                Map<String, String> config = avExeConfig.loadConfig();

                avExeAliasText.setText(config.get(PropertiesGroupUtils_ByKey.SAVE_KEYS));
                avExeText.setText(config.get(AV_EXE_KEY));
                avExeFormatText.setText(config.get(AV_EXE_FORMAT_KEY));
                avExeEncodeText.setText(config.get(AV_EXE_ENCODE_KEY));

                avExeConfig.next();
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        private void saveConfig() {
            try {
                Map<String, String> config = new HashMap<String, String>();

                Validate.notBlank(avExeAliasText.getText(), "alias不可為空!");
                Validate.notBlank(avExeText.getText(), "exe不可為空!");
                Validate.notBlank(avExeFormatText.getText(), "format不可為空!");
                // Validate.notBlank(avExeEncodeText.getText(),
                // "encoding不可為空!");

                config.put(PropertiesGroupUtils_ByKey.SAVE_KEYS, StringUtils.trimToEmpty(avExeAliasText.getText()));
                config.put(AV_EXE_KEY, StringUtils.trimToEmpty(avExeText.getText()));
                config.put(AV_EXE_FORMAT_KEY, StringUtils.trimToEmpty(avExeFormatText.getText()));
                config.put(AV_EXE_ENCODE_KEY, StringUtils.trimToEmpty(avExeEncodeText.getText()));

                avExeConfig.saveConfig(config);
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }

    private void dirCheckTextActionPerformed() {
        List<FileZ> lst = new ArrayList<FileZ>();
        File dirFile = new File(dirCheckText.getText());
        if (dirFile.isFile()) {
            lst.add(new FileZ(dirFile));
        } else {
            for (File f : dirFile.listFiles()) {
                lst.add(new FileZ(f));
            }
        }
        DefaultListModel model = JListUtil.createModel();
        for (FileZ f : lst) {
            model.addElement(f);
        }
        dirCheckList.setModel(model);
    }

    private void dirCheckListMouseClicked() {
        FileZ fileZ = (FileZ) JListUtil.getLeadSelectionObject(dirCheckList);
        System.out.println("file --- " + fileZ.file);
        if (fileZ.file.isDirectory()) {
            try {
                Desktop.getDesktop().open(fileZ.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            playAvFile(fileZ.file);
        }
    }

    private class FileZ {
        File file;
        String name;

        FileZ(File file) {
            this.file = file;
            this.name = file.getName();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
