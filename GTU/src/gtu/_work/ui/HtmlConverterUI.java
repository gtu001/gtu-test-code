package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.json.JSONException;
import org.json.JSONObject;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class HtmlConverterUI extends JFrame {

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
    private JTabbedPane tabbedPane_1;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JLabel lblNewLabel;
    private JTextField parameterText;
    private JTextArea htmlArea;
    private JTextArea javascriptArea;
    private JTextArea prototypeArea;
    private JButton executeBtn;
    private JButton clearBtn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(HtmlConverterUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HtmlConverterUI frame = new HtmlConverterUI();
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
    public HtmlConverterUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 585, 447);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("Tab1", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        lblNewLabel = new JLabel("變數名稱");
        panel_3.add(lblNewLabel);

        parameterText = new JTextField();
        parameterText.setText("obj");
        panel_3.add(parameterText);
        parameterText.setColumns(20);

        executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeBtn.actionPerformed", e);
            }
        });
        panel_3.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearBtn.actionPerformed", e);
            }
        });
        panel_3.add(clearBtn);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
        panel.add(tabbedPane_1, BorderLayout.CENTER);

        panel_7 = new JPanel();
        tabbedPane_1.addTab("Html", null, panel_7, null);
        panel_7.setLayout(new BorderLayout(0, 0));

        htmlArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(htmlArea);
        panel_7.add(JCommonUtil.createScrollComponent(htmlArea), BorderLayout.CENTER);

        panel_8 = new JPanel();
        tabbedPane_1.addTab("javascript", null, panel_8, null);
        panel_8.setLayout(new BorderLayout(0, 0));

        javascriptArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(javascriptArea);
        panel_8.add(JCommonUtil.createScrollComponent(javascriptArea), BorderLayout.CENTER);

        panel_9 = new JPanel();
        tabbedPane_1.addTab("prototype", null, panel_9, null);
        panel_9.setLayout(new BorderLayout(0, 0));

        prototypeArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(prototypeArea);
        panel_9.add(JCommonUtil.createScrollComponent(prototypeArea), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Tab2", null, panel_1, null);

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
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        swingUtil.addAction("clearBtn.actionPerformed", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                javascriptArea.setText("");
                prototypeArea.setText("");
                htmlArea.setText("");
                parameterText.setText("");
            }
        });
        swingUtil.addAction("executeBtn.actionPerformed", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                try {
                    int idx = tabbedPane_1.getSelectedIndex();

                    JavascriptObj j1 = new JavascriptObj();
                    PrototypeObj p1 = new PrototypeObj();

                    if (idx == 0) {
                        String html = StringUtils.defaultString(htmlArea.getText());
                        System.out.println("----from Html : " + html);
                        String parameterName = StringUtils.trimToEmpty(parameterText.getText());

                        j1.parseFromHtml(parameterName, html);
                        p1.parseFromHtml(parameterName, html);

                        javascriptArea.setText(j1.toSelf());
                        prototypeArea.setText(p1.toSelf());
                    } else if (idx == 1) {
                        String strVal = StringUtils.defaultString(javascriptArea.getText());
                        System.out.println("----from Javascript : " + strVal);
                        String parameterName = StringUtils.trimToEmpty(parameterText.getText());

                        j1.parseSelfToHtml(strVal);
                        String html = j1.toHtml();

                        htmlArea.setText(html);
                        p1.parseFromHtml(parameterName, html);

                        prototypeArea.setText(p1.toSelf());
                    } else if (idx == 2) {
                        String strVal = StringUtils.defaultString(prototypeArea.getText());
                        System.out.println("----from Prototype : " + strVal);
                        String parameterName = StringUtils.trimToEmpty(parameterText.getText());

                        p1.parseSelfToHtml(strVal);
                        String html = p1.toHtml();

                        htmlArea.setText(html);
                        j1.parseFromHtml(parameterName, html);

                        javascriptArea.setText(j1.toSelf());
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
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

    private abstract class HtmlProcess {
        abstract String toSelf();

        protected String html;
        protected String parameterName;
        protected String tagName;
        protected String tagContent;
        protected String innerText;
        protected Map<String, String> attr = new LinkedHashMap<String, String>();

        abstract void parseSelfToHtml(String strVal);

        public void parseFromHtml(String parameterName, String html) {
            this.parameterName = StringUtils.trimToEmpty(parameterName);
            if (StringUtils.isBlank(this.parameterName)) {
                this.parameterName = "obj";
            }
            this.html = StringUtils.trimToEmpty(html);
            {
                Pattern ptn = Pattern.compile("\\<(\\w+)\\s((?:.|\n)*?)\\/\\>");
                Matcher mth = ptn.matcher(html);
                if (mth.find()) {
                    tagName = StringUtils.defaultString(mth.group(1));
                    tagContent = StringUtils.defaultString(mth.group(2));
                    innerText = "";
                }
            }
            {
                Pattern ptn = Pattern.compile("\\<(\\w+)\\s((?:.|\n)*?)\\>((?:.|\n)*?)\\<\\/(\\w+)\\>");
                Matcher mth = ptn.matcher(html);
                if (mth.find()) {
                    tagName = StringUtils.defaultString(mth.group(1));
                    tagContent = StringUtils.defaultString(mth.group(2));
                    innerText = StringUtils.defaultString(mth.group(3));
                }
            }
            {
                Pattern ptn = Pattern.compile("(\\w+)\\=[\"']((?:.|\n)*?)[\"']");
                Matcher mth = ptn.matcher(tagContent);
                while (mth.find()) {
                    String key = StringUtils.trimToEmpty(mth.group(1));
                    String value = StringUtils.defaultString(mth.group(2));
                    attr.put(key, value);
                }
            }
        }

        public String toHtml() {
            List<String> attrLst = new ArrayList<String>();
            for (String key : attr.keySet()) {
                String value = attr.get(key);
                if ((StringUtils.startsWith(value, "\"") && StringUtils.endsWith(value, "\"")) || //
                        (StringUtils.startsWith(value, "'") && StringUtils.endsWith(value, "'"))) {
                    attrLst.add(key + "=" + value + "");
                } else {
                    attrLst.add(key + "=\"" + value + "\"");
                }
            }
            return String.format("<%1$s %2$s>%3$s</%1$s>", tagName, StringUtils.join(attrLst, " "), innerText);
        }
    }

    private class JavascriptObj extends HtmlProcess {
        @Override
        public String toSelf() {
            StringBuilder sb = new StringBuilder();
            sb.append("var " + this.parameterName + " = document.createElement(\"" + tagName + "\");\n");
            for (String key : attr.keySet()) {
                String value = attr.get(key);
                sb.append(this.parameterName + ".setAttribute(\"" + key + "\", \"" + value + "\");\n");
            }
            int idx = new Random().nextInt(100);
            sb.append("var txt" + idx + " = document.createTextNode(\"" + innerText + "\");\n");
            sb.append(this.parameterName + ".appendChild(txt" + idx + ");\n");
            return sb.toString();
        }

        @Override
        void parseSelfToHtml(String strVal) {
            strVal = StringUtils.defaultString(strVal);
            {
                Pattern ptn = Pattern.compile("var\\s+(\\w+)\\s*\\=\\s*document\\.createElement\\(\\s*[\"'](\\w+)[\"']\\s*\\)");
                Matcher mth = ptn.matcher(strVal);
                if (mth.find()) {
                    parameterName = StringUtils.defaultString(mth.group(1));
                    tagName = StringUtils.defaultString(mth.group(2));
                }
            }
            {
                Pattern ptn = Pattern.compile("document\\.createTextNode\\(\\s*[\"']((?:.|\n)*?)[\"']\\s*\\)");
                Matcher mth = ptn.matcher(strVal);
                if (mth.find()) {
                    innerText = StringUtils.defaultString(mth.group(1));
                }
            }
            {
                System.out.println("parameterName = " + parameterName);
                Pattern ptn = Pattern.compile(Pattern.quote(parameterName) + "\\.setAttribute\\(\\s*[\"'](\\w+)[\"']\\s*\\,\\s*((?:.|\\n)*?)\\)");
                Matcher mth = ptn.matcher(strVal);
                while (mth.find()) {
                    String key = StringUtils.trimToEmpty(mth.group(1));
                    String value = StringUtils.defaultString(mth.group(2));
                    attr.put(key, value);
                }
            }
        }
    }

    private class PrototypeObj extends HtmlProcess {
        @Override
        public String toSelf() {
            StringBuilder sb = new StringBuilder();
            String attrStr = new JSONObject(attr).toString();
            System.out.println("prototype attrStr = " + attrStr);
            sb.append(String.format("var %1$s = new Element('%2$s', %3$s);", parameterName, tagName, attrStr) + "\n");
            sb.append(parameterName + ".update(\"" + innerText + "\")");
            return sb.toString();
        }

        @Override
        void parseSelfToHtml(String strVal) {
            strVal = StringUtils.defaultString(strVal);
            {
                Pattern ptn = Pattern.compile("Element\\(\\s*[\"'](\\w+)[\"']\\s*\\,((?:.|\n)*?)\\)");
                Matcher mth = ptn.matcher(strVal);
                if (mth.find()) {
                    tagName = StringUtils.defaultString(mth.group(1));
                    tagContent = StringUtils.defaultString(mth.group(2));
                }
            }
            {
                System.out.println("tagContent = " + tagContent);
                attr.clear();
                Pattern ptn = Pattern.compile("[\"'](\\w+)[\"']\\s*\\:\\s*([\"'\\w\\.\\_\\(\\)]+)");
                Matcher mth = ptn.matcher(tagContent);
                while (mth.find()) {
                    String key = mth.group(1);
                    String value = mth.group(2);
                    attr.put(key, value);
                }
            }
            {
                Pattern ptn = Pattern.compile("\\.update\\(\\s*[\"']((?:.|\n)*?)[\"']\\s*\\)");
                Matcher mth = ptn.matcher(strVal);
                if (mth.find()) {
                    innerText = StringUtils.defaultString(mth.group(1));
                }
            }
        }
    }
}
