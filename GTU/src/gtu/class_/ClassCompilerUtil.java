package gtu.class_;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;
import gtu.log.finder.MainTest;
import gtu.runtime.ProcessWatcher;

public class ClassCompilerUtil {

    private static final ClassCompilerUtil _INST = new ClassCompilerUtil();

    public static ClassCompilerUtil getInstance() {
        return _INST;
    }

    public static void main(String[] args) throws Exception {
        ClassCompilerUtil t = new ClassCompilerUtil();
        t.testGenClass();
        // t.generateClasspathByMaven(new
        // File("D:/workstuff/workspace/gtu-test-code/GTU"), new
        // File("d:/test/classpath.txt"));
    }

    public void testGenClass() throws Exception {
        ClassCompilerUtil t = new ClassCompilerUtil();

        File classesDir = new File("D:/test");

        String classPath = "gtu.log.finder";
        String className = "MainTest_java_gogo123_25";

        String javaContent = t._loadJavaTxt(new File("D:/test/gtu/log/finder/MainTest_java_gogo123_25.java"));
        File javaHome = new File("C:\\Program Files\\Java\\jdk1.8.0_73");

        if (!new File("d:/test/classpath.txt").exists()) {
            t.generateClasspathByMaven(new File("D:/workstuff/workspace/gtu-test-code/GTU"), new File("d:/test/classpath.txt"));
        }
        String mvnClassPathStr = FileUtil.loadFromFile(new File("d:/test/classpath.txt"), "utf8");

        File antFile = buildClasspathJar(new File("D:/apps/apache-ant-1.8.2"), mvnClassPathStr);
        System.out.println("AntFile = " + antFile);

        File clzFile = t.createDynamicClass(javaContent, classPath, className, mvnClassPathStr, classesDir, javaHome);

        System.out.println("----------------------------------------------");

        URLClassLoader loader = new URLClassLoader(new URL[] { classesDir.toURL() }, Thread.currentThread().getContextClassLoader());

        Class<?> clz = loader.loadClass(classPath + "." + className);
        Constructor<?> c = clz.getDeclaredConstructor(new Class[0]);
        c.setAccessible(true);
        Object newObject = c.newInstance();

        System.out.println("產生實體成功 : " + newObject);
    }

    /**
     * 由於classpath可能有路徑空白問題, 需要重新fix過
     */
    private String _getFixClasspath(String classpathStr) {
        String[] clsPath = classpathStr.split(";", -1);
        StringBuilder sb = new StringBuilder();
        for (int ii = 0; ii < clsPath.length; ii++) {
            String s = clsPath[ii];
            if (ii == clsPath.length - 1) {
                sb.append(String.format("\"%s\"", s));
            } else {
                sb.append(String.format("\"%s\";", s));
            }
        }
        return sb.toString();
    }

    /**
     * 取得Java檔
     * 
     * @param packagePath
     *            : Ex : com.example.gtu
     * @param className
     *            : Ex : Test001
     */
    public File getJavaFile(String packagePath, String className, File targetDir) {
        packagePath = packagePath.replace('.', '/');
        File lastDir = new File(targetDir + "/" + packagePath + "/");
        if (!lastDir.exists()) {
            lastDir.mkdirs();
        }
        File classFile = new File(targetDir + "/" + packagePath + "/", className + ".java");
        return classFile;
    }

    /**
     * 取得class檔
     * 
     * @param packagePath
     *            : Ex : com.example.gtu
     * @param className
     *            : Ex : Test001
     */
    public File getClassFile(String packagePath, String className, File targetDir) {
        packagePath = packagePath.replace('.', '/');
        File lastDir = new File(targetDir + "/" + packagePath + "/");
        if (!lastDir.exists()) {
            lastDir.mkdirs();
        }
        File classFile = new File(targetDir + "/" + packagePath + "/", className + ".class");
        return classFile;
    }

