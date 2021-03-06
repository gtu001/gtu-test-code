package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import gtu.swing.util.JCommonUtil.HandleDocumentEvent;

public class SimpleCheckListDlg extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JTextField searchText;
    private JCheckBox distinctCheckbox;
    private MySearchHandler mMySearchHandler = new MySearchHandler();

    private ActionListener okButtonAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("aaa", "bbbbbbbb");
        map.put("ccc", "dddddddd");
        List<String> lst3 = Arrays.asList("aa", "bb", "cc", "dd");
        final SimpleCheckListDlg dlg = SimpleCheckListDlg.newInstance("XXXXXX", map, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(((SimpleCheckListDlg) e.getSource()).getCheckedList());
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(((SimpleCheckListDlg) e.getSource()).getCheckedList());
            }
        });
    }

    public static SimpleCheckListDlg newInstance(String title, List<String> titleLst, boolean isFocusLoseClose, ActionListener okButtonAction, final ActionListener onCloseListener) {
        try {
            final SimpleCheckListDlg dialog = new SimpleCheckListDlg(titleLst, null);
            dialog.setTitle(title);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.okButtonAction = okButtonAction;

            if (isFocusLoseClose) {
                JDialogHelper.applyAutoClose(dialog);
            }

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    if (onCloseListener != null) {
                        onCloseListener.actionPerformed(new ActionEvent(dialog, -1, "close"));
                    }
                }

                public void windowClosing(WindowEvent e) {
                }
            });
            JCommonUtil.setFrameAtop(dialog, true);
            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SimpleCheckListDlg newInstance(String title, Map<String, String> titleMap, boolean isFocusLoseClose, ActionListener okButtonAction, final ActionListener onCloseListener) {
        try {
            final SimpleCheckListDlg dialog = new SimpleCheckListDlg(null, titleMap);
            dialog.setTitle(title);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.okButtonAction = okButtonAction;

            if (isFocusLoseClose) {
                JDialogHelper.applyAutoClose(dialog);
            }

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    if (onCloseListener != null) {
                        onCloseListener.actionPerformed(new ActionEvent(dialog, -1, "close"));
                    }
                }

                public void windowClosing(WindowEvent e) {
                }
            });
            JCommonUtil.setFrameAtop(dialog, true);
            return dialog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCheckedList() {
        List<String> pkLst = new ArrayList<String>();
        JTableUtil t = JTableUtil.newInstance(table);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int ii = 0; ii < model.getRowCount(); ii++) {
            Boolean v = (Boolean) t.getValueAt(false, ii, 0);
            String n = (String) t.getValueAt(false, ii, 1);
            if (v) {
                pkLst.add(n);
            }
        }
        return pkLst;
    }

    public Map<String, String> getCheckedMap() {
        Map<String, String> rtnMap = new LinkedHashMap<String, String>();
        JTableUtil t = JTableUtil.newInstance(table);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int ii = 0; ii < model.getRowCount(); ii++) {
            Boolean v = (Boolean) t.getValueAt(false, ii, 0);
            String n = (String) t.getValueAt(false, ii, 1);
            String v2 = (String) t.getValueAt(false, ii, 2);
            if (v) {
                rtnMap.put(n, v2);
            }
        }
        return rtnMap;
    }

    private class MySearchHandler {

        List<String> titleLst;
        Map<String, String> titleMap;

        protected Pair<String, List<Pattern>> filterPattern(String filterText) {
            Pattern ptn = Pattern.compile("\\/(.*?)\\/");
            Matcher mth = ptn.matcher(filterText);
            StringBuffer sb = new StringBuffer();
            List<Pattern> lst = new ArrayList<Pattern>();
            while (mth.find()) {
                String temp = mth.group(1);
                Pattern tmpPtn = null;
                if (StringUtils.isNotBlank(temp)) {
                    try {
                        tmpPtn = Pattern.compile(temp, Pattern.CASE_INSENSITIVE);
                    } catch (Exception ex) {
                    }
                }
                if (tmpPtn != null) {
                    lst.add(tmpPtn);
                    mth.appendReplacement(sb, "");
                } else {
                    mth.appendReplacement(sb, mth.group(0));
                }
            }
            mth.appendTail(sb);
            return Pair.of(sb.toString(), lst);
        }

        private boolean handleRow(int rowIdx) {
            JTableUtil util = JTableUtil.newInstance(table);
            for (int jj = 0; jj < table.getColumnCount(); jj++) {
                Object val = util.getValueAt(false, rowIdx, jj);
                if (val instanceof String) {
                    String strVal = (String) val;
                    if (textLst != null) {
                        for (String txt : textLst) {
                            if (strVal.toLowerCase().contains(txt)) {
                                return true;
                            }
                        }
                    }
                    if (mthPtn != null) {
                        for (Pattern pp : mthPtn.getRight()) {
                            if (pp != null && pp.matcher(strVal).find()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        List<String> textLst;
        Pair<String, List<Pattern>> mthPtn;

        private void process() {
            try {
                Map<Integer, List<Integer>> changeColorMap = new HashMap<Integer, List<Integer>>();
                if (StringUtils.isBlank(searchText.getText())) {
                    JTableUtil.newInstance(table).setCellBackgroundColor(Color.green.brighter(), changeColorMap, Arrays.asList(0));
                    return;
                }

                mthPtn = filterPattern(searchText.getText());

                String text1 = StringUtils.trimToEmpty(mthPtn.getLeft());
                String text = text1.toLowerCase();
                textLst = new ArrayList<String>();
                for (String t : text1.split("\\^", -1)) {
                    t = StringUtils.trimToEmpty(t).toLowerCase();
                    if (StringUtils.isNotBlank(t)) {
                        textLst.add(t);
                    }
                }

                JTableUtil util = JTableUtil.newInstance(table);
                DefaultTableModel model = util.getModel();

                for (int ii = 0; ii < model.getRowCount(); ii++) {
                    List<Integer> lst = new ArrayList<Integer>();
                    changeColorMap.put(ii, lst);
                    A: for (int jj = 0; jj < table.getColumnCount(); jj++) {
                        Object val = util.getValueAt(false, ii, jj);
                        if (val instanceof String) {
                            String strVal = (String) val;
                            for (String txt : textLst) {
                                if (strVal.toLowerCase().contains(txt)) {
                                    lst.add(jj);
                                    continue A;
                                }
                            }
                            for (Pattern pp : mthPtn.getRight()) {
                                if (pp != null && pp.matcher(strVal).find()) {
                                    lst.add(jj);
                                    continue A;
                                }
                            }
                        }
                    }
                }
                JTableUtil.newInstance(table).setCellBackgroundColor(Color.green.brighter(), changeColorMap, Arrays.asList(0));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void initTable() {
            if (titleLst != null && !titleLst.isEmpty()) {
                DefaultTableModel model = JTableUtil.createModel(new int[] { 0 }, new Object[] { "勾選", "項目" }, new Class[] { Boolean.class, String.class });
                table.setModel(model);
                for (String column : titleLst) {
                    model.addRow(new Object[] { false, column });
                }
            } else if (titleMap != null && !titleMap.isEmpty()) {
                DefaultTableModel model = JTableUtil.createModel(new int[] { 0 }, new Object[] { "勾選", "項目", "說明" }, new Class[] { Boolean.class, String.class, String.class });
                table.setModel(model);
                for (String column : titleMap.keySet()) {
                    model.addRow(new Object[] { false, column, StringUtils.trimToEmpty(titleMap.get(column)) });
                }
            }
            if (titleLst != null && !titleLst.isEmpty()) {
                JTableUtil.setColumnWidths_Percent(table, new float[] { 10f, 90f });
            } else if (titleMap != null && !titleMap.isEmpty()) {
                Map<String, Object> preferences = new HashMap<String, Object>();
                Map<Integer, Integer> presetColumns = new HashMap<Integer, Integer>();
                presetColumns.put(0, 20);
                preferences.put("presetColumns", presetColumns);
                JTableUtil.setColumnWidths_ByDataContent(table, preferences, getInsets(), false);
            }
        }
    }

    /**
     * Create the dialog.
     */
    public SimpleCheckListDlg(List<String> titleLst, Map<String, String> titleMap) {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        mMySearchHandler.titleLst = titleLst;
        mMySearchHandler.titleMap = titleMap;

        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                JLabel lblNewLabel = new JLabel("搜尋");
                panel.add(lblNewLabel);
            }
            {
                searchText = new JTextField();
                panel.add(searchText);
                searchText.setColumns(30);

                distinctCheckbox = new JCheckBox("");
                panel.add(distinctCheckbox);
                distinctCheckbox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        if (!distinctCheckbox.isSelected()) {
                            mMySearchHandler.initTable();
                            return;
                        }
                        JTableUtil.newInstance(table).setRowFilter(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                arg0.setSource(mMySearchHandler.handleRow(arg0.getID()));
                            }
                        });
                    }
                });

                searchText.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (JMouseEventUtil.buttonRightClick(1, e)) {
                            JPopupMenuUtil.newInstance(searchText)//
                                    .addJMenuItem("多行貼上", new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                String text = StringUtils.defaultString(searchText.getText());
                                                List<String> lst = new ArrayList<String>();
                                                Scanner scan = new Scanner(text);
                                                while (scan.hasNext()) {
                                                    lst.add("/^" + scan.next() + "$/");
                                                }
                                                scan.close();
                                                searchText.setText(StringUtils.join(lst, "^"));
                                            } catch (Exception ex) {
                                            }
                                        }
                                    }).applyEvent(e).show();
                        }
                    }
                });

                searchText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                    @Override
                    public void process(DocumentEvent event) {
                        mMySearchHandler.process();
                    }
                }));
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
            table = new JTable();
            JTableUtil.defaultSetting(table);
            contentPanel.add(JCommonUtil.createScrollComponent(table), BorderLayout.CENTER);

            mMySearchHandler.initTable();
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        List<String> pkLst = getCheckedList();
                        if (!pkLst.isEmpty()) {
                            okButtonAction.actionPerformed(new ActionEvent(SimpleCheckListDlg.this, -1, "this"));
                            dispose();
                        } else {
                            JCommonUtil._jOptionPane_showMessageDialog_error("請選擇主鍵!");
                        }
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        {
            JCommonUtil.setJFrameCenter(this);
        }
    }
}
