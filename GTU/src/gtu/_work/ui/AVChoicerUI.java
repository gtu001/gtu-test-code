package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.constant.PatternCollection;
import gtu.date.DateFormatUtil;
import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.javafx.JavaFxMergeToSwing.JFXPanelToSwing;
import gtu.javafx.mp4player.MediaControl;
import gtu.jdk8.ex1.StreamUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper.MyNativeKeyAdapter;
import gtu.number.NumberUtil;
import gtu.properties.PropertiesGroupUtils_ByKey;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JLabelUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JSwingCommonConfigUtil;
import gtu.swing.util.JTabbedPaneUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTableUtil.TableColorDef;
import gtu.swing.util.JTextFieldUtil;
import gtu.swing.util.JTreeUtil;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import taobe.tec.jcc.JChineseConvertor;

public class AVChoicerUI extends JFrame {

    private static final long serialVersionUID = 1L;

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

    private PropertiesUtilBean config = new PropertiesUtilBean(AVChoicerUI.class);//
    {
        config = JSwingCommonConfigUtil.checkTestingPropertiesUtilBean(config, getClass(), AVChoicerUI.class.getSimpleName());
    }

    private Set<File> clickAvSet = new HashSet<File>();
    private AtomicReference<File> currentAvFile = new AtomicReference<File>();
    private CurrentFileHandler currentFileHandler = new CurrentFileHandler();
    // private List<File> cacheFileList = new ArrayList<File>();
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
    private JCheckBox sameFolderChk;
    private JTextField indicateFolderText;
    private HistoryConfigHandler historyConfigHandler = new HistoryConfigHandler();
    private JTextField historyBetweenConfigText;
    private JCheckBox ignoreHistoryConfigChk;
    private JButton renameBtn;
    private JToggleButton keyboardTriggerBtn;
    private JTabbedPane tabbedPane;

    private static AVChoicerUI mAVChoicerUI;

    private JTable emptyDirList;
    private DropFileChecker mDropFileChecker = null;
    private JList readyMoveLst;
    private JTextField readyMoveToDirText;
    private JTextField readyMoveFromDirText;
    private JButton readyMoveBtn;
    private JTextField emptyDirTargetText;
    private JButton emptyDirBtn;
    private AtomicReference<File> emptyDirHolder = new AtomicReference<File>();

