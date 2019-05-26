package gtu.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessLinuxConsoleReader {

    public static String getConsole(Process p) {
        try {
            StringBuffer sb = new StringBuffer();
            String s = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                System.out.println("line: " + s);
                sb.append(s);
                sb.append("\n");
            }
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("getConsole ERR : " + e.getMessage(), e);
        }
    }
}
