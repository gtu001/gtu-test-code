package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.collection.ListUtil;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil.DBDateFormat;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.file.FileUtil;
import gtu.poi.hssf.ExcelUtil;
import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesGroupUtils_ByKey;
import gtu.properties.PropertiesUtil;
import gtu.string.StringNumberUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTableUtil.ColumnSearchFilter;
import gtu.swing.util.JTextUndoUtil;
import net.sf.json.JSONArray;

public class FastDBQueryUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static File JAR_PATH_FILE = PropertiesUtil.getJarCurrentPath(FastDBQueryUI.class);
    static {
        if (!PropertiesUtil.isClassInJar(FastDBQueryUI.class)) {
            JAR_PATH_FILE = new File("/media/gtu001/OLD_D/my_tool/FastDBQueryUI");
            JAR_PATH_FILE = new File("D:/my_tool/FastDBQueryUI");
        }
    }

    private static final File sqlIdListFile = new File(JAR_PATH_FILE, "sqlList.properties");
    private static Properties sqlIdListProp;
    private static final File sqlIdListDSMappingFile = new File(JAR_PATH_FILE, "sqlList_DS_Mapping.properties");
    private static Properties sqlIdListDSMappingProp;

    // private static PropertiesGroupUtils dataSourceConfig = new
    // PropertiesGroupUtils(new File(JAR_PATH_FILE, "dataSource.properties"));
    private static PropertiesGroupUtils_ByKey dataSourceConfig = new PropertiesGroupUtils_ByKey(new File(JAR_PATH_FILE, "dataSource.properties"));

    private FastDBQueryUI_CrudDlgUI fastDBQueryUI_CrudDlgUI;
    private JPanel contentPane;
    private JList sqlList;
    private JButton sqlSaveButton;
    private JTextArea sqlTextArea;
    private JTextField sqlIdText;
    private JButton clearButton;
    private JButton executeSqlButton;
    private JScrollPane scrollPane_1;
    private JTable parametersTable;
    private JRadioButton updateSqlRadio;
    private JRadioButton querySqlRadio;
    private JPanel panel_5;
    private JTable queryResultTable;
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

    private PropertiesGroupUtils sqlParameterConfigLoad;
    private JButton nextParameterBtn;
    private JButton nextConnBtn;
    private JTextField dbNameIdText;
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

    private Pair<List<String>, List<Object[]>> queryList = null;
    private Pair<List<String>, List<Object[]>> filterRowsQueryList = null;

    private boolean distinctHasClicked = false;// 是否按過distinct btn

    private JButton excelExportBtn;
    private JRadioButton radio_import_excel;
    private JRadioButton radio_export_excel;

    private ButtonGroup btnGroup1;
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
    private JTextField maxRowsText;
    private JLabel lblMaxRows;

    private JFrameRGBColorPanel jFrameRGBColorPanel = null;
    private JButton prevConnBtn;
    private HideInSystemTrayHelper hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FastDBQueryUI frame = new FastDBQueryUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        setBounds(100, 100, 910, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Sql List", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, BorderLayout.CENTER);
        sqlList = new JList();
        sqlList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sqlListMouseClicked(e);
            }
        });

        sqlList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                sqlListKeyPressAction(evt);
            }
        });

        scrollPane.setViewportView(sqlList);

        sqlQueryText = new JTextField();
        sqlQueryText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String txt = sqlQueryText.getText();
                try {
                    // 初始化 sqlList
                    initLoadSqlListConfig(txt);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel.add(sqlQueryText, BorderLayout.NORTH);
        sqlQueryText.setColumns(10);
        sqlQueryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                String txt = JCommonUtil.getDocumentText(event);
                try {
                    // 初始化 sqlList
                    initLoadSqlListConfig(txt);
                } catch (Exception e) {
                    JCommonUtil.handleException(e);
                }
            }
        }));

        JPanel panel_2 = new JPanel();
        tabbedPane.addTab("Sql", null, panel_2, null);
        panel_2.setLayout(new BorderLayout(0, 0));

        sqlTextArea = new JTextArea();
        JTextUndoUtil.applyUndoProcess1(sqlTextArea);
        sqlTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sqlTextAreaMouseClickedAction(e);
            }
        });
        sqlTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0 && //
                e.getKeyCode() == KeyEvent.VK_S) {
                    JCommonUtil.triggerButtonActionPerformed(sqlSaveButton);
                }
            }
        });

        JCommonUtil.createScrollComponent(panel_2, sqlTextArea);
        // panel_2.add(sqlTextArea, BorderLayout.CENTER);

        sqlIdText = new JTextField();
        panel_2.add(sqlIdText, BorderLayout.NORTH);
        sqlIdText.setColumns(10);

        JPanel panel_3 = new JPanel();
        panel_2.add(panel_3, BorderLayout.SOUTH);

        sqlSaveButton = new JButton("儲存");
        panel_3.add(sqlSaveButton);

        clearButton = new JButton("清除");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearButtonClick();
            }
        });
        panel_3.add(clearButton);
        sqlSaveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSqlButtonClick();
            }
        });

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Parameters", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1, BorderLayout.CENTER);

        parametersTable = new JTable();
        scrollPane_1.setViewportView(parametersTable);

        JPanel panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.SOUTH);

        executeSqlButton = new JButton("執行sql");
        executeSqlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeSqlButtonClick();
            }
        });

        lblMaxRows = new JLabel("max rows :");
        panel_4.add(lblMaxRows);

        maxRowsText = new JTextField();
        maxRowsText.addFocusListener(new FocusListener() {
            int tempNumber = 0;

            @Override
            public void focusGained(FocusEvent e) {
                tempNumber = StringNumberUtil.parseInt(maxRowsText.getText(), tempNumber);
            }

            public void focusLost(FocusEvent e) {
                maxRowsText.setText("" + StringNumberUtil.parseInt(maxRowsText.getText(), tempNumber));
            }
        });
        maxRowsText.setToolTipText("n <= 0 表示無限制!");
        maxRowsText.setText("1000");

        panel_4.add(maxRowsText);
        maxRowsText.setColumns(5);

        querySqlRadio = new JRadioButton("查詢模式");
        panel_4.add(querySqlRadio);

        updateSqlRadio = new JRadioButton("修改模式");
        panel_4.add(updateSqlRadio);
        panel_4.add(executeSqlButton);

        panel_5 = new JPanel();
        tabbedPane.addTab("QueryResult", null, panel_5, null);
        panel_5.setLayout(new BorderLayout(0, 0));

        queryResultTable = new JTable();
        queryResultTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode() + "..." + KeyEvent.VK_ENTER);
                if (e.getKeyCode() == KeyEvent.VK_ENTER && queryResultTable.getSelectedRowCount() > 0) {
                    Component source = queryResultTable;
                    int id = -1;
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
        panel_12 = new JPanel();

        panel_5.add(panel_12, BorderLayout.CENTER);
        panel_12.setLayout(new BorderLayout(0, 0));

        panel_13 = new JPanel();
        panel_12.add(panel_13, BorderLayout.NORTH);

        distinctQueryBtn = new JButton("distinct");
        distinctQueryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                distinctQueryBtnAction();
            }
        });

        queryResultCountLabel = new JLabel("          ");
        panel_13.add(queryResultCountLabel);
        panel_13.add(distinctQueryBtn);

        label = new JLabel("欄位過濾");
        panel_13.add(label);

        columnFilterText = new JTextField();
        panel_13.add(columnFilterText);
        columnFilterText.setColumns(15);
        columnFilterText.setToolTipText("分隔符號為\"^\"");
        columnFilterText.addFocusListener(new FocusAdapter() {

            ColumnSearchFilter columnFilter;

            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (distinctHasClicked) {
                        queryModeProcess(queryList, true, null);
                        distinctHasClicked = false;
                    }
                    if (columnFilter == null || isResetQuery) {
                        columnFilter = new ColumnSearchFilter(queryResultTable, "^");
                        isResetQuery = false;
                    }
                    columnFilter.filterText(columnFilterText.getText());
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        lblNewLabel_3 = new JLabel("資料過濾");
        panel_13.add(lblNewLabel_3);

        rowFilterText = new JTextField();
        panel_13.add(rowFilterText);
        rowFilterText.setColumns(15);
        rowFilterText.setToolTipText("分隔符號為\"^\"");
        rowFilterText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    if (queryList == null || queryList.getRight().isEmpty()) {
                        return;
                    }

                    List<Object[]> qList = new ArrayList<Object[]>();

                    FindTextHandler finder = new FindTextHandler(rowFilterText, "^");
                    boolean allMatch = finder.isAllMatch();

                    List<String> cols = queryList.getLeft();
                    for (Object[] rows : queryList.getRight()) {
                        if (allMatch) {
                            qList.add(rows);
                            continue;
                        }

                        B: for (int ii = 0; ii < cols.size(); ii++) {
                            String value = finder.valueToString(rows[ii]);
                            for (String text : finder.getArry()) {
                                if (value.contains(text)) {
                                    qList.add(rows);
                                    break B;
                                }
                            }
                        }
                    }

                    System.out.println("qList - " + qList.size());

                    // 過濾欄位紀錄
                    filterRowsQueryList = Pair.of(cols, qList);

                    queryModeProcess(filterRowsQueryList, true, null);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        panel_14 = new JPanel();
        panel_12.add(panel_14, BorderLayout.WEST);

        panel_15 = new JPanel();
        panel_12.add(panel_15, BorderLayout.SOUTH);

        radio_import_excel = new JRadioButton("匯入excel");
        panel_15.add(radio_import_excel);

        radio_export_excel = new JRadioButton("匯出excel");
        panel_15.add(radio_export_excel);

        btnGroup1 = JButtonGroupUtil.createRadioButtonGroup(radio_import_excel, radio_export_excel);

        excelExportBtn = new JButton("動作");
        excelExportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excelExportBtnAction();
            }
        });
        panel_15.add(excelExportBtn);

        panel_16 = new JPanel();
        panel_12.add(panel_16, BorderLayout.EAST);

        panel_12.add(JTableUtil.getScrollPane(queryResultTable), BorderLayout.CENTER);

        panel_7 = new JPanel();
        tabbedPane.addTab("JSON", null, panel_7, null);
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
        panel_7.add(JCommonUtil.createScrollComponent(queryResultJsonTextArea), BorderLayout.CENTER);

        panel_6 = new JPanel();
        tabbedPane.addTab("Connection", null, panel_6, null);
        panel_6.setLayout(new FormLayout(
                new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        saveConnectionBtn = new JButton("儲存");
        saveConnectionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveConnectionBtnClick();
            }
        });

        lblDbName = new JLabel("DB Name");
        panel_6.add(lblDbName, "4, 2");

        dbNameIdText = new JTextField();
        dbNameIdText.setColumns(10);
        panel_6.add(dbNameIdText, "10, 2, fill, default");

        lblUrl = new JLabel("url");
        panel_6.add(lblUrl, "4, 6");

        dbUrlText = new JTextField();
        panel_6.add(dbUrlText, "10, 6, fill, default");
        dbUrlText.setColumns(10);

        lblNewLabel = new JLabel("user");
        panel_6.add(lblNewLabel, "4, 10");

        dbUserText = new JTextField();
        panel_6.add(dbUserText, "10, 10, fill, default");
        dbUserText.setColumns(10);

        lblNewLabel_1 = new JLabel("pwd");
        panel_6.add(lblNewLabel_1, "4, 14");

        dbPwdText = new JTextField();
        panel_6.add(dbPwdText, "10, 14, fill, default");
        dbPwdText.setColumns(10);

        lblNewLabel_2 = new JLabel("driver");
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

        removeConnectionBtn = new JButton("刪除設定");
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

        nextParameterBtn = new JButton("下一組設定");
        nextParameterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextParameterBtnClick();
            }
        });
        panel_4.add(nextParameterBtn);

        {
            // 初始化datasource
            this.initDataSourceProperties();

            // 初始化parameterTable
            initParametersTable();

            // 初始化queryResultTable
            JTableUtil.defaultSetting(queryResultTable);

            // radio 設群組
            JCommonUtil.createRadioButtonGroup(querySqlRadio, updateSqlRadio);
            querySqlRadio.setSelected(true);

            deleteParameterBtn = new JButton("刪除設定");
            deleteParameterBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteParameterBtnAction();
                }
            });
            panel_4.add(deleteParameterBtn);

            // 初始化 sqlList
            initLoadSqlListConfig("");
            initLoadSqlIdMappingConfig();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.defaultToolTipDelay();
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/big_boobs.ico");
            this.setTitle("You Set My World On Fire");

            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            jFrameRGBColorPanel.start();
            panel_17.add(jFrameRGBColorPanel.getToggleButton());

            hideInSystemTrayHelper.apply(this);
            panel_17.add(hideInSystemTrayHelper.getToggleButton());
        }
    }

    private void initParametersTable() {
        JTableUtil.defaultSetting_AutoResize(parametersTable);
        DefaultTableModel createModel = JTableUtil.createModel(false, new Object[] { "參數", "值", "類型" });
        parametersTable.setModel(createModel);

        // column = "Data Type"
        TableColumn sportColumn = parametersTable.getColumnModel().getColumn(2);
        JComboBox comboBox = new JComboBox();
        for (DataType e : DataType.values()) {
            comboBox.addItem(e);
        }
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private enum DataType {
        varchar(String.class) {
        }, //
        date(java.sql.Date.class) {
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
        timestamp(java.sql.Timestamp.class) {
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
        number(Number.class) {
        }, //
        NULL(void.class) {
        }, //
        UNKNOW(void.class) {
        },//
        ;

        final Class<?>[] clz;

        DataType(Class<?>... clz) {
            this.clz = clz;
        }

        protected Object applyDataChange(Object value) {
            return value;
        }
    }

    static class FindTextHandler {
        JTextField searchText;
        String delimit;

        FindTextHandler(JTextField searchText, String delimit) {
            this.searchText = searchText;
            this.delimit = delimit;
        }

        boolean isAllMatch() {
            return StringUtils.isBlank(searchText.getText());
        }

        String[] getArry() {
            String[] arry = StringUtils.trimToEmpty(searchText.getText()).toLowerCase().split(Pattern.quote(delimit), -1);
            List<String> rtnLst = new ArrayList<String>();
            for (int ii = 0; ii < arry.length; ii++) {
                if (StringUtils.isNotBlank(arry[ii])) {
                    rtnLst.add(StringUtils.trimToEmpty(arry[ii]));
                }
            }
            return rtnLst.toArray(new String[0]);
        }

        String valueToString(Object value) {
            return value == null ? "" : String.valueOf(value).toLowerCase();
        }
    }

    /**
     * 初始化sqlList
     */
    private void initLoadSqlListConfig(String queryText) throws IOException {
        if (!sqlIdListFile.exists()) {
            sqlIdListFile.createNewFile();
        }
        Properties prop = new Properties();
        PropertiesUtil.loadProperties(new FileInputStream(sqlIdListFile), prop);
        sqlIdListProp = prop;

        List<String> sqlIdList = new ArrayList<String>();
        for (Enumeration enu = sqlIdListProp.keys(); enu.hasMoreElements();) {
            String sqlId = (String) enu.nextElement();
            if (StringUtils.isBlank(queryText)) {
                sqlIdList.add(sqlId);
            } else if (StringUtils.isNotBlank(queryText) && sqlId.contains(queryText)) {
                sqlIdList.add(sqlId);
            }
        }
        Collections.sort(sqlIdList);

        DefaultListModel model = JListUtil.createModel();
        for (String s : sqlIdList) {
            model.addElement(s);
        }
        sqlList.setModel(model);
    }

    private void initLoadSqlIdMappingConfig() throws IOException {
        if (!sqlIdListDSMappingFile.exists()) {
            sqlIdListDSMappingFile.createNewFile();
        }
        Properties prop = new Properties();
        PropertiesUtil.loadProperties(new FileInputStream(sqlIdListDSMappingFile), prop);
        sqlIdListDSMappingProp = prop;
    }

    private void storeSqlIdListDsMappingProp() throws IOException {
        String sqlId = (String) sqlList.getSelectedValue();
        if (StringUtils.isBlank(sqlId)) {
            sqlId = StringUtils.trimToEmpty(sqlIdText.getText());
        }
        String dbNameId = dbNameIdText.getText();
        this.initLoadSqlIdMappingConfig();
        sqlIdListDSMappingProp.setProperty(sqlId, dbNameId);
        PropertiesUtil.storeProperties(sqlIdListDSMappingProp, sqlIdListDSMappingFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
    }

    /**
     * 初始化dataSource
     */
    private void initDataSourceProperties() throws IOException {
        Map<String, String> param = dataSourceConfig.loadConfig();
        if (param.containsKey(PropertiesGroupUtils_ByKey.SAVE_KEYS) && StringUtils.isNotBlank(param.get(PropertiesGroupUtils_ByKey.SAVE_KEYS))) {
            dbNameIdText.setText(param.get(PropertiesGroupUtils_ByKey.SAVE_KEYS));
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
            String dbNameId = dbNameIdText.getText();
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

            Class.forName(driver);
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
    }

    /**
     * 儲存sql
     */
    private void saveSqlButtonClick() {
        try {
            String sqlId = sqlIdText.getText().toString();
            String sql = sqlTextArea.getText().toString();
            JCommonUtil.isBlankErrorMsg(sqlId, "請輸入sql Id");
            JCommonUtil.isBlankErrorMsg(sql, "請輸入sql");

            SqlParam param = parseSqlToParam(sql);

            // 更新parameter表
            setParameterTable(param);

            // 儲存sqlList Prop
            this.saveSqlListProp(sqlId, sql);

            // 載入參數設定
            sqlParameterConfigLoad = new PropertiesGroupUtils(new File(JAR_PATH_FILE, "param_" + sqlId + ".properties"));

            // 刷新sqlList
            initLoadSqlListConfig("");
            initLoadSqlIdMappingConfig();
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 載入參數
     */
    private void setParameterTable(SqlParam param) {
        initParametersTable();
        DefaultTableModel createModel = (DefaultTableModel) parametersTable.getModel();
        for (String column : param.paramSet) {
            createModel.addRow(new Object[] { column, "", DataType.varchar });
        }
    }

    /**
     * 儲存prop
     */
    private void saveSqlListProp(String sqlId, String sql) throws IOException {
        Properties prop = sqlIdListProp;
        if (StringUtils.isNotBlank(sqlId) && StringUtils.isNotBlank(sql)) {
            prop.put(sqlId, sql);
        }
        PropertiesUtil.storeProperties(prop, sqlIdListFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
        System.out.println("儲存檔案路徑 : " + sqlIdListFile);
    }

    private Object getRealValue(String value, DataType dataType) {
        return dataType.applyDataChange(value);
    }

    /**
     * 執行sql
     */
    private void executeSqlButtonClick() {
        try {
            // init
            {
                isResetQuery = true;
                filterRowsQueryList = null;// rows 過濾清除
            }

            JTableUtil util = JTableUtil.newInstance(parametersTable);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            for (int ii = 0; ii < parametersTable.getRowCount(); ii++) {
                String columnName = (String) util.getRealValueAt(ii, 0);
                String value = (String) util.getRealValueAt(ii, 1);
                DataType dataType = (DataType) util.getRealValueAt(ii, 2);
                paramMap.put(columnName, getRealValue(value, dataType));
            }

            String sql = sqlTextArea.getText().toString();
            JCommonUtil.isBlankErrorMsg(sql, "請輸入sql");

            // 取得執行sql物件
            SqlParam param = parseSqlToParam(sql);

            // 檢查參數是否異動
            for (String columnName : param.paramSet) {
                if (!paramMap.containsKey(columnName)) {
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
                parameterList.addAll(((SqlParam_IfExists) param).processParamMap(paramMap));
                System.out.println("=====================================================");
                System.out.println(param.getQuestionSql());
                System.out.println(parameterList);
                System.out.println("=====================================================");
            }

            // 判斷執行模式
            if (querySqlRadio.isSelected()) {
                int maxRowsLimit = StringNumberUtil.parseInt(maxRowsText.getText(), 0);
                Pair<List<String>, List<Object[]>> queryList = JdbcDBUtil.queryForList_customColumns(param.getQuestionSql(), parameterList.toArray(), this.getDataSource().getConnection(), true,
                        maxRowsLimit);
                this.queryList = queryList;
                this.queryModeProcess(queryList, false, Pair.of(param, parameterList));
                this.showJsonArry(queryList);
            } else if (updateSqlRadio.isSelected()) {
                int modifyResult = JdbcDBUtil.modify(param.questionSql, parameterList.toArray(), this.getDataSource().getConnection(), true);
                JCommonUtil._jOptionPane_showMessageDialog_info("update : " + modifyResult);
            }

            // 儲存參數設定
            if (sqlParameterConfigLoad != null) {
                Map<String, String> paramMap2 = new HashMap<String, String>();
                JTableUtil util2 = JTableUtil.newInstance(parametersTable);
                DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
                for (int ii = 0; ii < model.getRowCount(); ii++) {
                    String col = (String) util2.getRealValueAt(ii, 0);
                    String val = (String) util2.getRealValueAt(ii, 1);
                    paramMap2.put(col, val);
                }
                try {
                    // 一般儲存參數處理
                    sqlParameterConfigLoad.saveConfig(paramMap2);
                } catch (Exception ex) {
                    // 出現異常詢問是否重設
                    boolean resetOk = false;
                    if (ex.getMessage().contains("參數不同")) {
                        boolean resetConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(ex.getMessage(), "是否要重設?");
                        if (resetConfirm) {
                            sqlParameterConfigLoad.clear();
                            sqlParameterConfigLoad.saveConfig(paramMap2);
                            resetOk = true;
                        }
                    }
                    if (!resetOk) {
                        throw ex;
                    }
                }
            }

            // 儲存sqlId mapping dataSource 設定
            storeSqlIdListDsMappingProp();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void showJsonArry(Pair<List<String>, List<Object[]>> queryList) {
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
            queryResultJsonTextArea.setText(JSONArray.fromObject(cloneLst).toString());
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
    private void queryModeProcess(Pair<List<String>, List<Object[]>> queryList, boolean silent, Pair<SqlParam, List<Object>> pair) {
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
            return;
        } else {
            if (!silent) {
                JCommonUtil._jOptionPane_showMessageDialog_info("size : " + queryList.getRight().size());
            }
            queryResultCountLabel.setText(String.valueOf(queryList.getRight().size()));
        }

        // 取得標題
        String[] titles = queryList.getLeft().toArray(new String[0]);

        // 查詢結果table
        DefaultTableModel createModel = JTableUtil.createModel(true, titles);
        queryResultTable.setModel(createModel);
        JTableUtil.setColumnWidths(queryResultTable, getInsets());
        for (Object[] rows : queryList.getRight()) {
            createModel.addRow(rows);
        }
    }

    private DefaultTableModel getFakeDataModel(Pair<SqlParam, List<Object>> pair) {
        TableInfo tabInfo = new TableInfo();
        DefaultTableModel model = JTableUtil.createModel(true, new Object[0]);
        List<Object[]> queryLst = new ArrayList<Object[]>();
        try {
            Map<String, Object> valMap = new LinkedHashMap<String, Object>();
            tabInfo.execute(pair.getLeft().getQuestionSql(), pair.getRight().toArray(), this.getDataSource().getConnection());
            List<String> columns = new ArrayList<String>(tabInfo.getColumns());
            model = JTableUtil.createModel(true, columns.toArray());
            List<Object> arry = new ArrayList<Object>();
            for (int ii = 0; ii < columns.size(); ii++) {
                String col = columns.get(ii);
                Object val = null;
                if (tabInfo.getNumberCol().contains(col)) {
                    val = (0);
                } else if (tabInfo.getDateCol().contains(col)) {
                    val = (new java.sql.Date(System.currentTimeMillis()));
                } else if (tabInfo.getTimestampCol().contains(col)) {
                    val = (new java.sql.Timestamp(System.currentTimeMillis()));
                } else {
                    val = ("1");
                }
                arry.add(val);
            }
            queryLst.add(arry.toArray());
            model.addRow(arry.toArray());
            this.queryList = Pair.of(columns, queryLst);
        } catch (Exception e) {
        }
        return model;
    }

    /**
     * parse Sql
     */
    private SqlParam parseSqlToParam(String sql) {
        // 中括號特殊處理
        int matchCount = 0;
        if ((matchCount = StringUtils.countMatches(sql, "[")) == StringUtils.countMatches(sql, "]")) {
            if (matchCount != 0) {
                return SqlParam_IfExists.parseToSqlParam(sql);
            }
        }

        // 一般處理
        Pattern ptn = Pattern.compile("\\:(\\w+)");
        Matcher mth = ptn.matcher(sql);

        List<String> paramList = new ArrayList<String>();
        Set<String> paramSet = new LinkedHashSet<String>();

        StringBuffer sb2 = new StringBuffer();

        while (mth.find()) {
            String key = mth.group(1);
            paramList.add(key);
            paramSet.add(key);
            mth.appendReplacement(sb2, "?");
        }
        mth.appendTail(sb2);

        SqlParam out = new SqlParam();
        out.orginialSql = sql;
        out.paramSet = paramSet;
        out.questionSql = sb2.toString();
        out.paramList = paramList;
        return out;
    }

    private static class SqlParam {
        String orginialSql;
        String questionSql;
        Set<String> paramSet = new LinkedHashSet<String>();
        List<String> paramList = new ArrayList<String>();

        public String getQuestionSql() {
            return questionSql;
        }
    }

    private static class SqlParam_IfExists extends SqlParam {
        List<Pair<List<String>, int[]>> paramListFix = new ArrayList<Pair<List<String>, int[]>>();

        private boolean isAllOk(List<String> paramLst, Map<String, Object> paramMap) {
            for (String col : paramLst) {
                if (paramMap.containsKey(col) && paramMap.get(col) != null) {
                    if (paramMap.get(col) instanceof String) {
                        String tmpParamVal = StringUtils.trimToEmpty((String) paramMap.get(col));
                        if (StringUtils.isBlank(tmpParamVal)) {
                            return false;
                        }
                    } else {
                        // TODO
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        public static SqlParam_IfExists parseToSqlParam(String sql) {
            SqlParam_IfExists sqlParam = new SqlParam_IfExists();
            sqlParam.orginialSql = sql;

            Pattern ptn = Pattern.compile("(\\[((?:.|\n)*?)\\]|\\:(\\w+))");
            Matcher mth = ptn.matcher(sql);

            while (mth.find()) {
                String quoteLine = mth.group(1);

                // 非必填檢查
                if (quoteLine.matches("^\\[(.|\n)*\\]")) {
                    String realQuoteLine = mth.group(2);
                    Pattern ptn2 = Pattern.compile("\\:(\\w+)");
                    Matcher mth2 = ptn2.matcher(realQuoteLine);

                    List<String> params = new ArrayList<String>();
                    while (mth2.find()) {
                        params.add(mth2.group(1));
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

        private String toQuestionMarkSql(String markSql, List<Object> rtnParamLst, Map<String, Object> paramMap) {
            Pattern ptn = Pattern.compile(":(\\w+)");
            Matcher mth = ptn.matcher(markSql);
            StringBuffer sb = new StringBuffer();

            while (mth.find()) {
                String col = mth.group(1);
                Object value = paramMap.get(col);

                rtnParamLst.add(value);
                String replaceVal = StringUtils.rightPad("?", mth.group().length());

                mth.appendReplacement(sb, replaceVal);
            }
            mth.appendTail(sb);

            String rtnStr = sb.toString().replaceAll("[\\[\\]]", " ");
            return rtnStr;
        }

        public List<Object> processParamMap(Map<String, Object> paramMap) {
            String orginialSqlBackup = this.orginialSql.toString();

            List<Object> rtnParamLst = new ArrayList<Object>();

            for (Pair<List<String>, int[]> row : paramListFix) {
                int[] start_end = row.getRight();

                String markSql = orginialSqlBackup.substring(start_end[0], start_end[1]);
                String replaceToSql = StringUtils.rightPad("", markSql.length());

                if (isAllOk(row.getLeft(), paramMap) || markSql.matches("\\:\\w+")) {
                    replaceToSql = this.toQuestionMarkSql(markSql, rtnParamLst, paramMap);
                }

                orginialSqlBackup = orginialSqlBackup.substring(0, start_end[0]) + //
                        replaceToSql + //
                        orginialSqlBackup.substring(start_end[1]);
            }

            this.questionSql = orginialSqlBackup;

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
        String sqlId = JListUtil.getLeadSelectionObject(sqlList);
        System.out.println("sqlId : " + sqlId);

        String sql = sqlIdListProp.getProperty(sqlId);
        sqlIdText.setText(sqlId);
        sqlTextArea.setText(sql);

        // 載入參數設定
        sqlParameterConfigLoad = new PropertiesGroupUtils(new File(JAR_PATH_FILE, "param_" + sqlId + ".properties"));
        loadParameterTableConfig();

        // 判斷是否要自動切換dataSource
        loadSqlIdMappingDataSourceConfig();

        // trigger 儲存按鈕
        JCommonUtil.triggerButtonActionPerformed(sqlSaveButton);
    }

    private void loadSqlIdMappingDataSourceConfig() {
        try {
            initLoadSqlIdMappingConfig();
            String sqlId = JListUtil.getLeadSelectionObject(sqlList);
            if (sqlIdListDSMappingProp.containsKey(sqlId)) {
                String saveKey = sqlIdListDSMappingProp.getProperty(sqlId);
                if (!StringUtils.equals(dbNameIdText.getText(), saveKey)) {
                    boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("目前DS為 : [" + dbNameIdText.getText() + "] \n 是否要切換為最後一次成功使用的DS :[" + saveKey + "], ", "切換dataSource");
                    if (confirm) {
                        Map<String, String> param = dataSourceConfig.getConfig(saveKey);
                        if (param.containsKey(PropertiesGroupUtils_ByKey.SAVE_KEYS) && StringUtils.isNotBlank(param.get(PropertiesGroupUtils_ByKey.SAVE_KEYS))) {
                            dbNameIdText.setText(param.get(PropertiesGroupUtils_ByKey.SAVE_KEYS));
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
        Map<String, String> paramMap = sqlParameterConfigLoad.loadConfig();
        initParametersTable();
        DefaultTableModel model = (DefaultTableModel) parametersTable.getModel();
        for (String col : paramMap.keySet()) {
            String val = paramMap.get(col);
            model.addRow(new Object[] { col, val, DataType.varchar });
        }
    }

    /**
     * 讀取下一組參數設定
     */
    private void nextParameterBtnClick() {
        if (sqlParameterConfigLoad == null) {
            return;
        }
        sqlParameterConfigLoad.next();
        loadParameterTableConfig();
    }

    /**
     * 下一組連線設定
     */
    private void nextConnBtnClick() {
        try {
            dataSourceConfig.next();
            initDataSourceProperties();
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
            initDataSourceProperties();
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
        return bds;
    }

    private void queryResultTableMouseClickAction(MouseEvent e) {
        try {
            if (JMouseEventUtil.buttonLeftClick(2, e)) {
                Validate.isTrue(this.queryList != null && !this.queryList.getRight().isEmpty(), "查詢結果為空!");

                if (fastDBQueryUI_CrudDlgUI != null && fastDBQueryUI_CrudDlgUI.isShowing()) {
                    fastDBQueryUI_CrudDlgUI.dispose();
                }

                JTableUtil jutil = JTableUtil.newInstance(queryResultTable);
                int[] orignRowPosArry = queryResultTable.getSelectedRows();

                List<Map<String, Object>> rowMapLst = new ArrayList<Map<String, Object>>();
                for (int orignRowPos : orignRowPosArry) {
                    System.out.println("orignRowPos " + orignRowPos);
                    int rowPos = JTableUtil.getRealRowPos(orignRowPos, queryResultTable);
                    System.out.println("rowPos " + rowPos);

                    int queryLstIndex = transRealRowToQuyerLstIndex(rowPos);
                    Map<String, Object> rowMap = getDetailToMap(this.queryList, queryLstIndex);
                    rowMapLst.add(rowMap);
                }

                Pair<List<String>, List<Object[]>> allRows = null;
                if (filterRowsQueryList != null) {
                    allRows = filterRowsQueryList;
                } else {
                    allRows = queryList;
                }

                fastDBQueryUI_CrudDlgUI = FastDBQueryUI_CrudDlgUI.newInstance(rowMapLst, getRandom_TableNSchema(), allRows, this);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private String getRandom_TableNSchema() {
        Pattern ptn = Pattern.compile("from\\s+(\\w+\\.\\w+|\\w+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        String sql = StringUtils.trimToEmpty(sqlTextArea.getText());
        Matcher mth = ptn.matcher(sql);
        if (mth.find()) {
            return mth.group(1);
        }
        return "";
    }

    private int transRealRowToQuyerLstIndex(int realRow) {
        TreeMap<Integer, String> columnMapping = new TreeMap<Integer, String>();
        JTableUtil util = JTableUtil.newInstance(queryResultTable);

        for (int ii = 0; ii < queryResultTable.getColumnCount(); ii++) {
            TableColumn column = queryResultTable.getTableHeader().getColumnModel().getColumn(ii);
            String columnHeader = (String) column.getHeaderValue();

            for (int jj = 0; jj < this.queryList.getLeft().size(); jj++) {
                String columnHeader2 = this.queryList.getLeft().get(jj);
                if (!columnMapping.containsKey(jj) && columnHeader.equalsIgnoreCase(columnHeader2)) {
                    columnMapping.put(jj, columnHeader2);
                }
            }
        }
        System.out.println(columnMapping);

        TreeMap<Integer, Object> map = new TreeMap<Integer, Object>();
        for (int col = 0; col < queryResultTable.getColumnCount(); col++) {
            int realCol = util.getRealColumnPos(col, queryResultTable);
            Object value = util.getModel().getValueAt(realRow, realCol);
            map.put(realCol, value);
        }

        // 用來比較取得row index用
        List<Object[]> newLst = new ArrayList<Object[]>();
        for (Object[] oldArry : this.queryList.getRight()) {
            List<Object> newArry = new ArrayList<Object>();
            for (int columnPos : columnMapping.keySet()) {
                newArry.add(oldArry[columnPos]);
            }
            newLst.add(newArry.toArray());
        }

        Object[] arry = map.values().toArray();
        return isContainObjectArray_Index(newLst, arry);
    }

    private Map<String, Object> getDetailToMap(Pair<List<String>, List<Object[]>> queryList2, int queryListIndex) {
        List<String> columns = queryList.getLeft();
        List<Map<String, Object>> cloneLst = new ArrayList<Map<String, Object>>();

        Object[] row = queryList.getRight().get(queryListIndex);

        Map<String, List<Object>> multiMap = new HashMap<String, List<Object>>();
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

    private void excelExportBtnAction() {
        try {
            ExcelUtil exlUtl = ExcelUtil.getInstance();
            AbstractButton selBtn = JButtonGroupUtil.getSelectedButton(btnGroup1);
            if (radio_import_excel == selBtn) {
                File xlsfile = JCommonUtil._jFileChooser_selectFileOnly();
                if (!xlsfile.exists() || !xlsfile.getName().endsWith(".xls")) {
                    JCommonUtil._jOptionPane_showMessageDialog_info("檔案錯誤(.xls)!");
                    return;
                }
                HSSFWorkbook wk = exlUtl.readExcel(xlsfile);
                HSSFSheet sheet = wk.getSheetAt(0);

                DefaultTableModel model = null;
                for (int ii = 0; ii <= 0; ii++) {
                    Row row = sheet.getRow(ii);
                    List<Object> titles = new ArrayList<Object>();
                    for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                        String value = ExcelUtil.getInstance().readCell(row.getCell(jj));
                        titles.add(value);
                    }
                    model = JTableUtil.createModel(true, titles.toArray());
                    queryResultTable.setModel(model);
                }

                for (int ii = 1; ii <= sheet.getLastRowNum(); ii++) {
                    Row row = sheet.getRow(ii);
                    if (row == null) {
                        continue;
                    }
                    List<Object> rows = new ArrayList<Object>();
                    for (int jj = 0; jj < row.getLastCellNum(); jj++) {
                        String value = ExcelUtil.getInstance().readCell(row.getCell(jj));
                        rows.add(value);
                    }
                    model.addRow(rows.toArray());
                }

            } else if (radio_export_excel == selBtn) {
                if (queryList == null || queryList.getRight().isEmpty()) {
                    JCommonUtil._jOptionPane_showMessageDialog_info("沒有資料!");
                    return;
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
                    int col1 = JTableUtil.getRealColumnPos(0, parametersTable);
                    int val1 = JTableUtil.getRealColumnPos(1, parametersTable);
                    Object col = paramUtl.getRealValueAt(JTableUtil.getRealRowPos(ii, parametersTable), col1);
                    Object val = paramUtl.getRealValueAt(JTableUtil.getRealRowPos(ii, parametersTable), val1);

                    exlUtl.getCellChk(exlUtl.getRowChk(sheet2, sqlRowPos), 0).setCellValue(String.valueOf(col));
                    exlUtl.getCellChk(exlUtl.getRowChk(sheet2, sqlRowPos), 1).setCellValue(String.valueOf(val));
                    sqlRowPos++;
                }

                // 寫資料
                List<String> columns = new ArrayList<String>(queryList.getLeft());
                HSSFRow titleRow0 = sheet0.createRow(0);
                for (int ii = 0; ii < columns.size(); ii++) {
                    exlUtl.setCellValue(exlUtl.getCellChk(titleRow0, ii), columns.get(ii));
                }
                HSSFRow titleRow1 = sheet1.createRow(0);
                for (int ii = 0; ii < columns.size(); ii++) {
                    exlUtl.setCellValue(exlUtl.getCellChk(titleRow1, ii), columns.get(ii));
                }

                for (int ii = 0; ii < queryList.getRight().size(); ii++) {
                    Row row_string = sheet0.createRow(ii + 1);
                    Row row_orign$ = sheet1.createRow(ii + 1);

                    Object[] rows = queryList.getRight().get(ii);
                    for (int jj = 0; jj < columns.size(); jj++) {
                        String col = columns.get(jj);
                        Object value = rows[jj];

                        exlUtl.setCellValue(exlUtl.getCellChk(row_string, jj), String.valueOf(value));
                        exlUtl.setCellValue(exlUtl.getCellChk(row_orign$, jj), value);
                    }
                }

                String filename = FastDBQueryUI.class.getSimpleName() + "_Export_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".xls";
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
            String dbNameId = dbNameIdText.getText();
            boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定要刪除:" + dbNameId, "刪除設定");
            if (confirm) {
                dataSourceConfig.removeConfig(dbNameId);
                JCommonUtil._jOptionPane_showMessageDialog_info("刪除成功! : dbNameId");
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
            Pair<List<String>, List<Object[]>> queryResultFinal = Pair.of(matchColumnLst, queryLst);
            this.queryModeProcess(queryResultFinal, true, null);
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

    private void sqlListKeyPressAction(KeyEvent evt) {
        try {
            JListUtil.newInstance(sqlList).defaultJListKeyPressed(evt, false);
            // 刪除
            if (evt.getKeyCode() == 127) {
                String sqlId = JListUtil.getLeadSelectionObject(sqlList);
                String sql = sqlIdListProp.getProperty(sqlId);

                boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("刪除 : " + sqlId + "\nSQL : " + sql, "是否刪除 : " + sqlId);
                if (deleteConfirm) {

                    // 刪除參數黨
                    File paramFile = new File(JAR_PATH_FILE, "param_" + sqlId + ".properties");
                    if (paramFile.exists()) {
                        paramFile.delete();
                    }

                    // 刪除sqlId
                    if (!paramFile.exists()) {
                        sqlIdListProp.remove(sqlId);
                        saveSqlListProp("", "");

                        JListUtil.removeElement(sqlList, sqlId);

                        // 移除db config mapping
                        sqlIdListDSMappingProp.remove(sqlId);
                        PropertiesUtil.storeProperties(sqlIdListDSMappingProp, sqlIdListDSMappingFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd HHmmss"));
                    }

                    JCommonUtil._jOptionPane_showMessageDialog_info("刪除" + (!paramFile.exists() ? "成功" : "失敗"));
                }
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
