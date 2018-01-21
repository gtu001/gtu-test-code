package gtu.file.randomAccess;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileTest2 {

    public static void main(String[] args) throws IOException {
        File file = new File("C:/Users/gtu001/Desktop/New Text Document.txt");
        RandomAccessFile rLogFile = new RandomAccessFile(file, "rw");
        rLogFile.seek(rLogFile.length());
        rLogFile.writeBytes("xxxxxx1");
        rLogFile.close();
        System.out.println("done...");
    }
}
