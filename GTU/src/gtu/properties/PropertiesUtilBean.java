package gtu.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesUtilBean {
    
    private Properties configProp;
    private File propFile;
    
    public PropertiesUtilBean (Class<?> clz) {
        try{
            configProp = new Properties();
            propFile = new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_config.properties");
            if(!propFile.exists()){
                propFile.createNewFile();
            }
            configProp = new Properties();
            configProp.load(new FileInputStream(propFile));
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
    public void store() {
        try{
            configProp.store(new FileOutputStream(propFile), "ClassConfig");
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public Properties getConfigProp() {
        return configProp;
    }

    public File getPropFile() {
        return propFile;
    }
}
