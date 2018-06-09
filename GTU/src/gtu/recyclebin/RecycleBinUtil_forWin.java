package gtu.recyclebin;

import java.io.File;
import java.io.IOException;

import gtu.file.FileUtil;

public class RecycleBinUtil_forWin {

    public static void main(String[] args) throws IOException {
        RecycleBinUtil_forWin.moveTo(new File("C:/Users/gtu00/OneDrive/Desktop/deleteJS.bat"));
        System.out.println("done...");
    }

    public static boolean moveTo(File fileOrDir) {
        try {
            if (!fileOrDir.exists()) {
                System.out.println("檔案不存在 : " + fileOrDir);
                return false;
            }
            File deleteBat = FileUtil.createFileFromURL(RecycleBinUtil_forWin.class.getResource("deleteJS.bat"), "deleteJS.bat");
            String command = String.format("cmd /c call \"%s\" \"%s\" ", deleteBat, fileOrDir);
            Process p = Runtime.getRuntime().exec(command);
            int resultCode = p.waitFor();
            String resultStr = (resultCode == 0 ? "刪除成功" : "刪除失敗") + " : " + fileOrDir;
            System.out.println(resultStr);
            return resultCode == 0;
        } catch (Exception e) {
            throw new RuntimeException("moveToRecycleBin ERR : " + e.getMessage(), e);
        }
    }
}