package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.FastDBQueryUI_ColumnSearchFilter.InnerMatch;
import gtu.clipboard.ClipboardUtil;
import gtu.collection.ListUtil;
import gtu.constant.PatternCollection;
import gtu.distribition.NormalDistributionFilter;
import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.image.ImageUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.log.LogWatcher;
import gtu.net.https.SimpleHttpsUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.AutoComboBox;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JChangeInputMethodUtil;
import gtu.swing.util.JColorUtil;
import gtu.swing.util.JComboBoxUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JProgressBarHelper;
import gtu.swing.util.JTabbedPaneUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTextPaneTextStyle;
import gtu.swing.util.JTextPaneUtil;
import gtu.swing.util.KeyEventUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.thread.util.ThreadUtil;
import taobe.tec.jcc.JChineseConvertor;

public class BrowserHistoryHandlerUI extends JFrame {

    static {
        ListUtil.setJdk16SortSetting();
    }

    private static final long serialVersionUID = 1L;
    private JTextField titleText;
    private JTextField urlText;
    private JLabel modifyTimeLabel;

    private PropertiesUtilBean configSelf = null;
    {
        if (PropertiesUtil.isClassInJar(BrowserHistoryHandlerUI.class)) {
            if (OsInfoUtil.isWindows()) {
                configSelf = new PropertiesUtilBean(BrowserHistoryHandlerUI.class, BrowserHistoryHandlerUI.class.getSimpleName() + "_win10");
            } else {
                configSelf = new PropertiesUtilBean(BrowserHistoryHandlerUI.class, BrowserHistoryHandlerUI.class.getSimpleName() + "_linux");
            }
        } else {
            if (OsInfoUtil.isWindows()) {
                configSelf = new PropertiesUtilBean(new File("D:/my_tool"), BrowserHistoryHandlerUI.class.getSimpleName() + "_win10");
            } else {
                configSelf = new PropertiesUtilBean(new File("/media/gtu001/OLD_D/my_tool/"), BrowserHistoryHandlerUI.class.getSimpleName() + "_linux");
            }
        }
        if (configSelf == null) {
            configSelf = new PropertiesUtilBean(BrowserHistoryHandlerUI.class, BrowserHistoryHandlerUI.class.getSimpleName());
        }
        System.out.println("configFile : " + configSelf.getPropFile());
    }

