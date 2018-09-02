package _temp;

import java.io.File;

import gtu.file.FileUtil;

public class Test_FucoReplaceSpecailChar {

    public static void main(String[] args) {
        File file = new File("I:/workstuff/workspace_scsb/SCSB_CCBill_DC/src/main/java/com/fuco/mb/conv/bank/RawDateConverter.java");
        String val = FileUtil.loadFromFile(file, "utf8");
        val = val.replace("\ufeff", "");
        FileUtil.saveToFile(file, val, "utf8");
        System.out.println("done...");
    }

}
