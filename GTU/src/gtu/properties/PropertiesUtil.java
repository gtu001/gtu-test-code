package gtu.properties;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.log.Log;
import gtu.swing.util.JCommonUtil;

public class PropertiesUtil {

    public static void main(String[] args) throws IOException, Exception {
        File file = new File("E:\\workstuff\\workstuff\\workspace_scsb\\Config\\UAT_Config\\AS.conf");
        Properties prop = PropertiesUtil.loadByChineseUTF8(file);
        for (Object key : prop.keySet()) {
            String val = prop.getProperty((String) key);
            System.out.println("-->>" + key + "\t" + val);
        }
        System.out.println("done...");
    }

    private PropertiesUtil() {
    }

    public static Properties loadByChineseUTF8(InputStream in) {
        Properties prop = new Properties();
        BufferedInputStream data = new BufferedInputStream(in);
        data.mark(0);
        // 可直接讀取中文黨
        try {
            List<String> lst = IOUtils.readLines(data, "utf8");
            for (String line : lst) {
                if (line.startsWith("#") || StringUtils.isBlank(line)) {
                    continue;
                }
                if (line.contains("=")) {
                    String[] vals = line.split("=", -1);
                    prop.setProperty(vals[0], vals[1]);
                } else {
                    prop.setProperty(line, line);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("loadByChineseUTF8 ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                data.reset();
            } catch (Exception e) {
            }
        }
        return prop;
    }

    public static Properties loadByChineseUTF8(File file) {
        try {
            return loadByChineseUTF8(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("loadByChineseUTF8 ERR : " + ex.getMessage(), ex);
        }
    }

    public static void saveByChineseUTF8(OutputStream out, Properties prop) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(out, "utf8"));
            List<String> keys = new ArrayList<String>();
            for (Object k : prop.keySet()) {
                keys.add((String) k);
            }
            Collections.sort(keys);
            for (String k : keys) {
                writer.write(k + "=" + prop.getProperty(k));
            }
        } catch (Exception ex) {
            throw new RuntimeException("saveByChineseUTF8 ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }
    
    public static void saveByChineseUTF8(File file, Properties prop) {
        try {
            saveByChineseUTF8(new FileOutputStream(file), prop);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("saveByChineseUTF8 ERR : " + ex.getMessage(), ex);
        }
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

    public static File getJarCurrentPath(Class<?> clz) {
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
            path = filepath.startsWith("/") ? filepath.substring(1) : filepath;
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
}
