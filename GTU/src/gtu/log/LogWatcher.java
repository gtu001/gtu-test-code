package gtu.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public abstract class LogWatcher {

    public static void main(String[] args) {
        // File logFile = new
        // File("c:/Users/wistronits/AppData/Local/Unity/Editor/Editor.log");
        // File logFile = new File("C:/Users/wistronits/Desktop/新文字文件.txt");
        File logFile = new File("/home/gtu001/桌面/tttttt.txt");
        final LogWatcher mTxtFileChecker = new LogWatcher(logFile, "UTF8") {
            @Override
            public void write(String line) {
                System.out.println(line);
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

    Map<Integer, Long> posLst = new HashMap<Integer, Long>();
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
            raf.seek(file.length() - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getPosLstSize() {
        return posLst.size();
    }

    public void checkFiles() {
        long currentFileSize = -1;
        if (previousFileSize == -1) {
            // System.out.println("checkFiles --- A");
            previousFileSize = file.length();
            processLineNumber();
            previousIndex = getPosLstSize();
            return;
        } else if ((currentFileSize = file.length()) != previousFileSize) {
            // System.out.println("checkFiles --- B");
            processLineNumber();
            int currentIndex = getPosLstSize();
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
            // System.out.print(line);
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
                // System.out.println("readLine " + start + " / " + end + " == "
                // + rtnVal);
                return rtnVal;
            } catch (Exception e) {
                throw new RuntimeException("readLine : " + index + " , Err:" + e.getMessage(), e);
            }
        }
        return null;
    }

    private void processLineNumber() {
        try {
            int index = getPosLstSize();
            long startTime = System.currentTimeMillis();
            System.out.println("processLineNumber--------start　: " + index);
            while ((raf.readLine()) != null) {
                posLst.put(index, raf.getFilePointer());
                index++;
                if (index % 10000 == 0) {
                    System.out.println("\tprocessLineNumber--------step　: " + index);
                }
            }
            System.out.println("processLineNumber--------end　: " + index);
            System.out.println("processLineNumber during : " + ((System.currentTimeMillis() - startTime) / 1000) + "sec");
            // System.out.println("checkFiles size " + posLst.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
