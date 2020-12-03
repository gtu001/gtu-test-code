package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseAdapter;
import org.jnativehook.mouse.NativeMouseEvent;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu._work.ui.JMenuBarUtil.JMenuAppender;
import gtu.keyboard_mouse.JnativehookKeyboardMouseHelper;
import gtu.number.RandomUtil;
import gtu.properties.PropertiesUtilBean;
import gtu.runtime.DesktopUtil;
import gtu.runtime.RuntimeBatPromptModeUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JMouseEventUtil;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.SwingActionUtil;
import gtu.swing.util.SwingActionUtil.Action;
import gtu.swing.util.SwingActionUtil.ActionAdapter;

public class DMMVRPlayerHotKeyUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private HideInSystemTrayHelper hideInSystemTrayHelper;
    private JFrameRGBColorPanel jFrameRGBColorPanel;
    private SwingActionUtil swingUtil;
    private JTabbedPane tabbedPane;
    private JPanel panel_2;
    private JLabel lblNewLabel;
    private JPanel panel_3;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel lblNewLabel_3;
    private JLabel lblNewLabel_4;
    private JPanel panel_4;
    private JPanel panel_5;
    private JPanel panel_6;
    private JPanel panel_7;
    private JButton previous2Btn;
    private JButton previous1Btn;
    private JButton next1Btn;
    private JButton next2Btn;
    private JButton playBtn;
    private JTextField previous2Text;
    private JTextField previous1Text;
    private JTextField next1Text;
    private JTextField next2Text;
    private JTextField playText;
    private GlobalKeyListenerExampleForEnglishUI keyUtil;
    private JLabel lblContinuec;
    private JPanel panel_8;
    private JTextField continueText;
    private JButton continueBtn;
    private JLabel lblEnable;
    private JPanel panel_9;
    private JToggleButton enableToggleBtn;
    private JLabel lblNewLabel_5;
    private JPanel panel_10;
    private JLabel label_1;
    private JLabel lblNewLabel_6;
    private JLabel lblNewLabel_7;
    private JLabel lblNewLabel_8;
    private JTextField _1text;
    private JButton _1Btn;
    private JPanel panel_11;
    private JTextField _2text;
    private JButton _2Btn;
    private JPanel panel_12;
    private JTextField _3text;
    private JButton _3Btn;
    private JPanel panel_13;
    private JTextField _4text;
    private JButton _4Btn;
    private JPanel panel_14;
    private JTextField _5text;
    private JButton _5Btn;

    private static AtomicReference<ActionListener> START_LISTENER = new AtomicReference<ActionListener>();
    private PropertiesUtilBean config = new PropertiesUtilBean(DMMVRPlayerHotKeyUI.class);
    private JPanel panel_15;
    private JPanel panel_16;
    private JPanel panel_17;
    private JPanel panel_18;
    private JPanel panel_19;
    private JList dmmList;
    private JLabel lblNewLabel_9;
    private JTextField dmmPlayerText;
    private JButton dmmPlayerSetBtn;
    private AtomicReference<DMMFile> currentFile = new AtomicReference<DMMFile>();
    private JButton dmmLstResetBtn;
    private JButton replayBtn;
    private JButton infoBtn;

    private static final String SUBNAME = ".wsdcf";
    private static final String SUBNAME1 = "wsdcf";
    private JPanel panel_20;
    private JPanel panel_21;
    private JPanel panel_22;
    private JPanel panel_23;
    private JPanel panel_24;
    private JList fixList;
    private FixFileProcess mFixFileProcess;
    private JButton fixFileApplyAllBtn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        if (!JFrameUtil.lockInstance_delable(DMMVRPlayerHotKeyUI.class)) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DMMVRPlayerHotKeyUI frame = new DMMVRPlayerHotKeyUI();
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);

                    frame.keyUtil = frame.new GlobalKeyListenerExampleForEnglishUI();
                    frame.keyUtil.init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DMMVRPlayerHotKeyUI() {
        swingUtil = SwingActionUtil.newInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 490, 382);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addChangeListener((ChangeListener) ActionAdapter.ChangeListener.create(ActionDefine.JTabbedPane_ChangeIndex.name(), swingUtil));
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("快進退", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        lblEnable = new JLabel("ENABLE");
        panel.add(lblEnable, "2, 2");

        panel_9 = new JPanel();
        panel.add(panel_9, "4, 2, fill, fill");

        enableToggleBtn = new JToggleButton("啟用");
        enableToggleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String label = enableToggleBtn.isSelected() ? "啟用" : "取消";
                enableToggleBtn.setText(label);
            }
        });
        panel_9.add(enableToggleBtn);

        infoBtn = new JButton("info");
        infoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("infoBtn.click", e);
            }
        });
        panel_9.add(infoBtn);

        lblNewLabel = new JLabel("<<(ctl+left)");
        panel.add(lblNewLabel, "2, 4");

        panel_3 = new JPanel();
        panel.add(panel_3, "4, 4, fill, fill");

        previous2Btn = new JButton("set");
        previous2Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                swingUtil.invokeAction("previous2Btn.click", arg0);
            }
        });

        previous2Text = new JTextField();
        panel_3.add(previous2Text);
        previous2Text.setColumns(10);
        panel_3.add(previous2Btn);

        lblNewLabel_1 = new JLabel("<(left)");
        panel.add(lblNewLabel_1, "2, 6");

        panel_7 = new JPanel();
        panel.add(panel_7, "4, 6, fill, fill");

        previous1Btn = new JButton("set");
        previous1Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("previous1Btn.click", e);
            }
        });

        previous1Text = new JTextField();
        previous1Text.setColumns(10);
        panel_7.add(previous1Text);
        panel_7.add(previous1Btn);

        lblNewLabel_2 = new JLabel(">(right)");
        panel.add(lblNewLabel_2, "2, 8");

        panel_6 = new JPanel();
        panel.add(panel_6, "4, 8, fill, fill");

        next1Btn = new JButton("set");
        next1Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("next1Btn.click", e);
            }
        });

        next1Text = new JTextField();
        next1Text.setColumns(10);
        panel_6.add(next1Text);
        panel_6.add(next1Btn);

        lblNewLabel_3 = new JLabel(">>(ctl+right)");
        panel.add(lblNewLabel_3, "2, 10");

        panel_5 = new JPanel();
        panel.add(panel_5, "4, 10, fill, fill");

        next2Btn = new JButton("set");
        next2Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("next2Btn.click", e);
            }
        });

        next2Text = new JTextField();
        next2Text.setColumns(10);
        panel_5.add(next2Text);
        panel_5.add(next2Btn);

        lblNewLabel_4 = new JLabel("pause/play(space)");
        panel.add(lblNewLabel_4, "2, 12");

        panel_4 = new JPanel();
        panel.add(panel_4, "4, 12, fill, fill");

        playBtn = new JButton("set");
        playBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("playBtn.click", e);
            }
        });

        playText = new JTextField();
        playText.setColumns(10);
        panel_4.add(playText);
        panel_4.add(playBtn);

        lblContinuec = new JLabel("continue(c)");
        panel.add(lblContinuec, "2, 14");

        panel_8 = new JPanel();
        panel.add(panel_8, "4, 14, fill, fill");

        continueText = new JTextField();
        continueText.setColumns(10);
        panel_8.add(continueText);

        continueBtn = new JButton("set");
        continueBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("continueBtn.click", e);
            }
        });
        panel_8.add(continueBtn);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("百分比位置", null, panel_1, null);
        panel_1.setLayout(new FormLayout(
                new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC,
                        ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        lblNewLabel_5 = new JLabel("1");
        panel_1.add(lblNewLabel_5, "2, 2");

        panel_10 = new JPanel();
        panel_1.add(panel_10, "6, 2, fill, fill");

        _1text = new JTextField();
        panel_10.add(_1text);
        _1text.setColumns(10);

        _1Btn = new JButton("set");
        _1Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("_1Btn.click", e);
            }
        });
        panel_10.add(_1Btn);

        label_1 = new JLabel("2");
        panel_1.add(label_1, "2, 4");

        panel_11 = new JPanel();
        panel_1.add(panel_11, "6, 4, fill, fill");

        _2text = new JTextField();
        _2text.setColumns(10);
        panel_11.add(_2text);

        _2Btn = new JButton("set");
        _2Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("_2Btn.click", e);
            }
        });
        panel_11.add(_2Btn);

        lblNewLabel_6 = new JLabel("3");
        panel_1.add(lblNewLabel_6, "2, 6");

        panel_12 = new JPanel();
        panel_1.add(panel_12, "6, 6, fill, fill");

        _3text = new JTextField();
        _3text.setColumns(10);
        panel_12.add(_3text);

        _3Btn = new JButton("set");
        _3Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("_3Btn.click", e);
            }
        });
        panel_12.add(_3Btn);

        lblNewLabel_7 = new JLabel("4");
        panel_1.add(lblNewLabel_7, "2, 8");

        panel_13 = new JPanel();
        panel_1.add(panel_13, "6, 8, fill, fill");

        _4text = new JTextField();
        _4text.setColumns(10);
        panel_13.add(_4text);

        _4Btn = new JButton("set");
        _4Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("_4Btn.click", e);
            }
        });
        panel_13.add(_4Btn);

        lblNewLabel_8 = new JLabel("5");
        panel_1.add(lblNewLabel_8, "2, 10");

        panel_14 = new JPanel();
        panel_1.add(panel_14, "6, 10, fill, fill");

        _5text = new JTextField();
        _5text.setColumns(10);
        panel_14.add(_5text);

        _5Btn = new JButton("set");
        _5Btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("_5Btn.click", e);
            }
        });
        panel_14.add(_5Btn);

        panel_15 = new JPanel();
        tabbedPane.addTab("播放清單", null, panel_15, null);
        panel_15.setLayout(new BorderLayout(0, 0));

        panel_16 = new JPanel();
        panel_15.add(panel_16, BorderLayout.NORTH);

        lblNewLabel_9 = new JLabel("DMMPlayer");
        panel_16.add(lblNewLabel_9);

        dmmPlayerText = new JTextField();
        panel_16.add(dmmPlayerText);
        dmmPlayerText.setColumns(10);

        dmmPlayerSetBtn = new JButton("set");
        dmmPlayerSetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("dmmPlayerSetBtn.click", e);
            }
        });
        panel_16.add(dmmPlayerSetBtn);

        dmmLstResetBtn = new JButton("reset");
        dmmLstResetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("dmmLstResetBtn.click", e);
            }
        });
        panel_16.add(dmmLstResetBtn);

        replayBtn = new JButton("replay");
        replayBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("replayBtn.click", e);
            }
        });
        panel_16.add(replayBtn);

        panel_17 = new JPanel();
        panel_15.add(panel_17, BorderLayout.WEST);

        panel_18 = new JPanel();
        panel_15.add(panel_18, BorderLayout.EAST);

        panel_19 = new JPanel();
        panel_15.add(panel_19, BorderLayout.SOUTH);

        dmmList = new JList();
        dmmList.setModel(new DefaultListModel());
        dmmList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("dmmList.mouseClick", e);
            }
        });
        dmmList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                swingUtil.invokeAction("dmmList.keyPress", e);
            }
        });
        panel_15.add(JCommonUtil.createScrollComponent(dmmList), BorderLayout.CENTER);

        panel_20 = new JPanel();
        tabbedPane.addTab("修改組列", null, panel_20, null);
        panel_20.setLayout(new BorderLayout(0, 0));

        panel_21 = new JPanel();
        panel_20.add(panel_21, BorderLayout.NORTH);

        fixFileApplyAllBtn = new JButton("全部執行");
        fixFileApplyAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                swingUtil.invokeAction("fixFileApplyAllBtn.click", e);
            }
        });
        panel_21.add(fixFileApplyAllBtn);

        panel_22 = new JPanel();
        panel_20.add(panel_22, BorderLayout.WEST);

        panel_23 = new JPanel();
        panel_20.add(panel_23, BorderLayout.EAST);

        panel_24 = new JPanel();
        panel_20.add(panel_24, BorderLayout.SOUTH);

        fixList = new JList();
        fixList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                swingUtil.invokeAction("fixList.click", e);
            }
        });
        fixList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                swingUtil.invokeAction("fixList.keyPress", e);
            }
        });
        fixList.setModel(new DefaultListModel());
        panel_20.add(JCommonUtil.createScrollComponent(fixList), BorderLayout.CENTER);

        panel_2 = new JPanel();
        tabbedPane.addTab("其他設定", null, panel_2, null);
        panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JCommonUtil.applyDropFiles(dmmList, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                dmmListAppendDMMFiles(files);
            }
        });
        {
            mFixFileProcess = new FixFileProcess();

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

            config.reflectInit(this);
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
                tabbedPane.requestFocus();
            }
        });
        swingUtil.addActionHex("previous1Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(previous1Text);
            }
        });
        swingUtil.addActionHex("previous2Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(previous2Text);
            }
        });
        swingUtil.addActionHex("next1Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(next1Text);
            }
        });
        swingUtil.addActionHex("next2Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(next2Text);
            }
        });
        swingUtil.addActionHex("playBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(playText);
            }
        });
        swingUtil.addActionHex("continueBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(continueText);
            }
        });
        swingUtil.addActionHex("_1Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(_1text);
            }
        });
        swingUtil.addActionHex("_2Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(_2text);
            }
        });
        swingUtil.addActionHex("_3Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(_3text);
            }
        });
        swingUtil.addActionHex("_4Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(_4text);
            }
        });
        swingUtil.addActionHex("_5Btn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                locatePosition(_5text);
            }
        });
        swingUtil.addActionHex("dmmList.mouseClick", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                dmmListMouseClick(evt);
            }
        });
        swingUtil.addActionHex("dmmList.keyPress", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                JListUtil.newInstance(dmmList).defaultJListKeyPressed(evt);
            }
        });
        swingUtil.addActionHex("dmmPlayerSetBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                config.reflectSetConfig(DMMVRPlayerHotKeyUI.this);
                config.store();
            }
        });
        swingUtil.addActionHex("dmmLstResetBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                dmmList.setModel(new DefaultListModel());
            }
        });
        swingUtil.addActionHex("replayBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                replayBtnClick();
            }
        });
        swingUtil.addActionHex("infoBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                JCommonUtil._jOptionPane_showMessageDialog_info("R隨機\nN下一部\nT重播");
            }
        });
        swingUtil.addActionHex("fixList.keyPress", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                JListUtil.newInstance(fixList).defaultJListKeyPressed(evt);
            }
        });
        swingUtil.addActionHex("fixList.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                if (JMouseEventUtil.buttonLeftClick(2, evt)) {
                    FixFile file = (FixFile) fixList.getSelectedValue();
                    if (file != null) {
                        mFixFileProcess.processFixFile(file);
                    }
                }
            }
        });
        swingUtil.addActionHex("fixFileApplyAllBtn.click", new Action() {
            @Override
            public void action(EventObject evt) throws Exception {
                DefaultListModel model = (DefaultListModel) fixList.getModel();
                for (int ii = 0; ii < model.getSize(); ii++) {
                    FixFile file = (FixFile) model.getElementAt(ii);
                    if (file != null) {
                        mFixFileProcess.processFixFile(file);
                    }
                }
            }
        });
    }

    private void dmmListAppendDMMFiles(List<File> files) {
        List<DMMFile> files2 = new ArrayList<DMMFile>();
        for (File f : files) {
            if (f.getName().toLowerCase().endsWith(SUBNAME)) {
                files2.add(new DMMFile(f));
            }
        }
        DefaultListModel model = (DefaultListModel) dmmList.getModel();
        for (DMMFile f2 : files2) {
            boolean findOk = false;
            A: for (int ii = 0; ii < model.getSize(); ii++) {
                DMMFile f = (DMMFile) model.getElementAt(ii);
                if (StringUtils.equals(f.path, f2.path)) {
                    findOk = true;
                    break A;
                }
            }
            if (!findOk) {
                model.addElement(f2);
            }
        }
    }

    private void randomDMMPlayerPlay() {
        List<DMMFile> randomLst = new ArrayList<DMMFile>();
        DefaultListModel model = (DefaultListModel) dmmList.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            DMMFile f = (DMMFile) model.getElementAt(ii);
            if (!f.isPlayed) {
                randomLst.add(f);
            }
        }
        if (randomLst.isEmpty()) {
            JCommonUtil._jOptionPane_showInputDialog("已撥放完所有內容!!");
            return;
        }
        DMMFile file = RandomUtil.pickOne(randomLst);
        playFile(file);
    }

    private void dmmListMouseClick(EventObject evt) {
        if (JMouseEventUtil.buttonLeftClick(2, evt)) {
            DMMFile file = (DMMFile) dmmList.getSelectedValue();
            playFile(file);
        }
        if (JMouseEventUtil.buttonRightClick(1, evt)) {
            final DMMFile file = (DMMFile) dmmList.getSelectedValue();
            JPopupMenuUtil.newInstance(dmmList)//
                    .addJMenuItem("改名", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (file.file.exists()) {
                                String name = file.file.getName();
                                String name1 = StringUtils.trimToEmpty(name).replaceAll("\\." + SUBNAME1 + "$", "");
                                name1 = JCommonUtil._jOptionPane_showInputDialog("修改檔名", name1);
                                String name2 = name1 + SUBNAME;
                                if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("確定是否改為:" + name2, "是否改名")) {
                                    if (!StringUtils.equals(name, name2)) {
                                        FixFile newFile = new FixFile(file);
                                        newFile.renameToName = name2;
                                        mFixFileProcess.addQueueWork(newFile);
                                    }
                                }
                            }
                        }
                    })//
                    .addJMenuItem("刪除", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (file.file.exists()) {
                                boolean delConfirm = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否刪除檔案 : " + file.name, "DEL");
                                if (delConfirm) {
                                    FixFile newFile = new FixFile(file);
                                    newFile.delFile = true;
                                    mFixFileProcess.addQueueWork(newFile);
                                }
                            }
                        }
                    })//
                    .addJMenuItem("開啟目錄", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (file.file.exists()) {
                                DesktopUtil.browseFileDirectory(file.file);
                            }
                        }
                    })//
                    .applyEvent(evt).show();
        }
    }

    private void replayBtnClick() {
        playFile(currentFile.get());
    }

    private void playFile(DMMFile file) {
        if (file == null) {
            System.out.println("檔案為null");
            setTitle("檔案為null");
            return;
        }
        if (!file.file.exists()) {
            System.out.println("檔案位置移動 : " + file.path);
            setTitle("檔案位置移動 : " + file.path);
            return;
        }
        file.isPlayed = true;
        setTitle(file.name);
        String player = dmmPlayerText.getText();
        if (StringUtils.isBlank(player)) {
            try {
                Desktop.getDesktop().open(file.file);
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        } else {
            try {
                RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance();
                String command = String.format(" \"%s\" \"%s\" ", player, file.path);
                inst.command(command);
                inst.apply();
            } catch (Exception e) {
                JCommonUtil.handleException(e);
            }
        }
        dmmList.repaint();
    }

    private void playNextDMMFile() {
        DefaultListModel model = (DefaultListModel) dmmList.getModel();
        for (int ii = 0; ii < model.getSize(); ii++) {
            DMMFile f = (DMMFile) model.getElementAt(ii);
            if (currentFile.get() == null) {
                currentFile.set(f);
                break;
            }
            if (StringUtils.equalsIgnoreCase(currentFile.get().path, f.path)) {
                if (ii + 1 <= model.getSize() - 1) {
                    DMMFile f2 = (DMMFile) model.getElementAt(ii + 1);
                    currentFile.set(f2);
                    break;
                } else {
                    DMMFile f2 = (DMMFile) model.getElementAt(0);
                    currentFile.set(f2);
                    break;
                }
            }
        }
        DMMFile file = currentFile.get();
        playFile(file);
    }

    private void locatePosition(final JTextField text) {
        START_LISTENER.set(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strVal = (String) e.getSource();
                text.setText(strVal);
                config.reflectSetConfig(DMMVRPlayerHotKeyUI.this);
                config.store();
            }
        });
    }

    private Pair<Integer, Integer> getPosition(JTextField text) {
        try {
            String strVal = StringUtils.trimToEmpty(text.getText());
            Pattern ptn = Pattern.compile("(\\d+)x(\\d+)");
            Matcher mth = ptn.matcher(strVal);
            if (mth.find()) {
                return Pair.of(Integer.parseInt(mth.group(1)), Integer.parseInt(mth.group(2)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void pressDMMVRPlayerBtn(String input) {
        Pair<Integer, Integer> pair = null;
        if ("<<".equals(input)) {
            pair = getPosition(previous2Text);
        } else if ("<".equals(input)) {
            pair = getPosition(previous1Text);
        } else if (">".equals(input)) {
            pair = getPosition(next1Text);
        } else if (">>".equals(input)) {
            pair = getPosition(next2Text);
        } else if ("play/pause".equals(input)) {
            pair = getPosition(playText);
        } else if ("continue".equals(input)) {
            pair = getPosition(continueText);
        } else if ("1".equals(input)) {
            pair = getPosition(_1text);
        } else if ("2".equals(input)) {
            pair = getPosition(_2text);
        } else if ("3".equals(input)) {
            pair = getPosition(_3text);
        } else if ("4".equals(input)) {
            pair = getPosition(_4text);
        } else if ("5".equals(input)) {
            pair = getPosition(_5text);
        }

        if (pair == null) {
            return;
        }
        try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            Robot robot = new Robot();
            robot.mouseMove(pair.getLeft(), pair.getRight());
            robot.mousePress(InputEvent.BUTTON1_MASK);
            Thread.sleep(50);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            Thread.sleep(50);
            robot.mouseMove(p.x, p.y);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GlobalKeyListenerExampleForEnglishUI extends NativeKeyAdapter {
        public void close() {
            try {
                org.jnativehook.GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
            if (!enableToggleBtn.isSelected()) {
                System.out.println("未啟用 enableToggleBtn");
                return;
            }

            System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
            // 模式check
            if ((e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
                pressDMMVRPlayerBtn("<<");
            } else if ((e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
                pressDMMVRPlayerBtn(">>");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
                pressDMMVRPlayerBtn("<");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
                pressDMMVRPlayerBtn(">");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
                pressDMMVRPlayerBtn("play/pause");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_C) {
                pressDMMVRPlayerBtn("continue");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_1) {
                pressDMMVRPlayerBtn("1");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_2) {
                pressDMMVRPlayerBtn("2");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_3) {
                pressDMMVRPlayerBtn("3");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_4) {
                pressDMMVRPlayerBtn("4");
            } else if (e.getKeyCode() == NativeKeyEvent.VC_5) {
                pressDMMVRPlayerBtn("5");
            }

            else if (e.getKeyCode() == NativeKeyEvent.VC_R) {
                randomDMMPlayerPlay();
            } else if (e.getKeyCode() == NativeKeyEvent.VC_N) {
                playNextDMMFile();
            } else if (e.getKeyCode() == NativeKeyEvent.VC_T) {
                replayBtnClick();
            }
        }

        public void init() {
            try {
                GlobalScreen.registerNativeHook();
                GlobalScreen.addNativeMouseListener(new GlobalNativeMouseExampleForEnglishUI());
                GlobalScreen.addNativeKeyListener(new GlobalKeyListenerExampleForEnglishUI());
                JnativehookKeyboardMouseHelper.getInstance().disableLogger();
            } catch (NativeHookException ex) {
                JCommonUtil.handleException(ex);
            }
        }
    }

    private class GlobalNativeMouseExampleForEnglishUI extends NativeMouseAdapter {

        private GlobalNativeMouseExampleForEnglishUI() {
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent paramNativeMouseEvent) {
            super.nativeMouseReleased(paramNativeMouseEvent);
            if (paramNativeMouseEvent.getButton() == NativeMouseEvent.BUTTON1 && START_LISTENER.get() != null) {
                String position = paramNativeMouseEvent.getX() + "x" + paramNativeMouseEvent.getY();
                START_LISTENER.get().actionPerformed(new ActionEvent(position, -1, "setPosition"));
                START_LISTENER.set(null);
            }
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

    private class FixFileProcess {
        private void addQueueWork(FixFile file) {
            file.file.queueWork = true;
            DefaultListModel model = (DefaultListModel) fixList.getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                FixFile f1 = (FixFile) model.getElementAt(ii);
                if (StringUtils.equals(f1.file.path, file.file.path)) {
                    model.removeElementAt(ii);
                    ii--;
                }
            }
            model.addElement(file);
            fixList.repaint();
            dmmList.repaint();
        }

        private void processFixFile(FixFile file) {
            if (file.workDone) {
                return;
            }
            if (StringUtils.isNotBlank(file.renameToName)) {
                doRename(file);
            } else if (file.delFile) {
                doDelFile(file);
            }
        }

        private void doRename(FixFile file1) {
            DMMFile file = file1.file;
            File newFile = new File(file.file.getParentFile(), file1.renameToName);
            boolean fixNameOk = file.file.renameTo(newFile);
            JCommonUtil._jOptionPane_showMessageDialog_info("修改檔名:" + file1.renameToName + " ->" + (fixNameOk ? "成功" : "失敗"));
            if (fixNameOk) {
                file.applyFile(newFile);
                file1.workDone = true;
                dmmList.repaint();
                fixList.repaint();
            }
        }

        private void doDelFile(FixFile file1) {
            DMMFile file = file1.file;
            boolean delConfirm2 = file.file.delete();
            JCommonUtil._jOptionPane_showInputDialog("刪除:  " + file.name + "->" + (delConfirm2 ? "成功" : "失敗"));
            if (delConfirm2) {
                DefaultListModel model = (DefaultListModel) dmmList.getModel();
                model.removeElement(file);
                file1.workDone = true;
                dmmList.repaint();
                fixList.repaint();
            }
        }
    }

    private class FixFile {
        boolean delFile = false;
        String renameToName = "";
        DMMFile file;
        boolean workDone = false;

        private FixFile(DMMFile file) {
            this.file = file;
        }

        public String toString() {
            String repr = "";
            if (delFile) {
                repr = "刪除";
            } else if (StringUtils.isNotBlank(renameToName)) {
                repr = "改名";
            }
            if (workDone) {
                repr += "已完成";
            }
            String resultStr = //
                    "<html><font color=" + (!workDone ? "RED" : "GREEN") + ">" + //
                            repr + //
                            "</font>" + file.name + //
                            "</html>";//
            return resultStr;
        }
    }

    private class DMMFile {
        File file;
        String name;
        String path;
        boolean isPlayed = false;
        boolean queueWork = false;

        private void applyFile(File file) {
            this.file = file;
            this.name = file.getName();
            this.path = file.getAbsolutePath();
        }

        DMMFile(File file) {
            applyFile(file);
        }

        public String toString() {
            String repr = "";
            if (isPlayed) {
                repr = "已撥放";
            }
            if (queueWork) {
                repr = "佇列操作";
            }
            String resultStr = //
                    "<html><font color=red>" + //
                            repr + //
                            "</font>" + name + //
                            "</html>";//
            return resultStr;
        }
    }
}
