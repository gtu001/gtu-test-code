package gtu.log.finder;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import gtu.class_.ClassCompilerUtil;
import gtu.class_.ClassUtil;
import gtu.clipboard.ClipboardUtil;
import gtu.date.DateUtil;
import gtu.file.FileUtil;
import gtu.javafx.traynotification.NotificationType;
import gtu.javafx.traynotification.TrayNotificationHelper;
import gtu.javafx.traynotification.animations.AnimationType;
import gtu.log.Logger2File;
import gtu.log.finder.DebugMointerParameterParserMF.ParseToObject;
import gtu.log.finder.DebugMointerTypeUtil.TempValueEnum;
import gtu.log.finder.DebugMointerUIUIContent.JTextFieldKeeper;
import gtu.swing.util.JCommonUtil;
import gtu.swing.util.JCommonUtil.HandleDocumentEvent;
import gtu.swing.util.JListUtil;
import gtu.swing.util.JOptionPaneUtil;
import gtu.swing.util.JOptionPaneUtil.ComfirmDialogResult;
import gtu.swing.util.JPopupMenuUtil;
import gtu.swing.util.JTableUtil;
import gtu.swing.util.JTreeUtil;
import gtu.swing.util.SysTrayUtil;
import javassist.Modifier;

/**
 * 使用說明 Resource name = 對應"來源"index物件(ps : 最左邊從1開始累加) Resource description 是數值,
 * 對應"目的", 表示設定到目的method的第幾個參數 Resource description 非數值, 對應"目的", 格式為
 * methodName[index]
 */
public class DebugMointerUI {

    static {
        if (System.getProperty("os.name").toLowerCase().contains("window")) {
            JCommonUtil.forceUIMode(true);
        } else {
            // JCommonUtil.forceUIMode(false);
        }

        // 放有相關必須要載入的classes
        Class<?>[] classes = new Class[] { //
                DebugMointerUI_forCglib.class, //
        }; //
    }

    public static class Constant {
        /**
         * 執行方法 : execute
         */
        public static final String INDICATE_METHOD_NAME = "executeMethodName";
        /**
         * 執行類別 : gtu.rmi.test.Test1
         */
        public static final String INDICATE_CLASS_NAME = "className";
        /**
         * 執行路徑 : C\:\\workspace\\workspace\\WebTest\\target\\classes
         */
        public static final String INDICATE_CLASS_PATH = "classPath";
        /**
         * 指定使用的Parameters 20180224加入
         */
        public static final String INDICATE_PARAMETERS = "parameters";

        /*
         * Map<String,String> map = new HashMap<String,String>();
         * map.put(DebugMointerUI.Constant.INDICATE_CLASS_PATH,
         * "C:/workspace/workspace/WebTest/target/classes");
         * map.put(DebugMointerUI.Constant.INDICATE_CLASS_NAME,
         * "gtu.rmi.test.Test1");
         * map.put(DebugMointerUI.Constant.INDICATE_EXECUTE_METHOD_NAME,
         * "execute"); DebugMointerUI.startWith(map, this);
         */
    }

    static Logger2File log;

    // 重要變數
    // --------------------------------------------------------------------------------------
    private volatile Object[] mointerObjects;// 所有可監看的物件
    private volatile Set<MointerBreakPoint> mointerBreakPointSet = new LinkedHashSet<MointerBreakPoint>();// 監控段點的設定值
    private volatile MointerBreakPoint currentEditMointerBreakPoint;// 目前正在編輯的段點設定
    private volatile StackTraceElement currentBreakPoint;// 當前呼叫的斷點
    private volatile boolean isEnabledUI = true;// 是否顯示UI
    private volatile AtomicBoolean isAutoExecute = new AtomicBoolean(false);// 是否自動化執行外掛
    private volatile Cache<Long, Object> tempModelTimeMointerMap;// 暫存區物件
    private volatile boolean initSysTrayOk;// 系統列初始化
    private volatile Properties configProp = new Properties();
    private volatile SysTrayUtil sysTrayUtil;// 用來產生系統狀態用
    private volatile Object returnObject;// 回傳給呼叫端用
    private volatile AtomicBoolean isExecuteComplete = new AtomicBoolean(false);// 判斷外掛執行是否完成
    private boolean isAutoExecuteMappingMethod = false;// 是否自動化執行外掛
                                                       // (與呼叫端的method相同)
    public volatile boolean isAutoDispose = false;// 是否自動unload UI
    private volatile Map<MenuItem, Callable<String>> sysTrayPopupMenuLabelChangeMap = new HashMap<MenuItem, Callable<String>>();// 設定系統列更新Label的行為
    private volatile Set<SysTrayUtil> keepForRemoveTray = new HashSet<SysTrayUtil>();// 用來初始化時移除多餘系統列圖示
    private volatile ExecuteConfig indicateExecuteConfig;// 自訂執行外掛程式
    private volatile int defaultReturnIndex = -1;// 預設回傳index
    private MethodNameTextHandler methodNameHandler = new MethodNameTextHandler();

    private DebugMointerUIUIContent uiCarrier;

    public final static File configFile;// 設定黨
    static {
        String confName = DebugMointerUI.class.getSimpleName() + "_config.properties";
        File confFile = new File(FileUtil.DESKTOP_PATH, confName);
        if (!new File(FileUtil.DESKTOP_PATH).exists()) {
            confFile = new File(System.getProperty("user.dir"), confName);
        }
        configFile = confFile;
        System.out.println("# configFile : " + configFile);
    }
    // 重要變數
    // --------------------------------------------------------------------------------------

    private static volatile DebugMointerUI inst;

    private static synchronized DebugMointerUI getInstance(boolean createNew) {
        if (inst == null || createNew) {
            inst = new DebugMointerUI();
            cleanSystray();
        }
        inst.uiCarrier = new DebugMointerUIUIContent();
        inst.initConfigProp();// 初始化configProp
        inst.mointerObjects = null;
        inst.returnObject = null;// 清空回傳直
        inst.currentEditMointerBreakPoint = null;// 目前正在編輯的段點設定
        inst.currentBreakPoint = null;// 當前呼叫的斷點
        inst.isExecuteComplete.set(false);// 判斷外掛執行是否完成
        inst.indicateExecuteConfig = null;
        if (inst.sysTrayUtil == null) {
            inst.initSysTrayOk = false;
        }
        // static check
        if (inst.mointerBreakPointSet == null) {
            inst.mointerBreakPointSet = new LinkedHashSet<MointerBreakPoint>();
        }
        if (inst.keepForRemoveTray == null) {
            inst.keepForRemoveTray = new HashSet<SysTrayUtil>();
        }
        initTempModelTimeMointerMap();
        inst.initGUI();
        inst.initGUI_detail();
        return inst;
    }

    public static void main(String[] args) {
        DebugMointerUI.startWith(new DebugMointerUI());
        getLogger().debug("done...");
    }

    // finalize
    // ---------------------------------------------------------------------------------------
    private synchronized static void cleanSystray() {
        if (inst.keepForRemoveTray == null) {
            inst.keepForRemoveTray = new HashSet<SysTrayUtil>();
        }
        for (SysTrayUtil sys : inst.keepForRemoveTray) {
            SysTrayUtil.removeTrayIcon(sys.getTrayIcon());
        }
        inst.keepForRemoveTray.clear();
    }

    private static void cleanAllStaticValue() {
        inst.mointerBreakPointSet = null;// 監控段點的設定值
        inst.tempModelTimeMointerMap = null;
        inst.keepForRemoveTray = null;// 用來初始化時移除多餘系統列圖示
        System.gc();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        cleanSystray();
        cleanAllStaticValue();
    }

    // 進入點
    // --------------------------------------------------------------------------------------
    public static void appendToTempModel(Object wantToAppendObj, char insertUpdateDelete) {
        getLogger().debug("#. appendToTempModel .s");
        if (wantToAppendObj == null) {
            return;
        }
        if (!checkIfNeedRunThisUI()) {
            getLogger().debug("設定為不執行!, run = off");
            disposeTool();
            return;
        }
        inst = getInstance(false);
        // 新增或修改
        switch (insertUpdateDelete) {
        case 'i':
            inst.tempModelTimeMointerMap.asMap().put(System.currentTimeMillis(), wantToAppendObj);
            getLogger().debug("result : insert");
            break;
        case 'u':
            boolean findOk = false;
            for (long key : inst.tempModelTimeMointerMap.asMap().keySet()) {
                Object val = inst.tempModelTimeMointerMap.asMap().get(key);
                if (val != null && val.getClass() == wantToAppendObj.getClass()) {
                    inst.tempModelTimeMointerMap.asMap().put(key, wantToAppendObj);
                    findOk = true;
                    getLogger().debug("result : update");
                    break;
                }
            }
            if (!findOk) {
                inst.tempModelTimeMointerMap.asMap().put(System.currentTimeMillis(), wantToAppendObj);
                getLogger().debug("result : insert");
            }
            break;
        case 'd':
            Long removeKey = null;
            for (long key : inst.tempModelTimeMointerMap.asMap().keySet()) {
                Object val = inst.tempModelTimeMointerMap.asMap().get(key);
                if (wantToAppendObj.equals(val)) {
                    removeKey = key;
                    break;
                }
            }
            if (removeKey != null) {
                inst.tempModelTimeMointerMap.asMap().remove(removeKey);
                getLogger().debug("remove : findOk!");
            } else {
                getLogger().debug("remove : not found!");
            }
            break;
        default:
            throw new RuntimeException("必須為i,u,d");
        }
        // 更新暫存區清單
        inst.uiCarrier.getTempList().setModel(inst.getTempModel());
        getLogger().debug("size : " + inst.uiCarrier.getTempList().getSize());
        getLogger().debug("#. appendToTempModel .e");
    }

    /**
     * 監控物件
     * 
     * @param objects
     *            the objects
     */
    public static <T> T startWith(Object... objects) {
        inst = getInstance(true);
        if (objects == null) {
            objects = new Object[] { null };
        }
        indicateExecuteMethodFilter(objects);
        inst.mointerObjects = ArrayUtils.add(objects, getTempModel());// getTempModel()為暫存區物件
        if (!checkIfNeedRunThisUI()) {
            getLogger().debug("設定為不執行!, run = off");
            disposeTool();
            return (T) getReturnObject();
        }
        getLogger().debug("mointerObjects=>" + Arrays.toString(inst.mointerObjects));
        if (!inst.handleBreakPoint()) {
            return (T) getReturnObject();
        }
        standardProcess();// 主邏輯於此
        return (T) getReturnObject();
    }

