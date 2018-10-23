package gtu.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class PropertiesGroupUtils_ByKey {

    public static final String SAVE_KEYS = "saveKey";

    Properties configProp = new Properties();
    File configFile;
    int currentIndex = 0;
    private List<Map<String, String>> mapList;

    public static void main(String[] args) {
        File file = new File("C:/workspace/gtu-test-code/GTU/src/gtu/swing/util/PropertiesGroupTest.properties");
        PropertiesGroupUtils_ByKey test = new PropertiesGroupUtils_ByKey(file);
        System.out.println(test.loadConfig());
        test.next();
        System.out.println(test.loadConfig());

        Map<String, String> map = new HashMap<String, String>();
        map.put("user", "AAA");
        map.put("pwd", "DDD");
        map.put("OK", "DDD");

        test.saveConfig(map);
    }

    public PropertiesGroupUtils_ByKey(File configFile) {
        this.configFile = configFile;
        FileInputStream fis = null;
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            fis = new FileInputStream(configFile);
            configProp.load(fis);
            System.out.println("paramConfig : " + configProp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<Map<String, String>> _getPropListMap() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (configProp.containsKey(SAVE_KEYS)) {
            List<String> saveKeys = _getSaveKeys();
            for (String _key : saveKeys) {
                if (StringUtils.isBlank(_key)) {
                    continue;
                }
                Map<String, String> map = getConfig(_key);
                list.add(map);
            }
        }
        return list;
    }

    public Map<String, String> getConfig(String saveKey) {
        Map<String, String> map = new HashMap<String, String>();
        for (Enumeration enu = configProp.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            if (key.endsWith(saveKey)) {
                String orignKey = StringUtils.substring(key, 0, key.length() - ("_" + saveKey).length());
                map.put(orignKey, configProp.getProperty(key));
            }
        }
        map.put(SAVE_KEYS, saveKey);
        return map;
    }

    /**
     * 儲存設定
     */
    public void saveConfig(Map<String, String> currentConfig) {
        // 儲存此group設定
        _saveConfigGroup(currentConfig);
        // 存檔
        _savePropFile();
        // 刷新
        _refresh();
    }

    /**
     * 移除設定
     */
    public void removeConfig(String saveKey) {
        // 移除Group設定
        _removeConfigGroup(saveKey);
        // 存檔
        _savePropFile();
        // 刷新
        _refresh();
    }

    private void _removeConfigGroup(String saveKey) {
        List<String> saveKeys = _getSaveKeys();
        saveKeys.remove(saveKey);
        configProp.setProperty(SAVE_KEYS, StringUtils.join(saveKeys, ","));
        for (Enumeration enu = configProp.keys(); enu.hasMoreElements();) {
            String key = (String) enu.nextElement();
            if (key.endsWith(saveKey)) {
                configProp.remove(key);
            }
        }
    }

    private void _refresh() {
        List<Map<String, String>> mapList2 = _getPropListMap();
        mapList = mapList2;
        currentIndex = 0;
    }

    private void _savePropFile() {
        PropertiesUtil.storeProperties(configProp, configFile, DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss"));
    }

    private void _saveConfigGroup(Map<String, String> currentConfig) {
        if (!currentConfig.containsKey(SAVE_KEYS)) {
            throw new RuntimeException("必須設定 : " + SAVE_KEYS);
        }
        String newKeys = currentConfig.get(SAVE_KEYS);
        if (StringUtils.isBlank(newKeys)) {
            throw new RuntimeException("必須設定 : " + SAVE_KEYS);
        }
        if (newKeys.contains(",")) {
            throw new RuntimeException(SAVE_KEYS + " 不可含有 \",\"");
        }

        List<String> saveKeys = _getSaveKeys();
        if (!saveKeys.contains(newKeys)) {
            saveKeys.add(newKeys);
        }
        configProp.setProperty(SAVE_KEYS, StringUtils.join(saveKeys, ","));

        for (String key : currentConfig.keySet()) {
            if (key.equals(SAVE_KEYS)) {
                continue;
            }
            String Zkey = key + "_" + newKeys;
            String val = currentConfig.get(key);
            configProp.setProperty(Zkey, val);
        }
    }

    private List<String> _getSaveKeys() {
        if (!configProp.containsKey(SAVE_KEYS)) {
            configProp.setProperty(SAVE_KEYS, "");
        }
        String strs = configProp.getProperty(SAVE_KEYS);
        List<String> list = new ArrayList<String>();
        for (String k : StringUtils.split(strs, ",")) {
            if (StringUtils.isNotBlank(k)) {
                list.add(k);
            }
        }
        return list;
    }

    /**
     * 讀取設定黨參數
     */
    public Map<String, String> loadConfig() {
        if (mapList == null) {
            mapList = _getPropListMap();
            currentIndex = 0;
            if (mapList.isEmpty()) {
                return new HashMap<String, String>();
            } else {
                return mapList.get(currentIndex);
            }
        }
        if (currentIndex < 0 || currentIndex >= mapList.size()) {
            currentIndex = 0;
        }
        if (mapList.isEmpty()) {
            return new HashMap<String, String>();
        } else {
            return mapList.get(currentIndex);
        }
    }

    /**
     * 設定下乙組
     */
    public void next() {
        currentIndex++;
    }
}