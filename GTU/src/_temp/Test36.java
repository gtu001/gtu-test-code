package _temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

public class Test36 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        int count = 0;
        File dir = new File("E:\\my_tool\\english");
        for(File f : dir.listFiles()) {
            if(f.getName().endsWith(".properties")) {
                Properties p = new Properties();
                p.load(new FileInputStream(f));
                count += p.size();
            }
        }
        System.out.println(count);
    }

}
