package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JTextAreaUtil;

public class ExecuteOpener_RemarkDlg extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel fileNameLbl;
    private JTextArea remarkArea;
    private PropertiesUtilBean remarkConfig;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        PropertiesUtilBean config = new PropertiesUtilBean(ExecuteOpener_RemarkDlg.class, ExecuteOpener_RemarkDlg.class.getSimpleName() + "_test");
        ExecuteOpener_RemarkDlg.newInstance("", config);
    }

    public static ExecuteOpener_RemarkDlg newInstance(String filename, PropertiesUtilBean remarkConfig) {
        ExecuteOpener_RemarkDlg dialog = new ExecuteOpener_RemarkDlg();
        try {
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.fileNameLbl.setText(filename);
            dialog.remarkConfig = remarkConfig;
            if (remarkConfig != null && remarkConfig.getConfigProp().containsKey(filename)) {
                String text = remarkConfig.getConfigProp().getProperty(filename);
                dialog.remarkArea.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    /**
     * Create the dialog.
     */
    public ExecuteOpener_RemarkDlg() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            {
                fileNameLbl = new JLabel("New label");
                panel.add(fileNameLbl);
            }
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.WEST);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.SOUTH);
        }
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.EAST);
        }
        {
            remarkArea = new JTextArea();
            JTextAreaUtil.applyCommonSetting(remarkArea);
            contentPanel.add(JCommonUtil.createScrollComponent(remarkArea), BorderLayout.CENTER);
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
                        if (remarkConfig != null) {
                            remarkConfig.getConfigProp().setProperty(fileNameLbl.getText(), remarkArea.getText());
                            remarkConfig.store();
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
        JCommonUtil.setJFrameCenter(this);
    }
}
