package gtu.dll;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class DllUtil {
    
    private static Logger logger = Logger.getLogger(DllUtil.class);

    public static void main(String[] args) {
        DllUtil.setJavaLibraryPath("/opt/tibco/tibrv/8.4/lib");
    }

    public static void setJavaLibraryPath(String newPath) {
        try {
            System.setProperty("java.library.path", newPath);
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception ex) {
            logger.error("ERROR : " + ex.getMessage(), ex);
        }
    }
}
