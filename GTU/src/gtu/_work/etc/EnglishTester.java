package gtu._work.etc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

import gtu.clipboard.ClipboardUtil;
import gtu.number.RandomUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.KeyEventUtil;
import gtu.swing.util.SysTrayUtil;

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
public class EnglishTester extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JButton saveBtn;
    private JButton startAllBtn;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane1;
    private JButton removeBtn;
    private JButton skipBtn;
    private JPanel jPanel5;
    private JButton startNow;
    private JPanel jPanel10;
    private JTextField showEnglishText;
    private JCheckBox picOnly;
    private JCheckBox showPicChkBox;
    private JCheckBox sortChkBox;
    private JCheckBox showChineseOption;
    private JButton picCheckBtn;
    private JTextField picCheckText;
    private JPanel jPanel9;
    private JButton scanPicBtn;
    private JButton saveConfigBtn2;
    private JButton[] answerBtn = new JButton[4];
    private JButton savePickBtn;
    private JButton pickBtn;
    private JTextField queryText;
    private JButton yahooDicBtn;
    private JButton googleSearchBtn;
    private JSlider fontSizeSliber;
    private JLabel propCountLabel;
    private JLabel questionCountLabel;
    private JScrollPane jScrollPane4;
    private JList fileList;
    private JPanel jPanel6;
    private JPanel jPanel4;
    private JTable propTable;
    private JTextArea englishArea;
    private JPanel jPanel3;
    private JPanel jPanel2;

    EnglishTester_showPicDialog showPicDialog;

    private File configFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), this.getClass().getSimpleName() + ".properties");
    private PropertiesUtilBean configProp = new PropertiesUtilBean(EnglishTester.class);
    private ConfigPropHelper configHelper = new ConfigPropHelper();
    
    private interface ConfigKey {
        String PIC_DIR_KEY = "picDir";
    }
    
    private static final int DELAY_TIME = 100;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EnglishTester inst = new EnglishTester();
