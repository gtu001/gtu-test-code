package gtu.jpg;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JTextFieldUtil;

public class FileToBmpUtilUI extends JFrame {

    private JPanel contentPane;
    private JTextField srcFileText;
    private JTextField toBmpText;
    private JTextField srcBmpText;
    private JTextField toFileText;
    private JTextField widthText;
    private JCheckBox usePicNameCheckbox;
    private JTextField indicateSizeText;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JTextField sevenZipText;
    private JLabel sevenZipLabel;
    private JLabel fileSizeDescLabel;
    
    private JFrameRGBColorPanel jFrameRGBColorPanel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(FileToBmpUtilUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileToBmpUtilUI frame = new FileToBmpUtilUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
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
        JTextFieldUtil.setupDragDropFilePath(srcFileText, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<File> srcFileLst = (List<File>) e.getSource();
                    for (File f : srcFileLst) {
                        File srcFile = f;
                        String newFileName = srcFile.getName() + ".bmp";
                        File destFile = new File(FileUtil.DESKTOP_DIR, newFileName);
                        int width = FileToBmpUtilVer3.getInstance().getWidth(f);
                        FileToBmpUtilVer3.getInstance().buildImageFromFile(srcFile, destFile, true, width);
                        System.out.println("#-- " + srcFile.getName() + " -> " + destFile.getName());
                    }
                    JCommonUtil._jOptionPane_showMessageDialog_info("產生圖檔成功[多檔] " + srcFileLst.size());
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        srcFileText.getDocument().addDocumentListener(null);
        srcFileText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    File file = JCommonUtil.filePathCheck(srcFileText.getText(), "檔案來源", false);
                    initToBmpText();
                    widthText.setText(String.valueOf(FileToBmpUtilVer3.getInstance().getWidth(file)));
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
                    FileToBmpUtilVer3.getInstance().buildImageFromFile(srcFile, toBmpFile, true, width);
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
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblSrcBmp = new JLabel("src bmp");
        panel_1.add(lblSrcBmp, "2, 2, right, default");

        srcBmpText = new JTextField();
        panel_1.add(srcBmpText, "4, 2, fill, default");
        srcBmpText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcBmpText, false);
        JTextFieldUtil.setupDragDropFilePath(srcBmpText, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<File> fileLst = (List<File>) e.getSource();
                    for (File f : fileLst) {
                        File srcBmpFile = f;
                        File toFile = new File(FileUtil.DESKTOP_DIR, FileUtil.getNameNoSubName(srcBmpFile));
                        FileToBmpUtilVer3.getInstance().getFileFromImage(srcBmpFile, toFile);// fileSize
                        System.out.println("#-- " + srcBmpFile.getName() + " -> " + toFile.getName());
                    }
                    JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案成功 [多檔] " + fileLst.size());
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        srcBmpText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                File f = new File(srcBmpText.getText());

                try {
                    fileSizeDescLabel.setText(FileUtil.getSizeDescription(f.length()));
                } catch (Exception ex) {
                    fileSizeDescLabel.setText("size[ERR]");
                }

                String toName = FileUtil.getNameNoSubName(f);
                File destFile = new File(FileUtil.DESKTOP_PATH, toName);
                toFileText.setText(destFile.getAbsolutePath());

                Pattern ptn = Pattern.compile(".*\\_(\\d+)\\.bmp");
                Matcher mth = ptn.matcher(srcBmpText.getText());
                if (mth.find()) {
                    indicateSizeText.setText(mth.group(1));
                }
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

                    // 用7z來產生檔案
                    if (StringUtils.isNotBlank(sevenZipLabel.getText())) {
                        File tmp7zFile = new File(toFile.getParentFile(), sevenZipLabel.getText());
                        if (tmp7zFile.exists()) {
                            JCommonUtil._jOptionPane_showMessageDialog_error("檔案已存在 : " + tmp7zFile.getName());
                        } else {
                            toFile = tmp7zFile;
                        }
                    }

                    int fileSize = 0;
                    try {
                        fileSize = Integer.parseInt(indicateSizeText.getText());
                    } catch (Exception ex) {
                        System.out.println("fileSize ERR : " + ex.getMessage());
                    }
                    if (!toFile.getParentFile().exists()) {
                        toFile.getParentFile().mkdirs();
                    }
                    FileToBmpUtilVer3.getInstance().getFileFromImage(srcBmpFile, toFile);// fileSize
                    JCommonUtil._jOptionPane_showMessageDialog_info("產生檔案成功 : " + toFile);
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });

        JLabel lblNewLabel_1 = new JLabel("資訊");
        panel_1.add(lblNewLabel_1, "2, 6");

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, "4, 6, fill, fill");

        fileSizeDescLabel = new JLabel("NA");
        panel_3.add(fileSizeDescLabel);

        JLabel lblSize = new JLabel("size");
        panel_1.add(lblSize, "2, 8, right, default");

        indicateSizeText = new JTextField();
        panel_1.add(indicateSizeText, "4, 8, fill, default");
        indicateSizeText.setColumns(10);

        JLabel lblNewLabel = new JLabel("7z");
        panel_1.add(lblNewLabel, "2, 10");

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, "4, 10, fill, fill");

        sevenZipText = new JTextField();
        panel_2.add(sevenZipText);
        sevenZipText.setColumns(10);

        sevenZipText.getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
            @Override
            public void process(DocumentEvent event) {
                try {
                    sevenZipLabel.setText(String.format("1.7z.%03d", Integer.parseInt(sevenZipText.getText())));
                } catch (Exception ex) {
                    sevenZipLabel.setText("");
                }
            }
        }));

        sevenZipLabel = new JLabel("");
        panel_2.add(sevenZipLabel);
        panel_1.add(btnGo_1, "2, 12");
        
        JPanel panel_4 = new JPanel();
        tabbedPane.addTab("Config", null, panel_4, null);

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.setJFrameIcon(this, "resource/images/ico/hacker.ico");
        hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
        hideInSystemTrayHelper.apply(this);
        
        jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
        jFrameRGBColorPanel.start();
        panel_4.add(jFrameRGBColorPanel.getToggleButton());
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
