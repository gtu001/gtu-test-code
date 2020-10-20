package gtu.util;

public class OsInfoUtil {

    private static boolean isWindows = false;
    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    public static boolean isWindows() {
        return isWindows;
    }
}
