package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import gtu.db.jdbc.util.DBDateUtil;
import gtu.string.StringUtilForDb;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SimpleCheckListDlg;

public class FastDBQueryUI_ReserveSqlDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextArea sqlTextArea;
    private JComboBox dbTypeComboBox;
    private JButton columnBtn;

    private String tableName;
    private List<List<Triple<String, Class, Object>>> sqlLst;
    private JCheckBox javaToDBChk;
    private SimpleCheckListDlg mSimpleCheckListDlg;
    private Map<String, String> choiceMap = new HashMap<String, String>();

    /**
     * Launch the application.
     */
    public static FastDBQueryUI_ReserveSqlDlg newInstance(String tableName, List<List<Triple<String, Class, Object>>> sqlLst) {
        try {
            FastDBQueryUI_ReserveSqlDlg dialog = new FastDBQueryUI_ReserveSqlDlg(tableName, sqlLst);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.processSql();
            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getColumn(String column) {
        if (javaToDBChk.isSelected()) {
            return StringUtilForDb.javaToDbField(column);
        }
        return column;
    }

    private void processSql() {
        if (StringUtils.isBlank(tableName)) {
            tableName = "XXXXXXXXXXX";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select *  \r\n").append(" from ").append(tableName).append("\r\n").append(" where 1=1 \r\n");
        for (int ii = 0; ii < sqlLst.size(); ii++) {
            List<Triple<String, Class, Object>> innerSqlLst = sqlLst.get(ii);
            String appendOrStr = "and";
            if (ii != 0) {
                appendOrStr = "or";
            }
            sb.append("   " + appendOrStr + " ( \r\n");
            for (int jj = 0; jj < innerSqlLst.size(); jj++) {
                Triple<String, Class, Object> arry = innerSqlLst.get(jj);
                String appendAndStr = "";
                if (jj != 0) {
                    appendAndStr = "and";
                }
                if (choiceMap == null || choiceMap.isEmpty() || choiceMap.containsKey(arry.getLeft())) {
                    sb.append("     " + appendAndStr + " ").append(getColumn(arry.getLeft())).append(" = ");
                    sb.append(getStringValue(arry)).append("\r\n");
                }
            }
            sb.append("     ) \r\n");
        }
        sqlTextArea.setText(sb.toString());
    }

    private void columnBtnAction() {
        String title = "選擇過濾欄位";
        Map<String, String> titleMap = new HashMap<String, String>();
        for (Triple<String, Class, Object> tri : sqlLst.get(0)) {
            String strVal = "";
            if (tri.getRight() != null) {
                strVal = String.valueOf(tri.getRight());
            }
            titleMap.put(tri.getLeft(), strVal);
        }
        if (mSimpleCheckListDlg != null) {
            mSimpleCheckListDlg.dispose();
        }
        mSimpleCheckListDlg = SimpleCheckListDlg.newInstance(title, titleMap, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                choiceMap = mSimpleCheckListDlg.getCheckedMap();
                processSql();
            }
        }, new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        mSimpleCheckListDlg.show();
    }

    private String getStringValue(Triple<String, Class, Object> arry) {
        if (arry.getRight() == null) {
            return "null";
        }
        DBDateUtil.DBDateFormat format = (DBDateUtil.DBDateFormat) dbTypeComboBox.getSelectedItem();
        if (arry.getMiddle() != null) {
            if (java.sql.Date.class == arry.getMiddle()) {
                return format.varchar2Date("'" + String.valueOf(arry.getRight()) + "'");
            } else if (java.sql.Timestamp.class == arry.getMiddle()) {
                return format.varchar2Timestamp("'" + String.valueOf(arry.getRight()) + "'");
            } else if (BigDecimal.class == arry.getMiddle()) {
                return String.valueOf(arry.getRight());
            }
        }
        return "'" + arry.getRight() + "'";
    }

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_ReserveSqlDlg(String tableName, List<List<Triple<String, Class, Object>>> sqlLst) {
        {
            this.tableName = tableName;
            this.sqlLst = sqlLst;
        }
        setBounds(100, 100, 548, 376);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                JLabel label = new JLabel("資料庫類型");
                panel.add(label);
            }
            {
                dbTypeComboBox = new JComboBox();
                dbTypeComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        processSql();
                    }
                });

                panel.add(dbTypeComboBox);
                DefaultComboBoxModel model = new DefaultComboBoxModel();
                for (DBDateUtil.DBDateFormat e : DBDateUtil.DBDateFormat.values()) {
                    model.addElement(e);
                }
                dbTypeComboBox.setModel(model);
            }
            {
                javaToDBChk = new JCheckBox("java->DB");
                javaToDBChk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        processSql();
                    }
                });
                panel.add(javaToDBChk);
            }
            {
                columnBtn = new JButton("挑選欄位");
                columnBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        columnBtnAction();
                    }
                });
                panel.add(columnBtn);
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.WEST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.EAST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.SOUTH);
        }
        {
            sqlTextArea = new JTextArea();
            contentPanel.add(JCommonUtil.createScrollComponent(sqlTextArea), BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
            }
        }
        {
            JTextAreaUtil.applyCommonSetting(sqlTextArea, false);
            JCommonUtil.setJFrameCenter(this);
        }
    }
}
