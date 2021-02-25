package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

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
                    JButton clearBtn = new JButton("清除");
                    clearBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            columnNameText.setText("");
                            sqlValueColumnText.setText("");
                            sqlLabelColumnText.setText("");
                            sqlTextArea.setText("");
                        }
                    });
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
                    getContentPane().add(JCommonUtil.createScrollComponent(codetableDefList), BorderLayout.WEST);
                }
                {
                    JPanel panel = new JPanel();
                    getContentPane().add(panel, BorderLayout.CENTER);
                    panel.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                            new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
                                    FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));
                    {
                        JLabel lblNewLabel = new JLabel("對應欄位Category");
                        panel.add(lblNewLabel, "2, 2, right, default");
                    }
                    {
                        columnNameText = new JTextField();
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
                        JLabel lblNewLabel_3 = new JLabel("SQL");
                        panel.add(lblNewLabel_3, "2, 8");
                    }
                    {
                        sqlTextArea = new JTextArea();
                        JTextAreaUtil.applyCommonSetting(sqlTextArea);
                        panel.add(JCommonUtil.createScrollComponent(sqlTextArea), "4, 8, fill, fill");
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

                for (CodeTableBean bean : lst) {
                    if (StringUtils.equalsIgnoreCase(bean.columnName, category1)) {
                        useCustomCodetableConfig = true;

                        if (!holderMap.containsKey(bean)) {
                            String valueColumn1 = StringUtils.trimToEmpty(bean.sqlValueColumn.toUpperCase()).toUpperCase();
                            String labelColumn1 = StringUtils.trimToEmpty(bean.sqlLabelColumn.toUpperCase()).toUpperCase();

                            Connection conn = dataSource.getConnection();

                            List<Map<String, Object>> queryLst = JdbcDBUtil.queryForList(bean.sql, new Object[] { category }, conn, true);
                            for (Map<String, Object> map : queryLst) {
                                if (map.containsKey(valueColumn1) && map.containsKey(labelColumn1)) {
                                    String key1 = toStringProcess(map.get(valueColumn1));
                                    String value1 = toStringProcess(map.get(labelColumn1));
                                    refMap.put(key1, value1);
                                } else {
                                    break;
                                }
                            }
                            holderMap.put(bean, refMap);
                        } else {
                            refMap = holderMap.get(bean);
                        }
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
                            String valueColumn1 = StringUtils.trimToEmpty(commonBean.sqlValueColumn.toUpperCase()).toUpperCase();
                            String labelColumn1 = StringUtils.trimToEmpty(commonBean.sqlLabelColumn.toUpperCase()).toUpperCase();

                            Connection conn = dataSource.getConnection();

                            List<Map<String, Object>> queryLst = JdbcDBUtil.queryForList(commonBean.sql, new Object[] { category }, conn, true);
                            for (Map<String, Object> map : queryLst) {
                                if (map.containsKey(valueColumn1) && map.containsKey(labelColumn1)) {
                                    String key1 = toStringProcess(map.get(valueColumn1));
                                    String value1 = toStringProcess(map.get(labelColumn1));
                                    refMap.put(key1, value1);
                                } else {
                                    break;
                                }
                            }
                            commonMap.put(category1, refMap);
                        } else {
                            refMap = commonMap.get(category1);
                        }
                    }
                }

                if (refMap.containsKey(value22)) {
                    return refMap.get(value22);
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public static class CodeTableBean {
        private static String[] KEYS_DEF = new String[] { "columnName" };
        private static String[] VALUES_DEF = new String[] { "sqlValueColumn", "sqlLabelColumn", "sql", "enable" };

        String columnName;
        String sqlValueColumn;
        String sqlLabelColumn;
        String sql;
        String enable;

        public static CodeTableBean of(String key, String value) {
            return PropertiesMultiUtil.of(key, value, CodeTableBean.class);
        }

        private String getKey() {
            return PropertiesMultiUtil.getKey(this);
        }

        private String getValue() {
            return PropertiesMultiUtil.getValue(this);
        }
    }
}
