package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.collection.MapUtil;
import gtu.db.JdbcDBUtil;
import gtu.properties.PropertiesMultiUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTextAreaUtil;

public class FastDBQueryUI_RefCodeTableDlg extends JDialog {

    private PropertiesUtilBean config = new PropertiesUtilBean(FastDBQueryUI_RefCodeTableDlg.class);

    public static void main(String[] args) {

    }

    private Map<CodeTableBean, Map<String, String>> holderMap = new HashMap<CodeTableBean, Map<String, String>>();
    private Map<String, Map<String, String>> commonMap = new HashMap<String, Map<String, String>>();
    private boolean enable = false;

    private JList codetableDefList;
    private JTextField columnNameText;
    private JTextField sqlValueColumnText;
    private JTextField sqlLabelColumnText;
    private JTextArea sqlTextArea;
    private JCheckBox enableCheck;
    private JCheckBox useQuestionConditionChk;

    public FastDBQueryUI_RefCodeTableDlg() {
        setTitle("定義CodeTable");
        setBounds(100, 100, 790, 477);
        getContentPane().setLayout(new BorderLayout());
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton saveBtn = new JButton("儲存");
                saveBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        try {
                            CodeTableBean bean = new CodeTableBean();
                            bean.columnName = StringUtils.trimToEmpty(columnNameText.getText());
                            bean.sqlValueColumn = JCommonUtil.isBlankErrorMsg(sqlValueColumnText, "sql value不可為空");
                            bean.sqlLabelColumn = JCommonUtil.isBlankErrorMsg(sqlLabelColumnText, "sql label不可為空");
                            bean.sql = JCommonUtil.isBlankErrorMsg(sqlTextArea, "sql不可為空");
                            bean.enable = enableCheck.isSelected() ? "Y" : "N";
                            bean.useQuestion = useQuestionConditionChk.isSelected() ? "Y" : "N";
                            config.getConfigProp().setProperty(bean.getKey(), bean.getValue());
                            config.store();
                            initCodeTableList();
                            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!");
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
                    }
                });
                {
                    JButton clearBtn = new JButton("清除輸入內容");
                    clearBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            columnNameText.setText("");
                            sqlValueColumnText.setText("");
                            sqlLabelColumnText.setText("");
                            sqlTextArea.setText("");
                            enableCheck.setSelected(true);
                            useQuestionConditionChk.setSelected(true);
                        }
                    });
                    {
                        JButton clearMemoryBtn = new JButton("清除記憶體暫存");
                        clearMemoryBtn.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                holderMap.clear();
                                commonMap.clear();
                                JCommonUtil._jOptionPane_showMessageDialog_info("已清除記憶體!");
                            }
                        });
                        buttonPane.add(clearMemoryBtn);
                    }
                    buttonPane.add(clearBtn);
                }
                buttonPane.add(saveBtn);
            }
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                {
                    JPanel panel = new JPanel();
                    getContentPane().add(panel, BorderLayout.NORTH);
                }
                {
                    JPanel panel = new JPanel();
                    getContentPane().add(panel, BorderLayout.EAST);
                }
                {
                    codetableDefList = new JList();
                    codetableDefList.setPreferredSize(new Dimension(200, 0));
                    codetableDefList.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            JListUtil.newInstance(codetableDefList).defaultJListKeyPressed(e, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        CodeTableBean bean = (CodeTableBean) e.getSource();
                                        boolean delConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定刪除? : " + bean.columnName, "刪除設定");
                                        if (delConfirm) {
                                            config.getConfigProp().remove(bean.getKey());
                                            config.store();
                                            initCodeTableList();
                                            e.setSource(true);
                                        }
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                    }
                                }
                            });
                        }
                    });

                    codetableDefList.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            CodeTableBean bean = JListUtil.getLeadSelectionObject(codetableDefList);
                            if (bean != null) {
                                columnNameText.setText(bean.columnName);
                                sqlValueColumnText.setText(bean.sqlValueColumn);
                                sqlLabelColumnText.setText(bean.sqlLabelColumn);
                                sqlTextArea.setText(bean.sql);
                                enableCheck.setSelected("Y".equalsIgnoreCase(StringUtils.trimToEmpty(bean.enable)));
                                useQuestionConditionChk.setSelected("Y".equalsIgnoreCase(StringUtils.trimToEmpty(bean.useQuestion)));
                            }
                        }
                    });

                    getContentPane().add(JCommonUtil.createScrollComponent(codetableDefList), BorderLayout.WEST);
                }
                {
                    JPanel panel = new JPanel();
                    getContentPane().add(panel, BorderLayout.CENTER);
                    panel.setLayout(
                            new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                                    new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                                            FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                                            FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));
                    {
                        JLabel lblNewLabel = new JLabel("對應欄位Category");
                        panel.add(lblNewLabel, "2, 2, right, default");
                    }
                    {
                        columnNameText = new JTextField();
                        columnNameText.setToolTipText("不給值表示為預設值");
                        panel.add(columnNameText, "4, 2, fill, default");
                        columnNameText.setColumns(10);
                    }
                    {
                        JLabel lblNewLabel_1 = new JLabel("SQL Value欄位");
                        panel.add(lblNewLabel_1, "2, 4, right, default");
                    }
                    {
                        sqlValueColumnText = new JTextField();
                        panel.add(sqlValueColumnText, "4, 4, fill, default");
                        sqlValueColumnText.setColumns(10);
                    }
                    {
                        JLabel lblNewLabel_2 = new JLabel("SQL Label欄位");
                        panel.add(lblNewLabel_2, "2, 6, right, default");
                    }
                    {
                        sqlLabelColumnText = new JTextField();
                        panel.add(sqlLabelColumnText, "4, 6, fill, default");
                        sqlLabelColumnText.setColumns(10);
                    }
                    {
                        JLabel lblNewLabel_5 = new JLabel("？條件帶入");
                        panel.add(lblNewLabel_5, "2, 8");
                    }
                    {
                        useQuestionConditionChk = new JCheckBox("");
                        panel.add(useQuestionConditionChk, "4, 8");
                    }
                    {
                        JLabel lblNewLabel_4 = new JLabel("啟用");
                        panel.add(lblNewLabel_4, "2, 10");
                    }
                    {
                        enableCheck = new JCheckBox("");
                        panel.add(enableCheck, "4, 10");
                    }
                    {
                        JLabel lblNewLabel_3 = new JLabel("SQL");
                        panel.add(lblNewLabel_3, "2, 12");
                    }
                    {
                        sqlTextArea = new JTextArea();
                        JTextAreaUtil.applyCommonSetting(sqlTextArea);
                        panel.add(JCommonUtil.createScrollComponent(sqlTextArea), "4, 12, fill, fill");
                    }
                }
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
            }
        }
        {
            initCodeTableList();
            JCommonUtil.setJFrameCenter(this);
            enable = true;
        }
    }

    private List<CodeTableBean> getCodeTableLst() {
        List<CodeTableBean> lst = new ArrayList<CodeTableBean>();
        for (Enumeration enu = config.getConfigProp().keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = config.getConfigProp().getProperty(key);
            CodeTableBean bean = CodeTableBean.of(key, value);
            lst.add(bean);
        }
        Collections.sort(lst, new Comparator<CodeTableBean>() {
            @Override
            public int compare(CodeTableBean o1, CodeTableBean o2) {
                return StringUtils.defaultString(o1.columnName).compareTo(o2.columnName);
            }
        });
        return lst;
    }

    private void initCodeTableList() {
        DefaultListModel model = new DefaultListModel();
        codetableDefList.setModel(model);
        List<CodeTableBean> lst = getCodeTableLst();
        for (CodeTableBean bean : lst) {
            model.addElement(bean);
        }
    }

    private String toStringProcess(Object value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    public String getTooltipReference(String category, Object value, DataSource dataSource) {
        try {
            if (enable) {
                String value22 = StringUtils.trimToEmpty(toStringProcess(value));
                if (StringUtils.isBlank(value22)) {
                    return null;
                }

                String category1 = StringUtils.trimToEmpty(category).toUpperCase();

                Map<String, String> refMap = new HashMap<String, String>();

                List<CodeTableBean> lst = getCodeTableLst();

                boolean useCustomCodetableConfig = false;

                String tooltipPrefix = "";

                for (CodeTableBean bean : lst) {
                    if (StringUtils.equalsIgnoreCase(bean.columnName, category1)) {
                        useCustomCodetableConfig = true;

                        if (!holderMap.containsKey(bean)) {

                            refMap = this.getReferenceMap("@自訂@", bean, category, dataSource);

                            holderMap.put(bean, refMap);
                            System.out.println("新增[holderMap] : " + category1 + "\t" + refMap);
                        } else {
                            refMap = holderMap.get(bean);
                        }

                        tooltipPrefix = getTooltipPrefix(bean, category);
                    }
                }

                if (!useCustomCodetableConfig) {
                    CodeTableBean commonBean = null;
                    for (CodeTableBean bean : lst) {
                        if (StringUtils.isBlank(bean.columnName)) {
                            commonBean = bean;
                            break;
                        }
                    }

                    if (commonBean != null) {
                        if (!commonMap.containsKey(category1)) {

                            refMap = this.getReferenceMap("@預設@", commonBean, category, dataSource);

                            commonMap.put(category1, refMap);
                            System.out.println("新增[commonMap] : " + category1 + "\t" + refMap);
                        } else {
                            refMap = commonMap.get(category1);
                        }

                        tooltipPrefix = getTooltipPrefix(commonBean, category);
                    }
                }

                if (refMap.containsKey(value22)) {
                    return "<html><font color=Blue>" + tooltipPrefix + "</font><font color=Green>" + refMap.get(value22) + "</font></html>";
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getTooltipPrefix(CodeTableBean bean, String category) {
        String message = "";
        if (StringUtils.isBlank(bean.columnName)) {
            message = "預設";
        } else {
            message = StringUtils.trimToEmpty(bean.columnName);
        }
        if ("Y".equalsIgnoreCase(bean.useQuestion)) {
            message += "(" + category + ")";
        }
        if (StringUtils.isNotBlank(message)) {
            message += "：";
        }
        return message;
    }

    private Map<String, String> getReferenceMap(String errorLabel, CodeTableBean bean, String category, DataSource dataSource) {
        Map<String, String> refMap = new HashMap<String, String>();
        try {
            String valueColumn1 = StringUtils.trimToEmpty(bean.sqlValueColumn.toUpperCase()).toUpperCase();
            String labelColumn1 = StringUtils.trimToEmpty(bean.sqlLabelColumn.toUpperCase()).toUpperCase();

            Connection conn = dataSource.getConnection();

            Object[] condition = new Object[] {};
            if ("Y".equalsIgnoreCase(bean.useQuestion)) {
                condition = new Object[] { category };
            }

            List<Map<String, Object>> queryLst = JdbcDBUtil.queryForList(bean.sql, condition, conn, true);
            for (Map<String, Object> map : queryLst) {
                if (MapUtil.constainIgnorecase(valueColumn1, map) && //
                        MapUtil.constainIgnorecase(labelColumn1, map)) {
                    String key1 = toStringProcess(MapUtil.getIgnorecase(valueColumn1, map));
                    String value1 = toStringProcess(MapUtil.getIgnorecase(labelColumn1, map));
                    refMap.put(key1, value1);
                } else {
                    break;
                }
            }
        } catch (Exception ex) {
            String message = "[ SQL : " + bean.sql + " , parameter : " + category + "]";
            System.err.println("[" + errorLabel + "] getReferenceMap ERR : " + ex.getMessage() + " --> \n" + message);
        }
        return refMap;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public static class CodeTableBean {
        private static String[] KEYS_DEF = new String[] { "columnName" };
        private static String[] VALUES_DEF = new String[] { "sqlValueColumn", "sqlLabelColumn", "sql", "enable", "useQuestion" };

        String columnName;
        String sqlValueColumn;
        String sqlLabelColumn;
        String sql;
        String enable;
        String useQuestion;

        public static CodeTableBean of(String key, String value) {
            return PropertiesMultiUtil.of(key, value, CodeTableBean.class);
        }

        private String getKey() {
            return PropertiesMultiUtil.getKey(this);
        }

        private String getValue() {
            return PropertiesMultiUtil.getValue(this);
        }

        public String toString() {
            if (StringUtils.isBlank(columnName)) {
                return "<html><font color=blue>預設</font></html>";
            }
            return "<html><font color=Black>" + StringUtils.trimToEmpty(columnName) + "</font></html>";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
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
            CodeTableBean other = (CodeTableBean) obj;
            if (columnName == null) {
                if (other.columnName != null)
                    return false;
            } else if (!columnName.equals(other.columnName))
                return false;
            return true;
        }
    }
}
