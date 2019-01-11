package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.EventObject;

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

import org.apache.commons.lang3.StringUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.html.simple.HtmlInputSimpleCreater;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class HtmlInputSimpleCreaterUI extends JFrame {

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
    private JTextArea htmlArea;
    private JTextField templateYamlFileText;
    private JLabel lblNewLabel;
    private JButton saveConfigBtn;
    private JButton executeBtn;
    private JButton clearBtn;
    private PropertiesUtilBean config = new PropertiesUtilBean(HtmlInputSimpleCreaterUI.class);
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JTextArea resultArea;
    private JLabel lblNewLabel_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(HtmlInputSimpleCreaterUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HtmlInputSimpleCreaterUI frame = new HtmlInputSimpleCreaterUI();
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
    public HtmlInputSimpleCreaterUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 570, 419);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("腳本", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        lblNewLabel = new JLabel("腳本檔");
        panel_3.add(lblNewLabel);

        templateYamlFileText = new JTextField();
        panel_3.add(templateYamlFileText);
        templateYamlFileText.setColumns(25);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(templateYamlFileText, false);

        saveConfigBtn = new JButton("儲存");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("saveConfigBtn.click", e);
            }
        });
        panel_3.add(saveConfigBtn);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeBtn.click", e);
            }
        });

        lblNewLabel_1 = new JLabel("format:中文\\s欄位[tag]\\s..etc  , 空白行為TR");
        lblNewLabel_1.setForeground(Color.RED);
        panel_6.add(lblNewLabel_1);
        panel_6.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.click", e);
            }
        });
        panel_6.add(clearBtn);

        htmlArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(htmlArea);
        panel.add(JCommonUtil.createScrollComponent(htmlArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("結果", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.NORTH);

        panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.WEST);

        panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.SOUTH);

        panel_10 = new JPanel();
        panel_1.add(panel_10, BorderLayout.EAST);

        resultArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(resultArea);
        panel_1.add(JCommonUtil.createScrollComponent(resultArea), BorderLayout.CENTER);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            config.reflectInit(this);

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
                htmlArea.setText("");
                resultArea.setText("");
            }
        });
        swingUtil.addActionHex("saveConfigBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.reflectSetConfig(HtmlInputSimpleCreaterUI.this);
                config.store();
                JCommonUtil._jOptionPane_showMessageDialog_info("save done!");
            }
        });
        swingUtil.addActionHex("executeBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                HtmlInputSimpleCreater t = new HtmlInputSimpleCreater();
                String fromData = htmlArea.getText();
                InputStream is = new FileInputStream(templateYamlFileText.getText());
                String result = t.execute(is, fromData);
                System.out.println(result);
                resultArea.setText(result);
                if (StringUtils.isNotBlank(result)) {
                    tabbedPane.setSelectedIndex(1);
                }
            }
        });
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
