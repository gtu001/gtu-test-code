package gtu.maven;

import gtu.file.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MvnToEclipseClassPathTool {

    public static void main(String[] args) throws Exception {
        new MvnToEclipseClassPathTool().execute();
        System.out.println("done...");
    }

    void execute_toProperties() {
        File m2Dir = new File(System.getProperty("user.home") + "/.m2/repository/");
        List<File> jarList = new ArrayList<File>();
        searchFileMatchs(m2Dir, ".+(?<!-javadoc|-sources)\\.jar", jarList);
        Collections.sort(jarList);
        String tmpPath = null;
        for (File f : jarList) {
            tmpPath = f.getAbsolutePath().replace('\\', '/');
            System.out.println(formatToCLassPath(tmpPath));
        }
    }

    List<File> jarList;

    void execute() throws Exception {
        File m2Dir = new File(System.getProperty("user.home") + "/.m2/repository/");
        jarList = new ArrayList<File>();
        searchFileMatchs(m2Dir, ".+(?<!-javadoc|-sources)\\.jar", jarList);
        MakeStuff.TO_ECLIPSE_CLASS_PATH.execute(this);//請修改此行
    }

    enum MakeStuff {
        TO_ECLIPSE_CLASS_PATH() {
            @Override
            void execute(MvnToEclipseClassPathTool this_) {
                String tmpPath = null;
                for (File f : this_.jarList) {
                    tmpPath = f.getAbsolutePath().replace('\\', '/');
                    System.out.println(formatToCLassPath(tmpPath));
                }
            }
        },
        TO_PROPERTIES() {
            @Override
            void execute(MvnToEclipseClassPathTool this_) throws FileNotFoundException, IOException {
                String tmpPath = null;
                StringBuilder sb = new StringBuilder();
                for (File f : this_.jarList) {
                    tmpPath = f.getAbsolutePath().replace('\\', '/') + ",";
                    sb.append(tmpPath);
                }
                Properties jarProp = new Properties();
                jarProp.setProperty("jar", sb.toString());
                jarProp.store(new FileOutputStream(FileUtil.DESKTOP_PATH + "test.properties"), "jar comment!!");
            }
        };

        abstract void execute(MvnToEclipseClassPathTool this_) throws Exception;
    }

    void execute_toClassPath() {
        File m2Dir = new File(System.getProperty("user.home") + "/.m2/repository/");
        List<File> jarList = new ArrayList<File>();
        searchFileMatchs(m2Dir, ".+(?<!-javadoc|-sources)\\.jar", jarList);
        String tmpPath = null;
        for (File f : jarList) {
            tmpPath = f.getAbsolutePath().replace('\\', '/');
            System.out.println(formatToCLassPath(tmpPath));
        }
    }

    static String formatToCLassPath(String jarPath) {
        return String.format("<classpathentry kind=\"lib\" path=\"%s\"/>", jarPath);
    }

    static void searchFileMatchs(File file, String pattern, List<File> fileList) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                searchFileMatchs(f, pattern, fileList);
            }
        } else {
            if (file.getName().matches(pattern)) {
                fileList.add(file);
            }
        }
    }
}
