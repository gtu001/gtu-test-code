package gtu.runtime;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import gtu.file.FileUtil;

public class ProcessRuntimeExec {

    public static void main(String[] args) {
        runBat(FileUtil.DESKTOP_PATH, "DC.bat");
        System.out.println("done...");
    }
    
    public static void runBat(String startFolder, String batName) {
        try {
//            ProcessBuilder pb = new ProcessBuilder();
//            pb.directory(new File(startFolder));
//            pb.command(batName);
//            Process proc = pb.start();
            String cmd = startFolder + File.separator + batName;
            Process proc = Runtime.getRuntime().exec("cmd /c " + cmd);

            List<String> lst = IOUtils.readLines(proc.getInputStream(), "big5");
            List<String> errLst = IOUtils.readLines(proc.getErrorStream(), "big5");

            for (String log : lst) {
                System.out.println("Bat>>" + log);
            }
            for (String log : errLst) {
                System.out.println("Bat ERR>>" + log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runShell(String startFolder, String shellName) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new File(startFolder));
            pb.command(Arrays.asList("./" + shellName));
            Process proc = pb.start();

            List<String> lst = IOUtils.readLines(proc.getInputStream(), "utf8");
            List<String> errLst = IOUtils.readLines(proc.getErrorStream(), "utf8");

            for (String log : lst) {
                System.out.println("Sh>>" + log);
            }
            for (String log : errLst) {
                System.out.println("Sh ERR>>" + log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
