package gtu.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTest {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        ZipTest t = new ZipTest();
        t.zipAllFile();
        System.out.println("將c:\\test\\壓縮成zip...完成!!");
    }

    private void zipAllFile() throws Exception {
        String zipPath = "c:\\export.zip";
        String srcPath = "C:\\test\\";

        FileOutputStream out = new FileOutputStream(zipPath);
        ZipOutputStream zipoutputstream = new ZipOutputStream(out);
        zipoutputstream.setMethod(ZipOutputStream.DEFLATED);

        File[] fileList = new File(srcPath).listFiles();
        
        for(File file : fileList) {
            byte[] rgb = new byte[1024];
            int n;

            FileInputStream fileinputstream;
            CRC32 crc32 = new CRC32();
            fileinputstream = new FileInputStream(file);
            while ((n = fileinputstream.read(rgb)) > -1) {
                crc32.update(rgb, 0, n);
            }
            fileinputstream.close();

            ZipEntry zipentry = new ZipEntry(file.getName());
            zipentry.setSize(file.length());
            zipentry.setTime(file.lastModified());
            zipentry.setCrc(crc32.getValue());

            zipoutputstream.putNextEntry(zipentry);
            fileinputstream = new FileInputStream(file);

            while ((n = fileinputstream.read(rgb)) > -1) {
                zipoutputstream.write(rgb, 0, n);
            }
            fileinputstream.close();
        }
        
        zipoutputstream.closeEntry();
        zipoutputstream.close();
    }

}
