package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.selenium.SeleniumUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JToggleButtonUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;
import gtu.swing.util.TextLineNumber;
import gtu.yaml.util.YamlUtil;
import gtu.yaml.util.YamlUtilBean;

public class SeleniumTestUI extends JFrame {

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
    private JList scriptList;
    private JTextArea scriptArea;
    private JButton saveBtn;
    private JTextField scriptNameText;
    private JButton executeBtn;
    private JLabel lblNewLabel;
    private JLabel lineNumberLbl;
    private JToggleButton pauseBtn;
    private MyConfig mMyConfig;
    private PropertiesUtilBean config = new PropertiesUtilBean(SeleniumTestUI.class);
    private JLabel lblDriver;
    private JTextField webDriverText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(SeleniumTestUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SeleniumTestUI frame = new SeleniumTestUI();
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
    public SeleniumTestUI() {
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
        tabbedPane.addTab("清單", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        lblDriver = new JLabel("driver位置");
        panel_6.add(lblDriver);

        webDriverText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(webDriverText, false);
        panel_6.add(webDriverText);
        webDriverText.setColumns(10);

        scriptList = new JList();
        scriptList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("scriptList.click", e);
            }
        });

        scriptList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JListUtil.newInstance(scriptList).defaultJListKeyPressed(e, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SeleniumScript del = (SeleniumScript) e.getSource();
                        boolean result = mMyConfig.remove(del);
                        e.setSource(result);
                    }
                });
            }
        });

        JCommonUtil.applyDropFiles(scriptList, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                if (!files.isEmpty()) {
                    mMyConfig.initFileAndReload(files.get(0));
                }
            }
        });

        panel.add(JCommonUtil.createScrollComponent(scriptList), BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("腳本", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_7 = new JPanel();
        panel_1.add(panel_7, BorderLayout.NORTH);

        lineNumberLbl = new JLabel("");
        panel_7.add(lineNumberLbl);

        pauseBtn = JToggleButtonUtil.createSimpleButton("pause", "continue", false, null);
        panel_7.add(pauseBtn);

        lblNewLabel = new JLabel("腳本名稱");
        panel_7.add(lblNewLabel);

        scriptNameText = new JTextField();
        panel_7.add(scriptNameText);
        scriptNameText.setColumns(20);

        panel_8 = new JPanel();
        panel_1.add(panel_8, BorderLayout.WEST);

        panel_9 = new JPanel();
        panel_1.add(panel_9, BorderLayout.EAST);

        panel_10 = new JPanel();
        panel_1.add(panel_10, BorderLayout.SOUTH);

        saveBtn = new JButton("save");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("saveBtn.click", e);
            }
        });
        panel_10.add(saveBtn);

        executeBtn = new JButton("execute");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeBtn.click", e);
            }
        });
        panel_10.add(executeBtn);

        scriptArea = new JTextArea();
        JTextAreaUtil.applyCommonSetting(scriptArea);
        JTextAreaUtil.applyEnterKeyFixPosition(scriptArea);
        JTextAreaUtil.applyTabKey(scriptArea);

        panel_1.add(JTextAreaUtil.createLineNumberWrap(scriptArea), BorderLayout.CENTER);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {

            config.reflectInit(this);
            mMyConfig = new MyConfig();

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
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    public static class SeleniumScript {
        String title;
        String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((title == null) ? 0 : title.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SeleniumScript other = (SeleniumScript) obj;
            if (title == null) {
                if (other.title != null)
                    return false;
            } else if (!title.equals(other.title))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    private class MyConfig {
        File configFile = new File(PropertiesUtil.getJarCurrentPath(SeleniumTestUI.class), SeleniumTestUI.class.getSimpleName() + "_config.yml");
        YamlUtilBean<SeleniumScript> config;

        public void initFileAndReload(File configFile) {
            configFile = new File(PropertiesUtil.getJarCurrentPath(SeleniumTestUI.class), SeleniumTestUI.class.getSimpleName() + "_config.yml");
            Map<String, Class<?>> configMap = new HashMap<String, Class<?>>();
            config = new YamlUtilBean<SeleniumScript>(configFile, SeleniumScript.class, configMap);
            this.reloadModel();
        }

        public MyConfig() {
            initFileAndReload(configFile);
        }

        public boolean remove(SeleniumScript del) {
            boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除" + del.title, "確認刪除");
            if (!confirm) {
                return false;
            }
            config.reload();
            config.remove(del);
            config.store();
            this.reloadModel();
            return true;
        }

        public void save(SeleniumScript mSeleniumScript) {
            config.reload();
            mSeleniumScript.content = YamlUtil.getPlainString(mSeleniumScript.content);
            config.setProperty(mSeleniumScript);
            config.store();
            this.reloadModel();
        }

        public void reloadModel() {
            config.reload();
            DefaultListModel model = new DefaultListModel();
            List<SeleniumScript> lst = config.getConfigProp();
            for (SeleniumScript m : lst) {
                model.addElement(m);
            }
            scriptList.setModel(model);
        }
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
        swingUtil.addActionHex("saveBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                SeleniumScript mSeleniumScript = new SeleniumScript();

                String title = JCommonUtil.isBlankErrorMsg(scriptNameText, "標題必須輸入");
                String content = JCommonUtil.isBlankErrorMsg(scriptArea, "腳本必須輸入");

                mSeleniumScript.title = StringUtils.trimToEmpty(title);
                mSeleniumScript.content = content;

                mMyConfig.save(mSeleniumScript);
            }
        });
        swingUtil.addActionHex("executeBtn.click", new Action() {
            Thread runnable;
            SeleniumService mSeleniumService;

            private void start(String content, String driverPath) {
                if (mSeleniumService != null) {
                    mSeleniumService.stop = true;
                }
                mSeleniumService = new SeleniumService() {
                    @Override
                    void setCurrentLineNumber(int lineNumber) {
                        lineNumberLbl.setText("" + lineNumber);
                    }

                    @Override
                    List<String> getContent() {
                        return StringUtil_.readContentToList(scriptArea.getText(), true, false, false);
                    }

                    @Override
                    boolean isPause() {
                        return pauseBtn.isSelected();
                    }

                    @Override
                    void Goto(String title, SeleniumService self) {
                        DefaultListModel model = (DefaultListModel) scriptList.getModel();
                        for (int ii = 0; ii < model.getSize(); ii++) {
                            SeleniumScript mSeleniumScript = (SeleniumScript) model.getElementAt(ii);
                            if (StringUtils.equals(StringUtils.trimToEmpty(mSeleniumScript.title), StringUtils.trimToEmpty(title))) {
                                scriptNameText.setText(mSeleniumScript.title);
                                scriptArea.setText(mSeleniumScript.content);
                                self.ii = 0;
                                break;
                            }
                        }
                    }
                };
                runnable = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        executeBtn.setEnabled(false);
                        mSeleniumService.processContent(content, driverPath);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                executeBtn.setEnabled(true);
                            }
                        }, 3000);
                    }
                });
                runnable.start();
            }

            @Override
            public void action(EventObject evt) throws Exception {
                String content = JCommonUtil.isBlankErrorMsg(scriptArea, "腳本必須輸入");
                String driverPath = JCommonUtil.isBlankErrorMsg(webDriverText, "webDriver必須設定");
                config.reflectSetConfig(SeleniumTestUI.this);
                config.store();
                if (runnable == null || runnable.getState() == Thread.State.TERMINATED) {
                    this.start(content, driverPath);
                } else if (runnable != null && runnable.getState() != Thread.State.NEW) {
                    boolean confirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("執行中, 是否開新的?", "start new one");
                    if (confirm) {
                        this.start(content, driverPath);
                    }
                }
            }
        });
        swingUtil.addActionHex("scriptList.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (JMouseEventUtil.buttonLeftClick(1, evt)) {
                    SeleniumScript mSeleniumScript = JListUtil.getLeadSelectionObject(scriptList);
                    if (mSeleniumScript != null) {
                        scriptNameText.setText(mSeleniumScript.title);
                        scriptArea.setText(mSeleniumScript.content);
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

    private enum PatternEnum {
        GET_URL(Pattern.compile("(?:get|url)\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String url = PatternEnum.parseValue(mth.group(1), self);
                self.driver.get(url);
            }
        }, //
        CSS(Pattern.compile("(\\w+)\\s*\\=\\s*(csss?)\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String cssType = mth.group(2);
                String express = PatternEnum.parseValue(mth.group(3), self);
                String word1 = express;
                String word2 = "";
                if (express.contains(",")) {
                    String[] expresses = express.split(",", -1);
                    word1 = expresses[0];
                    word2 = expresses[1];
                }
                Object elements = null;
                String desc = "";
                if ("csss".equalsIgnoreCase(cssType)) {
                    List<WebElement> list = SeleniumUtil.WebElementControl.waitPageElementByCsss(word1, word2, self.driver);
                    elements = list;
                    desc = "List-" + list.size();
                } else if ("css".equalsIgnoreCase(cssType)) {
                    elements = SeleniumUtil.WebElementControl.waitPageElementByCss(word1, word2, self.driver);
                    desc = "Node";
                }
                if (elements == null) {
                    System.out.println("找不到元素 : " + word1 + "\t" + word2);
                    return;
                }
                System.out.println("宣告元素 : " + var + " -" + desc);
                self.elementMap.put(var, elements);
            }
        }, //
        XPATH(Pattern.compile("(\\w+)\\s*\\=\\s*(xpaths?)\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String xpathType = mth.group(2);
                String express = PatternEnum.parseValue(mth.group(3), self);
                String word1 = express;
                WebElement element = SeleniumUtil.WebElementControl.waitPageElementByXpath(word1, self.driver);
                self.elementMap.put(var, element);
                Object elements = null;
                String desc = "";
                if ("xpaths".equalsIgnoreCase(xpathType)) {
                    List<WebElement> list = SeleniumUtil.WebElementControl.waitPageElementByXpaths(word1, self.driver);
                    elements = list;
                    desc = "List-" + list.size();
                } else if ("xpath".equalsIgnoreCase(xpathType)) {
                    elements = SeleniumUtil.WebElementControl.waitPageElementByXpath(word1, self.driver);
                    desc = "Node";
                }
                if (elements == null) {
                    System.out.println("找不到元素 : " + word1);
                    return;
                }
                System.out.println("宣告元素 : " + var + " 長" + desc);
                self.elementMap.put(var, elements);
            }
        }, //
        SET_VAL(Pattern.compile("(\\w+)\\.val\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String value = PatternEnum.parseValue(mth.group(2), self);
                if (self.elementMap.containsKey(var)) {
                    System.out.println("設值 : " + var + " = " + value);
                    WebElement element = (WebElement) self.elementMap.get(var);
                    SeleniumUtil.WebElementControl.setValue(element, value, self.driver);
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        GET_VAL(Pattern.compile("(\\w+)\\s*\\=\\s*(\\w+)\\.val\\(\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String textVar = mth.group(1);
                String var = mth.group(2);
                if (self.elementMap.containsKey(var)) {
                    WebElement element = (WebElement) self.elementMap.get(var);
                    String content = SeleniumUtil.WebElementControl.getValue(element);
                    System.out.println("設值 : " + var + " = " + content);
                    self.valueMap.put(textVar, content);
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        DATE(Pattern.compile("(\\w+)\\s*\\=\\s*(date|twdate)\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String dateVar = mth.group(1);
                String dateType = mth.group(2);
                String dateValue = PatternEnum.parseValue(mth.group(3), self);
                if ("NOW".equals(dateValue)) {
                    if ("date".equalsIgnoreCase(dateType)) {
                        dateValue = self.SDF.format(new Date());
                    } else if ("twdate".equals(dateType)) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR) - 1911;
                        dateValue = StringUtils.leftPad("" + year, 3, '0') + self.TW_SDF.format(new Date());
                    }
                } else {
                    try {
                        String[] dates = dateValue.split(",", -1);
                        String method = dates[0];
                        String addVal = dates[1];
                        int addVal2 = Integer.parseInt(addVal);
                        Calendar cal = Calendar.getInstance();
                        if ("YEAR".equalsIgnoreCase(method)) {
                            cal.add(Calendar.YEAR, addVal2);
                        } else if ("MONTH".equalsIgnoreCase(method)) {
                            cal.add(Calendar.MONTH, addVal2);
                        } else if ("DATE".equalsIgnoreCase(method) || "DAY".equalsIgnoreCase(method)) {
                            cal.add(Calendar.DATE, addVal2);
                        }
                        if ("date".equalsIgnoreCase(dateType)) {
                            dateValue = self.SDF.format(cal.getTime());
                        } else if ("twdate".equals(dateType)) {
                            int year = cal.get(Calendar.YEAR) - 1911;
                            dateValue = StringUtils.leftPad("" + year, 3, '0') + self.TW_SDF.format(cal.getTime());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("設值 : " + dateVar + " = " + dateValue);
                self.valueMap.put(dateVar, dateValue);
            }
        }, //
        CLICK(Pattern.compile("(\\w+)\\.click\\(\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                if (self.elementMap.containsKey(var)) {
                    System.out.println("點擊 : " + var);
                    WebElement element = (WebElement) self.elementMap.get(var);
                    element.click();
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        CLICK_UNTIL(Pattern.compile("click\\((.+)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                boolean clickSuccess = false;
                if (var.contains(",")) {
                    String[] arry = var.split(",", -1);
                    String type = StringUtils.trimToEmpty(arry[0]);
                    String express = StringUtils.trimToEmpty(arry[0]);
                    if ("css".equalsIgnoreCase(type)) {
                        SeleniumUtil.WebElementControl.clickUntil(self.driver, null, express);
                        clickSuccess = true;
                    } else if ("xpath".equalsIgnoreCase(type)) {
                        SeleniumUtil.WebElementControl.clickUntil(self.driver, express, null);
                        clickSuccess = true;
                    }
                }
                if (!clickSuccess) {
                    System.out.println("未能符合語法!");
                }
            }
        }, //
        PAUSE(Pattern.compile("pause\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                if (StringUtils.isBlank(var)) {
                    var = "是否繼續?";
                }
                while (!JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(var, "繼續執行:" + lineNumber)) {
                    System.out.println("未按下繼續...");
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, //
        CLEAR(Pattern.compile("(\\w+)\\.clear\\(\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                if (self.elementMap.containsKey(var)) {
                    System.out.println("點擊 : " + var);
                    WebElement element = (WebElement) self.elementMap.get(var);
                    element.clear();
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        ENTER(Pattern.compile("(\\w+)\\.enter\\(\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                if (self.elementMap.containsKey(var)) {
                    System.out.println("點擊 : " + var);
                    WebElement element = (WebElement) self.elementMap.get(var);
                    SeleniumUtil.WebElementControl.enter(element);
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        SELECT_ONE(Pattern.compile("(\\w+)\\.select\\((value|text|index)\\=(.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String type = mth.group(2);
                String val = PatternEnum.parseValue(mth.group(3), self);
                if (self.elementMap.containsKey(var)) {
                    System.out.println("下拉 : " + var);
                    WebElement element = (WebElement) self.elementMap.get(var);
                    String text = "";
                    String value = "";
                    Integer index = null;
                    if ("value".equalsIgnoreCase(type)) {
                        value = val;
                    } else if ("text".equalsIgnoreCase(type)) {
                        text = val;
                    } else if ("index".equalsIgnoreCase(type)) {
                        index = Integer.parseInt(val);
                    }
                    SeleniumUtil.WebElementControl.setSelect(element, text, value, index);
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        SHOW_SELECT(Pattern.compile("show(?:Select|Option|Options)\\((\\w+)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = PatternEnum.parseValue(mth.group(1), self);
                if (self.elementMap.containsKey(var)) {
                    System.out.println("下拉 : " + var);
                    WebElement element = (WebElement) self.elementMap.get(var);
                    System.out.println("###############");
                    SeleniumUtil.WebElementControl.getOptionsLst(element);
                    System.out.println("###############");
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        }, //
        EQ_GET(Pattern.compile("(\\w+)\\s*\\=\\s*(\\w+)\\.(?:eq|get)\\((\\d+)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = mth.group(1);
                String var2 = mth.group(2);
                int index = Integer.parseInt(PatternEnum.parseValue(mth.group(3), self));
                if (self.elementMap.containsKey(var2)) {
                    Object fetchVal = self.elementMap.get(var2);
                    if (fetchVal instanceof List) {
                        System.out.println("取元素 : " + var2 + " index : " + index);
                        List<WebElement> elements = (List<WebElement>) fetchVal;
                        WebElement element = elements.get(index);
                        self.elementMap.put(var1, element);
                    } else if (fetchVal instanceof WebElement) {
                        System.out.println("取元素 : " + var2);
                        WebElement element = (WebElement) fetchVal;
                        self.elementMap.put(var1, element);
                    }
                }
            }
        }, //
        FIND_XPATH(Pattern.compile("(\\w+)\\s*\\=\\s*(\\w+)\\.findXpath\\((.*)\\)")) {

            private boolean process(String var1, String var2, String express, WebElement element, SeleniumService self) {
                List<WebElement> elements2 = (List<WebElement>) element.findElements(By.xpath(express));
                Object value = null;
                if (elements2.isEmpty()) {
                    System.out.println("找不到元素 : " + var2 + "\t" + express);
                    return false;
                } else if (elements2.size() == 1) {
                    System.out.println("找到元素 : " + var1 + "\tNode");
                    value = elements2.get(0);
                } else {
                    System.out.println("找到元素 : " + var1 + "\tList-" + elements2.size());
                    value = elements2;
                }
                self.elementMap.put(var1, value);
                return true;
            }

            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = mth.group(1);
                String var2 = mth.group(2);
                String express = PatternEnum.parseValue(mth.group(3), self);
                if (self.elementMap.containsKey(var2)) {
                    Object fetchVal = self.elementMap.get(var2);
                    if (fetchVal instanceof List) {
                        List<WebElement> elements = (List<WebElement>) fetchVal;
                        for (WebElement e : elements) {
                            boolean findOk = this.process(var1, var2, express, e, self);
                            if (findOk) {
                                break;
                            }
                        }
                    } else if (fetchVal instanceof WebElement) {
                        WebElement element = (WebElement) fetchVal;
                        this.process(var1, var2, express, element, self);
                    }
                } else {
                    System.out.println("找不到元素 : " + var2 + "\t" + express);
                }
            }
        }, //
        SLEEP(Pattern.compile("(?:wait|sleep)\\(([\\.\\d]+)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                double sleeptime = Math.abs(Double.parseDouble(mth.group(1)));
                System.out.println("休息秒數 :" + sleeptime);
                long sleeptime2 = (long) (sleeptime * 1000);
                try {
                    Thread.currentThread().sleep(sleeptime2);
                } catch (Exception e) {
                }
            }
        }, //
        SHOW_INFO(Pattern.compile("showInfo\\(\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                System.out.println("################");
                for (String key : self.elementMap.keySet()) {
                    Object fetchVal = self.elementMap.get(key);
                    if (fetchVal instanceof List) {
                        List<WebElement> elements = (List<WebElement>) fetchVal;
                        System.out.println("\t" + key + "\tList----------start " + elements.size());
                        for (int ii = 0; ii < elements.size(); ii++) {
                            WebElement w = elements.get(ii);
                            System.out.println("\t" + w.getTagName() + "[id=" + w.getAttribute("id") + ", name=" + w.getAttribute("name") + "]");
                        }
                        System.out.println("\t" + key + "\tList----------end   " + elements.size());
                    } else if (fetchVal instanceof WebElement) {
                        WebElement w = (WebElement) fetchVal;
                        System.out.println("\t" + key + "\tNode : " + w.getTagName() + "[id=" + w.getAttribute("id") + ", name=" + w.getAttribute("name") + "]");
                    }
                }
                System.out.println("################");
            }
        }, //
        SAVE_HTML(Pattern.compile("savehtml\\((\\w*)\\)", Pattern.CASE_INSENSITIVE)) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = PatternEnum.parseValue(mth.group(1), self);
                String htmlContent = self.driver.getPageSource();
                File saveFile = new File(FileUtil.DESKTOP_DIR, SeleniumTestUI.class.getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".txt");
                if (StringUtils.isNotBlank(var1)) {
                    saveFile = new File(FileUtil.DESKTOP_DIR, SeleniumTestUI.class.getSimpleName() + "_" + var1 + ".txt");
                }
                FileUtil.saveToFile(saveFile, htmlContent, "UTF8");
                System.out.println("存檔 : " + saveFile);
            }
        }, //
        HTML(Pattern.compile("(\\w+)\\.html\\((\\w*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = mth.group(1);
                String var2 = PatternEnum.parseValue(mth.group(2), self);
                if (self.elementMap.containsKey(var1)) {
                    Object fetchVal = self.elementMap.get(var1);
                    if (fetchVal instanceof List) {
                        System.out.println("忽略List");
                    } else if (fetchVal instanceof WebElement) {
                        WebElement element = (WebElement) fetchVal;
                        System.out.println("################");
                        WebElement parent = SeleniumUtil.WebElementControl.getParent(element, self.driver);
                        String html = SeleniumUtil.WebElementControl.getHtml(parent);
                        System.out.println(html);
                        File saveFile = new File(FileUtil.DESKTOP_DIR, SeleniumTestUI.class.getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".txt");
                        if (StringUtils.isNotBlank(var2)) {
                            saveFile = new File(FileUtil.DESKTOP_DIR, SeleniumTestUI.class.getSimpleName() + "_" + var2 + ".txt");
                        }
                        FileUtil.saveToFile(saveFile, html, "UTF8");
                        System.out.println("################");
                    }
                } else {
                    System.out.println("找不到元素 : " + var1);
                }
            }
        }, //
        SWITCH_TO(Pattern.compile("switchTo\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = mth.group(1);
                String type = "";
                String val = "";
                if (var1.contains("=")) {
                    String[] ary = var1.split("=", -1);
                    type = StringUtils.trimToEmpty(ary[0]);
                    val = PatternEnum.parseValue(StringUtils.trimToEmpty(ary[1]), self);
                } else {
                    type = StringUtils.trimToEmpty(var1);
                }
                if ("index".equalsIgnoreCase(type)) {
                    int index = Integer.parseInt(val);
                    System.out.println("switch to index = " + index);
                    SeleniumUtil.WebElementControl.switchTo(self.driver, false, index, null, null);
                } else if ("nameOrId".equalsIgnoreCase(type)) {
                    System.out.println("switch to nameOrId = " + val);
                    SeleniumUtil.WebElementControl.switchTo(self.driver, false, null, val, null);
                } else if ("element".equalsIgnoreCase(type)) {
                    System.out.println("switch to element = " + val);
                    if (self.elementMap.containsKey(val)) {
                        WebElement element = (WebElement) self.elementMap.get(val);
                        SeleniumUtil.WebElementControl.switchTo(self.driver, false, null, null, element);
                    }
                } else {
                    System.out.println("switch to default");
                    SeleniumUtil.WebElementControl.switchTo(self.driver, true, null, null, null);
                }
            }
        }, //
        PRINT(Pattern.compile("print\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = mth.group(1);
                System.out.println(var1);
            }
        }, //
        PROMPT(Pattern.compile("(\\w+)\\s*\\=\\s*prompt\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = mth.group(1);
                String msg = mth.group(2);
                if (StringUtils.isBlank(msg)) {
                    msg = "請輸入變數 :" + var1;
                }
                String var2 = JCommonUtil._jOptionPane_showInputDialog(msg);
                self.valueMap.put(var1, var2);
                System.out.println("設定變數 : " + var1 + "\t" + var2);
            }
        }, //
        GOTO(Pattern.compile("GOTO\\((\\w+)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var1 = PatternEnum.parseValue(mth.group(1), self);
                if (StringUtils.isNotBlank(var1)) {
                    System.out.println("Goto 前往 title :" + var1);
                    self.Goto(var1, self);
                }
            }
        }, //
        ;

        final Pattern ptn;

        PatternEnum(Pattern ptn) {
            this.ptn = ptn;
        }

        private static String parseValue(String value, SeleniumService self) {
            Pattern ptn = Pattern.compile("\\$\\{(\\w+)\\}");
            Matcher mth = ptn.matcher(value);
            StringBuffer sb = new StringBuffer();
            while (mth.find()) {
                String value1 = mth.group();
                String key = mth.group(1);
                if (self.valueMap.containsKey(key)) {
                    value1 = self.valueMap.get(key);
                }
                mth.appendReplacement(sb, value1);
            }
            mth.appendTail(sb);
            return sb.toString();
        }

        public void apply002(Matcher mth, int lineNumber, SeleniumService self) {
            System.out.println("行:" + lineNumber + " - " + name());
            try {
                apply001(mth, lineNumber, self);
            } catch (Exception ex) {
                System.err.println("行:" + lineNumber + " - " + name() + ", 錯誤 : " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        abstract void apply001(Matcher mth, int lineNumber, SeleniumService self) throws Exception;
    }

    private abstract class SeleniumService {
        abstract void setCurrentLineNumber(int lineNumber);

        abstract List<String> getContent();

        abstract void Goto(String title, SeleniumService self);

        abstract boolean isPause();

        int ii = 0;

        SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat TW_SDF = new SimpleDateFormat("/MM/dd");
        WebDriver driver;
        Map<String, Object> elementMap = new HashMap<String, Object>();
        Map<String, String> valueMap = new HashMap<String, String>();
        boolean stop = false;

        private void processContent(String text, String driverPath) {
            driver = SeleniumUtil.getInstance().getDriver(driverPath);
            ii = 0;
            for (;;) {
                List<String> contentLst = getContent();
                if (ii > contentLst.size() - 1) {
                    break;
                }
                while (isPause()) {
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e1) {
                    }
                }
                String line = contentLst.get(ii);
                int lineNumber = ii + 1;
                ii++;
                setCurrentLineNumber(lineNumber);
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                if (line.startsWith("//")) {
                    continue;
                }
                for (PatternEnum e : PatternEnum.values()) {
                    Matcher mth = e.ptn.matcher(line);
                    if (mth.matches()) {
                        e.apply002(mth, lineNumber, this);
                    }
                }
                if (stop) {
                    break;
                }
            }
        }
    }
}
