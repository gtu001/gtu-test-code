package gtu.swing.util;

import java.io.File;

import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;

public class JSwingCommonConfigUtil {

    public static PropertiesUtilBean checkTestingPropertiesUtilBean(PropertiesUtilBean config, Class<?> mainFrameClz, String cfgSimpleClassName) {
        if (!PropertiesUtil.isClassInJar(mainFrameClz)) {
            if (OsInfoUtil.isWindows()) {
                return new PropertiesUtilBean(new File("D:/my_tool"), cfgSimpleClassName);
            } else {
                return new PropertiesUtilBean(new File("/media/gtu001/OLD_D/my_tool/"), cfgSimpleClassName);
            }
        }
        return config;
    }
}
