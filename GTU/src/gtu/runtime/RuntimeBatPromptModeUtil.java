package gtu.runtime;

import java.io.File;
import java.io.IOException;

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

    public void apply() {
        try {
            if (StringUtils.isBlank(cmd)) {
                throw new Exception("請設定bat內容!");
            }
            File tmpBat = File.createTempFile("tmp_", ".bat");
            FileUtil.saveToFile(tmpBat, cmd.toString(), "UTF-8");
            System.out.println("tempBat : " + tmpBat);
            Runtime.getRuntime().exec(String.format("cmd /c start cmd /k \"%s\" ", tmpBat));
        } catch (Exception ex) {
            throw new RuntimeException("batRun ERR : " + ex.getMessage(), ex);
        }
    }
}
