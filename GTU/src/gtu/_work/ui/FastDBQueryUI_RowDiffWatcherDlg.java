package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;

public class FastDBQueryUI_RowDiffWatcherDlg extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private JTextField searchText;
    private ActionListener okButtonAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        final FastDBQueryUI_RowDiffWatcherDlg dlg = FastDBQueryUI_RowDiffWatcherDlg.newInstance(Arrays.asList("aa", "bb", "cc", "dd"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(((FastDBQueryUI_RowDiffWatcherDlg) e.getSource()).getPkLst());
            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(((FastDBQueryUI_RowDiffWatcherDlg) e.getSource()).getPkLst());
            }
        });
    }

    public static FastDBQueryUI_RowDiffWatcherDlg newInstance(List<String> titleLst, ActionListener okButtonAction, final ActionListener onCloseListener) {
        try {
            final FastDBQueryUI_RowDiffWatcherDlg dialog = new FastDBQueryUI_RowDiffWatcherDlg(titleLst);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.okButtonAction = okButtonAction;

            JTableUtil.setColumnWidths_Percent(dialog.table, new float[] { 10f, 90f });

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

    public List<String> getPkLst() {
        List<String> pkLst = new ArrayList<String>();
        JTableUtil t = JTableUtil.newInstance(table);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int ii = 0; ii < model.getRowCount(); ii++) {
            Boolean v = (Boolean) t.getValueAt(true, ii, 0);
            String n = (String) t.getValueAt(true, ii, 1);
            if (v) {
                pkLst.add(n);
            }
        }
        return pkLst;
    }

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_RowDiffWatcherDlg(List<String> titleLst) {
        setTitle("請設定主鍵");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
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
                searchText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                    @Override
                    public void process(DocumentEvent event) {
                        try {
                            Map<Integer, List<Integer>> changeColorMap = new HashMap<Integer, List<Integer>>();
                            if (StringUtils.isBlank(searchText.getText())) {
                                JTableUtil.newInstance(table).setCellBackgroundColor(Color.green.brighter(), changeColorMap, Arrays.asList(0));
                                return;
                            }
                            String text1 = StringUtils.trimToEmpty(searchText.getText());
                            String text = text1.toLowerCase();
                            Pattern ptn = null;
                            try {
                                ptn = Pattern.compile(text1, Pattern.CASE_INSENSITIVE);
                            } catch (Exception ex2) {
                            }

                            JTableUtil util = JTableUtil.newInstance(table);
                            DefaultTableModel model = util.getModel();

                            for (int ii = 0; ii < model.getRowCount(); ii++) {
                                List<Integer> lst = new ArrayList<Integer>();
                                changeColorMap.put(ii, lst);
                                for (int jj = 0; jj < table.getColumnCount(); jj++) {
                                    Object val = util.getValueAt(true, ii, jj);
                                    if (val instanceof String) {
                                        String strVal = (String) val;
                                        if (strVal.toLowerCase().contains(text)) {
                                            lst.add(jj);
                                        } else if (ptn != null) {
                                            if (ptn.matcher(strVal).find()) {
                                                lst.add(jj);
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
            contentPanel.add(JCommonUtil.createScrollComponent(table), BorderLayout.CENTER);
            DefaultTableModel model = JTableUtil.createModel(new int[] { 0 }, new Object[] { "主鍵", "欄位" }, new Class[] { Boolean.class, String.class });
            table.setModel(model);
            for (String column : titleLst) {
                model.addRow(new Object[] { false, column });
            }
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
                        List<String> pkLst = getPkLst();
                        if (!pkLst.isEmpty()) {
                            okButtonAction.actionPerformed(new ActionEvent(FastDBQueryUI_RowDiffWatcherDlg.this, -1, "this"));
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
