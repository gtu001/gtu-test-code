package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolTip;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Row;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.FastDBQueryUI_XlsColumnDefLoader.XlsColumnDefClz;
import gtu._work.ui.FastDBQueryUI_XlsColumnDefLoader.XlsColumnDefType;
import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.binary.Base64JdkUtil;
import gtu.clipboard.ClipboardUtil;
import gtu.collection.ListUtil;
import gtu.db.ExternalJDBCDriverJarLoader;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil.DBDateFormat;
import gtu.db.sqlMaker.DbSqlCreater;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.log.LoggerAppender;
import gtu.number.RandomUtil;
import gtu.poi.hssf.ExcelUtil_Xls97;
import gtu.poi.hssf.ExcelWriter;
import gtu.poi.hssf.ExcelWriter.CellStyleHandler;
import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesGroupUtils_ByKey;
import gtu.properties.PropertiesMultiUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.spring.SimilarityUtil;
import gtu.string.StringNumberUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.AutoComboBox;
import gtu.swing.util.AutoComboBox.MatchType;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JComboBoxUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTabbedPaneUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTableUtil.ColumnSearchFilter;
import gtu.swing.util.JTableUtil.JTooltipTable;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTextFieldUtil;
import gtu.swing.util.JTextFieldUtil.JTextComponentSelectPositionHandler;
import gtu.swing.util.JTextUndoUtil;
import gtu.swing.util.JTooltipUtil;
import gtu.swing.util.KeyEventExecuteHandler;
import gtu.swing.util.KeyEventUtil;
import gtu.swing.util.SimpleTextDlg;
import gtu.swing.util.SwingTabTemplateUI;
import gtu.swing.util.SwingTabTemplateUI.ChangeTabHandlerGtu001;
import gtu.yaml.util.YamlMapUtil;
import gtu.yaml.util.YamlUtilBean;
import net.sf.json.JSONArray;
import net.sf.json.util.JSONUtils;
import taobe.tec.jcc.JChineseConvertor;

public class FastDBQueryUI extends JFrame {

    private static final String QUERY_RESULT_COLUMN_NO = "No.";

    private static final long serialVersionUID = 1L;

    private static File JAR_PATH_FILE = PropertiesUtil.getJarCurrentPath(FastDBQueryUI.class);
    static {
        if (!PropertiesUtil.isClassInJar(FastDBQueryUI.class)) {
            if (OsInfoUtil.isWindows()) {
                JAR_PATH_FILE = new File("D:\\my_tool\\FastDBQueryUI");
            } else {
                JAR_PATH_FILE = new File("/media/gtu001/OLD_D/my_tool/FastDBQueryUI");
            }
        }
    }

    static {
        System.setProperty("db2.jcc.charsetDecoderEncoder", "3");
    }

