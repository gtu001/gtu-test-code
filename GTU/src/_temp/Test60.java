package _temp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test60 {

    public static void main(String[] args) {
        Pattern ptn = Pattern.compile("\\_pct$");
        Matcher mth = ptn.matcher("Larry_pct");
        while (mth.find()) {
            System.out.println("FIND_OK");
        }
        System.out.println("done..");
    }

    private class LogFileChecker {
        TxtFileChecker log;

        int previousSize = -1;

        public void checkFiles() {
            if (previousSize == -1) {
                log.checkFiles();
                previousSize = log.posLst.size();
                return;
            } else {
                log.checkFiles();
                int currentSize = log.posLst.size();
                if (currentSize != previousSize) {
                    checkAddLines(previousSize, currentSize);
                }
            }
        }

        private void checkAddLines(int startLineNum, int endLineNum) {
            for (int ii = startLineNum; ii <= endLineNum; ii++) {
                String line = log.readLine(ii);
                System.out.println(line);
            }
        }
    }

    private class TxtFileChecker {
        List<Long> posLst;
        RandomAccessFile raf;
        String encoding;
        File file;

        public TxtFileChecker(File file, String encoding) {
            this.encoding = encoding;
            this.file = file;
            try {
                raf = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public void close() {
            try {
                raf.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public String readLine(int lineNumber) {
            if (posLst != null) {
                try {
                    long start = posLst.get(lineNumber - 1);
                    long end = -1;
                    if (posLst.size() > lineNumber) {
                        end = posLst.get(lineNumber);
                    } else {
                        end = file.length();
                    }
                    raf.seek(start);
                    byte[] bs = new byte[(int) (end - start + 1)];
                    raf.readFully(bs);
                    return new String(bs, encoding);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }

        public void checkFiles() {
            try {
                posLst = new ArrayList<Long>();
                String cur_line = "";
                while ((cur_line = raf.readLine()) != null) {
                    posLst.add(raf.getFilePointer());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
