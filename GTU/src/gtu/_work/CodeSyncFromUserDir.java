package gtu._work;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.lang3.ArrayUtils;

import gtu.file.FileUtil;

public class CodeSyncFromUserDir {

    public static void main(String[] args) throws IOException {
        File file = new File("\\\\10.174.3.214\\mis\\系統開發手冊\\PCWeb專案文件\\知識庫\\李金龍\\20170914_網路投保環境");
        File toDir = new File("D:\\workspace\\TWSP\\twsp");
        CodeSyncFromUserDir t = new CodeSyncFromUserDir();
        t.exceute(file, toDir, System.out);
        System.out.println("done...");
    }
    
    private PrintStream out;

    public void exceute(File fromDir, File toDir, PrintStream out) {
        try {
            this.out = out;
            
            if(!toDir.exists()) {
                toDir.mkdirs();
            }
            
            copyFiles(toDir, fromDir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String[] SKIP_ARRAY = new String[] { ".svn", ".settings" };
    private static final String[] SKIP_FILES = new String[] { ".project", ".classpath" };
    private static final String[] SKIP_SUB_FILES = new String[] { ".class" };

    private void copyFiles(File toDir, File f) throws IOException {
        if (f.isDirectory()) {
            // ↓↓↓↓↓↓ skip check
            if (ArrayUtils.contains(SKIP_ARRAY, f.getName())) {
                out.println("skip - " + f + " -> 特殊目錄");
                return;
            }
            // ↑↑↑↑↑↑ skip check
            for (File f2 : f.listFiles()) {
                File newDir = new File(toDir, f2.getName());
                if (f2.isDirectory() && !newDir.exists()) {
                    newDir.mkdir();
                }

                copyFiles(newDir, f2);
            }
        } else {
            File copyToFile = toDir;

            // ↓↓↓↓↓↓ skip check
            if (ArrayUtils.contains(SKIP_FILES, f.getName())) {
                out.println("skip - " + copyToFile + " -> 特殊檔名");
                return;
            }

            String chkFileName = f.getName().toLowerCase();
            for (String subName : SKIP_SUB_FILES) {
                if (chkFileName.endsWith(subName)) {
                    out.println("skip - " + copyToFile + " -> 特殊副檔名");
                    return;
                }
            }

            if (copyToFile.exists() && //
                    f.length() == copyToFile.length() && //
                    f.lastModified() == copyToFile.lastModified()) {
                out.println("skip - " + copyToFile + " -> 檔案相同");
                return;
            }
            // ↑↑↑↑↑↑ skip check

            copyToFile.delete();
            FileUtil.copyFile(f, copyToFile);
            out.println("copy - " + copyToFile);
        }
    }
}
