package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.icu.util.Calendar;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.selenium.SeleniumUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class SeleniumTestUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;

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
        tabbedPane.addTab("New tab", null, panel, null);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

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
        CSS(Pattern.compile("(\\w+)\\s*\\=\\s*(csss?)\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String cssType = mth.group(2);
                String express = mth.group(3);
                String word1 = express;
                String word2 = "";
                if (express.contains(",")) {
                    String[] expresses = express.split(",", -1);
                    word1 = expresses[0];
                    word2 = expresses[1];
                }
                WebElement element = SeleniumUtil.WebElementControl.waitPageElementByCss(word1, word2, self.driver);
                self.elementMap.put(var, element);
            }
        }, //
        XPATH(Pattern.compile("(\\w+)\\s*\\=\\s*(xpaths?)\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String xpathType = mth.group(2);
                String express = mth.group(3);
                String word1 = express;
                WebElement element = SeleniumUtil.WebElementControl.waitPageElementByXpath(word1, self.driver);
                self.elementMap.put(var, element);
            }
        }, //
        SET_VAL(Pattern.compile("(\\w+)\\.val\\((.*)\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                String value = mth.group(2);
                if (self.elementMap.containsKey(var)) {
                    WebElement element = self.elementMap.get(var);
                    SeleniumUtil.WebElementControl.setValue(element, value);
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
                    WebElement element = self.elementMap.get(var);
                    String content = SeleniumUtil.WebElementControl.getValue(element);
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
                String dateValue = mth.group(3);
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
                self.valueMap.put(dateVar, dateValue);
            }
        }, //
        CLICK(Pattern.compile("(\\w+)\\.click\\(\\)")) {
            @Override
            void apply001(Matcher mth, int lineNumber, SeleniumService self) {
                String var = mth.group(1);
                if (self.elementMap.containsKey(var)) {
                    WebElement element = self.elementMap.get(var);
                    element.click();
                } else {
                    System.out.println("行:" + lineNumber + ", 找不到元素:" + var);
                }
            }
        },//
        ;

        final Pattern ptn;

        PatternEnum(Pattern ptn) {
            this.ptn = ptn;
        }

        abstract void apply001(Matcher mth, int lineNumber, SeleniumService self);
    }

    private class SeleniumService {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat TW_SDF = new SimpleDateFormat("/MM/dd");
        WebDriver driver;
        Map<String, WebElement> elementMap = new HashMap<String, WebElement>();
        Map<String, String> valueMap = new HashMap<String, String>();

        private void processContent(String text) {
            List<String> contentLst = StringUtil_.readContentToList(text, true, false, false);
            for (int ii = 0; ii < contentLst.size(); ii++) {
                String line = contentLst.get(ii);
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                for (PatternEnum e : PatternEnum.values()) {
                    Matcher mth = e.ptn.matcher(line);
                    if (mth.matches()) {
                        e.apply001(mth, ii + 1, this);
                    }
                }
            }
        }
    }
}
