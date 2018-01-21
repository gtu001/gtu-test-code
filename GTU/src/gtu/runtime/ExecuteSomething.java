package gtu.runtime;

import java.io.IOException;

/**
 * 執行某執行檔
 * 
 * @author Troy 2012/1/7
 */
public class ExecuteSomething {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        // Runtime.getRuntime().exec("cmd /c start XXXX.bat"); //執行bat檔
        // Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c",
        // "build.bat"});

        Runtime rt = Runtime.getRuntime();
        // Process process = rt.exec("C:\\Windows\\notepad.exe");
        String ultraEdit = "C:\\Program Files (x86)\\IDM Computer Solutions\\UltraEdit\\Uedit32.exe ";
        Process process = rt.exec(ultraEdit + "C:\\downloads\\報表\\7-5-RLRP08610.doc");
        int wait = process.waitFor();
        System.out.println(wait);
        process.destroy();
    }
}
