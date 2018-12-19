package gtu.yaml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;

import org.yaml.snakeyaml.Yaml;

public class YamlUtil {

    public static Object loadFromFile(File file) {
        Yaml yaml = new Yaml();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return yaml.load(is);
        } catch (Exception ex) {
            throw new RuntimeException("loadFromFile ERR : " + ex.getMessage(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static Object loadInputStream(InputStream is) {
        Yaml yaml = new Yaml();
        try {
            return yaml.load(is);
        } catch (Exception ex) {
            throw new RuntimeException("loadFromFile ERR : " + ex.getMessage(), ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void saveToFile(File file, boolean append) {
        Yaml yaml = new Yaml();
        Writer writer = null;
        try {
            writer = new FileWriter(file, append);
            yaml.dump(yaml, writer);
        } catch (Exception ex) {
            throw new RuntimeException("loadFromFile ERR : " + ex.getMessage(), ex);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                } catch (Exception e) {
                }
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
