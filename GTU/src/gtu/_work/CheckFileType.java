package gtu._work;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 確認目錄底下有的檔案類型
 */
public class CheckFileType {

    /**
     * @param args
     */
    public static void main(String[] args) {
        CheckFileType test = new CheckFileType();
        File file = new File("C:\\workspace\\ris3rl2\\ris3rl2-web\\src\\main\\webapp");
        test.listFile(file);
        for (String subName : test.fileSet) {
            System.out.println(subName);
        }
        System.out.println("done...");
    }

    Set<String> fileSet = new HashSet<String>();

    void listFile(File file) {
        try {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    listFile(f);
                }
            } else {
                int pos = file.getName().lastIndexOf(".");
                fileSet.add(file.getName().substring(pos));
            }
        } catch (Exception ex) {
            System.err.println("error : " + file);
            ex.printStackTrace();
        }
    }
}
