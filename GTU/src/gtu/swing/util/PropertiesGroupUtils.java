package gtu.swing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class PropertiesGroupUtils {
    
    private static final Pattern PROP_KEY_PATTERN = Pattern.compile("(\\w+)\\_\\d+");
    private static final int MAX_PROP_COUNT = 100;
    
    Properties configProp = new Properties();
    File configFile;
    int currentIndex = 0;
    
    public static void main(String[] args){
        File file = new File("C:/workspace/gtu-test-code/GTU/src/gtu/swing/util/PropertiesGroupTest.properties");
        PropertiesGroupUtils test = new PropertiesGroupUtils(file);
        System.out.println(test.loadConfig());
        test.next();
        System.out.println(test.loadConfig());
        
        Map<String,String> map = new HashMap<String,String>();
        map.put("user", "AAA");
        map.put("pwd", "DDD");
        map.put("OK", "DDD");
        
        test.saveConfig(map);
    }

    public PropertiesGroupUtils(File configFile) {
        this.configFile = configFile;
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            configProp.load(new FileInputStream(configFile));
            System.out.println("paramConfig : " + configProp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setParametersToTable(int index, Map<String, String> param) {
        for (String column : param.keySet()) {
            String columnKey = column + "_" + index;
            String value = param.get(column);
            configProp.put(columnKey, value);
        }
    }

    private Map<String, String> getParameters(int index) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enumeration enu = configProp.keys(); enu.hasMoreElements();) {
            String column = (String) enu.nextElement();
            String value = configProp.getProperty(column);
            Matcher mth = PROP_KEY_PATTERN.matcher(column);
            if (mth.find()) {
                String realColum = mth.group(1);
                map.put(realColum, value);
            }
        }
        return map;
    }

    private Map<String, String> getParameters(int index, Set<String> columnNames) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String column : columnNames) {
            String columnKey = column + "_" + index;
            if (!configProp.containsKey(columnKey)) {
                return new LinkedHashMap<String, String>();
            }
            map.put(column, configProp.getProperty(columnKey));
        }
        return map;
    }

    private Set<String> loadParametersColumnNames() {
        Set<String> columnNames = new LinkedHashSet<String>();
        for(Enumeration enu = configProp.keys();enu.hasMoreElements();){
            String key = (String)enu.nextElement();
            Matcher mth = PROP_KEY_PATTERN.matcher(key);
            if(mth.find()){
                String column = mth.group(1);
                columnNames.add(column);
            }
        }
        return columnNames;
    }

    private boolean isSameMap(Map<String, String> main, Map<String, String> map2) {
        for (String key : main.keySet()) {
            String val = main.get(key);
            String val2 = map2.get(key);
            if (!StringUtils.equals(val, val2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 回傳-1無須儲存, 傳回int則為應該要存的index
     */
    private Integer findParameterConfigIndex(Map<String, String> currentConfig) {
        if (currentConfig.isEmpty()) {
            return -1;// 無值無須儲存
        }
        Set<String> columnNames = loadParametersColumnNames();
        for (int ii = 0; ii < MAX_PROP_COUNT; ii++) {
            Map<String, String> param = getParameters(ii, columnNames);
            if (param.isEmpty()) {
                return ii;// 設定為空需要儲存
            }
            if (isSameMap(currentConfig, param)) {
                return ii;// 如果找到一樣的救回傳true
            } else {
                continue;// 不相同就比較下一筆
            }
        }
        throw new RuntimeException("超過範圍!");
    }

    private void savePropFile() {
        try {
            configProp.store(new FileOutputStream(configFile), DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd HH:mm:ss"));
        } catch (Exception e) {
            throw new RuntimeException("存parameters設定黨失敗", e);
        }
    }
    
    private void validateColumnNameSame(Map<String, String> currentConfig){
        List<String> currentColumnArry = new ArrayList<String>(loadParametersColumnNames());
        if(currentColumnArry.isEmpty()){
            return;//若為空避掉驗證
        }
        List<String> newColumnArry = new ArrayList<String>(currentConfig.keySet());
        Collections.sort(currentColumnArry);
        Collections.sort(newColumnArry);
        if(currentColumnArry.size() != newColumnArry.size() || !currentColumnArry.equals(newColumnArry)){
            throw new RuntimeException("參數不同 \n 目前 : " + currentColumnArry + "\n新參數 : " + newColumnArry);
        }
    }

    /**
     * 儲存設定
     */
    public void saveConfig(Map<String, String> currentConfig) {
        //比對新舊是否相同
        validateColumnNameSame(currentConfig);
        
        // 判斷要儲存的index
        int saveIndex = findParameterConfigIndex(currentConfig);
        System.out.println("找到的index : " + saveIndex);
        if (saveIndex == -1) {
            saveIndex = 0;
        }
        System.out.println("儲存的index : " + saveIndex);

        // 設定到prop
        setParametersToTable(saveIndex, currentConfig);

        // 儲存設定黨
        savePropFile();
    }

    /**
     * 讀取設定黨參數
     * @return 
     */
    public Map<String, String> loadConfig() {
        // 讀欄位
        Set<String> columnNames = loadParametersColumnNames();

        // 讀參數
        Map<String, String> param = null;
        if (columnNames.isEmpty()) {
            param = getParameters(currentIndex);
        } else {
            param = getParameters(currentIndex, columnNames);
        }
        System.out.println("loadConfig currentIndex : " + currentIndex);
        return param;
    }

    private int getLastNonUseIndex() {
        Set<String> columnNames = loadParametersColumnNames();
        for (int ii = 0; ii < MAX_PROP_COUNT; ii++) {
            Map<String, String> param = getParameters(ii, columnNames);
            if (param.isEmpty()) {
                return ii;
            }
        }
        return -1;
    }

    /**
     * 設定下乙組
     */
    public void next() {
        currentIndex++;
        if (currentIndex >= getLastNonUseIndex()) {
            currentIndex = 0;
        }
    }
}