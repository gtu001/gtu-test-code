package com.ebao.job_report;

import gtu.db.tradevan.DBMain;
import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesGroupUtils_ByKey;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class BatchJobReportUI extends JFrame {

    private JPanel contentPane;
    private JTextArea runIdArea;
    private JTextField urlText;
    private JTextField usernameText;
    private JTextField passwordText;

//    private static final File configFile = new File(PropertiesUtil.getJarCurrentPath(BatchJobReportUI.class), BatchJobReportUI.class.getSimpleName() + "_dbConfig.properties"); // TODO
     private static final File configFile = new File("D:/my_tool/BatchJobReportUI_dbConfig.properties"); // TODO
    private PropertiesGroupUtils propConfig = new PropertiesGroupUtils(configFile);
//    private PropertiesGroupUtils_ByKey propConfig = new PropertiesGroupUtils_ByKey(configFile);

    private static final String KEY_URL = "url";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DIRPATH = "dir_path";
    private static final String KEY_DBNAME = "dbname";
//    private static final String KEY_DBNAME = PropertiesGroupUtils_ByKey.SAVE_KEYS;

    private JTextField pathText;

    BatchJobReport_GenXlsReport gen;
    BatchJobReport_GenTxt gen1;
    private JTextField dbNameText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BatchJobReportUI frame = new BatchJobReportUI();
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
    public BatchJobReportUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 649, 473);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("RUN_ID", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel p1 = new JPanel();
        panel.add(p1, BorderLayout.SOUTH);

        JButton btnNewButton = new JButton("總表");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    processJobTotalRpt();
                    JCommonUtil._jOptionPane_showMessageDialog_info("完成!");
                } catch (Throwable ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        p1.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Log");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Map<String, String> jobIdRunIdMap = processJobErrorLogTxt();
                    JCommonUtil._jOptionPane_showMessageDialog_info("完成!");
                } catch (Throwable ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        p1.add(btnNewButton_1);

        runIdArea = new JTextArea();
        JCommonUtil.createScrollComponent(panel, runIdArea);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Conn", null, panel_1, null);
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblName = new JLabel("Name");
        panel_1.add(lblName, "4, 2, right, default");

        dbNameText = new JTextField();
        dbNameText.setColumns(10);
        panel_1.add(dbNameText, "6, 2, fill, default");

        JLabel lblDbUrl = new JLabel("DB url");
        panel_1.add(lblDbUrl, "4, 6, right, default");

        urlText = new JTextField();
        panel_1.add(urlText, "6, 6, fill, default");
        urlText.setColumns(10);

        JLabel lblUsername = new JLabel("username");
        panel_1.add(lblUsername, "4, 10, right, default");

        usernameText = new JTextField();
        panel_1.add(usernameText, "6, 10, fill, default");
        usernameText.setColumns(10);

        JLabel lblPassword = new JLabel("password");
        panel_1.add(lblPassword, "4, 14, right, default");

        passwordText = new JTextField();
        panel_1.add(passwordText, "6, 14, fill, default");
        passwordText.setColumns(10);

        JLabel label = new JLabel("路徑");
        panel_1.add(label, "4, 18, right, default");

        pathText = new JTextField();
        panel_1.add(pathText, "6, 18, fill, default");
        pathText.setColumns(10);

        JButton nextConfigBtn = new JButton("下一組");
        nextConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextConfigBtnProcess();
            }
        });

        JButton saveConfigBtn = new JButton("儲存");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfigBtnProcess();
            }
        });
        panel_1.add(saveConfigBtn, "4, 22");
        panel_1.add(nextConfigBtn, "4, 24");

        loadConnInfo();
    }

    /**
     * 讀取設定
     */
    private void loadConnInfo() {
        Map<String, String> config = propConfig.loadConfig();
        if (config.containsKey(KEY_URL)) {
            urlText.setText(config.get(KEY_URL));
        }
        if (config.containsKey(KEY_DBNAME)) {
            dbNameText.setText(config.get(KEY_DBNAME));
        }
        if (config.containsKey(KEY_USERNAME)) {
            usernameText.setText(config.get(KEY_USERNAME));
        }
        if (config.containsKey(KEY_PASSWORD)) {
            passwordText.setText(config.get(KEY_PASSWORD));
        }
        if (config.containsKey(KEY_DIRPATH)) {
            pathText.setText(config.get(KEY_DIRPATH));
        }
    }

    /**
     * 總表
     */
    private void processJobTotalRpt() throws IOException {
        String text = runIdArea.getText();
        Validate.notBlank(text, "runId為空");

        List<String> runIdList = new ArrayList<String>();
        Scanner scan = new Scanner(text);
        while (scan.hasNext()) {
            String runId = scan.next();
            runIdList.add(runId);
        }
        scan.close();

        gen = new BatchJobReport_GenXlsReport();
        gen.filePath = pathText.getText();
        gen.execute(runIdList, getDataSource());
    }

    /**
     * 產生log報告 by runId
     */
    private Map<String, String> processJobErrorLogTxt() throws IOException {
        Map<String, String> jobIdRunIdMap = new LinkedHashMap<String, String>();

        String text = runIdArea.getText();
        Validate.notBlank(text, "runId為空");
        Scanner scan = new Scanner(text);
        while (scan.hasNext()) {
            String runId = scan.next();
            System.out.println("RUN_ID : " + runId);

            Map<String, Object> batchJobRun = query_BatchJobRun(runId);

            String jobId = String.valueOf(batchJobRun.get("JOB_ID"));

            gen1 = new BatchJobReport_GenTxt();
            gen1.filePath = pathText.getText();
            gen1.runIdFetchLogDesc(jobId, runId, getDataSource());

            jobIdRunIdMap.put(jobId, runId);
        }
        scan.close();

        return jobIdRunIdMap;
    }

    /**
     * 取得t_batch_job_run
     */
    private Map<String, Object> query_BatchJobRun(String runId) {
        String sql = "select * from t_batch_job_run t where run_id = '" + runId + "'";
        List<Map<String, Object>> query = getDBMain().query(sql);
        if (query.isEmpty()) {
            throw new RuntimeException("無資料 : " + sql);
        }
        return query.get(0);
    }

    private DBMain getDBMain() {
        DBMain dbMain = new DBMain();
        dbMain.setDataSource(getDataSource());
        return dbMain;
    }

    /**
     * 儲存設定
     */
    private void saveConfigBtnProcess() {
        try {
            String url = urlText.getText();
            String username = usernameText.getText();
            String password = passwordText.getText();
            String dirPath = pathText.getText();
            String dbName = dbNameText.getText();
            Map<String, String> map = new HashMap<String, String>();
            Validate.notBlank(url, "url為空");
            Validate.notBlank(username, "username為空");
            Validate.notBlank(password, "password為空");
            // Validate.notBlank(dirPath, "dirPath為空");
            map.put(KEY_URL, url);
            map.put(KEY_USERNAME, username);
            map.put(KEY_PASSWORD, password);
            map.put(KEY_DIRPATH, dirPath);
            map.put(KEY_DBNAME, dbName);

            Connection conn = getDataSource().getConnection();
            conn.close();

            propConfig.saveConfig(map);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 取得Datasource
     */
    private DataSource getDataSource() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(urlText.getText());
        bds.setUsername(usernameText.getText());
        bds.setPassword(passwordText.getText());
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        return bds;
    }

    /**
     * 下一組設定
     */
    private void nextConfigBtnProcess() {
        propConfig.next();
        loadConnInfo();
    }
}
