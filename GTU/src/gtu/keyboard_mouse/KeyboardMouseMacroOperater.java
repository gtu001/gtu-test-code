package gtu.keyboard_mouse;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import gtu.clipboard.ClipboardUtil;
import gtu.file.FileUtil;
import gtu.keyboard_mouse.KeyEventMapping.KeyCodeNotFoundException;
import gtu.keyboard_mouse.KeyEventMapping.NativeKeyEventToSwingKeyMapping;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.CleanTextInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.CustomInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.DelayInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.EventPattern;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.InputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.KeyInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.MouseInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.PasteInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.ScreenshotInputType;
import gtu.keyboard_mouse.KeyboardMouseMacroCreater.WheelInputType;
import gtu.log.Log;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JTextAreaUtil;

public class KeyboardMouseMacroOperater extends JFrame {

    private JPanel contentPane;
    private JList list;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        doMain();
    }

    private static void doMain() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Log.Setting.NORMAL.apply();

                    final KeyboardMouseMacroCreater creater = new KeyboardMouseMacroCreater();
                    final KeyboardMouseMacroOperater frame = new KeyboardMouseMacroOperater();
                    creater.setHideInSystemTrayHelper(frame.sysutil);
                    frame.stopCreaterListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            creater.exit();
                        }
                    };
                    creater.setIsOperaterRunning(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return frame.startTime.get() != -1;
                        }
                    });
                    creater.setAfterWriteMacroData(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frame.initList();
                        }
                    });
                    frame.preRunning = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            creater.pauseHook();
                        }
                    };
                    frame.postRunning = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            creater.startHook();
                        }
                    };
                    gtu.swing.util.JFrameUtil.setVisible(true, frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static volatile boolean MOTION_DEFINE_DO_STOP = false;
    private JListUtil lstUtil;
    private NativeKeyEventToSwingKeyMapping keyDefine = new NativeKeyEventToSwingKeyMapping();
    private HideInSystemTrayHelper sysutil;
    private ActionListener stopCreaterListener;
    private volatile AtomicLong startTime = new AtomicLong(-1L);
    private StopRunningNativeKeyListener stopRunningNativeKeyListener = new StopRunningNativeKeyListener();
    protected ActionListener preRunning;
    protected ActionListener postRunning;
    static ProgressInfo PROGRESS_INFO = new ProgressInfo();

    /**
     * Create the frame.
     */
    public KeyboardMouseMacroOperater() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 499, 501);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(1, 0, 0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        list = new JList();
        list.setToolTipText("<html>按Ctrl+Q開始/結束錄製巨集<br/>滑鼠點選項目開始撥放</html>");
        list.addMouseListener(new MouseAdapter() {

            private void sleep(long time) {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private void groupRun(FileZ fileZ) throws IOException {
                List<String> fList = FileUtils.readLines(fileZ.file);
                final List<MoveDefine> defineList = new ArrayList<MoveDefine>();
                for (String filename : fList) {
                    filename = StringUtils.trimToEmpty(filename);
                    if (filename.startsWith("#")) {
                        continue;
                    } else if (StringUtils.isNotBlank(filename)) {
                        File f1 = new File(KeyboardMouseMacroCreater.getMacroListDir(), filename);
                        File f2 = new File(filename);
                        // 一般檔案
                        if (f1.exists()) {
                            MoveDefine tmpDefine = new MoveDefine();
                            tmpDefine.readFile(f1);
                            defineList.add(tmpDefine);
                        } else if (f2.exists()) {
                            MoveDefine tmpDefine = new MoveDefine();
                            tmpDefine.readFile(f2);
                            defineList.add(tmpDefine);
                        } else {
                            // 特別指令
                            if (filename.startsWith("delay")) {
                                CustomDelyaMoveDefine d = new CustomDelyaMoveDefine(filename);
                                if (d.delayTime != 0) {
                                    defineList.add(d.createDelayMoveDefine(d.delayTime));
                                }
                            }
                        }
                    }
                }

                // 串聯所有MoveDefine
                for (int ii = 0; ii < defineList.size(); ii++) {
                    MoveDefine d1 = defineList.get(ii);
                    final MoveDefine d2 = (defineList.size() > ii + 1) ? defineList.get(ii + 1) : null;
                    if (d2 != null) {
                        d1.completeAction = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sleep(1000L);
                                d2.run();
                            }
                        };
                    } else {
                        d1.completeAction = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                long totalTime = 0;
                                for (int jj = 0; jj < defineList.size(); jj++) {
                                    totalTime += defineList.get(jj).duringTotalTime;
                                }
                                gtu.swing.util.JFrameUtil.setVisible(true, KeyboardMouseMacroOperater.this);
                                KeyboardMouseMacroOperater.this.setExtendedState(JFrame.NORMAL);
                                sysutil.displayMessage("Macro Operater", "[群組]操作結束! : " + totalTime, MessageType.WARNING);
                            }
                        };
                        break;
                    }
                }

                // 執行
                defineList.get(0).run();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (!lstUtil.isCorrectMouseClick(e)) {
                        return;
                    }

                    FileZ fileZ = lstUtil.getLeadSelectionObject(list);
                    System.out.println(fileZ);

                    KeyboardMouseMacroOperater.this.setExtendedState(JFrame.ICONIFIED);

                    switch (fileZ.type) {
                    case SINGLE:
                        MoveDefine d = new MoveDefine();
                        d.readFile(fileZ.file);
                        d.run();
                        break;
                    case GROUP:
                        groupRun(fileZ);
                        break;
                    }

                    System.out.println("moveDefine done !!");
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        lstUtil = JListUtil.newInstance(list);
        JScrollPane jspane = new JScrollPane();
        jspane.setViewportView(list);
        panel.add(jspane, BorderLayout.CENTER);

        JPanel p1 = new JPanel();
        panel.add(p1, BorderLayout.SOUTH);
        JPanel p2 = new JPanel();
        panel.add(p2, BorderLayout.NORTH);
        JPanel p3 = new JPanel();
        panel.add(p3, BorderLayout.WEST);
        JPanel p4 = new JPanel();
        panel.add(p4, BorderLayout.EAST);

        initList();

        sysutil = HideInSystemTrayHelper.newInstance();
        sysutil.setOpenAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initList();
            }
        });
        sysutil.setCloseAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopCreaterListener.actionPerformed(e);
            }
        });
        sysutil.apply(this, "鍵盤滑鼠自動", "resource/images/ico/Bomberman-icon.ico");

        this.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }
        });

        JCommonUtil.setJFrameCenter(this);
        JCommonUtil.defaultToolTipDelay();
    }

    static class ProgressInfo {
        JTextArea textarea;
        // JFrame dialog;
        JDialog dialog;

        ProgressInfo() {
            // textarea = new JTextArea();
            // JTextAreaUtil.setWrapTextArea(textarea);
            // dialog = new JFrame();
            // dialog.setContentPane(textarea);
            // dialog.setSize(new Dimension(400, 75));
            // JCommonUtil.setFrameAtop(dialog, true);
            // JCommonUtil.setLocationToRightBottomCorner(dialog);
            // gtu.swing.util.JFrameUtil.setVisible(false,dialog);

            dialog = new JDialog();
            dialog.setModalityType(ModalityType.MODELESS);
            textarea = new JTextArea();
            JTextAreaUtil.setWrapTextArea(textarea);
            dialog.setContentPane(textarea);
            dialog.setSize(new Dimension(400, 75));
            JCommonUtil.setLocationToRightBottomCorner(dialog);
            dialog.setVisible(false);
            dialog.pack();
            dialog.addFocusListener(new FocusListener() {
                @Override
                public void focusLost(FocusEvent e) {
                    dialog.toFront();
                }

                @Override
                public void focusGained(FocusEvent e) {
                }
            });
        }

        public void setVisible(boolean visible) {
            dialog.setVisible(visible);
        }

        public void setText(String text) {
            textarea.setText(text);
        }
    }

    private class CustomDelyaMoveDefine {
        String delayTimeUnformat = "";
        String unit = "";
        long delayTime = 0;

        CustomDelyaMoveDefine(String line) {
            Pattern ptn = Pattern.compile("delay\\s(\\d+)([\\w*])");
            Matcher mth = ptn.matcher(line);
            if (mth.find()) {
                delayTimeUnformat = mth.group(1);
                unit = mth.group(2);

                if (StringUtils.isNotBlank(delayTimeUnformat)) {
                    long delayTime = Long.parseLong(delayTimeUnformat);
                    if ("s".equalsIgnoreCase(unit)) {
                        delayTime = delayTime * 1000;
                    } else if ("m".equalsIgnoreCase(unit)) {
                        delayTime = delayTime * (1000 * 60);
                    }
                    this.delayTime = delayTime;
                }
            }
        }

        private MoveDefine createDelayMoveDefine(final long delayTime) {
            CustomInputType m = new CustomInputType();
            m.currentTime = 100;
            m.run = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(delayTime);
                    } catch (InterruptedException e) {
                    }
                }
            };
            MoveDefine d = new MoveDefine();
            d.addEvent(m.currentTime, m.run);
            d.addEventForLog(m.currentTime, m);
            return d;
        }
    }

    private class MoveDefine {

        Timer timer;
        Robot robot;
        volatile Map<Long, List<Runnable>> eventMap = new LinkedHashMap<Long, List<Runnable>>();
        Map<Long, List<InputType>> logMap = new LinkedHashMap<Long, List<InputType>>();
        ActionListener completeAction;
        long duringTotalTime = -1;

        private void readFile(File file) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                for (String line = null; (line = reader.readLine()) != null;) {
                    findMove(line);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void run() {
            startTime.set(System.currentTimeMillis());
            Runnable runner = new Runnable() {

                private List<Long> exeLst = new ArrayList<Long>();

                private boolean isOver(long currentTimeKey) {
                    if (eventMap.isEmpty()) {
                        return true;
                    }
                    List<Long> keys = new ArrayList<Long>(eventMap.keySet());
                    long val = keys.get(keys.size() - 1);
                    if (currentTimeKey > val) {
                        // 顯示未執行動作
                        StringBuilder sb = new StringBuilder();
                        for (long key : exeLst) {
                            if (logMap.containsKey(key) && eventMap.containsKey(key)) {
                                sb.append("#LOST# " + key + " --- " + logMap.get(key) + "\n");
                            } else if (logMap.containsKey(key) && !eventMap.containsKey(key)) {
                                sb.append(key + " --- " + logMap.get(key) + "\n");
                            } else {
                                // sb.append("" + key + "\n");
                            }
                        }
                        FileUtil.saveToFile(new File(FileUtil.DESKTOP_PATH, "not_ok.txt"), sb.toString(), "utf-8");
                        return true;
                    }
                    return false;
                }

                @Override
                public void run() {
                    // 執行前關閉監聽
                    if (preRunning != null) {
                        preRunning.actionPerformed(null);
                    }

                    timer = new Timer();
                    timer.schedule(new TimerTask() {

                        private void executeMove(long currentTimeKey) {
                            List<Runnable> list2 = eventMap.get(currentTimeKey);
                            List<InputType> list3 = logMap.get(currentTimeKey);
                            for (int ii = 0; ii < list2.size(); ii++) {
                                Runnable r = list2.get(ii);
                                InputType m = list3.get(ii);
                                r.run();
                                PROGRESS_INFO.setText(String.valueOf(m));
                            }
                            eventMap.remove(currentTimeKey);
                        }

                        @Override
                        public void run() {
                            long currentTimeKey = scheduledExecutionTime() - startTime.get();

                            exeLst.add(currentTimeKey);

                            boolean executeOk = false;

                            // 檢查當期動作
                            if (eventMap.containsKey(currentTimeKey)) {
                                executeMove(currentTimeKey);
                                executeOk = true;
                            }

                            // 檢查過期動作
                            if (!executeOk) {
                                Iterator<Long> it = eventMap.keySet().iterator();
                                if (it.hasNext()) {
                                    long chkCurrentKey = it.next();
                                    if (chkCurrentKey <= currentTimeKey) {
                                        executeMove(chkCurrentKey);
                                        executeOk = true;
                                    }
                                }
                            }

                            if (!executeOk) {
                                if (isOver(currentTimeKey) || MOTION_DEFINE_DO_STOP) {
                                    // 結束開啟key監聽
                                    if (postRunning != null) {
                                        postRunning.actionPerformed(null);
                                    }
                                    startTime.set(-1);
                                    timer.cancel();
                                    duringTotalTime = currentTimeKey;

                                    if (completeAction == null) {
                                        sysutil.displayMessage("Macro Operater", "操作結束! : " + duringTotalTime, MessageType.WARNING);
                                        KeyboardMouseMacroOperater.this.bringToTop();
                                        Toolkit.getDefaultToolkit().beep();
                                    } else {
                                        completeAction.actionPerformed(new ActionEvent(MoveDefine.this, 0, "complete1"));
                                    }
                                }
                            }
                        }
                    }, 0, 1);
                }
            };

            PROGRESS_INFO.setVisible(true);
            runner.run();
        }

        MoveDefine() {
            try {
                robot = new Robot();
                MOTION_DEFINE_DO_STOP = false;
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        private void findMove(String line) {
            line = StringUtils.trimToEmpty(line);
            if (line.startsWith("#")) {
                return;
            }

            InputType inputType = null;
            for (EventPattern e : EventPattern.values()) {
                if (e.isMatch(line)) {
                    inputType = e.get(line);
                    break;
                }
            }
            if (inputType == null) {
                throw new RuntimeException("無法判斷行為 : " + line);
            }

            if (inputType instanceof CleanTextInputType) {
                CleanTextInputType p = (CleanTextInputType) inputType;
                addEvent(p.currentTime, getCleanTextEvent(p));
                addEventForLog(p.currentTime, p);
            } else if (inputType instanceof MouseInputType) {
                MouseInputType m = (MouseInputType) inputType;
                addEvent(m.currentTime, getMouseEvent(m));
                addEventForLog(m.currentTime, m);
            } else if (inputType instanceof KeyInputType) {
                KeyInputType k = (KeyInputType) inputType;
                addEvent(k.currentTime, getKeyboardEvent(k));
                addEventForLog(k.currentTime, k);
            } else if (inputType instanceof DelayInputType) {
                DelayInputType d = (DelayInputType) inputType;
                addEvent(d.currentTime, getDelayEvent(d));
                addEventForLog(d.currentTime, d);
            } else if (inputType instanceof PasteInputType) {
                PasteInputType p = (PasteInputType) inputType;
                addEvent(p.currentTime, getPasteEvent(p));
                addEventForLog(p.currentTime, p);
            } else if (inputType instanceof WheelInputType) {
                WheelInputType p = (WheelInputType) inputType;
                addEvent(p.currentTime, getWheelEvent(p));
                addEventForLog(p.currentTime, p);
            } else if (inputType instanceof ScreenshotInputType) {
                ScreenshotInputType p = (ScreenshotInputType) inputType;
                addEvent(p.currentTime, getScreenshotEvent(p));
                addEventForLog(p.currentTime, p);
            } else if (inputType instanceof CustomInputType) {
                CustomInputType p = (CustomInputType) inputType;
                addEvent(p.currentTime, p.run);
                addEventForLog(p.currentTime, p);
            }
        }

        private Runnable getWheelEvent(final WheelInputType m) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        int val = m.wheelRotation * m.scrollAmount;
                        robot.mouseWheel(val);
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            };
            return run;
        }

        private Runnable getScreenshotEvent(final ScreenshotInputType m) {
            Runnable run = new Runnable() {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                @Override
                public void run() {
                    try {
                        BufferedImage capture = robot.createScreenCapture(screenRect);
                        ImageIO.write(capture, "bmp", new File(FileUtil.DESKTOP_PATH, "screenshot_" + sdf.format(System.currentTimeMillis()) + ".bmp"));
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            };
            return run;
        }

        private Runnable getDelayEvent(final DelayInputType d) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (d.delayTime > 0) {
                            startTime.set(startTime.get() + d.delayTime);// 正值延後
                        } else {
                            startTime.set(startTime.get() + d.delayTime);// 負值往前
                        }
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            };
            return run;
        }

        private Runnable getCleanTextEvent(final CleanTextInputType m) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        robot.mouseMove(m.x, m.y);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        robot.delay(50);
                        robot.keyPress(KeyEvent.VK_CONTROL);
                        robot.keyPress(KeyEvent.VK_A);
                        robot.keyRelease(KeyEvent.VK_A);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                        robot.delay(50);
                        robot.keyPress(KeyEvent.VK_BACK_SPACE);
                        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            };
            return run;
        }

        private Runnable getMouseEvent(final MouseInputType m) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (m.type == EventPattern.MOUSE_MOVE || //
                        m.type == EventPattern.MOUSE_DRAG) {
                            robot.mouseMove(m.x, m.y);
                        } else if (m.type == EventPattern.MOUSE_CLICK) {
                            switch (m.btn) {
                            case 1:
                                robot.mousePress(InputEvent.BUTTON1_MASK);
                                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                                break;
                            case 2:
                                robot.mousePress(InputEvent.BUTTON3_MASK);// 滑鼠右鍵是3
                                robot.mouseRelease(InputEvent.BUTTON3_MASK);
                                break;
                            case 3:
                                robot.mousePress(InputEvent.BUTTON2_MASK);
                                robot.mouseRelease(InputEvent.BUTTON2_MASK);
                                break;
                            }
                        } else if (m.type == EventPattern.MOUSE_PRESS) {
                            switch (m.btn) {
                            case 1:
                                robot.mousePress(InputEvent.BUTTON1_MASK);
                                break;
                            case 2:
                                robot.mousePress(InputEvent.BUTTON3_MASK);// 滑鼠右鍵是3
                                break;
                            case 3:
                                robot.mousePress(InputEvent.BUTTON2_MASK);
                                break;
                            }
                        } else if (m.type == EventPattern.MOUSE_RELEASE) {
                            switch (m.btn) {
                            case 1:
                                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                                break;
                            case 2:
                                robot.mouseRelease(InputEvent.BUTTON3_MASK);// 滑鼠右鍵是3
                                break;
                            case 3:
                                robot.mouseRelease(InputEvent.BUTTON2_MASK);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            };
            return run;
        }

        private Runnable getPasteEvent(final PasteInputType m) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        String val = m.pasteStr;
                        if (StringUtils.isNotBlank(val)) {
                            ClipboardUtil.getInstance().setContents(val);
                            Thread.sleep(50L);
                            robot.keyPress(KeyEvent.VK_CONTROL);
                            robot.keyPress(KeyEvent.VK_V);
                            robot.keyRelease(KeyEvent.VK_V);
                            robot.keyRelease(KeyEvent.VK_CONTROL);
                            System.out.println("貼上字串 : " + val);
                        }
                    } catch (Exception ex) {
                        JCommonUtil.handleException(ex);
                    }
                }
            };
            return run;
        }

        private Runnable getKeyboardEvent(final KeyInputType k) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (k.type == EventPattern.KEY_PRESS) {
                            for (MaskKey e : MaskKey.values()) {
                                if ((k.modifier & e.mask) != 0) {
                                    robot.keyPress(e.key);
                                }
                            }

                            int newKeyCode = keyDefine.getKeyCodeFromNativeKeyEvent(k.keycode);
                            robot.keyPress(newKeyCode);

                            for (MaskKey e : MaskKey.values()) {
                                if ((k.modifier & e.mask) != 0) {
                                    robot.keyRelease(e.key);
                                }
                            }

                        } else if (k.type == EventPattern.KEY_RELEASE) {
                            for (MaskKey e : MaskKey.values()) {
                                if ((k.modifier & e.mask) != 0) {
                                    robot.keyPress(e.key);
                                }
                            }

                            int newKeyCode = keyDefine.getKeyCodeFromNativeKeyEvent(k.keycode);
                            robot.keyRelease(newKeyCode);

                            for (MaskKey e : MaskKey.values()) {
                                if ((k.modifier & e.mask) != 0) {
                                    robot.keyRelease(e.key);
                                }
                            }
                        }
                    } catch (KeyCodeNotFoundException ex) {
                        Log.debug(ex, "Error : " + k + " -> " + ex.getMessage());
                    } catch (Exception ex) {
                        JCommonUtil.handleExceptionDetails("Error : " + k + " -> " + ex.getMessage(), ex);
                    }
                }
            };
            return run;
        }

        private void addEvent(Long currentKey, Runnable event) {
            List<Runnable> lst = new ArrayList<Runnable>();
            if (eventMap.containsKey(currentKey)) {
                eventMap.get(currentKey);
            }
            lst.add(event);
            eventMap.put(currentKey, lst);
        }

        private void addEventForLog(long currentTime, InputType inputType) {
            List<InputType> lst = new ArrayList<InputType>();
            if (logMap.containsKey(currentTime)) {
                lst = logMap.get(currentTime);
            }
            lst.add(inputType);
            logMap.put(currentTime, lst);
        }
    }

    private enum MaskKey {
        ALT(NativeInputEvent.ALT_MASK, KeyEvent.VK_ALT), //
        SHIFT(NativeInputEvent.SHIFT_MASK, KeyEvent.VK_SHIFT), //
        CTRL(NativeInputEvent.CTRL_MASK, KeyEvent.VK_CONTROL),//
        ;

        final int mask;
        final int key;

        MaskKey(int mask, int key) {
            this.mask = mask;
            this.key = key;
        }
    }

    private class StopRunningNativeKeyListener implements NativeKeyListener {

        StopRunningNativeKeyListener() {
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
            if ((e.getModifiers() & NativeInputEvent.CTRL_MASK) != 0 && //
                    e.getKeyCode() == NativeKeyEvent.VC_BACKQUOTE) {
                System.out.println("stop run operator ~~~~~");
                MOTION_DEFINE_DO_STOP = true;
                bringToTop();
            }
        }
    }

    private void bringToTop() {
        PROGRESS_INFO.setVisible(false);

        this.initList();
        gtu.swing.util.JFrameUtil.setVisible(true, this);
        this.setState(java.awt.Frame.NORMAL);
        this.toFront();
        this.repaint();
    }

    private void initList() {
        DefaultListModel model = JListUtil.createModel();
        if (KeyboardMouseMacroCreater.getMacroListDir().listFiles() != null) {
            for (File f : KeyboardMouseMacroCreater.getMacroListDir().listFiles()) {
                if (f.getName().startsWith("macro_") && f.getName().endsWith(".txt")) {
                    model.addElement(new FileZ(f, FileZ.FileType.SINGLE));
                } else if (f.getName().startsWith("group_") && f.getName().endsWith(".txt")) {
                    model.addElement(new FileZ(f, FileZ.FileType.GROUP));
                }
            }
        }
        list.setModel(model);
    }

    private static class FileZ {
        File file;
        FileType type;

        private enum FileType {
            SINGLE, GROUP;
        }

        FileZ(File file, FileType type) {
            this.file = file;
            this.type = type;
        }

        @Override
        public String toString() {
            return file.getName();
        }
    }
}
