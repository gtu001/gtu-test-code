package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.db.JdbcDBUtil;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.PropertiesGroupUtils;
import gtu.swing.util.PropertiesGroupUtils_ByKey;

public class FastDBQueryUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final File JAR_PATH_FILE = PropertiesUtil.getJarCurrentPath(FastDBQueryUI.class);
    // private static final File JAR_PATH_FILE = new
    // File("D:\\my_tool\\db_tool");

    private static final File sqlIdListFile = new File(JAR_PATH_FILE, "sqlList.properties");
    private static Properties sqlIdListProp;

    // private static PropertiesGroupUtils dataSourceConfig = new
    // PropertiesGroupUtils(new File(JAR_PATH_FILE, "dataSource.properties"));
    private static PropertiesGroupUtils_ByKey dataSourceConfig = new PropertiesGroupUtils_ByKey(new File(JAR_PATH_FILE, "dataSource.properties"));

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

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FastDBQueryUI frame = new FastDBQueryUI();
                    frame.setVisible(true);
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
        setBounds(100, 100, 609, 454);
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
        scrollPane.setViewportView(sqlList);

        sqlQueryText = new JTextField();
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

        querySqlRadio = new JRadioButton("查詢模式");
        panel_4.add(querySqlRadio);

        updateSqlRadio = new JRadioButton("修改模式");
        panel_4.add(updateSqlRadio);
        panel_4.add(executeSqlButton);

        panel_5 = new JPanel();
        tabbedPane.addTab("QueryResult", null, panel_5, null);
        panel_5.setLayout(new BorderLayout(0, 0));

        queryResultTable = new JTable();
        panel_5.add(JTableUtil.getScrollPane(queryResultTable), BorderLayout.CENTER);

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

        nextConnBtn = new JButton("下一組");
        nextConnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextConnBtnClick();
            }
        });
        panel_6.add(nextConnBtn, "6, 22");

        panel_7 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_7, null);
        panel_7.setLayout(new FormLayout(new ColumnSpec[] {}, new RowSpec[] {}));

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
            JTableUtil.defaultSetting_AutoResize(parametersTable);
            DefaultTableModel createModel = JTableUtil.createModel(false, new Object[] { "參數", "值" });
            parametersTable.setModel(createModel);

            // 初始化queryResultTable
            JTableUtil.defaultSetting(queryResultTable);

            // radio 設群組
            JCommonUtil.createRadioButtonGroup(querySqlRadio, updateSqlRadio);
            querySqlRadio.setSelected(true);

            // 初始化 sqlList
            initLoadSqlListConfig("");
        }
    }

    /**
     * 初始化sqlList
     */
    private void initLoadSqlListConfig(String queryText) throws FileNotFoundException, IOException {
        if (!sqlIdListFile.exists()) {
            sqlIdListFile.createNewFile();
        }
        Properties prop = new Properties();
        prop.load(new FileInputStream(sqlIdListFile));
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

            // 刷新sqlList
            initLoadSqlListConfig("");
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 載入參數
     */
    private void setParameterTable(SqlParam param) {
        DefaultTableModel createModel = JTableUtil.createModel(false, new Object[] { "參數", "值" });
        parametersTable.setModel(createModel);
        for (String column : param.paramSet) {
            createModel.addRow(new Object[] { column, "" });
        }
    }

    /**
     * 儲存prop
     */
    private void saveSqlListProp(String sqlId, String sql) throws IOException {
        Properties prop = sqlIdListProp;
        prop.put(sqlId, sql);
        prop.store(new FileOutputStream(sqlIdListFile), "寫入" + DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss"));
        System.out.println("儲存檔案路徑 : " + sqlIdListFile);
    }

    private Object getRealValue(String value) {
        return value;
    }

    /**
     * 執行sql
     */
    private void executeSqlButtonClick() {
        try {
            JTableUtil util = JTableUtil.newInstance(parametersTable);
            Map<String, Object> paramMap = new HashMap<String, Object>();
            for (int ii = 0; ii < parametersTable.getRowCount(); ii++) {
                String columnName = (String) util.getRealValueAt(ii, 0);
                String value = (String) util.getRealValueAt(ii, 1);
                paramMap.put(columnName, getRealValue(value));
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
            for (String columnName : param.paramList) {
                if (!paramMap.containsKey(columnName)) {
                    Validate.isTrue(false, "參數未設定 : " + columnName);
                }
                parameterList.add(paramMap.get(columnName));
            }

            // 判斷執行模式
            if (querySqlRadio.isSelected()) {
                List<Map<String, Object>> queryList = JdbcDBUtil.queryForList(param.questionSql, parameterList.toArray(), this.getDataSource().getConnection(), true);
                this.queryModeProcess(queryList);
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
                sqlParameterConfigLoad.saveConfig(paramMap2);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 查詢模式
     */
    private void queryModeProcess(List<Map<String, Object>> queryList) {
        if (queryList.isEmpty()) {
            JCommonUtil._jOptionPane_showMessageDialog_info("查無資料!");
            queryResultTable.setModel(JTableUtil.createModel(true, new Object[0]));
            return;
        } else {
            JCommonUtil._jOptionPane_showMessageDialog_info("size : " + queryList.size());
        }

        // 取得標題
        Object[] titles = queryList.get(0).keySet().toArray();

        // 查詢結果table
        DefaultTableModel createModel = JTableUtil.createModel(true, titles);
        queryResultTable.setModel(createModel);
        JTableUtil.setColumnWidths(queryResultTable, getInsets());
        for (Map<String, Object> map : queryList) {
            createModel.addRow(map.values().toArray());
        }
    }

    /**
     * parse Sql
     */
    private SqlParam parseSqlToParam(String sql) {
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
    }

    /**
     * 將設定黨設定到parameterTable
     */
    private void loadParameterTableConfig() {
        Map<String, String> paramMap = sqlParameterConfigLoad.loadConfig();
        DefaultTableModel model = JTableUtil.createModel(false, new Object[] { "參數", "值" });
        parametersTable.setModel(model);
        for (String col : paramMap.keySet()) {
            String val = paramMap.get(col);
            model.addRow(new Object[] { col, val });
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
     * 取得dataSource
     */
    private DataSource getDataSource() {
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
}
