package gtu.file.randomAccess;

import java.io.RandomAccessFile;

public abstract class RandomAccessFileReader {

    private RandomAccessFile sFile;
    private int lineNumber = 0;
    private long currentPos = 0;
    private String encoding;

    public RandomAccessFileReader(String path, String encoding) {
        try {
            this.sFile = new RandomAccessFile(path, "r");
            this.encoding = encoding;
            this.lineNumber = 0;
            this.currentPos = 0;
        } catch (Exception e) {
            throw new RuntimeException("init ERR : " + e.getMessage(), e);
        }
    }

    public abstract boolean readLine(String line, int lineNumber, long currentPos);

    public void start() {
        try {
            sFile.seek(0);
            while (currentPos < sFile.length()) {
                String line = sFile.readLine();
                line = new String(line.getBytes("ISO-8859-1"), encoding);
                currentPos = sFile.getFilePointer();
                lineNumber++;

                boolean isContinue = this.readLine(line, lineNumber, currentPos);
                if (!isContinue) {
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("start ERR : " + e.getMessage(), e);
        } finally {
            try {
                sFile.close();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        RandomAccessFileReader reader = new RandomAccessFileReader("C:/Users/wistronits/Desktop/test.txt", "UTF8") {
            @Override
            public boolean readLine(String line, int lineNumber, long currentPos) {
                System.out.println(lineNumber + " " + currentPos + " " + line);
                return true;
            }
        };
        reader.start();
    }
}