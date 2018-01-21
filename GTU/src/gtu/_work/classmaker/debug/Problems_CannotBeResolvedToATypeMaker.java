package gtu._work.classmaker.debug;

import gtu.collection.MapUtil;
import gtu.console.SystemInUtil;
import gtu.file.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Problems_CannotBeResolvedToATypeMaker {

    static class ProblemClass {
        private String cannotBeResolvedName;
        private String className;
        private String path;

        public ProblemClass(String cannotBeResolvedName, String className, String path) {
            super();
            this.cannotBeResolvedName = cannotBeResolvedName;
            this.className = className;
            this.path = path;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((cannotBeResolvedName == null) ? 0 : cannotBeResolvedName.hashCode());
            result = prime * result + ((className == null) ? 0 : className.hashCode());
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ProblemClass other = (ProblemClass) obj;
            if (cannotBeResolvedName == null) {
                if (other.cannotBeResolvedName != null)
                    return false;
            } else if (!cannotBeResolvedName.equals(other.cannotBeResolvedName))
                return false;
            if (className == null) {
                if (other.className != null)
                    return false;
            } else if (!className.equals(other.className))
                return false;
            if (path == null) {
                if (other.path != null)
                    return false;
            } else if (!path.equals(other.path))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "ProblemClass [cannotBeResolvedName=" + cannotBeResolvedName + "]";
        }
    }

    public static void main(String[] args) throws Exception {
        String message = SystemInUtil.readContent();
        Pattern pattern = Pattern.compile("^(\\w+)(\\scannot\\sbe\\sresolved\\sto\\sa\\stype\\s)(\\w+\\.java)(\\s*)([/|\\w|-]*)(\\s+)(.*)$");
        Matcher matcher = null;
        Set<ProblemClass> set = new HashSet<ProblemClass>();
        BufferedReader reader = new BufferedReader(new StringReader(message));
        for (String line = null; (line = reader.readLine()) != null;) {
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                String cannotBeResolvedName = matcher.group(1);
                String className = matcher.group(3);
                String path = matcher.group(5);
                set.add(new ProblemClass(cannotBeResolvedName, className, path));
            }
        }

        Map<Map<String, Object>, Collection<ProblemClass>> categoryMapByField = MapUtil.categoryMapByField(set, "className");

        File targetDir = new File("C:\\workspace_RS430\\rs430\\rl430");

        Set<String> notFoundImportClassSet = new HashSet<String>();

        for (Map<String, Object> key : categoryMapByField.keySet()) {
            System.out.println(key);
            String findJava = (String) key.get("className");
            List<File> searchResult = new ArrayList<File>();
            FileUtil.searchFileMatchs(targetDir, findJava, searchResult);
            if (searchResult.isEmpty()) {
                throw new RuntimeException("file not found!!");
            }

            BufferedReader rrr = new BufferedReader(new InputStreamReader(new FileInputStream(searchResult.get(0)), "UTF8"));
            for (String line = null; (line = rrr.readLine()) != null;) {
                for (ProblemClass p : categoryMapByField.get(key)) {
                    if (line.indexOf("import") != -1 && line.indexOf(p.cannotBeResolvedName) != -1) {
                        notFoundImportClassSet.add(line);
                    }
                }
                if (line.startsWith("public class") || line.startsWith("public interface")) { //表示讀完import的部分
                    break;
                }
            }
            rrr.close();
        }

        System.out.println("################");
        System.out.println("################");
        System.out.println("################");
        for (String notFoundClass : notFoundImportClassSet) {
            String classFullName = notFoundClass.replaceFirst("import", "").replaceAll(";", "").trim();
            String packageName = classFullName.substring(0, classFullName.lastIndexOf("."));
            String className = classFullName.substring(classFullName.lastIndexOf(".") + 1);
            System.out.println("package " + packageName + "; public class " + className + "{}");
        }
    }
}
