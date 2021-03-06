package gtu.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import gtu.file.FileUtil;

public class UnZipBean {

    public static void main(String[] args) {
        UnZipBean test = new UnZipBean();
        test.zipFile = ("C:\\MyPlugIn\\quantum.7z");
        test.targetDirectory = (FileUtil.DESKTOP_PATH + "Export");
        test.unzip();
        System.out.println("done...");
    }

    // 寫入緩衝大小
    final public static int FileWriteBuffer = 5 * 1024 * 1024;

    public static final int EOF = -1;
    static final int BUFFER = 2048;

    private String zipFile;
    private String targetDirectory;
    private ZipFile zf;

    /** Constructor */
    public UnZipBean() {
    }

    public UnZipBean(String zipFile, String targetDirectory) {
        this.zipFile = zipFile;
        this.targetDirectory = targetDirectory;
    }

    public void setZipFile(String zipFile) {
        this.zipFile = zipFile;
    }

    public String getZipFile() {
        return zipFile;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public String getTargetDirectory() {
        return targetDirectory;
    }

    public boolean unzip() {
        boolean done = false;
        if (zipFile != null) {
            try {
                zf = new ZipFile(zipFile);
                Enumeration enumeration = zf.entries();
                while (enumeration.hasMoreElements()) {
                    ZipEntry target = (ZipEntry) enumeration.nextElement();
                    // System.out.print(target.getName() + " .");
                    saveEntry(target);
                    // System.out.println(". unpacked");
                }
                done = true;
            } catch (FileNotFoundException e) {
                System.out.println("zipfile not found" + e.getMessage());
            } catch (ZipException e) {
                System.out.println("zip error..." + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO error..." + e.getMessage());
            } finally {
                try {
                    zf.close();
                } catch (IOException e) {
                    System.out.println("IO error...Can't close zip file" + e.getMessage());
                }
            }
        }
        return done;
    }

    private void saveEntry(ZipEntry target) throws ZipException, IOException {
        try {
            File file = new File(targetDirectory + File.separator + target.getName());
            if (target.isDirectory()) {
                file.mkdirs();
            } else {
                InputStream is = zf.getInputStream(target);
                BufferedInputStream bis = new BufferedInputStream(is);
                File dir = new File(file.getParent());
                dir.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos, FileWriteBuffer);

                int c;
                byte[] data = new byte[BUFFER];
                while ((c = bis.read(data, 0, BUFFER)) != EOF) {
                    bos.write(data, 0, c);
                }
                bos.flush();
                bos.close();
                fos.close();
            }
        } catch (ZipException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
}