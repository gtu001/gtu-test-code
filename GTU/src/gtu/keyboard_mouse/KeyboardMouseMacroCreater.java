package gtu.keyboard_mouse;

import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import gtu.date.DateFormatUtil;
import gtu.file.FileUtil;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;

public class KeyboardMouseMacroCreater implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

    private static final Logger logger = Logger.getLogger(KeyboardMouseMacroCreater.class.getPackage().getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                KeyboardMouseMacroCreater t = new KeyboardMouseMacroCreater();
            }
        });
    }

    private long startTime = -1;
    private Map<Long, List<NativeInputEventZ>> map = new LinkedHashMap<Long, List<NativeInputEventZ>>();
    private Callable<Boolean> isOperaterRunning;
    private ActionListener afterWriteMacroData;
    private HideInSystemTrayHelper hideInSystemTrayHelper;

    /**
     * 取得預設Macro List Dir
     */
    public static File getMacroListDir() {
        File macroDir = new File(FileUtil.DESKTOP_PATH, "gtu001_macro");
        if (!macroDir.exists()) {
            macroDir.mkdirs();
        }
        return macroDir;
    }

    public KeyboardMouseMacroCreater() {
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        ConsoleHandler localConsoleHandler = new ConsoleHandler();
        localConsoleHandler.setFormatter(new LogFormatter());
        localConsoleHandler.setLevel(Level.WARNING);
        logger.addHandler(localConsoleHandler);
        startHook();
    }

    private void checkRigisterHook(boolean onOff) {
        if (!GlobalScreen.isNativeHookRegistered()) {
            try {
                if (onOff) {
                    GlobalScreen.registerNativeHook();
                } else {
                    GlobalScreen.unregisterNativeHook();
                }
            } catch (NativeHookException e) {
                JCommonUtil.handleException(e);
                throw new RuntimeException(e);
            }
        }
    }

    public void startHook() {
        checkRigisterHook(true);
        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
        GlobalScreen.addNativeMouseWheelListener(this);
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
    }

    public void pauseHook() {
        checkRigisterHook(true);
        GlobalScreen.removeNativeKeyListener(this);
        GlobalScreen.removeNativeMouseListener(this);
        GlobalScreen.removeNativeMouseMotionListener(this);
        GlobalScreen.removeNativeMouseWheelListener(this);
        GlobalScreen.setEventDispatcher(new SwingDispatchService());
    }

    public void initialize() {
        startTime = System.currentTimeMillis();
        map = new LinkedHashMap<Long, List<NativeInputEventZ>>();
    }

    static enum EventPattern {
        KEY_PRESS("(\\d+)\\tkeyPress\\skeycode\\:(\\d+),modifier\\:(\\d+)", "keyPress") {
            @Override
            InputType get(String line) {
                return getK(line, ptn);
            }
        }, //
        KEY_RELEASE("(\\d+)\\tkeyRelease\\skeycode\\:(\\d+),modifier\\:(\\d+)", "keyRelease") {
            @Override
            InputType get(String line) {
                return getK(line, ptn);
            }
        }, //
        MOUSE_PRESS("(\\d+)\\tmousePress\\sx\\:(\\-?\\d+),y\\:(\\-?\\d+)\\sbtn\\:(\\d+)", "mousePress") {
            @Override
            InputType get(String line) {
                return getM(line, ptn);
            }
        }, //
        MOUSE_RELEASE("(\\d+)\\tmouseRelease\\sx\\:(\\-?\\d+),y\\:(\\-?\\d+)\\sbtn\\:(\\d+)", "mouseRelease") {
            @Override
            InputType get(String line) {
                return getM(line, ptn);
            }
        }, //
        MOUSE_MOVE("(\\d+)\\tmouseMove\\sx\\:(\\-?\\d+),y\\:(\\-?\\d+)\\sbtn\\:(\\d+)", "mouseMove") {
            @Override
            InputType get(String line) {
                return getM(line, ptn);
            }
        }, //
        MOUSE_DRAG("(\\d+)\\tmouseDrag\\sx\\:(\\-?\\d+),y\\:(\\-?\\d+)\\sbtn\\:(\\d+)", "mouseDrag") {
            @Override
            InputType get(String line) {
                return getM(line, ptn);
            }
        }, //
        MOUSE_CLICK("(\\d+)\\tmouseClick\\sx\\:(\\-?\\d+),y\\:(\\-?\\d+)\\sbtn\\:(\\d+)", "mouseClick") {
            @Override
            InputType get(String line) {
                return getM(line, ptn);
            }
        }, //
        CLEAN_INPUT("(\\d+)\\tcleanInput\\sx\\:(\\-?\\d+),y\\:(\\-?\\d+)\\sbtn\\:(\\d+)", "cleanInput") {
            @Override
            InputType get(String line) {
                MouseInputType m = (MouseInputType) getM(line, ptn);
                CleanTextInputType m2 = new CleanTextInputType(m);
                return m2;
            }
        }, //
        MOUSE_WHEEL("(\\d+)\\tmouseWheel\\srotation:(\\-?\\d+),amount:(\\d+)", "mouseWheel") {
            @Override
            InputType get(String line) {
                Matcher mth = ptn.matcher(line);
                if (mth.find()) {
                    WheelInputType d = new WheelInputType();
                    d.currentTime = Long.parseLong(mth.group(1));
                    d.wheelRotation = Integer.parseInt(mth.group(2));
                    d.scrollAmount = Integer.parseInt(mth.group(3));
                    return d;
                }
                throw new RuntimeException("無法取得參數!:" + line);
            }
        }, //
        DELAY("(\\d+)\\tdelay\\s(\\-?\\d+)", "delay") {
            @Override
            InputType get(String line) {
                Matcher mth = ptn.matcher(line);
                if (mth.find()) {
                    DelayInputType d = new DelayInputType();
                    d.currentTime = Long.parseLong(mth.group(1));
                    d.delayTime = Long.parseLong(mth.group(2));
                    return d;
                }
                throw new RuntimeException("無法取得參數!:" + line);
            }
        }, //
        PASTE_STRING("(\\d+)\\tpaste\\s(.*)", "paste") {
            @Override
            InputType get(String line) {
                Matcher mth = ptn.matcher(line);
                if (mth.find()) {
                    PasteInputType d = new PasteInputType();
                    d.currentTime = Long.parseLong(mth.group(1));
                    d.pasteStr = mth.group(2);
                    return d;
                }
                throw new RuntimeException("無法取得參數!:" + line);
            }
        }, //
        SCREENSHOOT("(\\d+)\\tscreenshot\\s", "screenshot") {
            @Override
            InputType get(String line) {
                Matcher mth = ptn.matcher(line);
                if (mth.find()) {
                    ScreenshotInputType d = new ScreenshotInputType();
                    d.currentTime = Long.parseLong(mth.group(1));
                    return d;
                }
                throw new RuntimeException("無法取得參數!:" + line);
            }
        },//
        ;

        final Pattern ptn;
        final String title;

        EventPattern(String ptn, String title) {
            this.ptn = Pattern.compile(ptn);
            this.title = title;
        }

        boolean isMatch(String line) {
            return ptn.matcher(line).find();
        }

        abstract InputType get(String line);

        InputType getK(String line, Pattern ptn) {
            Matcher mth = ptn.matcher(line);
            if (mth.find()) {
                long currentTime = Long.parseLong(mth.group(1));
                int keycode = Integer.parseInt(mth.group(2));
                int modifier = Integer.parseInt(mth.group(3));
                KeyInputType k = new KeyInputType();
                k.keycode = keycode;
                k.modifier = modifier;
                k.currentTime = currentTime;
                k.type = this;
                return k;
            }
            throw new RuntimeException("無法取得參數!:" + line);
        }

        InputType getM(String line, Pattern ptn) {
            Matcher mth = ptn.matcher(line);
            if (mth.find()) {
                long currentTime = Long.parseLong(mth.group(1));
                int x = Integer.parseInt(mth.group(2));
                int y = Integer.parseInt(mth.group(3));
                int btn = Integer.parseInt(mth.group(4));
                MouseInputType m = new MouseInputType();
                m.x = x;
                m.y = y;
                m.btn = btn;
                m.currentTime = currentTime;
                m.type = this;
                return m;
            }
            throw new RuntimeException("無法取得參數!:" + line);
        }
    }

    static class InputType {
        long currentTime;
        EventPattern type;
    }

    static class WheelInputType extends InputType {
        int scrollAmount;
        int wheelRotation;

        @Override
        public String toString() {
            return "WheelInputType [currentTime=" + currentTime + ", type=" + type + ", scrollAmount=" + scrollAmount + ", wheelRotation=" + wheelRotation + "]";
        }
    }

    static class PasteInputType extends InputType {
        String pasteStr;

        @Override
        public String toString() {
            return "PasteInputType [currentTime=" + currentTime + ", type=" + type + ", pasteStr=" + pasteStr + "]";
        }
    }

    static class DelayInputType extends InputType {
        long delayTime;

        @Override
        public String toString() {
            return "DelayInputType [currentTime=" + currentTime + ", type=" + type + ", delayTime=" + delayTime + "]";
        }
    }

    static class CleanTextInputType extends MouseInputType {
        CleanTextInputType(MouseInputType m) {
            this.btn = m.btn;
            this.x = m.x;
            this.y = m.y;
        }

        @Override
        public String toString() {
            return "CleanTextInputType [currentTime=" + currentTime + ", x=" + x + ", y=" + y + ", btn=" + btn + ", type=" + type + "]";
        }
    }

    static class CustomInputType extends InputType {
        Runnable run;

        @Override
        public String toString() {
            return "CustomInputType [currentTime=" + currentTime + ", type=" + type + "]";
        }
    }

    static class MouseInputType extends InputType {
        int x;
        int y;
        int btn;

        @Override
        public String toString() {
            return "MouseInputType [currentTime=" + currentTime + ", type=" + type + ", x=" + x + ", y=" + y + ", btn=" + btn + "]";
        }
    }

    static class ScreenshotInputType extends InputType {
        @Override
        public String toString() {
            return "ScreenshotInputType [currentTime=" + currentTime + ", type=" + type + "]";
        }
    }

    static class KeyInputType extends InputType {
        int keycode;
        int modifier;

        @Override
        public String toString() {
            return "KeyInputType [keycode=" + keycode + ", modifier=" + modifier + ", currentTime=" + currentTime + ", type=" + type + "]";
        }
    }

    private void writeMacroData() {
        File file = new File(getMacroListDir(), "macro_" + DateFormatUtil.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".txt");
        String fileName = JCommonUtil._jOptionPane_showInputDialog("請輸入檔名", file.getAbsolutePath());
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));

            for (Long key : map.keySet()) {

                List<NativeInputEventZ> lst = map.get(key);
                System.out.println("key - " + key);

                for (NativeInputEventZ n : lst) {
                    String command = "";

                    String remark = "";
                    if (n.orign instanceof NativeKeyEvent) {
                        NativeKeyEvent event = (NativeKeyEvent) n.orign;
                        remark = NativeKeyEvent.getKeyText(event.getKeyCode()) + "," + NativeKeyEvent.getModifiersText(event.getModifiers());
                    } else if (n.orign instanceof NativeMouseEvent) {
                        NativeMouseEvent event = (NativeMouseEvent) n.orign;
                        switch (event.getButton()) {
                        case 1:
                            remark = "Btn1";
                            break;
                        case 2:
                            remark = "Btn2";
                            break;
                        case 3:
                            remark = "Btn3";
                            break;
                        }
                    }
                    if (StringUtils.isNotBlank(remark)) {
                        remark = "\t--" + remark;
                    }

                    if (StringUtils.equals(n.type, "nativeMouseMoved") && n.orign instanceof NativeMouseEvent) {
                        NativeMouseEvent m1 = (NativeMouseEvent) n.orign;
                        command = String.format("%s\tx:%d,y:%d btn:%d %s", EventPattern.MOUSE_MOVE.title, m1.getX(), m1.getY(), m1.getButton(), remark);
                    } else if (StringUtils.equals(n.type, "nativeMouseDragged") && n.orign instanceof NativeMouseEvent) {
                        NativeMouseEvent m1 = (NativeMouseEvent) n.orign;
                        command = String.format("%s\tx:%d,y:%d btn:%d %s", EventPattern.MOUSE_DRAG.title, m1.getX(), m1.getY(), m1.getButton(), remark);
                    } else if (StringUtils.equals(n.type, "nativeMousePressed") && n.orign instanceof NativeMouseEvent) {
                        NativeMouseEvent m1 = (NativeMouseEvent) n.orign;
                        command = String.format("%s\tx:%d,y:%d btn:%d %s", EventPattern.MOUSE_PRESS.title, m1.getX(), m1.getY(), m1.getButton(), remark);
                    } else if (StringUtils.equals(n.type, "nativeMouseReleased") && n.orign instanceof NativeMouseEvent) {
                        NativeMouseEvent m1 = (NativeMouseEvent) n.orign;
                        command = String.format("%s\tx:%d,y:%d btn:%d %s", EventPattern.MOUSE_RELEASE.title, m1.getX(), m1.getY(), m1.getButton(), remark);
                    } else if (StringUtils.equals(n.type, "nativeMouseWheelMoved") && n.orign instanceof NativeMouseWheelEvent) {
                        NativeMouseWheelEvent m1 = (NativeMouseWheelEvent) n.orign;
                        command = String.format("%s\trotation:%d,amount:%d %s", EventPattern.MOUSE_WHEEL.title, m1.getWheelRotation(), m1.getScrollAmount(), remark);
                    } else if (StringUtils.equals(n.type, "nativeKeyPressed") && n.orign instanceof NativeKeyEvent) {
                        NativeKeyEvent m1 = (NativeKeyEvent) n.orign;
                        command = String.format("%s\tkeycode:%d,modifier:%d %s", EventPattern.KEY_PRESS.title, m1.getKeyCode(), m1.getModifiers(), remark);
                    } else if (StringUtils.equals(n.type, "nativeKeyReleased") && n.orign instanceof NativeKeyEvent) {
                        NativeKeyEvent m1 = (NativeKeyEvent) n.orign;
                        command = String.format("%s\tkeycode:%d,modifier:%d %s", EventPattern.KEY_RELEASE.title, m1.getKeyCode(), m1.getModifiers(), remark);
                    } else if (StringUtils.equals(n.type, "doScreenshot") && n.orign instanceof NativeKeyEvent) {
                        NativeKeyEvent m1 = (NativeKeyEvent) n.orign;
                        command = String.format("%s\t%s", EventPattern.SCREENSHOOT.title, remark);
                    }

                    if (StringUtils.isNotBlank(command)) {
                        writer.write(key + "\t" + command);
                        writer.newLine();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent paramNativeMouseEvent) {
        handleEvent("nativeMouseClicked", paramNativeMouseEvent);
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent paramNativeMouseEvent) {
        handleEvent("nativeMousePressed", paramNativeMouseEvent);
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent paramNativeMouseEvent) {
        handleEvent("nativeMouseReleased", paramNativeMouseEvent);
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent paramNativeMouseEvent) {
        handleEvent("nativeMouseMoved", paramNativeMouseEvent);
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent paramNativeMouseEvent) {
        handleEvent("nativeMouseDragged", paramNativeMouseEvent);
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent paramNativeMouseWheelEvent) {
        handleEvent("nativeMouseWheelMoved", paramNativeMouseWheelEvent);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent paramNativeKeyEvent) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent paramNativeKeyEvent) {
        handleKeyEvent("nativeKeyPressed", paramNativeKeyEvent);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent paramNativeKeyEvent) {
        handleKeyEvent("nativeKeyReleased", paramNativeKeyEvent);
    }

    private void handleKeyEvent(String type, NativeKeyEvent e) {
        if ((e.getModifiers() & NativeInputEvent.CTRL_L_MASK) != 0 && //
                e.getKeyCode() == NativeKeyEvent.VC_Q) {
            if ("nativeKeyReleased".equals(type)) {
                saveMarco();
            }
        } else if (e.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
            if ("nativeKeyReleased".equals(type)) {
                handleEvent("doScreenshot", e);
            }
        } else {
            handleEvent(type, e);
        }
    }

    public void exit() {
        try {
            GlobalScreen.unregisterNativeHook();
            System.runFinalization();
            System.exit(0);
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }

    private void trayShowMessage(String message) {
        if (hideInSystemTrayHelper != null) {
            hideInSystemTrayHelper.displayMessage("Macro creater", message, MessageType.INFO);
        } else {
            System.out.println("請初始化hideInSystemTrayHelper");
        }
    }

    private void saveMarco() {
        try {
            if (getIsOperaterRunning().call()) {
                System.out.println("operater正在執行中...");
                this.trayShowMessage("operater正在執行中...");
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (startTime == -1) {
            System.out.println("啟動錄製");
            this.initialize();// 啟動錄製
            trayShowMessage("啟動錄製");
            KeyboardMouseMacroOperater.PROGRESS_INFO.setVisible(true);

            return;
        }
        System.out.println("結束錄製");
        startTime = -1;
        trayShowMessage("結束錄製");
        KeyboardMouseMacroOperater.PROGRESS_INFO.setVisible(false);
        this.writeMacroData();

        if (getAfterWriteMacroData() != null) {
            getAfterWriteMacroData().actionPerformed(new ActionEvent(this, 0, "save"));
        }
    }

    private class NativeInputEventZ {
        NativeInputEvent orign;
        String type;

        NativeInputEventZ(String type, NativeInputEvent orign) {
            this.type = type;
            this.orign = orign;
        }
    }

    private <T extends NativeInputEvent> void handleEvent(String type, T paramNativeKeyEvent) {
        if (startTime == -1) {
            System.out.println("未啟動marco");
            return;
        }
        List<NativeInputEventZ> lst = new ArrayList<NativeInputEventZ>();
        long currentTimeKey = System.currentTimeMillis() - startTime;
        if (map.containsKey(currentTimeKey)) {
            lst = map.get(currentTimeKey);
        }
        lst.add(new NativeInputEventZ(type, paramNativeKeyEvent));
        map.put(currentTimeKey, lst);
        KeyboardMouseMacroOperater.PROGRESS_INFO.setText(currentTimeKey + "\t" + String.valueOf(paramNativeKeyEvent));
    }

    private final class LogFormatter extends Formatter {
        private LogFormatter() {
        }

        public String format(LogRecord paramLogRecord) {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(new Date(paramLogRecord.getMillis())).append(" ").append(paramLogRecord.getLevel().getLocalizedName()).append(":\t").append(formatMessage(paramLogRecord));
            if (paramLogRecord.getThrown() != null) {
                try {
                    StringWriter localStringWriter = new StringWriter();
                    PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
                    paramLogRecord.getThrown().printStackTrace(localPrintWriter);
                    localPrintWriter.close();
                    localStringBuilder.append(localStringWriter.toString());
                    localStringWriter.close();
                } catch (Exception localException) {
                }
            }
            return localStringBuilder.toString();
        }
    }

    public HideInSystemTrayHelper getHideInSystemTrayHelper() {
        return hideInSystemTrayHelper;
    }

    public void setHideInSystemTrayHelper(HideInSystemTrayHelper hideInSystemTrayHelper) {
        this.hideInSystemTrayHelper = hideInSystemTrayHelper;
    }

    public Callable<Boolean> getIsOperaterRunning() {
        return isOperaterRunning;
    }

    public void setIsOperaterRunning(Callable<Boolean> isOperaterRunning) {
        this.isOperaterRunning = isOperaterRunning;
    }

    public ActionListener getAfterWriteMacroData() {
        return afterWriteMacroData;
    }

    public void setAfterWriteMacroData(ActionListener afterWriteMacroData) {
        this.afterWriteMacroData = afterWriteMacroData;
    }
}
