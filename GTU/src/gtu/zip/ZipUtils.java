package gtu.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import gtu.file.FileUtil;

public class ZipUtils {

    public static void main(String[] a) throws Exception {
        File srcFile = new File("C:\\11111");
        File targetZip = new File("C:\\11111\\123.zip");
        File extractDir = new File("C:\\22222");

        try {
            // 壓縮
            new ZipUtils().makeZip(srcFile, targetZip);
            // 解壓縮
            new ZipUtils().unzipFile(targetZip, extractDir);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("done...");
    }

    /**
     * 解壓縮
     * 
     * @param zipfile
     *            zip檔位置
     * @param extractDir
     *            解壓縮資料夾
     * @return
     */
    public boolean unzipFile(File zipfile, File extractDir) {
        try {
            unZip(zipfile, extractDir.getAbsolutePath());
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            throw new RuntimeException(ex.getMessage(), ex);
            // return false;
        }
        return true;
    }

    /**
     * 建立資料夾
     * 
     * @param directory
     * @param subDirectory
     */
    private void createDirectory(String directory, String subDirectory) {
        String dir[];
        File fl = new File(directory);
        try {
            if (subDirectory == "" && fl.exists() != true)
                fl.mkdir();
            else if (subDirectory != "") {
                dir = subDirectory.replace('\\', '/').split("/");
                for (int i = 0; i < dir.length; i++) {
                    File subFile = new File(directory + File.separator + dir[i]);
                    if (subFile.exists() == false)
                        subFile.mkdir();
                    directory += File.separator + dir[i];
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * 解壓縮主程式 [若檔案已存在會自動覆蓋]
     * 
     * @param zipFileName
     * @param outputDirectory
     * @throws Exception
     *             // * org.apache.tools.zip.
     */
    private void unZip(File ZIPFile, String outputDirectory) throws Exception {
        try {
            ZipFile zipFile = new ZipFile(ZIPFile);
            java.util.Enumeration e = zipFile.entries();
            ZipEntry zipEntry = null;
            createDirectory(outputDirectory, "");
            // if(!outputDirectory.exists()) outputDirectory.mkdirs();

            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
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
            throw new RuntimeException(ex.getMessage(), ex);
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
    public void makeZip(File srcFile, File targetZip) throws IOException, FileNotFoundException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetZip));
        String dir = "";
        recurseFiles(srcFile, zos, dir);
        zos.close();
    }

    /**
     * 壓縮 主程式
     * 
     * @param file
     * @param zos
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void recurseFiles(File file, ZipOutputStream zos, String dir) throws IOException, FileNotFoundException {
        // 目錄
        if (file.isDirectory()) {
            System.out.println("找到資料夾:" + file.getName());
            dir += file.getName() + File.separator;
            String[] fileNames = file.list();
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) {
                    recurseFiles(new File(file, fileNames[i]), zos, dir);
                }
            }
        }
        // Otherwise, a file so add it as an entry to the Zip file.
        else {
            System.out.println("壓縮檔案:" + file.getName());

            byte[] buf = new byte[1024];
            int len;

            // Create a new Zip entry with the file's name.
            dir = dir.substring(dir.indexOf(File.separator) + 1);
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