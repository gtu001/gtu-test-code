package gtu.yaml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

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

    public static void saveToFile(File file, Object dumpObject, boolean append) {
        Constructor constructor = new Constructor();
        Representer representer = new Representer();
        DumperOptions options = new DumperOptions();
        {
            // options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            // options.setDefaultScalarStyle(ScalarStyle.FOLDED);
            // options.setDefaultScalarStyle(ScalarStyle.PLAIN);
            options.setDefaultScalarStyle(ScalarStyle.LITERAL);
            // options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);//json
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setAllowUnicode(true);
            options.setPrettyFlow(true);
            options.setWidth(-1);
            options.setIndent(2);
            options.setExplicitStart(true);
            options.setExplicitEnd(true);
            options.setSplitLines(true);
            // options.setCanonical(true);
        }
//        Yaml yaml = new Yaml(constructor, representer, options);
        Yaml yaml = new Yaml(options);
        Writer writer = null;
        try {
//            writer = new FileWriter(file, append);
            writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            yaml.dump(dumpObject, writer);
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
