package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.clipboard.ClipboardUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JListUtil.ItemColorTextHandler;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JOptionPaneUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class RegexReplacer extends javax.swing.JFrame {
    private static final String NOT_CONTAIN_ARRY_KEY = "not_arry";
    private static final String CONTAIN_ARRY_KEY = "arry";
    private static final String FREEMARKER_KEY = "ftl";
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegexReplacer inst = new RegexReplacer();
                inst.setLocationRelativeTo(null);
                gtu.swing.util.JFrameUtil.setVisible(true, inst);
            }
        });
    }

    public RegexReplacer() {
        super();
        initGUI();
    }

    private enum TabIndex {
        SOURCE, PARAM, TEMPLATE, RESULT
    }

    private void initGUI() {
        try {
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                jTabbedPane1.addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        try {
                            if (configHandler != null && jTabbedPane1.getSelectedIndex() == TabIndex.TEMPLATE.ordinal()) {
                                System.out.println("-------ChangeEvent[" + jTabbedPane1.getSelectedIndex() + "]");
                                configHandler.reloadTemplateList();
                            }
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("source", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            replaceArea = new JTextArea();
                            jScrollPane1.setViewportView(replaceArea);
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("param", null, jPanel2, null);
                    {
                        exeucte = new JButton();
                        jPanel2.add(exeucte, BorderLayout.SOUTH);
                        exeucte.setText("exeucte");
                        exeucte.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                exeucteActionPerformed(evt);
                            }
                        });
                    }
                    {
                        jPanel3 = new JPanel();
                        jPanel2.add(JCommonUtil.createScrollComponent(jPanel3), BorderLayout.CENTER);
                        jPanel3.setLayout(
                                new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                        new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("default:grow"), }));
                        {
                            lblNewLabel = new JLabel("key");
                            jPanel3.add(lblNewLabel, "2, 2, right, default");
                        }
                        {
                            configKeyText = new JTextField();
                            jPanel3.add(configKeyText, "4, 2, fill, default");
                            configKeyText.setColumns(10);
                        }
                        {
                            lblNewLabel_1 = new JLabel("from");
                            jPanel3.add(lblNewLabel_1, "2, 4, right, default");
                        }
                        {
                            repFromText = new JTextArea();
                            repFromText.setRows(3);
                            jPanel3.add(JCommonUtil.createScrollComponent(repFromText), "4, 4, fill, default");
                            repFromText.setColumns(10);
                        }
                        {
                            lblNewLabel_2 = new JLabel("to");
                            jPanel3.add(lblNewLabel_2, "2, 6, right, default");
                        }
                        {
                            repToText = new JTextArea();
                            repToText.setRows(3);
                            // repToText.setPreferredSize(new Dimension(0, 50));
                            jPanel3.add(JCommonUtil.createScrollComponent(repToText), "4, 6, fill, default");
                        }
                    }
                    {
                        addToTemplate = new JButton();
                        jPanel2.add(addToTemplate, BorderLayout.NORTH);
                        addToTemplate.setText("add to template");
                        addToTemplate.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                try {
                                    configHandler.put(configKeyText.getText(), repFromText.getText(), repToText.getText(), tradeOffArea.getText());
                                    configHandler.reloadTemplateList();
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });
                    }
                }
                {
                    jPanel5 = new JPanel();
                    BorderLayout jPanel5Layout = new BorderLayout();
                    jPanel5.setLayout(jPanel5Layout);
                    jTabbedPane1.addTab("template", null, jPanel5, null);
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel5.add(jScrollPane3, BorderLayout.CENTER);
                        {
                            templateList = new JList();
                            jScrollPane3.setViewportView(templateList);
                        }
                        templateList.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                if (templateList.getLeadSelectionIndex() == -1) {
                                    return;
                                }
                                PropConfigHandler.Config config = (PropConfigHandler.Config) JListUtil.getLeadSelectionObject(templateList);
                                configKeyText.setText(config.configKeyText);
                                repFromText.setText(config.fromVal);
                                repToText.setText(config.toVal);
                                tradeOffArea.setText(config.tradeOff);

                                templateList.setToolTipText(config.fromVal + " <----> " + config.toVal);

                                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                                    String replaceText = StringUtils.defaultString(replaceArea.getText());
                                    replaceText = replacer(config.fromVal, config.toVal, replaceText);
                                    resultArea.setText(replaceText);
                                    jTabbedPane1.setSelectedIndex(TabIndex.RESULT.ordinal());
                                    // 貼到記事本
                                    pasteTextToClipboard();
                                }
                            }
                        });
                        templateList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                PropConfigHandler.Config config = (PropConfigHandler.Config) JListUtil.getLeadSelectionObject(templateList);
                                JListUtil.newInstance(templateList).defaultJListKeyPressed(evt, false);
                                try {
                                    if (config != null) {
                                        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                                            configHandler.deleteConfig(config.configKeyText);
                                            configHandler.reloadTemplateList();
                                        }
                                    }
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });

                        // 改變顏色 ↓↓↓↓↓↓
                        JListUtil.newInstance(templateList).setItemColorTextProcess(new ItemColorTextHandler() {
                            public Pair<String, Color> setColorAndText(Object value) {
                                PropConfigHandler.Config config = (PropConfigHandler.Config) value;
                                if (config.tradeOffScore != 0) {
                                    return Pair.of(null, Color.GREEN);
                                }
                                return null;
                            }
                        });
                        // 改變顏色 ↑↑↑↑↑↑
                    }
                    {
                        scheduleExecute = new JButton();
                        jPanel5.add(scheduleExecute, BorderLayout.SOUTH);
                        scheduleExecute.setText("schedule execute");
                        scheduleExecute.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                scheduleExecuteActionPerformed(evt);
                            }
                        });
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("result", null, jPanel4, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        jPanel4.add(jScrollPane2, BorderLayout.CENTER);
                        {
                            resultArea = new JTextArea();
                            jScrollPane2.setViewportView(resultArea);
                        }
                    }
                }
            }

            {
                configHandler = new PropConfigHandler(prop, propFile, templateList, replaceArea);
                JCommonUtil.setFont(repToText, repFromText, replaceArea, templateList);
                {
                    tradeOffArea = new JTextArea();
                    tradeOffArea.setRows(3);
                    // tradeOffArea.setPreferredSize(new Dimension(0, 50));
                    tradeOffArea.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            try {
                                if (JMouseEventUtil.buttonLeftClick(2, e)) {
                                    String tradeOff = StringUtils.trimToEmpty(tradeOffArea.getText());
                                    JSONObject json = null;
                                    if (StringUtils.isBlank(tradeOff)) {
                                        json = new JSONObject();
                                        json.put(CONTAIN_ARRY_KEY, new JSONArray());
                                        json.put(NOT_CONTAIN_ARRY_KEY, new JSONArray());
                                        tradeOff = json.toString();
                                    } else {
                                        json = JSONObject.fromObject(tradeOff);
                                    }

                                    // 加入新的
                                    String selectItem = (String) JCommonUtil._JOptionPane_showInputDialog("請選擇類型!", "請選擇", new Object[] { "NA", "equal", "not_equal", "ftl" }, "NA");
                                    if ("NA".equals(selectItem)) {
                                        return;
                                    }

                                    String string = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("輸入新項目:"));
                                    string = StringUtils.trimToEmpty(string);

                                    if (StringUtils.isBlank(string)) {
                                        tradeOffArea.setText(json.toString());
                                        return;
                                    }

                                    String arryKey = "";
                                    String boolKey = "";
                                    String strKey = "";
                                    String intKey = "";

                                    if (selectItem.equals("equal")) {
                                        arryKey = CONTAIN_ARRY_KEY;
                                    } else if (selectItem.equals("not_equal")) {
                                        arryKey = NOT_CONTAIN_ARRY_KEY;
                                    } else if (selectItem.equals("ftl")) {
                                        strKey = FREEMARKER_KEY;
                                    } else {
                                        throw new RuntimeException("無法判斷的新增類型 : " + selectItem);
                                    }

                                    if (StringUtils.isNotBlank(arryKey)) {
                                        if (!json.containsKey(arryKey)) {
                                            json.put(arryKey, new JSONArray());
                                        }
                                        JSONArray arry = (JSONArray) json.get(arryKey);
                                        boolean findOk = false;
                                        for (int ii = 0; ii < arry.size(); ii++) {
                                            if (StringUtils.equalsIgnoreCase(arry.getString(ii), string)) {
                                                findOk = true;
                                                break;
                                            }
                                        }
                                        if (!findOk) {
                                            arry.add(string);
                                        }
                                    } else if (StringUtils.isNotBlank(strKey)) {
                                        json.put(strKey, string);
                                    } else if (StringUtils.isNotBlank(intKey)) {
                                        json.put(intKey, Integer.parseInt(string));
                                    } else if (StringUtils.isNotBlank(boolKey)) {
                                        json.put(boolKey, Boolean.valueOf(string));
                                    }

                                    tradeOffArea.setText(json.toString());

                                    JCommonUtil._jOptionPane_showMessageDialog_info("新增完成!");
                                }
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                    {
                        panel = new JPanel();
                        jPanel3.add(panel, "4, 8, fill, fill");
                        {
                            multiLineCheckBox = new JCheckBox("多行");
                            panel.add(multiLineCheckBox);
                        }
                        {
                            autoPasteToClipboardCheckbox = new JCheckBox("自動貼記事本");
                            panel.add(autoPasteToClipboardCheckbox);
                        }
                    }
                    {
                        lblNewLabel_3 = new JLabel("權重 ");
                        jPanel3.add(lblNewLabel_3, "2, 10");
                    }
                    jPanel3.add(JCommonUtil.createScrollComponent(tradeOffArea), "4, 10, fill, fill");
                }
                configHandler.reloadTemplateList();
            }

            this.setSize(512, 350);
            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.defaultToolTipDelay();

            JCommonUtil.frameCloseDo(this, new WindowAdapter() {
                public void windowClosing(WindowEvent paramWindowEvent) {
                    try {
                        prop.store(new FileOutputStream(propFile), "regexText");
                    } catch (Exception e) {
                        JCommonUtil.handleException("properties store error!", e);
                    }
                    setVisible(false);
                    dispose();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea replaceArea;
    private JScrollPane jScrollPane1;
    private JTextArea repFromText;
    private JScrollPane jScrollPane2;
    private JButton scheduleExecute;
    private JButton addToTemplate;
    private JScrollPane jScrollPane3;
    private JList templateList;
    private JPanel jPanel5;
    private JTextArea resultArea;
    private JPanel jPanel4;
    private JTextArea repToText;
    private JPanel jPanel3;
    private JButton exeucte;
    private JPanel jPanel2;

    static File propFile = new File(PropertiesUtil.getJarCurrentPath(RegexDirReplacer.class), "RegexReplacer_NEW.properties");
    static Properties prop = new Properties();

    private PropConfigHandler configHandler;

    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JTextField configKeyText;
    private JLabel lblNewLabel_3;
    private JTextArea tradeOffArea;
    private JPanel panel;
    private JCheckBox multiLineCheckBox;
    private JCheckBox autoPasteToClipboardCheckbox;

    private void exeucteActionPerformed(ActionEvent evt) {
        try {
            String replaceText = null;
            String fromPattern = null;
            String configkeytext = null;
            String toFormat = repToText.getText();
            Validate.notEmpty((configkeytext = configKeyText.getText()), "configKey can't empty");
            Validate.notEmpty((replaceText = replaceArea.getText()), "source can't empty");
            Validate.notEmpty((fromPattern = repFromText.getText()), "replace regex can't empty");
            resultArea.setText(replacer(fromPattern, toFormat, replaceText));
            // 切換到結果
            jTabbedPane1.setSelectedIndex(TabIndex.RESULT.ordinal());
            // 貼到記事本
            pasteTextToClipboard();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void pasteTextToClipboard() {
        if (autoPasteToClipboardCheckbox.isSelected()) {
            ClipboardUtil.getInstance().setContents(resultArea.getText());
        }
    }

    private void scheduleExecuteActionPerformed(ActionEvent evt) {
        String replaceText = null;
        Validate.notEmpty((replaceText = replaceArea.getText()), "source can't empty");
        DefaultListModel model = (DefaultListModel) templateList.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            PropConfigHandler.Config entry = (PropConfigHandler.Config) model.getElementAt(ii);
            replaceText = replacer(entry.fromVal, entry.toVal, replaceText);
        }
        resultArea.setText(replaceText);
    }

    /**
     * @param fromPattern
     *            要替換的來源pattern
     * @param toFormat
     *            要替換的目的pattern
     * @param replaceText
     *            要替換的本文
     */
    String replacer(String fromPattern, String toFormat, String replaceText) {
        String errorRtn = replaceText.toString();
        try {
            int patternFlag = 0;

            // 多行判斷
            if (multiLineCheckBox.isSelected()) {
                patternFlag = Pattern.DOTALL | Pattern.MULTILINE;
            }

            Pattern pattern = Pattern.compile(fromPattern, patternFlag);
            Matcher matcher = pattern.matcher(replaceText);

            StringBuffer sb = new StringBuffer();
            String tempStr = null;

            TradeOffConfig config = this.getTradeOffConfig();

            {
                int startPos = 0;
                for (; matcher.find();) {
                    tempStr = toFormat.toString();
                    sb.append(replaceText.substring(startPos, matcher.start()));

                    // ----------------------------------------------
                    if (StringUtils.isBlank(config.fremarkerKey)) {
                        // regex
                        for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                            System.out.println(ii + " -- " + matcher.group(ii));
                            tempStr = tempStr.replaceAll("#" + ii + "#", Matcher.quoteReplacement(matcher.group(ii)));
                        }
                    } else if (StringUtils.isNotBlank(config.fremarkerKey)) {
                        // freemarker
                        Map<String, Object> root = new HashMap<String, Object>();
                        TreeMap<Integer, Object> lstMap = new TreeMap<Integer, Object>();
                        for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                            lstMap.put(ii, matcher.group(ii));
                        }
                        root.put(StringUtils.trimToEmpty(config.fremarkerKey), lstMap.values());
                        System.out.println("template Map : " + root);
                        tempStr = FreeMarkerSimpleUtil.replace(tempStr, root);
                    }
                    // ----------------------------------------------

                    sb.append(tempStr);
                    startPos = matcher.end();
                }
                sb.append(replaceText.substring(startPos));
            }

            return sb.toString();
        } catch (Exception ex) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(), getTitle());
            return errorRtn;
        }
    }

    private class TradeOffConfig {
        String fremarkerKey;
        JSONObject json;

        TradeOffConfig(JSONObject json) {
            this.json = json;
            if (json.containsKey(FREEMARKER_KEY)) {
                fremarkerKey = json.getString(FREEMARKER_KEY);
            }
        }
    }

    private TradeOffConfig getTradeOffConfig() {
        TradeOffConfig EMPTY = new TradeOffConfig(new JSONObject());
        try {
            if (StringUtils.isBlank(tradeOffArea.getText())) {
                return EMPTY;
            }
            return new TradeOffConfig(JSONObject.fromObject(tradeOffArea.getText()));
        } catch (Exception ex) {
            JCommonUtil.handleException("getTradeOffObject ERR : " + ex, ex);
            return EMPTY;
        }
    }

    private static class PropConfigHandler {
        Properties prop;
        File configFile;
        JList templateList;
        JTextArea replaceArea;

        private static String delimit = "#^#";
        private static String delimit_Pattern = "\\Q#^#\\E";

        PropConfigHandler(Properties prop, File configFile, JList templateList, JTextArea replaceArea) {
            this.prop = prop;
            this.configFile = configFile;
            this.templateList = templateList;
            this.replaceArea = replaceArea;
            try {
                if (!propFile.exists()) {
                    propFile.createNewFile();
                }
                this.configFile = propFile;
                System.out.println(propFile + " == " + propFile.exists());
                prop.load(new FileInputStream(propFile));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void deleteConfig(String configKey) throws FileNotFoundException, IOException {
            if (!this.prop.containsKey(configKey)) {
                Validate.isTrue(false, "找不到:" + configKey);
            }

            if (prop.containsKey(configKey)) {
                boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("設定:" + configKey + " 是否要刪除!", "刪除確認");
                if (!deleteConfirm) {
                    return;
                }
            }

            this.prop.remove(configKey);
            this.prop.store(new FileOutputStream(propFile), "remove - " + configKey);
        }

        private void put(String configKey, String fromVal, String toVal, String tradeOff) throws FileNotFoundException, IOException {
            if (prop.containsKey(configKey)) {
                boolean saveConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("設定:" + configKey + " 已存在,是否要覆蓋!", "儲存確認");
                if (!saveConfirm) {
                    return;
                }
            }

            Validate.notEmpty(configKey, "configKey 不可為空!");
            Validate.notEmpty(fromVal, "fromVal 不可為空!");
            Validate.notEmpty(toVal, "toVal 不可為空!");
            tradeOff = StringUtils.trimToEmpty(tradeOff);
            prop.setProperty(configKey, fromVal + delimit + toVal + delimit + tradeOff);

            this.prop.store(new FileOutputStream(propFile), "remove - " + configKey);
        }

        void reloadTemplateList() {
            String repAreaText = StringUtils.trimToEmpty(replaceArea.getText());

            List<Config> lst = new ArrayList<Config>();
            for (Entry<Object, Object> entry : prop.entrySet()) {
                lst.add(new Config(entry));
            }

            for (Config conf : lst) {
                conf.processTradeOff(repAreaText);
            }

            Collections.sort(lst, new Comparator<Config>() {
                @Override
                public int compare(Config c1, Config c2) {
                    if (c1.tradeOffScore > c2.tradeOffScore) {
                        return -1;
                    } else if (c1.tradeOffScore < c2.tradeOffScore) {
                        return 1;
                    } else {
                        int c1Length = new Integer(StringUtils.trimToEmpty(c1.message).length());
                        int c2Length = new Integer(StringUtils.trimToEmpty(c2.message).length());
                        if (c1Length > c2Length) {
                            return -1;
                        } else if (c1Length < c2Length) {
                            return 1;
                        } else {
                            if (c1.isModify && !c2.isModify) {
                                return -1;
                            } else if (!c1.isModify && c2.isModify) {
                                return 1;
                            } else {
                                return c1.configKeyText.compareTo(c2.configKeyText);
                            }
                        }
                    }
                }
            });

            DefaultListModel templateListModel = new DefaultListModel();
            for (Config conf : lst) {
                System.out.println("tradeoff conf : " + conf.configKeyText + "\tscore:" + conf.tradeOffScore);
                templateListModel.addElement(conf);
            }
            templateList.setModel(templateListModel);
        }

        private static class Config {
            Config(Object key, Object value) {
                configKeyText = String.valueOf(key);
                String tmpVal = String.valueOf(value);
                String[] tmpVals = tmpVal.split(delimit_Pattern, -1);
                fromVal = getArry(tmpVals, 0);
                toVal = getArry(tmpVals, 1);
                tradeOff = getArry(tmpVals, 2);
            }

            private String getArry(String[] arry, int pos) {
                if (arry.length > pos) {
                    return arry[pos];
                }
                return "";
            }

            Config(Entry<Object, Object> entry) {
                this(entry.getKey(), entry.getValue());
            }

            String configKeyText;
            String fromVal;
            String toVal;
            String tradeOff;
            int tradeOffScore = 0;
            boolean isModify = false;
            String message;

            Pattern use_ptn = Pattern.compile("^\\/(.*)\\/$");

            private void __tradeOffProcess(String key, int offset, JSONObject json, String script, List<String> messageLst) {
                if (json.containsKey(key)) {
                    JSONArray arry = json.getJSONArray(key);

                    for (int ii = 0; ii < arry.size(); ii++) {
                        String string = arry.getString(ii);

                        boolean matchOk = false;

                        if (string.matches("^\\/.*\\/$")) {
                            Matcher mth = use_ptn.matcher(string);
                            mth.find();
                            Pattern ptn = Pattern.compile(mth.group(1), Pattern.MULTILINE | Pattern.DOTALL);
                            mth = ptn.matcher(script);
                            if (mth.find()) {
                                matchOk = true;
                            }
                        }

                        if (!matchOk && script.toLowerCase().contains(string.toLowerCase())) {
                            matchOk = true;
                        }

                        if (matchOk) {
                            tradeOffScore += offset;
                            String prefix = offset < 0 ? "[扣分項]" : "";
                            messageLst.add(prefix + string);
                            isModify = true;
                        }
                    }
                }
            }

            public void processTradeOff(String script) {
                tradeOffScore = 0;
                message = "";
                isModify = false;// 為改過

                if (StringUtils.isBlank(script) || StringUtils.isBlank(tradeOff)) {
                    return;
                }
                try {
                    JSONObject json = JSONObject.fromObject(tradeOff);
                    List<String> messageLst = new ArrayList<String>();

                    __tradeOffProcess(CONTAIN_ARRY_KEY, 2, json, script, messageLst);
                    __tradeOffProcess(NOT_CONTAIN_ARRY_KEY, -1, json, script, messageLst);

                    message = StringUtils.join(messageLst, ",");

                } catch (Exception ex) {
                    JCommonUtil.handleException("錯誤 :" + configKeyText + " -> " + ex.getMessage(), ex);
                }
            }

            public String toString() {
                return configKeyText + " = /" + StringUtils.trimToEmpty(message) + "/";
            }
        }
    }
}
