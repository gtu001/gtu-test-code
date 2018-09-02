package gtu.runtime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;

public class ProcessRuntimeExec {

    public static void main(String[] args) {
        runCommandForWin(FileUtil.DESKTOP_PATH + File.separator + "DC.bat");
        System.out.println("done...");
    }

    private String getInputStreamToString(InputStream inputStream, String encode) throws IOException {
        ByteArrayOutputStream bios = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, bios);
        return new String(bios.toByteArray(), encode);
    }

    private static ProcessRuntimeExec_Result _processHandler(Process exec, String encode) throws InterruptedException, IOException {
        ProcessRuntimeExec_Result vo = new ProcessRuntimeExec_Result();
        int result = exec.waitFor();
        vo.resultCode = result;
        List<String> inLst = IOUtils.readLines(exec.getInputStream(), encode);
        List<String> exLst = IOUtils.readLines(exec.getErrorStream(), encode);
        vo.inputLst = inLst;
        vo.errorLst = exLst;
        for (String line : inLst) {
            System.out.println("console>>" + line);
        }
        for (String line : exLst) {
            System.out.println("Error>>" + line);
        }
        System.out.println("執行 : " + (result == 0 ? "成功" : "失敗=" + result));
        return vo;
    }

    public static boolean runCommandForWin(String command) {
        try {
            Process exec = Runtime.getRuntime().exec("cmd /c " + command);
            int result = _processHandler(exec, "BIG5").resultCode;
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
            int result = _processHandler(exec, "UTF8").resultCode;
            return result == 0;
        } catch (Exception ex) {
            throw new RuntimeException("runCommandForLinux Err : " + ex.getMessage(), ex);
        }
    }

    public static ProcessRuntimeExec_Result runCommandForLinux_full(File startFolder, String command, String encode) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            if (startFolder != null) {
                pb.directory(startFolder);
            }
            pb.command(command);
            Process exec = pb.start();
            return _processHandler(exec, encode);
        } catch (Exception ex) {
            throw new RuntimeException("runCommandForLinux Err : " + ex.getMessage(), ex);
        }
    }
    
    public static ProcessRuntimeExec_Result runCommandForWin_full(String command, String encode) {
        try {
            Process exec = Runtime.getRuntime().exec("cmd /c " + command);
            return _processHandler(exec, encode);
        } catch (Exception ex) {
            throw new RuntimeException("runCommand Err : " + ex.getMessage(), ex);
        }
    }

    public static class ProcessRuntimeExec_Result {
        int resultCode = -1;
        List<String> inputLst = new ArrayList<String>();
        List<String> errorLst = new ArrayList<String>();

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public List<String> getInputLst() {
            return inputLst;
        }

        public void setInputLst(List<String> inputLst) {
            this.inputLst = inputLst;
        }

        public List<String> getErrorLst() {
            return errorLst;
        }

        public void setErrorLst(List<String> errorLst) {
            this.errorLst = errorLst;
        }

        @Override
        public String toString() {
            return "ProcessRuntimeExec_Result [resultCode=" + resultCode + ", inputLst=" + inputLst + ", errorLst=" + errorLst + "]";
        }
    }
}
