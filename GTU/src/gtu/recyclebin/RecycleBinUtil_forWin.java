package gtu.recyclebin;

import java.io.File;
import java.io.IOException;

import gtu.file.FileUtil;
import gtu.runtime.ProcessWatcher;

public class RecycleBinUtil_forWin {

    public static void main(String[] args) throws IOException {
        RecycleBinUtil_forWin.moveTo(new File("C:/Users/gtu00/OneDrive/Desktop/deleteJS.bat"));
        System.out.println("done...");
    }

    public static boolean moveTo(File fileOrDir) {
        for (int time = 0; time < 20; time++) {
            try {
                if (!fileOrDir.exists()) {
                    System.out.println("檔案不存在 : " + fileOrDir);
                    return false;
                }
                File deleteBat = FileUtil.createFileFromURL(RecycleBinUtil_forWin.class.getResource("deleteJS.bat"), "deleteJS.bat");
                String command = String.format("cmd /c call \"%s\" \"%s\" ", deleteBat, fileOrDir);
                Process p = Runtime.getRuntime().exec(command);
                int resultCode = p.waitFor();

                ProcessWatcher watcher = ProcessWatcher.newInstance(p);
                System.out.println("ERR Start ====================================");
                System.out.println("ERR : " + watcher.getErrorStreamToString());
                System.out.println("ERR End   ====================================");
                System.out.println("Info Start ===================================");
                System.out.println("Info : " + watcher.getInputStreamToString());
                System.out.println("INfo End   ===================================");
                String resultStr = (resultCode == 0 ? "刪除成功" : "刪除失敗") + " : " + fileOrDir;
                System.out.println(resultStr);
                if (resultCode == 0 && !fileOrDir.exists()) {
                    return true;
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                    }
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                }
            }
        }
        return false;
    }
}