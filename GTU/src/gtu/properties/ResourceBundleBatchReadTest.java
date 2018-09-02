package gtu.properties;
import java.io.File;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

public class ResourceBundleBatchReadTest {

    public static void main(String[] args) {
        ResourceBundleBatchReadTest xx = new ResourceBundleBatchReadTest();
        xx.initResource();
    }

    public void initResource() {
        try {

            Iterator iter = FileUtils.iterateFiles(new File(System.getProperty("user.dir")),
                    new String[] { "properties" }, true);

            while (iter.hasNext()) {
                File f = (File) iter.next();
                System.out.println(f);
                String fileName = (((File) f).getName()).split(".properties")[0];

                ResourceBundle.getBundle("rl\\" + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
