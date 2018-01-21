package gtu.string;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

import taobe.tec.jcc.JChineseConvertor;

public class GBK_Big5_transfer {

    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/gtu001/Desktop/my_tool");
        for (File f : file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".properties");
            }
        })) {
            Properties prop = new Properties();
            prop.load(new FileInputStream(f));
            for (Object k_ : prop.keySet()) {
                String key = (String) k_;
                String value = prop.getProperty(key);
                if (value == null) {
                    continue;
                }
                //檢轉凡
                value = JChineseConvertor.getInstance().s2t(value);
                prop.setProperty(key, value);
            }
            //prop.store(new FileOutputStream(f), "測試");
        }
        System.out.println("done..");
    }
}
