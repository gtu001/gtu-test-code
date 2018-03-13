package gtu.runtime;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class ProcessRuntimeExec {

    public static void main(String[] args) {

    }

    public static void runShell(String startFolder, String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new File(startFolder));
            pb.command(Arrays.asList("./" + command));
            Process proc = pb.start();

            List<String> lst = IOUtils.readLines(proc.getInputStream(), "utf8");
            List<String> errLst = IOUtils.readLines(proc.getErrorStream(), "utf8");

            for (String log : lst) {
                System.out.println("Sh>>" + log);
            }
            for (String log : errLst) {
                System.out.println("Sh ERR>>" + log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
