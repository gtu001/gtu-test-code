package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.file.FileUtil;
import gtu.image.ScreenshotUtil;
import gtu.image.ScreenshotUtil.PicType;
import gtu.swing.util.JButtonGroupUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;

public class ScreenshotUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper sysutil;
    private boolean isOn = false;
    private ScreenshotUtil screenshot;
    private JTextField picDestDir;
    private JComboBox picComboBox;
    private ThisConfig testConfig;
    private ThisConfig tmpTestConfig;
    private ButtonGroup btnGroup;
    private JTextField hotKeyText;
    private JTextField modifierText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ScreenshotUI frame = new ScreenshotUI();
                    frame.setVisible(false);
                    TestKeyListener keyListener = frame.new TestKeyListener();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showOnOffMessage() {
        sysutil.displayMessage("截圖工具", isOn ? "已啟動" : "已暫停", MessageType.INFO);
    }

    /**
     * Create the frame.
     */
    public ScreenshotUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("config", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), }));

        JLabel label = new JLabel("圖檔位置");
        panel.add(label, "2, 2, right, default");

        picDestDir = new JTextField();
        picDestDir.setText(getDefaultDir().getAbsolutePath());
        JCommonUtil.jTextFieldSetFilePathMouseEvent(picDestDir, true);
        panel.add(picDestDir, "4, 2, fill, default");
        picDestDir.setColumns(10);

        JLabel label_1 = new JLabel("圖檔類型");
        panel.add(label_1, "2, 4, right, default");

        picComboBox = new JComboBox();
        panel.add(picComboBox, "4, 4, fill, default");
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("JPG");
        model.addElement("BMP");
        picComboBox.setModel(model);
        picComboBox.setSelectedIndex(0);

        JLabel lblNewLabel = new JLabel("快速鍵");
        panel.add(lblNewLabel, "2, 6, right, default");

        JPanel panel_3 = new JPanel();
        panel.add(panel_3, "4, 6, fill, fill");

        hotKeyText = new JTextField();
        panel_3.add(hotKeyText);
        hotKeyText.setColumns(10);

        modifierText = new JTextField();
        modifierText.setColumns(10);
        panel_3.add(modifierText);

        JLabel label_2 = new JLabel("成功訊息");
        panel.add(label_2, "2, 8");

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, "4, 8, fill, fill");

        JRadioButton beepRadioBtn = new JRadioButton("beep");
        beepRadioBtn.setSelected(true);
        panel_2.add(beepRadioBtn);

        JRadioButton messageRadioBtn = new JRadioButton("message");
        panel_2.add(messageRadioBtn);

        btnGroup = JButtonGroupUtil.createRadioButtonGroup(beepRadioBtn, messageRadioBtn);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, "4, 16, fill, fill");

        JButton confirmChangeBtn = new JButton("確定");
        confirmChangeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                testConfig = getCurrentNewConfig();
                initScreenshot(testConfig);
            }
        });
        panel_1.add(confirmChangeBtn);

        JButton cancelConfigBtn = new JButton("取消");
        cancelConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tmpTestConfig == null) {
                    tmpTestConfig = getDefaultConfig();
                }
                testConfig = tmpTestConfig;
                initScreenshot(testConfig);
            }
        });
        panel_1.add(cancelConfigBtn);

        sysutil = HideInSystemTrayHelper.newInstance();
        MenuItem item = new MenuItem("start/pause");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOn = !isOn;
                showOnOffMessage();
            }
        });
        sysutil.getItems().add(item);
        sysutil.apply(this, "截圖工具", "resource/images/ico/screenshot.ico");

        // 初始化抓圖工具
        testConfig = getDefaultConfig();
        initScreenshot(testConfig);

        isOn = true;
        showOnOffMessage();
    }

    private File getDefaultDir() {
        return new File(FileUtil.DESKTOP_PATH, "screenshot_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd"));
    }

    private ThisConfig getDefaultConfig() {
        ThisConfig testConfig = new ThisConfig();
        testConfig.destDir = getDefaultDir().getAbsolutePath();
        testConfig.picType = "JPG";
        testConfig.listenKeyCode = NativeKeyEvent.VC_F12;
        testConfig.listenModifier = 0;
        return testConfig;
    }

    private ThisConfig getCurrentNewConfig() {
        ThisConfig testConfig = new ThisConfig();
        testConfig.destDir = new File(picDestDir.getText()).getAbsolutePath();
        testConfig.picType = String.valueOf(picComboBox.getSelectedItem());
        testConfig.listenKeyCode = Integer.parseInt(hotKeyText.getText());
        testConfig.listenModifier = Integer.parseInt(StringUtils.defaultIfBlank(modifierText.getText(), "0"));
        return testConfig;
    }

    private void applyDefaultScreenshot(ScreenshotUtil screenshot) {
        screenshot.setSaveSuccessAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File bmpFile = (File) e.getSource();
                String text = ((JRadioButton) JButtonGroupUtil.getSelectedButton(btnGroup)).getText();
                if ("beep".equalsIgnoreCase(text)) {
                    Toolkit.getDefaultToolkit().beep();
                } else if ("message".equalsIgnoreCase(text)) {
                    sysutil.displayMessage("產生截圖", bmpFile.getName(), MessageType.INFO);
                }
            }
        });
        screenshot.setSaveFailedAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] args = (Object[]) e.getSource();
                Exception ex = (Exception) args[0];
                JCommonUtil.handleException(ReflectionToStringBuilder.toString(args[1], ToStringStyle.MULTI_LINE_STYLE), ex);
            }
        });
    }

    private void initScreenshot(ThisConfig config) {
        screenshot = new ScreenshotUtil(null, "HHmmss", new File(config.destDir));
        screenshot.setPicType(PicType.valueOf(config.picType));
        applyDefaultScreenshot(screenshot);
        // update UI
        picDestDir.setText(testConfig.destDir);
        picComboBox.setSelectedItem(testConfig.picType);
        this.setTitle(NativeKeyEvent.getKeyText(testConfig.listenKeyCode));
    }

    private class ThisConfig {
        String picType;
        String destDir;
        Integer listenKeyCode;
        public int listenModifier;
    }

    private class TestKeyListener implements NativeKeyListener {

        TestKeyListener() {
            initialize();
        }

        private void initialize() {
            try {
                if (!GlobalScreen.isNativeHookRegistered()) {
                    GlobalScreen.registerNativeHook();
                }
            } catch (NativeHookException e) {
                JCommonUtil.handleException(e);
                throw new RuntimeException(e);
            }
            GlobalScreen.addNativeKeyListener(this);
        }

        public void close() {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.removeNativeKeyListener(this);
            }
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent paramNativeKeyEvent) {
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            if (hotKeyText.isFocusOwner() || modifierText.isFocusOwner()) {
                hotKeyText.setText(String.valueOf(e.getKeyCode()));
                modifierText.setText(String.valueOf(e.getModifiers()));
            } else if (e.getKeyCode() == testConfig.listenKeyCode && //
                    ((testConfig.listenModifier == 0) || //
                            (testConfig.listenModifier != 0 && e.getModifiers() == testConfig.listenModifier))//
            ) {//
                if (isOn) {
                    screenshot.prtsc();
                }
            }
        }
    }
}
