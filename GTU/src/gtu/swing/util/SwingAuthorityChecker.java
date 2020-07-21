package gtu.swing.util;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import gtu.properties.PropertiesUtil;

public class SwingAuthorityChecker {

    private Properties config = new Properties();
    private boolean isDebug = true;
    private Class<?> registerClz;
    private File file;

    public SwingAuthorityChecker(Class<?> clz) {
        this.registerClz = clz;
        this.file = new File("/home/gtu001/桌面/" + registerClz.getSimpleName() + "_auth.properties");
        if (this.file.exists()) {
            PropertiesUtil.loadProperties(file, config, true);
        }
    }

    public void store() {
        PropertiesUtil.storeProperties(config, file, "authFile");
    }

    public void check(String key) {
        if (!config.containsKey(key)) {
            if (isDebug) {
                String authVal = JCommonUtil._jOptionPane_showInputDialog("您無此權限:" + key + ",設定權限？", "0");
                if (StringUtils.isBlank(authVal)) {
                    authVal = "0";
                }
                config.setProperty(key, authVal);
                store();
            }
        }
        boolean isNoAuth = Integer.parseInt(config.getProperty(key)) == 0;
        if (isNoAuth) {
            Validate.isTrue(false, "您無此權限:" + key);
        }
    }
}
