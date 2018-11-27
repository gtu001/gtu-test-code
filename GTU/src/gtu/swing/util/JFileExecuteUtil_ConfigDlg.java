package gtu.swing.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class JFileExecuteUtil_ConfigDlg extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField madEditText;
    private JTextField wordText;
    private JTextField _7zText;
    private JTextField excelText;
    private JTextField firefoxText;
    private JTextField jdGuiText;
    private JTextField eclipseText;
    private JTextField eclipseCompanyText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        JFileExecuteUtil_ConfigDlg.newInstance();
    }

    public static void newInstance() {
        try {
            JFileExecuteUtil_ConfigDlg dialog = new JFileExecuteUtil_ConfigDlg();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSetText() {
        Properties prop = JFileExecuteUtil.executeConfig.getConfigProp();
        madEditText.setText(StringUtils.trimToEmpty(prop.getProperty("MAD_EDIT")));
        wordText.setText(StringUtils.trimToEmpty(prop.getProperty("WINWORD")));
        _7zText.setText(StringUtils.trimToEmpty(prop.getProperty("7Z")));
        excelText.setText(StringUtils.trimToEmpty(prop.getProperty("EXCEL")));
        firefoxText.setText(StringUtils.trimToEmpty(prop.getProperty("FIREFOX")));
        jdGuiText.setText(StringUtils.trimToEmpty(prop.getProperty("JD_GUI")));
        eclipseText.setText(StringUtils.trimToEmpty(prop.getProperty("ECLIPSE")));
        eclipseCompanyText.setText(StringUtils.trimToEmpty(prop.getProperty("ECLIPSE_COMPANY")));
    }

    /**
     * Create the dialog.
     */
    public JFileExecuteUtil_ConfigDlg() {
        setBounds(100, 100, 616, 434);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, }));
        {
            JLabel lblNewLabel = new JLabel("Mad Edit");
            contentPanel.add(lblNewLabel, "2, 2, right, default");
        }
        {
            madEditText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(madEditText, false);
            contentPanel.add(madEditText, "4, 2, fill, default");
            madEditText.setColumns(10);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("Word");
            contentPanel.add(lblNewLabel_1, "2, 4, right, default");
        }
        {
            wordText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(wordText, false);
            contentPanel.add(wordText, "4, 4, fill, default");
            wordText.setColumns(10);
        }
        {
            JLabel lblNewLabel_2 = new JLabel("7z");
            contentPanel.add(lblNewLabel_2, "2, 6, right, default");
        }
        {
            _7zText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(_7zText, false);
            contentPanel.add(_7zText, "4, 6, fill, default");
            _7zText.setColumns(10);
        }
        {
            JLabel lblNewLabel_3 = new JLabel("EXCEL");
            contentPanel.add(lblNewLabel_3, "2, 8, right, default");
        }
        {
            excelText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(excelText, false);
            contentPanel.add(excelText, "4, 8, fill, default");
            excelText.setColumns(10);
        }
        {
            JLabel lblNewLabel_4 = new JLabel("firefox");
            contentPanel.add(lblNewLabel_4, "2, 10, right, default");
        }
        {
            firefoxText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(firefoxText, false);
            contentPanel.add(firefoxText, "4, 10, fill, default");
            firefoxText.setColumns(10);
        }
        {
            JLabel lblNewLabel_5 = new JLabel("jd-GUI");
            contentPanel.add(lblNewLabel_5, "2, 12, right, default");
        }
        {
            jdGuiText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(jdGuiText, false);
            contentPanel.add(jdGuiText, "4, 12, fill, default");
            jdGuiText.setColumns(10);
        }
        {
            JLabel lblNewLabel_6 = new JLabel("eclipse");
            contentPanel.add(lblNewLabel_6, "2, 14, right, default");
        }
        {
            eclipseText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(eclipseText, false);
            contentPanel.add(eclipseText, "4, 14, fill, default");
            eclipseText.setColumns(10);
        }
        {
            JLabel lblNewLabel_7 = new JLabel("eclipse_company");
            contentPanel.add(lblNewLabel_7, "2, 16, right, default");
        }
        {
            eclipseCompanyText = new JTextField();
            JCommonUtil.jTextFieldSetFilePathMouseEvent(eclipseCompanyText, false);
            contentPanel.add(eclipseCompanyText, "4, 16, fill, default");
            eclipseCompanyText.setColumns(10);
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
                        try {
                            List<String> saveLst = new ArrayList<String>();
                            Properties prop = JFileExecuteUtil.executeConfig.getConfigProp();
                            if (StringUtils.isNotBlank(madEditText.getText())) {
                                prop.setProperty("MAD_EDIT", StringUtils.trimToEmpty(madEditText.getText()));
                                saveLst.add("MAD_EDIT");
                            }
                            if (StringUtils.isNotBlank(wordText.getText())) {
                                prop.setProperty("WINWORD", StringUtils.trimToEmpty(wordText.getText()));
                                saveLst.add("WINWORD");
                            }
                            if (StringUtils.isNotBlank(_7zText.getText())) {
                                prop.setProperty("7Z", StringUtils.trimToEmpty(_7zText.getText()));
                                saveLst.add("7Z");
                            }
                            if (StringUtils.isNotBlank(excelText.getText())) {
                                prop.setProperty("EXCEL", StringUtils.trimToEmpty(excelText.getText()));
                                saveLst.add("EXCEL");
                            }
                            if (StringUtils.isNotBlank(firefoxText.getText())) {
                                prop.setProperty("FIREFOX", StringUtils.trimToEmpty(firefoxText.getText()));
                                saveLst.add("FIREFOX");
                            }
                            if (StringUtils.isNotBlank(jdGuiText.getText())) {
                                prop.setProperty("JD_GUI", StringUtils.trimToEmpty(jdGuiText.getText()));
                                saveLst.add("JD_GUI");
                            }
                            if (StringUtils.isNotBlank(eclipseText.getText())) {
                                prop.setProperty("ECLIPSE", StringUtils.trimToEmpty(eclipseText.getText()));
                                saveLst.add("ECLIPSE");
                            }
                            if (StringUtils.isNotBlank(eclipseCompanyText.getText())) {
                                prop.setProperty("ECLIPSE_COMPANY", StringUtils.trimToEmpty(eclipseCompanyText.getText()));
                                saveLst.add("ECLIPSE_COMPANY");
                            }
                            JFileExecuteUtil.executeConfig.store();
                            JCommonUtil._jOptionPane_showMessageDialog_info("儲存以下 : \n" + saveLst);
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                        }
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
            {
                this.initSetText();
                JCommonUtil.setJFrameCenter(this);
            }
        }
    }

}
