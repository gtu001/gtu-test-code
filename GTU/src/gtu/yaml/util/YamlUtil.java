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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.LineBreak;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class YamlUtil {

    public static void main(String[] args) {
        // File fromFile = new
        // File("D:/workstuff/gtu-test-code/GTU/src/gtu/_work/ui/RegexReplacer_NEW.yml");
        // Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
        // List<RegexReplacer_Config> lst =
        // YamlMapUtil.getInstance().loadFromFile(fromFile,
        // RegexReplacer_Config.class, classMap);
        // for (RegexReplacer_Config t : lst) {
        // String tmpToVal = getPlainString(t.getToVal());
        // System.out.println(tmpToVal);
        // t.setToVal(tmpToVal);
        // System.out.println("=======================================");
        // }
        // File destFile = new File(FileUtil.DESKTOP_DIR, "test111.yml");
        // System.out.println(destFile);
        // YamlMapUtil.getInstance().saveToFile(destFile, lst, false);
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
     * 前不可以有tab 後不可以有tab和space
     * 
     * @param orignStr
     * @return
     */
    public static String getPlainString(String orignStr) {
        List<String> sbLst = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            Matcher mth = null;
            Pattern ptn = Pattern.compile("^[\\t\\s]+");
            String tmpSpace = null;
            reader = new BufferedReader(new StringReader(orignStr));
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.defaultString(line).replaceAll("[\\s\\t]+$", "");

                mth = ptn.matcher(line);
                StringBuffer sb2 = new StringBuffer();
                while (mth.find()) {
                    tmpSpace = mth.group();
                    tmpSpace = StringUtils.defaultString(tmpSpace).replaceAll("\t", "    ");
                    mth.appendReplacement(sb2, tmpSpace);
                }
                mth.appendTail(sb2);

                sbLst.add(sb2.toString());
            }
            return StringUtils.join(sbLst, "\n");
        } catch (Exception ex) {
            throw new RuntimeException("getPlainString ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

//    /**
//     * 判斷是否換行字尾結束
//     * 
//     * @param strData
//     * @return
//     */
//    public static boolean isChangeLineEnd(String strData) {
//        strData = StringUtils.defaultString(strData);
//        char[] arry = strData.toCharArray();
//        for (int ii = arry.length - 1; ii > 0; ii++) {
//            if (arry[ii] == ' ' || arry[ii] == '\t') {
//                continue;
//            } else if (arry[ii] == '\n') {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
}
