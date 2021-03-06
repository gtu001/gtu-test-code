package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.KeyEventExecuteHandler;

public class FastDBQueryUI_UpdateSqlArea extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextArea updateSqlArea;
    private JLabel lblForMessage;
    private static final String DELIMIT = "^;^";
    private static final String DELIMIT_SPLIT_PTN = Pattern.quote(DELIMIT);
    private SqlAreaHandler sqlAreaHandler = new SqlAreaHandler();
    private ActionListener confirmDo;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private boolean jFrameRGBColorPanel_isStop;
    private KeyEventExecuteHandler keyEventExecuteHandler;
    private JButton okButton;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        FastDBQueryUI_UpdateSqlArea.newInstance("XXXX", Arrays.asList("1111", "eeeee"), false, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(((FastDBQueryUI_RowPKSettingDlg) e.getSource()).getPkLst());
            }
        });
    }

    public static FastDBQueryUI_UpdateSqlArea newInstance(String title, List<String> sqlText, boolean jFrameRGBColorPanel_isStop, final ActionListener onCloseListener) {
        final FastDBQueryUI_UpdateSqlArea dialog = new FastDBQueryUI_UpdateSqlArea();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        dialog.lblForMessage.setText(title);
        dialog.updateSqlArea.setText(dialog.sqlAreaHandler.convert(sqlText));
        dialog.jFrameRGBColorPanel_isStop = jFrameRGBColorPanel_isStop;

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                if (onCloseListener != null) {
                    onCloseListener.actionPerformed(new ActionEvent(dialog, -1, "close"));
                }
            }

            public void windowClosing(WindowEvent e) {
            }
        });
        return dialog;
    }

    public void setConfirmDo(ActionListener confirmDo) {
        this.confirmDo = confirmDo;
    }

    private class SqlAreaHandler {
        private String convert(List<String> sqlLst) {
            return StringUtils.join(sqlLst, DELIMIT + "\n");
        }

        private List<String> convert(String sqlText) {
            String text = StringUtils.trimToEmpty(sqlText);
            text = text.replace("[\r\n]+", "");
            return Arrays.asList(text.split(DELIMIT_SPLIT_PTN, -1));
        }
    }

    public List<String> getSqlText() {
        return sqlAreaHandler.convert(this.updateSqlArea.getText());
    }

    /**
     * Create the dialog.
     */
    public FastDBQueryUI_UpdateSqlArea() {
        setBounds(100, 100, 554, 378);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                lblForMessage = new JLabel("                         ");
                panel.add(lblForMessage);
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
            updateSqlArea = new JTextArea();
            JTextAreaUtil.applyCommonSetting(updateSqlArea);
            contentPanel.add(JCommonUtil.createScrollComponent(updateSqlArea), BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton exportSqlBtn = new JButton("匯出檔案");
                exportSqlBtn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        exportSqlBtnAction();
                    }
                });
                buttonPane.add(exportSqlBtn);
            }
            {
                okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);// 預設按鈕

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (confirmDo != null) {
                            confirmDo.actionPerformed(e);
                        }
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

        this.keyEventExecuteHandler = KeyEventExecuteHandler.newInstance(this, null, null, new Runnable() {
            @Override
            public void run() {
                JCommonUtil.triggerButtonActionPerformed(okButton);
            }
        }, new Component[] {});

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.defaultToolTipDelay();
        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
        jFrameRGBColorPanel.setStop(jFrameRGBColorPanel_isStop);
    }

    private void exportSqlBtnAction() {
        String sqlText = StringUtils.defaultString(updateSqlArea.getText());
        if (sqlText.contains("^;^")) {
            sqlText = sqlText.replaceAll("\\^\\;\\^", ";");
            sqlText += ";";
        }
        String filename = JCommonUtil._jOptionPane_showInputDialog("請輸入匯出檔名",
                FastDBQueryUI.class.getSimpleName() + "_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss") + ".sql");
        if (StringUtils.isBlank(filename)) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔名有誤!");
            return;
        }
        File outputFile = new File(FileUtil.DESKTOP_DIR, filename);
        if (outputFile.exists()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔案已存在!");
            return;
        }
        FileUtil.saveToFile(outputFile, sqlText, "UTF8");
        JCommonUtil._jOptionPane_showMessageDialog_info("匯出完成 : " + outputFile);
    }
}
