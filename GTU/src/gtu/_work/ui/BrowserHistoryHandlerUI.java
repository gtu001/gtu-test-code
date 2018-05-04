package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
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

import gtu.clipboard.ClipboardUtil;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.AutoComboBox;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JComboBoxUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import taobe.tec.jcc.JChineseConvertor;

public class BrowserHistoryHandlerUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField titleText;
    private JTextField urlText;
    private JLabel modifyTimeLabel;
    private PropertiesUtilBean configSelf = new PropertiesUtilBean(BrowserHistoryHandlerUI.class);
    private PropertiesUtilBean bookmarkConfig;
    private JComboBox tagComboBox;
    private JTextArea remarkArea;
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

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BrowserHistoryHandlerUI frame = new BrowserHistoryHandlerUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done..v10");
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
                            FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

            JLabel lblTitle = new JLabel("title");
            panel.add(lblTitle, "2, 2, right, default");

            titleText = new JTextField();
            panel.add(titleText, "4, 2, fill, default");
            titleText.setColumns(10);

            JLabel lblUrl = new JLabel("url");
            panel.add(lblUrl, "2, 4, right, default");

            urlText = new JTextField();
            urlText.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    urlTextOnblur();
                }
            });
            panel.add(urlText, "4, 4, fill, default");
            urlText.setColumns(10);

            JLabel lblTag = new JLabel("tag");
            panel.add(lblTag, "2, 6, right, default");

            tagComboBox = new JComboBox();
            tagComboBoxUtil = AutoComboBox.applyAutoComboBox(tagComboBox);
            panel.add(tagComboBox, "4, 6, fill, default");

            JLabel lblRemark = new JLabel("remark");
            panel.add(lblRemark, "2, 8");

            JScrollPane scrollPane = new JScrollPane();
            panel.add(scrollPane, "4, 8, fill, fill");

            remarkArea = new JTextArea();
            scrollPane.setViewportView(remarkArea);

            JLabel lblCommandType = new JLabel("command type");
            panel.add(lblCommandType, "2, 10, right, default");

            commandTypComboBox = new JComboBox();
            DefaultComboBoxModel commandTypeComboModel = new DefaultComboBoxModel();//
            for (CommandTypeEnum e : CommandTypeEnum.values()) {
                commandTypeComboModel.addElement(e);
            }
            commandTypComboBox.setModel(commandTypeComboModel);
            panel.add(commandTypComboBox, "4, 10, fill, default");

            JPanel panel_2 = new JPanel();
            panel.add(panel_2, "4, 11, fill, fill");
            modifyTimeLabel = new JLabel("修改時間");
            panel_2.add(modifyTimeLabel);

            JButton saveBtn = new JButton("儲存");
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveCurrentBookmarkBtnAction();
                }
            });

            final JButton btnNewButton = new JButton("開啟");
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String url = StringUtils.trimToEmpty(urlText.getText());
                    commandTypeSetting.getValue().doOpen(url, BrowserHistoryHandlerUI.this);
                }
            });
            btnNewButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String url = StringUtils.trimToEmpty(urlText.getText());
                    doRightClickShowMenuAction(btnNewButton, e, url);
                }
            });
            panel_2.add(btnNewButton);
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

            JLabel label = new JLabel("快速搜尋");
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

            JPanel panel_3 = new JPanel();
            tabbedPane.addTab("設定", null, panel_3, null);
            panel_3.setLayout(new FormLayout(
                    new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
                            FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

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

            pack();
            this.setSize(800, 450);

            configSelf.reflectInit(this);

            if (configSelf.getConfigProp().containsKey("bookmarkConfigText")) {
                bookmarkConfig = new PropertiesUtilBean(new File(configSelf.getConfigProp().getProperty("bookmarkConfigText")));
            }

            initLoading();

            JCommonUtil.defaultToolTipDelay();
            JCommonUtil.setJFrameDefaultSetting(this);
            JCommonUtil.setLocationToRightBottomCorner(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            commandTypeSetting = new CommandTypeSetting();
            sysUtil.apply(this);
            keyboardListener.initialize();
            bringToTop();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private enum CommandTypeEnum {
        DEFAULT("預設") {
            @Override
            void _doOpen(String url) {
                try {
                    DesktopUtil.browse(url);
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        }, //
        IE_EAGE("Microsoft Edge") {
            @Override
            void _doOpen(String url) {
                try {
                    Runtime.getRuntime().exec("cmd /c start microsoft-edge:" + url);
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        }, //
        FIREFOX("Mozilla Firefox") {
            @Override
            void _doOpen(String url) {
                try {
                    String exePath = "C:/Program Files/Mozilla Firefox/firefox.exe";
                    String command = String.format("cmd /c call \"%s\" %s ", exePath, url);
                    Runtime.getRuntime().exec(command);
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        }, //
        CHROME("Google Chrome") {
            @Override
            void _doOpen(String url) {
                try {
                    String exePath = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
                    String command = String.format("cmd /c call \"%s\" %s ", exePath, url);
                    Runtime.getRuntime().exec(command);
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

        abstract void _doOpen(String url);

        public void doOpen(String url, BrowserHistoryHandlerUI _this) {
            url = StringUtils.trimToEmpty(url);

            // 判斷是否為自動產生
            if (StringUtil_.isUUID(url)) {
                JCommonUtil._jOptionPane_showMessageDialog_error("此非合理URL!");
                return;
            }

            System.out.println("[doOpen]>>>" + this.name());
            _doOpen(url);
            UrlConfig d = UrlConfig.parseTo(url, _this.bookmarkConfig.getConfigProp().getProperty(url));
            _this.clickUrlDoLogAction(d);
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
        lastClick(9) {
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

    private void saveCurrentBookmarkBtnAction() {
        try {
            String url = StringUtils.trimToEmpty(urlText.getText());
            String title = StringUtils.trimToEmpty(titleText.getText());
            String tag = StringUtils.trimToEmpty(tagComboBox.getSelectedItem().toString());
            String remark = StringUtils.trimToEmpty(remarkArea.getText().toString());
            String commandType = commandTypeSetting.getValue().name();

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
            d.timestampLastest = "";

            bookmarkConfig.getConfigProp().setProperty(url, UrlConfig.getConfigValue(d));
            bookmarkConfig.store();

            this.initLoading();

            // 因為initLoading會清空
            tagComboBoxUtil.getTextComponent().setText(tag);

            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
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
    }

    private void urlTableResize() {
        JTableUtil.setColumnWidths_Percent(urlTable, UrlTableConfigEnum.getWidthConfig());
    }

    private void initLoading() {
        if (bookmarkConfig == null) {
            System.out.println("bookmarkConfig null !!!");
            return;
        }

        final List<String> tagLst = new ArrayList<String>();
        final List<UrlConfig> lst = new ArrayList<UrlConfig>();

        JTableUtil tableUtil = JTableUtil.newInstance(urlTable);
        DefaultTableModel model = JTableUtil.createModel(new int[] { UrlTableConfigEnum.開啟.ordinal() }, UrlTableConfigEnum.getTitleConfig());
        tableUtil.hiddenColumn(UrlTableConfigEnum.VO.name());
        urlTable.setModel(model);

        urlTableResize();

        for (String v : new String[] { UrlTableConfigEnum.刪除.name() }) {
            System.out.println("columnIsButton = " + v);
            tableUtil.columnIsButton(v);
        }
        tableUtil.columnIsComponent(UrlTableConfigEnum.開啟.ordinal(), new JCheckBox());// 設定為checkbox

        final String searchText = StringUtils.trimToEmpty(searchComboBoxUtil.getTextComponent().getText()).toLowerCase();
        System.out.println("searchText " + searchText);

        for (Enumeration<?> enu = bookmarkConfig.getConfigProp().keys(); enu.hasMoreElements();) {
            String url = (String) enu.nextElement();
            String title_tag_remark_time = bookmarkConfig.getConfigProp().getProperty(url);

            System.out.println("<<" + title_tag_remark_time);
            final UrlConfig d = UrlConfig.parseTo(url, title_tag_remark_time);

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

                private boolean isMatch(String singleText) {
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
                    }
                    return false;
                }

                @Override
                public void run() {
                    String[] searchArry = searchText.split(",", -1);
                    boolean isAllMatch = true;
                    for (String singleText : searchArry) {
                        singleText = StringUtils.trimToEmpty(singleText);
                        if (StringUtils.isNotBlank(singleText)) {
                            if (!isMatch(singleText)) {
                                isAllMatch = false;
                                break;
                            }
                        }
                    }
                    if (isAllMatch) {
                        lst.add(d);
                    }
                }
            }.run();

            // 過濾重複的
            new Runnable() {
                @Override
                public void run() {
                    for (String v : tagLst) {
                        if (v.equalsIgnoreCase(d.tag)) {
                            return;
                        }
                    }
                    tagLst.add(d.tag);
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
                return o1.timestamp.compareTo(o2.timestamp);
            }
        });

        for (final UrlConfig d : lst) {
            model.addRow(UrlTableConfigEnum.getRow(d, this));
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

    private void urlTextOnblur() {
        try {
            if (StringUtils.isBlank(urlText.getText())) {
                return;
            }

            // 檔案
            File file = new File(urlText.getText());
            if (file.exists()) {
                urlText.setText(file.toURL().toString());
                if (StringUtils.isBlank(titleText.getText())) {
                    titleText.setText(file.getName());
                }
                return;
            }

            // 超連結
            if (StringUtils.isBlank(titleText.getText())) {
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

        private static String getConfigValue(UrlConfig d) {
            return d.title + "^" + d.tag + "^" + d.remark + "^" + d.timestamp + "^" + d.commandType + "^" + d.timestampLastest + "^" + d.clickTimes;
        }

        private static String getArryStr(String[] args, int index) {
            if (args != null && args.length > index) {
                return args[index];
            }
            return "";
        }

        private static UrlConfig parseTo(String key, String title_tag_remark_time) {
            String[] args = StringUtils.trimToEmpty(title_tag_remark_time).split("\\^", -1);
            if (args.length >= 4) {
                String title = getArryStr(args, 0);
                String tag = getArryStr(args, 1);
                String remark = getArryStr(args, 2);
                String timestamp = getArryStr(args, 3);
                String commandType = getArryStr(args, 4);
                String timestampLastest = getArryStr(args, 5);
                String clickTimes = getArryStr(args, 6);

                UrlConfig d = new UrlConfig();
                d.title = title;
                d.tag = tag;
                d.remark = remark;
                d.timestamp = timestamp;
                d.url = key;
                d.commandType = commandType;
                d.timestampLastest = timestampLastest;
                d.clickTimes = clickTimes;

                return d;
            }
            throw new RuntimeException("無法取得設定 : " + key + " -> " + title_tag_remark_time);
        }
    }

    private String getHtmlTitle(String url) throws IOException {
        String title = "";
        try {
            String content = __doGetRequest_UserAgent(url, "UTF-8", DEFAULT_USER_AGENT);
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
            System.out.println(ReflectionToStringBuilder.toString(d));
            urlText.setText(d.url);
            titleText.setText(d.title);
            remarkArea.setText(d.remark);
            modifyTimeLabel.setText(d.timestamp);
            tagComboBoxUtil.getTextComponent().setText(d.tag);
            commandTypeSetting.setValue(d.commandType);

            urlTable.setToolTipText(d.url);
            System.out.println("[open]<<<" + d.title + " = " + d.url);

            if (JMouseEventUtil.buttonLeftClick(2, e)) {
                commandTypeSetting.getValue().doOpen(d.url, BrowserHistoryHandlerUI.this);
            } else {
                doRightClickShowMenuAction(urlTable, e, d.url);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
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
                if (DesktopUtil.getFile(url) != null) {
                    popupUtil//
                            .addJMenuItem("開啟目錄", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        String url = StringUtils.trimToEmpty(urlText.getText());
                                        DesktopUtil.openDir(url);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                            .addJMenuItem("以參數開啟", new ActionListener() {
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
                                    try {
                                        String url = StringUtils.trimToEmpty(urlText.getText());
                                        File file = DesktopUtil.getFile(url);
                                        if (file == null || !file.exists() || !file.isFile()) {
                                            JCommonUtil._jOptionPane_showMessageDialog_error("無法執行此連結!");
                                            return;
                                        }
                                        UrlConfig d = UrlConfig.parseTo(url, bookmarkConfig.getConfigProp().getProperty(url));
                                        String command = d.remark;
                                        if (command.contains("%s")) {
                                            command = String.format(command, file);
                                        }
                                        RuntimeBatPromptModeUtil.newInstance().command(command).apply();
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            })//
                    ;
                }

                // 多項處理
                if (true) {
                    final JTableUtil jtab = JTableUtil.newInstance(urlTable);
                    final int[] rowArry = jtab.getSelectedRows();

                    popupUtil//
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
                                        for (int row : rowArry) {
                                            UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                            vo.tag = newTag;
                                            bookmarkConfig.getConfigProp().setProperty(vo.url, UrlConfig.getConfigValue(vo));
                                            sb.append("" + vo.title + "\n");
                                            size++;
                                        }
                                        boolean isSave = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString() + "確定修改Tag : " + newTag, "確認");
                                        if (isSave) {
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
                                        for (int row : rowArry) {
                                            UrlConfig vo = (UrlConfig) jtab.getModel().getValueAt(row, UrlTableConfigEnum.VO.ordinal());
                                            vo.remark = newRemark;
                                            bookmarkConfig.getConfigProp().setProperty(vo.url, UrlConfig.getConfigValue(vo));
                                            sb.append("" + vo.title + "\n");
                                            size++;
                                        }
                                        boolean isSave = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(sb.toString() + "確定修改Remark : " + newRemark, "確認");
                                        if (isSave) {
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
                    ;
                }

                popupUtil.applyEvent(e).show();
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
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

        boolean useRobotFocus = JCommonUtil.focusComponent(searchComboBoxUtil.getTextComponent(), true);
        if (useRobotFocus) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        ((JTextField) searchComboBoxUtil.getTextComponent()).selectAll();
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
        public void nativeKeyPressed(NativeKeyEvent paramNativeKeyEvent) {
        }

        private void startNewUI() {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        if (BrowserHistoryHandlerUI.this.isShowing()) {
                            BrowserHistoryHandlerUI.this.setVisible(false);
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

    private static String __doGetRequest_UserAgent(String url, String encoding, String userAgent) {
        try {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            URI uri = new URI(url);

            CookieStore cookieStore = new BasicCookieStore();
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            if (userAgent != null && userAgent.length() > 0) {
                httpget.setHeader("User-Agent", userAgent);
            }

            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                InputStream instream = entity.getContent();
                Writer writer = new StringWriter();
                char[] buffer = new char[1024];
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(instream, encoding));
                    int n;
                    while ((n = reader.read(buffer)) != -1) {
                        writer.write(buffer, 0, n);
                    }
                } finally {
                    instream.close();
                }
                String result = writer.toString();
                return result;
            } else {
                String errMsg = "StatusCode : " + response.getStatusLine().getStatusCode() + ", " + response.getStatusLine().getReasonPhrase();
                throw new RuntimeException(errMsg);
            }
        } catch (Exception ex) {
            throw new RuntimeException("doGetRequest_UserAgent Err : " + ex.getMessage(), ex);
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
}
