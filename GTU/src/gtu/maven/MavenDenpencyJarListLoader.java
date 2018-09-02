package gtu.maven;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        File pomFile = new File("‪/media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU/pom.xml");
        File mavenDir = new File("/media/gtu001/OLD_D/apps/apache-maven-3.3.9/bin/mvn"); // System.getenv("M2_HOME")

        List<String> jarLst = MavenDenpencyJarListLoader.newInstance()//
                .pomFile(pomFile)//
                .mavenExePath(mavenDir)//
                .build();//
        for (String jarPath : jarLst) {
            System.out.println("\t" + jarPath);
        }
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
                execdo.command(rootPath);
            }
            execdo.command((isWindows ? "" : "sh ") + FileUtil.replaceSpecialChar(mavenExePath.getAbsolutePath()) + " dependency:build-classpath -DincludeScope=runtime");

            Process exec = execdo.apply();
            ProcessWatcher _inst = ProcessWatcher.newInstance(exec);
            _inst.getStream();

            boolean buildSuccess = false;

            StringBuilder sb = new StringBuilder();
            reader = new LineNumberReader(new StringReader(_inst.getInputStreamToString()));
            int startPos = -1;
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
                System.err.println("input : " + _inst.getInputStreamToString());
                System.err.println("error : " + _inst.getErrorStreamToString());
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
