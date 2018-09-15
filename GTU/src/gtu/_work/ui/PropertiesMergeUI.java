package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import javax.swing.JCheckBox;

public class PropertiesMergeUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField srcDirText;
    private JTextField extensionNameText;
    private JList mergeList;
    private JButton mergeBtn;
    private JCheckBox silentModeChk;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PropertiesMergeUI frame = new PropertiesMergeUI();
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
    public PropertiesMergeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.NORTH);

        silentModeChk = new JCheckBox("silent");
        panel_1.add(silentModeChk);

        JLabel lblNewLabel_1 = new JLabel("副檔名");
        panel_1.add(lblNewLabel_1);

        extensionNameText = new JTextField();
        extensionNameText.setText(".properties");
        panel_1.add(extensionNameText);
        extensionNameText.setColumns(10);

        JLabel lblNewLabel = new JLabel("src dir");
        panel_1.add(lblNewLabel);

        srcDirText = new JTextField();
        panel_1.add(srcDirText);
        srcDirText.setColumns(10);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(srcDirText, true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initDirList();
            }
        });

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, BorderLayout.WEST);

        JPanel panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.SOUTH);

        mergeBtn = new JButton("merge");
        mergeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mergeBtnAction();
            }
        });
        panel_3.add(mergeBtn);

        JPanel panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.EAST);

        mergeList = new JList();
        mergeList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                JListUtil.newInstance(mergeList).defaultJListKeyPressed(arg0);
            }
        });
        panel.add(JCommonUtil.createScrollComponent(mergeList), BorderLayout.CENTER);

        JCommonUtil.setJFrameCenter(this);
    }

    private void initDirList() {
        try {
            String extensionName = extensionNameText.getText();
            Validate.notBlank(extensionName, "副檔名未輸入!");
            File srcDir = new File(srcDirText.getText());
            Validate.isTrue(srcDir.listFiles() != null && srcDir.listFiles().length > 0, "目錄下無檔案!");

            DefaultListModel model = JListUtil.createModel();
            Arrays.asList(srcDir.listFiles()).stream().forEach(f -> {
                FileZ f2 = new FileZ(f);
                model.addElement(f2);
            });
            mergeList.setModel(model);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private void mergeBtnAction() {
        try {
            String extensionName = StringUtils.trimToEmpty(extensionNameText.getText());
            Validate.notBlank(extensionName, "副檔名未輸入!");

            FileAppender appender = new FileAppender();
            appender.isProp = extensionName.contains("properties");
            appender.silent = silentModeChk.isSelected();

            IntStream.range(0, mergeList.getModel().getSize())//
                    .mapToObj(i -> (FileZ) mergeList.getModel().getElementAt(i))//
                    .forEach(fz -> {
                        appender.loadFile(fz.f);
                    });

            appender.save();
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    private class FileAppender {
        List<String> lst = new ArrayList<String>();
        Properties prop = new Properties();

        boolean isProp = false;
        boolean silent = false;

        private void loadFile(File file) {
            if (isProp) {
                try {
                    Properties p = new Properties();
                    p.load(new FileInputStream(file));
                    p.keySet().stream().map(v -> (String) v).forEach(key -> {
                        String val = p.getProperty(key);
                        append(key, val, silent);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("loadFile ERR : " + e.getMessage(), e);
                }
            } else {
                try {
                    List<String> lst = FileUtils.readLines(file);
                    lst.stream().map(StringUtils::trimToEmpty).forEach(v -> {
                        append(v, v, silent);
                    });
                } catch (Exception e) {
                    throw new RuntimeException("loadFile ERR : " + e.getMessage(), e);
                }
            }
        }

        private void append(String key, String value, boolean silent) {
            if (isProp) {
                // 當key同value不同要提示
                if (!silent) {
                    if (prop.containsKey(key)) {
                        String orignVal = prop.getProperty(key);
                        if (!StringUtils.equals(value, orignVal)) {
                            String[] arry = new String[] { orignVal, value };
                            value = (String) JCommonUtil._JOptionPane_showInputDialog("key 已存在 : " + key, "選擇適當的一個", arry, null);
                            if (value == null) {
                                Validate.isTrue(false, "中斷!!!");
                            }
                        }
                    }
                } else {
                    String orignVal = prop.getProperty(key);
                    if (StringUtils.length(orignVal) > StringUtils.length(value)) {
                        value = orignVal;
                    }
                }
                prop.setProperty(key, value);
            } else {
                if (!lst.contains(key)) {
                    lst.add(key);
                }
                if (!lst.contains(value)) {
                    lst.add(value);
                }
            }
        }

        private void save() {
            String dateText = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss");
            String defaultName = "file_" + dateText + "." + (isProp ? "properties" : "txt");
            String name = JCommonUtil._jOptionPane_showInputDialog("請出入檔名", defaultName);
            File saveFile = new File(FileUtil.DESKTOP_DIR, name);
            if (isProp) {
                try {
                    prop.store(new FileOutputStream(saveFile), DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss"));
                } catch (Exception e) {
                    throw new RuntimeException("loadFile ERR : " + e.getMessage(), e);
                }
            } else {
                StringBuffer sb = new StringBuffer();
                lst.stream().forEach(v -> sb.append(StringUtils.trimToEmpty(v) + "\r\n"));
                FileUtil.saveToFile(saveFile, sb.toString(), "UTF-8");
            }
            JCommonUtil._jOptionPane_showMessageDialog_info("儲存成功!\n" + name);
        }
    }

    private class FileZ {
        File f;

        FileZ(File f) {
            this.f = f;
        }

        public String toString() {
            return f.getName();
        }
    }
}
