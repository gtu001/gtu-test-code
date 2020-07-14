package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;
import gtu.xml.xmlmapper.jaxb.ReadXmlGenerateJaxbBean;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JCheckBox;

public class ReadXmlGenerateJaxbBeanUI extends JFrame {

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
    private JTextArea xmlArea;
    private JButton executeBtn;
    private JButton clearBtn;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JTextArea resultBeanArea;
    private JLabel lblNewLabel;
    private JCheckBox isXmlDbTypeChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(ReadXmlGenerateJaxbBeanUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ReadXmlGenerateJaxbBeanUI frame = new ReadXmlGenerateJaxbBeanUI();
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
    public ReadXmlGenerateJaxbBeanUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 552, 414);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("XML", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        isXmlDbTypeChk = new JCheckBox("xml的tag為是否為底線分隔");
        isXmlDbTypeChk.setSelected(true);
        panel_3.add(isXmlDbTypeChk);

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

        lblNewLabel = new JLabel("＊List 在 propOrder 會有問題須自行調整");
        lblNewLabel.setForeground(Color.RED);
        panel_6.add(lblNewLabel);
        panel_6.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.click", e);
            }
        });
        panel_6.add(clearBtn);

        xmlArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(xmlArea);
        panel.add(JCommonUtil.createScrollComponent(xmlArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Bean", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.NORTH);

        panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.WEST);

        panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.SOUTH);

        panel_10 = new JPanel();
        panel_1.add(panel_10, BorderLayout.EAST);

        resultBeanArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(resultBeanArea);
        panel_1.add(JCommonUtil.createScrollComponent(resultBeanArea), BorderLayout.CENTER);

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
        swingUtil.addActionHex("executeBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                ReadXmlGenerateJaxbBean b = new ReadXmlGenerateJaxbBean();
                Map<String, String> javaBeanMap = b.executeXmlStr(xmlArea.getText(), isXmlDbTypeChk.isSelected());
                StringBuilder sb = new StringBuilder();
                for (String key : javaBeanMap.keySet()) {
                    sb.append(javaBeanMap.get(key) + "\n\n");
                }
                resultBeanArea.setText(sb.toString());
                JCommonUtil._jOptionPane_showMessageDialog_info("完成!");
            }
        });
        swingUtil.addActionHex("clearBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                resultBeanArea.setText("");
                xmlArea.setText("");
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
