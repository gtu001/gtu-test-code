package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.binary.Base64JdkUtil;
import gtu.file.FileUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;
import taobe.tec.jcc.JChineseConvertor;

public class ChineseSwapUI extends JFrame {

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
    private JLabel lblNewLabel;
    private JTextField pathDirText;
    private JTextArea chineseArea;
    private JRadioButton toBig5Radio;
    private JRadioButton toGbkRadio;
    private JButton translateBtn;
    private JLabel lblNewLabel_1;
    private JTextField fromEncodeText;
    private ButtonGroup btnGroup;
    private JRadioButton toEncodeBase64Radio;
    private JRadioButton toDecodeBase64Radio;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(ChineseSwapUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChineseSwapUI frame = new ChineseSwapUI();
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
    public ChineseSwapUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 563, 392);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("繁簡轉換", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        lblNewLabel = new JLabel("轉換檔案");
        panel_3.add(lblNewLabel);

        pathDirText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(pathDirText, false);
        panel_3.add(pathDirText);
        pathDirText.setColumns(40);

        lblNewLabel_1 = new JLabel("來源編碼");
        panel_3.add(lblNewLabel_1);

        fromEncodeText = new JTextField();
        fromEncodeText.setText("UTF8");
        panel_3.add(fromEncodeText);
        fromEncodeText.setColumns(10);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        toBig5Radio = new JRadioButton("轉繁");
        panel_6.add(toBig5Radio);

        toGbkRadio = new JRadioButton("轉簡");
        toGbkRadio.setSelected(true);
        panel_6.add(toGbkRadio);

        translateBtn = new JButton("轉換");
        translateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("translateBtn.click", e);
            }
        });

        toEncodeBase64Radio = new JRadioButton("encode base64");
        toEncodeBase64Radio.setSelected(true);
        panel_6.add(toEncodeBase64Radio);

        toDecodeBase64Radio = new JRadioButton("decode base64");
        toDecodeBase64Radio.setSelected(true);
        panel_6.add(toDecodeBase64Radio);
        panel_6.add(translateBtn);

        chineseArea = new JTextArea();
        System.out.println(chineseArea.getFont());
        panel.add(JCommonUtil.createScrollComponent(chineseArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Other", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            btnGroup = JButtonGroupUtil.createRadioButtonGroup(toBig5Radio, toGbkRadio, toEncodeBase64Radio, toDecodeBase64Radio);

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
        swingUtil.addActionHex("translateBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (StringUtils.isNotBlank(chineseArea.getText())) {
                    String text = chineseArea.getText();
                    if (JButtonGroupUtil.getSelectedButton(btnGroup) == toBig5Radio) {
                        chineseArea.setText(JChineseConvertor.getInstance().s2t(text));
                    } else if (JButtonGroupUtil.getSelectedButton(btnGroup) == toGbkRadio) {
                        chineseArea.setText(JChineseConvertor.getInstance().t2s(text));
                    } else if (JButtonGroupUtil.getSelectedButton(btnGroup) == toEncodeBase64Radio) {
                        chineseArea.setText(Base64JdkUtil.encode(text));
                    } else if (JButtonGroupUtil.getSelectedButton(btnGroup) == toDecodeBase64Radio) {
                        chineseArea.setText(Base64JdkUtil.decodeToString(text));
                    }
                }

                if (StringUtils.isNotBlank(pathDirText.getText())) {
                    File file = new File(pathDirText.getText());
                    String toFileName = FileUtil.getNameNoSubName(file);
                    String toFileSubName = FileUtil.getSubName(file);
                    String text = FileUtil.loadFromFile(file, fromEncodeText.getText());
                    if (JButtonGroupUtil.getSelectedButton(btnGroup) == toBig5Radio) {
                        text = JChineseConvertor.getInstance().s2t(text);
                        chineseArea.setText(text);
                        File toFile = new File(file.getParentFile(), toFileName + "_轉繁." + toFileSubName);
                        FileUtil.saveToFile(toFile, text, "UTF8");
                        JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案 : " + toFile);
                    } else if (JButtonGroupUtil.getSelectedButton(btnGroup) == toGbkRadio) {
                        text = JChineseConvertor.getInstance().t2s(text);
                        chineseArea.setText(text);
                        File toFile = new File(file.getParentFile(), toFileName + "_轉簡." + toFileSubName);
                        FileUtil.saveToFile(toFile, text, "UTF8");
                        JCommonUtil._jOptionPane_showMessageDialog_info("文字轉換完成!");
                        JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案 : " + toFile);
                    }
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
