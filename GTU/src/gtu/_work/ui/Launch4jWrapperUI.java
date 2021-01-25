package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class Launch4jWrapperUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JPanel panel_3;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JTextField jarText;
    private JTextField iconText;
    private JPanel panel_8;
    private JButton executeBtn;
    private JTextField launch4jExeText;
    private JLabel lblNewLabel_2;
    private JButton saveConfigBtn;
    private PropertiesUtilBean config = new PropertiesUtilBean(Launch4jWrapperUI.class);

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(Launch4jWrapperUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Launch4jWrapperUI frame = new Launch4jWrapperUI();
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
    public Launch4jWrapperUI() {
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
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        panel_3 = new JPanel();
        panel.add(panel_3, BorderLayout.NORTH);

        panel_4 = new JPanel();
        panel.add(panel_4, BorderLayout.WEST);

        panel_5 = new JPanel();
        panel.add(panel_5, BorderLayout.EAST);

        panel_6 = new JPanel();
        panel.add(panel_6, BorderLayout.SOUTH);

        panel_7 = new JPanel();
        panel.add(panel_7, BorderLayout.CENTER);
        panel_7.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"), }));

        lblNewLabel = new JLabel("JAR");
        panel_7.add(lblNewLabel, "2, 2, right, default");

        jarText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(jarText, false);
        panel_7.add(jarText, "4, 2, fill, default");
        jarText.setColumns(10);

        lblNewLabel_1 = new JLabel("ICON");
        panel_7.add(lblNewLabel_1, "2, 4, right, default");

        iconText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(iconText, false);
        panel_7.add(iconText, "4, 4, fill, default");
        iconText.setColumns(10);

        lblNewLabel_2 = new JLabel("launch4j exe");
        panel_7.add(lblNewLabel_2, "2, 6, right, default");

        launch4jExeText = new JTextField();
        JCommonUtil.jTextFieldSetFilePathMouseEvent(launch4jExeText, false);
        panel_7.add(launch4jExeText, "4, 6, fill, default");
        launch4jExeText.setColumns(10);

        panel_8 = new JPanel();
        panel_7.add(panel_8, "4, 10, fill, fill");

        saveConfigBtn = new JButton("儲存設定");
        saveConfigBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("saveConfigBtn.click", e);
            }
        });
        panel_8.add(saveConfigBtn);

        executeBtn = new JButton("製作");
        executeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("executeBtn.click", e);
            }
        });
        panel_8.add(executeBtn);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        {
            config.reflectInit(this);

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
        swingUtil.addActionHex("saveConfigBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.reflectSetConfig(Launch4jWrapperUI.this);
                config.store();
            }
        });
        swingUtil.addActionHex("executeBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                String jarPath = JCommonUtil.isBlankErrorMsg(jarText, "jarPath");
                String iconPath = JCommonUtil.isBlankErrorMsg(iconText, "iconPath");
                String launch4jPath = JCommonUtil.isBlankErrorMsg(launch4jExeText, "launch4jPath");

                String exeName = FileUtil.getNameNoSubName(new File(jarPath).getName()) + ".exe";
                File exePathFile = new File(FileUtil.DESKTOP_DIR, exeName);

                Map<String, Object> root = new HashMap<String, Object>();
                root.put("jarPath", jarPath);
                root.put("exePath", exePathFile.getAbsolutePath());
                root.put("iconPath", iconPath);
                root.put("singletonName", "BrowserHistoryHandlerUI");

                URL url = ClassLoader.getSystemResource("gtu/ant/AntJarToExe_Launch4j_config.xml");

                System.out.println("url 0-00 " + url);
                File tempFile = File.createTempFile("antJarToExe_", ".xml");
                FreeMarkerSimpleUtil.replace(tempFile, url, root);
                System.out.println("DEST - " + tempFile);

                Process process = RuntimeBatPromptModeUtil.newInstance().command(String.format("java -jar \"%s\" \"%s\"", launch4jPath, tempFile)).apply();
                ProcessWatcher p = ProcessWatcher.newInstance(process);
                p.getStreamSync();
                System.out.println(p.getErrorStreamToString());
                System.out.println(p.getInputStreamToString());

                System.out.println("done...");
            }
        });
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
