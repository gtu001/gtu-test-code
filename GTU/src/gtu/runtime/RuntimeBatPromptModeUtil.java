package gtu.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;

public class RuntimeBatPromptModeUtil {

    private static boolean isWindows = false;
    private static String prefix = "  ";
    private static String chgLine = "\r\n";
    private static final String BAT_FORAT;

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
            prefix = StringUtils.leftPad("", Integer.parseInt(System.getProperty("spaceLength", "50")));
            chgLine = "\r\n";
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
            prefix = "";
            chgLine = "\n";
        }

        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append("   REM @echo off \n");
        sb.append("   for /f \"tokens=2 delims=:.\" %%x in ('chcp') do set cp=%%x \n");
        sb.append("   chcp {0}  >nul \n");
        sb.append("    \n");
        sb.append("   {1} \n");
        sb.append("    \n");
        sb.append("   chcp %cp%>nul    \n");
        BAT_FORAT = sb.toString();
    }

    public static void main(String[] args) throws IOException {
        RuntimeBatPromptModeUtil t = RuntimeBatPromptModeUtil.newInstance();
        String result = MessageFormat.format(BAT_FORAT, new Object[] { "555555", "command here" });
        System.out.println(result);
        System.out.println("done...");
    }

    private StringBuffer cmd = new StringBuffer();
    private boolean runInBatFile = true;

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

    public RuntimeBatPromptModeUtil runInBatFile(boolean runInBatFile) {
        this.runInBatFile = runInBatFile;
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
        String encoding = isWindows ? "BIG5" : "UTF8";
        return this.apply("tmp_", encoding);
    }

    private String getBatChcpMapping(String encode) {
        if (StringUtils.equalsIgnoreCase("big5", encode) || StringUtils.equalsIgnoreCase("big-5", encode)) {
            return "950";
        } else if (StringUtils.equalsIgnoreCase("gbk", encode)) {
            return "936";
        } else if (StringUtils.equalsIgnoreCase("utf8", encode) || StringUtils.equalsIgnoreCase("utf-8", encode)) {
            return "65001";
        }
        return "";
    }

    public Process apply(String prefix, String encode) {
        try {
            if (StringUtils.isBlank(cmd)) {
                throw new Exception("請設定 bat / sh 內容!");
            }
            if (StringUtils.isBlank(encode)) {
                encode = isWindows ? "BIG5" : "UTF8";
            }
            prefix = StringUtils.isBlank(prefix) ? "tmp_" : prefix;

            if (isWindows) {
                String fixCommand = __fixCommand(cmd.toString());
                String chcpMapping = getBatChcpMapping(encode);
                if (StringUtils.isNotBlank(chcpMapping)) {
                    fixCommand = MessageFormat.format(BAT_FORAT, new Object[] { chcpMapping, fixCommand });
                    System.out.println("Fix chcp : " + chcpMapping + " ### start !");
                    System.out.println(fixCommand);
                    System.out.println("Fix chcp : " + chcpMapping + " ### end !");
                }
                if (runInBatFile) {
                    File tmpBat = File.createTempFile(prefix, ".bat");
                    FileUtil.saveToFile(tmpBat, fixCommand, encode);
                    System.out.println("tempBat : " + tmpBat);
                    return Runtime.getRuntime().exec(String.format("cmd /C start cmd /K \"%s\" ", tmpBat));
                } else {
                    File tmpBat = File.createTempFile(prefix, ".bat");
                    FileUtil.saveToFile(tmpBat, fixCommand, encode);
                    System.out.println("tempBat : " + tmpBat);
                    return Runtime.getRuntime().exec(String.format("cmd /C \"%s\" ", tmpBat));
                }
            } else {
                cmd.insert(0, "#!/bin/bash\r\n");
                File tmpSh = File.createTempFile(prefix, ".sh");
                FileUtil.saveToFile(tmpSh, __fixCommand(cmd.toString()), encode);
                System.out.println("tmpSh : " + tmpSh);
                Runtime.getRuntime().exec(String.format("chmod u+x %s", tmpSh));
                return Runtime.getRuntime().exec(String.format("sh %s ", tmpSh));
            }
        } catch (Throwable ex) {
            throw new RuntimeException("batRun ERR : " + ex.getMessage(), ex);
        }
    }

    private List<String> getCommandList() {
        String tmpCmd = __fixCommand(cmd.toString());
        String[] arry = tmpCmd != null ? tmpCmd.toString().split("\n", -1) : new String[0];
        return new ArrayList<String>(Arrays.asList(arry));
    }

    public String getCommand() {
        String tmpCmd = __fixCommand(cmd.toString());
        return tmpCmd != null ? tmpCmd.toString() : "";
    }

}