    /**
     * 產生class
     */
    public File createDynamicClass(String javaContent, String packagePath, String className, String classpathStr, File targetDir, File javaHome) {
        try {
            packagePath = packagePath.replace('.', '/');
            File lastDir = new File(targetDir + "/" + packagePath + "/");
            if (!lastDir.exists()) {
                lastDir.mkdirs();
            }
            File javaFile = new File(targetDir + "/" + packagePath + "/", className + ".java");

            javaContent = javaContent.replaceAll(Pattern.quote("${class_name}"), className);
            _saveToJava(javaFile, javaContent);

            System.out.println(javaContent);

            StringBuilder cp = new StringBuilder();
            if (StringUtils.isNotBlank(classpathStr)) {
                System.out.println("使用自訂classpath");
                cp.append(classpathStr);
            } else {
                cp.append(getClasspath());
            }

            File tmpClassPathFile = File.createTempFile("TmpClasspath_", ".txt");
            FileUtil.saveToFile(tmpClassPathFile, cp.toString(), "utf8");

            String verbose = ""; // "-verbose";
            String command = "\"" + javaHome + "/bin/javac\" ";

            String argument = " -sourcepath \"" + //
                    targetDir + "\" -cp @" + tmpClassPathFile + " " + verbose + " -encoding UTF-8 -d \"" + targetDir + "\"  \"" + javaFile.toString() + "\"";

            System.out.println(command + argument);

            Process process = Runtime.getRuntime().exec(command + argument);

            ProcessWatcher watcher = ProcessWatcher.newInstance(process);
            watcher.getStream();
            String errMsg = watcher.getErrorStreamToString();
            if (StringUtils.isNotBlank(errMsg)) {
                throw new RuntimeException("create class : " + errMsg);
            }

            File clzFile = new File(targetDir + "/" + packagePath, className + ".class");
            if (!clzFile.exists()) {
                throw new FileNotFoundException(clzFile.toString());
            }

            System.out.println("產生class : " + clzFile);
            return clzFile;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private File buildClasspathJar(File antHome, String classpathStr) throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("<project name=\"example\" default=\"main\">\n");

        List<String> csList = new ArrayList<String>();
        String[] cs = classpathStr.split(";", -1);
        for (int ii = 0; ii < cs.length; ii++) {
            String c = cs[ii];
            sb.append("<property name=\"A" + ii + "\" value=\"" + c + "\" />\n");
            csList.add("${A" + ii + "}");
        }

        sb.append("<target name=\"main\">\n");
        sb.append("<jar destfile=\"pathing.jar\">\n");
        sb.append("<manifest>\n");
        sb.append("<attribute name=\"Class-Path\" value=\"" + StringUtils.join(csList.iterator(), ' ') + "\"/>\n");
        sb.append("</manifest>\n");
        sb.append("</jar>\n");
        sb.append("</target>\n");
        sb.append("</project>\n");

        File f = new File("d:/test/antClasspath.xml");
        FileUtil.saveToFile(f, sb.toString(), "utf8");
        Runtime.getRuntime().exec(String.format("\"" + antHome + "/bin/ant.bat\" -f \"%s\"", f));
        return f;
    }

    /**
     * 取得classes目錄
     */
    public static File getClassesDir(Class<?> clz) {
        final File f = new File(MainTest.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return f;
    }

    /**
     * 取得classpath
     */
    public static String[] getClasspathArray() {
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);
        return classpathEntries;
    }

    /**
     * 取得classpath
     */
    public static String getClasspath() {
        return System.getProperty("java.class.path");
    }

    /**
     * 取得maven專案所使用的depedency jar
     */
    public static void generateClasspathByMaven(File mavenDir, File targetClassPathFile) {
        try {
            File parentFile = targetClassPathFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            String command = "cmd /c start cmd.exe /C \""// "/C產生後關閉 /K產生後開啟
                    + "cd " + mavenDir + " && "//
                    + "mvn dependency:build-classpath -DincludeScope=runtime "//
                    + "-Dmdep.outputFile=" + targetClassPathFile + "\"";//
            Process child = Runtime.getRuntime().exec(command);
            ProcessWatcher watcher = ProcessWatcher.newInstance(child);
            watcher.getStream();
            String errMsg = watcher.getErrorStreamToString();
            if (StringUtils.isNotBlank(errMsg)) {
                throw new Exception("classpath取得錯誤 : " + errMsg);
            }
            if (!targetClassPathFile.exists() || !targetClassPathFile.isFile()) {
                throw new Exception("未產生成功!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void _saveToJava(File tmpFile, String javaText) {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile), "UTF-8"));
            reader = new BufferedReader(new StringReader(javaText));
            for (String line = null; (line = reader.readLine()) != null;) {
                writer.write(line);
                writer.newLine();
            }
            reader.close();
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String _loadJavaTxt(File javaFile) {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(javaFile), "utf8"));
            StringBuilder sb = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}