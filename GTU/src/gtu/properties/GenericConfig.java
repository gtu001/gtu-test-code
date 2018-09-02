package gtu.properties;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GenericConfig {
    
    private static final Properties configProp;
    
    public static Logger getLogger(Class<?> clz) {
        PropertyConfigurator.configure("settings/log4j.properties");
        return Logger.getLogger(clz);
    }
    
    static {
        try{
            Properties prop = new Properties();
            prop.load(new FileInputStream("config/config.properties"));
            configProp = prop;
        }catch(Exception ex){
            throw new RuntimeException("", ex);
        }
    }
    
    public static final String DB_URL_FET = configProp.getProperty("DB_URL_FET");
    public static final String DB_USERID_FET = configProp.getProperty("DB_USERID_FET");
    public static final String DB_PASSWORD_FET = configProp.getProperty("DB_PASSWORD_FET");
    
    public static final String DB_URL_SBCC = configProp.getProperty("DB_URL_SBCC");
    public static final String DB_USERID_SBCC = configProp.getProperty("DB_USERID_SBCC");
    public static final String DB_PASSWORD_SBCC = configProp.getProperty("DB_PASSWORD_SBCC");
    
    public static final String OUTPUT_DIR = configProp.getProperty("OUTPUT_DIR");
    
    public static final String EMAIL_HOSTNAME = configProp.getProperty("EMAIL_HOSTNAME");
    public static final String EMAIL_FROM = configProp.getProperty("EMAIL_FROM");
    public static final String EMAIL_TO_LIST = configProp.getProperty("EMAIL_TO_LIST");
    
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
        List<String> configKey = new ArrayList<String>();
        for(Field f : GenericConfig.class.getDeclaredFields()){
            if(StringUtils.isAllUpperCase(f.getName().replaceAll("_", ""))){
                configKey.add(f.getName());
                String val = (String) f.get(GenericConfig.class);
                if(StringUtils.isBlank(val)){
                    System.err.println("prop : " + f.getName() + ":未設定!");
                }
            }
        }
        for(Object k : configProp.keySet()){
            String key = (String)k;
            if(!configKey.contains(key)){
                System.err.println("java : " + key + ":未設定!");
            }
        }
        System.out.println("done...");
    }
}
