package gtu.runtime;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gtu.log.JdkLoggerUtil;

public class DesktopUtil {

    private static Logger logger = Logger.getLogger(DesktopUtil.class.getSimpleName());

    static {
        JdkLoggerUtil.setupRootLogLevel(Level.INFO);
        logger.setLevel(Level.ALL);
    }

    public static File getFile(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            File tempFile = null;
            //try 1
            if ((tempFile = new File(url)).exists()) {
                return tempFile;
            }
            if (url.startsWith("file:")) {
                // try 2
                if ((tempFile = new File(new URL(url).getFile())).exists()) {
                    return tempFile;
                }
                // try 3
                Pattern ptn = Pattern.compile("file\\:[\\/]+(.*)");
                Matcher mth = ptn.matcher(url);
                if (mth.find()) {
                    if ((tempFile = new File(mth.group(1))).exists()) {
                        return tempFile;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("getFile ERR : " + e.getMessage(), e);
        }
    }

    public static void openDir(String url) {
        try {
            File f = getFile(url);
            if(f == null) {
                return;
            }
            if (f.isFile()) {
                f = f.getParentFile();
            }
            if (f.isDirectory()) {
                Desktop.getDesktop().open(f);
            }
        } catch (Exception e) {
            throw new RuntimeException("openDir ERR : " + e.getMessage(), e);
        }
    }

    public static void browse(String url) {
        try {
            URL url2 = new URL(url);
            logger.log(Level.INFO, "url : " + url2);
            if (url.startsWith("file:")) {
                try {
                    Desktop.getDesktop().browse(url2.toURI());// 嘗試正常流程
                } catch (Exception ex) {
                    logger.log(Level.WARNING, "browse try 1 : " + ex.getMessage());
                    Desktop.getDesktop().open(getFile(url));
                }
            } else {
                Desktop.getDesktop().browse(url2.toURI());
            }
        } catch (Exception ex) {
            throw new RuntimeException("browse ERR : " + ex.getMessage(), ex);
        }
    }
}
