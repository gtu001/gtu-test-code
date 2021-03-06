package gtu._work.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
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
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
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
import gtu.binary.StringUtil4FullChar;
import gtu.clipboard.ClipboardListener;
import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.number.RandomUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.spring.SimilarityUtil;
import gtu.swing.util.AutoComboBox;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JComboBoxUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFontChooserHelper;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JSwingCommonConfigUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTextFieldUtil;
import gtu.swing.util.KeyEventUtil;
import gtu.swing.util.SimpleCheckListDlg;
import taobe.tec.jcc.JChineseConvertor;

public class EnglishSearchUI extends JFrame {

    private static final String HermannEbbinghaus_Memory_Category_ENGLISH = "Eng";
    private static final String HermannEbbinghaus_Memory_Category_OTHER = "Other";

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
    private JButton reviewMemWaitingListBtn;
    private JButton reviewMemResetBtn;
    private JButton reviewMemConfigBtn;
    private JLabel offlineReadyLabel;
    private JButton reviewMemFromFileBtn;
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
    private JComboBox screenSelectComboBox;
    private AutoComboBox searchEnglishIdText_auto;
    private List<String> englishLst;
    Integer tvModeDlgFontSize = null;
    private AtomicBoolean queryButtonActionHolder = new AtomicBoolean();

    private PropertiesUtilBean propertyBean = new PropertiesUtilBean(this.getClass());
    {
        // if (!PropertiesUtil.isClassInJar(EnglishSearchUI.class)) {
        // if (OsInfoUtil.isWindows()) {
        // propertyBean = new PropertiesUtilBean(new
        // File("D:/my_tool/EnglishSearchUI/EnglishSearchUI_win10_config.properties"));
        // } else {
        // propertyBean = new PropertiesUtilBean(new
        // File("/media/gtu001/OLD_D/my_tool/EnglishSearchUI/EnglishSearchUI_linux_config.properties"));
        // }
        // }
        propertyBean = JSwingCommonConfigUtil.checkTestingPropertiesUtilBean_diffConfig(propertyBean, EnglishSearchUI.class, "/"); // EnglishSearchUI/
        System.out.println("configFile : " + propertyBean.getPropFile());
    }

    private static final String NEW_WORD_PATH = "new_word_path";
    private static final String OFFLINE_WORD_PATH = "offline_word_path";
    private static final String MEMORY_BANK_PATH = "memory_bank_path";
    private GlobalKeyListenerExampleForEnglishUI keyUtil;
    private __ClipboardListener listenClipboardThread = null;
    private Thread checkFocusOwnerThread;
    private SearchEnglishIdTextController searchEnglishIdTextController = new SearchEnglishIdTextController();
    private HideInSystemTrayHelper sysutil;

