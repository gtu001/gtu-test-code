package gtu.properties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.log.JdkLoggerUtil;
import gtu.properties.PropertiesUtilBean.FileChangeHandler;

public class PropertiesUtilChinese {

    private static final Logger logger = JdkLoggerUtil.getLogger(PropertiesUtilChinese.class, true);

    private Properties configProp;
    private File propFile;
    private FileChangeHandler fileChangeHandler;
    private String encode;

    public void init(File customFile, String encode) {
        try {
            propFile = customFile;
            fileChangeHandler = new FileChangeHandler(propFile);
            this.encode = encode;
            logger.info("configFile : " + propFile);
            if (!propFile.exists()) {
                propFile.createNewFile();
            }
            configProp = loadFileByCoding(propFile, encode);
            logger.info("configFile size : " + configProp.size());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void reload() {
        init(propFile, encode);
    }

    public PropertiesUtilChinese(File customFile, String encode) {
        init(customFile, encode);
    }

    public PropertiesUtilChinese(Class<?> clz, String encode) {
        this(new File(PropertiesUtil.getJarCurrentPath(clz), clz.getSimpleName() + "_config.properties"), encode);
    }

    private Properties loadFileByCoding(File file, String encode) throws IOException {
        Properties prop = new Properties();
        List<String> list = FileUtils.readLines(file, encode);
        for (String key : list) {
            if (key.startsWith("#") || StringUtils.isBlank(key)) {
                continue;
            }
            String value = "";
            if (key.contains("=")) {
                int pos = key.indexOf("=");
                key = key.substring(0, pos);
                value = key.substring(pos + 1);
            }
            prop.setProperty(key, value);
        }
        return prop;
    }

    public void store() {
        BufferedWriter writer = null;
        try {
            String remark = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS").format(new Date());
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(propFile), this.encode));
            writer.write(remark);
            writer.newLine();
            for (Object k : configProp.keySet()) {
                String key = (String) k;
                String value = configProp.getProperty(key);
                String line = key;
                if (StringUtils.isNotBlank(value)) {
                    line = key + "=" + value;
                }
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            logger.info("store success!");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                writer.close();
                fileChangeHandler.resetLastModifiedUnderControl();
            } catch (Exception e) {
            }
        }
    }

    public boolean isFileChangeUncontrolled() {
        return fileChangeHandler.isChange();
    }

    public Properties getConfigProp() {
        return configProp;
    }

    public File getPropFile() {
        return propFile;
    }
}
