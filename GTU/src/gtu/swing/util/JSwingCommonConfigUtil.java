package gtu.swing.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import gtu.file.OsInfoUtil;
import gtu.properties.PropertiesUtil;
import gtu.properties.PropertiesUtilBean;

public class JSwingCommonConfigUtil {

    public static PropertiesUtilBean checkTestingPropertiesUtilBean(PropertiesUtilBean config, Class<?> mainFrameClz, String cfgSimpleClassName) {
        File dir = null;
        if (!PropertiesUtil.isClassInJar(mainFrameClz)) {
            if (OsInfoUtil.isWindows()) {
                dir = new File("D:/my_tool");
            } else {
                dir = new File("/media/gtu001/OLD_D/my_tool/");
            }
        }
        if (dir == null || dir.exists()) {
            dir = PropertiesUtil.getJarCurrentPath(mainFrameClz);
        }
        if (dir.exists()) {
            return new PropertiesUtilBean(dir, cfgSimpleClassName);
        }
        return config;
    }

    public static PropertiesUtilBean checkTestingPropertiesUtilBean_diffConfig(PropertiesUtilBean config, Class<?> mainFrameClz, String middleDir) {
        String simpleName = mainFrameClz.getSimpleName();
        if (OsInfoUtil.isWindows()) {
            simpleName = simpleName + "_win10";
        } else {
            simpleName = simpleName + "_linux";
        }
        middleDir = StringUtils.trimToEmpty(middleDir);
        return checkTestingPropertiesUtilBean(config, mainFrameClz, middleDir + simpleName);
    }
}
