package gtu.runtime;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import gtu.log.JdkLoggerUtil;

public class DesktopUtil {

    private static Logger logger = Logger.getLogger(DesktopUtil.class.getSimpleName());

    private static boolean isWindows = false;

    static {
        JdkLoggerUtil.setupRootLogLevel(Level.INFO);
        logger.setLevel(Level.ALL);

        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    public static void main(String[] args) {
        boolean bool = isFile("file://C:/Users/gtu00/Downloads/勇者鬥惡龍1");
        System.out.println(bool);
        boolean bool2 = isFile("http://goods.ruten.com.tw/item/show?21816895673680");
        System.out.println(bool2);
        System.out.println("done...");
    }

    public static boolean isFile(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
            if (new File(url).exists()) {
                return true;
            }
            try {
                if (new URL(url).getProtocol().equals("file")) {
                    return true;
                }
            } catch (java.net.MalformedURLException ex) {
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("isFile ERR : " + e.getMessage(), e);
        }
    }

    public static File getFile_ignoreFailed(String url) {
        try {
            return getFile(url);
        } catch (Throwable ex) {
            System.err.println("<getFile_ignoreFailed> WARNING : " + ex.getMessage());
            return null;
        }
    }

    public static File getFile(String url) {
        try {
            if (!isFile(url)) {
                return null;
            }
            url = URLDecoder.decode(url, "UTF-8");
            File tempFile = null;
            // try 1
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
            openDir(f);
        } catch (Exception e) {
            throw new RuntimeException("openDir ERR : " + e.getMessage(), e);
        }
    }

    public static void openDir(File f) {
        try {
            if (f == null) {
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
                    File file = getFile(url);
                    logger.log(Level.WARNING, "file : " + file);
                    if (file == null || !file.exists()) {
                        throw new Exception("file : " + file + " not exists !!");
                    } else {
                        try {
                            if (isWindows) {
                                Desktop.getDesktop().open(file);
                            } else {
                                RuntimeBatPromptModeUtil inst = RuntimeBatPromptModeUtil.newInstance();
                                inst.command(file.getAbsolutePath());
                                inst.apply();
                            }
                        } catch (Exception ex1) {
                            logger.log(Level.WARNING, "browse try 2 : " + ex1.getMessage());
                            Runtime.getRuntime().exec(String.format("cmd /c start notepad \"%s\"", file));
                        }
                    }
                }
            } else {
                Desktop.getDesktop().browse(url2.toURI());
            }
        } catch (Exception ex) {
            throw new RuntimeException("browse ERR : " + ex.getMessage() + " --> " + url, ex);
        }
    }
}
