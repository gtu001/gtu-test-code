package gtu._work.ui;

import gtu.db.JdbcDBUtil;
import gtu.properties.PropertiesUtil;
import gtu.runtime.ClipboardUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import javax.swing.JCheckBox;

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
public class RegexCatchReplacer_Ebao extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegexCatchReplacer_Ebao inst = new RegexCatchReplacer_Ebao();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public RegexCatchReplacer_Ebao() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            {
            }
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jTabbedPane1 = new JTabbedPane();
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
                            replaceArea.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    JPopupMenuUtil.newInstance(replaceArea).applyEvent(evt).addJMenuItem("load from file", true, new ActionListener() {

                                        Thread newThread;

                                        public void actionPerformed(ActionEvent arg0) {
                                            if (newThread != null && newThread.getState() != Thread.State.TERMINATED) {
                                                JCommonUtil._jOptionPane_showMessageDialog_error("file is loading!");
                                                return;
                                            }

                                            final File file = JCommonUtil._jFileChooser_selectFileOnly();
                                            if (file == null) {
                                                JCommonUtil._jOptionPane_showMessageDialog_error("file is not correct!");
                                                return;
                                            }
                                            String defaultCharset = Charset.defaultCharset().displayName();
                                            String chst = (String) JCommonUtil._jOptionPane_showInputDialog("input your charset!", defaultCharset);
                                            final Charset charset2 = Charset.forName(StringUtils.defaultIfEmpty(chst, defaultCharset));

                                            newThread = new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
                                                public void run() {
                                                    try {
                                                        loadFromFileSb = new StringBuilder();
                                                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset2));
                                                        for (String line = null; (line = reader.readLine()) != null;) {
                                                            loadFromFileSb.append(line + "\n");
                                                        }
                                                        reader.close();
                                                        replaceArea.setText(loadFromFileSb.toString());
                                                        JCommonUtil._jOptionPane_showMessageDialog_info("load completed!");
                                                    } catch (Exception e) {
                                                        JCommonUtil.handleException(e);
                                                    }
                                                }
                                            }, "" + System.currentTimeMillis());
                                            newThread.setDaemon(true);
                                            newThread.start();
                                        }
                                    }).show();
                                }
                            });
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
                        exeucte.setPreferredSize(new java.awt.Dimension(491, 125));
                        exeucte.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                exeucteActionPerformed(evt);
                            }
                        });
                    }
                    {
                        jPanel3 = new JPanel();
                        GroupLayout jPanel3Layout = new GroupLayout((JComponent) jPanel3);
                        jPanel3.setLayout(jPanel3Layout);
                        jPanel2.add(jPanel3, BorderLayout.CENTER);
                        {
                            repFromText = new JTextField();
                        }
                        {
                            repToText = new JTextField();
                        }
                        jPanel3Layout.setHorizontalGroup(jPanel3Layout
                                .createSequentialGroup()
                                .addContainerGap(25, 25)
                                .addGroup(
                                        jPanel3Layout.createParallelGroup()
                                                .addGroup(jPanel3Layout.createSequentialGroup().addComponent(repFromText, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel3Layout.createSequentialGroup().addComponent(repToText, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(20, Short.MAX_VALUE));
                        jPanel3Layout.setVerticalGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
                                .addComponent(repFromText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(repToText, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                    }
                    {
                        addToTemplate = new JButton();
                        jPanel2.add(addToTemplate, BorderLayout.NORTH);
                        addToTemplate.setText("add to template");
                        addToTemplate.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                prop.put(repFromText.getText(), repToText.getText());
                                reloadTemplateList();
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
                        jScrollPane2.setPreferredSize(new java.awt.Dimension(491, 283));
                        {
                            DefaultTableModel resultAreaModel = JTableUtil.createModel(true, "match", "count");
                            resultArea = new JTable();
                            jScrollPane2.setViewportView(resultArea);
                            JTableUtil.defaultSetting(resultArea);
                            resultArea.setModel(resultAreaModel);
                        }
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
                            reloadTemplateList();
                        }
                        templateList.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent evt) {
                                if (templateList.getLeadSelectionIndex() == -1) {
                                    return;
                                }
                                Entry<Object, Object> entry = (Entry<Object, Object>) JListUtil.getLeadSelectionObject(templateList);
                                repFromText.setText((String) entry.getKey());
                                repToText.setText((String) entry.getValue());
                            }
                        });
                        templateList.addKeyListener(new KeyAdapter() {
                            public void keyPressed(KeyEvent evt) {
                                JListUtil.newInstance(templateList).defaultJListKeyPressed(evt);
                            }
                        });
                    }
                }
                {
                    jPanel6 = new JPanel();
                    FlowLayout jPanel6Layout = new FlowLayout();
                    jPanel6.setLayout(jPanel6Layout);
                    jTabbedPane1.addTab("result1", null, jPanel6, null);
                    {
                        resultBtn1 = new JButton();
                        jPanel6.add(resultBtn1);
                        resultBtn1.setText("to String[]");
                        resultBtn1.setPreferredSize(new java.awt.Dimension(105, 32));
                        resultBtn1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                JTableUtil tableUtil = JTableUtil.newInstance(resultArea);
                                int[] rowPoss = tableUtil.getSelectedRows();
                                DefaultTableModel model = tableUtil.getModel();
                                List<Object> valueList = new ArrayList<Object>();
                                for (int ii = 0; ii < rowPoss.length; ii++) {
                                    valueList.add(model.getValueAt(rowPoss[ii], 0));
                                }
                                String reult = valueList.toString().replaceAll("[\\s]", "").replaceAll("[\\,]", "\",\"").replaceAll("[\\[\\]]", "\"");
                                ClipboardUtil.getInstance().setContents(reult);
                            }
                        });
                    }
                    {
                        resultBtn2 = new JButton();
                        jPanel6.add(resultBtn2);
                        resultBtn2.setText("TODO");
                        resultBtn2.setPreferredSize(new java.awt.Dimension(105, 32));
                        resultBtn2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                System.out.println("resultBtn1.actionPerformed, event=" + evt);
                                // TODO add your code for
                                // resultBtn1.actionPerformed
                                JCommonUtil._jOptionPane_showMessageDialog_info("TODO");
                            }
                        });
                    }
                }
            }
            this.setSize(512, 350);
            JCommonUtil.setFont(repToText, repFromText, replaceArea, templateList);
            {
                panel = new JPanel();
                jTabbedPane1.addTab("eBao", null, panel, null);
                panel.setLayout(new BorderLayout(0, 0));
                {
                    scrollPane = new JScrollPane();
                    panel.add(scrollPane, BorderLayout.CENTER);
                    {
                        ebaoTable = new JTable();
                        scrollPane.setViewportView(ebaoTable);
                        // TODO
                        DefaultTableModel ebaoModel = JTableUtil.createModel(true, "match", "label");
                        JTableUtil.defaultSetting(ebaoTable);
                        ebaoTable.setModel(ebaoModel);
                    }
                }
                {
                    exactEbaoSearchChk = new JCheckBox("精確查詢");
                    panel.add(exactEbaoSearchChk, BorderLayout.NORTH);
                }
            }

            JCommonUtil.frameCloseDo(this, new WindowAdapter() {
                public void windowClosing(WindowEvent paramWindowEvent) {
                    if (StringUtils.isNotBlank(repFromText.getText())) {
                        prop.put(repFromText.getText(), repToText.getText());
                    }
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

    StringBuilder loadFromFileSb;

    private JTabbedPane jTabbedPane1;
    private JPanel jPanel1;
    private JTextArea replaceArea;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JButton resultBtn2;
    private JPanel jPanel6;
    private JTable resultArea;
    private JTextField repFromText;
    private JButton addToTemplate;
    private JButton resultBtn1;
    private JScrollPane jScrollPane3;
    private JList templateList;
    private JPanel jPanel5;
    private JPanel jPanel4;
    private JTextField repToText;
    private JPanel jPanel3;
    private JButton exeucte;
    private JPanel jPanel2;

    static File propFile = new File(PropertiesUtil.getJarCurrentPath(RegexDirReplacer.class), "RegexReplacer.properties");
    static Properties prop = new Properties();
    static Properties ebaoProp = new Properties();
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTable ebaoTable;
    private JCheckBox exactEbaoSearchChk;
    static {
        try {
            if (!propFile.exists()) {
                propFile.createNewFile();
            }
            System.out.println(propFile + " == " + propFile.exists());
            prop.load(new FileInputStream(propFile));

            // ebao db config
            File jarFile = PropertiesUtil.getJarCurrentPath(RegexDirReplacer.class);
            File propFile = new File(jarFile, "dbConfig.properties");
            Properties prop2 = new Properties();
            prop2.load(new FileInputStream(propFile));
            ebaoProp = prop2;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void reloadTemplateList() {
        DefaultListModel templateListModel = new DefaultListModel();
        for (Entry<Object, Object> entry : prop.entrySet()) {
            templateListModel.addElement(entry);
        }
        templateList.setModel(templateListModel);
    }

    private void exeucteActionPerformed(ActionEvent evt) {
        String loadFromFileSbStr = loadFromFileSb == null ? "" : loadFromFileSb.toString();
        String replaceText = StringUtils.defaultIfEmpty(replaceArea.getText(), loadFromFileSbStr);
        String fromPattern = repFromText.getText();
        String toFormat = repToText.getText();
        Validate.notEmpty(replaceText, "source can't empty");
        Validate.notEmpty(fromPattern, "replace regex can't empty");
        replacer(fromPattern, toFormat, replaceText);
    }

    /**
     * @param fromPattern
     *            要替換的來源pattern
     * @param toFormat
     *            要替換的目的pattern
     * @param replaceText
     *            要替換的本文
     */
    void replacer(String fromPattern, String toFormat, String replaceText) {
        try {
            Pattern pattern = Pattern.compile(fromPattern);
            Matcher matcher = pattern.matcher(replaceText);
            Map<String, Integer> tmap = new LinkedHashMap<String, Integer>();
            String tempStr = null;
            for (; matcher.find();) {
                tempStr = toFormat.toString();
                for (int ii = 0; ii <= matcher.groupCount(); ii++) {
                    System.out.println(ii + " -- " + matcher.group(ii));
                    tempStr = tempStr.replaceAll("#" + ii + "#", Matcher.quoteReplacement(matcher.group(ii)));
                    if (!tmap.containsKey(tempStr)) {
                        tmap.put(tempStr, 0);
                    }
                    tmap.put(tempStr, tmap.get(tempStr) + 1);
                }
            }
            DefaultTableModel model = JTableUtil.createModel(true, "match", "count");
            for (String str : tmap.keySet()) {
                model.addRow(new Object[] { str, tmap.get(str) });
            }
            setTitle("total : " + model.getRowCount());
            resultArea.setModel(model);

            // 加上ebao邏輯
            JTableUtil resultAreaUtil = JTableUtil.newInstance(resultArea);
            DefaultTableModel model2 = JTableUtil.createModel(true, "msgId", "Label");
            boolean isExactSearch = exactEbaoSearchChk.isSelected();
            ebaoTable.setModel(model2);
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                String msgId = (String) resultAreaUtil.getRealValueAt(ii, 0);
                callEbaoMsgId(msgId, isExactSearch);
            }
        } catch (Exception ex) {
            JOptionPaneUtil.newInstance().iconErrorMessage().showMessageDialog(ex.getMessage(), getTitle());
            ex.printStackTrace();
        }
    }

    private DataSource getDbDataSource() {
        String url = null;
        String username = null;
        String password = null;
        if (ebaoProp.containsKey("url")) {
            url = ebaoProp.getProperty("url");
        }
        if (ebaoProp.containsKey("username")) {
            username = ebaoProp.getProperty("username");
        }
        if (ebaoProp.containsKey("password")) {
            password = ebaoProp.getProperty("password");
        }
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(url);
        bds.setUsername(username);
        bds.setPassword(password);
        bds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        return bds;
    }

    private void callEbaoMsgId(String msgId, boolean exactSearch){
        if(StringUtils.isBlank(msgId) || StringUtils.length(msgId) < 3){
            return;
        }
        String dataLike = " like '%"+msgId+"%' ";
        if(exactSearch){
            dataLike = " = '"+msgId+"' ";
        }
        String sql = "select * from t_string_resource where (str_id = '"+msgId+"' or str_data "+ dataLike +") and lang_id = 311 ";
        try {
            Connection conn = this.getDbDataSource().getConnection();
            List<Map<String,Object>> queryList = JdbcDBUtil.queryForList(sql, null, conn, false);
            DefaultTableModel model = (DefaultTableModel)ebaoTable.getModel();
            for (Map<String,Object> map : queryList) {
                model.addRow(new Object[] { map.get("STR_ID"), map.get("STR_DATA")});
            }
            try{
                conn.close();
            }catch(Exception ex){
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
