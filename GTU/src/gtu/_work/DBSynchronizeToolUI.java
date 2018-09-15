package gtu._work;

import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class DBSynchronizeToolUI extends JFrame {

    private JPanel contentPane;
    private JTextField fileText;
    private JButton exeBtn;
    private JTextField urlText;
    private JTextField userText;
    private JTextField passwordText;
    private JButton nextConfigBtn;

    File currentJarFile = PropertiesUtil.getJarCurrentPath(DBSynchronizeToolUI.class);
    // File currentJarFile = new File("C:/Users/gtu001_5F/Desktop");
    File connectionConfigFile = new File(currentJarFile, "dbConfig.properties");
    PropertiesGroupUtils connectionConfigPropUtil = new PropertiesGroupUtils(connectionConfigFile);
    private JTextField driverText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DBSynchronizeToolUI frame = new DBSynchronizeToolUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DBSynchronizeToolUI() {
        try{
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(100, 100, 631, 442);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(new BorderLayout(0, 0));

            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            contentPane.add(tabbedPane, BorderLayout.CENTER);

            JPanel panel = new JPanel();
            tabbedPane.addTab("New tab", null, panel, null);
            panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                    FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                    ColumnSpec.decode("default:grow"), }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

            fileText = new JTextField();
            panel.add(fileText, "10, 4, fill, default");
            fileText.setColumns(10);
            JCommonUtil.jTextFieldSetFilePathMouseEvent(fileText, false);

            exeBtn = new JButton("執行");
            exeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    exeBtnAction();
                }
            });
            panel.add(exeBtn, "4, 8");

            JPanel panel_1 = new JPanel();
            tabbedPane.addTab("New tab", null, panel_1, null);
            panel_1.setLayout(new FormLayout(new ColumnSpec[] {
                    FormFactory.RELATED_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.RELATED_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.RELATED_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.RELATED_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.RELATED_GAP_COLSPEC,
                    ColumnSpec.decode("default:grow"),},
                new RowSpec[] {
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.RELATED_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,}));

            JLabel lblUrl = new JLabel("url");
            panel_1.add(lblUrl, "4, 4");

            urlText = new JTextField();
            panel_1.add(urlText, "10, 4, fill, default");
            urlText.setColumns(10);

            JLabel lblUser = new JLabel("username");
            panel_1.add(lblUser, "4, 8");

            userText = new JTextField();
            panel_1.add(userText, "10, 8, fill, default");
            userText.setColumns(10);

            JLabel lblPassword = new JLabel("password");
            panel_1.add(lblPassword, "4, 12");

            passwordText = new JTextField();
            panel_1.add(passwordText, "10, 12, fill, default");
            passwordText.setColumns(10);
            
                        JButton applyConfigBtn = new JButton("使用");
                        applyConfigBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                applyConfigBtnAction();
                            }
                        });
                        
                        JLabel lblDriver = new JLabel("driver");
                        panel_1.add(lblDriver, "4, 16");
                        
                        driverText = new JTextField();
                        driverText.setColumns(10);
                        panel_1.add(driverText, "10, 16, fill, default");
                        panel_1.add(applyConfigBtn, "4, 26");
                        
                                    nextConfigBtn = new JButton("下一組");
                                    nextConfigBtn.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            nextConfigBtnAction();
                                        }
                                    });
                                    panel_1.add(nextConfigBtn, "6, 26");
            
            nextConfigBtnAction();
        }catch(Throwable ex){
            JCommonUtil.handleException(ex);
        }
    }

    // execute
    private void exeBtnAction() {
        try {
            String user = userText.getText();
            String pwd = passwordText.getText();
            String url = urlText.getText();
            Validate.notBlank(user, "user cant empty");
            Validate.notBlank(url, "url cant empty");

            File excelFile = JCommonUtil.filePathCheck(fileText.getText(), "excel", "xls");

            DBSynchronizeTool tool = new DBSynchronizeTool();
            tool.setPassword(pwd);
            tool.setUrl(url);
            tool.setUserName(user);

            tool.execute(excelFile);

            JCommonUtil._jOptionPane_showMessageDialog_info("執行完成");
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
        }
    }

    // nextConfigBtnAction
    private void nextConfigBtnAction() {
        connectionConfigPropUtil.next();
        Map<String, String> map = connectionConfigPropUtil.loadConfig();
        String user = map.get("user");
        String pwd = map.get("pwd");
        String url = map.get("url");
        if (StringUtils.isNotBlank(user)) {
            userText.setText(user);
        }
        if (StringUtils.isNotBlank(url)) {
            urlText.setText(url);
        }
        if (StringUtils.isNotBlank(pwd)) {
            passwordText.setText(pwd);
        }
    }

    // applyConfigBtnAction
    private void applyConfigBtnAction() {
        String user = userText.getText();
        String pwd = passwordText.getText();
        String url = urlText.getText();
        if (StringUtils.isNotBlank(url) && StringUtils.isNotBlank(user)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("user", user);
            map.put("pwd", pwd);
            map.put("url", url);
            connectionConfigPropUtil.saveConfig(map);
            JCommonUtil._jOptionPane_showMessageDialog_error("save success!");
        }
    }
}
