package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;
import gtu.xml.XmlFormatter;

public class JsonFormatterUI extends JFrame {

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
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JTextArea jsonFromArea;
    private JTextArea jsonToArea;
    private JButton clearBtn;
    private JButton clearBtn2;
    private JRadioButton jsonRadio;
    private JRadioButton xmlRadio;
    private ButtonGroup buttonGroup;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(JsonFormatterUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JsonFormatterUI frame = new JsonFormatterUI();
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
    public JsonFormatterUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 689, 485);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("原始", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.SOUTH);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.action", e);
            }
        });

        jsonRadio = new JRadioButton("json");
        jsonRadio.setSelected(true);
        panel_5.add(jsonRadio);

        xmlRadio = new JRadioButton("xml");
        panel_5.add(xmlRadio);
        panel_5.add(clearBtn);

        buttonGroup = JButtonGroupUtil.createRadioButtonGroup(jsonRadio, xmlRadio);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.EAST);

        jsonFromArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(jsonFromArea);
        panel.add(JCommonUtil.createScrollComponent(jsonFromArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("排版過後", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.NORTH);

        panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.WEST);

        panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.SOUTH);

        clearBtn2 = new JButton("清除");
        clearBtn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.action", e);
            }
        });
        panel_9.add(clearBtn2);

        panel_10 = new JPanel();
        panel_1.add(panel_10, BorderLayout.EAST);

        jsonToArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(jsonToArea);
        panel_1.add(JCommonUtil.createScrollComponent(jsonToArea), BorderLayout.CENTER);

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
        swingUtil.addAction(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addAction(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {

            private String getFormatJSON(String orignJSON) {
                try {
                    JSONObject json = new JSONObject(orignJSON);
                    return json.toString(4);
                } catch (JSONException ex) {
                    try {
                        JSONArray json = new JSONArray(orignJSON);
                        return json.toString(4);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
                try {
                    if (tabbedPane.getSelectedIndex() == 1) {
                        String jsonFromText = StringUtils.defaultString(jsonFromArea.getText());
                        if (StringUtils.isBlank(jsonFromText)) {
                            return;
                        }

                        String resultStr = "";
                        if (JButtonGroupUtil.getSelectedButton(buttonGroup) == jsonRadio) {
                            resultStr = getFormatJSON(jsonFromText);
                        } else if (JButtonGroupUtil.getSelectedButton(buttonGroup) == xmlRadio) {
                            // resultStr = new
                            // XmlFormatter2().format(jsonFromText);
                            resultStr = XmlFormatter.format(jsonFromText);
                        }

                        jsonToArea.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        swingUtil.addAction("clearBtn.action", new Action() {

            @Override
            public void action(EventObject evt) throws Exception {
                jsonFromArea.setText("");
                jsonToArea.setText("");
            }
        });
        swingUtil.addAction("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", new Action() {

            @Override
            public void action(EventObject evt) throws Exception {
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
