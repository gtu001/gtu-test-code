package gtu.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;

public class RuntimeBatPromptModeUtil {

    public static void main(String[] args) throws IOException {
        RuntimeBatPromptModeUtil t = new RuntimeBatPromptModeUtil();
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
        }
        return this;
    }

    public RuntimeBatPromptModeUtil command(String cmd) {
        if (cmd != null) {
            this.cmd.append(cmd);
        }
        return this;
    }

    private String __fixCommand(String cmd) {
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new StringReader(cmd));
            for (String line = null; (line = reader.readLine()) != null;) {
                sb.append("  " + line + "\r\n");
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

    public void apply() {
        this.apply("BIG5");
    }

    public void apply(String encode) {
        try {
            if (StringUtils.isBlank(cmd)) {
                throw new Exception("請設定bat內容!");
            }
            File tmpBat = File.createTempFile("tmp_", ".bat");
            FileUtil.saveToFile(tmpBat, __fixCommand(cmd.toString()), encode);
            System.out.println("tempBat : " + tmpBat);
            Runtime.getRuntime().exec(String.format("cmd /c start cmd /k \"%s\" ", tmpBat));
        } catch (Exception ex) {
            throw new RuntimeException("batRun ERR : " + ex.getMessage(), ex);
        }
    }
}
