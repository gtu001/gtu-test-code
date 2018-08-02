package gtu.springdata;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import gtu.console.SystemInUtil;
import gtu.springdata.EntitySimpleCreater.___JavaPropertyConfig;
import gtu.string.StringUtilForDb;

public class EntitySimpleCreater_MAIN {

    public static void main(String[] args) throws IllegalAccessException, IOException {
        // FIXME ↓↓↓↓↓↓
        // -------------------------------------------------------------------------
        File srcDir = new File("D:/workstuff/workspace_taida/isa95-model/src/main/java");
        String packagePath = "com.delta.mes.model.isa95.operations.definition";
        String className = "";
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
            if (StringUtils.isBlank(className)) {
                className = line;
                continue;
            }
            String name = StringUtilForDb.dbFieldToJava(line);
            lstOrign.add(name);
        }
        reader.close();

        // -------------------------------------------------------------------------
        EntitySimpleCreater t = new EntitySimpleCreater(srcDir, packagePath);

        Map<String, Object> root = new HashMap<String, Object>();
        List<___JavaPropertyConfig> lst = new ArrayList<___JavaPropertyConfig>();

        for (int ii = 0; ii < lstOrign.size(); ii++) {
            String javaName = lstOrign.get(ii);
            String type = "String";
            if (ii == 0) {
//                type = "Long";
                type = "String";
            }
            lst.add(___JavaPropertyConfig.ofJavaName(javaName, type));
        }

        root.put("className", className);
        root.put("propertiesLst", lst);

        t.execute(root);

        System.out.println("done...");
    }
}
