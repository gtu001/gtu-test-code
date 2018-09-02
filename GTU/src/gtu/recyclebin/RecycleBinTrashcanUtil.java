package gtu.recyclebin;

import java.io.File;

public class RecycleBinTrashcanUtil {

    public static boolean moveToTrashCan(File... files) {
        try {
            com.sun.jna.platform.FileUtils fileUtil = com.sun.jna.platform.FileUtils.getInstance();
            if (fileUtil.hasTrash()) {
                fileUtil.moveToTrash(files);
            }
            for (File f : files) {
                if (f.exists()) {
                    System.out.println("can't move to trash can : " + f);
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
