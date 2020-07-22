package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.db.JdbcDBUtil;
import gtu.db.jdbc.util.DBDateUtil;
import gtu.db.sqlMaker.DbSqlCreater;
import gtu.db.sqlMaker.DbSqlCreater.TableInfo;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class TaiwanInsuranceDBToolUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JTextArea sqlTextArea;
    private JLabel lblNewLabel;
    private JTextField tableText;
    private JButton generateSqlBtn;
    private JButton clearBtn;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;
    private JTextField driverText;
    private JTextField jdbcUrlText;
    private JTextField userNameText;
    private JTextField passwordText;
    private JPanel panel_7;
    private JButton saveConnBtn;

    private PropertiesUtilBean config = new PropertiesUtilBean(TaiwanInsuranceDBToolUI.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(TaiwanInsuranceDBToolUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TaiwanInsuranceDBToolUI frame = new TaiwanInsuranceDBToolUI();
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
    public TaiwanInsuranceDBToolUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("查詢", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        lblNewLabel = new JLabel("表名稱");
        panel_3.add(lblNewLabel);

        tableText = new JTextField();
        panel_3.add(tableText);
        tableText.setColumns(20);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        generateSqlBtn = new JButton("產生SQL");
        generateSqlBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("generateSqlBtn.click", e);
            }
        });
        panel_6.add(generateSqlBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.click", e);
            }
        });
        panel_6.add(clearBtn);

        sqlTextArea = new JTextArea();
        sqlTextArea.setToolTipText("輸入SQL");
        panel.add(JCommonUtil.createScrollComponent(sqlTextArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("DB設定", null, panel_1, null);
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        lblNewLabel_1 = new JLabel("Driver");
        panel_1.add(lblNewLabel_1, "2, 2, right, default");

        driverText = new JTextField();
        panel_1.add(driverText, "4, 2, fill, default");
        driverText.setColumns(10);

        lblNewLabel_2 = new JLabel("JDBC URL");
        panel_1.add(lblNewLabel_2, "2, 4, right, default");

        jdbcUrlText = new JTextField();
        panel_1.add(jdbcUrlText, "4, 4, fill, default");
        jdbcUrlText.setColumns(10);

        lblNewLabel_3 = new JLabel("帳號");
        panel_1.add(lblNewLabel_3, "2, 6, right, default");

        userNameText = new JTextField();
        panel_1.add(userNameText, "4, 6, fill, default");
        userNameText.setColumns(10);

        lblNewLabel_4 = new JLabel("密碼");
        panel_1.add(lblNewLabel_4, "2, 8, right, default");

        passwordText = new JTextField();
        panel_1.add(passwordText, "4, 8, fill, default");
        passwordText.setColumns(10);

        panel_7 = new JPanel();
        panel_1.add(panel_7, "4, 12, fill, fill");

        saveConnBtn = new JButton("儲存");
        saveConnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("saveConnBtn.click", e);
            }
        });
        panel_7.add(saveConnBtn);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
            this.setTitle("You Set My World On Fire");

            config.reflectInit(this);
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addActionHex(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addActionHex(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addActionHex("clearBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                tableText.setText("");
                sqlTextArea.setText("");
            }
        });
        swingUtil.addActionHex("saveConnBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.reflectSetConfig(TaiwanInsuranceDBToolUI.this);
                config.store();
                JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
            }
        });
        swingUtil.addActionHex("generateSqlBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                String table = tableText.getText();
                String sql = sqlTextArea.getText();
                JCommonUtil.isBlankErrorMsg(table, "請輸入表名稱");
                JCommonUtil.isBlankErrorMsg(sql, "請輸入SQL");

                String driver = driverText.getText();
                String url = jdbcUrlText.getText();
                String username = userNameText.getText();
                String password = passwordText.getText();
                DbSqlCreater mDbSqlCreater = new DbSqlCreater(driver, url, username, password);
                TableInfo tableInfo = mDbSqlCreater.execute(table, "1!=1", null);
                tableInfo.setDbDateDateFormat(DBDateUtil.DBDateFormat.Oracle);

                List<Map<String, Object>> queryLst = JdbcDBUtil.queryForList(sql, getDataSource().getConnection(), true);
                StringBuffer sb = new StringBuffer();
                int count = 0;
                for (Map<String, Object> map : queryLst) {
                    sb.append(tableInfo.createInsertSql(transferMap(map), Collections.EMPTY_SET, true) + ";\r\n");
                    count++;
                }

                File inserSqlFile = new File(FileUtil.DESKTOP_DIR, "INSERT_" + table + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".sql");
                FileUtil.saveToFile(inserSqlFile, sb.toString(), "UTF-8");
                JCommonUtil._jOptionPane_showMessageDialog_info("產生完成! , size = " + count);
            }
        });
    }

    private Map<String, String> transferMap(Map<String, Object> map) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : map.keySet()) {
            Object val = map.get(key);
            String newVal = null;
            if (val != null) {
                newVal = String.valueOf(val);
            }
            rtnMap.put(key, newVal);
        }
        return rtnMap;
    }

    private DataSource getDataSource() {
        String driver = driverText.getText();
        String url = jdbcUrlText.getText();
        String username = userNameText.getText();
        String password = passwordText.getText();

        BasicDataSource ds2 = new BasicDataSource();
        ds2.setUrl(url);
        ds2.setUsername(username);
        ds2.setPassword(password);
        ds2.setDriverClassName(driver);

        ds2.setMaxActive(10);
        ds2.setInitialSize(1);
        ds2.setMaxIdle(600000);
        return ds2;
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item")//
                .addMenuItem("detail1", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file")//
                .addMenuItem("item1", null)//
                .addMenuItem("item2", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
