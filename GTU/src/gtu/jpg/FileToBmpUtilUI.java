package gtu.jpg;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import javax.swing.JCheckBox;

public class FileToBmpUtilUI extends JFrame {

    private JPanel contentPane;
    private JTextField srcFileText;
    private JTextField toBmpText;
    private JTextField srcBmpText;
    private JTextField toFileText;
    private JTextField widthText;
    private JCheckBox usePicNameCheckbox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileToBmpUtilUI frame = new FileToBmpUtilUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FileToBmpUtilUI() {
        setTitle("file <-> bmp");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 535, 401);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("file->bmp", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblSrcFile = new JLabel("src file");
        panel.add(lblSrcFile, "2, 2, right, default");

        srcFileText = new JTextField();
        panel.add(srcFileText, "4, 2, fill, default");
        srcFileText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcFileText, false);
        srcFileText.getDocument().addDocumentListener(null);
        srcFileText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    File file = JCommonUtil.filePathCheck(srcFileText.getText(), "檔案來源", false);
                    initToBmpText();
                    widthText.setText(String.valueOf(FileToBmpUtilVer2.getInstance().getWidth(file)));
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        }));

        JLabel lblDestBmp = new JLabel("dest bmp");
        panel.add(lblDestBmp, "2, 4, right, default");

        toBmpText = new JTextField();
        toBmpText.setColumns(10);
        panel.add(toBmpText, "4, 4, fill, default");
        JCommonUtil.jTextFieldSetFilePathMouseEvent(toBmpText, false);
        toBmpText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
            }
        }));

        JButton btnGo = new JButton("go");
        btnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                try {
                    File srcFile = JCommonUtil.filePathCheck(srcFileText.getText(), "檔案來源", false);
                    File toBmpFile = new File(toBmpText.getText());
                    if (!toBmpFile.getParentFile().exists()) {
                        toBmpFile.getParentFile().mkdirs();
                    }
                    if (!StringUtils.isNumeric(widthText.getText())) {
                        Validate.isTrue(false, "寬度有誤");
                    }
                    int width = Integer.parseInt(widthText.getText());
                    FileToBmpUtilVer2.getInstance().buildImageFromFile(srcFile, toBmpFile, true, width);
                    JCommonUtil._jOptionPane_showMessageDialog_info("產生圖檔成功 : " + toBmpFile);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        JLabel lblWidth = new JLabel("width");
        panel.add(lblWidth, "2, 6, right, default");

        widthText = new JTextField();
        widthText.setColumns(10);
        panel.add(widthText, "4, 6, fill, default");
        panel.add(btnGo, "2, 8");

        usePicNameCheckbox = new JCheckBox("使用圖片檔名");
        usePicNameCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    initToBmpText();
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel.add(usePicNameCheckbox, "4, 8");

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("bmp->file", null, panel_1, null);
        panel_1.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblSrcBmp = new JLabel("src bmp");
        panel_1.add(lblSrcBmp, "2, 2, right, default");

        srcBmpText = new JTextField();
        panel_1.add(srcBmpText, "4, 2, fill, default");
        srcBmpText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcBmpText, false);
        srcBmpText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                File f = new File(srcBmpText.getText());
                String toName = FileUtil.getNameNoSubName(f);
                File destFile = new File(FileUtil.DESKTOP_PATH, toName);
                toFileText.setText(destFile.getAbsolutePath());
            }
        }));

        JLabel lblToFile = new JLabel("to file");
        panel_1.add(lblToFile, "2, 4, right, default");

        toFileText = new JTextField();
        panel_1.add(toFileText, "4, 4, fill, default");
        toFileText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(toFileText, false);
        toFileText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
            }
        }));

        JButton btnGo_1 = new JButton("go");
        btnGo_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                try {
                    File srcBmpFile = JCommonUtil.filePathCheck(srcBmpText.getText(), "BMP檔案來源", "bmp");
                    File toFile = new File(toFileText.getText());
                    if (!toFile.getParentFile().exists()) {
                        toFile.getParentFile().mkdirs();
                    }
                    FileToBmpUtilVer2.getInstance().getFileFromImage_FixName(srcBmpFile, toFile);
                    JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案成功 : " + toFile);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        panel_1.add(btnGo_1, "2, 6");
    }

    /*
     * 初始化圖檔
     */
    private void initToBmpText() {
        if (usePicNameCheckbox.isSelected()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = sdf.format(new Date());
            toBmpText.setText(new File(FileUtil.DESKTOP_PATH, "IMG" + fileName + ".bmp").getAbsolutePath());
        } else {
            File file = new File(srcFileText.getText());
            if (file.isFile()) {
                toBmpText.setText(new File(FileUtil.DESKTOP_PATH, file.getName() + ".bmp").getAbsolutePath());
            }
        }
    }
}
