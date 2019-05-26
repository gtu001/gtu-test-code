package gtu.yaml.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import gtu.log.JdkLoggerUtil;
import gtu.properties.PropertiesUtil;
import gtu.runtime.DesktopUtil;

public class YamlUtilBean<T> {

    private File yamlFile;
    private Class<T> yamlClz;
    private List<T> yamlLst;
    private Map<String, Class<?>> classMap;

    private static final Logger logger = JdkLoggerUtil.getLogger(YamlUtilBean.class, true);

    static {
        // JdkLoggerUtil.setupRootLogLevel(Level.INFO);
    }

    public void reload() {
        init(yamlFile, yamlClz, classMap);
    }

    public void init(File customFile, Class<T> targetClz, Map<String, Class<?>> classMap) {
        try {
            this.yamlFile = customFile;
            this.yamlClz = targetClz;
            this.classMap = classMap;
            logger.info("configFile : " + yamlFile);
            if (!yamlFile.exists()) {
                if (!yamlFile.getParentFile().exists()) {
                    yamlFile.getParentFile().mkdirs();
                }
                logger.info("!!!!! 設定檔不存在建立新檔 : " + yamlFile);
                yamlFile.createNewFile();
            }
            yamlLst = YamlMapUtil.getInstance().loadFromFile(this.yamlFile, this.yamlClz, this.classMap);
            if (yamlLst == null) {
                yamlLst = new ArrayList<T>();
                store();
            }
            logger.info("configFile size : " + "");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
        }
    }

    public YamlUtilBean(File customFile, Class<T> targetClz, Map<String, Class<?>> classMap) {
        this.init(customFile, targetClz, classMap);
    }

    public YamlUtilBean(Class<?> clz, Class<T> targetClz, Map<String, Class<?>> classMap) {
        this.init(new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_config.yaml"), targetClz, classMap);
    }

    public YamlUtilBean(Class<?> clz, String fileName, Class<T> targetClz, Map<String, Class<?>> classMap) {
        this.init(new File(PropertiesUtil.getJarCurrentPath(clz), fileName + "_config.yaml"), targetClz, classMap);
    }

    public YamlUtilBean(File parentDir, String fileName, Class<T> targetClz, Map<String, Class<?>> classMap) {
        this.init(new File(parentDir, fileName + "_config.yaml"), targetClz, classMap);
    }

    public boolean contains(T bean) {
        return this.yamlLst.contains(bean);
    }

    public T getProperty(T bean) {
        int index = this.yamlLst.indexOf(bean);
        if (index != -1) {
            return this.yamlLst.get(index);
        }
        return null;
    }

    public void setProperty(T bean) {
        this.remove(bean);
        this.yamlLst.add(bean);
    }

    public void remove(T bean) {
        if (this.yamlLst.contains(bean)) {
            this.yamlLst.remove(bean);
        }
    }

    public void store() {
        YamlMapUtil.getInstance().saveToFile(yamlFile, yamlLst, false);
        logger.info("store success!");
    }

    public void openDir() {
        DesktopUtil.openDir(yamlFile);
    }

    public void browse() {
        try {
            DesktopUtil.browse(yamlFile.toURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> getConfigProp() {
        return yamlLst;
    }

    public File getPropFile() {
        return yamlFile;
    }
}
