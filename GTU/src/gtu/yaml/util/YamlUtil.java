package gtu.yaml.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import gtu.file.FileUtil;
import gtu.html.simple.HtmlInputSimpleCreater.HtmlInputSimpleCreater_HtmlType;
import gtu.html.simple.HtmlInputSimpleCreater.HtmlInputSimpleCreater_HtmlTypeHandler;

public class YamlUtil {

    public static void main(String[] args) {
        File fromFile = new File("/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/src/gtu/html/simple/HtmlInputSimpleCreater_Thymeleaf.yaml");
        Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
        classMap.put("tagLst", HtmlInputSimpleCreater_HtmlType.class);
        HtmlInputSimpleCreater_HtmlTypeHandler self = YamlMapUtil.getInstance().loadFromFile(fromFile, HtmlInputSimpleCreater_HtmlTypeHandler.class, classMap);
        for (HtmlInputSimpleCreater_HtmlType t : self.getTagLst()) {
            System.out.println(t.getTagId());
            System.out.println(t.getTemplate());
            System.out.println("=======================================");
        }
        YamlMapUtil.getInstance().saveToFile(new File(FileUtil.DESKTOP_DIR, "test_yaml.yml"), self, false);
        System.out.println("done...");
    }

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
            options.setDefaultScalarStyle(ScalarStyle.PLAIN);// LITERAL
            // options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);//json
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);// BLOCK
            options.setLineBreak(LineBreak.UNIX);
            options.setAllowUnicode(true);
            options.setPrettyFlow(true);
            options.setWidth(-1);
            options.setIndent(2);
            options.setExplicitStart(true);
            options.setExplicitEnd(true);
            options.setSplitLines(false);
            // options.setCanonical(true);
            // options.setVersion(Version.V1_1);
        }
        // Yaml yaml = new Yaml(constructor, representer, options);
        Yaml yaml = new Yaml(options);
        Writer writer = null;
        try {
            // writer = new FileWriter(file, append);
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

    /**
     * 字串要把結尾空白trim掉不然格式會跑掉
     * 
     * @param orignStr
     * @return
     */
    public static String getPlainString(String orignStr) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(orignStr));
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.defaultString(line).replaceAll("[\\s\\t]+$", "");
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("getPlainString ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
}
