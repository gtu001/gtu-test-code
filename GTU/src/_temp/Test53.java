package _temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gtu.file.FileUtil;

public class Test53 {

    public static void main(String[] args) throws InterruptedException, IOException {
        Test53 t = new Test53();

        List<String> lst = new ArrayList<String>();
        
        FileInputStream fis = new FileInputStream(new File(FileUtil.DESKTOP_DIR, "orign.txt"));

        LineNumberReader reader = new LineNumberReader(new InputStreamReader(fis, "utf8"));
        for (String line = null; (line = reader.readLine()) != null;) {
            line = StringUtils.trim(line);
            if(!lst.contains(line)) {
                lst.add(line);
            }
        }
        reader.close();
        
        String content = StringUtils.join(lst, "\n");
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "ok.txt"), content, "utf8");
        System.out.println("done...");
    }
}
