package gtu.properties;

import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.log.JdkLoggerUtil;
import gtu.runtime.DesktopUtil;

public class PropertiesUtilBean {

    private Properties configProp;
    private File propFile;
    private FileChangeHandler fileChangeHandler;
    private _JFrameReflectionToConfig configReflect = new _JFrameReflectionToConfig();

    private static final Logger logger = JdkLoggerUtil.getLogger(PropertiesUtilBean.class, true);

    static {
        // JdkLoggerUtil.setupRootLogLevel(Level.INFO);
    }

    public void reload() {
        init(propFile);
    }

    public void init(File customFile) {
        FileInputStream fis = null;
        try {
            propFile = customFile;
            fileChangeHandler = new FileChangeHandler(propFile);
            logger.info("configFile : " + propFile);
            if (!propFile.exists()) {
                if (!propFile.getParentFile().exists()) {
                    propFile.getParentFile().mkdirs();
                }
                logger.info("!!!!! 設定檔不存在建立新檔 : " + propFile);
                propFile.createNewFile();
            }
            configProp = new Properties();
            fis = new FileInputStream(propFile);
            configProp.load(fis);
            logger.info("configFile size : " + configProp.size());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class FileChangeHandler {
        File file;
        long lastModifyTime = -1;

        FileChangeHandler(File file) {
            this.file = file;
            lastModifyTime = file.lastModified();
        }

        public void resetLastModifiedUnderControl() {
            this.lastModifyTime = file.lastModified();
        }

        public boolean isChange() {
            return file.lastModified() != lastModifyTime;
        }

        public void merge(Properties currentProp) throws FileNotFoundException, IOException {
            Properties changeProp = new Properties();
            if (isChange()) {
                changeProp.load(new FileInputStream(file));
                for (Enumeration<?> enu = currentProp.keys(); enu.hasMoreElements();) {
                    String key = (String) enu.nextElement();
                    changeProp.remove(key);
                }
                currentProp.putAll(changeProp);
            }
        }
    }

    public boolean isFileChangeUncontrolled() {
        return fileChangeHandler.isChange();
    }

    public PropertiesUtilBean(File customFile) {
        this.init(customFile);
    }

    public PropertiesUtilBean(Class<?> clz) {
        this.init(new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_config.properties"));
    }

    public PropertiesUtilBean(Class<?> clz, String fileName) {
        this.init(new File(PropertiesUtil.getJarCurrentPath(clz), fileName + "_config.properties"));
    }
    
    public PropertiesUtilBean(File parentDir, String fileName) {
        this.init(new File(parentDir, fileName + "_config.properties"));
    }

    public void store() {
        try {
            fileChangeHandler.merge(configProp);
            String remark = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS").format(new Date());
            PropertiesUtil.storeProperties(configProp, propFile, "SAVE_TIME : " + remark);
            fileChangeHandler.resetLastModifiedUnderControl();
            logger.info("store success!");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void openDir() {
        DesktopUtil.openDir(propFile);
    }

    public void browse() {
        try {
            DesktopUtil.browse(propFile.toURL().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Properties getConfigProp() {
        return configProp;
    }

    public File getPropFile() {
        return propFile;
    }

    public void reflectInit(Window jframe) {
        configReflect.setToUI(jframe, null);
    }

    public void reflectSetConfig(Window jframe) {
        configReflect.setToConfig(jframe, null);
    }

    public void reflectInit(Window jframe, List<JComponent> componentArry) {
        configReflect.setToUI(jframe, componentArry);
    }

    public void reflectSetConfig(Window jframe, List<JComponent> componentArry) {
        configReflect.setToConfig(jframe, componentArry);
    }

    // swing 自動化設直
    private class _JFrameReflectionToConfig {
        private void setToUI(Window jframe, List<JComponent> componentArry) {
            for (Field f : jframe.getClass().getDeclaredFields()) {
                if (configProp.containsKey(f.getName())) {
                    String value = configProp.getProperty(f.getName());
                    try {
                        JComponent component = (JComponent) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                        if (!isMatch(component, componentArry)) {
                            continue;
                        }
                        if (f.getType() == JTextField.class) {
                            JTextField text = (JTextField) component;
                            text.setText(value);
                            logger.info("set " + f.getName() + " = " + value);
                        } else if (f.getType() == JCheckBox.class) {
                            JCheckBox text = (JCheckBox) component;
                            text.setSelected(Boolean.parseBoolean(value));
                            logger.info("set " + f.getName() + " = " + Boolean.parseBoolean(value));
                        } else if (f.getType() == JComboBox.class) {
                            JComboBox text = (JComboBox) component;
                            int selectIndex = Integer.parseInt(value.split("^")[0]);
                            text.setSelectedIndex(selectIndex);
                            logger.info("set " + f.getName() + " = " + value);
                        } else if (f.getType() == JTextArea.class) {
                            JTextArea text = (JTextArea) component;
                            text.setText(value);
                            logger.info("set " + f.getName() + " = " + value);
                        } else if (f.getType() == JTextPane.class) {
                            JTextPane text = (JTextPane) component;
                            text.setText(value);
                            logger.info("set " + f.getName() + " = " + value);
                        }
                    } catch (Exception e) {
                        logger.warning("reflection field not found : " + f.getName() + " -> " + e.getMessage());
                    }
                }
            }
        }

        private boolean isMatch(JComponent component, List<JComponent> componentArry) {
            if (componentArry == null) {
                return true;
            }
            return componentArry.contains(component);
        }

        private void setToConfig(Window jframe, List<JComponent> componentArry) {
            for (Field f : jframe.getClass().getDeclaredFields()) {
                try {
                    JComponent component = (JComponent) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                    if (!isMatch(component, componentArry)) {
                        continue;
                    }
                    if (f.getType() == JTextField.class) {
                        JTextField text = (JTextField) component;
                        configProp.setProperty(f.getName(), StringUtils.trimToEmpty(text.getText()));
                        logger.info("set " + f.getName() + " = " + StringUtils.trimToEmpty(text.getText()));
                    } else if (f.getType() == JCheckBox.class) {
                        JCheckBox text = (JCheckBox) component;
                        configProp.setProperty(f.getName(), String.valueOf(text.isSelected()));
                        logger.info("set " + f.getName() + " = " + String.valueOf(text.isSelected()));
                    } else if (f.getType() == JComboBox.class) {
                        JComboBox text = (JComboBox) component;
                        String value = text.getSelectedIndex() + "^" + text.getSelectedItem();
                        configProp.setProperty(f.getName(), value);
                        logger.info("set " + f.getName() + " = " + value);
                    } else if (f.getType() == JTextArea.class) {
                        JTextArea text = (JTextArea) component;
                        configProp.setProperty(f.getName(), StringUtils.trimToEmpty(text.getText()));
                        logger.info("set " + f.getName() + " = " + StringUtils.trimToEmpty(text.getText()));
                    } else if (f.getType() == JTextPane.class) {
                        JTextPane text = (JTextPane) component;
                        configProp.setProperty(f.getName(), StringUtils.trimToEmpty(text.getText()));
                        logger.info("set " + f.getName() + " = " + StringUtils.trimToEmpty(text.getText()));
                    }
                } catch (Exception e) {
                    logger.warning("reflection field not found : " + f.getName() + " -> " + e.getMessage());
                }
            }
        }
    }
}
