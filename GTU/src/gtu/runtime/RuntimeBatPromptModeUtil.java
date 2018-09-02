package gtu.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;

public class RuntimeBatPromptModeUtil {

    private static boolean isWindows = false;
    private static String prefix = "  ";
    private static String chgLine = "\r\n";

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
            prefix = "  ";
            chgLine = "\r\n";
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
            prefix = "";
            chgLine = "\n";
        }
    }

    public static void main(String[] args) throws IOException {
        RuntimeBatPromptModeUtil t = RuntimeBatPromptModeUtil.newInstance();

        t.command("cd /media/gtu001/OLD_D/workstuff/workspace/gtu-test-code/GTU");
        t.command("sh /media/gtu001/OLD_D/apps/apache-maven-3.3.9/bin/mvn dependency:build-classpath -DincludeScope=runtime");

        String console = ProcessLinuxConsoleReader.getConsole(t.apply());
        System.out.println(console);

        System.out.println("done...");
    }

    private StringBuffer cmd = new StringBuffer();

    private RuntimeBatPromptModeUtil() {
    }

    public static RuntimeBatPromptModeUtil newInstance() {
        return new RuntimeBatPromptModeUtil();
    }

    public RuntimeBatPromptModeUtil command(StringBuffer cmd) {
        if (cmd != null) {
            this.cmd.append(cmd);
            this.cmd.append(chgLine);
        }
        return this;
    }

    public RuntimeBatPromptModeUtil command(String cmd) {
        if (cmd != null) {
            this.cmd.append(cmd);
            this.cmd.append(chgLine);
        }
        return this;
    }

    private String __fixCommand(String cmd) {
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new StringReader(cmd));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append(prefix);
                sb.append(line);
                sb.append(chgLine);
            }
            System.out.println("Command Body Start-----------------------------------------");
            System.out.println(sb);
            System.out.println("Command Body ENd  -----------------------------------------");
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("getCommand ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public Process apply() {
        return this.apply("BIG5");
    }

    public Process apply(String encode) {
        try {
            if (StringUtils.isBlank(cmd)) {
                throw new Exception("請設定 bat / sh 內容!");
            }

            if (isWindows) {
                File tmpBat = File.createTempFile("tmp_", ".bat");
                FileUtil.saveToFile(tmpBat, __fixCommand(cmd.toString()), encode);
                System.out.println("tempBat : " + tmpBat);
                return Runtime.getRuntime().exec(String.format("cmd /c start cmd /k \"%s\" ", tmpBat));
            } else {
                cmd.insert(0, "#!/bin/bash\r\n");
                File tmpSh = File.createTempFile("tmp_", ".sh");
                FileUtil.saveToFile(tmpSh, __fixCommand(cmd.toString()), encode);
                System.out.println("tmpSh : " + tmpSh);
                Runtime.getRuntime().exec(String.format("chmod u+x %s", tmpSh));
                return Runtime.getRuntime().exec(String.format("sh %s ", tmpSh));
            }
        } catch (Exception ex) {
            throw new RuntimeException("batRun ERR : " + ex.getMessage(), ex);
        }
    }
}
