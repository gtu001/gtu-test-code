package _temp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

public class Test42 {

    public static void main(String[] args) throws InterruptedException, IOException {
        File dir = new File("D:/workstuff/workspace_ebmw");
        chkDir(dir);
        System.out.println("done...");
    }

    private static void chkDir(File file) {
        if (file.isDirectory()) {
            if (file.getName().equals("bin")) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Arrays.asList(file.listFiles()).stream().forEach(Test42::chkDir);
            }
        }
    }
}