    /**
     * 監控物件
     * 
     * @param objects
     *            the objects
     */
    public static <T> T startWithAndDispose(Object... objects) {
        List<Object> lists = new ArrayList<Object>();
        if (objects != null) {
            for (Object v : objects) {
                lists.add(v);
            }
        }
        T rtn = startWith(lists.toArray());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disposeTool();
            }
        }).start();
        return rtn;
    }

    /**
     * 監控物件
     * 
     * @param objects
     *            the objects
     */
    public static <T> T startWithReturn(T object) {
        inst = getInstance(true);
        ModifyObject<T> modifyObj = new ModifyObject<T>();
        modifyObj.object = object;
        inst.mointerObjects = new Object[] { modifyObj, getTempModel() };// getTempModel()為暫存區物件
        getLogger().debug("mointerObjects=>" + Arrays.toString(inst.mointerObjects));
        if (!checkIfNeedRunThisUI()) {
            getLogger().debug("設定為不執行!, run = off");
            disposeTool();
            return object;
        }
        if (!inst.handleBreakPoint()) {
            return object;
        }
        standardProcess();// 主邏輯於此
        ModifyObject<T> rtnObject = (ModifyObject<T>) inst.mointerObjects[0];
        if (rtnObject != null) {
            return (T) rtnObject.object;
        }
        return object;
    }

    /**
     * 取消UI
     */
    public synchronized static void disposeTool() {
        inst = getInstance(false);
        getLogger().debug("DebugMointerUI dispose!!");
        inst.isEnabledUI = true;
        getLogger().close();
        inst.uiCarrier.get_ui().dispose();
        cleanSystray();
        inst.initSysTrayOk = false;
        cleanAllStaticValue();
    }

    /**
     * 監控物件
     * 
     * @param objects
     *            the objects
     */
    public static <T> T startWithReflect(Object... objects) {
        List<Object> lists = new ArrayList<Object>();
        lists.add(getCallerReflectMap(null));
        if (objects != null) {
            for (Object v : objects) {
                lists.add(v);
            }
        }
        return startWith(lists.toArray());
    }

    /**
     * 監控物件
     * 
     * @param objects
     *            the objects
     */
    public static <T> T startWithReflectAndDispose(Object... objects) {
        List<Object> lists = new ArrayList<Object>();
        lists.add(getCallerReflectMap(null));
        if (objects != null) {
            for (Object v : objects) {
                lists.add(v);
            }
        }
        T rtn = startWith(lists.toArray());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disposeTool();
            }
        }).start();
        return rtn;
    }

    /**
     * 監控物件
     * 
     * @param objects
     *            the objects
     */
    public static <T> T startWithReflectAndDisposeIndicateMethod(String method, Object... objects) {
        List<Object> lists = new ArrayList<Object>();
        lists.add(getCallerReflectMap(method));
        if (objects != null) {
            for (Object v : objects) {
                lists.add(v);
            }
        }
        T rtn = startWith(lists.toArray());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                disposeTool();
            }
        }).start();
        return rtn;
    }

    /**
     * 取得呼叫的Class, Method
     */
    private static Map<String, String> getCallerReflectMap(String indicateMethod) {
        Map<String, String> map = new HashMap<String, String>();
        StackTraceElement[] els = Thread.currentThread().getStackTrace();
        boolean findSelf = false;
        for (int ii = 0; ii < els.length; ii++) {
            StackTraceElement e = els[ii];
            // System.out.println(ii + "..." + e.getClassName());
            if (DebugMointerUI.class.getName().equals(e.getClassName())) {
                findSelf = true;
            }
            if (findSelf && !DebugMointerUI.class.getName().equals(e.getClassName())) {
                String classPathName = els[ii].getClassName();
                map.put(Constant.INDICATE_CLASS_NAME, classPathName + "_");
                map.put(Constant.INDICATE_METHOD_NAME, els[ii].getMethodName());
                if (StringUtils.isNotBlank(indicateMethod)) {
                    map.put(Constant.INDICATE_METHOD_NAME, indicateMethod);
                }
                return map;
            }
        }
        return map;
    }

    public void setVisible(boolean visible) {
        uiCarrier.get_ui().setVisible(visible);
    }

    private static void standardProcess() {
        // 重設mointerObject下拉
        inst.initMointerObjects();

        if ((!inst.uiCarrier.get_ui().isVisible() || inst.isAutoExecute.get()) && inst.isEnabledUI) {
            inst.uiCarrier.get_ui().setLocationRelativeTo(null);
            inst.setVisible(!inst.isAutoExecute.get());// 有問題

            if (!inst.isDynamicClassMode()) {
                autoExecutePlugIn();// 讀取外部workspace的classes執行
            } else {
                inst.dynamicExecuteButtonAction();// 使用maven從textarea編譯class執行
            }
        }
        // displayStatus();
    }

    private static Object getReturnObject() {
        if (inst.defaultReturnIndex != -1 && !inst.isExecuteComplete.get() && inst.mointerObjects != null) {
            Object indicateReturn = inst.mointerObjects[inst.defaultReturnIndex];
            getLogger().debug("# defaultReturn = " + indicateReturn);
            return indicateReturn;
        }
        getLogger().debug("# returnObject = " + inst.returnObject);
        return inst.returnObject;
    }

    private static void displayStatus() {
        getLogger().debug("## - isEnabledUI = " + inst.isEnabledUI);
        getLogger().debug("## -gtu.swing.util.JFrameUtil.isVisible( inst) = " + inst.uiCarrier.get_ui().isVisible());
        getLogger().debug("## - isAutoExecute = " + inst.isAutoExecute);
        getLogger().debug("## - inst.isExecuteComplete = " + inst.isExecuteComplete);
        getLogger().debug("## - isAutoExecuteMappingMethod = " + inst.isAutoExecuteMappingMethod);
        getLogger().debug("## - inst.indicateExecuteConfig = " + inst.indicateExecuteConfig);
        getLogger().debug("## - inst.currentEditMointerBreakPoint = " + inst.currentEditMointerBreakPoint);
        getLogger().debug("## - inst.currentBreakPoint = " + inst.currentBreakPoint);
        getLogger().debug("## - inst.returnObject = " + inst.returnObject);
        getLogger().debug("## - inst.defaultReturnIndex = " + inst.defaultReturnIndex);
    }

    private static void indicateExecuteMethodFilter(Object... objects) {
        // 自訂執行method ↓↓↓↓↓↓
        if (objects.length > 0 && //
                objects[0] != null && //
                Map.class.isAssignableFrom(objects[0].getClass())) {
            Map<?, ?> classInfo = (Map<?, ?>) objects[0];
            // 自動執行參數
            ExecuteConfig config = new ExecuteConfig();
            boolean settingOk = false;
            if (classInfo.containsKey(Constant.INDICATE_CLASS_PATH) && //
                    StringUtils.isNotBlank((String) classInfo.get(Constant.INDICATE_CLASS_PATH))) {
                settingOk = true;
                config.classPath = (String) classInfo.get(Constant.INDICATE_CLASS_PATH);
            }
            if (classInfo.containsKey(Constant.INDICATE_CLASS_NAME) && //
                    StringUtils.isNotBlank((String) classInfo.get(Constant.INDICATE_CLASS_NAME))) {
                settingOk = true;
                config.className = (String) classInfo.get(Constant.INDICATE_CLASS_NAME);
            }
            if (classInfo.containsKey(Constant.INDICATE_METHOD_NAME) && //
                    StringUtils.isNotBlank((String) classInfo.get(Constant.INDICATE_METHOD_NAME))) {
                settingOk = true;
                config.executeMethodName = (String) classInfo.get(Constant.INDICATE_METHOD_NAME);
            }
            if (classInfo.containsKey(Constant.INDICATE_PARAMETERS) && //
                    classInfo.get(Constant.INDICATE_PARAMETERS) != null && //
                    classInfo.get(Constant.INDICATE_PARAMETERS).getClass().isArray()) {
                config.indicateParameters = (Object[]) classInfo.get(Constant.INDICATE_PARAMETERS);
            }
            if (settingOk) {
                inst.indicateExecuteConfig = config;
                inst.isAutoExecute.set(true);
            }

            // 預設回傳index
            if (classInfo.containsKey("defaultReturnIndex")) {
                inst.defaultReturnIndex = (Integer) classInfo.get("defaultReturnIndex");
            }
        }
    }

    // 進入點
    // --------------------------------------------------------------------------------------
    private static synchronized void autoExecutePlugIn() {
        if (!inst.isAutoExecute.get()) {
            getLogger().debug("#### \"未設定\"自動執行 !! : [isAutoExecute]=" + inst.isAutoExecute);
            return;
        }
        if (inst.indicateExecuteConfig == null) {
            getLogger().debug("#### 沒有設定任何可以自動執行的邏輯 !!");
            return;
        }
        try {
            ExecuteConfig exe = inst.indicateExecuteConfig;
            textfieldSetValue(inst.uiCarrier.getClassNameText(), StringUtils.trimToEmpty(exe.className));
            textfieldSetValue(inst.uiCarrier.getClasspathText(), _autoExecutePlugIn_getExecutePath());
            textfieldSetValue(inst.uiCarrier.getExecuteMethodNameText(), inst.methodNameHandler.replace(exe.executeMethodName));

            getLogger().debug("##Plugin Info↓↓↓↓↓↓↓-----------------------------------");
            getLogger().debug(String.format("\t時間:%tY/%<tm/%<td %<tH:%<tM:%<tS", System.currentTimeMillis()));
            getLogger().debug("\tclasspathText = " + inst.uiCarrier.getClasspathText().getText());
            getLogger().debug("\tclassNameText = " + inst.uiCarrier.getClassNameText().getText());
            getLogger().debug("\texecuteMethodNameText = " + inst.methodNameHandler.getExecuteMethodNameText());
            getLogger().debug("##Plugin Info↑↑↑↑↑↑↑-----------------------------------");

            inst.importClassBtnActionPerformed(true);
            inst.executeImportClass(true, true);

            inst.uiCarrier.get_ui().setVisible(false);

            if (inst.indicateExecuteConfig != null) {
                inst.isAutoExecute.set(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            inst.isAutoExecute.set(false);// 有問題
            inst.isExecuteComplete.set(false);
            inst.updateSysTrayPopupMenuLabel();
            inst.uiCarrier.get_ui().setVisible(true);

            // JCommonUtil.setFrameAtop(inst, false);

            // 設定動態class資訊
            inst.setDynamicClassArea();
        }
    }

    /**
     * 取得一個不為空的外掛classPath路徑
     */
    private static String _autoExecutePlugIn_getExecutePath() {
        try {
            DefaultListModel model = (DefaultListModel) inst.uiCarrier.getExecuteList().getModel();
            for (int ii = 0; ii < model.getSize(); ii++) {
                ExecuteConfig e = (ExecuteConfig) model.get(ii);
                if (StringUtils.isNotBlank(e.classPath)) {
                    return e.classPath;
                }
            }
            return "";
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 判斷是否為動態載入模式
     */
    private boolean isDynamicClassMode() {
        try {
            textfieldSetValue(inst.uiCarrier.getClassNameText(), inst.indicateExecuteConfig.className);
            textfieldSetValue(inst.uiCarrier.getClasspathText(), inst.indicateExecuteConfig.classPath);
            textfieldSetValue(inst.uiCarrier.getExecuteMethodNameText(), methodNameHandler.replace(inst.indicateExecuteConfig.executeMethodName));

            setDynamicClassArea();
            dynamicLoadingConfigAction(true);

            File targetDir = new File(uiCarrier.getDynamic_classesDirText().getText());
            if (!targetDir.exists()) {
                return false;
            }

            String classPath = uiCarrier.getDynamic_classPathText().getText();
            String className = uiCarrier.getDynamic_classNameText().getText();
            classPath = classPath.replace('.', '/');
            // File classFile = new File(targetDir + "/" + classPath + "/" +
            // className + ".class");
            File classFile = ClassCompilerUtil.getInstance().getClassFile(classPath, className, targetDir);
            if (classFile.exists()) {
                return true;
            }
            return false;
        } catch (Throwable ex) {
            // JCommonUtil.handleException(ex);
            return false;
        }
    }

    /**
     * 動態載入class
     */
    private boolean dynamicLoadingConfigAction(boolean silentMode) {
        try {
            // 目的目錄
            File dynamicTargetDir = null;
            final String DYNAMIC_TARGET_DIR_KEY = "dynamic_target_dir";
            if (StringUtils.isBlank(uiCarrier.getDynamic_classesDirText().getText())) {
                String path = inst.configProp.getProperty(DYNAMIC_TARGET_DIR_KEY);
                if (StringUtils.isBlank(path)) {
                    Validate.isTrue(false, "classes目錄未設定");
                }
                uiCarrier.getDynamic_classesDirText().setText(path);
                dynamicTargetDir = new File(path);
            } else {
                dynamicTargetDir = new File(uiCarrier.getDynamic_classesDirText().getText());
            }

            // JAVA_HOME
            File dynamicJavaHomeDir = null;
            final String DYNAMIC_JAVA_HOME_KEY = "dynamic_java_home";
            if (StringUtils.isBlank(uiCarrier.getDynamic_javaHomeText().getText())) {
                String path = inst.configProp.getProperty(DYNAMIC_JAVA_HOME_KEY);
                if (StringUtils.isBlank(path)) {
                    Validate.isTrue(false, "JAVA_HOME目錄未設定");
                }
                uiCarrier.getDynamic_javaHomeText().setText(path);
                dynamicJavaHomeDir = new File(path);
            } else {
                dynamicJavaHomeDir = new File(uiCarrier.getDynamic_javaHomeText().getText());
            }

            // maven專案目錄設定
            File dynamicMvnProjectDir = null;
            final String DYNAMIC_MVN_PROJECT_DIR_KEY = "dynamic_mvn_project_dir";
            if (StringUtils.isBlank(uiCarrier.getDynamic_mvnProjectText().getText())) {
                String path = inst.configProp.getProperty(DYNAMIC_MVN_PROJECT_DIR_KEY);
                if (StringUtils.isBlank(path)) {
                    // throw new RuntimeException("Mvn Project目錄未設定");
                } else {
                    uiCarrier.getDynamic_mvnProjectText().setText(path);
                    dynamicMvnProjectDir = new File(path);
                }
            } else {
                dynamicMvnProjectDir = new File(uiCarrier.getDynamic_mvnProjectText().getText());
            }

            String className = uiCarrier.get_ui().getTitle().toString().replace('.', '_').replace(':', '_');
            uiCarrier.getDynamic_classNameText().setText(className);

            String javaContent = uiCarrier.getDynamicClassArea().getText();
            if (StringUtils.isBlank(javaContent)) {
                return false;
            }

            String replaceFromClassName = StringUtils.defaultString(uiCarrier.getClassNameText().getText());
            String repClassName = replaceFromClassName.substring(replaceFromClassName.lastIndexOf(".") + 1);
            String packagePath = replaceFromClassName.substring(0, replaceFromClassName.lastIndexOf("."));

            uiCarrier.getDynamic_classPathText().setText(packagePath);

            javaContent = javaContent.replaceAll(repClassName, className);

            System.out.println("repClassName - " + repClassName);
            System.out.println("packagePath - " + packagePath);

            String packagePathForFile = packagePath.replace('.', '/');
            File javaFile = new File(dynamicTargetDir + "/" + packagePathForFile + "/", className + ".java");

            if (javaFile.exists()) {
                javaContent = FileUtil.loadFromFile(javaFile, "utf8");
            }

            uiCarrier.getRunningClassArea().setText(javaContent);
            return true;
        } catch (Throwable ex) {
            System.out.println("silentMode =" + silentMode);
            if (silentMode == false) {
                JCommonUtil.handleException(ex);
            }
            return false;
        }
    }

    /**
     * 動態載入class
     */
    private void dynamicExecuteButtonAction() {
        try {
            String javaContent = uiCarrier.getRunningClassArea().getText();
            String packagePath = uiCarrier.getDynamic_classPathText().getText();
            String className = uiCarrier.getDynamic_classNameText().getText();
            File targetDir = new File(uiCarrier.getDynamic_classesDirText().getText());
            File javaHome = new File(uiCarrier.getDynamic_javaHomeText().getText());
            File mvnProjectDir = new File(uiCarrier.getDynamic_mvnProjectText().getText());

            Validate.notEmpty(packagePath, "請先點選物件概觀/類別範例");
            Validate.notEmpty(className, "請先點選物件概觀/類別範例");

            String compareJavaContent = FileUtil.loadFromFile(ClassCompilerUtil.getInstance().getJavaFile(packagePath, className, targetDir), "utf8");
            File classFile = ClassCompilerUtil.getInstance().getClassFile(packagePath, className, targetDir);
            if (!classFile.exists() || !StringUtils.equals(compareJavaContent, javaContent)) {

                // 產生maven專案classpath檔
                String classpathStr = loadMvnClasspathStr(mvnProjectDir, targetDir);

                // 編譯class
                classFile = dynamicGetCompileClass(javaContent, packagePath, className, classpathStr, targetDir, javaHome);
                if (classFile == null) {
                    Validate.isTrue(false, "編譯時間超時");
                }
                getLogger().debug("★★重新編譯 : " + classFile);
            }

            String classNameZ = packagePath + "." + className;
            String classPath = targetDir.getAbsolutePath();
            String methodName = StringUtils.defaultString(methodNameHandler.getExecuteMethodNameText());

            textfieldSetValue(inst.uiCarrier.getClassNameText(), classNameZ);
            textfieldSetValue(inst.uiCarrier.getClasspathText(), classPath);
            textfieldSetValue(inst.uiCarrier.getExecuteMethodNameText(), methodNameHandler.replace(methodName));

            getLogger().debug("##Plugin Info↓↓↓↓↓↓↓-----------------------------------");
            getLogger().debug(String.format("\t時間:%tY/%<tm/%<td %<tH:%<tM:%<tS", System.currentTimeMillis()));
            getLogger().debug("\tclasspathText = " + inst.uiCarrier.getClasspathText().getText());
            getLogger().debug("\tclassNameText = " + inst.uiCarrier.getClassNameText());
            getLogger().debug("\texecuteMethodNameText = " + inst.methodNameHandler.getExecuteMethodNameText());
            getLogger().debug("##Plugin Info↑↑↑↑↑↑↑-----------------------------------");

            inst.importClassBtnActionPerformed(true);
            inst.executeImportClass(true, false);

            inst.uiCarrier.get_ui().setVisible(false);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            inst.uiCarrier.get_ui().setVisible(true);
        }
    }

    /**
     * 取得編譯class
     */
    private File dynamicGetCompileClass(final String javaContent, final String packagePath, final String className, final String classpathStr, final File targetDir, final File javaHome) {
        final int TIMEOUT = 30000;
        ExecutorService service = Executors.newCachedThreadPool();
        Future<File> future = service.submit(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return ClassCompilerUtil.getInstance().createDynamicClass(javaContent, packagePath, className, classpathStr, targetDir, javaHome);
            }
        });
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            future.cancel(true);
            return null;
        }
    }

    /**
     * 取得Mvn classpath
     */
    private String loadMvnClasspathStr(File mvnProjectDir, File targetDir) {
        if (mvnProjectDir != null && mvnProjectDir.exists()) {
            File classpathFile = new File(targetDir, "classpath.txt");
            if (classpathFile != null && !classpathFile.exists()) {
                if (targetDir != null && targetDir.exists() && targetDir.isDirectory()) {
                    if (!classpathFile.exists()) {
                        ClassCompilerUtil.generateClasspathByMaven(mvnProjectDir, classpathFile);
                        getLogger().debug("★★使用Maven專案classpath");
                        return FileUtil.loadFromFile(classpathFile, "utf8");
                    }
                }
            }
        }
        return "";
    }

    /**
     * 設定系統參數
     */
    private void setConfigPropProperties(String key, String value) {
        String val = null;
        if (inst.configProp.containsKey(key)) {
            val = inst.configProp.getProperty(key);
        }
        if (!StringUtils.equals(value, val)) {
            inst.configProp.setProperty(key, value);
            try {
                inst.configProp.store(new FileOutputStream(inst.configFile), uiCarrier.get_ui().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void textfieldSetValue(JTextFieldKeeper textField, String value) {
        if (StringUtils.isBlank(value)) {
            return;
        }
        textField.setText(value);
    }

    private DebugMointerUI() {
        super();
    }

    private void initGUI() {
        try {
            uiCarrier.initUI();

            uiCarrier.get_ui().setTitle("程式監控管理員 by gtu001");
            BorderLayout thisLayout = new BorderLayout();
            uiCarrier.get_ui().getContentPane().setLayout(thisLayout);
            uiCarrier.get_ui().addWindowListener(new WindowAdapter() {
                public void windowDeactivated(WindowEvent e) {
                    // 縮小視窗
                    if (isAutoDispose) {
                        getLogger().debug("自動 unload !!");
                        disposeTool();
                    }
                }
            });
            {
                uiCarrier.get_ui().getContentPane().add(uiCarrier.getjTabbedPane1(), BorderLayout.CENTER);
                uiCarrier.getjTabbedPane1().addChangeListener(new ChangeListener() {
                    public void stateChanged(ChangeEvent evt) {
                        if (uiCarrier.getjTabbedPane1().getSelectedIndex() == 4) {// 暫存區管理
                            uiCarrier.getTempList().setModel(getTempModel());
                        } else if (uiCarrier.getjTabbedPane1().getSelectedIndex() == 7) {// 動態類別
                            dynamicLoadingConfigAction(false);
                        }
                        System.out.println("tabIndex : " + uiCarrier.getjTabbedPane1().getSelectedIndex());
                    }
                });
                {
                    BorderLayout jPanel1Layout = new BorderLayout();
                    uiCarrier.getjPanel1().setLayout(jPanel1Layout);
                    uiCarrier.getjTabbedPane1().addTab("主物件", null, uiCarrier.getjPanel1(), null);
                    {
                        uiCarrier.getjPanel1().add(uiCarrier.getjScrollPane1(), BorderLayout.CENTER);
                        uiCarrier.getjScrollPane1().setPreferredSize(new java.awt.Dimension(648, 349));
                        {
                            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
                            DefaultTreeModel model = new DefaultTreeModel(rootNode);
                            uiCarrier.getCompositeTree().setModel(model);
                            uiCarrier.getjScrollPane1().setViewportView(uiCarrier.getCompositeTree());
                            uiCarrier.getCompositeTree().addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    compositeTreeKeyPressed(evt);
                                }
                            });
                            uiCarrier.setJtreeUtil(JTreeUtil.newInstance(uiCarrier.getCompositeTree()));
                            uiCarrier.getCompositeTree().addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    compositeTreeMouseClicked(evt);
                                }
                            });
                        }
                    }
                    {
                        uiCarrier.getjPanel1().add(uiCarrier.getjPanel3(), BorderLayout.NORTH);
                        uiCarrier.getjPanel3().setPreferredSize(new java.awt.Dimension(648, 36));
                        {
                            uiCarrier.getjPanel3().add(uiCarrier.getjLabel1());
                            uiCarrier.getjLabel1().setText("監控物件");
                        }
                        {
                            DefaultComboBoxModel moniterComboBoxModel = new DefaultComboBoxModel();
                            uiCarrier.getjPanel3().add(uiCarrier.getMoniterComboBox());
                            uiCarrier.getMoniterComboBox().addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    moniterComboBoxActionPerformed(evt);
                                }
                            });
                            uiCarrier.getMoniterComboBox().setModel(moniterComboBoxModel);
                        }
                    }
                }
                {
                    BorderLayout jPanel2Layout = new BorderLayout();
                    uiCarrier.getjPanel2().setLayout(jPanel2Layout);
                    uiCarrier.getjTabbedPane1().addTab("節點內容", null, uiCarrier.getjPanel2(), null);
                    {
                        FlowLayout jPanel4Layout = new FlowLayout();
                        uiCarrier.getjPanel4().setLayout(jPanel4Layout);
                        uiCarrier.getjPanel2().add(uiCarrier.getjPanel4(), BorderLayout.NORTH);
                        uiCarrier.getjPanel4().setPreferredSize(new java.awt.Dimension(648, 46));

                        // 以指定類別顯示細節 ↓↓↓↓↓↓
                        DefaultComboBoxModel nodeInfoComboBoxModel = new DefaultComboBoxModel();
                        uiCarrier.getNodeInfoComboBox().setModel(nodeInfoComboBoxModel);
                        uiCarrier.getNodeInfoComboBox().addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent evt) {
                                Object clz = uiCarrier.getNodeInfoComboBox().getSelectedItem();
                                if (!(clz instanceof Class)) {
                                    return;
                                }
                                appendInChrildren(getSelectNodeObject(), null, (Class<?>) clz);
                            }
                        });

                        uiCarrier.getjPanel4().add(uiCarrier.getNodeInfoComboBox());
                        uiCarrier.getjPanel4().add(getIndicateNodeClassBtn());
                    }
                    {
                        uiCarrier.getjPanel2().add(uiCarrier.getjScrollPane2(), BorderLayout.CENTER);
                        uiCarrier.getjScrollPane2().setPreferredSize(new java.awt.Dimension(648, 352));
                        {
                            uiCarrier.getjScrollPane2().setViewportView(uiCarrier.getCompositeTable());
                            uiCarrier.getCompositeTable().setModel(JTableUtil.createModel(true, "名稱", "資料"));
                        }
                    }
                }
                {
                    uiCarrier.getjTabbedPane1().addTab("監控條件", null, uiCarrier.getjPanel5(), null);
                    BorderLayout jPanel5Layout = new BorderLayout();
                    uiCarrier.getjPanel5().setLayout(jPanel5Layout);
                    {
                        BorderLayout jPanel6Layout = new BorderLayout();
                        uiCarrier.getjPanel6().setLayout(jPanel6Layout);
                        uiCarrier.getjPanel5().add(uiCarrier.getjPanel6(), BorderLayout.NORTH);
                        uiCarrier.getjPanel6().setPreferredSize(new java.awt.Dimension(648, 80));
                        {
                            uiCarrier.getjPanel6().add(uiCarrier.getConditionActivationBtn(), BorderLayout.EAST);
                            // default -------------------------
                            // conditionActivationBtn.setText("enable");
                            // conditionActivationBtn.setSelected(true);
                            // default -------------------------
                            uiCarrier.getConditionActivationBtn().setPreferredSize(new java.awt.Dimension(100, 80));
                            uiCarrier.getConditionActivationBtn().addFocusListener(new FocusAdapter() {
                                public void focusLost(FocusEvent evt) {
                                    updateCurrentMointerBreakPoint(evt);
                                }
                            });
                            uiCarrier.getConditionActivationBtn().addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    JCommonUtil.setJToggleButtonText(uiCarrier.getConditionActivationBtn(), new String[] { "enable", "disable" });
                                }
                            });
                        }
                        {
                            uiCarrier.getjPanel6().add(uiCarrier.getjScrollPane3(), BorderLayout.CENTER);
                            uiCarrier.getjScrollPane3().setPreferredSize(new java.awt.Dimension(548, 80));
                            {
                                uiCarrier.getjScrollPane3().setViewportView(uiCarrier.getConditionArea());
                                uiCarrier.getConditionArea().addFocusListener(new FocusAdapter() {
                                    public void focusLost(FocusEvent evt) {
                                        updateCurrentMointerBreakPoint(evt);
                                    }
                                });
                            }
                        }
                    }
                    {
                        uiCarrier.getjPanel5().add(uiCarrier.getjScrollPane4(), BorderLayout.CENTER);
                        uiCarrier.getjScrollPane4().setPreferredSize(new java.awt.Dimension(648, 325));
                        {
                            uiCarrier.getjScrollPane4().setViewportView(uiCarrier.getBreakpointTable());
                            uiCarrier.getBreakpointTable().addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    breakpointTableMouseClicked(evt);
                                }
                            });
                            DefaultTableModel model = JTableUtil.createModel(true, "程式行數", "條件", "符合", "啟用");
                            uiCarrier.getBreakpointTable().setModel(model);
                        }
                    }
                }
                {
                    BorderLayout jPanel7Layout = new BorderLayout();
                    uiCarrier.getjPanel7().setLayout(jPanel7Layout);
                    uiCarrier.getjTabbedPane1().addTab("變數", null, uiCarrier.getjPanel7(), null);
                    {
                        uiCarrier.getjPanel7().add(uiCarrier.getjScrollPane5(), BorderLayout.CENTER);
                        uiCarrier.getjScrollPane5().setPreferredSize(new java.awt.Dimension(648, 405));
                        {
                            uiCarrier.getjScrollPane5().setViewportView(uiCarrier.getParameterTable());
                            DefaultTableModel model = JTableUtil.createModel(false, "變數", "值");
                            uiCarrier.getParameterTable().setModel(model);
                            uiCarrier.getParameterTable().addFocusListener(new FocusAdapter() {
                                public void focusGained(FocusEvent evt) {
                                    updateParameter(evt);
                                }

                                public void focusLost(FocusEvent evt) {
                                    updateParameter(evt);
                                }
                            });
                            uiCarrier.getParameterTable().getTableHeader().addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    final JMenuItem importItem = new JMenuItem("匯入參數設定");
                                    importItem.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                File file = JCommonUtil._jFileChooser_selectFileOnly();
                                                if (file == null) {
                                                    JCommonUtil._jOptionPane_showMessageDialog_error_NonUICompatible("檔案錯誤!");
                                                    return;
                                                }
                                                DefaultTableModel model = (DefaultTableModel) uiCarrier.getParameterTable().getModel();
                                                Properties prop = new Properties();
                                                prop.load(new FileInputStream(file));
                                                for (Object okey : prop.keySet()) {
                                                    model.addRow(new Object[] { (String) okey, "" });
                                                }
                                            } catch (Exception ex) {
                                                JCommonUtil.handleException(ex);
                                                showErrorLogInArea(ex);
                                            }
                                        }
                                    });
                                    final JMenuItem exportItem = new JMenuItem("匯出參數設定");
                                    exportItem.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                File file = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
                                                if (file == null) {
                                                    JCommonUtil._jOptionPane_showMessageDialog_error_NonUICompatible("檔案錯誤!");
                                                    return;
                                                }
                                                DefaultTableModel model = (DefaultTableModel) uiCarrier.getParameterTable().getModel();
                                                Properties prop = new Properties();
                                                for (int ii = 0; ii < model.getRowCount(); ii++) {
                                                    String val = (String) uiCarrier.getParameterTable().getValueAt(ii, 0);
                                                    prop.put(val, "");
                                                }
                                                prop.store(new FileOutputStream(file), "參數黨");
                                            } catch (Exception ex) {
                                                JCommonUtil.handleException(ex);
                                                showErrorLogInArea(ex);
                                            }
                                        }
                                    });
                                    final JMenuItem cleanAllItem = new JMenuItem("全部清空");
                                    cleanAllItem.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                uiCarrier.getParameterTable().setModel(JTableUtil.createModel(false, "變數", "值"));
                                            } catch (Exception ex) {
                                                JCommonUtil.handleException(ex);
                                                showErrorLogInArea(ex);
                                            }
                                        }
                                    });

                                    JTableUtil util = JTableUtil.newInstance(uiCarrier.getParameterTable());
                                    JPopupMenuUtil.newInstance(uiCarrier.getParameterTable()).applyEvent(evt)//
                                            .addJMenuItem(util.jMenuItem_addRow(false, null))//
                                            .addJMenuItem(importItem)//
                                            .addJMenuItem(exportItem)//
                                            .addJMenuItem(cleanAllItem)//
                                            .show();
                                }
                            });
                            uiCarrier.getParameterTable().addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    JTableUtil util = JTableUtil.newInstance(uiCarrier.getParameterTable());
                                    JPopupMenuUtil.newInstance(uiCarrier.getParameterTable()).applyEvent(evt)//
                                            .addJMenuItem(util.jMenuItem_addRow(false, null))//
                                            .addJMenuItem(util.jMenuItem_removeRow(null))//
                                            .show();
                                }
                            });
                        }
                    }
                }
                {
                    BorderLayout jPanel9Layout = new BorderLayout();
                    uiCarrier.getjPanel9().setLayout(jPanel9Layout);
                    uiCarrier.getjTabbedPane1().addTab("暫存區管理", null, uiCarrier.getjPanel9(), null);
                    {
                        FlowLayout jPanel10Layout = new FlowLayout();
                        uiCarrier.getjPanel9().add(uiCarrier.getjPanel10(), BorderLayout.NORTH);
                        uiCarrier.getjPanel10().setPreferredSize(new java.awt.Dimension(648, 60));
                        uiCarrier.getjPanel10().setLayout(jPanel10Layout);
                        {
                            uiCarrier.getjPanel10().add(uiCarrier.getjLabel2());
                            uiCarrier.getjLabel2().setText("\u5efa\u7acb\u57fa\u672c\u7269\u4ef6");
                        }
                        {
                            DefaultComboBoxModel tempValueComboxModel = new DefaultComboBoxModel();
                            for (TempValueEnum e : TempValueEnum.values()) {
                                tempValueComboxModel.addElement(e);
                            }
                            uiCarrier.getjPanel10().add(uiCarrier.getTempValueCombox());
                            uiCarrier.getTempValueCombox().setModel(tempValueComboxModel);
                        }
                        {
                            uiCarrier.getjPanel10().add(uiCarrier.getTempValueSetText());
                            uiCarrier.getTempValueSetText().setPreferredSize(new java.awt.Dimension(346, 24));
                        }
                        {
                            uiCarrier.getjPanel10().add(uiCarrier.getTempValueSetBtn());

                            DefaultComboBoxModel tempObjClassComboxModel = new DefaultComboBoxModel();
                            uiCarrier.getTempObjClassCombox().addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    try {
                                        Object object = JListUtil.getLeadSelectionObject(uiCarrier.getTempList());
                                        if (object == null) {
                                            JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("請選擇暫存區物件");
                                            return;
                                        }
                                        showInfoAtTempTable(object, (Class<?>) uiCarrier.getTempObjClassCombox().getSelectedItem());
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex, false);
                                        showErrorLogInArea(ex);
                                    }
                                }
                            });
                            uiCarrier.getTempObjClassCombox().setModel(tempObjClassComboxModel);

                            uiCarrier.getjPanel10().add(uiCarrier.getTempObjClassCombox());
                            uiCarrier.getTempValueSetBtn().setText("新增至暫存區");// 設定至當前
                            uiCarrier.getTempValueSetBtn().addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    try {
                                        // int pos =
                                        // tempList.getSelectedIndex();
                                        // Validate.isTrue(pos != -1, "未選擇項目!");
                                        Validate.notEmpty(uiCarrier.getTempValueSetText().getText());
                                        String tempStr = uiCarrier.getTempValueSetText().getText();
                                        TempValueEnum tempValueEnum = (TempValueEnum) uiCarrier.getTempValueCombox().getSelectedItem();
                                        // getTempModel().setElementAt(tempValueEnum.applyString(tempStr),
                                        // pos);
                                        appendToTempModel(tempValueEnum.applyString(tempStr), 'i');
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                        showErrorLogInArea(ex);
                                    }
                                }
                            });
                        }
                    }
                    {
                        uiCarrier.getjPanel9().add(uiCarrier.getjScrollPane7(), BorderLayout.CENTER);
                        uiCarrier.getjScrollPane7().setPreferredSize(new java.awt.Dimension(648, 345));
                        {
                            uiCarrier.getjScrollPane7().setViewportView(uiCarrier.getTempValueTable());
                            uiCarrier.getTempValueTable().addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    try {
                                        if (evt.getButton() != MouseEvent.BUTTON1 || evt.getClickCount() != 2) {
                                            return;
                                        }
                                        DefaultTableModel model = JTableUtil.newInstance(uiCarrier.getTempValueTable()).getModel();
                                        Object value = model.getValueAt(JTableUtil.newInstance(uiCarrier.getTempValueTable()).getSelectedRow(), 0);
                                        if (value instanceof JObject) {
                                            JObject obj = (JObject) value;
                                            if (obj.field != null) {
                                                setParseStringToField(obj);
                                            } else if (obj.method != null) {
                                                Object tempValue = methodInvoke(obj);
                                                boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(//
                                                        "是否要將結果資料新增至暫存區:\n" + tempValue, "設定暫存資料");
                                                if (result) {
                                                    appendToTempModel(tempValue, 'u');
                                                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("設定資料到暫存區:" + tempValue + ",成功!");
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        JCommonUtil.handleException(e);
                                        showErrorLogInArea(e);
                                    }
                                }
                            });
                            uiCarrier.getTempValueTable().setModel(JTableUtil.createModel(false, "變數", "值"));
                        }
                    }
                    {
                        uiCarrier.getjPanel9().add(uiCarrier.getjScrollPane8(), BorderLayout.EAST);
                        uiCarrier.getjScrollPane8().setPreferredSize(new java.awt.Dimension(154, 345));
                        {
                            uiCarrier.getjScrollPane8().setViewportView(uiCarrier.getTempList());
                            uiCarrier.getTempList().setModel(getTempModel());
                            uiCarrier.getTempList().addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent evt) {
                                    try {
                                        if (evt.getButton() == MouseEvent.BUTTON1) {
                                            Object object = JListUtil.getLeadSelectionObject(uiCarrier.getTempList());
                                            showInfoAtTempTable(object, null);
                                        } else if (evt.getButton() == MouseEvent.BUTTON3) {
                                            JPopupMenuUtil.newInstance(uiCarrier.getTempList()).applyEvent(evt)//
                                                    .addJMenuItem("刪除選擇項目", new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            Object object = JListUtil.getLeadSelectionObject(uiCarrier.getTempList());
                                                            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(//
                                                                    ReflectionToStringBuilder.toString(object, ToStringStyle.MULTI_LINE_STYLE) + //
                                                            "\n是否要刪除?", "將選擇項目刪除");
                                                            if (result) {
                                                                appendToTempModel(object, 'd');
                                                                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("刪除成功!");
                                                            }
                                                        }
                                                    }).addJMenuItem("複製選擇項目並新增", new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            Object object = JListUtil.getLeadSelectionObject(uiCarrier.getTempList());
                                                            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(//
                                                                    ReflectionToStringBuilder.toString(object, ToStringStyle.MULTI_LINE_STYLE) + //
                                                            "\n是否要複製後新增這個項目?", "複製後新增項目");
                                                            if (result) {
                                                                Serializable serialObj = SerializationUtils.clone((Serializable) object);
                                                                appendToTempModel(serialObj, 'i');
                                                                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("複製成功!");
                                                            }
                                                        }
                                                    }).addJMenuItem("設定為回傳值", new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            Object object = JListUtil.getLeadSelectionObject(uiCarrier.getTempList());
                                                            boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(//
                                                                    ReflectionToStringBuilder.toString(object, ToStringStyle.MULTI_LINE_STYLE) + //
                                                            "\n是否要設定?", "將選擇項目回傳");
                                                            if (result) {
                                                                inst.returnObject = object;
                                                                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("設定成功!");
                                                            }
                                                        }
                                                    }).show();
                                        }
                                    } catch (Exception ex) {
                                        JCommonUtil.handleException(ex);
                                        showErrorLogInArea(ex);
                                    }
                                }
                            });
                            uiCarrier.getTempList().addKeyListener(new KeyAdapter() {
                                public void keyPressed(KeyEvent evt) {
                                    // JListUtil.newInstance(tempList).defaultJListKeyPressed(evt);
                                    Object objects[] = uiCarrier.getTempList().getSelectedValues();
                                    if (objects == null || objects.length == 0) {
                                        return;
                                    }
                                    DefaultListModel model = (DefaultListModel) uiCarrier.getTempList().getModel();
                                    int lastIndex = model.getSize() - 1;
                                    Object swap = null;
                                    for (Object current : objects) {
                                        int index = model.indexOf(current);
                                        switch (evt.getKeyCode()) {
                                        case 38:// up
                                            if (index != 0) {
                                                swap = model.getElementAt(index - 1);
                                                model.setElementAt(swap, index);
                                                model.setElementAt(current, index - 1);
                                            }
                                            break;
                                        case 40:// down
                                            if (index != lastIndex) {
                                                swap = model.getElementAt(index + 1);
                                                model.setElementAt(swap, index);
                                                model.setElementAt(current, index + 1);
                                            }
                                            break;
                                        case 127:// del
                                            model.removeElementAt(index);
                                            appendToTempModel(current, 'd');
                                            break;
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
                {
                    BorderLayout jPanel11Layout = new BorderLayout();
                    uiCarrier.getjPanel11().setLayout(jPanel11Layout);
                    uiCarrier.getjTabbedPane1().addTab("掛載類別", null, uiCarrier.getjPanel11(), null);
                    {
                        uiCarrier.getjPanel11().add(uiCarrier.getjPanel12(), BorderLayout.NORTH);
                        uiCarrier.getjPanel12().setPreferredSize(new java.awt.Dimension(648, 95));
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getjLabel3());
                            uiCarrier.getjLabel3().setText("\u8a2d\u5b9aclasspath");
                        }
                        {
                            JCommonUtil.jTextFieldSetFilePathMouseEvent(uiCarrier.getClasspathText().get(), true);
                            uiCarrier.getjPanel12().add(uiCarrier.getClasspathText().get());

                            uiCarrier.getTempModelCheckBox().setText("\u66ab\u5b58\u5340\u6aa2\u67e5");
                            uiCarrier.getTempModelCheckBox().setSelected(true);

                            uiCarrier.getjPanel12().add(uiCarrier.getTempModelCheckBox());
                            uiCarrier.getClasspathText().get().setPreferredSize(new java.awt.Dimension(526, 24));
                        }
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getjLabel4());
                            uiCarrier.getjLabel4().setText("\u985e\u5225\u540d\u7a31");
                        }
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getClassNameText().get());
                            uiCarrier.getClassNameText().get().setPreferredSize(new java.awt.Dimension(461, 24));
                        }
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getImportClassBtn());
                            uiCarrier.getImportClassBtn().setText("\u8f09\u5165\u985e\u5225");
                            uiCarrier.getImportClassBtn().addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    importClassBtnActionPerformed(false);
                                }
                            });
                        }
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getjLabel5());
                            uiCarrier.getjLabel5().setText("\u57f7\u884cmethod");
                            uiCarrier.getjLabel5().setPreferredSize(new java.awt.Dimension(85, 17));
                        }
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getExecuteMethodNameText().get());
                            uiCarrier.getExecuteMethodNameText().get().setPreferredSize(new java.awt.Dimension(424, 24));
                        }
                        {
                            uiCarrier.getjPanel12().add(uiCarrier.getExecuteMethodBtn());
                            uiCarrier.getExecuteMethodBtn().setText("\u57f7\u884c");
                            uiCarrier.getExecuteMethodBtn().addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent evt) {
                                    executeImportClass(false, true);
                                }
                            });
                        }
                    }
                    {
                        BorderLayout jPanel16Layout = new BorderLayout();
                        uiCarrier.getjPanel16().setLayout(jPanel16Layout);
                        uiCarrier.getjPanel11().add(uiCarrier.getjPanel16(), BorderLayout.CENTER);
                        {
                            uiCarrier.getjPanel16().add(uiCarrier.getjScrollPane12(), BorderLayout.CENTER);
                            uiCarrier.getjScrollPane12().setPreferredSize(new java.awt.Dimension(648, 310));
                            {
                                ListModel exeucteListModel = new DefaultListModel();
                                uiCarrier.getjScrollPane12().setViewportView(uiCarrier.getExecuteList().get());
                                uiCarrier.getExecuteList().get().addFocusListener(new FocusAdapter() {
                                    public void focusLost(FocusEvent evt) {
                                        try {
                                            saveAndAddExecuteConfig(false);
                                        } catch (Exception ex) {
                                            JCommonUtil.handleException(ex);
                                            showErrorLogInArea(ex);
                                        }
                                    }
                                });
                                uiCarrier.getExecuteList().get().setModel(exeucteListModel);
                                uiCarrier.getExecuteList().get().addMouseListener(new MouseAdapter() {
                                    public void mouseClicked(MouseEvent evt) {
                                        ExecuteConfig exe = (ExecuteConfig) JListUtil.getLeadSelectionObject(uiCarrier.getExecuteList().get());
                                        if (exe == null) {
                                            return;
                                        }
                                        uiCarrier.getClassNameText().setText(StringUtils.trimToEmpty(exe.className));
                                        uiCarrier.getClasspathText().setText(StringUtils.trimToEmpty(exe.classPath));
                                        methodNameHandler.setExecuteMethodNameText(StringUtils.trimToEmpty(exe.executeMethodName));

                                        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                                            // 直接執行
                                            importClassBtnActionPerformed(true);
                                            executeImportClass(true, true);
                                        }
                                    }
                                });
                                uiCarrier.getExecuteList().get().addKeyListener(new KeyAdapter() {
                                    public void keyPressed(KeyEvent evt) {
                                        JListUtil.newInstance(uiCarrier.getExecuteList().get()).defaultJListKeyPressed(evt);
                                    }
                                });
                            }
                        }
                    }
                }
                {
                    BorderLayout jPanel13Layout = new BorderLayout();
                    uiCarrier.getjPanel13().setLayout(jPanel13Layout);
                    uiCarrier.getjTabbedPane1().addTab("物件概觀", null, uiCarrier.getjPanel13(), null);
                    {
                        uiCarrier.getjPanel13().add(uiCarrier.getjScrollPane9(), BorderLayout.CENTER);
                        uiCarrier.getjScrollPane9().setPreferredSize(new java.awt.Dimension(648, 405));
                        {
                            uiCarrier.getjScrollPane9().setViewportView(uiCarrier.getjTabbedPane2());
                            {
                                BorderLayout jPanel14Layout = new BorderLayout();
                                uiCarrier.getjPanel14().setLayout(jPanel14Layout);
                                uiCarrier.getjTabbedPane2().addTab("主物件", null, uiCarrier.getjPanel14(), null);
                                {
                                    uiCarrier.getjPanel14().add(uiCarrier.getjScrollPane10(), BorderLayout.CENTER);
                                    uiCarrier.getjScrollPane10().setPreferredSize(new java.awt.Dimension(640, 373));
                                    {
                                        uiCarrier.getjScrollPane10().setViewportView(uiCarrier.getShowInfoArea());
                                        uiCarrier.getShowInfoArea().setEditable(false);
                                        uiCarrier.getShowInfoArea().addMouseListener(new MouseAdapter() {
                                            public void mouseClicked(MouseEvent evt) {
                                                showInfoAreaMouseClicked(evt);
                                            }
                                        });
                                    }
                                }
                            }
                            {
                                BorderLayout jPanel15Layout = new BorderLayout();
                                uiCarrier.getjPanel15().setLayout(jPanel15Layout);
                                uiCarrier.getjTabbedPane2().addTab("變數", null, uiCarrier.getjPanel15(), null);
                                uiCarrier.getjTabbedPane2().addTab("呼叫順序", null, inst.uiCarrier.getCallStackArea(), null);
                                uiCarrier.getjTabbedPane2().addTab("錯誤Log", null, inst.uiCarrier.getErrorLogArea(), null);
                                {
                                    uiCarrier.getjPanel15().add(uiCarrier.getjScrollPane11(), BorderLayout.CENTER);
                                    uiCarrier.getjScrollPane11().setPreferredSize(new java.awt.Dimension(640, 373));
                                    {
                                        uiCarrier.getjScrollPane11().setViewportView(uiCarrier.getShowInfoArea2());
                                        uiCarrier.getShowInfoArea2().setEditable(false);
                                        {
                                            uiCarrier.getjTabbedPane2().addTab("類別範例", null, uiCarrier.getPanel(), null);
                                            uiCarrier.getPanel().setLayout(new BorderLayout(0, 0));
                                            {
                                                uiCarrier.getPanel().add(uiCarrier.getScrollPane(), BorderLayout.CENTER);
                                                {
                                                    uiCarrier.getScrollPane().setViewportView(uiCarrier.getDynamicClassArea());
                                                }
                                            }
                                        }
                                        {
                                            uiCarrier.getjTabbedPane1().addTab("動態類別", null, uiCarrier.getPanel_1(), null);
                                            uiCarrier.getPanel_1().setLayout(new BorderLayout(0, 0));
                                            {
                                                uiCarrier.getPanel_2().setPreferredSize(new Dimension(0, 50));
                                                uiCarrier.getPanel_1().add(uiCarrier.getPanel_2(), BorderLayout.NORTH);
                                                uiCarrier.getPanel_2().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                                                {
                                                    uiCarrier.getPanel_2().add(uiCarrier.getLblJavahome());
                                                }
                                                {
                                                    uiCarrier.getDynamic_javaHomeText().setColumns(20);
                                                    uiCarrier.getPanel_2().add(uiCarrier.getDynamic_javaHomeText());
                                                    JCommonUtil.jTextFieldSetFilePathMouseEvent(uiCarrier.getDynamic_javaHomeText(), true);
                                                    uiCarrier.getDynamic_javaHomeText().getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                                        @Override
                                                        public void process(DocumentEvent event) {
                                                            final String DYNAMIC_JAVA_HOME_KEY = "dynamic_java_home";
                                                            File dir = new File(uiCarrier.getDynamic_javaHomeText().getText());
                                                            if (dir.exists() && dir.isDirectory()) {
                                                                setConfigPropProperties(DYNAMIC_JAVA_HOME_KEY, dir.getAbsolutePath());
                                                            }
                                                        }
                                                    }));
                                                }
                                                {
                                                    uiCarrier.getPanel_2().add(uiCarrier.getLblMvn());
                                                }
                                                {

                                                    uiCarrier.getDynamic_mvnProjectText().setColumns(20);
                                                    uiCarrier.getPanel_2().add(uiCarrier.getDynamic_mvnProjectText());
                                                    JCommonUtil.jTextFieldSetFilePathMouseEvent(uiCarrier.getDynamic_mvnProjectText(), true);
                                                    uiCarrier.getDynamic_mvnProjectText().getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                                        @Override
                                                        public void process(DocumentEvent event) {
                                                            final String DYNAMIC_MVN_PROJECT_DIR_KEY = "dynamic_mvn_project_dir";
                                                            File dir = new File(uiCarrier.getDynamic_mvnProjectText().getText());
                                                            if (dir.exists() && dir.isDirectory()) {
                                                                setConfigPropProperties(DYNAMIC_MVN_PROJECT_DIR_KEY, dir.getAbsolutePath());
                                                            }
                                                        }
                                                    }));
                                                }
                                                {
                                                    uiCarrier.getPanel_2().add(uiCarrier.getLblClasses());
                                                }
                                                {
                                                    uiCarrier.getPanel_2().add(uiCarrier.getDynamic_classesDirText());
                                                    uiCarrier.getDynamic_classesDirText().setColumns(20);
                                                    JCommonUtil.jTextFieldSetFilePathMouseEvent(uiCarrier.getDynamic_classesDirText(), true);
                                                    uiCarrier.getDynamic_classesDirText().getDocument().addDocumentListener(JCommonUtil.getDocumentListener(new HandleDocumentEvent() {
                                                        @Override
                                                        public void process(DocumentEvent event) {
                                                            final String DYNAMIC_TARGET_DIR_KEY = "dynamic_target_dir";
                                                            File dir = new File(uiCarrier.getDynamic_classesDirText().getText());
                                                            if (dir.exists() && dir.isDirectory()) {
                                                                setConfigPropProperties(DYNAMIC_TARGET_DIR_KEY, dir.getAbsolutePath());
                                                            }
                                                        }
                                                    }));
                                                }
                                                {
                                                    uiCarrier.getPanel_2().add(uiCarrier.getLblClass());
                                                }
                                                {
                                                    uiCarrier.getDynamic_classPathText().setEditable(false);
                                                    uiCarrier.getDynamic_classPathText().setColumns(20);
                                                    uiCarrier.getPanel_2().add(uiCarrier.getDynamic_classPathText());
                                                }
                                                {
                                                    uiCarrier.getDynamic_classNameText().setEditable(false);
                                                    uiCarrier.getDynamic_classNameText().setColumns(20);
                                                    uiCarrier.getPanel_2().add(uiCarrier.getDynamic_classNameText());
                                                }
                                                {
                                                    uiCarrier.getDynamic_executeBtn().addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent paramActionEvent) {
                                                            dynamicExecuteButtonAction();
                                                        }
                                                    });
                                                    uiCarrier.getPanel_2().add(uiCarrier.getDynamic_executeBtn());
                                                }
                                            }
                                            {
                                                uiCarrier.getPanel_1().add(uiCarrier.getPanel_3(), BorderLayout.SOUTH);
                                            }
                                            {
                                                uiCarrier.getRunningClassArea().setFont(new Font("微軟正黑體", Font.PLAIN, 13));
                                                JScrollPane jScrollPane1_1 = uiCarrier.createJScrollPane();
                                                uiCarrier.getPanel_1().add(jScrollPane1_1, BorderLayout.CENTER);
                                                jScrollPane1_1.setViewportView(uiCarrier.getRunningClassArea());
                                            }
                                        }
                                        uiCarrier.getShowInfoArea2().addMouseListener(new MouseAdapter() {
                                            public void mouseClicked(MouseEvent evt) {
                                                showInfoArea2MouseClicked(evt);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
            uiCarrier.get_ui().pack();
            uiCarrier.get_ui().setSize(758, 530);

            uiCarrier.get_ui().setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            uiCarrier.get_ui().setModalityType(ModalityType.APPLICATION_MODAL);
            JCommonUtil.setJFrameCenter(uiCarrier.get_ui());
            // JCommonUtil.setJFrameIcon(uiCarrier.get_ui(),
            // "resource/images/ico/fantasyForYou.ico");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新設定 mointerObjects
     */
    private void initMointerObjects() {
        DefaultComboBoxModel moniterComboBoxModel = new DefaultComboBoxModel();
        if (mointerObjects != null && mointerObjects.length > 0) {
            for (int ii = 0; ii < mointerObjects.length; ii++) {
                // 設定下拉
                MoniterObjectComboBox obj = new MoniterObjectComboBox();
                obj.object = mointerObjects[ii];
                obj.index = ii;
                moniterComboBoxModel.addElement(obj);
            }
        }
        uiCarrier.getMoniterComboBox().setModel(moniterComboBoxModel);
        if (moniterComboBoxModel.getSize() != 0) {
            uiCarrier.getMoniterComboBox().setSelectedIndex(0);
            MoniterObjectComboBox moniterObject = (MoniterObjectComboBox) uiCarrier.getMoniterComboBox().getSelectedItem();
            if (moniterObject != null) {
                setRootObject(moniterObject.object);
            }
        } else {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
            DefaultTreeModel model = new DefaultTreeModel(rootNode);
            uiCarrier.getCompositeTree().setModel(model);
        }
    }

    /**
     * 初始化UI
     */
    private synchronized void initGUI_detail() {
        // 設定系統列
        if (!initSysTrayOk) {
            final MenuItem menuItemD1 = uiCarrier.createMenuItem();
            menuItemD1.setLabel(DateUtil.getCurrentDateTime(true));
            menuItemD1.setEnabled(false);
            // 啟動 or 關閉 UI
            final MenuItem menuItem1 = uiCarrier.createMenuItem();
            sysTrayPopupMenuLabelChangeMap.put(menuItem1, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "EnabledUI : " + (inst.isEnabledUI ? "On" : "Off");
                }
            });
            menuItem1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (inst.isEnabledUI) {
                        inst.isEnabledUI = false;
                        inst.uiCarrier.get_ui().setVisible(false);
                    } else {
                        inst.isEnabledUI = true;
                    }
                    sysTrayDisplayMessage("##純毓工具##", inst.isEnabledUI);
                    updateSysTrayPopupMenuLabel();
                    getLogger().debug("DebugMointerUI.isEnabledUI = " + inst.isEnabledUI);
                }
            });
            // 自動執行
            final MenuItem menuItem2 = uiCarrier.createMenuItem();
            sysTrayPopupMenuLabelChangeMap.put(menuItem2, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "auto : " + (inst.isAutoExecute.get() ? "On" : "Off");
                }
            });
            menuItem2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (inst.isAutoExecute.get()) {
                        inst.isAutoExecute.set(false);
                    } else {
                        inst.isAutoExecute.set(true);
                        if (inst.uiCarrier.get_ui().isVisible()) {
                            inst.uiCarrier.get_ui().setVisible(false);
                            if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("現在是否立即執行?", "自動啟動")) {
                                autoExecutePlugIn();
                            } else {
                                inst.isExecuteComplete.set(true);
                            }
                        }
                    }
                    sysTrayDisplayMessage("##純毓工具## - 自動執行", inst.isAutoExecute.get());
                    updateSysTrayPopupMenuLabel();
                }
            });
            // 自動執行mapping的method名稱
            final MenuItem menuItem3 = uiCarrier.createMenuItem();
            sysTrayPopupMenuLabelChangeMap.put(menuItem3, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "caller : " + (isAutoExecuteMappingMethod ? "On" : "Off");
                }
            });
            menuItem3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (isAutoExecuteMappingMethod) {
                        isAutoExecuteMappingMethod = false;
                    } else {
                        isAutoExecuteMappingMethod = true;
                    }
                    sysTrayDisplayMessage("##純毓工具## - 自動Mapping執行", inst.isAutoExecuteMappingMethod);
                    updateSysTrayPopupMenuLabel();
                }
            });
            // 自動unload
            final MenuItem menuItem4 = uiCarrier.createMenuItem();
            sysTrayPopupMenuLabelChangeMap.put(menuItem4, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "autoDispose : " + (isAutoDispose ? "On" : "Off");
                }
            });
            menuItem4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (isAutoDispose) {
                        isAutoDispose = false;
                    } else {
                        isAutoDispose = true;
                    }
                    sysTrayDisplayMessage("##純毓工具## - 自動dispose", inst.isAutoDispose);
                    updateSysTrayPopupMenuLabel();
                }
            });
            // 清空暫存
            final MenuItem menuItem5 = uiCarrier.createMenuItem();
            sysTrayPopupMenuLabelChangeMap.put(menuItem5, new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "clean cache : " + tempModelTimeMointerMap.size();
                }
            });
            menuItem5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    initTempModelTimeMointerMap();
                    sysTrayDisplayMessage("##純毓工具## - 清空快取!", true);
                    updateSysTrayPopupMenuLabel();
                }
            });

            updateSysTrayPopupMenuLabel();
            sysTrayUtil = SysTrayUtil.newInstance();
            sysTrayUtil.createDefaultTray("DebugMointerUI", //
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            inst.uiCarrier.get_ui().setVisible(true);
                        }
                    }, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            disposeTool();
                        }
                    }, menuItemD1, menuItem1, menuItem2, menuItem3, menuItem4);
            if (sysTrayUtil != null) {
                if (keepForRemoveTray == null) {
                    keepForRemoveTray = new HashSet<SysTrayUtil>();
                }
                keepForRemoveTray.add(sysTrayUtil);
            }
            initSysTrayOk = true;
        }

        // 載入設定黨
        if (configFile.exists()) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(configFile));
            } catch (Exception e) {
                JCommonUtil.handleException(e);
                showErrorLogInArea(e);
            }
            DefaultListModel model = JListUtil.createModel();
            for (int ii = 0; ii <= 100; ii++) {
                String key = ExecuteConfig.class.getSimpleName() + "_" + ii;
                if (prop.containsKey(key)) {
                    String propValue = prop.getProperty(key);
                    ExecuteConfig exe = new ExecuteConfig(propValue);
                    model.addElement(exe);
                }
            }
            uiCarrier.getExecuteList().setModel(model);
            configProp = prop;
        }

        // 設定物件概觀
        uiCarrier.getShowInfoArea().setText("");
        if (mointerObjects != null && mointerObjects.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int ii = 0; ii < mointerObjects.length; ii++) {
                Object toObj = mointerObjects[ii];
                this.appendShowInfoAreaData(ii, "", toObj, sb);
            }
            uiCarrier.getShowInfoArea().setText(sb.toString());
        }
    }

    private void sysTrayDisplayMessage(String title, boolean status) {
        if (sysTrayUtil != null) {
            sysTrayUtil.getTrayIcon().displayMessage(title, status ? "啟用" : "關閉", MessageType.INFO);
        }
    }

    private void updateSysTrayPopupMenuLabel() {
        for (MenuItem item : sysTrayPopupMenuLabelChangeMap.keySet()) {
            try {
                String value = sysTrayPopupMenuLabelChangeMap.get(item).call();
                item.setLabel(value);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void appendShowInfoAreaData(int ii, String parseStr, Object toObj, StringBuilder sb) {
        sb.append("Start<" + ii + ">↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓\n");
        if (StringUtils.isNotBlank(parseStr)) {
            sb.append("parseStr = " + parseStr + "\n");
        }
        // 匯出黨內容
        if (toObj == null) {
            sb.append("<null>\n");
        } else {
            sb.append(DebugMointerTypeUtil.toStringExport(toObj));
        }
        if (sb.charAt(sb.length() - 1) != '\n') {
            sb.append("\n");
        }
        sb.append("End<" + ii + ">↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑\n");
    }

    /**
     * 設定監控物件
     */
    private void setRootObject(Object object) {
        // if (object == null) {
        // return;
        // }
        JObject obj = new JObject();
        obj.object = object;
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(obj);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        uiCarrier.getCompositeTree().setModel(model);
    }

    /**
     * 一般節點左鍵處理
     */
    private void modifyOrInvoke(JObject obj, DefaultMutableTreeNode selectNode, int clickCount) {
        Object object = obj.object;
        if (object == null) {
            return;
        } else if (obj.field == null && obj.method == null) {
            getLogger().debug("==========>all null");
            if (object.getClass().isArray()) {
                this.appendInChrildrenForArray(object, selectNode, false);
            } else {
                this.appendInChrildren(object, selectNode, null);
            }
        } else if (obj.field != null) {
            if (clickCount == 2) {
                try {
                    obj.field.setAccessible(true);
                    Object newObj = obj.field.get(object);
                    if (DebugMointerTypeUtil.isPrimitive(obj.getFieldRealType())) {
                        getLogger().debug("==========>isPrimitive");
                        this.setParseStringToField(obj);
                    } else if (obj.getFieldRealType().isArray()) {
                        getLogger().debug("==========>appendInChrildrenForArray");
                        this.appendInChrildrenForArray(newObj, selectNode, false);
                    } else {
                        getLogger().debug("==========>appendInChrildren");
                        this.appendInChrildren(newObj, selectNode, null);
                        if (newObj == null) {
                            this.setParseStringToField(obj);
                        }
                    }
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        } else if (obj.method != null) {
            if (clickCount == 2) {
                this.methodInvoke(obj);
            }
        }
    }

    private void setParseStringToField(JObject obj) {
        try {
            obj.field.setAccessible(true);
            Object newObj = obj.field.get(obj.object);
            String rtnValue = JCommonUtil._jOptionPane_showInputDialog("設值:" + obj.field.getName(), "" + newObj);
            if (Modifier.isFinal(obj.field.getModifiers())) {
                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("這是final無法修改");
                return;
            }
            if ("null".equalsIgnoreCase(rtnValue)) {
                obj.field.set(obj.object, null);
            } else if (rtnValue != null) {
                obj.field.set(obj.object, this.getParseObject(rtnValue));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Object methodInvoke(JObject obj) {
        Object rtnValue = null;
        List<Object> parameterList = new ArrayList<Object>();
        obj.method.setAccessible(true);
        if (obj.method.getParameterTypes() != null && obj.method.getParameterTypes().length > 0) {
            for (int ii = 0; ii < obj.method.getParameterTypes().length; ii++) {
                Class<?> clz = obj.method.getParameterTypes()[ii];
                String val = JCommonUtil._jOptionPane_showInputDialog(obj + "輸入第" + ii + "參數(" + clz + ")");
                if (val == null) {
                    parameterList.add(null);
                } else if (DebugMointerTypeUtil.isPrimitive(clz)) {
                    parameterList.add(DebugMointerTypeUtil.parseToType(val, clz));
                } else {
                    parameterList.add(getParseObject(val));
                }
            }
        }
        String title = "呼叫" + obj.method.getName();
        String message = "==>" + obj.method.getName() + "\r\n餐數如下:\r\n";
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < parameterList.size(); ii++) {
            Object val = parameterList.get(ii);
            String valClassName = val == null ? "<null>" : val.getClass().getSimpleName();
            sb.append(ii + " = " + val + "(" + valClassName + ")" + "\r\n");
        }
        message += sb;
        ComfirmDialogResult result = JOptionPaneUtil.newInstance().confirmButtonYesNoCancel().iconPlainMessage().showConfirmDialog(message, title);
        if (result == ComfirmDialogResult.YES_OK_OPTION) {
            try {
                long startTime = System.currentTimeMillis();
                rtnValue = obj.method.invoke(obj.object, parameterList.toArray());
                String successMessage = "執行成功!\r\n執行時間:" + (System.currentTimeMillis() - startTime);
                if (obj.method.getReturnType() != void.class) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(message);
                    sb2.append("取得回傳結果:\r\n");
                    sb2.append(ReflectionToStringBuilder.toString(rtnValue, ToStringStyle.MULTI_LINE_STYLE, true, true));
                    sb2.append(successMessage);
                    ComfirmDialogResult result2 = JOptionPaneUtil.newInstance().confirmButtonYesNoCancel().iconPlainMessage().showConfirmDialog(sb2.toString(), "是否寫擋?");
                    if (result2 == ComfirmDialogResult.YES_OK_OPTION) {
                        File writeFile = new File(FileUtil.DESKTOP_PATH, title + "_" + DateUtil.getCurrentDateTime(false) + ".log");
                        FileUtils.writeStringToFile(writeFile, sb2.toString(), "utf8");
                        JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("寫擋成功\n" + writeFile);
                    }
                } else {
                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible(successMessage);
                }
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
                showErrorLogInArea(ex);
            }
        }
        return rtnValue;
    }

    /**
     * 陣列物件節點左鍵處理
     */
    private void modifyOrInvoke(JObjectIsArray obj, DefaultMutableTreeNode selectNode, int clickCount) {
        if (clickCount == 1) {
            return;
        }
        Object orignValue = obj.get();
        if (orignValue == null || (orignValue != null && DebugMointerTypeUtil.isPrimitive(orignValue.getClass()))) {
            String defaultVal = orignValue == null ? "null" : String.valueOf(orignValue);
            String rtnValue = JCommonUtil._jOptionPane_showInputDialog("設定陣列值 - index : " + obj.index + " = " + obj.get(), defaultVal);
            Object value = this.getParseObject(rtnValue);
            obj.set(value);
        } else if (orignValue.getClass().isArray()) {
            appendInChrildrenForArray(orignValue, selectNode, false);
        } else {
            appendInChrildren(orignValue, selectNode, null);
        }
    }

    /**
     * 用來顯示集合物件細節
     */
    private void modifyOrInvoke(JObjectIsArrayDetail obj, DefaultMutableTreeNode selectNode, int clickCount) {
        if (clickCount == 1) {
            return;
        }
        appendInChrildrenForArray(obj.object, selectNode, obj.isSort);
    }

    /**
     * 對節點點滑鼠左鍵 ******
     */
    private void modifyOrInvoke(final DefaultMutableTreeNode selectNode, MouseEvent evt) {
        final Object obj = selectNode.getUserObject();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            this.appendNodeInfoComboBox(obj);
            if (obj instanceof JObject) {
                this.modifyOrInvoke((JObject) obj, selectNode, evt.getClickCount());
            } else if (obj instanceof JObjectIsArray) {
                this.modifyOrInvoke((JObjectIsArray) obj, selectNode, evt.getClickCount());
            } else if (obj instanceof JObjectIsArrayDetail) {
                this.modifyOrInvoke((JObjectIsArrayDetail) obj, selectNode, evt.getClickCount());
            } else {
                throw new RuntimeException("不在預期範圍內的物件 : " + obj);
            }
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            // 設定下拉選單第二項title
            JPopupMenuUtil popup = JPopupMenuUtil.newInstance(uiCarrier.getCompositeTree()).applyEvent(evt);
            if (obj instanceof JObjectIsArrayDetail) {
                final JObjectIsArrayDetail detail = (JObjectIsArrayDetail) obj;
                popup.addJMenuItem("排序:" + (detail.isSort ? "on" : "off"), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        try {
                            detail.isSort = !detail.isSort;
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                            showErrorLogInArea(ex);
                        }
                    }
                }).addJMenuItem("重新整理", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        try {
                            selectNode.removeAllChildren();
                            uiCarrier.getCompositeTree().updateUI();
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                            showErrorLogInArea(ex);
                        }
                    }
                })//
                ;
            } else {
                Object[] twoValue = getRealObject(obj);
                String title = (String) twoValue[0];
                final Optional objectOptional = (Optional) twoValue[1];
                popup.addJMenuItem("設定路徑到記是本", new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        try {
                            String path = "_#" + getFullPath(selectNode) + "#_";
                            getLogger().debug("getPath = " + path);
                            JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("路徑:" + path + "\n值:" + //
                            addEnterToOrignStr(String.valueOf(getParseObject(path))));
                            ClipboardUtil.getInstance().setContents(path);
                        } catch (Exception ex) {
                            JCommonUtil.handleException(ex);
                            showErrorLogInArea(ex);
                        }
                    }
                })//
                        .addJMenuItem("設定路徑到變數", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    String path = "_#" + getFullPath(selectNode) + "#_";
                                    getLogger().debug("getPath = " + path);
                                    DefaultTableModel model = (DefaultTableModel) uiCarrier.getParameterTable().getModel();
                                    model.addRow(new Object[] { path, "" });
                                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("路徑:" + path + "\n值:" + //
                                    addEnterToOrignStr(String.valueOf(getParseObject(path))));
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }
                        })//
                        .addJMenuItem(title, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    Object tempValue = null;
                                    if (objectOptional.isPresent()) {
                                        tempValue = objectOptional.orNull();
                                    } else {
                                        tempValue = methodInvoke((JObject) obj);
                                    }
                                    boolean result = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(//
                                            "是否要將資料新增至暫存區:\n" + tempValue, "設定暫存資料");
                                    if (result) {
                                        appendToTempModel(tempValue, 'u');
                                        JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("設定資料到暫存區:" + tempValue + ",成功!");
                                    }
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }
                        })//
                        .addJMenuItem("將暫存區資料設定至此", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    Object tempValue = JListUtil.getLeadSelectionObject(uiCarrier.getTempList());
                                    boolean result = JCommonUtil
                                            ._JOptionPane_showConfirmDialog_yesNoOption("是否要將暫存區資料設定至物件:\n" + ReflectionToStringBuilder.toString(tempValue, ToStringStyle.MULTI_LINE_STYLE), "設定資料");
                                    if (result) {
                                        String message = setRealObject(obj, tempValue);
                                        JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible(message);
                                    }
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }
                        })//
                        .addJMenuItem("將物件匯出檔案", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    Object toObj = ((Optional) getRealObject(obj)[1]).orNull();
                                    if (toObj == null) {
                                        Validate.isTrue(false, "資料為null");
                                    }
                                    File file = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
                                    Validate.isTrue(file != null, "檔案錄竟錯誤");
                                    ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
                                    os.writeObject(toObj);
                                    os.flush();
                                    os.close();
                                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible(objectToString(toObj) + " : 資料匯出成功!");
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }

                            private String objectToString(Object object) {
                                return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
                            }
                        })//
                        .addJMenuItem("將檔案寫回物件", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    File file = JCommonUtil._jFileChooser_selectFileOnly();
                                    Validate.isTrue(file != null, "檔案錄竟錯誤");
                                    ObjectInputStream os = new ObjectInputStream(new FileInputStream(file));
                                    Object toObj = os.readObject();
                                    os.close();
                                    String messgae = setRealObject(obj, toObj);
                                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible(messgae + "\n" + objectToString(toObj) + " : 資料寫入成功!");
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }

                            private String objectToString(Object object) {
                                return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
                            }
                        })//
                        .addJMenuItem("印出此物件細節", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Object toObj = objectOptional.orNull();
                                    if (toObj == null) {
                                        Validate.isTrue(false, "資料為null");
                                    }
                                    // 匯出黨內容
                                    String export = DebugMointerTypeUtil.toStringExport(toObj);
                                    if (JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption(addEnterToOrignStr(export), "是否匯出檔案?")) {
                                        File file = JCommonUtil._jFileChooser_selectFileOnly_saveFile();
                                        Validate.isTrue(file != null, "檔案錄竟錯誤");
                                        FileUtils.write(file, export, "utf8");
                                        JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("資料印出成功!");
                                    }
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }
                        }).addJMenuItem("重新整理", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    selectNode.removeAllChildren();
                                    uiCarrier.getCompositeTree().updateUI();
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }
                        }).addJMenuItem("匯出成Html", new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                try {
                                    Object toObj = ((Optional) getRealObject(obj)[1]).orNull();
                                    if (toObj == null) {
                                        Validate.isTrue(false, "資料為null");
                                    }
                                    File file = JCommonUtil._jFileChooser_selectFileOnly_saveFile();

                                    Properties prop = new Properties();
                                    try {
                                        prop.load(new FileInputStream(configFile));
                                    } catch (Exception e) {
                                        prop = configProp;
                                    }

                                    int layerCount = 7;// default 7曾
                                    final String LAY_COUNT_KEY = "html.layerCount";
                                    if (prop.containsKey(LAY_COUNT_KEY) && StringUtils.isNumeric(prop.getProperty(LAY_COUNT_KEY))) {
                                        layerCount = Integer.parseInt(prop.getProperty(LAY_COUNT_KEY));
                                    }

                                    List<String> ignoreContains = null;
                                    final String IGNORE_CONTAINS_KEY = "html.ignoreContains";
                                    if (prop.containsKey(IGNORE_CONTAINS_KEY) && StringUtils.isNotBlank(prop.getProperty(IGNORE_CONTAINS_KEY))) {
                                        String str = StringUtils.defaultString(prop.getProperty(IGNORE_CONTAINS_KEY));
                                        ignoreContains = Arrays.asList(str.split(",", -1));
                                    }

                                    long startTime = System.currentTimeMillis();
                                    DebugMointerTypeUtilForHTML htmlUtil = new DebugMointerTypeUtilForHTML();
                                    getLogger().debug("@layerCount = " + layerCount);
                                    getLogger().debug("@ignoreContains = " + ignoreContains);
                                    htmlUtil.executeAll(toObj, null, file, ignoreContains, layerCount, true);
                                    startTime = System.currentTimeMillis() - startTime;

                                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible(objectToString(toObj) + " : 資料建立成功!\n耗時:" + startTime);
                                } catch (Exception ex) {
                                    JCommonUtil.handleException(ex);
                                    showErrorLogInArea(ex);
                                }
                            }

                            private String objectToString(Object object) {
                                return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
                            }
                        });
            }
            popup.show();

        }
    }

    private Object[] getRealObject(Object obj) {
        String title = "";
        final SingletonMap fetchValue = new SingletonMap();
        if (obj instanceof JObject) {
            JObject jobj = (JObject) obj;
            if (jobj.field != null) {
                title = "複製此變數並新增至暫存";
                jobj.field.setAccessible(true);
                try {
                    fetchValue.setValue(Optional.fromNullable(jobj.field.get(jobj.object)));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (jobj.method != null) {
                title = "呼叫此方法並將結果新增至暫存";
                fetchValue.setValue(Optional.absent());
            } else {
                title = "複製此變數並新增至暫存";
                fetchValue.setValue(Optional.fromNullable(jobj.object));
            }
        } else if (obj instanceof JObjectIsArray) {
            JObjectIsArray jobj = (JObjectIsArray) obj;
            title = "複製此變數並新增至暫存";
            fetchValue.setValue(Optional.fromNullable(jobj.get()));
        } else {
            throw new RuntimeException("物件型態不在預期範圍 : " + obj);
        }
        return new Object[] { title, fetchValue.getValue() };
    }

    private String setRealObject(Object obj, Object dataValue) {
        String message = "";
        String dataValueMessage = addEnterToOrignStr(String.valueOf(dataValue));
        if (obj instanceof JObject) {
            JObject jobj = (JObject) obj;
            if (jobj.field != null) {
                jobj.field.setAccessible(true);
                try {
                    jobj.field.set(jobj.object, dataValue);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                message = "設定\r\n" + jobj.field.getName() + ":" + dataValueMessage + "\r\n,成功!";
            } else if (jobj.method != null) {
                message = "未設值TODO";// TODO
            } else {
                message = "未設值TODO";// TODO
            }
        } else if (obj instanceof JObjectIsArray) {
            JObjectIsArray jobj = (JObjectIsArray) obj;
            jobj.set(dataValue);
            message = "設定\r\n[" + jobj.index + "]:" + dataValueMessage + "\r\n,成功!";
        }
        return message;
    }

    private String addEnterToOrignStr(String str) {
        final int maxLength = 800;
        if (str.length() > maxLength) {
            str = StringUtils.substring(str, 0, maxLength) + "...etc";
        }
        String[] strs = str.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            for (String s2 : Splitter.fixedLength(100).split(s)) {
                sb.append(s2 + "\n");
            }
        }
        return sb.toString();
    }

    private void appendInChrildrenForArray(Object newObj, DefaultMutableTreeNode selectNode, boolean doSort) {
        DefaultTableModel tableModel = JTableUtil.createModel(true, "名稱", "資料");
        List<JObjectIsArray> sortList = new ArrayList<JObjectIsArray>();
        if (Map.class.isAssignableFrom(newObj.getClass())) {
            Map<?, ?> map = (Map<?, ?>) newObj;
            for (Object key : map.keySet()) {
                JObjectIsArray jobjectArray = new JObjectIsArray();
                jobjectArray.object = newObj;
                jobjectArray.key = key;
                sortList.add(jobjectArray);
            }
        } else if (Collection.class.isAssignableFrom(newObj.getClass())) {
            Collection<?> coll = (Collection<?>) newObj;
            for (int ii = 0; ii < coll.size(); ii++) {
                JObjectIsArray jobjectArray = new JObjectIsArray();
                jobjectArray.object = coll;
                jobjectArray.index = ii;
                sortList.add(jobjectArray);
            }
        } else if (newObj.getClass().isArray()) {
            for (int ii = 0; ii < Array.getLength(newObj); ii++) {
                JObjectIsArray jobjectArray = new JObjectIsArray();
                jobjectArray.object = newObj;
                jobjectArray.index = ii;
                sortList.add(jobjectArray);
            }
        }
        if (doSort) {
            Collections.sort(sortList);
            getLogger().debug("排序!!");
        }
        getLogger().debug("jobjectArray = " + sortList);
        for (JObjectIsArray jobjectArray : sortList) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(jobjectArray);
            selectNode.add(node);
            Object leftValue = jobjectArray.isMap() ? jobjectArray.key : jobjectArray.index;
            Object[] rows = new Object[] { leftValue, jobjectArray.get() };
            getLogger().debug("row = " + Arrays.toString(rows));
            tableModel.addRow(rows);
        }
        uiCarrier.getCompositeTable().setModel(tableModel);
    }

    private void appendInChrildren(Object object, DefaultMutableTreeNode selectNode, Class<?> indicateClass) {
        if (object == null) {
            return;
        }
        if (selectNode == null) {
            selectNode = new DefaultMutableTreeNode();// 讓他不出錯
        }
        if (indicateClass == null) {
            indicateClass = object.getClass();
        }
        DefaultTableModel tableModel = JTableUtil.createModel(true, "名稱", "資料");
        if (Map.class.isAssignableFrom(indicateClass) || Collection.class.isAssignableFrom(indicateClass)) {
            // 特別加入以便觀察參數
            JObjectIsArrayDetail detail = new JObjectIsArrayDetail(object);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(detail);
            selectNode.add(node);
        }
        if (indicateClass.getDeclaredFields() != null) {
            for (Field f : indicateClass.getDeclaredFields()) {
                JObject f1 = new JObject();
                f1.object = object;
                f1.field = f;
                f1.setFieldRealType();// XXX - 需要特別指定型別
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(f1);
                selectNode.add(node);
                try {
                    f.setAccessible(true);
                    tableModel.addRow(new Object[] { f1, f.get(object) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        if (indicateClass.getFields() != null) {
            for (Field f : indicateClass.getFields()) {
                JObject f1 = new JObject();
                f1.object = object;
                f1.field = f;
                f1.isInherit = true;
                f1.setFieldRealType();// XXX - 需要特別指定型別
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(f1);
                selectNode.add(node);
                try {
                    f.setAccessible(true);
                    tableModel.addRow(new Object[] { f1, f.get(object) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        Set<MethodCompare> set = new HashSet<MethodCompare>();
        if (indicateClass.getDeclaredMethods() != null) {
            for (Method m : indicateClass.getDeclaredMethods()) {
                if (set.contains(new MethodCompare(m))) {
                    continue;
                } else {
                    set.add(new MethodCompare(m));
                }
                JObject m1 = new JObject();
                m1.object = object;
                m1.method = m;
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(m1);
                selectNode.add(node);
                try {
                    m.setAccessible(true);
                    tableModel.addRow(new Object[] { m1, this.getMethodValue(object, m) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        if (indicateClass.getMethods() != null) {
            for (Method m : indicateClass.getMethods()) {
                if (set.contains(new MethodCompare(m))) {
                    continue;
                } else {
                    set.add(new MethodCompare(m));
                }
                JObject m1 = new JObject();
                m1.object = object;
                m1.method = m;
                m1.isInherit = true;
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(m1);
                selectNode.add(node);
                try {
                    m.setAccessible(true);
                    tableModel.addRow(new Object[] { m1, this.getMethodValue(object, m) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        if (indicateClass.isArray()) {
            for (int ii = 0; ii < Array.getLength(object); ii++) {
                JObjectIsArray obj = new JObjectIsArray();
                obj.object = object;
                obj.index = ii;
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj);
                selectNode.add(node);
                tableModel.addRow(new Object[] { obj, obj.get() });
            }
        }
        uiCarrier.getCompositeTable().setModel(tableModel);
    }

    /**
     * 目前只取得toString跟hashCode
     */
    private String getMethodValue(Object object, Method mth) {
        try {
            mth.setAccessible(true);
            if (mth.getName().equals("toString") || //
                    mth.getName().equals("hashCode")) {
                return String.valueOf(mth.invoke(object, new Object[0]));
            }
            return "NA";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "NA";
        }
    }

    /**
     * 設定顯示暫存物件資訊
     */
    private void showInfoAtTempTable(Object object, Class<?> indicateClz) {
        if (object == null) {
            return;
        }
        if (indicateClz == null) {
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            uiCarrier.getTempObjClassCombox().setModel(model);
            Class<?> clz = object.getClass();
            while (true) {
                model.addElement(clz);
                if (clz.getSuperclass() != null) {
                    clz = clz.getSuperclass();
                } else {
                    break;
                }
            }
        }
        indicateClz = indicateClz == null ? object.getClass() : indicateClz;
        DefaultTableModel tableModel = JTableUtil.createModel(true, "名稱", "資料");
        if (indicateClz.getDeclaredFields() != null) {
            for (Field f : indicateClz.getDeclaredFields()) {
                JObject f1 = new JObject();
                f1.object = object;
                f1.field = f;
                f1.setFieldRealType();// XXX - 需要特別指定型別
                try {
                    f.setAccessible(true);
                    tableModel.addRow(new Object[] { f1, f.get(object) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        if (indicateClz.getFields() != null) {
            for (Field f : indicateClz.getFields()) {
                JObject f1 = new JObject();
                f1.object = object;
                f1.field = f;
                f1.isInherit = true;
                f1.setFieldRealType();// XXX - 需要特別指定型別
                try {
                    f.setAccessible(true);
                    tableModel.addRow(new Object[] { f1, f.get(object) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        Set<MethodCompare> set = new HashSet<MethodCompare>();
        if (indicateClz.getDeclaredMethods() != null) {
            for (Method m : indicateClz.getDeclaredMethods()) {
                if (set.contains(new MethodCompare(m))) {
                    continue;
                } else {
                    set.add(new MethodCompare(m));
                }
                JObject m1 = new JObject();
                m1.object = object;
                m1.method = m;
                try {
                    m.setAccessible(true);
                    tableModel.addRow(new Object[] { m1, this.getMethodValue(object, m) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        if (indicateClz.getMethods() != null) {
            for (Method m : indicateClz.getMethods()) {
                if (set.contains(new MethodCompare(m))) {
                    continue;
                } else {
                    set.add(new MethodCompare(m));
                }
                JObject m1 = new JObject();
                m1.object = object;
                m1.method = m;
                m1.isInherit = true;
                try {
                    m.setAccessible(true);
                    tableModel.addRow(new Object[] { m1, this.getMethodValue(object, m) });
                } catch (Exception ex) {
                    JCommonUtil.handleException(ex.getMessage(), ex, false);
                    showErrorLogInArea(ex);
                }
            }
        }
        if (indicateClz.isArray()) {
            for (int ii = 0; ii < Array.getLength(object); ii++) {
                JObjectIsArray obj = new JObjectIsArray();
                obj.object = object;
                obj.index = ii;
                tableModel.addRow(new Object[] { obj, obj.get() });
            }
        }
        uiCarrier.getTempValueTable().setModel(tableModel);
    }

    private void compositeTreeMouseClicked(MouseEvent evt) {
        DefaultMutableTreeNode selectNode = uiCarrier.getJtreeUtil().getSelectItem();
        if (selectNode != null) {
            try {
                this.modifyOrInvoke(selectNode, evt);
            } catch (Exception ex) {
                JCommonUtil.handleException(ex);
                showErrorLogInArea(ex);
            }
        }
    }

    private String getFullPath(DefaultMutableTreeNode selectNode) {
        Object object = selectNode.getUserObject();
        if (object == null) {
            return "";
        }
        Stack<String> stack = new Stack<String>();
        fetchFullPath(selectNode, stack);
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop() + ".");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        if (sb.charAt(0) == '.') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    private void fetchFullPath(DefaultMutableTreeNode selectNode, Stack<String> stack) {
        if (selectNode == null) {
            return;
        }
        Object object = selectNode.getUserObject();
        if (object == null) {
            return;
        }
        if (object instanceof JObject) {
            JObject jobject = (JObject) object;
            if (StringUtils.isBlank(jobject.getName()) && selectNode.getParent() == null) {
                for (int ii = 0; ii < mointerObjects.length; ii++) {
                    if (mointerObjects[ii] == jobject.object) {
                        stack.push("[" + String.valueOf(ii) + "]");
                        break;
                    }
                }
            } else if (!stack.isEmpty() && stack.peek().matches("\\[\\d+\\]")) {
                stack.push(jobject.getName() + stack.pop());
            } else {
                stack.push(jobject.getName());
            }
        } else if (object instanceof JObjectIsArray) {
            JObjectIsArray jobject = (JObjectIsArray) object;
            stack.push("[" + jobject.index + "]");
        }
        fetchFullPath((DefaultMutableTreeNode) selectNode.getParent(), stack);
    }

    /**
     * 若第一次執行則新增段點,並判斷斷點是否符合監控條件
     */
    private boolean handleBreakPoint() {
        try {
            // 加入目前段點
            StackTraceElement currentElement = null;
            StackTraceElement[] sks = Thread.currentThread().getStackTrace();
            // xxxxxxxxxxxxxxxxx debug ↓↓↓↓↓
            uiCarrier.getCallStackArea().setText("");
            File threadLog = new File(FileUtil.DESKTOP_PATH, "currentThread_" + DateUtil.getCurrentDate(false) + ".txt");
            for (int ii = 0; ii < sks.length; ii++) {
                uiCarrier.getCallStackArea().append(sks[ii] + "\n");
            }
            // xxxxxxxxxxxxxxxxx debug ↑↑↑↑↑
            boolean findThisOk = false;
            for (int ii = 0; ii < sks.length; ii++) {
                if (StringUtils.equals(sks[ii].getFileName(), "DebugMointerUI.java")) {
                    findThisOk = true;
                }
                if (findThisOk && //
                        !StringUtils.equals(sks[ii].getFileName(), "DebugMointerUI.java") && //
                        !StringUtils.equals(sks[ii].getFileName(), "InvocationEvent.java")) {
                    currentElement = sks[ii];
                    break;
                }
            }
            if (currentElement == null) {
                StringBuilder sb = new StringBuilder();
                for (int ii = 0; ii < sks.length; ii++) {
                    sb.append(ii + "...." + sks[ii] + "---" + ReflectionToStringBuilder.toString(sks[ii], ToStringStyle.SHORT_PREFIX_STYLE) + "\n");
                }
                // throw new RuntimeException("無法找到正確stackTraceElement : \n" +
                // sb);//TODO
                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("無法找到正確stackTraceElement : \n" + sb);
                return false;
            }

            // 設定現在斷點名稱
            uiCarrier.get_ui().setTitle(currentElement.getFileName() + "." + currentElement.getMethodName() + ":" + currentElement.getLineNumber());

            // 判斷目前斷點是否已存在
            MointerBreakPoint mp = new MointerBreakPoint();
            mp.mointerLine = currentElement;
            mp.isEnable = true;
            mp.condition = "";// 空白預設為true

            currentBreakPoint = currentElement;
            getLogger().debug("## 目前斷點 : " + currentBreakPoint);

            DefaultTableModel model = JTableUtil.createModel(true, "程式行數", "條件", "符合", "啟用");
            if (!mointerBreakPointSet.contains(mp)) {
                mointerBreakPointSet.add(mp);
                for (MointerBreakPoint mp2 : mointerBreakPointSet) {
                    Boolean result = null;
                    try {
                        result = this.parseConditionStr(mp2.condition);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    model.addRow(new Object[] { mp2, mp2.condition, result, mp2.isEnable });
                }
                uiCarrier.getBreakpointTable().setModel(model);
            }

            // 判斷目前段點是否符合條件
            for (MointerBreakPoint mp3 : mointerBreakPointSet) {
                if (mp3.equals(mp)) {
                    if (mp3.isEnable && validateConditionIsOk(mp3.condition)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
            return true;// 有錯還是開啟
        }
    }

    /**
     * 判斷監控條件式是否符合條件
     */
    private boolean validateConditionIsOk(String condition) {
        if (StringUtils.isBlank(condition)) {
            return true;
        } else if (StringUtils.equalsIgnoreCase(condition, "true") || StringUtils.equalsIgnoreCase(condition, "false")) {
            return Boolean.parseBoolean(condition);
        } else {
            return this.parseConditionStr(condition);
        }
    }

    private Boolean parseConditionStr(String condition) {
        if (StringUtils.isBlank(condition)) {
            return null;
        }
        return DebugMointerLogicParser.newInstance(condition, new ParseToObject() {
            @Override
            public Object apply(String parseStr) {
                if (DebugMointerGetParseObject.isQuoteParameter(parseStr)) {
                    return getParseObject(parseStr);
                }
                throw new RuntimeException("無法解析條件式 : " + parseStr);
            }
        }).execute();
    }

    // Event
    // ------------------------------------------------------------------------------------------

    private void moniterComboBoxActionPerformed(ActionEvent evt) {
        try {
            JComboBox select = (JComboBox) evt.getSource();
            MoniterObjectComboBox moniterObject = (MoniterObjectComboBox) select.getSelectedItem();
            if (moniterObject != null) {
                setRootObject(moniterObject.object);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
        }
    }

    private void breakpointTableMouseClicked(MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON1) {
            updateCurrentMointerBreakPoint(evt);
            int rowId = JTableUtil.newInstance(uiCarrier.getBreakpointTable()).getSelectedRow();
            MointerBreakPoint mp = (MointerBreakPoint) uiCarrier.getBreakpointTable().getValueAt(rowId, 0);
            Boolean result = null;
            try {
                result = parseConditionStr(mp.condition);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            uiCarrier.getBreakpointTable().setValueAt(mp.condition, rowId, 1);
            uiCarrier.getBreakpointTable().setValueAt(result, rowId, 2);
            uiCarrier.getBreakpointTable().setValueAt(mp.isEnable, rowId, 3);
            uiCarrier.getConditionArea().setText(mp.condition);
            uiCarrier.getConditionActivationBtn().setSelected(mp.isEnable);
            uiCarrier.getConditionActivationBtn().setText(mp.isEnable ? "enable" : "disable");
            currentEditMointerBreakPoint = mp;
        }
        if (evt.getButton() == MouseEvent.BUTTON3) {
            JPopupMenuUtil.newInstance(uiCarrier.getBreakpointTable()).applyEvent(evt)//
                    .addJMenuItem("刷新狀態", new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            DefaultTableModel model = JTableUtil.createModel(true, "程式行數", "條件", "符合", "啟用");
                            for (MointerBreakPoint mp2 : mointerBreakPointSet) {
                                Boolean bool = null;
                                String condition = mp2.condition;
                                try {
                                    bool = parseConditionStr(condition);
                                } catch (Exception ex) {
                                    condition = "";
                                }
                                model.addRow(new Object[] { mp2, condition, bool, mp2.isEnable });
                            }
                            uiCarrier.getBreakpointTable().setModel(model);
                        }
                    })//
                    .show();
        }
    }

    private void updateCurrentMointerBreakPoint(EventObject evt) {
        try {
            if (currentEditMointerBreakPoint != null) {
                if (StringUtils.isNotBlank(uiCarrier.getConditionArea().getText())) {
                    try {
                        boolean result = this.parseConditionStr(uiCarrier.getConditionArea().getText());
                        getLogger().debug(uiCarrier.getConditionArea().getText() + " => " + result);
                    } catch (Exception ex) {
                        // JCommonUtil.handleException(ex.getMessage(), ex,
                        // false);//有錯誤也無須提醒,因為不同斷點一定會錯
                        // conditionArea.setText("");//清空會導致不同中斷點互相清除資料
                        ex.printStackTrace();
                        showErrorLogInArea(ex);
                    }
                }

                // 條件無誤設定值給設定黨
                currentEditMointerBreakPoint.condition = uiCarrier.getConditionArea().getText();
                currentEditMointerBreakPoint.isEnable = uiCarrier.getConditionActivationBtn().isSelected();
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
        }
    }

    private void updateParameter(EventObject evt) {
        try {
            DefaultTableModel model = (DefaultTableModel) uiCarrier.getParameterTable().getModel();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                String parseStr = (String) model.getValueAt(ii, 0);
                if (StringUtils.isBlank(parseStr)) {
                    continue;
                }
                model.setValueAt(parseStr, ii, 0);
                Object value = null;
                try {
                    // 參數清單
                    Object orginObj = getParseObject(parseStr);
                    value = DebugMointerTypeUtil.parseExplicitToString(orginObj);
                } catch (Exception ex) {
                    value = "E:" + ex.getMessage();
                    ex.printStackTrace();
                }
                model.setValueAt(value, ii, 1);
            }
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
        }
    }

    private void showInfoArea2MouseClicked(MouseEvent evt) {
        if (evt.getButton() != MouseEvent.BUTTON1 || evt.getClickCount() != 2) {
            return;
        }
        try {
            uiCarrier.getShowInfoArea2().setText("");
            StringBuilder sb = new StringBuilder();
            DefaultTableModel model = (DefaultTableModel) uiCarrier.getParameterTable().getModel();
            for (int ii = 0; ii < model.getRowCount(); ii++) {
                String parseStr = (String) model.getValueAt(ii, 0);
                if (StringUtils.isBlank(parseStr)) {
                    continue;
                }
                try {
                    // 參數清單
                    Object orginObj = getParseObject(parseStr);

                    // 設定參數物件概觀
                    this.appendShowInfoAreaData(ii, parseStr, orginObj, sb);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // 設定參數物件概觀
            uiCarrier.getShowInfoArea2().setText(sb.toString());
            JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("已更新參數資料!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
        }
    }

    private void showInfoAreaMouseClicked(MouseEvent evt) {
        if (evt.getButton() != MouseEvent.BUTTON1 || evt.getClickCount() != 2) {
            return;
        }
        try {
            uiCarrier.getShowInfoArea().setText("");
            if (mointerObjects != null && mointerObjects.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (int ii = 0; ii < mointerObjects.length; ii++) {
                    Object toObj = mointerObjects[ii];
                    this.appendShowInfoAreaData(ii, "", toObj, sb);
                }
                uiCarrier.getShowInfoArea().setText(sb.toString());
            }
            JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("已更新參數資料!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
        }
    }

    // JavaBean
    // -------------------------------------------------------------------------------------------------------
    /**
     * 用來顯示集合物件的頂頭物件
     */
    private static class JObjectIsArrayDetail {
        Object object;
        boolean isMap;
        boolean isCollection;
        boolean isArray;
        boolean isSort;
        int size;

        JObjectIsArrayDetail(Object object) {
            this.object = object;
            this.isMap = Map.class.isAssignableFrom(object.getClass());
            this.isCollection = Collection.class.isAssignableFrom(object.getClass());
            this.isArray = object.getClass().isArray();
            if (isMap) {
                size = ((Map<?, ?>) object).size();
            }
            if (isCollection) {
                size = ((Collection<?>) object).size();
            }
            if (isArray) {
                size = Array.getLength(object);
            }
        }

        @Override
        public String toString() {
            return "集合" + object.getClass().getSimpleName() + ", size : " + size;
        }
    }

    /**
     * 陣列物件屬性
     */
    private static class JObjectIsArray implements Comparable<JObjectIsArray> {
        Object object;
        int index;
        Object key;

        boolean isMap() {
            return Map.class.isAssignableFrom(object.getClass());
        }

        boolean isCollection() {
            return Collection.class.isAssignableFrom(object.getClass());
        }

        boolean isArray() {
            return object.getClass().isArray();
        }

        int getLen() {
            if (isMap()) {
                return ((Map) object).size();
            } else if (isCollection()) {
                return ((Collection) object).size();
            } else if (isArray()) {
                return Array.getLength(object);
            } else {
                throw new RuntimeException("不在預期範圍內物件 : " + object);
            }
        }

        Object get() {
            if (isMap()) {
                return ((Map) object).get(key);
            } else if (isCollection()) {
                Collection<?> coll = (Collection<?>) object;
                int ii = 0;
                for (Object obj : coll) {
                    if (ii == index) {
                        return obj;
                    }
                    ii++;
                }
                throw new RuntimeException("無法取得對應index : " + index);
            } else if (isArray()) {
                return Array.get(object, index);
            } else {
                throw new RuntimeException("不在預期範圍內物件 : " + object);
            }
        }

        void set(Object value) {
            if (isMap()) {
                ((Map) object).put(key, value);
            } else if (isCollection()) {
                if (List.class.isAssignableFrom(object.getClass())) {
                    List<Object> list = (List<Object>) object;
                    list.set(index, value);
                } else {
                    Collection<Object> coll = (Collection<Object>) object;
                    coll.remove(get());
                    coll.add(value);
                }
            } else if (isArray()) {
                Array.set(object, index, value);
            } else {
                throw new RuntimeException("不在預期範圍內物件 : " + object);
            }
        }

        public String toString() {
            if (isMap()) {
                Object value = get();
                String clsName = value != null ? value.getClass().getSimpleName() : "<null>";
                return key + ":" + value + "(" + clsName + ")";
            } else if (isCollection()) {
                Object value = get();
                String clsName = value != null ? value.getClass().getSimpleName() : "<null>";
                return index + ":" + value + "(" + clsName + ")";
            } else if (isArray()) {
                return index + ":" + Array.get(object, index) + "(" + object.getClass().getComponentType().getSimpleName() + ")";
            } else {
                throw new RuntimeException("不在預期範圍內物件 : " + object);
            }
        }

        @Override
        public int compareTo(JObjectIsArray other) {
            if (isMap() && other.isMap()) {
                return StringUtils.defaultString(String.valueOf(this.key)).compareTo(StringUtils.defaultString(String.valueOf(other.key)));
            } else if (isCollection() && other.isCollection()) {
                return StringUtils.defaultString(String.valueOf(this.get())).compareTo(StringUtils.defaultString(String.valueOf(other.get())));
            } else if (isArray() && other.isArray()) {
                return StringUtils.defaultString(String.valueOf(this.get())).compareTo(StringUtils.defaultString(String.valueOf(other.get())));
            } else {
                throw new RuntimeException("無法處理的compareTo : " + this.object + " / " + other.object);
            }
        }
    }

    /**
     * 一般物件屬性
     */
    static class JObject {
        Object object;
        Field field;
        Method method;
        Class<?> fieldRealType;
        boolean isInherit;

        public Class<?> getFieldRealType() {
            if (fieldRealType == null) {
                return field.getType();
            }
            return fieldRealType;
        }

        private void setFieldRealType() {
            try {
                field.setAccessible(true);
                Object fetchVal = field.get(object);
                if (fetchVal != null) {
                    fieldRealType = fetchVal.getClass();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            if (field != null) {
                return field.getName();
            } else if (method != null) {
                return method.getName();
            } else {
                return "";
            }
        }

        public static String getModifier(int modifier) {
            StringBuilder sb = new StringBuilder();
            if (Modifier.isPublic(modifier))
                sb.append("+");
            if (Modifier.isPrivate(modifier))
                sb.append("-");
            if (Modifier.isProtected(modifier))
                sb.append("#");
            if (Modifier.isPackage(modifier))
                sb.append("~");
            if (Modifier.isStatic(modifier))
                sb.append("s");
            if (Modifier.isFinal(modifier))
                sb.append("f");
            if (Modifier.isSynchronized(modifier))
                sb.append("_syn_");
            if (Modifier.isVolatile(modifier))
                sb.append("_vol_");
            if (Modifier.isTransient(modifier))
                sb.append("_tra_");
            if (Modifier.isNative(modifier))
                sb.append("_nav_");
            if (Modifier.isInterface(modifier))
                sb.append("_inf_");
            if (Modifier.isAnnotation(modifier))
                sb.append("_ann_");
            if (Modifier.isEnum(modifier))
                sb.append("_enu_");
            if (Modifier.isAbstract(modifier))
                sb.append("_abs_");
            if (Modifier.isStrict(modifier))
                sb.append("_stc_");
            return sb.toString();
        }

        @Override
        public String toString() {
            if (object == null) {
                return "<null>";
            }
            String name = "";
            String inherit = isInherit ? "<P>" : "";
            if (field != null) {
                String typeClass = field.getType().getSimpleName();
                if (fieldRealType != null) {
                    typeClass = fieldRealType.getSimpleName();
                }
                name = inherit + "[F][" + getModifier(field.getModifiers()) + "]" + field.getName() + "(" + typeClass + ")";
            } else if (method != null) {
                List<String> parameterList = new ArrayList<String>();
                if (method.getParameterTypes() != null && method.getParameterTypes().length > 0) {
                    for (Class<?> clz : method.getParameterTypes()) {
                        parameterList.add(clz.getSimpleName());
                    }
                }
                name = inherit + "[M][" + getModifier(method.getModifiers()) + "]" + "(" + method.getReturnType().getSimpleName() + ")" + method.getName() + parameterList;
            } else {
                name = object.getClass().toString();
            }
            return name;
        }
    }

    /**
     * 用來設定下拉選單
     */
    private static class MoniterObjectComboBox {
        int index;
        Object object;

        private String objectToString(Object object) {
            return object.getClass().getName() + "@" + Integer.toHexString(object.hashCode());
        }

        @Override
        public String toString() {
            if (object == null) {
                return index + " = <null>";
            }
            String className = objectToString(object);
            if (object instanceof DefaultListModel) {
                className = "暫存區(請找vector取得路徑)";
            }
            if (object instanceof ModifyObject) {
                ModifyObject<Object> o = (ModifyObject<Object>) object;
                String value = "<null>";
                if (o.object != null) {
                    value = objectToString(o.object);
                }
                className = "原始物件[" + value + "]";
            }
            return index + " = " + className;
        }
    }

    /**
     * 當user指修改某個原生物件才使用
     */
    private static class ModifyObject<T> {
        T object;

        @Override
        public String toString() {
            String name = "<null>";
            String className = "<null>";
            if (object != null) {
                name = object.toString();
                className = object.getClass().getCanonicalName();
            }
            return "原始物件 [" + name + " - " + className + "]";
        }
    }

    /**
     * 判斷是否監控此段點的條件
     */
    private static class MointerBreakPoint {
        StackTraceElement mointerLine;
        String condition;
        boolean isEnable;

        @Override
        public String toString() {
            return mointerLine.toString();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((mointerLine == null) ? 0 : mointerLine.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MointerBreakPoint other = (MointerBreakPoint) obj;
            if (mointerLine == null) {
                if (other.mointerLine != null)
                    return false;
            } else if (!mointerLine.equals(other.mointerLine))
                return false;
            return true;
        }
    }

    /**
     * 判斷是否取得重複method
     */
    private static class MethodCompare {
        String methodName;
        Class<?> returnType;
        List<Class<?>> parameterType;

        MethodCompare(Method m) {
            methodName = m.getName();
            returnType = m.getReturnType();
            if (m.getParameterTypes() != null) {
                parameterType = Arrays.asList(m.getParameterTypes());
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
            result = prime * result + ((parameterType == null) ? 0 : parameterType.hashCode());
            result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodCompare other = (MethodCompare) obj;
            if (methodName == null) {
                if (other.methodName != null)
                    return false;
            } else if (!methodName.equals(other.methodName))
                return false;
            if (parameterType == null) {
                if (other.parameterType != null)
                    return false;
            } else if (!parameterType.equals(other.parameterType))
                return false;
            if (returnType == null) {
                if (other.returnType != null)
                    return false;
            } else if (!returnType.equals(other.returnType))
                return false;
            return true;
        }
    }

    /**
     * 取得parse物件
     */
    public Object getParseObject(String parseStr) {
        DebugMointerGetParseObject parseObj = new DebugMointerGetParseObject(mointerObjects, getLogger());
        return parseObj.getParseObject(parseStr);
    }

    // 按鍵搜尋
    // -------------------------------------------------------------------------------------------------------------------
    private Set<List<Object>> findAllSet = new LinkedHashSet<List<Object>>();
    private Thread tempSearchKeyThread;
    private SingletonMap tempSearchKeyTimeMap = new SingletonMap();
    private StringBuffer tempSearchKey = new StringBuffer();

    private void compositeTreeKeyPressed(KeyEvent evt) {
        DefaultMutableTreeNode node = JTreeUtil.newInstance(uiCarrier.getCompositeTree()).getSelectItem();
        if (node.getParent() == null) {
            tempSearchKey = new StringBuffer();
            return;
        }
        getLogger().debug("keyCode = " + evt.getKeyCode());
        tempSearchKey.append(evt.getKeyChar());
        tempSearchKeyTimeMap.setValue(System.currentTimeMillis());

        // 案Esc清除
        if (evt.getKeyCode() == 27) {
            tempSearchKey = new StringBuffer();
            getLogger().debug("clean....");
        }

        // 按F3找下一個
        if (evt.getKeyCode() == 114) {
            getLogger().debug("Find Next!!! --> " + findAllSet.size());
            int index = 0, findIndex = -1;
            for (List<Object> list2 : findAllSet) {
                if (list2.get(list2.size() - 1) == node) {
                    findIndex = index;
                }
                if (findIndex != -1 && findIndex + 1 == index) {
                    TreePath path = new TreePath(list2.toArray());
                    getLogger().debug("next " + findIndex + " -->" + list2);
                    uiCarrier.getCompositeTree().setSelectionPath(path);
                    getLogger().debug(">>>" + JTreeUtil.newInstance(uiCarrier.getCompositeTree()).getSelectItem());
                    return;
                }
                index++;
            }
            return;
        }

        if (tempSearchKeyThread == null || tempSearchKeyThread.getState() == Thread.State.TERMINATED) {
            tempSearchKeyThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (System.currentTimeMillis() < (Long) (tempSearchKeyTimeMap.getValue()) + 2000) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tempSearchKey = new StringBuffer();
                    getLogger().debug("clean....");
                }
            });
            tempSearchKeyThread.start();
        }

        String searchKey = tempSearchKey.toString().toLowerCase();
        getLogger().debug("search key : " + searchKey);
        findAllSet.clear();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        for (int ii = 0; ii < parent.getChildCount(); ii++) {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode) parent.getChildAt(ii);
            boolean findOk = false;
            if (current.getUserObject() instanceof JObject) {
                JObject cur = (JObject) current.getUserObject();
                if (cur.getName().toLowerCase().startsWith(searchKey)) {
                    findOk = true;
                }
            } else if (current.getUserObject() instanceof JObject) {
                JObjectIsArray cur = (JObjectIsArray) current.getUserObject();
                if (StringUtils.isNumeric(tempSearchKey.toString()) && cur.index == Integer.parseInt(tempSearchKey.toString())) {
                    findOk = true;
                }
            }
            if (findOk) {
                LinkedList<Object> list = new LinkedList<Object>();
                do {
                    list.addFirst(current);
                } while ((current = (DefaultMutableTreeNode) current.getParent()) != null);
                findAllSet.add(list);
            }
        }
        for (List<Object> list2 : findAllSet) {
            getLogger().debug("<<" + list2);
        }
        if (!findAllSet.isEmpty()) {
            List<Object> list2 = findAllSet.iterator().next();
            if (!list2.isEmpty()) {
                TreePath path = new TreePath(list2.toArray());
                getLogger().debug("find-->" + list2);
                uiCarrier.getCompositeTree().setSelectionPath(path);
            }
        }
    }

    private void saveAndAddExecuteConfig(boolean saveTextboxData) throws FileNotFoundException, IOException {
        String classPath = uiCarrier.getClasspathText().getText();
        String className = uiCarrier.getClassNameText().getText();
        String executeMethodName = methodNameHandler.getExecuteMethodNameText();
        Validate.notEmpty(classPath, "輸入classpath");
        Validate.notEmpty(className, "輸入類別路徑");
        Validate.notEmpty(executeMethodName, "執行method不可為空!");
        ExecuteConfig exe = new ExecuteConfig();
        exe.className = StringUtils.trimToEmpty(className);
        exe.classPath = StringUtils.trimToEmpty(classPath);
        exe.executeMethodName = StringUtils.trimToEmpty(executeMethodName);
        DefaultListModel model = (DefaultListModel) uiCarrier.getExecuteList().getModel();
        // 移除掉所有設定
        for (int ii = 0;; ii++) {
            String propKey = ExecuteConfig.class.getSimpleName() + "_" + ii;
            if (configProp.containsKey(propKey)) {
                configProp.remove(propKey);
            } else {
                break;
            }
        }
        // 判斷設定是否已存在
        for (int ii = 0; ii < model.getSize(); ii++) {
            int index = ii;// 不用存從0開始
            if (saveTextboxData) {
                index = ii + 1;// 從1開始
            }
            String propKey = ExecuteConfig.class.getSimpleName() + "_" + index;
            ExecuteConfig exeCurrent = (ExecuteConfig) model.getElementAt(ii);
            configProp.setProperty(propKey, exeCurrent.toProp());
            if (exeCurrent.equals(exe)) {
                configProp.remove(propKey);
                model.removeElementAt(ii);
                // getLogger().debug("設定已存在:" + propKey + " = " +
                // exeCurrent.toProp());
            }
        }
        if (saveTextboxData) {
            String propKey = ExecuteConfig.class.getSimpleName() + "_0";// 最新的放0
            configProp.setProperty(propKey, exe.toProp());
            model.addElement(exe);
        }
        configProp.store(new FileOutputStream(configFile), "設定黨");
        getLogger().debug("存檔:" + configFile);
    }

    private void importClassBtnActionPerformed(boolean slientMode) {
        try {
            String classPath = uiCarrier.getClasspathText().getText();
            String className = uiCarrier.getClassNameText().getText();
            // String executeMethodName = getExecuteMethodNameText();

            Validate.notEmpty(classPath, "輸入classpath");
            Validate.notEmpty(className, "輸入類別路徑");

            File classpathFile = JCommonUtil.filePathCheck(classPath, "設定classpath", false);
            URLClassLoader loader = new URLClassLoader(new URL[] { classpathFile.toURL() }, Thread.currentThread().getContextClassLoader());

            Class<?> clz = Class.forName(className, true, loader);
            // Class<?> clz = loader.loadClass(className);
            Constructor<?> c = clz.getDeclaredConstructor(new Class[0]);
            c.setAccessible(true);
            Object newObject = c.newInstance();

            // 加入自訂物件
            Object[] objects = inst.mointerObjects;
            for (int ii = 0; ii < objects.length; ii++) {
                if (objects[ii] != null && objects[ii] != newObject && //
                        StringUtils.equals(objects[ii].getClass().getCanonicalName(), clz.getCanonicalName())) {
                    getLogger().debug("移除已存在 :" + ii + " --> " + objects[ii]);
                    objects = ArrayUtils.remove(objects, ii);
                    ii--;
                }
            }

            inst.mointerObjects = ArrayUtils.add(objects, newObject);
            // getLogger().debug("mointerObjects=>" +
            // Arrays.toString(inst.mointerObjects));

            int index = inst.mointerObjects.length - 1;
            if (!slientMode) {
                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("載入自訂物件[" + index + "] :\n" + newObject + "\n成功!");
            }

            // 不知道為何會被蓋掉,很詭異
            uiCarrier.getClasspathText().setText(classPath);
            uiCarrier.getClassNameText().setText(className);

            boolean autowire = false;
            if (!slientMode) {
                autowire = JCommonUtil._JOptionPane_showConfirmDialog_yesNoOption("是否要自動綁定?", "不建議(可能會綁錯物件)");
            } else {
                autowire = true;
            }

            // 自動綁定物件
            if (autowire) {
                // 執行mapping
                DebugMointerMappingField mappingObj = new DebugMointerMappingField(newObject, this, mointerObjects, uiCarrier.getTempModelCheckBox());
                mappingObj.executeMapping();

                if (!slientMode) {
                    JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("自動對應完成!\n成功:\n" + mappingObj.getOkSb() + "\n失敗:\n" + mappingObj.getErrSb());
                } else {
                    getLogger().debug("自動綁定注入完成!\n成功:\n" + mappingObj.getOkSb() + "\n失敗:\n" + mappingObj.getErrSb());
                }
            }
        } catch (Throwable ex) {
            JCommonUtil.handleException(ex);
            showErrorLogInArea(ex);
        }
    }

    private void executeImportClass(boolean slientMode, boolean saveConfig) {
        String classInfo = "";
        try {
            String classPath = uiCarrier.getClasspathText().getText();
            String className = uiCarrier.getClassNameText().getText();
            String executeMethodName = methodNameHandler.getExecuteMethodNameText();

            Validate.notEmpty(classPath, "輸入classpath");
            Validate.notEmpty(className, "輸入類別路徑");
            Validate.notEmpty(executeMethodName, "執行method不可為空!");

            boolean findObj = false;
            boolean findMethod = false;
            A: for (int ii = 0; ii < mointerObjects.length; ii++) {
                Object obj = mointerObjects[ii];
                if (obj != null && StringUtils.equals(obj.getClass().getName(), className)) {
                    findObj = true;

                    // 使用cglib時使用指定參數
                    if (inst.indicateExecuteConfig != null && //
                            inst.indicateExecuteConfig.indicateParameters != null && inst.indicateExecuteConfig.indicateParameters.length != 0) {
                        for (Method method : obj.getClass().getMethods()) {
                            if (StringUtils.equals(method.getName(), executeMethodName) && //
                                    this.isObjectParamaterClassMatch_forCglib(method.getParameterTypes(), inst.indicateExecuteConfig.indicateParameters)) {
                                findMethod = true;
                                classInfo = obj.getClass().getName() + "." + method.getName() + "()";
                                exactExecute(method, obj, inst.indicateExecuteConfig.indicateParameters, slientMode);
                                break A;
                            }
                        }
                    }

                    // 無參數執行
                    for (Method method : obj.getClass().getMethods()) {
                        if (StringUtils.equals(method.getName(), executeMethodName) && method.getParameterTypes().length == 0) {
                            findMethod = true;
                            classInfo = obj.getClass().getName() + "." + method.getName() + "()";
                            exactExecute(method, obj, new Object[0], slientMode);
                            break A;
                        }
                    }

                    // 多參數執行
                    for (Method method : obj.getClass().getMethods()) {
                        if (StringUtils.equals(method.getName(), executeMethodName) && method.getParameterTypes().length != 0) {
                            findMethod = true;
                            classInfo = obj.getClass().getName() + "." + method.getName() + "()";
                            try {
                                DebugMointerMappingFieldForMethod mappingObj = new DebugMointerMappingFieldForMethod(this, mointerObjects, method);
                                List<Object> parameterList = mappingObj.getExactExecuteParameterList();
                                exactExecute(method, obj, parameterList.toArray(), slientMode);
                                break A;
                            } catch (UnsupportedOperationException ex) {
                                getLogger().debug(ex.getMessage());
                                continue;
                            }
                        }
                    }
                }
            }

            Validate.isTrue(findObj, "找不到類別:" + className + ",請先載入類別");
            Validate.isTrue(findMethod, "找不到方法:" + executeMethodName);

            if (saveConfig) {
                saveAndAddExecuteConfig(true);
            }
        } catch (Throwable ex) {
            String errorMessage = "執行失敗 : " + classInfo + ", message : " + ex.getMessage();
            File errFile = JCommonUtil.handleException(errorMessage, ex);
            showErrorLogInArea(ex);
            if (errFile != null && errFile.exists() && !uiCarrier.isUseMockUI()) {
                try {
                    Desktop.getDesktop().browse(errFile.toURI());
                    File exeLogFile = new File(FileUtil.DESKTOP_PATH, "Logger.log");
                    if (exeLogFile.exists()) {
                        Desktop.getDesktop().browse(exeLogFile.toURI());
                    }
                } catch (java.awt.HeadlessException ignore) {
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorLogInArea(e);
                }
            }
            if (isAutoExecute.get()) {
                throw new RuntimeException(errorMessage, ex);
            }
        }
    }

    private boolean isObjectParamaterClassMatch_forCglib(Class<?>[] clz, Object[] params) {
        if (clz.length != params.length) {
            return false;
        }
        for (int ii = 0; ii < clz.length; ii++) {
            if (params[ii] == null) {
                continue;
            }
            if (!ClassUtil.isAssignFrom(clz[ii], params[ii].getClass())) {
                return false;
            }
        }
        return true;
    }

    private void exactExecute(Method method, Object obj, Object[] params, boolean slientMode) throws Throwable {
        String classInfo = obj.getClass().getName() + "." + method.getName() + "()";
        getLogger().debug("## 執行method : " + method);
        getLogger().debug("#START  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        long startTime = System.currentTimeMillis();
        // returnObject = method.invoke(obj, new Object[0]);
        returnObject = methodExecute(method, obj, params);
        inst.isExecuteComplete.set(true);
        String timeMessage = "執行時間:" + (System.currentTimeMillis() - startTime);
        String successMessage = classInfo + "\n" + timeMessage;
        getLogger().debug("#END    ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
        getLogger().debug(timeMessage);
        if (!slientMode) {
            JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("執行成功!!\n" + successMessage);
        } else {
            executeSuccessMessage(successMessage);
            if (isAutoExecute.get()) {
                getLogger().debug("## - 設定執行成功!!!" + isExecuteComplete);
            }
        }
    }

    private void executeSuccessMessage(String successMessage) {
        try {
            TrayNotificationHelper.newInstance()//
                    .title("執行成功!!")//
                    .message(successMessage)//
                    .notificationType(NotificationType.INFORMATION)//
                    .rectangleFill(TrayNotificationHelper.RandomColorFill.getInstance().get())//
                    .animationType(AnimationType.FADE)//
                    .onPanelClickCallback(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean useEditor = false;
                            String key = "logger_editor";
                            if (inst.configProp.containsKey(key)) {
                                File editor = new File(inst.configProp.getProperty(key));
                                if (editor.exists()) {
                                    getLogger().showFileIndicateEditor(editor);
                                    useEditor = true;
                                }
                            }
                            if (!useEditor) {
                                getLogger().showFile();
                            }
                        }
                    }).show(1500);
        } catch (Throwable ex) {
            System.err.println(ex.getMessage());
            try {
                sysTrayUtil.displayMessage("執行成功!!", successMessage, TrayIcon.MessageType.INFO);
                System.out.println("執行成功!! : " + successMessage);
            } catch (Throwable ex2) {
            }
        }
    }

    private Object methodExecute(final Method method, final Object object, final Object[] parameters) throws Throwable {
        final SingletonMap errorMap = new SingletonMap();
        final SingletonMap returnMap = new SingletonMap();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    method.setAccessible(true);
                    returnMap.setValue(method.invoke(object, parameters));
                } catch (Throwable e) {
                    errorMap.setValue(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        long startTime = System.currentTimeMillis();
        long executeTimeOut = 10 * 60 * 1000;
        int sleepFailTime = 0;
        while (thread.getState() != State.TERMINATED) {
            try {
                Thread.sleep(1);
                if (System.currentTimeMillis() - startTime > executeTimeOut) {
                    getLogger().debug("## - 執行超過時間!!! : " + executeTimeOut);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                sleepFailTime++;
                if (sleepFailTime > 10) {
                    break;
                }
            }
        }
        if (errorMap.getValue() != null) {
            throw (Throwable) errorMap.getValue();
        }
        return returnMap.getValue();
    }

    public static class ExecuteConfig {
        String classPath;
        String className;
        String executeMethodName;
        Object[] indicateParameters;// 20180224加入

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((className == null) ? 0 : className.hashCode());
            result = prime * result + ((classPath == null) ? 0 : classPath.hashCode());
            result = prime * result + ((executeMethodName == null) ? 0 : executeMethodName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ExecuteConfig other = (ExecuteConfig) obj;
            if (className == null) {
                if (other.className != null)
                    return false;
            } else if (!className.equals(other.className))
                return false;
            if (classPath == null) {
                if (other.classPath != null)
                    return false;
            } else if (!classPath.equals(other.classPath))
                return false;
            if (executeMethodName == null) {
                if (other.executeMethodName != null)
                    return false;
            } else if (!executeMethodName.equals(other.executeMethodName))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return className + "." + executeMethodName + "() - " + classPath;
        }

        public String toProp() {
            return classPath + "," + className + "," + executeMethodName;
        }

        public ExecuteConfig() {
        }

        public ExecuteConfig(String propValue) {
            propValue = StringUtils.defaultString(propValue);
            String[] vals = propValue.split(",", -1);
            if (vals.length != 3) {
                throw new RuntimeException("資料有誤!: " + propValue);
            }
            classPath = StringUtils.trimToEmpty(vals[0]);
            className = StringUtils.trimToEmpty(vals[1]);
            executeMethodName = StringUtils.trimToEmpty(vals[2]);
        }
    }

    static DefaultListModel getTempModel() {
        DefaultListModel tempModel = JListUtil.createModel();
        if (inst == null) {
            return tempModel;
        }
        initTempModelTimeMointerMap();
        for (long checkInTime : inst.tempModelTimeMointerMap.asMap().keySet()) {
            tempModel.addElement(inst.tempModelTimeMointerMap.asMap().get(checkInTime));
        }
        return tempModel;
    }

    private Object getSelectNodeObject() {
        Object object = null;
        DefaultMutableTreeNode currentNode = uiCarrier.getJtreeUtil().getSelectItem();
        if (currentNode != null && currentNode.getUserObject() != null) {
            Object userObject = currentNode.getUserObject();
            if (userObject instanceof JObject && ((JObject) userObject).object != null) {
                object = ((JObject) userObject).object;
            } else {
                getLogger().debug("!!getSelectNodeObject - userObject(非JObject) : " + userObject);
            }
        } else {
            getLogger().debug("!!getSelectNodeObject - 節點為空");
        }
        return object;
    }

    private void appendNodeInfoComboBox(Object object) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        if (object instanceof JObject) {
            JObject obj = (JObject) object;
            if (obj.object != null) {
                model.addElement(obj.object.getClass());
                for (Class<?> clz = obj.object.getClass(); (clz = clz.getSuperclass()) != null;) {
                    model.addElement(clz);
                }
            }
        } else {
            getLogger().debug("appendNodeInfoComboBox --> 非JObject : " + object);
            ;
        }
        if (model.getSize() == 0) {
            model.addElement("NA");
        }
        uiCarrier.getNodeInfoComboBox().setModel(model);
    }

    private JButton getIndicateNodeClassBtn() {
        uiCarrier.getIndicateNodeClassBtn().setText("報告");
        uiCarrier.getIndicateNodeClassBtn().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Object object = getSelectNodeObject();
                if (object == null) {
                    JCommonUtil._jOptionPane_showMessageDialog_error_NonUICompatible("物件為null,或是不支援顯示的類型!");
                    return;
                }
                Object clzX = uiCarrier.getNodeInfoComboBox().getSelectedItem();
                if (!(clzX instanceof Class)) {
                    JCommonUtil._jOptionPane_showMessageDialog_error_NonUICompatible("類別有誤:" + clzX);
                    return;
                }
                Class<?> clz = (Class<?>) clzX;
                JObject mod = new JObject();
                StringBuilder sb = new StringBuilder();
                sb.append("[" + object + "]\n");
                sb.append("\tas class[" + clz.getName() + "]\n");
                for (Field f : clz.getDeclaredFields()) {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(object);
                        String modifiers = mod.getModifier(f.getModifiers());
                        sb.append("[F][" + modifiers + "]" + f.getName() + "(" + f.getType() + ")\n");
                        try {
                            sb.append("\t" + ReflectionToStringBuilder.toString(value, ToStringStyle.MULTI_LINE_STYLE) + "\n");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            sb.append("\t" + value + "\n");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (Method m : clz.getDeclaredMethods()) {
                    m.setAccessible(true);
                    try {
                        List<String> parameterList = new ArrayList<String>();
                        if (m.getParameterTypes() != null && m.getParameterTypes().length > 0) {
                            for (Class<?> clz2 : m.getParameterTypes()) {
                                parameterList.add(clz2.getSimpleName());
                            }
                        }
                        String modifiers = mod.getModifier(m.getModifiers());
                        sb.append("[M][" + modifiers + "]" + "(" + m.getReturnType().getSimpleName() + ")" + m.getName() + parameterList + "\n");
                        sb.append("\t" + DebugMointerUI.this.getMethodValue(object, m) + "\n");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                uiCarrier.getShowInfoArea2().setText(sb.toString());
                JCommonUtil._jOptionPane_showMessageDialog_info_NonUICompatible("報告於[物件蓋觀->變數]");
            }
        });
        return uiCarrier.getIndicateNodeClassBtn();
    }

    // 放在constructor 會需要改名為_init_
    private class MethodNameTextHandler {
        private String replace(String methodName) {
            if ("<init>".equals(methodName)) {
                return "__init__";
            }
            return methodName;
        }

        private String getExecuteMethodNameText() {
            return replace(inst.uiCarrier.getExecuteMethodNameText().getText());
        }

        private void setExecuteMethodNameText(String methodName) {
            inst.uiCarrier.getExecuteMethodNameText().setText(replace(methodName));
        }
    }

    // 以指定類別顯示細節 ↑↑↑↑↑↑

    public static Logger2File getLogger() {
        if (log == null) {
            try {
                if (inst == null) {
                    inst = getInstance(false);
                }
                final String basepathKey = "log.basepath";
                if (inst.configProp.containsKey(basepathKey)) {
                    String basePath = inst.configProp.getProperty(basepathKey);
                    log = new Logger2File(basePath, DebugMointerUI.class.getSimpleName() + "_Logger");
                } else {
                    log = new Logger2File(DebugMointerUI.class.getSimpleName() + "_Logger");
                }
            } catch (Exception ex) {
                ex.printStackTrace();

                // 在linux上必須用此做法
                log = new Logger2File(DebugMointerUI.class.getSimpleName() + "_Logger");
            }
        }
        return log;
    }

    /**
     * 初始化configProp
     */
    private Properties initConfigProp() {
        Properties prop = new Properties();
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                System.out.println("# configFile create : " + configFile);
            }
            prop.load(new FileInputStream(configFile));
            inst.configProp = prop;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    private void showErrorLogInArea(Throwable ex) {
        try {
            if (ex != null && inst.uiCarrier.getErrorLogArea() != null) {
                ex.printStackTrace(JCommonUtil.getNewPrintStream2JTextArea(inst.uiCarrier.getErrorLogArea(), -1, false));
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
    }

    private static void initTempModelTimeMointerMap() {
        if (inst.tempModelTimeMointerMap == null) {
            RemovalListener<Long, Object> listener = new RemovalListener<Long, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Long, Object> notify) {
                    if (notify.wasEvicted()) {
                        getLogger().debug("移除過期  : " + DateFormatUtils.format(notify.getKey(), "yyyy/MM/dd HH:mm:ss") + " -> " + notify.getValue() + "");
                    }
                }
            };
            inst.tempModelTimeMointerMap = CacheBuilder.newBuilder().recordStats().maximumSize(2000)//
                    // expireAfterWrite : after this period, the object will be
                    // evicted from the cache, and replaced the next time it is
                    // requested.
                    .expireAfterWrite(60, TimeUnit.MINUTES)
                    // refreshAfterWrite : after this period, the object will be
                    // refreshed using the loadCache method. (with our new
                    // price)
                    // .refreshAfterWrite(60, TimeUnit.SECONDS)
                    .removalListener(listener)//
                    .build();
        }
    }

    private static boolean checkIfNeedRunThisUI() {
        if (configFile.exists()) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(configFile));
            } catch (Exception e) {
            }
            final String key = "run";
            if (prop.containsKey(key) && "off".equalsIgnoreCase(prop.getProperty(key))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 設定 動態類別資訊
     */
    private void setDynamicClassArea() {
        if (uiCarrier.isUseMockUI()) {
            return;
        }

        String classpath = StringUtils.defaultString(uiCarrier.getClasspathText().getText());
        String className = StringUtils.defaultString(uiCarrier.getClassNameText().getText());
        String methodName = StringUtils.defaultString(methodNameHandler.getExecuteMethodNameText());
        StringBuilder sb = new StringBuilder();
        try {
            String paramsStr = "";
            StringBuilder fb = new StringBuilder();
            StringBuilder logb1 = new StringBuilder();
            StringBuilder logb2 = new StringBuilder();
            List<String> paramStrList = new ArrayList<String>();
            Set<String> importList = new HashSet<String>();
            for (int ii = 1; ii < mointerObjects.length - 1; ii++) {
                if (mointerObjects[ii] != null) {
                    Class<?> clz = mointerObjects[ii].getClass();
                    if (!DebugMointerTypeUtil.isPrimitive(clz) && clz.getInterfaces().length > 0) {
                        clz = clz.getInterfaces()[0];
                    }

                    paramStrList.add("@DebugMointerUIMapping(index=" + ii + ") " + clz.getName() + " arg" + ii);
                    importList.add("import " + clz.getName() + "; \n");

                    fb.append("\t@Resource(name=\"" + ii + "\")\n");
                    fb.append("\t" + clz.getName() + " field" + ii + ";\n");

                    logb1.append(String.format("\tlogger.debug(\"arg%1$d = \" + arg%1$d);\n", ii));
                    logb2.append(String.format("\tlogger.debug(\"field%1$d = \" + field%1$d);\n", ii));
                }
            }
            paramsStr = StringUtils.join(paramStrList, ",\n");
            String packageName = className.substring(0, className.lastIndexOf("."));
            String className_ = className.substring(className.lastIndexOf(".") + 1);
            sb.append("//" + classpath + "\n");
            sb.append("package " + packageName + ";\n");
            sb.append("\n");
            sb.append("import gtu.log.finder.DebugMointerMappingFieldForMethod.DebugMointerUIMapping; \n");
            sb.append("import gtu.log.Logger2File; \n");
            sb.append("import gtu.log.finder.DebugMointerUI; \n");
            sb.append("import javax.annotation.Resource; \n");
            sb.append("import java.util.*; \n");
            for (String imp : importList) {
                sb.append(imp);
            }
            sb.append("\n");
            sb.append("public class " + className_ + " {\n");
            sb.append("\t private static Logger2File logger = DebugMointerUI.getLogger();\n");
            sb.append(fb);
            sb.append("\t public Object " + methodName + "(" + paramsStr + ") {\n");
            sb.append(logb1);
            sb.append(logb2);
            sb.append("\t\t return null;\n");
            sb.append("\t}\n");
            sb.append("}\n");
        } catch (Exception ex) {
            ex.printStackTrace();
            sb = new StringBuilder();
        }
        uiCarrier.getDynamicClassArea().setText(sb.toString());
    }
    
    public static Object[] getMointerObjects() {
        return inst.mointerObjects;
    }
}
