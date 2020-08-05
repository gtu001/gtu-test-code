package gtu.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;
import gtu.runtime.RuntimeBatPromptModeUtil;

public class MavenDenpencyJarListLoader {

    private static boolean isWindows = false;
    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // File pomFile = new
        // File("‪/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/pom.xml");
        // File mavenDir = new
        // File("/media/gtu001/OLD_D/apps/apache-maven-3.3.9/bin/mvn"); //
        // System.getenv("M2_HOME")
        // //-----------------------------------------------------------------
        File pomFile = new File("D:/workstuff/gtu-test-code/GTU/pom.xml");
        File mavenDir = new File("D:/apps/apache-maven-3.3.9/bin/mvn"); //

        // File pomFile = new
        // File("/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/pom.xml");
        // File mavenDir = new
        // File("/media/gtu001/OLD_D/apps/apache-maven-3.3.9/bin/mvn"); //

        // [jarfinder] AntJarFinder - mavenExe :
        // /media/gtu001/OLD_D/apps/apache-maven-3.3.9/bin/mvn - true
        // [jarfinder] AntJarFinder - pomFile :
        // /media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/pom.xml -
        // true

        // System.getenv("M2_HOME")
        // -----------------------------------------------------------------
        List<String> jarLst = MavenDenpencyJarListLoader.newInstance()//
                .pomFile(pomFile)//
                .mavenExePath(mavenDir)//
                .build();//
        for (String jarPath : jarLst) {
            System.out.println("\t" + jarPath);
        }
        System.out.println("done...");
    }

    private File pomFile;
    private File mavenExePath;

    private MavenDenpencyJarListLoader() {
    }

    public static MavenDenpencyJarListLoader newInstance() {
        return new MavenDenpencyJarListLoader();
    }

    public MavenDenpencyJarListLoader pomFile(File pomFile) {
        this.pomFile = pomFile;
        return this;
    }

    public MavenDenpencyJarListLoader mavenExePath(File mavenExePath) {
        this.mavenExePath = mavenExePath;
        return this;
    }

    public List<String> build() {
        LineNumberReader reader = null;
        try {
            boolean buildSuccess = false;
            StringBuilder sb = new StringBuilder();

            File pomUsageFile = new File(pomFile.getParentFile(), "pom_usage.txt");
            if (!pomUsageFile.exists()) {
                if (mavenExePath == null) {
                    File tmpExeFile = new File(System.getenv("M2_HOME") + "/bin/mvn");
                    if (tmpExeFile.exists()) {
                        mavenExePath = tmpExeFile;
                    }
                }

                String rootPath = pomFile.getAbsolutePath().substring(0, pomFile.getAbsolutePath().indexOf(":") + 1);

                RuntimeBatPromptModeUtil execdo = RuntimeBatPromptModeUtil.newInstance();
                execdo.command(String.format("cd %s", FileUtil.replaceSpecialChar(pomFile.getParent())));
                if (isWindows) {
                    execdo.runInBatFile(false);
                    execdo.command(rootPath);
                } else {
                    execdo.runInBatFile(false);
                }
                execdo.command((isWindows ? "" : "sh ") + FileUtil.replaceSpecialChar(mavenExePath.getAbsolutePath()) + " dependency:build-classpath -DincludeScope=runtime");
                System.out.println("----------------------------------------");
                System.out.println(execdo.getCommand());
                System.out.println("----------------------------------------");

                Process exec = execdo.apply();
                ProcessWatcher _inst = ProcessWatcher.newInstance(exec);
                _inst.getStreamSync();

                reader = new LineNumberReader(new StringReader(_inst.getInputStreamToString()));
                for (String line = null; (line = reader.readLine()) != null;) {
                    sb.append(line + "\r\n");
                    if (StringUtils.trimToEmpty(line).startsWith("[INFO] BUILD SUCCESS")) {
                        buildSuccess = true;
                    }
                }

                if (!buildSuccess) {
                    System.err.println("input : " + _inst.getInputStreamToString());
                    System.err.println("error : " + _inst.getErrorStreamToString());
                    throw new Exception("非正常結束!, not build success");
                }

                FileUtil.saveToFile(pomUsageFile, sb.toString(), "UTF8");
            }

            sb.setLength(0);
            int startPos = -1;
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(pomUsageFile), "UTF8"));
            for (String line = null; (line = reader.readLine()) != null;) {
                line = StringUtils.trimToEmpty(line);
                // System.out.println("....." + line);

                if (line.startsWith("[INFO] Dependencies classpath:")) {
                    startPos = reader.getLineNumber() + 1;
                }
                if (startPos != -1 && reader.getLineNumber() >= startPos) {
                    if (line.startsWith("[INFO] ------")) {
                        continue;
                    }
                    if (line.startsWith("[INFO] BUILD SUCCESS")) {
                        buildSuccess = true;
                        break;
                    }
                    sb.append(line);
                }
            }

            if (!buildSuccess) {
                throw new Exception("非正常結束!, not build success");
            }

            List<String> rtnLst = new ArrayList<String>();
            String delimit = isWindows ? "\\;" : "\\:";
            String[] jarArry = sb.toString().split(delimit, -1);
            for (String jarPath : jarArry) {
                rtnLst.add(jarPath);
            }
            return rtnLst;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
}
