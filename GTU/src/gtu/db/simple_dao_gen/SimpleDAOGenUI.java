package gtu.db.simple_dao_gen;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.db.simple_dao_gen.forSpring.GenDaoAllMain_forSpring;
import gtu.db.simple_dao_gen.forSpring_ex2.GenDaoAllMain_forSpring_Ex2;
import gtu.properties.PropertiesGroupUtils_ByKey;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;

public class SimpleDAOGenUI extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SimpleDAOGenUI frame = new SimpleDAOGenUI();
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
    public SimpleDAOGenUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 577, 438);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), }));

        JLabel lblDbName = new JLabel("db name");
        panel.add(lblDbName, "2, 2, right, default");

        dbNameText = new JTextField();
        panel.add(dbNameText, "4, 2, fill, default");
        dbNameText.setColumns(10);

        JLabel lblDbUrl = new JLabel("db url");
        panel.add(lblDbUrl, "2, 4, right, default");

        dbUrlText = new JTextField();
        panel.add(dbUrlText, "4, 4, fill, default");
        dbUrlText.setColumns(10);

        JLabel lblDbUserId = new JLabel("db user id");
        panel.add(lblDbUserId, "2, 6, right, default");

        dbUserIdText = new JTextField();
        panel.add(dbUserIdText, "4, 6, fill, default");
        dbUserIdText.setColumns(10);

        JLabel lblDbPassword = new JLabel("db password");
        panel.add(lblDbPassword, "2, 8, right, default");

        dbPasswordText = new JTextField();
        panel.add(dbPasswordText, "4, 8, fill, default");
        dbPasswordText.setColumns(10);

        JLabel lblDriverClass = new JLabel("driver class");
        panel.add(lblDriverClass, "2, 10, right, default");

        dbDriverText = new JTextField();
        dbDriverText.setColumns(10);
        panel.add(dbDriverText, "4, 10, fill, default");

        JLabel lblTableName = new JLabel("table name");
        panel.add(lblTableName, "2, 12, right, default");

        tableNameText = new JTextField();
        panel.add(tableNameText, "4, 12, fill, default");
        tableNameText.setColumns(10);

        JLabel lblPkList = new JLabel("pk list");
        panel.add(lblPkList, "2, 14, right, default");

        pkListText = new JTextField();
        panel.add(pkListText, "4, 14, fill, default");
        pkListText.setColumns(10);

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, "4, 16, fill, fill");

        JButton btnNewButton = new JButton("產生DAO");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateDAOAction();
            }
        });

        JButton button_1 = new JButton("下一組");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextSetup();
            }
        });

        JButton button_2 = new JButton("儲存設定");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveSetup();
            }
        });
        
        daoTypeCombox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("simple");
        model.addElement("spring");
        model.addElement("spring_fuco");
        daoTypeCombox.setModel(model);
        panel_2.add(daoTypeCombox);
        panel_2.add(button_2);
        panel_2.add(button_1);

        JButton button = new JButton("刪除設定");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSetup();
            }
        });
        panel_2.add(button);
        panel_2.add(btnNewButton);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        generateResultArea = new JTextArea();
        JCommonUtil.createScrollComponent(panel_1, generateResultArea);
        
        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setJFrameIcon(this, "resource/images/ico/dao.ico");
    }

    File propFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), "simpleDaoGenUI_dbConfig.properties");
    //File propFile = new File("I:\\my_tool\\simple_dao_gen\\simpleDaoGenUI_dbConfig.properties");
    PropertiesGroupUtils_ByKey tool = new PropertiesGroupUtils_ByKey(propFile);

    private JTextField dbNameText;
    private JTextField dbUrlText;
    private JTextField dbUserIdText;
    private JTextField dbPasswordText;
    private JTextField tableNameText;
    private JTextField pkListText;
    private JTextArea generateResultArea;
    private JTextField dbDriverText;
    private JComboBox daoTypeCombox;
    
    private Connection getConnection() throws Exception {
        try {
            String dbUrl = dbUrlText.getText();
            String dbUserId = dbUserIdText.getText();
            String dbPassword = dbPasswordText.getText();
            String dbDriver = dbDriverText.getText();

            Validate.notEmpty(dbUrl, "db url empty!");
            Validate.notEmpty(dbUserId, "db userId empty!");
            Validate.notEmpty(dbUserId, "db Driver empty!");
            
            Class.forName(dbDriver).newInstance();
            Connection conn = DriverManager.getConnection(dbUrl, dbUserId, dbPassword);
            return conn;
        } catch (Exception e) {
            throw e;
        }
    }

    private void generateDAOAction() {
        try {
            String dbName = dbNameText.getText();
            String dbUrl = dbUrlText.getText();
            String dbUserId = dbUserIdText.getText();
            String dbPassword = dbPasswordText.getText();
            String dbDriver = dbDriverText.getText();

            Validate.notEmpty(dbName, "db name empty!");
            Validate.notEmpty(dbUrl, "db url empty!");
            Validate.notEmpty(dbUserId, "db userId empty!");
            Validate.notEmpty(dbUserId, "db Driver empty!");
            
            String tableName = tableNameText.getText();
            String _pkList = pkListText.getText();
            
            Validate.notEmpty(dbUserId, "tableName empty!");
            
            String[] pkListArry = StringUtils.trimToEmpty(_pkList).split(",", -1);
            List<String> pkList = Arrays.asList(pkListArry);
            
            String daoType = (String)daoTypeCombox.getSelectedItem();
            String txt = "";
            if("simple".equals(daoType)) {
                GenDaoAllMain t = new GenDaoAllMain();
                Connection conn = getConnection();
                List<String> columnList = t.getColumnList(tableName, conn);
                txt = t.execute(tableName, columnList, pkList, getConnection());
            }else if("spring".equals(daoType)) {
                GenDaoAllMain_forSpring t = new GenDaoAllMain_forSpring();
                Connection conn = getConnection();
                List<String> columnList = t.getColumnList(tableName, conn);
                txt = t.execute(tableName, columnList, pkList, getConnection());
            }else if("spring_fuco".equals(daoType)) {
                GenDaoAllMain_forSpring_Ex2 t = new GenDaoAllMain_forSpring_Ex2();
                Connection conn = getConnection();
                List<String> columnList = t.getColumnList(tableName, conn);
                txt = t.execute(tableName, columnList, pkList, getConnection());
            }
            
            generateResultArea.setText(txt);
            JCommonUtil._jOptionPane_showMessageDialog_info("產生成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void saveSetup() {
        try {
            Map<String, String> map = new HashMap<String, String>();

            String dbName = dbNameText.getText();
            String dbUrl = dbUrlText.getText();
            String dbUserId = dbUserIdText.getText();
            String dbPassword = dbPasswordText.getText();
            String dbDriver = dbDriverText.getText();

            Validate.notEmpty(dbName, "db name empty!");
            Validate.notEmpty(dbUrl, "db url empty!");
            Validate.notEmpty(dbUserId, "db userId empty!");
            Validate.notEmpty(dbDriver, "db Driver empty!");

            map.put(PropertiesGroupUtils_ByKey.SAVE_KEYS, dbName);
            map.put("dbUrl", dbUrl);
            map.put("dbUserId", dbUserId);
            map.put("dbPassword", dbPassword);
            map.put("dbDriver", dbDriver);
            
            Connection conn = getConnection();
            conn.close();

            tool.saveConfig(map);
            
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void nextSetup() {
        try {
            tool.next();
            Map<String, String> map = tool.loadConfig();
            dbNameText.setText(map.get(PropertiesGroupUtils_ByKey.SAVE_KEYS));
            dbUrlText.setText(map.get("dbUrl"));
            dbUserIdText.setText(map.get("dbUserId"));
            dbPasswordText.setText(map.get("dbPassword"));
            dbDriverText.setText(map.get("dbDriver"));
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void deleteSetup() {
        try {
            String dbName = dbNameText.getText();
            Validate.notEmpty(dbName, "db name empty!");
            tool.removeConfig(dbName);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
