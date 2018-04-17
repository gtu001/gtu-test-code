package gtu.runtime;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import gtu.log.JdkLoggerUtil;

public class DesktopUtil {

    private static Logger logger = Logger.getLogger(DesktopUtil.class.getSimpleName());

    static {
        JdkLoggerUtil.setupRootLogLevel(Level.INFO);
        logger.setLevel(Level.ALL);
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
                    Desktop.getDesktop().open(new File(URLDecoder.decode(url2.getFile(), "UTF-8")));
                }
            } else {
                Desktop.getDesktop().browse(url2.toURI());
            }
        } catch (Exception ex) {
            throw new RuntimeException("browse ERR : " + ex.getMessage(), ex);
        }
    }
}
