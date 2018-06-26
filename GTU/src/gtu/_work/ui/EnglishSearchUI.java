package gtu._work.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Robot;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseAdapter;
import org.jnativehook.mouse.NativeMouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.englishtester.EnglishwordInfoDAO;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.HermannEbbinghaus_Memory;
import gtu._work.HermannEbbinghaus_Memory.MemData;
import gtu._work.etc.EnglishTester_Diectory;
import gtu._work.etc.EnglishTester_Diectory.WordInfo;
import gtu._work.etc.EnglishTester_Diectory2;
import gtu._work.etc.EnglishTester_Diectory2.WordInfo2;
import gtu.clipboard.ClipboardListener;
import gtu.file.FileUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.number.RandomUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.HistoryComboBox;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTextAreaUtil;

public class EnglishSearchUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox searchEnglishIdText;
    private JTextArea searchResultArea;
    private JTextArea meaningText;
    private JButton queryButton;
    private JTextField newWordTxtPathText;
    private JTabbedPane tabbedPane;
    private JPanel panel_4;
    private JCheckBox reviewMemChk;

    PropertiesUtilBean propertyBean = new PropertiesUtilBean(EnglishSearchUI.class);
    private static final String NEW_WORD_PATH = "new_word_path";
    private static final String OFFLINE_WORD_PATH = "offline_word_path";
    private GlobalKeyListenerExampleForEnglishUI keyUtil;
    private __ClipboardListener listenClipboardThread = null;
    private Thread checkFocusOwnerThread;
    private SearchEnglishIdTextController searchEnglishIdTextController = new SearchEnglishIdTextController();
    private HideInSystemTrayHelper sysutil;

    private Properties offlineProp;
    private static final boolean DEBUG = !PropertiesUtil.isClassInJar(EnglishSearchUI.class);

    private HermannEbbinghaus_Memory memory = new HermannEbbinghaus_Memory(getDebugDir4Memory(), "EnglishSearchUI_MemoryBank.properties");

    private static File getDebugDir4Memory() {
        File f1 = new File("D:/gtu001_dropbox/Dropbox/Apps/gtu001_test/etc_config/");
        File f2 = new File("E:/gtu001_dropbox/Dropbox/Apps/gtu001_test/etc_config/");
        for (File f : new File[] { f1, f2 }) {
            if (f.exists() && f.isDirectory()) {
                return f;
            }
        }
        return null;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        // SocketUtilForSwing.startSwingUI("127.0.0.1", 65535,
        // EnglishSearchUI.class, new Runnable() {
        // @Override
        // public void run() {
        // try {
        // startNewUI();
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // }
        // }
        // });
        JnativehookKeyboardMouseHelper.getInstance().disableLogger();
        FRAME = new EnglishSearchUI();
        FRAME.keyUtil = FRAME.new GlobalKeyListenerExampleForEnglishUI();
        FRAME.keyUtil.init();

        // if(true){
        // DesktopUtil.openDir(FRAME.propertyBean.getPropFile().getAbsolutePath());
        // }
    }

    private static volatile EnglishSearchUI FRAME;

    private static void startNewUI() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    synchronized (EnglishSearchUI.class) {
                        if (FRAME == null) {
                            FRAME = new EnglishSearchUI();
                        }
                    }

                    if (FRAME.isVisible() && FRAME.searchEnglishIdTextController.isFocusOwner()) {
                        FRAME.setVisible(false);
                    } else {
                        FRAME.bringToTop();
                    }
                    System.out.println("startNewUI done...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    FocusListener focusListener = new FocusListener() {
        @Override
        public void focusLost(FocusEvent e) {
        }

        @Override
        public void focusGained(FocusEvent e) {
            focusSearchEnglishIdText();
        }
    };
    private JCheckBox listenClipboardChk;
    private JCheckBox rightBottomCornerChk;
    private JCheckBox autoSearchChk;
    private JCheckBox mouseSelectionChk;
    private JButton showNewWordTxtBtn;
    private JCheckBox focusTopChk;
    private JPanel panel_3;
    private JTextField offlineConfigText;
    private JCheckBox offlineModeChk;
    private JLabel label;
    private JCheckBox offlineModeFirstChk;
    private JCheckBox simpleSentanceChk;
    private JCheckBox robotFocusChk;

    private void startCheckFocusOwnerThread() {
        if (checkFocusOwnerThread == null || checkFocusOwnerThread.getState() == Thread.State.TERMINATED) {
            checkFocusOwnerThread = new Thread(new Runnable() {

                private String getTitle(String title) {
                    Pattern ptn = Pattern.compile("(生字數\\s:\\s\\d+)");
                    Matcher mth = ptn.matcher(title);
                    if (mth.find()) {
                        return mth.group(1);
                    }
                    return "";
                }

                @Override
                public void run() {
                    while (true) {
                        try {
                            boolean isFocus = searchEnglishIdTextController.isFocusOwner();

                            String title = EnglishSearchUI.this.getTitle();
                            title = getTitle(title) + " " + (isFocus ? "Focused" : "NA");
                            EnglishSearchUI.this.setTitle(title);

                            contentPane.setBackground((isFocus ? Color.YELLOW : Color.LIGHT_GRAY));

                            sleep(100L);
                        } catch (Exception e) {
                            JCommonUtil.handleException(e);
                        }
                    }
                }
            });
            checkFocusOwnerThread.setDaemon(true);
            checkFocusOwnerThread.start();
        }
    }

    private ActionListener MemDo = new ActionListener() {

        private List<String> getAllList(String key) {
            List<String> keys = new ArrayList<String>();
            String ch = StringUtils.substring(StringUtils.trim(key), 0, 1).toLowerCase();
            for (Enumeration<?> enu = offlineProp.keys(); enu.hasMoreElements();) {
                String v = (String) enu.nextElement();
                if (v.toLowerCase().startsWith(ch)) {
                    keys.add(v);
                }
            }
            return keys;
        }

        private String getRandom(List<String> allLst) {
            return allLst.get(new Random().nextInt(allLst.size()));
        }

        private Map<String, String> getRandomMap(int size, List<String> allLst) {
            Map<String, String> map = new HashMap<String, String>();
            if (allLst == null || allLst.isEmpty()) {
                System.out.println("沒資料 getRandomMap");
                return map;
            }
            while (map.size() <= size) {
                String str = getRandom(allLst);
                if (!map.containsKey(str)) {
                    String meaning = offlineProp.getProperty(str);
                    map.put(meaning, str);
                }
            }
            return map;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (offlineProp == null || offlineProp.isEmpty()) {
                loadOfflineConfig();
            }
            System.out.println("offlineProp size = " + offlineProp.size());

            MemData d = (MemData) event.getSource();
            long period = event.getWhen();
            String reviewType = event.getActionCommand();

            String meaning = "";
            if (StringUtils.isNotBlank(d.getRemark())) {
                meaning = d.getRemark();
            } else if (offlineProp.containsKey(d.getKey())) {
                meaning = offlineProp.getProperty(d.getKey());
            }

            List<String> allLst = this.getAllList(d.getKey());
            Map<String, String> questionMap = this.getRandomMap(3, allLst);
            questionMap.put(meaning, d.getKey());
            List<String> meaningLst = new ArrayList<String>(questionMap.keySet());
            meaningLst = RandomUtil.randomList(meaningLst);

            StringBuilder sb = new StringBuilder();
            sb.append("複習 : " + d.getKey() + "\n");
            sb.append("加入時間 : " + DateFormatUtils.format(d.getRegisterTime(), d.getDateFormat()) + "\n");
            sb.append("進程 : " + d.getReviewTime() + "\n");

            String choiceMeaning = (String) JCommonUtil._JOptionPane_showInputDialog(sb, "複習" + reviewType + " " + period, meaningLst.toArray(new String[0]), "");

            boolean choiceCorrect = StringUtils.equals(choiceMeaning, meaning);

            StringBuilder sb2 = new StringBuilder();
            sb2.append("是否答對 : " + (choiceCorrect ? "對" : "錯") + "\n");
            sb2.append("正確解答 : " + d.getKey() + " : " + meaning + "\n");
            sb2.append("你選的 : " + questionMap.get(choiceMeaning) + " : " + choiceMeaning + "\n");

            JCommonUtil._jOptionPane_showMessageDialog_info(sb2);
            
            if (!choiceCorrect) {
                d.setWaitingTriggerTime(3 * 1000);
            }
        }
    };
    private JButton reviewMemWaitingListBtn;

    /**
     * Create the frame.
     */
    public EnglishSearchUI() {
        addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                focusSearchEnglishIdText();
            }

            public void windowLostFocus(WindowEvent e) {
                // clearAllInput();
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // clearAllInput();
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 540, 377);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("翻譯", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(-1, 100));
        panel_1.add(panel_2, BorderLayout.NORTH);
        panel_2.setLayout(new BorderLayout(0, 0));

        searchEnglishIdText = new JComboBox();
        HistoryComboBox.applyComboBox(searchEnglishIdText, new ArrayList());
        searchEnglishIdText.setFont(new Font("細明體", Font.PLAIN, 16));
        searchEnglishIdTextController.get().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed ---- " + e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("key ENTER");
                    queryButtonAction(true);
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    System.out.println("DEL00000000000000000000000000000000");
                    clearAllInput();
                }
            }
        });
        searchEnglishIdTextController.get().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (JMouseEventUtil.buttonRightClick(1, arg0)) {
                    JPopupMenuUtil.newInstance(searchEnglishIdTextController.get())//
                            .addJMenuItem("google翻譯", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    try {
                                        String text = StringUtils.trimToEmpty(searchEnglishIdTextController.get().getText());
                                        if (StringUtils.isBlank(text)) {
                                            return;
                                        }
                                        text = URLEncoder.encode(text, "UTF-8");
                                        String url = String.format("https://translate.google.com.tw/?hl=zh-TW#en/zh-TW/%s", text);
                                        DesktopUtil.browse(url);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            }).applyEvent(arg0)//
                            .show();
                }
            }
        });

        searchEnglishIdText.addFocusListener(focusListener);

        panel_2.add(searchEnglishIdText, BorderLayout.NORTH);
        // searchEnglishIdText.setColumns(10);

        queryButton = new JButton("查詢");
        queryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                queryButtonAction(true);
            }
        });
        panel_2.add(queryButton, BorderLayout.EAST);

        meaningText = new JTextArea();
        meaningText.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane jScrollPane1 = new JScrollPane();
        panel_2.add(jScrollPane1, BorderLayout.CENTER);
        // jScrollPane1.setPreferredSize(new java.awt.Dimension(411, 262));
        jScrollPane1.setViewportView(meaningText);
        meaningText.addFocusListener(focusListener);
        JTextAreaUtil.setWrapTextArea(meaningText);
        meaningText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("# meaningText click");
                if (JMouseEventUtil.buttonLeftClick(1, e) && //
                StringUtils.isNotBlank(meaningText.getText()) //
                ) {
                    JCommonUtil._jOptionPane_showInputDialog("", meaningText.getText());
                }
            }
        });

        searchResultArea = new JTextArea();
        searchResultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JTextAreaUtil.setWrapTextArea(searchResultArea);
        JCommonUtil.createScrollComponent(panel_1, searchResultArea);
        searchResultArea.addFocusListener(focusListener);

        JPanel panel = new JPanel();
        tabbedPane.addTab("設定", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblNewLabel = new JLabel("new_word.txt路徑");
        panel.add(lblNewLabel, "2, 2, right, default");

        newWordTxtPathText = new JTextField();
        newWordTxtPathText.setColumns(20);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(newWordTxtPathText, false);

        JPanel pp1 = new JPanel();
        pp1.add(newWordTxtPathText);
        pp1.setPreferredSize(new Dimension(0, 100));
        panel.add(pp1, "4, 2, fill, default");

        showNewWordTxtBtn = new JButton("開啟new_word.txt");
        pp1.add(showNewWordTxtBtn);
        showNewWordTxtBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new File(newWordTxtPathText.getText()).toURI());
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        listenClipboardChk = new JCheckBox("監聽剪貼簿");
        listenClipboardChk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("< listenClipboardChk actionPerformed !!");
                listenClipboardThread.setMointerOn(listenClipboardChk.isSelected());
                mouseSelectionChk.setSelected(listenClipboardChk.isSelected());// XXX
            }
        });

        label = new JLabel("離線檔路徑");
        panel.add(label, "2, 4");

        panel_3 = new JPanel();
        panel_3.setPreferredSize(new Dimension(0, 100));
        panel.add(panel_3, "4, 4, fill, fill");

        offlineConfigText = new JTextField();
        offlineConfigText.setColumns(20);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(offlineConfigText, true);
        panel_3.add(offlineConfigText);
        panel.add(listenClipboardChk, "4, 6");

        autoSearchChk = new JCheckBox("開啟時自動查詢");
        panel.add(autoSearchChk, "4, 8");

        mouseSelectionChk = new JCheckBox("監控滑鼠圈選");
        panel.add(mouseSelectionChk, "4, 10");

        rightBottomCornerChk = new JCheckBox("視窗位置鎖定右下角");
        rightBottomCornerChk.setSelected(true);
        panel.add(rightBottomCornerChk, "4, 12");

        focusTopChk = new JCheckBox("鎖定最上層");
        focusTopChk.setSelected(false);
        panel.add(focusTopChk, "4, 14");

        offlineModeChk = new JCheckBox("離線模式");
        panel.add(offlineModeChk, "4, 16");

        offlineModeFirstChk = new JCheckBox("離線模式優先");
        panel.add(offlineModeFirstChk, "4, 18");

        simpleSentanceChk = new JCheckBox("簡化例句");
        panel.add(simpleSentanceChk, "4, 20");

        robotFocusChk = new JCheckBox("機械Focus");
        panel.add(robotFocusChk, "4, 22");

        sysutil = HideInSystemTrayHelper.newInstance();
        sysutil.apply(this, "英語字典", "resource/images/ico/janna.ico");

        // 設定new_word.txt路徑
        String value = propertyBean.getConfigProp().getProperty(NEW_WORD_PATH);
        if (StringUtils.isNotBlank(value)) {
            newWordTxtPathText.setText(value);
        } else {
            File newWordFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), "new_word.txt");
            if (newWordFile.exists()) {
                newWordTxtPathText.setText(newWordFile.getAbsolutePath());
            }
        }

        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                focusSearchEnglishIdText();
            }
        });

        this.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                // 使用word模式時需要自動隱藏
                if (EnglishSearchUI.this.autoSearchChk.isSelected() && //
                EnglishSearchUI.this.listenClipboardChk.isSelected() && //
                EnglishSearchUI.this.mouseSelectionChk.isSelected()//
                ) {
                    EnglishSearchUI.this.setVisible(false);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        JButton configSettingBtn = new JButton("儲存設定");
        configSettingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configSettingBtnAction();
            }
        });

        panel_4 = new JPanel();
        panel.add(panel_4, "4, 24, fill, fill");

        reviewMemChk = new JCheckBox("定時複習");
        reviewMemChk.setSelected(false);
        panel_4.add(reviewMemChk);

        reviewMemWaitingListBtn = new JButton("等待清單");
        reviewMemWaitingListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String waitingLstStr = StringUtils.join(memory.getWaitingList(), "\n");
                JCommonUtil._jOptionPane_showMessageDialog_info(waitingLstStr);
            }
        });
        panel_4.add(reviewMemWaitingListBtn);
        panel.add(configSettingBtn, "2, 26");

        // ----------------------------------------------------------------------------------------------------------

        // 置中
        JCommonUtil.setJFrameCenter(this);
        listenClipboardChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("listenClipboardChk")));
        rightBottomCornerChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("rightBottomCornerChk")));
        autoSearchChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("autoSearchChk")));
        showNewWordTxtBtn.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("showNewWordTxtBtn")));
        mouseSelectionChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("mouseSelectionChk")));
        offlineModeChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("offlineModeChk")));
        offlineModeFirstChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("offlineModeFirstChk")));
        simpleSentanceChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("simpleSentanceChk")));
        robotFocusChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("robotFocusChk")));
        offlineConfigText.setText(propertyBean.getConfigProp().getProperty(OFFLINE_WORD_PATH));

        JCommonUtil.frameCloseDo(this, new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                propertyBean.getConfigProp().setProperty("focusTopChk", String.valueOf(focusTopChk.isSelected()));
                propertyBean.getConfigProp().setProperty("listenClipboardChk", String.valueOf(listenClipboardChk.isSelected()));
                propertyBean.getConfigProp().setProperty("rightBottomCornerChk", String.valueOf(rightBottomCornerChk.isSelected()));
                propertyBean.getConfigProp().setProperty("autoSearchChk", String.valueOf(autoSearchChk.isSelected()));
                propertyBean.getConfigProp().setProperty("showNewWordTxtBtn", String.valueOf(showNewWordTxtBtn.isSelected()));
                propertyBean.getConfigProp().setProperty("mouseSelectionChk", String.valueOf(mouseSelectionChk.isSelected()));
                propertyBean.getConfigProp().setProperty("offlineModeChk", String.valueOf(offlineModeChk.isSelected()));
                propertyBean.getConfigProp().setProperty("offlineModeFirstChk", String.valueOf(offlineModeFirstChk.isSelected()));
                propertyBean.getConfigProp().setProperty("simpleSentanceChk", String.valueOf(simpleSentanceChk.isSelected()));
                propertyBean.getConfigProp().setProperty("robotFocusChk", String.valueOf(robotFocusChk.isSelected()));
                propertyBean.getConfigProp().setProperty("reviewMemChk", String.valueOf(reviewMemChk.isSelected()));
                propertyBean.store();
                System.exit(0);
            }
        });

        // 設定離線檔參照路徑
        this.initOfflineConfigText();

        // 確認是否Focus
        startCheckFocusOwnerThread();

        // 確認是否監聽記事本
        startListenClipboardThread();

        // 讀取離線檔
        loadOfflineConfig();

        // 設定記憶時鐘功能
        memory.setMemDo(MemDo);
        memory.start();
        if (DEBUG) {
            DesktopUtil.browse(memory.getFile().toURI().toString());
        }
    }

    private void startListenClipboardThread() {
        try {
            if (listenClipboardThread == null || listenClipboardThread.getState() == Thread.State.TERMINATED) {
                listenClipboardThread = new __ClipboardListener();
                listenClipboardThread.setDaemon(true);
                listenClipboardThread.start();
                listenClipboardThread.setMointerOn(listenClipboardChk.isSelected());
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private void bringToTop() {
        JCommonUtil.setFrameAtop(EnglishSearchUI.this, focusTopChk.isSelected());

        for (; true;) {
            this.tabbedPane.setSelectedIndex(0);
            if (this.tabbedPane.getSelectedIndex() == 0) {
                break;
            }
            sleep(10);
        }

        // 判斷是否鎖定右下角
        if (rightBottomCornerChk.isSelected()) {
            JCommonUtil.setLocationToRightBottomCorner(this);
        }

        focusSearchEnglishIdText();

        // 開啟時自動查詢
        if (StringUtils.isNotBlank(searchEnglishIdTextController.getText()) && //
                autoSearchChk.isSelected()) {
            queryButtonAction(false);
        }
    }

    private void initOfflineConfigText() {
        if (StringUtils.isBlank(offlineConfigText.getText())) {
            {
                File exportFileJsonFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), "exportFileJson.bin");
                if (exportFileJsonFile.exists()) {
                    offlineConfigText.setText(exportFileJsonFile.getAbsolutePath());
                }
            }
            {
                File exportFileJsonFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), "exportFileJava.bin");
                if (exportFileJsonFile.exists()) {
                    offlineConfigText.setText(exportFileJsonFile.getAbsolutePath());
                }
            }
        }
    }

    private void loadOfflineConfig() {
        try {
            Properties prop = new Properties();
            this.initOfflineConfigText();

            File file = new File(offlineConfigText.getText());

            if (DEBUG) {
                System.out.println("DEBUG MODE -----");
                File dir1 = new File("D:/gtu001_dropbox/Dropbox/Apps/gtu001_test/");
                File dir2 = new File("E:/gtu001_dropbox/Dropbox/Apps/gtu001_test/");
                for (File f1 : new File[] { dir1, dir2 }) {
                    if (f1.exists() && f1.isDirectory()) {
                        int max = -1;
                        Pattern ptn = Pattern.compile("bak(\\d+)");
                        for (File f : f1.listFiles()) {
                            if (f.getName().startsWith("bak")) {
                                Matcher mth = ptn.matcher(f.getName());
                                if (mth.find()) {
                                    max = Math.max(max, Integer.parseInt(mth.group(1)));
                                }
                            }
                        }
                        file = new File(f1 + File.separator + "bak" + max, "exportFileJava.bin");
                    }
                }
                System.out.println("offline file --- " + file);
            }

            if (file.exists()) {
                if (file.getName().equals("exportFileJson.bin")) {
                    if (file.exists()) {
                        System.out.println("read --- " + "exportFileJson.bin");
                        String content = FileUtil.loadFromFile(file, "utf8");
                        JSONArray arry = new JSONArray(content);
                        for (int ii = 0; ii < arry.length(); ii++) {
                            JSONObject obj = (JSONObject) arry.get(ii);
                            String englishId = obj.getString("englishId");
                            String englishDesc = obj.getString("englishDesc");
                            prop.setProperty(englishId, englishDesc);
                        }
                        System.out.println("read --- " + prop.size());
                    }
                }
                if (file.getName().equals("exportFileJava.bin")) {
                    System.out.println("read --- " + "exportFileJava.bin");
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream input = new ObjectInputStream(fis);
                    List<EnglishwordInfoDAO.EnglishWord> list = new ArrayList<EnglishwordInfoDAO.EnglishWord>();
                    try {
                        for (Object readObj = null; (readObj = input.readObject()) != null;) {
                            EnglishwordInfoDAO.EnglishWord word = (EnglishwordInfoDAO.EnglishWord) readObj;
                            String englishId = word.getEnglishId();
                            String englishDesc = word.getEnglishDesc();
                            prop.setProperty(englishId, englishDesc);
                        }
                        input.close();
                    } catch (java.io.EOFException ex) {
                    }
                    System.out.println("read --- " + prop.size());
                }
            }

            offlineProp = prop;
        } catch (Exception e1) {
            JCommonUtil.handleException(e1);
        }
    }

    private boolean queryButtonAction_offline(String text) {
        if (offlineProp.isEmpty()) {
            this.loadOfflineConfig();
        }
        meaningText.setText("");
        searchResultArea.setText("");
        if (offlineProp.containsKey(text)) {
            String content = offlineProp.getProperty(text);
            meaningText.setText(content);
            this.appendMemoryBank(content, "");
            return true;
        } else {
            meaningText.setText("查無此字!!");
            return false;
        }
    }

    private void queryButtonAction_online(final String text) {
        final EnglishTester_Diectory t = new EnglishTester_Diectory();
        final EnglishTester_Diectory2 t2 = new EnglishTester_Diectory2();

        WordInfo info = new WordInfo();
        try {
            info = t.parseToWordInfo(text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final StringBuffer sb = new StringBuffer();
        sb.append(text + "\n");
        sb.append(info.getPronounce() + "\n");
        sb.append(info.getMeaning() + "\n");
        sb.append("\n");

        final AtomicBoolean findOk = new AtomicBoolean(false);

        meaningText.setText(info.getMeaning());

        if (StringUtils.isNotBlank(info.getMeaning())) {
            findOk.set(true);
            this.appendMemoryBank(text, info.getMeaning());
        }

        final Set<String> meaningSet = new HashSet<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int maxLoop = simpleSentanceChk.isSelected() ? 1 : 10;

                for (int ii = 0; ii < maxLoop; ii++) {
                    WordInfo2 info2 = t2.parseToWordInfo(text, 1);
                    List<Pair<String, String>> exampleSentanceList = info2.getExampleSentanceList();
                    if (exampleSentanceList.isEmpty()) {
                        break;
                    }
                    for (Pair<String, String> p : exampleSentanceList) {
                        sb.append(p.getKey() + "\n");
                        sb.append(p.getValue() + "\n");
                        sb.append("\n");
                    }

                    meaningSet.add(info2.getMeaning2());

                    if (findOk.get() == false && StringUtils.isNotBlank(info2.getMeaning2())) {
                        findOk.set(true);
                        appendMemoryBank(text, info2.getMeaning2());
                    }
                }
                searchResultArea.setText(sb.toString());
                meaningText.setText(meaningText.getText() + ";" + StringUtils.join(meaningSet, ";"));

                // set top the UI
                // if (!EnglishSearchUI.this.isFocusOwner()) {
                // bringToTop();
                // }
            }
        }).start();
    }

    private void appendMemoryBank(String word, String desc) {
        memory.append(word);
    }

    private void queryButtonAction(boolean bringToFront) {
        try {
            if (!queryButton.isEnabled()) {
                return;
            }
            queryButton.setEnabled(false);

            final String text = StringUtils.trimToEmpty(searchEnglishIdTextController.getText()).toLowerCase();
            if (StringUtils.isBlank(text)) {
                return;
            }

            if (offlineModeFirstChk.isSelected()) {
                if (!this.queryButtonAction_offline(text)) {
                    this.queryButtonAction_online(text);
                }
            } else {
                if (offlineModeChk.isSelected()) {
                    this.queryButtonAction_offline(text);
                } else {
                    this.queryButtonAction_online(text);
                }
            }

            // writeNewData(text);
            int wordSize = writeNewData2(text);
            setTitle("生字數 : " + wordSize);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        } finally {
            queryButton.setEnabled(true);
            if (bringToFront) {
                bringToTop();
            }
        }
    }

    private boolean focusSearchEnglishIdText() {
        boolean isRobotFocus = JCommonUtil.focusComponent(searchEnglishIdTextController.get(), robotFocusChk.isSelected(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramActionEvent) {
                searchEnglishIdTextController.setSelectAll();
            }
        });
        return isRobotFocus;
    }

    private class SearchEnglishIdTextController {
        private void setText(String text) {
            // searchEnglishIdText.setText("");
            DefaultComboBoxModel model = (DefaultComboBoxModel) searchEnglishIdText.getModel();
            int pos = model.getIndexOf(text);
            if (pos == -1) {
                model.addElement(text);
            }
        }

        private void setInputText(String text) {
            text = StringUtils.trimToEmpty(text);
            get().setText(text);
        }

        private void setSelectAll() {
            String text = get().getText();
            get().setSelectionStart(0);
            get().setSelectionEnd(text.length());
            System.out.println(">> setSelectAll !!");
        }

        private String getText() {
            return get().getText();
        }

        private boolean isFocusOwner() {
            return get().isFocusOwner();
        }

        private JComboBox getCombobox() {
            return searchEnglishIdText;
        }

        private JTextComponent get() {
            return (JTextComponent) searchEnglishIdText.getEditor().getEditorComponent();
        }
    }

    private void clearAllInput() {
        searchEnglishIdTextController.setInputText("");
        searchEnglishIdTextController.setText("");
        searchResultArea.setText("");
        meaningText.setText("");
        searchEnglishIdText.requestFocus();
    }

    private int writeNewData2(String word) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getAppendTextFile()), "utf8"));
        Set<String> set = new LinkedHashSet<String>();
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trimToEmpty(line);
            if (StringUtils.isNotBlank(line)) {
                set.add(line);
            }
        }
        reader.close();
        // 空白太多當成句子不處理
        if (StringUtils.countMatches(StringUtils.trimToEmpty(word), " ") < 4) {
            set.add(word);
        }
        StringBuffer sb = new StringBuffer();
        for (String v : set) {
            sb.append(v + "\r\n");
        }
        FileUtil.saveToFile(getAppendTextFile(), sb.toString(), "utf8");
        return set.size();
    }

    private void writeNewData(String word) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(getAppendTextFile(), "rw");
        raf.seek(raf.length() - 1);
        raf.writeUTF("\n" + word + "\n");
        raf.close();
    }

    private File getAppendTextFile() {
        String val = propertyBean.getConfigProp().getProperty(NEW_WORD_PATH);
        if (StringUtils.isBlank(val)) {
            JCommonUtil._jOptionPane_showMessageDialog_error("未設定new_word.txt路徑, 建立一個在桌面!");
            File newFile = new File(FileUtil.DESKTOP_PATH + File.separator + "new_word.txt");
            try {
                newFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            val = newFile.getAbsolutePath();
            newWordTxtPathText.setText(val);
            propertyBean.getConfigProp().setProperty(NEW_WORD_PATH, val);
        }
        return new File(val);
    }

    private void configSettingBtnAction() {
        try {
            File f = JCommonUtil.filePathCheck(newWordTxtPathText.getText(), "請輸入new_word.txt路徑", "txt");
            if (!f.getName().equals("new_word.txt")) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案名稱必須為 new_word.txt!");
                return;
            }

            if (new File(offlineConfigText.getText()).exists()) {
                propertyBean.getConfigProp().setProperty(OFFLINE_WORD_PATH, offlineConfigText.getText());
            }

            propertyBean.getConfigProp().put(NEW_WORD_PATH, f.getCanonicalPath());
            propertyBean.store();

            JCommonUtil._jOptionPane_showMessageDialog_info("設定成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private class GlobalKeyListenerExampleForEnglishUI extends NativeKeyAdapter {
        public void close() {
            try {
                org.jnativehook.GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
            System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
            // 模式check
            if ((e.getModifiers() & NativeInputEvent.ALT_L_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_F2) {
                listenClipboardChk.setSelected(!listenClipboardChk.isSelected());
                JCommonUtil.triggerButtonActionPerformed(listenClipboardChk);
                sysutil.displayMessage("字典", "監聽" + (listenClipboardChk.isSelected() ? "啟用" : "停止"), MessageType.INFO);
                // 監聽開啟UI
            } else if (// (e.getModifiers() & NativeInputEvent.SHIFT_MASK) != 0
                       // && //
            (e.getModifiers() & NativeInputEvent.ALT_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_X) {
                startNewUI();
            }
        }

        public void init() {
            try {
                GlobalScreen.registerNativeHook();
                GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExampleForEnglishUI());
                GlobalScreen.addNativeMouseListener(new GlobalNativeMouseExampleForEnglishUI());
                JnativehookKeyboardMouseHelper.getInstance().disableLogger();
                startNewUI();
            } catch (NativeHookException ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }

    private class GlobalNativeMouseExampleForEnglishUI extends NativeMouseAdapter {
        Robot robot;

        private GlobalNativeMouseExampleForEnglishUI() {
            try {
                robot = new Robot();
            } catch (AWTException e) {
            }
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent paramNativeMouseEvent) {
            super.nativeMouseReleased(paramNativeMouseEvent);
            if (mouseSelectionChk.isSelected()) {
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_C);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
        }
    }

    private class __ClipboardListener extends ClipboardListener {
        @Override
        public void processText(String text) {
            getclipboardText(text);
        }

        public void getclipboardText(String word) {
            String english = getEnglish(StringUtils.trimToEmpty(word));
            String oldEnglish = StringUtils.trimToEmpty(searchEnglishIdTextController.get().getText());
            if (StringUtils.isNotBlank(english)) {
                if (StringUtils.trimToEmpty(english).length() == 1) {
                    return;
                }

                // 與原先內容不同清除內容
                if (!StringUtils.equalsIgnoreCase(english, oldEnglish)) {
                    clearAllInput();
                }

                System.out.println("----" + english);
                // searchEnglishIdTextController.setText(english);
                searchEnglishIdTextController.setInputText(english);
                // queryButtonAction();//暫時不自動查詢
                bringToTop();
            } else {
            }
        }

        Pattern ptn = Pattern.compile("[a-zA-Z\\s\\-]*");

        private String getEnglish(String text) {
            Matcher mth = ptn.matcher(text);
            List<String> lst = new ArrayList<String>();
            while (mth.find()) {
                lst.add(StringUtils.trim(mth.group(0)));
            }
            return StringUtils.join(lst, " ");
        }
    }
}
