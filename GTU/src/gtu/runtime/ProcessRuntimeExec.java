package gtu.runtime;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import gtu.file.FileUtil;

public class ProcessRuntimeExec {

    public static void main(String[] args) {
        runCommandForWin(FileUtil.DESKTOP_PATH + File.separator + "DC.bat");
        System.out.println("done...");
    }

    private static int _processHandler(Process exec, String encode) throws InterruptedException, IOException {
        int result = exec.waitFor();
        List<String> inLst = IOUtils.readLines(exec.getInputStream(), encode);
        List<String> exLst = IOUtils.readLines(exec.getErrorStream(), encode);
        for (String line : inLst) {
            System.out.println("console>>" + line);
        }
        for (String line : exLst) {
            System.out.println("Error>>" + line);
        }
        System.out.println("執行 : " + (result == 0 ? "成功" : "失敗=" + result));
        return result;
    }

    public static boolean runCommandForWin(String command) {
        try {
            Process exec = Runtime.getRuntime().exec("cmd /c " + command);
            int result = _processHandler(exec, "BIG5");
            return result == 0;
        } catch (Exception ex) {
            throw new RuntimeException("runCommand Err : " + ex.getMessage(), ex);
        }
    }

    public static boolean runCommandForLinux(String startFolder, String shellName) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new File(startFolder));
            pb.command(Arrays.asList("./" + shellName));
            Process exec = pb.start();
            int result = _processHandler(exec, "UTF8");
            return result == 0;
        } catch (Exception ex) {
            throw new RuntimeException("runCommandForLinux Err : " + ex.getMessage(), ex);
        }
    }
}
