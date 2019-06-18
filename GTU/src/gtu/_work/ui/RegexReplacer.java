package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.file.OsInfoUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.string.StringUtil_;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JListUtil.ItemColorTextHandler;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.JTextUndoUtil;
import gtu.swing.util.JTooltipUtil;
import gtu.swing.util.KeyEventExecuteHandler;
import gtu.swing.util.SwingTabTemplateUI;
import gtu.swing.util.SwingTabTemplateUI.ChangeTabHandlerGtu001;
import gtu.swing.util.SwingTabTemplateUI.FocusTabHandlerGtu001;
import gtu.swing.util.SwingTabTemplateUI.SwingTabTemplateUI_Callback;
import gtu.yaml.util.YamlMapUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.util.JSONUtils;

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
    private static final long serialVersionUID = 1L;

    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private HideInSystemTrayHelper hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
    private KeyEventExecuteHandler keyEventExecuteHandler;
    private List<String> tempComboLst;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        // SwingUtilities.invokeLater(new Runnable() {
        // public void run() {
        // RegexReplacer inst = new RegexReplacer();
        // inst.setLocationRelativeTo(null);
        // gtu.swing.util.JFrameUtil.setVisible(true, inst);
        // }
        // });
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            System.out.println("=====" + info.getClassName());
            // javax.swing.UIManager.setLookAndFeel(info.getClassName());
        }
        final SwingTabTemplateUI tabUI = SwingTabTemplateUI.newInstance(null, "cheater.ico", RegexReplacer.class, true, new SwingTabTemplateUI_Callback() {

            @Override
            public void beforeInit(SwingTabTemplateUI self) {
            }

            @Override
            public void afterInit(final SwingTabTemplateUI self) {
                self.getJframe().addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent paramWindowEvent) {
                        try {
                            RegexReplacer ui = (RegexReplacer) self.getJframeKeeperLst().get(self.getSelectTabIndex());
                            ui.configHandler.saveProp();
                        } catch (Exception e) {
                            JCommonUtil.handleException("properties store error!", e);
                        }
                        self.getJframe().setVisible(false);
                        self.getJframe().dispose();
                    }
                });
            }
        });

        tabUI.setEventAfterChangeTab(new ChangeTabHandlerGtu001() {
            public void afterChangeTab(int tabIndex, List<JFrame> jframeKeeperLst) {
                if (jframeKeeperLst != null && !jframeKeeperLst.isEmpty()) {
                    RegexReplacer regex = ((RegexReplacer) jframeKeeperLst.get(tabIndex));
                    regex.jTabbedPane1.setSelectedIndex(0);
                }
            }
        });
        tabUI.setEventOnFocus(new FocusTabHandlerGtu001() {
            public void focusOnWin(List<JFrame> jframeKeeperLst) {
                for (JFrame f : jframeKeeperLst) {
                    RegexReplacer regex = ((RegexReplacer) f);

                    // 還在進行
                    if (System.currentTimeMillis() - regex.keyEventExecuteHandler.getExecuteStartTime() < 1000) {
                        continue;
                    }

                    regex.jTabbedPane1.setSelectedIndex(0);
                }
            }
        });

        tabUI.setSize(700, 550);
        tabUI.startUI();
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
                    jTabbedPane1.addTab("source", null, jPanel1, null);
                    jPanel1.setLayout(new BorderLayout(0, 0));
                    {
                        panel_2 = new JPanel();
                        jPanel1.add(panel_2, BorderLayout.NORTH);
                    }
                    {
                        panel_3 = new JPanel();
                        jPanel1.add(panel_3, BorderLayout.WEST);
                    }
                    {
                        panel_4 = new JPanel();
                        jPanel1.add(panel_4, BorderLayout.EAST);
                    }
                    {
                        panel_5 = new JPanel();
                        jPanel1.add(panel_5, BorderLayout.SOUTH);
                        {
                            clearReplaceAreaBtn = new JButton("清除");
                            clearReplaceAreaBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    replaceArea.setText("");
                                    resultArea.setText("");
                                }
                            });
                            panel_5.add(clearReplaceAreaBtn);
                        }
                    }
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        {
                            replaceArea = new JTextArea();
                            JTextAreaUtil.applyCommonSetting(replaceArea);
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
                        JPanel pppPanel = new JPanel();
                        jPanel2.add(pppPanel, BorderLayout.SOUTH);
                        exeucte = new JButton();
                        exeucte.setText("exeucte");
                        exeucte.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                exeucteActionPerformed(evt);
                            }
                        });
                        pppPanel.add(exeucte);

                        JButton clearTemplateBtn = new JButton();
                        clearTemplateBtn.setText("清除");
                        clearTemplateBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                clearTemplateBtnAction();
                            }
                        });
                        pppPanel.add(clearTemplateBtn);
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
                            JTextUndoUtil.applyUndoProcess1(configKeyText);
                            jPanel3.add(configKeyText, "4, 2, fill, default");
                            configKeyText.setColumns(10);
                        }
                        {
                            lblNewLabel_1 = new JLabel("from");
                            jPanel3.add(lblNewLabel_1, "2, 4, right, default");
                        }
                        {
                            repFromText = new JTextArea();
                            JTextAreaUtil.applyCommonSetting(repFromText);
                            repFromText.setRows(4);
                            jPanel3.add(JCommonUtil.createScrollComponent(repFromText), "4, 4, fill, default");
                            repFromText.setColumns(10);
                        }
                        {
                            lblNewLabel_2 = new JLabel("to");
                            jPanel3.add(lblNewLabel_2, "2, 6, right, default");
                        }
                        {
                            repToText = new JTextArea();
                            JTextAreaUtil.applyCommonSetting(repToText);
                            repToText.setRows(10);
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
                                    String configKey = configKeyText.getText();
                                    String repFrom = repFromText.getText();
                                    String repTo = repToText.getText();
                                    String tradeOff = tradeOffArea.getText();
                                    boolean isMultiLine = multiLineCheckBox.isSelected();

                                    System.out.println("configKey : " + configKey);
                                    System.out.println("repFrom : " + repFrom);
                                    System.out.println("repTo : " + repTo);
                                    System.out.println("tradeOff : " + tradeOff);
                                    System.out.println("isMultiLine : " + isMultiLine);

                                    configHandler.put(configKey, repFrom, repTo, isMultiLine, tradeOff, tempComboLst);

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
                        JPanel northPanel_1 = new JPanel();
                        jPanel5.add(northPanel_1, BorderLayout.NORTH);
                        {
                            lblNewLabel_4 = new JLabel("搜尋");
                            northPanel_1.add(lblNewLabel_4);
                        }
                        {
                            templateListFilterText = new JTextField();
                            northPanel_1.add(templateListFilterText);
                            templateListFilterText.setColumns(30);
                            templateListFilterText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                @Override
                                public void process(DocumentEvent event) {
                                    configHandler.reloadTemplateList(JCommonUtil.getDocumentText(event));
                                }
                            }));
                        }

                        JPanel eastPanel_1 = new JPanel();
                        JPanel westPanel_1 = new JPanel();
                        jPanel5.add(eastPanel_1, BorderLayout.EAST);
                        jPanel5.add(westPanel_1, BorderLayout.WEST);

                        templateList.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                if (templateList.getLeadSelectionIndex() == -1) {
                                    return;
                                }
                                RegexReplacer_Config config = (RegexReplacer_Config) JListUtil.getLeadSelectionObject(templateList);
                                configKeyText.setText(config.configKeyText);
                                repFromText.setText(config.fromVal);
                                repToText.setText(config.toVal);
                                tradeOffArea.setText(config.tradeOff);
                                multiLineCheckBox.setSelected(config.isMultiLine);

                                System.out.println("configKey : " + configKeyText.getText());

                                // 暫放組合技
                                tempComboLst = config.comboLst;
                                resetComboReplaceBtnText();

                                // 放入執行紀錄 並 載入預設
                                configHandler.loadExample(configKeyText.getText());

                                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                                    exeucteActionPerformed(null);

                                    jTabbedPane1.setSelectedIndex(TabIndex.RESULT.ordinal());
                                }
                            }
                        });
                        templateList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                RegexReplacer_Config config = (RegexReplacer_Config) JListUtil.getLeadSelectionObject(templateList);
                                JListUtil.newInstance(templateList).defaultJListKeyPressed(evt, false);
                                try {
                                    if (config != null) {
                                        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                                            configHandler.deleteConfig(config.configKeyText);
                                            configHandler.reloadTemplateList();

                                            configHandler.deleteExample(config.configKeyText);
                                        }
                                    }
                                } catch (Exception e) {
                                    JCommonUtil.handleException(e);
                                }
                            }
                        });

                        JListUtil.newInstance(templateList).applyOnHoverEvent(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                RegexReplacer_Config config = (RegexReplacer_Config) e.getSource();
                                String tip = "<html>" + JTooltipUtil.escapeHtml(config.toVal) + "</html>";
                                templateList.setToolTipText(tip);
                            }
                        });

                        // 改變顏色 ↓↓↓↓↓↓
                        JListUtil.newInstance(templateList).setItemColorTextProcess(new ItemColorTextHandler() {
                            public Pair<String, Color> setColorAndText(Object value) {
                                RegexReplacer_Config config = (RegexReplacer_Config) value;
                                if (config.tradeOffScore != 0) {
                                    return Pair.of(null, Color.GREEN);
                                }
                                return null;
                            }
                        });
                        // 改變顏色 ↑↑↑↑↑↑
                    }
                    {
                        JPanel jpanel = new JPanel();
                        jPanel5.add(jpanel, BorderLayout.SOUTH);
                    }
                }
                {
                    jPanel4 = new JPanel();
                    BorderLayout jPanel4Layout = new BorderLayout();
                    jPanel4.setLayout(jPanel4Layout);
                    jTabbedPane1.addTab("result", null, jPanel4, null);
                    {
                        jScrollPane2 = new JScrollPane();
                        JPanel jPanel4_inner = new JPanel();
                        jPanel4.add(jPanel4_inner, BorderLayout.CENTER);
                        jPanel4_inner.setLayout(new BorderLayout(0, 0));
                        {
                            panel_6 = new JPanel();
                            jPanel4_inner.add(panel_6, BorderLayout.NORTH);
                        }
                        {
                            panel_7 = new JPanel();
                            jPanel4_inner.add(panel_7, BorderLayout.WEST);
                        }
                        {
                            panel_8 = new JPanel();
                            jPanel4_inner.add(panel_8, BorderLayout.SOUTH);
                            {
                                resultAreaClearBtn = new JButton("清除");
                                resultAreaClearBtn.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        resultArea.setText("");
                                    }
                                });
                                panel_8.add(resultAreaClearBtn);
                            }
                            {
                                distinctResultBtn = new JButton("distinct");
                                distinctResultBtn.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            final List<String> lst = StringUtil_.readContentToList(resultArea.getText(), true, true, true);
                                            JPopupMenuUtil.newInstance(distinctResultBtn).applyEvent(e)//
                                                    .addJMenuItem("一般", new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            resultArea.setText(StringUtils.join(lst, "\r\n"));
                                                        }
                                                    })//
                                                    .addJMenuItem("排序", new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            Collections.sort(lst);
                                                            resultArea.setText(StringUtils.join(lst, "\r\n"));
                                                        }
                                                    })//
                                                    .addJMenuItem("排序[反向]", new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            Collections.sort(lst);
                                                            Collections.reverse(lst);
                                                            resultArea.setText(StringUtils.join(lst, "\r\n"));
                                                        }
                                                    })//
                                                    .show();
                                        } catch (Exception ex) {
                                            JCommonUtil.handleException(ex);
                                        }
                                    }
                                });
                                panel_8.add(distinctResultBtn);
                            }
                        }
                        {
                            panel_9 = new JPanel();
                            jPanel4_inner.add(panel_9, BorderLayout.EAST);
                        }
                        {
                            jPanel4_inner.add(jScrollPane2, BorderLayout.CENTER);
                        }
                        {
                            resultArea = new JTextArea();
                            JTextAreaUtil.applyCommonSetting(resultArea);
                            jScrollPane2.setViewportView(resultArea);
                        }
                    }
                }
                //
                panel_1 = new JPanel();
                //
                {
                    yamlConfigFileText = new JTextField();
                    JCommonUtil.jTextFieldSetFilePathMouseEvent(yamlConfigFileText, false);
                    panel_1.add(yamlConfigFileText);
                    yamlConfigFileText.setColumns(40);
                    yamlConfigFileText.setToolTipText("設定yaml設定檔路徑");
                }
                {
                    saveConfigBtn = new JButton("儲存設定");
                    saveConfigBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                configBean.reflectSetConfig(RegexReplacer.this);
                                configBean.store();
                                JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                    {
                        freemarkBaseDirText = new JTextField();
                        JCommonUtil.jTextFieldSetFilePathMouseEvent(freemarkBaseDirText, true);
                        freemarkBaseDirText.setToolTipText("設定freemark專案目錄");
                        freemarkBaseDirText.setColumns(40);
                        panel_1.add(freemarkBaseDirText);
                    }
                    panel_1.add(saveConfigBtn);
                }
                {
                    yamlOpenFileBtn = new JButton("開啟yaml");
                    yamlOpenFileBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                final File file = new File(yamlConfigFileText.getText());
                                final AtomicReference<String> fileURL = new AtomicReference<String>(file.toURL().toString());
                                JPopupMenuUtil.newInstance(yamlOpenFileBtn)//
                                        .addJMenuItem("開啟目錄", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                DesktopUtil.openDir(file);
                                            }
                                        }).addJMenuItem("開啟檔案", new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                DesktopUtil.browse(fileURL.get());
                                            }
                                        }).applyEvent(e)//
                                        .show();
                            } catch (Exception ex) {
                                JCommonUtil.handleException(ex);
                            }
                        }
                    });
                    panel_1.add(yamlOpenFileBtn);
                }
            }
            {
                JCommonUtil.setFont(repToText, repFromText, replaceArea, templateList);
                {
                    jTabbedPane1.addTab("config", null, panel_1, null);
                    panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                }
                {
                    tradeOffArea = new JTextArea();
                    JTextAreaUtil.applyCommonSetting(tradeOffArea);
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
                                        for (SelectionObj e1 : SelectionObj.values()) {
                                            if ("arryKey".equals(e1.type)) {
                                                json.put(e1.key, new JSONArray());
                                            }
                                        }
                                        tradeOff = json.toString();
                                    } else {
                                        json = JSONObject.fromObject(tradeOff);
                                    }

                                    // 加入新的
                                    SelectionObj selectItem = (SelectionObj) JCommonUtil._JOptionPane_showInputDialog("請選擇類型!", "請選擇", SelectionObj.values(), SelectionObj.NA);
                                    if (selectItem == null || selectItem == selectItem.NA) {
                                        return;
                                    }

                                    String string = StringUtils.trimToEmpty(JCommonUtil._jOptionPane_showInputDialog("輸入新項目:"));
                                    string = StringUtils.trimToEmpty(string);

                                    if (StringUtils.isBlank(string)) {
                                        tradeOffArea.setText(json.toString());
                                        return;
                                    }

                                    if (StringUtils.equals(selectItem.type, "arryKey")) {
                                        String arryKey = selectItem.key;
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
                                    } else if (StringUtils.equals(selectItem.type, "boolKey")) {
                                        String boolKey = selectItem.key;
                                        json.put(boolKey, Boolean.valueOf(string));
                                    } else if (StringUtils.equals(selectItem.type, "strKey")) {
                                        String strKey = selectItem.key;
                                        json.put(strKey, string);
                                    } else if (StringUtils.equals(selectItem.type, "intKey")) {
                                        String intKey = selectItem.key;
                                        json.put(intKey, Integer.parseInt(string));
                                    } else {
                                        throw new RuntimeException("無法判斷的新增類型 : " + selectItem);
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
                        {
                            comboReplaceBtn = new JButton("組合技");
                            comboReplaceBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent arg0) {
                                    try {
                                        List<RegexReplacer_Config> rightLst = new ArrayList<RegexReplacer_Config>();
                                        if (tempComboLst != null) {
                                            for (String key : tempComboLst) {
                                                RegexReplacer_Config d = configHandler.getProperty(key);
                                                if (d != null) {
                                                    rightLst.add(d);
                                                }
                                            }
                                        }

                                        final RegexReplacer_ComboDlg<RegexReplacer_Config> dlg = new RegexReplacer_ComboDlg<RegexReplacer_Config>();
                                        dlg.apply(configHandler.orignLst, rightLst, new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent arg0) {
                                                tempComboLst = new ArrayList<String>();
                                                for (RegexReplacer_Config d : dlg.getChoiceLst()) {
                                                    tempComboLst.add(d.configKeyText);
                                                }
                                                resetComboReplaceBtnText();
                                                dlg.dispose();
                                            }
                                        });
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                            panel.add(comboReplaceBtn);
                        }
                    }
                    {
                        lblNewLabel_3 = new JLabel("權重 ");
                        jPanel3.add(lblNewLabel_3, "2, 10");
                    }
                    jPanel3.add(JCommonUtil.createScrollComponent(tradeOffArea), "4, 10, fill, fill");
                }
            }

            // zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz

            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_1.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_1.add(hideInSystemTrayHelper.getToggleButton(false));

            // ui init
            {
                configBean.reflectInit(this);
                if (StringUtils.isBlank(yamlConfigFileText.getText())) {
                    yamlConfigFileText.setText(yamlFile.getAbsolutePath());
                }
            }
            // config init
            {
                File configFile = new File(yamlConfigFileText.getText());
                configHandler = new PropConfigHandler(configFile, templateList, replaceArea, resultArea);
                configHandler.reloadTemplateList();
            }

            this.setSize(672, 506);
            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.defaultToolTipDelay();
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/cheater.ico");
            hideInSystemTrayHelper.apply(this);

            keyEventExecuteHandler = KeyEventExecuteHandler.newInstance(this, null, null, new Runnable() {

                @Override
                public void run() {
                    exeucteActionPerformed(null);
                }
            });

            this.setTitle("You Set My World On Fire");

            JCommonUtil.frameCloseDo(this, new WindowAdapter() {
                public void windowClosing(WindowEvent paramWindowEvent) {
                    try {
                        configHandler.saveProp();
                    } catch (Exception e) {
                        JCommonUtil.handleException("properties store error!", e);
                    }
                    setVisible(false);
                    dispose();
                }
            });
        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea replaceArea;
    private JScrollPane jScrollPane1;
    private JTextArea repFromText;
    private JScrollPane jScrollPane2;
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

    static File yamlFile = new File(PropertiesUtil.getJarCurrentPath(RegexReplacer.class), RegexReplacer.class.getSimpleName() + "_NEW.yml");

    private PropConfigHandler configHandler;
    private PropertiesUtilBean configBean = new PropertiesUtilBean(RegexReplacer.class);

    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JTextField configKeyText;
    private JLabel lblNewLabel_3;
    private JTextArea tradeOffArea;
    private JPanel panel;
    private JCheckBox multiLineCheckBox;
    private JCheckBox autoPasteToClipboardCheckbox;
    private JPanel panel_1;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JButton clearReplaceAreaBtn;
    private JPanel panel_6;
    private JPanel panel_7;
    private JPanel panel_8;
    private JPanel panel_9;
    private JButton resultAreaClearBtn;
    private JLabel lblNewLabel_4;
    private JTextField templateListFilterText;
    private JTextField yamlConfigFileText;
    private JButton saveConfigBtn;
    private JButton yamlOpenFileBtn;
    private JButton comboReplaceBtn;

    AtomicBoolean replaceLock = new AtomicBoolean(false);
    private JTextField freemarkBaseDirText;
    private JButton distinctResultBtn;

    private void exeucteActionPerformed(ActionEvent evt) {
        if (replaceLock.get()) {
            return;
        }
        replaceLock.set(true);
        try {
            TradeOffConfig config = this.getTradeOffConfig(tradeOffArea.getText());

            if (tempComboLst != null && !tempComboLst.isEmpty()) {
                String replaceText = replaceArea.getText();
                for (String comboKey : tempComboLst) {
                    RegexReplacer_Config d = configHandler.getProperty(comboKey);
                    if (d != null) {
                        replaceText = replacerDetail(d.fromVal, d.toVal, replaceText, d.isMultiLine, d.tradeOff);
                    }
                }
                resultArea.setText(replaceText);
            } else if (!config.sourceFiles.isEmpty()) {

                String fromPattern = null;
                String configkeytext = null;
                String toFormat = repToText.getText();
                Validate.notEmpty((configkeytext = configKeyText.getText()), "configKey can't empty");
                Validate.notEmpty((fromPattern = repFromText.getText()), "replace regex can't empty");

                List<String> successLst = new ArrayList<String>();
                List<String> failLst = new ArrayList<String>();
                for (File file : config.sourceFiles) {
                    String replaceText = FileUtil.loadFromFile(file, "UTF8");
                    String replaceTextOk = replacerDetail(fromPattern, toFormat, replaceText, multiLineCheckBox.isSelected(), tradeOffArea.getText());
                    FileUtil.saveToFile(file, replaceTextOk, "UTF8");
                    if (!StringUtils.equals(replaceText, replaceTextOk)) {
                        successLst.add(file.getName());
                    } else {
                        failLst.add(file.getName());
                    }
                }
                JCommonUtil._jOptionPane_showMessageDialog_info("寫檔完成! : " + "\n成功:" + StringUtils.join(successLst, "\n") + "\n失敗:" + StringUtils.join(failLst, "\n"));

            } else {
                // !!!!NORMAL PROCESS HERE!!!!
                String replaceText = null;
                String fromPattern = null;
                String configkeytext = null;
                String toFormat = repToText.getText();
                Validate.notEmpty((configkeytext = configKeyText.getText()), "configKey can't empty");
                Validate.notEmpty((replaceText = replaceArea.getText()), "source can't empty");
                Validate.notEmpty((fromPattern = repFromText.getText()), "replace regex can't empty");
                resultArea.setText(replacerDetail(fromPattern, toFormat, replaceText, multiLineCheckBox.isSelected(), tradeOffArea.getText()));
                // 切換到結果
                jTabbedPane1.setSelectedIndex(TabIndex.RESULT.ordinal());
                // 貼到記事本
                pasteTextToClipboard();

                // 放入執行紀錄
                configHandler.putExample(configKeyText.getText());
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        } finally {
            replaceLock.set(false);
        }
    }

    private void pasteTextToClipboard() {
        if (autoPasteToClipboardCheckbox.isSelected()) {
            ClipboardUtil.getInstance().setContents(resultArea.getText());
        }
    }

    /**
     * @param fromPattern
     *            要替換的來源pattern
     * @param toFormat
     *            要替換的目的pattern
     * @param replaceText
     *            要替換的本文
     */
    String replacerDetail(String fromPattern, String $toFormat, String replaceText, boolean isMultiLine, String tradeOffAreaText) {
        TradeOffConfig config = this.getTradeOffConfig(tradeOffAreaText);

        List<String> toFormatLst = new ArrayList<String>();
        if (StringUtils.isNotBlank(config.split)) {
            String[] arrys = $toFormat.split(Pattern.quote(config.split));
            toFormatLst.addAll(Arrays.asList(arrys));
        } else {
            toFormatLst.add($toFormat);
        }

        List<String> rtnLst = new ArrayList<String>();
        for (String toFormat : toFormatLst) {
            try {
                int patternFlag = 0;

                // 多行判斷
                if (isMultiLine) {
                    patternFlag = Pattern.DOTALL | Pattern.MULTILINE;
                }

                Pattern pattern = Pattern.compile(fromPattern, patternFlag);
                Matcher matcher = pattern.matcher(replaceText);

                StringBuffer sb = new StringBuffer();
                String tempStr = null;
                {
                    if (StringUtils.isNotBlank(config.prefix)) {
                        sb.append(config.prefix + "\r\n");
                    }

                    int startPos = 0;
                    for (; matcher.find();) {
                        tempStr = toFormat.toString();

                        if (!config.isOnlyMatch) {
                            sb.append(replaceText.substring(startPos, matcher.start()));
                        }

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
                            tempStr = FreeMarkerSimpleUtil.replace(new File(freemarkBaseDirText.getText()), tempStr, root);
                        }
                        // ----------------------------------------------

                        sb.append(tempStr);
                        startPos = matcher.end();

                        if (config.isOnlyMatch) {
                            sb.append("\r\n");
                        }
                    }

                    if (!config.isOnlyMatch) {
                        sb.append(replaceText.substring(startPos));
                    }

                    if (StringUtils.isNotBlank(config.suffix)) {
                        sb.append(config.suffix + "\r\n");
                    }
                }

                rtnLst.add(sb.toString());
            } catch (Exception ex) {
                // JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(),
                // getTitle());
                JCommonUtil.handleException(ex);
                return "";
            }
        }

        return StringUtils.join(rtnLst, "\r\n");
    }

    private class TradeOffConfig {
        String fremarkerKey;
        String prefix;
        String suffix;
        String split;
        boolean isOnlyMatch = false;
        JSONObject json;
        List<File> sourceFiles = new ArrayList<File>();

        TradeOffConfig(JSONObject json) {
            this.json = json;
            if (json.containsKey(SelectionObj.ftl.key)) {
                fremarkerKey = json.getString(SelectionObj.ftl.key);
            }
            if (json.containsKey(SelectionObj.only_match.key)) {
                isOnlyMatch = json.getBoolean(SelectionObj.only_match.key);
            }
            if (json.containsKey(SelectionObj.prefix.key)) {
                prefix = json.getString(SelectionObj.prefix.key);
            }
            if (json.containsKey(SelectionObj.suffix.key)) {
                suffix = json.getString(SelectionObj.suffix.key);
            }
            if (json.containsKey(SelectionObj.split.key)) {
                split = json.getString(SelectionObj.split.key);
            }
            if (json.containsKey(SelectionObj.sourceFiles.key)) {
                JSONArray fileString = json.getJSONArray(SelectionObj.sourceFiles.key);
                for (int ii = 0; ii < fileString.size(); ii++) {
                    File file = new File(fileString.getString(ii));
                    if (file.exists()) {
                        sourceFiles.add(file);
                    }
                }
            }
        }
    }

    private TradeOffConfig getTradeOffConfig(String tradeOffAreaText) {
        TradeOffConfig EMPTY = new TradeOffConfig(new JSONObject());
        try {
            if (StringUtils.isBlank(tradeOffAreaText)) {
                return EMPTY;
            }
            return new TradeOffConfig(JSONObject.fromObject(tradeOffAreaText));
        } catch (Exception ex) {
            JCommonUtil.handleException("getTradeOffObject ERR : " + ex, ex);
            return EMPTY;
        }
    }

    private static class PropConfigHandler {
        File configFile;
        JList templateList;
        JTextArea replaceArea;
        JTextArea resultArea;

        List<RegexReplacer_Config> orignLst = Collections.EMPTY_LIST;

        private String fixKey(String fixKey) {
            return StringUtils.trimToEmpty(StringUtils.trimToEmpty(fixKey).replaceAll("\n", ""));
        }

        private RegexReplacer_Config getProperty(String configKey) {
            configKey = fixKey(configKey);
            for (RegexReplacer_Config d : orignLst) {
                d.configKeyText = fixKey(d.configKeyText);
                if (StringUtils.equals(configKey, d.configKeyText)) {
                    return d;
                }
            }
            return null;
        }

        PropConfigHandler(File configFile, JList templateList, JTextArea replaceArea, JTextArea resultArea) {
            this.configFile = configFile;
            this.templateList = templateList;
            this.replaceArea = replaceArea;
            this.resultArea = resultArea;
            this.reloadInit();
        }

        private void reloadInit() {
            try {
                System.out.println(configFile + " == " + configFile.exists());
                if (!configFile.exists()) {
                    configFile.createNewFile();
                }
                orignLst = YamlMapUtil.getInstance().loadFromFile(configFile, RegexReplacer_Config.class, null);
                if (orignLst == null) {
                    orignLst = new ArrayList<RegexReplacer_Config>();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void deleteConfig(String configKey) {
            configKey = fixKey(configKey);
            RegexReplacer_Config d = getProperty(configKey);
            if (d == null) {
                Validate.isTrue(false, "找不到:" + configKey);
            }

            if (d != null) {
                boolean deleteConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("設定:" + configKey + " 是否要刪除!", "刪除確認");
                if (!deleteConfirm) {
                    return;
                }
            }

            orignLst.remove(d);
            this.saveProp();
        }

        private void put(String configKey, String fromVal, String toVal, boolean isMultiLine, String tradeOff, List<String> comboLst) throws FileNotFoundException, IOException {
            configKey = fixKey(configKey);
            RegexReplacer_Config d = getProperty(configKey);
            if (d != null) {
                boolean saveConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("設定:" + configKey + " 已存在,是否要覆蓋!", "儲存確認");
                if (!saveConfirm) {
                    return;
                }
            }

            Validate.notEmpty(configKey, "configKey 不可為空!");
            Validate.notEmpty(fromVal, "fromVal 不可為空!");
            if (StringUtils.isBlank(toVal) && !JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("toVal 為空?", "check")) {
                return;
            }

            if ((comboLst != null && !comboLst.isEmpty()) && StringUtils.isNotBlank(toVal)) {
                Validate.isTrue(false, "comboLst不為空!!");
            }

            tradeOff = StringUtils.trimToEmpty(tradeOff);

            if (d == null) {
                d = new RegexReplacer_Config();
                orignLst.add(d);
            }
            d.configKeyText = configKey;
            d.fromVal = fromVal;
            d.toVal = toVal;
            d.tradeOff = tradeOff;
            d.comboLst = comboLst;
            d.isMultiLine = isMultiLine;

            this.saveProp();
        }

        public void putExample(String key) {
            key = fixKey(key);
            if (StringUtils.equals(resultArea.getText(), replaceArea.getText())) {
                return;
            }
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(replaceArea.getText())) {
                RegexReplacer_Config d = getProperty(key);
                if (d != null) {
                    d.exampleArea = replaceArea.getText();
                }
                saveProp();
            }
        }

        public void loadExample(String key) {
            key = fixKey(key);
            RegexReplacer_Config d = getProperty(key);
            if (d == null) {
                return;
            }
            if (StringUtils.isBlank(replaceArea.getText()) && StringUtils.isNotBlank(d.exampleArea)) {
                replaceArea.setText(d.exampleArea);
            }
        }

        public void deleteExample(String configKeyText) {
            configKeyText = fixKey(configKeyText);
            RegexReplacer_Config d = getProperty(configKeyText);
            if (d != null) {
                d.exampleArea = "";
                saveProp();
            }
        }

        private void saveProp() {
            File yamlFile = new File(configFile.getParentFile(), FileUtil.getNameNoSubName(configFile) + ".yml");
            YamlMapUtil.getInstance().saveToFilePlain(yamlFile, orignLst, false, null);
            System.out.println("YAML SAVE!!!!");
        }

        private static String formatByNetSf(Object jsonObject) {
            try {
                if (StringUtils.isBlank((String) jsonObject)) {
                    return "";
                }
                if (jsonObject instanceof String) {
                    return JSONUtils.valueToString(JSONSerializer.toJSON(jsonObject), 8, 4);
                } else {
                    return net.sf.json.util.JSONUtils.valueToString(jsonObject, 8, 4);
                }
            } catch (Throwable ex) {
                throw new RuntimeException("formatByNetSf ERR : " + ex.getMessage() + " --> " + jsonObject, ex);
            }
        }

        void reloadTemplateList() {
            reloadTemplateList("");
        }

        void reloadTemplateList(String filterText) {
            reloadInit();

            filterText = StringUtils.trimToEmpty(filterText).toLowerCase();
            String repAreaText = StringUtils.trimToEmpty(replaceArea.getText());

            List<RegexReplacer_Config> lst = new ArrayList<RegexReplacer_Config>();
            for (RegexReplacer_Config conf : orignLst) {
                boolean findOk = false;
                if (StringUtils.isBlank(filterText)) {
                    findOk = true;
                } else {
                    if (StringUtils.trimToEmpty(conf.configKeyText).toLowerCase().contains(filterText)) {
                        findOk = true;
                    }
                }
                if (findOk) {
                    lst.add(conf);
                }
            }

            for (RegexReplacer_Config conf : lst) {
                conf.processTradeOff(repAreaText);
            }

            Collections.sort(lst, new Comparator<RegexReplacer_Config>() {
                @Override
                public int compare(RegexReplacer_Config c1, RegexReplacer_Config c2) {
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
            for (RegexReplacer_Config conf : lst) {
                System.out.println("tradeoff conf : " + conf.configKeyText + "\tscore:" + conf.tradeOffScore);
                templateListModel.addElement(conf);
            }
            templateList.setModel(templateListModel);
        }
    }

    private void resetComboReplaceBtnText() {
        String color = "black";
        String text = "組合技";
        if (tempComboLst != null && !tempComboLst.isEmpty()) {
            color = "red";
            text = "組合技" + tempComboLst.size();
        }
        comboReplaceBtn.setText(String.format("<html><font color='%s'>%s</font></html>", color, text));
    }

    public static class RegexReplacer_Config {

        boolean isMultiLine;// 1
        String exampleArea;// 1
        String configKeyText;// 1
        String fromVal;// 1
        String toVal;// 1
        String tradeOff;// 1
        List<String> comboLst;// 1

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

                __tradeOffProcess(SelectionObj.equal.key, 2, json, script, messageLst);
                __tradeOffProcess(SelectionObj.not_equal.key, -1, json, script, messageLst);

                message = StringUtils.join(messageLst, ",");

            } catch (Exception ex) {
                JCommonUtil.handleException("錯誤 :" + configKeyText + " -> " + ex.getMessage(), ex);
            }
        }

        public String toString() {
            String prefix = "";
            if (comboLst != null && !comboLst.isEmpty()) {
                prefix = " Combo ";
            }
            return String.format("<html><font color='red'>%1$s</font>%2$s</html>", prefix, configKeyText + " = /" + StringUtils.trimToEmpty(message) + "/");
        }

        public String getExampleArea() {
            return exampleArea;
        }

        public void setExampleArea(String exampleArea) {
            this.exampleArea = exampleArea;
        }

        public String getConfigKeyText() {
            return configKeyText;
        }

        public void setConfigKeyText(String configKeyText) {
            this.configKeyText = configKeyText;
        }

        public String getFromVal() {
            return fromVal;
        }

        public void setFromVal(String fromVal) {
            this.fromVal = StringUtils.defaultString(fromVal).replaceAll("[\t\r\n\\s]$", "");
        }

        public String getToVal() {
            return toVal;
        }

        public void setToVal(String toVal) {
            this.toVal = toVal;
        }

        public String getTradeOff() {
            return tradeOff;
        }

        public void setTradeOff(String tradeOff) {
            this.tradeOff = tradeOff;
        }

        public List<String> getComboLst() {
            return comboLst;
        }

        public void setComboLst(List<String> comboLst) {
            this.comboLst = comboLst;
        }

        public boolean isMultiLine() {
            return isMultiLine;
        }

        public void setMultiLine(boolean isMultiLine) {
            this.isMultiLine = isMultiLine;
        }
    }

    private enum SelectionObj {
        NA("na", "NA", "NA"), //
        equal("arry", "equal (含有的字串  ,正則加//)", "arryKey"), //
        not_equal("not_arry", "not_equal (不含有的字串  ,正則加//)", "arryKey"), //
        ftl("ftl", "ftl (設定ftl變數 , ex:arry)", "strKey"), //
        only_match("only_match", "only_match (是否只抓group, true|false)", "boolKey"), //
        prefix("prefix", "prefix (前置文字)", "strKey"), //
        suffix("suffix", "suffix (後置文字)", "strKey"), //
        split("split", "split (分頁)", "strKey"), //
        sourceFiles("sourceFiles", "sourceFiles (固定來源檔)", "arryKey"),//
        ;
        final String key;
        final String label;
        final String type;

        SelectionObj(String key, String label, String type) {
            this.key = key;
            this.label = label;
            this.type = type;
        }

        public String toString() {
            return label;
        }
    }

    private void clearTemplateBtnAction() {
        configKeyText.setText("");
        repFromText.setText("");
        repToText.setText("");
        tradeOffArea.setText("");
        multiLineCheckBox.setSelected(false);
        autoPasteToClipboardCheckbox.setSelected(false);
        this.tempComboLst = new ArrayList<String>();
    }
}