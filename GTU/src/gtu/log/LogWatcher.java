package gtu.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class LogWatcher {

    public static void main(String[] args) {
        File logFile = new File("c:/Users/wistronits/AppData/Local/Unity/Editor/Editor.log");
        // File logFile = new File("C:/Users/wistronits/Desktop/新文字文件.txt");
        final LogWatcher mTxtFileChecker = new LogWatcher(logFile, "UTF8") {
            @Override
            public void write(String line) {
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mTxtFileChecker.checkFiles();
            }
        }, 0, 1000);
        System.out.println("done..");
    }

    List<Long> posLst = new ArrayList<Long>();
    RandomAccessFile raf;
    String encoding;
    File file;
    int previousIndex = -1;
    long previousFileSize = -1;

    public LogWatcher(final File file, final String encoding) {
        this.encoding = encoding;
        this.file = file;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkFiles() {
        long currentFileSize = -1;
        if (previousFileSize == -1) {
            // System.out.println("checkFiles --- A");
            previousFileSize = file.length();
            processLineNumber();
            previousIndex = posLst.size();
            return;
        } else if ((currentFileSize = file.length()) != previousFileSize) {
            // System.out.println("checkFiles --- B");
            processLineNumber();
            int currentIndex = posLst.size();
            if (currentIndex != previousIndex) {
                checkAddLines(previousIndex, currentIndex);
                previousIndex = currentIndex;
                previousFileSize = currentFileSize;
            }
        }
    }

    public abstract void write(final String line);

    private void checkAddLines(final int startLineNum, final int endLineNum) {
        for (int ii = startLineNum; ii < endLineNum; ii++) {
            String line = readLine(ii);
//            System.out.print(line);
            write(line);
        }
    }

    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readLine(final int index) {
        if (posLst != null) {
            try {
                long start = 0;
                long end = posLst.get(index);
                if (index > 0) {
                    start = posLst.get(index - 1);
                }
                raf.seek(start);
                byte[] bs = new byte[(int) (end - start)];
                raf.readFully(bs);
                String rtnVal = new String(bs, encoding);
                // System.out.println("readLine " + start + " / " + end + "
                // == " + rtnVal);
                return rtnVal;
            } catch (Exception e) {
                throw new RuntimeException("readLine : " + index + " , Err:" + e.getMessage(), e);
            }
        }
        return null;
    }

    private void processLineNumber() {
        try {
            while ((raf.readLine()) != null) {
                posLst.add(raf.getFilePointer());
            }
            // System.out.println("checkFiles size " + posLst.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
