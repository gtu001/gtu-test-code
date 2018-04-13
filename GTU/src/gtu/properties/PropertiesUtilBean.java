package gtu.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.StringUtils;

public class PropertiesUtilBean {

    private Properties configProp;
    private File propFile;
    private _JFrameReflectionToConfig configReflect = new _JFrameReflectionToConfig();

    public PropertiesUtilBean(Class<?> clz) {
        try {
            configProp = new Properties();
            propFile = new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_config.properties");
            if (!propFile.exists()) {
                propFile.createNewFile();
            }
            configProp = new Properties();
            configProp.load(new FileInputStream(propFile));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void store() {
        try {
            configProp.store(new FileOutputStream(propFile), "ClassConfig");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Properties getConfigProp() {
        return configProp;
    }

    public File getPropFile() {
        return propFile;
    }

    public void init(JFrame jframe) {
        configReflect.setToUI(jframe);
    }

    public void setConfig(JFrame jframe) {
        configReflect.setToConfig(jframe);
    }

    // swing 自動化設直
    private class _JFrameReflectionToConfig {
        private void setToUI(JFrame jframe) {
            for (Field f : jframe.getClass().getDeclaredFields()) {
                if (configProp.containsKey(f.getName())) {
                    String value = configProp.getProperty(f.getName());
                    try {
                        if (f.getType() == JTextField.class) {
                            JTextField text = (JTextField) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                            text.setText(value);
                            System.out.println("set " + f.getName() + " = " + value);
                        } else if (f.getType() == JCheckBox.class) {
                            JCheckBox text = (JCheckBox) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                            text.setSelected(Boolean.parseBoolean(value));
                            System.out.println("set " + f.getName() + " = " + Boolean.parseBoolean(value));
                        } else if (f.getType() == JComboBox.class) {
                            JComboBox text = (JComboBox) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                            int selectIndex = Integer.parseInt(value.split("^")[0]);
                            text.setSelectedIndex(selectIndex);
                            System.out.println("set " + f.getName() + " = " + value);
                        } else if (f.getType() == JTextArea.class) {
                            JTextArea text = (JTextArea) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                            text.setText(value);
                            System.out.println("set " + f.getName() + " = " + value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void setToConfig(JFrame jframe) {
            for (Field f : jframe.getClass().getDeclaredFields()) {
                try {
                    if (f.getType() == JTextField.class) {
                        JTextField text = (JTextField) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                        configProp.setProperty(f.getName(), StringUtils.trimToEmpty(text.getText()));
                        System.out.println("set " + f.getName() + " = " + StringUtils.trimToEmpty(text.getText()));
                    } else if (f.getType() == JCheckBox.class) {
                        JCheckBox text = (JCheckBox) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                        configProp.setProperty(f.getName(), String.valueOf(text.isSelected()));
                        System.out.println("set " + f.getName() + " = " + String.valueOf(text.isSelected()));
                    } else if (f.getType() == JComboBox.class) {
                        JComboBox text = (JComboBox) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                        String value = text.getSelectedIndex() + "^" + text.getSelectedItem();
                        configProp.setProperty(f.getName(), value);
                        System.out.println("set " + f.getName() + " = " + value);
                    } else if (f.getType() == JTextArea.class) {
                        JTextArea text = (JTextArea) FieldUtils.readDeclaredField(jframe, f.getName(), true);
                        configProp.setProperty(f.getName(), StringUtils.trimToEmpty(text.getText()));
                        System.out.println("set " + f.getName() + " = " + StringUtils.trimToEmpty(text.getText()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
