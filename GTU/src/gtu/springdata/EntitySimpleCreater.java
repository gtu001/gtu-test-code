package gtu.springdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.string.StringUtilForDb;

public class EntitySimpleCreater {

    static final String ENTITY_STR;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("package ${packagePath!'packagePath undefined'};                                               \n");
        sb.append("                                                           \n");

        // import here
        sb.append("import java.util.List;                                  \n");
        sb.append("import javax.persistence.CascadeType;                   \n");
        sb.append("import javax.persistence.Column;                        \n");
        sb.append("import javax.persistence.Entity;                        \n");
        sb.append("import javax.persistence.FetchType;                     \n");
        sb.append("import javax.persistence.GeneratedValue;                \n");
        sb.append("import javax.persistence.Id;                            \n");
        sb.append("import javax.persistence.JoinColumn;                    \n");
        sb.append("import javax.persistence.OneToMany;                     \n");
        sb.append("import javax.persistence.Table;                         \n");
        sb.append("import javax.persistence.Transient;                     \n");
        sb.append("import net.minidev.json.annotate.JsonIgnore;            \n");
        // import here

        sb.append("                                                           \n");
        sb.append("@Entity                                                    \n");
        sb.append("@Table(name = \"${tableName!'tableName undefined'}\")                                                    \n");
        sb.append("public class ${className!'className undefined'} {                                         \n");
        sb.append("    @Id                                                    \n");
        sb.append("    //@GeneratedValue                                        \n");
        sb.append(" <#list propertiesLst as var>                                                                    \n");
        sb.append("    @Column(name = \"${var.dbColumn!'dbColumn undefined'}\")                                                       \n");
        sb.append("    private ${var.type!'type undefined'} ${var.javaName!'javaName undefined'};                                                       \n");
        sb.append(" </#list>                                                                                      \n");
        sb.append(" <#list propertiesLst as var>                                                                    \n");
        sb.append("    public ${var.type!'type undefined'} ${var.getterMethod!'getterMethod undefined'}(){                                               \n");
        sb.append("        return this.${var.javaName!'javaName undefined'};                                                         \n");
        sb.append("    }                                                                                      \n");
        sb.append("    public void ${var.setterMethod!'setterMethod undefined'}(${var.type!'type undefined'} ${var.javaName!'javaName undefined'}){                         \n");
        sb.append("        this.${var.javaName!'javaName undefined'} = ${var.javaName!'javaName undefined'};                                             \n");
        sb.append("    }                                                                                      \n");
        sb.append(" </#list>                                                                                      \n");
        sb.append("}                                                          \n");
        sb.append("                                                           \n");
        ENTITY_STR = sb.toString();
    }

    public static void main(String[] args) throws IllegalAccessException, IOException {
        File srcDir = new File("C:/Users/wistronits/Desktop/新增資料夾");
        String packagePath = "com.gtu.test.entity";
        EntitySimpleCreater t = new EntitySimpleCreater(srcDir, packagePath);

        Map<String, Object> root = new HashMap<String, Object>();

        List<___JavaPropertyConfig> lst = new ArrayList<___JavaPropertyConfig>();
        lst.add(___JavaPropertyConfig.ofDBColumn("long test id", "long"));
        lst.add(___JavaPropertyConfig.ofJavaName("testId1", "String"));
        lst.add(___JavaPropertyConfig.ofJavaName("testInt", "int"));

        root.put("className", "GtuTestEntity");
        root.put("propertiesLst", lst);

        t.execute(root);

        System.out.println("done...");
    }

    public void execute(Map<String, Object> root) {
        this.validateParams(root);

        if (!this.targetGenerateDir.exists()) {
            this.targetGenerateDir.mkdirs();
        }

        String resultContent = FreeMarkerSimpleUtil.replace(ENTITY_STR, root);
        // System.out.println(resultContent);

        this.saveToJavaFile(root, resultContent);
    }

    private void saveToJavaFile(Map<String, Object> root, String resultContent) {
        String javaName = ((String) root.get("className")) + ".java";
        File javaFile = new File(targetGenerateDir, javaName);
        FileUtil.saveToFile(javaFile, resultContent, "UTF8");
        System.out.println("generate : " + javaFile);
    }

    private void validateParams(Map<String, Object> root) {
        if (!root.containsKey("packagePath") && StringUtils.isNotBlank(packagePath)) {
            root.put("packagePath", packagePath);
        }
        Validate.isTrue(root.containsKey("packagePath"), "packagePath未定義!");
        Validate.isTrue(root.containsKey("className"), "className未定義!");
        Validate.isTrue(root.containsKey("propertiesLst"), "propertiesLst未定義!");
        List<?> lst = (List) root.get("propertiesLst");
        if (!lst.isEmpty()) {
            Validate.isTrue(lst.get(0) instanceof ___JavaPropertyConfig, "propertiesLst類別錯誤 , 必須為 : " + ___JavaPropertyConfig.class.getName());
        }

        String className = (String) root.get("className");
        System.out.println("className - " + className);
        root.put("tableName", StringUtilForDb.javaToDbField(className));
        System.out.println(root);
    }

    public static class ___JavaPropertyConfig {
        private String dbColumn;
        private String type;
        private String javaName;
        private String getterMethod;
        private String setterMethod;

        public static ___JavaPropertyConfig ofJavaName(String javaName, String type) {
            ___JavaPropertyConfig t = new ___JavaPropertyConfig();
            __otherProcess(javaName, type, t);
            return t;
        }

        public static ___JavaPropertyConfig ofDBColumn(String dbColumn, String type) {
            ___JavaPropertyConfig t = new ___JavaPropertyConfig();
            __otherProcess(StringUtilForDb.dbFieldToJava(dbColumn), type, t);
            return t;
        }

        private static void __otherProcess(String javaName, String type, ___JavaPropertyConfig t) {
            t.type = type;
            t.javaName = javaName;
            t.dbColumn = StringUtilForDb.javaToDbField(javaName);
            String getterPrefix = "get";
            if ("boolean".equals(type) || "Boolean".equals(type)) {
                getterPrefix = "is";
            }
            t.getterMethod = getterPrefix + StringUtils.capitalize(javaName);
            t.setterMethod = "set" + StringUtils.capitalize(javaName);
        }

        public String getDbColumn() {
            return dbColumn;
        }

        public String getType() {
            return type;
        }

        public String getJavaName() {
            return javaName;
        }

        public String getGetterMethod() {
            return getterMethod;
        }

        public String getSetterMethod() {
            return setterMethod;
        }
    }

    private File srcFolder;
    private String packagePath;
    private File targetGenerateDir;

    public EntitySimpleCreater(File srcFolder, String packagePath) {
        this.srcFolder = srcFolder;
        this.packagePath = packagePath;
        this.targetGenerateDir = new File(srcFolder, packagePath.replaceAll("\\.", "/"));
    }
}
