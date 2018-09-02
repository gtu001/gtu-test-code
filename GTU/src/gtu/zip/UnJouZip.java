package gtu.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 戶役政邏輯檔案
 * 
 * @author 陳勇洲
 */
public class UnJouZip {

    public static void main(String[] args) {
        System.out.println("done...");
    }

    private static Logger log = LoggerFactory.getLogger(UnJouZip.class);

    /**
     * 建立資料夾
     * 
     * @param directory
     * @param subDirectory
     * @throws RisException
     */
    private void createDirectory(String directory, String subDirectory) throws Exception {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory.length() == 0 && fl.exists() != true)
                fl.mkdir();
            else if (subDirectory.length() != 0) {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            log.error("建立目錄 {} 錯誤", directory, ex);
            throw new Exception("建立目錄" + directory + "錯誤", ex);
        }
    }

    /**
     * 解壓縮主程式
     * 
     * @param zipFileName
     * @param outputDirectory
     * @throws RisException
     * @throws Exception
     */
    private void unZip(File ZIPFile, String outputDirectory) throws Exception {
        try {
            org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(ZIPFile);
            java.util.Enumeration e = zipFile.getEntries();
            org.apache.tools.zip.ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            // if(!outputDirectory.exists()) outputDirectory.mkdirs();

            while (e.hasMoreElements()) {
                zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
                System.out.println("unziping " + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    String name = zipEntry.getName();
                    name = name.substring(0, name.length() - 1);
                    File f = new File(outputDirectory + File.separator + name);
                    f.mkdir();
                    System.out.println("創建立目錄：" + outputDirectory + File.separator + name);
                } else {
                    String fileName = zipEntry.getName();
                    fileName = fileName.replace('\\', '/');

                    if (fileName.indexOf("/") != -1) {
                        createDirectory(outputDirectory, fileName.substring(0, fileName.lastIndexOf("/")));
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                    }

                    File f = new File(outputDirectory + File.separator + zipEntry.getName());

                    f.createNewFile();
                    InputStream in = zipFile.getInputStream(zipEntry);
                    FileOutputStream out = new FileOutputStream(f);

                    byte[] by = new byte[1024];
                    int c;
                    while ((c = in.read(by)) != -1) {
                        out.write(by, 0, c);
                    }
                    out.close();
                    in.close();
                }
            }
        } catch (Exception ex) {
            log.error("解壓檔案 {} 錯誤", ZIPFile, ex);
            throw new Exception("解壓檔案" + ZIPFile + "錯誤", ex);
        }
    }

    /**
     * 建立 zip 檔
     * 
     * @param srcFile
     *            想要壓縮的資料夾
     * @param targetZip
     *            壓縮zip檔
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void makeZip(List<File> srcFiles, File targetZip) throws IOException, FileNotFoundException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZip));
        String dir = "";
        for (File srcFile : srcFiles) {
            recurseFiles(srcFile, zos, dir);
        }
        zos.close();
    }

    /**
     * zip壓縮 主程式
     * 
     * @param file
     * @param zos
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void recurseFiles(File file, ZipOutputStream zos, String dir) throws IOException, FileNotFoundException {
        // 目錄
        if (file.isDirectory()) {
            // logger.debug("找到資料夾: {} {}", file.getName(),
            // file.listFiles().length);
            dir += file.getName() + File.separator;
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    recurseFiles(files[i], zos, dir);
                }
            }
        }
        // Otherwise, a file so add it as an entry to the Zip file.
        else {
            // logger.debug("壓縮檔案: {} {}", file.getName(), dir +
            // file.getName());
            byte[] buf = new byte[1024];
            int len;

            // Create a new Zip entry with the file's name.
            // dir = dir.substring(dir.indexOf(File.separator) + 1);
            ZipEntry zipEntry = new ZipEntry(dir + file.getName());
            // Create a buffered input stream out of the file
            // we're trying to add into the Zip archive.
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            zos.putNextEntry(zipEntry);
            // Read bytes from the file and write into the Zip archive.

            while ((len = in.read(buf)) >= 0) {
                zos.write(buf, 0, len);
            }

            // Close the input stream.
            in.close();

            // Close this entry in the Zip stream.
            zos.closeEntry();
        }
    }

}