    private Properties offlineProp;
    private HermannEbbinghaus_Memory memory = new HermannEbbinghaus_Memory();
    private DialogTitleUpdaterObervable dialogObervable = new DialogTitleUpdaterObervable();
    private EnglishIdDropdownHandler mEnglishIdDropdownHandler;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SimpleCheckListDlg mSimpleCheckListDlg;
    private AtomicReference<String> notFoundWord = new AtomicReference<String>();
    private LRUMap propOtherMap = new LRUMap(3000);
    private ReentrantLock brinToTopLock = new ReentrantLock();
    private JCheckBox forceOfflineCheckbox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(EnglishSearchUI.class)) {
            return;
        }

        JnativehookKeyboardMouseHelper.getInstance().disableLogger();
        FRAME = new EnglishSearchUI();
        FRAME.keyUtil = FRAME.new GlobalKeyListenerExampleForEnglishUI();
        FRAME.keyUtil.init();
    }

    private static volatile EnglishSearchUI FRAME;

    private static void startNewUI() {
        // EventQueue.invokeLater(new Runnable() {
        // public void run() {
        // try {
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // });
        synchronized (EnglishSearchUI.class) {
            if (FRAME == null) {
                FRAME = new EnglishSearchUI();
            }
        }
        if (JCommonUtil.isOnTop(FRAME)) {
            gtu.swing.util.JFrameUtil.setVisible(false, FRAME);
        } else {
            FRAME.bringToTop();
        }
        System.out.println("startNewUI done...");
    }

    FocusListener focusListener = new FocusListener() {
        @Override
        public void focusLost(FocusEvent e) {
        }

        @Override
        public void focusGained(FocusEvent e) {
            searchEnglishIdTextController.focusSearchEnglishIdText();
        }
    };

    private void startCheckFocusOwnerThread() {
        if (jFrameRGBColorPanel == null) {
            jFrameRGBColorPanel = new JFrameRGBColorPanel(contentPane);
            jFrameRGBColorPanel.setIgnoreLst(this);
            jFrameRGBColorPanel.addIgnoreLst(searchEnglishIdText);
            jFrameRGBColorPanel.setAfterProcessEvent(new ActionListener() {
                private String getTitle(String title) {
                    Pattern ptn = Pattern.compile("(生字數\\s:\\s\\d+)");
                    Matcher mth = ptn.matcher(title);
                    if (mth.find()) {
                        return mth.group(1);
                    }
                    return "";
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean isFocus = searchEnglishIdTextController.isFocusOwner();
                    String title = EnglishSearchUI.this.getTitle();
                    title = getTitle(title) + " " + (isFocus ? "Focused" : "NA");
                    EnglishSearchUI.this.setTitle(title);
                }
            });

            panel_10.add(jFrameRGBColorPanel.getToggleButton(false));
        }
    }

    private ActionListener MemDo = new ActionListener() {

        private List<String> getAllList(String key) {
            List<String> keys = new ArrayList<String>();
            String ch = StringUtils.substring(StringUtils.trim(key), 0, 1).toLowerCase();
            for (Enumeration<?> enu = getOfflineProp().keys(); enu.hasMoreElements();) {
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
                    String meaning = getOfflineProp().getProperty(str);
                    map.put(meaning, str);
                }
            }
            return map;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (getOfflineProp() == null || getOfflineProp().isEmpty()) {
                loadOfflineConfig();
            }
            System.out.println("offlineProp size = " + getOfflineProp().size());

            final MemData d = (MemData) event.getSource();

            if (StringUtils.isBlank(d.getCategory()) || HermannEbbinghaus_Memory_Category_ENGLISH.equals(d.getCategory())) {
                createEnglishWordDialog(event);
            } else if (HermannEbbinghaus_Memory_Category_OTHER.equals(d.getCategory())) {
                createOtherQuestionDialog(event);
            }
        }

        private void createOtherQuestionDialog(ActionEvent event) {
            final MemData d = (MemData) event.getSource();
            long period = event.getWhen();
            String reviewType = event.getActionCommand();

            final MouseMarkQueryHandler mouseMarkQueryHandler = new MouseMarkQueryHandler();
            final EnglishSearchUI_MemoryBank_DialogOtherUI choiceDialog = new EnglishSearchUI_MemoryBank_DialogOtherUI();
            dialogObervable.add(choiceDialog);

            choiceDialog.initial();
            choiceDialog.createDialog("複習階段 :" + reviewType + " [組列 : " + memory.getQueue().size() + "]", //
                    d.getKey(), //
                    d.getRemark(), //
                    new ActionListener() { // delete key
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean confirmDel = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定刪除 : " + d.getKey(), "從設定檔中刪除");
                            if (confirmDel) {
                                memory.deleteKey(d.getKey());
                                choiceDialog.closeDialog();
                            }
                        }
                    }, new ActionListener() {// choice
                                             // one
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            choiceDialog.closeDialog();
                        }
                    }, new ActionListener() {// modify
                                             // desc
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String newMeaning = JCommonUtil._jOptionPane_showInputDialog("請輸入新解釋  : " + d.getKey(), d.getRemark());
                            if (newMeaning != null && !StringUtils.equals(newMeaning, d.getRemark())) {
                                d.setRemark(newMeaning);
                                JCommonUtil._jOptionPane_showMessageDialog_info("原為 : " + d.getRemark() + "\n修正為 : " + newMeaning, "修正成功 " + d.getKey());
                                choiceDialog.closeDialog();
                            }
                        }
                    }, new ActionListener() {// skip
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            long waitingTime = RandomUtil.rangeInteger(10, 30) * 60 * 1000;
                            d.setWaitingTriggerTime(-1);// TODO 改成進入下個階段排成
                            choiceDialog.closeDialog();
                        }
                    }, new ActionListener() {// skip
                                             // all
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                int min = Integer.valueOf(JCommonUtil._jOptionPane_showInputDialog("請輸入延後分鐘數?", "5"));
                                memory.suspend(Range.between(min, min + 40));
                            } catch (Exception ex) {
                                memory.suspend();
                            }
                            d.setRetry(true);
                            choiceDialog.closeDialog();
                        }
                    }, new ActionListener() { // onCreate
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mouseMarkQueryHandler.before();
                        }
                    }, new ActionListener() { // onClose
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mouseMarkQueryHandler.after();
                            dialogObervable.remove(choiceDialog);
                        }
                    }, new ActionListener() { // append
                                              // new
                                              // word
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                String key = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("輸入單字:"));
                                if (StringUtils.isBlank(key)) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("未輸入單字!");
                                    return;
                                }
                                String desc = getChs2Big5(getEnglishMeaning(key));
                                memory.append(key, HermannEbbinghaus_Memory_Category_ENGLISH, desc);
                                memory.store();
                                JCommonUtil._jOptionPane_showMessageDialog_info("加入成功!!\n" + key + "\n" + desc);
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    }, new ActionListener() { // suspend
                                              // key
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean confirmDel = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定終止 : " + d.getKey(), "從設定檔中停止");
                            if (confirmDel) {
                                memory.suspendKey(d.getKey());
                                d.setWaitingTriggerTime(9999 * 60 * 1000);
                                choiceDialog.closeDialog();
                            }
                        }
                    });
            choiceDialog.showDialog();
        }

        private void createEnglishWordDialog(ActionEvent event) {
            final MemData d = (MemData) event.getSource();
            long period = event.getWhen();
            String reviewType = event.getActionCommand();

            final AtomicBoolean checkChoiceEqual = new AtomicBoolean(true);
            final AtomicReference<String> meaning = new AtomicReference<String>();
            if (StringUtils.isNotBlank(d.getRemark())) {
                meaning.set(d.getRemark());
            } else if (getOfflineProp().containsKey(d.getKey())) {
                meaning.set(getOfflineProp().getProperty(d.getKey()));
            }

            List<String> allLst = this.getAllList(d.getKey());
            Map<String, String> questionMap = this.getRandomMap(3, allLst);
            questionMap.put(meaning.get(), d.getKey());
            List<String> meaningLst = new ArrayList<String>(questionMap.keySet());
            meaningLst = RandomUtil.randomList(meaningLst);

            // 正確答案第幾項
            int correctAnswerIndex = meaningLst.indexOf(meaning.get());

            StringBuilder sb = new StringBuilder();
            sb.append("複習 : " + d.getKey() + "\n");
            sb.append("加入時間 : " + DateFormatUtils.format(d.getRegisterTime(), d.getDateFormat()) + "\n");
            sb.append("進程 : " + d.getReviewTime() + "\n");

            // 若只有一個答案就把空白放第一個
            if (meaningLst.size() == 1) {
                String onlyOne = meaningLst.get(0);
                meaningLst.set(0, "");
                meaningLst.add(onlyOne);
            }

            String choiceMeaning = "";

            switch (2) {
            case 1:
                choiceMeaning = (String) JCommonUtil._JOptionPane_showInputDialog(sb, "複習" + reviewType + " " + period, meaningLst.toArray(new String[0]), "");
                break;
            case 2:
                final EnglishSearchUI_MemoryBank_DialogUI choiceDialog = new EnglishSearchUI_MemoryBank_DialogUI();
                dialogObervable.add(choiceDialog);
                final MouseMarkQueryHandler mouseMarkQueryHandler = new MouseMarkQueryHandler();
                choiceDialog.initial();
                choiceDialog.createDialog("複習階段 :" + reviewType + " [組列 : " + memory.getQueue().size() + "]", //
                        d.getKey(), //
                        meaningLst.toArray(new String[0]), //
                        correctAnswerIndex, //
                        new ActionListener() { // delete key
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                boolean confirmDel = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定刪除 : " + d.getKey(), "從設定檔中刪除");
                                if (confirmDel) {
                                    checkChoiceEqual.set(false);
                                    memory.deleteKey(d.getKey());
                                    choiceDialog.closeDialog();
                                }
                            }
                        }, new ActionListener() {// choice
                                                 // one
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                choiceDialog.closeDialog();
                            }
                        }, new ActionListener() {// modify
                                                 // desc
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String newMeaning = JCommonUtil._jOptionPane_showInputDialog("請輸入新解釋  : " + d.getKey(), meaning.get());
                                if (newMeaning != null) {
                                    newMeaning = getChs2Big5(newMeaning);
                                }
                                if (newMeaning != null && !StringUtils.equals(newMeaning, meaning.get())) {
                                    choiceDialog.setNewMeaning(meaning.get(), newMeaning);
                                    meaning.set(newMeaning);
                                    d.setRemark(newMeaning);
                                    checkChoiceEqual.set(false);
                                    JCommonUtil._jOptionPane_showMessageDialog_info("原為 : " + meaning.get() + "\n修正為 : " + newMeaning, "修正成功 " + d.getKey());
                                    choiceDialog.closeDialog();
                                }
                            }
                        }, new ActionListener() {// skip
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                long waitingTime = RandomUtil.rangeInteger(10, 30) * 60 * 1000;
                                d.setWaitingTriggerTime(-1);// TODO 改成進入下個階段排成
                                checkChoiceEqual.set(false);
                                choiceDialog.closeDialog();
                            }
                        }, new ActionListener() {// skip
                                                 // all
                            Pattern ptn = Pattern.compile("\\d+\\-\\d+");

                            private Range getMinRange(String strVal) {
                                Matcher mth = ptn.matcher(strVal);
                                if (mth.matches()) {
                                    return Range.between(Integer.parseInt(mth.group(1)), Integer.parseInt(mth.group(2)));
                                } else {
                                    int min = Integer.parseInt(strVal);
                                    return Range.between(min, min);
                                }
                            }

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    String strVal = JCommonUtil._jOptionPane_showInputDialog("請輸入延後分鐘數?(\\d+ or \\d+-\\d+)", "1-40");
                                    memory.suspend(getMinRange(strVal));
                                } catch (Exception ex) {
                                    memory.suspend();
                                }
                                d.setRetry(true);
                                checkChoiceEqual.set(false);
                                choiceDialog.closeDialog();
                            }
                        }, new ActionListener() { // onCreate
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mouseMarkQueryHandler.before();
                            }
                        }, new ActionListener() { // onClose
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mouseMarkQueryHandler.after();
                                dialogObervable.remove(choiceDialog);
                            }
                        }, new ActionListener() { // append
                                                  // new
                                                  // word
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    String key = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("輸入單字:"));
                                    if (StringUtils.isBlank(key)) {
                                        JCommonUtil._jOptionPane_showMessageDialog_error("未輸入單字!");
                                        return;
                                    }
                                    String desc = getChs2Big5(getEnglishMeaning(key));
                                    memory.append(key, HermannEbbinghaus_Memory_Category_ENGLISH, desc);
                                    memory.store();
                                    JCommonUtil._jOptionPane_showMessageDialog_info("加入成功!!\n" + key + "\n" + desc);
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                }
                            }
                        }, new ActionListener() { // suspend
                                                  // key
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                boolean confirmDel = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定終止 : " + d.getKey(), "從設定檔中停止");
                                if (confirmDel) {
                                    checkChoiceEqual.set(false);
                                    memory.suspendKey(d.getKey());
                                    d.setWaitingTriggerTime(9999 * 60 * 1000);
                                    choiceDialog.closeDialog();
                                }
                            }
                        });
                choiceDialog.showDialog();
                choiceMeaning = choiceDialog.getChoiceAnswer();
                break;
            }

            boolean choiceCorrect = StringUtils.equals(choiceMeaning, meaning.get());
            if (!choiceCorrect && checkChoiceEqual.get()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("是否答對 : " + (choiceCorrect ? "對" : "錯") + "\n");
                sb2.append("你選的 : " + questionMap.get(choiceMeaning) + " : " + choiceMeaning + "\n");
                sb2.append("正確解答 : " + d.getKey() + " : " + meaning + "\n");
                JCommonUtil._jOptionPane_showMessageDialog_info(sb2);
                d.setWaitingTriggerTime(30 * 1000);
            }
        }
    };

    private ActionListener onOffDo = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            sysutil.displayMessage("Memory Bank", event.getActionCommand(), MessageType.INFO);
        }
    };
    private JButton reviewMemResumeBtn;
    private JCheckBox lostFocusHiddenChk;
    private JButton reviewMemoryMergeBtn;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JTextPane googleTranslateArea;
    private JButton googleTranslateBtn;
    private JButton googleTranslateClearBtn;
    private JPanel panel_10;
    private JToggleButton tglbtnNewToggleButton;
    private JButton googleTranslateChangeFontBtn;
    private JPanel panel_11;
    private JPanel panel_12;
    private JPanel panel_13;
    private JPanel panel_14;
    private JPanel panel_15;
    private JLabel lblNewLabel_1;
    private JTextField emeoryQuestionText;
    private JTextArea emeoryQuestionDescArea;
    private JButton emeoryClearBtn;
    private JButton emeoryRemoveBtn;
    private JButton emeoryAddBtn;

    /**
     * Create the frame.
     */
    public EnglishSearchUI() {
        JCommonUtil.defaultToolTipDelay();
        addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                // focusSearchEnglishIdText(); TODO
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
        setBounds(100, 100, 540, 347);// xxxxxxxx setBounds(100, 100, 540, 347);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == TabIndexOrder.googleTranslate.ordinal()) {
                    // googleTranslateArea.setText(searchEnglishIdTextController.getText());
                }
            }
        });
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("翻譯", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_2.setPreferredSize(new Dimension(-1, 100));
        panel_1.add(panel_2, BorderLayout.NORTH);
        panel_2.setLayout(new BorderLayout(0, 0));

        searchEnglishIdText = new JComboBox();
        // HistoryComboBox.applyComboBox(searchEnglishIdText, new ArrayList());
        searchEnglishIdText_auto = AutoComboBox.applyAutoComboBox(searchEnglishIdText);
        searchEnglishIdText.setFont(new Font("細明體", Font.PLAIN, 16));
        searchEnglishIdTextController.get().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                mEnglishIdDropdownHandler.restoreDropdownLst();

                // System.out.println("keyPressed ---- " + e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("key ENTER");
                    queryButtonAction(true);
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    System.out.println("DEL00000000000000000000000000000000");
                    clearAllInput();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (gtu.swing.util.JFrameUtil.isVisible(EnglishSearchUI.this)) {
                        gtu.swing.util.JFrameUtil.setVisible(false, EnglishSearchUI.this);
                    }
                } else if (//
                (KeyEventUtil.isMaskKeyPress(e, "c") || //
                KeyEventUtil.isMaskKeyPress(e, "s") || //
                KeyEventUtil.isMaskKeyPress(e, "a")//
                ) && e.getKeyCode() == KeyEvent.VK_I//
                ) {
                    showTvModeDialog();
                }
            }
        });

        searchEnglishIdTextController.get().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (JMouseEventUtil.buttonRightClick(1, arg0)) {
                    JPopupMenuUtil.newInstance(searchEnglishIdTextController.get())//
                            .addJMenuItem("輸入錯誤單字", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    String text = searchEnglishIdTextController.get().getText();
                                    if (StringUtils.isNotBlank(text)) {
                                        try {
                                            int wordSize = writeNewData2(text, true);
                                            setTitle("生字數 : " + wordSize);
                                        } catch (Exception e) {
                                            JCommonUtil.handleException(e);
                                        }
                                    }
                                }
                            }).addJMenuItem("複製", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    String text = searchEnglishIdTextController.get().getText();
                                    if (StringUtils.isNotBlank(text)) {
                                        ClipboardUtil.getInstance().setContents(text);
                                    }
                                }
                            }).addJMenuItem("貼上", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    String text = ClipboardUtil.getInstance().getContents();
                                    if (StringUtils.isNotBlank(text)) {
                                        searchEnglishIdTextController.get().setText(text);
                                    }
                                }
                            }).addJMenuItem("google翻譯", new ActionListener() {
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
                            }).addJMenuItem("google查詢", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    try {
                                        String text = StringUtils.trimToEmpty(searchEnglishIdTextController.get().getText());
                                        if (StringUtils.isBlank(text)) {
                                            return;
                                        }
                                        text = URLEncoder.encode(text, "UTF-8");
                                        String url = String.format("https://www.google.com.tw/search?q=%s&oi=ddle&ct=144867951&hl=en", text);
                                        DesktopUtil.browse(url);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            }).addJMenuItem("google圖片", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    try {
                                        String text = StringUtils.trimToEmpty(searchEnglishIdTextController.get().getText());
                                        if (StringUtils.isBlank(text)) {
                                            return;
                                        }
                                        text = URLEncoder.encode(text, "UTF-8");
                                        String url = String.format("https://www.google.com.tw/search?hl=zh-TW&tbm=isch&source=hp&biw=1362&bih=798&q=%s", text);
                                        DesktopUtil.browse(url);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            }).addJMenuItem("下拉字典", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    searchEnglishIdText_auto.applyComboxBoxList(englishLst);
                                }
                            }).addJMenuItem("下拉歷史紀錄", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    File newWordFile = new File(StringUtils.trimToEmpty(newWordTxtPathText.getText()));
                                    if (newWordFile.exists()) {
                                        List<String> lst = FileUtil.loadFromFile_asList(newWordFile, "UTF8");
                                        Collections.reverse(lst);
                                        searchEnglishIdText_auto.applyComboxBoxList(lst, false);
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
        JTextAreaUtil.applyCommonSetting(meaningText);
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
                    showTvModeDialog();
                }
            }
        });

        searchResultArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(searchResultArea);
        searchResultArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JTextAreaUtil.setWrapTextArea(searchResultArea);
        JCommonUtil.createScrollComponent(panel_1, searchResultArea);
        searchResultArea.addFocusListener(focusListener);

        JPanel panel = new JPanel();
        tabbedPane.addTab("設定", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

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
                // focusSearchEnglishIdText(); TODO
            }
        });

        this.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if (lostFocusHiddenChk.isSelected()) {
                    gtu.swing.util.JFrameUtil.setVisible(false, EnglishSearchUI.this);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });

        offlineReadyLabel = new JLabel("");
        offlineReadyLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                offlineReadyLabelAction();
            }
        });
        panel_3.add(offlineReadyLabel);

        JButton configSettingBtn = new JButton("儲存設定");
        configSettingBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                configSettingBtnAction();
            }
        });

        lostFocusHiddenChk = new JCheckBox("失焦隱藏");
        panel.add(lostFocusHiddenChk, "4, 24");

        forceOfflineCheckbox = new JCheckBox("強制離線");
        panel.add(forceOfflineCheckbox, "4, 26");
        panel.add(configSettingBtn, "2, 30");

        panel_4 = new JPanel();
        panel.add(panel_4, "4, 30, fill, fill");

        reviewMemChk = new JCheckBox("定時複習");
        reviewMemChk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (reviewMemChk.isSelected()) {
                    memory.start();
                } else {
                    memory.stop();
                }
            }
        });
        panel_4.add(reviewMemChk);

        reviewMemWaitingListBtn = new JButton("等待清單");
        reviewMemWaitingListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reviewMemWaitingListBtnAction();
            }
        });
        panel_4.add(reviewMemWaitingListBtn);

        reviewMemResetBtn = new JButton("重設");
        reviewMemResetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reviewMemResetBtnAction();
            }
        });
        panel_4.add(reviewMemResetBtn);

        reviewMemConfigBtn = new JButton("開啟設定檔");
        reviewMemConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reviewMemConfigBtnAction();
            }
        });
        panel_4.add(reviewMemConfigBtn);

        reviewMemFromFileBtn = new JButton("加入記憶清單");
        reviewMemFromFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reviewMemFromFileBtn();
            }
        });
        panel_4.add(reviewMemFromFileBtn);

        reviewMemResumeBtn = new JButton("中斷回復");
        reviewMemResumeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reviewMemResumeBtnAction();
            }
        });
        panel_4.add(reviewMemResumeBtn);
        reviewMemChk.setSelected(Boolean.valueOf(propertyBean.getConfigProp().getProperty("reviewMemChk")));

        reviewMemoryMergeBtn = new JButton("合併");
        reviewMemoryMergeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                reviewMemoryMergeBtnAction();
            }
        });
        panel_4.add(reviewMemoryMergeBtn);

        screenSelectComboBox = new JComboBox();
        screenSelectComboBox.setToolTipText("選擇螢幕");
        panel_4.add(screenSelectComboBox);
        JComboBoxUtil.setShowOnScreenSelectActionListener(screenSelectComboBox, this, true);

        panel_10 = new JPanel();
        panel.add(panel_10, "4, 32, fill, fill");

        panel_5 = new JPanel();
        tabbedPane.addTab("Google翻譯", null, panel_5, null);
        panel_5.setLayout(new BorderLayout(0, 0));

        panel_6 = new JPanel();
        panel_5.add(panel_6, BorderLayout.NORTH);

        panel_7 = new JPanel();
        panel_5.add(panel_7, BorderLayout.WEST);

        panel_8 = new JPanel();
        panel_5.add(panel_8, BorderLayout.SOUTH);

        googleTranslateBtn = new JButton("翻譯");
        googleTranslateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = StringUtils.trimToEmpty(googleTranslateArea.getText());
                    text = URLEncoder.encode(text, "UTF-8");
                    DesktopUtil.browse("https://translate.google.com.tw/?hl=zh-TW#en/zh-TW/" + text);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        googleTranslateChangeFontBtn = new JButton("改字形");
        googleTranslateChangeFontBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Font font = JFontChooserHelper.showChooser(getGoogleTranslateArea());
                if (font != null) {
                    JTextAreaUtil.setJTextPaneFont(googleTranslateArea, font, Color.BLACK);
                }
            }
        });

        panel_8.add(googleTranslateChangeFontBtn);
        panel_8.add(googleTranslateBtn);

        googleTranslateClearBtn = new JButton("清除");
        googleTranslateClearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                googleTranslateArea.setText("");
            }
        });

        panel_8.add(googleTranslateClearBtn);

        panel_9 = new JPanel();
        panel_5.add(panel_9, BorderLayout.EAST);

        googleTranslateArea = new JTextPane();
        JTextAreaUtil.applyCommonSetting(googleTranslateArea);

        MutableAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(set, 1);
        googleTranslateArea.setParagraphAttributes(set, true);

        googleTranslateArea.addMouseListener(new MouseAdapter() {

            final String PTN = "[\\w\\-\\_]+";

            private String getSearchWord() {
                StringBuilder wordSb = new StringBuilder();
                try {
                    int startPos = googleTranslateArea.getSelectionStart();
                    int endPos = googleTranslateArea.getSelectionEnd();
                    String googleText = StringUtils.defaultString(JTextFieldUtil.getText(googleTranslateArea));
                    char[] arry = googleText.toCharArray();

                    if (startPos != -1 && endPos != -1) {
                        if (startPos == endPos) {
                            A: for (int ii = startPos; ii > 0; ii--) {
                                char c = arry[ii];
                                if (!String.valueOf(c).matches(PTN)) {
                                    break A;
                                } else {
                                    wordSb.insert(0, c);
                                }
                            }
                        } else if (startPos - 1 >= 0) {
                            A: for (int ii = startPos - 1; ii > 0; ii--) {
                                char c = arry[ii];
                                if (!String.valueOf(c).matches(PTN)) {
                                    break A;
                                } else {
                                    wordSb.insert(0, c);
                                }
                            }
                        }

                        wordSb.append(StringUtils.substring(googleText, startPos, endPos));

                        if (startPos == endPos) {
                            if (endPos + 1 <= arry.length) {
                                A: for (int ii = endPos + 1; ii < arry.length; ii++) {
                                    char c = arry[ii];
                                    if (!String.valueOf(c).matches(PTN)) {
                                        break A;
                                    } else {
                                        wordSb.append(c);
                                    }
                                }
                            }
                        } else {
                            A: for (int ii = endPos; ii < arry.length; ii++) {
                                char c = arry[ii];
                                if (!String.valueOf(c).matches(PTN)) {
                                    break A;
                                } else {
                                    wordSb.append(c);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("SearchWord =  " + wordSb);
                return wordSb.toString();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    if (JMouseEventUtil.buttonLeftClick(1, e)) {
                        String searchWord = googleTranslateArea.getSelectedText();
                        if (StringUtils.isBlank(searchWord)) {
                            googleTranslateArea_SetTooltip(null, null);
                            return;
                        }
                        searchEnglishIdTextController.setInputText(searchWord);
                        queryButtonAction(false);
                    } else if (JMouseEventUtil.buttonLeftClick(2, e)) {
                        String searchWord = getSearchWord();
                        if (StringUtils.isBlank(searchWord)) {
                            googleTranslateArea_SetTooltip(null, null);
                            return;
                        }
                        searchEnglishIdTextController.setInputText(searchWord);
                        queryButtonAction(false);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        panel_5.add(JCommonUtil.createScrollComponent(googleTranslateArea, false, true), BorderLayout.CENTER);

        panel_11 = new JPanel();
        tabbedPane.addTab("記憶工具", null, panel_11, null);
        panel_11.setLayout(new BorderLayout(0, 0));

        panel_12 = new JPanel();
        panel_11.add(panel_12, BorderLayout.NORTH);

        lblNewLabel_1 = new JLabel("標題");
        panel_12.add(lblNewLabel_1);

        emeoryQuestionText = new JTextField();
        panel_12.add(emeoryQuestionText);
        emeoryQuestionText.setColumns(35);

        panel_13 = new JPanel();
        panel_11.add(panel_13, BorderLayout.WEST);

        panel_14 = new JPanel();
        panel_11.add(panel_14, BorderLayout.EAST);

        panel_15 = new JPanel();
        panel_11.add(panel_15, BorderLayout.SOUTH);

        emeoryAddBtn = new JButton("加入記憶");
        emeoryAddBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Validate.notBlank(emeoryQuestionText.getText(), "題目不可為空");
                    Validate.notBlank(emeoryQuestionDescArea.getText(), "答案不可為空");
                    memory.append(StringUtils.defaultString(emeoryQuestionText.getText()), HermannEbbinghaus_Memory_Category_OTHER, StringUtils.defaultString(emeoryQuestionDescArea.getText()));
                    memory.store();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_15.add(emeoryAddBtn);

        emeoryClearBtn = new JButton("清空");
        emeoryClearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                emeoryQuestionText.setText("");
                emeoryQuestionDescArea.setText("");
            }
        });
        panel_15.add(emeoryClearBtn);

        emeoryRemoveBtn = new JButton("移除記憶");
        emeoryRemoveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    Validate.notBlank(emeoryQuestionText.getText(), "題目不可為空");
                    memory.deleteKey(StringUtils.defaultString(emeoryQuestionText.getText()));
                    memory.store();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_15.add(emeoryRemoveBtn);

        emeoryQuestionDescArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(emeoryQuestionDescArea);
        panel_11.add(JCommonUtil.createScrollComponent(emeoryQuestionDescArea), BorderLayout.CENTER);

        // ----------------------------------------------------------------------------------------------------------

        // 置中
        JCommonUtil.setJFrameCenter(this);

        propertyBean.reflectInit(this);

        // service ----------------------------
        mEnglishIdDropdownHandler = new EnglishIdDropdownHandler();

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
                propertyBean.getConfigProp().setProperty("lostFocusHiddenChk", String.valueOf(lostFocusHiddenChk.isSelected()));
                propertyBean.store();
                System.exit(0);
            }
        });

        // 確認是否Focus
        startCheckFocusOwnerThread();

        // 確認是否監聽記事本
        startListenClipboardThread();

        // 讀取離線檔
        this.offlineReadyLabelAction();

        // 設定記憶時鐘功能
        this.init_HermannEbbinghaus_Memory();
    }

    private void showTvModeDialog() {
        final String key = "tvModeDlgFontSize";
        if (propertyBean.getConfigProp().containsKey(key)) {
            tvModeDlgFontSize = Integer.parseInt(propertyBean.getConfigProp().getProperty(key));
        }
        EnglishSearchUI_TVModeDlg.newInstance(meaningText.getText(), tvModeDlgFontSize, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tvModeDlgFontSize = e.getID();
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tvModeDlgFontSize = e.getID();
                if (Integer.parseInt(propertyBean.getConfigProp().getProperty(key)) != tvModeDlgFontSize) {
                    propertyBean.getConfigProp().setProperty(key, String.valueOf(tvModeDlgFontSize));
                    propertyBean.store();
                }
            }
        });
    }

    private void startListenClipboardThread() {
        try {
            if (listenClipboardThread == null || listenClipboardThread.getState() == Thread.State.TERMINATED) {
                listenClipboardThread = new __ClipboardListener();
                listenClipboardThread.setDaemon(true);
                listenClipboardThread.start();
                listenClipboardThread.setMointerOn(listenClipboardChk.isSelected());
                listenClipboardThread.setWatchException(false);
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
        // if (searchEnglishIdTextController.isFocusOwner()) {
        if (JCommonUtil.isOnTop(EnglishSearchUI.this)) {
            return;
        }
        if (brinToTopLock.tryLock()) {
            try {
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
                    JCommonUtil.setLocationToRightBottomCorner(this, (Integer) screenSelectComboBox.getSelectedItem());
                }

                searchEnglishIdTextController.focusSearchEnglishIdText();

                // 開啟時自動查詢
                if (StringUtils.isNotBlank(searchEnglishIdTextController.getText())) {
                    if (autoSearchChk.isSelected()) {
                        queryButtonAction(false);
                    }
                }

                // searchEnglishIdTextController.doFocus();

            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            } finally {
                if (brinToTopLock.isLocked()) {
                    brinToTopLock.unlock();
                }
            }
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

        if (StringUtils.isBlank(offlineConfigText.getText())) {
            System.out.println("DEBUG MODE -----");
            File dir1 = new File("D:/gtu001_dropbox/Dropbox/Apps/gtu001_test/");
            File dir2 = new File("E:/gtu001_dropbox/Dropbox/Apps/gtu001_test/");
            File dir3 = PropertiesUtil.getJarCurrentPath(getClass());
            for (File f1 : new File[] { dir1, dir2, dir3 }) {
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
                    File file = new File(f1 + File.separator + "bak" + max, "exportFileJava.bin");
                    offlineConfigText.setText(file.getAbsolutePath());
                }
            }
        }
    }

    private void loadOfflineConfig() {
        try {
            Properties prop = new Properties();
            this.initOfflineConfigText();

            File file = new File(offlineConfigText.getText());

            if (file.exists()) {
                if (file.getName().equals("exportFileJson.bin")) {
                    if (file.exists()) {
                        System.out.println("read --- " + "exportFileJson.bin");
                        String content = FileUtil.loadFromFile(file, "utf8");
                        JSONArray arry = new JSONArray(content);
                        englishLst = new ArrayList<String>();
                        for (int ii = 0; ii < arry.length(); ii++) {
                            JSONObject obj = (JSONObject) arry.get(ii);
                            String englishId = obj.getString("englishId");
                            String englishDesc = obj.getString("englishDesc");
                            prop.setProperty(englishId, englishDesc);
                            englishLst.add(englishId);
                        }
                        if (searchEnglishIdText_auto != null) {
                            searchEnglishIdText_auto.applyComboxBoxList(englishLst);
                            System.out.println("searchEnglishIdText_auto size : " + englishLst.size());
                        }
                        System.out.println("read --- " + prop.size());
                    }
                }
                if (file.getName().equals("exportFileJava.bin")) {
                    System.out.println("read --- " + "exportFileJava.bin");
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream input = new ObjectInputStream(fis);
                    List<EnglishwordInfoDAO.EnglishWord> list = new ArrayList<EnglishwordInfoDAO.EnglishWord>();
                    englishLst = new ArrayList<String>();
                    try {
                        for (Object readObj = null; (readObj = input.readObject()) != null;) {
                            EnglishwordInfoDAO.EnglishWord word = (EnglishwordInfoDAO.EnglishWord) readObj;
                            String englishId = word.getEnglishId();
                            String englishDesc = word.getEnglishDesc();
                            prop.setProperty(englishId, englishDesc);
                            englishLst.add(englishId);
                        }
                        input.close();
                    } catch (java.io.EOFException ex) {
                    } finally {
                        if (searchEnglishIdText_auto != null) {
                            searchEnglishIdText_auto.applyComboxBoxList(englishLst);
                            System.out.println("searchEnglishIdText_auto size : " + englishLst.size());
                        }
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
            googleTranslateArea_SetTooltip(text, content);
            this.appendMemoryBank(text, content);
            return true;
        } else if (propOtherMap.containsKey(text)) {
            String content = (String) propOtherMap.get(text);
            meaningText.setText(content);
            googleTranslateArea_SetTooltip(text, content);
            this.appendMemoryBank(text, content);
            return true;
        } else {
            meaningText.setText("查無此字!!");
            googleTranslateArea_SetTooltip(text, "查無此字!!");
            return false;
        }
    }

    private void googleTranslateArea_SetTooltip(String text, String meaning) {
        if (StringUtils.isBlank(text) || StringUtils.isBlank(meaning)) {
            googleTranslateArea.setToolTipText(null);
            return;
        }
        List<String> lst = StringUtil4FullChar.fixLength(meaning, 50);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<font color='blue'><b>" + text + "</b></font><br/>");
        for (String s : lst) {
            sb.append(s + "<br/>");
        }
        sb.append("</html>");
        googleTranslateArea.setToolTipText(sb.toString());
    }

    private void queryButtonAction_online(final String text) {
        final EnglishTester_Diectory t = new EnglishTester_Diectory();
        final EnglishTester_Diectory2 t2 = new EnglishTester_Diectory2();

        final AtomicBoolean findOk = new AtomicBoolean(false);

        final Set<String> meaningSet = new HashSet<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // ---------------------------------------------------------
                    final StringBuffer sb = new StringBuffer();
                    if (!StringUtils.contains(text, " ")) {
                        WordInfo info = new WordInfo();
                        try {
                            info = t.parseToWordInfo(text);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        sb.append(text + "\n");
                        sb.append(info.getPronounce() + "\n");
                        sb.append(info.getMeaning() + "\n");
                        sb.append("\n");

                        meaningText.setText(info.getMeaning());
                        googleTranslateArea_SetTooltip(text, info.getMeaning());

                        if (StringUtils.isNotBlank(info.getMeaning())) {
                            findOk.set(true);
                            appendMemoryBank(text, info.getMeaning());
                        }
                    } else {
                        WordInfo2 info2 = t2.parseToWordInfo(text, 1);

                        sb.append(text + "\n");
                        sb.append(info2.getMeaning2() + "\n");
                        sb.append(info2.getMeaningList() + "\n");
                        sb.append("\n");

                        meaningText.setText(info2.getMeaning2());
                        googleTranslateArea_SetTooltip(text, info2.getMeaning2());

                        if (StringUtils.isNotBlank(info2.getMeaning2())) {
                            findOk.set(true);
                            appendMemoryBank(text, info2.getMeaning2());
                        }
                    }

                    propOtherMap.put(text, meaningText.getText());

                    // ---------------------------------------------------------
                    int maxLoop = simpleSentanceChk.isSelected() ? 1 : 10;

                    for (int ii = 0; ii < maxLoop; ii++) {
                        try {
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
                            if (ii == 0) {
                                googleTranslateArea_SetTooltip(text, info2.getMeaning2());
                            }

                            if (findOk.get() == false && StringUtils.isNotBlank(info2.getMeaning2())) {
                                findOk.set(true);
                                appendMemoryBank(text, info2.getMeaning2());
                            }
                        } catch (Exception ex) {
                            break;
                        }
                    }
                    searchResultArea.setText(sb.toString());
                    meaningText.setText(meaningText.getText() + ";" + StringUtils.join(meaningSet, ";"));

                    mEnglishIdDropdownHandler.hidePopup4EnglishIdText();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (findOk.get() == false && offlineProp != null && !offlineProp.isEmpty()) {
                        if (!StringUtils.equalsIgnoreCase(text, notFoundWord.get())) {
                            //
                            List<String> wordLst = similarityWordList(text);
                            int limit = 20;
                            Map<String, String> wordMap = new LinkedHashMap<String, String>();
                            for (int ii = 0; ii < wordLst.size(); ii++) {
                                String t1 = wordLst.get(ii);
                                wordMap.put(t1, offlineProp.getProperty(t1));
                                if (ii >= limit) {
                                    break;
                                }
                            }
                            if (!wordMap.isEmpty()) {
                                if (mSimpleCheckListDlg != null) {
                                    mSimpleCheckListDlg.dispose();
                                }
                                notFoundWord.set(text);
                                mSimpleCheckListDlg = SimpleCheckListDlg.newInstance("相似單字", wordMap, true, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        List<String> wordLst2 = ((SimpleCheckListDlg) e.getSource()).getCheckedList();
                                        if (!wordLst2.isEmpty()) {
                                            String deleteWord = StringUtils.trimToEmpty(searchEnglishIdText_auto.getTextComponent().getText());
                                            try {
                                                writeNewData2(deleteWord, true);
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            try {
                                                writeNewData2(wordLst2.get(0), false);
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                            searchEnglishIdText_auto.getTextComponent().setText(wordLst2.get(0));
                                        }
                                    }
                                }, new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 取得相似度排序
     */
    public List<String> similarityWordList(String englishWord) {
        long startTime = System.currentTimeMillis();

        List<String> rtnList = new ArrayList<String>();
        if (offlineProp == null) {
            return rtnList;
        }

        Map<Double, List<String>> map = new HashMap<Double, List<String>>();
        for (Enumeration enu = offlineProp.keys(); enu.hasMoreElements();) {
            String word1 = (String) enu.nextElement();
            Double d = SimilarityUtil.sim(englishWord, word1);
            List<String> lst = new ArrayList<String>();
            if (map.containsKey(d)) {
                lst = map.get(d);
            }
            lst.add(word1);
            map.put(d, lst);
        }

        List<Double> valueList = new ArrayList<Double>(map.keySet());
        Collections.sort(valueList);
        Collections.reverse(valueList);

        for (Double d : valueList) {
            rtnList.addAll(map.get(d));
        }

        long during = System.currentTimeMillis() - startTime;
        return rtnList;
    }

    private void appendMemoryBank(String word, String desc) {
        desc = getChs2Big5(desc);
        if (StringUtils.isNotBlank(desc) && memory != null && memory.isInitDone()) {
            memory.append(word, HermannEbbinghaus_Memory_Category_ENGLISH, desc);
        }
    }

    private void queryButtonAction(boolean bringToFront) {
        try {
            if (queryButtonActionHolder.get() == false) {
                queryButtonActionHolder.set(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        queryButtonActionHolder.set(false);
                    }
                }, 500);
            } else {
                return;
            }

            mEnglishIdDropdownHandler.hidePopup4EnglishIdText();

            if (!queryButton.isEnabled()) {
                return;
            }
            queryButton.setEnabled(false);

            final String text = StringUtils.trimToEmpty(searchEnglishIdTextController.getText()).toLowerCase();
            if (StringUtils.isBlank(text)) {
                return;
            }

            if (forceOfflineCheckbox.isSelected()) {
                this.queryButtonAction_offline(text);
            } else {
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
            }

            // writeNewData(text);
            int wordSize = writeNewData2(text, false);
            setTitle("生字數 : " + wordSize);

            if (searchEnglishIdTextController.isFocusOwner()) {
                searchEnglishIdTextController.setSelectAll();
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        } finally {
            queryButton.setEnabled(true);
            if (bringToFront) {
                bringToTop();
            }
            mEnglishIdDropdownHandler.hidePopup4EnglishIdText();
        }
    }

    private class EnglishIdDropdownHandler {
        int maxRowCount = 0;
        DefaultComboBoxModel empty;

        EnglishIdDropdownHandler() {
            maxRowCount = searchEnglishIdText.getMaximumRowCount();
            empty = new DefaultComboBoxModel();
        }

        private void restoreDropdownLst() {
            // searchEnglishIdText_auto.applyComboxBoxList(englishLst);
            // searchEnglishIdText.setPopupVisible(true);
        }

        private void hidePopup4EnglishIdText() {
            // searchEnglishIdText.setModel(empty);
            new Timer().schedule(new TimerTask() {
                public void run() {
                    int time = 0;
                    while (true) {
                        if (searchEnglishIdText.isPopupVisible()) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    searchEnglishIdText.hidePopup();
                                    searchEnglishIdText.setPopupVisible(false);
                                }
                            });
                        }
                        time++;
                        if (time >= 30) {
                            break;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }, 0);
        }
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
            if (mouseSelectionChk.isSelected()) {
                return;
            }
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

        private void focus() {
            JCommonUtil.focusComponent(searchEnglishIdText, false, null);
        }

        private JComboBox getCombobox() {
            return searchEnglishIdText;
        }

        private JTextComponent get() {
            return (JTextComponent) searchEnglishIdText.getEditor().getEditorComponent();
        }

        private void focusSearchEnglishIdText() {
            new Timer().schedule(new TimerTask() {
                int repeatTime = 0;

                @Override
                public void run() {
                    // =================================================
                    if (!searchEnglishIdTextController.isFocusOwner()) {
                        boolean isRobotFocus = JCommonUtil.focusComponent(searchEnglishIdTextController.get(), robotFocusChk.isSelected(), new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent paramActionEvent) {
                                mEnglishIdDropdownHandler.hidePopup4EnglishIdText();
                            }//
                        });
                        if (!isRobotFocus) {
                            mEnglishIdDropdownHandler.hidePopup4EnglishIdText();
                        }
                    }
                    searchEnglishIdTextController.getCombobox().hidePopup();
                    searchEnglishIdTextController.setSelectAll();
                    searchEnglishIdTextController.getCombobox().hidePopup();

                    // =================================================
                    if (searchEnglishIdTextController.isFocusOwner() || repeatTime >= 5) {
                        this.cancel();
                    } else {
                        repeatTime++;
                    }
                }
            }, 200, 200);
        }
    }

    private void clearAllInput() {
        searchEnglishIdTextController.setInputText("");
        searchEnglishIdTextController.setText("");
        searchResultArea.setText("");
        meaningText.setText("");
        searchEnglishIdText.requestFocus();
    }

    private String getFixWord(String word) {
        word = StringUtils.trimToEmpty(word);
        word = word.replaceAll("[^a-zA-Z]+$", "");
        return word;
    }

    private int writeNewData2(String word, boolean isDelete) throws IOException {
        File file = getAppendTextFile();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
        Set<String> set = new LinkedHashSet<String>();
        word = getFixWord(word);
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trimToEmpty(line);
            if (StringUtils.isNotBlank(line)) {
                String tmpWord = getFixWord(line);
                if (isDelete) {
                    if (StringUtils.equalsIgnoreCase(tmpWord, word)) {
                        continue;
                    }
                }
                set.add(tmpWord);
            }
        }
        reader.close();
        // 空白太多當成句子不處理
        if (StringUtils.countMatches(StringUtils.trimToEmpty(word), " ") < 4) {
            if (!isDelete) {
                set.add(getFixWord(word));
            }
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
        // String val = propertyBean.getConfigProp().getProperty(NEW_WORD_PATH);
        String val = StringUtils.trimToEmpty(newWordTxtPathText.getText());
        if (StringUtils.isBlank(val)) {
            JCommonUtil._jOptionPane_showMessageDialog_error("未設定new_word.txt路徑, 建立一個在桌面!");
            File newFile = new File(FileUtil.DESKTOP_PATH + File.separator + "new_word.txt");
            val = newFile.getAbsolutePath();
            newWordTxtPathText.setText(val);
            propertyBean.getConfigProp().setProperty(NEW_WORD_PATH, val);
        }
        File file = new File(val);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                file = new File(PropertiesUtil.getJarCurrentPath(getClass()), "new_word.txt");
                try {
                    file.createNewFile();
                } catch (IOException e2) {
                    throw new RuntimeException("getAppendTextFile ERR : " + e2.getMessage(), e2);
                }
            }
        }
        return file;
    }

    private void configSettingBtnAction() {
        try {
            /*
             * if (StringUtils.isBlank(newWordTxtPathText.getText())) { File fff
             * = new File(FileUtil.DESKTOP_PATH, "new_word.txt"); if
             * (!fff.exists()) { fff.createNewFile(); }
             * newWordTxtPathText.setText(fff.getAbsolutePath()); } File f =
             * JCommonUtil.filePathCheck(newWordTxtPathText.getText(),
             * "請輸入new_word.txt路徑", "txt"); if
             * (!f.getName().equals("new_word.txt")) { JCommonUtil.
             * _jOptionPane_showMessageDialog_error("檔案名稱必須為 new_word.txt!");
             * return; }
             * 
             * if (new File(offlineConfigText.getText()).exists()) {
             * propertyBean.getConfigProp().setProperty(OFFLINE_WORD_PATH,
             * offlineConfigText.getText()); }
             * 
             * propertyBean.getConfigProp().put(NEW_WORD_PATH,
             * f.getCanonicalPath());
             * 
             */

            propertyBean.reflectSetConfig(this);
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
            } else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                // if(EnglishSearchUI.gtu.swing.util.JFrameUtil.isVisible(this))
                // {
                // gtu.swing.util.JFrameUtil.setVisible(false,EnglishSearchUI.this);
                // }
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

    private Properties getOfflineProp() {
        return offlineProp;
    }

    private void reviewMemResetBtnAction() {
        boolean resetConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要重設?", "確定要重設?");

        if (!resetConfirm) {
            JCommonUtil._jOptionPane_showMessageDialog_info("重設取消!");
            return;
        }

        boolean resetTimeer = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否重設時間?", "是否重設時間?");
        System.out.println("重設時間!! : " + resetTimeer);

        Date newDate = new Date();
        List<MemData> lst = memory.getAllMemData(true);
        for (MemData d : lst) {
            if (StringUtils.isBlank(d.getRemark())) {
                String meaning = getEnglishMeaning(d.getKey());
                d.setRemark(meaning);
            }
            if (resetTimeer) {
                d.setRegisterTime(newDate);
                d.resetReviewTime();
            }
        }

        memory.overwrite(lst);
        JCommonUtil._jOptionPane_showMessageDialog_info("重設完成!");
    }

    private String getEnglishMeaning(String text) {
        if (offlineProp.isEmpty()) {
            this.loadOfflineConfig();
        }
        text = StringUtils.trimToEmpty(text).toLowerCase();
        if (offlineProp.containsKey(text)) {
            return offlineProp.getProperty(text);
        }
        final EnglishTester_Diectory2 t2 = new EnglishTester_Diectory2();
        WordInfo2 info2 = t2.parseToWordInfo(text, 1);
        return info2.getMeaning2();
    }

    private void reviewMemWaitingListBtnAction() {
        try {
            File tmpFile = File.createTempFile(this.getClass().getSimpleName() + "_", "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss.SSSSS") + ".txt");
            String waitingLstStr = StringUtils.join(memory.getWaitingList(), "\r\n");
            FileUtil.saveToFile(tmpFile, waitingLstStr, "UTF8");
            DesktopUtil.browse(tmpFile.toURL().toString());
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    private void reviewMemConfigBtnAction() {
        try {
            boolean initOk = this.init_HermannEbbinghaus_Memory();

            File memoryFile = null;
            if (!initOk) {
                final String FILE_NAME = "EnglishSearchUI_MemoryBank.properties";
                File dirOrFile = JCommonUtil._jFileChooser_selectFileAndDirectory();
                if (dirOrFile.exists()) {
                    if (dirOrFile.isFile()) {
                        if (dirOrFile.getName().equals(FILE_NAME)) {
                            memoryFile = dirOrFile;
                        } else {
                            memoryFile = new File(dirOrFile.getParentFile(), FILE_NAME);
                        }
                    } else {
                        memoryFile = new File(dirOrFile, FILE_NAME);
                    }

                    propertyBean.getConfigProp().setProperty(MEMORY_BANK_PATH, memoryFile.getAbsolutePath());
                    propertyBean.store();

                    boolean initOk2 = this.init_HermannEbbinghaus_Memory();
                    if (!initOk2) {
                        Validate.isTrue(false, "初始化失敗!! : " + memoryFile);
                    }
                }
            } else {
                memoryFile = new File(propertyBean.getConfigProp().getProperty(MEMORY_BANK_PATH));
                if (!memoryFile.exists()) {
                    Validate.isTrue(false, "檔案不存在!! : " + memoryFile);
                }
            }

            DesktopUtil.browse(memory.getFile().toURL().toString());
        } catch (MalformedURLException e) {
            JCommonUtil.handleException(e);
        }
    }

    private void reviewMemFromFileBtn() {
        try {
            File file = JCommonUtil._jFileChooser_selectFileOnly();
            if (!file.exists()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案有誤!");
                return;
            }
            Properties prop = new Properties();
            prop.load(new FileInputStream(file));

            for (Enumeration<?> enu = prop.keys(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                String meaning = this.getEnglishMeaning(key);
                memory.append(key, HermannEbbinghaus_Memory_Category_ENGLISH, meaning);
            }

            JCommonUtil._jOptionPane_showMessageDialog_info("重設完成!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void offlineReadyLabelAction() {
        initOfflineConfigText();
        loadOfflineConfig();
        if (offlineProp.isEmpty()) {
            offlineReadyLabel.setText("Not Yet!");
        } else {
            offlineReadyLabel.setText("done : " + offlineProp.size());
        }
    }

    @Override
    public void setVisible(boolean b) {
        if (b == false) {
            System.out.println("Hidden  ----------------------------------------");
            // Thread.dumpStack();
            System.out.println("Hidden  ----------------------------------------");
        }
        super.setVisible(b);
    }

    private boolean init_HermannEbbinghaus_Memory() {
        File file = null;
        if (propertyBean.getConfigProp().containsKey(MEMORY_BANK_PATH)) {
            file = new File(propertyBean.getConfigProp().getProperty(MEMORY_BANK_PATH));
            FileUtil.createNewFile(file);
            if (file.isFile() && file.getName().equals("EnglishSearchUI_MemoryBank.properties")) {
                // OK!!
            } else {
                return false;
            }
        } else {
            return false;
        }

        memory.init(file);
        memory.setMemDo(MemDo);
        memory.setOnOffDo(onOffDo);
        memory.setUpdateQueueDo(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<String> lst = (Set<String>) e.getSource();
                dialogObervable.update(lst);
            }
        });

        memory.start();
        return true;
    }

    private void reviewMemResumeBtnAction() {
        try {
            setBounds(100, 100, 540, 347);
            JCommonUtil.setLocationToRightBottomCorner(this, (Integer) screenSelectComboBox.getSelectedItem());
            memory.resume();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
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

    private class MouseMarkQueryHandler {
        boolean orignStatus;

        private MouseMarkQueryHandler() {
        }

        public void before() {
            orignStatus = isMouseQuery();
            setMouseOuery(false);
        }

        public void after() {
            if (orignStatus) {
                setMouseOuery(true);
            }
        }

        private boolean isMouseQuery() {
            return mouseSelectionChk.isSelected() && listenClipboardChk.isSelected();
        }

        private void setMouseOuery(final boolean value) {
            mouseSelectionChk.setSelected(value);
            listenClipboardChk.setSelected(value);
            listenClipboardThread.setMointerOn(value);
        }
    }

    private class DialogTitleUpdaterObervable extends Observable {
        private class DialogHolder implements Observer {
            JDialog dialog;

            DialogHolder(JDialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public void update(Observable o, Object arg) {
                Set<String> lst = (Set<String>) arg;

                Pattern ptn = Pattern.compile("\\[組列\\s*\\:\\s*(\\d+)\\s*\\]");
                Matcher mth = ptn.matcher(this.dialog.getTitle());

                StringBuffer sb = new StringBuffer();
                String newReplaceStr = String.format("[組列 : %d]", lst.size());
                while (mth.find()) {
                    mth.appendReplacement(sb, newReplaceStr);
                }
                mth.appendTail(sb);

                System.out.println("update >>" + sb);

                this.dialog.setTitle(sb.toString());
            }
        }

        List<DialogHolder> holderLst = new ArrayList<DialogHolder>();

        public void add(JDialog o1) {
            DialogHolder holder = new DialogHolder(o1);
            holderLst.add(holder);
            this.addObserver(holder);
        }

        public void update(Set<String> queue) {
            this.setChanged();
            this.notifyObservers(queue);
        }

        public void remove(JDialog dialog) {
            for (int ii = 0; ii < holderLst.size(); ii++) {
                DialogHolder holder = holderLst.get(ii);
                if (holder.dialog == dialog) {
                    this.deleteObserver(holder);
                    holderLst.remove(ii);
                    ii--;
                }
            }
        }
    }

    private void reviewMemoryMergeBtnAction() {
        try {
            File dir = null;
            File dir2 = memory.getFile().getParentFile();
            if (memory.isInitDone() && dir2.isDirectory() && dir2.exists()) {
                dir = dir2;
            } else {
                dir = JCommonUtil._jFileChooser_selectFileAndDirectory();
                if (!dir.isDirectory()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("必須是dropbox目錄!");
                    return;
                }
            }

            List<String> mergeFileLst = new ArrayList<String>();
            int addCount = 0;

            for (File f : dir.listFiles()) {
                if (f.getName().matches("EnglishSearchUI_MemoryBank.*\\.properties")) {
                    HermannEbbinghaus_Memory m1 = new HermannEbbinghaus_Memory();
                    m1.init(f);
                    List<MemData> lst = m1.getAllMemData(false);

                    mergeFileLst.add(f.getName());

                    for (MemData d : lst) {
                        if (!memory.containsKey(d.getKey())) {
                            memory.append(d);
                            addCount++;
                        } else {
                            Float d1 = d.getReviewTimeMin();
                            MemData d2Data = memory.get(d.getKey());
                            if (d2Data != null) {
                                Float d2 = d2Data.getReviewTimeMin();
                                if (d1 < d2) {
                                    memory.append(d2Data);
                                }
                            }
                        }
                    }

                    if (!f.getName().equals("EnglishSearchUI_MemoryBank.properties")) {
                        f.delete();// /media/gtu001/OLD_D/gtu001_dropbox/Dropbox/Apps/gtu001_test/etc_config
                    }
                }
            }

            memory.store();

            JCommonUtil._jOptionPane_showMessageDialog_info(StringUtils.join(mergeFileLst, "\r\n") + "\n加入數 : " + addCount + "\nMerge完成!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private JTextPane getGoogleTranslateArea() {
        return googleTranslateArea;
    }

    private enum TabIndexOrder {
        translate, config, googleTranslate;
    }
}
