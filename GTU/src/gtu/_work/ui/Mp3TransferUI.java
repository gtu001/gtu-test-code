package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.constant.PatternCollection;
import gtu.file.FileUtil;
import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTextAreaUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class Mp3TransferUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JLabel lblNewLabel;
    private JTextField ffmpegText;
    private JLabel lblNewLabel_1;
    private JTextField targetDirText;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JList ffmpegList;
    private JPanel panel_7;
    private JButton saveConfigBtn;
    private static final File configFile = new File(PropertiesUtil.getJarCurrentPath(Mp3TransferUI.class), Mp3TransferUI.class.getName() + "_config.properties");
    private PropertiesGroupUtils config = new PropertiesGroupUtils(configFile);
    private JButton nextConfigBtn;
    private JButton clearFfmpegListBtn;
    private JButton executeFfmpegBtn;
    private JPanel panel_8;
    private JPanel panel_9;
    private JPanel panel_10;
    private JPanel panel_11;
    private JPanel panel_12;
    private JTextArea logArea;
    private JLabel lblNewLabel_2;
    private JTextField volumnFixText;
    private JLabel lblNewLabel_3;
    private JTextField encodingText;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance(Mp3TransferUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Mp3TransferUI frame = new Mp3TransferUI();
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
    public Mp3TransferUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("基本設定", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        RowSpec.decode("default:grow"), }));

        lblNewLabel = new JLabel("ffmepg");
        panel.add(lblNewLabel, "2, 2, right, default");

        ffmpegText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(ffmpegText, true);
        panel.add(ffmpegText, "4, 2, fill, default");
        ffmpegText.setColumns(10);

        lblNewLabel_2 = new JLabel("音量調整");
        panel.add(lblNewLabel_2, "2, 4, right, default");

        volumnFixText = new JTextField();
        volumnFixText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                swingUtil.invokeAction("volumnFixTextFocus", e);
            }
        });
        volumnFixText.setText("1");
        panel.add(volumnFixText, "4, 4, fill, default");
        volumnFixText.setColumns(10);

        lblNewLabel_1 = new JLabel("目的目錄");
        panel.add(lblNewLabel_1, "2, 6, right, default");

        targetDirText = new JTextField();
        targetDirText.setText(FileUtil.DESKTOP_PATH);
        JCommonUtil.jTextFieldSetFilePathMouseEvent(targetDirText, false);
        panel.add(targetDirText, "4, 6, fill, default");
        targetDirText.setColumns(10);

        lblNewLabel_3 = new JLabel("編碼");
        panel.add(lblNewLabel_3, "2, 8, right, default");

        encodingText = new JTextField();
        panel.add(encodingText, "4, 8, fill, default");
        encodingText.setColumns(10);

        panel_7 = new JPanel();
        panel.add(panel_7, "4, 18, fill, fill");

        saveConfigBtn = new JButton("儲存");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("saveConfigBtn.click", e);
            }
        });
        panel_7.add(saveConfigBtn);

        nextConfigBtn = new JButton("下一組");
        nextConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("nextConfigBtn.click", e);
            }
        });
        panel_7.add(nextConfigBtn);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("影片清單", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.NORTH);

        panel_4 = new JPanel();
        panel_1.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel_1.add(panel_5, BorderLayout.SOUTH);

        clearFfmpegListBtn = new JButton("清除");
        clearFfmpegListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("clearFfmpegListBtn.click", e);
            }
        });
        panel_5.add(clearFfmpegListBtn);

        executeFfmpegBtn = new JButton("執行");
        executeFfmpegBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeFfmpegBtn.click", e);
            }
        });
        panel_5.add(executeFfmpegBtn);

        panel_6 = new JPanel();
        panel_1.add(panel_6, BorderLayout.EAST);

        ffmpegList = new JList();
        ffmpegList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                JListUtil.newInstance(ffmpegList).defaultJListKeyPressed(e);
            }
        });
        panel_1.add(JCommonUtil.createScrollComponent(ffmpegList), BorderLayout.CENTER);

        panel_8 = new JPanel();
        tabbedPane.addTab("Log", null, panel_8, null);
        panel_8.setLayout(new BorderLayout(0, 0));

        panel_9 = new JPanel();
        panel_8.add(panel_9, BorderLayout.NORTH);

        panel_10 = new JPanel();
        panel_8.add(panel_10, BorderLayout.WEST);

        panel_11 = new JPanel();
        panel_8.add(panel_11, BorderLayout.EAST);

        panel_12 = new JPanel();
        panel_8.add(panel_12, BorderLayout.SOUTH);

        logArea = new JTextArea();
        panel_8.add(JCommonUtil.createScrollComponent(logArea), BorderLayout.CENTER);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            // 掛載所有event
            applyAllEvents();

            JCommonUtil.setJFrameCenter(this);
            JCommonUtil.setJFrameIcon(this, "resource/images/ico/tk_aiengine.ico");
            hideInSystemTrayHelper = HideInSystemTrayHelper.newInstance();
            hideInSystemTrayHelper.apply(this);
            jFrameRGBColorPanel = new JFrameRGBColorPanel(this);
            panel_2.add(jFrameRGBColorPanel.getToggleButton(false));
            panel_2.add(hideInSystemTrayHelper.getToggleButton(false));
            this.applyAppMenu();
            JCommonUtil.defaultToolTipDelay();
            this.setTitle("You Set My World On Fire");
        }
    }

    private enum ActionDefine {
        TEST_DEFAULT_EVENT, //
        JTabbedPane_ChangeIndex, //
        ;
    }

    private void applyAllEvents() {
        swingUtil.addActionHex(ActionDefine.TEST_DEFAULT_EVENT.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("====Test Default Event!!====");
            }
        });
        swingUtil.addActionHex(ActionDefine.JTabbedPane_ChangeIndex.name(), new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                System.out.println("tabbedPane : " + tabbedPane.getSelectedIndex());
            }
        });
        JCommonUtil.applyDropFiles(this, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> lst = new ArrayList<File>();
                List<File> droppedFiles = (List<File>) e.getSource();
                for (File f : droppedFiles) {
                    FileUtil.searchFileMatchs(f, ".*\\." + PatternCollection.VIDEO_PATTERN, lst);
                }
                DefaultListModel model = JListUtil.createModel();
                for (File f : lst) {
                    model.addElement(new ZFile(f));
                }
                ffmpegList.setModel(model);
            }
        });
        swingUtil.addActionHex("saveConfigBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ffmepg", ffmpegText.getText());
                map.put("encoding", encodingText.getText());
                config.saveConfig(map);
            }
        });
        swingUtil.addActionHex("nextConfigBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.next();
                Map<String, String> map = config.loadConfig();
                ffmpegText.setText(map.get("ffmepg"));
                encodingText.setText(map.get("encoding"));
            }
        });
        swingUtil.addActionHex("clearFfmpegListBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                ffmpegList.setModel(JListUtil.createModel());
            }
        });
        swingUtil.addActionHex("executeFfmpegBtn.click", new Action() {

            private String getEncoding() {
                if (StringUtils.isBlank(encodingText.getText())) {
                    encodingText.setText("UTF8");
                    return "UTF8";
                }
                return StringUtils.trimToEmpty(encodingText.getText());
            }

            private void run_inner(final ZFile aviFile, final File targetDir, final PrintStream outStream) {
                RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance();
                inst.runInBatFile(true);
                File newMp3File = FileUtil.getNewSubName(new File(targetDir, aviFile.file.getName()), "mp3");
                String command = String.format("\"%1$s\" -i \"%2$s\" -b:a 192K -vn -af \"volume=%4$s\" \"%3$s\"", //
                        StringUtils.trimToEmpty(ffmpegText.getText()), //
                        StringUtils.trimToEmpty(aviFile.file.getAbsolutePath()), //
                        newMp3File.getAbsolutePath(), //
                        getVolumnFixTextValue() //
                );
                inst.command(command);
                aviFile.isStart = true;
                JCommonUtil.updateUI(ffmpegList, logArea);
                ProcessWatcher watcher = ProcessWatcher.newInstance(inst.apply(getEncoding()));
                watcher.getStreamAndPrintStream(outStream, outStream, false);
                aviFile.isDone = true;
                JCommonUtil.updateUI(ffmpegList, logArea);
            }

            @Override
            public void action(EventObject evt) throws Exception {
                final File targetDir = new File(StringUtils.trimToEmpty(targetDirText.getText()));
                Validate.isTrue(targetDir.isDirectory(), "目的目錄不存在");

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PrintStream outStream = JTextAreaUtil.getNewPrintStream2JTextArea(logArea);
                        DefaultListModel model = (DefaultListModel) ffmpegList.getModel();
                        for (int ii = 0; ii < model.getSize(); ii++) {
                            ZFile zfile = (ZFile) model.getElementAt(ii);
                            run_inner(zfile, targetDir, outStream);
                        }
                        JCommonUtil._jOptionPane_showMessageDialog_info("完成");
                    }
                });
                t.start();
            }
        });
        swingUtil.addActionHex("volumnFixTextFocus", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                try {
                    double d = Double.parseDouble(volumnFixText.getText());
                    if (d <= 0) {
                        throw new Exception("error");
                    }
                } catch (Exception ex) {
                    volumnFixText.setText("1");
                }
            }
        });
    }

    private Double getVolumnFixTextValue() {
        try {
            double d = Double.parseDouble(volumnFixText.getText());
            if (d <= 0) {
                throw new Exception("error");
            }
            return d;
        } catch (Exception ex) {
            return 1d;
        }
    }

    private class ZFile {
        File file;
        boolean isStart = false;
        boolean isDone = false;

        public ZFile(File file) {
            this.file = file;
        }

        public String toString() {
            String color = "blue";
            String msg = "waiting";
            if (isStart) {
                color = "green";
                msg = "starting";
            }
            if (isDone) {
                color = "gray";
                msg = "Done";
            }
            return String.format("<html><font color='%s'>%s</font>%s</html>", //
                    color, msg, file.getName());
        }
    }

    private void applyAppMenu() {
        JMenu menu1 = JMenuAppender.newInstance("child_item")//
                .addMenuItem("detail1", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .getMenu();
        JMenu mainMenu = JMenuAppender.newInstance("file")//
                .addMenuItem("item1", null)//
                .addMenuItem("item2", (ActionListener) ActionAdapter.ActionListener.create(ActionDefine.TEST_DEFAULT_EVENT.name(), getSwingUtil()))//
                .addChildrenMenu(menu1)//
                .getMenu();
        JMenuBarUtil.newInstance().addMenu(mainMenu).apply(this);
    }

    public SwingActionUtil getSwingUtil() {
        return swingUtil;
    }
}
