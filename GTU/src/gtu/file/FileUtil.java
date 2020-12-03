/*
 * Copyright (c) 2010-2020 IISI. All rights reserved.
 * 
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import gtu.date.DateUtil;
import gtu.swing.JFrameTest;
import gtu.swing.util.JCommonUtil;
import sun.security.action.GetPropertyAction;

/**
 * @author Troy 2009/05/04
 * 
 */
public class FileUtil {

    public static void main(String[] args) {
        JLabel lbl = new JLabel();
        JCommonUtil.applyDropFiles(lbl, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = (List<File>) e.getSource();
                File file = files.get(0);
                lbl.setText(FileUtil.getSizeDescription(file.length()) + "__" + file.length());
            }
        });
        JFrameTest.simpleTestComponent(lbl);

        System.out.println(getSizeLength(8.93, "mb"));
    }

    private FileUtil() {
    }

    static {
        boolean isOsWindowsSystem = false;
        String destopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
        if (System.getProperty("os.name").equals("Windows XP")) {
            destopPath = System.getProperty("user.home") + File.separator + "桌面" + File.separator;
            isOsWindowsSystem = true;
            System.out.println("FileUtil : Windows XP");
        }
        if (System.getProperty("os.name").equals("Windows 7")) {
            destopPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator;
            isOsWindowsSystem = true;
            System.out.println("FileUtil : Windows 7");
        }
        if (System.getProperty("os.name").equals("Windows 10")) {
            String tmpDesktop = System.getProperty("user.home") + File.separator + "OneDrive" + File.separator + "Desktop" + File.separator;
            File tmpDesktopFile = new File(tmpDesktop);
            if (tmpDesktopFile.exists() && tmpDesktopFile.isDirectory()) {
                destopPath = tmpDesktop;
            }
            isOsWindowsSystem = true;
            System.out.println("FileUtil : Windows 10");
        }
        if (System.getProperty("os.name").equals("Linux")) {
            String tmpDesktop = System.getProperty("user.home") + File.separator + "桌面" + File.separator;
            destopPath = tmpDesktop;
            isOsWindowsSystem = false;
            System.out.println("FileUtil : Linux");
        }

        DESKTOP_PATH = destopPath;
        OS_IS_WINDOWS = isOsWindowsSystem;

        // GetPropertyAction localGetPropertyAction = new
        // GetPropertyAction("java.io.tmpdir");
        // TEMP_DIR = (String)
        // AccessController.doPrivileged(localGetPropertyAction) + "\\";
        TEMP_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    }

    /** 桌面路徑 */
    public static final String DESKTOP_PATH;
    public static final File DESKTOP_DIR = new File(FileUtil.DESKTOP_PATH);
    public static final String TEMP_DIR;
    public static final boolean OS_IS_WINDOWS;

    /** 專案路徑 */
    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * 換副檔名
     * 
     * @param file
     *            要換的檔案
     * @param subName
     *            要換的副檔名
     * @return
     */
    public static File changeSubName(File file, String subName) {
        String name = file.getName();
        if (name.indexOf(".") == -1) {
            throw new RuntimeException("此檔案無副檔名 :" + name);
        }
        name = name.substring(0, name.indexOf(".")) + "." + subName;
        return new File(file.getParent() + "\\" + name);
    }

    /**
     * 取得標準完整路徑
     */
    public static String getCanonicalPath(File file) {
        String path = "";
        try {
            path = file.getCanonicalPath();
        } catch (Exception ex) {
            path = file.getAbsolutePath();
        }
        return path;
    }

    /**
     * 當檔案存在而且不能寫入, 則創立一個新的
     */
    public static File getNewFile(File file) {
        int index = 0;
        do {
            if (!file.exists() || (file.exists() && file.canWrite())) {
                break;
            }
            file = new File(file.getParent(), FileUtil.getNameNoSubName(file) + "_" + index + "." + FileUtil.getSubName(file));
            index++;
        } while (true);
        return file;
    }

    /**
     * 讀取檔案
     * 
     * @param fileName
     *            完整檔案路徑
     * @return
     */
    public static byte[] loadFromFile(String fileName) {
        return loadFromFile(new File(fileName));
    }

    /**
     * 讀取檔案
     * 
     * @param fileName
     *            完整檔案路徑
     * @return
     */
    public static byte[] loadFromFile(File file) {
        byte[] ret = null;
        try {
            ret = new byte[(int) file.length()];
            FileInputStream in = new FileInputStream(file);
            in.read(ret);
            in.close();
        } catch (Throwable e) {
            ret = null;
        }
        return ret;
    }

    /**
     * 讀取檔案
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] loadFileToByte(File file) throws IOException {
        byte[] arrayOfByte = new byte[4096];
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
            baos.write(arrayOfByte, 0, i);
        }
        baos.close();
        input.close();
        return baos.toByteArray();
    }

    /**
     * 存檔案
     * 
     * @param fileName
     *            完整檔案路徑
     * @param data
     *            若為字串 .getBytes() 轉為byte[]
     */
    public static void saveToFile(String fileName, byte[] data) {
        saveToFile(new File(fileName), data);
    }

    /**
     * 存檔案
     * 
     * @param fileName
     *            完整檔案路徑
     * @param data
     *            若為字串 .getBytes() 轉為byte[]
     */
    public static void saveToFile(File file, byte[] data) {
        try {
            // 廢棄
            // FileOutputStream out = new FileOutputStream(file);
            // out.write(data);
            // out.close();
            BufferedInputStream buffIn = new BufferedInputStream(new ByteArrayInputStream(data));
            BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(file));
            byte[] arr = new byte[1024 * 1024];
            int available = -1;
            while ((available = buffIn.read(arr)) > 0) {
                buffOut.write(arr, 0, available);
            }
            buffOut.flush();
            buffOut.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 讀取檔案
     * 
     * @param file
     * @param encode
     * @return
     */
    public static String loadFromFile(File file, String encode) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public static List<String> loadFromFile_asList(File file, String encode) {
        BufferedReader reader = null;
        try {
            List<String> lst = new ArrayList<String>();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encode));
            String line = null;
            while ((line = reader.readLine()) != null) {
                lst.add(line);
            }
            return lst;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public static Map<Integer, String> loadFromFile_asMap(File file, String encode) {
        LineNumberReader reader = null;
        try {
            Map<Integer, String> map = new LinkedHashMap<Integer, String>();
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), encode));
            String line = null;
            while ((line = reader.readLine()) != null) {
                map.put(reader.getLineNumber(), line);
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 讀取串流
     * 
     * @param file
     * @param encode
     * @return
     */
    public static String loadFromStream(InputStream is, String encode) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, encode));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException("loadFromStream ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 儲存檔案
     * 
     * @param file
     * @param content
     * @param encode
     */
    public static void saveToFile(File file, String content, String encode) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encode));
            writer.write(content);
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException("saveToFile ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                writer.close();
            } catch (Exception ex1) {
            }
        }
    }

    /**
     * 讀檔成逐行成ArrayList
     */
    public static List<String> readFileToList(File fileName, String codeing) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), codeing));
            String data = null;
            do {
                data = reader.readLine();
                if (data == null)
                    break;
                else {
                    list.add(data);
                }
            } while (true);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return list;
    }

    public static long getFreespace(File copyTo) {
        long freespace = 0;
        for (File root : File.listRoots()) {
            if (copyTo.getAbsolutePath().contains(root.getAbsolutePath())) {
                freespace = root.getFreeSpace();
                break;
            }
        }
        return freespace;
    }

    public static boolean copyDirectory(File copyFromDir, File copyToDir) {
        if (copyFromDir.isDirectory() && copyToDir.isDirectory()) {
            boolean isAllOk = true;
            List<File> fileLst = new ArrayList<File>();
            FileUtil.searchFilefind(copyFromDir, ".*", fileLst);

            FileMoveSpeedCalculator mFileMoveSpeedCalculator = new FileMoveSpeedCalculator(null, fileLst);
            mFileMoveSpeedCalculator.start();

            File copyToDirStarter = new File(copyToDir, copyFromDir.getName());
            if (copyToDirStarter.exists()) {
                System.out.println("檔案複製錯誤 : 複製目的目錄存在 : " + copyToDirStarter + "");
                return false;
            }

            for (File copyFromFile : fileLst) {
                if (copyFromFile.isDirectory()) {
                    File destFile = exportFileToTargetPath(copyFromFile, copyFromDir, copyToDirStarter);
                    destFile.mkdirs();
                }
            }

            String replaceBasePath = copyFromDir.getAbsolutePath();
            String targetBasepath = copyToDirStarter.getAbsolutePath();
            for (File copyFromFile : fileLst) {
                if (copyFromFile.isFile()) {
                    String filePath = copyFromFile.getAbsolutePath();
                    try {
                        File toFile = exportFileToTarget(filePath, replaceBasePath, targetBasepath);
                        if (copyFromFile.length() != toFile.length()) {
                            System.out.println("檔案複製大小不一致 : " + toFile);
                            isAllOk = false;
                        }
                    } catch (IOException e) {
                        System.out.println("檔案複製錯誤 : " + e.getMessage());
                        isAllOk = false;
                    }
                }
            }

            mFileMoveSpeedCalculator.end("copyDirectory");
            return isAllOk;
        } else {
            return false;
        }
    }

    public static boolean moveDirectory(File copyFromDir, File copyToDir) {
        boolean result = copyDirectory(copyFromDir, copyToDir);
        if (result) {
            return FileDeleteHelper.deleteQuietly(copyFromDir);
        }
        return false;
    }

    /**
     * 檔案複製
     * 
     * @param copyFrom
     * @param copyTo
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File copyFrom, File copyTo) {
        try {
            long fromSize = copyFrom.length();
            long freespace = getFreespace(copyTo);
            if (fromSize >= freespace) {
                throw new RuntimeException("空間不足 , src : " + fromSize + " , dst freespace : " + freespace);
            }
            // if (!isLockingSupported()) {
            // return copyFileNormal(copyFrom, copyTo);
            // } else {
            // return copyFileLocking(copyFrom, copyTo);
            // }
            FileMoveSpeedCalculator mFileMoveSpeedCalculator = new FileMoveSpeedCalculator(copyFrom, null);
            mFileMoveSpeedCalculator.start();
            doCopyFile(copyFrom, copyTo, true);
            mFileMoveSpeedCalculator.end("copyFile");
            if (copyTo.exists()) {
                if (copyTo.length() == copyFrom.length()) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException("copyFile ERR : " + ex.getMessage(), ex);
        }
    }

    public static boolean moveFile(File copyFrom, File copyTo) {
        boolean result = copyFile(copyFrom, copyTo);
        if (result) {
            if (copyFrom.length() == copyTo.length()) {
                return FileDeleteHelper.deleteQuietly(copyFrom);
            }
        }
        return false;
    }

    private static class FileMoveSpeedCalculator {
        List<File> fileLst;
        long startTime = -1;
        long endTime = -1;
        long totalSize = 0;
        String totalSizeDesc;

        private FileMoveSpeedCalculator(File fromFile, List<File> fromFileLst) {
            this.fileLst = fromFileLst;
            if (fromFileLst == null) {
                List<File> fileLst = new ArrayList<File>();
                if (fromFile != null) {
                    if (fromFile.isDirectory()) {
                        FileUtil.searchFilefind(fromFile, ".*", fileLst);
                    } else {
                        fileLst.add(fromFile);
                    }
                }
                this.fileLst = fileLst;
            }
            for (File f : this.fileLst) {
                totalSize += f.length();
            }
            totalSizeDesc = FileUtil.getSizeDescription(totalSize);
        }

        private void start() {
            startTime = System.currentTimeMillis();
        }

        private void end(String label) {
            endTime = System.currentTimeMillis();
            String duringDesc = DateUtil.wasteTotalTime(endTime - startTime);
            System.out.println("搬運結束 :[" + label + "] 檔案數 : " + fileLst.size() + " , 總大小 : " + totalSizeDesc + " , 耗時 : " + duringDesc + " , 均速 : " + getResult());
        }

        private String getResult() {
            try {
                long duringTime = endTime - startTime;
                long duringSecond = (duringTime / 1000);
                BigDecimal result = new BigDecimal(totalSize).divide(new BigDecimal(duringSecond), 3, BigDecimal.ROUND_HALF_UP);
                String desc = FileUtil.getSizeDescription(result.longValue());
                return desc;
            } catch (Exception ex) {
                return "NA";
            }
        }
    }

    /**
     * 是否支援lock
     * 
     * @return
     */
    public static boolean isLockingSupported() {
        boolean bool1 = System.getProperty("java.specification.version").compareTo("1.4") >= 0;
        boolean bool2 = (!(System.getProperty("os.name").toLowerCase().equals("hp-ux"))) || (!(System.getProperty("os.arch").toLowerCase().startsWith("ia64")));
        return bool1 && bool2;
    }

    /**
     * 一般的copy
     * 
     * @param copyFrom
     * @param copyTo
     * @return
     * @throws IOException
     */
    public static boolean copyFileNormal(File copyFrom, File copyTo) {
        BufferedInputStream input = null;
        BufferedOutputStream output = null;
        try {
            byte[] arrayOfByte = new byte[4096];
            input = new BufferedInputStream(new FileInputStream(copyFrom));
            output = new BufferedOutputStream(new FileOutputStream(copyTo, false));
            int i;
            while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
                output.write(arrayOfByte, 0, i);
            }
            output.close();
            input.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
            try {
                output.close();
            } catch (Exception e) {
            }
        }
        if (!copyFrom.exists() || copyTo == null) {
            return false;
        }
        return true;
    }

    /**
     * 複製檔案 檢查lock
     * 
     * @param copyFrom
     * @param copyTo
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean copyFileLocking(File copyFrom, File copyTo) throws FileNotFoundException, IOException {
        if ((!(copyFrom.exists())) || (copyTo == null)) {
            return false;
        }
        FileOutputStream fileOutput = null;
        try {
            byte[] arrayOfByte = new byte[4096];
            fileOutput = new FileOutputStream(copyTo, true);
            FileChannel channel = fileOutput.getChannel();
            FileLock fileLock = channel.tryLock();
            if (fileLock != null) {
                channel.truncate(0L);
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(copyFrom));
                BufferedOutputStream output = new BufferedOutputStream(fileOutput);
                try {
                    int i;
                    while ((i = input.read(arrayOfByte, 0, arrayOfByte.length)) != -1) {
                        output.write(arrayOfByte, 0, i);
                    }
                    output.flush();
                    fileLock.release();
                } catch (Exception ex) {
                    throw new RuntimeException("copyFileLocking ERR : " + ex.getMessage(), ex);
                } finally {
                    output.close();
                    input.close();
                }
                return true;
            }
        } catch (Exception ex) {
            throw new RuntimeException("copyFileLocking ERR : " + ex.getMessage(), ex);
        } finally {
            fileOutput.close();
        }
        throw new IOException("Unable to acquire file lock for " + copyTo);
    }

    private static void doCopyFile(final File srcFile, final File destFile, final boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        final long ONE_KB = 1024;// 1kb
        final long ONE_MB = ONE_KB * ONE_KB; // 1mb
        final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;// 30mb

        FileInputStream fis = null;
        FileChannel input = null;
        FileOutputStream fos = null;
        FileChannel output = null;

        try {
            fis = new FileInputStream(srcFile);
            input = fis.getChannel();
            fos = new FileOutputStream(destFile);
            output = fos.getChannel();

            final long size = input.size(); // TODO See IO-386
            long pos = 0;
            long count = 0;
            while (pos < size) {
                final long remain = size - pos;
                count = remain > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : remain;
                final long bytesCopied = output.transferFrom(input, pos, count);
                if (bytesCopied == 0) { // IO-385 - can happen if file is
                                        // truncated after caching the size
                    break; // ensure we don't loop forever
                }
                pos += bytesCopied;
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            fis.close();
            fos.close();
        }

        final long srcLen = srcFile.length(); // TODO See IO-386
        final long dstLen = destFile.length(); // TODO See IO-386
        if (srcLen != dstLen) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "' Expected length: " + srcLen + " Actual: " + dstLen);
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    /**
     * 刪除檔案
     * 
     * @param fileName
     *            檔案路徑
     */
    public static void deleteFile(String fileName) {
        File f = new File(fileName);
        if (!f.exists())
            throw new IllegalArgumentException("Delete: no such file or directory: " + fileName);
        if (!f.canWrite())
            throw new IllegalArgumentException("Delete: write protected: " + fileName);
        if (f.isDirectory()) {
            String[] files = f.list();
            if (files.length > 0)
                throw new IllegalArgumentException("Delete: directory not empty: " + fileName);
        }
        boolean success = f.delete();
        if (!success)
            throw new IllegalArgumentException("Delete: deletion failed");
    }

    /**
     * 檔案搬移 (可換檔名或資料夾名)
     * 
     * @param soucepath
     *            來源檔路徑名
     * @param despath
     *            目的檔路徑名
     * @return
     */
    public static boolean fileMove(String soucepath, String despath) {
        File f = new File(soucepath);
        File des = new File(despath);
        return f.renameTo(des);
    }

    /**
     * 傳回目錄底下所有檔案 File的 List物件
     * 
     * @param fileName
     *            目錄路徑
     * @param fileList
     * @return
     */
    public static void traceFileList(File file, List<File> fileList) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                traceFileList(f, fileList);
            }
        } else {
            fileList.add(file);
        }
    }

    /**
     * 根據filePath 建立一個目錄結構類似的檔案目錄於新的目錄
     * targetBasepath，filePath前面的路徑replaceBasePath將會被targetBasepath給取代
     * 
     * @param filePath
     *            來源檔案或目錄路徑(可為檔案或目錄)
     * @param replaceBasePath
     *            來源檔案根目錄
     * @param targetBasepath
     *            新建立目錄結構的根目錄
     * @return
     * @throws IOException
     */
    public static File exportFileToTarget(String filePath, String replaceBasePath, String targetBasepath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("檔案路徑不存在 : " + filePath);
        }
        String path = null;
        boolean isfile = false;
        if (file.isDirectory()) {
            path = file.getAbsolutePath();
        } else {
            path = file.getParent();
            isfile = true;
        }
        int cut = new File(replaceBasePath).getAbsolutePath().length();
        String makeDirPath = targetBasepath + File.separator + path.substring(cut);
        String makeNewFile = makeDirPath + File.separator + file.getName();
        File makeDirFile = new File(makeDirPath);
        if (!makeDirFile.exists()) {
            makeDirFile.mkdirs();
        }
        File fromFile = new File(filePath);
        File toFile = new File(makeNewFile);
        if (isfile) {
            if (!copyFile(fromFile, toFile)) {
                throw new RuntimeException("檔案複製失敗 : " + makeNewFile);
            }
        }
        return toFile;
    }

    /**
     * 根據filePath 建立一個目錄結構類似的檔案目錄於新的目錄
     * targetBasepath，filePath前面的路徑replaceBasePath將會被targetBasepath給取代
     * 
     * @param srcFile
     *            來源檔案或目錄路徑(可為檔案或目錄)
     * @param srcBaseDir
     *            來源檔案根目錄
     * @param targetBaseDir
     *            新建立目錄結構的根目錄
     * @return
     * @throws IOException
     */
    public static File exportFileToTargetPath(File srcFile, File srcBaseDir, File targetBaseDir) {
        if (!srcFile.exists()) {
            throw new RuntimeException("檔案路徑不存在 : " + srcFile);
        }
        String path = null;
        if (srcFile.isDirectory()) {
            path = srcFile.getAbsolutePath();
        } else {
            path = srcFile.getParent();
        }
        int cut = srcBaseDir.getAbsolutePath().length();
        String makeDirPath = targetBaseDir + File.separator + path.substring(cut);
        String makeNewFile = makeDirPath + File.separator + srcFile.getName();
        return new File(makeNewFile);
    }

    /**
     * 從list中取得其共同的parent dir
     * 
     * @param list
     * @return
     */
    public static File exportReceiveBaseDir(List<File> list) {
        File commonDir = null;
        File dir = null;
        for (File file : list) {
            if (file.isFile()) {
                dir = file.getParentFile();
            } else {
                dir = file;
            }
            if (commonDir == null) {
                commonDir = dir;
            }
            for (; !commonDir.equals(dir);) {
                if (commonDir == null || dir == null) {
                    return null;
                }
                int tmplen = commonDir.getAbsolutePath().length();
                int dirlen = dir.getAbsolutePath().length();
                if (tmplen > dirlen) {
                    commonDir = commonDir.getParentFile();
                } else {
                    dir = dir.getParentFile();
                }
            }
        }
        return commonDir;
    }

    /**
     * 取得此檔案個跟目錄
     * 
     * @param file
     * @return
     */
    public static File getRoot(File file) {
        File tmp = file.getParentFile();
        File rtn = null;
        for (; tmp != null; rtn = tmp, tmp = tmp.getParentFile())
            ;
        return rtn;
    }

    /**
     * 搜尋目標符合pattern的檔案(matches)
     * 
     * @param file
     *            搜尋的目錄
     * @param pattern
     *            regex pattern
     * @param fileList
     *            找到符合的檔案
     */
    public static void searchFileContains(File file, String containsText, boolean ignoreCase, List<File> fileList) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                searchFileContains(f, containsText, ignoreCase, fileList);
            }
        } else {
            if (!ignoreCase && file.getName().contains(containsText)) {
                if (!fileList.contains(file)) {
                    fileList.add(file);
                }
            } else if (ignoreCase && file.getName().toLowerCase().contains(containsText.toLowerCase())) {
                if (!fileList.contains(file)) {
                    fileList.add(file);
                }
            }
        }
    }

    /**
     * 搜尋目標符合pattern的檔案(matches)
     * 
     * @param file
     *            搜尋的目錄
     * @param pattern
     *            regex pattern
     * @param fileList
     *            找到符合的檔案
     */
    public static void searchFileMatchs(File file, String pattern, List<File> fileList) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                searchFileMatchs(f, pattern, fileList);
            }
        } else {
            if (file.getName().matches(pattern)) {
                if (!fileList.contains(file)) {
                    fileList.add(file);
                }
            }
        }
    }

    /**
     * 搜尋目標符合pattern的檔案(find)
     * 
     * @param file
     *            搜尋的目錄
     * @param pattern
     *            regex pattern
     * @param fileList
     *            找到符合的檔案
     */
    public static void searchFilefind(File file, String pattern, List<File> fileList) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory() && file.listFiles() != null) {
            for (File f : file.listFiles()) {
                searchFilefind(f, pattern, fileList);
            }
        } else {
            if (Pattern.compile(pattern).matcher(file.getName()).find()) {
                if (!fileList.contains(file)) {
                    fileList.add(file);
                }
            }
        }
    }

    /**
     * 將檔案清單前置路徑截掉
     * 
     * @param cutPath
     *            截掉的路徑
     * @param fileList
     *            要截掉的清單
     * @return
     */
    public static List<String> cutRootPath(File cutPath, List<File> fileList) {
        List<String> list = new ArrayList<String>();
        String cPath = cutPath.getAbsolutePath();
        int cPathLen = cPath.length();
        String afterPath = null;
        for (File file : fileList) {
            if (file.getAbsolutePath().startsWith(cPath)) {
                afterPath = file.getAbsolutePath().substring(cPathLen);
                // System.out.println(afterPath);
                list.add(afterPath);
            } else {
                throw new RuntimeException("前置路徑不正確 : " + file.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * 建立parent目錄結構
     * 
     * @param file
     */
    public static void createParentFolder(File srcFile, File targetFile) {
        if (!srcFile.exists()) {
            throw new RuntimeException("檔案不存在: " + srcFile.getAbsolutePath());
        }
        if (!srcFile.getName().equals(targetFile.getName())) {
            throw new RuntimeException("檔名不同: (來源)" + srcFile.getName() + "\t(目的)" + targetFile.getName());
        }
        File createDir = null;
        if (srcFile.isFile()) {
            createDir = targetFile.getParentFile();
        } else if (srcFile.isDirectory()) {
            System.out.println("建立目錄:" + targetFile.getAbsolutePath());
            createDir = targetFile;
        }
        if (!createDir.exists()) {
            createDir.mkdirs();
        }
    }

    /**
     * 是否為有效黨名
     * 
     * @param paramString
     * @return
     */
    public static boolean isValidFileName(String paramString) {
        boolean rtn = false;
        if (paramString != null) {
            File localFile = new File(paramString);
            try {
                if (localFile.getCanonicalPath() != null) {
                    rtn = true;
                }
            } catch (IOException ex) {
            }
        }
        return rtn;
    }

    /**
     * 檔名轉URL
     * 
     * @param file
     * @return
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    public static URL fileToURL(File file) throws UnsupportedEncodingException, MalformedURLException {
        String path = file.getAbsolutePath();
        path = URLEncoder.encode(path, "UTF8");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/") && file.isDirectory()) {
            path = path + "/";
        }
        URL url = new URL("file", "", path);
        return url;
    }

    /**
     * 取得指定副檔名的檔案
     * 
     * @return
     */
    public static File getIndicateFileExtension(File file, String extension) {
        int pos = -1;
        String name = file.getName();
        if ((pos = name.lastIndexOf(".")) != -1) {
            name = name.substring(0, pos);
        }
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        return new File(file.getParent(), name + extension);
    }

    /**
     * 取得預設類別參數檔案
     * 
     * @param clz
     * @param createDir
     * @return
     */
    public static File getDefaultExportDir(Class<?> clz, boolean createDir) {
        File dir = new File(FileUtil.DESKTOP_PATH + //
                "export_" + clz.getSimpleName() + "_" + //
                DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd_HHmmss"));
        if (!dir.exists() && createDir) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getFileInDefaultDir(String filename) {
        return getFileInDefaultDir(filename, false);
    }

    public static File getFileInDefaultDir(String filename, boolean findDesktopSubDir) {
        File current = null;
        for (File d : File.listRoots()) {
            current = new File(d, filename);
            if (current.exists()) {
                System.out.println("getFile : " + current);
                return current;
            }
        }
        String[] dir = new String[] { DESKTOP_PATH, System.getProperty("user.home") };
        for (String dstr : dir) {
            current = new File(dstr, filename);
            if (current.exists()) {
                System.out.println("getFile : " + current);
                return current;
            }
        }
        if (findDesktopSubDir) {
            List<File> list = new ArrayList<File>();
            searchFileMatchs(DESKTOP_DIR, filename, list);
            if (!list.isEmpty()) {
                System.out.println("getFile : " + list.get(0));
                return list.get(0);
            }
        }
        throw new Error(filename + " not found!");
    }

    /**
     * 刪除目錄底下所有空目錄
     * 
     * @param file
     */
    public static void deleteEmptyDir(File file) {
        if (!file.exists() || file.isFile()) {
            return;
        }
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
            } else {
                for (File f : file.listFiles()) {
                    deleteEmptyDir(f);
                }
                if (file.list().length == 0) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 刪除目錄底下所有空目錄 , 並告訴你刪了那些
     * 
     * @param file
     * @param deleteList
     */
    public static void deleteEmptyDir(File file, List<File> deleteList) {
        if (!file.exists() || file.isFile()) {
            return;
        }
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                deleteList.add(file);
                file.delete();
            } else {
                for (File f : file.listFiles()) {
                    deleteEmptyDir(f, deleteList);
                }
                if (file.list().length == 0) {
                    deleteList.add(file);
                    file.delete();
                }
            }
        }
    }

    /**
     * 儲存錯誤於桌面
     * 
     * @param ex
     * @param name
     */
    public static void handleException(Throwable ex, String name) {
        try {
            PrintWriter pw = new PrintWriter(new File(FileUtil.DESKTOP_PATH, name + ".log"));
            ex.printStackTrace(pw);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static File getNewSubName(File orginFile, String subName) {
        String name = orginFile.getName();
        subName = StringUtils.trimToEmpty(subName);
        Validate.notBlank(subName, "副檔名不可為空");
        if (!subName.startsWith(".")) {
            subName = "." + subName;
        }
        return new File(orginFile.getParentFile(), getNameNoSubName(name) + subName);
    }

    /**
     * 取得副檔名 不含"."
     * 
     * @param file
     * @return
     */
    public static String getSubName(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (file.isFile() && pos != -1) {
            return name.substring(pos + 1);
        }
        return "";
    }

    /**
     * 取得副檔名 不含"."
     * 
     * @param file
     * @return
     */
    public static String getSubName(String filepath) {
        String name = filepath;
        int pos = name.lastIndexOf(".");
        if (pos != -1) {
            name = name.substring(pos + 1);
            Pattern ptn = Pattern.compile("\\w+");
            Matcher mth = ptn.matcher(name);
            if (mth.find()) {
                return mth.group();
            }
        }
        return "";
    }

    /**
     * 取得檔名不含副檔名 速度慢(如果檔案不存在更慢)
     * 
     * @param file
     * @return
     */
    public static String getNameNoSubName(File file) {
        return getNameNoSubName(file.getName());
    }

    /**
     * 取得檔名不含副檔名 速度慢(如果檔案不存在更慢)
     * 
     * @param file
     * @return
     */
    public static String getNameNoSubName(String filename) {
        int pos = filename.lastIndexOf(".");
        if (pos != -1) {
            return filename.substring(0, pos);
        }
        return filename;
    }

    /**
     * 取得檔案大小長度
     * 
     * @param size
     * @param sizeType
     * @return
     */
    public static long getSizeLength(double size, String sizeType) {
        BigDecimal rtnSize = new BigDecimal(size);
        String[] suffixS = new String[] { "kb", "mb", "gb", "tb" };
        for (int ii = 0; ii < suffixS.length; ii++) {
            rtnSize = rtnSize.multiply(new BigDecimal(1024));
            if (StringUtils.equalsIgnoreCase(sizeType.toLowerCase(), suffixS[ii])) {
                break;
            }
        }
        return rtnSize.longValue();
    }

    /**
     * 取得檔案的大小敘述 Ex : 100kb
     * 
     * @param filelength
     * @return
     */
    public static String getSizeDescription(long filelength) {
        long size = filelength;
        String suffix = null;
        String[] suffixS = new String[] { "kb", "mb", "gb" };
        BigDecimal result = null;
        for (int ii = 0; ii < suffixS.length && size > 1024; ii++) {
            if (size / 1024 < 1024) {
                result = BigDecimal.valueOf(size).divide(new BigDecimal(1024d), 2, RoundingMode.HALF_UP);
                // result = result.setScale(2, RoundingMode.HALF_UP);
            }
            size = size / 1024;
            suffix = suffixS[ii];
        }
        if (suffix == null) {
            suffix = "byte";
        }
        String sizeStr = String.valueOf(size);
        if (result != null) {
            // System.out.println(size + " / " + result.toString());
            sizeStr = result.toString();
        }
        return sizeStr + suffix;
    }

    public static Pair<String, String> getSizeDescriptionOrign(long filelength) {
        long size = filelength;
        String suffix = null;
        String[] suffixS = new String[] { "kb", "mb", "gb" };
        BigDecimal result = null;
        for (int ii = 0; ii < suffixS.length && size > 1024; ii++) {
            if (size / 1024 < 1024) {
                result = BigDecimal.valueOf(size).divide(new BigDecimal(1024d), 2, RoundingMode.HALF_UP);
                // result = result.setScale(2, RoundingMode.HALF_UP);
            }
            size = size / 1024;
            suffix = suffixS[ii];
        }
        if (suffix == null) {
            suffix = "byte";
        }
        String sizeStr = String.valueOf(size);
        if (result != null) {
            // System.out.println(size + " / " + result.toString());
            sizeStr = result.toString();
        }
        return Pair.of(sizeStr, suffix);
    }

    public static int getSizeDescriptionCompare(String left, String right) {
        Pattern ptn = Pattern.compile("(\\-?[\\d\\.]+)(byte|kb|mb|gb)");
        Matcher mth = ptn.matcher(StringUtils.trimToEmpty(left));
        mth.find();
        Matcher mth2 = ptn.matcher(StringUtils.trimToEmpty(right));
        mth2.find();
        return getSizeDescriptionCompare(//
                Pair.of(mth.group(1), mth.group(2)), //
                Pair.of(mth2.group(1), mth2.group(2)));
    }

    public static int getSizeDescriptionCompare(Pair<String, String> left, Pair<String, String> right) {
        String[] unitRank = new String[] { "byte", "kb", "mb", "gb" };
        int leftRank = ArrayUtils.indexOf(unitRank, left.getRight());
        int rightRank = ArrayUtils.indexOf(unitRank, right.getRight());
        int unitCompare = new Integer(leftRank).compareTo(rightRank);
        if (unitCompare != 0) {
            return unitCompare;
        } else {
            int result = new Double(Double.parseDouble(left.getLeft()))//
                    .compareTo(Double.parseDouble(right.getLeft()));
            return result;
        }
    }

    /**
     * 開啟權限
     */
    public static void openPermission(File savefile) {
        savefile.setExecutable(true, false);
        savefile.setWritable(true, false);
        savefile.setReadable(true, false);
    }

    /**
     * 從class目錄底下取得檔案成字串
     * 
     * @param clz
     *            clz所在位置
     * @param fileName
     *            檔名Ex : xxx.txt(不用路徑)
     * @return
     */
    public static String getFileFromClass(Class<?> clz, String fileName) {
        String sql = null;
        InputStream stream = null;
        try {
            stream = clz.getResourceAsStream(fileName);
            sql = org.apache.commons.io.IOUtils.toString(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                    stream = null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sql;
    }

    /**
     * 從url建立一個檔案
     * 
     * @param url
     * @param tempFileName
     * @return
     * @throws IOException
     */
    public static File createFileFromURL(URL url, String tempFileName) throws IOException {
        String tDir = System.getProperty("java.io.tmpdir");
        String path = tDir + File.separator + tempFileName;
        File file = new File(path);
        // file.deleteOnExit();
        FileUtils.copyURLToFile(url, file);
        return file;
    }

    /**
     * 移除掉特殊字元 解法式把路徑貼到 *.properties 會顯示無法正常顯示的特殊字元
     */
    public static String replaceSpecialChar(String path) {
        return path.replace("\u202A", "");
    }

    public static long getCreateTime(File file) {
        Path path = file.toPath();
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            System.out.println("Exception handled when trying to get file " + "attributes: " + e.getMessage());
        }
        if (attr != null) {
            // System.out.println("creationTime: " + attr.creationTime());
            // System.out.println("lastAccessTime: " + attr.lastAccessTime());
            // System.out.println("lastModifiedTime: " +
            // attr.lastModifiedTime());
            return attr.creationTime().toMillis();
        } else {
            return -1;
        }
    }

    /**
     * 避掉黨名的特殊符號 \/:*?"<>|
     */
    public static String escapeFilename(String filename, boolean escapeFileSeparator) {
        String replaceSeparator = "\\\\\\/";
        if (!escapeFileSeparator) {
            replaceSeparator = "";
        }
        Pattern ptn = Pattern.compile("[" + replaceSeparator + "\\:\\*\\?\"\\<\\>\\|\r\n]", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(filename);
        StringBuffer sb = new StringBuffer();
        while (mth.find()) {
            mth.appendReplacement(sb, "-");
        }
        mth.appendTail(sb);
        String fileName = sb.toString();
        fileName = fileName.replaceAll("[\r\n]", "");
        fileName = fileName.replaceAll("  ", " ");
        fileName = fileName.replaceAll("　", "");
        fileName = fileName.trim();
        return fileName;
    }

    public static boolean validatePath(String filename, boolean ignoreNotEscapeFileSepator) {
        String replaceSeparator = "\\\\\\/";
        if (ignoreNotEscapeFileSepator) {
            replaceSeparator = "";
        }
        Pattern ptn = Pattern.compile("[" + replaceSeparator + "\\:\\*\\?\"\\<\\>\\|\r\n]", Pattern.DOTALL | Pattern.MULTILINE);
        Matcher mth = ptn.matcher(filename);
        if (mth.find()) {
            return false;
        }
        return true;
    }

    /**
     * 避掉黨名的特殊符號 \/:*?"<>|
     * 
     * @param filename
     * @param ignoreNotEscapeFileSepator
     *            false = 要把 \/轉成全形, true = 不把 \/轉成全形
     * @return
     */
    public static String escapeFilename_replaceToFullChar(String filename, boolean ignoreNotEscapeFileSepator) {
        StringBuffer sb = new StringBuffer();
        char[] arry = StringUtils.trimToEmpty(filename).toCharArray();
        Character cc = null;
        for (char c : arry) {
            switch (c) {
            case ':':
                cc = '：';
                break;//
            case '*':
                cc = '＊';
                break;//
            case '?':
                cc = '？';
                break;//
            case '"':
                cc = '＂';
                break;//
            case '<':
                cc = '＜';
                break;//
            case '>':
                cc = '＞';
                break;//
            case '|':
                cc = '｜';
                break;//
            case '\\':
                cc = '＼';
                break;//
            case '/':
                cc = '／';
                break;//
            default:
                cc = c;
                break;
            }

            if (c == '\r' || c == '\n') {
                continue;
            }
            if (ignoreNotEscapeFileSepator) {
                if (c == '/' || c == '\\') {
                    sb.append(c);
                    continue;
                }
            }

            sb.append(cc);
        }
        return sb.toString();
    }

    public static boolean contentEquals(File file1, File file2) {
        try {
            return FileUtils.contentEquals(file1, file2);
        } catch (Exception e) {
            throw new RuntimeException("contentEquals ERR : " + e.getMessage(), e);
        }
    }

    public static boolean createNewFile(File file) {
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e1) {
                System.out.println("createNewFile ERR : " + e1.getMessage());
                return false;
            }
        } else {
            System.out.println("取消建立新檔, 檔案已存在 : " + file);
            return false;
        }
    }

    public static boolean mkdirs(File file) {
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            System.out.println("取消建立目錄, 路徑已存在 : " + file);
            return false;
        }
    }

    public static String fixPath(String filePathName, boolean isUnixSeparator) {
        return FilenameUtils.normalize(filePathName, isUnixSeparator);
    }

    public static String getFileRoot(File file) {
        if (!OS_IS_WINDOWS) {
            return null;
        }
        for (File f : File.listRoots()) {
            if (file.getAbsolutePath().contains(f.getAbsolutePath())) {
                return f.getAbsolutePath().replaceAll("\\" + File.separator + "+$", "");
            }
        }
        throw new RuntimeException("找不到根目錄：" + file);
    }

    public static File getParentFolder(File file) {
        File tmpFile = file;
        do {
            if (tmpFile.isFile() || !tmpFile.exists()) {
                tmpFile = file.getParentFile();
            }
        } while (!(tmpFile != null && tmpFile.isDirectory()));
        return tmpFile;
    }

    public static int countLines(String filename) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(filename));
            byte[] c = new byte[1024];
            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }
            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i = 0; i < 1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }
            // count remaining characters
            while (readChars != -1) {
                System.out.println(readChars);
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }
            return count == 0 ? 1 : count;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
    }

    public void writeBOM(FileOutputStream writer) throws IOException {
        writer.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
    }

    public static File createTempDir(String prefix, String suffix) {
        File tmpdir = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
        if (prefix.length() < 3)
            throw new IllegalArgumentException("Prefix string too short");
        if (StringUtils.isBlank(suffix))
            suffix = "_Dir";
        String dirName = prefix + "_" + System.currentTimeMillis() + "_" + suffix;
        File dir = new File(tmpdir, dirName);
        if (!dir.mkdirs()) {
            throw new RuntimeException("建立暫時目錄失敗 :" + dir);
        }
        return dir;
    }

    private static class FileDeleteHelper {
        public static boolean deleteQuietly(final File file) {
            if (file == null) {
                return false;
            }
            try {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                }
            } catch (final Exception ignored) {
            }

            try {
                return file.delete();
            } catch (final Exception ignored) {
                return false;
            }
        }

        public static void cleanDirectory(final File directory) throws IOException {
            final File[] files = verifiedListFiles(directory);

            IOException exception = null;
            for (final File file : files) {
                try {
                    forceDelete(file);
                } catch (final IOException ioe) {
                    exception = ioe;
                }
            }

            if (null != exception) {
                throw exception;
            }
        }

        private static File[] verifiedListFiles(final File directory) throws IOException {
            if (!directory.exists()) {
                final String message = directory + " does not exist";
                throw new IllegalArgumentException(message);
            }

            if (!directory.isDirectory()) {
                final String message = directory + " is not a directory";
                throw new IllegalArgumentException(message);
            }

            final File[] files = directory.listFiles();
            if (files == null) { // null if security restricted
                throw new IOException("Failed to list contents of " + directory);
            }
            return files;
        }

        public static void forceDelete(final File file) throws IOException {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                final boolean filePresent = file.exists();
                if (!file.delete()) {
                    if (!filePresent) {
                        throw new FileNotFoundException("File does not exist: " + file);
                    }
                    final String message = "Unable to delete file: " + file;
                    throw new IOException(message);
                }
            }
        }

        public static void deleteDirectory(final File directory) throws IOException {
            if (!directory.exists()) {
                return;
            }

            if (!isSymlink(directory)) {
                cleanDirectory(directory);
            }

            if (!directory.delete()) {
                final String message = "Unable to delete directory " + directory + ".";
                throw new IOException(message);
            }
        }

        public static boolean isSymlink(final File file) throws IOException {
            if (file == null) {
                throw new NullPointerException("File must not be null");
            }
            return Files.isSymbolicLink(file.toPath());
        }
    }

    public static class FileZ {
        File entity;

        private String name;
        private Long length;
        private String canonicalPath;
        private String parent;
        private Boolean absolute;
        private Boolean setReadOnly;
        private String[] list;
        private Boolean exts;
        private Boolean canExecute;
        private Boolean canRead;
        private Boolean canWrite;
        private File absoluteFile;
        private String absolutePath;
        private File canonicalFile;
        private Long freeSpace;
        private File parentFile;
        private String path;
        private Long totalSpace;
        private Long usableSpace;
        private Boolean directory;
        private Boolean file;
        private Boolean hidden;
        private Long lastModified;
        private URI toURI;
        private URL toURL;
        private static File[] listRoots;
        private File[] listFiles;

        public void cleanAll() {
            for (Field f : FileZ.class.getDeclaredFields()) {
                try {
                    f.setAccessible(true);
                    f.set(this, null);
                } catch (Exception ex) {
                }
            }
        }

        public FileZ(File entity) {
            this.entity = entity;
        }

        public String getName() {
            if (this.name == null) {
                this.name = entity.getName();
            }
            return this.name;
        }

        public long length() {
            if (this.length == null) {
                this.length = entity.length();
            }
            return this.length;
        }

        public String getCanonicalPath() throws IOException {
            if (this.canonicalPath == null) {
                this.canonicalPath = entity.getCanonicalPath();
            }
            return this.canonicalPath;
        }

        public String getParent() {
            if (this.parent == null) {
                this.parent = entity.getParent();
            }
            return this.parent;
        }

        public boolean isAbsolute() {
            if (this.absolute == null) {
                this.absolute = entity.isAbsolute();
            }
            return this.absolute;
        }

        public boolean setReadOnly() {
            if (this.setReadOnly == null) {
                this.setReadOnly = entity.setReadOnly();
            }
            return this.setReadOnly;
        }

        public String[] list() {
            if (this.list == null) {
                this.list = entity.list();
            }
            return this.list;
        }

        public boolean exists() {
            if (this.exts == null) {
                this.exts = entity.exists();
            }
            return this.exts;
        }

        public boolean canExecute() {
            if (this.canExecute == null) {
                this.canExecute = entity.canExecute();
            }
            return this.canExecute;
        }

        public boolean canRead() {
            if (this.canRead == null) {
                this.canRead = entity.canRead();
            }
            return this.canRead;
        }

        public boolean canWrite() {
            if (this.canWrite == null) {
                this.canWrite = entity.canWrite();
            }
            return this.canWrite;
        }

        public boolean createNewFile() throws IOException {
            return entity.createNewFile();
        }

        public File getAbsoluteFile() {
            if (this.absoluteFile == null) {
                this.absoluteFile = entity.getAbsoluteFile();
            }
            return this.absoluteFile;
        }

        public String getAbsolutePath() {
            if (this.absolutePath == null) {
                this.absolutePath = entity.getAbsolutePath();
            }
            return this.absolutePath;
        }

        public File getCanonicalFile() throws IOException {
            if (this.canonicalFile == null) {
                this.canonicalFile = entity.getCanonicalFile();
            }
            return this.canonicalFile;
        }

        public long getFreeSpace() {
            if (this.freeSpace == null) {
                this.freeSpace = entity.getFreeSpace();
            }
            return this.freeSpace;
        }

        public File getParentFile() {
            if (this.parentFile == null) {
                this.parentFile = entity.getParentFile();
            }
            return this.parentFile;
        }

        public String getPath() {
            if (this.path == null) {
                this.path = entity.getPath();
            }
            return this.path;
        }

        public long getTotalSpace() {
            if (this.totalSpace == null) {
                this.totalSpace = entity.getTotalSpace();
            }
            return this.totalSpace;
        }

        public long getUsableSpace() {
            if (this.usableSpace == null) {
                this.usableSpace = entity.getUsableSpace();
            }
            return this.usableSpace;
        }

        public boolean isDirectory() {
            if (this.directory == null) {
                this.directory = entity.isDirectory();
            }
            return this.directory;
        }

        public boolean isFile() {
            if (this.file == null) {
                this.file = entity.isFile();
            }
            return this.file;
        }

        public boolean isHidden() {
            if (this.hidden == null) {
                this.hidden = entity.isHidden();
            }
            return this.hidden;
        }

        public long lastModified() {
            if (this.lastModified == null) {
                this.lastModified = entity.lastModified();
            }
            return this.lastModified;
        }

        public File[] listFiles() {
            if (this.listFiles == null) {
                this.listFiles = entity.listFiles();
            }
            return this.listFiles;
        }

        public static File[] listRoots() {
            if (listRoots == null) {
                listRoots = File.listRoots();
            }
            return listRoots;
        }

        public boolean mkdir() {
            return entity.mkdir();
        }

        public boolean mkdirs() {
            return entity.mkdirs();
        }

        public URI toURI() {
            if (this.toURI == null) {
                this.toURI = entity.toURI();
            }
            return this.toURI;
        }

        public URL toURL() throws MalformedURLException {
            if (this.toURL == null) {
                this.toURL = entity.toURL();
            }
            return this.toURL;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((canonicalPath == null) ? 0 : canonicalPath.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FileZ other = (FileZ) obj;
            if (canonicalPath == null) {
                if (other.canonicalPath != null)
                    return false;
            } else if (!canonicalPath.equals(other.canonicalPath))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return entity.toString();
        }

        public File getFile() {
            return entity;
        }
    }
}