    private PropertiesUtilBean bookmarkConfig;
    private JComboBox tagComboBox;
    private JTextPane remarkArea;
    private JTable urlTable;
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
    private JComboBox searchComboBox;
    private JTextField bookmarkConfigText;
    private AutoComboBox tagComboBoxUtil;
    private AutoComboBox searchComboBoxUtil;
    private HideInSystemTrayHelper sysUtil = HideInSystemTrayHelper.newInstance();
    private JComboBox commandTypComboBox;
    private CommandTypeSetting commandTypeSetting;
    private BrowserHistoryHandlerUI_KeyboardListener keyboardListener = new BrowserHistoryHandlerUI_KeyboardListener();
    private JTabbedPane tabbedPane;
    private ColumnColorHandler columnColorHandler;
    private JLabel matchCountLabel;
    private JCheckBox nonWorkChk;
    private JCheckBox directOpenFileChk;
    private static OtherOpenPath otherOpenPath;
    private JCheckBox useRemarkOpenChk;
    private JCheckBox hiddenChk;
    private JCheckBox showHiddenChk;
    private JButton dropboxMergeBtn;
    private JTextArea batLogArea;
    private JTextField batWaittingTimeText;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private JToggleButton toggleChangeColorBtn;
    private HyperlinkJTextPaneHandler mHyperlinkJTextPaneHandler;
    private ImageIconConst mImageIconConst;
    private LastestUpdateKeeper mLastestUpdateKeeper;
    private Map<String, PeriodThread> periodSecHolder = new HashMap<String, PeriodThread>();
    private AtomicBoolean searchBool = new AtomicBoolean(false);
    private JComboBox orderMarkComboBox;
    private JComboBox bootstartCombobox;
    private JTextField periodSecText;
    private JButton openUrlBtn;
    private JTextPane logWatcherTextArea;
    private JToggleButton logWatcherBtn;
    private JTextField logWatcherPeriodText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(BrowserHistoryHandlerUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BrowserHistoryHandlerUI frame = new BrowserHistoryHandlerUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done..v11");
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public BrowserHistoryHandlerUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            getContentPane().add(tabbedPane, BorderLayout.CENTER);

            JPanel panel = new JPanel();
            tabbedPane.addTab("編輯書籤", null, panel, null);
            panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

            JLabel lblTitle = new JLabel("title");
            panel.add(lblTitle, "2, 2, right, default");

            titleText = new JTextField();
            titleText.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        JPopupMenuUtil.newInstance(titleText)//
                                .addJMenuItem("刷新", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        urlTextOnblur(true);
                                    }
                                }).applyEvent(e).show();
                    }
                }
            });
            panel.add(titleText, "4, 2, fill, default");
            titleText.setColumns(10);

            JLabel lblUrl = new JLabel("url");
            panel.add(lblUrl, "2, 4, right, default");

            urlText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(urlText, true);
            urlText.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    urlTextOnblur(false);
                }
            });
            panel.add(urlText, "4, 4, fill, default");
            urlText.setColumns(10);

            JLabel lblTag = new JLabel("tag");
            panel.add(lblTag, "2, 6, right, default");

            tagComboBox = new JComboBox();
            tagComboBoxUtil = AutoComboBox.applyAutoComboBox(tagComboBox);
            panel.add(tagComboBox, "4, 6, fill, default");
            tagComboBoxUtil.getTextComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent event) {
                    JChangeInputMethodUtil.toEnglish();
                }
            });

            JLabel lblRemark = new JLabel("remark");
            panel.add(lblRemark, "2, 8");

            JScrollPane scrollPane = new JScrollPane();
            panel.add(scrollPane, "4, 8, fill, fill");

            remarkArea = new JTextPane();
            JTextAreaUtil.applyCommonSetting(remarkArea);
            scrollPane.setViewportView(remarkArea);

            mHyperlinkJTextPaneHandler = new HyperlinkJTextPaneHandler(remarkArea);
            remarkArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mHyperlinkJTextPaneHandler.onAddMouseListener(e);
                }
            });

            JLabel lblCommandType = new JLabel("command type");
            panel.add(lblCommandType, "2, 10, right, default");

            commandTypComboBox = new JComboBox();
            DefaultComboBoxModel commandTypeComboModel = new DefaultComboBoxModel();//
            for (CommandTypeEnum e : CommandTypeEnum.values()) {
                commandTypeComboModel.addElement(e);
            }
            commandTypComboBox.setModel(commandTypeComboModel);
            panel.add(commandTypComboBox, "4, 10, fill, default");

            JPanel panel_11 = new JPanel();
            panel.add(panel_11, "4, 11, fill, fill");

            JLabel lblNewLabel_5 = new JLabel("置頂排序");
            panel_11.add(lblNewLabel_5);

            orderMarkComboBox = new JComboBox();
            orderMarkComboBox.setModel(JComboBoxUtil.createModel("", "1", "2", "3", "4", "5"));
            panel_11.add(orderMarkComboBox);

            JPanel panel_4 = new JPanel();
            panel.add(panel_4, "4, 13, fill, fill");

            useRemarkOpenChk = new JCheckBox("使用remark開啟");
            panel_4.add(useRemarkOpenChk);

            JButton saveBatFileBtn = new JButton("儲存bat檔");
            saveBatFileBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveBatFileBtnAction();
                }
            });
            panel_4.add(saveBatFileBtn);

            hiddenChk = new JCheckBox("隱藏");
            panel_4.add(hiddenChk);

            bootstartCombobox = new JComboBox();
            bootstartCombobox.setToolTipText("boot啟動順序");
            DefaultComboBoxModel b1model = new DefaultComboBoxModel();
            b1model.addElement("");
            for (int ii = 0; ii < 10; ii++) {
                b1model.addElement("" + String.valueOf(ii + 1));
            }
            bootstartCombobox.setModel(b1model);
            panel_4.add(bootstartCombobox);

            periodSecText = new JTextField();
            panel_4.add(periodSecText);
            periodSecText.setColumns(5);
            periodSecText.setToolTipText("設定執行週期(秒)");

            JPanel panel_2 = new JPanel();
            panel.add(panel_2, "4, 15, fill, fill");
            modifyTimeLabel = new JLabel("修改時間");
            panel_2.add(modifyTimeLabel);

            JButton saveBtn = new JButton("儲存");
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    beforeSaveCheckBrowser();
                    saveCurrentBookmarkBtnAction();
                }
            });

            openUrlBtn = new JButton("開啟");
            openUrlBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String url = StringUtils.trimToEmpty(urlText.getText());
                        doOpenUrlMaster(url);
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            openUrlBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String url = StringUtils.trimToEmpty(urlText.getText());
                    doRightClickShowMenuAction(openUrlBtn, e, url);
                }
            });
            panel_2.add(openUrlBtn);

            JButton showInDirectoryBtn = new JButton("目錄");
            showInDirectoryBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openFileDirectory();
                }
            });
            panel_2.add(showInDirectoryBtn);
            panel_2.add(saveBtn);

            JButton deleteBtn = new JButton("刪除");
            deleteBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteBtnAction();
                }
            });
            panel_2.add(deleteBtn);

            JButton clearBtn = new JButton("清空");
            clearBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearBtnAction();
                }
            });
            panel_2.add(clearBtn);

            JPanel panel_1 = new JPanel();
            tabbedPane.addTab("歷史書籤", null, panel_1, null);
            panel_1.setLayout(new BorderLayout(0, 0));

            JPanel panel_2x = new JPanel();
            panel_1.add(panel_2x, BorderLayout.NORTH);

            showHiddenChk = new JCheckBox("顯示隱藏");
            showHiddenChk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    initLoading();
                }
            });
            panel_2x.add(showHiddenChk);

            directOpenFileChk = new JCheckBox("file直接打開");
            panel_2x.add(directOpenFileChk);

            JLabel label = new JLabel("快速搜尋");
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    fastSearchMouseClicked(e);
                }
            });
            panel_2x.add(label);

            searchComboBox = new JComboBox();
            JComboBoxUtil.newInstance(searchComboBox).setWidth(300);
            searchComboBoxUtil = AutoComboBox.applyAutoComboBox(searchComboBox);
            panel_2x.add(searchComboBox);

            JButton allOpenBtn = new JButton("全開");
            allOpenBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    allOpenBtnAction();
                }
            });
            panel_2x.add(allOpenBtn);

            nonWorkChk = new JCheckBox("列出無效連結");
            nonWorkChk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    nonWorkChkAction();
                }
            });
            panel_2x.add(nonWorkChk);

            matchCountLabel = new JLabel("");
            panel_2x.add(matchCountLabel);
            searchComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    initLoading();
                }
            });

            JLabel lblNewLabel_1 = new JLabel("           ");
            panel_1.add(lblNewLabel_1, BorderLayout.SOUTH);

            JLabel lblNewLabel_2 = new JLabel("           ");
            panel_1.add(lblNewLabel_2, BorderLayout.WEST);

            JLabel lblNewLabel_3 = new JLabel("           ");
            panel_1.add(lblNewLabel_3, BorderLayout.EAST);

            urlTable = new JTable();
            // JTableUtil.defaultSetting_AutoResize(urlTable);
            JTableUtil.defaultSetting(urlTable);
            panel_1.add(JCommonUtil.createScrollComponent(urlTable), BorderLayout.CENTER);
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    try {
                        urlTableResize();
                    } catch (Exception ex) {
                    }
                }
            });
            urlTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    urlTableClickAction(e);
                }
            });
            JTableUtil.newInstance(urlTable).applyOnHoverEvent(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Pair<Integer, Integer> pair = (Pair<Integer, Integer>) e.getSource();
                        JTableUtil jtab = JTableUtil.newInstance(urlTable);
                        int rowPos = pair.getLeft();
                        int colPos = UrlTableConfigEnum.VO.ordinal();
                        UrlConfig d = (UrlConfig) jtab.getRealValueAt(rowPos, colPos);
                        urlTable.setToolTipText(d.url);
                    } catch (Exception ex) {
                    }
                }
            });

            JPanel panel_3 = new JPanel();
            tabbedPane.addTab("設定", null, panel_3, null);
            panel_3.setLayout(new FormLayout(
                    new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
                            FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

            JLabel lblNewLabel = new JLabel("bookmark config");
            panel_3.add(lblNewLabel, "2, 2, right, default");

            bookmarkConfigText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(bookmarkConfigText, false);
            panel_3.add(bookmarkConfigText, "4, 2, 24, 1, fill, default");
            bookmarkConfigText.setColumns(10);

            JButton configSaveBtn = new JButton("儲存設定");
            configSaveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    configSaveBtnAction();
                }
            });
            panel_3.add(configSaveBtn, "28, 2");

            JPanel panel_5 = new JPanel();
            panel_3.add(panel_5, "6, 4, fill, fill");

            dropboxMergeBtn = new JButton("合併dropbox設定");
            dropboxMergeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    dropboxMergeBtnAction();
                }
            });
            panel_5.add(dropboxMergeBtn);

            JPanel panel_6 = new JPanel();
            tabbedPane.addTab("bat Log", null, panel_6, null);
            panel_6.setLayout(new BorderLayout(0, 0));

            JPanel panel_7 = new JPanel();
            panel_6.add(panel_7, BorderLayout.NORTH);
            panel_7.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            JLabel lblNewLabel_4 = new JLabel("console等待時間(秒):");
            panel_7.add(lblNewLabel_4);

            batWaittingTimeText = new JTextField();
            batWaittingTimeText.setText("60");
            panel_7.add(batWaittingTimeText);
            batWaittingTimeText.setColumns(10);

            JPanel panel_8 = new JPanel();
            panel_6.add(panel_8, BorderLayout.WEST);

            JPanel panel_9 = new JPanel();
            panel_6.add(panel_9, BorderLayout.EAST);

            JPanel panel_10 = new JPanel();
            panel_6.add(panel_10, BorderLayout.SOUTH);

            batLogArea = new JTextArea();
            JTextAreaUtil.applyCommonSetting(batLogArea);
            panel_6.add(JCommonUtil.createScrollComponent(batLogArea), BorderLayout.CENTER);

            JPanel panel_12 = new JPanel();
            tabbedPane.addTab("LogWatcher", null, panel_12, null);
            panel_12.setLayout(new BorderLayout(0, 0));

            JPanel panel_13 = new JPanel();
            panel_12.add(panel_13, BorderLayout.NORTH);

            logWatcherBtn = new JToggleButton("監聽");
            logWatcherBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logWatcherBtnToggleAction();
                }
            });

            logWatcherCustomFileText = new JTextField();
            panel_13.add(logWatcherCustomFileText);
            logWatcherCustomFileText.setColumns(10);
            logWatcherCustomFileText.setToolTipText("自訂檔案");
            JCommonUtil.jTextFieldSetFilePathMouseEvent(logWatcherCustomFileText, false);

            logWatcherFrontChk = new JCheckBox("提前");
            panel_13.add(logWatcherFrontChk);

            JLabel lblNewLabel_6 = new JLabel("監聽間隔");
            panel_13.add(lblNewLabel_6);

            logWatcherPeriodText = new JTextField();
            logWatcherPeriodText.setText("500");
            panel_13.add(logWatcherPeriodText);
            logWatcherPeriodText.setColumns(10);
            panel_13.add(logWatcherBtn);

            logWatcherClearBtn = new JButton("清除");
            logWatcherClearBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    logWatcherTextArea.setText("");
                }
            });

            panel_13.add(logWatcherClearBtn);

            logWatcherSizeChangeLbl = new JLabel("");
            panel_13.add(logWatcherSizeChangeLbl);

            JPanel panel_14 = new JPanel();
            panel_12.add(panel_14, BorderLayout.WEST);

            JPanel panel_15 = new JPanel();
            panel_12.add(panel_15, BorderLayout.EAST);

            JPanel panel_16 = new JPanel();
            panel_12.add(panel_16, BorderLayout.SOUTH);

            logWatcherTextArea = new JTextPane();
            JTextAreaUtil.applyCommonSetting(logWatcherTextArea);
            JTextAreaUtil.setScrollToBottomPloicy(logWatcherTextArea);
            logWatcherTextArea.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                @Override
                public void process(DocumentEvent event) {
                    logWatcherTextAreaLogCacheClean();
                }
            }));
            logWatcherTextArea.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    logWatcherTextAreaKeyEventAction(e);
                }
            });
            panel_12.add(JCommonUtil.createScrollComponent(logWatcherTextArea), BorderLayout.CENTER);

            pack();
            this.setSize(900, 500);// XXX <---------- 設寬高度

            // ============================================================================================================
            // ============================================================================================================
            // ============================================================================================================

            configSelf.reflectInit(this);

            if (configSelf.getConfigProp().containsKey("bookmarkConfigText")) {
                bookmarkConfig = new PropertiesUtilBean(new File(configSelf.getConfigProp().getProperty("bookmarkConfigText")));
            } else {
                // 使用jar 所在路徑
                File bookmarkConfigFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), "BrowserHistoryHandlerUI_bookmark.properties");
                if (bookmarkConfigFile.exists()) {
                    bookmarkConfig = new PropertiesUtilBean(bookmarkConfigFile);
                    bookmarkConfigText.setText(bookmarkConfigFile.getAbsolutePath());
                }
            }

            mImageIconConst = new ImageIconConst(urlTable);
            mLastestUpdateKeeper = new LastestUpdateKeeper();

            JCommonUtil.defaultToolTipDelay();
            JCommonUtil.setJFrameDefaultSetting(this);
            JCommonUtil.setLocationToRightBottomCorner(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/file-manager.ico");
            commandTypeSetting = new CommandTypeSetting();
            sysUtil.apply(this);
            keyboardListener.initialize();
            bringToTop();
            columnColorHandler = new ColumnColorHandler(urlTable, bookmarkConfig);
            initAddSaveShortcutKeyEvent();
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this.getContentPane());
            jFrameRGBColorPanel.setIgnoreLst(this);
            panel_3.add(jFrameRGBColorPanel.getToggleButton(false), "8, 4");

            // final do
            initLoading();

            bootstartStarting();

            otherOpenPath = new OtherOpenPath(configSelf);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void doOpenUrlMaster(String url) {
        boolean isPeriodProc = false;
        if (StringUtils.isNotBlank(periodSecText.getText()) && StringUtils.isNumericSpace(periodSecText.getText())) {
            int periodSecs = Integer.parseInt(StringUtils.trimToEmpty(periodSecText.getText()));
            isPeriodProc = true;
            if (periodSecHolder.containsKey(url)) {
                if (periodSecHolder.get(url).stop == false) {
                    periodSecHolder.get(url).stop = true;
                }
            } else {
                PeriodThread task = new PeriodThread(url, periodSecHolder);
                task.doStart(true, periodSecs);
                periodSecHolder.put(url, task);
            }
        }
        if (!isPeriodProc) {
            commandTypeSetting.getValue().doOpen(url, BrowserHistoryHandlerUI.this);
        }
        if (periodSecHolder.containsKey(url)) {
            String lbl = periodSecHolder.get(url).stop ? "結束" : "進行中";
            openUrlBtn.setText("週期執行(" + lbl + ")");
        } else {
            openUrlBtn.setText("開啟");
        }
    }

    private boolean isPeriodSecRunning(String url) {
        if (periodSecHolder.containsKey(url)) {
            if (!periodSecHolder.get(url).stop) {
                return true;
            }
        }
        return false;
    }

    private List<UrlConfig> getUrlConfigList(boolean reload) {
        List<UrlConfig> lst = new ArrayList<UrlConfig>();
        if (reload && bookmarkConfig != null) {
            bookmarkConfig.reload();
        }
        if (bookmarkConfig == null || bookmarkConfig.getConfigProp() == null) {
            return lst;
        }
        for (Enumeration<?> enu = bookmarkConfig.getConfigProp().keys(); enu.hasMoreElements();) {
            String url = (String) enu.nextElement();
            final String title_tag_remark_time = bookmarkConfig.getConfigProp().getProperty(url);
            AtomicReference<UrlConfig> dd = new AtomicReference<UrlConfig>();
            try {
                dd.set(UrlConfig.parseTo(url, title_tag_remark_time));
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
            final UrlConfig d = dd.get();
            lst.add(d);
        }
        return lst;
    }

    private void bootstartStarting() {
        List<UrlConfig> bootLst = new ArrayList<UrlConfig>();
        List<UrlConfig> lst = getUrlConfigList(false);
        for (UrlConfig d : lst) {
            if (StringUtils.isNotBlank(d.bootStart)) {
                bootLst.add(d);
            }
        }
        Collections.sort(bootLst, new Comparator<UrlConfig>() {
            @Override
            public int compare(UrlConfig arg0, UrlConfig arg1) {
                return StringUtils.trimToEmpty(arg0.bootStart).compareTo(StringUtils.trimToEmpty(arg1.bootStart));
            }
        });
        StringBuilder sb = new StringBuilder();
        for (UrlConfig d : bootLst) {
            CommandTypeEnum e = CommandTypeEnum.valueOfFrom(d.commandType);
            e.doOpen(d.url, this);
            sb.append("bootstart -> ").append(d.url).append("\r\n");
        }
        batLogArea.setText(sb.toString());
    }

    private static String fixWindowUrl(String url) {
        if (OsInfoUtil.isWindows()) {
            url = StringUtils.defaultString(url);
            if (url.startsWith("file:")) {
                try {
                    url = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                Pattern ptn = Pattern.compile("file\\:[\\/]+(.*)");
                Matcher mth = ptn.matcher(url);
                if (mth.find()) {
                    File file = new File(mth.group(1));
                    System.out.println("orign file : " + file);
                    if (file.exists()) {
                        try {
                            return file.toURL().toString();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }

                    String paddingUrl = file.getAbsolutePath();
                    paddingUrl = paddingUrl.replaceAll("^\\w+\\:", "");
                    for (File root : File.listRoots()) {
                        File tmpFile = new File(root + File.separator + paddingUrl);
                        if (tmpFile.exists()) {
                            try {
                                return tmpFile.toURL().toString();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return url;
    }

    private enum CommandTypeEnum {
        DEFAULT("預設") {
            @Override
            void _doOpen(String url, BrowserHistoryHandlerUI _this) {
                try {
                    String dValue = _this.bookmarkConfig.getConfigProp().getProperty(url);
                    String fixUrl = fixWindowUrl(url);
                    if (StringUtils.isNotBlank(dValue)) {
                        UrlConfig d = UrlConfig.parseTo(url, dValue);
                        if (!"Y".equalsIgnoreCase(d.isUseRemarkOpen)) {
                            DesktopUtil.browse(fixUrl);
                        } else {
                            _this.doOpenWithRemark(d, false);
                        }
                    } else {
                        DesktopUtil.browse(fixUrl);
                    }
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        }, //
        IE_EAGE("Microsoft Edge") {
            @Override
            void _doOpen(String url, BrowserHistoryHandlerUI _this) {
                try {
                    if (OsInfoUtil.isWindows()) {
                        if (System.getProperty("os.name").equals("Windows 10")) {
                            Runtime.getRuntime().exec("cmd /c start microsoft-edge:" + url);
                            return;
                        }
                        String exePath = "C:/Program Files/Internet Explorer/iexplore.exe";
                        if (StringUtils.isNotBlank(BrowserHistoryHandlerUI.otherOpenPath.PATH_IE)) {
                            exePath = BrowserHistoryHandlerUI.otherOpenPath.PATH_IE;
                        }
                        String command = String.format("cmd /c call \"%s\" \"%s\" ", exePath, url);
                        Runtime.getRuntime().exec(command);
                    } else {
                        CommandTypeEnum.DEFAULT.doOpen(title, _this);
                    }
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        }, //
        FIREFOX("Mozilla Firefox") {
            @Override
            void _doOpen(String url, BrowserHistoryHandlerUI _this) {
                try {
                    String exePath = "C:/Program Files/Mozilla Firefox/firefox.exe";
                    String commandFormat = "cmd /c call \"%s\" \"%s\" ";
                    if (!OsInfoUtil.isWindows()) {
                        exePath = "/usr/bin/firefox ";
                        commandFormat = "%s %s";
                    }
                    if (StringUtils.isNotBlank(BrowserHistoryHandlerUI.otherOpenPath.PATH_FIREFOX)) {
                        exePath = BrowserHistoryHandlerUI.otherOpenPath.PATH_FIREFOX;
                    }
                    String finalCommand = String.format(commandFormat, exePath, url);
                    System.out.println(finalCommand);
                    Runtime.getRuntime().exec(finalCommand);
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        }, //
        CHROME("Google Chrome") {
            @Override
            void _doOpen(String url, BrowserHistoryHandlerUI _this) {
                try {
                    String exePath = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
                    String commandFormat = "cmd /c call \"%s\" \"%s\" ";
                    if (!OsInfoUtil.isWindows()) {
                        exePath = "/opt/google/chrome/google-chrome ";// --enable-plugins
                        commandFormat = "%s %s";
                    }
                    if (StringUtils.isNotBlank(BrowserHistoryHandlerUI.otherOpenPath.PATH_CHROME)) {
                        exePath = BrowserHistoryHandlerUI.otherOpenPath.PATH_CHROME;
                    }
                    String finalCommand = String.format(commandFormat, exePath, url);
                    System.out.println(finalCommand);
                    Runtime.getRuntime().exec(finalCommand);
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        },//
        ;

        final String title;

        private CommandTypeEnum(String title) {
            this.title = title;
        }

        abstract void _doOpen(String url, BrowserHistoryHandlerUI _this);

        public void doOpen(String url, BrowserHistoryHandlerUI _this) {
            url = StringUtils.trimToEmpty(url);

            if (StringUtils.isBlank(url)) {
                return;
            }

            UrlConfig d = null;
            try {
                d = UrlConfig.parseTo(url, _this.bookmarkConfig.getConfigProp().getProperty(url));
            } catch (Exception ex) {
            }

            System.out.println("[doOpen]>>>" + this.name());

            // 判斷是否為自動產生
            if (d != null) {
                if (StringUtil_.isUUID(url) && !"Y".equalsIgnoreCase(d.isUseRemarkOpen)) {
                    // JCommonUtil._jOptionPane_showMessageDialog_error("此非合理URL!");
                } else if ("Y".equalsIgnoreCase(d.isUseRemarkOpen)) {
                    _this.doOpenWithRemark(d, false);
                } else {
                    // if (OsInfoUtil.isWindows()) {
                    _doOpen(url, _this);
                    // } else {
                    // CommandTypeEnum.DEFAULT._doOpen(url, _this);
                    // }
                }

                _this.clickUrlDoLogAction(d);
            }

            if (d == null) {
                System.out.println("!!![error] 無法執行任何操作 : " + url);
            }
        }

        public String toString() {
            return this.title;
        }

        private static CommandTypeEnum valueOfFrom(String commandType) {
            CommandTypeEnum e = CommandTypeEnum.DEFAULT;
            try {
                e = CommandTypeEnum.valueOf(commandType);
            } catch (Exception ex) {
            }
            return e;
        }
    }

    private static class ImageIconConst {
        private static Icon pigIcon;
        private static Icon transparentIcon;
        private static int width = -1;

        ImageIconConst(JTable urlTable) {
            width = urlTable.getRowHeight();
        }

        private Icon getPigIcon() {
            if (pigIcon == null) {
                Image image = ImageUtil.getInstance().getIcoImage("resource/images/ico/Pig_SC.ico");
                pigIcon = getIcon(image);
            }
            return pigIcon;
        }

        private Icon getIcon(Image image) {
            Image image2 = ImageUtil.getInstance().getScaledImage(image, width, width);
            return ImageUtil.getInstance().imageToIcon(image2);
        }

        private Icon getTransparentIcon() {
            if (transparentIcon == null) {
                transparentIcon = ImageUtil.getInstance().createTransparentIcon(width, width);
            }
            return transparentIcon;
        }
    }

    private enum UrlTableConfigEnum {
        刪除(5) {
            @Override
            Object get(final UrlConfig d, final BrowserHistoryHandlerUI _this) {
                JButton delBtn = new JButton("刪除");
                delBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("click 刪除");
                        String message = "title : " + d.title + "\n" + //
                        "url : " + d.url + "\n" + //
                        "tag : " + d.tag + "\n" + //
                        "remark : " + d.remark + "\n" + //
                        "timestamp : " + d.timestamp + "\n" + //
                        "";
                        boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(message, "確定刪除");
                        if (!result) {
                            return;
                        }
                        _this.bookmarkConfig.getConfigProp().remove(d.url);
                        _this.bookmarkConfig.store();
                        _this.initLoading();
                        JCommonUtil._jOptionPane_showMessageDialog_info("刪除成功!");
                    }
                });
                return delBtn;
            }
        }, //
        開啟(3) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return true;// 預設勾選
            }
        }, //
        Icon(3) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                int width = _this.urlTable.getRowHeight();
                try {
                    String newUrl = fixWindowUrl(d.url);
                    if (!StringUtils.equals(newUrl, d.url)) {
                        return _this.mImageIconConst.getPigIcon();
                    } else {
                        File f = DesktopUtil.getFile(newUrl);
                        if (f.exists()) {
                            Image image = null;
                            if (OsInfoUtil.isWindows()) {
                                Icon icon = ImageUtil.getInstance().getIconFromExe(f);
                                image = ImageUtil.getInstance().iconToImage(icon);
                                Image image2 = ImageUtil.getInstance().getScaledImage(image, width, width);
                                return ImageUtil.getInstance().imageToIcon(image2);
                            } else {
                                return _this.mImageIconConst.getPigIcon();
                            }
                        }
                    }
                    return _this.mImageIconConst.getTransparentIcon();
                } catch (Exception ex) {
                    return _this.mImageIconConst.getTransparentIcon();
                }
            }
        }, //
        title(35) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.title;
            }
        }, //
        url(10) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.url;
            }
        }, //
        tag(10) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.tag;
            }
        }, //
        timestamp(8) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.timestamp;
            }
        }, //
        lastClick(6) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.timestampLastest;
            }
        }, //
        clickTimes(5) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.clickTimes;
            }
        }, //
        remark(19) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.remark;
            }
        }, //
        bootStart(5) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d.bootStart;
            }
        },
        VO(0) {
            @Override
            Object get(UrlConfig d, BrowserHistoryHandlerUI _this) {
                return d;
            }
        },//
        ;

        final float width;

        UrlTableConfigEnum(float width) {
            this.width = width;
        }

        private static float[] getWidthConfig() {
            List<Float> lst = new ArrayList<Float>();
            for (UrlTableConfigEnum e : UrlTableConfigEnum.values()) {
                lst.add(e.width);
            }
            return ArrayUtils.toPrimitive(lst.toArray(new Float[0]));
        }

        private static String[] getTitleConfig() {
            List<String> lst = new ArrayList<String>();
            for (UrlTableConfigEnum e : UrlTableConfigEnum.values()) {
                lst.add(e.name());
            }
            return lst.toArray(new String[0]);
        }

        private static Object[] getRow(UrlConfig d, BrowserHistoryHandlerUI _this) {
            List<Object> lst = new ArrayList<Object>();
            for (UrlTableConfigEnum e : UrlTableConfigEnum.values()) {
                lst.add(e.get(d, _this));
            }
            return lst.toArray();
        }

        abstract Object get(UrlConfig d, BrowserHistoryHandlerUI _this);
    }

    private void clickUrlDoLogAction(UrlConfig d) {
        try {
            int clickTime = NumberUtils.toInt(d.clickTimes, 0);
            clickTime++;

            d.clickTimes = String.valueOf(clickTime);
            d.timestampLastest = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss");

            bookmarkConfig.getConfigProp().setProperty(d.url, UrlConfig.getConfigValue(d));
            bookmarkConfig.store();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void beforeSaveCheckBrowser() {
        String url = StringUtils.trimToEmpty(urlText.getText());
        if (StringUtils.isNotBlank(url)) {
            if (!bookmarkConfig.getConfigProp().containsKey(url)) {
                // no use
            }
            if (url.matches("https?\\:.*")) {
                String commandType = commandTypeSetting.getValue().name();
                if (ArrayUtils.contains(new String[] { CommandTypeEnum.DEFAULT.name(), CommandTypeEnum.IE_EAGE.name() }, commandType)) {
                    boolean changeBrowser = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("目前使用瀏覽器" + commandType + "是否要改變瀏覽器?(換成firefox)", "切換瀏覽器");
                    if (changeBrowser) {
                        commandTypeSetting.setValue(CommandTypeEnum.FIREFOX.name());
                    }
                }
            }
        }
    }

    private void saveCurrentBookmarkBtnAction() {
        try {
            Validate.isTrue(StringUtils.isNumericSpace(periodSecText.getText()), "periodSec 必須是數值");

            String url = StringUtils.trimToEmpty(urlText.getText());
            String title = StringUtils.trimToEmpty(titleText.getText());
            String tag = StringUtils.trimToEmpty(tagComboBox.getSelectedItem().toString());
            String remark = StringUtils.trimToEmpty(remarkArea.getText().toString());
            String commandType = commandTypeSetting.getValue().name();
            String isUseRemarkOpen = useRemarkOpenChk.isSelected() ? "Y" : "N";
            String isHidden = hiddenChk.isSelected() ? "Y" : "N";
            String orderMark = StringUtils.trimToEmpty((String) orderMarkComboBox.getSelectedItem());//
            String bootStart = StringUtils.trimToEmpty(String.valueOf(bootstartCombobox.getSelectedItem()));
            String periodSec = StringUtils.trimToEmpty(periodSecText.getText());

            Validate.notNull(bookmarkConfig, "請先設定bookmark設定黨路徑");
            // Validate.notEmpty(url, "url 為空");
            Validate.notEmpty(title, "title 為空");
            Validate.notEmpty(tag, "tag 為空");

            if (StringUtils.isBlank(url)) {
                url = UUID.randomUUID().toString();
            }

            UrlConfig d = new UrlConfig();
            if (bookmarkConfig.getConfigProp().containsKey(url)) {
                String orignStr = bookmarkConfig.getConfigProp().getProperty(url);
                d = UrlConfig.parseTo(url, orignStr);
            } else {
                d.timestamp = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss");
                d.clickTimes = "";
            }

            d.url = url;
            d.title = title;
            d.tag = tag;
            d.remark = remark;
            d.commandType = commandType;
            d.timestampLastest = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss");
            d.updateTimestamp = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss");
            d.isUseRemarkOpen = isUseRemarkOpen;
            d.isHidden = isHidden;
            d.orderMark = orderMark;
            d.bootStart = bootStart;
            d.periodSec = periodSec;

            bookmarkConfig.getConfigProp().setProperty(url, UrlConfig.getConfigValue(d));
            bookmarkConfig.store();

            this.initLoading();

            // 因為initLoading會清空
            tagComboBoxUtil.setSelectItemAndText(tag);

            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");

            mLastestUpdateKeeper.update(true);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void clearBtnAction() {
        urlText.setText("");
        titleText.setText("");
        tagComboBoxUtil.getTextComponent().setText("");
        searchComboBoxUtil.getTextComponent().setText("");
        remarkArea.setText("");
        modifyTimeLabel.setText("");
        commandTypComboBox.setSelectedItem(CommandTypeEnum.DEFAULT.name());
        orderMarkComboBox.setSelectedItem("");
    }

    private void urlTableResize() {
        JTableUtil.setColumnWidths_Percent(urlTable, UrlTableConfigEnum.getWidthConfig());
    }

    private void initLoading() {
        nonWorkChk.setSelected(false);
        initLoading(null);
    }

    private void initLoading(final JProgressBarHelper progressBarHelper) {
        try {
            if (searchBool.get()) {
                return;
            }
            searchBool.set(true);

            if (bookmarkConfig == null) {
                System.out.println("bookmarkConfig null !!!");
                return;
            }

            final List<String> tagLst = new ArrayList<String>();
            final List<UrlConfig> lst = new ArrayList<UrlConfig>();

            final JTableUtil tableUtil = JTableUtil.newInstance(urlTable);
            DefaultTableModel model = JTableUtil.createModel(new int[] { UrlTableConfigEnum.開啟.ordinal() }, UrlTableConfigEnum.getTitleConfig());
            tableUtil.hiddenColumn(UrlTableConfigEnum.VO.name());
            urlTable.setModel(model);

            tableUtil.setColumnSortComparator(UrlTableConfigEnum.clickTimes.ordinal(), new Comparator<String>() {
                private Integer getVal(String val) {
                    try {
                        return Integer.parseInt(val);
                    } catch (Exception ex) {
                        return Integer.MIN_VALUE;
                    }
                }

                @Override
                public int compare(String o1, String o2) {
                    return getVal(o1).compareTo(getVal(o2));
                }
            });

            columnColorHandler.apply();

            urlTableResize();

            for (String v : new String[] { UrlTableConfigEnum.刪除.name() }) {
                System.out.println("columnIsButton = " + v);
                tableUtil.columnIsButton(v);
            }
            tableUtil.columnIsComponent(UrlTableConfigEnum.開啟.ordinal(), new JCheckBox());// 設定為checkbox

            final String searchText = StringUtils.trimToEmpty(searchComboBoxUtil.getTextComponent().getText()).toLowerCase();
            System.out.println("searchText = " + searchText);
            if (StringUtils.isBlank(searchText)) {
                System.out.println("====================");
                new Exception("").printStackTrace();
            }

            for (Enumeration<?> enu = bookmarkConfig.getConfigProp().keys(); enu.hasMoreElements();) {
                String url = (String) enu.nextElement();
                final String title_tag_remark_time = bookmarkConfig.getConfigProp().getProperty(url);

                AtomicReference<UrlConfig> dd = new AtomicReference<UrlConfig>();
                try {
                    dd.set(UrlConfig.parseTo(url, title_tag_remark_time));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }

                final UrlConfig d = dd.get();

                new Runnable() {
                    private String s2t(String oringStr) {
                        try {
                            return JChineseConvertor.getInstance().s2t(oringStr);
                        } catch (Exception ex) {
                            return oringStr;
                        }
                    }

                    private String decode(String url) {
                        try {
                            return URLDecoder.decode(url, "UTF-8");
                        } catch (Exception ex) {
                            return url;
                        }
                    }

                    private boolean isUrlNotOk(UrlConfig d) {
                        try {
                            int stateCode = SimpleHttpsUtil.newInstance().getStatusCode(d.url, 5000);
                            if (stateCode == 301) {
                                String urlNew = d.url.replaceFirst("http:\\/", "https:\\/");
                                stateCode = SimpleHttpsUtil.newInstance().getStatusCode(urlNew, 5000);
                            }
                            if (stateCode != 200) {
                                JCommonUtil._jOptionPane_showMessageDialog_error(stateCode + " " + d.title);
                                return true;
                            }
                        } catch (Exception ex) {
                            JCommonUtil.handleException("Err Url : " + d.title + ", " + d.url, ex);
                            return true;
                        }
                        return false;
                    }

                    private boolean isNonWrokUrl(UrlConfig d) {
                        if (StringUtil_.isUUID(d.url)) {
                            return false;
                        } else if (DesktopUtil.isFile(d.url)) {
                            File file = DesktopUtil.getFile(d.url);
                            if (file == null || !file.exists()) {
                                return true;
                            }
                        } else {
                            if (isUrlNotOk(d)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    class InnerMatch {
                        Pattern ptn;

                        InnerMatch(String singleText) {
                            singleText = singleText.replaceAll(Pattern.quote("*"), ".*");
                            ptn = Pattern.compile(singleText, Pattern.CASE_INSENSITIVE);
                        }

                        boolean find(String value) {
                            Matcher mth = ptn.matcher(value);
                            return mth.find();
                        }
                    }

                    private boolean isNormalMatch_detail(String singleText) {
                        String tag = s2t(d.tag);
                        String remark = s2t(d.remark);
                        String url = decode(d.url);

                        if (StringUtils.isBlank(singleText)) {
                            return true;
                        } else if (d.title.toLowerCase().contains(singleText) || //
                        tag.toLowerCase().contains(singleText) || //
                        remark.toLowerCase().contains(singleText) || //
                        d.timestamp.toLowerCase().contains(singleText) || //
                        url.toLowerCase().contains(singleText) //
                        ) {
                            return true;
                        } else {
                            if (singleText.contains("*")) {
                                InnerMatch m = new InnerMatch(singleText);
                                if (m.find(d.title) || //
                                m.find(tag) || //
                                m.find(remark) || //
                                m.find(d.timestamp) || //
                                m.find(url) //
                                ) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }

                    private boolean isNormalMatch_logic(String compareTarget, String comparator, String compareText) {
                        String comareThisStr = "";
                        boolean isTag = false;
                        if ("tag".equalsIgnoreCase(compareTarget)) {
                            comareThisStr = d.tag;
                            isTag = true;
                        } else if ("remark".equalsIgnoreCase(compareTarget)) {
                            comareThisStr = d.remark;
                        }

                        NormalLogicTagMatch tagChk = new NormalLogicTagMatch(d, comparator, compareText);
                        if (isTag) {
                            return tagChk.isMatch();
                        }

                        if ("^=".equalsIgnoreCase(comparator)) {
                            if (comareThisStr.toLowerCase().startsWith(compareText.toLowerCase())) {
                                return true;
                            }
                        } else if ("$=".equalsIgnoreCase(comparator)) {
                            if (comareThisStr.toLowerCase().endsWith(compareText.toLowerCase())) {
                                return true;
                            }
                        } else if ("*=".equalsIgnoreCase(comparator)) {
                            if (comareThisStr.toLowerCase().contains(compareText.toLowerCase())) {
                                return true;
                            }
                        } else if ("=".equalsIgnoreCase(comparator)) {
                            if (comareThisStr.equalsIgnoreCase(compareText)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    private Pattern logicPtn = Pattern.compile("^\\[(tag|remark)(\\=|\\^\\=|\\&\\=|\\*\\=)(.*)\\]", Pattern.CASE_INSENSITIVE);

                    private boolean isNormalMatch(String singleText) {
                        // 條件查詢
                        Matcher mth = logicPtn.matcher(singleText);
                        if (mth.find()) {
                            String compareTarget = mth.group(1);
                            String comparator = mth.group(2);
                            String compareText = mth.group(3);
                            return this.isNormalMatch_logic(compareTarget, comparator, compareText);
                        }

                        // 一般查詢
                        String[] searchArry = searchText.split(",", -1);
                        for (String _singleText : searchArry) {
                            _singleText = StringUtils.trimToEmpty(_singleText);
                            if (StringUtils.isNotBlank(_singleText)) {
                                if (isNormalMatch_detail(_singleText)) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }

                    private boolean isNotHiddenObj(UrlConfig d) {
                        if (showHiddenChk.isSelected()) {
                            return true;
                        }
                        return !"Y".equalsIgnoreCase(d.isHidden);
                    }

                    @Override
                    public void run() {
                        if (nonWorkChk.isSelected()) {
                            if (isNonWrokUrl(d)) {
                                // System.out.println("find = " +
                                // title_tag_remark_time);
                                lst.add(d);
                            }
                            if (progressBarHelper != null) {
                                progressBarHelper.addOne();
                            }
                        } else {
                            if (StringUtils.isBlank(searchText) && //
                            isNotHiddenObj(d)) {
                                // System.out.println("find = " +
                                // title_tag_remark_time);
                                lst.add(d);
                            } else if (isNormalMatch(searchText) && //
                            isNotHiddenObj(d)) {
                                // System.out.println("find = " +
                                // title_tag_remark_time);
                                lst.add(d);
                            }
                        }
                    }
                }.run();

                // 過濾重複的
                new Runnable() {
                    private boolean isContains(String val) {
                        for (String str : tagLst) {
                            if (StringUtils.equalsIgnoreCase(str, val)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public void run() {
                        String[] strs = StringUtils.split(d.tag, ",");
                        for (String str : strs) {
                            str = StringUtils.trimToEmpty(str);
                            if (!isContains(str)) {
                                tagLst.add(str);
                            }
                        }
                    }
                }.run();
            }

            // 設定tag 夏拉
            Collections.sort(tagLst);
            tagComboBoxUtil.applyComboxBoxList(tagLst);
            searchComboBoxUtil.applyComboxBoxList(tagLst, searchText);

            // 設定urlTable
            Collections.sort(lst, new Comparator<UrlConfig>() {
                @Override
                public int compare(UrlConfig o1, UrlConfig o2) {
                    if (StringUtils.isBlank(o1.orderMark) && StringUtils.isBlank(o2.orderMark)) {
                        return o1.timestamp.compareTo(o2.timestamp);
                    }
                    if (StringUtils.isNotBlank(o1.orderMark) && StringUtils.isNotBlank(o2.orderMark)) {
                        return o1.orderMark.compareTo(o2.orderMark);
                    }
                    if (StringUtils.isNotBlank(o1.orderMark) && StringUtils.isBlank(o2.orderMark)) {
                        return -1;
                    }
                    if (StringUtils.isBlank(o1.orderMark) && StringUtils.isNotBlank(o2.orderMark)) {
                        return 1;
                    }
                    return 0;
                }
            });

            for (final UrlConfig d : lst) {
                model.addRow(UrlTableConfigEnum.getRow(d, this));
            }

            System.out.println("searchSize : " + model.getRowCount());

            matchCountLabel.setText((model.getRowCount() == 0) ? "查無!" : "數量:" + model.getRowCount());

            // 如果只有一筆直接打開
            if (model.getRowCount() == 1 && directOpenFileChk.isSelected()) {
                JTableUtil jtab = JTableUtil.newInstance(urlTable);
                int realRowPos = JTableUtil.getRealRowPos(0, urlTable);
                UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(realRowPos, UrlTableConfigEnum.VO.ordinal());
                if (DesktopUtil.isFile(vo.url)) {
                    CommandTypeEnum e = CommandTypeEnum.valueOfFrom(vo.commandType);
                    e.doOpen(vo.url, BrowserHistoryHandlerUI.this);
                }
            }

            // 重設bookmarkConfig 時間
            mLastestUpdateKeeper.update(false);
        } finally {
            searchBool.set(false);
        }
    }

    private class LastestUpdateKeeper {
        String latestAdd;
        String latestUpdate;

        private String getLatestModify(List<UrlConfig> lst) {
            Collections.sort(lst, new Comparator<UrlConfig>() {
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                @Override
                public int compare(UrlConfig o1, UrlConfig o2) {
                    if (StringUtils.isBlank(o1.updateTimestamp)) {
                        return 1;
                    } else if (StringUtils.isNotBlank(o1.updateTimestamp) && StringUtils.isBlank(o2.updateTimestamp)) {
                        return -1;
                    } else if (StringUtils.isBlank(o1.updateTimestamp) && StringUtils.isBlank(o2.updateTimestamp)) {
                        return 0;
                    }
                    try {
                        Date d1 = SDF.parse(o1.updateTimestamp);
                        Date d2 = SDF.parse(o2.updateTimestamp);
                        if (d1.after(d2)) {
                            return -1;
                        } else if (d1.before(d2)) {
                            return 1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            UrlConfig urlConf = lst.get(0);
            String tmp = urlConf.title + "_" + urlConf.updateTimestamp;
            return tmp;
        }

        private String getLatestAdd(List<UrlConfig> lst) {
            Collections.sort(lst, new Comparator<UrlConfig>() {
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                @Override
                public int compare(UrlConfig o1, UrlConfig o2) {
                    if (StringUtils.isBlank(o1.timestamp)) {
                        return 1;
                    } else if (StringUtils.isNotBlank(o1.timestamp) && StringUtils.isBlank(o2.timestamp)) {
                        return -1;
                    } else if (StringUtils.isBlank(o1.timestamp) && StringUtils.isBlank(o2.timestamp)) {
                        return 0;
                    }
                    try {
                        Date d1 = SDF.parse(o1.timestamp);
                        Date d2 = SDF.parse(o2.timestamp);
                        if (d1.after(d2)) {
                            return -1;
                        } else if (d1.before(d2)) {
                            return 1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            UrlConfig urlConf = lst.get(0);
            String tmp = urlConf.title + "_" + urlConf.timestamp;
            return tmp;
        }

        public void update(boolean reload) {
            if (reload || StringUtils.isBlank(latestAdd) || StringUtils.isBlank(latestUpdate)) {
                List<UrlConfig> lst = getUrlConfigList(reload);
                if (!CollectionUtils.isEmpty(lst)) {
                    latestAdd = getLatestAdd(lst);
                    latestUpdate = getLatestModify(lst);
                } else {
                    latestAdd = "NA";
                    latestUpdate = "NA";
                }
            }
            BrowserHistoryHandlerUI.this.setTitle("新增 : " + latestAdd + ", 修改 : " + latestUpdate);
        }
    }

    private static class ColumnColorHandler {
        JTableUtil tableUtil;
        int totalClickTimes = -1;
        int hasClickSize = 0;
        int maxClickTime = 0;
        boolean initOk = false;

        JTable urlTable;
        PropertiesUtilBean bookmarkConfig;

        private ColumnColorHandler(JTable urlTable, PropertiesUtilBean bookmarkConfig) {
            this.urlTable = urlTable;
            this.bookmarkConfig = bookmarkConfig;
        }

        private int getClickTime(int row) {
            try {
                return Integer.parseInt((String) tableUtil.getRealValueAt(row, UrlTableConfigEnum.clickTimes.ordinal()));
            } catch (Exception ex) {
                return 0;
            }
        }

        private enum ColorDef {
            DEF1("", 0, 3), //
            DEF2("#ffff99", 3, 8), //
            DEF3("#ff6666", 8, 13), //
            DEF4("#ff0000", 13, Integer.MAX_VALUE),//
            ;

            final String colorStr;
            final int min;
            final int max;

            ColorDef(String colorStr, int min, int max) {
                this.colorStr = colorStr;
                this.min = min;
                this.max = max;
            }
        }

        private Color getColorByClickTimes(int clickTimes) {
            for (ColorDef e : ColorDef.values()) {
                if (e.min <= clickTimes && e.max > clickTimes) {
                    if (StringUtils.isBlank(e.colorStr)) {
                        return null;
                    } else {
                        return JColorUtil.rgb(e.colorStr);
                    }
                }
            }
            throw new RuntimeException("getColorByClickTimes ERR : " + clickTimes);
        }

        private void initClickTimesConfig() {
            if (initOk == true) {
                return;
            }
            try {
                tableUtil = JTableUtil.newInstance(urlTable);

                for (Enumeration enu = bookmarkConfig.getConfigProp().keys(); enu.hasMoreElements();) {
                    String key = (String) enu.nextElement();
                    UrlConfig d = UrlConfig.parseTo(key, bookmarkConfig.getConfigProp().getProperty(key));
                    int currTime = 0;
                    try {
                        currTime = Integer.parseInt(d.clickTimes);
                        maxClickTime = Math.max(maxClickTime, currTime);
                    } catch (Exception ex) {
                    }
                    if (currTime > 0) {
                        hasClickSize++;
                    }
                    totalClickTimes += currTime;
                }
                initOk = true;
            } catch (Exception ex) {
                System.out.println("initClickTimesConfig ERR : " + ex.getMessage());
            }
        }

        private void apply() {
            initClickTimesConfig();

            // 設定欄位顏色
            tableUtil.setColumnColor_byCondition(UrlTableConfigEnum.title.ordinal(), new JTableUtil.TableColorDef() {
                public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    int clickTime = getClickTime(row);
                    return Pair.of(getColorByClickTimes(clickTime), null);
                }
            });

            tableUtil.setColumnColor_byCondition(UrlTableConfigEnum.url.ordinal(), new JTableUtil.TableColorDef() {
                public Pair<Color, Color> getTableColour(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    String url = (String) tableUtil.getRealValueAt(row, UrlTableConfigEnum.url.ordinal());
                    if (DesktopUtil.getFile_ignoreFailed(url) != null) {
                        return Pair.of(JColorUtil.rgb("#b3f0ff"), null);
                    } else if (StringUtil_.isUUID(url)) {
                        return Pair.of(JColorUtil.rgb("#ffeba5"), null);
                    } else {
                        return Pair.of(JColorUtil.rgb("#c6ffb3"), null);
                    }
                }
            });
        }

    }

    private void deleteBtnAction() {
        try {
            Validate.notNull(bookmarkConfig, "請先設定bookmark設定黨路徑!");

            System.out.println("click 刪除");
            String message = "title : " + titleText.getText() + "\n" + //
                    "url : " + urlText.getText() + "\n" + //
                    "tag : " + tagComboBox.getSelectedItem() + "\n" + //
                    "remark : " + remarkArea.getText() + "\n" + //
                    "timestamp : " + modifyTimeLabel.getText() + "\n" + //
                    "";
            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(message, "確定刪除");
            if (!result) {
                return;
            }

            if (bookmarkConfig.getConfigProp().containsKey(urlText.getText())) {
                bookmarkConfig.getConfigProp().remove(urlText.getText());
                bookmarkConfig.store();
                initLoading();
                JCommonUtil._jOptionPane_showMessageDialog_info("移除成功!");
                return;
            }
            JCommonUtil._jOptionPane_showMessageDialog_info("找不到此設定!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void urlTextOnblur(boolean forceReloadTitle) {
        try {
            if (StringUtils.isBlank(urlText.getText())) {
                return;
            }

            try {// 以現有就載入
                String key = StringUtils.trimToEmpty(urlText.getText());
                UrlConfig d = UrlConfig.parseTo(key, bookmarkConfig.getConfigProp().getProperty(key));
                this.setUrlConfigToUI(d, false);
            } catch (Exception ex) {
            }

            // 檔案
            File file = new File(urlText.getText());
            if (file.exists()) {
                urlText.setText(file.toURL().toString());
                if (StringUtils.isBlank(titleText.getText())) {
                    titleText.setText(file.getName());
                }
                return;
            } else if (StringUtils.isBlank(titleText.getText()) || forceReloadTitle) {
                // 超連結
                String title = getHtmlTitle(urlText.getText());
                titleText.setText(title);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private static class UrlConfig {
        String clickTimes;
        String title;
        String url;
        String tag;
        String remark;
        String timestamp;// 建立時間
        String timestampLastest;// 最後一次點及時間
        String commandType;
        String isUseRemarkOpen;// 是否使用remarkOpen
        String isHidden;// 是否隱藏此項目
        String orderMark; // 特許排序
        String bootStart; // 開啟馬上執行
        String updateTimestamp; // 最後修改時間
        String periodSec;// 週期執行

        private static SpecialCharHandler specialCharHandler;

        static {
            specialCharHandler = new SpecialCharHandler();
        }

        private static String getConfigValue(UrlConfig d) {
            String title = specialCharHandler.getBeforeSave(d.title);
            String tag = specialCharHandler.getBeforeSave(d.tag);
            String timestamp = specialCharHandler.getBeforeSave(d.timestamp);
            String commandType = specialCharHandler.getBeforeSave(d.commandType);
            String timestampLastest = specialCharHandler.getBeforeSave(d.timestampLastest);
            String clickTimes = specialCharHandler.getBeforeSave(d.clickTimes);
            String isUseRemarkOpen = specialCharHandler.getBeforeSave(d.isUseRemarkOpen);
            String isHidden = specialCharHandler.getBeforeSave(d.isHidden);
            String remark = specialCharHandler.getBeforeSave(d.remark);
            String orderMark = specialCharHandler.getBeforeSave(d.orderMark);
            String bootStart = specialCharHandler.getBeforeSave(d.bootStart);
            String updateTimestamp = specialCharHandler.getBeforeSave(d.updateTimestamp);
            String periodSec = specialCharHandler.getBeforeSave(d.periodSec);
            return title + "^" + tag + "^" + remark + "^" + timestamp + "^" + commandType + "^" + timestampLastest + "^" + clickTimes + "^" + isUseRemarkOpen + "^" + isHidden + "^" + orderMark + "^"
                    + bootStart + "^" + updateTimestamp + "^" + periodSec;
        }

        private static String getArryStr(String[] args, int index) {
            if (args != null && args.length > index) {
                return specialCharHandler.getFromProperty(args[index]);
            }
            return "";
        }

        private static UrlConfig parseTo(String key, String propertiesValue) {
            String[] args = StringUtils.trimToEmpty(propertiesValue).split("\\^", -1);
            if (args.length >= 4) {
                String title = getArryStr(args, 0);
                String tag = getArryStr(args, 1);
                String remark = getArryStr(args, 2);
                String timestamp = getArryStr(args, 3);
                String commandType = getArryStr(args, 4);
                String timestampLastest = getArryStr(args, 5);
                String clickTimes = getArryStr(args, 6);
                String isUseRemarkOpen = getArryStr(args, 7);
                String isHidden = getArryStr(args, 8);
                String orderMark = getArryStr(args, 9);
                String bootStart = getArryStr(args, 10);
                String updateTimestamp = getArryStr(args, 11);
                String periodSec = getArryStr(args, 12);

                UrlConfig d = new UrlConfig();
                d.title = title;
                d.tag = tag;
                d.remark = remark;
                d.timestamp = timestamp;
                d.url = key;
                d.commandType = commandType;
                d.timestampLastest = timestampLastest;
                d.clickTimes = clickTimes;
                d.isUseRemarkOpen = isUseRemarkOpen;
                d.isHidden = isHidden;
                d.orderMark = orderMark;
                d.bootStart = bootStart;
                d.updateTimestamp = updateTimestamp;
                d.periodSec = periodSec;

                return d;
            }
            throw new RuntimeException("無法取得設定 : " + key + " -> " + propertiesValue);
        }

        private static class SpecialCharHandler {
            private final String REP_STR = "_#SPECIALCHAR#_";

            private String getBeforeSave(String orignRemark) {
                String text = StringUtils.trimToEmpty(orignRemark);
                text = text.replaceAll("\\^", REP_STR);
                return text;
            }

            private String getFromProperty(String orignRemark) {
                String text = StringUtils.trimToEmpty(orignRemark);
                return text.replaceAll(Pattern.quote(REP_STR), "^");
            }
        }
    }

    private String getHtmlTitle(String url) throws IOException {
        url = StringUtils.trimToEmpty(url);
        try {
            File file = DesktopUtil.getFile(url);
            if (file.exists()) {
                return file.getName();
            }
        } catch (Exception ex) {
        }
        final String finalUrl = url;

        JProgressBarHelper jProgDlg = JProgressBarHelper.newInstance(this, "取得標題")//
                .indeterminate(true)//
                .modal(false)//
                .max(100).build()//
                .showRightNow();//

        String title = "";
        try {
            title = ThreadUtil.runUseBlockingQueue(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String title = "";
                    try {
                        String content = __doGetRequest_UserAgent(finalUrl, DEFAULT_USER_AGENT);
                        System.out.println("-----------------------------------------------------------------------");
                        // System.out.println(content);

                        Pattern ptn = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.MULTILINE);
                        Matcher mth = ptn.matcher(content);
                        if (mth.find()) {
                            title = mth.group(1);
                            title = StringEscapeUtils.unescapeHtml(title);
                            System.out.println(title);
                        }
                        System.out.println("-----------------------------------------------------------------------");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
            }, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jProgDlg.dismiss();
        }
        return title;
    }

    private void configSaveBtnAction() {
        try {
            File file = JCommonUtil.filePathCheck(bookmarkConfigText.getText(), "bookmark設定黨", "properties");
            bookmarkConfig = new PropertiesUtilBean(file);
            initLoading();
            configSelf.reflectSetConfig(this);
            configSelf.store();
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void urlTableClickAction(MouseEvent e) {
        try {
            JTableUtil jtab = JTableUtil.newInstance(urlTable);
            int rowPos = jtab.getSelectedRow();
            int colPos = UrlTableConfigEnum.VO.ordinal();

            Object config = null;
            try {
                config = jtab.getRealValueAt(rowPos, colPos);
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                return;
            }

            if (config == null || !(config instanceof UrlConfig)) {
                System.out.println("<<<選取有誤");
                return;
            }

            UrlConfig d = (UrlConfig) config;
            System.out.println("click 選取");
            this.setUrlConfigToUI(d, true);

            System.out.println("[open]<<<" + d.title + " = " + d.url);

            if (JMouseEventUtil.buttonLeftClick(2, e)) {
                // commandTypeSetting.getValue().doOpen(d.url,
                // BrowserHistoryHandlerUI.this);
                doOpenUrlMaster(d.url);
            } else {
                doRightClickShowMenuAction(urlTable, e, d.url);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void setUrlConfigToUI(UrlConfig d, boolean throwEx) {
        try {
            System.out.println(ReflectionToStringBuilder.toString(d));
            urlText.setText(d.url);
            titleText.setText(d.title);
            mHyperlinkJTextPaneHandler.setText(d.remark);
            modifyTimeLabel.setText(d.timestamp);
            tagComboBoxUtil.setSelectItemAndText(d.tag);
            commandTypeSetting.setValue(d.commandType);
            useRemarkOpenChk.setSelected("Y".equals(d.isUseRemarkOpen));
            hiddenChk.setSelected("Y".equals(d.isHidden));
            bootstartCombobox.setSelectedItem(StringUtils.trimToEmpty(d.bootStart));
            periodSecText.setText(StringUtils.trimToEmpty(d.periodSec));
        } catch (Exception ex) {
            if (throwEx) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class HyperlinkJTextPaneHandler {
        private Pattern ptn = Pattern.compile(PatternCollection.HYPER_LINK_PATTERN);
        JTextPane remarkArea;

        private HyperlinkJTextPaneHandler(JTextPane remarkArea) {
            this.remarkArea = remarkArea;
        }

        private void setText(String remark) {
            remarkArea.setText(remark);
            if (StringUtils.isNotBlank(remark)) {
                Matcher mth = ptn.matcher(remark);
                while (mth.find()) {
                    JTextPaneTextStyle.of(remarkArea).startEnd(mth.start(), mth.end()).foregroundColor(Color.blue).italic(true).underline(true).apply();
                }
            }
        }

        private void onAddMouseListener(MouseEvent e) {
            if (JMouseEventUtil.buttonLeftClick(2, e)) {
                int pos = remarkArea.getCaretPosition();
                String remark = remarkArea.getText();
                if (StringUtils.isNotBlank(remark)) {
                    Matcher mth = ptn.matcher(remark);
                    while (mth.find()) {
                        if (mth.start() <= pos && mth.end() >= pos) {
                            System.out.println("<<<<<<<<<" + mth.group());
                            DesktopUtil.browse(mth.group());
                        }
                    }
                }
            }
        }
    }

    private class YellowMarkJTextPaneHandler {
        JTextPane logWatcherTextArea;
        String findText;

        private YellowMarkJTextPaneHandler(JTextPane logWatcherTextArea, String findText) {
            this.logWatcherTextArea = logWatcherTextArea;
            this.findText = findText;
        }

        private void process() {
            String remark = logWatcherTextArea.getText();
            JTextPaneUtil.newInstance(logWatcherTextArea).setTextReset(remark);
            if (StringUtils.isNotBlank(remark)) {
                Pattern ptn = Pattern.compile(Pattern.quote(findText), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
                Matcher mth = ptn.matcher(remark);
                while (mth.find()) {
                    int start = mth.start(0);
                    int end = mth.end(0);
                    JTextPaneTextStyle.of(logWatcherTextArea).startEnd(start, end).backgroundColor(Color.YELLOW).italic(true).apply();
                }

                // int tempPos = 0;
                // while (true) {
                // int startPos = StringUtils.indexOf(remark, findText,
                // tempPos);
                // if (startPos == -1) {
                // break;
                // }
                // JTextPaneTextStyle.of(logWatcherTextArea).startEnd(startPos,
                // startPos +
                // findText.length()).backgroundColor(Color.YELLOW).italic(true).apply();
                // tempPos = startPos + findText.length();
                // }
            }
        }
    }

    private class CommandTypeSetting {
        private void setValue(String commandType) {
            commandTypComboBox.setSelectedItem(CommandTypeEnum.valueOfFrom(commandType));
        }

        private CommandTypeEnum getValue() {
            CommandTypeEnum commandType = (CommandTypeEnum) commandTypComboBox.getSelectedItem();
            if (commandType == null) {
                return CommandTypeEnum.DEFAULT;
            }
            return commandType;
        }
    }

    private void allOpenBtnAction() {
        try {
            JTableUtil jtab = JTableUtil.newInstance(urlTable);
            for (int ii = 0; ii < jtab.getModel().getRowCount(); ii++) {
                boolean isChk = (Boolean) jtab.getRealValueAt(ii, UrlTableConfigEnum.開啟.ordinal());
                UrlConfig vo = (UrlConfig) jtab.getRealValueAt(ii, UrlTableConfigEnum.VO.ordinal());
                if (isChk) {
                    CommandTypeEnum e = CommandTypeEnum.valueOfFrom(vo.commandType);
                    e.doOpen(vo.url, BrowserHistoryHandlerUI.this);
                }
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void doRightClickShowMenuAction(JComponent parent, MouseEvent e, String url) {
        try {
            url = StringUtils.trimToEmpty(url);
            if (JMouseEventUtil.buttonRightClick(1, e)) {
                JPopupMenuUtil popupUtil = JPopupMenuUtil.newInstance(parent);//

                // 檔案的處理
                if (DesktopUtil.getFile_ignoreFailed(url) != null) {
                    popupUtil//
                            .addJMenuItem("開啟目錄", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    openFileDirectory();
                                }
                            })//
                            .addJMenuItem("檔案以參數開啟", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        String url = StringUtils.trimToEmpty(urlText.getText());
                                        File file = DesktopUtil.getFile(url);
                                        url = file.getAbsolutePath();
                                        String options = JCommonUtil._jOptionPane_showInputDialog("請輸入執行參數", "");
                                        String command = String.format("cmd /k /c call \"%s\"  %s ", url, options);
                                        Runtime.getRuntime().exec(command);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                            .addJMenuItem("以remark開啟", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String url = StringUtils.trimToEmpty(urlText.getText());
                                    UrlConfig d = UrlConfig.parseTo(url, bookmarkConfig.getConfigProp().getProperty(url));
                                    doOpenWithRemark(d, false);
                                }
                            })//
                    ;
                }

                // 多項處理
                if (true) {
                    final JTableUtil jtab = JTableUtil.newInstance(urlTable);
                    final int[] rowArry = jtab.getSelectedRows(true);

                    popupUtil//
                            .addJMenuItem("URL以參數開啟", new ActionListener() {

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        String url = StringUtils.trimToEmpty(urlText.getText());
                                        UrlConfig d = UrlConfig.parseTo(url, bookmarkConfig.getConfigProp().getProperty(url));

                                        String options = JCommonUtil._jOptionPane_showInputDialog("請輸入執行參數", "");

                                        if (StringUtils.isNotBlank(options)) {
                                            options = URLEncoder.encode(options, "UTF-8");
                                        }
                                        String newURL = String.format(url, options);

                                        CommandTypeEnum e1 = CommandTypeEnum.valueOfFrom(d.commandType);
                                        e1.doOpen(newURL, BrowserHistoryHandlerUI.this);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                            .addJMenuItem("修改tag", new ActionListener() {

                                private String getDefaultTag() {
                                    Set<String> set = new LinkedHashSet<String>();
                                    for (int row : rowArry) {
                                        UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                        set.add(vo.tag);
                                    }
                                    return StringUtils.join(set, ",");
                                }

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        String newTag = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("請賦予新的tag!", getDefaultTag()));
                                        if (StringUtils.isBlank(newTag)) {
                                            return;
                                        }
                                        StringBuilder sb = new StringBuilder();
                                        int size = 0;
                                        Properties tempConf = new Properties();
                                        for (int row : rowArry) {
                                            UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                            vo.tag = newTag;
                                            tempConf.setProperty(vo.url, UrlConfig.getConfigValue(vo));
                                            sb.append("" + vo.title + "\n");
                                            size++;
                                        }
                                        boolean isSave = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString() + "確定修改Tag : " + newTag, "確認");
                                        if (isSave) {
                                            bookmarkConfig.getConfigProp().putAll(tempConf);
                                            bookmarkConfig.store();
                                            initLoading();
                                            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功! size : " + size);
                                        }
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                            .addJMenuItem("修改remark", new ActionListener() {

                                private String getDefaultRemark() {
                                    Set<String> set = new LinkedHashSet<String>();
                                    for (int row : rowArry) {
                                        UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                        set.add(vo.remark);
                                    }
                                    return StringUtils.join(set, ",");
                                }

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        String newRemark = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("請賦予新的tag!", getDefaultRemark()));
                                        StringBuilder sb = new StringBuilder();
                                        int size = 0;
                                        Properties tempConf = new Properties();
                                        for (int row : rowArry) {
                                            UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                            vo.remark = newRemark;
                                            tempConf.setProperty(vo.url, UrlConfig.getConfigValue(vo));
                                            sb.append("" + vo.title + "\n");
                                            size++;
                                        }
                                        boolean isSave = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString() + "確定修改Remark : " + newRemark, "確認");
                                        if (isSave) {
                                            bookmarkConfig.getConfigProp().putAll(tempConf);
                                            bookmarkConfig.store();
                                            initLoading();
                                            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功! size : " + size);
                                        }
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                            .addJMenuItem("複製連結", new ActionListener() {

                                private String getRealAbstractUrl(String url) {
                                    File f = DesktopUtil.getFile(url);
                                    if (f != null) {
                                        return f.getAbsolutePath();
                                    }
                                    return url;
                                }

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        StringBuilder sb = new StringBuilder();
                                        for (int row : rowArry) {
                                            UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                            sb.append(getRealAbstractUrl(vo.url) + "\n");
                                        }
                                        ClipboardUtil.getInstance().setContents(sb.toString());
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                            .addJMenuItem("顯示/隱藏", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        String isHdden = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("是否隱藏 (Y/N)", "N"));
                                        StringBuilder sb = new StringBuilder();
                                        int size = 0;
                                        Properties tempConf = new Properties();
                                        for (int row : rowArry) {
                                            UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                            vo.isHidden = isHdden;
                                            tempConf.setProperty(vo.url, UrlConfig.getConfigValue(vo));
                                            sb.append("" + vo.title + "\n");
                                            size++;
                                        }
                                        boolean isSave = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString() + "確定修改隱藏 : " + isHdden, "確認");
                                        if (isSave) {
                                            bookmarkConfig.getConfigProp().putAll(tempConf);
                                            bookmarkConfig.store();
                                            initLoading();
                                            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功! size : " + size);
                                        }
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                    ;
                }

                popupUtil.applyEvent(e).show();
            }
        } catch (

        Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void openFileDirectory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = StringUtils.trimToEmpty(urlText.getText());
                    File file = DesktopUtil.getFile(url);
                    if (file.exists()) {
                        DesktopUtil.browseFileDirectory(file);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }).start();
    }

    private void initAddSaveShortcutKeyEvent() {
        Component[] arry = new Component[] { titleText, //
                urlText, //
                remarkArea, //
                searchComboBoxUtil.getTextComponent(),//
        };
        for (Component e : arry) {
            e.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_S) {
                        System.out.println("do save");
                        beforeSaveCheckBrowser();
                        saveCurrentBookmarkBtnAction();
                    }
                }
            });
        }
    }

    private void nonWorkChkAction() {
        if (!nonWorkChk.isSelected()) {
            return;
        }
        final JProgressBarHelper helper = JProgressBarHelper.newInstance(BrowserHistoryHandlerUI.this, "檢測URL中");
        helper.closeListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BrowserHistoryHandlerUI.this.nonWorkChk.setSelected(false);
            }
        });
        helper.max(bookmarkConfig.getConfigProp().size());
        helper.build();
        helper.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initLoading(helper);
                helper.dismissByMax();
            }
        }).start();
    }

    private void bringToTop() {
        JCommonUtil.setFrameAtop(this, false);

        for (; true;) {
            this.tabbedPane.setSelectedIndex(1);
            if (this.tabbedPane.getSelectedIndex() == 1) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }

        JCommonUtil.setLocationToRightBottomCorner(this);

        JCommonUtil.focusComponent(searchComboBoxUtil.getTextComponent(), true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent paramActionEvent) {
                ((JTextField) searchComboBoxUtil.getTextComponent()).selectAll();
            }
        });
    }

    private static class OtherOpenPath {
        private static String PATH_IE;
        private static String PATH_CHROME;
        private static String PATH_FIREFOX;

        private OtherOpenPath(PropertiesUtilBean configSelf) {
            PATH_CHROME = StringUtils.trimToEmpty(configSelf.getConfigProp().getProperty("chrome"));
            PATH_FIREFOX = StringUtils.trimToEmpty(configSelf.getConfigProp().getProperty("firefox"));
            PATH_IE = StringUtils.trimToEmpty(configSelf.getConfigProp().getProperty("ie"));
        }
    }

    private void doOpenWithRemark(UrlConfig d, boolean isSaveBatFile) {
        try {
            String command = d.remark;
            command = StringUtils.trimToEmpty(command);

            if (StringUtil_.isUUID(d.url) && StringUtils.isNotBlank(command) && "Y".equalsIgnoreCase(d.isUseRemarkOpen)) {
                // 純BAT命令
            } else {
                Pattern ptn = Pattern.compile("\\%(?:1\\$|)s");
                Matcher mth = ptn.matcher(command);
                if (mth.find()) {
                    String fixUrl = fixWindowUrl(d.url);
                    File file = DesktopUtil.getFile(fixUrl);
                    if (file == null || !file.exists() || !file.isFile()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("無法執行此連結!");
                        return;
                    }
                    command = String.format(command, file);
                } else {
                    // do nothing
                }
            }

            RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance().command(command);
            if (isPeriodSecRunning(d.url)) {
                inst.runInBatFile(false);
            }
            if (!isSaveBatFile) {
                int waittingTime = 0;
                try {
                    waittingTime = Integer.parseInt(batWaittingTimeText.getText());
                } catch (Exception ex) {
                }
                ProcessWatcher watcher = ProcessWatcher.newInstance(inst.apply());
                watcher.processAsyncCallback(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        batLogArea.setText("");
                        batLogArea.append("產生時間 : " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss.SSS") + "\n");
                        ProcessWatcher p = (ProcessWatcher) e.getSource();
                        batLogArea.append("[ERR]  \n");
                        batLogArea.append(p.getErrorStreamToString());
                        batLogArea.append("\n\n[INFO] \n");
                        batLogArea.append(p.getInputStreamToString());
                    }
                }, waittingTime * 1000L);
                watcher.getStreamAsync();
            } else {
                String subName = OsInfoUtil.isWindows() ? ".bat" : ".sh";
                String encoding = OsInfoUtil.isWindows() ? "BIG5" : "UTF8";
                String fileName = JCommonUtil._jOptionPane_showInputDialog("輸入檔名", "bat_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + subName);
                if (StringUtils.isNotBlank(fileName)) {
                    FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, fileName), inst.getCommand(), encoding);
                    JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功！");
                }
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------

    private class BrowserHistoryHandlerUI_KeyboardListener implements NativeKeyListener {

        BrowserHistoryHandlerUI_KeyboardListener() {
            initialize();
        }

        private void initialize() {
            try {
                if (!GlobalScreen.isNativeHookRegistered()) {
                    GlobalScreen.registerNativeHook();
                }
                JnativehookKeyboardMouseHelper.getInstance().disableLogger();
            } catch (NativeHookException e) {
                JCommonUtil.handleException(e);
                throw new RuntimeException(e);
            }
            GlobalScreen.removeNativeKeyListener(this);// 記得她媽先移除否則會多掛listener
                                                       // XXX
            GlobalScreen.addNativeKeyListener(this);
        }

        public void close() {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.removeNativeKeyListener(this);
            }
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent parazzmNativeKeyEvent) {
        }

        private void startNewUI() {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        if (JFrameUtil.isVisible(BrowserHistoryHandlerUI.this)) {
                            gtu.swing.util.JFrameUtil.setVisible(false, BrowserHistoryHandlerUI.this);
                        } else {
                            bringToTop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            synchronized (BrowserHistoryHandlerUI.class) {
                if ((e.getModifiers() & NativeInputEvent.META_L_MASK) != 0 && //
                        e.getKeyCode() == NativeKeyEvent.VC_Z) {
                    startNewUI();
                }
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------

    private static String __doGetRequest_UserAgent(String url, String userAgent) {
        try {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            URI uri = new URI(url);

            CookieStore cookieStore = new BasicCookieStore();
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);

            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpget = new HttpGet(uri);
            if (userAgent != null && userAgent.length() > 0) {
                httpget.setHeader("User-Agent", userAgent);
            }

            Pattern charsetPtn = Pattern.compile("\\<meta\\s+.*?charset=\"?(.*?)\"?\\>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                InputStream instream = entity.getContent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] arr = new byte[1024 * 1024];
                int available = -1;
                while ((available = instream.read(arr)) > 0) {
                    baos.write(arr, 0, available);
                }
                baos.flush();
                baos.close();

                String content = new String(baos.toByteArray(), "UTF-8");
                Matcher mth = charsetPtn.matcher(content);
                if (mth.find()) {
                    String realCharset = mth.group(1);
                    System.out.println("!!!! Charset => " + realCharset);
                    if (StringUtils.contains(realCharset.toUpperCase(), "BIG")) {
                        content = new String(baos.toByteArray(), "BIG5");
                    } else if (StringUtils.contains(realCharset.toUpperCase(), "GBK")) {
                        content = new String(baos.toByteArray(), "GBK");
                    }
                }
                return content;
            } else {
                String errMsg = "StatusCode : " + response.getStatusLine().getStatusCode() + ", " + response.getStatusLine().getReasonPhrase();
                throw new RuntimeException(errMsg);
            }
        } catch (Exception ex) {
            throw new RuntimeException("doGetRequest_UserAgent Err : " + ex.getMessage(), ex);
        }
    }

    private class NormalLogicTagMatch {
        List<String> tagLst = new ArrayList<String>();
        String comparator;
        String compareText;

        NormalLogicTagMatch(UrlConfig d, String comparator, String compareText) {
            String[] arry = StringUtils.trimToEmpty(d.tag).split(",");
            for (String v : arry) {
                v = StringUtils.trimToEmpty(v);
                if (StringUtils.isNotBlank(v)) {
                    tagLst.add(v);
                }
            }
            this.comparator = comparator;
            this.compareText = compareText;
        }

        private boolean isMatch() {
            for (String tag : tagLst) {
                if ("^=".equalsIgnoreCase(comparator)) {
                    if (tag.toLowerCase().startsWith(compareText.toLowerCase())) {
                        return true;
                    }
                } else if ("$=".equalsIgnoreCase(comparator)) {
                    if (tag.toLowerCase().endsWith(compareText.toLowerCase())) {
                        return true;
                    }
                } else if ("*=".equalsIgnoreCase(comparator)) {
                    if (tag.toLowerCase().contains(compareText.toLowerCase())) {
                        return true;
                    }
                } else if ("=".equalsIgnoreCase(comparator)) {
                    if (tag.equalsIgnoreCase(compareText)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static String __doGetRequest(String urlStr, String encode) throws IOException {
        StringBuilder response = new StringBuilder();
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader isr = null;
        char[] buff = new char[4096];
        int size = 0;
        int r = 0;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            if (conn == null)
                return "";

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setDoInput(true);

            is = conn.getInputStream();
            isr = new InputStreamReader(is, encode);
            while ((r = isr.read(buff)) > 0) {
                response.append(buff, 0, r);
                size += r;
                if (size >= Integer.MAX_VALUE) {
                    break;
                }
            }

            return response.toString();

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private class DropboxBookmarkConfigMergeHandler {
        private List<File> removeLst = new ArrayList<File>();
        private List<String> mergeFileLst = new ArrayList<String>();

        private void cleanFiles() {
            for (File f : removeLst) {
                if (!f.getName().equals("BrowserHistoryHandlerUI_bookmark.properties")) {
                    f.delete();
                }
            }
        }

        private UrlConfig __getNewConfig_for_Merge(UrlConfig d1, UrlConfig d2) throws ParseException {
            // if (StringUtils.isBlank(d1.timestampLastest) &&
            // StringUtils.isBlank(d2.timestampLastest)) {
            // if (UrlConfig.getConfigValue(d1).length() >
            // UrlConfig.getConfigValue(d2).length()) {
            // return d1;
            // } else {
            // return d2;
            // }
            // }

            if (StringUtils.isNotBlank(d1.timestampLastest) && StringUtils.isBlank(d2.timestampLastest)) {
                return d1;
            } else if (StringUtils.isNotBlank(d2.timestampLastest) && StringUtils.isBlank(d1.timestampLastest)) {
                return d2;
            } else if (StringUtils.isBlank(d1.timestampLastest) && StringUtils.isBlank(d2.timestampLastest)) {
                return d1;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            if (sdf.parse(d1.timestampLastest).after(sdf.parse(d2.timestampLastest))) {
                return d1;
            } else {
                return d2;
            }
        }

        private void __mergeToMap(Properties prop, Map<String, UrlConfig> mergeMap) throws ParseException {
            for (Enumeration<?> enu = prop.keys(); enu.hasMoreElements();) {
                String url = (String) enu.nextElement();
                String propValue = prop.getProperty(url);
                UrlConfig d = UrlConfig.parseTo(url, propValue);
                if (mergeMap.containsKey(url)) {
                    d = __getNewConfig_for_Merge(d, mergeMap.get(url));
                }
                mergeMap.put(url, d);
            }
        }

        private Properties getMergeProperties(File dropboxDir) throws FileNotFoundException, IOException, ParseException {
            Map<String, UrlConfig> map = new HashMap<String, UrlConfig>();
            for (File f : dropboxDir.listFiles()) {
                if (f.getName().matches("BrowserHistoryHandlerUI_bookmark.*\\.properties")) {
                    Properties prop = new Properties();
                    PropertiesUtil.loadProperties(new FileInputStream(f), prop);
                    __mergeToMap(prop, map);

                    mergeFileLst.add(f.getName());

                    if (!f.equals(bookmarkConfig.getPropFile())) {
                        removeLst.add(f);
                    }
                }
            }
            __mergeToMap(bookmarkConfig.getConfigProp(), map);

            Properties prop = new Properties();
            for (UrlConfig d : map.values()) {
                prop.setProperty(d.url, UrlConfig.getConfigValue(d));
            }
            return prop;
        }
    }

    private void dropboxMergeBtnAction() {
        try {
            File dir = bookmarkConfig.getPropFile().getParentFile();
            DropboxBookmarkConfigMergeHandler handler = new DropboxBookmarkConfigMergeHandler();
            bookmarkConfig.getConfigProp().putAll(handler.getMergeProperties(dir));
            bookmarkConfig.store();
            handler.cleanFiles();
            JCommonUtil._jOptionPane_showMessageDialog_info(StringUtils.join(handler.mergeFileLst, "\r\n") + "\nMerge完成!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void saveBatFileBtnAction() {
        try {
            String url = StringUtils.trimToEmpty(urlText.getText());
            if (bookmarkConfig.getConfigProp().containsKey(url)) {
                String propVal = bookmarkConfig.getConfigProp().getProperty(url);
                UrlConfig d = UrlConfig.parseTo(url, propVal);
                doOpenWithRemark(d, true);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void fastSearchMouseClicked(MouseEvent e) {
        if (!JMouseEventUtil.buttonRightClick(1, e)) {
            return;
        }
        JPopupMenuUtil.newInstance((JComponent) e.getSource())//
                .addJMenuItem("熱門選項", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SearchResultProcess mSearchResultProcess = new SearchResultProcess() {

                            @Override
                            List<UrlConfig> filter(List<UrlConfig> lst) {
                                return new NormalDistributionFilter<UrlConfig>(lst) {
                                    public boolean isNeedPredict(UrlConfig bean) {
                                        return StringUtils.isNotBlank(bean.clickTimes);
                                    }

                                    public BigDecimal getValue(UrlConfig bean) {
                                        return new BigDecimal(bean.clickTimes);
                                    }

                                    public Comparator<UrlConfig> getCompare() {
                                        return new Comparator<UrlConfig>() {
                                            private Integer getVal(String val) {
                                                try {
                                                    return Integer.parseInt(val);
                                                } catch (Exception ex) {
                                                    return Integer.MIN_VALUE;
                                                }
                                            }

                                            @Override
                                            public int compare(UrlConfig o1, UrlConfig o2) {
                                                return getVal(o1.clickTimes).compareTo(getVal(o2.clickTimes)) * -1;
                                            }
                                        };
                                    }
                                }.getRangeLst(0.90, 1);
                            }
                        };
                        mSearchResultProcess.process();
                    }
                })//
                .addJMenuItem("冷門選項", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SearchResultProcess mSearchResultProcess = new SearchResultProcess() {

                            @Override
                            List<UrlConfig> filter(List<UrlConfig> lst) {
                                return new NormalDistributionFilter<UrlConfig>(lst) {
                                    public boolean isNeedPredict(UrlConfig bean) {
                                        return StringUtils.isNotBlank(bean.clickTimes);
                                    }

                                    public BigDecimal getValue(UrlConfig bean) {
                                        return new BigDecimal(bean.clickTimes);
                                    }

                                    public Comparator<UrlConfig> getCompare() {
                                        return new Comparator<UrlConfig>() {
                                            private Integer getVal(String val) {
                                                try {
                                                    return Integer.parseInt(val);
                                                } catch (Exception ex) {
                                                    return Integer.MIN_VALUE;
                                                }
                                            }

                                            @Override
                                            public int compare(UrlConfig o1, UrlConfig o2) {
                                                return getVal(o1.clickTimes).compareTo(getVal(o2.clickTimes));
                                            }
                                        };
                                    }
                                }.getRangeLst(0, 0.5);
                            }
                        };
                        mSearchResultProcess.process();
                    }
                }).applyEvent(e).show();
    }

    private abstract class SearchResultProcess {
        abstract List<UrlConfig> filter(List<UrlConfig> lst);

        private void process() {
            // ------------------------------------------
            final JTableUtil tableUtil = JTableUtil.newInstance(urlTable);
            DefaultTableModel model = JTableUtil.createModel(new int[] { UrlTableConfigEnum.開啟.ordinal() }, UrlTableConfigEnum.getTitleConfig());
            tableUtil.hiddenColumn(UrlTableConfigEnum.VO.name());
            urlTable.setModel(model);
            columnColorHandler.apply();

            urlTableResize();

            for (String v : new String[] { UrlTableConfigEnum.刪除.name() }) {
                System.out.println("columnIsButton = " + v);
                tableUtil.columnIsButton(v);
            }
            tableUtil.columnIsComponent(UrlTableConfigEnum.開啟.ordinal(), new JCheckBox());// 設定為checkbox

            // ------------------------------------------
            List<UrlConfig> lst = new ArrayList<UrlConfig>();
            for (Enumeration<?> enu = bookmarkConfig.getConfigProp().keys(); enu.hasMoreElements();) {
                String url = (String) enu.nextElement();
                final String title_tag_remark_time = bookmarkConfig.getConfigProp().getProperty(url);
                UrlConfig dd = null;
                try {
                    dd = UrlConfig.parseTo(url, title_tag_remark_time);
                    lst.add(dd);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }

            lst = filter(lst);

            for (final UrlConfig d : lst) {
                model.addRow(UrlTableConfigEnum.getRow(d, BrowserHistoryHandlerUI.this));
            }

            System.out.println("searchSize : " + model.getRowCount());

            matchCountLabel.setText((model.getRowCount() == 0) ? "查無!" : "數量:" + model.getRowCount());

            // 重設bookmarkConfig 時間
            BrowserHistoryHandlerUI.this.setTitle("書籤最後修改時間 : " + DateFormatUtils.format(bookmarkConfig.getPropFile().lastModified(), "yyyy/MM/dd HH:mm:ss"));
        }
    }

    private class PeriodThread extends TimerTask {
        private boolean stop = false;
        private Map<String, PeriodThread> periodSecHolder;
        private String url;
        private Timer timer = new Timer();

        public PeriodThread(String url, Map<String, PeriodThread> periodSecHolder) {
            this.url = url;
            this.periodSecHolder = periodSecHolder;
        }

        public void doStart(boolean doInstant, int second) {
            long startLag = second * 1000;
            long period = second * 1000;
            if (doInstant) {
                startLag = 0;
            }
            timer.schedule(this, startLag, period);
        }

        private void removeThis() {
            for (String mUrl : periodSecHolder.keySet()) {
                if (StringUtils.equals(mUrl, this.url)) {
                    periodSecHolder.remove(mUrl);
                    System.out.println("period Remove : " + this.url);
                    break;
                }
            }
        }

        @Override
        public void run() {
            if (stop) {
                this.cancel();
                removeThis();
                return;
            }
            {
                commandTypeSetting.getValue().doOpen(this.url, BrowserHistoryHandlerUI.this);
            }
        }
    }

    // LogWatcher ↓↓↓↓↓↓
    // ====================================================================================================================
    private LogWatcherPeriodTask mLogWatcherPeriodTask = null;
    private JButton logWatcherClearBtn;
    private JCheckBox logWatcherFrontChk;
    private JLabel logWatcherSizeChangeLbl;
    private JTextField logWatcherCustomFileText;

    private class LogWatcherPeriodTask extends TimerTask {
        boolean stop = false;
        LogWatcher mTxtFileChecker;
        long bringToFrontTime = -1;
        long bringToFrontPeriod = 1500;
        File logFile;

        private void bringToFront() {
            if (bringToFrontTime == -1 || (System.currentTimeMillis() - bringToFrontTime) > bringToFrontPeriod) {
                JTabbedPaneUtil.newInst(tabbedPane).setSelectedIndexByTitle("LogWatcher");
                if (logWatcherFrontChk.isSelected() && !logWatcherTextArea.isFocusOwner()) {
                    JCommonUtil.setFrameAtop(BrowserHistoryHandlerUI.this, false);
                    bringToFrontTime = System.currentTimeMillis();
                    JTextAreaUtil.setScrollToPosition(logWatcherTextArea, null);
                }
            }
        }

        private LogWatcherPeriodTask(File logFile) {
            try {
                this.logFile = logFile;
                mTxtFileChecker = new LogWatcher(logFile, "UTF8") {
                    @Override
                    public void write(String line) {
                        bringToFront();
                        JTextPaneUtil.newInstance(logWatcherTextArea).append(line);
                    }
                };
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        @Override
        public void run() {
            try {
                if (stop) {
                    this.cancel();
                    JCommonUtil._jOptionPane_showMessageDialog_error("停止監聽Log");
                    logWatcherBtn.setText("監聽off");
                    try {
                        long beforeSize = Long.parseLong(logWatcherSizeChangeLbl.getText());
                        logWatcherSizeChangeLbl.setText("差異 :" + String.valueOf(logFile.length() - beforeSize));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return;
                }
                mTxtFileChecker.checkFiles();
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }
    };

    private void logWatcherBtnToggleAction() {
        try {
            if (logWatcherBtn.isSelected()) {
                if (logWatcherBtn.getText().contains("監聽closing")) {
                    return;
                }
                File logFile = null;
                if (StringUtils.isNotBlank(logWatcherCustomFileText.getText())) {
                    logFile = DesktopUtil.getFile(logWatcherCustomFileText.getText());
                }
                if (logFile == null) {
                    try {
                        logFile = DesktopUtil.getFile(urlText.getText());
                    } catch (Exception ex) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("檔案路徑有誤!");
                        return;
                    }
                }
                int period = 500;
                try {
                    period = Integer.parseInt(logWatcherPeriodText.getText());
                } catch (Exception ex) {
                    logWatcherPeriodText.setText(String.valueOf(period));
                }
                logWatcherTextArea.setText("## 監聽開始 : " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss.SSS") + "\n");
                mLogWatcherPeriodTask = new LogWatcherPeriodTask(logFile);
                new Timer().schedule(mLogWatcherPeriodTask, 0, period);
                logWatcherBtn.setText("監聽start");
                logWatcherSizeChangeLbl.setText(String.valueOf(logFile.length()));
            } else if (mLogWatcherPeriodTask != null) {
                mLogWatcherPeriodTask.stop = true;
                logWatcherBtn.setText("監聽closing");
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void logWatcherTextAreaLogCacheClean() {
        if (logWatcherTextArea.getText().length() > 200000) {
            String text = StringUtils.substring(logWatcherTextArea.getText(), 100000);
            logWatcherTextArea.setText("");
            StringBuilder sb = new StringBuilder();
            sb.append("已清除部分log(" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss.SSS") + ")...\n");
            sb.append(text);
            logWatcherTextArea.setText(sb.toString());
        }
    }

    private void logWatcherTextAreaKeyEventAction(KeyEvent e) {
        if (KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_F) {
            String findText = JCommonUtil._jOptionPane_showInputDialog("搜尋text!");
            if (StringUtils.isNotEmpty(findText)) {
                YellowMarkJTextPaneHandler mYellowMarkJTextPaneHandler = new YellowMarkJTextPaneHandler(logWatcherTextArea, findText);
                mYellowMarkJTextPaneHandler.process();
            }
        }
    }
    // LogWatcher ↑↑↑↑↑↑
    // ====================================================================================================================
}