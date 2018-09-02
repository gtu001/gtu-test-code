package gtu.maven;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.TemplateException;
import gtu.freemarker.FreeMarkerSimpleUtil;

public class IisiWebProjectClassPathMaker {

    public static void main(String[] args) throws IOException, TemplateException {
        String workspace = "C:/Users/gtu001/Desktop/workspace_TOMCAT/";
        String projectName = "sris-rl";

        Map<String, Object> root = new HashMap<String, Object>();

        List<Project> projList = new ArrayList<Project>();
        File file = new File(workspace, projectName);
        for (File sub : file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return dir.isDirectory();
            }
        })) {
            File tmp1 = new File(sub, "/src/main/java");
            File tmp2 = new File(sub, "/src/main/resources");

            if (tmp1.exists()) {
                Project proj = new Project();
                System.out.println(sub.getName() + "_java" + " : " + tmp1);
                proj.setName(sub.getName() + "_java");
                proj.setPath(tmp1.getAbsolutePath().replace('\\', '/'));
                projList.add(proj);
            }
            if (tmp2.exists()) {
                Project proj = new Project();
                System.out.println(sub.getName() + "_resources" + " : " + tmp2);
                proj.setName(sub.getName() + "_resources");
                proj.setPath(tmp2.getAbsolutePath().replace('\\', '/'));
                projList.add(proj);
            }
        }

        root.put("project", projList);

        String projectText = FreeMarkerSimpleUtil.replace(DOT_PROJECT, root);
        String classpathText = FreeMarkerSimpleUtil.replace(DOT_CLASSPATH, root);

        System.err.println("## paste to .project");
        System.out.println(projectText);
        System.err.println("## paste to .classpath");
        System.out.println(classpathText);
        System.out.println("done...");
    }

    public static class Project {
        String name;
        String path;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    static String DOT_PROJECT;
    static String DOT_CLASSPATH;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(" <linkedResources>                                                       \n");
        sb.append(" <#list project as proj>                                                 \n");
        sb.append("         <link>                                                          \n");
        sb.append("                 <name>${proj.name}</name>                               \n");
        sb.append("                 <type>2</type>                                          \n");
        sb.append("                 <location>${proj.path}</location>                       \n");
        sb.append("         </link>                                                         \n");
        sb.append(" </#list>                                                                \n");
        sb.append(" </linkedResources>                                                      \n");
        DOT_PROJECT = sb.toString();

        sb = new StringBuilder();
        sb.append(" <#list project as proj>                                        ");
        sb.append("        <classpathentry kind=\"src\" path=\"${proj.name}\"/>   \n");
        sb.append(" </#list>                                                       ");
        DOT_CLASSPATH = sb.toString();
    }
}
