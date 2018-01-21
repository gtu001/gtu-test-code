package gtu._work.eclipse.plugin;

import gtu.file.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 保留一版未更新plugin的eclipse 與更新後的eclipse比對 匯出新加的檔案
 * 
 * @author Troy 2012/1/17
 */
public class EclipsePlugInUpdateExport {

    /**
     * @param args
     */
    public static void main(String[] args) {

        File file1 = new File("C:\\資拓宏宇相關檔案\\eclipse");// 安裝plug in的eclipse目錄
        File file2 = new File("C:\\資拓宏宇相關檔案\\orign_eclipse");// 未安裝plug
                                                             // in的eclipse目錄
        File file3 = new File(FileUtil.DESKTOP_PATH + "Export");// 匯出目錄

        List<File> fileList1 = new ArrayList<File>();
        List<File> fileList2 = new ArrayList<File>();
        FileUtil.traceFileList(file1, fileList1);
        FileUtil.traceFileList(file2, fileList2);

        List<String> fileList1_ = FileUtil.cutRootPath(file1, fileList1);
        List<String> fileList2_ = FileUtil.cutRootPath(file2, fileList2);

        List<String> disjunctionList = (List<String>) CollectionUtils.disjunction(fileList1_, fileList2_);

        if (!file3.exists()) {
            file3.mkdirs();
        }

        String rootPath = file1.getAbsolutePath() + "\\";
        String exportPath = file3.getAbsolutePath() + "\\";
        File chkFile = null;
        for (String cutPath : disjunctionList) {
            chkFile = new File(rootPath + cutPath);
            if (!chkFile.exists()) {
                throw new RuntimeException("檔案不存在:" + chkFile.getAbsolutePath());
            }

            byte[] b = FileUtil.loadFromFile(chkFile);
            FileUtil.createParentFolder(chkFile, new File(exportPath + cutPath));
            FileUtil.saveToFile(exportPath + cutPath, b);
        }
        System.out.println("done...");
    }
}
