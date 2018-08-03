package gtu.springdata;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;
import gtu.file.FileUtil;

public class SimpleCrudRepositoryGenerater {

    static final String REPOSITORY_STR;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("                                                                       \n");
        sb.append("package {0};                                                           \n");
        sb.append("                                                                       \n");
        sb.append("import org.springframework.context.annotation.Profile;                 \n");
        sb.append("import org.springframework.data.repository.CrudRepository;             \n");
        sb.append("                                                                       \n");
        sb.append("import {1};                                                            \n");
        sb.append("                                                                       \n");
        sb.append("@Profile('{' \"spring-data\", \"servers\" })                                 \n");
        sb.append("public interface {2} extends CrudRepository<{3}, String> '{'         \n"); // Long TODO 
        sb.append("                                                                       \n");
        sb.append("}                                                                      \n");
        sb.append("                                                                       \n");
        REPOSITORY_STR = sb.toString();
    }

    public static void main(String[] args) throws IllegalAccessException, IOException {
        SimpleCrudRepositoryGenerater t = new SimpleCrudRepositoryGenerater();
        t.execute();
        System.out.println("done...");
    }

    public void execute() throws IOException {
        // FIXME ↓↓↓↓↓↓
        // -------------------------------------------------------------------------
        String entityPackage = "com.delta.mes.model.isa95.resources.equipment";
        String repositoryPackage = "com.delta.mes.model.isa95.new_dao";
        File srcFolder = new File("D:/workstuff/workspace_taida/Taida_Model/src/main/java/");
        // FIXME ↑↑↑↑↑↑
        // -------------------------------------------------------------------------

        String text = SystemInUtil.readContent();

        List<String> lstOrign = new ArrayList<String>();
        LineNumberReader reader = new LineNumberReader(new StringReader(text));
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trim(line);
            if (StringUtils.isBlank(line)) {
                continue;
            }
            lstOrign.add(line);
        }
        reader.close();

        // -------------------------------------------------------------------------
        // main process
        this.execute(lstOrign, entityPackage, repositoryPackage, srcFolder);
        // -------------------------------------------------------------------------
    }

    public void execute(List<String> entityLst, String entityPackage, String repositoryPackage, File srcFolder) {
        for (String entityName : entityLst) {
            String entityPackageName = entityPackage + "." + entityName;
            this.processFile(entityPackageName, repositoryPackage, srcFolder);
        }
    }

    private void processFile(String entityPackageName, String repositoryPackage, File srcFolder) {
        String entityName = entityPackageName.substring(entityPackageName.lastIndexOf(".") + 1);
        String repostoryName = entityName + "Repository";

        String result = MessageFormat.format(REPOSITORY_STR, //
                new Object[] { //
                        repositoryPackage, //
                        entityPackageName, //
                        repostoryName, //
                        entityName //
                });//

        File targetDir = new File(srcFolder, repositoryPackage.replaceAll("\\.", "/"));
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File javaFile = new File(targetDir, repostoryName + ".java");
        System.out.println("generate : " + javaFile);
        FileUtil.saveToFile(javaFile, result, "UTF8");
    }
}
