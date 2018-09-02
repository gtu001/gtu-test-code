package gtu.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipToSameDirectory {
    /**
     * 將壓縮檔壓縮在同一個資料夾底下
     *
     * @param fileList
     * @param zipFile
     * @return
     * @throws IOException
     */
    public File makeZip(List<File> fileList, File zipFile) throws IOException {
        byte[] buf = new byte[1024];
        ZipOutputStream output = new ZipOutputStream(new FileOutputStream(zipFile));
        for (File f : fileList) {
            ZipEntry entry = new ZipEntry(f.getName());
            entry.setTime(f.lastModified());
            entry.setSize(f.length());
            output.putNextEntry(entry);

            int len;
            FileInputStream fin = new FileInputStream(f);
            BufferedInputStream in = new BufferedInputStream(fin);
            while ((len = in.read(buf)) >= 0) {
                output.write(buf, 0, len);
            }
            output.closeEntry();
            in.close();
        }
        output.flush();
        output.close();
        return zipFile;
    }
}
