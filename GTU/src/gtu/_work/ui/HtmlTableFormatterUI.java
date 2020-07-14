package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import org.apache.commons.lang.StringUtils;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.string.StringUtil_;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class HtmlTableFormatterUI extends JFrame {

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
    private JTextArea orignHtmlArea;
    private JTextArea formatHtmlArea;
    private JButton clearBtn;
    private JButton clearBtn2;
    private JLabel lblNewLabel;
    private JTextField specialPatternText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(HtmlTableFormatterUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HtmlTableFormatterUI frame = new HtmlTableFormatterUI();
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
    public HtmlTableFormatterUI() {
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

        lblNewLabel = new JLabel("特別Pattern");
        panel_3.add(lblNewLabel);

        specialPatternText = new JTextField();
        panel_3.add(specialPatternText);
        specialPatternText.setColumns(30);

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
        panel_5.add(clearBtn);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.EAST);

        orignHtmlArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(orignHtmlArea);
        panel.add(JCommonUtil.createScrollComponent(orignHtmlArea), BorderLayout.CENTER);

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

        formatHtmlArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(formatHtmlArea);
        panel_1.add(JCommonUtil.createScrollComponent(formatHtmlArea), BorderLayout.CENTER);

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

            private String getSpace(int length) {
                return StringUtils.leftPad(" ", length);
            }

            private String fixNoSpace(String html) {
                html = StringUtils.defaultString(html);
                Pattern ptn = Pattern.compile("\\<(?:.|\n)*?\\>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                Matcher mth = ptn.matcher(html);
                StringBuffer sb = new StringBuffer();
                while (mth.find()) {
                    String tmp = StringUtil_.appendReplacementEscape(mth.group());
                    System.out.println("fixNoSpace - " + tmp);
                    mth.appendReplacement(sb, tmp);
                }
                mth.appendTail(sb);
                return sb.toString();
            }

            private String getSpecialPattern() {
                String ptnStr = StringUtils.trimToEmpty(specialPatternText.getText());
                if (StringUtils.isNotBlank(ptnStr)) {
                    return "|" + ptnStr;
                }
                return "";
            }

            private String fixTagChangeLineBody(String orignTag, int spaceLength) {
                return orignTag.replaceAll("\n", "\n" + getSpace(spaceLength + 4));
            }

            private String formatter(String html) {
                html = StringUtils.defaultString(html);
                Pattern ptn = Pattern.compile("\\<\\/?(table|tr|td|\\w+" + getSpecialPattern() + ")(\\s(?:.|\n)*?)?\\>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                Matcher mth = ptn.matcher(html);
                StringBuffer sb = new StringBuffer();
                while (mth.find()) {
                    String orignTag = StringUtil_.appendReplacementEscape(mth.group());
                    String tag = mth.group(1);
                    String body = mth.group(2);
                    System.out.println("----" + tag);
                    System.out.println("\t" + body);
                    if ("table".equalsIgnoreCase(tag)) {
                        mth.appendReplacement(sb, "\r\n" + getSpace(0) + fixTagChangeLineBody(orignTag, 0));
                    } else if ("tr".equalsIgnoreCase(tag)) {
                        mth.appendReplacement(sb, "\r\n" + getSpace(4) + fixTagChangeLineBody(orignTag, 4));
                    } else if ("td".equalsIgnoreCase(tag)) {
                        mth.appendReplacement(sb, "\r\n" + getSpace(8) + fixTagChangeLineBody(orignTag, 8));
                    } else {
                        mth.appendReplacement(sb, "\r\n" + getSpace(12) + fixTagChangeLineBody(orignTag, 12));
                    }
                }
                mth.appendTail(sb);
                return sb.toString();
            }

            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
                try {
                    if (tabbedPane.getSelectedIndex() == 1) {
                        String orignHtml = StringUtils.defaultString(orignHtmlArea.getText());
                        if (StringUtils.isBlank(orignHtml)) {
                            return;
                        }
                        String resultStr = fixNoSpace(orignHtml);
                        resultStr = formatter(resultStr);
                        formatHtmlArea.setText(resultStr);
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        swingUtil.addAction("clearBtn.action", new Action() {

            @Override
            public void action(EventObject evt) throws Exception {
                orignHtmlArea.setText("");
                formatHtmlArea.setText("");
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
