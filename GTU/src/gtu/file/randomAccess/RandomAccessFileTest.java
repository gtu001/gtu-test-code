package gtu.file.randomAccess;

import gtu.class_.ClassPathUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest {

    public static void main(String[] args) throws IOException {
        String fileName = ClassPathUtil.getJavaFilePath(RandomAccessFileTest.class) + "test.txt";
        File file = new File(fileName);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        String tempRAFStr = null;
        while ((tempRAFStr = raf.readLine()) != null) {
            long currentPos = raf.getFilePointer(); // 取得目前讀取位置
            System.out.println("tempRAFStr: " + tempRAFStr + "\t pos = " + currentPos);
        }
        System.out.println("file eof..");

        raf.seek(0); // 重置讀取位置 (可設定從哪個字元開始)

        while ((tempRAFStr = raf.readLine()) != null) {
            long currentPos = raf.getFilePointer(); // 取得目前讀取位置
            System.out.println("tempRAFStr: " + tempRAFStr + "\t pos = " + currentPos);
            if (tempRAFStr.indexOf("3:cccccc") != -1) {
                raf.writeBytes("[test]");
            }
        }
        System.out.println("file eof..");

        raf.seek(raf.length());// 設定位置為檔案結尾
        raf.writeBytes("\nend...");
    }
}
