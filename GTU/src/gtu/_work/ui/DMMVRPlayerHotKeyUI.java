package gtu._work.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.EventObject;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
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
import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JFrameRGBColorPanel;
import gtu.swing.util.JFrameUtil;
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
    private static AtomicReference<ActionListener> START_LISTENER = new AtomicReference<ActionListener>();
    private PropertiesUtilBean config = new PropertiesUtilBean(DMMVRPlayerHotKeyUI.class);

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
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), },
                new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        lblNewLabel = new JLabel("<<(ctl+left)");
        panel.add(lblNewLabel, "2, 2");

        panel_3 = new JPanel();
        panel.add(panel_3, "4, 2, fill, fill");

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
        panel.add(lblNewLabel_1, "2, 4");

        panel_7 = new JPanel();
        panel.add(panel_7, "4, 4, fill, fill");

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
        panel.add(lblNewLabel_2, "2, 6");

        panel_6 = new JPanel();
        panel.add(panel_6, "4, 6, fill, fill");

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
        panel.add(lblNewLabel_3, "2, 8");

        panel_5 = new JPanel();
        panel.add(panel_5, "4, 8, fill, fill");

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
        panel.add(lblNewLabel_4, "2, 10");

        panel_4 = new JPanel();
        panel.add(panel_4, "4, 10, fill, fill");

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

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);

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
        String strVal = StringUtils.trimToEmpty(text.getText());
        Pattern ptn = Pattern.compile("(\\d+)x(\\d+)");
        Matcher mth = ptn.matcher(strVal);
        if (mth.find()) {
            return Pair.of(Integer.parseInt(mth.group(1)), Integer.parseInt(mth.group(2)));
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

    private GlobalKeyListenerExampleForEnglishUI keyUtil;

    private class GlobalKeyListenerExampleForEnglishUI extends NativeKeyAdapter {
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
}
