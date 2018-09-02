package gtu.velocity.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import gtu.file.FileUtil;

public class BuilderBeanCodeGenerate {

    private List<String> fields;
    private String className;
    private String destinationPath;
    private String packagePath;
    private String template = "E:\\workstuff\\workspace\\gtu-test-code\\GTU\\src\\gtu\\velocity\\builder\\BuilderBeanVelocityTemplate.java";

    public static void main(String[] args) {
        BuilderBeanCodeGenerate builder = new BuilderBeanCodeGenerate();
        builder.className = "TestBean";

        List<String> fields = new ArrayList<String>();
        fields.add("id");
        fields.add("name");
        fields.add("password");
        fields.add("phone");

        builder.fields = fields;
        builder.packagePath = "gtu.velocity.builder";
//        builder.destinationPath = (BuilderBeanCodeGenerate.class.getResource("").getPath()).substring(1).replaceAll(
//                "bin", "src");
        builder.destinationPath = FileUtil.DESKTOP_PATH;

        builder.generateBean();
        System.out.println("ok!...");
    }

    /**
     * 產生Builder Pattern的JavaBean
     */
    public void generateBean() {
        try {
            // initial Velocity Properties
            Properties props = new Properties();
            props.setProperty("resource.loader", "string");
            props.setProperty("string.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            props.setProperty("string.resource.loader.repository.class",
                    "org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl");

            // initial Velocity
            Velocity.init(props);

            // registry template name & body
            StringResourceRepository vsRepository = StringResourceLoader.getRepository();

            // new velocityContext & put properties
            VelocityContext context = new VelocityContext();
            context.put("class_name", this.className);
            context.put("fields", this.fields);
            context.put("fields_map", this.upperFristCharToMap(this.fields));
            context.put("package_path", this.checkPackagePath(this.packagePath, this.destinationPath));

            String path = template;
            System.out.println("範本：" + path);
            
            byte[] bytes = BuilderBeanCodeGenerate.loadFromFile(path);
            if (bytes == null) {
                throw new Exception("file not found!");
            }
            String templateName = className;
            String templateBody = new String(bytes, "UTF-8");
            vsRepository.putStringResource(templateName, templateBody);

            // get velocity template
            Template template = Velocity.getTemplate(templateName, "UTF-8");

            // merge velocity Context & template
            StringWriter sw = new StringWriter();
            template.merge(context, sw);

            // set the merged body
            // System.out.println(sw.getBuffer().toString());
            String destPath = checkPathSeparator(this.destinationPath) + this.className + ".java";
            System.out.println("產生檔案於：" + destPath);
            BuilderBeanCodeGenerate.saveToFile(destPath, sw.getBuffer().toString().getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 確認package的路徑，若未設則用檔案輸出路徑來當作package路徑
     */
    private String checkPackagePath(String packagePath, String defaultPath) {
        StringBuffer rtn = new StringBuffer();
        if (StringUtils.isEmpty(packagePath)) {
            if (defaultPath.indexOf(":") != -1) {
                defaultPath = defaultPath.substring(2);
                char[] chars = defaultPath.toCharArray();
                for (int ii = 0; ii < chars.length; ii++) {
                    if (chars[ii] != File.separatorChar && chars[ii] != '/') {
                        chars[ii] = '.';
                    }
                    rtn.append(chars[ii]);
                }
                String rtnstr = rtn.toString();
                if (rtnstr.endsWith(".")) {
                    return rtnstr.substring(0, rtnstr.length() - 1);
                }
            }
        } else {
            return packagePath;
        }
        return "";
    }

    /**
     * 確認最後一個字元是否有加分隔符號
     */
    private String checkPathSeparator(String path) {
        char chk = path.charAt(path.length() - 1);
        if (chk != File.separatorChar && chk != '/') {
            path = path + File.separatorChar;
        }
        return path;
    }

    /**
     * 將list成員第一個字元大寫 ,原始狀態為key
     */
    private Map<String, String> upperFristCharToMap(List<String> list) {
        Iterator<String> it = list.iterator();
        Map<String, String> map = new HashMap<String, String>();
        while (it.hasNext()) {
            String key = it.next();
            String value = key.substring(0, 1).toUpperCase() + key.substring(1);
            map.put(key, value);
        }
        return map;
    }

    private static byte[] loadFromFile(String fileName) {
        byte[] ret = null;
        try {
            File file = new File(fileName);
            ret = new byte[(int) file.length()];
            FileInputStream in = new FileInputStream(file);
            in.read(ret);
            in.close();
        } catch (Throwable e) {
            ret = null;
        }
        return ret;
    }

    private static void saveToFile(String fileName, byte[] data) {
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(fileName);
            out.write(data);
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
