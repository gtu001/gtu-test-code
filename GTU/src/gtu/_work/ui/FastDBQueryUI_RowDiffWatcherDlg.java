package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTableUtil;

public class FastDBQueryUI_RowDiffWatcherDlg extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
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
        });
    }

    public static FastDBQueryUI_RowDiffWatcherDlg newInstance(List<String> titleLst, ActionListener okButtonAction) {
        try {
            FastDBQueryUI_RowDiffWatcherDlg dialog = new FastDBQueryUI_RowDiffWatcherDlg(titleLst);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.okButtonAction = okButtonAction;
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
                        okButtonAction.actionPerformed(new ActionEvent(FastDBQueryUI_RowDiffWatcherDlg.this, -1, "this"));
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        {
            JCommonUtil.setJFrameCenter(this);
        }
    }
}