    private static File sqlIdListFile = new File(JAR_PATH_FILE, "sqlList.properties");
    private static final File sqlIdListDSMappingFile = new File(JAR_PATH_FILE, "sqlList_DS_Mapping.properties");
    private SqlIdConfigBeanHandler sqlIdConfigBeanHandler;
    private SqlIdListDSMappingHandler sqlIdListDSMappingHandler;
    private SqlParameterConfigLoadHandler sqlParameterConfigLoadHandler = new SqlParameterConfigLoadHandler();
    private SqlIdColumnHolder mSqlIdColumnHolder = new SqlIdColumnHolder();
    public LoggerAppender updateLogger = new LoggerAppender(new File(JAR_PATH_FILE, "updateLog_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd") + ".txt"));
    private static final String SQL_PARAM_PTN = "\\:([a-zA-Z]\\w*)";
    private static PropertiesGroupUtils_ByKey dataSourceConfig = new PropertiesGroupUtils_ByKey(new File(JAR_PATH_FILE, "dataSource.properties"));

    private JPanel contentPane;
    private JList sqlList;
    private JButton sqlSaveButton;
    private JTextArea sqlTextArea;
    private JTextField sqlIdText;
    private JButton clearButton;
    private JScrollPane scrollPane_1;
    private JTable parametersTable;
    private JPanel panel_5;
    private JTooltipTable queryResultTable;
    private JPanel panel_6;
    private JTextField dbUrlText;
    private JTextField dbUserText;
    private JTextField dbPwdText;
    private JTextField dbDriverText;
    private JLabel lblUrl;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JButton saveConnectionBtn;
    private JPanel panel_7;

    private JButton nextParameterBtn;
    private JButton nextConnBtn;
    private JComboBox dbNameIdText;
    private AutoComboBox dbNameIdText_Auto;
    private AutoComboBox sqlMappingFilterText_Auto;
    private AutoComboBox refSearchCategoryCombobox_Auto;

    private JLabel lblDbName;
    private JTextField sqlQueryText;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JPanel panel_11;
    private JTextArea queryResultJsonTextArea;
    private JPanel panel_12;
    private JPanel panel_13;
    private JPanel panel_14;
    private JPanel panel_15;
    private JPanel panel_16;
    private JPanel newPanel1;

    private Triple<List<String>, List<Class<?>>, List<Object[]>> queryList = null;
    private Triple<List<String>, List<Class<?>>, List<Object[]>> filterRowsQueryList = null;

    private boolean distinctHasClicked = false;// 是否按過distinct btn

    private JButton excelExportBtn;
    private JRadioButton radio_import_excel;
    private JRadioButton radio_export_excel;

    private ButtonGroup btnExcelBtn;
    private JLabel label;
    private JTextField columnFilterText;

    private boolean isResetQuery = true;// 是否重新查詢
    private JPanel panel_17;
    private JButton removeConnectionBtn;
    private JLabel lblNewLabel_3;
    private JTextField rowFilterText;
    private JButton distinctQueryBtn;
    private JLabel queryResultCountLabel;
    private JButton deleteParameterBtn;
    private JButton saveParameterTableBtn;
    private JButton importYamlConfigBtn;

    private static AtomicReference<ExternalJDBCDriverJarLoader> externalJDBCDriverJarLoader = new AtomicReference<ExternalJDBCDriverJarLoader>();
    private static AtomicReference<JFrameRGBColorPanel> jFrameRGBColorPanel = new AtomicReference<JFrameRGBColorPanel>();
    private static AtomicReference<HideInSystemTrayHelper> hideInSystemTrayHelper = new AtomicReference<HideInSystemTrayHelper>();

    private JButton prevConnBtn;
    private JLabel lblNewLabel_4;
    private JTextField sqlContentFilterText;
    private JLabel lblNewLabel_5;

    private String importExcelSheetName;// 匯入目前的sheet name
    private JButton connTestBtn;
    private JPanel panel_18;
    private JPanel panel_19;
    private JPanel panel_20;
    private JPanel panel_21;
    private JPanel panel_22;
    private JLabel lblNewLabel_6;
    private JTextField refSearchText;
    private JLabel lblNewLabel_7;
    private JTextArea refContentArea;
    private JList refSearchList;
    private RefSearchListConfigHandler refSearchListConfigHandler;
    private JButton refContentConfigSaveBtn;
    private JButton refContentConfigClearBtn;
    private JComboBox refSearchCategoryCombobox;
    private JComboBox refSearchColorComboBox;
    private JTextField refConfigPathText;
    private JLabel lbl_config_etc;
    private JPanel panel_23;
    private JButton saveEtcConfigBtn;
    private EtcConfigHandler etcConfigHandler;
    private JPanel panel_24;

    private static SwingTabTemplateUI TAB_UI1;
    private JLabel lblDb;
    private JComboBox sqlMappingFilterText;
    private JButton sqlFilterClearBtn;
    private JLabel lblNewLabel_8;
    private JLabel lblNewLabel_9;
    private JComboBox sqlIdCategoryComboBox;
    private AutoComboBox sqlIdCategoryComboBox_Auto;
    private JComboBox sqlIdColorComboBox;
    private JLabel lblNewLabel_10;
    private JButton exportYamlConfigBtn;
    private JButton sqlIdFixNameBtn;
    private SqlIdConfigBean sqlBean;// 當前選則
    private JTextField maxRowsText;
    private JButton executeSqlButton;
    private JLabel label_1;
    private JRadioButton updateSqlRadio;
    private JRadioButton querySqlRadio;
    private JButton executeSqlButton2;
    private JButton refConfigPathYamlExportBtn;
    private JTabbedPane tabbedPane;
    private JTextArea sqlParamCommentArea;
    private JTextArea sqlIdCommentArea;
    private EditColumnHistoryHandler editColumnHistoryHandler;
    private JLabel lblNewLabel_11;
    private JLabel lblNewLabel_12;
    private JLabel queryResultTimeLbl;
    private JLabel lblNewLabel_13;
    private JTextComponentSelectPositionHandler mSqlTextAreaJTextAreaSelectPositionHandler;
    private SqlTextAreaPromptHandler mSqlTextAreaPromptHandler;
    private JComboBox tableColumnDefText;
    private AutoComboBox tableColumnDefText_Auto;
    private JButton tableColumnConfigBtn;
    private JLabel lblNewLabel_14;

    private SearchAndReplace mSearchAndReplace = new SearchAndReplace();
    private JPanel panel_25;
    private static final String ICO_FILENAME = "big_boobs.ico";// "big_boobs.ico";//"Pig_SC.ico"
    private JButton setFontSizeBtn;
    private JComboBox sqlPageDbConnCombox;
    private JPanel panel_26;
    private JLabel lblNewLabel_15;
    private JLabel lblNewLabel_16;
    private JTextField compareBeforeXlsText;
    private JTextField compareAfterXlsText;
    private JPanel panel_27;
    private JButton compareXlsExecuteBtn;
    private JButton compareXlsClearBtn;
    private JLabel lblNewLabel_17;
    private JTextField compareXlsMiddleNameText;
    protected TableColumnDefTextHandler mTableColumnDefTextHandler;
    private JPanel panel_28;
    private JPanel panel_29;
    private JPanel panel_30;
    private JPanel panel_31;
    private JPanel panel_32;
    private JButton tableColumnConfigBtn2;
    private JTextField columnXlsDefTableQryText;
    private JLabel lblNewLabel_18;
    private JTable columnXlsDefTableColumnQryTable;
    private JButton clearParameterBtn;
    private AtomicReference<FastDBQueryUI_RecordWatcher> mRecordWatcher = new AtomicReference<FastDBQueryUI_RecordWatcher>();
    private JToggleButton recordWatcherToggleBtn;
    private JCheckBox rowFilterTextKeepMatchChk;
    private JButton resetQueryBtn;
    private JDlgHolderBringToFrontHandler mJDlgHolderBringToFrontHandler;
    private XlsColumnDefDlg mXlsColumnDefDlg;
    private JTextField columnXlsDefColumnQryText;
    private JTextField columnXlsDefOtherQryText;
    private JLabel label_2;
    private JLabel lblNewLabel_19;
    private JLabel columnXlsDefFindRowCountLbl;

    private final Predicate IGNORE_PREDICT = new Predicate() {
        @Override
        public boolean evaluate(Object input) {
            Class<?>[] igs = new Class[] { JTextField.class, JTextArea.class };
            for (Class<?> c : igs) {
                if (input.getClass() == c) {
                    return true;
                }
            }
            return false;
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        System.out.println("start...");
        /*
         * EventQueue.invokeLater(new Runnable() { public void run() { try {
         * FastDBQueryUI frame = new FastDBQueryUI();
         * gtu.swing.util.JFrameUtil.setVisible(true, frame); } catch (Exception
         * e) { e.printStackTrace(); } } });
         */
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            System.out.println("=====" + info.getClassName());
            // javax.swing.UIManager.setLookAndFeel(info.getClassName());
        }
        SwingTabTemplateUI tabUI = SwingTabTemplateUI.newInstance(null, ICO_FILENAME, FastDBQueryUI.class, true, new SwingTabTemplateUI.SwingTabTemplateUI_Callback() {
            @Override
            public void beforeInit(SwingTabTemplateUI self) {
                if (jFrameRGBColorPanel.get() == null) {
                    jFrameRGBColorPanel.set(new JFrameRGBColorPanel(self.getJframe()));
                }
                if (hideInSystemTrayHelper.get() == null) {
                    hideInSystemTrayHelper.set(HideInSystemTrayHelper.newInstance());
                    hideInSystemTrayHelper.get().apply(self.getJframe());
                }
            }

            @Override
            public void afterInit(SwingTabTemplateUI self) {
                loadExternalJars();
            }
        });
        tabUI.setEventAfterChangeTab(new ChangeTabHandlerGtu001() {
            public void afterChangeTab(int tabIndex, List<JFrame> jframeKeeperLst) {
                if (jframeKeeperLst != null && !jframeKeeperLst.isEmpty()) {
                    ((FastDBQueryUI) jframeKeeperLst.get(tabIndex)).reloadAllProperties();
                    ((FastDBQueryUI) jframeKeeperLst.get(tabIndex)).moveTabToQueryResultIfHasRecords();
                }
            }
        });

        java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        tabUI.setSize((int) (scr_size.width * 0.8), (int) (scr_size.height * 0.8));
        tabUI.startUI();
        tabUI.getSysTrayUtil().createDefaultTray();
        TAB_UI1 = tabUI;
    }

    /**
     * Create the frame.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     */
    public FastDBQueryUI() throws FileNotFoundException, IOException, ClassNotFoundException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, (int) (scr_size.width * 0.8), (int) (scr_size.height * 0.8));
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Sql列表", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);
        sqlList = new JList() {
            @Override
            public JToolTip createToolTip() {
                return JTooltipUtil.createToolTip(null, null);
            }
        };
        sqlList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sqlListMouseClicked(e);
            }
        });
        sqlList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                sqlListKeyPressAction(e);
            }
        });
        JListUtil.newInstance(sqlList).applyOnHoverEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlIdConfigBean sqlBean = (SqlIdConfigBean) e.getSource();
                sqlList.setToolTipText(StringUtils.trimToNull(sqlBean.sqlComment));
            }
        });
        JListUtil.newInstance(sqlList).applyOnHoverEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlIdConfigBean bean = (SqlIdConfigBean) e.getSource();
                String comment = "<font color=\"red\">" + bean.sqlComment + "</font><br/>";
                String tooltip = "<html>" + comment + JTooltipUtil.escapeHtml(bean.sql) + "</html>";
                sqlList.setToolTipText(tooltip);
            }
        });

        scrollPane.setViewportView(sqlList);

        newPanel1 = new JPanel();
        sqlQueryText = new JTextField();
        sqlQueryText.setToolTipText("SQL ID標籤過濾");
        sqlQueryText.setColumns(15);

        lblNewLabel_4 = new JLabel("SQL ID過濾");
        newPanel1.add(lblNewLabel_4);
        newPanel1.add(sqlQueryText);

        panel.add(newPanel1, BorderLayout.NORTH);

        lblNewLabel_5 = new JLabel("SQL與欄位過濾");
        newPanel1.add(lblNewLabel_5);

        sqlContentFilterText = new JTextField();
        sqlContentFilterText.setColumns(15);
        sqlContentFilterText.setToolTipText("SQL內所包含文字以及所含欄位過濾");

        newPanel1.add(sqlContentFilterText);

        lblDb = new JLabel("DB名稱過濾");
        newPanel1.add(lblDb);

        sqlMappingFilterText = new JComboBox();
        sqlMappingFilterText.setToolTipText("SQL ID標籤過濾");
        // dbNameIdText.setColumns(10);
        sqlMappingFilterText_Auto = AutoComboBox.applyAutoComboBox(sqlMappingFilterText);
        sqlMappingFilterText_Auto.setMatchType(MatchType.Contains);

        newPanel1.add(sqlMappingFilterText);

        for (JTextComponent text : new JTextComponent[] { sqlQueryText, sqlContentFilterText, sqlMappingFilterText_Auto.getTextComponent() }) {
            text.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    try {
                        // 初始化 sqlList
                        initLoadSqlListConfig();
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            });
            text.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                @Override
                public void process(DocumentEvent event) {
                    try {
                        // 初始化 sqlList
                        initLoadSqlListConfig();
                    } catch (Exception e) {
                        JCommonUtil.handleException(e);
                    }
                }
            }));
        }

        sqlFilterClearBtn = new JButton("清除");
        sqlFilterClearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e1) {
                sqlQueryText.setText("");
                sqlContentFilterText.setText("");
                sqlMappingFilterText_Auto.getTextComponent().setText("");
                try {
                    // 初始化 sqlList
                    initLoadSqlListConfig();
                } catch (Exception e) {
                    JCommonUtil.handleException(e);
                }
            }
        });

        newPanel1.add(sqlFilterClearBtn);
        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("SQL", null, panel_2, null);
        panel_2.setLayout(new BorderLayout(0, 0));

        sqlTextArea = new JTextArea();
        sqlTextArea.setFocusTraversalKeysEnabled(false);

        // JTextAreaUtil.applyCommonSetting(sqlTextArea, false);
        {
            if (true) {
                JTextAreaUtil.applyFont(sqlTextArea);
            }
            JTextUndoUtil.applyUndoProcess2(sqlTextArea);
        }

        sqlTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sqlTextAreaMouseClickedAction(e);
            }
        });
        sqlTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean isConsume = false;
                if (KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_S) {
                    JCommonUtil.triggerButtonActionPerformed(sqlSaveButton);
                } else if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER && mSqlTextAreaPromptHandler != null) {
                    isConsume = mSqlTextAreaPromptHandler.performSelectTopColumn(e);
                    if (!isConsume) {
                        JTextAreaUtil.triggerTabKey(sqlTextArea, e);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && mSqlTextAreaPromptHandler != null) {
                    isConsume = mSqlTextAreaPromptHandler.performSelectClose();
                } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN && mSqlTextAreaPromptHandler != null) {
                    isConsume = mSqlTextAreaPromptHandler.performSelectUpDown(e);
                } else if (KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_F && mSearchAndReplace != null) {
                    isConsume = mSearchAndReplace.findKey();
                } else if (KeyEventUtil.isMaskKeyPress(e, "s") && e.getKeyCode() == KeyEvent.VK_F3 && mSearchAndReplace != null) {
                    isConsume = mSearchAndReplace.findNext(false);
                } else if (e.getKeyCode() == KeyEvent.VK_F3 && mSearchAndReplace != null) {
                    isConsume = mSearchAndReplace.findNext(true);
                } else if (KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_H && mSearchAndReplace != null) {
                    isConsume = mSearchAndReplace.replaceAll();
                } else if (!isConsume && mSqlTextAreaPromptHandler != null) {
                    mSqlTextAreaPromptHandler.performUpdateLocation();
                    isConsume = mSqlTextAreaPromptHandler.checkPopupListFocus(e);
                }
                if (isConsume) {
                    e.consume();
                    System.out.println("-----Consume");
                }
            }
        });
        sqlTextArea.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                sqlTextAreaChange();
                sqlTextAreaPromptProcess("insertUpdate", event);
            }
        }));

        mSqlTextAreaJTextAreaSelectPositionHandler = JTextComponentSelectPositionHandler.newInst(sqlTextArea);

        panel_2.add(JCommonUtil.createScrollComponent(sqlTextArea));

        JPanel sqlIdPanel = new JPanel();
        lblNewLabel_10 = new JLabel("顏色");
        sqlIdPanel.add(lblNewLabel_10);

        sqlIdColorComboBox = new JComboBox();
        sqlIdColorComboBox.setModel(RefSearchColor.getModel());
        sqlIdPanel.add(sqlIdColorComboBox);
        sqlIdColorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sqlTextAreaChange();
            }
        });

        lblNewLabel_9 = new JLabel("類別");
        sqlIdPanel.add(lblNewLabel_9);

        sqlIdCategoryComboBox = new JComboBox();
        sqlIdCategoryComboBox_Auto = AutoComboBox.applyAutoComboBox(sqlIdCategoryComboBox);
        sqlIdPanel.add(sqlIdCategoryComboBox);
        sqlIdCategoryComboBox_Auto.getTextComponent().getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                sqlTextAreaChange();
            }
        }));

        lblNewLabel_8 = new JLabel("SQL ID");
        sqlIdPanel.add(lblNewLabel_8);
        sqlIdText = new JTextField();
        sqlIdPanel.add(sqlIdText);
        sqlIdText.setToolTipText("設定SQL ID");
        sqlIdText.setColumns(30);
        sqlIdText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                sqlTextAreaChange();
            }
        }));

        {// 多包一層
            JPanel innerPanel1 = new JPanel();
            innerPanel1.setLayout(new BorderLayout(0, 0));
            innerPanel1.add(sqlIdPanel, BorderLayout.NORTH);

            {// 多包一層
                JPanel innerPanel11 = new JPanel();

                sqlIdCommentArea = new JTextArea();
                sqlIdCommentArea.setToolTipText("SQL註解");
                JTextAreaUtil.applyCommonSetting(sqlIdCommentArea, false);
                sqlIdCommentArea.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && //
                        e.getKeyCode() == KeyEvent.VK_S) {
                            JCommonUtil.triggerButtonActionPerformed(sqlSaveButton);
                        }
                    }
                });
                sqlIdCommentArea.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                    @Override
                    public void process(DocumentEvent event) {
                        sqlTextAreaChange();
                    }
                }));

                innerPanel1.add(innerPanel11, BorderLayout.CENTER);
                innerPanel11.setLayout(new FormLayout(
                        new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
                                FormFactory.RELATED_GAP_COLSPEC, },
                        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, }));

                lblNewLabel_11 = new JLabel("註解");
                innerPanel11.add(lblNewLabel_11, "2, 2");

                innerPanel11.add(sqlIdCommentArea, "4, 2, fill, fill");
            }

            panel_2.add(innerPanel1, BorderLayout.NORTH);
        }

        sqlIdFixNameBtn = new JButton("選擇功能");
        sqlIdFixNameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPopupMenuUtil.newInstance(sqlIdFixNameBtn)//
                        .addJMenuItem("改名", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sqlIdFixNameBtnAction("rename");
                            }
                        })//
                        .addJMenuItem("複製", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sqlIdFixNameBtnAction("clone");
                            }
                        })//
                        .addJMenuItem("刪除", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                deleteSqlIdConfigBean(getCurrentEditSqlIdConfigBean());
                            }
                        })//
                        .applyEvent(e)//
                        .show();
            }
        });

        sqlPageDbConnCombox = new JComboBox();
        sqlPageDbConnCombox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dbNameIdText_Auto.getTextComponent().setText(String.valueOf(sqlPageDbConnCombox.getSelectedItem()));
                JCommonUtil.triggerButtonActionPerformed(dbNameIdText);
            }
        });

        sqlIdPanel.add(sqlPageDbConnCombox);
        sqlIdPanel.add(sqlIdFixNameBtn);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new BorderLayout(0, 0));
        panel_2.add(panel_3, BorderLayout.SOUTH);

        {
            panel_25 = new JPanel();
            panel_3.add(panel_25, BorderLayout.SOUTH);
            panel_3.add(panel_25);

            label_1 = new JLabel("max rows :");
            panel_25.add(label_1);

            maxRowsText = new JTextField();
            maxRowsText.setToolTipText("設定最大筆數,小於等於0則無限制");
            maxRowsText.setText("1000");
            maxRowsText.setColumns(5);
            panel_25.add(maxRowsText);

            sqlSaveButton = new JButton("儲存");
            sqlSaveButton.setToolTipText("快速鍵 Ctrl+S");
            panel_25.add(sqlSaveButton);

            clearButton = new JButton("清除");
            clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearButtonClick();
                }
            });
            panel_25.add(clearButton);

            querySqlRadio = new JRadioButton("查詢模式");
            querySqlRadio.setSelected(true);
            panel_25.add(querySqlRadio);

            updateSqlRadio = new JRadioButton("修改模式");
            JButtonGroupUtil.createRadioButtonGroup(querySqlRadio, updateSqlRadio);

            panel_25.add(updateSqlRadio);

            executeSqlButton = new JButton("執行Sql");
            executeSqlButton.setToolTipText("快速鍵 F5");
            executeSqlButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    executeSqlButtonClick();
                }
            });
            panel_25.add(executeSqlButton);
        }

        sqlSaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSqlButtonClick(true);
            }
        });

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("SQL參數", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        scrollPane_1 = new JScrollPane();

        JPanel innerPanel2 = new JPanel();
        innerPanel2.setLayout(new BorderLayout(0, 0));
        innerPanel2.add(scrollPane_1, BorderLayout.CENTER);

        {// 多包一層
            JPanel innerPanel11 = new JPanel();

            sqlParamCommentArea = new JTextArea();
            sqlParamCommentArea.setToolTipText("SQL參數註解");
            JTextAreaUtil.applyCommonSetting(sqlParamCommentArea, false);

            innerPanel11.setLayout(new FormLayout(
                    new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
                            FormFactory.RELATED_GAP_COLSPEC, },
                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, }));

            lblNewLabel_12 = new JLabel("註解");
            innerPanel11.add(lblNewLabel_12, "2, 2");
            innerPanel11.add(sqlParamCommentArea, "4, 2, fill, fill");

            innerPanel2.add(innerPanel11, BorderLayout.SOUTH);

            lblNewLabel_13 = new JLabel("選填項目用中括號\"[]\"表示, 參數用 :paramKey 表示, 注入式SQL用 _#sqlKey#_ 表示");
            lblNewLabel_13.setForeground(Color.RED);
            innerPanel11.add(lblNewLabel_13, "4, 3");
        }

        panel_1.add(innerPanel2, BorderLayout.CENTER);

        parametersTable = new JTable();
        parametersTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = parametersTable.columnAtPoint(e.getPoint());
                final String name = parametersTable.getColumnName(col);
                System.out.println("Column index selected " + col + " " + name);

                if (JMouseEventUtil.buttonRightClick(1, e)) {
                    JPopupMenuUtil.newInstance(parametersTable)//
                            .addJMenuItem("複製欄位", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    List<String> lst = new ArrayList<String>();
                                    // 取選擇
                                    int[] rows = parametersTable.getSelectedRows();
                                    if (rows != null && rows.length > 0) {
                                        for (int ii = 0; ii < rows.length; ii++) {
                                            int row = JTableUtil.getRealRowPos(rows[ii], parametersTable);
                                            int col = JTableUtil.getRealColumnPos(0, parametersTable);
                                            lst.add((String) parametersTable.getValueAt(row, col));
                                        }
                                    }
                                    // 取全部
                                    if (lst.isEmpty()) {
                                        for (int row = 0; row < parametersTable.getRowCount(); row++) {
                                            int col = JTableUtil.getRealColumnPos(0, parametersTable);
                                            lst.add((String) parametersTable.getValueAt(row, col));
                                        }
                                    }
                                    ClipboardUtil.getInstance().setContents(StringUtils.join(lst, "^"));
                                }
                            }).applyEvent(e)//
                            .show();
                }
            }
        });

        parametersTable.addMouseListener(new MouseAdapter() {

            Pattern ptn = Pattern.compile("[\\w\\-\\:\\/]+\\s\\d{2}\\:\\d{2}\\:\\d{2}|[\\w\\-\\:\\/]+|\\w+");

            private void updateColumnParameter(List<String> params) {
                DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
                A: for (int ii = 0; ii < model.getRowCount(); ii++) {
                    String column = (String) model.getValueAt(ii, ParameterTableColumnDef.COLUMN.idx);
                    int pos = ListUtil.indexOfIgnorecase(column, params);
                    if (pos != -1) {
                        for (int jj = 0; jj < params.size(); jj++) {
                            if (jj > pos) {
                                if (params.get(jj) != null && !"null".equals(params.get(jj))) {
                                    model.setValueAt(params.get(jj), ii, ParameterTableColumnDef.VALUE.idx);
                                    break A;
                                }
                            }
                        }
                    }
                }
            }

            private void updateColumnParameters() {
                BufferedReader reader = null;
                Matcher mth = null;
                try {
                    String content = ClipboardUtil.getInstance().getContents();
                    reader = new BufferedReader(new StringReader(content));
                    for (String line = null; (line = reader.readLine()) != null;) {
                        List<String> lst = new ArrayList<String>();
                        mth = ptn.matcher(line);
                        while (mth.find()) {
                            String word = StringUtils.trimToEmpty(mth.group());
                            if (StringUtils.isNotBlank(word)) {
                                lst.add(word);
                            }
                        }
                        updateColumnParameter(lst);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (JMouseEventUtil.buttonRightClick(1, e)) {
                    JPopupMenuUtil.newInstance(parametersTable)//
                            .addJMenuItem("從剪貼簿貼上", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    updateColumnParameters();
                                }
                            }).applyEvent(e)//
                            .show();
                }
            }
        });

        JTableUtil.newInstance(parametersTable).applyOnHoverEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pair<Integer, Integer> pair = (Pair<Integer, Integer>) e.getSource();
                int row = pair.getLeft();
                int col = pair.getRight();
                String column = (String) parametersTable.getValueAt(row, ParameterTableColumnDef.COLUMN.idx);
                SqlParam bean = parseSqlToParam(sqlBean.sql);
                if (bean instanceof SqlParam_IfExists) {
                    SqlParam_IfExists bean2 = (SqlParam_IfExists) bean;
                    if (bean2.paramSetSentanceMap.containsKey(column)) {
                        String sentance = bean2.paramSetSentanceMap.get(column);
                        parametersTable.setToolTipText("<html>" + JTooltipUtil.escapeHtml(sentance) + "</html>");
                        return;
                    }
                }
                parametersTable.setToolTipText(null);
            }
        });

        scrollPane_1.setViewportView(parametersTable);

        JPanel panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.SOUTH);

        panel_5 = new JPanel();
        tabbedPane.addTab("查詢結果", null, panel_5, null);
        panel_5.setLayout(new BorderLayout(0, 0));

        queryResultTable = new JTooltipTable();
        queryResultTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode() + "..." + KeyEvent.VK_ENTER);
                if (e.getKeyCode() == KeyEvent.VK_ENTER && queryResultTable.getSelectedRowCount() > 0) {
                    Component source = queryResultTable;
                    int id = KeyEvent.KEY_PRESSED;
                    long when = System.currentTimeMillis();
                    int modifiers = 0;
                    int x = 0;
                    int y = 0;
                    int clickCount = 2;
                    boolean popupTrigger = false;
                    int button = MouseEvent.BUTTON1;
                    MouseEvent e2 = new MouseEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger, button);
                    queryResultTableMouseClickAction(e2);
                }
            }
        });
        queryResultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                queryResultTableMouseClickAction(e);
            }
        });
        queryResultTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = queryResultTable.columnAtPoint(e.getPoint());
                final String name = queryResultTable.getColumnName(col);
                System.out.println("Column index selected " + col + " " + name);

                if (JMouseEventUtil.buttonRightClick(1, e)) {
                    JPopupMenuUtil popUtil = JPopupMenuUtil.newInstance(queryResultTable);//
                    popUtil.addJMenuItem("複製 : " + name, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ClipboardUtil.getInstance().setContents(name);
                        }
                    }).addJMenuItem("複製全部(逗號)", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JTableUtil tabUtil = JTableUtil.newInstance(queryResultTable);
                            List<Object> lst = tabUtil.getColumnTitleArray();
                            ClipboardUtil.getInstance().setContents(StringUtils.join(lst, " , "));
                        }
                    }).addJMenuItem("複製全部(多行)", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JTableUtil tabUtil = JTableUtil.newInstance(queryResultTable);
                            List<Object> lst = tabUtil.getColumnTitleArray();
                            ClipboardUtil.getInstance().setContents(StringUtils.join(lst, "\r\n"));
                        }
                    }).addJMenuItem("Sql Column IN (...)", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            doSetColumnSqlInProcess(name, false);
                        }
                    }).addJMenuItem("Sql Column IN (...) [distinct]", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            doSetColumnSqlInProcess(name, true);
                        }
                    });
                    if (mTableColumnDefTextHandler != null) {
                        List<String> pkLst = mTableColumnDefTextHandler.getPkLst();
                        if (CollectionUtils.isNotEmpty(pkLst)) {
                            popUtil.addJMenuItem("參考excel設定PK", new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String pkMsg = StringUtils.join(mTableColumnDefTextHandler.getPkLst(), "\r\n");
                                    new SimpleTextDlg(pkMsg, "", null).show();
                                }
                            });
                        }
                    }
                    popUtil.applyEvent(e).show();
                }
            }
        });

        panel_12 = new JPanel();

        panel_5.add(panel_12, BorderLayout.CENTER);
        panel_12.setLayout(new BorderLayout(0, 0));

        panel_13 = new JPanel();
        panel_12.add(panel_13, BorderLayout.NORTH);

        distinctQueryBtn = new JButton("Distinct");
        distinctQueryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                distinctQueryBtnAction();
            }
        });

        lblNewLabel_14 = new JLabel();
        panel_13.add(lblNewLabel_14);

        tableColumnDefText = new JComboBox();
        tableColumnDefText_Auto = AutoComboBox.applyAutoComboBox(tableColumnDefText);
        panel_13.add(tableColumnDefText);

        tableColumnConfigBtn = new JButton("設定");
        panel_13.add(tableColumnConfigBtn);
        tableColumnConfigBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableColumnConfigBtnAction();
            }
        });

        mTableColumnDefTextHandler = new TableColumnDefTextHandler();
        tableColumnDefText_Auto.getTextComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                mTableColumnDefTextHandler.action();
            }
        });

        tableColumnDefText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mTableColumnDefTextHandler.action();
            }
        });

        queryResultCountLabel = new JLabel("          ");
        panel_13.add(queryResultCountLabel);
        panel_13.add(distinctQueryBtn);

        label = new JLabel("欄位過濾");
        panel_13.add(label);

        columnFilterText = new JTextField();
        columnFilterText.setToolTipText("多欄位用\"^\"分隔");
        panel_13.add(columnFilterText);
        columnFilterText.setColumns(20);
        columnFilterText.setToolTipText("分隔符號為\"^\"");

        columnFilterText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            ColumnSearchFilter columnFilter;

            @Override
            public void process(DocumentEvent event) {
                try {
                    if (checkIsNeedResetQueryResultTable(true)) {
                        return;
                    }
                    if (distinctHasClicked) {
                        queryModeProcess(queryList, true, null, null);
                        distinctHasClicked = false;
                    }
                    if (columnFilter == null || isResetQuery) {
                        columnFilter = new ColumnSearchFilter(queryResultTable, "^", new Object[] { QUERY_RESULT_COLUMN_NO });
                        isResetQuery = false;
                    }
                    columnFilter.filterText(columnFilterText.getText());
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        lblNewLabel_3 = new JLabel("資料過濾");
        panel_13.add(lblNewLabel_3);

        rowFilterText = new JTextField();
        rowFilterText.setToolTipText("多值用\"^\"分隔, 請按Enter執行");
        panel_13.add(rowFilterText);
        rowFilterText.setColumns(20);

        rowFilterTextKeepMatchChk = new JCheckBox("只保留符合");
        panel_13.add(rowFilterTextKeepMatchChk);

        resetQueryBtn = new JButton("重設");
        resetQueryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                columnFilterText.setText("");
                rowFilterText.setText("");
                rowFilterTextKeepMatchChk.setSelected(false);
                checkIsNeedResetQueryResultTable(true);
                {
                    filterRowsQueryList = null;
                    isResetQuery = true;
                    queryModeProcess(queryList, true, null, null);//
                }
            }
        });
        panel_13.add(resetQueryBtn);

        rowFilterText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                rowFilterTextDoFilter.run();
            }
        });
        rowFilterText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowFilterTextDoFilter.run();
            }
        });
        rowFilterTextKeepMatchChk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rowFilterTextDoFilter.run();
            }
        });

        for (final JTextField f : new JTextField[] { rowFilterText, columnFilterText }) {
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        JPopupMenuUtil util = JPopupMenuUtil.newInstance(f).addJMenuItem("空白換成\"^\"", new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String[] texts = StringUtils.split(f.getText(), " ");
                                List<String> arry = new ArrayList<String>();
                                for (String x : texts) {
                                    x = StringUtils.trimToEmpty(x);
                                    if (StringUtils.isNotBlank(x)) {
                                        arry.add(x);
                                    }
                                }
                                f.setText(StringUtils.join(arry, "^"));
                            }
                        });
                        util.addJMenuItem(new S2T_And_T2S_EventHandler(f).getMenuItem(false));
                        util.addJMenuItem(new S2T_And_T2S_EventHandler(f).getMenuItem(true));
                        util.applyEvent(e).show();
                    }
                }
            });
        }

        panel_14 = new JPanel();
        panel_12.add(panel_14, BorderLayout.WEST);

        panel_15 = new JPanel();
        panel_12.add(panel_15, BorderLayout.SOUTH);

        queryResultTimeLbl = new JLabel("        ");
        panel_15.add(queryResultTimeLbl);

        radio_import_excel = new JRadioButton("匯入excel");
        panel_15.add(radio_import_excel);

        radio_export_excel = new JRadioButton("匯出excel");
        panel_15.add(radio_export_excel);

        btnExcelBtn = JButtonGroupUtil.createRadioButtonGroup(radio_import_excel, radio_export_excel);

        excelExportBtn = new JButton("動作");
        excelExportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excelExportBtnAction();
            }
        });
        panel_15.add(excelExportBtn);

        recordWatcherToggleBtn = new JToggleButton("監聽");
        recordWatcherToggleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startRecordWatcher(recordWatcherToggleBtn.isSelected());
            }
        });
        panel_15.add(recordWatcherToggleBtn);

        panel_16 = new JPanel();
        panel_12.add(panel_16, BorderLayout.EAST);

        panel_12.add(JTableUtil.getScrollPane(queryResultTable), BorderLayout.CENTER);

        panel_7 = new JPanel();
        tabbedPane.addTab("查詢結果JSON", null, panel_7, null);
        panel_7.setLayout(new BorderLayout(0, 0));

        panel_8 = new JPanel();
        panel_7.add(panel_8, BorderLayout.NORTH);

        panel_9 = new JPanel();
        panel_7.add(panel_9, BorderLayout.WEST);

        panel_10 = new JPanel();
        panel_7.add(panel_10, BorderLayout.SOUTH);

        panel_11 = new JPanel();
        panel_7.add(panel_11, BorderLayout.EAST);

        queryResultJsonTextArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(queryResultJsonTextArea, false);
        panel_7.add(JCommonUtil.createScrollComponent(queryResultJsonTextArea), BorderLayout.CENTER);

        panel_18 = new JPanel();
        tabbedPane.addTab("參考備註", null, panel_18, null);
        panel_18.setLayout(new BorderLayout(0, 0));

        panel_19 = new JPanel();
        panel_18.add(panel_19, BorderLayout.NORTH);

        refSearchCategoryCombobox = new JComboBox();
        refSearchCategoryCombobox_Auto = AutoComboBox.applyAutoComboBox(refSearchCategoryCombobox);
        refSearchCategoryCombobox_Auto.setMatchType(MatchType.Contains);
        refSearchCategoryCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String category = refSearchCategoryCombobox_Auto.getTextComponent().getText();
                    String text = refSearchText.getText();
                    if (refSearchListConfigHandler != null) {
                        refSearchListConfigHandler.find(category, text);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        refSearchColorComboBox = new JComboBox();
        refSearchColorComboBox.setModel(RefSearchColor.getModel());
        panel_19.add(refSearchColorComboBox);
        panel_19.add(refSearchCategoryCombobox);

        lblNewLabel_6 = new JLabel("搜尋條件");
        panel_19.add(lblNewLabel_6);

        refSearchText = new JTextField();
        refSearchText.setToolTipText("儲存時為key");
        refSearchText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    String category = refSearchCategoryCombobox_Auto.getTextComponent().getText();
                    String text = refSearchText.getText();
                    if (refSearchListConfigHandler != null) {
                        refSearchListConfigHandler.find(category, text);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        panel_19.add(refSearchText);
        refSearchText.setColumns(15);

        lblNewLabel_7 = new JLabel("內文");
        panel_19.add(lblNewLabel_7);

        refContentArea = new JTextArea();
        refContentArea.setToolTipText("參考備註");
        JTextAreaUtil.applyCommonSetting(refContentArea, false);
        refContentArea.setRows(3);
        refContentArea.setColumns(25);
        panel_19.add(JCommonUtil.createScrollComponent(refContentArea));

        refContentConfigSaveBtn = new JButton("儲存");
        refContentConfigSaveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String category = refSearchCategoryCombobox_Auto.getTextComponent().getText();
                    RefSearchColor categoryColor = (RefSearchColor) refSearchColorComboBox.getSelectedItem();
                    refSearchListConfigHandler.add(category, refSearchText.getText(), refContentArea.getText(), categoryColor.colorCode);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_19.add(refContentConfigSaveBtn);

        refContentConfigClearBtn = new JButton("清除");
        refContentConfigClearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    refSearchCategoryCombobox_Auto.getTextComponent().setText("");
                    refSearchText.setText("");
                    refContentArea.setText("");
                    refSearchListConfigHandler.find("", "");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_19.add(refContentConfigClearBtn);

        panel_20 = new JPanel();
        panel_18.add(panel_20, BorderLayout.WEST);

        panel_21 = new JPanel();
        panel_18.add(panel_21, BorderLayout.EAST);

        panel_22 = new JPanel();
        panel_18.add(panel_22, BorderLayout.SOUTH);

        lbl_config_etc = new JLabel("參考備註設定擋路徑");
        panel_22.add(lbl_config_etc);

        refConfigPathText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(refConfigPathText, true);
        panel_22.add(refConfigPathText);
        refConfigPathText.setColumns(30);

        refSearchList = new JList();
        refSearchList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    RefSearchListConfigBean bean = (RefSearchListConfigBean) refSearchList.getSelectedValue();
                    if (bean == null) {
                        refSearchList.setToolTipText(null);
                        return;
                    }
                    if (JMouseEventUtil.buttonLeftClick(1, e)) {
                        refSearchText.setText(bean.searchKey);
                        refContentArea.setText(bean.content);
                        refSearchCategoryCombobox_Auto.getTextComponent().setText(bean.category);
                        refSearchColorComboBox.setSelectedItem(RefSearchColor.valueFrom(bean.categoryColor));
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        refSearchList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    JListUtil.newInstance(refSearchList).defaultJListKeyPressed(e, false);
                    if (e.getKeyCode() == 127) {
                        RefSearchListConfigBean bean = (RefSearchListConfigBean) refSearchList.getSelectedValue();
                        if (bean == null) {
                            return;
                        }
                        refSearchListConfigHandler.delete(bean.category, bean.searchKey);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        JListUtil.newInstance(refSearchList).applyOnHoverEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RefSearchListConfigBean bean = (RefSearchListConfigBean) e.getSource();
                refSearchList.setToolTipText(StringUtils.trimToNull(bean.content));
            }
        });

        panel_18.add(JCommonUtil.createScrollComponent(refSearchList), BorderLayout.CENTER);

        panel_26 = new JPanel();
        tabbedPane.addTab("匯出檔比對", null, panel_26, null);
        panel_26.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        lblNewLabel_15 = new JLabel("初始匯出檔xls");
        panel_26.add(lblNewLabel_15, "2, 2, right, default");

        compareBeforeXlsText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(compareBeforeXlsText, false);
        panel_26.add(compareBeforeXlsText, "4, 2, fill, default");
        compareBeforeXlsText.setColumns(10);

        lblNewLabel_16 = new JLabel("結果匯出檔xls");
        panel_26.add(lblNewLabel_16, "2, 4, right, default");

        compareAfterXlsText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(compareAfterXlsText, false);
        panel_26.add(compareAfterXlsText, "4, 4, fill, default");
        compareAfterXlsText.setColumns(10);

        lblNewLabel_17 = new JLabel("產出檔中間名");
        panel_26.add(lblNewLabel_17, "2, 6, right, default");

        compareXlsMiddleNameText = new JTextField();
        panel_26.add(compareXlsMiddleNameText, "4, 6, fill, default");
        compareXlsMiddleNameText.setColumns(10);

        panel_27 = new JPanel();
        panel_26.add(panel_27, "4, 8, fill, fill");

        compareXlsExecuteBtn = new JButton("比對");
        compareXlsExecuteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compareXlsExecuteBtnAction();
            }
        });
        panel_27.add(compareXlsExecuteBtn);

        compareXlsClearBtn = new JButton("清除");
        compareXlsClearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                compareBeforeXlsText.setText("");
                compareAfterXlsText.setText("");
                compareXlsMiddleNameText.setText("");
            }
        });
        panel_27.add(compareXlsClearBtn);

        panel_28 = new JPanel();
        tabbedPane.addTab("Excel欄位定義", null, panel_28, null);
        panel_28.setLayout(new BorderLayout(0, 0));

        panel_29 = new JPanel();
        panel_28.add(panel_29, BorderLayout.NORTH);

        tableColumnConfigBtn2 = new JButton("設定");
        tableColumnConfigBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableColumnConfigBtnAction();
            }
        });

        columnXlsDefFindRowCountLbl = new JLabel("");
        panel_29.add(columnXlsDefFindRowCountLbl);
        panel_29.add(tableColumnConfigBtn2);

        lblNewLabel_18 = new JLabel("表");
        panel_29.add(lblNewLabel_18);

        columnXlsDefTableQryText = new JTextField();
        panel_29.add(columnXlsDefTableQryText);
        columnXlsDefTableQryText.setColumns(20);
        columnXlsDefTableQryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                initColumnXlsDefTableColumnQryTable();
            }
        }));

        label_2 = new JLabel("欄位");
        panel_29.add(label_2);

        columnXlsDefColumnQryText = new JTextField();
        columnXlsDefColumnQryText.setColumns(20);
        panel_29.add(columnXlsDefColumnQryText);
        columnXlsDefColumnQryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                initColumnXlsDefTableColumnQryTable();
            }
        }));

        lblNewLabel_19 = new JLabel("其他");
        panel_29.add(lblNewLabel_19);

        columnXlsDefOtherQryText = new JTextField();
        columnXlsDefOtherQryText.setColumns(20);
        panel_29.add(columnXlsDefOtherQryText);
        columnXlsDefOtherQryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                // initColumnXlsDefTableColumnQryTable();
            }
        }));

        columnXlsDefOtherQryText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                initColumnXlsDefTableColumnQryTable();
            }
        });

        panel_30 = new JPanel();
        panel_28.add(panel_30, BorderLayout.WEST);

        panel_31 = new JPanel();
        panel_28.add(panel_31, BorderLayout.SOUTH);

        panel_32 = new JPanel();
        panel_28.add(panel_32, BorderLayout.EAST);

        columnXlsDefTableQryText.setToolTipText("分隔\"^\", 正則/.../");
        columnXlsDefColumnQryText.setToolTipText("分隔\"^\", 正則/.../");
        columnXlsDefOtherQryText.setToolTipText("分隔\"^\", 正則/.../");

        columnXlsDefTableQryText.addMouseListener(new S2T_And_T2S_EventHandler(columnXlsDefTableQryText).getEvent());
        columnXlsDefColumnQryText.addMouseListener(new S2T_And_T2S_EventHandler(columnXlsDefColumnQryText).getEvent());
        columnXlsDefOtherQryText.addMouseListener(new S2T_And_T2S_EventHandler(columnXlsDefOtherQryText).getEvent());

        columnXlsDefTableColumnQryTable = new JTable();
        panel_28.add(JCommonUtil.createScrollComponent(columnXlsDefTableColumnQryTable), BorderLayout.CENTER);
        JTableUtil.defaultSetting(columnXlsDefTableColumnQryTable);

        panel_6 = new JPanel();
        tabbedPane.addTab("DB連線設定", null, panel_6, null);
        panel_6.setLayout(new FormLayout(
                new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        saveConnectionBtn = new JButton("儲存連線設定");
        saveConnectionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveConnectionBtnClick();
            }
        });

        lblDbName = new JLabel("DB設定名");
        panel_6.add(lblDbName, "4, 2");

        dbNameIdText = new JComboBox();
        // dbNameIdText.setColumns(10);
        dbNameIdText_Auto = AutoComboBox.applyAutoComboBox(dbNameIdText);
        dbNameIdText_Auto.setMatchType(MatchType.Contains);
        reload_DataSourceConfig_autoComplete();
        panel_6.add(dbNameIdText, "10, 2, fill, default");
        dbNameIdText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String key = dbNameIdText_getText();
                    Map<String, String> param = dataSourceConfig.getConfig(key);
                    if (param == null || param.isEmpty()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("選擇錯誤!");
                        return;
                    }
                    initDataSourceProperties(param);
                    dataSourceConfig.setCurrentIndex(key);
                } catch (Exception e1) {
                    JCommonUtil.handleException(e1);
                }
            }
        });

        lblUrl = new JLabel("連線URL");
        panel_6.add(lblUrl, "4, 6");

        dbUrlText = new JTextField();
        panel_6.add(dbUrlText, "10, 6, fill, default");
        dbUrlText.setColumns(10);

        lblNewLabel = new JLabel("DB帳號");
        panel_6.add(lblNewLabel, "4, 10");

        dbUserText = new JTextField();
        panel_6.add(dbUserText, "10, 10, fill, default");
        dbUserText.setColumns(10);

        lblNewLabel_1 = new JLabel("DB密碼");
        panel_6.add(lblNewLabel_1, "4, 14");

        dbPwdText = new JTextField();
        panel_6.add(dbPwdText, "10, 14, fill, default");
        dbPwdText.setColumns(10);

        lblNewLabel_2 = new JLabel("Driver類別");
        panel_6.add(lblNewLabel_2, "4, 18");

        dbDriverText = new JTextField();
        panel_6.add(dbDriverText, "10, 18, fill, default");
        dbDriverText.setColumns(10);
        panel_6.add(saveConnectionBtn, "4, 22");

        panel_17 = new JPanel();
        panel_6.add(panel_17, "10, 22, fill, fill");

        prevConnBtn = new JButton("上一組");
        prevConnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                previousConnBtnClick();
            }
        });
        panel_17.add(prevConnBtn);

        nextConnBtn = new JButton("下一組");
        panel_17.add(nextConnBtn);

        removeConnectionBtn = new JButton("刪除連線設定");
        removeConnectionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeConnectionBtnAction();
            }
        });
        panel_17.add(removeConnectionBtn);

        nextConnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextConnBtnClick();
            }
        });

        nextParameterBtn = new JButton("下一組參數");
        nextParameterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextParameterBtnClick();
            }
        });

        saveParameterTableBtn = new JButton("儲存參數");
        saveParameterTableBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    saveParameterTableConfig(true);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_4.add(saveParameterTableBtn);
        panel_4.add(nextParameterBtn);

        connTestBtn = new JButton("測試連線");
        connTestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connTestBtnAction();
            }
        });
        panel_6.add(connTestBtn, "4, 24");

        panel_24 = new JPanel();
        panel_6.add(panel_24, "10, 32, fill, fill");

        if (true) {
            panel_24.add(JComboBoxUtil.createLookAndFeelComboBox(new Callable<JFrame>() {
                @Override
                public JFrame call() throws Exception {
                    return TAB_UI1.getJframe();
                }
            }));
        }

        panel_23 = new JPanel();
        panel_6.add(panel_23, "10, 34, fill, fill");

        saveEtcConfigBtn = new JButton("儲存設定組態");
        saveEtcConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEtcConfigBtnAction();
            }
        });

        exportYamlConfigBtn = new JButton("匯出yaml設定");
        exportYamlConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File sqlIdFile = new File(FileUtil.DESKTOP_PATH, FastDBQueryUI.class.getSimpleName() + "_sqlList.yml");
                    sqlIdConfigBeanHandler.init("");
                    YamlMapUtil.getInstance().saveToFilePlain(sqlIdFile, sqlIdConfigBeanHandler.lst, false, null);
                    JCommonUtil._jOptionPane_showMessageDialog_info("done...");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        setFontSizeBtn = new JButton("設定字型大小");
        setFontSizeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String val = JCommonUtil._jOptionPane_showInputDialog("輸入字型大小", "20");
                    if (val != null) {
                        setAllFontSize(Integer.parseInt(val));
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_23.add(setFontSizeBtn);
        panel_23.add(exportYamlConfigBtn);

        importYamlConfigBtn = new JButton("匯入yaml設定");
        importYamlConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File yamlFile = JCommonUtil._jFileChooser_selectFileOnly();
                    if (!(yamlFile != null && yamlFile.exists() && yamlFile.getName().endsWith("yml"))) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("請選擇yml檔");
                        return;
                    }
                    sqlIdConfigBeanHandler.saveYamlToProp(yamlFile, true);
                    JCommonUtil._jOptionPane_showMessageDialog_info("匯入成功!");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_23.add(importYamlConfigBtn);
        panel_23.add(saveEtcConfigBtn);

        deleteParameterBtn = new JButton("刪除當前參數");
        deleteParameterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sqlParameterConfigLoadHandler.deleteParameterBtnAction();
            }
        });

        clearParameterBtn = new JButton("清除參數");
        clearParameterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
                JTableUtil u = JTableUtil.newInstance(parametersTable);
                for (int ii = 0; ii < model.getRowCount(); ii++) {
                    u.setValueAt(false, "", ii, ParameterTableColumnDef.VALUE.idx);
                }
            }
        });
        panel_4.add(clearParameterBtn);
        panel_4.add(deleteParameterBtn);

        executeSqlButton2 = new JButton("執行Sql");
        executeSqlButton2.setToolTipText("快速鍵 F5");
        executeSqlButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeSqlButtonClick();
            }
        });
        panel_4.add(executeSqlButton2);

        refConfigPathYamlExportBtn = new JButton("產生yaml");
        refConfigPathYamlExportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File yamlFile = JCommonUtil._jFileChooser_selectFileOnly();
                    if (yamlFile == null) {
                        return;
                    }

                    List<RefSearchListConfigBean> lst = new ArrayList<RefSearchListConfigBean>();
                    Properties prop = PropertiesUtil.loadProperties(yamlFile, null, true);
                    for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
                        String key = (String) enu.nextElement();
                        String value = prop.getProperty(key);
                        RefSearchListConfigBean bean = PropertiesMultiUtil.of(key, value, RefSearchListConfigBean.class);
                        lst.add(bean);
                    }

                    File destYamlFile = new File(FileUtil.DESKTOP_DIR, FastDBQueryUI.class.getSimpleName() + "_Ref.yml");
                    YamlMapUtil.getInstance().saveToFilePlain(destYamlFile, lst, false, null);
                    JCommonUtil._jOptionPane_showMessageDialog_info("ok!");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        queryResultTable.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (mJDlgHolderBringToFrontHandler != null) {
                    mJDlgHolderBringToFrontHandler.bringToFront();
                }
            }
        });

        // -----------------------------------------------------------------------------------------------
        // -----------------------------------------------------------------------------------------------
        {
            sqlIdConfigBeanHandler = new SqlIdConfigBeanHandler();
            sqlIdListDSMappingHandler = new SqlIdListDSMappingHandler();
            mJDlgHolderBringToFrontHandler = new JDlgHolderBringToFrontHandler();

            // 初始化datasource
            this.initDataSourceProperties(null);

            // 初始化parameterTable
            initParametersTable();

            // 初始化queryResultTable
            JTableUtil.defaultSetting(queryResultTable);

            // 初始化 sqlList
            initLoadSqlListConfig();
            sqlIdListDSMappingHandler.init();

            etcConfigHandler = new EtcConfigHandler();
            etcConfigHandler.reflectInit();

            refSearchListConfigHandler = new RefSearchListConfigHandler(refConfigPathText, refSearchList, refSearchCategoryCombobox);

            panel_22.add(refConfigPathYamlExportBtn);

            KeyEventExecuteHandler.newInstance(FastDBQueryUI.this, "查詢中...", null, new Runnable() {
                @Override
                public void run() {
                    executeSqlButtonClick();
                }
            }, new Component[] {});

            KeyEventExecuteHandler.newInstance(FastDBQueryUI.this, "", new Transformer() {
                public Object transform(Object input) {
                    KeyEvent e = (KeyEvent) input;
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        return true;
                    }
                    return false;
                }
            }, new Runnable() {
                @Override
                public void run() {
                    if (tabbedPane.getSelectedIndex() > 0) {
                        tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
                    }
                }
            }, IGNORE_PREDICT);

            KeyEventExecuteHandler.newInstance(FastDBQueryUI.this, "", new Transformer() {
                public Object transform(Object input) {
                    KeyEvent e = (KeyEvent) input;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        return true;
                    }
                    return false;
                }
            }, new Runnable() {
                @Override
                public void run() {
                    if (tabbedPane.getSelectedIndex() <= tabbedPane.getTabCount() - 1) {
                        tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
                    }
                }
            }, IGNORE_PREDICT);

            editColumnHistoryHandler = new EditColumnHistoryHandler();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.defaultToolTipDelay();
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/" + ICO_FILENAME);// big_boobs.ico
            this.setTitle("You Set My World On Fire");

            if (jFrameRGBColorPanel.get() == null) {
                jFrameRGBColorPanel.set(new JFrameRGBColorPanel(this));
            }
            //
            panel_17.add(jFrameRGBColorPanel.get().getToggleButton(false));

            if (hideInSystemTrayHelper.get() == null) {
                hideInSystemTrayHelper.set(HideInSystemTrayHelper.newInstance());
                hideInSystemTrayHelper.get().apply(this);
            }
            panel_17.add(hideInSystemTrayHelper.get().getToggleButton(false));
        }
    }

    private void setAllFontSize(int size) {
        JCommonUtil.setUIFont("Serif", size);
        JCommonUtil.setUIFontSize(this, 12, size);

        sqlTextArea.setFont(sqlTextArea.getFont().deriveFont((float) size));
        queryResultJsonTextArea.setFont(queryResultJsonTextArea.getFont().deriveFont((float) size));
        refContentArea.setFont(refContentArea.getFont().deriveFont((float) size));
        sqlParamCommentArea.setFont(sqlParamCommentArea.getFont().deriveFont((float) size));
        sqlIdCommentArea.setFont(sqlIdCommentArea.getFont().deriveFont((float) size));
    }

    private void initParametersTable() {
        JTableUtil.defaultSetting_AutoResize(parametersTable);
        DefaultTableModel createModel = JTableUtil.createModel(//
                new int[] { ParameterTableColumnDef.USE.idx, ParameterTableColumnDef.VALUE.idx, ParameterTableColumnDef.TYPE.idx }, //
                new Object[] { "使用", "參數", "值", "類型" });
        parametersTable.setModel(createModel);
        JTableUtil.newInstance(parametersTable).setRowHeightByFontSize();
        JTableUtil.setColumnWidths_Percent(parametersTable, new float[] { 5, 32, 32, 31 });

        // column = "Data Type"
        TableColumn sportColumn = parametersTable.getColumnModel().getColumn(3);
        JComboBox comboBox = new JComboBox();
        for (DataType e : DataType.values()) {
            comboBox.addItem(e);
        }
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private enum DataType {
        varchar(new Class<?>[] { String.class }, false) {
        }, //
        date(new Class<?>[] { java.sql.Date.class }, false) {
            protected Object applyDataChange(Object value) {
                System.out.println("-------" + value + " -> " + value.getClass());
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    try {
                        String val = (String) value;
                        java.sql.Date newVal = java.sql.Date.valueOf(val);
                        return newVal;
                    } catch (Exception ex) {
                        throw new RuntimeException("請輸入Date格式 yyyy-MM-dd , value : " + value + ", ERR : " + ex.getMessage(), ex);
                    }
                }
                return value;
            }
        }, //
        timestamp(new Class<?>[] { java.sql.Timestamp.class }, false) {
            protected Object applyDataChange(Object value) {
                System.out.println("-------" + value + " -> " + value.getClass());
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    try {
                        String val = (String) value;
                        java.sql.Timestamp newVal = java.sql.Timestamp.valueOf(val);
                        return newVal;
                    } catch (Exception ex) {
                        throw new RuntimeException("請輸入Timestamp格式 yyyy-MM-dd HH:mm:ss.SSSSS, value : " + value + ", ERR : " + ex.getMessage(), ex);
                    }
                }
                return value;
            }
        }, //
        number(new Class<?>[] { Number.class }, false) {
        }, //
        NULL(new Class<?>[] { void.class }, true) {
            protected Object applyDataChange(Object value) {
                return null;
            }
        }, //
        Empty(new Class<?>[] { String.class }, true) {
            protected Object applyDataChange(Object value) {
                return "";
            }
        }, //
        Varchar_Array(new Class<?>[] { String.class }, true) {
            protected Object applyDataChange(Object value) {
                System.out.println("-------" + value + " -> " + value.getClass());
                if (value instanceof String && StringUtils.isNotBlank((String) value)) {
                    List<Object> lst = new ArrayList<Object>();
                    String strVal = (String) value;
                    String[] valArry = StringUtils.split(strVal, ',');
                    for (String str : valArry) {
                        lst.add(StringUtils.trimToEmpty(str));
                    }
                    return lst.toArray();
                }
                return value;
            }
        }, //
        UNKNOW(new Class<?>[] { void.class }, false) {
        },//
        ;

        final Class<?>[] clz;
        final boolean forceAddColumn;

        DataType(Class<?>[] clz, boolean forceAddColumn) {
            this.clz = clz;
            this.forceAddColumn = forceAddColumn;
        }

        protected Object applyDataChange(Object value) {
            return value;
        }

        protected boolean isForceAddColumn() {
            return forceAddColumn;
        }
    }

    /**
     * 初始化sqlList
     */
    private void initLoadSqlListConfig() throws IOException {
        sqlIdConfigBeanHandler.init(sqlIdCategoryComboBox_Auto.getTextComponent().getText());
        sqlIdListDSMappingHandler.init();

        String queryText = StringUtils.trimToEmpty(sqlQueryText.getText()).toLowerCase();
        String contentFilterText = StringUtils.trimToEmpty(sqlContentFilterText.getText()).toLowerCase();
        String mappingFilterText = StringUtils.trimToEmpty(sqlMappingFilterText_Auto.getTextComponent().getText()).toLowerCase();

        List<SqlIdConfigBean> sqlIdList = new ArrayList<SqlIdConfigBean>();
        for (SqlIdConfigBean enu : sqlIdConfigBeanHandler.lst) {
            String sqlId = enu.sqlId;
            String category = StringUtils.trimToEmpty(enu.category).toLowerCase();
            String sqlIdCompare = sqlId.toLowerCase().toLowerCase();
            String content = StringUtils.trimToEmpty(enu.sql).toLowerCase();
            String comment = StringUtils.trimToEmpty(enu.sqlComment).toLowerCase();

            boolean findOk = false;

            if (StringUtils.isBlank(queryText) && StringUtils.isBlank(contentFilterText)) {
                findOk = true;
            } else {
                if (StringUtils.isNotBlank(queryText)) {
                    if ((category.contains(queryText) || sqlIdCompare.contains(queryText) || comment.contains(queryText))) {
                        findOk = true;
                    }
                }

                if (!findOk && StringUtils.isNotBlank(contentFilterText)) {
                    if (content.contains(contentFilterText)) {
                        findOk = true;
                    } else if (mSqlIdColumnHolder.isColumnExists(sqlId, contentFilterText)) {
                        findOk = true;
                    }
                }
            }

            if (StringUtils.isNotBlank(mappingFilterText)) {
                if (findOk) {
                    if (StringUtils.isNotBlank(sqlIdListDSMappingHandler.getProperty(sqlId)) && //
                            sqlIdListDSMappingHandler.getProperty(sqlId).toLowerCase().contains(mappingFilterText)) {
                    } else {
                        findOk = false;
                    }
                }
            }

            if (findOk) {
                sqlIdList.add(enu);
            }
        }

        Collections.sort(sqlIdList, new Comparator<SqlIdConfigBean>() {
            @Override
            public int compare(SqlIdConfigBean o1, SqlIdConfigBean o2) {
                int compare1 = StringUtils.trimToEmpty(o1.category).toLowerCase().compareTo(StringUtils.trimToEmpty(o2.category).toLowerCase());
                int compare2 = StringUtils.trimToEmpty(o1.sqlId).toLowerCase().compareTo(StringUtils.trimToEmpty(o2.sqlId).toLowerCase());
                if (compare1 != 0) {
                    return compare1;
                }
                return compare2;
            }
        });

        DefaultListModel model = JListUtil.createModel();
        for (SqlIdConfigBean s : sqlIdList) {
            model.addElement(s);
        }
        sqlList.setModel(model);
    }

    // ---------------------------------------------db conn combox ↓↓↓↓↓↓
    private String dbNameIdText_getText() {
        return StringUtils.defaultString(dbNameIdText_Auto.getTextComponent().getText());
    }

    private void dbNameIdText_setText(String text) {
        // dbNameIdText_Auto.setSelectItemAndText(text);
        dbNameIdText_Auto.getTextComponent().setText(text);
        sqlPageDbConnCombox.setSelectedItem(text);
    }

    private void reload_DataSourceConfig_autoComplete() {
        dbNameIdText_Auto.applyComboxBoxList(dataSourceConfig.getSaveKeys(), dbNameIdText_getText());
        sqlMappingFilterText_Auto.applyComboxBoxList(dataSourceConfig.getSaveKeys(), dbNameIdText_getText());
        sqlPageDbConnCombox.setModel(JComboBoxUtil.createModel(dataSourceConfig.getSaveKeys()));
    }
    // ---------------------------------------------db conn combox ↑↑↑↑↑↑

    /**
     * 初始化dataSource
     */
    private void initDataSourceProperties(Map<String, String> param) throws IOException {
        if (param == null || param.isEmpty()) {
            param = dataSourceConfig.loadConfig();
        }
        if (param.containsKey(PropertiesGroupUtils_ByKey.SAVE_KEYS) && StringUtils.isNotBlank(param.get(PropertiesGroupUtils_ByKey.SAVE_KEYS))) {
            dbNameIdText_setText(param.get(PropertiesGroupUtils_ByKey.SAVE_KEYS));
        }
        if (param.containsKey("url") && StringUtils.isNotBlank(param.get("url"))) {
            dbUrlText.setText(param.get("url"));
        }
        if (param.containsKey("user") && StringUtils.isNotBlank(param.get("user"))) {
            dbUserText.setText(param.get("user"));
        }
        if (param.containsKey("pwd")) {// 密碼可以空
            dbPwdText.setText(param.get("pwd"));
        }
        if (param.containsKey("driver") && StringUtils.isNotBlank(param.get("driver"))) {
            dbDriverText.setText(param.get("driver"));
        }
    }

    /**
     * 儲存連線設定
     */
    private void saveConnectionBtnClick() {
        try {
            String dbNameId = dbNameIdText_getText();
            String url = dbUrlText.getText();
            String user = dbUserText.getText();
            String pwd = dbPwdText.getText();
            String driver = dbDriverText.getText();
            JCommonUtil.isBlankErrorMsg(dbNameId, "DBName empty");
            JCommonUtil.isBlankErrorMsg(url, "url empty");
            JCommonUtil.isBlankErrorMsg(user, "user empty");
            // JCommonUtil.isBlankErrorMsg(pwd, "pwd empty");//密碼可以空
            JCommonUtil.isBlankErrorMsg(driver, "driver empty");

            Map<String, String> param = new HashMap<String, String>();
            param.put(PropertiesGroupUtils_ByKey.SAVE_KEYS, dbNameId);
            param.put("url", url);
            param.put("user", user);
            param.put("pwd", pwd);
            param.put("driver", driver);

            dataSourceConfig.saveConfig(param);
            reload_DataSourceConfig_autoComplete();

            if (externalJDBCDriverJarLoader.get() != null) {
                System.out.println("## use custom class loader");
                externalJDBCDriverJarLoader.get().registerDriver(driver);
                Class.forName(driver, true, externalJDBCDriverJarLoader.get().getUrlClassLoader());
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 清空text
     */
    private void clearButtonClick() {
        sqlIdText.setText("");
        sqlTextArea.setText("");
        sqlIdCommentArea.setText("");
        this.sqlBean = null;
        setSqlListSelection(this.sqlBean);
    }

    /**
     * 儲存sql
     */
    private void saveSqlButtonClick(boolean saveSqlIdConfig) {
        try {
            if (isSqlIdChange()) {
                boolean isContinue = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("您輸入SqlID以存在:" + sqlIdText.getText() + ", 是否要繼續?", "已存在SqlID");
                if (!isContinue) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("儲存取消!!!");
                    return;
                }
            }

            String sqlId = sqlIdText.getText();
            RefSearchColor color = (RefSearchColor) sqlIdColorComboBox.getSelectedItem();
            String category = sqlIdCategoryComboBox_Auto.getTextComponent().getText();
            String sql = sqlTextArea.getText();
            String sqlComment = sqlIdCommentArea.getText();
            JCommonUtil.isBlankErrorMsg(sqlId, "請輸入sql Id");
            JCommonUtil.isBlankErrorMsg(sql, "請輸入sql");

            SqlParam param = parseSqlToParam(sql);

            // 更新parameter表
            setParameterTable(param);

            // 儲存sqlList Prop
            SqlIdConfigBean bean = new SqlIdConfigBean();
            bean.color = color.colorCode;
            bean.category = category;
            bean.sqlId = sqlId;
            bean.sql = sql;
            bean.sqlComment = sqlComment;

            // 改變TabUI標題
            changeTabUITitile(bean.sqlId);

            if (saveSqlIdConfig) {
                bean = this.saveSqlListProp(bean);
            }
            // 儲存DS設定
            if (saveSqlIdConfig) {
                sqlIdListDSMappingHandler.store(false);// sqlPageDbConnCombox
            }

            // 載入參數設定
            sqlParameterConfigLoadHandler.init(bean.getUniqueKey());

            // 刷新sqlList
            initLoadSqlListConfig();
            sqlIdListDSMappingHandler.init();

            // 儲存變更
            setSqlListSelection(bean);
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
        }
    }

    // 儲存變更
    private void setSqlListSelection(SqlIdConfigBean bean) {
        sqlBean = bean;
        sqlList.setSelectedValue(sqlBean, true);
        sqlTextAreaChange();
    }

    /**
     * 載入參數
     */
    private void setParameterTable(SqlParam param) {
        initParametersTable();
        DefaultTableModel createModel = (DefaultTableModel) parametersTable.getModel();
        for (String column : param.getOrderParametersLst()) {
            createModel.addRow(new Object[] { true, column, "", DataType.varchar });
        }
    }

    /**
     * 儲存prop
     */
    private SqlIdConfigBean saveSqlListProp(SqlIdConfigBean bean) throws IOException {
        System.out.println("#saveSqlListProp = " + ReflectionToStringBuilder.toString(bean));
        sqlIdConfigBeanHandler.save(bean);
        System.out.println("儲存檔案路徑 : " + sqlIdListFile);
        return bean;
    }

    private Object getRealValue(String value, DataType dataType) {
        return dataType.applyDataChange(value);
    }

    private enum ParameterTableColumnDef {
        USE(0), COLUMN(1), VALUE(2), TYPE(3);

        final int idx;

        ParameterTableColumnDef(int idx) {
            this.idx = idx;
        }
    }

    private String getCurrentSQL() {
        String sql = sqlTextArea.getText().toString();
        if (StringUtils.isNotBlank(sqlTextArea.getSelectedText())) {
            sql = sqlTextArea.getSelectedText();
        }
        return sql;
    }

    /**
     * 執行sql
     */
    private void executeSqlButtonClick() {
        long startTime = System.currentTimeMillis();
        try {
            // init
            {
                isResetQuery = true;
                filterRowsQueryList = null;// rows 過濾清除
                importExcelSheetName = null; // 清除匯入黨名
                queryResultTimeLbl.setText("");
            }

            JTableUtil util = JTableUtil.newInstance(parametersTable);

            Map<String, Object> paramMap = new HashMap<String, Object>();
            Map<String, String> sqlInjectMap = new LinkedHashMap<String, String>();

            Set<String> forceAddColumns = new HashSet<String>();

            for (int ii = 0; ii < parametersTable.getRowCount(); ii++) {
                Boolean isInUse = (Boolean) util.getRealValueAt(ii, ParameterTableColumnDef.USE.idx);
                if (isInUse == null) {
                    isInUse = false;
                }

                String columnName = (String) util.getRealValueAt(ii, ParameterTableColumnDef.COLUMN.idx);
                String value = (String) util.getRealValueAt(ii, ParameterTableColumnDef.VALUE.idx);

                if (SqlParam.sqlInjectionPATTERN.matcher(columnName).matches()) {
                    // sql Injection
                    if (isInUse) {
                        sqlInjectMap.put(columnName, StringUtils.trimToEmpty(value));
                    } else {
                        sqlInjectMap.put(columnName, null);
                    }
                } else {
                    // 一般處理
                    DataType dataType = (DataType) util.getRealValueAt(ii, ParameterTableColumnDef.TYPE.idx);
                    if (isInUse) {
                        paramMap.put(columnName, getRealValue(value, dataType));
                    } else {
                        paramMap.put(columnName, null);
                    }

                    if (dataType.isForceAddColumn()) {
                        forceAddColumns.add(columnName);
                    }
                }
            }

            String sql = getCurrentSQL();

            JCommonUtil.isBlankErrorMsg(sql, "請輸入sql");

            // 取得執行sql物件
            SqlParam param = parseSqlToParam(sql);

            // 檢查參數是否異動
            for (String columnName : param.paramSet) {
                if (!paramMap.containsKey(columnName) && !sqlInjectMap.containsKey(columnName)) {
                    Validate.isTrue(false, "參數有異動!, 請重新按儲存按鈕");
                }
            }

            // 組參數列
            List<Object> parameterList = new ArrayList<Object>();
            if (param.getClass() == SqlParam.class) {
                for (String columnName : param.paramList) {
                    if (!paramMap.containsKey(columnName)) {
                        Validate.isTrue(false, "參數未設定 : " + columnName);
                    }
                    parameterList.add(paramMap.get(columnName));
                }
            } else if (param.getClass() == SqlParam_IfExists.class) {
                parameterList.addAll(((SqlParam_IfExists) param).processParamMap(paramMap, sqlInjectMap, forceAddColumns));
            }

            // 設定 sqlInjectionMap
            param.sqlInjectionMap.putAll(sqlInjectMap);

            System.out.println("尚未執行=====================================================");
            System.out.println(param.getQuestionSql());
            for (int i = 0; i < parameterList.size(); i++) {
                System.out.println("param[" + i + "]:\"" + parameterList.get(i) + "\"  (" + (parameterList.get(i) != null ? parameterList.get(i).getClass().getName() : "NA") + ")");
            }
            System.out.println("尚未執行=====================================================");

            // 判斷執行模式
            if (querySqlRadio.isSelected()) {
                int maxRowsLimit = StringNumberUtil.parseInt(maxRowsText.getText(), 0);
                Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult = JdbcDBUtil.queryForList_customColumns(param.getQuestionSql(), parameterList.toArray(),
                        this.getDataSource().getConnection(), true, maxRowsLimit);

                createRecordWatcher(orignQueryResult, param.getQuestionSql(), parameterList.toArray(), true, maxRowsLimit);

                mSqlIdColumnHolder.setColumns(mSqlIdColumnHolder.getSqlId(), orignQueryResult.getLeft());

                this.queryList = orignQueryResult;

                // 切換查詢結果
                if (!queryList.getRight().isEmpty()) {
                    tabbedPane.setSelectedIndex(3);
                }

                this.queryModeProcess(queryList, false, Pair.of(param, parameterList), null);

                this.showJsonArry(queryList);

                // 過濾欄位apply
                if (StringUtils.isNotBlank(rowFilterText.getText())) {
                    rowFilterTextDoFilter.run();
                }
            } else if (updateSqlRadio.isSelected()) {
                int modifyResult = JdbcDBUtil.modify(param.questionSql, parameterList.toArray(), this.getDataSource().getConnection(), true);
                JCommonUtil._jOptionPane_showMessageDialog_info("update : " + modifyResult);
            }

            // 設定欄位解釋定義
            setupCustomColumnDefExcelChinese();

            // 儲存參數設定
            saveParameterTableConfig(false);

            // 儲存sqlId mapping dataSource 設定
            sqlIdListDSMappingHandler.store(true);
        } catch (Exception ex) {
            queryResultTable.setModel(JTableUtil.createModel(true, "ERROR"));
            String category = refSearchCategoryCombobox_Auto.getTextComponent().getText();
            String findMessage = refSearchListConfigHandler.findExceptionMessage(category, ex.getMessage());
            // 一般顯示
            if (StringUtils.isBlank(findMessage)) {
                JCommonUtil.handleException(ex);
            } else {
                // html顯示
                JCommonUtil.handleException(String.format("參考 : %s", findMessage), ex, true, "", "yyyyMMdd", false, true);
            }
        } finally {
            BigDecimal duringTime = new BigDecimal(System.currentTimeMillis() - startTime).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_EVEN);
            queryResultTimeLbl.setText("查詢耗時:  " + duringTime + " 秒");
            JTableUtil.newInstance(queryResultTable).setRowHeightByFontSize();
        }
    }

    private void setupCustomColumnDefExcelChinese() {
        List<String> tabLst = new ArrayList<String>();
        tabLst.add(getRandom_TableNSchema());
        if (mSqlTextAreaPromptHandler != null && mSqlTextAreaPromptHandler.tabMap != null) {
            for (Object tab : mSqlTextAreaPromptHandler.tabMap.keySet()) {
                boolean findOk = false;
                String newTabName = String.valueOf(tab);
                A: for (String tabName : tabLst) {
                    if (StringUtils.equalsIgnoreCase(tabName, newTabName)) {
                        findOk = true;
                        break A;
                    }
                }
                if (!findOk) {
                    tabLst.add(newTabName);
                }
            }
        }
        tableColumnDefText_Auto.applyComboxBoxList(tabLst);
    }

    // 儲存參數設定
    private void saveParameterTableConfig(boolean showMsg) {
        if (!sqlParameterConfigLoadHandler.isInitOk()) {
            if (showMsg) {
                JCommonUtil._jOptionPane_showMessageDialog_error("參數設定檔未初始化!");
            }
            return;
        } else {
            Map<String, String> paramMap2 = new HashMap<String, String>();
            JTableUtil util2 = JTableUtil.newInstance(parametersTable);
            DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                String col = (String) util2.getRealValueAt(ii, ParameterTableColumnDef.COLUMN.idx);
                String val = (String) util2.getRealValueAt(ii, ParameterTableColumnDef.VALUE.idx);
                paramMap2.put(col, StringUtils.trimToEmpty(val));
            }
            try {
                // 一般儲存參數處理
                sqlParameterConfigLoadHandler.saveConfig(paramMap2, sqlParamCommentArea.getText());
            } catch (Exception ex) {
                // 出現異常詢問是否重設
                boolean resetOk = false;
                if (ex.getMessage().contains("參數不同")) {
                    boolean resetConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(ex.getMessage(), "是否要重設?");
                    if (resetConfirm) {
                        sqlParameterConfigLoadHandler.clear();
                        sqlParameterConfigLoadHandler.saveConfig(paramMap2, sqlParamCommentArea.getText());
                        resetOk = true;
                    }
                }
                if (!resetOk) {
                    throw new RuntimeException(ex);
                }
                if (showMsg) {
                    JCommonUtil._jOptionPane_showMessageDialog_info("參數儲存成功!");
                }
            }
        }
    }

    private void showJsonArry(Triple<List<String>, List<Class<?>>, List<Object[]>> queryList) {
        try {
            List<String> columns = queryList.getLeft();
            List<Map<String, Object>> cloneLst = new ArrayList<Map<String, Object>>();
            for (Object[] rows : queryList.getRight()) {
                Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
                for (int ii = 0; ii < columns.size(); ii++) {
                    String col = columns.get(ii);
                    Object val = rows[ii];
                    rowMap.put(col, val);
                }
                cloneLst.add(rowMap);
                for (String key : queryList.getLeft()) {
                    if (rowMap.get(key) != null && (rowMap.get(key) instanceof java.sql.Date || rowMap.get(key) instanceof java.sql.Timestamp)) {
                        rowMap.put(key, String.valueOf(rowMap.get(key)));
                    }
                }
            }
            queryResultJsonTextArea.setText(JSONUtils.valueToString(JSONArray.fromObject(cloneLst), 8, 4));
        } catch (Exception ex) {
            queryResultJsonTextArea.setText("");
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 查詢模式
     * 
     * @param pair
     */
    private void queryModeProcess(Triple<List<String>, List<Class<?>>, List<Object[]>> queryList, boolean silent, Pair<SqlParam, List<Object>> pair,
            Map<Integer, List<Integer>> changeColorRowCellIdxMap) {
        if (queryList.getRight().isEmpty()) {
            if (!silent) {
                System.out.println("fake row----");
                queryResultTable.setModel(getFakeDataModel(pair));
                JCommonUtil._jOptionPane_showMessageDialog_info("查無資料!");
            } else {
                DefaultTableModel createModel = JTableUtil.createModel(true, "");
                queryResultTable.setModel(createModel);
            }
            queryResultCountLabel.setText("0");
            JTableUtil.newInstance(queryResultTable).setRowHeightByFontSize();
            return;
        } else {
            if (!silent) {
                // JCommonUtil._jOptionPane_showMessageDialog_info("size : " +
                // queryList.getRight().size());
            }
            queryResultCountLabel.setText(String.valueOf(queryList.getRight().size()));
        }

        // 查詢結果table
        LinkedList<String> left = new LinkedList<String>(queryList.getLeft());
        LinkedList<Class<?>> middle = new LinkedList<Class<?>>(queryList.getMiddle());
        left.addFirst(QUERY_RESULT_COLUMN_NO);
        middle.addFirst(JButton.class);
        DefaultTableModel createModel = JTableUtil.createModelIndicateType(true, left, middle);

        queryResultTable.setModel(createModel);

        JTableUtil.newInstance(queryResultTable).setRowHeightByFontSize();

        // 設定 Value 顯示方式
        JTableUtil.newInstance(queryResultTable).columnUseCommonFormatter(null, false);

        for (int ii = 0; ii < queryList.getRight().size(); ii++) {
            Object[] rows = queryList.getRight().get(ii);
            Object[] rows2 = new Object[rows.length + 1];
            System.arraycopy(rows, 0, rows2, 1, rows.length);
            rows2[0] = createSelectionBtn(ii + 1);
            createModel.addRow(rows2);
        }

        if (changeColorRowCellIdxMap != null) {
            JTableUtil.newInstance(queryResultTable).setCellBackgroundColor(Color.green.brighter(), changeColorRowCellIdxMap);
        }

        JTableUtil.newInstance(queryResultTable).columnIsButton(QUERY_RESULT_COLUMN_NO);

        if (true) {
            Map<String, Object> preference = new HashMap<String, Object>();
            preference.put("offset", 0.75f);
            preference.put("isCaculateTitle", true);
            preference.put("maxWidth", 500);
            Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
            presetColumns.put(0, String.valueOf(queryList.getRight().size()).length() * 10 + 15);
            preference.put("presetColumns", presetColumns);
            JTableUtil.setColumnWidths_ByDataContent(queryResultTable, preference, getInsets());
        } else {
            JTableUtil.setColumnWidths(queryResultTable, getInsets());
        }
    }

    private JToggleButton createSelectionBtn(int serialNo) {
        JToggleButton selectionBtn = new JToggleButton(String.valueOf((serialNo)));
        selectionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTableUtil.newInstance(queryResultTable).setRowSelection();
            }
        });
        return selectionBtn;
    }

    private DefaultTableModel getFakeDataModel(Pair<SqlParam, List<Object>> pair) {
        FakeDataModelHandler handler = new FakeDataModelHandler(pair, this.getDataSource());
        this.queryList = handler.getQueryList();
        return handler.getModel();
    }

    /**
     * parse Sql
     */
    private SqlParam parseSqlToParam(String sql) {
        // 中括號特殊處理
        int matchCount = 0;
        if ((matchCount = StringUtils.countMatches(sql, "[")) == StringUtils.countMatches(sql, "]")) {
            if (matchCount != 0) {
                SqlParam_IfExists sqlParam = SqlParam_IfExists.parseToSqlParam(sql);
                sqlParam.parseToSqlInjectionMap(sql);
                return sqlParam;
            }
        }
        return SqlParam.parseToSqlParam(sql);
    }

    private static class SqlParam {
        String orginialSql;
        String questionSql;
        Set<String> paramSet = new LinkedHashSet<String>();
        List<String> paramList = new ArrayList<String>();

        private static Pattern sqlInjectionPATTERN = Pattern.compile("\\_\\#\\w+\\#\\_");
        Map<String, String> sqlInjectionMap = new LinkedHashMap<String, String>();

        private String sqlInjectionReplace() {
            Matcher mth = sqlInjectionPATTERN.matcher(questionSql);
            StringBuffer sb = new StringBuffer();
            while (mth.find()) {
                String key = mth.group();
                String replaceStr = StringUtils.trimToEmpty(sqlInjectionMap.get(key));
                mth.appendReplacement(sb, replaceStr);
            }
            mth.appendTail(sb);
            return sb.toString();
        }

        public String getQuestionSql() {
            return sqlInjectionReplace();
        }

        public void parseToSqlInjectionMap(String sql) {
            Matcher mth = sqlInjectionPATTERN.matcher(sql);
            while (mth.find()) {
                String key = mth.group();
                sqlInjectionMap.put(key, "");
            }
        }

        public List<String> getOrderParametersLst() {
            List<String> lst = new ArrayList<String>();
            lst.addAll(paramSet);

            Set<String> s1 = new LinkedHashSet<String>();
            for (String k : sqlInjectionMap.keySet()) {
                if (!paramSet.contains(k)) {
                    s1.add(k);
                }
            }
            lst.addAll(s1);
            return lst;
        }

        protected static String getIgnoreCommonentSql(String sql) {
            StringBuffer sb = new StringBuffer();
            Pattern ptn1 = Pattern.compile("\\/\\*(?:.|\n)*?\\*\\/", Pattern.MULTILINE | Pattern.DOTALL);
            Matcher mth = ptn1.matcher(sql);
            while (mth.find()) {
                mth.appendReplacement(sb, "");
            }
            mth.appendTail(sb);

            Pattern ptn2 = Pattern.compile("\\-{2}.*");
            sql = sb.toString();
            sb.setLength(0);
            Matcher mth2 = ptn2.matcher(sql);
            while (mth2.find()) {
                String tmp = mth2.group();
                if (tmp.indexOf("]") == -1) {// 如果有對應參數設定則不拿掉
                    mth2.appendReplacement(sb, "");
                } else {
                    mth2.appendReplacement(sb, tmp);
                }
            }
            mth2.appendTail(sb);
            return sb.toString();
        }

        public static SqlParam parseToSqlParam(String sql) {
            // 一般處理
            Pattern ptn = Pattern.compile(SQL_PARAM_PTN);
            Matcher mth = ptn.matcher(getIgnoreCommonentSql(sql));// <----------------

            List<String> paramList = new ArrayList<String>();
            Set<String> paramSet = new LinkedHashSet<String>();

            StringBuffer sb2 = new StringBuffer();

            while (mth.find()) {
                String key = mth.group(1);
                if (isNotParam(key)) {
                    continue;
                }
                paramList.add(key);
                paramSet.add(key);
                mth.appendReplacement(sb2, "?");
            }
            mth.appendTail(sb2);

            SqlParam sqlParam = new SqlParam();
            sqlParam.orginialSql = sql;
            sqlParam.paramSet = paramSet;
            sqlParam.questionSql = sb2.toString();
            sqlParam.paramList = paramList;
            sqlParam.parseToSqlInjectionMap(sql);
            return sqlParam;
        }

        protected static boolean isNotParam(String sql) {
            return StringUtils.defaultString(sql).matches("\\:?\\d+.*");
        }
    }

    private static class SqlParam_IfExists extends SqlParam {
        List<Pair<List<String>, int[]>> paramListFix = new ArrayList<Pair<List<String>, int[]>>();
        private Map<String, String> paramSetSentanceMap = new HashMap<String, String>();

        private boolean isParametersAllOk(List<String> paramLst, Map<String, Object> paramMap, Map<String, String> sqlInjectionMap, Set<String> forceAddColumns) {
            List<Pair<String, Boolean>> paramBoolLst = new ArrayList<Pair<String, Boolean>>();
            for (String col : paramLst) {
                if (paramMap.containsKey(col)) {
                    if (forceAddColumns.contains(col)) {
                        paramBoolLst.add(Pair.of(col, true));
                    } else if (paramMap.get(col) != null) {
                        if (paramMap.get(col) instanceof String) {
                            String tmpParamVal = StringUtils.trimToEmpty((String) paramMap.get(col));
                            if (StringUtils.isNotBlank(tmpParamVal)) {
                                paramBoolLst.add(Pair.of(col, true));
                            } else {
                                paramBoolLst.add(Pair.of(col, false));
                            }
                        } else {
                            paramBoolLst.add(Pair.of(col, false));
                            System.out.println("false-----特殊型別 : " + col);
                        }
                    } else {
                        paramBoolLst.add(Pair.of(col, false));
                    }
                } else if (sqlInjectionMap.containsKey(col) && StringUtils.isNotBlank(sqlInjectionMap.get(col))) {
                    paramBoolLst.add(Pair.of(col, true));
                } else {
                    paramBoolLst.add(Pair.of(col, false));
                }
            }
            boolean isOk = true;
            for (Pair<String, Boolean> param : paramBoolLst) {
                System.out.println("param : " + param.getLeft() + "\t" + param.getRight());
                if (!param.getRight()) {
                    isOk = false;
                }
            }
            return isOk;
        }

        public static SqlParam_IfExists parseToSqlParam(String sql) {
            SqlParam_IfExists sqlParam = new SqlParam_IfExists();
            sqlParam.orginialSql = sql;

            Pattern ptn = Pattern.compile("(\\[((?:.|\n)*?)\\]|" + SQL_PARAM_PTN + ")");
            Matcher mth = ptn.matcher(getIgnoreCommonentSql(sql));

            while (mth.find()) {
                String quoteLine = mth.group(1);

                // 非必填檢查
                if (quoteLine.matches("^\\[(.|\n)*\\]")) {
                    String realQuoteLine = mth.group(2);
                    Pattern ptn2 = Pattern.compile(SQL_PARAM_PTN);
                    Pattern ptn3 = Pattern.compile("\\_\\#.*?\\#\\_");
                    Matcher mth2 = ptn2.matcher(realQuoteLine);
                    Matcher mth3 = ptn3.matcher(realQuoteLine);

                    List<String> params = new ArrayList<String>();
                    while (mth2.find()) {
                        String para = mth2.group(1);
                        if (isNotParam(para)) {
                            continue;
                        }
                        params.add(para);
                        sqlParam.paramSetSentanceMap.put(para, quoteLine);
                    }

                    while (mth3.find()) {
                        String para = mth3.group(0);
                        params.add(para);
                        sqlParam.paramSetSentanceMap.put(para, quoteLine);
                    }
                    sqlParam.paramSet.addAll(params);

                    if (!params.isEmpty()) {
                        sqlParam.paramListFix.add(Pair.of(params, new int[] { mth.start(), mth.end() }));
                    }
                }
                // 必填檢查
                else {
                    String realQuoteLine = mth.group(3);
                    sqlParam.paramSet.add(realQuoteLine);
                    sqlParam.paramListFix.add(Pair.of(Arrays.asList(realQuoteLine), new int[] { mth.start(1), mth.end(1) }));
                }
            }
            return sqlParam;
        }

        private String toQuestionMarkSql(String markSql, List<Object> rtnParamLst, Map<String, Object> paramMap, Map<String, String> sqlInjectionMap) {
            // -----------------------------------------------------------------------------
            Pattern ptn = Pattern.compile(SQL_PARAM_PTN);
            Matcher mth = ptn.matcher(markSql);
            StringBuffer sb = new StringBuffer();

            while (mth.find()) {
                String col = mth.group(1);
                Object value = paramMap.get(col);

                if (isNotParam(col)) {
                    continue;
                }

                rtnParamLst.add(value);
                String replaceVal = StringUtils.rightPad("?", mth.group().length());

                mth.appendReplacement(sb, replaceVal);
            }
            mth.appendTail(sb);

            // ------------------------------------------------------------------------------
            markSql = sb.toString();
            sb.setLength(0);
            Pattern ptn2 = Pattern.compile("\\_\\#.*?\\#\\_");
            Matcher mth2 = ptn2.matcher(markSql);

            while (mth2.find()) {
                String col = mth2.group(0);
                if (sqlInjectionMap.containsKey(col)) {
                    mth2.appendReplacement(sb, (String) sqlInjectionMap.get(col));
                } else {
                    mth2.appendReplacement(sb, col);
                }
            }
            mth2.appendTail(sb);
            String rtnStr = sb.toString().replaceAll("[\\[\\]]", " ");
            // ------------------------------------------------------------------------------
            return rtnStr;
        }

        public List<Object> processParamMap(Map<String, Object> paramMap, Map<String, String> sqlInjectionMap, Set<String> forceAddColumns) {
            String orginialSqlBackup = getIgnoreCommonentSql(this.orginialSql.toString());
            StringBuffer sb = new StringBuffer();

            List<Object> rtnParamLst = new ArrayList<Object>();

            int tempStartPos = 0;

            for (Pair<List<String>, int[]> row : paramListFix) {
                int[] start_end = row.getRight();

                String markSql = orginialSqlBackup.substring(start_end[0], start_end[1]);
                String replaceToSql_FIX = StringUtils.rightPad("", markSql.length());

                if (isParametersAllOk(row.getLeft(), paramMap, sqlInjectionMap, forceAddColumns) || markSql.matches("\\:\\w+")) {
                    replaceToSql_FIX = this.toQuestionMarkSql(markSql, rtnParamLst, paramMap, sqlInjectionMap);
                }

                sb.append(orginialSqlBackup.substring(tempStartPos, start_end[0]));
                sb.append(replaceToSql_FIX);

                tempStartPos = start_end[1];
            }

            if (tempStartPos != 0) {
                sb.append(orginialSqlBackup.substring(tempStartPos));
            }

            this.questionSql = sb.toString();
            return rtnParamLst;
        }
    }

    /**
     * 讀取sqlId相對的sql
     */
    private void sqlListMouseClicked(MouseEvent e) {
        // if(!JMouseEventUtil.buttonLeftClick(2, e)){
        // return;
        // }
        sqlBean = (SqlIdConfigBean) JListUtil.getLeadSelectionObject(sqlList);
        System.out.println("sqlId : " + sqlBean.getUniqueKey());

        sqlIdText.setText(sqlBean.sqlId);
        // ---------------------------------------
        // sqlTextArea.setText(sqlBean.sql);
        JTextAreaUtil.setText_withoutTriggerChange(sqlTextArea, sqlBean.sql);
        // ---------------------------------------
        sqlIdCategoryComboBox.setSelectedItem(sqlBean.category);
        sqlIdColorComboBox.setSelectedItem(RefSearchColor.valueFrom(sqlBean.color));
        sqlIdCommentArea.setText(sqlBean.sqlComment);

        // 載入參數設定
        sqlParameterConfigLoadHandler.init(sqlBean.getUniqueKey());
        loadParameterTableConfig();

        // 判斷是否要自動切換dataSource
        loadSqlIdMappingDataSourceConfig();

        // trigger 儲存按鈕
        saveSqlButtonClick(false);

        // 設定 tab標題
        changeTabUITitile(sqlBean.sqlId);
    }

    private void changeTabUITitile(String title) {
        TAB_UI1.setTabTitle(null, title);
    }

    private void loadSqlIdMappingDataSourceConfig() {
        try {
            sqlIdListDSMappingHandler.init();
            SqlIdConfigBean bean = (SqlIdConfigBean) JListUtil.getLeadSelectionObject(sqlList);
            if (sqlIdListDSMappingHandler.containsKey(bean.getUniqueKey())) {
                String saveKey = sqlIdListDSMappingHandler.getProperty(bean.getUniqueKey());
                if (!StringUtils.equals(dbNameIdText_getText(), saveKey)) {
                    System.out.println("切換為最後一次成功使用的DS :[" + saveKey + "], ");
                    dbNameIdText_setText(saveKey);
                    /*
                     * Map<String, String> param =
                     * dataSourceConfig.getConfig(saveKey); if
                     * (param.containsKey(PropertiesGroupUtils_ByKey. SAVE_KEYS)
                     * && StringUtils.isNotBlank(param.get(
                     * PropertiesGroupUtils_ByKey.SAVE_KEYS))) {
                     * dbNameIdText_setText(param.get(
                     * PropertiesGroupUtils_ByKey.SAVE_KEYS)); } if
                     * (param.containsKey("url") &&
                     * StringUtils.isNotBlank(param.get("url"))) {
                     * dbUrlText.setText(param.get("url")); } if
                     * (param.containsKey("user") &&
                     * StringUtils.isNotBlank(param.get("user"))) {
                     * dbUserText.setText(param.get("user")); } if
                     * (param.containsKey("pwd")) {// 密碼可以空
                     * dbPwdText.setText(param.get("pwd")); } if
                     * (param.containsKey("driver") &&
                     * StringUtils.isNotBlank(param.get("driver"))) {
                     * dbDriverText.setText(param.get("driver")); }
                     */
                }
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 將設定黨設定到parameterTable
     */
    private void loadParameterTableConfig() {
        // 按順序載入參數
        List<String> paramSet = Collections.emptyList();
        {
            String sql = sqlTextArea.getText().toString();
            SqlParam param = parseSqlToParam(sql);
            paramSet = param.getOrderParametersLst();
        }

        sqlParamCommentArea.setText(sqlParameterConfigLoadHandler.loadComment());
        Map<String, String> paramMap = sqlParameterConfigLoadHandler.loadConfig();
        initParametersTable();
        DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
        for (String col : paramSet) { // paramMap.keySet()
            String val = paramMap.get(col);
            model.addRow(new Object[] { true, col, val, DataType.varchar });
        }
    }

    /**
     * 讀取下一組參數設定
     */
    private void nextParameterBtnClick() {
        if (!sqlParameterConfigLoadHandler.isInitOk()) {
            return;
        }
        sqlParameterConfigLoadHandler.next();
        loadParameterTableConfig();
    }

    /**
     * 下一組連線設定
     */
    private void nextConnBtnClick() {
        try {
            dataSourceConfig.next();
            initDataSourceProperties(null);
        } catch (IOException e) {
            JCommonUtil.handleException(e);
        }
    }

    /**
     * 上一組連線設定
     */
    private void previousConnBtnClick() {
        try {
            dataSourceConfig.previous();
            initDataSourceProperties(null);
        } catch (IOException e) {
            JCommonUtil.handleException(e);
        }
    }

    /**
     * 取得dataSource
     */
    public DataSource getDataSource() {
        String url = dbUrlText.getText();
        String user = dbUserText.getText();
        String pwd = dbPwdText.getText();
        String driver = dbDriverText.getText();
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(url);
        bds.setUsername(user);
        bds.setPassword(pwd);
        bds.setDriverClassName(driver);
        if (externalJDBCDriverJarLoader.get() != null) {
            System.out.println("## use custom class loader");
            externalJDBCDriverJarLoader.get().registerDriver(driver);
            bds.setDriverClassLoader(externalJDBCDriverJarLoader.get().getUrlClassLoader());
        }
        return bds;
    }

    private static void loadExternalJars() {
        File jarDir = PropertiesUtil.getJarCurrentPath(FastDBQueryUI.class);
        if (jarDir.list() == null) {
            return;
        }
        if (externalJDBCDriverJarLoader.get() == null) {
            externalJDBCDriverJarLoader.set(new ExternalJDBCDriverJarLoader());
        }
        ExternalJDBCDriverJarLoader tool = externalJDBCDriverJarLoader.get();
        for (File jar : jarDir.listFiles()) {
            tool.addJar(jar);
        }
    }

    private void queryResultTableMouseClickAction(MouseEvent e) {
        try {
            class StartEditProcess {
                private FastDBQueryUI_CrudDlgUI fastDBQueryUI_CrudDlgUI;
                private FastDBQueryUI_RowCompareDlg fastDBQueryUI_RowCompareDlg;

                String openType = "";

                StartEditProcess() {
                    if (queryList != null && !queryList.getRight().isEmpty() && StringUtils.isBlank(importExcelSheetName)) {
                        openType = "CRUD";
                    } else {
                        openType = "XLS_COMPARE";
                    }
                    if (fastDBQueryUI_CrudDlgUI != null && fastDBQueryUI_CrudDlgUI.isShowing()) {
                        fastDBQueryUI_CrudDlgUI.dispose();
                    }
                    if (fastDBQueryUI_RowCompareDlg != null && fastDBQueryUI_RowCompareDlg.isShowing()) {
                        fastDBQueryUI_RowCompareDlg.disable();
                    }
                }

                // 一般查詢
                void openCRUD() {
                    JTableUtil jutil = JTableUtil.newInstance(queryResultTable);
                    int[] orignRowPosArry = queryResultTable.getSelectedRows();

                    List<Map<String, Object>> rowMapLst = new ArrayList<Map<String, Object>>();
                    for (int orignRowPos : orignRowPosArry) {
                        System.out.println("orignRowPos " + orignRowPos);
                        int rowPos = JTableUtil.getRealRowPos(orignRowPos, queryResultTable);
                        System.out.println("rowPos " + rowPos);

                        int queryLstIndex = transRealRowToQuyerLstIndex(rowPos, queryList.getRight());
                        Map<String, Object> rowMap = getDetailToMap(queryLstIndex);
                        rowMapLst.add(rowMap);
                    }

                    Triple<List<String>, List<Class<?>>, List<Object[]>> allRows = null;
                    if (filterRowsQueryList != null) {
                        allRows = filterRowsQueryList;
                    } else {
                        allRows = queryList;
                    }

                    fastDBQueryUI_CrudDlgUI = FastDBQueryUI_CrudDlgUI.newInstance(rowMapLst, getRandom_TableNSchema(), allRows, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mJDlgHolderBringToFrontHandler.remove((JDialog) e.getSource());
                        }
                    }, FastDBQueryUI.this);
                    mJDlgHolderBringToFrontHandler.add(fastDBQueryUI_CrudDlgUI);
                }

                void openXLS_COMPARE() throws SQLException, Exception {
                    // 如果是用 excel 匯入 使用excel資料開啟
                    String shemaTable = JCommonUtil._jOptionPane_showInputDialog("請輸\"資料表名稱\",格視為 : Schema.TableName", importExcelSheetName);
                    if (StringUtils.isBlank(shemaTable)) {
                        Validate.isTrue(false, "查詢結果為空!");
                    }

                    Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult = JdbcDBUtil.queryForList_customColumns(//
                            String.format(" select * from %s where 1=1 ", shemaTable), //
                            new Object[0], getDataSource().getConnection(), true, 1);

                    Pair<List<String>, List<Object[]>> excelImportLst = transRealRowToQuyerLstIndex(orignQueryResult);

                    int selectRowIndex = queryResultTable.getSelectedRow();

                    fastDBQueryUI_RowCompareDlg = FastDBQueryUI_RowCompareDlg.newInstance(shemaTable, selectRowIndex, excelImportLst, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mJDlgHolderBringToFrontHandler.remove((JDialog) e.getSource());
                        }
                    }, FastDBQueryUI.this);
                    mJDlgHolderBringToFrontHandler.add(fastDBQueryUI_RowCompareDlg);
                }

                void start() throws Exception {
                    if ("CRUD".equals(openType)) {
                        openCRUD();
                    } else if ("XLS_COMPARE".equals(openType)) {
                        openXLS_COMPARE();
                    }
                }
            }

            final StartEditProcess d = new StartEditProcess();

            if (JMouseEventUtil.buttonLeftClick(2, e)) {
                // d.start();
                new JMenuItem_BasicMenu().run();
            }

            if (JMouseEventUtil.buttonRightClick(1, e)) {
                JPopupMenuUtil ppap = JPopupMenuUtil.newInstance(queryResultTable);

                ppap.addJMenuItem(new JMenuItem_BasicMenu().getItem());//

                ppap.addJMenuItem("選擇此列", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int[] rows = JTableUtil.newInstance(queryResultTable).getSelectedRows(false);
                        for (int row : rows) {
                            JToggleButton b = (JToggleButton) JTableUtil.newInstance(queryResultTable).getValueAt(true, row, 0);
                            b.setSelected(!b.isSelected());
                        }

                        JTableUtil.newInstance(queryResultTable).setRowSelection();
                    }
                });//

                addKeepSelectionOnly(ppap);

                ppap.addJMenuItem("進行CRUD操作", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            d.openCRUD();
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });//

                ppap.addJMenuItem("進行比對操作", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            d.openXLS_COMPARE();
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });//

                ppap.addJMenuItem(addBase64Menus())//
                        .applyEvent(e)//
                        .show();
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void addKeepSelectionOnly(JPopupMenuUtil ppap) {
        try {
            class SelectRowIdxProc {
                void execute(final int[] selectRowIdxArry) {
                    List<Object[]> sourceLst = queryList.getRight();
                    if (filterRowsQueryList != null) {
                        sourceLst = filterRowsQueryList.getRight();
                    }
                    List<Object[]> newLst = new ArrayList<Object[]>();
                    for (int ii = 0; ii < selectRowIdxArry.length; ii++) {
                        int queryListIdx = transRealRowToQuyerLstIndex(selectRowIdxArry[ii], sourceLst);
                        newLst.add(sourceLst.get(queryListIdx));
                    }
                    Triple<List<String>, List<Class<?>>, List<Object[]>> newLstForChoice = Triple.of(queryList.getLeft(), queryList.getMiddle(), newLst);
                    queryModeProcess(newLstForChoice, true, null, null);//
                    filterRowsQueryList = newLstForChoice;
                    isResetQuery = false;
                }
            }

            final int[] selectRowIdxArry = JTableUtil.newInstance(queryResultTable).getSelectedRows(true);
            final int[] selectColIdxArry = JTableUtil.newInstance(queryResultTable).getSelectedColumns(true);
            JMenuAppender chdMenu = JMenuAppender.newInstance("保留已選欄/列");
            chdMenu.addMenuItem("只保留已選列 :" + selectRowIdxArry.length, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new SelectRowIdxProc().execute(selectRowIdxArry);
                }
            });
            chdMenu.addMenuItem("只保留已選欄 :" + selectColIdxArry.length, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<Object> colLst = JTableUtil.newInstance(queryResultTable).getColumnTitleArray();
                    List<String> strLst = new ArrayList<String>();
                    for (int jj = 0; jj < selectColIdxArry.length; jj++) {
                        strLst.add(String.valueOf(colLst.get(selectColIdxArry[jj])));
                    }
                    columnFilterText.setText(StringUtils.join(strLst, "^"));
                }
            });
            chdMenu.addMenuItem("*只保留已「勾」選列*", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<Integer> rowLst = new ArrayList<Integer>();
                    for (int ii = 0; ii < queryResultTable.getRowCount(); ii++) {
                        int rowIdx = JTableUtil.getRealRowPos(ii, queryResultTable);
                        Object v = JTableUtil.newInstance(queryResultTable).getValueAt(false, rowIdx, 0);
                        if (v instanceof JToggleButton && ((JToggleButton) v).isSelected()) {
                            rowLst.add(rowIdx);
                        }
                    }
                    new SelectRowIdxProc().execute(ArrayUtils.toPrimitive(rowLst.toArray(new Integer[0])));
                }
            });
            ppap.addJMenuItem(chdMenu.getMenu());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class JMenuItem_BasicMenu {
        SimpleTextDlg mSimpleTextDlg = null;
        JMenuItem item;

        JMenuItem_BasicMenu() {
            mSimpleTextDlg = new SimpleTextDlg(JTableUtil.newInstance(queryResultTable).getSelectedValue(), "", null);
            item = new JMenuItem("此資料長度 : " + mSimpleTextDlg.getMessage().getBytes().length);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    run();
                }
            });
        }

        JMenuItem getItem() {
            return item;
        }

        void run() {
            mSimpleTextDlg.show();
        }
    }

    private JMenu addBase64Menus() {
        JMenuAppender chdMenu = JMenuAppender.newInstance("Base64");
        chdMenu.addMenuItem("Encode", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object val = JTableUtil.newInstance(queryResultTable).getSelectedValue();
                if (val != null) {
                    String strVal = String.valueOf(val);
                    String decodeVal = Base64JdkUtil.encode(strVal);
                    JCommonUtil._jOptionPane_showInputDialog("Base64Encode:" + strVal, decodeVal);
                }
            }
        });
        chdMenu.addMenuItem("Decode", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object val = JTableUtil.newInstance(queryResultTable).getSelectedValue();
                if (val != null) {
                    String strVal = String.valueOf(val);
                    String decodeVal = Base64JdkUtil.decodeToString(strVal);
                    JCommonUtil._jOptionPane_showInputDialog("Base64Decode:" + strVal, decodeVal);
                }
            }
        });
        return chdMenu.getMenu();
    }

    private String getRandom_TableNSchema() {
        Pattern ptn = Pattern.compile("from\\s+(\\w+[\\.\\w]+|\\w+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        String sql = StringUtils.trimToEmpty(getCurrentSQL());
        Matcher mth = ptn.matcher(sql);
        if (mth.find()) {
            return mth.group(1);
        }
        return "";
    }

    private boolean isColumnNoExists() {
        JTableUtil util = JTableUtil.newInstance(queryResultTable);
        if (QUERY_RESULT_COLUMN_NO.equals(String.valueOf(util.getColumnTitleArray().get(0)))) {
            return true;
        }
        return false;
    }

    private Pair<List<String>, List<Object[]>> transRealRowToQuyerLstIndex(Triple<List<String>, List<Class<?>>, List<Object[]>> excelImportLst) {
        TreeMap<Integer, String> columnMapping = getQueryResult_ColumnDefine();
        List<String> leftLst = new ArrayList<String>(columnMapping.values());
        JTableUtil util = JTableUtil.newInstance(queryResultTable);
        boolean removeNoColumn = isColumnNoExists();
        // 如果是使用 excel 匯入 需要重組 資料
        List<Object[]> rightRowsLit = new ArrayList<Object[]>();
        for (int row = 0; row < queryResultTable.getRowCount(); row++) {
            TreeMap<Integer, Object> map = new TreeMap<Integer, Object>();
            A: for (int col = 0; col < queryResultTable.getColumnCount(); col++) {
                int realCol = util.getRealColumnPos(col, queryResultTable);
                int realRow = util.getRealRowPos(row, queryResultTable);
                Object value = util.getModel().getValueAt(realRow, realCol);

                if (removeNoColumn && realCol == 0) {
                    continue A;
                }
                map.put(realCol, value);
            }
            rightRowsLit.add(map.values().toArray());
        }
        return Pair.of(leftLst, rightRowsLit);
    }

    private TreeMap<Integer, String> getQueryResult_ColumnDefine() {
        TreeMap<Integer, String> columnMapping = new TreeMap<Integer, String>();
        JTableUtil util = JTableUtil.newInstance(queryResultTable);

        for (int ii = 0; ii < queryResultTable.getColumnCount(); ii++) {
            TableColumn column = queryResultTable.getTableHeader().getColumnModel().getColumn(ii);
            String columnHeader = (String) column.getHeaderValue();

            if (this.queryList != null && !this.queryList.getLeft().isEmpty()) {
                for (int jj = 0; jj < this.queryList.getLeft().size(); jj++) {
                    String columnHeader2 = this.queryList.getLeft().get(jj);
                    if (!columnMapping.containsKey(jj) && columnHeader.equalsIgnoreCase(columnHeader2)) {
                        columnMapping.put(jj, columnHeader2);
                    }
                }
            } else {
                columnMapping.put(ii, columnHeader);
            }
        }
        System.out.println(columnMapping);
        return columnMapping;
    }

    private int transRealRowToQuyerLstIndex(int realRow, List<Object[]> sourceLst) {
        JTableUtil util = JTableUtil.newInstance(queryResultTable);
        TreeMap<Integer, String> columnMapping = getQueryResult_ColumnDefine();

        // 如果是使用 excel 匯入 需要重組 資料
        TreeMap<Integer, Object> map = new TreeMap<Integer, Object>();
        for (int col = 0; col < queryResultTable.getColumnCount(); col++) {
            int realCol = util.getRealColumnPos(col, queryResultTable);
            Object value = util.getModel().getValueAt(realRow, realCol);
            map.put(realCol, value);
        }
        // 移除按第一個鈕欄
        if (isColumnNoExists()) {
            map.remove(0);
        }

        // 用來比較取得row index用
        List<Object[]> newLst = new ArrayList<Object[]>();
        for (Object[] oldArry : sourceLst) {
            List<Object> newArry = new ArrayList<Object>();
            for (int columnPos : columnMapping.keySet()) {
                newArry.add(oldArry[columnPos]);
            }
            newLst.add(newArry.toArray());
        }

        Object[] arry = map.values().toArray();
        return isContainObjectArray_Index(newLst, arry);
    }

    private Map<String, Object> getDetailToMap(int queryListIndex) {
        List<String> columns = queryList.getLeft();
        Object[] row = queryList.getRight().get(queryListIndex);

        Map<String, List<Object>> multiMap = new LinkedHashMap<String, List<Object>>();
        for (int ii = 0; ii < columns.size(); ii++) {
            String col = columns.get(ii);
            Object val = row[ii];
            List<Object> valueLst = new ArrayList<Object>();
            if (multiMap.containsKey(col)) {
                valueLst = multiMap.get(col);
            }
            valueLst.add(val);
            multiMap.put(col, valueLst);
        }

        Map<String, Object> rtnMap = new LinkedHashMap<String, Object>();
        for (String col : multiMap.keySet()) {
            List<Object> valueLst = multiMap.get(col);
            if (valueLst.size() == 1) {
                rtnMap.put(col, valueLst.get(0));
            } else {
                if (!ListUtil.isAllEquals(valueLst)) {
                    Object value = JCommonUtil._JOptionPane_showInputDialog("此欄位[" + col + "]顯示多次,請選擇正確的值:", col, valueLst.toArray(), valueLst.get(0));
                    rtnMap.put(col, value);
                } else {
                    rtnMap.put(col, valueLst.get(0));
                }
            }
        }
        return rtnMap;
    }

    private int getColumnIndicateIndex(int columnLstIdx, List<String> columns, String column) {
        for (int ii = 0; ii < columns.size(); ii++) {
            if (StringUtils.equalsIgnoreCase(columns.get(ii), column)) {
                return ii;
            }
        }
        return -1;
    }

    private void excelExportBtnAction() {
        try {
            ExcelUtil_Xls97 exlUtl = ExcelUtil_Xls97.getInstance();
            AbstractButton selBtn = JButtonGroupUtil.getSelectedButton(btnExcelBtn);
            if (radio_import_excel == selBtn) {
                File xlsfile = JCommonUtil._jFileChooser_selectFileOnly();
                if (!xlsfile.exists() || !xlsfile.getName().endsWith(".xls")) {
                    JCommonUtil._jOptionPane_showMessageDialog_info("檔案錯誤(.xls)!");
                    return;
                }

                // 選擇sheet
                HSSFWorkbook wk = exlUtl.readExcel(xlsfile);
                List<String> shLst = new ArrayList<String>();
                for (int ii = 0; ii < wk.getNumberOfSheets(); ii++) {
                    HSSFSheet sh = wk.getSheetAt(ii);
                    shLst.add(sh.getSheetName());
                }
                importExcelSheetName = (String) JCommonUtil._JOptionPane_showInputDialog(//
                        "請選擇sheet,共[" + shLst.size() + "]個", "選擇sheet", shLst.toArray(), shLst.get(0));
                if (StringUtils.isBlank(importExcelSheetName)) {
                    JCommonUtil._jOptionPane_showMessageDialog_info("sheetname 錯誤!");
                    return;
                }

                HSSFSheet sheet = wk.getSheet(importExcelSheetName);

                DefaultTableModel model = null;
                for (int ii = 0; ii <= 0; ii++) {
                    Row row = sheet.getRow(ii);
                    List<Object> titles = new ArrayList<Object>();
                    for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                        String value = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));
                        titles.add(value);
                    }
                    model = JTableUtil.createModel(true, titles.toArray());
                    queryResultTable.setModel(model);
                    JTableUtil.newInstance(queryResultTable).setRowHeightByFontSize();
                }

                for (int ii = 1; ii <= sheet.getLastRowNum(); ii++) {
                    Row row = sheet.getRow(ii);
                    if (row == null) {
                        continue;
                    }
                    List<Object> rows = new ArrayList<Object>();
                    for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                        String value = ExcelUtil_Xls97.getInstance().readCell(row.getCell(jj));
                        rows.add(value);
                    }
                    model.addRow(rows.toArray());
                }
            } else if (radio_export_excel == selBtn) {
                Triple<List<String>, List<Class<?>>, List<Object[]>> tmpQueryList = null;
                if (filterRowsQueryList != null && !isResetQuery) {
                    tmpQueryList = filterRowsQueryList;
                } else if (queryList != null) {
                    tmpQueryList = queryList;
                }

                if (tmpQueryList == null || tmpQueryList.getRight().isEmpty()) {
                    JCommonUtil._jOptionPane_showMessageDialog_info("沒有資料!");
                    return;
                }

                List<String> columnLst = new ArrayList<String>();
                if (StringUtils.isNotBlank(columnFilterText.getText())) {
                    List<Object> lst = JTableUtil.newInstance(queryResultTable).getColumnTitleArray();
                    for (Object v : lst) {
                        String name = String.valueOf(v);
                        if (QUERY_RESULT_COLUMN_NO.equals(name)) {
                            continue;
                        }
                        columnLst.add(name);
                    }
                }

                HSSFWorkbook wk = new HSSFWorkbook();
                HSSFSheet sheet0 = wk.createSheet("string value sheet");
                HSSFSheet sheet1 = wk.createSheet("orign value sheet");
                HSSFSheet sheet2 = wk.createSheet("sql");

                // 寫sql
                exlUtl.getCellChk(exlUtl.getRowChk(sheet2, 0), 0).setCellValue(StringUtils.trimToEmpty(sqlTextArea.getText()));
                JTableUtil paramUtl = JTableUtil.newInstance(parametersTable);
                int sqlRowPos = 2;
                for (int ii = 0; ii < paramUtl.getModel().getRowCount(); ii++) {
                    int col1 = JTableUtil.getRealColumnPos(ParameterTableColumnDef.COLUMN.idx, parametersTable);
                    int val1 = JTableUtil.getRealColumnPos(ParameterTableColumnDef.VALUE.idx, parametersTable);
                    Object col = paramUtl.getRealValueAt(JTableUtil.getRealRowPos(ii, parametersTable), col1);
                    Object val = paramUtl.getRealValueAt(JTableUtil.getRealRowPos(ii, parametersTable), val1);

                    exlUtl.getCellChk(exlUtl.getRowChk(sheet2, sqlRowPos), 0).setCellValue(String.valueOf(col));
                    exlUtl.getCellChk(exlUtl.getRowChk(sheet2, sqlRowPos), 1).setCellValue(String.valueOf(val));
                    sqlRowPos++;
                }

                // 寫資料
                CellStyleHandler titleCs = ExcelWriter.CellStyleHandler.newInstance(wk.createCellStyle())//
                        .setForegroundColor(new HSSFColor.LIGHT_GREEN());
                List<String> columns = new ArrayList<String>(tmpQueryList.getLeft());
                HSSFRow titleRow0 = sheet0.createRow(0);
                HSSFRow titleRow1 = sheet1.createRow(0);
                if (columnLst.isEmpty()) {
                    for (int ii = 0; ii < columns.size(); ii++) {
                        exlUtl.setCellValue(exlUtl.getCellChk(titleRow0, ii), columns.get(ii));
                        titleCs.applyStyle(exlUtl.getCellChk(titleRow0, ii));
                    }
                    for (int ii = 0; ii < columns.size(); ii++) {
                        exlUtl.setCellValue(exlUtl.getCellChk(titleRow1, ii), columns.get(ii));
                        titleCs.applyStyle(exlUtl.getCellChk(titleRow1, ii));
                    }
                } else {
                    for (int ii = 0; ii < columnLst.size(); ii++) {
                        exlUtl.setCellValue(exlUtl.getCellChk(titleRow0, ii), columnLst.get(ii));
                        titleCs.applyStyle(exlUtl.getCellChk(titleRow0, ii));
                    }
                    for (int ii = 0; ii < columnLst.size(); ii++) {
                        exlUtl.setCellValue(exlUtl.getCellChk(titleRow1, ii), columnLst.get(ii));
                        titleCs.applyStyle(exlUtl.getCellChk(titleRow1, ii));
                    }
                }

                if (columnLst.isEmpty()) {
                    for (int ii = 0; ii < tmpQueryList.getRight().size(); ii++) {
                        Row row_string = sheet0.createRow(ii + 1);
                        Row row_orign$ = sheet1.createRow(ii + 1);

                        Object[] rows = tmpQueryList.getRight().get(ii);
                        for (int jj = 0; jj < columns.size(); jj++) {
                            String col = columns.get(jj);
                            Object value = rows[jj];

                            exlUtl.setCellValue(exlUtl.getCellChk(row_string, jj), String.valueOf(value));
                            exlUtl.setCellValue(exlUtl.getCellChk(row_orign$, jj), value);
                        }
                    }
                } else {
                    for (int ii = 0; ii < tmpQueryList.getRight().size(); ii++) {
                        Row row_string = sheet0.createRow(ii + 1);
                        Row row_orign$ = sheet1.createRow(ii + 1);

                        Object[] rows = tmpQueryList.getRight().get(ii);
                        for (int jj = 0; jj < columnLst.size(); jj++) {
                            String col = columnLst.get(jj);

                            int newIdx = getColumnIndicateIndex(jj, columns, col);
                            Object value = rows[newIdx];

                            exlUtl.setCellValue(exlUtl.getCellChk(row_string, jj), String.valueOf(value));
                            exlUtl.setCellValue(exlUtl.getCellChk(row_orign$, jj), value);
                        }
                    }
                }

                exlUtl.autoCellSize(sheet0);
                exlUtl.autoCellSize(sheet1);
                exlUtl.autoCellSize(sheet2);

                String filename = FastDBQueryUI.class.getSimpleName() + //
                        "_Export_" + //
                        "_" + StringUtils.trimToEmpty(sqlIdText.getText()) + "_" + //
                        DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + //
                        ".xls";
                filename = JCommonUtil._jOptionPane_showInputDialog("儲存檔案", filename);
                if (StringUtils.isNotBlank(filename) || !filename.endsWith(".xls")) {
                    File exportFile = new File(FileUtil.DESKTOP_DIR, filename);
                    exlUtl.writeExcel(exportFile, wk);
                    if (exportFile.exists()) {
                        JCommonUtil._jOptionPane_showMessageDialog_info("匯出成功!");
                    }
                } else {
                    JCommonUtil._jOptionPane_showMessageDialog_info("檔名有誤!");
                }
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void removeConnectionBtnAction() {
        try {
            String dbNameId = dbNameIdText_getText();
            boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要刪除:" + dbNameId, "刪除設定");
            if (confirm) {
                dataSourceConfig.removeConfig(dbNameId);
                JCommonUtil._jOptionPane_showMessageDialog_info("刪除成功! : " + dbNameId);
                reload_DataSourceConfig_autoComplete();
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    protected void distinctQueryBtnAction() {
        try {
            distinctHasClicked = true;

            TreeMap<Integer, String> columnMapping = new TreeMap<Integer, String>();
            JTableUtil util = JTableUtil.newInstance(queryResultTable);

            for (int ii = 0; ii < queryResultTable.getColumnCount(); ii++) {
                TableColumn column = queryResultTable.getTableHeader().getColumnModel().getColumn(ii);
                int pos2 = util.getRealColumnPos(ii, queryResultTable);
                // System.out.println(column.getHeaderValue() + " - " + ii + " -
                // " + pos2);
                columnMapping.put(pos2, (String) column.getHeaderValue());
            }
            System.out.println(columnMapping);
            List<Object[]> queryLst = new ArrayList<Object[]>();
            for (int row = 0; row < util.getModel().getRowCount(); row++) {
                TreeMap<Integer, Object> map = new TreeMap<Integer, Object>();
                for (int col = 0; col < queryResultTable.getColumnCount(); col++) {
                    int realCol = util.getRealColumnPos(col, queryResultTable);
                    int realRow = util.getRealRowPos(row, queryResultTable);
                    Object value = util.getModel().getValueAt(realRow, realCol);
                    map.put(realCol, value);
                }
                Object[] arry = map.values().toArray();
                if (isContainObjectArray_Index(queryLst, arry) == -1) {
                    queryLst.add(arry);
                }
            }

            List<String> matchColumnLst = new ArrayList<String>(columnMapping.values());
            Triple<List<String>, List<Class<?>>, List<Object[]>> queryResultFinal = fixPairToTripleQueryResult(Pair.of(matchColumnLst, queryLst));
            this.queryModeProcess(queryResultFinal, true, null, null);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private int isContainObjectArray_Index(List<Object[]> allLst, Object[] arry) {
        String compareTo = Arrays.toString(arry);
        System.out.println("[isContainObjectArray_Index] compareTo - \t" + compareTo);
        for (int ii = 0; ii < allLst.size(); ii++) {
            Object[] arry1 = allLst.get(ii);
            String compareFrom = Arrays.toString(arry1);
            if (compareFrom.equals(compareTo)) {
                System.out.println("[isContainObjectArray_Index] ArryIndex[" + ii + "] - \t" + compareFrom);
                return ii;
            }
        }
        return -1;
    }

    private void sqlTextAreaMouseClickedAction(MouseEvent e) {
        if (JMouseEventUtil.buttonRightClick(1, e)) {
            JPopupMenuUtil.newInstance(sqlTextArea)//
                    .addJMenuItem("SQL 格式化", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String sql = StringUtils.defaultString(sqlTextArea.getText());
                            sql = getSqlFormater(sql);
                            sql = getSelectColumnFormater(sql);
                            sqlTextArea.setText(sql);
                        }

                        Pattern ptn = Pattern.compile("(\\[.*?\\]|\\swhere|\\sand|\\sor|\\sfrom|\\sunion|\\souter\\s+join|\\sinner\\s+join|\\sleft\\s+join|\\sright\\s+join|\\sjoin|\\son)",
                                Pattern.CASE_INSENSITIVE);
                        Pattern ptn2 = Pattern.compile("select\\s+((?:.|\n)*?)from\\s+.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

                        private String getSqlFormater(String sql) {
                            List<String> lst = StringUtil_.readContentToList(sql, true, false, false);
                            sql = StringUtils.join(lst, "  ");
                            StringBuffer sb = new StringBuffer();
                            Matcher mth = ptn.matcher(sql);
                            while (mth.find()) {
                                mth.appendReplacement(sb, "\r\n" + mth.group(1));
                            }
                            mth.appendTail(sb);
                            return sb.toString();
                        }

                        private String getSelectColumnFormater(String sql) {
                            StringBuffer sb = new StringBuffer();
                            Matcher mth = ptn2.matcher(sql);
                            int startPos = 0;
                            if (mth.find()) {
                                sb.append(sql.substring(startPos, mth.start(1)));
                                startPos = mth.end(1);
                                String selectDesc = mth.group(1);
                                selectDesc = selectDesc.replaceAll(",", ",\r\n    ");
                                sb.append(selectDesc);
                            }
                            sb.append(sql.substring(startPos));
                            return sb.toString();
                        }
                    })//
                    .addJMenuItem("SQL 基礎 Select", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String tableName = sqlTextArea.getSelectedText();
                            if (StringUtils.isBlank(tableName)) {
                                tableName = "TABLE_NAME";
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("\t\r\n");
                            sb.append("\t\r\n");
                            sb.append("\tselect t.* \r\n");
                            sb.append("\tfrom ").append(tableName).append(" t \r\n");
                            sb.append("\twhere 1=1 \r\n");
                            sb.append("\t\r\n");
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());
                            sqlTextArea.setText(prefix + sb + suffix);
                        }
                    })//
                    .addJMenuItem("SQL 基礎 Update", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String tableName = sqlTextArea.getSelectedText();
                            if (StringUtils.isBlank(tableName)) {
                                tableName = "TABLE_NAME";
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append("\t\r\n");
                            sb.append("\t\r\n");
                            sb.append("\tupdate ").append(tableName).append(" t \r\n");
                            sb.append("\tset t.AAAAAAA = 'xxxxxx' ").append(" t \r\n");
                            sb.append("\twhere t.AAAAAAA = 'xxxxxx' \r\n");
                            sb.append("\t\r\n");
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());
                            sqlTextArea.setText(prefix + sb + suffix);
                        }
                    })//
                    .addJMenuItem("SQL 基礎 left join", new ActionListener() {

                        private String getRandomAlias() {
                            char a = (char) RandomUtil.rangeInteger((int) 'a', (int) 'z');
                            int b = RandomUtil.rangeInteger(0, 9);
                            return String.valueOf(a) + String.valueOf(b);
                        }

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selection = sqlTextArea.getSelectedText();
                            if (StringUtils.isBlank(selection)) {
                                return;
                            }
                            StringBuilder sb = new StringBuilder();
                            Pattern ptn = Pattern.compile("[\\w+\\.]+");
                            Matcher mth = ptn.matcher(selection);
                            while (mth.find()) {
                                String cond = mth.group();
                                String alais = getRandomAlias();
                                sb.append("\t  left join " + cond + " " + alais + " on "+ alais + ".XXXXXXXX = t.XXXXXXXX "//
                                        + " and "+ alais + ".YYYYYYYY = t.YYYYYYYY "//
                                        + " \r\n");//
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());
                            sqlTextArea.setText(prefix + sb + suffix);
                        }
                    })//
                    .addJMenuItem("SQL 基礎 Where[硬]", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selection = sqlTextArea.getSelectedText();
                            if (StringUtils.isBlank(selection)) {
                                return;
                            }
                            StringBuilder sb = new StringBuilder();
                            Pattern ptn = Pattern.compile("[\\w+\\.]+");
                            Matcher mth = ptn.matcher(selection);
                            while (mth.find()) {
                                String cond = mth.group();
                                sb.append("\t   and ").append(cond).append(" = 'XXXXXXXXXX'   \r\n");
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());
                            sqlTextArea.setText(prefix + sb + suffix);
                        }
                    })//
                    .addJMenuItem("SQL 基礎 Where[代]", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String selection = sqlTextArea.getSelectedText();
                            if (StringUtils.isBlank(selection)) {
                                return;
                            }
                            StringBuilder sb = new StringBuilder();
                            Pattern ptn = Pattern.compile("[\\w+\\.]+");
                            Matcher mth = ptn.matcher(selection);
                            while (mth.find()) {
                                String cond = mth.group();
                                sb.append("\t [  and ").append(cond).append(" = :").append(cond).append("  ] \r\n");
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());
                            sqlTextArea.setText(prefix + sb + suffix);
                        }
                    })//
                    .addJMenuItem("插入系統日", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String column = sqlTextArea.getSelectedText();

                            DBDateFormat e2 = (DBDateFormat) JCommonUtil._JOptionPane_showInputDialog("請選擇日期格式", "日期格式化", DBDateFormat.values(), null);
                            if (e2 == null) {
                                return;
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());

                            column = e2.sysdate();

                            sqlTextArea.setText(prefix + column + suffix);
                        }
                    })//
                    .addJMenuItem("Date 改為字串", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String column = sqlTextArea.getSelectedText();

                            DBDateFormat e2 = (DBDateFormat) JCommonUtil._JOptionPane_showInputDialog("請選擇日期格式", "日期格式化", DBDateFormat.values(), null);
                            if (e2 == null) {
                                return;
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());

                            column = e2.date2Varchar(column);

                            sqlTextArea.setText(prefix + column + suffix);
                        }
                    })//
                    .addJMenuItem("Timestamp 改為字串", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String column = sqlTextArea.getSelectedText();

                            DBDateFormat e2 = (DBDateFormat) JCommonUtil._JOptionPane_showInputDialog("請選擇日期格式", "日期格式化", DBDateFormat.values(), null);
                            if (e2 == null) {
                                return;
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());

                            column = e2.timestamp2Varchar(column);

                            sqlTextArea.setText(prefix + column + suffix);
                        }
                    })//
                    .addJMenuItem("字串改為 Date", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String column = sqlTextArea.getSelectedText();

                            DBDateFormat e2 = (DBDateFormat) JCommonUtil._JOptionPane_showInputDialog("請選擇日期格式", "日期格式化", DBDateFormat.values(), null);
                            if (e2 == null) {
                                return;
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());

                            column = e2.varchar2Date(column);

                            sqlTextArea.setText(prefix + column + suffix);
                        }
                    })//
                    .addJMenuItem("字串改為 Timestamp", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String column = sqlTextArea.getSelectedText();

                            DBDateFormat e2 = (DBDateFormat) JCommonUtil._JOptionPane_showInputDialog("請選擇日期格式", "日期格式化", DBDateFormat.values(), null);
                            if (e2 == null) {
                                return;
                            }
                            String prefix = StringUtils.substring(sqlTextArea.getText(), 0, sqlTextArea.getSelectionStart());
                            String suffix = StringUtils.substring(sqlTextArea.getText(), sqlTextArea.getSelectionEnd());

                            column = e2.varchar2Timestamp(column);

                            sqlTextArea.setText(prefix + column + suffix);
                        }
                    })//
                    .applyEvent(e)//
                    .show();
        }
    }

    private void deleteSqlIdConfigBean(SqlIdConfigBean sqlBean) {
        String sql = sqlBean.sql;

        boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("刪除 : " + sqlBean.getUniqueKey() + "\nSQL : " + sql, "是否刪除 : " + sqlBean.getUniqueKey());
        if (deleteConfirm) {

            // 刪除參數黨
            File paramFile = new File(JAR_PATH_FILE, "param_" + sqlBean.getUniqueKey() + ".properties");
            if (paramFile.exists()) {
                paramFile.delete();
            }

            // 刪除sqlId
            if (!paramFile.exists()) {
                sqlIdConfigBeanHandler.remove(sqlBean);

                JListUtil.removeElement(sqlList, sqlBean);

                // 移除db config mapping
                sqlIdListDSMappingHandler.remove(sqlBean.getUniqueKey());
            }

            // 刪除sqlIdColumn設定
            mSqlIdColumnHolder.remove(mSqlIdColumnHolder.getSqlId());

            JCommonUtil._jOptionPane_showMessageDialog_info("刪除" + (!paramFile.exists() ? "成功" : "失敗"));

            try {
                initLoadSqlListConfig();
            } catch (IOException e) {
                JCommonUtil.handleException(e);
            }
        }
    }

    private void sqlListKeyPressAction(KeyEvent evt) {
        try {
            JListUtil.newInstance(sqlList).defaultJListKeyPressed(evt, false);
            // 刪除
            System.out.println("click del key : " + (evt.getKeyCode() == 127));
            if (evt.getKeyCode() == 127) {
                SqlIdConfigBean sqlBean = JListUtil.getLeadSelectionObject(sqlList);
                this.deleteSqlIdConfigBean(sqlBean);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    public JFrameRGBColorPanel getjFrameRGBColorPanel() {
        return jFrameRGBColorPanel.get();
    }

    private void connTestBtnAction() {
        Connection conn = null;
        try {
            conn = this.getDataSource().getConnection();
            JCommonUtil._jOptionPane_showMessageDialog_info("連線成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException("測試連線失敗 : " + ex.getMessage(), ex);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    private class SqlIdConfigBeanHandler {
        Properties sqlIdListProp;
        List<SqlIdConfigBean> lst = new ArrayList<SqlIdConfigBean>();

        private SqlIdConfigBeanHandler() {
            init("");
        }

        public void remove(SqlIdConfigBean sqlBean) {
            init("");
            boolean removeOk = lst.remove(sqlBean);
            System.out.println("removeOk = " + removeOk);
            store();
        }

        public void save(SqlIdConfigBean b) {
            b.validate();
            init("");
            if (lst.contains(b)) {
                SqlIdConfigBean b2 = lst.get(lst.indexOf(b));
                b2.category = b.category;
                b2.color = b.color;
                b2.sql = b.sql;
                b2.sqlId = b.sqlId;
                b2.sqlComment = b.sqlComment;
            } else {
                lst.add(b);
            }
            store();
            init(b.category);
        }

        private void saveYamlToProp(File yamlFile, boolean replaceCurrentConfigFile) {
            File propFile = new File(FileUtil.DESKTOP_DIR, "sqlList.properties");
            if (propFile.exists()) {
                if (!JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("檔案已存在於桌面 sqlList.properties, 是否要覆蓋?", "是否要覆蓋?")) {
                    return;
                }
            }
            Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
            List<SqlIdConfigBean> lst = YamlMapUtil.getInstance().loadFromFile(yamlFile, SqlIdConfigBean.class, classMap);
            Properties prop = new Properties();
            for (SqlIdConfigBean bean : lst) {
                prop.setProperty(bean.getKey(), bean.getValue());
            }
            PropertiesUtil.storeProperties(prop, propFile, "");
            if (replaceCurrentConfigFile) {
                sqlIdListFile = propFile;
                sqlIdListProp = prop;
                init("");
            }
        }

        private void store() {
            sqlIdListProp.clear();
            for (SqlIdConfigBean bean : lst) {
                sqlIdListProp.setProperty(bean.getKey(), bean.getValue());
            }
            PropertiesUtil.storeProperties(sqlIdListProp, sqlIdListFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd-HHmmss"));
        }

        private void init(String category) {
            if (!sqlIdListFile.exists()) {
                try {
                    sqlIdListFile.createNewFile();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            lst.clear();
            sqlIdListProp = PropertiesUtil.loadProperties(sqlIdListFile, null, false);
            Set<String> categoryLst = new TreeSet<String>();
            for (Enumeration<Object> enu = sqlIdListProp.keys(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                String value = sqlIdListProp.getProperty(key);
                SqlIdConfigBean bean = SqlIdConfigBean.of(key, value);
                lst.add(bean);
                if (!categoryLst.contains(bean.category)) {
                    categoryLst.add(bean.category);
                }
            }
            ListUtil.sortIgnoreCase(lst);
            sqlIdCategoryComboBox_Auto.applyComboxBoxList(new ArrayList<String>(categoryLst), category);
        }
    }

    public static class SqlIdConfigBean {
        private static String[] KEYS_DEF = new String[] { "color", "category", "sqlId", "sqlComment" };
        private static String[] VALUES_DEF = new String[] { "sql" };

        String color;
        String category;
        String sqlId;
        String sql;
        String sqlComment;

        public String getCategory() {
            return category;
        }

        public String getSqlId() {
            return sqlId;
        }

        public String getSql() {
            return sql;
        }

        private String getUniqueKey() {
            String prefix = "";
            if (StringUtils.isNotBlank(category)) {
                prefix = StringUtils.trimToEmpty(category) + "_";
            }
            return prefix + StringUtils.trimToEmpty(sqlId);
        }

        public static SqlIdConfigBean of(String key, String value) {
            return PropertiesMultiUtil.of(key, value, SqlIdConfigBean.class);
        }

        private String getKey() {
            return PropertiesMultiUtil.getKey(this);
        }

        private String getValue() {
            return PropertiesMultiUtil.getValue(this);
        }

        private void validate() {
            if (StringUtils.isBlank(sqlId) || StringUtils.isBlank(sql)) {
                Validate.isTrue(false, "sqlId, sql 不可為空!");
            }
            if (!FileUtil.validatePath(sqlId, false)) {
                Validate.isTrue(false, "sqlId 不可含有特殊字元 [\\/:*?\"<>|]");
            }
            if (!FileUtil.validatePath(category, false)) {
                Validate.isTrue(false, "category 不可含有特殊字元 [\\/:*?\"<>|]");
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SqlIdConfigBean other = (SqlIdConfigBean) obj;
            if (category == null) {
                if (other.category != null)
                    return false;
            } else if (!category.equals(other.category))
                return false;
            if (sqlId == null) {
                if (other.sqlId != null)
                    return false;
            } else if (!sqlId.equals(other.sqlId))
                return false;
            return true;
        }

        public boolean equalsAll(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SqlIdConfigBean other = (SqlIdConfigBean) obj;
            if (category == null) {
                if (other.category != null)
                    return false;
            } else if (!category.equals(other.category))
                return false;
            if (color == null) {
                if (other.color != null)
                    return false;
            } else if (!color.equals(other.color))
                return false;
            if (sql == null) {
                if (other.sql != null)
                    return false;
            } else if (!sql.equals(other.sql))
                return false;
            if (sqlComment == null) {
                if (other.sqlComment != null)
                    return false;
            } else if (!sqlComment.equals(other.sqlComment))
                return false;
            if (sqlId == null) {
                if (other.sqlId != null)
                    return false;
            } else if (!sqlId.equals(other.sqlId))
                return false;
            return true;
        }

        public String toString() {
            String fixStyle = "style=\"background-color: #000000;\"";
            if (StringUtils.isNotBlank(category)) {
                return String.format("<html><body %4$s><font color=\"%1$s\"><b></b>%2$s</font>&nbsp;&nbsp;<font color=\"%1$s\">%3$s</font></body></html>", //
                        StringUtils.trimToEmpty(color), //
                        "『" + StringUtils.trimToEmpty(category) + "』  ", //
                        StringUtils.trimToEmpty(sqlId), //
                        StringUtils.trimToEmpty(color).equalsIgnoreCase("YELLOW") ? fixStyle : "" //
                );
            } else {
                return String.format("<html><body %3$s><font color=\"%1$s\">%2$s</font></body></html>", //
                        StringUtils.trimToEmpty(color), //
                        StringUtils.trimToEmpty(sqlId), //
                        StringUtils.trimToEmpty(color).equalsIgnoreCase("YELLOW") ? fixStyle : ""//
                );
            }
        }

        // getter & setter ----------------------------------------------
        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public String getSqlComment() {
            return sqlComment;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setSqlId(String sqlId) {
            this.sqlId = sqlId;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public void setSqlComment(String sqlComment) {
            this.sqlComment = sqlComment;
        }
    }

    public static class RefSearchListConfigBean {
        private static String[] KEYS_DEF = new String[] { "category", "searchKey" };
        private static String[] VALUES_DEF = new String[] { "content", "categoryColor" };

        String category;
        String searchKey;
        String content;
        String categoryColor;

        public void setCategory(String category) {
            this.category = category;
        }

        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCategoryColor(String categoryColor) {
            this.categoryColor = categoryColor;
        }

        public String getCategory() {
            return category;
        }

        public String getSearchKey() {
            return searchKey;
        }

        public String getContent() {
            return content;
        }

        public String getCategoryColor() {
            return categoryColor;
        }

        private static String getArry(int idx, String[] arry, String defaultVal) {
            if (idx <= arry.length - 1) {
                return StringUtils.trimToEmpty(arry[idx]);
            }
            return defaultVal;
        }

        public static RefSearchListConfigBean of(String key, String value) {
            RefSearchListConfigBean bean = new RefSearchListConfigBean();
            String[] keys = StringUtils.trimToEmpty(key).split(Pattern.quote("#^#"));
            String[] values = StringUtils.trimToEmpty(value).split(Pattern.quote("#^#"));
            bean.category = getArry(0, keys, "NA");
            bean.searchKey = getArry(1, keys, "");
            bean.content = getArry(0, values, "");
            bean.categoryColor = getArry(1, values, "blue");
            return bean;
        }

        public static String getContent(String value) {
            String[] values = StringUtils.trimToEmpty(value).split(Pattern.quote("#^#"));
            return getArry(0, values, "");
        }

        public static String getKey(String category, String searchKey) {
            return StringUtils.trimToEmpty(category) + "#^#" + StringUtils.trimToEmpty(searchKey);
        }

        public static String getValue(String content, String categoryColor) {
            return StringUtils.trimToEmpty(content) + "#^#" + StringUtils.trimToEmpty(categoryColor);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((category == null) ? 0 : category.hashCode());
            result = prime * result + ((searchKey == null) ? 0 : searchKey.hashCode());
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
            RefSearchListConfigBean other = (RefSearchListConfigBean) obj;
            if (category == null) {
                if (other.category != null)
                    return false;
            } else if (!category.equals(other.category))
                return false;
            if (searchKey == null) {
                if (other.searchKey != null)
                    return false;
            } else if (!searchKey.equals(other.searchKey))
                return false;
            return true;
        }

        public boolean isMatch(String category, String text) {
            category = StringUtils.trimToEmpty(category).toLowerCase();
            text = StringUtils.trimToEmpty(text);

            boolean isCategoryOk = (StringUtils.trimToEmpty(this.category).toLowerCase().contains(category));
            boolean isSearchKeyOk = (StringUtils.trimToEmpty(this.searchKey).toLowerCase().contains(text));
            boolean isContentOk = (StringUtils.trimToEmpty(this.content).toLowerCase().contains(text));

            if (StringUtils.isBlank(category) && StringUtils.isBlank(text)) {
                return true;
            } else if (StringUtils.isNotBlank(category) && StringUtils.isNotBlank(text)) {
                if (isCategoryOk && (isSearchKeyOk || isContentOk)) {
                    return true;
                }
            } else if (StringUtils.isBlank(category) && StringUtils.isNotBlank(text)) {
                if (isSearchKeyOk || isContentOk) {
                    return true;
                }
            } else if (StringUtils.isNotBlank(category) && StringUtils.isBlank(text)) {
                if (isCategoryOk) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            String fixStyle = "style=\"background-color: #000000;\"";
            return String.format("<html><body %4$s><font color=\"%1$s\"><b></b>%2$s</font>&nbsp;&nbsp;  <font color=\"black\">%3$s</font></body></html>", //
                    StringUtils.trimToEmpty(this.categoryColor), //
                    StringUtils.trimToEmpty(this.category), //
                    StringUtils.trimToEmpty(this.searchKey), //
                    StringUtils.trimToEmpty(this.categoryColor).equalsIgnoreCase("YELLOW") ? fixStyle : ""//
            );
        }

        public String toStringInfo() {
            return "RefSearchListConfigBean [category=" + category + ", searchKey=" + searchKey + ", content=" + content + ", categoryColor=" + categoryColor + "]";
        }
    }

    private enum RefSearchColor {
        黑("BLACK"), //
        藍("BLUE"), //
        黃("YELLOW"), //
        綠("GREEN"), //
        紅("RED"),//
        ;

        final String colorCode;

        RefSearchColor(String colorCode) {
            this.colorCode = colorCode;
        }

        private static DefaultComboBoxModel getModel() {
            DefaultComboBoxModel d = new DefaultComboBoxModel();
            for (RefSearchColor e : RefSearchColor.values()) {
                d.addElement(e);
            }
            return d;
        }

        private static RefSearchColor valueFrom(String categoryColor) {
            for (RefSearchColor e : RefSearchColor.values()) {
                if (StringUtils.equals(e.colorCode, categoryColor)) {
                    return e;
                }
            }
            return RefSearchColor.黑;
        }
    }

    private class RefSearchListConfigHandler {

        private YamlUtilBean<RefSearchListConfigBean> config;
        private JList jList;
        private JComboBox refSearchCategoryCombobox;
        private JTextField refConfigPathText;

        private RefSearchListConfigHandler(JTextField refConfigPathText, JList jList, JComboBox refSearchCategoryCombobox) {
            String fileName = FastDBQueryUI.class.getSimpleName() + "_Ref.yml";
            File configFile = new File(refConfigPathText.getText());
            if (configFile == null || !configFile.exists()) {
                config = new YamlUtilBean<RefSearchListConfigBean>(new File(JAR_PATH_FILE, fileName), RefSearchListConfigBean.class, null);
            } else {
                config = new YamlUtilBean<RefSearchListConfigBean>(configFile, RefSearchListConfigBean.class, null);
            }
            refConfigPathText.setText(config.getPropFile().getAbsolutePath());

            this.refConfigPathText = refConfigPathText;
            this.jList = jList;
            this.refSearchCategoryCombobox = refSearchCategoryCombobox;
            this.find("", "");
        }

        public void reload() {
            config.reload();
        }

        private void find(String category, String text) {
            text = StringUtils.trimToEmpty(text).toLowerCase();
            List<RefSearchListConfigBean> lst = new ArrayList<RefSearchListConfigBean>();
            Set<String> categoryLst = new TreeSet<String>();

            for (RefSearchListConfigBean bean : config.getConfigProp()) {
                if (bean.isMatch(category, text)) {
                    lst.add(bean);
                }
                if (StringUtils.isNotBlank(bean.category)) {
                    categoryLst.add(bean.category);
                }
            }
            ListUtil.sortIgnoreCase(lst);
            DefaultListModel model = JListUtil.createModel();
            for (RefSearchListConfigBean key : lst) {
                model.addElement(key);
            }
            this.jList.setModel(model);

            refSearchCategoryCombobox_Auto.applyComboxBoxList(new ArrayList<String>(categoryLst));
            refSearchCategoryCombobox_Auto.setSelectItemAndText(category);
        }

        private void add(String category, String searchKey, String content, String categoryColor) {
            if (StringUtils.isBlank(category) || StringUtils.isBlank(searchKey) || StringUtils.isBlank(content)) {
                JCommonUtil._jOptionPane_showMessageDialog_error("請輸入內容!");
                return;
            }

            RefSearchListConfigBean bean = new RefSearchListConfigBean();
            bean.category = category;
            bean.searchKey = searchKey;
            bean.content = content;
            bean.categoryColor = categoryColor;

            if (config.contains(bean)) {
                String compareContent = bean.content;
                if (!StringUtils.equals(compareContent, content)) {
                    boolean confirmOk = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("已存在 : " + category + " " + searchKey + ", 是否要蓋掉?", "覆蓋確認");
                    if (!confirmOk) {
                        return;
                    }
                }
            }

            config.setProperty(bean);
            config.store();
            find(category, "");
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        }

        private void delete(String category, String searchKey) {
            RefSearchListConfigBean bean = new RefSearchListConfigBean();
            bean.category = category;
            bean.searchKey = searchKey;
            if (config.contains(bean)) {
                boolean confirmOk = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除 : " + category + " " + searchKey + "?", "確認刪除");
                if (!confirmOk) {
                    return;
                }
            } else {
                JCommonUtil._jOptionPane_showMessageDialog_error("找不到 : " + category + " " + searchKey + "!");
                return;
            }
            config.remove(bean);
            config.store();
            find("", "");
        }

        private RefSearchListConfigBean get(String category, String searchKey) {
            RefSearchListConfigBean bean = new RefSearchListConfigBean();
            bean.category = category;
            bean.searchKey = searchKey;
            if (!config.contains(bean)) {
                JCommonUtil._jOptionPane_showMessageDialog_error("找不到 : " + category + " " + searchKey);
                return null;
            }
            return config.getProperty(bean);
        }

        private String findExceptionMessage(String category, String message) {
            if (StringUtils.isBlank(message)) {
                return "";
            }
            Map<Double, List<RefSearchListConfigBean>> compareMap = new TreeMap<Double, List<RefSearchListConfigBean>>();
            for (RefSearchListConfigBean bean : config.getConfigProp()) {
                if (StringUtils.isNotBlank(category) && !StringUtils.equalsIgnoreCase(bean.category, category)) {
                    continue;
                }

                Double score = SimilarityUtil.sim(message.toLowerCase(), bean.searchKey.toLowerCase());
                System.out.println("加入排行 --> 分數 : " + score + "\t" + bean.toStringInfo());

                List<RefSearchListConfigBean> keyLst = new ArrayList<RefSearchListConfigBean>();
                if (compareMap.containsKey(score)) {
                    keyLst = compareMap.get(score);
                }
                keyLst.add(bean);

                compareMap.put(score, keyLst);
            }
            if (compareMap.isEmpty()) {
                return "";
            }
            Double k = Double.MIN_VALUE;
            for (Double k1 : compareMap.keySet()) {
                k = Math.max(k, k1);
            }
            if (k != null || k != 0) {
                List<RefSearchListConfigBean> keyLst = compareMap.get(k);
                if (keyLst == null || keyLst.isEmpty()) {
                    return "";
                }
                Collections.sort(keyLst, new Comparator<RefSearchListConfigBean>() {
                    @Override
                    public int compare(RefSearchListConfigBean o1, RefSearchListConfigBean o2) {
                        String o1Val = RefSearchListConfigBean.getKey(o1.category, o1.searchKey);
                        String o2Val = RefSearchListConfigBean.getKey(o2.category, o2.searchKey);
                        return new Integer(StringUtils.trimToEmpty(o1Val).length()).compareTo(StringUtils.trimToEmpty(o2Val).length());
                    }
                });
                RefSearchListConfigBean refKey = keyLst.get(0);
                if (config.contains(refKey)) {
                    RefSearchListConfigBean bean = config.getProperty(refKey);
                    BigDecimal dd = new BigDecimal(k);
                    dd = dd.setScale(3, BigDecimal.ROUND_HALF_UP);
                    System.out.println("最高分 --> 分數 : " + dd + "\t" + bean.toStringInfo());
                    return String.format("[score:%s] ", dd.toString()) + String.format("<font color=\"%s\"><b>%s</b></font>", bean.categoryColor, bean.content);
                }
            }
            return "";
        }
    }

    class EtcConfigHandler {
        PropertiesUtilBean config = new PropertiesUtilBean(JAR_PATH_FILE, FastDBQueryUI.class.getSimpleName() + "_Etc");
        List<JComponent> containArry = new ArrayList<JComponent>();

        EtcConfigHandler() {
            containArry.add(FastDBQueryUI.this.refConfigPathText);
        }

        public void reflectInit() {
            config.reflectInit(FastDBQueryUI.this, containArry);
        }

        public void reflectSetConfig() {
            config.reflectSetConfig(FastDBQueryUI.this, containArry);
        }

        public void setProperty(String key, String value) {
            config.getConfigProp().setProperty(key, value);
        }

        public String getProperty(String key) {
            return config.getConfigProp().getProperty(key);
        }

        public void store() {
            config.store();
        }

        public void reload() {
            config.reload();
        }
    }

    private void saveEtcConfigBtnAction() {
        try {
            etcConfigHandler.reflectSetConfig();
            etcConfigHandler.store();
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    public void reloadAllProperties() {
        try {
            initLoadSqlListConfig();
            sqlIdListDSMappingHandler.init();
            // loadParameterTableConfig();//不需要
            refSearchListConfigHandler.reload();
            etcConfigHandler.reload();
            dataSourceConfig = new PropertiesGroupUtils_ByKey(new File(JAR_PATH_FILE, "dataSource.properties"));
        } catch (IOException e) {
            JCommonUtil.handleException("reloadAllProperties ERR : " + e.getMessage(), e);
        }
    }

    private static class HardcodeParamDetecter {
        Map<String, String> values = new HashMap<String, String>();
        Pattern ptn1 = Pattern.compile("([\\w\\.]+)[\\s|\\t]*\\=[\\s|\\t]*(\'[^\n]*?\'|[\\-\\d\\.]+)", Pattern.MULTILINE | Pattern.DOTALL);
        Pattern ptn2 = Pattern.compile("(\'[^\n]*?\'|[\\-\\d\\.]+)[\\s|\\t]*\\=[\\s|\\t]*([\\w\\.]+)", Pattern.MULTILINE | Pattern.DOTALL);

        HardcodeParamDetecter(String sql) {
            sql = StringUtils.defaultString(sql);
            Matcher mth = ptn1.matcher(sql);
            while (mth.find()) {
                String param = getParam(mth.group(1));
                String value = getVal(mth.group(2));
                System.out.println("\t detect : K:" + param + " \t V:" + value);
                values.put(param, value);
            }
            mth = ptn2.matcher(sql);
            while (mth.find()) {
                String param = getParam(mth.group(2));
                String value = getVal(mth.group(1));
                System.out.println("\t detect : K:" + param + " \t V:" + value);
                values.put(param, value);
            }
        }

        private String getParam(String value) {
            return StringUtils.defaultString(value).replaceAll("^\\w+\\.", "").toUpperCase();
        }

        private String getVal(String value) {
            String tmpVal = StringUtils.defaultString(value).replaceAll("^\'|\'$", "");
            if (!tmpVal.contains("'")) {
                return tmpVal;
            } else {
                return tmpVal.substring(tmpVal.lastIndexOf("'") + 1);
            }
        }
    }

    private class FakeDataModelHandler {
        private Map<String, String> getParametersTable_Map() {
            Map<String, String> paramMap = new LinkedHashMap<String, String>();
            JTableUtil util = JTableUtil.newInstance(parametersTable);
            for (int ii = 0; ii < parametersTable.getRowCount(); ii++) {
                String columnName = (String) util.getRealValueAt(ii, ParameterTableColumnDef.COLUMN.idx);
                String value = (String) util.getRealValueAt(ii, 1);
                paramMap.put(columnName, value);
            }
            return paramMap;
        }

        private Map<String, String> getHardcodeParameters() {
            return new HardcodeParamDetecter(sqlTextArea.getText()).values;
        }

        TableInfo tabInfo = new TableInfo();
        DefaultTableModel model = JTableUtil.createModel(true, new Object[0]);
        List<Object[]> queryLst = new ArrayList<Object[]>();
        Map<String, String> parameterTableMap;
        Map<String, String> hardcodeMap;
        List<String> columns;
        Triple<List<String>, List<Class<?>>, List<Object[]>> queryList;

        private Object getValue(Object val, char type) {
            switch (type) {
            case 'i':
                try {
                    return Integer.parseInt(String.valueOf(val));
                } catch (Throwable ex) {
                    return 0;
                }
            case 'd':
                try {
                    return java.sql.Date.valueOf(String.valueOf(val));
                } catch (Throwable ex) {
                    return new java.sql.Date(System.currentTimeMillis());
                }
            case 't':
                try {
                    return java.sql.Timestamp.valueOf(String.valueOf(val));
                } catch (Throwable ex) {
                    return new java.sql.Timestamp(System.currentTimeMillis());
                }
            default:
                try {
                    if (val != null) {
                        return String.valueOf(val);
                    } else {
                        return "1";
                    }
                } catch (Throwable ex) {
                    return "1";
                }
            }
        }

        public FakeDataModelHandler(Pair<SqlParam, List<Object>> pair, DataSource ds) {
            try {
                this.parameterTableMap = getParametersTable_Map();
                this.hardcodeMap = getHardcodeParameters();

                tabInfo.execute(pair.getLeft().getQuestionSql(), pair.getRight().toArray(), ds.getConnection());
                columns = new ArrayList<String>(tabInfo.getColumns());
                model = JTableUtil.createModel(true, columns.toArray());

                List<Object> arry = new ArrayList<Object>();
                List<Class<?>> typeLst = new ArrayList<Class<?>>();
                for (int ii = 0; ii < columns.size(); ii++) {
                    String col = columns.get(ii);
                    Object val = null;
                    char type = ' ';
                    Class<?> typeClz = null;

                    // 用 參數表的 值來當作預設值
                    if (hardcodeMap.containsKey(col)) {
                        val = hardcodeMap.get(col);
                        editColumnHistoryHandler.addColumnDef(col, val);
                    } else if (parameterTableMap.containsKey(col)) {
                        val = parameterTableMap.get(col);
                        editColumnHistoryHandler.addColumnDef(col, val);
                    } else if (editColumnHistoryHandler.hasColumnDef(col)) {
                        val = editColumnHistoryHandler.getSingleValue(col);
                    }

                    if (tabInfo.getNumberCol().contains(col)) {
                        type = 'i';
                        typeClz = BigDecimal.class;
                    } else if (tabInfo.getDateCol().contains(col)) {
                        type = 'd';
                        typeClz = java.sql.Date.class;
                    } else if (tabInfo.getTimestampCol().contains(col)) {
                        type = 't';
                        typeClz = java.sql.Timestamp.class;
                    } else {
                        type = ' ';
                        typeClz = String.class;
                    }

                    val = getValue(val, type);
                    arry.add(val);
                    typeLst.add(typeClz);
                }
                queryLst.add(arry.toArray());
                model.addRow(arry.toArray());
                queryList = Triple.of(columns, typeLst, queryLst);

                // 儲存 欄位編輯歷史紀錄
                editColumnHistoryHandler.store();
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }

        public Triple<List<String>, List<Class<?>>, List<Object[]>> getQueryList() {
            return queryList;
        }

        public DefaultTableModel getModel() {
            return model;
        }
    }

    private void sqlIdFixNameBtnAction(String mode) {
        try {
            if (sqlBean == null) {
                JCommonUtil._jOptionPane_showMessageDialog_error("請先選擇SQL List");
                return;
            } else if (sqlBean != null && sqlParameterConfigLoadHandler.isInitOk()) {
                String chkName = "param_" + sqlBean.getUniqueKey() + ".properties";
                if (!StringUtils.equals(sqlParameterConfigLoadHandler.configFile.getName(), chkName)) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("檔名不同無法改檔名" + chkName + " <--> " + sqlParameterConfigLoadHandler.configFile.getName());
                    return;
                }
            }

            String sqlId = sqlIdText.getText();
            RefSearchColor color = (RefSearchColor) sqlIdColorComboBox.getSelectedItem();
            String category = sqlIdCategoryComboBox_Auto.getTextComponent().getText();
            String sql = sqlTextArea.getText();
            String sqlComment = sqlIdCommentArea.getText();

            JCommonUtil.isBlankErrorMsg(sqlId, "請輸入sql Id");
            JCommonUtil.isBlankErrorMsg(sql, "請輸入sql");

            Validate.isTrue(StringUtils.equals(sql, sqlBean.sql), "sql不可異動!");

            SqlIdConfigBean bean = new SqlIdConfigBean();
            bean.sql = sql;
            bean.sqlId = sqlId;
            bean.category = category;
            bean.color = color.colorCode;
            bean.sqlComment = sqlComment;

            File newFile = sqlParameterConfigLoadHandler.getFile(bean.getUniqueKey());
            File oldFile = sqlParameterConfigLoadHandler.getFile(sqlBean.getUniqueKey());
            if (StringUtils.equalsIgnoreCase(newFile.getName(), oldFile.getName())) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔名相同無須修改 : " + newFile.getName());
                return;
            }

            if (!oldFile.exists()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("原檔案不存在! : " + oldFile);
                return;
            }
            if (newFile.exists()) {
                boolean overwriteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("目的檔案已存在, 是否覆蓋?! : " + newFile, "目的檔案已存在, 是否覆蓋?!");
                if (!overwriteConfirm) {
                    return;
                }
            }

            if ("rename".equals(mode)) {
                // DS Mapping 修正
                sqlIdListDSMappingHandler.cloneTo(sqlBean, bean, true);

                // 參數設定黨改名
                oldFile.renameTo(newFile);

                // sql設定修正
                sqlIdConfigBeanHandler.remove(sqlBean);
                sqlIdConfigBeanHandler.save(bean);
            } else if ("clone".equals(mode)) {
                // DS Mapping 修正
                sqlIdListDSMappingHandler.cloneTo(sqlBean, bean, false);

                // 參數設定黨改名
                FileUtil.copyFile(oldFile, newFile);

                // sql設定修正
                sqlIdConfigBeanHandler.save(bean);
            }

            initLoadSqlListConfig();
            JCommonUtil._jOptionPane_showMessageDialog_info("已修正為 : " + bean.getUniqueKey());
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private class SqlIdListDSMappingHandler {
        private Properties sqlIdListDSMappingProp;

        SqlIdListDSMappingHandler() {
            init();
        }

        public boolean containsKey(String uniqueKey) {
            return sqlIdListDSMappingProp.containsKey(uniqueKey);
        }

        public String getProperty(String sqlId) {
            return sqlIdListDSMappingProp.getProperty(sqlId);
        }

        private void init() {
            try {
                if (!sqlIdListDSMappingFile.exists()) {
                    sqlIdListDSMappingFile.createNewFile();
                }
                sqlIdListDSMappingProp = PropertiesUtil.loadProperties(sqlIdListDSMappingFile, null, false);
            } catch (Exception ex) {
                throw new RuntimeException("SqlIdListDSMappingHandler init ERR : " + ex.getMessage(), ex);
            }
        }

        private void remove(String sqlId) {
            sqlIdListDSMappingProp.remove(sqlId);
            PropertiesUtil.storeProperties(sqlIdListDSMappingProp, sqlIdListDSMappingFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
        }

        private void store(boolean fromSqlLst) throws IOException {
            SqlIdConfigBean bean = null;
            if (!fromSqlLst) {
                bean = (SqlIdConfigBean) sqlList.getSelectedValue();
            } else {
                bean = getCurrentEditSqlIdConfigBean();
            }
            if (bean == null) {
                return;
            }
            try {
                bean.validate();
            } catch (Exception ex) {
                return;
            }
            String sqlId = bean.getUniqueKey();
            String dbNameId = dbNameIdText_getText();
            this.init();
            sqlIdListDSMappingProp.setProperty(sqlId, dbNameId);
            PropertiesUtil.storeProperties(sqlIdListDSMappingProp, sqlIdListDSMappingFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
        }

        private void cloneTo(SqlIdConfigBean from, SqlIdConfigBean to, boolean removeOld) {
            this.init();
            if (sqlIdListDSMappingProp.containsKey(from.sqlId)) {
                String moveToValue = sqlIdListDSMappingProp.getProperty(from.sqlId);
                if (removeOld) {
                    sqlIdListDSMappingProp.remove(from.sqlId);
                }
                sqlIdListDSMappingProp.setProperty(to.getUniqueKey(), moveToValue);
                PropertiesUtil.storeProperties(sqlIdListDSMappingProp, sqlIdListDSMappingFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
            }
        }
    }

    private static class SqlParameterConfigLoadHandler {
        private static final String PARAM_COMMENT_KEY = "#PARAM_COMMENT_KEY#";

        private PropertiesGroupUtils sqlParameterConfigLoad;
        private File configFile;

        private Map<String, String> loadConfig() {
            Map<String, String> clone = new HashMap<String, String>(sqlParameterConfigLoad.loadConfig());
            clone.remove(PARAM_COMMENT_KEY);
            return clone;
        }

        private String loadComment() {
            return StringUtils.trimToEmpty(sqlParameterConfigLoad.loadConfig().get(PARAM_COMMENT_KEY));
        }

        public void clear() {
            sqlParameterConfigLoad.clear();
        }

        public void next() {
            sqlParameterConfigLoad.next();
        }

        private void saveConfig(Map<String, String> currentConfig, String paramComment) {
            currentConfig.put(PARAM_COMMENT_KEY, StringUtils.trimToEmpty(paramComment));
            sqlParameterConfigLoad.saveConfig(currentConfig);
        }

        private boolean isInitOk() {
            return sqlParameterConfigLoad != null && configFile != null;
        }

        private File getFile(String sqlId) {
            return new File(JAR_PATH_FILE, "param_" + sqlId + ".properties");
        }

        private void init(String sqlId) {
            configFile = new File(JAR_PATH_FILE, "param_" + sqlId + ".properties");
            sqlParameterConfigLoad = new PropertiesGroupUtils(configFile);
        }

        private void deleteParameterBtnAction() {
            if (sqlParameterConfigLoad == null) {
                return;
            }
            Map<String, String> configMap = sqlParameterConfigLoad.loadConfig();
            boolean delConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要刪除 :" + configMap, "確認刪除?");
            if (delConfirm) {
                sqlParameterConfigLoad.removeConfig();
            }
        }
    }

    private class SqlIdColumnHolder {
        private PropertiesUtilBean config;
        private File configFile;

        private String getSqlId() {
            if (sqlBean != null) {
                return sqlBean.sqlId;
            }
            return StringUtils.trimToEmpty(sqlIdText.getText());
        }

        private SqlIdColumnHolder() {
            configFile = new File(JAR_PATH_FILE, SqlIdColumnHolder.class.getSimpleName() + ".properties");
            config = new PropertiesUtilBean(configFile);
        }

        private void remove(String sqlId) {
            if (!config.getConfigProp().containsKey(sqlId)) {
                return;
            }
            config.getConfigProp().remove(sqlId);
            config.store();
        }

        private void setColumns(String sqlId, List<String> columns) {
            if (StringUtils.isBlank(sqlId)) {
                return;
            }
            String value = StringUtils.join(columns, "^");
            config.getConfigProp().setProperty(sqlId, value);
            config.store();
        }

        private boolean isColumnExists(String sqlId, String column) {
            column = StringUtils.trimToEmpty(column);
            if (StringUtils.isBlank(column)) {
                return false;
            }
            if (!config.getConfigProp().containsKey(sqlId)) {
                return false;
            }
            String columnStr = config.getConfigProp().getProperty(sqlId);
            String[] columns = StringUtils.split(columnStr, "^");
            for (String col : columns) {
                if (StringUtils.trimToEmpty(col).equalsIgnoreCase(column)) {
                    return true;
                }
            }
            return false;
        }
    }

    private SqlIdConfigBean getCurrentEditSqlIdConfigBean() {
        String sqlId = sqlIdText.getText();
        RefSearchColor color = (RefSearchColor) sqlIdColorComboBox.getSelectedItem();
        String category = sqlIdCategoryComboBox_Auto.getTextComponent().getText();
        String sql = sqlTextArea.getText();
        String sqlComment = sqlIdCommentArea.getText();

        SqlIdConfigBean bean = new SqlIdConfigBean();
        bean.sql = sql;
        bean.sqlId = sqlId;
        bean.category = category;
        bean.color = color.colorCode;
        bean.sqlComment = sqlComment;
        return bean;
    }

    private boolean isSqlIdChange() {
        SqlIdConfigBean currentBean = getCurrentEditSqlIdConfigBean();
        if (sqlBean != null) {
            if (StringUtils.equalsIgnoreCase(sqlBean.sqlId, currentBean.sqlId)) {
                return false;
            }
        }
        sqlIdConfigBeanHandler.init("");
        List<SqlIdConfigBean> lst = sqlIdConfigBeanHandler.lst;
        for (SqlIdConfigBean b : lst) {
            if (StringUtils.equalsIgnoreCase(b.sqlId, currentBean.sqlId)) {
                return true;
            }
        }
        return false;
    }

    private void sqlTextAreaChange() {
        try {
            String text = sqlTextArea.getText();
            boolean isNotEqual = false;
            if (sqlBean != null) {
                if (!sqlBean.equalsAll(getCurrentEditSqlIdConfigBean())) {
                    isNotEqual = true;
                }
            } else {
                if (StringUtils.isNotBlank(text) || StringUtils.isNotBlank(getCurrentEditSqlIdConfigBean().getUniqueKey())) {
                    isNotEqual = true;
                }
            }
            if (isNotEqual) {
                sqlSaveButton.setText("<html><font color='RED'>＊儲存</font></html>");
            } else {
                sqlSaveButton.setText("<html><font color='BLACK'>儲存</font></html>");
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 計算欄位型態
     * 
     * @param queryLst
     * @return
     */
    private Triple<List<String>, List<Class<?>>, List<Object[]>> fixPairToTripleQueryResult(Pair<List<String>, List<Object[]>> queryLst) {
        List<Object[]> lst = queryLst.getRight();
        TreeMap<Integer, Class<?>> typeMap = new TreeMap<Integer, Class<?>>();
        A: for (int ii = 0; ii < lst.size(); ii++) {
            if (queryLst.getLeft().size() == typeMap.size()) {
                break A;
            }
            Object[] arry = lst.get(ii);
            B: for (int jj = 0; jj < arry.length; jj++) {
                if (typeMap.containsKey(jj)) {
                    continue;
                }
                if (arry[jj] != null) {
                    typeMap.put(jj, arry[jj].getClass());
                }
            }
        }
        for (int ii = 0; ii < queryLst.getLeft().size(); ii++) {
            if (!typeMap.containsKey(ii)) {
                typeMap.put(ii, Object.class);
            }
        }
        List<Class<?>> typeLst = new ArrayList<Class<?>>(typeMap.values());
        return Triple.of(queryLst.getLeft(), typeLst, queryLst.getRight());
    }

    protected void handleExceptionForExecuteSQL(Exception ex) {
        String category = refSearchCategoryCombobox_Auto.getTextComponent().getText();
        String findMessage = refSearchListConfigHandler.findExceptionMessage(category, ex.getMessage());
        // 一般顯示
        if (StringUtils.isBlank(findMessage)) {
            JCommonUtil.handleException(ex);
        } else {
            // html顯示
            JCommonUtil.handleException(String.format("參考 : %s", findMessage), ex, true, "", "yyyyMMdd", false, true);
        }
    }

    public EtcConfigHandler getEtcConfig() {
        return etcConfigHandler;
    }

    static class EditColumnHistoryHandler {
        String delimit = "#^#";
        PropertiesUtilBean config = new PropertiesUtilBean(JAR_PATH_FILE, FastDBQueryUI.class.getSimpleName() + "_ColumnHis");

        protected void addColumnDef(String column, Object value) {
            column = StringUtils.trimToEmpty(column).toUpperCase();
            String realVal = value == null ? "" : String.valueOf(value);
            realVal = StringUtils.trimToEmpty(realVal);
            String[] values = StringUtils.trimToEmpty(config.getConfigProp().getProperty(column)).split(Pattern.quote(delimit));
            Set<String> vals = new LinkedHashSet<String>();
            vals.add(realVal);
            for (String v : values) {
                vals.add(v);
            }
            String valStr = StringUtils.join(vals, delimit);
            config.getConfigProp().setProperty(column, valStr);
        }

        protected void store() {
            config.store();
        }

        protected List<String> getColumnValues(String column) {
            column = StringUtils.trimToEmpty(column).toUpperCase();
            String[] values = StringUtils.trimToEmpty(config.getConfigProp().getProperty(column)).split(Pattern.quote(delimit));
            Set<String> vals = new LinkedHashSet<String>();
            for (String v : values) {
                vals.add(v);
            }
            return new ArrayList<String>(vals);
        }

        protected String getSingleValue(String column) {
            try {
                return getColumnValues(column).get(0);
            } catch (Exception ex) {
                return "1";
            }
        }

        protected boolean hasColumnDef(String column) {
            column = StringUtils.trimToEmpty(column).toUpperCase();
            return config.getConfigProp().containsKey(column);
        }
    }

    public EditColumnHistoryHandler getEditColumnConfig() {
        return editColumnHistoryHandler;
    }

    private class SqlTextAreaPromptHandler {
        String queryText = "";
        String tableAlias = "";
        String columnPrefix = "";
        LRUMap tabMap = new LRUMap(20);
        LRUMap failMap = new LRUMap(100);
        Pair<Integer, Integer> columnIndex;
        int queryTextPos = -1;
        JPopupMenuUtil util;
        int currentMenuIndex = 0;

        private SqlTextAreaPromptHandler() {
        }

        public boolean performUpdateLocation() {
            Rectangle rect = mSqlTextAreaJTextAreaSelectPositionHandler.getRect();
            if (rect == null || util == null) {
                return false;
            }
            if (StringUtils.isBlank(queryText)) {
                util.dismiss();
            }
            util.setLocation(sqlTextArea, (int) rect.getX(), (int) rect.getY());
            return false;
        }

        public boolean performSelectClose() {
            if (util == null) {
                return false;
            }
            if (util.getJPopupMenu().isShowing()) {
                util.dismiss();
                return true;
            }
            return false;
        }

        public boolean performSelectUpDown(KeyEvent e) {
            if (util == null) {
                return false;
            }
            if (util.getJPopupMenu().isShowing() && !util.getJPopupMenu().isFocusOwner()) {
                JCommonUtil.focusComponent(util.getJPopupMenu(), false, null);
                util.getJPopupMenu().dispatchEvent(e);
                return true;
            }
            return false;
        }

        public boolean performSelectTopColumn(KeyEvent e2) {
            if (util == null) {
                return false;
            }
            if (util.getJPopupMenu().isShowing() && !util.getJPopupMenu().isFocusOwner() && !util.getMenuList().isEmpty()) {
                JCommonUtil.focusComponent(util.getJPopupMenu(), false, null);
                util.getJPopupMenu().dispatchEvent(e2);// 原生的event才會正確
                return true;
            }
            return false;
        }

        private void init(DocumentEvent event) {
            String tmpSql = StringUtils.substring(sqlTextArea.getText(), 0, event.getOffset() + event.getLength());
            Pattern ptn = Pattern.compile("[\\s\n]", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(tmpSql);
            queryTextPos = -1;
            while (mth.find()) {
                queryTextPos = mth.end();
            }
            queryText = StringUtils.substring(tmpSql, queryTextPos);
            currentMenuIndex = 0;
            System.out.println("prompt - [" + queryText + "]");
        }

        private void mainProcess() {
            if (queryText.contains(".")) {
                delimitDBTable();
            } else {
                return;
            }
            List<String> tables = getTableName(tableAlias);
            if (tables.isEmpty()) {
                return;
            }
            for (int ii = 0; ii < tables.size(); ii++) {
                String tableName = tables.get(ii);
                if (failMap.containsKey(tableName)) {
                    if (System.currentTimeMillis() - (Long) failMap.get(tableName) < 3 * 60 * 1000) {
                        System.out.println("前次失敗未滿3分鐘 : " + tableName);
                        return;
                    }
                }
                DbSqlCreater.TableInfo tab = querySchema(tableName);
                if (tab != null) {
                    List<String> columnLst = getColumnLst(tab);
                    if (!columnLst.isEmpty()) {
                        showPopup(columnLst);
                        break;
                    }
                }
            }
        }

        private void showPopup(List<String> columnLst) {
            Rectangle rect = mSqlTextAreaJTextAreaSelectPositionHandler.getRect();
            util = JPopupMenuUtil.newInstance(sqlTextArea, true);
            util.applyEvent(rect);
            // util.getJPopupMenu().setFocusable(false);
            for (int ii = 0; ii < columnLst.size(); ii++) {
                final String col = columnLst.get(ii);
                util.addJMenuItem(col, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        replaceColumn(col);
                        util.dismiss();
                    }
                });
            }
            util.getJPopupMenu().addMenuKeyListener(new MenuKeyListener() {
                @Override
                public void menuKeyTyped(MenuKeyEvent arg0) {
                }

                @Override
                public void menuKeyReleased(MenuKeyEvent arg0) {
                }

                @Override
                public void menuKeyPressed(MenuKeyEvent arg0) {
                    if (arg0.getKeyCode() == 38 || arg0.getKeyCode() == 40) {// 上下
                    } else if (arg0.getKeyCode() == KeyEvent.VK_ENTER || arg0.getKeyCode() == KeyEvent.VK_TAB) {
                        JMenuItem item = null;
                        if ((item = JPopupMenuUtil.getCurrentSelectItem()) != null) {
                            JCommonUtil.triggerButtonActionPerformed(item);
                        } else {
                            JPopupMenuUtil.setCurrentSelectItem(util.getJPopupMenu(), 0, null);
                            item = JPopupMenuUtil.getCurrentSelectItem();
                            JCommonUtil.triggerButtonActionPerformed(item);
                        }
                    }
                }
            });
            util.show();
            sqlTextArea.requestFocus();
        }

        private void replaceColumn(String column) {
            if (columnIndex == null) {
                return;
            }
            String textOrign = StringUtils.defaultString(sqlTextArea.getText());
            String text = StringUtils.substring(textOrign, 0, columnIndex.getLeft()) + column;
            int afterPos = text.length();
            text = text + StringUtils.substring(textOrign, columnIndex.getRight());

            JTextFieldUtil.setTextIgnoreDocumentListener(sqlTextArea, text);

            sqlTextArea.updateUI();

            sqlTextArea.setSelectionStart(afterPos);
            sqlTextArea.setSelectionEnd(afterPos);
        }

        private List<String> getColumnLst(DbSqlCreater.TableInfo tab) {
            List<String> columnLst = new ArrayList<String>();
            if (StringUtils.isNotBlank(columnPrefix)) {
                String _columnPrefix = columnPrefix.toLowerCase();
                for (String col : tab.getColumns()) {
                    if (col.toLowerCase().startsWith(_columnPrefix)) {
                        columnLst.add(col);
                    }
                }
            } else {
                columnLst.addAll(tab.getColumns());
            }
            Collections.sort(columnLst);
            return columnLst;
        }

        private List<String> getTableName(String tableAlias) {
            List<String> tables = new ArrayList<String>();
            String tmpSql = StringUtils.trimToEmpty(sqlTextArea.getText());
            Pattern ptn = Pattern.compile("[,\\s\n\t]([\\.\\w\\_]+)[\\s\t]+" + tableAlias + "[,\\s\n\t]", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher mth = ptn.matcher(tmpSql);
            while (mth.find()) {
                tables.add(mth.group(1));
            }
            return tables;
        }

        private DbSqlCreater.TableInfo querySchema(String tableName) {
            if (tabMap.containsKey(tableName)) {
                return (DbSqlCreater.TableInfo) tabMap.get(tableName);
            } else {
                DbSqlCreater.TableInfo tab = new DbSqlCreater.TableInfo();
                try {
                    tab.execute("select * from " + tableName + " where 1!=1 ", getDataSource().getConnection());
                    tabMap.put(tableName, tab);
                    failMap.remove(tableName);
                } catch (Throwable e) {
                    e.printStackTrace();
                    failMap.put(tableName, System.currentTimeMillis());
                }
                return tab;
            }
        }

        private void delimitDBTable() {
            Pattern ptn2 = Pattern.compile("(.*)\\.(.*)");
            Matcher mth2 = ptn2.matcher(queryText);
            if (mth2.find()) {
                tableAlias = mth2.group(1);
                columnPrefix = mth2.group(2);
                columnIndex = Pair.of(queryTextPos + mth2.start(2), queryTextPos + mth2.end(2));
            } else {
                tableAlias = queryText.replaceAll("\\.+$", "");
                columnIndex = null;
            }
        }

        private boolean checkPopupListFocus(KeyEvent arg0) {
            if (arg0.getKeyCode() == 38 || arg0.getKeyCode() == 40) {// 上下
                if (util.getJPopupMenu().isShowing()) {
                    util.getJPopupMenu().dispatchEvent(arg0);
                }
                sqlTextArea.requestFocus();
                return true;
            } else if (!sqlTextArea.isFocusOwner()) {
                if (util.getJPopupMenu().isShowing()) {
                    util.getJPopupMenu().dispatchEvent(arg0);
                }
                sqlTextArea.requestFocus();
            }
            return false;
        }
    }

    private void doSetColumnSqlInProcess(String columnName, boolean distinct) {
        try {
            List<String> lst = new ArrayList<String>();
            int index = -1;
            for (int idx = 0; idx <= queryList.getLeft().size(); idx++) {
                if (StringUtils.equalsIgnoreCase(columnName, queryList.getLeft().get(idx))) {
                    index = idx;
                    break;
                }
            }
            if (index == -1) {
                Validate.isTrue(false, "找不到欄位 :" + columnName);
            }
            for (Object[] arry : queryList.getRight()) {
                Object val = arry[index];
                if (val == null) {
                    continue;
                }
                String strVal = StringUtils.trimToEmpty(String.valueOf(val));
                if (distinct) {
                    if (!lst.contains(strVal)) {
                        lst.add(strVal);
                    }
                } else {
                    lst.add(strVal);
                }
            }
            String resultSql = StringUtils.join(lst, "','");
            resultSql = "'" + resultSql + "'";
            ClipboardUtil.getInstance().setContents(resultSql);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void sqlTextAreaPromptProcess(String label, DocumentEvent event) {
        if (mSqlTextAreaPromptHandler == null) {
            mSqlTextAreaPromptHandler = new SqlTextAreaPromptHandler();
        }
        mSqlTextAreaPromptHandler.init(event);
        mSqlTextAreaPromptHandler.mainProcess();
    }

    private void moveTabToQueryResultIfHasRecords() {
        DefaultTableModel model = (DefaultTableModel) queryResultTable.getModel();
        if (model.getRowCount() != 0) {
            JTabbedPaneUtil.newInst(tabbedPane).setSelectedIndexByTitle("查詢結果");
        }
    }

    private class SearchAndReplace {
        String findKey;
        String lastestStatusArea;
        List<Pair<Integer, Integer>> findLst = new ArrayList<Pair<Integer, Integer>>();

        public boolean findKey() {
            findKey = JCommonUtil._jOptionPane_showInputDialog("搜尋:", "");
            if (StringUtils.isBlank(findKey)) {
                return true;
            }
            lastestStatusArea = StringUtils.defaultString(sqlTextArea.getText());

            Pattern findPtn = Pattern.compile(Pattern.quote(findKey), Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher findMth = findPtn.matcher(lastestStatusArea);

            boolean isFirst = true;
            while (findMth.find()) {
                findLst.add(Pair.of(findMth.start(), findMth.end()));

                if (isFirst) {
                    sqlTextArea.setSelectionStart(findMth.start());
                    sqlTextArea.setSelectionEnd(findMth.end());
                    isFirst = false;
                }
            }
            if (isFirst) {
                JCommonUtil._jOptionPane_showMessageDialog_error("找不到 : " + findKey);
            }
            return true;
        }

        public boolean replaceAll() {
            if (StringUtils.isBlank(findKey)) {
                findKey = JCommonUtil._jOptionPane_showInputDialog("搜尋:", "");
            }
            if (StringUtils.isBlank(findKey)) {
                return true;
            }
            String replaceKey = JCommonUtil._jOptionPane_showInputDialog("將" + findKey + "取代為:", "");
            if (replaceKey == null) {
                JCommonUtil._jOptionPane_showMessageDialog_error("錯誤！");
                return true;
            }
            replaceKey = StringUtils.defaultString(replaceKey);

            Pattern findPtn = Pattern.compile(Pattern.quote(findKey), Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
            Matcher findMth = findPtn.matcher(StringUtils.defaultString(sqlTextArea.getText()));
            StringBuffer sb = new StringBuffer();
            while (findMth.find()) {
                findMth.appendReplacement(sb, replaceKey);
            }
            findMth.appendTail(sb);
            sqlTextArea.setText(sb.toString());
            sqlTextArea.setSelectionStart(StringUtils.defaultString(sqlTextArea.getText()).length());
            sqlTextArea.updateUI();
            return true;
        }

        public boolean findNext(boolean isForward) {
            String tmpAreaText = StringUtils.defaultString(sqlTextArea.getText());
            if (StringUtils.isNotBlank(tmpAreaText) && StringUtils.equals(tmpAreaText, lastestStatusArea) && !findLst.isEmpty()) {
                int idx = 0;

                for (int ii = 0; ii < findLst.size(); ii++) {
                    Pair<Integer, Integer> p = findLst.get(ii);
                    if (p.getLeft() == sqlTextArea.getSelectionStart() && p.getRight() == sqlTextArea.getSelectionEnd()) {
                        idx = ii;
                        break;
                    }
                }
                if (isForward) {
                    idx++;
                    if (idx >= findLst.size()) {
                        idx = 0;
                    }
                } else {
                    idx--;
                    if (idx < 0) {
                        idx = findLst.size() - 1;
                    }
                }

                Pair<Integer, Integer> pos = findLst.get(idx);
                sqlTextArea.setSelectionStart(pos.getLeft());
                sqlTextArea.setSelectionEnd(pos.getRight());
            }
            return true;
        }
    }

    private void startRecordWatcher(boolean isStart) {
        boolean allOk = false;
        if (isStart) {
            if (this.queryList != null) {
                if (mRecordWatcher.get() != null && //
                        (mRecordWatcher.get().getState() == Thread.State.NEW)) {
                    allOk = true;
                    FastDBQueryUI_RowDiffWatcherDlg mFastDBQueryUI_RowDiffWatcherDlg = FastDBQueryUI_RowDiffWatcherDlg.newInstance(this.queryList.getLeft(), new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            FastDBQueryUI_RowDiffWatcherDlg dlg = (FastDBQueryUI_RowDiffWatcherDlg) e.getSource();
                            List<Integer> pkIndexLst = new ArrayList<Integer>();
                            for (int ii = 0; ii < queryList.getLeft().size(); ii++) {
                                String column = queryList.getLeft().get(ii);
                                for (String mColumn : dlg.getPkLst()) {
                                    if (StringUtils.equals(column, mColumn)) {
                                        pkIndexLst.add(ii);
                                    }
                                }
                            }
                            if (pkIndexLst.isEmpty()) {
                                JCommonUtil._jOptionPane_showMessageDialog_error("請選擇主鍵!");
                            } else {
                                mRecordWatcher.get().setPkIndexLst(pkIndexLst);
                                mRecordWatcher.get().start();
                                recordWatcherToggleBtn.setText("監聽on");
                            }
                        }
                    }, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            mJDlgHolderBringToFrontHandler.remove((JDialog) e.getSource());
                        }
                    });
                    mJDlgHolderBringToFrontHandler.add(mFastDBQueryUI_RowDiffWatcherDlg);
                }
            }
            if (!allOk) {
                JCommonUtil._jOptionPane_showMessageDialog_error("請重新查詢");
            }
        } else {
            if (mRecordWatcher.get() != null) {
                mRecordWatcher.get().doStop(true);
            }
        }
    }

    private void createRecordWatcher(Triple<List<String>, List<Class<?>>, List<Object[]>> orignQueryResult, String sql, Object[] params, boolean b, int maxRowsLimit) {
        if (mRecordWatcher.get() != null) {
            mRecordWatcher.get().doStop(false);
        }
        String fileMiddleName = sqlIdText.getText();
        if (StringUtils.isBlank(fileMiddleName)) {
            fileMiddleName = getRandom_TableNSchema();
        }
        mRecordWatcher.set(new FastDBQueryUI_RecordWatcher(orignQueryResult, sql, params, maxRowsLimit, new Callable<Connection>() {
            @Override
            public Connection call() throws Exception {
                return getDataSource().getConnection();
            }
        }, 1000, fileMiddleName, TAB_UI1.getSysTrayUtil(), new Transformer() {
            @Override
            public Object transform(Object input) {
                recordWatcherToggleBtn.setSelected(false);
                recordWatcherToggleBtn.setText("監聽off");
                Map<String, Object> map = (Map<String, Object>) input;
                Throwable ex = (Throwable) map.get("ex");
                String msg = (String) map.get("msg");
                if (ex == null && StringUtils.isNotBlank(msg)) {
                    JCommonUtil._jOptionPane_showMessageDialog_error(msg);
                } else if (ex != null) {
                    JCommonUtil.handleException(msg, ex);
                }
                return null;
            }
        }));
    }

    private boolean checkIsNeedResetQueryResultTable(boolean isCheckColumnFilterText) {
        if (true) {
            return false;
        }
        boolean isNeedReset = false;
        if (isCheckColumnFilterText && StringUtils.isBlank(columnFilterText.getText())) {
            isNeedReset = true;
        } else if (!isCheckColumnFilterText && StringUtils.isBlank(rowFilterText.getText())) {
            isNeedReset = true;
        }
        if (isNeedReset) {
            filterRowsQueryList = null;
            isResetQuery = true;
            queryModeProcess(queryList, true, null, null);//
        }
        return isNeedReset;
    }

    private final Runnable rowFilterTextDoFilter = new Runnable() {

        private FastDBQueryUI_ColumnSearchFilter columnFilter;

        private Triple<List<String>, List<Class<?>>, List<Object[]>> getQuery() {
            if (!isResetQuery && filterRowsQueryList != null) {
                return filterRowsQueryList;
            } else if (queryList != null) {
                return queryList;
            }
            return null;
        }

        private void runProcess() {
            Triple<List<String>, List<Class<?>>, List<Object[]>> queryList = getQuery();
            if (queryList == null) {
                return;
            }

            if (columnFilter == null || isResetQuery) {
                columnFilter = new FastDBQueryUI_ColumnSearchFilter(queryList, "^", new Object[] { QUERY_RESULT_COLUMN_NO });
                isResetQuery = false;
            }
            columnFilter.filterColumnText(columnFilterText.getText());
            queryList = columnFilter.getResult();

            columnFilter.filterRowText(rowFilterText.getText(), isColumnNoExists(), rowFilterTextKeepMatchChk.isSelected(), queryList);
            Pair<Triple<List<String>, List<Class<?>>, List<Object[]>>, Map<Integer, List<Integer>>> resultFinal = columnFilter.getResultFinal();

            queryList = resultFinal.getLeft();
            Map<Integer, List<Integer>> changeColorRowCellIdxMap = resultFinal.getRight();

            queryModeProcess(queryList, true, null, changeColorRowCellIdxMap);//

            filterRowsQueryList = queryList;
            isResetQuery = false;
        }

        @Override
        public void run() {
            if (!checkIsNeedResetQueryResultTable(false)) {
                try {
                    runProcess();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }
    };

    private class JDlgHolderBringToFrontHandler {
        LRUMap map = new LRUMap(10);

        public void add(JDialog dlg) {
            map.put(dlg.getClass(), dlg);
        }

        public void remove(JDialog dlg) {
            map.remove(dlg.getClass());
        }

        public void bringToFront() {
            for (;;) {
                if (map.isEmpty()) {
                    break;
                }
                final JDialog dlg = (JDialog) map.get(map.firstKey());
                if (!dlg.isVisible()) {
                    map.remove(map.firstKey());
                    continue;
                }
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        JCommonUtil.setFrameAtop(dlg, false);
                    }
                });
                break;
            }
        }
    }

    class TableColumnDefTextHandler {
        FastDBQueryUI_XlsColumnDefLoader xlsLoader = null;
        int fromIndex = -1;
        int toIndex = -1;
        int pkIndex = -1;
        int fkIndex = -1;

        private boolean init() {
            File dir = new File(FileUtil.DESKTOP_DIR, "FastColumnDef");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (xlsLoader == null) {
                xlsLoader = new FastDBQueryUI_XlsColumnDefLoader(null);
                xlsLoader.execute();
            }
            if (tableColumnDefText.getSelectedItem() != null && StringUtils.isNotBlank((String) tableColumnDefText.getSelectedItem())) {
                if (mXlsColumnDefDlg == null || mXlsColumnDefDlg.getConfig() == null) {
                    Validate.isTrue(false, "請先按設定");
                }
                xlsLoader.setMappingConfig(mXlsColumnDefDlg.getConfig());
                return true;
            }
            return false;
        }

        private void init2() {
            File dir = new File(FileUtil.DESKTOP_DIR, "FastColumnDef");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (xlsLoader == null) {
                xlsLoader = new FastDBQueryUI_XlsColumnDefLoader(null);
                xlsLoader.execute();
            }
            if (mXlsColumnDefDlg == null || mXlsColumnDefDlg.getConfig() == null) {
                Validate.isTrue(false, "請先按設定");
            }
            xlsLoader.setMappingConfig(mXlsColumnDefDlg.getConfig());
        }

        public void action() {
            try {
                if (init()) {
                    String table = String.valueOf(tableColumnDefText.getSelectedItem());
                    queryResultTable.setTitleTooltipTransformer(xlsLoader.getTableTitleTransformer(table));
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }

        public String getChinese(String column) {
            try {
                if (init()) {
                    String table = String.valueOf(tableColumnDefText.getSelectedItem());
                    return xlsLoader.getDBColumnChinese(column, table);
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
            return null;
        }

        public List<String> getPkLst() {
            try {
                if (init()) {
                    String table = String.valueOf(tableColumnDefText.getSelectedItem());
                    return xlsLoader.getPkList(table);
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
            return Collections.emptyList();
        }

        public Triple<DefaultTableModel, Integer, ActionListener> query(String tableQry, String columnQry, String otherQry, JTable jtable, JFrame jframe) {
            init2();
            return xlsLoader.query(tableQry, columnQry, otherQry, jtable, jframe);
        }
    }

    // ===========================================================================================================================

    public List<String> getCompareXlsColumnLst(File xlsFile) {
        ExcelUtil_Xls97 xlsUtil = ExcelUtil_Xls97.getInstance();
        HSSFWorkbook wb = xlsUtil.readExcel(xlsFile);
        HSSFSheet sheet = wb.getSheetAt(0);
        if (wb.getNumberOfSheets() == 2) {
            sheet = wb.getSheetAt(1);
        }
        List<String> columnLst = new ArrayList<String>();
        for (int jj = 0; jj < sheet.getRow(0).getLastCellNum(); jj++) {
            String value = ExcelUtil_Xls97.getInstance().readCell(sheet.getRow(0).getCell(jj));
            columnLst.add(value);
        }
        return columnLst;
    }

    private void compareXlsExecuteBtnAction() {
        try {
            final String fileMiddleName = StringUtils.trimToEmpty(compareXlsMiddleNameText.getText());
            final File beforeXlsFile = JCommonUtil.filePathCheck(compareBeforeXlsText.getText(), "初始XLS檔案錯誤", "xls");
            final File afterXlsFile = JCommonUtil.filePathCheck(compareAfterXlsText.getText(), "結果XLS檔案錯誤", "xls");
            JCommonUtil.isBlankErrorMsg(fileMiddleName, "中間檔名不可為空");
            final List<String> columnLst = getCompareXlsColumnLst(beforeXlsFile);

            final FastDBQueryUI_RowDiffWatcherDlg mFastDBQueryUI_RowDiffWatcherDlg = (FastDBQueryUI_RowDiffWatcherDlg.newInstance(columnLst, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FastDBQueryUI_RowDiffWatcherDlg dlg = (FastDBQueryUI_RowDiffWatcherDlg) e.getSource();
                    final List<Integer> pkIndexLst = new ArrayList<Integer>();
                    for (int ii = 0; ii < columnLst.size(); ii++) {
                        String column = columnLst.get(ii);
                        for (String mColumn : dlg.getPkLst()) {
                            if (StringUtils.equals(column, mColumn)) {
                                pkIndexLst.add(ii);
                            }
                        }
                    }
                    if (pkIndexLst.isEmpty()) {
                        JCommonUtil._jOptionPane_showMessageDialog_error("請選擇主鍵!");
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // -------------------------------------------------↓↓↓↓↓↓

                                FastDBQueryUI_RecordWatcherDirectXls mFastDBQueryUI_RecordWatcherDirectXls = new FastDBQueryUI_RecordWatcherDirectXls(fileMiddleName, pkIndexLst);
                                Pair<File, String> result = mFastDBQueryUI_RecordWatcherDirectXls.run(beforeXlsFile, afterXlsFile);
                                File reulstFile = result.getLeft();
                                String errMsg = result.getRight();
                                if (StringUtils.isNotBlank(errMsg)) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error(errMsg);
                                }
                                if (reulstFile != null && reulstFile.exists()) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("檔案產生成功\n" + reulstFile);
                                }
                                // -------------------------------------------------↑↑↑↑↑↑
                            }
                        }).start();
                    }
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mJDlgHolderBringToFrontHandler.remove((JDialog) e.getSource());
                }
            }));
            mJDlgHolderBringToFrontHandler.add(mFastDBQueryUI_RowDiffWatcherDlg);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    // ==============================================================================================
    private void tableColumnConfigBtnAction() {
        if (mXlsColumnDefDlg == null) {
            mXlsColumnDefDlg = new XlsColumnDefDlg();
        }
        mXlsColumnDefDlg.show();
    }

    private class XlsColumnDefDlg {
        private PropertiesUtilBean config = new PropertiesUtilBean(JAR_PATH_FILE, FastDBQueryUI.class.getSimpleName() + "_XlsColumnDefDlg");
        List<XlsColumnDefClz> lst;
        final JDialog dlg;
        final JLabel lbl;
        final JButton btn;
        JTable table;

        private List<XlsColumnDefClz> getConfig() {
            return lst;
        }

        private DefaultTableModel loadConfig() {
            List<XlsColumnDefClz> lst = new ArrayList<XlsColumnDefClz>();
            Properties prop = config.getConfigProp();
            XlsColumnDefClz c1 = new XlsColumnDefClz();
            XlsColumnDefClz c2 = new XlsColumnDefClz();
            XlsColumnDefClz c3 = new XlsColumnDefClz();
            if (prop.containsKey("column")) {
                c1.fromConfig(prop.getProperty("column"));
            } else {
                c1 = XlsColumnDefType.COLUMN.getConfig();
            }
            if (prop.containsKey("chinese")) {
                c2.fromConfig(prop.getProperty("chinese"));
            } else {
                c2 = XlsColumnDefType.CHINESE.getConfig();
            }
            if (prop.containsKey("pk")) {
                c3.fromConfig(prop.getProperty("pk"));
            } else {
                c3 = XlsColumnDefType.PK.getConfig();
            }
            lst.add(c1);
            lst.add(c2);
            lst.add(c3);
            for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
                String key = (String) enu.nextElement();
                if (key.contains("TAG")) {
                    XlsColumnDefClz c4 = new XlsColumnDefClz();
                    c4.fromConfig(prop.getProperty(key));
                    lst.add(c4);
                }
            }
            DefaultTableModel model = JTableUtil.createModel(false, "類型", "標籤字", "index", "含有文字", "顏色");
            for (XlsColumnDefClz cx : lst) {
                model.addRow(cx.toArray());
            }
            return model;
        }

        private List<XlsColumnDefClz> saveAction() {
            Properties prop = config.getConfigProp();
            prop.clear();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            List<XlsColumnDefClz> lst = new ArrayList<XlsColumnDefClz>();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                XlsColumnDefClz c1 = new XlsColumnDefClz();
                c1.type = (XlsColumnDefType) model.getValueAt(ii, 0);
                c1.label = (String) model.getValueAt(ii, 1);
                c1.index = (Integer) model.getValueAt(ii, 2);
                c1.containText = (String) model.getValueAt(ii, 3);
                c1.color = (String) model.getValueAt(ii, 4);
                if (c1.type == XlsColumnDefType.COLUMN) {
                    prop.setProperty("column", c1.toConfig());
                } else if (c1.type == XlsColumnDefType.CHINESE) {
                    prop.setProperty("chinese", c1.toConfig());
                } else if (c1.type == XlsColumnDefType.PK) {
                    prop.setProperty("pk", c1.toConfig());
                } else {
                    prop.setProperty("TAG" + ii, c1.toConfig());
                }
                lst.add(c1);
            }
            config.store();
            return lst;
        }

        private void initTable() {
            TableColumn sportColumn = table.getColumnModel().getColumn(0);
            JComboBox comboBox = new JComboBox();
            for (XlsColumnDefType e : XlsColumnDefType.values()) {
                comboBox.addItem(e);
            }
            sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
        }

        public XlsColumnDefDlg() {
            dlg = new JDialog() {
                public Dimension getPreferredSize() {
                    return new Dimension(600, 350);
                }
            };
            dlg.setModal(true);
            final JPanel pan = new JPanel();
            pan.setLayout(new BorderLayout(0, 0));
            lbl = new JLabel("");
            lbl.setText("設定EXCEL欄位定義");
            pan.add(lbl, BorderLayout.NORTH);
            table = new JTable();
            table.setModel(loadConfig());
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JMouseEventUtil.buttonRightClick(1, e)) {
                        JPopupMenuUtil.newInstance(table).addJMenuItem(JTableUtil.newInstance(table).getDefaultJMenuItems()).applyEvent(e).show();
                    }
                }
            });
            JTableUtil.defaultSetting(table);
            initTable();
            pan.add(JCommonUtil.createScrollComponent(table), BorderLayout.CENTER);
            btn = new JButton("確定");
            pan.add(btn, BorderLayout.SOUTH);
            dlg.getContentPane().add(pan);
            dlg.pack();
            JCommonUtil.setJFrameCenter(dlg);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    lst = saveAction();
                    dlg.dispose();

                    // 重設
                    if (mTableColumnDefTextHandler != null) {
                        mTableColumnDefTextHandler.action();
                    }
                }
            });
        }

        public void show() {
            dlg.setVisible(true);
        }
    }

    // ======================================================================================================================
    private void initColumnXlsDefTableColumnQryTable() {
        try {
            if (mTableColumnDefTextHandler == null) {
                mTableColumnDefTextHandler.init();
            }
            String tableQry = columnXlsDefTableQryText.getText();
            String columnQry = columnXlsDefColumnQryText.getText();
            String otherQry = columnXlsDefOtherQryText.getText();

            Triple<DefaultTableModel, Integer, ActionListener> result = mTableColumnDefTextHandler.query(tableQry, columnQry, otherQry, columnXlsDefTableColumnQryTable, this);
            DefaultTableModel model = result.getLeft();
            columnXlsDefFindRowCountLbl.setText(String.valueOf(result.getMiddle()));
            columnXlsDefTableColumnQryTable.setModel(model);
            result.getRight().actionPerformed(new ActionEvent("", -1, ""));
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    // ======================================================================================================================

    private class S2T_And_T2S_EventHandler {

        JTextField input;

        S2T_And_T2S_EventHandler(JTextField input) {
            this.input = input;
        }

        final Transformer trans = new Transformer() {
            public Object transform(final Object _input) {
                String text = input.getText();
                try {
                    boolean s2t = (Boolean) _input;
                    if (StringUtils.isNotBlank(input.getSelectedText())) {
                        String before = StringUtils.substring(text, 0, input.getSelectionStart());
                        String middle = input.getSelectedText();
                        if (s2t) {
                            middle = JChineseConvertor.getInstance().s2t(middle);
                        } else {
                            middle = JChineseConvertor.getInstance().t2s(middle);
                        }
                        String after = StringUtils.substring(text, input.getSelectionEnd());
                        return before + middle + after;
                    } else {
                        if (s2t) {
                            text = JChineseConvertor.getInstance().s2t(text);
                        } else {
                            text = JChineseConvertor.getInstance().t2s(text);
                        }
                        return text;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return text;
            }
        };

        public JMenuItem getMenuItem(final boolean isS2t) {
            JMenuItem item = new JMenuItem(isS2t ? "簡轉繁" : "繁轉簡");
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    input.setText((String) trans.transform(isS2t));
                }
            });
            return item;
        }

        public MouseAdapter getEvent() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        final JTextField input = (JTextField) e.getSource();
                        if (JMouseEventUtil.buttonRightClick(1, e)) {

                            JPopupMenuUtil.newInstance(input)//
                                    .addJMenuItem("繁轉簡", new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            input.setText((String) trans.transform(false));
                                        }
                                    }).addJMenuItem("簡轉繁", new ActionListener() {

                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            input.setText((String) trans.transform(true));
                                        }
                                    }).applyEvent(e).show();
                        }
                    } catch (Exception ex1) {
                        JCommonUtil.handleException(ex1);
                    }
                }
            };
        }
    }

}