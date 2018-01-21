package gtu._work.ui;

import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTextAreaUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

/**
 * @author gtu001
 *
 */
public class StoryReaderUI extends JFrame {

    private JPanel contentPane;

    private JList fileList = new JList();
    private JTextArea textArea = new JTextArea();
    private JLabel lineNumberLabel = new JLabel("");

    private static File configFile = new File(PropertiesUtil.getJarCurrentPath(StoryReaderUI.class), StoryReaderUI.class.getSimpleName() + "_fileList.properties");
    private static Properties configProp = new Properties();

    private Map<Integer, String> lineNumberMap = new LinkedHashMap<Integer, String>();
    private int currentLineNumber = -1;
    private int maxLineNumber = -1;
    private String currentFileUrl;

    /**
     * 初始化檔案清單
     */
    private void loadAndSyncConfigProp() {
        Properties prop = new Properties();
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            prop.load(new FileInputStream(configFile));
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
        if (configProp == null) {
            configProp = new Properties();
        }
        for (Enumeration enu = prop.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = prop.getProperty(key);
            if (!configProp.containsKey(key)) {
                configProp.setProperty(key, value);
                System.out.println("Add---" + key + " : " + value);
            }
        }
        DefaultListModel model = JListUtil.createModel();
        for (Enumeration enu = configProp.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            String value = configProp.getProperty(key);
            System.out.println("List---" + key + " : " + value);
            model.addElement(key);
        }
        fileList.setModel(model);
        try {
            configProp.store(new FileOutputStream(configFile), "------");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 新增檔案
     */
    private void addNewFileToFileList() {
        File file = JCommonUtil._jFileChooser_selectFileOnly();
        if (file == null) {
            return;
        }
        String key = file.getAbsolutePath();
        System.out.println("key = " + key);
        if (!configProp.containsKey(key)) {
            configProp.setProperty(key, "-1");
        }

        // 初始化檔案清單
        loadAndSyncConfigProp();
    }

    /**
     * 點檔案清單
     */
    private void fileListClickEvent() {
        String fileUrl = JListUtil.getLeadSelectionObject(fileList);
        if (fileUrl == null) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
            return;
        }
        File file = new File(fileUrl);
        if (!file.exists()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
            return;
        }
        try {
            lineNumberMap = new LinkedHashMap<Integer, String>();
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), "utf8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                lineNumberMap.put(reader.getLineNumber(), line);
                maxLineNumber = reader.getLineNumber();
            }
            reader.close();

            currentLineNumber = Integer.parseInt(configProp.getProperty(fileUrl));
            currentFileUrl = fileUrl;

            // 重設textarea
            resetTextArea(null);
        } catch (Exception e) {
            JCommonUtil.handleException(e);
        }
    }

    /**
     * 設定儲存點
     */
    private void savepoint() {
        if (StringUtils.isNotBlank(currentFileUrl)) {
            if (maxLineNumber != -1 && currentLineNumber != -1) {
                if (currentLineNumber > 0 && currentLineNumber <= maxLineNumber) {
                    configProp.setProperty(currentFileUrl, String.valueOf(currentLineNumber));
                    try {
                        configProp.store(new FileOutputStream(configFile), "------");
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            }
        }
    }
    
    /**
     * 重設儲存點
     */
    private void resetSavepoint(){
        if (StringUtils.isNotBlank(currentFileUrl)) {
            configProp.setProperty(currentFileUrl, "-1");
            try {
                configProp.store(new FileOutputStream(configFile), "------");
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }

    /**
     * 重設textarea
     */
    private void resetTextArea(String upperAndDownText) {
        if (upperAndDownText != null) {
            switch (upperAndDownText.charAt(0)) {
            case 'u':
                currentLineNumber--;
                break;
            case 'd':
                currentLineNumber++;
                break;
            }
        }

        if (currentLineNumber <= 1) {
            currentLineNumber = 1;
        }

        if (currentLineNumber >= maxLineNumber) {
            JCommonUtil._jOptionPane_showMessageDialog_error("本文結束!");
            return;
        }
        
        String text = null;
        boolean nextChk = false;
        do {
            lineNumberLabel.setText(String.valueOf(currentLineNumber));
            text = StringUtils.defaultString(lineNumberMap.get(currentLineNumber));
            textArea.setText(text);

            if (StringUtils.isBlank(text)) {
                switch (upperAndDownText.charAt(0)) {
                case 'u':
                    currentLineNumber--;
                    nextChk = currentLineNumber > 1;
                    break;
                case 'd':
                    currentLineNumber++;
                    nextChk = currentLineNumber < maxLineNumber;
                    break;
                }
            } else {
                break;
            }
        } while (nextChk);
    }

    /**
     * 設定字形大小
     */
    private void setFontSize() {
        String val = JCommonUtil._jOptionPane_showInputDialog("字型大小", "12");
        if (val != null) {
            int fontSize = Integer.parseInt(val);
            textArea.setFont(new java.awt.Font("新細明體", 0, fontSize));
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StoryReaderUI frame = new StoryReaderUI();
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
    public StoryReaderUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 513, 374);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("檔案清單", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        JButton button = new JButton("開檔");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                addNewFileToFileList();
            }
        });
        panel.add(button, BorderLayout.SOUTH);

        panel.add(fileList, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("文件", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.EAST);
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

        JButton button_1 = new JButton("上");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTextArea("u");
            }
        });

        panel_2.add(lineNumberLabel);
        panel_2.add(button_1);

        JButton button_2 = new JButton("下");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetTextArea("d");
            }
        });
        panel_2.add(button_2);

        JButton btnFont = new JButton("大小");
        btnFont.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                setFontSize();
            }
        });
        panel_2.add(btnFont);

        JButton btnRec = new JButton("紀錄");
        btnRec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                savepoint();
            }
        });
        panel_2.add(btnRec);
        
        JButton btnReset = new JButton("重設");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                resetSavepoint();
            }
        });
        panel_2.add(btnReset);

        JScrollPane scrollPanl = JCommonUtil.createScrollComponent(textArea, false, true);
        textArea.setEditable(false);
        JTextAreaUtil.setWrapTextArea(textArea);

        panel_1.add(scrollPanl, BorderLayout.CENTER);

        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                fileListClickEvent();
            }
        });

        // 初始化檔案清單
        loadAndSyncConfigProp();

        //離開前儲存
        exitThisApp();
    }

    /**
     * 離開前儲存
     */
    private void exitThisApp() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int closeOption = JOptionPane.showConfirmDialog(null, "離開前儲存?");
                if (closeOption == JOptionPane.YES_OPTION) {
                    savepoint();
                    loadAndSyncConfigProp();
                }
                StoryReaderUI.this.setVisible(false);
                StoryReaderUI.this.dispose();
                System.exit(0);
            }
        });
    }

}
