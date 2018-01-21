package gtu.zip;

import gtu.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 測試可用 但不可解7z檔
 * 
 * @author 2012/1/17
 */
public class ZipUtil {

    public static void main(String[] args) throws IOException {
        ZipUtil test = new ZipUtil();
        // test.zip("c:\\url.txt", "c:\\test\\url.zip");
        test.unzip("C:\\Jar\\ant-1.7.1.jar", FileUtil.DESKTOP_PATH + "EXPORT");
        System.out.println("done...");
    }

    public void zip(String source, String dest) throws IOException {
        OutputStream os = new FileOutputStream(dest);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ZipOutputStream zos = new ZipOutputStream(bos);

        // 支持中文，但有缺陷！?是硬??！
        zos.setEncoding("GBK");

        File file = new File(source);

        String basePath = null;
        if (file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }

        zipFile(file, basePath, zos);

        zos.closeEntry();
        zos.close();
    }

    public void unzip(String zipFile, String dest) throws IOException {
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> en = zip.getEntries();
        ZipEntry entry = null;
        byte[] buffer = new byte[1024];
        int length = -1;
        InputStream input = null;
        BufferedOutputStream bos = null;
        File file = null;

        while (en.hasMoreElements()) {
            entry = (ZipEntry) en.nextElement();
            if (entry.isDirectory()) {
                file = new File(dest, entry.getName());
                if (!file.exists()) {
                    file.mkdir();
                }
                continue;
            }

            input = zip.getInputStream(entry);
            file = new File(dest, entry.getName());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            bos = new BufferedOutputStream(new FileOutputStream(file));

            while (true) {
                length = input.read(buffer);
                if (length == -1)
                    break;
                bos.write(buffer, 0, length);
            }
            bos.close();
            input.close();
        }
        zip.close();
    }

    private void zipFile(File source, String basePath, ZipOutputStream zos) throws IOException {
        File[] files = new File[0];

        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }

        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + "/";
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                InputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                while ((length = bis.read(buf)) > 0) {
                    zos.write(buf, 0, length);
                }
                is.close();
            }
        }
    }
}