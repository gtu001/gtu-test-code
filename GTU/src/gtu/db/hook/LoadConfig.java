package gtu.db.hook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Troy 2009/02/02
 * 
 */
public class LoadConfig {

    public static void main(String[] args) {
        // System.out.println(getFileLoad("c:\\UecDB.cfg", false));
        Properties xx = getFileLoad("c:\\UecDB.cfg", false);
        // System.out.println(xx);
        // System.out.println(getBundleLoad("gtu.db.db", false));
        // System.out.println(System.getProperties());
    }

    public static Properties getFileLoad(String fileName, boolean setProToSys) {
        Properties prop = new Properties();
        synchronized (System.getProperties()) {
            try {
                prop.load(new FileInputStream(fileName));
                Enumeration<Object> settings = prop.keys();
                if (setProToSys)
                    while (settings.hasMoreElements()) {
                        String setting = (String) settings.nextElement();
                        System.getProperties().put(setting, prop.get(setting));
                    }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

    public static Properties getBundleLoad(String classPath, boolean setProToSys) {
        Properties prop = new Properties();
        synchronized (System.getProperties()) {
            ResourceBundle resources = ResourceBundle.getBundle(classPath);
            try {
                Enumeration<String> ee = resources.getKeys();
                while (ee.hasMoreElements()) {
                    String key = ee.nextElement().toString();
                    String val = resources.getString(key);
                    if (setProToSys)
                        System.getProperties().put(key, val);
                    prop.setProperty(key, val);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

    /**
     * 讀bundle檔成HashMap
     */
    public static HashMap<String, String> getFileToMap(String fileName) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            FileReader FileStream = new FileReader(fileName);
            BufferedReader BufferedStream = new BufferedReader(FileStream);
            String data;
            do {
                data = BufferedStream.readLine();
                if (data == null)
                    break;
                else {
                    String xx[] = data.split("=");
                    map.put(xx[0], xx[1]);
                }
            } while (true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }
}