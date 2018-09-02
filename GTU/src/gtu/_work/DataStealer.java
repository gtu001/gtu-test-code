package gtu._work;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 偷別人硬碟東西用的
 */
public class DataStealer {

    private String[] subName = { ".java", ".jsp", ".jar", ".avi", ".mp4", ".rmvb", ".wmv" };

    public static void main(String[] args) throws IOException {
        DataStealer test = new DataStealer();
        File file = new File("F:\\");

        test.listFile(file);
        if (test.fileSet.size() == 0) {
            System.out.println("無感興趣資料可偷!!");
        } else {
            List<File> list = new ArrayList<File>(test.fileSet);
            Collections.sort(list);
            for (File f : list) {
                System.out.println(f);
            }
        }
        System.out.println("done...");
    }

    Set<File> fileSet = new HashSet<File>();

    void listFile(File file) {
        try {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    listFile(f);
                }
            } else if (StringUtils.indexOfAny(file.getName(), subName) != -1) {
                fileSet.add(file.getParentFile());
            }
        } catch (Exception ex) {
            System.err.println("error : " + file);
            ex.printStackTrace();
        }
    }
}
