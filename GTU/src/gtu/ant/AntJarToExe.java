package gtu.ant;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import gtu.file.FileUtil;
import gtu.freemarker.FreeMarkerSimpleUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;

/**
 * 將jar黨打包成exe黨
 */
public class AntJarToExe extends Task {

    private AntConfigHelper config;

    private Set<Icon> icons = new LinkedHashSet<Icon>();
    private Set<Singleton> singletons = new LinkedHashSet<Singleton>();
    private String jarPath;
    private String exePath;
    private String launch4jExe;

    private static boolean isWindows = false;
    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    public static void main(String[] args) {
        Map<String, Object> root = new HashMap<String, Object>();
        // root.put("jarPath",
        // "C:\\Users\\gtu00\\OneDrive\\Desktop/EnglishSearchUI.jar");
        // root.put("exePath",
        // "C:\\Users\\gtu00\\OneDrive\\Desktop/EnglishSearchUI.exe");
        root.put("jarPath", "/home/gtu001/桌面/BrowserHistoryHandlerUI.jar");
        root.put("exePath", "/home/gtu001/桌面/BrowserHistoryHandlerUI.exe");
        // root.put("iconPath",
        // "I:\\workstuff\\workspace\\gtu-test-code\\GTU\\src/resource/images/ico/janna.ico");
        root.put("iconPath", "/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/src/resource/images/ico/tk_aiengine.ico");
        root.put("singletonName", "BrowserHistoryHandlerUI");
        URL url = AntJarToExe.class.getResource("AntJarToExe_Launch4j_config.xml");
        System.out.println("url 0-00 " + url);
        try {
            File tempFile = File.createTempFile("antJarToExe_", ".xml");
            FreeMarkerSimpleUtil.replace(tempFile, url, root);
            System.out.println("DEST - " + tempFile);
            File tempFile2 = new File(FileUtil.DESKTOP_PATH, tempFile.getName());
            FileUtil.copyFile(tempFile, tempFile2);
            // Runtime.getRuntime().exec(String.format("cmd /c call \"%s\"
            // \"%s\"", "C:\\Program Files (x86)\\Launch4j\\launch4j.exe",
            // tempFile2));

            Process process = RuntimeBatPromptModeUtil.newInstance().command(String.format("java -jar \"%s\" \"%s\"", "/media/gtu001/OLD_D/apps/launch4j/launch4j.jar", tempFile2)).apply("UTF8");
            ProcessWatcher p = ProcessWatcher.newInstance(process);
            p.getStream();
            System.out.println(p.getErrorStreamToString());
            System.out.println(p.getInputStreamToString());

            System.out.println("done...");
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    @Override
    public void execute() throws BuildException {
        try {
            config = AntConfigHelper.of(this.getProject());

            String iconPath = "";
            if (!icons.isEmpty()) {
                iconPath = config.getParseAfterValue(icons.iterator().next().text);
            }

            String singletonName = "";
            if (!singletons.isEmpty()) {
                singletonName = config.getParseAfterValue(singletons.iterator().next().text);
            }

            this.log("launch4jExe - " + launch4jExe);
            this.log("jarPath - " + jarPath);
            this.log("exePath - " + exePath);
            this.log("iconPath - " + iconPath);
            this.log("singletonName - " + singletonName);
            File launch4jExeFile = new File(launch4jExe);
            this.log("launch4jExe exists - " + launch4jExeFile.exists());
            File jarFile = new File(jarPath);
            this.log("jarFile exists - " + jarFile.exists());
            File iconFile = new File(iconPath);
            this.log("iconFile exists - " + iconFile.exists());
            File exeFile = new File(exePath);
            this.log("exePath dir exists - " + exeFile.getParentFile().exists());

            Map<String, Object> root = new HashMap<String, Object>();
            root.put("jarPath", jarPath);
            root.put("exePath", exePath);
            root.put("iconPath", iconPath);
            root.put("singletonName", singletonName);

            URL url = this.getClass().getResource("AntJarToExe_Launch4j_config.xml");
            File tempFile = File.createTempFile("antJarToExe_", ".xml");
            FreeMarkerSimpleUtil.replace(tempFile, url, root);
            this.log("create config file - " + tempFile);

            Process process = null;
            if (isWindows) {
                process = Runtime.getRuntime().exec(String.format("cmd /c call \"%s\" \"%s\"", launch4jExeFile, tempFile));
            } else {
                process = RuntimeBatPromptModeUtil.newInstance().command(String.format("java -jar \"%s\" \"%s\"", launch4jExeFile, tempFile)).apply();
            }

            ProcessWatcher p = ProcessWatcher.newInstance(process);
            p.getStream();
            this.log(p.getErrorStreamToString());
            this.log(p.getInputStreamToString());

            this.log("make exe done...");
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public void setExePath(String exePath) {
        this.exePath = exePath;
    }

    public void setLaunch4jExe(String launch4jExe) {
        this.launch4jExe = launch4jExe;
    }

    public Icon createIcon() {
        Icon pkg = new Icon();
        icons.add(pkg);
        return pkg;
    }

    public Singleton createSingleton() {
        Singleton sch = new Singleton();
        singletons.add(sch);
        return sch;
    }

    public class Singleton {
        private String text;
        private String enable;

        public void addText(String text) {
            this.text = text;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }
    }

    public class Icon {
        private String text;
        private String enable;

        public void addText(String text) {
            this.text = text;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }
    }
}