//                inst.setLocationRelativeTo(null);
                JCommonUtil.setLocationToRightBottomCorner(inst);
                inst.setVisible(true);
            }
        });
    }

    public EnglishTester() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            JCommonUtil.defaultToolTipDelay();
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
                getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
                jTabbedPane1.setPreferredSize(new java.awt.Dimension(462, 259));

                jTabbedPane1.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        // XXX
                        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
                        jTabbedPane1.requestFocus();// 設定FOCUS TODO
                        // XXX
                        // XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx
                    }
                });
                jTabbedPane1.addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent evt) {
                        System.out.println("2===" + evt.getKeyCode());
                        if (evt.getKeyCode() == 49) {// 0建
                            jTabbedPane1.setSelectedIndex(0);
                        }
                        if (evt.getKeyCode() == 50) {// 1建
                            jTabbedPane1.setSelectedIndex(1);
                        }
                        if (evt.getKeyCode() == 10) {// enter建
                            skipBtnAction();
                        }
                        if (evt.getKeyCode() == 32) {// 空白建
                            removeBtnAction();
                        }
                    }
                });
                {
                    jPanel1 = new JPanel();
                    BorderLayout jPanel1Layout = new BorderLayout();
                    jPanel1.setLayout(jPanel1Layout);
                    jTabbedPane1.addTab("english", null, jPanel1, null);
                    {
                        jScrollPane1 = new JScrollPane();
                        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
                        jScrollPane1.setPreferredSize(new java.awt.Dimension(420, 141));
                        {
                            englishArea = new JTextArea();
                            jScrollPane1.setViewportView(englishArea);
                            englishArea.setFont(new java.awt.Font("Microsoft JhengHei", 0, 22));
                        }
                    }
                    {
                        jPanel5 = new JPanel();
                        jPanel1.add(jPanel5, BorderLayout.SOUTH);
                        jPanel5.setPreferredSize(new java.awt.Dimension(402, 65));
                        {
                            skipBtn = new JButton();
                            jPanel5.add(skipBtn);
                            skipBtn.setText("skip");
                            skipBtn.setPreferredSize(new java.awt.Dimension(187, 24));
                            skipBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    skipBtnAction();
                                }
                            });
                        }
                        {
                            removeBtn = new JButton();
                            jPanel5.add(removeBtn);
                            removeBtn.setText("remove");
                            removeBtn.setPreferredSize(new java.awt.Dimension(180, 24));
                            removeBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    removeBtnAction();
                                }
                            });
                        }
                        {
                            questionCountLabel = new JLabel();
                            jPanel5.add(questionCountLabel);
                            questionCountLabel.setPreferredSize(new java.awt.Dimension(47, 21));
                            questionCountLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                            questionCountLabel.setToolTipText("剩餘數");
                        }
                        {
                            propCountLabel = new JLabel();
                            jPanel5.add(propCountLabel);
                            propCountLabel.setPreferredSize(new java.awt.Dimension(45, 21));
                            propCountLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                            propCountLabel.setToolTipText("總數");
                        }
                        {
                            googleSearchBtn = new JButton();
                            jPanel5.add(googleSearchBtn);
                            googleSearchBtn.setText("<html>GPic</html>");
                            googleSearchBtn.setPreferredSize(new java.awt.Dimension(58, 24));
                            googleSearchBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    try {
                                        String word = currentWordIndex.trim();
                                        ClipboardUtil.getInstance().setContents(word);
                                        word = word.replace(" ", "%20");
                                        URI uri = new URI("https://www.google.com.tw/search?num=10&hl=zh-TW&site=imghp&tbm=isch&source=hp&biw=1280&bih=696&q=" + word);
                                        //URI uri = new URI("http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=" + word);
                                        Desktop.getDesktop().browse(uri);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                        }
                        {
                            yahooDicBtn = new JButton();
                            jPanel5.add(yahooDicBtn);
                            yahooDicBtn.setText("<html>Dict</html>");
                            yahooDicBtn.setPreferredSize(new java.awt.Dimension(57, 24));
                            yahooDicBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    try {
                                        // URI uri = new
                                        // URI("http://tw.dictionary.yahoo.com/dictionary?p="
                                        // + currentWord.trim());
                                        URI uri = new URI("http://www.dreye.com/axis/ddict.jsp?ver=big5&dod=0102&w=" + currentWordIndex.trim() + "&x=0&y=0");
                                        Desktop.getDesktop().browse(uri);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                        }
                        {
                            pickBtn = new JButton();
                            jPanel5.add(pickBtn);
                            pickBtn.setText("<html>+Pick</html>");
                            pickBtn.setPreferredSize(new java.awt.Dimension(60, 24));
                            pickBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    try {
                                        String key = currentWordIndex;
                                        String value = englishProp.getProperty(currentWordIndex);
                                        if (StringUtils.isEmpty(value)) {
                                            JCommonUtil._jOptionPane_showMessageDialog_error("add pick failed : no such word => " + key);
                                        } else {
                                            pickProp.setProperty(key, value);
                                            JCommonUtil._jOptionPane_showMessageDialog_info("key=" + key + "\nvalue=" + value + "\nsize=" + pickProp.size(), "加入特選成功");
                                        }
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                        }
                        {
                            scanPicBtn = new JButton();
                            scanPicBtn.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                }
                            });
                            jPanel5.add(scanPicBtn);
                            scanPicBtn.setPreferredSize(new java.awt.Dimension(46, 24));
                            scanPicBtn.addMouseListener(new MouseAdapter() {

                                public void mouseClicked(MouseEvent evt) {
                                    if (picDir == null) {
                                        JCommonUtil._jOptionPane_showMessageDialog_error("picDir is null");
                                        return;
                                    }

                                    if (picSet != null && picSet.size() > 0) {
                                        try {
                                            Desktop.getDesktop().open(picSet.iterator().next());
                                        } catch (IOException e) {
                                            JCommonUtil.handleException(e);
                                        }
                                        return;
                                    }

                                    try {
                                        String text = currentWordIndex.trim().toLowerCase();
                                        ClipboardUtil.getInstance().setContents(text);
                                        text = text.replace(" ", "%20");
                                        URI uri = new URI("https://www.google.com.tw/search?num=10&hl=zh-TW&site=imghp&tbm=isch&source=hp&biw=1280&bih=696&q=" + text);
                                        //URI uri = new URI("http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=" + text);
                                        Desktop.getDesktop().browse(uri);
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                        }
                        {
                            showChineseOption = new JCheckBox();
                            showChineseOption.setSelected(true);
                            jPanel5.add(showChineseOption);
                        }
                    }
                }
                {
                    jPanel2 = new JPanel();
                    BorderLayout jPanel2Layout = new BorderLayout();
                    jPanel2.setLayout(jPanel2Layout);
                    jTabbedPane1.addTab("chinese", null, jPanel2, null);
                    jPanel2.setPreferredSize(new java.awt.Dimension(420, 211));
                    {
                        showEnglishText = new JTextField();
                        jPanel2.add(showEnglishText, BorderLayout.NORTH);
                        showEnglishText.setEditable(false);
                    }
                    {
                        jPanel10 = new JPanel();
                        jPanel2.add(jPanel10, BorderLayout.CENTER);
                    }
                    {
                        answerBtn[0] = new JButton();
                        jPanel10.add(answerBtn[0]);
                        answerBtn[0].setPreferredSize(new java.awt.Dimension(190, 110));
                        answerBtn[0].addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                answerBtnClick(answerBtn[0]);
                            }
                        });
                    }
                    {
                        answerBtn[1] = new JButton();
                        jPanel10.add(answerBtn[1]);
                        answerBtn[1].setPreferredSize(new java.awt.Dimension(190, 110));
                        answerBtn[1].addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                answerBtnClick(answerBtn[1]);
                            }
                        });
                    }
                    {
                        answerBtn[2] = new JButton();
                        jPanel10.add(answerBtn[2]);
                        answerBtn[2].setPreferredSize(new java.awt.Dimension(190, 110));
                        answerBtn[2].addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                answerBtnClick(answerBtn[2]);
                            }
                        });
                    }
                    {
                        answerBtn[3] = new JButton();
                        jPanel10.add(answerBtn[3]);
                        answerBtn[3].setPreferredSize(new java.awt.Dimension(190, 110));
                        answerBtn[3].addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                answerBtnClick(answerBtn[3]);
                            }
                        });
                    }
                    {
                        for (int ii = 0; ii < 4; ii++) {
                            answerBtn[ii].setFont(new java.awt.Font("標楷體", 0, 14));
                        }
                    }
                }
                {
                    jPanel3 = new JPanel();
                    BorderLayout jPanel3Layout = new BorderLayout();
                    jPanel3.setLayout(jPanel3Layout);
                    jTabbedPane1.addTab("word", null, jPanel3, null);
                    {
                        jScrollPane3 = new JScrollPane();
                        jPanel3.add(jScrollPane3, BorderLayout.CENTER);
                        jScrollPane3.setPreferredSize(new java.awt.Dimension(420, 187));
                        {
                            propTable = new JTable();
                            jScrollPane3.setViewportView(propTable);
                            JTableUtil.defaultSetting(propTable);
                            propTable.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    JPopupMenuUtil.newInstance(propTable).addJMenuItem(JTableUtil.newInstance(propTable).getDefaultJMenuItems()).applyEvent(evt).show();
                                }
                            });
                        }
                    }
                    {
                        saveBtn = new JButton();
                        jPanel3.add(saveBtn, BorderLayout.SOUTH);
                        saveBtn.setText("save table");
                        saveBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                DefaultTableModel model = JTableUtil.newInstance(propTable).getModel();
                                for (int ii = 0; ii < model.getRowCount(); ii++) {
                                    String key = (String) model.getValueAt(ii, 0);
                                    String value = (String) model.getValueAt(ii, 1);
                                    if (!englishProp.containsKey(key)) {
                                        englishProp.setProperty(key, value);
                                    }
                                }
                                try {
                                    englishProp.store(new FileOutputStream(englishFile), "comment");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                JCommonUtil._jOptionPane_showMessageDialog_info("save file ok!  \n" + englishFile);
                            }
                        });
                    }
                    {
                        queryText = new JTextField();
                        jPanel3.add(queryText, BorderLayout.NORTH);
                        queryText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                            @Override
                            public void process(DocumentEvent event) {
                                String text = JCommonUtil.getDocumentText(event);
                                Pattern pattern = Pattern.compile(text);
                                Matcher matcher = null;
                                DefaultTableModel propTableModel = JTableUtil.createModel(false, "english", "chinese");
                                for (Enumeration<?> enu = englishProp.propertyNames(); enu.hasMoreElements();) {
                                    String key = (String) enu.nextElement();
                                    String value = englishProp.getProperty(key);
                                    if (key.contains(text)) {
                                        propTableModel.addRow(new Object[] { key, value });
                                        continue;
                                    }
                                    matcher = pattern.matcher(key);
                                    if (matcher.find()) {
                                        propTableModel.addRow(new Object[] { key, value });
                                        continue;
                                    }
                                }
                                propTable.setModel(propTableModel);
                            }
                        }));
                    }
                }
                {
                    jPanel4 = new JPanel();
                    jTabbedPane1.addTab("config", null, jPanel4, null);
                    {
                        savePickBtn = new JButton();
                        jPanel4.add(savePickBtn);
                        savePickBtn.setText("save pick");
                        savePickBtn.setPreferredSize(new java.awt.Dimension(116, 40));
                        savePickBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                if (englishFile == null) {
                                    File file = new File(//
                                            PropertiesUtil.getJarCurrentPath(EnglishTester.class), "temp.properties");
                                    englishFile = file;
                                }
                                if (pickProp.isEmpty()) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("沒有任何字可存檔!");
                                    return;
                                }
                                String fileName = englishFile.getName().replaceAll("\\.properties", "_bak.properties");
                                File jarWhereFile = PropertiesUtil.getJarCurrentPath(EnglishTester.class);
                                fileName = JCommonUtil._jOptionPane_showInputDialog("save target properties", fileName);
                                if (StringUtils.isEmpty(fileName)) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("can't save!");
                                    return;
                                }
                                if (fileName.equalsIgnoreCase(englishFile.getName())) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("不可與englishFile檔名相同");
                                    return;
                                }
                                if (!fileName.endsWith(".properties")) {
                                    fileName += ".properties";
                                }
                                File newFile = new File(jarWhereFile, fileName);
                                Properties oldProp = new Properties();
                                if (newFile.exists()) {
                                    try {
                                        oldProp.load(new FileInputStream(newFile));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                oldProp.putAll(pickProp);
                                try {
                                    oldProp.store(new FileOutputStream(newFile), "comment");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                JCommonUtil._jOptionPane_showMessageDialog_info("save file ok!  \n" + newFile);
                            }
                        });
                    }
                    {
                        saveConfigBtn2 = new JButton();
                        jPanel4.add(saveConfigBtn2);
                        saveConfigBtn2.setText("save config");
                        saveConfigBtn2.setPreferredSize(new java.awt.Dimension(108, 40));
                        saveConfigBtn2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                saveConfigBtnAction();
                            }
                        });
                    }
                    {
                        startAllBtn = new JButton();
                        jPanel4.add(startAllBtn);
                        startAllBtn.setText("start all");
                        startAllBtn.setPreferredSize(new java.awt.Dimension(101, 40));
                        startAllBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                Object[] files = fileList.getSelectedValues();
                                if (files == null || files.length == 0) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("無選擇任何properties檔");
                                    return;
                                }
                                Properties allProp = new Properties();
                                Properties prop = new Properties();
                                for (Object ff : files) {
                                    try {
                                        prop.load(new FileInputStream((File) ff));
                                    } catch (Exception e) {
                                        JCommonUtil.handleException(e);
                                    }
                                    allProp.putAll(prop);
                                }
                                englishProp = allProp;
                                System.out.println("englishProp = " + englishProp.size());
                                startNow();
                            }
                        });
                    }
                    {
                        startNow = new JButton();
                        jPanel4.add(startNow);
                        startNow.setText("start now");
                        startNow.setPreferredSize(new java.awt.Dimension(101, 40));

                        startNow.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                startNow();
                            }
                        });
                    }
                    {
                        picOnly = new JCheckBox();
                        jPanel4.add(picOnly);
                        picOnly.setText("picOnly");
                    }
                    {
                        sortChkBox = new JCheckBox();
                        jPanel4.add(sortChkBox);
                        sortChkBox.setText("sort");
                    }
                    {
                        showPicChkBox = new JCheckBox();
                        showPicChkBox.setSelected(true);
                        jPanel4.add(showPicChkBox);
                        showPicChkBox.setText("showPic");
                    }
                    {
                        JCommonUtil.defaultToolTipDelay();
                        fontSizeSliber = new JSlider(JSlider.HORIZONTAL);
                        jPanel4.add(fontSizeSliber);
                        fontSizeSliber.setPreferredSize(new java.awt.Dimension(419, 35));
                        fontSizeSliber.setValue(22);
                        fontSizeSliber.setMinimum(22);
                        fontSizeSliber.setMaximum(300);
                        fontSizeSliber.setMajorTickSpacing(30);
                        fontSizeSliber.setMinorTickSpacing(5);
                        fontSizeSliber.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        fontSizeSliber.setPaintTicks(false);
                        fontSizeSliber.setPaintLabels(true);
                        {
                            picFolderDirText = new JTextField();
                            JCommonUtil.jTextFieldSetFilePathMouseEvent(picFolderDirText, true);
                            jPanel4.add(picFolderDirText);
                            picFolderDirText.setColumns(20);
                        }
                        fontSizeSliber.addChangeListener(new ChangeListener() {
                            @Override
                            public void stateChanged(ChangeEvent e) {
                                int size = fontSizeSliber.getValue();
                                fontSizeSliber.setToolTipText("" + size);
                                englishArea.setFont(new java.awt.Font("Microsoft JhengHei", 0, size));
                            }
                        });

                    }
                }
                {
                    jPanel6 = new JPanel();
                    jTabbedPane1.addTab("files", null, jPanel6, null);
                    BorderLayout jPanel6Layout = new BorderLayout();
                    jPanel6.setLayout(jPanel6Layout);
                    {
                        jScrollPane4 = new JScrollPane();
                        jPanel6.add(jScrollPane4, BorderLayout.CENTER);
                        jScrollPane4.setPreferredSize(new java.awt.Dimension(420, 211));
                        {
                            fileList = new JList();
                            reloadFileList();
                            jScrollPane4.setViewportView(fileList);
                            fileList.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    final File file = JListUtil.getLeadSelectionObject(fileList);
                                    if (JMouseEventUtil.buttonRightClick(1, evt)) {
                                        JPopupMenuUtil.newInstance(EnglishTester.this.fileList).applyEvent(evt)//
                                                .addJMenuItem("reload", new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        reloadFileList();
                                                    }
                                                })//
                                                .addJMenuItem("delete : " + file.getName(), new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("delete : " + file.getName() + " ?", "confirm");
                                                        if (result) {
                                                            file.delete();
                                                            reloadFileList();
                                                        }
                                                    }//
                                                }).show();
                                        return;
                                    }
                                    if (evt.getClickCount() == 1) {
                                        return;
                                    }
                                    if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("目前的題目將被清除,確定開啟此檔案?\n" + file.getName(), "警告")) {
                                        loadEnglishFile(file);
                                    }
                                }
                            });
                            fileList.addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    JListUtil.newInstance(fileList).defaultJListKeyPressed(evt);
                                }
                            });
                        }
                    }
                }
                {
                    jPanel9 = new JPanel();
                    jTabbedPane1.addTab("pic", null, jPanel9, null);
                    {
                        picCheckText = new JTextField();
                        jPanel9.add(picCheckText);
                        picCheckText.setPreferredSize(new java.awt.Dimension(177, 39));
                    }
                    {
                        picCheckBtn = new JButton();
                        jPanel9.add(picCheckBtn);
                        picCheckBtn.setText("check");
                        picCheckBtn.setPreferredSize(new java.awt.Dimension(98, 43));
                        {
                            jPanel11 = new JPanel();
                            jTabbedPane1.addTab("輸入練習", null, jPanel11, null);
                            jPanel11.setLayout(new BorderLayout(0, 0));
                            {
                                inputTestArea2 = new JTextArea();
                                inputTestArea2.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
                                inputTestArea2.addKeyListener(new KeyAdapter() {
                                    @Override
                                    public void keyReleased(KeyEvent e) {
                                        inputTestTrainer.keyin(e);
                                    }
                                });
                                jPanel11.add(inputTestArea2, BorderLayout.SOUTH);
                            }
                            {
                                inputTestArea1 = new JTextArea();
                                JTextAreaUtil.setWrapTextArea(inputTestArea1);
                                inputTestArea1.setFont(new Font("微軟正黑體", Font.PLAIN, 22));
                                jPanel11.add(inputTestArea1, BorderLayout.CENTER);
                            }
                            {
                                panel = new JPanel();
                                jPanel11.add(panel, BorderLayout.NORTH);
                                {
                                    inputTestLabel = new JLabel("");
                                    panel.add(inputTestLabel);
                                }
                                {
                                    inputTestChk = new JCheckBox("");
                                    inputTestChk.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            inputTestTrainer.initQuestion();
                                        }
                                    });
                                    panel.add(inputTestChk);
                                }
                            }
                        }
                        picCheckBtn.addActionListener(new ActionListener() {
                            void scanPic(String searchWord, File file, Set<File> findFile) {
                                if (file.isDirectory()) {
                                    File[] list = null;
                                    if ((list = file.listFiles()) != null) {
                                        for (File f : list) {
                                            scanPic(searchWord, f, findFile);
                                        }
                                    }
                                } else {
                                    String text = searchWord;
                                    String name = file.getName().toLowerCase();
                                    if (isMatch(name, text)) {
                                        findFile.add(file);
                                    }
                                }
                            }

                            public void actionPerformed(ActionEvent evt) {
                                picDir = new File(picFolderDirText.getText());
                                if (picDir == null) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("picDir is null");
                                    return;
                                }
                                if(!picDir.exists() || !picDir.isDirectory()) {
                                    JCommonUtil._jOptionPane_showMessageDialog_error("picDir 路徑錯誤");
                                    return;
                                }

                                picCheckBtn.setText("search..");

                                String searchWord = picCheckText.getText().toLowerCase().trim();

                                Set<File> picSet2 = new HashSet<File>();
                                scanPic(searchWord, picDir, picSet2);

                                if (picSet2 != null && picSet2.size() > 0) {
                                    picCheckBtn.setText("" + picSet2.size());

                                    try {
                                        Desktop.getDesktop().open(picSet2.iterator().next());
                                    } catch (IOException e) {
                                        JCommonUtil.handleException(e);
                                    }
                                } else {
                                    picCheckBtn.setText("0");
                                }
                            }
                        });
                    }
                }
            }
            
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/english_tester.ico");
            
            pack();
            this.setSize(423, 314);
            
            configHelper.init();
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }
    
    private class ConfigPropHelper {
        private void init() {
            if(configProp.getConfigProp().containsKey(ConfigKey.PIC_DIR_KEY)) {
                String v1 = configProp.getConfigProp().getProperty(ConfigKey.PIC_DIR_KEY);
                picFolderDirText.setText(v1);
            }
        }
        
        private void save() {
            File f1 = new File(picFolderDirText.getText());
            if(f1.exists() && f1.isDirectory()) {
                String fPath = f1.getAbsolutePath();
                configProp.getConfigProp().put(ConfigKey.PIC_DIR_KEY, fPath);
            }else {
                JCommonUtil._jOptionPane_showMessageDialog_error("路徑錯誤 :" + f1);
                return;
            }
            configProp.store();
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
        }
    }
    

    void browseOnlineDictionary() {
        try {
            String text = currentWordIndex.trim().toLowerCase();
            ClipboardUtil.getInstance().setContents(text);
            text = text.replace(" ", "%20");
            URI uri = new URI("http://www.thefreedictionary.com/" + text);
            Desktop.getDesktop().browse(uri);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    void answerBtnClick(JButton answerBtn_) {
        // 設定圖片背景
        setPictureDialogShow(false);
        for (int ii = 0; ii < 4; ii++) {
            answerBtn[ii].setBackground(JCommonUtil.DEFULAT_BTN_COLOR);
        }
        if (answerBtn_ == null) {
            return;
        }
        if (answerBtn[correctBtnNum] != answerBtn_) {
            answerBtn_.setBackground(Color.RED);
            // ↓↓↓↓↓↓↓↓↓↓↓答錯的邏輯
            String key = currentWordIndex;
            String value = englishProp.getProperty(currentWordIndex);
            if (StringUtils.isNotEmpty(value)) {
                pickProp.setProperty(key, value);
            }
            currentEnglishText = "";
            browseOnlineDictionary();
            // ↑↑↑↑↑↑↑↑↑↑↑答錯的邏輯
        } else {
            // ↓↓↓↓↓↓↓↓↓↓↓答對的邏輯
            answerBtn[correctBtnNum].setBackground(Color.GREEN);
            if (StringUtils.equals(currentEnglishText, currentWordIndex)) {
                currentEnglishText = "";
                removeBtnAction();

                // 設定圖片背景
                setPictureDialogShow(false);
            } else {
                currentEnglishText = currentWordIndex;

                // 設定圖片背景
                setPictureDialogShow(true);
            }
            // ↑↑↑↑↑↑↑↑↑↑↑答對的邏輯
        }
    }

    void resetAnswers(String word) {
        String answer = englishProp.getProperty(word);

        List<String> questionList = new ArrayList<String>();
        for (Enumeration<Object> enu = englishProp.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            questionList.add(key);
        }

        correctBtnNum = RandomUtil.rangeInteger(0, 3);
        // System.out.println("正確答案=====>" + correctBtnNum);

        List<String> hasList = new ArrayList<String>();
        hasList.add(word);

        for (int ii = 0; ii < 4; ii++) {
            int index = -1;
            for (;;) {
                index = RandomUtil.rangeInteger(0, questionList.size() - 1);
                String key = questionList.get(index);
                if (!hasList.contains(key)) {
                    String value = englishProp.getProperty(key);
                    answerBtn[ii].setText(formatChangeLine(key, value));
                    hasList.add(key);
                    break;
                }
            }
        }

        answerBtn[correctBtnNum].setText(formatChangeLine(word, answer));
    }

    String formatChangeLine(String question, String answer) {
        answer = answer.replaceAll("\\[.*\\]", "");
        answer = answer.replaceAll("-", "");

        Pattern ptn = Pattern.compile(question, Pattern.CASE_INSENSITIVE);
        Matcher matcher = ptn.matcher(answer);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        return "<html>" + sb.toString() + "</html>";
    }

    /**
     * 載入所有檔案清單
     */
    void reloadFileList() {
        DefaultListModel fileListModel = new DefaultListModel();
        File targetDir = PropertiesUtil.getJarCurrentPath(EnglishTester.class); // FIXME
//        targetDir = new File("D:/my_tool/english"); // FIXME
        File[] list = targetDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(".*\\.properties");
            }
        });
        Arrays.sort(list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.lastModified() < o2.lastModified()) {
                    return -1;
                }
                if (o1.lastModified() > o2.lastModified()) {
                    return 1;
                }
                return 0;
            }
        });
        for (File f : list) {
            fileListModel.addElement(f);
        }
        fileList.setModel(fileListModel);
    }

    void skipBtnAction() {
        if (!wordsList.contains(currentWordIndex)) {
            JCommonUtil._jOptionPane_showMessageDialog_error("測驗題目有問題!,請重新測驗");
            return;
        }
        answerBtnClick(null);
        if (!wordsList.isEmpty()) {
            setVisible(false);
            wordsList = RandomUtil.randomList(wordsList);
        }
    }

    void removeBtnAction() {
        if (!wordsList.contains(currentWordIndex)) {
            JCommonUtil._jOptionPane_showMessageDialog_error("測驗題目有問題!,請重新測驗");
            return;
        }
        answerBtnClick(null);
        if (!wordsList.isEmpty()) {
            wordsList.remove(0);
            setVisible(false);
            questionCountLabel.setText(String.valueOf(wordsList.size()));
        }
    }

    void loadEnglishFile(File file) {
        englishFile = file;
        if (englishFile == null) {
            englishFile = new File(PropertiesUtil.getJarCurrentPath(EnglishTester.class), "english.properties");
        }
        try {
            englishProp = new Properties();
            englishProp.load(new FileInputStream(englishFile));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (englishProp.isEmpty()) {
            englishProp.put("test1", "abc");
        }
        setTitle(englishFile.getName());
        DefaultTableModel propTableModel = JTableUtil.createModel(false, "english", "chinese");
        for (Enumeration<?> enu = englishProp.propertyNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = englishProp.getProperty(key);
            propTableModel.addRow(new Object[] { key, value });
        }
        propTable.setModel(propTableModel);
    }

    void scanPic() {
        scanPicBtn.setText("X");
        scanPicBtn.setToolTipText(englishProp.getProperty(currentWordIndex));

        picSet = new HashSet<File>();
        if (picDir != null) {

            if (allPicFileList == null) {
                allPicFileList = new HashSet<FileZ>();
                loadAllPic(picDir, allPicFileList);
            }

            String engText = currentWordIndex.toLowerCase().trim();
            for (FileZ fz : allPicFileList) {
                String fileName = fz.name;
                if (this.isMatch(fileName, engText)) {
                    picSet.add(fz.file);
                }
            }
        }

        scanPicBtn.setText("" + picSet.size());
    }

    void loadAllPic(File file, Set<FileZ> findFile) {
        if (file.isDirectory()) {
            File[] list = null;
            if ((list = file.listFiles()) != null) {
                for (File f : list) {
                    loadAllPic(f, findFile);
                }
            }
        } else {
            String name = file.getName().toLowerCase();
            int pos = name.lastIndexOf(".");
            if (pos != -1) {
                name = name.substring(0, pos);
                FileZ fz = new FileZ();
                fz.file = file;
                fz.name = name;
                findFile.add(fz);
            }
        }
    }

    boolean isMatch(String fileName, String english) {
        Pattern ptn = Pattern.compile("(.?)" + english + "(.?)");
        Matcher matcher = ptn.matcher(fileName);
        while (matcher.find()) {
            String a1 = matcher.group(1);
            String a2 = matcher.group(2);
            String format = "[^a-zA-Z]";
            boolean a1Match = (StringUtils.isEmpty(a1) || a1.matches(format));
            boolean a2Match = (StringUtils.isEmpty(a2) || a2.matches(format));
            if (a1Match && a2Match) {
                return true;
            }
        }
        return false;
    }

    List<String> wordsList;
    File englishFile;
    Properties englishProp;
    Properties pickProp;
    File picDir = new File("D:\\english_pic");
    int correctBtnNum = -1;
    String currentEnglishText;
    Set<File> picSet;
    Set<FileZ> allPicFileList;

    String currentWordIndex;
    private JPanel jPanel11;
    private JTextArea inputTestArea2;
    private JTextArea inputTestArea1;
    InputTestTrainer inputTestTrainer = new InputTestTrainer();
    private JPanel panel;
    private JLabel inputTestLabel;
    private JCheckBox inputTestChk;
    
    SysTrayUtil trayUtil = SysTrayUtil.newInstance();
    private JTextField picFolderDirText;

    void filterHasPicProp() {
        picDir = new File(picFolderDirText.getText());
        if (picDir == null) {
            JCommonUtil._jOptionPane_showMessageDialog_error("picDir null 2");
            return;
        }
        if(!picDir.exists() || !picDir.isDirectory()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("picDir 路徑錯誤2");
            return;
        }

        if (allPicFileList == null) {
            allPicFileList = new HashSet<FileZ>();
            loadAllPic(picDir, allPicFileList);
        }

        Properties pppp = new Properties();
        for (Object key_ : englishProp.keySet()) {
            String engText = (String) key_;
            String value = englishProp.getProperty(engText);
            engText = engText.toLowerCase().trim();

            boolean findOk = false;
            for (FileZ fz : allPicFileList) {
                String fileName = fz.name;
                if (this.isMatch(fileName, engText)) {
                    findOk = true;
                    break;
                }
            }

            if (findOk == false) {
                pppp.put(engText, value);
            }
        }

        englishProp = pppp;
    }

    /**
     * 輸入單字訓練
     */
    private class InputTestTrainer {
        /**
         * 初始化剩餘字數
         */
        private void initSizeLabel() {
            if (wordsList != null) {
                inputTestLabel.setText(String.valueOf(wordsList.size()));
            }
        }

        /**
         * 初始化輸入練習題目
         */
        private void initQuestion() {
            if (wordsList != null && !wordsList.isEmpty()) {
                boolean isCheck = inputTestChk.isSelected();
                String eng = wordsList.get(0);
                if (isCheck) {
                    String desc = englishProp.getProperty(eng);
                    eng = eng + "\n" + desc;
                }
                inputTestArea1.setText(eng);
            }
            initSizeLabel();
        }

        Thread onlyThread;

        /**
         * 輸入比對
         */
        private void keyin(KeyEvent e) {
            if (wordsList != null && !wordsList.isEmpty()) {
                compareInputIfSuccess();
                addToRecitePickProperties(e);
            }
            initSizeLabel();
        }
        
        /**
         * 將單字加入背誦清單
         */
        private void addToRecitePickProperties(KeyEvent e){
            String v = wordsList.get(0);
            String desc = englishProp.getProperty(v);
            if(KeyEventUtil.isMaskKeyPress(e, "c") && e.getKeyCode() == KeyEvent.VK_C){
                if(pickProp == null){
                    pickProp = new Properties();
                }
                trayUtil.createDefaultTray();
                if(pickProp.containsKey(v)){
                    pickProp.remove(v);
                    trayUtil.getTrayIcon().displayMessage("移除成功!", v + " " + desc, MessageType.INFO);
                }else{
                    pickProp.setProperty(v, desc);
                    trayUtil.getTrayIcon().displayMessage("加入成功!", v + " " + desc, MessageType.INFO);
                }
            }
        }
        
        /**
         * 判斷書入是否正確 若正確救下一提
         */
        private void compareInputIfSuccess(){
            String v = wordsList.get(0);
            String v2 = inputTestArea2.getText();
            if (StringUtils.equalsIgnoreCase(v, v2)) {
                String desc = englishProp.getProperty(v);
                inputTestArea1.setText(v + "\n" + desc);

                if (onlyThread == null || onlyThread.getState() == Thread.State.TERMINATED) {
                    onlyThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(!inputTestChk.isSelected()){
                                    Thread.sleep(1000L);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // 下一提
                            nextQuestion();
                            inputTestArea2.setText("");
                        }
                    });
                }

                if (onlyThread.getState() == Thread.State.NEW) {
                    onlyThread.start();
                }
            }
        }

        /**
         * 下一提
         */
        private void nextQuestion() {
            if (wordsList != null && !wordsList.isEmpty()) {
                wordsList.remove(0);
                if (wordsList.isEmpty()) {
                    JCommonUtil._jOptionPane_showMessageDialog_error("無題目了!");
                } else {
                    initQuestion();
                }
            } else {
                JCommonUtil._jOptionPane_showMessageDialog_error("無題目了!");
            }
            initSizeLabel();
        }
    }

    void startNow() {
        final StringBuilder currentTime = new StringBuilder();
        currentTime.append(System.currentTimeMillis());

        wordsList = new ArrayList<String>();
        pickProp = new Properties();

        if (picOnly.isSelected()) {
            filterHasPicProp();
        }

        for (Enumeration<?> enu = englishProp.propertyNames(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = englishProp.getProperty(key);
            wordsList.add(key);
        }
        wordsList = RandomUtil.randomList(wordsList);

        if (sortChkBox.isSelected()) {
            Collections.sort(wordsList);
            ifIsNumberSort(wordsList);
        }

        propCountLabel.setText(String.valueOf(englishProp.size()));
        questionCountLabel.setText(String.valueOf(wordsList.size()));

        // 初始化輸入練習題目
        inputTestTrainer.initQuestion();

        setVisible(false);

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // jTabbedPane1.setSelectedIndex(tabSelectedCombo.getSelectedIndex());//
                // 設定顯示英文還中文

                if(!EnglishTester.this.isVisible()){
                    setVisible(true);
                }

                // 尚未換題無須後續處理
                if (currentWordIndex == wordsList.get(0)) {
                    return;
                }

                // System.out.println(wordsList);
                if (!wordsList.isEmpty()) {
                    String word = wordsList.get(0);

                    // ↓↓↓↓↓設定題目
                    setCurrentWord(word);
                    // ↑↑↑↑↑設定題目

                    scanPic();
                    if (showChineseOption.isSelected()) {
                        setPictureDialogShow(true);
                    }

                    // ↓↓↓↓↓設定答案
                    resetAnswers(word);
                    // ↑↑↑↑↑設定答案
                } else {
                    JCommonUtil._jOptionPane_showMessageDialog_info("words is over");
                    timer.cancel();
                }
            }
        }, 0, DELAY_TIME);
    }

    void setCurrentWord(String word) {
        currentWordIndex = word;
        englishArea.setText(word);
        showEnglishText.setText(word);

        if (showChineseOption.isSelected()) {
            String answer = formatChangeLine(word, englishProp.getProperty(word));
            answer = answer.replaceAll("<html>", "").replaceAll("</html>", "");
            englishArea.setText(word + "\n\n" + answer);
        }
    }

    void setPictureDialogShow(boolean show) {
        if (!showPicChkBox.isSelected()) {
            show = false;
        }
        if (showPicDialog == null) {
            showPicDialog = new EnglishTester_showPicDialog();
            showPicDialog.setLocation((int) this.getLocation().getX(), (int) this.getLocation().getY());
        }
        if (show == false) {
            showPicDialog.setVisible(false);
            return;
        } else {
            // 設定圖片背景
            if (!picSet.isEmpty()) {
                File file = picSet.iterator().next();

                System.out.println("repaint = " + file);
                Image image = new ImageIcon(file.getAbsolutePath()).getImage();

                showPicDialog.getjPanel1().setImage(image);
                showPicDialog.repaint();

                if (!showPicDialog.isVisible()) {
                    showPicDialog.setVisible(true);
                }
            } else {
                showPicDialog.setVisible(false);
            }
        }
    }

    void ifIsNumberSort(List<String> list) {
        boolean allNumberOk = true;
        int length = 0;
        for (String val : list) {
            if (!StringUtils.isNumeric(val)) {
                allNumberOk = false;
                break;
            }
            length = Math.max(length, val.length());
        }
        if (allNumberOk == false) {
            return;
        }
        final int finalLen = length;
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                o1 = StringUtils.leftPad(o1, finalLen);
                o2 = StringUtils.leftPad(o2, finalLen);
                return o1.compareTo(o2);
            }
        });
    }

    static class FileZ {
        File file;
        String name;
    }
    
    private void saveConfigBtnAction() {
        try {
            configHelper.save();
        }catch(Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
