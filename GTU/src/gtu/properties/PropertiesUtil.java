package gtu.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import gtu.log.Log;
import gtu.swing.util.JCommonUtil;

public class PropertiesUtil {

    public static void main(String[] args) throws IOException, Exception {
        System.out.println("done...");
    }

    private static boolean isWindows = false;

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    private PropertiesUtil() {
    }

    private static File checkFilePath(File file) {
        try {
            if (!file.exists()) {
                file = new File(URLDecoder.decode(file.getAbsolutePath(), "utf8"));
                System.err.println("---->" + file);
            }
            return new File(file.getAbsolutePath());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return file;
        }
    }

    public static boolean isClassInJar(Class<?> clz) {
        Class<?> outterClz = clz.getEnclosingClass();
        if (outterClz != null) {
            clz = outterClz;
        }
        URL url = clz.getResource(clz.getSimpleName() + ".class");
        String protocal = url.getProtocol();
        // String filepath = url.getFile();
        boolean isInJar = "jar".equals(protocal);
        System.out.println("[isClassInJar] : " + isInJar);
        return isInJar;
    }

    public static File getJarCurrentPath(Class<?> clz) {
        Class<?> outterClz = clz.getEnclosingClass();
        if (outterClz != null) {
            clz = outterClz;
        }
        URL url = clz.getResource(clz.getSimpleName() + ".class");
        String protocal = url.getProtocol();
        String filepath = url.getFile();

        String path = null;
        if (protocal.equals("zip")) {
            path = filepath;
            path = path.substring(0, path.lastIndexOf("!"));
            File file = new File(path).getParentFile();
            file = checkFilePath(file);
            System.err.println("use zip : " + file);
            return file;
        }
        if (protocal.equals("jar")) {
            path = filepath.replaceFirst("file:", "");
            path = path.substring(0, path.lastIndexOf("!"));
            File file = new File(path).getParentFile();
            file = checkFilePath(file);
            System.err.println("use jar : " + file);
            return file;
        }
        if (protocal.equals("file")) {
            if (isWindows) {
                path = filepath.startsWith("/") ? filepath.substring(1) : filepath;
            } else {
                path = filepath;
            }
            File file = new File(path).getParentFile();
            System.err.println("use file : " + file);
            file = checkFilePath(file);
            return file;
        }
        File defaultFile = new File(clz.getResource("").getFile()).getAbsoluteFile();
        defaultFile = checkFilePath(defaultFile);
        System.err.println("use default : " + defaultFile);
        try {
            Log.Setting.NORMAL.apply();
            Log.debug("protocal = " + protocal);
            Log.debug("filepath = " + filepath);
            JCommonUtil._jOptionPane_showMessageDialog_error(//
                    "protocal : \n" + protocal + "\nfilepath : \n" + filepath);
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
        return defaultFile;
    }

    public static String getComments(File file) {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file)));
            StringBuffer sb = new StringBuffer();
            for (String line = null; (line = reader.readLine()) != null;) {
                if (line.startsWith("#")) {
                    sb.append(line + "\r\n");
                }
            }
            String comment = UnicodeToUTF8Util.unicodeToUtf8(sb.toString());
            return comment;
        } catch (Exception e) {
            throw new RuntimeException("getComment ERR : " + e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 不會咬住檔案的開檔方式
     * 
     * @param inputStream
     * @return
     */
    public static Properties loadProperties(InputStream inputStream, Properties _prop) {
        Properties prop = new Properties();
        try {
            if (_prop != null) {
                prop = _prop;
            }
            prop.load(inputStream);
            return prop;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 不會咬住檔案的開檔方式
     * 
     * @param inputStream
     * @return
     */
    public static Properties loadProperties(File file, Properties _prop, boolean failThrow) {
        FileInputStream inputStream = null;
        Properties prop = new Properties();
        try {
            inputStream = new FileInputStream(file);
            if (_prop != null) {
                prop = _prop;
            }
            prop.load(inputStream);
            return prop;
        } catch (Exception ex) {
            ex.printStackTrace();
            if (failThrow) {
                throw new RuntimeException(ex);
            }
            return prop;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 不會咬住檔案的存檔方式
     * 
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static void storeProperties(Properties prop, OutputStream ouputStream, String comment) {
        try {
            prop.store(ouputStream, comment);
        } catch (Exception ex) {
            throw new RuntimeException("[storeProperties] 存檔失敗  , ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                ouputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ouputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void storeProperties(Properties prop, File configFile, String comment) {
        try {
            prop.store(new FileOutputStream(configFile), comment);
        } catch (Exception ex) {
            throw new RuntimeException("[storeProperties] 存檔失敗 ,檔案 : " + configFile + " , ERR : " + ex.getMessage(), ex);
        }
    }
}