    private static Pattern movPtn = Pattern.compile(PatternCollection.VIDEO_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Pattern jpgPtn = Pattern.compile(PatternCollection.PICTURE_PATTERN, Pattern.CASE_INSENSITIVE);
    private JTree emptyDirTree;
    private JLabel dropHereLbl;
    private JPanel jfxPanel;
    private JFXPanelToSwing mJFXPanelToSwing;
    private JCheckBox emptyDirChkAllChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(AVChoicerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    mAVChoicerUI = new AVChoicerUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, mAVChoicerUI);
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
        setBounds(100, 100, 700, 433);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println("changeTab : " + tabbedPane.getSelectedIndex());
                try {
                    if (tabbedPane.getSelectedIndex() == -1) {
                        return;
                    }
                    tabbedPane.requestFocus();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

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
        replayBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (JMouseEventUtil.buttonLeftClick(1, e)) {
                    currentFileHandler.replay();
                } else if (JMouseEventUtil.buttonRightClick(1, e)) {
                    JPopupMenuUtil.newInstance(replayBtn)//
                            .addJMenuItem("normal", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    currentFileHandler.replay_for_mplayer("");
                                }
                            })//
                            .addJMenuItem("mirror", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    currentFileHandler.replay_for_mplayer("mirror");
                                }
                            })//
                            .addJMenuItem("90", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    currentFileHandler.replay_for_mplayer("90");
                                }
                            })//
                            .addJMenuItem("180", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    currentFileHandler.replay_for_mplayer("180");
                                }
                            })//
                            .addJMenuItem("270", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    currentFileHandler.replay_for_mplayer("270");
                                }
                            })//
                            .addJMenuItem("180_mirror", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    currentFileHandler.replay_for_mplayer("180_mirror");
                                }
                            })//
                            .applyEvent(e).show();
                }
            }
        });

        renameBtn = new JButton("改名");
        renameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // currentFileHandler.rename();
            }
        });
        renameBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (JMouseEventUtil.buttonLeftClick(1, arg0)) {
                    currentFileHandler.rename();
                } else {
                    JPopupMenuUtil.newInstance(renameBtn)//
                            .addJMenuItem("超棒", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    currentFileHandler.awsomeRename();
                                }
                            })//
                            .addJMenuItem("開啟封面", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    currentFileHandler.openFindCoverPic();
                                }
                            })//
                            .applyEvent(arg0).show();
                }
            }
        });

        panel_7.add(renameBtn);
        panel_7.add(replayBtn);

        keyboardTriggerBtn = new JToggleButton("Off");
        keyboardTriggerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardTriggerBtn.setText(keyboardTriggerBtn.isSelected() ? "On" : "Off");
            }
        });
        panel_7.add(keyboardTriggerBtn);

        JPanel panel_8 = new JPanel();
        panel.add(panel_8, BorderLayout.WEST);

        JPanel panel_9 = new JPanel();
        panel.add(panel_9, BorderLayout.SOUTH);

        ignoreHistoryConfigChk = new JCheckBox("忽略重播間隔");
        panel_9.add(ignoreHistoryConfigChk);

        sameFolderChk = new JCheckBox("播放同目錄");
        panel_9.add(sameFolderChk);

        indicateFolderText = new JTextField();
        indicateFolderText.setToolTipText("指定播放某目錄");
        JCommonUtil.jTextFieldSetFilePathMouseEvent(indicateFolderText, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                triggerIndicateFolderTextFoucsLost();
            }
        });
        indicateFolderText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                triggerIndicateFolderTextFoucsLost();
            }
        });
        panel_9.add(indicateFolderText);
        indicateFolderText.setColumns(10);

        sameFolderChk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choiceAVBtn.setText(!sameFolderChk.isSelected() ? "隨機撥放" : "播放同目錄");
            }
        });

        JPanel panel_10 = new JPanel();
        panel.add(panel_10, BorderLayout.EAST);

        choiceAVBtn = new JButton("隨機撥放");
        choiceAVBtn.setFont(new Font("新細明體", Font.PLAIN, 50));
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

        JLabel lblNewLabel_4 = new JLabel("重播間隔(日)");
        panel_2.add(lblNewLabel_4, "2, 10, right, default");

        historyBetweenConfigText = new JTextField();
        panel_2.add(historyBetweenConfigText, "4, 10, fill, default");
        historyBetweenConfigText.setColumns(10);

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
        JTextFieldUtil.applyDeaultSettings(moveToText);
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
        JCommonUtil.applyDropFiles(moveToList, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> droppedFiles = (List<File>) e.getSource();
                if (!droppedFiles.isEmpty()) {
                    moveToHandler.add(droppedFiles);
                }
            }
        });

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

                    // 設定目的 dir
                    readyMoveToDirText.setText(dir.getAbsolutePath());
                }
            }
        });

        JPanel panel_18 = new JPanel();
        tabbedPane.addTab("目錄檢視", null, panel_18, null);
        panel_18.setLayout(new BorderLayout(0, 0));

        JPanel panel_19 = new JPanel();
        panel_18.add(panel_19, BorderLayout.NORTH);

        JButton dirCheckListResetBtn = new JButton("重設");
        dirCheckListResetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dirCheckListResetBtnAction();
            }
        });
        panel_19.add(dirCheckListResetBtn);

        JLabel lblNewLabel_3 = new JLabel("檢視目錄");
        panel_19.add(lblNewLabel_3);

        dirCheckText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(dirCheckText, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dirCheckTextActionPerformed(null);
            }
        });
        dirCheckText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                dirCheckTextActionPerformed(null);
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
        dirCheckList.setModel(JListUtil.createModel());
        dirCheckList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (JMouseEventUtil.buttonLeftClick(2, arg0)) {
                    dirCheckListMouseClicked();
                }
            }
        });

        dirCheckList.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 127) {
                    if (dirCheckList.getSelectedIndices() != null && dirCheckList.getSelectedIndices().length > 1) {
                        DefaultListModel model = (DefaultListModel) dirCheckList.getModel();
                        for (int ii = 0; ii < model.getSize(); ii++) {
                            model.removeElementAt(ii);
                        }
                        return;
                    }
                }
                JListUtil.newInstance(dirCheckList).defaultJListKeyPressed(e, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileZ f = (FileZ) e.getSource();
                        boolean deleteOk = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除:" + f.file, "刪除??");
                        if (deleteOk) {
                            f.file.delete();
                            e.setSource(true);
                        } else {
                            e.setSource(false);
                        }
                    }
                });
            }
        });
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                dirCheckList.repaint();
            }
        });

        panel_18.add(JCommonUtil.createScrollComponent(dirCheckList), BorderLayout.CENTER);

        JPanel panel_28 = new JPanel();
        tabbedPane.addTab("淨空BT資料夾", null, panel_28, null);
        panel_28.setLayout(new BorderLayout(0, 0));

        JPanel panel_29 = new JPanel();
        panel_28.add(panel_29, BorderLayout.NORTH);

        emptyDirBtn = new JButton("淨空搬出");
        emptyDirBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emptyDirBtnAction();
            }
        });

        emptyDirChkAllChk = new JCheckBox("");
        emptyDirChkAllChk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean chkAll = emptyDirChkAllChk.isSelected();
                DefaultTableModel model = (DefaultTableModel) emptyDirList.getModel();
                for (int ii = 0; ii < model.getRowCount(); ii++) {
                    model.setValueAt(chkAll, ii, 0);
                }
                emptyDirList.repaint();
            }
        });
        panel_29.add(emptyDirChkAllChk);

        emptyDirTargetText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(emptyDirTargetText, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                if (files != null && !files.isEmpty() && files.get(0).isDirectory()) {
                    config.reflectSetConfig(AVChoicerUI.this);
                    config.store();
                }
            }
        });
        panel_29.add(emptyDirTargetText);
        emptyDirTargetText.setColumns(10);
        panel_29.add(emptyDirBtn);

        dropHereLbl = new JLabel("Drop Here");
        JLabelUtil.applyDropDirPath(dropHereLbl, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = (File) e.getSource();
                if (file != null && file.exists()) {
                    emptyDirListCheckDir(file);
                }
            }
        });

        panel_29.add(dropHereLbl);

        JPanel panel_30 = new JPanel();
        panel_28.add(panel_30, BorderLayout.WEST);

        emptyDirTree = new JTree();
        panel_30.add(JCommonUtil.createScrollComponent(emptyDirTree));
        JCommonUtil.applyDropFiles(emptyDirTree, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                File dir = (File) files.get(0);
                JTreeUtil.newInstance(emptyDirTree).fileSystem(dir);
            }
        });
        emptyDirTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultMutableTreeNode node = JTreeUtil.newInstance(emptyDirTree).getSelectItem();
                if (node.getParent() == null) {
                    return;
                }
                JTreeUtil.JFile jfile = (JTreeUtil.JFile) JTreeUtil.newInstance(emptyDirTree).getSelectItemUserObject();
                File file = jfile.getFile();
                if (file.isDirectory()) {
                    emptyDirListCheckDir(file);
                }
            }
        });

        JPanel panel_31 = new JPanel();
        panel_28.add(panel_31, BorderLayout.EAST);

        JPanel panel_32 = new JPanel();
        panel_28.add(panel_32, BorderLayout.SOUTH);

        emptyDirList = new JTable();
        JCommonUtil.applyDropFiles(emptyDirList, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                if (files.isEmpty()) {
                    return;
                }
                File file = files.get(0);
                if (file != null && file.exists()) {
                    emptyDirListCheckDir(file);
                }
            }
        });
        JCommonUtil.applyDropFiles(panel_28, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                if (files.isEmpty()) {
                    return;
                }
                File file = files.get(0);
                if (file != null && file.exists()) {
                    emptyDirListCheckDir(file);
                }
            }
        });

        initEmptyDirList();

        panel_28.add(JCommonUtil.createScrollComponent(emptyDirList), BorderLayout.CENTER);

        JPanel panel_23 = new JPanel();
        tabbedPane.addTab("準備移動", null, panel_23, null);
        panel_23.setLayout(new BorderLayout(0, 0));

        JPanel panel_24 = new JPanel();
        panel_23.add(panel_24, BorderLayout.NORTH);

        readyMoveFromDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(readyMoveFromDirText, true);
        panel_24.add(readyMoveFromDirText);
        readyMoveFromDirText.setColumns(10);

        JButton readyMoveFromBtn = new JButton("Add");
        panel_24.add(readyMoveFromBtn);
        readyMoveFromBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File moveFromFile = JCommonUtil.filePathCheck(readyMoveFromDirText.getText(), "來源檔案有誤", false);
                    DefaultListModel model = (DefaultListModel) readyMoveLst.getModel();
                    model.addElement(new MoveFileZ(moveFromFile));
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        JLabel lblNewLabel_5 = new JLabel("目的地");
        panel_24.add(lblNewLabel_5);

        readyMoveToDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(readyMoveToDirText, true);
        panel_24.add(readyMoveToDirText);
        readyMoveToDirText.setColumns(10);

        readyMoveBtn = new JButton("開始移動");
        readyMoveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean startMove = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否開始移動？", "Start Move");
                if (startMove) {
                    readyMoveBtnAction();
                    config.reflectSetConfig(AVChoicerUI.this);
                    config.store();
                }
            }
        });
        panel_24.add(readyMoveBtn);

        JPanel panel_25 = new JPanel();
        panel_23.add(panel_25, BorderLayout.WEST);

        JPanel panel_26 = new JPanel();
        panel_23.add(panel_26, BorderLayout.EAST);

        JPanel panel_27 = new JPanel();
        panel_23.add(panel_27, BorderLayout.SOUTH);

        readyMoveLst = new JList();
        readyMoveLst.setModel(JListUtil.createModel());
        JCommonUtil.applyDropFiles(readyMoveLst, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel model = (DefaultListModel) readyMoveLst.getModel();
                List<File> lst = (List<File>) e.getSource();
                for (File f : lst) {
                    model.addElement(new MoveFileZ(f));
                }
            }
        });
        panel_23.add(JCommonUtil.createScrollComponent(readyMoveLst), BorderLayout.CENTER);
        readyMoveLst.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                JListUtil.newInstance(readyMoveLst).defaultJListKeyPressed(e);
            }
        });
        readyMoveLst.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (JMouseEventUtil.buttonLeftClick(2, e)) {
                    MoveFileZ file = (MoveFileZ) JListUtil.getLeadSelectionObject(readyMoveLst);
                    if (file != null) {
                        File openToFile = file.file;
                        if (file.moveToFile != null && file.moveToFile.exists()) {
                            openToFile = file.moveToFile;
                        }
                        if (file instanceof MoveFileBatch) {
                            ((MoveFileBatch) file).showLog();
                            File fromDir = ((MoveFileBatch) file).emptyDirHolder;
                            if (fromDir.exists()) {
                                DesktopUtil.browseFileDirectory(fromDir);
                            } else {
                                DesktopUtil.browseFileDirectory(openToFile);
                            }
                        } else {
                            DesktopUtil.browseFileDirectory(openToFile);
                        }
                    }
                }
            }
        });

        JCommonUtil.applyDropFiles(dirCheckList, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                List<File> files = (List<File>) arg0.getSource();
                dirCheckTextActionPerformed(files);
            }
        });

        JCommonUtil.applyDropFiles(choiceAVBtn, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> lst = (List<File>) e.getSource();
                if (lst != null && !lst.isEmpty()) {
                    if (lst.size() == 1) {
                        File file = lst.get(0);
                        if (file.isFile()) {
                            playAvFile(historyConfigHandler.getPlayFile(new Callable<File>() {
                                @Override
                                public File call() throws Exception {
                                    return file;
                                }
                            }));
                        } else {
                            dirCheckText.setText(file.getAbsolutePath());
                            dirCheckTextActionPerformed(null);
                            JTabbedPaneUtil.newInst(tabbedPane).setSelectedIndexByTitle("目錄檢視");

                            // 設定當前目錄
                            indicateFolderText.setText(file.getAbsolutePath());
                            triggerIndicateFolderTextFoucsLost();
                        }
                    } else {
                        // cacheFileList = new ArrayList<File>();
                        // cacheFileList.addAll(dirCheckTextActionPerformed(lst));
                        dirCheckTextActionPerformed(lst);
                    }
                }
            }
        });

        jfxPanel = new JPanel();
        tabbedPane.addTab("影片", null, jfxPanel, null);
        jfxPanel.setLayout(new BorderLayout(0, 0));

        {
            mJFXPanelToSwing = new JFXPanelToSwing() {
                @Override
                public Scene createScene(JFXPanel fxPanel) {
                    Scene scene = new Scene(new Pane(), javafx.scene.paint.Color.ALICEBLUE);
                    return scene;
                }

                @Override
                public void appendToJFrame(Container frame, JFXPanel fxPanel) {
                    frame.add(fxPanel, BorderLayout.CENTER);
                }
            };
            mJFXPanelToSwing.execute(jfxPanel);
            // resize
            this.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    if (mJFXPanelToSwing.getRoot() != null && mJFXPanelToSwing.getRoot() instanceof MediaControl) {
                        MediaControl mediaControl = ((MediaControl) mJFXPanelToSwing.getRoot());
                        mediaControl.resize();
                    }
                }
            });
        }

        // 000000000000000000000000000000000000000000000000000000000000000000000000000000000

        config.reflectInit(this);
        initAvDirList();
        avExeConfigHandler = new AvExeConfigHandler();
        avExeConfigHandler.nextConfig();

        initKeyboardAndMouseHandler();

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setJFrameIcon(this, "resource/images/ico/pornHub.ico");
        JCommonUtil.defaultToolTipDelay();

        trayUtil = HideInSystemTrayHelper.newInstance();
        trayUtil.apply(this);

        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);

        panel_16.add(jFrameRGBColorPanel.getToggleButton(false));

        System.out.println("file.encoding : " + System.getProperty("file.encoding"));
    }

    // 播放影片
    public void executeAVMovieToPlayer(File movie) {
        try {
            System.out.println("[executeAVMovieToPlayer] ------------ start ");
            double volume = 1d;
            if (mJFXPanelToSwing.getRoot() != null && mJFXPanelToSwing.getRoot() instanceof MediaControl) {
                MediaControl mediaControl = ((MediaControl) mJFXPanelToSwing.getRoot());
                volume = mediaControl.getVolume();
                mediaControl.dispose();
            }
            String currentFile = movie.toURI().toString();
            System.out.println(currentFile);
            Media pick = new Media(currentFile);
            MediaPlayer player = new MediaPlayer(pick);
            MediaControl mMediaControl = new MediaControl(player);
            mMediaControl.setAutoStart(true);
            mMediaControl.setVolume(volume);
            mMediaControl.setPlayRate(1d);
            mJFXPanelToSwing.setRoot(mMediaControl);
            JTabbedPaneUtil.newInst(tabbedPane).setSelectedIndexByTitle("影片");
            System.out.println("[executeAVMovieToPlayer] ------------ end ");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void triggerIndicateFolderTextFoucsLost() {
        File avFolder = new File(indicateFolderText.getText());
        if (avFolder != null && avFolder.exists() && avFolder.isDirectory()) {
            currentAvFile.set(avFolder);
            // this.cacheFileList = this.getSubFolderAvLst(avFolder);
            dirCheckTextActionPerformed(this.getSubFolderAvLst(avFolder));
            resetSameFolderChk(true);
        } else {
            indicateFolderText.setText("");
            resetSameFolderChk(false);
        }
    }

    private class MoveToHandler {
        private PropertiesUtilBean moveConfig = new PropertiesUtilBean(MoveToHandler.class, AVChoicerUI.class.getSimpleName() + "_" + MoveToHandler.class.getSimpleName());
        // private PropertiesUtilBean moveConfig = new PropertiesUtilBean(new
        // File("/media/gtu001/OLD_D/my_tool/AVChoicerUI_MoveToHandler_config.properties"));

        private void add(List<File> dropLst) {
            try {
                for (File dir : dropLst) {
                    if (!dir.isDirectory() || !dir.exists()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("目錄不存在:" + dir);
                        break;
                    }
                    moveConfig.getConfigProp().setProperty(dir.getAbsolutePath(), dir.getAbsolutePath());
                }
                reload();
                moveToText.setText("");
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

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
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                StringBuffer sb1 = new StringBuffer();
                                Object[] p1 = fileMove(currentFileHandler.getAvFile(), getMoveTargetFile(dir, currentFileHandler.getAvFile()));
                                sb1.append("移動結果 : \r\n");
                                sb1.append("檔案1 : " + Arrays.toString(p1) + "\r\n");
                                for (File jpg : currentFileHandler.getJpgFile()) {
                                    Object[] p2 = fileMove(jpg, getMoveTargetFile(dir, jpg));
                                    sb1.append("檔案1 : " + Arrays.toString(p2) + "\r\n");
                                }
                                JCommonUtil._jOptionPane_showMessageDialog_info(sb1.toString());
                            } catch (Exception e) {
                                JCommonUtil.handleException(e);
                            }
                        }
                    });
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

                FileUtil.moveFileByBat(srcFile, toFile);
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

    private List<File> getCacheFileLst() {
        List<File> lst = new ArrayList<File>();
        DefaultListModel model = (DefaultListModel) dirCheckList.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            FileZ f = (FileZ) model.get(ii);
            if (movPtn.matcher(f.file.getName()).find()) {
                lst.add(f.file);
            }
        }
        return lst;
    }

    private File getRandomAvFile() {
        List<File> cacheFileList = getCacheFileLst();
        if (cacheFileList == null || cacheFileList.isEmpty()) {
            cacheFileList = new ArrayList<File>();
            final List<File> cacheFileList2 = cacheFileList;
            DefaultListModel model = (DefaultListModel) avDirList.getModel();
            IntStream.range(0, model.size())//
                    .mapToObj(i -> (File) model.getElementAt(i))//
                    .forEach(file -> {
                        FileUtil.searchFileMatchs(file, ".*\\." + PatternCollection.VIDEO_PATTERN, cacheFileList2);
                    });
        }

        System.out.println("cacheFileList.size() = " + cacheFileList.size());

        File choiceFile = null;
        List<File> cloneLst = new ArrayList<File>();

        File avDir = null;
        if (sameFolderChk.isSelected()) {
            if (currentAvFile.get() != null && currentAvFile.get().exists()) {
                if (currentAvFile.get().getName().matches(".*\\." + PatternCollection.VIDEO_PATTERN)) {
                    avDir = currentAvFile.get().getParentFile();
                } else {
                    avDir = currentAvFile.get();
                }
            } else if (StringUtils.isNotBlank(indicateFolderText.getText())) {
                avDir = currentAvFile.get().isFile() ? //
                        currentAvFile.get().getParentFile() : currentAvFile.get();
            }
        }

        if (avDir != null) {
            cloneLst = getSubFolderAvLst(avDir);
        } else {
            cloneLst.addAll(cacheFileList);
        }

        int cacheCloneLstSize = cloneLst.size();
        cloneLst.removeAll(clickAvSet);

        if (!cloneLst.isEmpty()) {
            choiceFile = cloneLst.get(new Random().nextInt(cloneLst.size()));
        } else {
            JCommonUtil._jOptionPane_showMessageDialog_info("清單已跑完一輪");
            choiceFile = cacheFileList.get(new Random().nextInt(cacheFileList.size()));
            clickAvSet.clear();
            resetSameFolderChk(false);
        }

        // 設定以看數
        movCountLabel.setText(String.format("以看%d, 總數%d", clickAvSet.size(), cacheCloneLstSize));

        currentAvFile.set(choiceFile);
        clickAvSet.add(choiceFile);
        return choiceFile;
    }

    private List<File> getSubFolderAvLst(File folder) {
        List<File> folderFileLst = new ArrayList<File>();
        FileUtil.searchFileMatchs(folder, ".*\\." + PatternCollection.VIDEO_PATTERN, folderFileLst);
        return folderFileLst;
    }

    private void resetSameFolderChk(boolean isSelected) {
        sameFolderChk.setSelected(isSelected);
        JCommonUtil.triggerButtonActionPerformed(sameFolderChk);
    }

    private class CurrentFileHandler {
        private AtomicReference<File> tempFile = new AtomicReference<File>();
        private AtomicReference<List<File>> tempJpgFile = new AtomicReference<List<File>>();
        private AtomicReference<File> tempDir = new AtomicReference<File>();

        // private File __findJpgFile(File avFile) {
        // try {
        // String currentFileName = FileUtil.getNameNoSubName(avFile);
        // Pattern ptn = Pattern.compile(currentFileName,
        // Pattern.CASE_INSENSITIVE);
        // return Stream.of(avFile.getParentFile().listFiles()).filter(f ->
        // f.getName().endsWith(".jpg")).filter(f -> {
        // String jpgName = FileUtil.getNameNoSubName(f);
        // Matcher mth = ptn.matcher(jpgName);
        // return mth.find();
        // }).findAny().orElse(null);
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // return null;
        // }
        // }

        public void openFindCoverPic() {
            if (this.tempJpgFile.get() != null && !this.tempJpgFile.get().isEmpty()) {
                for (File f : this.tempJpgFile.get()) {
                    try {
                        Desktop.getDesktop().open(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public File getAvFile() {
            return tempFile.get();
        }

        public List<File> getJpgFile() {
            return tempJpgFile.get();
        }

        private void setFile(File avFile) {
            tempFile.set(avFile);
            tempJpgFile.set(findCoverPic(avFile));// 找圖片
            try {
                int val = tempJpgFile.get().size();
                if (val != 0) {
                    renameBtn.setText("" + val);
                } else {
                    renameBtn.setText("改名");
                }
            } catch (Exception ex) {
                renameBtn.setText("改名");
            }
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
                totalSize = getCacheFileLst().size();
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
            final File file = tempFile.get();
            if (file == null) {
                System.out.println("[deleteFile] 檔案為null");
                return;
            }
            if (!file.exists()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                return;
            }
            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除此檔 : " + file, "刪除檔案!");
            if (result) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtil.deleteFileToRecycleBin(file, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    boolean delResult = (boolean) e.getSource();
                                    trayUtil.displayMessage(delResult ? "刪除成功!" : "刪除失敗", file.toString(), MessageType.INFO);
                                    deleteAVFileLabel.setText(file.exists() ? "Done!" : "NotDone!");
                                    setCountLabel();
                                    // resetCacheFileList();
                                    removeFromCacheLst(file);
                                    dirCheckTextActionPerformed(null);
                                }
                            });
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                }).start();
            }
        }

        private void removeFromCacheLst(File removeFile) {
            List<File> cacheFileList = getCacheFileLst();
            if (cacheFileList != null) {
                for (int ii = 0; ii < cacheFileList.size(); ii++) {
                    if (cacheFileList.get(ii) == removeFile) {
                        cacheFileList.remove(ii);
                        break;
                    }
                }
            }
        }

        private void openContainDir() {
            try {
                // File dir = tempDir.get();
                // if (!dir.exists()) {
                // JCommonUtil._jOptionPane_showMessageDialog_error("目錄不存在!");
                // return;
                // }
                // Desktop.getDesktop().open(dir);
                DesktopUtil.browseFileDirectory(getAvFile());
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        private AtomicReference<String> latestReplayForMplayerStatus = new AtomicReference<String>();

        private void replay_for_mplayer(String rotate) {
            playAvFile_for_VLC(historyConfigHandler.getPlayFile(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return tempFile.get();
                }
            }), rotate);
            dirCheckList.repaint();
            latestReplayForMplayerStatus.set(rotate);
        }

        private void replay() {
            playAvFile(historyConfigHandler.getPlayFile(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return tempFile.get();
                }
            }));
            dirCheckList.repaint();
            latestReplayForMplayerStatus.set("");
        }

        private void replayDefault() {
            if (StringUtils.isNoneBlank(latestReplayForMplayerStatus.get())) {
                replay_for_mplayer(latestReplayForMplayerStatus.get());
            } else {
                replay();
            }
        }

        private void rename() {
            try {
                if (tempFile == null) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return;
                }
                File file = tempFile.get();
                if (file == null || !file.exists()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return;
                }
                String name = FileUtil.getNameNoSubName(file);
                String subName = FileUtil.getSubName(file);
                name = JCommonUtil._jOptionPane_showInputDialog("改名", name);
                if (StringUtils.isNotBlank(name)) {
                    name = JChineseConvertor.getInstance().s2t(name);
                    File renmaeToFile = new File(file.getParentFile(), name + "." + subName);
                    if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定改名為\n" + renmaeToFile.getAbsolutePath(), renmaeToFile.getName())) {
                        boolean result = file.renameTo(renmaeToFile);
                        JCommonUtil._jOptionPane_showMessageDialog_info("改名 " + (result ? "成功" : "失敗") + " : " + renmaeToFile);
                        if (result) {
                            tempFile.set(renmaeToFile);
                        }
                    }
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        private void awsomeRename() {
            try {
                if (tempFile == null) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return;
                }
                File file = tempFile.get();
                if (file == null || !file.exists()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return;
                }
                String name = FileUtil.getNameNoSubName(file);
                String subName = FileUtil.getSubName(file);
                if (name.startsWith("超棒_")) {
                    return;
                }
                name = "超棒_" + name;
                if (StringUtils.isNotBlank(name)) {
                    name = JChineseConvertor.getInstance().s2t(name);
                    File renmaeToFile = new File(file.getParentFile(), name + "." + subName);
                    if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定改名為\n" + renmaeToFile.getAbsolutePath(), renmaeToFile.getName())) {
                        boolean result = file.renameTo(renmaeToFile);
                        JCommonUtil._jOptionPane_showMessageDialog_info("改名 " + (result ? "成功" : "失敗") + " : " + renmaeToFile);
                        if (result) {
                            tempFile.set(renmaeToFile);
                        }
                    }
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        private List<File> findCoverPic(File avFile) {
            try {
                if (avFile == null) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return Collections.EMPTY_LIST;
                }
                File file = avFile;
                if (file == null || !file.exists()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                    return Collections.EMPTY_LIST;
                }
                String name = FileUtil.getNameNoSubName(file);

                Pattern coverJpgPtn = Pattern.compile("([a-zA-Z]{2,5})[\\-\\_]?([0-9]{2,5})");
                Matcher mth = coverJpgPtn.matcher(name);
                if (!mth.find()) {
                    // JCommonUtil._jOptionPane_showMessageDialog_error("無法匹配名子");
                    return Collections.EMPTY_LIST;
                }
                String cover1 = mth.group(1).toLowerCase();
                String cover2 = mth.group(2).toLowerCase();

                if (file.getParentFile() != null) {
                    List<File> coverLst = new ArrayList<File>();
                    File[] lst = file.getParentFile().listFiles();
                    if (lst != null) {
                        for (File jpg : lst) {
                            String jpgName = jpg.getName().toLowerCase();
                            if (jpgName.matches(".*\\." + PatternCollection.PICTURE_PATTERN)) {
                                if (jpgName.contains(cover1) && jpgName.contains(cover2)) {
                                    coverLst.add(jpg);
                                }
                            }
                        }
                        if (!coverLst.isEmpty()) {
                            return coverLst;
                        }
                    }
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
            return Collections.EMPTY_LIST;
        }

        public void playNextFile() {
            FileZ filez = null;
            DefaultListModel model = (DefaultListModel) dirCheckList.getModel();
            if (tempFile.get() == null) {
                if (model.getSize() != 0) {
                    filez = (FileZ) model.getElementAt(0);
                }
            } else {
                File f1 = tempFile.get();
                A: for (int ii = 0; ii < model.getSize(); ii++) {
                    FileZ f = (FileZ) model.getElementAt(ii);
                    if (f1.equals(f.file)) {
                        if (ii + 1 <= model.getSize() - 1) {
                            filez = (FileZ) model.getElementAt(ii + 1);
                            break A;
                        }
                    }
                }
                if (filez == null && model.getSize() != 0) {
                    filez = (FileZ) model.getElementAt(0);
                }
            }
            if (filez != null) {
                tempFile.set(filez.file);
                replay();
            }
        }
    }

    private void resetCacheFileList() {
        dirCheckList.setModel(new DefaultListModel());
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
        playAvFile(historyConfigHandler.getPlayFile(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return getRandomAvFile();
            }
        }));
        dirCheckList.repaint();
    }

    private class HistoryConfigHandler {
        private SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private PropertiesUtilBean historyConfig = new PropertiesUtilBean(AVChoicerUI.class, "AVChoicerUI_History");

        private boolean isAlreadyPlayInRecent(File avFile) {
            if (ignoreHistoryConfigChk.isSelected()) {
                return false;
            }

            double days = 3D;
            try {
                days = Double.parseDouble(historyBetweenConfigText.getText());
            } catch (Exception ex) {
                historyBetweenConfigText.setText("3");
            }

            if (!historyConfig.getConfigProp().containsKey(avFile.getName())) {
                return false;
            } else {
                String timestamp = historyConfig.getConfigProp().getProperty(avFile.getName());
                try {
                    Date d = SDF.parse(timestamp);
                    if ((System.currentTimeMillis() - d.getTime()) > (days * 24 * 60 * 60 * 1000)) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (ParseException e) {
                    return false;
                }
            }
        }

        private void setPlayFile(File avFile) {
            historyConfig.getConfigProp().setProperty(avFile.getName(), SDF.format(new Date()));
            historyConfig.store();
        }

        private File getPlayFile(Callable fetchFileCall) {
            try {
                int repeatCount = 0;
                while (true) {
                    File file = (File) fetchFileCall.call();
                    boolean falseIsOk = historyConfigHandler.isAlreadyPlayInRecent(file);
                    if (!falseIsOk) {
                        setPlayFile(file);
                        return file;
                    }
                    repeatCount++;
                    if (repeatCount >= 5) {
                        setPlayFile(file);
                        return file;
                    }
                }
            } catch (Exception e) {
                JCommonUtil.handleException(e);
                throw new RuntimeException("HistoryConfigHandler ERR : " + e.getMessage(), e);
            }
        }
    }

    private void playAvFile_for_VLC(File avFile, String rotate) {
        try {
            File exe = new File(avExeText.getText());
            String commandFormat = "\"%3$s\" %1$s \"%2$s\" ";
            String encoding = avExeEncodeText.getText();

            // --transform-type {90,180,270,hflip,vflip}
            String rotateString = "";
            switch (rotate) {
            case "mirror":
                rotateString = " --vout-filter=transform --transform-type=hflip --video-filter \"transform{true}\" ";
                break;
            case "90":
                rotateString = " --vout-filter=transform --transform-type=90 --video-filter \"transform{true}\" ";
                break;
            case "180":
                rotateString = " --vout-filter=transform --transform-type=180 --video-filter \"transform{true}\" ";
                break;
            case "270":
                rotateString = " --vout-filter=transform --transform-type=270 --video-filter \"transform{true}\" ";
                break;
            case "180_mirror":
                rotateString = " --vout-filter=transform --transform-type=vflip --video-filter \"transform{true}\" ";
                break;
            default:
                rotateString = " ";
                break;
            }

            currentFileHandler.setFile(avFile);
            System.out.println("檔案存在 : " + avFile.exists() + " -> " + avFile);

            RuntimeBatPromptModeUtil t = RuntimeBatPromptModeUtil.newInstance();
            String command = String.format(commandFormat, rotateString, avFile.getCanonicalPath(), exe);
            System.out.println("CMD ==> " + command);
            System.out.println("encoding ==> " + encoding);
            t.command(command);
            t.runInBatFile(true);

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

    private void playAvFile_for_mplayer(File avFile, String rotate) {
        try {
            // File exe = new File(avExeText.getText());
            String commandFormat = "mplayer %1$s \"%2$s\" ";
            String encoding = avExeEncodeText.getText();

            String rotateString = "";
            switch (rotate) {
            case "mirror":
                rotateString = " -zoom -vf-add mirror ";
                break;
            case "90":
                rotateString = " -zoom -vf-add rotate=1 ";
                break;
            case "180":
                rotateString = " -zoom -vf-add flip ";
                break;
            case "270":
                rotateString = " -zoom -vf-add mirror,rotate=1 ";
                break;
            case "180_mirror":
                rotateString = " -zoom -vf-add mirror,flip ";
                break;
            default:
                rotateString = " -zoom ";
                break;
            }

            currentFileHandler.setFile(avFile);
            System.out.println("檔案存在 : " + avFile.exists() + " -> " + avFile);

            RuntimeBatPromptModeUtil t = RuntimeBatPromptModeUtil.newInstance();
            String command = String.format(commandFormat, rotateString, avFile.getCanonicalPath());
            System.out.println("CMD ==> " + command);
            System.out.println("encoding ==> " + encoding);
            t.command(command);
            t.runInBatFile(true);

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

    private void playAvFile(File avFile) {
        try {
            currentFileHandler.setFile(avFile);
            System.out.println("檔案存在 : " + avFile.exists() + " -> " + avFile);
            if (StringUtils.isBlank(avExeText.getText())) {
                executeAVMovieToPlayer(avFile);
                return;
            }

            // -Dfile.encoding=UTF-8
            // File exe = getMediaPlayerExe();
            File exe = new File(avExeText.getText());
            String commandFormat = avExeFormatText.getText();
            String encoding = avExeEncodeText.getText();

            RuntimeBatPromptModeUtil t = RuntimeBatPromptModeUtil.newInstance();
            String command = String.format(commandFormat, exe, avFile.getCanonicalPath());
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
        private final String AV_HISTORY_BETWEEN_DAY_CONFIG_KEY = "avHistoryBetweenDayConfig";

        public void AvExeConfigHandler() {
        }

        private void nextConfig() {
            try {
                Map<String, String> config = avExeConfig.loadConfig();

                avExeAliasText.setText(config.get(PropertiesGroupUtils_ByKey.SAVE_KEYS));
                avExeText.setText(config.get(AV_EXE_KEY));
                avExeFormatText.setText(config.get(AV_EXE_FORMAT_KEY));
                avExeEncodeText.setText(config.get(AV_EXE_ENCODE_KEY));
                historyBetweenConfigText.setText(config.get(AV_HISTORY_BETWEEN_DAY_CONFIG_KEY));

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
                Validate.notBlank(historyBetweenConfigText.getText(), "歷史重播間隔不可為空");

                if (!NumberUtil.isNumber(historyBetweenConfigText.getText(), false)) {
                    Validate.isTrue(false, "歷史重播間隔必須為數值");
                }

                // Validate.notBlank(avExeEncodeText.getText(),
                // "encoding不可為空!");

                config.put(PropertiesGroupUtils_ByKey.SAVE_KEYS, StringUtils.trimToEmpty(avExeAliasText.getText()));
                config.put(AV_EXE_KEY, StringUtils.trimToEmpty(avExeText.getText()));
                config.put(AV_EXE_FORMAT_KEY, StringUtils.trimToEmpty(avExeFormatText.getText()));
                config.put(AV_EXE_ENCODE_KEY, StringUtils.trimToEmpty(avExeEncodeText.getText()));
                config.put(AV_HISTORY_BETWEEN_DAY_CONFIG_KEY, StringUtils.trimToEmpty(historyBetweenConfigText.getText()));

                avExeConfig.saveConfig(config);
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }

    private class DropFileChecker {
        List<File> forReturnLst = new ArrayList<File>();
        List<File> imgReturnLst = new ArrayList<File>();

        private List<File> getExistsLst() {
            List<File> lst = new ArrayList<File>();
            DefaultListModel model = (DefaultListModel) dirCheckList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                FileZ f = (FileZ) model.getElementAt(ii);
                lst.add(f.file);
            }
            return lst;
        }

        DropFileChecker(List<File> fileLst) {
            if (fileLst == null) {
                File dirFile = new File(dirCheckText.getText());
                fileLst = new ArrayList<File>();
                gtu.file.FileUtil.searchFileMatchs(dirFile, ".*\\." + PatternCollection.VIDEO_PATTERN, fileLst);
                gtu.file.FileUtil.searchFileMatchs(dirFile, ".*\\." + PatternCollection.PICTURE_PATTERN, imgReturnLst);
            }

            List<File> existsLst = getExistsLst();
            forReturnLst.addAll(existsLst);

            for (File f : fileLst) {
                appendFile(f, forReturnLst);
            }

            DefaultListModel model = JListUtil.createModel();
            for (File f : forReturnLst) {
                model.addElement(new FileZ(f, true));
            }
            for (File f : imgReturnLst) {
                model.addElement(new FileZ(f, false));
            }
            dirCheckList.setModel(model);

            // 設定以看數
            movCountLabel.setText(String.format("以看%d, 總數%d", clickAvSet.size(), forReturnLst.size()));
        }

        private void appendFile(File startFile, List<File> appendLst) {
            if (startFile.isDirectory()) {
                if (startFile.listFiles() != null) {
                    for (File f : startFile.listFiles()) {
                        appendFile(f, appendLst);
                    }
                }
            } else if (startFile.getName().matches(".*\\." + PatternCollection.VIDEO_PATTERN) && !appendLst.contains(startFile)) {
                appendLst.add(startFile);
            }
        }
    }

    private List<File> dirCheckTextActionPerformed(List<File> fileLst) {
        mDropFileChecker = new DropFileChecker(fileLst);
        return mDropFileChecker.forReturnLst;
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
        } else if (fileZ.file.getName().matches(".*\\." + PatternCollection.PICTURE_PATTERN)) {
            try {
                if (OsInfoUtil.isWindows()) {
                    Desktop.getDesktop().open(fileZ.file);
                } else {
                    if (mDropFileChecker != null) {
                        List<String> lst = new ArrayList<String>();
                        for (File f : mDropFileChecker.imgReturnLst) {
                            lst.add("\"" + f.getAbsolutePath() + "\"");
                        }
                        RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance();
                        inst.command("eog " + StringUtils.join(lst, " "));
                        inst.apply();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            playAvFile(historyConfigHandler.getPlayFile(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return fileZ.file;
                }
            }));
        }
        dirCheckList.repaint();
    }

    private void readyMoveBtnAction() {
        final DefaultListModel model = (DefaultListModel) readyMoveLst.getModel();
        File targetDir = JCommonUtil.filePathCheck(readyMoveToDirText.getText(), "移動到目錄為空", true);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("目的目錄不存在 : " + targetDir);
            return;
        }
        if (model.getSize() == 0) {
            return;
        }
        readyMoveBtn.setText("移動中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int ii = 0; ii < model.getSize(); ii++) {
                    MoveFileZ file = (MoveFileZ) model.getElementAt(ii);

                    if (file instanceof MoveFileBatch) {
                        MoveFileBatch mFile = ((MoveFileBatch) file);
                        boolean beforeMoveSuccesss = mFile.beforeMove();
                        if (!beforeMoveSuccesss) {
                            file.moveStateChs = "BeforeFail...";
                            file.moveStateColor = "RED";
                            continue;
                        }
                    }

                    file.moveStateChs = "移動中...";
                    file.moveStateColor = "BLUE";
                    File moveTo = new File(targetDir, file.file.getName());
                    if (file.moveToFile == null) {
                        file.moveToFile = moveTo;
                    }
                    if (!file.file.exists()) {
                        file.moveStateChs = "NotExists...";
                        file.moveStateColor = "RED";
                    } else {
                        if (file.file.isDirectory()) {
                            try {
                                // FileUtils.moveDirectory(file.file, moveTo);
                                boolean result = FileUtil.moveFileByBat(file.file, targetDir);
                                if (!result) {
                                    file.moveStateChs = "Error...";
                                    file.moveStateColor = "RED";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                file.moveStateChs = "Error...";
                                file.moveStateColor = "RED";
                            }
                        } else if (file.file.isFile()) {
                            try {
                                // FileUtils.moveFile(file.file, moveTo);
                                boolean result = FileUtil.moveFileByBat(file.file, moveTo);
                                if (!result) {
                                    file.moveStateChs = "Error...";
                                    file.moveStateColor = "RED";
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                file.moveStateChs = "Error...";
                                file.moveStateColor = "RED";
                            }
                        }
                        file.moveStateChs = "Completed...";
                        file.moveStateColor = "GREEN";
                    }
                    readyMoveLst.repaint();
                }
                readyMoveLst.repaint();
                JCommonUtil._jOptionPane_showMessageDialog_info("全部完成!!");
                readyMoveBtn.setText("開始移動");
            }
        }).start();
    }

    private static class MoveFileZ extends FileZ {

        MoveFileZ(File file) {
            super(file);
        }

        MoveFileZ(File file, File moveToFile) {
            super(file);
            this.moveToFile = moveToFile;
        }

        String moveStateColor = "Blue";
        String moveStateChs = "";
        File moveToFile;

        public String toString() {
            if (moveToFile != null && moveToFile.exists()) {
                checkColor(moveToFile);
            } else {
                checkColor(file);
            }
            return String.format("<html>"//
                    + "<font color='%1$s'>%2$s</font>"//
                    + "<span style='color : %3$s ; background-color : %4$s;'>%5$s</span>"//
                    + "</html>", ///
                    moveStateColor, moveStateChs, "black", bgColor, name);//
        }
    }

    private static class FileZ {
        File file;
        String name;
        boolean isMovie = true;
        String bgColor;

        FileZ(File file) {
            this.file = file;
            this.name = file.getName();
            if (file.isFile()) {
                Matcher mth1 = movPtn.matcher(name);
                Matcher mth2 = jpgPtn.matcher(name);
                if (mth1.find()) {
                    this.isMovie = true;
                } else if (mth2.find()) {
                }
            }
        }

        FileZ(File file, boolean isMovie) {
            this.file = file;
            this.name = file.getName();
            this.isMovie = isMovie;
        }

        protected void checkColor(File file) {
            bgColor = "write";
            Matcher mth1 = movPtn.matcher(name);
            Matcher mth2 = jpgPtn.matcher(name);
            if (file.isDirectory()) {
                bgColor = "#00b0f9";
            } else {
                if (mth1.find()) {
                    bgColor = "#cce8cf";
                } else if (mth2.find()) {
                    bgColor = "yellow";
                }
            }
        }

        @Override
        public String toString() {
            String fontColor = "black";
            checkColor(file);
            String viewOk = "";
            if (mAVChoicerUI != null) {
                viewOk = mAVChoicerUI.historyConfigHandler.isAlreadyPlayInRecent(file) ? "<font color=red>閱</font>" : "";
            }
            return String.format("<html><span style='color : %s ; background-color : %s;'>" + viewOk + "%s</span></html>", fontColor, bgColor, name);
        }
    }

    private void dirCheckListResetBtnAction() {
        resetCacheFileList();
    }

    // =======================================================================================================================

    public void initKeyboardAndMouseHandler() {
        JnativehookKeyboardMouseHelper.startNativeKeyboardAndMouse(new MyNativeKeyAdapter() {
            @Override
            public void nativeMouseReleased(NativeMouseEvent e) {
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
                if (keyboardTriggerBtn.isSelected()) {
                    if (e.getKeyCode() == NativeKeyEvent.VC_R) {
                        choiceAVBtnAction();
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_N) {
                        currentFileHandler.playNextFile();
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_T) {
                        currentFileHandler.replayDefault();
                    } else if (e.getKeyCode() == NativeKeyEvent.VC_DELETE) {
                        currentFileHandler.deleteFile();
                    }
                }

                try {
                    System.out.println(" key code = " + e.getKeyCode() + " / " + e.getKeyChar());

                    if (mJFXPanelToSwing.getFxPanel() != null && mJFXPanelToSwing.getScene() != null) {
                        javafx.scene.Parent parent = mJFXPanelToSwing.getRoot();

                        if (!(parent instanceof MediaControl)) {
                            System.out.println("當前面板未初始化為 MediaControl[112233]");
                        } else {
                            MediaControl mMediaControl = (MediaControl) parent;
                            System.out.println("mMediaControl = " + mMediaControl);
                            if (JnativehookKeyboardMouseHelper.isMaskKeyPress(e, "c")) {
                                System.out.println("1min");
                                if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
                                    System.out.println("-1m");
                                    mMediaControl.forwardOrBackward(-1 * 60 * 1000);
                                } else if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
                                    System.out.println("+1m");
                                    mMediaControl.forwardOrBackward(60 * 1000);
                                }
                            } else if (JnativehookKeyboardMouseHelper.isMaskKeyPress(e, "s")) {
                                if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
                                    System.out.println("-30s");
                                    mMediaControl.forwardOrBackward(-1 * 30 * 1000);
                                } else if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
                                    System.out.println("+30s");
                                    mMediaControl.forwardOrBackward(30 * 1000);
                                }
                            } else {
                                if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
                                    System.out.println("-15s");
                                    mMediaControl.forwardOrBackward(-1 * 15 * 1000);
                                } else if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
                                    System.out.println("+15s");
                                    mMediaControl.forwardOrBackward(15 * 1000);
                                }
                            }

                            // play/pause
                            if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
                                System.out.println("play/pause");
                                mMediaControl.playPauseClicked();
                            }

                            // fullscreen
                            if (JnativehookKeyboardMouseHelper.isMaskKeyPress(e, "a") && //
                            e.getKeyCode() == NativeKeyEvent.VC_ENTER) {
                                JFrame mJFrame = AVChoicerUI.this;
                                if (mJFrame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
                                    // mJFrame.setUndecorated(true);
                                    System.out.println("fullscreen on");
                                    mJFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                                } else if (mJFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                                    // mJFrame.setUndecorated(false);
                                    System.out.println("fullscreen off");
                                    mJFrame.setExtendedState(JFrame.NORMAL);
                                }
                            }

                            // volume
                            if (e.getKeyCode() == NativeKeyEvent.VC_UP) {
                                System.out.println("volume +");
                                mMediaControl.addVolume(0.1d);
                            } else if (e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
                                System.out.println("volume -");
                                mMediaControl.addVolume(-0.1d);
                            }

                            // play rate
                            if (e.getKeyCode() == NativeKeyEvent.VC_OPEN_BRACKET) {
                                System.out.println("play rate +");
                                mMediaControl.addPlayRate(-0.1d);
                            } else if (e.getKeyCode() == NativeKeyEvent.VC_CLOSE_BRACKET) {
                                System.out.println("play rate -");
                                mMediaControl.addPlayRate(0.1d);
                            }
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // =========================================================================================================================
    private void initEmptyDirList() {
        DefaultTableModel model = JTableUtil.createModel(new int[] { 0 }, //
                new Object[] { "keep", "name", "size", "file" }, //
                new Class[] { Boolean.class, String.class, String.class, File.class }//
        );//
        emptyDirList.setModel(model);
        JTableUtil.setColumnWidths_Percent(emptyDirList, new float[] { 5, 60, 30, 5 });
        JTableUtil.newInstance(emptyDirList).setColumnColor_byCondition(1, new TableColorDef() {
            @Override
            public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String fileName = (String) table.getValueAt(row, 1);
                File file = (File) table.getValueAt(row, 3);
                if (fileName.toLowerCase().matches(".*\\." + PatternCollection.VIDEO_AND_PICTURE_PATTERN)) {
                    return Pair.of(java.awt.Color.GREEN, null);
                } else if (file.length() >= FileUtil.getSizeLength(40, "mb")) {
                    return Pair.of(java.awt.Color.GREEN, null);
                }
                return null;
            }
        });
    }

    private void emptyDirListCheckDir(File dir) {
        dropHereLbl.setText(dir.getName());
        emptyDirHolder.set(dir);
        initEmptyDirList();
        DefaultTableModel model = (DefaultTableModel) emptyDirList.getModel();
        List<File> fileLst = new ArrayList<File>();
        FileUtil.searchFilefind(dir, ".*", fileLst);
        Collections.sort(fileLst, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1 == null || !o1.exists()) {
                    return 1;
                }
                if (o2 == null || !o2.exists()) {
                    return 1;
                }
                return Long.valueOf(o1.length()).compareTo(Long.valueOf(o2.length())) * -1;
            }
        });
        for (int ii = 0; ii < fileLst.size(); ii++) {
            File file = fileLst.get(ii);
            model.addRow(new Object[] { false, file.getName(), FileUtil.getSizeDescription(file.length()), file });
        }
    }

    private class MoveFileBatch extends MoveFileZ {

        List<File> moveFiles;
        File moveToDir;
        File emptyDirHolder;
        boolean beforeMoveDone = false;
        List<String> moveLog2 = new ArrayList<String>();

        MoveFileBatch(List<File> moveFiles, File moveToDir, File emptyDirHolder) {
            super(new File(moveToDir, emptyDirHolder.getName()));
            this.moveToDir = new File(moveToDir, emptyDirHolder.getName());
            this.moveFiles = moveFiles;
            this.emptyDirHolder = emptyDirHolder;
        }

        private void showLog() {
            JCommonUtil._jOptionPane_showMessageDialog_info("搬檔案結果\n " + StringUtils.join(moveLog2, "\r\n"));
        }

        private boolean beforeMove() {
            if (beforeMoveDone) {
                return true;
            }
            int failCount = 0;
            List<File> moveToList = new ArrayList<File>();
            for (File fromFile : moveFiles) {
                long fileSize = fromFile.length();
                if (!moveToDir.exists()) {
                    moveToDir.mkdirs();
                }
                File moveToFile = new File(moveToDir, fromFile.getName());
                moveToList.add(moveToFile);
                try {
                    FileUtil.moveFileByBat(fromFile, moveToFile);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            for (File moveToFile : moveToList) {
                boolean moveResult = false;
                if (moveToFile.exists()) {
                    moveResult = true;
                } else {
                    failCount++;
                }
                moveLog2.add((moveResult ? moveToFile : moveToFile) + "\t" + (moveResult ? "成功" : "失敗"));
            }
            if (failCount > 0) {
                // ClipboardUtil.getInstance().setContents(StringUtils.join(moveLog2,
                // "\r\n"));
            } else {
                try {
                    FileUtils.deleteDirectory(emptyDirHolder);
                    beforeMoveDone = true;
                    return true;
                } catch (Exception e) {
                    JCommonUtil.handleException(e);
                }
            }
            return false;
        }
    }

    private void emptyDirBtnAction() {
        int checkCol = 0;
        int fileCol = 3;
        File moveToDir = new File(emptyDirTargetText.getText());
        if (!moveToDir.exists() || !moveToDir.isDirectory()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("必須設定搬移目錄");
            return;
        }
        if (emptyDirHolder.get() == null || !emptyDirHolder.get().exists() || !emptyDirHolder.get().isDirectory()) {
            // 單檔移動
            DefaultTableModel model = (DefaultTableModel) emptyDirList.getModel();
            JTableUtil util = JTableUtil.newInstance(emptyDirList);
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                boolean isMoveOut = (Boolean) util.getValueAt(true, ii, checkCol);
                if (isMoveOut) {
                    File moveFile = (File) util.getValueAt(true, ii, fileCol);
                    if (moveFile.exists()) {
                        DefaultListModel model2 = (DefaultListModel) readyMoveLst.getModel();
                        model2.addElement(new MoveFileZ(moveFile));
                    }
                }
            }
            emptyDirList.setModel(new DefaultTableModel());
        } else {
            // 多檔移動
            DefaultTableModel model = (DefaultTableModel) emptyDirList.getModel();
            List<File> moveFiles = new ArrayList<File>();
            List<String> moveLog = new ArrayList<String>();
            JTableUtil util = JTableUtil.newInstance(emptyDirList);
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                boolean isMoveOut = (Boolean) util.getValueAt(true, ii, checkCol);
                if (isMoveOut) {
                    File moveFile = (File) util.getValueAt(true, ii, fileCol);
                    if (moveFile.exists()) {
                        moveFiles.add(moveFile);
                        moveLog.add(moveFile.getName());
                    }
                }
            }
            boolean confirmMove = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否移出以下檔案?\n" + StringUtils.join(moveLog, "\r\n"), "移出檔案數 : " + moveFiles.size());
            if (confirmMove) {
                MoveFileBatch tMoveFileBatch = new MoveFileBatch(moveFiles, moveToDir, emptyDirHolder.get());
                DefaultListModel model2 = (DefaultListModel) readyMoveLst.getModel();
                model2.addElement(tMoveFileBatch);
            }
        }
    }
}