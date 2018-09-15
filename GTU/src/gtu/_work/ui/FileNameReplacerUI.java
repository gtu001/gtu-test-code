package gtu._work.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;

public class FileNameReplacerUI extends JFrame {

    private JPanel contentPane;
    private JTextField fileNameText;
    private JTextField resultText;
    private JPanel panel;
    private JButton executeBtn;
    private JButton clearBtn;

    private PropertiesUtilBean config = new PropertiesUtilBean(FileNameReplacerUI.class);
    private JCheckBox isFullPathOrJustNameChk;
    private JCheckBox useFullCharReplaceChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileNameReplacerUI frame = new FileNameReplacerUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FileNameReplacerUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 420, 208);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        JLabel lblNewLabel = new JLabel("filename");
        contentPane.add(lblNewLabel, "2, 2, right, default");

        fileNameText = new JTextField();
        contentPane.add(fileNameText, "4, 2, fill, default");
        fileNameText.setColumns(10);

        isFullPathOrJustNameChk = new JCheckBox("完整路徑");
        contentPane.add(isFullPathOrJustNameChk, "4, 4");

        useFullCharReplaceChk = new JCheckBox("使用全形替換");
        contentPane.add(useFullCharReplaceChk, "4, 6");

        resultText = new JTextField();
        contentPane.add(resultText, "4, 8, fill, default");
        resultText.setColumns(10);

        panel = new JPanel();
        contentPane.add(panel, "4, 12, fill, fill");

        executeBtn = new JButton("執行");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeBtnAction();
            }
        });
        panel.add(executeBtn);

        clearBtn = new JButton("清除");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearBtnAction();
            }
        });
        panel.add(clearBtn);

        config.reflectInit(this);

        JCommonUtil.setJFrameDefaultSetting(this);
        JCommonUtil.frameCloseConfirm(this, false, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    config.reflectSetConfig(FileNameReplacerUI.this);
                    config.store();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
    }

    private void executeBtnAction() {
        try {
            String path = StringUtils.trimToEmpty(fileNameText.getText());
            String result = "";
            if (useFullCharReplaceChk.isSelected()) {
                result = FileUtil.escapeFilename_replaceToFullChar(path, !isFullPathOrJustNameChk.isSelected());
            } else {
                result = FileUtil.escapeFilename(path, !isFullPathOrJustNameChk.isSelected());
            }
            resultText.setText(result);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void clearBtnAction() {
        try {
            fileNameText.setText("");
            resultText.setText("");
            // isFullPathOrJustNameChk
            // useFullCharReplaceChk
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
