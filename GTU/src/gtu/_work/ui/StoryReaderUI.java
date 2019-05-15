package gtu._work.ui;

import java.awt.BorderLayout;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseAdapter;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseWheelAdapter;
import org.jnativehook.mouse.NativeMouseWheelEvent;

import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFontChooserHelper;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTextAreaUtil;
import javax.swing.JCheckBox;

/**
 * @author gtu001
 *
 */
public class StoryReaderUI extends JFrame {

    private JPanel contentPane;
    private JTextArea textArea = new JTextArea();
    private JLabel lineNumberLabel = new JLabel("");

    private static File configFile = new File(PropertiesUtil.getJarCurrentPath(StoryReaderUI.class), StoryReaderUI.class.getSimpleName() + "_fileList.properties");
    private static Properties configProp = new Properties();

    private Map<Integer, String> lineNumberMap = new LinkedHashMap<Integer, String>();
    private int currentLineNumber = -1;
    private int maxLineNumber = -1;
    private String currentFileUrl;
    private JList storyList;
    private JTextField storyEncodingText;
    private PropertiesUtilBean configBean = new PropertiesUtilBean(StoryReaderUI.class);

    /**
     * 初始化檔案清單
     */
    private void loadAndSyncConfigProp() {
        try {
            Properties prop = new Properties();
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            prop.load(new FileInputStream(configFile));
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
            storyList.setModel(model);
            configProp.store(new FileOutputStream(configFile), "------");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 新增檔案
     */
    private void addNewFileToFileList() {
        try {
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
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 點檔案清單
     */
    private void fileListClickEvent() {
        try {
            String fileUrl = JListUtil.getLeadSelectionObject(storyList);
            if (fileUrl == null) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                return;
            }
            File file = new File(fileUrl);
            if (!file.exists()) {
                JCommonUtil._jOptionPane_showMessageDialog_error("檔案不存在!");
                return;
            }

            lineNumberMap = new LinkedHashMap<Integer, String>();
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), storyEncodingText.getText()));
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
        try {
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
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 重設儲存點
     */
    private void resetSavepoint() {
        try {
            if (StringUtils.isNotBlank(currentFileUrl)) {
                configProp.setProperty(currentFileUrl, "-1");
                try {
                    configProp.store(new FileOutputStream(configFile), "------");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 重設textarea
     */
    private void resetTextArea(String upperAndDownText) {
        try {
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
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    /**
     * 設定字形大小
     */
    private void setupFont() {
        try {
            JFontChooserHelper.showChooser(textArea);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
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

        JPanel panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        JLabel lblNewLabel = new JLabel("開啟編碼");
        panel_3.add(lblNewLabel);

        storyEncodingText = new JTextField();
        storyEncodingText.setText("UTF8");
        panel_3.add(storyEncodingText);
        storyEncodingText.setColumns(10);

        JPanel panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        JPanel panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        storyList = new JList();
        panel.add(JCommonUtil.createScrollComponent(storyList), BorderLayout.CENTER);

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

        JButton btnFont = new JButton("字");
        btnFont.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                setupFont();
            }
        });
        panel_2.add(btnFont);

        JButton btnRec = new JButton("紀");
        btnRec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                savepoint();
            }
        });
        panel_2.add(btnRec);

        JButton btnReset = new JButton("重");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                resetSavepoint();
            }
        });
        panel_2.add(btnReset);

        wheelChangePageChk = new JCheckBox("翻");
        panel_2.add(wheelChangePageChk);

        JScrollPane scrollPanl = JCommonUtil.createScrollComponent(textArea, false, true);
        textArea.setEditable(false);
        JTextAreaUtil.setWrapTextArea(textArea);

        panel_1.add(scrollPanl, BorderLayout.CENTER);

        storyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                fileListClickEvent();
            }
        });

        // 初始化檔案清單
        loadAndSyncConfigProp();

        // 離開前儲存
        exitThisApp();

        configBean.reflectInit(this);

        JCommonUtil.setLocationToRightBottomCorner(this);
    }

    /**
     * 離開前儲存
     */
    private void exitThisApp() {
        try {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    int closeOption = JOptionPane.showConfirmDialog(null, "離開前儲存?");
                    if (closeOption == JOptionPane.YES_OPTION) {
                        savepoint();
                        loadAndSyncConfigProp();
                        configBean.reflectSetConfig(StoryReaderUI.this);
                        configBean.store();
                    }
                    gtu.swing.util.JFrameUtil.setVisible(false, StoryReaderUI.this);
                    StoryReaderUI.this.dispose();
                    System.exit(0);
                }
            });
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Launch the application.
     */
    private static StoryReaderUI FRAME;
    private GlobalKeyListener_StoryReaderUI keyUtil;
    private JCheckBox wheelChangePageChk;

    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(StoryReaderUI.class)) {
            return;
        }

        JnativehookKeyboardMouseHelper.getInstance().disableLogger();
        FRAME = new StoryReaderUI();
        FRAME.keyUtil = FRAME.new GlobalKeyListener_StoryReaderUI();
        FRAME.keyUtil.init();
    }

    private static void startNewUI() {
        synchronized (StoryReaderUI.class) {
            if (FRAME == null) {
                FRAME = new StoryReaderUI();
            }
        }
        if (gtu.swing.util.JFrameUtil.isVisible(FRAME) && FRAME.isFocusOwner()) {
            gtu.swing.util.JFrameUtil.setVisible(false, FRAME);
        } else {
            JCommonUtil.setFrameAtop(FRAME, false);
            JCommonUtil.setLocationToRightBottomCorner(FRAME);
        }
        System.out.println("startNewUI done...");
    }

    private class GlobalNativeMouseWh__StoryReaderUI extends NativeMouseWheelAdapter {
        public void nativeMouseWheelMoved(NativeMouseWheelEvent arg0) {
            if (wheelChangePageChk.isSelected()) {
                if (arg0.getWheelRotation() < 0) {
                    resetTextArea("u");
                } else if (arg0.getWheelRotation() > 0) {
                    resetTextArea("d");
                }
            }
        }
    }

    private class GlobalKeyListener_StoryReaderUI extends NativeKeyAdapter {
        public void close() {
            try {
                org.jnativehook.GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
            System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
            // 模式check
            if ((e.getModifiers() & NativeInputEvent.ALT_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_A) {
                startNewUI();
            } else if (e.getKeyCode() == NativeKeyEvent.VC_UP || e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
                resetTextArea("u");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_DOWN || e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
                resetTextArea("d");
            }
        }

        public void init() {
            try {
                GlobalScreen.registerNativeHook();
                GlobalScreen.addNativeKeyListener(new GlobalKeyListener_StoryReaderUI());
                GlobalScreen.addNativeMouseWheelListener(new GlobalNativeMouseWh__StoryReaderUI());
                JnativehookKeyboardMouseHelper.getInstance().disableLogger();
                startNewUI();
            } catch (NativeHookException ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }
}
